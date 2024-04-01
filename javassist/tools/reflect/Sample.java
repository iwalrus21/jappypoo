/*    */ package javassist.tools.reflect;
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
/*    */ public class Sample
/*    */ {
/*    */   private Metaobject _metaobject;
/*    */   private static ClassMetaobject _classobject;
/*    */   
/*    */   public Object trap(Object[] args, int identifier) throws Throwable {
/* 28 */     Metaobject mobj = this._metaobject;
/* 29 */     if (mobj == null) {
/* 30 */       return ClassMetaobject.invoke(this, identifier, args);
/*    */     }
/* 32 */     return mobj.trapMethodcall(identifier, args);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static Object trapStatic(Object[] args, int identifier) throws Throwable {
/* 38 */     return _classobject.trapMethodcall(identifier, args);
/*    */   }
/*    */   
/*    */   public static Object trapRead(Object[] args, String name) {
/* 42 */     if (args[0] == null) {
/* 43 */       return _classobject.trapFieldRead(name);
/*    */     }
/* 45 */     return ((Metalevel)args[0])._getMetaobject().trapFieldRead(name);
/*    */   }
/*    */   
/*    */   public static Object trapWrite(Object[] args, String name) {
/* 49 */     Metalevel base = (Metalevel)args[0];
/* 50 */     if (base == null) {
/* 51 */       _classobject.trapFieldWrite(name, args[1]);
/*    */     } else {
/* 53 */       base._getMetaobject().trapFieldWrite(name, args[1]);
/*    */     } 
/* 55 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\tools\reflect\Sample.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */