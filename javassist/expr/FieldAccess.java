/*     */ package javassist.expr;
/*     */ 
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.CtBehavior;
/*     */ import javassist.CtClass;
/*     */ import javassist.CtField;
/*     */ import javassist.CtPrimitiveType;
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
/*     */ public class FieldAccess
/*     */   extends Expr
/*     */ {
/*     */   int opcode;
/*     */   
/*     */   protected FieldAccess(int pos, CodeIterator i, CtClass declaring, MethodInfo m, int op) {
/*  32 */     super(pos, i, declaring, m);
/*  33 */     this.opcode = op;
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
/*     */   public String getFileName() {
/*  58 */     return super.getFileName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStatic() {
/*  65 */     return isStatic(this.opcode);
/*     */   }
/*     */   
/*     */   static boolean isStatic(int c) {
/*  69 */     return (c == 178 || c == 179);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReader() {
/*  76 */     return (this.opcode == 180 || this.opcode == 178);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWriter() {
/*  83 */     return (this.opcode == 181 || this.opcode == 179);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CtClass getCtClass() throws NotFoundException {
/*  90 */     return this.thisClass.getClassPool().get(getClassName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  97 */     int index = this.iterator.u16bitAt(this.currentPos + 1);
/*  98 */     return getConstPool().getFieldrefClassName(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFieldName() {
/* 105 */     int index = this.iterator.u16bitAt(this.currentPos + 1);
/* 106 */     return getConstPool().getFieldrefName(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtField getField() throws NotFoundException {
/* 113 */     CtClass cc = getCtClass();
/* 114 */     int index = this.iterator.u16bitAt(this.currentPos + 1);
/* 115 */     ConstPool cp = getConstPool();
/* 116 */     return cc.getField(cp.getFieldrefName(index), cp.getFieldrefType(index));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtClass[] mayThrow() {
/* 126 */     return super.mayThrow();
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
/*     */   public String getSignature() {
/* 138 */     int index = this.iterator.u16bitAt(this.currentPos + 1);
/* 139 */     return getConstPool().getFieldrefType(index);
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
/* 153 */     this.thisClass.getClassFile();
/* 154 */     ConstPool constPool = getConstPool();
/* 155 */     int pos = this.currentPos;
/* 156 */     int index = this.iterator.u16bitAt(pos + 1);
/*     */     
/* 158 */     Javac jc = new Javac(this.thisClass);
/* 159 */     CodeAttribute ca = this.iterator.get();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 164 */     try { CtClass params[], retType, fieldType = Descriptor.toCtClass(constPool.getFieldrefType(index), this.thisClass
/* 165 */           .getClassPool());
/* 166 */       boolean read = isReader();
/* 167 */       if (read) {
/* 168 */         params = new CtClass[0];
/* 169 */         retType = fieldType;
/*     */       } else {
/*     */         
/* 172 */         params = new CtClass[1];
/* 173 */         params[0] = fieldType;
/* 174 */         retType = CtClass.voidType;
/*     */       } 
/*     */       
/* 177 */       int paramVar = ca.getMaxLocals();
/* 178 */       jc.recordParams(constPool.getFieldrefClassName(index), params, true, paramVar, 
/* 179 */           withinStatic());
/*     */ 
/*     */ 
/*     */       
/* 183 */       boolean included = checkResultValue(retType, statement);
/* 184 */       if (read) {
/* 185 */         included = true;
/*     */       }
/* 187 */       int retVar = jc.recordReturnType(retType, included);
/* 188 */       if (read) {
/* 189 */         jc.recordProceed(new ProceedForRead(retType, this.opcode, index, paramVar));
/*     */       }
/*     */       else {
/*     */         
/* 193 */         jc.recordType(fieldType);
/* 194 */         jc.recordProceed(new ProceedForWrite(params[0], this.opcode, index, paramVar));
/*     */       } 
/*     */ 
/*     */       
/* 198 */       Bytecode bytecode = jc.getBytecode();
/* 199 */       storeStack(params, isStatic(), paramVar, bytecode);
/* 200 */       jc.recordLocalVariables(ca, pos);
/*     */       
/* 202 */       if (included) {
/* 203 */         if (retType == CtClass.voidType) {
/* 204 */           bytecode.addOpcode(1);
/* 205 */           bytecode.addAstore(retVar);
/*     */         } else {
/*     */           
/* 208 */           bytecode.addConstZero(retType);
/* 209 */           bytecode.addStore(retVar, retType);
/*     */         } 
/*     */       }
/* 212 */       jc.compileStmnt(statement);
/* 213 */       if (read) {
/* 214 */         bytecode.addLoad(retVar, retType);
/*     */       }
/* 216 */       replace0(pos, bytecode, 3); }
/*     */     catch (CompileError e)
/* 218 */     { throw new CannotCompileException(e); }
/* 219 */     catch (NotFoundException e) { throw new CannotCompileException(e); }
/* 220 */     catch (BadBytecode e)
/* 221 */     { throw new CannotCompileException("broken method"); }
/*     */   
/*     */   }
/*     */   
/*     */   static class ProceedForRead
/*     */     implements ProceedHandler {
/*     */     CtClass fieldType;
/*     */     int opcode;
/*     */     int targetVar;
/*     */     int index;
/*     */     
/*     */     ProceedForRead(CtClass type, int op, int i, int var) {
/* 233 */       this.fieldType = type;
/* 234 */       this.targetVar = var;
/* 235 */       this.opcode = op;
/* 236 */       this.index = i;
/*     */     }
/*     */ 
/*     */     
/*     */     public void doit(JvstCodeGen gen, Bytecode bytecode, ASTList args) throws CompileError {
/*     */       int stack;
/* 242 */       if (args != null && !gen.isParamListName(args)) {
/* 243 */         throw new CompileError("$proceed() cannot take a parameter for field reading");
/*     */       }
/*     */ 
/*     */       
/* 247 */       if (FieldAccess.isStatic(this.opcode)) {
/* 248 */         stack = 0;
/*     */       } else {
/* 250 */         stack = -1;
/* 251 */         bytecode.addAload(this.targetVar);
/*     */       } 
/*     */       
/* 254 */       if (this.fieldType instanceof CtPrimitiveType) {
/* 255 */         stack += ((CtPrimitiveType)this.fieldType).getDataSize();
/*     */       } else {
/* 257 */         stack++;
/*     */       } 
/* 259 */       bytecode.add(this.opcode);
/* 260 */       bytecode.addIndex(this.index);
/* 261 */       bytecode.growStack(stack);
/* 262 */       gen.setType(this.fieldType);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void setReturnType(JvstTypeChecker c, ASTList args) throws CompileError {
/* 268 */       c.setType(this.fieldType);
/*     */     }
/*     */   }
/*     */   
/*     */   static class ProceedForWrite
/*     */     implements ProceedHandler
/*     */   {
/*     */     CtClass fieldType;
/*     */     int opcode;
/*     */     int targetVar;
/*     */     int index;
/*     */     
/*     */     ProceedForWrite(CtClass type, int op, int i, int var) {
/* 281 */       this.fieldType = type;
/* 282 */       this.targetVar = var;
/* 283 */       this.opcode = op;
/* 284 */       this.index = i;
/*     */     }
/*     */ 
/*     */     
/*     */     public void doit(JvstCodeGen gen, Bytecode bytecode, ASTList args) throws CompileError {
/*     */       int stack;
/* 290 */       if (gen.getMethodArgsLength(args) != 1) {
/* 291 */         throw new CompileError("$proceed() cannot take more than one parameter for field writing");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 296 */       if (FieldAccess.isStatic(this.opcode)) {
/* 297 */         stack = 0;
/*     */       } else {
/* 299 */         stack = -1;
/* 300 */         bytecode.addAload(this.targetVar);
/*     */       } 
/*     */       
/* 303 */       gen.atMethodArgs(args, new int[1], new int[1], new String[1]);
/* 304 */       gen.doNumCast(this.fieldType);
/* 305 */       if (this.fieldType instanceof CtPrimitiveType) {
/* 306 */         stack -= ((CtPrimitiveType)this.fieldType).getDataSize();
/*     */       } else {
/* 308 */         stack--;
/*     */       } 
/* 310 */       bytecode.add(this.opcode);
/* 311 */       bytecode.addIndex(this.index);
/* 312 */       bytecode.growStack(stack);
/* 313 */       gen.setType(CtClass.voidType);
/* 314 */       gen.addNullIfVoid();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void setReturnType(JvstTypeChecker c, ASTList args) throws CompileError {
/* 320 */       c.atMethodArgs(args, new int[1], new int[1], new String[1]);
/* 321 */       c.setType(CtClass.voidType);
/* 322 */       c.addNullIfVoid();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\expr\FieldAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */