/*    */ package javassist;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.InputStream;
/*    */ import java.net.MalformedURLException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ByteArrayClassPath
/*    */   implements ClassPath
/*    */ {
/*    */   protected String classname;
/*    */   protected byte[] classfile;
/*    */   
/*    */   public ByteArrayClassPath(String name, byte[] classfile) {
/* 61 */     this.classname = name;
/* 62 */     this.classfile = classfile;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() {}
/*    */ 
/*    */   
/*    */   public String toString() {
/* 71 */     return "byte[]:" + this.classname;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public InputStream openClassfile(String classname) {
/* 78 */     if (this.classname.equals(classname)) {
/* 79 */       return new ByteArrayInputStream(this.classfile);
/*    */     }
/* 81 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public URL find(String classname) {
/* 88 */     if (this.classname.equals(classname)) {
/* 89 */       String cname = classname.replace('.', '/') + ".class";
/*    */       
/*    */       try {
/* 92 */         return new URL("file:/ByteArrayClassPath/" + cname);
/*    */       }
/* 94 */       catch (MalformedURLException malformedURLException) {}
/*    */     } 
/*    */     
/* 97 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\ByteArrayClassPath.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */