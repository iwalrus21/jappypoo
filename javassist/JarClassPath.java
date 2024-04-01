/*     */ package javassist;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
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
/*     */ final class JarClassPath
/*     */   implements ClassPath
/*     */ {
/*     */   JarFile jarfile;
/*     */   String jarfileURL;
/*     */   
/*     */   JarClassPath(String pathname) throws NotFoundException {
/*     */     try {
/* 129 */       this.jarfile = new JarFile(pathname);
/* 130 */       this
/* 131 */         .jarfileURL = (new File(pathname)).getCanonicalFile().toURI().toURL().toString();
/*     */       
/*     */       return;
/* 134 */     } catch (IOException iOException) {
/* 135 */       throw new NotFoundException(pathname);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream openClassfile(String classname) throws NotFoundException {
/*     */     try {
/* 142 */       String jarname = classname.replace('.', '/') + ".class";
/* 143 */       JarEntry je = this.jarfile.getJarEntry(jarname);
/* 144 */       if (je != null) {
/* 145 */         return this.jarfile.getInputStream(je);
/*     */       }
/* 147 */       return null;
/*     */     }
/* 149 */     catch (IOException iOException) {
/* 150 */       throw new NotFoundException("broken jar file?: " + this.jarfile
/* 151 */           .getName());
/*     */     } 
/*     */   }
/*     */   public URL find(String classname) {
/* 155 */     String jarname = classname.replace('.', '/') + ".class";
/* 156 */     JarEntry je = this.jarfile.getJarEntry(jarname);
/* 157 */     if (je != null) {
/*     */       try {
/* 159 */         return new URL("jar:" + this.jarfileURL + "!/" + jarname);
/*     */       }
/* 161 */       catch (MalformedURLException malformedURLException) {}
/*     */     }
/* 163 */     return null;
/*     */   }
/*     */   
/*     */   public void close() {
/*     */     try {
/* 168 */       this.jarfile.close();
/* 169 */       this.jarfile = null;
/*     */     }
/* 171 */     catch (IOException iOException) {}
/*     */   }
/*     */   
/*     */   public String toString() {
/* 175 */     return (this.jarfile == null) ? "<null>" : this.jarfile.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\JarClassPath.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */