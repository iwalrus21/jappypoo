/*     */ package javassist;
/*     */ 
/*     */ import javassist.bytecode.Bytecode;
/*     */ import javassist.bytecode.ClassFile;
/*     */ import javassist.bytecode.Descriptor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class CtNewWrappedConstructor
/*     */   extends CtNewWrappedMethod
/*     */ {
/*     */   private static final int PASS_NONE = 0;
/*     */   private static final int PASS_PARAMS = 2;
/*     */   
/*     */   public static CtConstructor wrapped(CtClass[] parameterTypes, CtClass[] exceptionTypes, int howToCallSuper, CtMethod body, CtMethod.ConstParameter constParam, CtClass declaring) throws CannotCompileException {
/*     */     try {
/*  36 */       CtConstructor cons = new CtConstructor(parameterTypes, declaring);
/*  37 */       cons.setExceptionTypes(exceptionTypes);
/*  38 */       Bytecode code = makeBody(declaring, declaring.getClassFile2(), howToCallSuper, body, parameterTypes, constParam);
/*     */ 
/*     */       
/*  41 */       cons.getMethodInfo2().setCodeAttribute(code.toCodeAttribute());
/*     */       
/*  43 */       return cons;
/*     */     }
/*  45 */     catch (NotFoundException e) {
/*  46 */       throw new CannotCompileException(e);
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
/*     */   protected static Bytecode makeBody(CtClass declaring, ClassFile classfile, int howToCallSuper, CtMethod wrappedBody, CtClass[] parameters, CtMethod.ConstParameter cparam) throws CannotCompileException {
/*  59 */     int stacksize, superclazz = classfile.getSuperclassId();
/*  60 */     Bytecode code = new Bytecode(classfile.getConstPool(), 0, 0);
/*  61 */     code.setMaxLocals(false, parameters, 0);
/*  62 */     code.addAload(0);
/*  63 */     if (howToCallSuper == 0) {
/*  64 */       stacksize = 1;
/*  65 */       code.addInvokespecial(superclazz, "<init>", "()V");
/*     */     }
/*  67 */     else if (howToCallSuper == 2) {
/*  68 */       stacksize = code.addLoadParameters(parameters, 1) + 1;
/*  69 */       code.addInvokespecial(superclazz, "<init>", 
/*  70 */           Descriptor.ofConstructor(parameters));
/*     */     } else {
/*     */       int stacksize2; String desc;
/*  73 */       stacksize = compileParameterList(code, parameters, 1);
/*     */       
/*  75 */       if (cparam == null) {
/*  76 */         stacksize2 = 2;
/*  77 */         desc = CtMethod.ConstParameter.defaultConstDescriptor();
/*     */       } else {
/*     */         
/*  80 */         stacksize2 = cparam.compile(code) + 2;
/*  81 */         desc = cparam.constDescriptor();
/*     */       } 
/*     */       
/*  84 */       if (stacksize < stacksize2) {
/*  85 */         stacksize = stacksize2;
/*     */       }
/*  87 */       code.addInvokespecial(superclazz, "<init>", desc);
/*     */     } 
/*     */     
/*  90 */     if (wrappedBody == null) {
/*  91 */       code.add(177);
/*     */     } else {
/*  93 */       int stacksize2 = makeBody0(declaring, classfile, wrappedBody, false, parameters, CtClass.voidType, cparam, code);
/*     */ 
/*     */       
/*  96 */       if (stacksize < stacksize2) {
/*  97 */         stacksize = stacksize2;
/*     */       }
/*     */     } 
/* 100 */     code.setMaxStack(stacksize);
/* 101 */     return code;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\CtNewWrappedConstructor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */