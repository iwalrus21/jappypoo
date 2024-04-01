/*    */ package javassist.compiler.ast;
/*    */ 
/*    */ import javassist.compiler.CompileError;
/*    */ import javassist.compiler.MemberResolver;
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
/*    */ 
/*    */ public class CallExpr
/*    */   extends Expr
/*    */ {
/*    */   private MemberResolver.Method method;
/*    */   
/*    */   private CallExpr(ASTree _head, ASTList _tail) {
/* 30 */     super(67, _head, _tail);
/* 31 */     this.method = null;
/*    */   }
/*    */   
/*    */   public void setMethod(MemberResolver.Method m) {
/* 35 */     this.method = m;
/*    */   }
/*    */   
/*    */   public MemberResolver.Method getMethod() {
/* 39 */     return this.method;
/*    */   }
/*    */   
/*    */   public static CallExpr makeCall(ASTree target, ASTree args) {
/* 43 */     return new CallExpr(target, new ASTList(args));
/*    */   }
/*    */   public void accept(Visitor v) throws CompileError {
/* 46 */     v.atCallExpr(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\ast\CallExpr.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */