/*    */ package javassist;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ import java.lang.ref.WeakReference;
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
/*    */ public class LoaderClassPath
/*    */   implements ClassPath
/*    */ {
/*    */   private WeakReference clref;
/*    */   
/*    */   public LoaderClassPath(ClassLoader cl) {
/* 49 */     this.clref = new WeakReference<ClassLoader>(cl);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 53 */     Object cl = null;
/* 54 */     if (this.clref != null) {
/* 55 */       cl = this.clref.get();
/*    */     }
/* 57 */     return (cl == null) ? "<null>" : cl.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public InputStream openClassfile(String classname) {
/* 66 */     String cname = classname.replace('.', '/') + ".class";
/* 67 */     ClassLoader cl = this.clref.get();
/* 68 */     if (cl == null) {
/* 69 */       return null;
/*    */     }
/* 71 */     return cl.getResourceAsStream(cname);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public URL find(String classname) {
/* 82 */     String cname = classname.replace('.', '/') + ".class";
/* 83 */     ClassLoader cl = this.clref.get();
/* 84 */     if (cl == null) {
/* 85 */       return null;
/*    */     }
/* 87 */     return cl.getResource(cname);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() {
/* 94 */     this.clref = null;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\LoaderClassPath.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */