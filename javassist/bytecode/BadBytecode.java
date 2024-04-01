/*    */ package javassist.bytecode;
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
/*    */ public class BadBytecode
/*    */   extends Exception
/*    */ {
/*    */   public BadBytecode(int opcode) {
/* 24 */     super("bytecode " + opcode);
/*    */   }
/*    */   
/*    */   public BadBytecode(String msg) {
/* 28 */     super(msg);
/*    */   }
/*    */   
/*    */   public BadBytecode(String msg, Throwable cause) {
/* 32 */     super(msg, cause);
/*    */   }
/*    */   
/*    */   public BadBytecode(MethodInfo minfo, Throwable cause) {
/* 36 */     super(minfo.toString() + " in " + minfo
/* 37 */         .getConstPool().getClassName() + ": " + cause
/* 38 */         .getMessage(), cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\BadBytecode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */