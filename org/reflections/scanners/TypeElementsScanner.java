/*    */ package org.reflections.scanners;
/*    */ 
/*    */ import com.google.common.base.Joiner;
/*    */ 
/*    */ public class TypeElementsScanner
/*    */   extends AbstractScanner
/*    */ {
/*    */   private boolean includeFields = true;
/*    */   private boolean includeMethods = true;
/*    */   private boolean includeAnnotations = true;
/*    */   private boolean publicOnly = true;
/*    */   
/*    */   public void scan(Object cls) {
/* 14 */     String className = getMetadataAdapter().getClassName(cls);
/* 15 */     if (!acceptResult(className))
/*    */       return; 
/* 17 */     getStore().put(className, "");
/*    */     
/* 19 */     if (this.includeFields) {
/* 20 */       for (Object field : getMetadataAdapter().getFields(cls)) {
/* 21 */         String fieldName = getMetadataAdapter().getFieldName(field);
/* 22 */         getStore().put(className, fieldName);
/*    */       } 
/*    */     }
/*    */     
/* 26 */     if (this.includeMethods) {
/* 27 */       for (Object method : getMetadataAdapter().getMethods(cls)) {
/* 28 */         if (!this.publicOnly || getMetadataAdapter().isPublic(method)) {
/*    */           
/* 30 */           String methodKey = getMetadataAdapter().getMethodName(method) + "(" + Joiner.on(", ").join(getMetadataAdapter().getParameterNames(method)) + ")";
/* 31 */           getStore().put(className, methodKey);
/*    */         } 
/*    */       } 
/*    */     }
/*    */     
/* 36 */     if (this.includeAnnotations) {
/* 37 */       for (Object annotation : getMetadataAdapter().getClassAnnotationNames(cls)) {
/* 38 */         getStore().put(className, "@" + annotation);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public TypeElementsScanner includeFields() {
/* 44 */     return includeFields(true);
/* 45 */   } public TypeElementsScanner includeFields(boolean include) { this.includeFields = include; return this; }
/* 46 */   public TypeElementsScanner includeMethods() { return includeMethods(true); }
/* 47 */   public TypeElementsScanner includeMethods(boolean include) { this.includeMethods = include; return this; }
/* 48 */   public TypeElementsScanner includeAnnotations() { return includeAnnotations(true); }
/* 49 */   public TypeElementsScanner includeAnnotations(boolean include) { this.includeAnnotations = include; return this; }
/* 50 */   public TypeElementsScanner publicOnly(boolean only) { this.publicOnly = only; return this; } public TypeElementsScanner publicOnly() {
/* 51 */     return publicOnly(true);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\reflections\scanners\TypeElementsScanner.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */