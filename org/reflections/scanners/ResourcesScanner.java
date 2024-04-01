/*    */ package org.reflections.scanners;
/*    */ 
/*    */ import org.reflections.vfs.Vfs;
/*    */ 
/*    */ public class ResourcesScanner
/*    */   extends AbstractScanner
/*    */ {
/*    */   public boolean acceptsInput(String file) {
/*  9 */     return !file.endsWith(".class");
/*    */   }
/*    */   
/*    */   public Object scan(Vfs.File file, Object classObject) {
/* 13 */     getStore().put(file.getName(), file.getRelativePath());
/* 14 */     return classObject;
/*    */   }
/*    */   
/*    */   public void scan(Object cls) {
/* 18 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\reflections\scanners\ResourcesScanner.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */