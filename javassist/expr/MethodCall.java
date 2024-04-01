/*     */ package javassist.expr;
/*     */ 
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtBehavior;
/*     */ import javassist.CtClass;
/*     */ import javassist.CtMethod;
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
/*     */ public class MethodCall
/*     */   extends Expr
/*     */ {
/*     */   protected MethodCall(int pos, CodeIterator i, CtClass declaring, MethodInfo m) {
/*  32 */     super(pos, i, declaring, m);
/*     */   }
/*     */   
/*     */   private int getNameAndType(ConstPool cp) {
/*  36 */     int pos = this.currentPos;
/*  37 */     int c = this.iterator.byteAt(pos);
/*  38 */     int index = this.iterator.u16bitAt(pos + 1);
/*     */     
/*  40 */     if (c == 185) {
/*  41 */       return cp.getInterfaceMethodrefNameAndType(index);
/*     */     }
/*  43 */     return cp.getMethodrefNameAndType(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtBehavior where() {
/*  50 */     return super.where();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLineNumber() {
/*  59 */     return super.getLineNumber();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/*  68 */     return super.getFileName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CtClass getCtClass() throws NotFoundException {
/*  76 */     return this.thisClass.getClassPool().get(getClassName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*     */     String cname;
/*  86 */     ConstPool cp = getConstPool();
/*  87 */     int pos = this.currentPos;
/*  88 */     int c = this.iterator.byteAt(pos);
/*  89 */     int index = this.iterator.u16bitAt(pos + 1);
/*     */     
/*  91 */     if (c == 185) {
/*  92 */       cname = cp.getInterfaceMethodrefClassName(index);
/*     */     } else {
/*  94 */       cname = cp.getMethodrefClassName(index);
/*     */     } 
/*  96 */     if (cname.charAt(0) == '[') {
/*  97 */       cname = Descriptor.toClassName(cname);
/*     */     }
/*  99 */     return cname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMethodName() {
/* 106 */     ConstPool cp = getConstPool();
/* 107 */     int nt = getNameAndType(cp);
/* 108 */     return cp.getUtf8Info(cp.getNameAndTypeName(nt));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtMethod getMethod() throws NotFoundException {
/* 115 */     return getCtClass().getMethod(getMethodName(), getSignature());
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
/* 129 */     ConstPool cp = getConstPool();
/* 130 */     int nt = getNameAndType(cp);
/* 131 */     return cp.getUtf8Info(cp.getNameAndTypeDescriptor(nt));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtClass[] mayThrow() {
/* 141 */     return super.mayThrow();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSuper() {
/* 149 */     return (this.iterator.byteAt(this.currentPos) == 183 && 
/* 150 */       !where().getDeclaringClass().getName().equals(getClassName()));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void replace(String statement) throws CannotCompileException {
/*     */     String classname, methodname, signature;
/*     */     int opcodeSize;
/* 180 */     this.thisClass.getClassFile();
/* 181 */     ConstPool constPool = getConstPool();
/* 182 */     int pos = this.currentPos;
/* 183 */     int index = this.iterator.u16bitAt(pos + 1);
/*     */ 
/*     */ 
/*     */     
/* 187 */     int c = this.iterator.byteAt(pos);
/* 188 */     if (c == 185) {
/* 189 */       opcodeSize = 5;
/* 190 */       classname = constPool.getInterfaceMethodrefClassName(index);
/* 191 */       methodname = constPool.getInterfaceMethodrefName(index);
/* 192 */       signature = constPool.getInterfaceMethodrefType(index);
/*     */     }
/* 194 */     else if (c == 184 || c == 183 || c == 182) {
/*     */       
/* 196 */       opcodeSize = 3;
/* 197 */       classname = constPool.getMethodrefClassName(index);
/* 198 */       methodname = constPool.getMethodrefName(index);
/* 199 */       signature = constPool.getMethodrefType(index);
/*     */     } else {
/*     */       
/* 202 */       throw new CannotCompileException("not method invocation");
/*     */     } 
/* 204 */     Javac jc = new Javac(this.thisClass);
/* 205 */     ClassPool cp = this.thisClass.getClassPool();
/* 206 */     CodeAttribute ca = this.iterator.get();
/*     */     
/* 208 */     try { CtClass[] params = Descriptor.getParameterTypes(signature, cp);
/* 209 */       CtClass retType = Descriptor.getReturnType(signature, cp);
/* 210 */       int paramVar = ca.getMaxLocals();
/* 211 */       jc.recordParams(classname, params, true, paramVar, 
/* 212 */           withinStatic());
/* 213 */       int retVar = jc.recordReturnType(retType, true);
/* 214 */       if (c == 184) {
/* 215 */         jc.recordStaticProceed(classname, methodname);
/* 216 */       } else if (c == 183) {
/* 217 */         jc.recordSpecialProceed("$0", classname, methodname, signature, index);
/*     */       } else {
/*     */         
/* 220 */         jc.recordProceed("$0", methodname);
/*     */       } 
/*     */ 
/*     */       
/* 224 */       checkResultValue(retType, statement);
/*     */       
/* 226 */       Bytecode bytecode = jc.getBytecode();
/* 227 */       storeStack(params, (c == 184), paramVar, bytecode);
/* 228 */       jc.recordLocalVariables(ca, pos);
/*     */       
/* 230 */       if (retType != CtClass.voidType) {
/* 231 */         bytecode.addConstZero(retType);
/* 232 */         bytecode.addStore(retVar, retType);
/*     */       } 
/*     */       
/* 235 */       jc.compileStmnt(statement);
/* 236 */       if (retType != CtClass.voidType) {
/* 237 */         bytecode.addLoad(retVar, retType);
/*     */       }
/* 239 */       replace0(pos, bytecode, opcodeSize); }
/*     */     catch (CompileError e)
/* 241 */     { throw new CannotCompileException(e); }
/* 242 */     catch (NotFoundException e) { throw new CannotCompileException(e); }
/* 243 */     catch (BadBytecode e)
/* 244 */     { throw new CannotCompileException("broken method"); }
/*     */   
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\expr\MethodCall.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */