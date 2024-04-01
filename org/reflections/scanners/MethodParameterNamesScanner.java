/*    */ package org.reflections.scanners;
/*    */ 
/*    */ import com.google.common.base.Joiner;
/*    */ import java.lang.reflect.Modifier;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javassist.bytecode.LocalVariableAttribute;
/*    */ import javassist.bytecode.MethodInfo;
/*    */ import org.reflections.adapters.MetadataAdapter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MethodParameterNamesScanner
/*    */   extends AbstractScanner
/*    */ {
/*    */   public void scan(Object cls) {
/* 19 */     MetadataAdapter md = getMetadataAdapter();
/*    */     
/* 21 */     for (Object method : md.getMethods(cls)) {
/* 22 */       String key = md.getMethodFullKey(cls, method);
/* 23 */       if (acceptResult(key)) {
/* 24 */         LocalVariableAttribute table = (LocalVariableAttribute)((MethodInfo)method).getCodeAttribute().getAttribute("LocalVariableTable");
/* 25 */         int length = table.tableLength();
/* 26 */         int i = Modifier.isStatic(((MethodInfo)method).getAccessFlags()) ? 0 : 1;
/* 27 */         if (i < length) {
/* 28 */           List<String> names = new ArrayList<>(length - i);
/* 29 */           for (; i < length; names.add(((MethodInfo)method).getConstPool().getUtf8Info(table.nameIndex(i++))));
/* 30 */           getStore().put(key, Joiner.on(", ").join(names));
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\reflections\scanners\MethodParameterNamesScanner.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */