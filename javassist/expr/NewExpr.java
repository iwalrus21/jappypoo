/*     */ package javassist.expr;
/*     */ 
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtBehavior;
/*     */ import javassist.CtClass;
/*     */ import javassist.CtConstructor;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.Bytecode;
/*     */ import javassist.bytecode.CodeAttribute;
/*     */ import javassist.bytecode.CodeIterator;
/*     */ import javassist.bytecode.ConstPool;
/*     */ import javassist.bytecode.Descriptor;
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
/*     */ public class NewExpr
/*     */   extends Expr
/*     */ {
/*     */   String newTypeName;
/*     */   int newPos;
/*     */   
/*     */   protected NewExpr(int pos, CodeIterator i, CtClass declaring, MethodInfo m, String type, int np) {
/*  36 */     super(pos, i, declaring, m);
/*  37 */     this.newTypeName = type;
/*  38 */     this.newPos = np;
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
/*     */   public CtBehavior where() {
/*  59 */     return super.where();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLineNumber() {
/*  68 */     return super.getLineNumber();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/*  77 */     return super.getFileName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CtClass getCtClass() throws NotFoundException {
/*  84 */     return this.thisClass.getClassPool().get(this.newTypeName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  91 */     return this.newTypeName;
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
/*     */   public String getSignature() {
/* 105 */     ConstPool constPool = getConstPool();
/* 106 */     int methodIndex = this.iterator.u16bitAt(this.currentPos + 1);
/* 107 */     return constPool.getMethodrefType(methodIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtConstructor getConstructor() throws NotFoundException {
/* 114 */     ConstPool cp = getConstPool();
/* 115 */     int index = this.iterator.u16bitAt(this.currentPos + 1);
/* 116 */     String desc = cp.getMethodrefType(index);
/* 117 */     return getCtClass().getConstructor(desc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtClass[] mayThrow() {
/* 127 */     return super.mayThrow();
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
/*     */   private int canReplace() throws CannotCompileException {
/* 142 */     int op = this.iterator.byteAt(this.newPos + 3);
/* 143 */     if (op == 89)
/* 144 */       return (this.iterator.byteAt(this.newPos + 4) == 94 && this.iterator
/* 145 */         .byteAt(this.newPos + 5) == 88) ? 6 : 4; 
/* 146 */     if (op == 90 && this.iterator
/* 147 */       .byteAt(this.newPos + 4) == 95) {
/* 148 */       return 5;
/*     */     }
/* 150 */     return 3;
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
/*     */   public void replace(String statement) throws CannotCompileException {
/* 164 */     this.thisClass.getClassFile();
/*     */     
/* 166 */     int bytecodeSize = 3;
/* 167 */     int pos = this.newPos;
/*     */     
/* 169 */     int newIndex = this.iterator.u16bitAt(pos + 1);
/*     */ 
/*     */ 
/*     */     
/* 173 */     int codeSize = canReplace();
/* 174 */     int end = pos + codeSize;
/* 175 */     for (int i = pos; i < end; i++) {
/* 176 */       this.iterator.writeByte(0, i);
/*     */     }
/* 178 */     ConstPool constPool = getConstPool();
/* 179 */     pos = this.currentPos;
/* 180 */     int methodIndex = this.iterator.u16bitAt(pos + 1);
/*     */     
/* 182 */     String signature = constPool.getMethodrefType(methodIndex);
/*     */     
/* 184 */     Javac jc = new Javac(this.thisClass);
/* 185 */     ClassPool cp = this.thisClass.getClassPool();
/* 186 */     CodeAttribute ca = this.iterator.get();
/*     */     
/* 188 */     try { CtClass[] params = Descriptor.getParameterTypes(signature, cp);
/* 189 */       CtClass newType = cp.get(this.newTypeName);
/* 190 */       int paramVar = ca.getMaxLocals();
/* 191 */       jc.recordParams(this.newTypeName, params, true, paramVar, 
/* 192 */           withinStatic());
/* 193 */       int retVar = jc.recordReturnType(newType, true);
/* 194 */       jc.recordProceed(new ProceedForNew(newType, newIndex, methodIndex));
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 199 */       checkResultValue(newType, statement);
/*     */       
/* 201 */       Bytecode bytecode = jc.getBytecode();
/* 202 */       storeStack(params, true, paramVar, bytecode);
/* 203 */       jc.recordLocalVariables(ca, pos);
/*     */       
/* 205 */       bytecode.addConstZero(newType);
/* 206 */       bytecode.addStore(retVar, newType);
/*     */       
/* 208 */       jc.compileStmnt(statement);
/* 209 */       if (codeSize > 3) {
/* 210 */         bytecode.addAload(retVar);
/*     */       }
/* 212 */       replace0(pos, bytecode, 3); }
/*     */     catch (CompileError e)
/* 214 */     { throw new CannotCompileException(e); }
/* 215 */     catch (NotFoundException e) { throw new CannotCompileException(e); }
/* 216 */     catch (BadBytecode e)
/* 217 */     { throw new CannotCompileException("broken method"); }
/*     */   
/*     */   }
/*     */   
/*     */   static class ProceedForNew implements ProceedHandler { CtClass newType;
/*     */     int newIndex;
/*     */     int methodIndex;
/*     */     
/*     */     ProceedForNew(CtClass nt, int ni, int mi) {
/* 226 */       this.newType = nt;
/* 227 */       this.newIndex = ni;
/* 228 */       this.methodIndex = mi;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void doit(JvstCodeGen gen, Bytecode bytecode, ASTList args) throws CompileError {
/* 234 */       bytecode.addOpcode(187);
/* 235 */       bytecode.addIndex(this.newIndex);
/* 236 */       bytecode.addOpcode(89);
/* 237 */       gen.atMethodCallCore(this.newType, "<init>", args, false, true, -1, null);
/*     */       
/* 239 */       gen.setType(this.newType);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void setReturnType(JvstTypeChecker c, ASTList args) throws CompileError {
/* 245 */       c.atMethodCallCore(this.newType, "<init>", args);
/* 246 */       c.setType(this.newType);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\expr\NewExpr.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */