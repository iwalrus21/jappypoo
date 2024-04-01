/*    */ package org.reflections.vfs;
/*    */ 
/*    */ import com.google.common.collect.AbstractIterator;
/*    */ import java.io.IOException;
/*    */ import java.util.Enumeration;
/*    */ import java.util.Iterator;
/*    */ import java.util.jar.JarFile;
/*    */ import java.util.zip.ZipEntry;
/*    */ import java.util.zip.ZipFile;
/*    */ import org.reflections.Reflections;
/*    */ 
/*    */ public class ZipDir implements Vfs.Dir {
/*    */   final ZipFile jarFile;
/*    */   
/*    */   public ZipDir(JarFile jarFile) {
/* 16 */     this.jarFile = jarFile;
/*    */   }
/*    */   
/*    */   public String getPath() {
/* 20 */     return this.jarFile.getName();
/*    */   }
/*    */   
/*    */   public Iterable<Vfs.File> getFiles() {
/* 24 */     return new Iterable<Vfs.File>() {
/*    */         public Iterator<Vfs.File> iterator() {
/* 26 */           return (Iterator<Vfs.File>)new AbstractIterator<Vfs.File>() {
/* 27 */               final Enumeration<? extends ZipEntry> entries = ZipDir.this.jarFile.entries();
/*    */               
/*    */               protected Vfs.File computeNext() {
/* 30 */                 while (this.entries.hasMoreElements()) {
/* 31 */                   ZipEntry entry = this.entries.nextElement();
/* 32 */                   if (!entry.isDirectory()) {
/* 33 */                     return new ZipFile(ZipDir.this, entry);
/*    */                   }
/*    */                 } 
/*    */                 
/* 37 */                 return (Vfs.File)endOfData();
/*    */               }
/*    */             };
/*    */         }
/*    */       };
/*    */   }
/*    */   public void close() {
/*    */     
/* 45 */     try { this.jarFile.close(); } catch (IOException e)
/* 46 */     { if (Reflections.log != null) {
/* 47 */         Reflections.log.warn("Could not close JarFile", e);
/*    */       } }
/*    */   
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 54 */     return this.jarFile.getName();
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\reflections\vfs\ZipDir.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */