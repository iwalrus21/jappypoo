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
/*     */ 
/*     */ public class Instanceof
/*     */   extends Expr
/*     */ {
/*     */   protected Instanceof(int pos, CodeIterator i, CtClass declaring, MethodInfo m) {
/*  33 */     super(pos, i, declaring, m);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtBehavior where() {
/*  40 */     return super.where();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLineNumber() {
/*  49 */     return super.getLineNumber();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/*  59 */     return super.getFileName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtClass getType() throws NotFoundException {
/*  68 */     ConstPool cp = getConstPool();
/*  69 */     int pos = this.currentPos;
/*  70 */     int index = this.iterator.u16bitAt(pos + 1);
/*  71 */     String name = cp.getClassInfo(index);
/*  72 */     return this.thisClass.getClassPool().getCtClass(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtClass[] mayThrow() {
/*  82 */     return super.mayThrow();
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
/*  94 */     this.thisClass.getClassFile();
/*  95 */     ConstPool constPool = getConstPool();
/*  96 */     int pos = this.currentPos;
/*  97 */     int index = this.iterator.u16bitAt(pos + 1);
/*     */     
/*  99 */     Javac jc = new Javac(this.thisClass);
/* 100 */     ClassPool cp = this.thisClass.getClassPool();
/* 101 */     CodeAttribute ca = this.iterator.get();
/*     */ 
/*     */ 
/*     */     
/* 105 */     try { CtClass[] params = { cp.get("java.lang.Object") };
/* 106 */       CtClass retType = CtClass.booleanType;
/*     */       
/* 108 */       int paramVar = ca.getMaxLocals();
/* 109 */       jc.recordParams("java.lang.Object", params, true, paramVar, 
/* 110 */           withinStatic());
/* 111 */       int retVar = jc.recordReturnType(retType, true);
/* 112 */       jc.recordProceed(new ProceedForInstanceof(index));
/*     */ 
/*     */       
/* 115 */       jc.recordType(getType());
/*     */ 
/*     */ 
/*     */       
/* 119 */       checkResultValue(retType, statement);
/*     */       
/* 121 */       Bytecode bytecode = jc.getBytecode();
/* 122 */       storeStack(params, true, paramVar, bytecode);
/* 123 */       jc.recordLocalVariables(ca, pos);
/*     */       
/* 125 */       bytecode.addConstZero(retType);
/* 126 */       bytecode.addStore(retVar, retType);
/*     */       
/* 128 */       jc.compileStmnt(statement);
/* 129 */       bytecode.addLoad(retVar, retType);
/*     */       
/* 131 */       replace0(pos, bytecode, 3); }
/*     */     catch (CompileError e)
/* 133 */     { throw new CannotCompileException(e); }
/* 134 */     catch (NotFoundException e) { throw new CannotCompileException(e); }
/* 135 */     catch (BadBytecode e)
/* 136 */     { throw new CannotCompileException("broken method"); }
/*     */   
/*     */   }
/*     */   
/*     */   static class ProceedForInstanceof
/*     */     implements ProceedHandler
/*     */   {
/*     */     int index;
/*     */     
/*     */     ProceedForInstanceof(int i) {
/* 146 */       this.index = i;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void doit(JvstCodeGen gen, Bytecode bytecode, ASTList args) throws CompileError {
/* 152 */       if (gen.getMethodArgsLength(args) != 1) {
/* 153 */         throw new CompileError("$proceed() cannot take more than one parameter for instanceof");
/*     */       }
/*     */ 
/*     */       
/* 157 */       gen.atMethodArgs(args, new int[1], new int[1], new String[1]);
/* 158 */       bytecode.addOpcode(193);
/* 159 */       bytecode.addIndex(this.index);
/* 160 */       gen.setType(CtClass.booleanType);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void setReturnType(JvstTypeChecker c, ASTList args) throws CompileError {
/* 166 */       c.atMethodArgs(args, new int[1], new int[1], new String[1]);
/* 167 */       c.setType(CtClass.booleanType);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\expr\Instanceof.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */