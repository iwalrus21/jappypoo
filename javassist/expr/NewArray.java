/*     */ package javassist.expr;
/*     */ 
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.CtBehavior;
/*     */ import javassist.CtClass;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NewArray
/*     */   extends Expr
/*     */ {
/*     */   int opcode;
/*     */   
/*     */   protected NewArray(int pos, CodeIterator i, CtClass declaring, MethodInfo m, int op) {
/*  35 */     super(pos, i, declaring, m);
/*  36 */     this.opcode = op;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtBehavior where() {
/*  43 */     return super.where();
/*     */   }
/*     */ 
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
/*     */ 
/*     */ 
/*     */   
/*     */   public CtClass[] mayThrow() {
/*  71 */     return super.mayThrow();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtClass getComponentType() throws NotFoundException {
/*  81 */     if (this.opcode == 188) {
/*  82 */       int atype = this.iterator.byteAt(this.currentPos + 1);
/*  83 */       return getPrimitiveType(atype);
/*     */     } 
/*  85 */     if (this.opcode == 189 || this.opcode == 197) {
/*     */       
/*  87 */       int index = this.iterator.u16bitAt(this.currentPos + 1);
/*  88 */       String desc = getConstPool().getClassInfo(index);
/*  89 */       int dim = Descriptor.arrayDimension(desc);
/*  90 */       desc = Descriptor.toArrayComponent(desc, dim);
/*  91 */       return Descriptor.toCtClass(desc, this.thisClass.getClassPool());
/*     */     } 
/*     */     
/*  94 */     throw new RuntimeException("bad opcode: " + this.opcode);
/*     */   }
/*     */   
/*     */   CtClass getPrimitiveType(int atype) {
/*  98 */     switch (atype) {
/*     */       case 4:
/* 100 */         return CtClass.booleanType;
/*     */       case 5:
/* 102 */         return CtClass.charType;
/*     */       case 6:
/* 104 */         return CtClass.floatType;
/*     */       case 7:
/* 106 */         return CtClass.doubleType;
/*     */       case 8:
/* 108 */         return CtClass.byteType;
/*     */       case 9:
/* 110 */         return CtClass.shortType;
/*     */       case 10:
/* 112 */         return CtClass.intType;
/*     */       case 11:
/* 114 */         return CtClass.longType;
/*     */     } 
/* 116 */     throw new RuntimeException("bad atype: " + atype);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDimension() {
/* 124 */     if (this.opcode == 188)
/* 125 */       return 1; 
/* 126 */     if (this.opcode == 189 || this.opcode == 197) {
/*     */       
/* 128 */       int index = this.iterator.u16bitAt(this.currentPos + 1);
/* 129 */       String desc = getConstPool().getClassInfo(index);
/* 130 */       return Descriptor.arrayDimension(desc) + ((this.opcode == 189) ? 1 : 0);
/*     */     } 
/*     */ 
/*     */     
/* 134 */     throw new RuntimeException("bad opcode: " + this.opcode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCreatedDimensions() {
/* 143 */     if (this.opcode == 197) {
/* 144 */       return this.iterator.byteAt(this.currentPos + 3);
/*     */     }
/* 146 */     return 1;
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
/*     */     
/* 161 */     try { replace2(statement); }
/*     */     catch (CompileError e)
/* 163 */     { throw new CannotCompileException(e); }
/* 164 */     catch (NotFoundException e) { throw new CannotCompileException(e); }
/* 165 */     catch (BadBytecode e)
/* 166 */     { throw new CannotCompileException("broken method"); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   private void replace2(String statement) throws CompileError, NotFoundException, BadBytecode, CannotCompileException {
/*     */     int codeLength;
/*     */     String desc;
/* 174 */     this.thisClass.getClassFile();
/* 175 */     ConstPool constPool = getConstPool();
/* 176 */     int pos = this.currentPos;
/*     */ 
/*     */     
/* 179 */     int index = 0;
/* 180 */     int dim = 1;
/*     */     
/* 182 */     if (this.opcode == 188) {
/* 183 */       index = this.iterator.byteAt(this.currentPos + 1);
/* 184 */       CtPrimitiveType cpt = (CtPrimitiveType)getPrimitiveType(index);
/* 185 */       desc = "[" + cpt.getDescriptor();
/* 186 */       codeLength = 2;
/*     */     }
/* 188 */     else if (this.opcode == 189) {
/* 189 */       index = this.iterator.u16bitAt(pos + 1);
/* 190 */       desc = constPool.getClassInfo(index);
/* 191 */       if (desc.startsWith("[")) {
/* 192 */         desc = "[" + desc;
/*     */       } else {
/* 194 */         desc = "[L" + desc + ";";
/*     */       } 
/* 196 */       codeLength = 3;
/*     */     }
/* 198 */     else if (this.opcode == 197) {
/* 199 */       index = this.iterator.u16bitAt(this.currentPos + 1);
/* 200 */       desc = constPool.getClassInfo(index);
/* 201 */       dim = this.iterator.byteAt(this.currentPos + 3);
/* 202 */       codeLength = 4;
/*     */     } else {
/*     */       
/* 205 */       throw new RuntimeException("bad opcode: " + this.opcode);
/*     */     } 
/* 207 */     CtClass retType = Descriptor.toCtClass(desc, this.thisClass.getClassPool());
/*     */     
/* 209 */     Javac jc = new Javac(this.thisClass);
/* 210 */     CodeAttribute ca = this.iterator.get();
/*     */     
/* 212 */     CtClass[] params = new CtClass[dim];
/* 213 */     for (int i = 0; i < dim; i++) {
/* 214 */       params[i] = CtClass.intType;
/*     */     }
/* 216 */     int paramVar = ca.getMaxLocals();
/* 217 */     jc.recordParams("java.lang.Object", params, true, paramVar, 
/* 218 */         withinStatic());
/*     */ 
/*     */ 
/*     */     
/* 222 */     checkResultValue(retType, statement);
/* 223 */     int retVar = jc.recordReturnType(retType, true);
/* 224 */     jc.recordProceed(new ProceedForArray(retType, this.opcode, index, dim));
/*     */     
/* 226 */     Bytecode bytecode = jc.getBytecode();
/* 227 */     storeStack(params, true, paramVar, bytecode);
/* 228 */     jc.recordLocalVariables(ca, pos);
/*     */     
/* 230 */     bytecode.addOpcode(1);
/* 231 */     bytecode.addAstore(retVar);
/*     */     
/* 233 */     jc.compileStmnt(statement);
/* 234 */     bytecode.addAload(retVar);
/*     */     
/* 236 */     replace0(pos, bytecode, codeLength);
/*     */   }
/*     */   
/*     */   static class ProceedForArray
/*     */     implements ProceedHandler {
/*     */     CtClass arrayType;
/*     */     int opcode;
/*     */     int index;
/*     */     int dimension;
/*     */     
/*     */     ProceedForArray(CtClass type, int op, int i, int dim) {
/* 247 */       this.arrayType = type;
/* 248 */       this.opcode = op;
/* 249 */       this.index = i;
/* 250 */       this.dimension = dim;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void doit(JvstCodeGen gen, Bytecode bytecode, ASTList args) throws CompileError {
/* 256 */       int num = gen.getMethodArgsLength(args);
/* 257 */       if (num != this.dimension) {
/* 258 */         throw new CompileError("$proceed() with a wrong number of parameters");
/*     */       }
/*     */       
/* 261 */       gen.atMethodArgs(args, new int[num], new int[num], new String[num]);
/*     */       
/* 263 */       bytecode.addOpcode(this.opcode);
/* 264 */       if (this.opcode == 189) {
/* 265 */         bytecode.addIndex(this.index);
/* 266 */       } else if (this.opcode == 188) {
/* 267 */         bytecode.add(this.index);
/*     */       } else {
/* 269 */         bytecode.addIndex(this.index);
/* 270 */         bytecode.add(this.dimension);
/* 271 */         bytecode.growStack(1 - this.dimension);
/*     */       } 
/*     */       
/* 274 */       gen.setType(this.arrayType);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void setReturnType(JvstTypeChecker c, ASTList args) throws CompileError {
/* 280 */       c.setType(this.arrayType);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\expr\NewArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */