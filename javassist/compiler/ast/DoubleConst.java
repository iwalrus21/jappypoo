/*    */ package javassist.compiler.ast;
/*    */ 
/*    */ import javassist.compiler.CompileError;
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
/*    */ public class DoubleConst
/*    */   extends ASTree
/*    */ {
/*    */   protected double value;
/*    */   protected int type;
/*    */   
/*    */   public DoubleConst(double v, int tokenId) {
/* 29 */     this.value = v; this.type = tokenId;
/*    */   } public double get() {
/* 31 */     return this.value;
/*    */   } public void set(double v) {
/* 33 */     this.value = v;
/*    */   }
/*    */   
/*    */   public int getType() {
/* 37 */     return this.type;
/*    */   } public String toString() {
/* 39 */     return Double.toString(this.value);
/*    */   }
/*    */   public void accept(Visitor v) throws CompileError {
/* 42 */     v.atDoubleConst(this);
/*    */   }
/*    */   
/*    */   public ASTree compute(int op, ASTree right) {
/* 46 */     if (right instanceof IntConst)
/* 47 */       return compute0(op, (IntConst)right); 
/* 48 */     if (right instanceof DoubleConst) {
/* 49 */       return compute0(op, (DoubleConst)right);
/*    */     }
/* 51 */     return null;
/*    */   }
/*    */   
/*    */   private DoubleConst compute0(int op, DoubleConst right) {
/*    */     int newType;
/* 56 */     if (this.type == 405 || right.type == 405) {
/*    */       
/* 58 */       newType = 405;
/*    */     } else {
/* 60 */       newType = 404;
/*    */     } 
/* 62 */     return compute(op, this.value, right.value, newType);
/*    */   }
/*    */   
/*    */   private DoubleConst compute0(int op, IntConst right) {
/* 66 */     return compute(op, this.value, right.value, this.type);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static DoubleConst compute(int op, double value1, double value2, int newType) {
/*    */     double newValue;
/* 73 */     switch (op) {
/*    */       case 43:
/* 75 */         newValue = value1 + value2;
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
/* 93 */         return new DoubleConst(newValue, newType);case 45: newValue = value1 - value2; return new DoubleConst(newValue, newType);case 42: newValue = value1 * value2; return new DoubleConst(newValue, newType);case 47: newValue = value1 / value2; return new DoubleConst(newValue, newType);case 37: newValue = value1 % value2; return new DoubleConst(newValue, newType);
/*    */     } 
/*    */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\ast\DoubleConst.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */