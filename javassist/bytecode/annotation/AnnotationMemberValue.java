/*    */ package javassist.bytecode.annotation;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.Method;
/*    */ import javassist.ClassPool;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AnnotationMemberValue
/*    */   extends MemberValue
/*    */ {
/*    */   Annotation value;
/*    */   
/*    */   public AnnotationMemberValue(ConstPool cp) {
/* 36 */     this(null, cp);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AnnotationMemberValue(Annotation a, ConstPool cp) {
/* 44 */     super('@', cp);
/* 45 */     this.value = a;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   Object getValue(ClassLoader cl, ClassPool cp, Method m) throws ClassNotFoundException {
/* 51 */     return AnnotationImpl.make(cl, getType(cl), cp, this.value);
/*    */   }
/*    */   
/*    */   Class getType(ClassLoader cl) throws ClassNotFoundException {
/* 55 */     if (this.value == null) {
/* 56 */       throw new ClassNotFoundException("no type specified");
/*    */     }
/* 58 */     return loadClass(cl, this.value.getTypeName());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Annotation getValue() {
/* 65 */     return this.value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setValue(Annotation newValue) {
/* 72 */     this.value = newValue;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 79 */     return this.value.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void write(AnnotationsWriter writer) throws IOException {
/* 86 */     writer.annotationValue();
/* 87 */     this.value.write(writer);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void accept(MemberValueVisitor visitor) {
/* 94 */     visitor.visitAnnotationMemberValue(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\annotation\AnnotationMemberValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */