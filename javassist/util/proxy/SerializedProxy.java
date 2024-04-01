/*    */ package javassist.util.proxy;
/*    */ 
/*    */ import java.io.InvalidClassException;
/*    */ import java.io.InvalidObjectException;
/*    */ import java.io.ObjectStreamException;
/*    */ import java.io.Serializable;
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedActionException;
/*    */ import java.security.PrivilegedExceptionAction;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class SerializedProxy
/*    */   implements Serializable
/*    */ {
/*    */   private String superClass;
/*    */   private String[] interfaces;
/*    */   private byte[] filterSignature;
/*    */   private MethodHandler handler;
/*    */   
/*    */   SerializedProxy(Class proxy, byte[] sig, MethodHandler h) {
/* 39 */     this.filterSignature = sig;
/* 40 */     this.handler = h;
/* 41 */     this.superClass = proxy.getSuperclass().getName();
/* 42 */     Class[] infs = proxy.getInterfaces();
/* 43 */     int n = infs.length;
/* 44 */     this.interfaces = new String[n - 1];
/* 45 */     String setterInf = ProxyObject.class.getName();
/* 46 */     String setterInf2 = Proxy.class.getName();
/* 47 */     for (int i = 0; i < n; i++) {
/* 48 */       String name = infs[i].getName();
/* 49 */       if (!name.equals(setterInf) && !name.equals(setterInf2)) {
/* 50 */         this.interfaces[i] = name;
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Class loadClass(final String className) throws ClassNotFoundException {
/*    */     try {
/* 63 */       return AccessController.<Class<?>>doPrivileged(new PrivilegedExceptionAction<Class<?>>() {
/*    */             public Object run() throws Exception {
/* 65 */               ClassLoader cl = Thread.currentThread().getContextClassLoader();
/* 66 */               return Class.forName(className, true, cl);
/*    */             }
/*    */           });
/*    */     }
/* 70 */     catch (PrivilegedActionException pae) {
/* 71 */       throw new RuntimeException("cannot load the class: " + className, pae.getException());
/*    */     } 
/*    */   }
/*    */   
/*    */   Object readResolve() throws ObjectStreamException {
/*    */     try {
/* 77 */       int n = this.interfaces.length;
/* 78 */       Class[] infs = new Class[n];
/* 79 */       for (int i = 0; i < n; i++) {
/* 80 */         infs[i] = loadClass(this.interfaces[i]);
/*    */       }
/* 82 */       ProxyFactory f = new ProxyFactory();
/* 83 */       f.setSuperclass(loadClass(this.superClass));
/* 84 */       f.setInterfaces(infs);
/* 85 */       Proxy proxy = f.createClass(this.filterSignature).newInstance();
/* 86 */       proxy.setHandler(this.handler);
/* 87 */       return proxy;
/*    */     }
/* 89 */     catch (ClassNotFoundException e) {
/* 90 */       throw new InvalidClassException(e.getMessage());
/*    */     }
/* 92 */     catch (InstantiationException e2) {
/* 93 */       throw new InvalidObjectException(e2.getMessage());
/*    */     }
/* 95 */     catch (IllegalAccessException e3) {
/* 96 */       throw new InvalidClassException(e3.getMessage());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassis\\util\proxy\SerializedProxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */