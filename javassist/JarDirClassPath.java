/*     */ package javassist;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FilenameFilter;
/*     */ import java.io.InputStream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class JarDirClassPath
/*     */   implements ClassPath
/*     */ {
/*     */   JarClassPath[] jars;
/*     */   
/*     */   JarDirClassPath(String dirName) throws NotFoundException {
/*  80 */     File[] files = (new File(dirName)).listFiles(new FilenameFilter() {
/*     */           public boolean accept(File dir, String name) {
/*  82 */             name = name.toLowerCase();
/*  83 */             return (name.endsWith(".jar") || name.endsWith(".zip"));
/*     */           }
/*     */         });
/*     */     
/*  87 */     if (files != null) {
/*  88 */       this.jars = new JarClassPath[files.length];
/*  89 */       for (int i = 0; i < files.length; i++)
/*  90 */         this.jars[i] = new JarClassPath(files[i].getPath()); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public InputStream openClassfile(String classname) throws NotFoundException {
/*  95 */     if (this.jars != null)
/*  96 */       for (int i = 0; i < this.jars.length; i++) {
/*  97 */         InputStream is = this.jars[i].openClassfile(classname);
/*  98 */         if (is != null) {
/*  99 */           return is;
/*     */         }
/*     */       }  
/* 102 */     return null;
/*     */   }
/*     */   
/*     */   public URL find(String classname) {
/* 106 */     if (this.jars != null)
/* 107 */       for (int i = 0; i < this.jars.length; i++) {
/* 108 */         URL url = this.jars[i].find(classname);
/* 109 */         if (url != null) {
/* 110 */           return url;
/*     */         }
/*     */       }  
/* 113 */     return null;
/*     */   }
/*     */   
/*     */   public void close() {
/* 117 */     if (this.jars != null)
/* 118 */       for (int i = 0; i < this.jars.length; i++)
/* 119 */         this.jars[i].close();  
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\JarDirClassPath.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */