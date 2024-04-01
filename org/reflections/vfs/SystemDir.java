/*    */ package org.reflections.vfs;
/*    */ 
/*    */ import com.google.common.collect.AbstractIterator;
/*    */ import com.google.common.collect.Lists;
/*    */ import java.io.File;
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Stack;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SystemDir
/*    */   implements Vfs.Dir
/*    */ {
/*    */   private final File file;
/*    */   
/*    */   public SystemDir(File file) {
/* 19 */     if (file != null && (!file.isDirectory() || !file.canRead())) {
/* 20 */       throw new RuntimeException("cannot use dir " + file);
/*    */     }
/*    */     
/* 23 */     this.file = file;
/*    */   }
/*    */   
/*    */   public String getPath() {
/* 27 */     if (this.file == null) {
/* 28 */       return "/NO-SUCH-DIRECTORY/";
/*    */     }
/* 30 */     return this.file.getPath().replace("\\", "/");
/*    */   }
/*    */   
/*    */   public Iterable<Vfs.File> getFiles() {
/* 34 */     if (this.file == null || !this.file.exists()) {
/* 35 */       return Collections.emptyList();
/*    */     }
/* 37 */     return new Iterable<Vfs.File>() {
/*    */         public Iterator<Vfs.File> iterator() {
/* 39 */           return (Iterator<Vfs.File>)new AbstractIterator<Vfs.File>()
/*    */             {
/*    */               final Stack<File> stack;
/*    */               
/*    */               protected Vfs.File computeNext() {
/* 44 */                 while (!this.stack.isEmpty()) {
/* 45 */                   File file = this.stack.pop();
/* 46 */                   if (file.isDirectory()) {
/* 47 */                     this.stack.addAll(SystemDir.listFiles(file)); continue;
/*    */                   } 
/* 49 */                   return new SystemFile(SystemDir.this, file);
/*    */                 } 
/*    */ 
/*    */                 
/* 53 */                 return (Vfs.File)endOfData();
/*    */               }
/*    */             };
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   private static List<File> listFiles(File file) {
/* 61 */     File[] files = file.listFiles();
/*    */     
/* 63 */     if (files != null) {
/* 64 */       return Lists.newArrayList((Object[])files);
/*    */     }
/* 66 */     return Lists.newArrayList();
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {}
/*    */ 
/*    */   
/*    */   public String toString() {
/* 74 */     return getPath();
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\reflections\vfs\SystemDir.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */