/*     */ package javassist.compiler.ast;
/*     */ 
/*     */ import javassist.compiler.CompileError;
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
/*     */ public class IntConst
/*     */   extends ASTree
/*     */ {
/*     */   protected long value;
/*     */   protected int type;
/*     */   
/*     */   public IntConst(long v, int tokenId) {
/*  29 */     this.value = v; this.type = tokenId;
/*     */   } public long get() {
/*  31 */     return this.value;
/*     */   } public void set(long v) {
/*  33 */     this.value = v;
/*     */   }
/*     */   
/*     */   public int getType() {
/*  37 */     return this.type;
/*     */   } public String toString() {
/*  39 */     return Long.toString(this.value);
/*     */   }
/*     */   public void accept(Visitor v) throws CompileError {
/*  42 */     v.atIntConst(this);
/*     */   }
/*     */   
/*     */   public ASTree compute(int op, ASTree right) {
/*  46 */     if (right instanceof IntConst)
/*  47 */       return compute0(op, (IntConst)right); 
/*  48 */     if (right instanceof DoubleConst) {
/*  49 */       return compute0(op, (DoubleConst)right);
/*     */     }
/*  51 */     return null;
/*     */   } private IntConst compute0(int op, IntConst right) {
/*     */     int newType;
/*     */     long newValue;
/*  55 */     int type1 = this.type;
/*  56 */     int type2 = right.type;
/*     */     
/*  58 */     if (type1 == 403 || type2 == 403) {
/*  59 */       newType = 403;
/*  60 */     } else if (type1 == 401 && type2 == 401) {
/*     */       
/*  62 */       newType = 401;
/*     */     } else {
/*  64 */       newType = 402;
/*     */     } 
/*  66 */     long value1 = this.value;
/*  67 */     long value2 = right.value;
/*     */     
/*  69 */     switch (op) {
/*     */       case 43:
/*  71 */         newValue = value1 + value2;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 110 */         return new IntConst(newValue, newType);case 45: newValue = value1 - value2; return new IntConst(newValue, newType);case 42: newValue = value1 * value2; return new IntConst(newValue, newType);case 47: newValue = value1 / value2; return new IntConst(newValue, newType);case 37: newValue = value1 % value2; return new IntConst(newValue, newType);case 124: newValue = value1 | value2; return new IntConst(newValue, newType);case 94: newValue = value1 ^ value2; return new IntConst(newValue, newType);case 38: newValue = value1 & value2; return new IntConst(newValue, newType);case 364: newValue = this.value << (int)value2; newType = type1; return new IntConst(newValue, newType);case 366: newValue = this.value >> (int)value2; newType = type1; return new IntConst(newValue, newType);case 370: newValue = this.value >>> (int)value2; newType = type1; return new IntConst(newValue, newType);
/*     */     } 
/*     */     return null;
/*     */   } private DoubleConst compute0(int op, DoubleConst right) {
/* 114 */     double newValue, value1 = this.value;
/* 115 */     double value2 = right.value;
/*     */     
/* 117 */     switch (op) {
/*     */       case 43:
/* 119 */         newValue = value1 + value2;
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
/* 137 */         return new DoubleConst(newValue, right.type);case 45: newValue = value1 - value2; return new DoubleConst(newValue, right.type);case 42: newValue = value1 * value2; return new DoubleConst(newValue, right.type);case 47: newValue = value1 / value2; return new DoubleConst(newValue, right.type);case 37: newValue = value1 % value2; return new DoubleConst(newValue, right.type);
/*     */     } 
/*     */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\ast\IntConst.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */