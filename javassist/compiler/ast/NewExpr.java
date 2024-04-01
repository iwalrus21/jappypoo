/*    */ package javassist.compiler.ast;
/*    */ 
/*    */ import javassist.compiler.CompileError;
/*    */ import javassist.compiler.TokenId;
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
/*    */ public class NewExpr
/*    */   extends ASTList
/*    */   implements TokenId
/*    */ {
/*    */   protected boolean newArray;
/*    */   protected int arrayType;
/*    */   
/*    */   public NewExpr(ASTList className, ASTList args) {
/* 30 */     super(className, new ASTList(args));
/* 31 */     this.newArray = false;
/* 32 */     this.arrayType = 307;
/*    */   }
/*    */   
/*    */   public NewExpr(int type, ASTList arraySize, ArrayInit init) {
/* 36 */     super(null, new ASTList(arraySize));
/* 37 */     this.newArray = true;
/* 38 */     this.arrayType = type;
/* 39 */     if (init != null) {
/* 40 */       append(this, init);
/*    */     }
/*    */   }
/*    */   
/*    */   public static NewExpr makeObjectArray(ASTList className, ASTList arraySize, ArrayInit init) {
/* 45 */     NewExpr e = new NewExpr(className, arraySize);
/* 46 */     e.newArray = true;
/* 47 */     if (init != null) {
/* 48 */       append(e, init);
/*    */     }
/* 50 */     return e;
/*    */   }
/*    */   public boolean isArray() {
/* 53 */     return this.newArray;
/*    */   }
/*    */   
/*    */   public int getArrayType() {
/* 57 */     return this.arrayType;
/*    */   } public ASTList getClassName() {
/* 59 */     return (ASTList)getLeft();
/*    */   } public ASTList getArguments() {
/* 61 */     return (ASTList)getRight().getLeft();
/*    */   } public ASTList getArraySize() {
/* 63 */     return getArguments();
/*    */   }
/*    */   public ArrayInit getInitializer() {
/* 66 */     ASTree t = getRight().getRight();
/* 67 */     if (t == null) {
/* 68 */       return null;
/*    */     }
/* 70 */     return (ArrayInit)t.getLeft();
/*    */   }
/*    */   public void accept(Visitor v) throws CompileError {
/* 73 */     v.atNewExpr(this);
/*    */   }
/*    */   protected String getTag() {
/* 76 */     return this.newArray ? "new[]" : "new";
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\ast\NewExpr.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */