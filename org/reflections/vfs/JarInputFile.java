/*    */ package org.reflections.vfs;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.zip.ZipEntry;
/*    */ 
/*    */ 
/*    */ public class JarInputFile
/*    */   implements Vfs.File
/*    */ {
/*    */   private final ZipEntry entry;
/*    */   private final JarInputDir jarInputDir;
/*    */   private final long fromIndex;
/*    */   private final long endIndex;
/*    */   
/*    */   public JarInputFile(ZipEntry entry, JarInputDir jarInputDir, long cursor, long nextCursor) {
/* 17 */     this.entry = entry;
/* 18 */     this.jarInputDir = jarInputDir;
/* 19 */     this.fromIndex = cursor;
/* 20 */     this.endIndex = nextCursor;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 24 */     String name = this.entry.getName();
/* 25 */     return name.substring(name.lastIndexOf("/") + 1);
/*    */   }
/*    */   
/*    */   public String getRelativePath() {
/* 29 */     return this.entry.getName();
/*    */   }
/*    */   
/*    */   public InputStream openInputStream() throws IOException {
/* 33 */     return new InputStream()
/*    */       {
/*    */         public int read() throws IOException {
/* 36 */           if (JarInputFile.this.jarInputDir.cursor >= JarInputFile.this.fromIndex && JarInputFile.this.jarInputDir.cursor <= JarInputFile.this.endIndex) {
/* 37 */             int read = JarInputFile.this.jarInputDir.jarInputStream.read();
/* 38 */             JarInputFile.this.jarInputDir.cursor++;
/* 39 */             return read;
/*    */           } 
/* 41 */           return -1;
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\reflections\vfs\JarInputFile.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */