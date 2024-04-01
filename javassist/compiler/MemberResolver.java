/*     */ package javassist.compiler;
/*     */ 
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.WeakHashMap;
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtClass;
/*     */ import javassist.CtField;
/*     */ import javassist.Modifier;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.bytecode.ClassFile;
/*     */ import javassist.bytecode.Descriptor;
/*     */ import javassist.bytecode.MethodInfo;
/*     */ import javassist.compiler.ast.ASTList;
/*     */ import javassist.compiler.ast.ASTree;
/*     */ import javassist.compiler.ast.Declarator;
/*     */ import javassist.compiler.ast.Keyword;
/*     */ import javassist.compiler.ast.Symbol;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MemberResolver
/*     */   implements TokenId
/*     */ {
/*     */   private ClassPool classPool;
/*     */   private static final int YES = 0;
/*     */   private static final int NO = -1;
/*     */   private static final String INVALID = "<invalid>";
/*     */   
/*     */   public ClassPool getClassPool() {
/*     */     return this.classPool;
/*     */   }
/*     */   
/*     */   private static void fatal() throws CompileError {
/*     */     throw new CompileError("fatal");
/*     */   }
/*     */   
/*     */   public static class Method
/*     */   {
/*     */     public CtClass declaring;
/*     */     public MethodInfo info;
/*     */     public int notmatch;
/*     */     
/*     */     public Method(CtClass c, MethodInfo i, int n) {
/*     */       this.declaring = c;
/*     */       this.info = i;
/*     */       this.notmatch = n;
/*     */     }
/*     */     
/*     */     public boolean isStatic() {
/*     */       int acc = this.info.getAccessFlags();
/*     */       return ((acc & 0x8) != 0);
/*     */     }
/*     */   }
/*     */   
/*     */   public Method lookupMethod(CtClass clazz, CtClass currentClass, MethodInfo current, String methodName, int[] argTypes, int[] argDims, String[] argClassNames) throws CompileError {
/*     */     Method maybe = null;
/*     */     if (current != null && clazz == currentClass && current.getName().equals(methodName)) {
/*     */       int res = compareSignature(current.getDescriptor(), argTypes, argDims, argClassNames);
/*     */       if (res != -1) {
/*     */         Method r = new Method(clazz, current, res);
/*     */         if (res == 0)
/*     */           return r; 
/*     */         maybe = r;
/*     */       } 
/*     */     } 
/*     */     Method m = lookupMethod(clazz, methodName, argTypes, argDims, argClassNames, (maybe != null));
/*     */     if (m != null)
/*     */       return m; 
/*     */     return maybe;
/*     */   }
/*     */   
/*     */   private Method lookupMethod(CtClass clazz, String methodName, int[] argTypes, int[] argDims, String[] argClassNames, boolean onlyExact) throws CompileError {
/*     */     Method maybe = null;
/*     */     ClassFile cf = clazz.getClassFile2();
/*     */     if (cf != null) {
/*     */       List<MethodInfo> list = cf.getMethods();
/*     */       int n = list.size();
/*     */       for (int i = 0; i < n; i++) {
/*     */         MethodInfo minfo = list.get(i);
/*     */         if (minfo.getName().equals(methodName) && (minfo.getAccessFlags() & 0x40) == 0) {
/*     */           int res = compareSignature(minfo.getDescriptor(), argTypes, argDims, argClassNames);
/*     */           if (res != -1) {
/*     */             Method r = new Method(clazz, minfo, res);
/*     */             if (res == 0)
/*     */               return r; 
/*     */             if (maybe == null || maybe.notmatch > res)
/*     */               maybe = r; 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     if (onlyExact) {
/*     */       maybe = null;
/*     */     } else if (maybe != null) {
/*     */       return maybe;
/*     */     } 
/*     */     int mod = clazz.getModifiers();
/*     */     boolean isIntf = Modifier.isInterface(mod);
/*     */     try {
/*     */       if (!isIntf) {
/*     */         CtClass pclazz = clazz.getSuperclass();
/*     */         if (pclazz != null) {
/*     */           Method r = lookupMethod(pclazz, methodName, argTypes, argDims, argClassNames, onlyExact);
/*     */           if (r != null)
/*     */             return r; 
/*     */         } 
/*     */       } 
/*     */     } catch (NotFoundException notFoundException) {}
/*     */     try {
/*     */       CtClass[] ifs = clazz.getInterfaces();
/*     */       int size = ifs.length;
/*     */       for (int i = 0; i < size; i++) {
/*     */         Method r = lookupMethod(ifs[i], methodName, argTypes, argDims, argClassNames, onlyExact);
/*     */         if (r != null)
/*     */           return r; 
/*     */       } 
/*     */       if (isIntf) {
/*     */         CtClass pclazz = clazz.getSuperclass();
/*     */         if (pclazz != null) {
/*     */           Method r = lookupMethod(pclazz, methodName, argTypes, argDims, argClassNames, onlyExact);
/*     */           if (r != null)
/*     */             return r; 
/*     */         } 
/*     */       } 
/*     */     } catch (NotFoundException notFoundException) {}
/*     */     return maybe;
/*     */   }
/*     */   
/*     */   private int compareSignature(String desc, int[] argTypes, int[] argDims, String[] argClassNames) throws CompileError {
/*     */     int result = 0;
/*     */     int i = 1;
/*     */     int nArgs = argTypes.length;
/*     */     if (nArgs != Descriptor.numOfParameters(desc))
/*     */       return -1; 
/*     */     int len = desc.length();
/*     */     for (int n = 0; i < len; n++) {
/*     */       char c = desc.charAt(i++);
/*     */       if (c == ')')
/*     */         return (n == nArgs) ? result : -1; 
/*     */       if (n >= nArgs)
/*     */         return -1; 
/*     */       int dim = 0;
/*     */       while (c == '[') {
/*     */         dim++;
/*     */         c = desc.charAt(i++);
/*     */       } 
/*     */       if (argTypes[n] == 412) {
/*     */         if (dim == 0 && c != 'L')
/*     */           return -1; 
/*     */         if (c == 'L')
/*     */           i = desc.indexOf(';', i) + 1; 
/*     */       } else if (argDims[n] != dim) {
/*     */         if (dim != 0 || c != 'L' || !desc.startsWith("java/lang/Object;", i))
/*     */           return -1; 
/*     */         i = desc.indexOf(';', i) + 1;
/*     */         result++;
/*     */         if (i <= 0)
/*     */           return -1; 
/*     */       } else if (c == 'L') {
/*     */         int j = desc.indexOf(';', i);
/*     */         if (j < 0 || argTypes[n] != 307)
/*     */           return -1; 
/*     */         String cname = desc.substring(i, j);
/*     */         if (!cname.equals(argClassNames[n])) {
/*     */           CtClass clazz = lookupClassByJvmName(argClassNames[n]);
/*     */           try {
/*     */             if (clazz.subtypeOf(lookupClassByJvmName(cname))) {
/*     */               result++;
/*     */             } else {
/*     */               return -1;
/*     */             } 
/*     */           } catch (NotFoundException e) {
/*     */             result++;
/*     */           } 
/*     */         } 
/*     */         i = j + 1;
/*     */       } else {
/*     */         int t = descToType(c);
/*     */         int at = argTypes[n];
/*     */         if (t != at)
/*     */           if (t == 324 && (at == 334 || at == 303 || at == 306)) {
/*     */             result++;
/*     */           } else {
/*     */             return -1;
/*     */           }  
/*     */       } 
/*     */     } 
/*     */     return -1;
/*     */   }
/*     */   
/*     */   public CtField lookupFieldByJvmName2(String jvmClassName, Symbol fieldSym, ASTree expr) throws NoFieldException {
/*     */     String field = fieldSym.get();
/*     */     CtClass cc = null;
/*     */     try {
/*     */       cc = lookupClass(jvmToJavaName(jvmClassName), true);
/*     */     } catch (CompileError e) {
/*     */       throw new NoFieldException(jvmClassName + "/" + field, expr);
/*     */     } 
/*     */     try {
/*     */       return cc.getField(field);
/*     */     } catch (NotFoundException e) {
/*     */       jvmClassName = javaToJvmName(cc.getName());
/*     */       throw new NoFieldException(jvmClassName + "$" + field, expr);
/*     */     } 
/*     */   }
/*     */   
/*     */   public CtField lookupFieldByJvmName(String jvmClassName, Symbol fieldName) throws CompileError {
/*     */     return lookupField(jvmToJavaName(jvmClassName), fieldName);
/*     */   }
/*     */   
/*     */   public CtField lookupField(String className, Symbol fieldName) throws CompileError {
/*     */     CtClass cc = lookupClass(className, false);
/*     */     try {
/*     */       return cc.getField(fieldName.get());
/*     */     } catch (NotFoundException notFoundException) {
/*     */       throw new CompileError("no such field: " + fieldName.get());
/*     */     } 
/*     */   }
/*     */   
/*     */   public CtClass lookupClassByName(ASTList name) throws CompileError {
/*     */     return lookupClass(Declarator.astToClassName(name, '.'), false);
/*     */   }
/*     */   
/*     */   public CtClass lookupClassByJvmName(String jvmName) throws CompileError {
/*     */     return lookupClass(jvmToJavaName(jvmName), false);
/*     */   }
/*     */   
/*     */   public CtClass lookupClass(Declarator decl) throws CompileError {
/*     */     return lookupClass(decl.getType(), decl.getArrayDim(), decl.getClassName());
/*     */   }
/*     */   
/*     */   public CtClass lookupClass(int type, int dim, String classname) throws CompileError {
/*     */     String cname = "";
/*     */     if (type == 307) {
/*     */       CtClass clazz = lookupClassByJvmName(classname);
/*     */       if (dim > 0) {
/*     */         cname = clazz.getName();
/*     */       } else {
/*     */         return clazz;
/*     */       } 
/*     */     } else {
/*     */       cname = getTypeName(type);
/*     */     } 
/*     */     while (dim-- > 0)
/*     */       cname = cname + "[]"; 
/*     */     return lookupClass(cname, false);
/*     */   }
/*     */   
/*     */   static String getTypeName(int type) throws CompileError {
/*     */     String cname = "";
/*     */     switch (type) {
/*     */       case 301:
/*     */         cname = "boolean";
/*     */         return cname;
/*     */       case 306:
/*     */         cname = "char";
/*     */         return cname;
/*     */       case 303:
/*     */         cname = "byte";
/*     */         return cname;
/*     */       case 334:
/*     */         cname = "short";
/*     */         return cname;
/*     */       case 324:
/*     */         cname = "int";
/*     */         return cname;
/*     */       case 326:
/*     */         cname = "long";
/*     */         return cname;
/*     */       case 317:
/*     */         cname = "float";
/*     */         return cname;
/*     */       case 312:
/*     */         cname = "double";
/*     */         return cname;
/*     */       case 344:
/*     */         cname = "void";
/*     */         return cname;
/*     */     } 
/*     */     fatal();
/*     */     return cname;
/*     */   }
/*     */   
/*     */   public CtClass lookupClass(String name, boolean notCheckInner) throws CompileError {
/*     */     Hashtable<String, String> cache = getInvalidNames();
/*     */     Object found = cache.get(name);
/*     */     if (found == "<invalid>")
/*     */       throw new CompileError("no such class: " + name); 
/*     */     if (found != null)
/*     */       try {
/*     */         return this.classPool.get((String)found);
/*     */       } catch (NotFoundException notFoundException) {} 
/*     */     CtClass cc = null;
/*     */     try {
/*     */       cc = lookupClass0(name, notCheckInner);
/*     */     } catch (NotFoundException e) {
/*     */       cc = searchImports(name);
/*     */     } 
/*     */     cache.put(name, cc.getName());
/*     */     return cc;
/*     */   }
/*     */   
/*     */   private static WeakHashMap invalidNamesMap = new WeakHashMap<Object, Object>();
/*     */   private Hashtable invalidNames;
/*     */   
/*     */   public MemberResolver(ClassPool cp) {
/* 423 */     this.invalidNames = null;
/*     */     this.classPool = cp;
/*     */   } public static int getInvalidMapSize() {
/* 426 */     return invalidNamesMap.size();
/*     */   }
/*     */   private Hashtable getInvalidNames() {
/* 429 */     Hashtable<Object, Object> ht = this.invalidNames;
/* 430 */     if (ht == null) {
/* 431 */       synchronized (MemberResolver.class) {
/* 432 */         WeakReference<Hashtable> ref = (WeakReference)invalidNamesMap.get(this.classPool);
/* 433 */         if (ref != null) {
/* 434 */           ht = ref.get();
/*     */         }
/* 436 */         if (ht == null) {
/* 437 */           ht = new Hashtable<Object, Object>();
/* 438 */           invalidNamesMap.put(this.classPool, new WeakReference<Hashtable<Object, Object>>(ht));
/*     */         } 
/*     */       } 
/*     */       
/* 442 */       this.invalidNames = ht;
/*     */     } 
/*     */     
/* 445 */     return ht;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private CtClass searchImports(String orgName) throws CompileError {
/* 451 */     if (orgName.indexOf('.') < 0) {
/* 452 */       Iterator<String> it = this.classPool.getImportedPackages();
/* 453 */       while (it.hasNext()) {
/* 454 */         String pac = it.next();
/* 455 */         String fqName = pac + '.' + orgName;
/*     */         try {
/* 457 */           return this.classPool.get(fqName);
/*     */         }
/* 459 */         catch (NotFoundException e) {
/*     */           try {
/* 461 */             if (pac.endsWith("." + orgName)) {
/* 462 */               return this.classPool.get(pac);
/*     */             }
/* 464 */           } catch (NotFoundException notFoundException) {}
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 469 */     getInvalidNames().put(orgName, "<invalid>");
/* 470 */     throw new CompileError("no such class: " + orgName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private CtClass lookupClass0(String classname, boolean notCheckInner) throws NotFoundException {
/* 476 */     CtClass cc = null;
/*     */     while (true) {
/*     */       try {
/* 479 */         cc = this.classPool.get(classname);
/*     */       }
/* 481 */       catch (NotFoundException e) {
/* 482 */         int i = classname.lastIndexOf('.');
/* 483 */         if (notCheckInner || i < 0) {
/* 484 */           throw e;
/*     */         }
/* 486 */         StringBuffer sbuf = new StringBuffer(classname);
/* 487 */         sbuf.setCharAt(i, '$');
/* 488 */         classname = sbuf.toString();
/*     */       } 
/*     */       
/* 491 */       if (cc != null) {
/* 492 */         return cc;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String resolveClassName(ASTList name) throws CompileError {
/* 501 */     if (name == null) {
/* 502 */       return null;
/*     */     }
/* 504 */     return javaToJvmName(lookupClassByName(name).getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String resolveJvmClassName(String jvmName) throws CompileError {
/* 511 */     if (jvmName == null) {
/* 512 */       return null;
/*     */     }
/* 514 */     return javaToJvmName(lookupClassByJvmName(jvmName).getName());
/*     */   }
/*     */   
/*     */   public static CtClass getSuperclass(CtClass c) throws CompileError {
/*     */     try {
/* 519 */       CtClass sc = c.getSuperclass();
/* 520 */       if (sc != null) {
/* 521 */         return sc;
/*     */       }
/* 523 */     } catch (NotFoundException notFoundException) {}
/* 524 */     throw new CompileError("cannot find the super class of " + c
/* 525 */         .getName());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static CtClass getSuperInterface(CtClass c, String interfaceName) throws CompileError {
/*     */     try {
/* 532 */       CtClass[] intfs = c.getInterfaces();
/* 533 */       for (int i = 0; i < intfs.length; i++)
/* 534 */       { if (intfs[i].getName().equals(interfaceName))
/* 535 */           return intfs[i];  } 
/* 536 */     } catch (NotFoundException notFoundException) {}
/* 537 */     throw new CompileError("cannot find the super inetrface " + interfaceName + " of " + c
/* 538 */         .getName());
/*     */   }
/*     */   
/*     */   public static String javaToJvmName(String classname) {
/* 542 */     return classname.replace('.', '/');
/*     */   }
/*     */   
/*     */   public static String jvmToJavaName(String classname) {
/* 546 */     return classname.replace('/', '.');
/*     */   }
/*     */   
/*     */   public static int descToType(char c) throws CompileError {
/* 550 */     switch (c) {
/*     */       case 'Z':
/* 552 */         return 301;
/*     */       case 'C':
/* 554 */         return 306;
/*     */       case 'B':
/* 556 */         return 303;
/*     */       case 'S':
/* 558 */         return 334;
/*     */       case 'I':
/* 560 */         return 324;
/*     */       case 'J':
/* 562 */         return 326;
/*     */       case 'F':
/* 564 */         return 317;
/*     */       case 'D':
/* 566 */         return 312;
/*     */       case 'V':
/* 568 */         return 344;
/*     */       case 'L':
/*     */       case '[':
/* 571 */         return 307;
/*     */     } 
/* 573 */     fatal();
/* 574 */     return 344;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getModifiers(ASTList mods) {
/* 579 */     int m = 0;
/* 580 */     while (mods != null) {
/* 581 */       Keyword k = (Keyword)mods.head();
/* 582 */       mods = mods.tail();
/* 583 */       switch (k.get()) {
/*     */         case 335:
/* 585 */           m |= 0x8;
/*     */         
/*     */         case 315:
/* 588 */           m |= 0x10;
/*     */         
/*     */         case 338:
/* 591 */           m |= 0x20;
/*     */         
/*     */         case 300:
/* 594 */           m |= 0x400;
/*     */         
/*     */         case 332:
/* 597 */           m |= 0x1;
/*     */         
/*     */         case 331:
/* 600 */           m |= 0x4;
/*     */         
/*     */         case 330:
/* 603 */           m |= 0x2;
/*     */         
/*     */         case 345:
/* 606 */           m |= 0x40;
/*     */         
/*     */         case 342:
/* 609 */           m |= 0x80;
/*     */         
/*     */         case 347:
/* 612 */           m |= 0x800;
/*     */       } 
/*     */ 
/*     */     
/*     */     } 
/* 617 */     return m;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\MemberResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */