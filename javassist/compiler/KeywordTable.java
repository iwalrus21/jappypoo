/*    */ package javassist.compiler;
/*    */ 
/*    */ import java.util.HashMap;
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
/*    */ public final class KeywordTable
/*    */   extends HashMap
/*    */ {
/*    */   public int lookup(String name) {
/* 23 */     Object found = get(name);
/* 24 */     if (found == null) {
/* 25 */       return -1;
/*    */     }
/* 27 */     return ((Integer)found).intValue();
/*    */   }
/*    */   
/*    */   public void append(String name, int t) {
/* 31 */     put((K)name, (V)new Integer(t));
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\KeywordTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */