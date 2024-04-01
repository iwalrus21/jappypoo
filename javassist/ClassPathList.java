/*    */ package javassist;
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
/*    */ final class ClassPathList
/*    */ {
/*    */   ClassPathList next;
/*    */   ClassPath path;
/*    */   
/*    */   ClassPathList(ClassPath p, ClassPathList n) {
/* 30 */     this.next = n;
/* 31 */     this.path = p;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\ClassPathList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */