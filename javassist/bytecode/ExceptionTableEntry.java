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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class ExceptionTableEntry
/*    */ {
/*    */   int startPc;
/*    */   int endPc;
/*    */   int handlerPc;
/*    */   int catchType;
/*    */   
/*    */   ExceptionTableEntry(int start, int end, int handle, int type) {
/* 32 */     this.startPc = start;
/* 33 */     this.endPc = end;
/* 34 */     this.handlerPc = handle;
/* 35 */     this.catchType = type;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\ExceptionTableEntry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */