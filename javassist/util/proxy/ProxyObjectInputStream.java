/*    */ package javassist.util.proxy;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectStreamClass;
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
/*    */ public class ProxyObjectInputStream
/*    */   extends ObjectInputStream
/*    */ {
/*    */   private ClassLoader loader;
/*    */   
/*    */   public ProxyObjectInputStream(InputStream in) throws IOException {
/* 46 */     super(in);
/* 47 */     this.loader = Thread.currentThread().getContextClassLoader();
/* 48 */     if (this.loader == null) {
/* 49 */       this.loader = ClassLoader.getSystemClassLoader();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setClassLoader(ClassLoader loader) {
/* 59 */     if (loader != null) {
/* 60 */       this.loader = loader;
/*    */     } else {
/* 62 */       loader = ClassLoader.getSystemClassLoader();
/*    */     } 
/*    */   }
/*    */   
/*    */   protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
/* 67 */     boolean isProxy = readBoolean();
/* 68 */     if (isProxy) {
/* 69 */       String name = (String)readObject();
/* 70 */       Class<?> superClass = this.loader.loadClass(name);
/* 71 */       int length = readInt();
/* 72 */       Class[] interfaces = new Class[length];
/* 73 */       for (int i = 0; i < length; i++) {
/* 74 */         name = (String)readObject();
/* 75 */         interfaces[i] = this.loader.loadClass(name);
/*    */       } 
/* 77 */       length = readInt();
/* 78 */       byte[] signature = new byte[length];
/* 79 */       read(signature);
/* 80 */       ProxyFactory factory = new ProxyFactory();
/*    */ 
/*    */       
/* 83 */       factory.setUseCache(true);
/* 84 */       factory.setUseWriteReplace(false);
/* 85 */       factory.setSuperclass(superClass);
/* 86 */       factory.setInterfaces(interfaces);
/* 87 */       Class<?> proxyClass = factory.createClass(signature);
/* 88 */       return ObjectStreamClass.lookup(proxyClass);
/*    */     } 
/* 90 */     return super.readClassDescriptor();
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassis\\util\proxy\ProxyObjectInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */