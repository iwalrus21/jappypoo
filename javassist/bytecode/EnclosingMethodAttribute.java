/*     */ package javassist.bytecode;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
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
/*     */ public class EnclosingMethodAttribute
/*     */   extends AttributeInfo
/*     */ {
/*     */   public static final String tag = "EnclosingMethod";
/*     */   
/*     */   EnclosingMethodAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
/*  37 */     super(cp, n, in);
/*     */   }
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
/*     */   public EnclosingMethodAttribute(ConstPool cp, String className, String methodName, String methodDesc) {
/*  50 */     super(cp, "EnclosingMethod");
/*  51 */     int ci = cp.addClassInfo(className);
/*  52 */     int ni = cp.addNameAndTypeInfo(methodName, methodDesc);
/*  53 */     byte[] bvalue = new byte[4];
/*  54 */     bvalue[0] = (byte)(ci >>> 8);
/*  55 */     bvalue[1] = (byte)ci;
/*  56 */     bvalue[2] = (byte)(ni >>> 8);
/*  57 */     bvalue[3] = (byte)ni;
/*  58 */     set(bvalue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EnclosingMethodAttribute(ConstPool cp, String className) {
/*  69 */     super(cp, "EnclosingMethod");
/*  70 */     int ci = cp.addClassInfo(className);
/*  71 */     int ni = 0;
/*  72 */     byte[] bvalue = new byte[4];
/*  73 */     bvalue[0] = (byte)(ci >>> 8);
/*  74 */     bvalue[1] = (byte)ci;
/*  75 */     bvalue[2] = (byte)(ni >>> 8);
/*  76 */     bvalue[3] = (byte)ni;
/*  77 */     set(bvalue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int classIndex() {
/*  84 */     return ByteArray.readU16bit(get(), 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int methodIndex() {
/*  91 */     return ByteArray.readU16bit(get(), 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String className() {
/*  98 */     return getConstPool().getClassInfo(classIndex());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String methodName() {
/* 107 */     ConstPool cp = getConstPool();
/* 108 */     int mi = methodIndex();
/* 109 */     if (mi == 0) {
/* 110 */       return "<clinit>";
/*     */     }
/* 112 */     int ni = cp.getNameAndTypeName(mi);
/* 113 */     return cp.getUtf8Info(ni);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String methodDescriptor() {
/* 121 */     ConstPool cp = getConstPool();
/* 122 */     int mi = methodIndex();
/* 123 */     int ti = cp.getNameAndTypeDescriptor(mi);
/* 124 */     return cp.getUtf8Info(ti);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AttributeInfo copy(ConstPool newCp, Map classnames) {
/* 136 */     if (methodIndex() == 0) {
/* 137 */       return new EnclosingMethodAttribute(newCp, className());
/*     */     }
/* 139 */     return new EnclosingMethodAttribute(newCp, className(), 
/* 140 */         methodName(), methodDescriptor());
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\EnclosingMethodAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */