/*     */ package javassist.util;
/*     */ 
/*     */ import com.sun.jdi.Bootstrap;
/*     */ import com.sun.jdi.ReferenceType;
/*     */ import com.sun.jdi.VirtualMachine;
/*     */ import com.sun.jdi.connect.AttachingConnector;
/*     */ import com.sun.jdi.connect.Connector;
/*     */ import com.sun.jdi.connect.IllegalConnectorArgumentsException;
/*     */ import com.sun.jdi.event.Event;
/*     */ import com.sun.jdi.event.EventIterator;
/*     */ import com.sun.jdi.event.EventQueue;
/*     */ import com.sun.jdi.event.EventSet;
/*     */ import com.sun.jdi.request.EventRequest;
/*     */ import com.sun.jdi.request.EventRequestManager;
/*     */ import com.sun.jdi.request.MethodEntryRequest;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HotSwapper
/*     */ {
/*  83 */   private static final String TRIGGER_NAME = Trigger.class.getName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HotSwapper(int port) throws IOException, IllegalConnectorArgumentsException {
/*  93 */     this(Integer.toString(port));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 104 */   private VirtualMachine jvm = null;
/* 105 */   private MethodEntryRequest request = null;
/* 106 */   private Map newClassFiles = null;
/* 107 */   private Trigger trigger = new Trigger();
/*     */   public HotSwapper(String port) throws IOException, IllegalConnectorArgumentsException {
/* 109 */     AttachingConnector connector = (AttachingConnector)findConnector("com.sun.jdi.SocketAttach");
/*     */     
/* 111 */     Map arguments = connector.defaultArguments();
/* 112 */     ((Connector.Argument)arguments.get("hostname")).setValue("localhost");
/* 113 */     ((Connector.Argument)arguments.get("port")).setValue(port);
/* 114 */     this.jvm = connector.attach(arguments);
/* 115 */     EventRequestManager manager = this.jvm.eventRequestManager();
/* 116 */     this.request = methodEntryRequests(manager, TRIGGER_NAME);
/*     */   }
/*     */   private static final String HOST_NAME = "localhost";
/*     */   private Connector findConnector(String connector) throws IOException {
/* 120 */     List connectors = Bootstrap.virtualMachineManager().allConnectors();
/* 121 */     Iterator<Connector> iter = connectors.iterator();
/* 122 */     while (iter.hasNext()) {
/* 123 */       Connector con = iter.next();
/* 124 */       if (con.name().equals(connector)) {
/* 125 */         return con;
/*     */       }
/*     */     } 
/*     */     
/* 129 */     throw new IOException("Not found: " + connector);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static MethodEntryRequest methodEntryRequests(EventRequestManager manager, String classpattern) {
/* 135 */     MethodEntryRequest mereq = manager.createMethodEntryRequest();
/* 136 */     mereq.addClassFilter(classpattern);
/* 137 */     mereq.setSuspendPolicy(1);
/* 138 */     return mereq;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void deleteEventRequest(EventRequestManager manager, MethodEntryRequest request) {
/* 145 */     manager.deleteEventRequest((EventRequest)request);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reload(String className, byte[] classFile) {
/* 155 */     ReferenceType classtype = toRefType(className);
/* 156 */     Map<Object, Object> map = new HashMap<Object, Object>();
/* 157 */     map.put(classtype, classFile);
/* 158 */     reload2(map, className);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reload(Map classFiles) {
/* 170 */     Set set = classFiles.entrySet();
/* 171 */     Iterator<Map.Entry> it = set.iterator();
/* 172 */     Map<Object, Object> map = new HashMap<Object, Object>();
/* 173 */     String className = null;
/* 174 */     while (it.hasNext()) {
/* 175 */       Map.Entry e = it.next();
/* 176 */       className = (String)e.getKey();
/* 177 */       map.put(toRefType(className), e.getValue());
/*     */     } 
/*     */     
/* 180 */     if (className != null)
/* 181 */       reload2(map, className + " etc."); 
/*     */   }
/*     */   
/*     */   private ReferenceType toRefType(String className) {
/* 185 */     List<ReferenceType> list = this.jvm.classesByName(className);
/* 186 */     if (list == null || list.isEmpty()) {
/* 187 */       throw new RuntimeException("no such class: " + className);
/*     */     }
/* 189 */     return list.get(0);
/*     */   }
/*     */   
/*     */   private void reload2(Map map, String msg) {
/* 193 */     synchronized (this.trigger) {
/* 194 */       startDaemon();
/* 195 */       this.newClassFiles = map;
/* 196 */       this.request.enable();
/* 197 */       this.trigger.doSwap();
/* 198 */       this.request.disable();
/* 199 */       Map ncf = this.newClassFiles;
/* 200 */       if (ncf != null) {
/* 201 */         this.newClassFiles = null;
/* 202 */         throw new RuntimeException("failed to reload: " + msg);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void startDaemon() {
/* 208 */     (new Thread() {
/*     */         private void errorMsg(Throwable e) {
/* 210 */           System.err.print("Exception in thread \"HotSwap\" ");
/* 211 */           e.printStackTrace(System.err);
/*     */         }
/*     */         
/*     */         public void run() {
/* 215 */           EventSet events = null;
/*     */           try {
/* 217 */             events = HotSwapper.this.waitEvent();
/* 218 */             EventIterator iter = events.eventIterator();
/* 219 */             while (iter.hasNext()) {
/* 220 */               Event event = iter.nextEvent();
/* 221 */               if (event instanceof com.sun.jdi.event.MethodEntryEvent) {
/* 222 */                 HotSwapper.this.hotswap();
/*     */                 
/*     */                 break;
/*     */               } 
/*     */             } 
/* 227 */           } catch (Throwable e) {
/* 228 */             errorMsg(e);
/*     */           } 
/*     */           try {
/* 231 */             if (events != null) {
/* 232 */               events.resume();
/*     */             }
/* 234 */           } catch (Throwable e) {
/* 235 */             errorMsg(e);
/*     */           } 
/*     */         }
/* 238 */       }).start();
/*     */   }
/*     */   
/*     */   EventSet waitEvent() throws InterruptedException {
/* 242 */     EventQueue queue = this.jvm.eventQueue();
/* 243 */     return queue.remove();
/*     */   }
/*     */   
/*     */   void hotswap() {
/* 247 */     Map map = this.newClassFiles;
/* 248 */     this.jvm.redefineClasses(map);
/* 249 */     this.newClassFiles = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassis\\util\HotSwapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */