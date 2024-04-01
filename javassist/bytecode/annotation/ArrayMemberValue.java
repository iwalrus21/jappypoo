/*     */ package javassist.bytecode.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Array;
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
/*     */ public class ArrayMemberValue
/*     */   extends MemberValue
/*     */ {
/*     */   MemberValue type;
/*     */   MemberValue[] values;
/*     */   
/*     */   public ArrayMemberValue(ConstPool cp) {
/*  38 */     super('[', cp);
/*  39 */     this.type = null;
/*  40 */     this.values = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayMemberValue(MemberValue t, ConstPool cp) {
/*  49 */     super('[', cp);
/*  50 */     this.type = t;
/*  51 */     this.values = null;
/*     */   }
/*     */ 
/*     */   
/*     */   Object getValue(ClassLoader cl, ClassPool cp, Method method) throws ClassNotFoundException {
/*     */     Class<?> clazz;
/*  57 */     if (this.values == null) {
/*  58 */       throw new ClassNotFoundException("no array elements found: " + method
/*  59 */           .getName());
/*     */     }
/*  61 */     int size = this.values.length;
/*     */     
/*  63 */     if (this.type == null) {
/*  64 */       clazz = method.getReturnType().getComponentType();
/*  65 */       if (clazz == null || size > 0) {
/*  66 */         throw new ClassNotFoundException("broken array type: " + method
/*  67 */             .getName());
/*     */       }
/*     */     } else {
/*  70 */       clazz = this.type.getType(cl);
/*     */     } 
/*  72 */     Object a = Array.newInstance(clazz, size);
/*  73 */     for (int i = 0; i < size; i++) {
/*  74 */       Array.set(a, i, this.values[i].getValue(cl, cp, method));
/*     */     }
/*  76 */     return a;
/*     */   }
/*     */   
/*     */   Class getType(ClassLoader cl) throws ClassNotFoundException {
/*  80 */     if (this.type == null) {
/*  81 */       throw new ClassNotFoundException("no array type specified");
/*     */     }
/*  83 */     Object a = Array.newInstance(this.type.getType(cl), 0);
/*  84 */     return a.getClass();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MemberValue getType() {
/*  93 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MemberValue[] getValue() {
/* 100 */     return this.values;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(MemberValue[] elements) {
/* 107 */     this.values = elements;
/* 108 */     if (elements != null && elements.length > 0) {
/* 109 */       this.type = elements[0];
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 116 */     StringBuffer buf = new StringBuffer("{");
/* 117 */     if (this.values != null) {
/* 118 */       for (int i = 0; i < this.values.length; i++) {
/* 119 */         buf.append(this.values[i].toString());
/* 120 */         if (i + 1 < this.values.length) {
/* 121 */           buf.append(", ");
/*     */         }
/*     */       } 
/*     */     }
/* 125 */     buf.append("}");
/* 126 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(AnnotationsWriter writer) throws IOException {
/* 133 */     int num = (this.values == null) ? 0 : this.values.length;
/* 134 */     writer.arrayValue(num);
/* 135 */     for (int i = 0; i < num; i++) {
/* 136 */       this.values[i].write(writer);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void accept(MemberValueVisitor visitor) {
/* 143 */     visitor.visitArrayMemberValue(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\annotation\ArrayMemberValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */