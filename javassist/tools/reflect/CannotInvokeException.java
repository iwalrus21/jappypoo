/*    */ package javassist.tools.reflect;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
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
/*    */ 
/*    */ public class CannotInvokeException
/*    */   extends RuntimeException
/*    */ {
/* 32 */   private Throwable err = null;
/*    */ 
/*    */ 
/*    */   
/*    */   public Throwable getReason() {
/* 37 */     return this.err;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public CannotInvokeException(String reason) {
/* 43 */     super(reason);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CannotInvokeException(InvocationTargetException e) {
/* 50 */     super("by " + e.getTargetException().toString());
/* 51 */     this.err = e.getTargetException();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CannotInvokeException(IllegalAccessException e) {
/* 58 */     super("by " + e.toString());
/* 59 */     this.err = e;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CannotInvokeException(ClassNotFoundException e) {
/* 66 */     super("by " + e.toString());
/* 67 */     this.err = e;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\tools\reflect\CannotInvokeException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */