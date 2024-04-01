/*    */ package me.zero.alpine.type;
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
/*    */ public class Cancellable
/*    */ {
/*    */   private boolean cancelled;
/*    */   
/*    */   public final void cancel() {
/* 22 */     this.cancelled = true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final boolean isCancelled() {
/* 29 */     return this.cancelled;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\zero\alpine\type\Cancellable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */