/*    */ package javassist.runtime;
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
/*    */ public class Cflow
/*    */   extends ThreadLocal
/*    */ {
/*    */   private static class Depth
/*    */   {
/* 29 */     private int depth = 0;
/* 30 */     int get() { return this.depth; }
/* 31 */     void inc() { this.depth++; } void dec() {
/* 32 */       this.depth--;
/*    */     } }
/*    */   
/*    */   protected synchronized Object initialValue() {
/* 36 */     return new Depth();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void enter() {
/* 42 */     ((Depth)get()).inc();
/*    */   }
/*    */ 
/*    */   
/*    */   public void exit() {
/* 47 */     ((Depth)get()).dec();
/*    */   }
/*    */ 
/*    */   
/*    */   public int value() {
/* 52 */     return ((Depth)get()).get();
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\runtime\Cflow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */