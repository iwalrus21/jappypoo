/*     */ package javassist;
/*     */ 
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.Bytecode;
/*     */ import javassist.bytecode.ClassFile;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CtConstructor
/*     */   extends CtBehavior
/*     */ {
/*     */   protected CtConstructor(MethodInfo minfo, CtClass declaring) {
/*  38 */     super(declaring, minfo);
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
/*     */   public CtConstructor(CtClass[] parameters, CtClass declaring) {
/*  57 */     this((MethodInfo)null, declaring);
/*  58 */     ConstPool cp = declaring.getClassFile2().getConstPool();
/*  59 */     String desc = Descriptor.ofConstructor(parameters);
/*  60 */     this.methodInfo = new MethodInfo(cp, "<init>", desc);
/*  61 */     setModifiers(1);
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
/*     */   public CtConstructor(CtConstructor src, CtClass declaring, ClassMap map) throws CannotCompileException {
/* 101 */     this((MethodInfo)null, declaring);
/* 102 */     copy(src, true, map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConstructor() {
/* 109 */     return this.methodInfo.isConstructor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isClassInitializer() {
/* 116 */     return this.methodInfo.isStaticInitializer();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLongName() {
/* 126 */     return getDeclaringClass().getName() + (
/* 127 */       isConstructor() ? Descriptor.toString(getSignature()) : ".<clinit>()");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 138 */     if (this.methodInfo.isStaticInitializer()) {
/* 139 */       return "<clinit>";
/*     */     }
/* 141 */     return this.declaringClass.getSimpleName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 152 */     CodeAttribute ca = getMethodInfo2().getCodeAttribute();
/* 153 */     if (ca == null) {
/* 154 */       return false;
/*     */     }
/*     */     
/* 157 */     ConstPool cp = ca.getConstPool();
/* 158 */     CodeIterator it = ca.iterator();
/*     */     
/*     */     try {
/* 161 */       int op0 = it.byteAt(it.next());
/* 162 */       if (op0 != 177) { int pos; if (op0 == 42 && it
/*     */           
/* 164 */           .byteAt(pos = it.next()) == 183)
/* 165 */         { int desc; if ((desc = cp.isConstructor(getSuperclassName(), it
/* 166 */               .u16bitAt(pos + 1))) != 0 && "()V"
/* 167 */             .equals(cp.getUtf8Info(desc)) && it
/* 168 */             .byteAt(it.next()) == 177 && 
/* 169 */             !it.hasNext()); }  return false; }
/*     */     
/* 171 */     } catch (BadBytecode badBytecode) {
/* 172 */       return false;
/*     */     } 
/*     */   }
/*     */   private String getSuperclassName() {
/* 176 */     ClassFile cf = this.declaringClass.getClassFile2();
/* 177 */     return cf.getSuperclass();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean callsSuper() throws CannotCompileException {
/* 186 */     CodeAttribute codeAttr = this.methodInfo.getCodeAttribute();
/* 187 */     if (codeAttr != null) {
/* 188 */       CodeIterator it = codeAttr.iterator();
/*     */       try {
/* 190 */         int index = it.skipSuperConstructor();
/* 191 */         return (index >= 0);
/*     */       }
/* 193 */       catch (BadBytecode e) {
/* 194 */         throw new CannotCompileException(e);
/*     */       } 
/*     */     } 
/*     */     
/* 198 */     return false;
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
/*     */   public void setBody(String src) throws CannotCompileException {
/* 211 */     if (src == null)
/* 212 */       if (isClassInitializer()) {
/* 213 */         src = ";";
/*     */       } else {
/* 215 */         src = "super();";
/*     */       }  
/* 217 */     super.setBody(src);
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
/*     */   public void setBody(CtConstructor src, ClassMap map) throws CannotCompileException {
/* 235 */     setBody0(src.declaringClass, src.methodInfo, this.declaringClass, this.methodInfo, map);
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
/*     */   public void insertBeforeBody(String src) throws CannotCompileException {
/* 248 */     CtClass cc = this.declaringClass;
/* 249 */     cc.checkModify();
/* 250 */     if (isClassInitializer()) {
/* 251 */       throw new CannotCompileException("class initializer");
/*     */     }
/* 253 */     CodeAttribute ca = this.methodInfo.getCodeAttribute();
/* 254 */     CodeIterator iterator = ca.iterator();
/*     */     
/* 256 */     Bytecode b = new Bytecode(this.methodInfo.getConstPool(), ca.getMaxStack(), ca.getMaxLocals());
/* 257 */     b.setStackDepth(ca.getMaxStack());
/* 258 */     Javac jv = new Javac(b, cc);
/*     */     try {
/* 260 */       jv.recordParams(getParameterTypes(), false);
/* 261 */       jv.compileStmnt(src);
/* 262 */       ca.setMaxStack(b.getMaxStack());
/* 263 */       ca.setMaxLocals(b.getMaxLocals());
/* 264 */       iterator.skipConstructor();
/* 265 */       int pos = iterator.insertEx(b.get());
/* 266 */       iterator.insert(b.getExceptionTable(), pos);
/* 267 */       this.methodInfo.rebuildStackMapIf6(cc.getClassPool(), cc.getClassFile2());
/*     */     }
/* 269 */     catch (NotFoundException e) {
/* 270 */       throw new CannotCompileException(e);
/*     */     }
/* 272 */     catch (CompileError e) {
/* 273 */       throw new CannotCompileException(e);
/*     */     }
/* 275 */     catch (BadBytecode e) {
/* 276 */       throw new CannotCompileException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getStartPosOfBody(CodeAttribute ca) throws CannotCompileException {
/* 284 */     CodeIterator ci = ca.iterator();
/*     */     try {
/* 286 */       ci.skipConstructor();
/* 287 */       return ci.next();
/*     */     }
/* 289 */     catch (BadBytecode e) {
/* 290 */       throw new CannotCompileException(e);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtMethod toMethod(String name, CtClass declaring) throws CannotCompileException {
/* 317 */     return toMethod(name, declaring, (ClassMap)null);
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
/*     */   public CtMethod toMethod(String name, CtClass declaring, ClassMap map) throws CannotCompileException {
/* 351 */     CtMethod method = new CtMethod(null, declaring);
/* 352 */     method.copy(this, false, map);
/* 353 */     if (isConstructor()) {
/* 354 */       MethodInfo minfo = method.getMethodInfo2();
/* 355 */       CodeAttribute ca = minfo.getCodeAttribute();
/* 356 */       if (ca != null) {
/* 357 */         removeConsCall(ca);
/*     */         try {
/* 359 */           this.methodInfo.rebuildStackMapIf6(declaring.getClassPool(), declaring
/* 360 */               .getClassFile2());
/*     */         }
/* 362 */         catch (BadBytecode e) {
/* 363 */           throw new CannotCompileException(e);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 368 */     method.setName(name);
/* 369 */     return method;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void removeConsCall(CodeAttribute ca) throws CannotCompileException {
/* 375 */     CodeIterator iterator = ca.iterator();
/*     */     try {
/* 377 */       int pos = iterator.skipConstructor();
/* 378 */       if (pos >= 0) {
/* 379 */         int mref = iterator.u16bitAt(pos + 1);
/* 380 */         String desc = ca.getConstPool().getMethodrefType(mref);
/* 381 */         int num = Descriptor.numOfParameters(desc) + 1;
/* 382 */         if (num > 3) {
/* 383 */           pos = (iterator.insertGapAt(pos, num - 3, false)).position;
/*     */         }
/* 385 */         iterator.writeByte(87, pos++);
/* 386 */         iterator.writeByte(0, pos);
/* 387 */         iterator.writeByte(0, pos + 1);
/* 388 */         Descriptor.Iterator it = new Descriptor.Iterator(desc);
/*     */         while (true) {
/* 390 */           it.next();
/* 391 */           if (it.isParameter()) {
/* 392 */             iterator.writeByte(it.is2byte() ? 88 : 87, pos++);
/*     */             
/*     */             continue;
/*     */           } 
/*     */           break;
/*     */         } 
/*     */       } 
/* 399 */     } catch (BadBytecode e) {
/* 400 */       throw new CannotCompileException(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\CtConstructor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */