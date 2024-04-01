/*     */ package javassist;
/*     */ 
/*     */ import javassist.bytecode.Bytecode;
/*     */ import javassist.bytecode.ConstPool;
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
/*     */ 
/*     */ public class CtNewConstructor
/*     */ {
/*     */   public static final int PASS_NONE = 0;
/*     */   public static final int PASS_ARRAY = 1;
/*     */   public static final int PASS_PARAMS = 2;
/*     */   
/*     */   public static CtConstructor make(String src, CtClass declaring) throws CannotCompileException {
/*  68 */     Javac compiler = new Javac(declaring);
/*     */     try {
/*  70 */       CtMember obj = compiler.compile(src);
/*  71 */       if (obj instanceof CtConstructor)
/*     */       {
/*  73 */         return (CtConstructor)obj;
/*     */       }
/*     */     }
/*  76 */     catch (CompileError e) {
/*  77 */       throw new CannotCompileException(e);
/*     */     } 
/*     */     
/*  80 */     throw new CannotCompileException("not a constructor");
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
/*     */   public static CtConstructor make(CtClass[] parameters, CtClass[] exceptions, String body, CtClass declaring) throws CannotCompileException {
/*     */     try {
/* 101 */       CtConstructor cc = new CtConstructor(parameters, declaring);
/* 102 */       cc.setExceptionTypes(exceptions);
/* 103 */       cc.setBody(body);
/* 104 */       return cc;
/*     */     }
/* 106 */     catch (NotFoundException e) {
/* 107 */       throw new CannotCompileException(e);
/*     */     } 
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
/*     */   public static CtConstructor copy(CtConstructor c, CtClass declaring, ClassMap map) throws CannotCompileException {
/* 127 */     return new CtConstructor(c, declaring, map);
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
/*     */   public static CtConstructor defaultConstructor(CtClass declaring) throws CannotCompileException {
/* 139 */     CtConstructor cons = new CtConstructor((CtClass[])null, declaring);
/*     */     
/* 141 */     ConstPool cp = declaring.getClassFile2().getConstPool();
/* 142 */     Bytecode code = new Bytecode(cp, 1, 1);
/* 143 */     code.addAload(0);
/*     */     try {
/* 145 */       code.addInvokespecial(declaring.getSuperclass(), "<init>", "()V");
/*     */     
/*     */     }
/* 148 */     catch (NotFoundException e) {
/* 149 */       throw new CannotCompileException(e);
/*     */     } 
/*     */     
/* 152 */     code.add(177);
/*     */ 
/*     */     
/* 155 */     cons.getMethodInfo2().setCodeAttribute(code.toCodeAttribute());
/* 156 */     return cons;
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
/*     */   public static CtConstructor skeleton(CtClass[] parameters, CtClass[] exceptions, CtClass declaring) throws CannotCompileException {
/* 181 */     return make(parameters, exceptions, 0, null, null, declaring);
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
/*     */   public static CtConstructor make(CtClass[] parameters, CtClass[] exceptions, CtClass declaring) throws CannotCompileException {
/* 200 */     return make(parameters, exceptions, 2, null, null, declaring);
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
/*     */ 
/*     */   
/*     */   public static CtConstructor make(CtClass[] parameters, CtClass[] exceptions, int howto, CtMethod body, CtMethod.ConstParameter cparam, CtClass declaring) throws CannotCompileException {
/* 316 */     return CtNewWrappedConstructor.wrapped(parameters, exceptions, howto, body, cparam, declaring);
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\CtNewConstructor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */