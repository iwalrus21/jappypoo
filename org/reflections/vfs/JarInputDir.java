/*    */ package org.reflections.vfs;
/*    */ 
/*    */ import com.google.common.collect.AbstractIterator;
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
/*    */ import java.util.Iterator;
/*    */ import java.util.jar.JarInputStream;
/*    */ import java.util.zip.ZipEntry;
/*    */ import org.reflections.ReflectionsException;
/*    */ import org.reflections.util.Utils;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JarInputDir
/*    */   implements Vfs.Dir
/*    */ {
/*    */   private final URL url;
/*    */   JarInputStream jarInputStream;
/* 19 */   long cursor = 0L;
/* 20 */   long nextCursor = 0L;
/*    */   
/*    */   public JarInputDir(URL url) {
/* 23 */     this.url = url;
/*    */   }
/*    */   
/*    */   public String getPath() {
/* 27 */     return this.url.getPath();
/*    */   }
/*    */   
/*    */   public Iterable<Vfs.File> getFiles() {
/* 31 */     return new Iterable<Vfs.File>() {
/*    */         public Iterator<Vfs.File> iterator() {
/* 33 */           return (Iterator<Vfs.File>)new AbstractIterator<Vfs.File>()
/*    */             {
/*    */ 
/*    */ 
/*    */ 
/*    */               
/*    */               protected Vfs.File computeNext()
/*    */               {
/*    */                 try {
/*    */                   while (true) {
/* 43 */                     ZipEntry entry = JarInputDir.this.jarInputStream.getNextJarEntry();
/* 44 */                     if (entry == null) {
/* 45 */                       return (Vfs.File)endOfData();
/*    */                     }
/*    */                     
/* 48 */                     long size = entry.getSize();
/* 49 */                     if (size < 0L) size = 4294967295L + size; 
/* 50 */                     JarInputDir.this.nextCursor += size;
/* 51 */                     if (!entry.isDirectory())
/* 52 */                       return new JarInputFile(entry, JarInputDir.this, JarInputDir.this.cursor, JarInputDir.this.nextCursor); 
/*    */                   } 
/* 54 */                 } catch (IOException e) {
/* 55 */                   throw new ReflectionsException("could not get next zip entry", e);
/*    */                 } 
/*    */               }
/*    */             };
/*    */         }
/*    */       };
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 65 */     Utils.close(this.jarInputStream);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\reflections\vfs\JarInputDir.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */