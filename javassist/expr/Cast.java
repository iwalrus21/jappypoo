/*     */ package javassist.expr;
/*     */ 
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtBehavior;
/*     */ import javassist.CtClass;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.Bytecode;
/*     */ import javassist.bytecode.CodeAttribute;
/*     */ import javassist.bytecode.CodeIterator;
/*     */ import javassist.bytecode.ConstPool;
/*     */ import javassist.bytecode.MethodInfo;
/*     */ import javassist.compiler.CompileError;
/*     */ import javassist.compiler.Javac;
/*     */ import javassist.compiler.JvstCodeGen;
/*     */ import javassist.compiler.JvstTypeChecker;
/*     */ import javassist.compiler.ProceedHandler;
/*     */ import javassist.compiler.ast.ASTList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Cast
/*     */   extends Expr
/*     */ {
/*     */   protected Cast(int pos, CodeIterator i, CtClass declaring, MethodInfo m) {
/*  32 */     super(pos, i, declaring, m);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtBehavior where() {
/*  39 */     return super.where();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLineNumber() {
/*  48 */     return super.getLineNumber();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/*  57 */     return super.getFileName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtClass getType() throws NotFoundException {
/*  65 */     ConstPool cp = getConstPool();
/*  66 */     int pos = this.currentPos;
/*  67 */     int index = this.iterator.u16bitAt(pos + 1);
/*  68 */     String name = cp.getClassInfo(index);
/*  69 */     return this.thisClass.getClassPool().getCtClass(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtClass[] mayThrow() {
/*  79 */     return super.mayThrow();
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
/*     */   public void replace(String statement) throws CannotCompileException {
/*  91 */     this.thisClass.getClassFile();
/*  92 */     ConstPool constPool = getConstPool();
/*  93 */     int pos = this.currentPos;
/*  94 */     int index = this.iterator.u16bitAt(pos + 1);
/*     */     
/*  96 */     Javac jc = new Javac(this.thisClass);
/*  97 */     ClassPool cp = this.thisClass.getClassPool();
/*  98 */     CodeAttribute ca = this.iterator.get();
/*     */ 
/*     */ 
/*     */     
/* 102 */     try { CtClass[] params = { cp.get("java.lang.Object") };
/* 103 */       CtClass retType = getType();
/*     */       
/* 105 */       int paramVar = ca.getMaxLocals();
/* 106 */       jc.recordParams("java.lang.Object", params, true, paramVar, 
/* 107 */           withinStatic());
/* 108 */       int retVar = jc.recordReturnType(retType, true);
/* 109 */       jc.recordProceed(new ProceedForCast(index, retType));
/*     */ 
/*     */ 
/*     */       
/* 113 */       checkResultValue(retType, statement);
/*     */       
/* 115 */       Bytecode bytecode = jc.getBytecode();
/* 116 */       storeStack(params, true, paramVar, bytecode);
/* 117 */       jc.recordLocalVariables(ca, pos);
/*     */       
/* 119 */       bytecode.addConstZero(retType);
/* 120 */       bytecode.addStore(retVar, retType);
/*     */       
/* 122 */       jc.compileStmnt(statement);
/* 123 */       bytecode.addLoad(retVar, retType);
/*     */       
/* 125 */       replace0(pos, bytecode, 3); }
/*     */     catch (CompileError e)
/* 127 */     { throw new CannotCompileException(e); }
/* 128 */     catch (NotFoundException e) { throw new CannotCompileException(e); }
/* 129 */     catch (BadBytecode e)
/* 130 */     { throw new CannotCompileException("broken method"); }
/*     */   
/*     */   }
/*     */   
/*     */   static class ProceedForCast
/*     */     implements ProceedHandler
/*     */   {
/*     */     int index;
/*     */     CtClass retType;
/*     */     
/*     */     ProceedForCast(int i, CtClass t) {
/* 141 */       this.index = i;
/* 142 */       this.retType = t;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void doit(JvstCodeGen gen, Bytecode bytecode, ASTList args) throws CompileError {
/* 148 */       if (gen.getMethodArgsLength(args) != 1) {
/* 149 */         throw new CompileError("$proceed() cannot take more than one parameter for cast");
/*     */       }
/*     */ 
/*     */       
/* 153 */       gen.atMethodArgs(args, new int[1], new int[1], new String[1]);
/* 154 */       bytecode.addOpcode(192);
/* 155 */       bytecode.addIndex(this.index);
/* 156 */       gen.setType(this.retType);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void setReturnType(JvstTypeChecker c, ASTList args) throws CompileError {
/* 162 */       c.atMethodArgs(args, new int[1], new int[1], new String[1]);
/* 163 */       c.setType(this.retType);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\expr\Cast.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */