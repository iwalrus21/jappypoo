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
/*     */ public class CharMemberValue
/*     */   extends MemberValue
/*     */ {
/*     */   int valueIndex;
/*     */   
/*     */   public CharMemberValue(int index, ConstPool cp) {
/*  40 */     super('C', cp);
/*  41 */     this.valueIndex = index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharMemberValue(char c, ConstPool cp) {
/*  50 */     super('C', cp);
/*  51 */     setValue(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharMemberValue(ConstPool cp) {
/*  58 */     super('C', cp);
/*  59 */     setValue(false);
/*     */   }
/*     */   
/*     */   Object getValue(ClassLoader cl, ClassPool cp, Method m) {
/*  63 */     return new Character(getValue());
/*     */   }
/*     */   
/*     */   Class getType(ClassLoader cl) {
/*  67 */     return char.class;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char getValue() {
/*  74 */     return (char)this.cp.getIntegerInfo(this.valueIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(char newValue) {
/*  81 */     this.valueIndex = this.cp.addIntegerInfo(newValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  88 */     return Character.toString(getValue());
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
/* 102 */     visitor.visitCharMemberValue(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\annotation\CharMemberValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */