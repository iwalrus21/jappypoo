/*    */ package org.reflections.scanners;
/*    */ 
/*    */ import org.reflections.vfs.Vfs;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ public class TypesScanner
/*    */   extends AbstractScanner
/*    */ {
/*    */   public Object scan(Vfs.File file, Object classObject) {
/* 13 */     classObject = super.scan(file, classObject);
/* 14 */     String className = getMetadataAdapter().getClassName(classObject);
/* 15 */     getStore().put(className, className);
/* 16 */     return classObject;
/*    */   }
/*    */ 
/*    */   
/*    */   public void scan(Object cls) {
/* 21 */     throw new UnsupportedOperationException("should not get here");
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\reflections\scanners\TypesScanner.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */