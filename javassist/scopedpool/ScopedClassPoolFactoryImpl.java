/*    */ package javassist.scopedpool;
/*    */ 
/*    */ import javassist.ClassPool;
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
/*    */ public class ScopedClassPoolFactoryImpl
/*    */   implements ScopedClassPoolFactory
/*    */ {
/*    */   public ScopedClassPool create(ClassLoader cl, ClassPool src, ScopedClassPoolRepository repository) {
/* 33 */     return new ScopedClassPool(cl, src, repository, false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ScopedClassPool create(ClassPool src, ScopedClassPoolRepository repository) {
/* 41 */     return new ScopedClassPool(null, src, repository, true);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\scopedpool\ScopedClassPoolFactoryImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */