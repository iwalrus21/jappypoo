/*    */ package javassist;
/*    */ 
/*    */ import javassist.bytecode.ClassFile;
/*    */ import javassist.bytecode.InnerClassesAttribute;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class CtNewNestedClass
/*    */   extends CtNewClass
/*    */ {
/*    */   CtNewNestedClass(String realName, ClassPool cp, boolean isInterface, CtClass superclass) {
/* 29 */     super(realName, cp, isInterface, superclass);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setModifiers(int mod) {
/* 36 */     mod &= 0xFFFFFFF7;
/* 37 */     super.setModifiers(mod);
/* 38 */     updateInnerEntry(mod, getName(), this, true);
/*    */   }
/*    */   
/*    */   private static void updateInnerEntry(int mod, String name, CtClass clazz, boolean outer) {
/* 42 */     ClassFile cf = clazz.getClassFile2();
/* 43 */     InnerClassesAttribute ica = (InnerClassesAttribute)cf.getAttribute("InnerClasses");
/*    */     
/* 45 */     if (ica == null) {
/*    */       return;
/*    */     }
/* 48 */     int n = ica.tableLength();
/* 49 */     for (int i = 0; i < n; i++) {
/* 50 */       if (name.equals(ica.innerClass(i))) {
/* 51 */         int acc = ica.accessFlags(i) & 0x8;
/* 52 */         ica.setAccessFlags(i, mod | acc);
/* 53 */         String outName = ica.outerClass(i);
/* 54 */         if (outName != null && outer)
/*    */           try {
/* 56 */             CtClass parent = clazz.getClassPool().get(outName);
/* 57 */             updateInnerEntry(mod, name, parent, false);
/*    */           }
/* 59 */           catch (NotFoundException e) {
/* 60 */             throw new RuntimeException("cannot find the declaring class: " + outName);
/*    */           }  
/*    */         break;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\CtNewNestedClass.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */