/*     */ package javassist;
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
/*     */ public final class CtPrimitiveType
/*     */   extends CtClass
/*     */ {
/*     */   private char descriptor;
/*     */   private String wrapperName;
/*     */   private String getMethodName;
/*     */   private String mDescriptor;
/*     */   private int returnOp;
/*     */   private int arrayType;
/*     */   private int dataSize;
/*     */   
/*     */   CtPrimitiveType(String name, char desc, String wrapper, String methodName, String mDesc, int opcode, int atype, int size) {
/*  35 */     super(name);
/*  36 */     this.descriptor = desc;
/*  37 */     this.wrapperName = wrapper;
/*  38 */     this.getMethodName = methodName;
/*  39 */     this.mDescriptor = mDesc;
/*  40 */     this.returnOp = opcode;
/*  41 */     this.arrayType = atype;
/*  42 */     this.dataSize = size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPrimitive() {
/*  50 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getModifiers() {
/*  59 */     return 17;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char getDescriptor() {
/*  66 */     return this.descriptor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getWrapperName() {
/*  73 */     return this.wrapperName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getGetMethodName() {
/*  81 */     return this.getMethodName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getGetMethodDescriptor() {
/*  89 */     return this.mDescriptor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getReturnOp() {
/*  96 */     return this.returnOp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getArrayType() {
/* 104 */     return this.arrayType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDataSize() {
/* 111 */     return this.dataSize;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\CtPrimitiveType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */