/*     */ package javassist;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import javassist.bytecode.AccessFlag;
/*     */ import javassist.bytecode.AttributeInfo;
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.Bytecode;
/*     */ import javassist.bytecode.ClassFile;
/*     */ import javassist.bytecode.MethodInfo;
/*     */ import javassist.bytecode.SyntheticAttribute;
/*     */ import javassist.compiler.JvstCodeGen;
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
/*     */ class CtNewWrappedMethod
/*     */ {
/*     */   private static final String addedWrappedMethod = "_added_m$";
/*     */   
/*     */   public static CtMethod wrapped(CtClass returnType, String mname, CtClass[] parameterTypes, CtClass[] exceptionTypes, CtMethod body, CtMethod.ConstParameter constParam, CtClass declaring) throws CannotCompileException {
/*  35 */     CtMethod mt = new CtMethod(returnType, mname, parameterTypes, declaring);
/*     */     
/*  37 */     mt.setModifiers(body.getModifiers());
/*     */     try {
/*  39 */       mt.setExceptionTypes(exceptionTypes);
/*     */     }
/*  41 */     catch (NotFoundException e) {
/*  42 */       throw new CannotCompileException(e);
/*     */     } 
/*     */     
/*  45 */     Bytecode code = makeBody(declaring, declaring.getClassFile2(), body, parameterTypes, returnType, constParam);
/*     */     
/*  47 */     MethodInfo minfo = mt.getMethodInfo2();
/*  48 */     minfo.setCodeAttribute(code.toCodeAttribute());
/*     */     
/*  50 */     return mt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Bytecode makeBody(CtClass clazz, ClassFile classfile, CtMethod wrappedBody, CtClass[] parameters, CtClass returnType, CtMethod.ConstParameter cparam) throws CannotCompileException {
/*  60 */     boolean isStatic = Modifier.isStatic(wrappedBody.getModifiers());
/*  61 */     Bytecode code = new Bytecode(classfile.getConstPool(), 0, 0);
/*  62 */     int stacksize = makeBody0(clazz, classfile, wrappedBody, isStatic, parameters, returnType, cparam, code);
/*     */     
/*  64 */     code.setMaxStack(stacksize);
/*  65 */     code.setMaxLocals(isStatic, parameters, 0);
/*  66 */     return code;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static int makeBody0(CtClass clazz, ClassFile classfile, CtMethod wrappedBody, boolean isStatic, CtClass[] parameters, CtClass returnType, CtMethod.ConstParameter cparam, Bytecode code) throws CannotCompileException {
/*     */     int stacksize2;
/*     */     String desc, bodyname;
/*  79 */     if (!(clazz instanceof CtClassType)) {
/*  80 */       throw new CannotCompileException("bad declaring class" + clazz
/*  81 */           .getName());
/*     */     }
/*  83 */     if (!isStatic) {
/*  84 */       code.addAload(0);
/*     */     }
/*  86 */     int stacksize = compileParameterList(code, parameters, isStatic ? 0 : 1);
/*     */ 
/*     */ 
/*     */     
/*  90 */     if (cparam == null) {
/*  91 */       stacksize2 = 0;
/*  92 */       desc = CtMethod.ConstParameter.defaultDescriptor();
/*     */     } else {
/*     */       
/*  95 */       stacksize2 = cparam.compile(code);
/*  96 */       desc = cparam.descriptor();
/*     */     } 
/*     */     
/*  99 */     checkSignature(wrappedBody, desc);
/*     */ 
/*     */     
/*     */     try {
/* 103 */       bodyname = addBodyMethod((CtClassType)clazz, classfile, wrappedBody);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 109 */     catch (BadBytecode e) {
/* 110 */       throw new CannotCompileException(e);
/*     */     } 
/*     */     
/* 113 */     if (isStatic) {
/* 114 */       code.addInvokestatic(Bytecode.THIS, bodyname, desc);
/*     */     } else {
/* 116 */       code.addInvokespecial(Bytecode.THIS, bodyname, desc);
/*     */     } 
/* 118 */     compileReturn(code, returnType);
/*     */     
/* 120 */     if (stacksize < stacksize2 + 2) {
/* 121 */       stacksize = stacksize2 + 2;
/*     */     }
/* 123 */     return stacksize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void checkSignature(CtMethod wrappedBody, String descriptor) throws CannotCompileException {
/* 130 */     if (!descriptor.equals(wrappedBody.getMethodInfo2().getDescriptor())) {
/* 131 */       throw new CannotCompileException("wrapped method with a bad signature: " + wrappedBody
/*     */           
/* 133 */           .getDeclaringClass().getName() + '.' + wrappedBody
/* 134 */           .getName());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String addBodyMethod(CtClassType clazz, ClassFile classfile, CtMethod src) throws BadBytecode, CannotCompileException {
/* 142 */     Hashtable<CtMethod, String> bodies = clazz.getHiddenMethods();
/* 143 */     String bodyname = (String)bodies.get(src);
/* 144 */     if (bodyname == null)
/*     */       while (true) {
/* 146 */         bodyname = "_added_m$" + clazz.getUniqueNumber();
/* 147 */         if (classfile.getMethod(bodyname) == null) {
/* 148 */           ClassMap map = new ClassMap();
/* 149 */           map.put(src.getDeclaringClass().getName(), clazz.getName());
/*     */           
/* 151 */           MethodInfo body = new MethodInfo(classfile.getConstPool(), bodyname, src.getMethodInfo2(), map);
/*     */           
/* 153 */           int acc = body.getAccessFlags();
/* 154 */           body.setAccessFlags(AccessFlag.setPrivate(acc));
/* 155 */           body.addAttribute((AttributeInfo)new SyntheticAttribute(classfile.getConstPool()));
/*     */           
/* 157 */           classfile.addMethod(body);
/* 158 */           bodies.put(src, bodyname);
/* 159 */           CtMember.Cache cache = clazz.hasMemberCache();
/* 160 */           if (cache != null)
/* 161 */             cache.addMethod(new CtMethod(body, clazz));  break;
/*     */         } 
/*     */       }  
/* 164 */     return bodyname;
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
/*     */   static int compileParameterList(Bytecode code, CtClass[] params, int regno) {
/* 176 */     return JvstCodeGen.compileParameterList(code, params, regno);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void compileReturn(Bytecode code, CtClass type) {
/* 183 */     if (type.isPrimitive()) {
/* 184 */       CtPrimitiveType pt = (CtPrimitiveType)type;
/* 185 */       if (pt != CtClass.voidType) {
/* 186 */         String wrapper = pt.getWrapperName();
/* 187 */         code.addCheckcast(wrapper);
/* 188 */         code.addInvokevirtual(wrapper, pt.getGetMethodName(), pt
/* 189 */             .getGetMethodDescriptor());
/*     */       } 
/*     */       
/* 192 */       code.addOpcode(pt.getReturnOp());
/*     */     } else {
/*     */       
/* 195 */       code.addCheckcast(type);
/* 196 */       code.addOpcode(176);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\CtNewWrappedMethod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */