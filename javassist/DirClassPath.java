/*    */ package javassist;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
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
/*    */ final class DirClassPath
/*    */   implements ClassPath
/*    */ {
/*    */   String directory;
/*    */   
/*    */   DirClassPath(String dirName) {
/* 39 */     this.directory = dirName;
/*    */   }
/*    */   
/*    */   public InputStream openClassfile(String classname) {
/*    */     
/* 44 */     try { char sep = File.separatorChar;
/*    */       
/* 46 */       String filename = this.directory + sep + classname.replace('.', sep) + ".class";
/* 47 */       return new FileInputStream(filename.toString()); }
/*    */     
/* 49 */     catch (FileNotFoundException fileNotFoundException) {  }
/* 50 */     catch (SecurityException securityException) {}
/* 51 */     return null;
/*    */   }
/*    */   
/*    */   public URL find(String classname) {
/* 55 */     char sep = File.separatorChar;
/*    */     
/* 57 */     String filename = this.directory + sep + classname.replace('.', sep) + ".class";
/* 58 */     File f = new File(filename);
/* 59 */     if (f.exists()) {
/*    */       
/* 61 */       try { return f.getCanonicalFile().toURI().toURL(); }
/*    */       
/* 63 */       catch (MalformedURLException malformedURLException) {  }
/* 64 */       catch (IOException iOException) {}
/*    */     }
/* 66 */     return null;
/*    */   }
/*    */   
/*    */   public void close() {}
/*    */   
/*    */   public String toString() {
/* 72 */     return this.directory;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\DirClassPath.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */