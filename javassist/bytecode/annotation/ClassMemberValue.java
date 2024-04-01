/*     */ package javassist.bytecode.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Method;
/*     */ import javassist.ClassPool;
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.ConstPool;
/*     */ import javassist.bytecode.Descriptor;
/*     */ import javassist.bytecode.SignatureAttribute;
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
/*     */ public class ClassMemberValue
/*     */   extends MemberValue
/*     */ {
/*     */   int valueIndex;
/*     */   
/*     */   public ClassMemberValue(int index, ConstPool cp) {
/*  44 */     super('c', cp);
/*  45 */     this.valueIndex = index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassMemberValue(String className, ConstPool cp) {
/*  54 */     super('c', cp);
/*  55 */     setValue(className);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassMemberValue(ConstPool cp) {
/*  63 */     super('c', cp);
/*  64 */     setValue("java.lang.Class");
/*     */   }
/*     */ 
/*     */   
/*     */   Object getValue(ClassLoader cl, ClassPool cp, Method m) throws ClassNotFoundException {
/*  69 */     String classname = getValue();
/*  70 */     if (classname.equals("void"))
/*  71 */       return void.class; 
/*  72 */     if (classname.equals("int"))
/*  73 */       return int.class; 
/*  74 */     if (classname.equals("byte"))
/*  75 */       return byte.class; 
/*  76 */     if (classname.equals("long"))
/*  77 */       return long.class; 
/*  78 */     if (classname.equals("double"))
/*  79 */       return double.class; 
/*  80 */     if (classname.equals("float"))
/*  81 */       return float.class; 
/*  82 */     if (classname.equals("char"))
/*  83 */       return char.class; 
/*  84 */     if (classname.equals("short"))
/*  85 */       return short.class; 
/*  86 */     if (classname.equals("boolean")) {
/*  87 */       return boolean.class;
/*     */     }
/*  89 */     return loadClass(cl, classname);
/*     */   }
/*     */   
/*     */   Class getType(ClassLoader cl) throws ClassNotFoundException {
/*  93 */     return loadClass(cl, "java.lang.Class");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getValue() {
/* 102 */     String v = this.cp.getUtf8Info(this.valueIndex);
/*     */     try {
/* 104 */       return SignatureAttribute.toTypeSignature(v).jvmTypeName();
/* 105 */     } catch (BadBytecode e) {
/* 106 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(String newClassName) {
/* 116 */     String setTo = Descriptor.of(newClassName);
/* 117 */     this.valueIndex = this.cp.addUtf8Info(setTo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 124 */     return getValue().replace('$', '.') + ".class";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(AnnotationsWriter writer) throws IOException {
/* 131 */     writer.classInfoIndex(this.cp.getUtf8Info(this.valueIndex));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void accept(MemberValueVisitor visitor) {
/* 138 */     visitor.visitClassMemberValue(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\annotation\ClassMemberValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */