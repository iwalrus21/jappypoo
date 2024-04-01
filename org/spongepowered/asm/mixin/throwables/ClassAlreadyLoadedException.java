/*    */ package org.spongepowered.asm.mixin.throwables;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ClassAlreadyLoadedException
/*    */   extends MixinException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public ClassAlreadyLoadedException(String message) {
/* 39 */     super(message);
/*    */   }
/*    */   
/*    */   public ClassAlreadyLoadedException(Throwable cause) {
/* 43 */     super(cause);
/*    */   }
/*    */   
/*    */   public ClassAlreadyLoadedException(String message, Throwable cause) {
/* 47 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\spongepowered\asm\mixin\throwables\ClassAlreadyLoadedException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */