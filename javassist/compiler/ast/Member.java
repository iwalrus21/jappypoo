/*    */ package javassist.compiler.ast;
/*    */ 
/*    */ import javassist.CtField;
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
/*    */ 
/*    */ 
/*    */ public class Member
/*    */   extends Symbol
/*    */ {
/*    */   private CtField field;
/*    */   
/*    */   public Member(String name) {
/* 31 */     super(name);
/* 32 */     this.field = null;
/*    */   }
/*    */   public void setField(CtField f) {
/* 35 */     this.field = f;
/*    */   } public CtField getField() {
/* 37 */     return this.field;
/*    */   } public void accept(Visitor v) throws CompileError {
/* 39 */     v.atMember(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\ast\Member.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */