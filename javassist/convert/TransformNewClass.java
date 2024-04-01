/*    */ package javassist.convert;
/*    */ 
/*    */ import javassist.CannotCompileException;
/*    */ import javassist.CtClass;
/*    */ import javassist.bytecode.CodeAttribute;
/*    */ import javassist.bytecode.CodeIterator;
/*    */ import javassist.bytecode.ConstPool;
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
/*    */ public final class TransformNewClass
/*    */   extends Transformer
/*    */ {
/*    */   private int nested;
/*    */   private String classname;
/*    */   private String newClassName;
/*    */   private int newClassIndex;
/*    */   private int newMethodNTIndex;
/*    */   private int newMethodIndex;
/*    */   
/*    */   public TransformNewClass(Transformer next, String classname, String newClassName) {
/* 30 */     super(next);
/* 31 */     this.classname = classname;
/* 32 */     this.newClassName = newClassName;
/*    */   }
/*    */   
/*    */   public void initialize(ConstPool cp, CodeAttribute attr) {
/* 36 */     this.nested = 0;
/* 37 */     this.newClassIndex = this.newMethodNTIndex = this.newMethodIndex = 0;
/*    */   }
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
/*    */   public int transform(CtClass clazz, int pos, CodeIterator iterator, ConstPool cp) throws CannotCompileException {
/* 51 */     int c = iterator.byteAt(pos);
/* 52 */     if (c == 187) {
/* 53 */       int index = iterator.u16bitAt(pos + 1);
/* 54 */       if (cp.getClassInfo(index).equals(this.classname)) {
/* 55 */         if (iterator.byteAt(pos + 3) != 89) {
/* 56 */           throw new CannotCompileException("NEW followed by no DUP was found");
/*    */         }
/*    */         
/* 59 */         if (this.newClassIndex == 0) {
/* 60 */           this.newClassIndex = cp.addClassInfo(this.newClassName);
/*    */         }
/* 62 */         iterator.write16bit(this.newClassIndex, pos + 1);
/* 63 */         this.nested++;
/*    */       }
/*    */     
/* 66 */     } else if (c == 183) {
/* 67 */       int index = iterator.u16bitAt(pos + 1);
/* 68 */       int typedesc = cp.isConstructor(this.classname, index);
/* 69 */       if (typedesc != 0 && this.nested > 0) {
/* 70 */         int nt = cp.getMethodrefNameAndType(index);
/* 71 */         if (this.newMethodNTIndex != nt) {
/* 72 */           this.newMethodNTIndex = nt;
/* 73 */           this.newMethodIndex = cp.addMethodrefInfo(this.newClassIndex, nt);
/*    */         } 
/*    */         
/* 76 */         iterator.write16bit(this.newMethodIndex, pos + 1);
/* 77 */         this.nested--;
/*    */       } 
/*    */     } 
/*    */     
/* 81 */     return pos;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\convert\TransformNewClass.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */