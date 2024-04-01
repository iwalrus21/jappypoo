/*     */ package javassist.bytecode.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Method;
/*     */ import javassist.ClassPool;
/*     */ import javassist.bytecode.ConstPool;
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
/*     */ public class LongMemberValue
/*     */   extends MemberValue
/*     */ {
/*     */   int valueIndex;
/*     */   
/*     */   public LongMemberValue(int index, ConstPool cp) {
/*  40 */     super('J', cp);
/*  41 */     this.valueIndex = index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LongMemberValue(long j, ConstPool cp) {
/*  50 */     super('J', cp);
/*  51 */     setValue(j);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LongMemberValue(ConstPool cp) {
/*  58 */     super('J', cp);
/*  59 */     setValue(0L);
/*     */   }
/*     */   
/*     */   Object getValue(ClassLoader cl, ClassPool cp, Method m) {
/*  63 */     return new Long(getValue());
/*     */   }
/*     */   
/*     */   Class getType(ClassLoader cl) {
/*  67 */     return long.class;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getValue() {
/*  74 */     return this.cp.getLongInfo(this.valueIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(long newValue) {
/*  81 */     this.valueIndex = this.cp.addLongInfo(newValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  88 */     return Long.toString(getValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(AnnotationsWriter writer) throws IOException {
/*  95 */     writer.constValueIndex(getValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void accept(MemberValueVisitor visitor) {
/* 102 */     visitor.visitLongMemberValue(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\annotation\LongMemberValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */