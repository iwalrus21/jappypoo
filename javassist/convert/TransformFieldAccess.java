/*    */ package javassist.convert;
/*    */ 
/*    */ import javassist.CtClass;
/*    */ import javassist.CtField;
/*    */ import javassist.Modifier;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class TransformFieldAccess
/*    */   extends Transformer
/*    */ {
/*    */   private String newClassname;
/*    */   private String newFieldname;
/*    */   private String fieldname;
/*    */   private CtClass fieldClass;
/*    */   private boolean isPrivate;
/*    */   private int newIndex;
/*    */   private ConstPool constPool;
/*    */   
/*    */   public TransformFieldAccess(Transformer next, CtField field, String newClassname, String newFieldname) {
/* 37 */     super(next);
/* 38 */     this.fieldClass = field.getDeclaringClass();
/* 39 */     this.fieldname = field.getName();
/* 40 */     this.isPrivate = Modifier.isPrivate(field.getModifiers());
/* 41 */     this.newClassname = newClassname;
/* 42 */     this.newFieldname = newFieldname;
/* 43 */     this.constPool = null;
/*    */   }
/*    */   
/*    */   public void initialize(ConstPool cp, CodeAttribute attr) {
/* 47 */     if (this.constPool != cp) {
/* 48 */       this.newIndex = 0;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int transform(CtClass clazz, int pos, CodeIterator iterator, ConstPool cp) {
/* 60 */     int c = iterator.byteAt(pos);
/* 61 */     if (c == 180 || c == 178 || c == 181 || c == 179) {
/*    */       
/* 63 */       int index = iterator.u16bitAt(pos + 1);
/*    */       
/* 65 */       String typedesc = TransformReadField.isField(clazz.getClassPool(), cp, this.fieldClass, this.fieldname, this.isPrivate, index);
/*    */       
/* 67 */       if (typedesc != null) {
/* 68 */         if (this.newIndex == 0) {
/* 69 */           int nt = cp.addNameAndTypeInfo(this.newFieldname, typedesc);
/*    */           
/* 71 */           this.newIndex = cp.addFieldrefInfo(cp
/* 72 */               .addClassInfo(this.newClassname), nt);
/* 73 */           this.constPool = cp;
/*    */         } 
/*    */         
/* 76 */         iterator.write16bit(this.newIndex, pos + 1);
/*    */       } 
/*    */     } 
/*    */     
/* 80 */     return pos;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\convert\TransformFieldAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */