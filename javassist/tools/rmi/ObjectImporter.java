/*     */ package javassist.tools.rmi;
/*     */ 
/*     */ import java.applet.Applet;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.net.Socket;
/*     */ import java.net.URL;
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
/*     */ public class ObjectImporter
/*     */   implements Serializable
/*     */ {
/*  76 */   private final byte[] endofline = new byte[] { 13, 10 };
/*     */   
/*     */   private String servername;
/*     */   private String orgServername;
/*  80 */   protected byte[] lookupCommand = "POST /lookup HTTP/1.0".getBytes(); private int port; private int orgPort;
/*  81 */   protected byte[] rmiCommand = "POST /rmi HTTP/1.0".getBytes();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectImporter(Applet applet) {
/*  92 */     URL codebase = applet.getCodeBase();
/*  93 */     this.orgServername = this.servername = codebase.getHost();
/*  94 */     this.orgPort = this.port = codebase.getPort();
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
/*     */   public ObjectImporter(String servername, int port) {
/* 111 */     this.orgServername = this.servername = servername;
/* 112 */     this.orgPort = this.port = port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getObject(String name) {
/*     */     try {
/* 124 */       return lookupObject(name);
/*     */     }
/* 126 */     catch (ObjectNotFoundException e) {
/* 127 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHttpProxy(String host, int port) {
/* 136 */     String proxyHeader = "POST http://" + this.orgServername + ":" + this.orgPort;
/* 137 */     String cmd = proxyHeader + "/lookup HTTP/1.0";
/* 138 */     this.lookupCommand = cmd.getBytes();
/* 139 */     cmd = proxyHeader + "/rmi HTTP/1.0";
/* 140 */     this.rmiCommand = cmd.getBytes();
/* 141 */     this.servername = host;
/* 142 */     this.port = port;
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
/*     */   public Object lookupObject(String name) throws ObjectNotFoundException {
/*     */     try {
/* 156 */       Socket sock = new Socket(this.servername, this.port);
/* 157 */       OutputStream out = sock.getOutputStream();
/* 158 */       out.write(this.lookupCommand);
/* 159 */       out.write(this.endofline);
/* 160 */       out.write(this.endofline);
/*     */       
/* 162 */       ObjectOutputStream dout = new ObjectOutputStream(out);
/* 163 */       dout.writeUTF(name);
/* 164 */       dout.flush();
/*     */       
/* 166 */       InputStream in = new BufferedInputStream(sock.getInputStream());
/* 167 */       skipHeader(in);
/* 168 */       ObjectInputStream din = new ObjectInputStream(in);
/* 169 */       int n = din.readInt();
/* 170 */       String classname = din.readUTF();
/* 171 */       din.close();
/* 172 */       dout.close();
/* 173 */       sock.close();
/*     */       
/* 175 */       if (n >= 0) {
/* 176 */         return createProxy(n, classname);
/*     */       }
/* 178 */     } catch (Exception e) {
/* 179 */       e.printStackTrace();
/* 180 */       throw new ObjectNotFoundException(name, e);
/*     */     } 
/*     */     
/* 183 */     throw new ObjectNotFoundException(name);
/*     */   }
/*     */   
/* 186 */   private static final Class[] proxyConstructorParamTypes = new Class[] { ObjectImporter.class, int.class };
/*     */ 
/*     */   
/*     */   private Object createProxy(int oid, String classname) throws Exception {
/* 190 */     Class<?> c = Class.forName(classname);
/* 191 */     Constructor<?> cons = c.getConstructor(proxyConstructorParamTypes);
/* 192 */     return cons.newInstance(new Object[] { this, new Integer(oid) });
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
/*     */   public Object call(int objectid, int methodid, Object[] args) throws RemoteException {
/*     */     boolean result;
/*     */     Object rvalue;
/*     */     String errmsg;
/*     */     try {
/* 225 */       Socket sock = new Socket(this.servername, this.port);
/*     */       
/* 227 */       OutputStream out = new BufferedOutputStream(sock.getOutputStream());
/* 228 */       out.write(this.rmiCommand);
/* 229 */       out.write(this.endofline);
/* 230 */       out.write(this.endofline);
/*     */       
/* 232 */       ObjectOutputStream dout = new ObjectOutputStream(out);
/* 233 */       dout.writeInt(objectid);
/* 234 */       dout.writeInt(methodid);
/* 235 */       writeParameters(dout, args);
/* 236 */       dout.flush();
/*     */       
/* 238 */       InputStream ins = new BufferedInputStream(sock.getInputStream());
/* 239 */       skipHeader(ins);
/* 240 */       ObjectInputStream din = new ObjectInputStream(ins);
/* 241 */       result = din.readBoolean();
/* 242 */       rvalue = null;
/* 243 */       errmsg = null;
/* 244 */       if (result) {
/* 245 */         rvalue = din.readObject();
/*     */       } else {
/* 247 */         errmsg = din.readUTF();
/*     */       } 
/* 249 */       din.close();
/* 250 */       dout.close();
/* 251 */       sock.close();
/*     */       
/* 253 */       if (rvalue instanceof RemoteRef) {
/* 254 */         RemoteRef ref = (RemoteRef)rvalue;
/* 255 */         rvalue = createProxy(ref.oid, ref.classname);
/*     */       }
/*     */     
/* 258 */     } catch (ClassNotFoundException e) {
/* 259 */       throw new RemoteException(e);
/*     */     }
/* 261 */     catch (IOException e) {
/* 262 */       throw new RemoteException(e);
/*     */     }
/* 264 */     catch (Exception e) {
/* 265 */       throw new RemoteException(e);
/*     */     } 
/*     */     
/* 268 */     if (result) {
/* 269 */       return rvalue;
/*     */     }
/* 271 */     throw new RemoteException(errmsg);
/*     */   }
/*     */ 
/*     */   
/*     */   private void skipHeader(InputStream in) throws IOException {
/*     */     int len;
/*     */     do {
/* 278 */       len = 0; int c;
/* 279 */       while ((c = in.read()) >= 0 && c != 13) {
/* 280 */         len++;
/*     */       }
/* 282 */       in.read();
/* 283 */     } while (len > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeParameters(ObjectOutputStream dout, Object[] params) throws IOException {
/* 289 */     int n = params.length;
/* 290 */     dout.writeInt(n);
/* 291 */     for (int i = 0; i < n; i++) {
/* 292 */       if (params[i] instanceof Proxy) {
/* 293 */         Proxy p = (Proxy)params[i];
/* 294 */         dout.writeObject(new RemoteRef(p._getObjectId()));
/*     */       } else {
/*     */         
/* 297 */         dout.writeObject(params[i]);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\tools\rmi\ObjectImporter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */