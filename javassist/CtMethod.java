/*     */ package javassist;
/*     */ 
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.Bytecode;
/*     */ import javassist.bytecode.CodeAttribute;
/*     */ import javassist.bytecode.CodeIterator;
/*     */ import javassist.bytecode.ConstPool;
/*     */ import javassist.bytecode.Descriptor;
/*     */ import javassist.bytecode.MethodInfo;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CtMethod
/*     */   extends CtBehavior
/*     */ {
/*     */   protected String cachedStringRep;
/*     */   
/*     */   CtMethod(MethodInfo minfo, CtClass declaring) {
/*  38 */     super(declaring, minfo);
/*  39 */     this.cachedStringRep = null;
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
/*     */   public CtMethod(CtClass returnType, String mname, CtClass[] parameters, CtClass declaring) {
/*  55 */     this((MethodInfo)null, declaring);
/*  56 */     ConstPool cp = declaring.getClassFile2().getConstPool();
/*  57 */     String desc = Descriptor.ofMethod(returnType, parameters);
/*  58 */     this.methodInfo = new MethodInfo(cp, mname, desc);
/*  59 */     setModifiers(1025);
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
/*     */   public CtMethod(CtMethod src, CtClass declaring, ClassMap map) throws CannotCompileException {
/* 115 */     this((MethodInfo)null, declaring);
/* 116 */     copy(src, false, map);
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
/*     */   public static CtMethod make(String src, CtClass declaring) throws CannotCompileException {
/* 132 */     return CtNewMethod.make(src, declaring);
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
/*     */   public static CtMethod make(MethodInfo minfo, CtClass declaring) throws CannotCompileException {
/* 147 */     if (declaring.getClassFile2().getConstPool() != minfo.getConstPool()) {
/* 148 */       throw new CannotCompileException("bad declaring class");
/*     */     }
/* 150 */     return new CtMethod(minfo, declaring);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 159 */     return getStringRep().hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void nameReplaced() {
/* 167 */     this.cachedStringRep = null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   final String getStringRep() {
/* 173 */     if (this.cachedStringRep == null) {
/* 174 */       this
/* 175 */         .cachedStringRep = this.methodInfo.getName() + Descriptor.getParamDescriptor(this.methodInfo.getDescriptor());
/*     */     }
/* 177 */     return this.cachedStringRep;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 185 */     return (obj != null && obj instanceof CtMethod && ((CtMethod)obj)
/* 186 */       .getStringRep().equals(getStringRep()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLongName() {
/* 196 */     return getDeclaringClass().getName() + "." + 
/* 197 */       getName() + Descriptor.toString(getSignature());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 204 */     return this.methodInfo.getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String newname) {
/* 211 */     this.declaringClass.checkModify();
/* 212 */     this.methodInfo.setName(newname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtClass getReturnType() throws NotFoundException {
/* 219 */     return getReturnType0();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 227 */     CodeAttribute ca = getMethodInfo2().getCodeAttribute();
/* 228 */     if (ca == null) {
/* 229 */       return ((getModifiers() & 0x400) != 0);
/*     */     }
/* 231 */     CodeIterator it = ca.iterator();
/*     */     try {
/* 233 */       return (it.hasNext() && it.byteAt(it.next()) == 177 && 
/* 234 */         !it.hasNext());
/*     */     }
/* 236 */     catch (BadBytecode badBytecode) {
/* 237 */       return false;
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
/*     */   public void setBody(CtMethod src, ClassMap map) throws CannotCompileException {
/* 257 */     setBody0(src.declaringClass, src.methodInfo, this.declaringClass, this.methodInfo, map);
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
/*     */   public void setWrappedBody(CtMethod mbody, ConstParameter constParam) throws CannotCompileException {
/*     */     CtClass params[], retType;
/* 275 */     this.declaringClass.checkModify();
/*     */     
/* 277 */     CtClass clazz = getDeclaringClass();
/*     */ 
/*     */     
/*     */     try {
/* 281 */       params = getParameterTypes();
/* 282 */       retType = getReturnType();
/*     */     }
/* 284 */     catch (NotFoundException e) {
/* 285 */       throw new CannotCompileException(e);
/*     */     } 
/*     */     
/* 288 */     Bytecode code = CtNewWrappedMethod.makeBody(clazz, clazz
/* 289 */         .getClassFile2(), mbody, params, retType, constParam);
/*     */ 
/*     */ 
/*     */     
/* 293 */     CodeAttribute cattr = code.toCodeAttribute();
/* 294 */     this.methodInfo.setCodeAttribute(cattr);
/* 295 */     this.methodInfo.setAccessFlags(this.methodInfo.getAccessFlags() & 0xFFFFFBFF);
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
/*     */   public static class ConstParameter
/*     */   {
/*     */     public static ConstParameter integer(int i) {
/* 318 */       return new CtMethod.IntConstParameter(i);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static ConstParameter integer(long i) {
/* 327 */       return new CtMethod.LongConstParameter(i);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static ConstParameter string(String s) {
/* 336 */       return new CtMethod.StringConstParameter(s);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int compile(Bytecode code) throws CannotCompileException {
/* 345 */       return 0;
/*     */     }
/*     */     
/*     */     String descriptor() {
/* 349 */       return defaultDescriptor();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static String defaultDescriptor() {
/* 356 */       return "([Ljava/lang/Object;)Ljava/lang/Object;";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     String constDescriptor() {
/* 365 */       return defaultConstDescriptor();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static String defaultConstDescriptor() {
/* 372 */       return "([Ljava/lang/Object;)V";
/*     */     }
/*     */   }
/*     */   
/*     */   static class IntConstParameter extends ConstParameter {
/*     */     int param;
/*     */     
/*     */     IntConstParameter(int i) {
/* 380 */       this.param = i;
/*     */     }
/*     */     
/*     */     int compile(Bytecode code) throws CannotCompileException {
/* 384 */       code.addIconst(this.param);
/* 385 */       return 1;
/*     */     }
/*     */     
/*     */     String descriptor() {
/* 389 */       return "([Ljava/lang/Object;I)Ljava/lang/Object;";
/*     */     }
/*     */     
/*     */     String constDescriptor() {
/* 393 */       return "([Ljava/lang/Object;I)V";
/*     */     }
/*     */   }
/*     */   
/*     */   static class LongConstParameter extends ConstParameter {
/*     */     long param;
/*     */     
/*     */     LongConstParameter(long l) {
/* 401 */       this.param = l;
/*     */     }
/*     */     
/*     */     int compile(Bytecode code) throws CannotCompileException {
/* 405 */       code.addLconst(this.param);
/* 406 */       return 2;
/*     */     }
/*     */     
/*     */     String descriptor() {
/* 410 */       return "([Ljava/lang/Object;J)Ljava/lang/Object;";
/*     */     }
/*     */     
/*     */     String constDescriptor() {
/* 414 */       return "([Ljava/lang/Object;J)V";
/*     */     }
/*     */   }
/*     */   
/*     */   static class StringConstParameter extends ConstParameter {
/*     */     String param;
/*     */     
/*     */     StringConstParameter(String s) {
/* 422 */       this.param = s;
/*     */     }
/*     */     
/*     */     int compile(Bytecode code) throws CannotCompileException {
/* 426 */       code.addLdc(this.param);
/* 427 */       return 1;
/*     */     }
/*     */     
/*     */     String descriptor() {
/* 431 */       return "([Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;";
/*     */     }
/*     */     
/*     */     String constDescriptor() {
/* 435 */       return "([Ljava/lang/Object;Ljava/lang/String;)V";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\CtMethod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */