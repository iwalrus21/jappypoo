/*     */ package javassist.bytecode.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Method;
/*     */ import javassist.ClassPool;
/*     */ import javassist.bytecode.ConstPool;
/*     */ import javassist.bytecode.Descriptor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EnumMemberValue
/*     */   extends MemberValue
/*     */ {
/*     */   int typeIndex;
/*     */   int valueIndex;
/*     */   
/*     */   public EnumMemberValue(int type, int value, ConstPool cp) {
/*  45 */     super('e', cp);
/*  46 */     this.typeIndex = type;
/*  47 */     this.valueIndex = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EnumMemberValue(ConstPool cp) {
/*  55 */     super('e', cp);
/*  56 */     this.typeIndex = this.valueIndex = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Object getValue(ClassLoader cl, ClassPool cp, Method m) throws ClassNotFoundException {
/*     */     try {
/*  63 */       return getType(cl).getField(getValue()).get(null);
/*     */     }
/*  65 */     catch (NoSuchFieldException e) {
/*  66 */       throw new ClassNotFoundException(getType() + "." + getValue());
/*     */     }
/*  68 */     catch (IllegalAccessException e) {
/*  69 */       throw new ClassNotFoundException(getType() + "." + getValue());
/*     */     } 
/*     */   }
/*     */   
/*     */   Class getType(ClassLoader cl) throws ClassNotFoundException {
/*  74 */     return loadClass(cl, getType());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType() {
/*  83 */     return Descriptor.toClassName(this.cp.getUtf8Info(this.typeIndex));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setType(String typename) {
/*  92 */     this.typeIndex = this.cp.addUtf8Info(Descriptor.of(typename));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getValue() {
/*  99 */     return this.cp.getUtf8Info(this.valueIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(String name) {
/* 106 */     this.valueIndex = this.cp.addUtf8Info(name);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 110 */     return getType() + "." + getValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(AnnotationsWriter writer) throws IOException {
/* 117 */     writer.enumConstValue(this.cp.getUtf8Info(this.typeIndex), getValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void accept(MemberValueVisitor visitor) {
/* 124 */     visitor.visitEnumMemberValue(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\annotation\EnumMemberValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */