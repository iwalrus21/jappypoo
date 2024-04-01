/*     */ package org.reflections.vfs;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.reflections.Reflections;
/*     */ import org.reflections.ReflectionsException;
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
/*     */ public class UrlTypeVFS
/*     */   implements Vfs.UrlType
/*     */ {
/*  27 */   public static final String[] REPLACE_EXTENSION = new String[] { ".ear/", ".jar/", ".war/", ".sar/", ".har/", ".par/" };
/*     */   
/*  29 */   final String VFSZIP = "vfszip";
/*  30 */   final String VFSFILE = "vfsfile";
/*     */   
/*     */   public boolean matches(URL url) {
/*  33 */     return ("vfszip".equals(url.getProtocol()) || "vfsfile".equals(url.getProtocol()));
/*     */   }
/*     */   
/*     */   public Vfs.Dir createDir(URL url) {
/*     */     try {
/*  38 */       URL adaptedUrl = adaptURL(url);
/*  39 */       return new ZipDir(new JarFile(adaptedUrl.getFile()));
/*  40 */     } catch (Exception e) {
/*     */       try {
/*  42 */         return new ZipDir(new JarFile(url.getFile()));
/*  43 */       } catch (IOException e1) {
/*  44 */         if (Reflections.log != null) {
/*  45 */           Reflections.log.warn("Could not get URL", e);
/*  46 */           Reflections.log.warn("Could not get URL", e1);
/*     */         } 
/*     */ 
/*     */         
/*  50 */         return null;
/*     */       } 
/*     */     } 
/*     */   } public URL adaptURL(URL url) throws MalformedURLException {
/*  54 */     if ("vfszip".equals(url.getProtocol()))
/*  55 */       return replaceZipSeparators(url.getPath(), this.realFile); 
/*  56 */     if ("vfsfile".equals(url.getProtocol())) {
/*  57 */       return new URL(url.toString().replace("vfsfile", "file"));
/*     */     }
/*  59 */     return url;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   URL replaceZipSeparators(String path, Predicate<File> acceptFile) throws MalformedURLException {
/*  65 */     int pos = 0;
/*  66 */     while (pos != -1) {
/*  67 */       pos = findFirstMatchOfDeployableExtention(path, pos);
/*     */       
/*  69 */       if (pos > 0) {
/*  70 */         File file = new File(path.substring(0, pos - 1));
/*  71 */         if (acceptFile.apply(file)) return replaceZipSeparatorStartingFrom(path, pos);
/*     */       
/*     */       } 
/*     */     } 
/*  75 */     throw new ReflectionsException("Unable to identify the real zip file in path '" + path + "'.");
/*     */   }
/*     */   
/*     */   int findFirstMatchOfDeployableExtention(String path, int pos) {
/*  79 */     Pattern p = Pattern.compile("\\.[ejprw]ar/");
/*  80 */     Matcher m = p.matcher(path);
/*  81 */     if (m.find(pos)) {
/*  82 */       return m.end();
/*     */     }
/*  84 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*  88 */   Predicate<File> realFile = new Predicate<File>() {
/*     */       public boolean apply(File file) {
/*  90 */         return (file.exists() && file.isFile());
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   URL replaceZipSeparatorStartingFrom(String path, int pos) throws MalformedURLException {
/*  96 */     String zipFile = path.substring(0, pos - 1);
/*  97 */     String zipPath = path.substring(pos);
/*     */     
/*  99 */     int numSubs = 1;
/* 100 */     for (String ext : REPLACE_EXTENSION) {
/* 101 */       while (zipPath.contains(ext)) {
/* 102 */         zipPath = zipPath.replace(ext, ext.substring(0, 4) + "!");
/* 103 */         numSubs++;
/*     */       } 
/*     */     } 
/*     */     
/* 107 */     String prefix = "";
/* 108 */     for (int i = 0; i < numSubs; i++) {
/* 109 */       prefix = prefix + "zip:";
/*     */     }
/*     */     
/* 112 */     if (zipPath.trim().length() == 0) {
/* 113 */       return new URL(prefix + "/" + zipFile);
/*     */     }
/* 115 */     return new URL(prefix + "/" + zipFile + "!" + zipPath);
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\reflections\vfs\UrlTypeVFS.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */