/*    */ package javassist.convert;
/*    */ 
/*    */ import javassist.ClassPool;
/*    */ import javassist.CtClass;
/*    */ import javassist.CtField;
/*    */ import javassist.Modifier;
/*    */ import javassist.NotFoundException;
/*    */ import javassist.bytecode.BadBytecode;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TransformReadField
/*    */   extends Transformer
/*    */ {
/*    */   protected String fieldname;
/*    */   protected CtClass fieldClass;
/*    */   protected boolean isPrivate;
/*    */   protected String methodClassname;
/*    */   protected String methodName;
/*    */   
/*    */   public TransformReadField(Transformer next, CtField field, String methodClassname, String methodName) {
/* 35 */     super(next);
/* 36 */     this.fieldClass = field.getDeclaringClass();
/* 37 */     this.fieldname = field.getName();
/* 38 */     this.methodClassname = methodClassname;
/* 39 */     this.methodName = methodName;
/* 40 */     this.isPrivate = Modifier.isPrivate(field.getModifiers());
/*    */   }
/*    */ 
/*    */   
/*    */   static String isField(ClassPool pool, ConstPool cp, CtClass fclass, String fname, boolean is_private, int index) {
/* 45 */     if (!cp.getFieldrefName(index).equals(fname)) {
/* 46 */       return null;
/*    */     }
/*    */     try {
/* 49 */       CtClass c = pool.get(cp.getFieldrefClassName(index));
/* 50 */       if (c == fclass || (!is_private && isFieldInSuper(c, fclass, fname))) {
/* 51 */         return cp.getFieldrefType(index);
/*    */       }
/* 53 */     } catch (NotFoundException notFoundException) {}
/* 54 */     return null;
/*    */   }
/*    */   
/*    */   static boolean isFieldInSuper(CtClass clazz, CtClass fclass, String fname) {
/* 58 */     if (!clazz.subclassOf(fclass)) {
/* 59 */       return false;
/*    */     }
/*    */     try {
/* 62 */       CtField f = clazz.getField(fname);
/* 63 */       return (f.getDeclaringClass() == fclass);
/*    */     }
/* 65 */     catch (NotFoundException notFoundException) {
/* 66 */       return false;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public int transform(CtClass tclazz, int pos, CodeIterator iterator, ConstPool cp) throws BadBytecode {
/* 72 */     int c = iterator.byteAt(pos);
/* 73 */     if (c == 180 || c == 178) {
/* 74 */       int index = iterator.u16bitAt(pos + 1);
/* 75 */       String typedesc = isField(tclazz.getClassPool(), cp, this.fieldClass, this.fieldname, this.isPrivate, index);
/*    */       
/* 77 */       if (typedesc != null) {
/* 78 */         if (c == 178) {
/* 79 */           iterator.move(pos);
/* 80 */           pos = iterator.insertGap(1);
/* 81 */           iterator.writeByte(1, pos);
/* 82 */           pos = iterator.next();
/*    */         } 
/*    */         
/* 85 */         String type = "(Ljava/lang/Object;)" + typedesc;
/* 86 */         int mi = cp.addClassInfo(this.methodClassname);
/* 87 */         int methodref = cp.addMethodrefInfo(mi, this.methodName, type);
/* 88 */         iterator.writeByte(184, pos);
/* 89 */         iterator.write16bit(methodref, pos + 1);
/* 90 */         return pos;
/*    */       } 
/*    */     } 
/*    */     
/* 94 */     return pos;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\convert\TransformReadField.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */