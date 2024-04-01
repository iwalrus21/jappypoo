/*    */ package org.reflections.scanners;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ public class FieldAnnotationsScanner
/*    */   extends AbstractScanner
/*    */ {
/*    */   public void scan(Object cls) {
/*  9 */     String className = getMetadataAdapter().getClassName(cls);
/* 10 */     List<Object> fields = getMetadataAdapter().getFields(cls);
/* 11 */     for (Object field : fields) {
/* 12 */       List<String> fieldAnnotations = getMetadataAdapter().getFieldAnnotationNames(field);
/* 13 */       for (String fieldAnnotation : fieldAnnotations) {
/*    */         
/* 15 */         if (acceptResult(fieldAnnotation)) {
/* 16 */           String fieldName = getMetadataAdapter().getFieldName(field);
/* 17 */           getStore().put(fieldAnnotation, String.format("%s.%s", new Object[] { className, fieldName }));
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\reflections\scanners\FieldAnnotationsScanner.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */