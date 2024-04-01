/*    */ package javassist.bytecode.annotation;
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
/*    */ public class NoSuchClassError
/*    */   extends Error
/*    */ {
/*    */   private String className;
/*    */   
/*    */   public NoSuchClassError(String className, Error cause) {
/* 30 */     super(cause.toString(), cause);
/* 31 */     this.className = className;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getClassName() {
/* 38 */     return this.className;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\annotation\NoSuchClassError.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */