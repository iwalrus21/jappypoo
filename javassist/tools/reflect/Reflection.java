/*     */ package javassist.tools.reflect;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.ClassPool;
/*     */ import javassist.CodeConverter;
/*     */ import javassist.CtClass;
/*     */ import javassist.CtField;
/*     */ import javassist.CtMethod;
/*     */ import javassist.CtNewMethod;
/*     */ import javassist.Modifier;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.Translator;
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.ClassFile;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Reflection
/*     */   implements Translator
/*     */ {
/*     */   static final String classobjectField = "_classobject";
/*     */   static final String classobjectAccessor = "_getClass";
/*     */   static final String metaobjectField = "_metaobject";
/*     */   static final String metaobjectGetter = "_getMetaobject";
/*     */   static final String metaobjectSetter = "_setMetaobject";
/*     */   static final String readPrefix = "_r_";
/*     */   static final String writePrefix = "_w_";
/*     */   static final String metaobjectClassName = "javassist.tools.reflect.Metaobject";
/*     */   static final String classMetaobjectClassName = "javassist.tools.reflect.ClassMetaobject";
/*     */   protected CtMethod trapMethod;
/*     */   protected CtMethod trapStaticMethod;
/*     */   protected CtMethod trapRead;
/*     */   protected CtMethod trapWrite;
/*     */   protected CtClass[] readParam;
/*     */   protected ClassPool classPool;
/*     */   protected CodeConverter converter;
/*     */   
/*     */   private boolean isExcluded(String name) {
/*  89 */     return (name.startsWith("_m_") || name
/*  90 */       .equals("_getClass") || name
/*  91 */       .equals("_setMetaobject") || name
/*  92 */       .equals("_getMetaobject") || name
/*  93 */       .startsWith("_r_") || name
/*  94 */       .startsWith("_w_"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Reflection() {
/* 101 */     this.classPool = null;
/* 102 */     this.converter = new CodeConverter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start(ClassPool pool) throws NotFoundException {
/* 109 */     this.classPool = pool;
/* 110 */     String msg = "javassist.tools.reflect.Sample is not found or broken.";
/*     */     
/*     */     try {
/* 113 */       CtClass c = this.classPool.get("javassist.tools.reflect.Sample");
/* 114 */       rebuildClassFile(c.getClassFile());
/* 115 */       this.trapMethod = c.getDeclaredMethod("trap");
/* 116 */       this.trapStaticMethod = c.getDeclaredMethod("trapStatic");
/* 117 */       this.trapRead = c.getDeclaredMethod("trapRead");
/* 118 */       this.trapWrite = c.getDeclaredMethod("trapWrite");
/* 119 */       this
/* 120 */         .readParam = new CtClass[] { this.classPool.get("java.lang.Object") };
/*     */     }
/* 122 */     catch (NotFoundException e) {
/* 123 */       throw new RuntimeException("javassist.tools.reflect.Sample is not found or broken.");
/* 124 */     } catch (BadBytecode e) {
/* 125 */       throw new RuntimeException("javassist.tools.reflect.Sample is not found or broken.");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onLoad(ClassPool pool, String classname) throws CannotCompileException, NotFoundException {
/* 136 */     CtClass clazz = pool.get(classname);
/* 137 */     clazz.instrument(this.converter);
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
/*     */   public boolean makeReflective(String classname, String metaobject, String metaclass) throws CannotCompileException, NotFoundException {
/* 157 */     return makeReflective(this.classPool.get(classname), this.classPool
/* 158 */         .get(metaobject), this.classPool
/* 159 */         .get(metaclass));
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
/*     */   public boolean makeReflective(Class clazz, Class metaobject, Class metaclass) throws CannotCompileException, NotFoundException {
/* 183 */     return makeReflective(clazz.getName(), metaobject.getName(), metaclass
/* 184 */         .getName());
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
/*     */   public boolean makeReflective(CtClass clazz, CtClass metaobject, CtClass metaclass) throws CannotCompileException, CannotReflectException, NotFoundException {
/* 210 */     if (clazz.isInterface()) {
/* 211 */       throw new CannotReflectException("Cannot reflect an interface: " + clazz
/* 212 */           .getName());
/*     */     }
/* 214 */     if (clazz.subclassOf(this.classPool.get("javassist.tools.reflect.ClassMetaobject"))) {
/* 215 */       throw new CannotReflectException("Cannot reflect a subclass of ClassMetaobject: " + clazz
/*     */           
/* 217 */           .getName());
/*     */     }
/* 219 */     if (clazz.subclassOf(this.classPool.get("javassist.tools.reflect.Metaobject"))) {
/* 220 */       throw new CannotReflectException("Cannot reflect a subclass of Metaobject: " + clazz
/*     */           
/* 222 */           .getName());
/*     */     }
/* 224 */     registerReflectiveClass(clazz);
/* 225 */     return modifyClassfile(clazz, metaobject, metaclass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void registerReflectiveClass(CtClass clazz) {
/* 233 */     CtField[] fs = clazz.getDeclaredFields();
/* 234 */     for (int i = 0; i < fs.length; i++) {
/* 235 */       CtField f = fs[i];
/* 236 */       int mod = f.getModifiers();
/* 237 */       if ((mod & 0x1) != 0 && (mod & 0x10) == 0) {
/* 238 */         String name = f.getName();
/* 239 */         this.converter.replaceFieldRead(f, clazz, "_r_" + name);
/* 240 */         this.converter.replaceFieldWrite(f, clazz, "_w_" + name);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean modifyClassfile(CtClass clazz, CtClass metaobject, CtClass metaclass) throws CannotCompileException, NotFoundException {
/* 249 */     if (clazz.getAttribute("Reflective") != null) {
/* 250 */       return false;
/*     */     }
/* 252 */     clazz.setAttribute("Reflective", new byte[0]);
/*     */     
/* 254 */     CtClass mlevel = this.classPool.get("javassist.tools.reflect.Metalevel");
/* 255 */     boolean addMeta = !clazz.subtypeOf(mlevel);
/* 256 */     if (addMeta) {
/* 257 */       clazz.addInterface(mlevel);
/*     */     }
/* 259 */     processMethods(clazz, addMeta);
/* 260 */     processFields(clazz);
/*     */ 
/*     */     
/* 263 */     if (addMeta) {
/* 264 */       CtField ctField = new CtField(this.classPool.get("javassist.tools.reflect.Metaobject"), "_metaobject", clazz);
/*     */       
/* 266 */       ctField.setModifiers(4);
/* 267 */       clazz.addField(ctField, CtField.Initializer.byNewWithParams(metaobject));
/*     */       
/* 269 */       clazz.addMethod(CtNewMethod.getter("_getMetaobject", ctField));
/* 270 */       clazz.addMethod(CtNewMethod.setter("_setMetaobject", ctField));
/*     */     } 
/*     */     
/* 273 */     CtField f = new CtField(this.classPool.get("javassist.tools.reflect.ClassMetaobject"), "_classobject", clazz);
/*     */     
/* 275 */     f.setModifiers(10);
/* 276 */     clazz.addField(f, CtField.Initializer.byNew(metaclass, new String[] { clazz
/* 277 */             .getName() }));
/*     */     
/* 279 */     clazz.addMethod(CtNewMethod.getter("_getClass", f));
/* 280 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void processMethods(CtClass clazz, boolean dontSearch) throws CannotCompileException, NotFoundException {
/* 286 */     CtMethod[] ms = clazz.getMethods();
/* 287 */     for (int i = 0; i < ms.length; i++) {
/* 288 */       CtMethod m = ms[i];
/* 289 */       int mod = m.getModifiers();
/* 290 */       if (Modifier.isPublic(mod) && !Modifier.isAbstract(mod)) {
/* 291 */         processMethods0(mod, clazz, m, i, dontSearch);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void processMethods0(int mod, CtClass clazz, CtMethod m, int identifier, boolean dontSearch) throws CannotCompileException, NotFoundException {
/*     */     CtMethod body, m2;
/* 300 */     String name = m.getName();
/*     */     
/* 302 */     if (isExcluded(name)) {
/*     */       return;
/*     */     }
/*     */     
/* 306 */     if (m.getDeclaringClass() == clazz) {
/* 307 */       if (Modifier.isNative(mod)) {
/*     */         return;
/*     */       }
/* 310 */       m2 = m;
/* 311 */       if (Modifier.isFinal(mod)) {
/* 312 */         mod &= 0xFFFFFFEF;
/* 313 */         m2.setModifiers(mod);
/*     */       } 
/*     */     } else {
/*     */       
/* 317 */       if (Modifier.isFinal(mod)) {
/*     */         return;
/*     */       }
/* 320 */       mod &= 0xFFFFFEFF;
/* 321 */       m2 = CtNewMethod.delegator(findOriginal(m, dontSearch), clazz);
/* 322 */       m2.setModifiers(mod);
/* 323 */       clazz.addMethod(m2);
/*     */     } 
/*     */     
/* 326 */     m2.setName("_m_" + identifier + "_" + name);
/*     */ 
/*     */     
/* 329 */     if (Modifier.isStatic(mod)) {
/* 330 */       body = this.trapStaticMethod;
/*     */     } else {
/* 332 */       body = this.trapMethod;
/*     */     } 
/*     */     
/* 335 */     CtMethod wmethod = CtNewMethod.wrapped(m.getReturnType(), name, m
/* 336 */         .getParameterTypes(), m.getExceptionTypes(), body, 
/* 337 */         CtMethod.ConstParameter.integer(identifier), clazz);
/*     */     
/* 339 */     wmethod.setModifiers(mod);
/* 340 */     clazz.addMethod(wmethod);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private CtMethod findOriginal(CtMethod m, boolean dontSearch) throws NotFoundException {
/* 346 */     if (dontSearch) {
/* 347 */       return m;
/*     */     }
/* 349 */     String name = m.getName();
/* 350 */     CtMethod[] ms = m.getDeclaringClass().getDeclaredMethods();
/* 351 */     for (int i = 0; i < ms.length; i++) {
/* 352 */       String orgName = ms[i].getName();
/* 353 */       if (orgName.endsWith(name) && orgName
/* 354 */         .startsWith("_m_") && ms[i]
/* 355 */         .getSignature().equals(m.getSignature())) {
/* 356 */         return ms[i];
/*     */       }
/*     */     } 
/* 359 */     return m;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void processFields(CtClass clazz) throws CannotCompileException, NotFoundException {
/* 365 */     CtField[] fs = clazz.getDeclaredFields();
/* 366 */     for (int i = 0; i < fs.length; i++) {
/* 367 */       CtField f = fs[i];
/* 368 */       int mod = f.getModifiers();
/* 369 */       if ((mod & 0x1) != 0 && (mod & 0x10) == 0) {
/* 370 */         mod |= 0x8;
/* 371 */         String name = f.getName();
/* 372 */         CtClass ftype = f.getType();
/*     */         
/* 374 */         CtMethod wmethod = CtNewMethod.wrapped(ftype, "_r_" + name, this.readParam, null, this.trapRead, 
/*     */             
/* 376 */             CtMethod.ConstParameter.string(name), clazz);
/*     */         
/* 378 */         wmethod.setModifiers(mod);
/* 379 */         clazz.addMethod(wmethod);
/* 380 */         CtClass[] writeParam = new CtClass[2];
/* 381 */         writeParam[0] = this.classPool.get("java.lang.Object");
/* 382 */         writeParam[1] = ftype;
/* 383 */         wmethod = CtNewMethod.wrapped(CtClass.voidType, "_w_" + name, writeParam, null, this.trapWrite, 
/*     */ 
/*     */             
/* 386 */             CtMethod.ConstParameter.string(name), clazz);
/* 387 */         wmethod.setModifiers(mod);
/* 388 */         clazz.addMethod(wmethod);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void rebuildClassFile(ClassFile cf) throws BadBytecode {
/* 394 */     if (ClassFile.MAJOR_VERSION < 50) {
/*     */       return;
/*     */     }
/* 397 */     Iterator<MethodInfo> methods = cf.getMethods().iterator();
/* 398 */     while (methods.hasNext()) {
/* 399 */       MethodInfo mi = methods.next();
/* 400 */       mi.rebuildStackMap(this.classPool);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\tools\reflect\Reflection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */