/*     */ package javassist;
/*     */ 
/*     */ import javassist.compiler.CompileError;
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
/*     */ public class CannotCompileException
/*     */   extends Exception
/*     */ {
/*     */   private Throwable myCause;
/*     */   private String message;
/*     */   
/*     */   public Throwable getCause() {
/*  32 */     return (this.myCause == this) ? null : this.myCause;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Throwable initCause(Throwable cause) {
/*  40 */     this.myCause = cause;
/*  41 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getReason() {
/*  50 */     if (this.message != null) {
/*  51 */       return this.message;
/*     */     }
/*  53 */     return toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CannotCompileException(String msg) {
/*  62 */     super(msg);
/*  63 */     this.message = msg;
/*  64 */     initCause(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CannotCompileException(Throwable e) {
/*  74 */     super("by " + e.toString());
/*  75 */     this.message = null;
/*  76 */     initCause(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CannotCompileException(String msg, Throwable e) {
/*  87 */     this(msg);
/*  88 */     initCause(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CannotCompileException(NotFoundException e) {
/*  96 */     this("cannot find " + e.getMessage(), e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CannotCompileException(CompileError e) {
/* 103 */     this("[source error] " + e.getMessage(), (Throwable)e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CannotCompileException(ClassNotFoundException e, String name) {
/* 111 */     this("cannot find " + name, e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CannotCompileException(ClassFormatError e, String name) {
/* 118 */     this("invalid class format: " + name, e);
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\CannotCompileException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */