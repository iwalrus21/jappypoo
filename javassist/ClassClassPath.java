/*    */ package javassist;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ import java.net.URL;
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
/*    */ public class ClassClassPath
/*    */   implements ClassPath
/*    */ {
/*    */   private Class thisClass;
/*    */   
/*    */   public ClassClassPath(Class c) {
/* 55 */     this.thisClass = c;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   ClassClassPath() {
/* 67 */     this(Object.class);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public InputStream openClassfile(String classname) {
/* 74 */     String jarname = "/" + classname.replace('.', '/') + ".class";
/* 75 */     return this.thisClass.getResourceAsStream(jarname);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public URL find(String classname) {
/* 84 */     String jarname = "/" + classname.replace('.', '/') + ".class";
/* 85 */     return this.thisClass.getResource(jarname);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 95 */     return this.thisClass.getName() + ".class";
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\ClassClassPath.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */