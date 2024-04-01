/*    */ package javassist.util.proxy;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.io.ObjectStreamClass;
/*    */ import java.io.OutputStream;
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
/*    */ public class ProxyObjectOutputStream
/*    */   extends ObjectOutputStream
/*    */ {
/*    */   public ProxyObjectOutputStream(OutputStream out) throws IOException {
/* 44 */     super(out);
/*    */   }
/*    */   
/*    */   protected void writeClassDescriptor(ObjectStreamClass desc) throws IOException {
/* 48 */     Class<?> cl = desc.forClass();
/* 49 */     if (ProxyFactory.isProxyClass(cl)) {
/* 50 */       writeBoolean(true);
/* 51 */       Class<?> superClass = cl.getSuperclass();
/* 52 */       Class[] interfaces = cl.getInterfaces();
/* 53 */       byte[] signature = ProxyFactory.getFilterSignature(cl);
/* 54 */       String name = superClass.getName();
/* 55 */       writeObject(name);
/*    */       
/* 57 */       writeInt(interfaces.length - 1);
/* 58 */       for (int i = 0; i < interfaces.length; i++) {
/* 59 */         Class<ProxyObject> interfaze = interfaces[i];
/* 60 */         if (interfaze != ProxyObject.class && interfaze != Proxy.class) {
/* 61 */           name = interfaces[i].getName();
/* 62 */           writeObject(name);
/*    */         } 
/*    */       } 
/* 65 */       writeInt(signature.length);
/* 66 */       write(signature);
/*    */     } else {
/* 68 */       writeBoolean(false);
/* 69 */       super.writeClassDescriptor(desc);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassis\\util\proxy\ProxyObjectOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */