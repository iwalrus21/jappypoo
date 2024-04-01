/*    */ package org.reflections.scanners;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.reflections.adapters.MetadataAdapter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MethodParameterScanner
/*    */   extends AbstractScanner
/*    */ {
/*    */   public void scan(Object cls) {
/* 13 */     MetadataAdapter md = getMetadataAdapter();
/*    */     
/* 15 */     for (Object method : md.getMethods(cls)) {
/*    */       
/* 17 */       String signature = md.getParameterNames(method).toString();
/* 18 */       if (acceptResult(signature)) {
/* 19 */         getStore().put(signature, md.getMethodFullKey(cls, method));
/*    */       }
/*    */       
/* 22 */       String returnTypeName = md.getReturnTypeName(method);
/* 23 */       if (acceptResult(returnTypeName)) {
/* 24 */         getStore().put(returnTypeName, md.getMethodFullKey(cls, method));
/*    */       }
/*    */       
/* 27 */       List<String> parameterNames = md.getParameterNames(method);
/* 28 */       for (int i = 0; i < parameterNames.size(); i++) {
/* 29 */         for (Object paramAnnotation : md.getParameterAnnotationNames(method, i)) {
/* 30 */           if (acceptResult((String)paramAnnotation))
/* 31 */             getStore().put(paramAnnotation, md.getMethodFullKey(cls, method)); 
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\reflections\scanners\MethodParameterScanner.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */