/*    */ package org.reflections.scanners;
/*    */ 
/*    */ import com.google.common.base.Predicate;
/*    */ import org.reflections.util.FilterBuilder;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SubTypesScanner
/*    */   extends AbstractScanner
/*    */ {
/*    */   public SubTypesScanner() {
/* 12 */     this(true);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public SubTypesScanner(boolean excludeObjectClass) {
/* 18 */     if (excludeObjectClass) {
/* 19 */       filterResultsBy((Predicate<String>)(new FilterBuilder()).exclude(Object.class.getName()));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void scan(Object cls) {
/* 25 */     String className = getMetadataAdapter().getClassName(cls);
/* 26 */     String superclass = getMetadataAdapter().getSuperclassName(cls);
/*    */     
/* 28 */     if (acceptResult(superclass)) {
/* 29 */       getStore().put(superclass, className);
/*    */     }
/*    */     
/* 32 */     for (String anInterface : getMetadataAdapter().getInterfacesNames(cls)) {
/* 33 */       if (acceptResult(anInterface))
/* 34 */         getStore().put(anInterface, className); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\reflections\scanners\SubTypesScanner.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */