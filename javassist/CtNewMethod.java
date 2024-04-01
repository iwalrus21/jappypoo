/*     */ package javassist;
/*     */ 
/*     */ import javassist.bytecode.Bytecode;
/*     */ import javassist.bytecode.ConstPool;
/*     */ import javassist.bytecode.ExceptionsAttribute;
/*     */ import javassist.bytecode.FieldInfo;
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
/*     */ public class CtNewMethod
/*     */ {
/*     */   public static CtMethod make(String src, CtClass declaring) throws CannotCompileException {
/*  45 */     return make(src, declaring, null, null);
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
/*     */   public static CtMethod make(String src, CtClass declaring, String delegateObj, String delegateMethod) throws CannotCompileException {
/*  69 */     Javac compiler = new Javac(declaring);
/*     */     try {
/*  71 */       if (delegateMethod != null) {
/*  72 */         compiler.recordProceed(delegateObj, delegateMethod);
/*     */       }
/*  74 */       CtMember obj = compiler.compile(src);
/*  75 */       if (obj instanceof CtMethod) {
/*  76 */         return (CtMethod)obj;
/*     */       }
/*  78 */     } catch (CompileError e) {
/*  79 */       throw new CannotCompileException(e);
/*     */     } 
/*     */     
/*  82 */     throw new CannotCompileException("not a method");
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
/*     */   public static CtMethod make(CtClass returnType, String mname, CtClass[] parameters, CtClass[] exceptions, String body, CtClass declaring) throws CannotCompileException {
/* 106 */     return make(1, returnType, mname, parameters, exceptions, body, declaring);
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
/*     */   public static CtMethod make(int modifiers, CtClass returnType, String mname, CtClass[] parameters, CtClass[] exceptions, String body, CtClass declaring) throws CannotCompileException {
/*     */     try {
/* 134 */       CtMethod cm = new CtMethod(returnType, mname, parameters, declaring);
/*     */       
/* 136 */       cm.setModifiers(modifiers);
/* 137 */       cm.setExceptionTypes(exceptions);
/* 138 */       cm.setBody(body);
/* 139 */       return cm;
/*     */     }
/* 141 */     catch (NotFoundException e) {
/* 142 */       throw new CannotCompileException(e);
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
/*     */   
/*     */   public static CtMethod copy(CtMethod src, CtClass declaring, ClassMap map) throws CannotCompileException {
/* 163 */     return new CtMethod(src, declaring, map);
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
/*     */   public static CtMethod copy(CtMethod src, String name, CtClass declaring, ClassMap map) throws CannotCompileException {
/* 185 */     CtMethod cm = new CtMethod(src, declaring, map);
/* 186 */     cm.setName(name);
/* 187 */     return cm;
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
/*     */   public static CtMethod abstractMethod(CtClass returnType, String mname, CtClass[] parameters, CtClass[] exceptions, CtClass declaring) throws NotFoundException {
/* 208 */     CtMethod cm = new CtMethod(returnType, mname, parameters, declaring);
/* 209 */     cm.setExceptionTypes(exceptions);
/* 210 */     return cm;
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
/*     */   public static CtMethod getter(String methodName, CtField field) throws CannotCompileException {
/* 225 */     FieldInfo finfo = field.getFieldInfo2();
/* 226 */     String fieldType = finfo.getDescriptor();
/* 227 */     String desc = "()" + fieldType;
/* 228 */     ConstPool cp = finfo.getConstPool();
/* 229 */     MethodInfo minfo = new MethodInfo(cp, methodName, desc);
/* 230 */     minfo.setAccessFlags(1);
/*     */     
/* 232 */     Bytecode code = new Bytecode(cp, 2, 1);
/*     */     try {
/* 234 */       String fieldName = finfo.getName();
/* 235 */       if ((finfo.getAccessFlags() & 0x8) == 0) {
/* 236 */         code.addAload(0);
/* 237 */         code.addGetfield(Bytecode.THIS, fieldName, fieldType);
/*     */       } else {
/*     */         
/* 240 */         code.addGetstatic(Bytecode.THIS, fieldName, fieldType);
/*     */       } 
/* 242 */       code.addReturn(field.getType());
/*     */     }
/* 244 */     catch (NotFoundException e) {
/* 245 */       throw new CannotCompileException(e);
/*     */     } 
/*     */     
/* 248 */     minfo.setCodeAttribute(code.toCodeAttribute());
/* 249 */     CtClass cc = field.getDeclaringClass();
/*     */     
/* 251 */     return new CtMethod(minfo, cc);
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
/*     */   public static CtMethod setter(String methodName, CtField field) throws CannotCompileException {
/* 268 */     FieldInfo finfo = field.getFieldInfo2();
/* 269 */     String fieldType = finfo.getDescriptor();
/* 270 */     String desc = "(" + fieldType + ")V";
/* 271 */     ConstPool cp = finfo.getConstPool();
/* 272 */     MethodInfo minfo = new MethodInfo(cp, methodName, desc);
/* 273 */     minfo.setAccessFlags(1);
/*     */     
/* 275 */     Bytecode code = new Bytecode(cp, 3, 3);
/*     */     try {
/* 277 */       String fieldName = finfo.getName();
/* 278 */       if ((finfo.getAccessFlags() & 0x8) == 0) {
/* 279 */         code.addAload(0);
/* 280 */         code.addLoad(1, field.getType());
/* 281 */         code.addPutfield(Bytecode.THIS, fieldName, fieldType);
/*     */       } else {
/*     */         
/* 284 */         code.addLoad(1, field.getType());
/* 285 */         code.addPutstatic(Bytecode.THIS, fieldName, fieldType);
/*     */       } 
/*     */       
/* 288 */       code.addReturn(null);
/*     */     }
/* 290 */     catch (NotFoundException e) {
/* 291 */       throw new CannotCompileException(e);
/*     */     } 
/*     */     
/* 294 */     minfo.setCodeAttribute(code.toCodeAttribute());
/* 295 */     CtClass cc = field.getDeclaringClass();
/*     */     
/* 297 */     return new CtMethod(minfo, cc);
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
/*     */   public static CtMethod delegator(CtMethod delegate, CtClass declaring) throws CannotCompileException {
/*     */     try {
/* 326 */       return delegator0(delegate, declaring);
/*     */     }
/* 328 */     catch (NotFoundException e) {
/* 329 */       throw new CannotCompileException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static CtMethod delegator0(CtMethod delegate, CtClass declaring) throws CannotCompileException, NotFoundException {
/*     */     int s;
/* 336 */     MethodInfo deleInfo = delegate.getMethodInfo2();
/* 337 */     String methodName = deleInfo.getName();
/* 338 */     String desc = deleInfo.getDescriptor();
/* 339 */     ConstPool cp = declaring.getClassFile2().getConstPool();
/* 340 */     MethodInfo minfo = new MethodInfo(cp, methodName, desc);
/* 341 */     minfo.setAccessFlags(deleInfo.getAccessFlags());
/*     */     
/* 343 */     ExceptionsAttribute eattr = deleInfo.getExceptionsAttribute();
/* 344 */     if (eattr != null) {
/* 345 */       minfo.setExceptionsAttribute((ExceptionsAttribute)eattr
/* 346 */           .copy(cp, null));
/*     */     }
/* 348 */     Bytecode code = new Bytecode(cp, 0, 0);
/* 349 */     boolean isStatic = Modifier.isStatic(delegate.getModifiers());
/* 350 */     CtClass deleClass = delegate.getDeclaringClass();
/* 351 */     CtClass[] params = delegate.getParameterTypes();
/*     */     
/* 353 */     if (isStatic) {
/* 354 */       s = code.addLoadParameters(params, 0);
/* 355 */       code.addInvokestatic(deleClass, methodName, desc);
/*     */     } else {
/*     */       
/* 358 */       code.addLoad(0, deleClass);
/* 359 */       s = code.addLoadParameters(params, 1);
/* 360 */       code.addInvokespecial(deleClass, methodName, desc);
/*     */     } 
/*     */     
/* 363 */     code.addReturn(delegate.getReturnType());
/* 364 */     code.setMaxLocals(++s);
/* 365 */     code.setMaxStack((s < 2) ? 2 : s);
/* 366 */     minfo.setCodeAttribute(code.toCodeAttribute());
/*     */     
/* 368 */     return new CtMethod(minfo, declaring);
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
/*     */   public static CtMethod wrapped(CtClass returnType, String mname, CtClass[] parameterTypes, CtClass[] exceptionTypes, CtMethod body, CtMethod.ConstParameter constParam, CtClass declaring) throws CannotCompileException {
/* 475 */     return CtNewWrappedMethod.wrapped(returnType, mname, parameterTypes, exceptionTypes, body, constParam, declaring);
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\CtNewMethod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */