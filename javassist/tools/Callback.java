/*     */ package javassist.tools;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.UUID;
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.CtBehavior;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Callback
/*     */ {
/*  52 */   public static HashMap callbacks = new HashMap<Object, Object>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String sourceCode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Callback(String src) {
/*  64 */     String uuid = UUID.randomUUID().toString();
/*  65 */     callbacks.put(uuid, this);
/*  66 */     this.sourceCode = "((javassist.tools.Callback) javassist.tools.Callback.callbacks.get(\"" + uuid + "\")).result(new Object[]{" + src + "});";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void result(Object[] paramArrayOfObject);
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  77 */     return sourceCode();
/*     */   }
/*     */   
/*     */   public String sourceCode() {
/*  81 */     return this.sourceCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void insertBefore(CtBehavior behavior, Callback callback) throws CannotCompileException {
/*  94 */     behavior.insertBefore(callback.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void insertAfter(CtBehavior behavior, Callback callback) throws CannotCompileException {
/* 110 */     behavior.insertAfter(callback.toString(), false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void insertAfter(CtBehavior behavior, Callback callback, boolean asFinally) throws CannotCompileException {
/* 131 */     behavior.insertAfter(callback.toString(), asFinally);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int insertAt(CtBehavior behavior, Callback callback, int lineNum) throws CannotCompileException {
/* 150 */     return behavior.insertAt(lineNum, callback.toString());
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\tools\Callback.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */