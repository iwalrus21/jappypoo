/*     */ package javassist.tools.rmi;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InvalidClassException;
/*     */ import java.io.NotSerializableException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.ClassPool;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.tools.web.BadHttpRequest;
/*     */ import javassist.tools.web.Webserver;
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
/*     */ public class AppletServer
/*     */   extends Webserver
/*     */ {
/*     */   private StubGenerator stubGen;
/*     */   private Hashtable exportedNames;
/*     */   private Vector exportedObjects;
/*  43 */   private static final byte[] okHeader = "HTTP/1.0 200 OK\r\n\r\n"
/*  44 */     .getBytes();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AppletServer(String port) throws IOException, NotFoundException, CannotCompileException {
/*  54 */     this(Integer.parseInt(port));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AppletServer(int port) throws IOException, NotFoundException, CannotCompileException {
/*  65 */     this(ClassPool.getDefault(), new StubGenerator(), port);
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
/*     */   public AppletServer(int port, ClassPool src) throws IOException, NotFoundException, CannotCompileException {
/*  77 */     this(new ClassPool(src), new StubGenerator(), port);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private AppletServer(ClassPool loader, StubGenerator gen, int port) throws IOException, NotFoundException, CannotCompileException {
/*  83 */     super(port);
/*  84 */     this.exportedNames = new Hashtable<Object, Object>();
/*  85 */     this.exportedObjects = new Vector();
/*  86 */     this.stubGen = gen;
/*  87 */     addTranslator(loader, gen);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/*  94 */     super.run();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized int exportObject(String name, Object obj) throws CannotCompileException {
/* 112 */     Class<?> clazz = obj.getClass();
/* 113 */     ExportedObject eo = new ExportedObject();
/* 114 */     eo.object = obj;
/* 115 */     eo.methods = clazz.getMethods();
/* 116 */     this.exportedObjects.addElement(eo);
/* 117 */     eo.identifier = this.exportedObjects.size() - 1;
/* 118 */     if (name != null) {
/* 119 */       this.exportedNames.put(name, eo);
/*     */     }
/*     */     try {
/* 122 */       this.stubGen.makeProxyClass(clazz);
/*     */     }
/* 124 */     catch (NotFoundException e) {
/* 125 */       throw new CannotCompileException(e);
/*     */     } 
/*     */     
/* 128 */     return eo.identifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doReply(InputStream in, OutputStream out, String cmd) throws IOException, BadHttpRequest {
/* 137 */     if (cmd.startsWith("POST /rmi ")) {
/* 138 */       processRMI(in, out);
/* 139 */     } else if (cmd.startsWith("POST /lookup ")) {
/* 140 */       lookupName(cmd, in, out);
/*     */     } else {
/* 142 */       super.doReply(in, out, cmd);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void processRMI(InputStream ins, OutputStream outs) throws IOException {
/* 148 */     ObjectInputStream in = new ObjectInputStream(ins);
/*     */     
/* 150 */     int objectId = in.readInt();
/* 151 */     int methodId = in.readInt();
/* 152 */     Exception err = null;
/* 153 */     Object rvalue = null;
/*     */     
/*     */     try {
/* 156 */       ExportedObject eo = this.exportedObjects.elementAt(objectId);
/* 157 */       Object[] args = readParameters(in);
/* 158 */       rvalue = convertRvalue(eo.methods[methodId].invoke(eo.object, args));
/*     */     
/*     */     }
/* 161 */     catch (Exception e) {
/* 162 */       err = e;
/* 163 */       logging2(e.toString());
/*     */     } 
/*     */     
/* 166 */     outs.write(okHeader);
/* 167 */     ObjectOutputStream out = new ObjectOutputStream(outs);
/* 168 */     if (err != null) {
/* 169 */       out.writeBoolean(false);
/* 170 */       out.writeUTF(err.toString());
/*     */     } else {
/*     */       
/*     */       try {
/* 174 */         out.writeBoolean(true);
/* 175 */         out.writeObject(rvalue);
/*     */       }
/* 177 */       catch (NotSerializableException e) {
/* 178 */         logging2(e.toString());
/*     */       }
/* 180 */       catch (InvalidClassException e) {
/* 181 */         logging2(e.toString());
/*     */       } 
/*     */     } 
/* 184 */     out.flush();
/* 185 */     out.close();
/* 186 */     in.close();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Object[] readParameters(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 192 */     int n = in.readInt();
/* 193 */     Object[] args = new Object[n];
/* 194 */     for (int i = 0; i < n; i++) {
/* 195 */       Object a = in.readObject();
/* 196 */       if (a instanceof RemoteRef) {
/* 197 */         RemoteRef ref = (RemoteRef)a;
/*     */         
/* 199 */         ExportedObject eo = this.exportedObjects.elementAt(ref.oid);
/* 200 */         a = eo.object;
/*     */       } 
/*     */       
/* 203 */       args[i] = a;
/*     */     } 
/*     */     
/* 206 */     return args;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Object convertRvalue(Object rvalue) throws CannotCompileException {
/* 212 */     if (rvalue == null) {
/* 213 */       return null;
/*     */     }
/* 215 */     String classname = rvalue.getClass().getName();
/* 216 */     if (this.stubGen.isProxyClass(classname)) {
/* 217 */       return new RemoteRef(exportObject((String)null, rvalue), classname);
/*     */     }
/* 219 */     return rvalue;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void lookupName(String cmd, InputStream ins, OutputStream outs) throws IOException {
/* 225 */     ObjectInputStream in = new ObjectInputStream(ins);
/* 226 */     String name = DataInputStream.readUTF(in);
/* 227 */     ExportedObject found = (ExportedObject)this.exportedNames.get(name);
/* 228 */     outs.write(okHeader);
/* 229 */     ObjectOutputStream out = new ObjectOutputStream(outs);
/* 230 */     if (found == null) {
/* 231 */       logging2(name + "not found.");
/* 232 */       out.writeInt(-1);
/* 233 */       out.writeUTF("error");
/*     */     } else {
/*     */       
/* 236 */       logging2(name);
/* 237 */       out.writeInt(found.identifier);
/* 238 */       out.writeUTF(found.object.getClass().getName());
/*     */     } 
/*     */     
/* 241 */     out.flush();
/* 242 */     out.close();
/* 243 */     in.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\tools\rmi\AppletServer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */