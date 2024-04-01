/*    */ package javassist.expr;
/*    */ 
/*    */ import javassist.CtClass;
/*    */ import javassist.CtConstructor;
/*    */ import javassist.CtMethod;
/*    */ import javassist.NotFoundException;
/*    */ import javassist.bytecode.CodeIterator;
/*    */ import javassist.bytecode.MethodInfo;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ConstructorCall
/*    */   extends MethodCall
/*    */ {
/*    */   protected ConstructorCall(int pos, CodeIterator i, CtClass decl, MethodInfo m) {
/* 37 */     super(pos, i, decl, m);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMethodName() {
/* 44 */     return isSuper() ? "super" : "this";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CtMethod getMethod() throws NotFoundException {
/* 53 */     throw new NotFoundException("this is a constructor call.  Call getConstructor().");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CtConstructor getConstructor() throws NotFoundException {
/* 60 */     return getCtClass().getConstructor(getSignature());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isSuper() {
/* 68 */     return super.isSuper();
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\expr\ConstructorCall.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */