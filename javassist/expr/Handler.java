/*     */ package javassist.expr;
/*     */ 
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.CtBehavior;
/*     */ import javassist.CtClass;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.bytecode.Bytecode;
/*     */ import javassist.bytecode.CodeAttribute;
/*     */ import javassist.bytecode.CodeIterator;
/*     */ import javassist.bytecode.ConstPool;
/*     */ import javassist.bytecode.ExceptionTable;
/*     */ import javassist.bytecode.MethodInfo;
/*     */ import javassist.compiler.CompileError;
/*     */ import javassist.compiler.Javac;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Handler
/*     */   extends Expr
/*     */ {
/*  27 */   private static String EXCEPTION_NAME = "$1";
/*     */ 
/*     */   
/*     */   private ExceptionTable etable;
/*     */   
/*     */   private int index;
/*     */ 
/*     */   
/*     */   protected Handler(ExceptionTable et, int nth, CodeIterator it, CtClass declaring, MethodInfo m) {
/*  36 */     super(et.handlerPc(nth), it, declaring, m);
/*  37 */     this.etable = et;
/*  38 */     this.index = nth;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CtBehavior where() {
/*  44 */     return super.where();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLineNumber() {
/*  52 */     return super.getLineNumber();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/*  61 */     return super.getFileName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtClass[] mayThrow() {
/*  68 */     return super.mayThrow();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtClass getType() throws NotFoundException {
/*  76 */     int type = this.etable.catchType(this.index);
/*  77 */     if (type == 0) {
/*  78 */       return null;
/*     */     }
/*  80 */     ConstPool cp = getConstPool();
/*  81 */     String name = cp.getClassInfo(type);
/*  82 */     return this.thisClass.getClassPool().getCtClass(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFinally() {
/*  90 */     return (this.etable.catchType(this.index) == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void replace(String statement) throws CannotCompileException {
/*  99 */     throw new RuntimeException("not implemented yet");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void insertBefore(String src) throws CannotCompileException {
/* 110 */     this.edited = true;
/*     */     
/* 112 */     ConstPool cp = getConstPool();
/* 113 */     CodeAttribute ca = this.iterator.get();
/* 114 */     Javac jv = new Javac(this.thisClass);
/* 115 */     Bytecode b = jv.getBytecode();
/* 116 */     b.setStackDepth(1);
/* 117 */     b.setMaxLocals(ca.getMaxLocals());
/*     */     
/*     */     try {
/* 120 */       CtClass type = getType();
/* 121 */       int var = jv.recordVariable(type, EXCEPTION_NAME);
/* 122 */       jv.recordReturnType(type, false);
/* 123 */       b.addAstore(var);
/* 124 */       jv.compileStmnt(src);
/* 125 */       b.addAload(var);
/*     */       
/* 127 */       int oldHandler = this.etable.handlerPc(this.index);
/* 128 */       b.addOpcode(167);
/* 129 */       b.addIndex(oldHandler - this.iterator.getCodeLength() - b
/* 130 */           .currentPc() + 1);
/*     */       
/* 132 */       this.maxStack = b.getMaxStack();
/* 133 */       this.maxLocals = b.getMaxLocals();
/*     */       
/* 135 */       int pos = this.iterator.append(b.get());
/* 136 */       this.iterator.append(b.getExceptionTable(), pos);
/* 137 */       this.etable.setHandlerPc(this.index, pos);
/*     */     }
/* 139 */     catch (NotFoundException e) {
/* 140 */       throw new CannotCompileException(e);
/*     */     }
/* 142 */     catch (CompileError e) {
/* 143 */       throw new CannotCompileException(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\expr\Handler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */