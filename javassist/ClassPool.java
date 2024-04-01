/*      */ package javassist;
/*      */ 
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.net.URL;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.security.ProtectionDomain;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import javassist.bytecode.ClassFile;
/*      */ import javassist.bytecode.Descriptor;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ClassPool
/*      */ {
/*      */   private static Method defineClass1;
/*      */   private static Method defineClass2;
/*      */   private static Method definePackage;
/*      */   
/*      */   static {
/*      */     try {
/*   79 */       AccessController.doPrivileged(new PrivilegedExceptionAction() {
/*      */             public Object run() throws Exception {
/*   81 */               Class<?> cl = Class.forName("java.lang.ClassLoader");
/*   82 */               ClassPool.defineClass1 = cl.getDeclaredMethod("defineClass", new Class[] { String.class, byte[].class, int.class, int.class });
/*      */ 
/*      */ 
/*      */               
/*   86 */               ClassPool.defineClass2 = cl.getDeclaredMethod("defineClass", new Class[] { String.class, byte[].class, int.class, int.class, ProtectionDomain.class });
/*      */ 
/*      */ 
/*      */               
/*   90 */               ClassPool.definePackage = cl.getDeclaredMethod("definePackage", new Class[] { String.class, String.class, String.class, String.class, String.class, String.class, String.class, URL.class });
/*      */ 
/*      */ 
/*      */               
/*   94 */               return null;
/*      */             }
/*      */           });
/*      */     }
/*   98 */     catch (PrivilegedActionException pae) {
/*   99 */       throw new RuntimeException("cannot initialize ClassPool", pae.getException());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean childFirstLookup = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean doPruning = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int compressCount;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int COMPRESS_THRESHOLD = 100;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean releaseUnmodifiedClassFile = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ClassPoolTail source;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ClassPool parent;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Hashtable classes;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  154 */   private Hashtable cflow = null;
/*      */ 
/*      */   
/*      */   private static final int INIT_HASH_SIZE = 191;
/*      */ 
/*      */   
/*      */   private ArrayList importedPackages;
/*      */ 
/*      */   
/*      */   public ClassPool() {
/*  164 */     this((ClassPool)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassPool(boolean useDefaultPath) {
/*  177 */     this((ClassPool)null);
/*  178 */     if (useDefaultPath) {
/*  179 */       appendSystemPath();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassPool(ClassPool parent) {
/*  190 */     this.classes = new Hashtable<Object, Object>(191);
/*  191 */     this.source = new ClassPoolTail();
/*  192 */     this.parent = parent;
/*  193 */     if (parent == null) {
/*  194 */       CtClass[] pt = CtClass.primitiveTypes;
/*  195 */       for (int i = 0; i < pt.length; i++) {
/*  196 */         this.classes.put(pt[i].getName(), pt[i]);
/*      */       }
/*      */     } 
/*  199 */     this.cflow = null;
/*  200 */     this.compressCount = 0;
/*  201 */     clearImportedPackages();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static synchronized ClassPool getDefault() {
/*  229 */     if (defaultPool == null) {
/*  230 */       defaultPool = new ClassPool(null);
/*  231 */       defaultPool.appendSystemPath();
/*      */     } 
/*      */     
/*  234 */     return defaultPool;
/*      */   }
/*      */   
/*  237 */   private static ClassPool defaultPool = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected CtClass getCached(String classname) {
/*  247 */     return (CtClass)this.classes.get(classname);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void cacheCtClass(String classname, CtClass c, boolean dynamic) {
/*  258 */     this.classes.put(classname, c);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected CtClass removeCached(String classname) {
/*  269 */     return (CtClass)this.classes.remove(classname);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*  276 */     return this.source.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void compress() {
/*  284 */     if (this.compressCount++ > 100) {
/*  285 */       this.compressCount = 0;
/*  286 */       Enumeration<CtClass> e = this.classes.elements();
/*  287 */       while (e.hasMoreElements()) {
/*  288 */         ((CtClass)e.nextElement()).compress();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void importPackage(String packageName) {
/*  310 */     this.importedPackages.add(packageName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clearImportedPackages() {
/*  321 */     this.importedPackages = new ArrayList();
/*  322 */     this.importedPackages.add("java.lang");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Iterator getImportedPackages() {
/*  332 */     return this.importedPackages.iterator();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void recordInvalidClassName(String name) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void recordCflow(String name, String cname, String fname) {
/*  362 */     if (this.cflow == null) {
/*  363 */       this.cflow = new Hashtable<Object, Object>();
/*      */     }
/*  365 */     this.cflow.put(name, new Object[] { cname, fname });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object[] lookupCflow(String name) {
/*  374 */     if (this.cflow == null) {
/*  375 */       this.cflow = new Hashtable<Object, Object>();
/*      */     }
/*  377 */     return (Object[])this.cflow.get(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtClass getAndRename(String orgName, String newName) throws NotFoundException {
/*  401 */     CtClass clazz = get0(orgName, false);
/*  402 */     if (clazz == null) {
/*  403 */       throw new NotFoundException(orgName);
/*      */     }
/*  405 */     if (clazz instanceof CtClassType) {
/*  406 */       ((CtClassType)clazz).setClassPool(this);
/*      */     }
/*  408 */     clazz.setName(newName);
/*      */     
/*  410 */     return clazz;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   synchronized void classNameChanged(String oldname, CtClass clazz) {
/*  419 */     CtClass c = getCached(oldname);
/*  420 */     if (c == clazz) {
/*  421 */       removeCached(oldname);
/*      */     }
/*  423 */     String newName = clazz.getName();
/*  424 */     checkNotFrozen(newName);
/*  425 */     cacheCtClass(newName, clazz, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtClass get(String classname) throws NotFoundException {
/*      */     CtClass clazz;
/*  446 */     if (classname == null) {
/*  447 */       clazz = null;
/*      */     } else {
/*  449 */       clazz = get0(classname, true);
/*      */     } 
/*  451 */     if (clazz == null) {
/*  452 */       throw new NotFoundException(classname);
/*      */     }
/*  454 */     clazz.incGetCounter();
/*  455 */     return clazz;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtClass getOrNull(String classname) {
/*  474 */     CtClass clazz = null;
/*  475 */     if (classname == null) {
/*  476 */       clazz = null;
/*      */     } else {
/*      */ 
/*      */       
/*      */       try {
/*      */ 
/*      */         
/*  483 */         clazz = get0(classname, true);
/*      */       }
/*  485 */       catch (NotFoundException notFoundException) {}
/*      */     } 
/*  487 */     if (clazz != null) {
/*  488 */       clazz.incGetCounter();
/*      */     }
/*  490 */     return clazz;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtClass getCtClass(String classname) throws NotFoundException {
/*  514 */     if (classname.charAt(0) == '[') {
/*  515 */       return Descriptor.toCtClass(classname, this);
/*      */     }
/*  517 */     return get(classname);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected synchronized CtClass get0(String classname, boolean useCache) throws NotFoundException {
/*  527 */     CtClass clazz = null;
/*  528 */     if (useCache) {
/*  529 */       clazz = getCached(classname);
/*  530 */       if (clazz != null) {
/*  531 */         return clazz;
/*      */       }
/*      */     } 
/*  534 */     if (!this.childFirstLookup && this.parent != null) {
/*  535 */       clazz = this.parent.get0(classname, useCache);
/*  536 */       if (clazz != null) {
/*  537 */         return clazz;
/*      */       }
/*      */     } 
/*  540 */     clazz = createCtClass(classname, useCache);
/*  541 */     if (clazz != null) {
/*      */       
/*  543 */       if (useCache) {
/*  544 */         cacheCtClass(clazz.getName(), clazz, false);
/*      */       }
/*  546 */       return clazz;
/*      */     } 
/*      */     
/*  549 */     if (this.childFirstLookup && this.parent != null) {
/*  550 */       clazz = this.parent.get0(classname, useCache);
/*      */     }
/*  552 */     return clazz;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected CtClass createCtClass(String classname, boolean useCache) {
/*  564 */     if (classname.charAt(0) == '[') {
/*  565 */       classname = Descriptor.toClassName(classname);
/*      */     }
/*  567 */     if (classname.endsWith("[]")) {
/*  568 */       String base = classname.substring(0, classname.indexOf('['));
/*  569 */       if ((!useCache || getCached(base) == null) && find(base) == null) {
/*  570 */         return null;
/*      */       }
/*  572 */       return new CtArray(classname, this);
/*      */     } 
/*      */     
/*  575 */     if (find(classname) == null) {
/*  576 */       return null;
/*      */     }
/*  578 */     return new CtClassType(classname, this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URL find(String classname) {
/*  591 */     return this.source.find(classname);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void checkNotFrozen(String classname) throws RuntimeException {
/*  603 */     CtClass clazz = getCached(classname);
/*  604 */     if (clazz == null) {
/*  605 */       if (!this.childFirstLookup && this.parent != null) {
/*      */         try {
/*  607 */           clazz = this.parent.get0(classname, true);
/*      */         }
/*  609 */         catch (NotFoundException notFoundException) {}
/*  610 */         if (clazz != null) {
/*  611 */           throw new RuntimeException(classname + " is in a parent ClassPool.  Use the parent.");
/*      */         }
/*      */       }
/*      */     
/*      */     }
/*  616 */     else if (clazz.isFrozen()) {
/*  617 */       throw new RuntimeException(classname + ": frozen class (cannot edit)");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   CtClass checkNotExists(String classname) {
/*  628 */     CtClass clazz = getCached(classname);
/*  629 */     if (clazz == null && 
/*  630 */       !this.childFirstLookup && this.parent != null) {
/*      */       try {
/*  632 */         clazz = this.parent.get0(classname, true);
/*      */       }
/*  634 */       catch (NotFoundException notFoundException) {}
/*      */     }
/*      */     
/*  637 */     return clazz;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   InputStream openClassfile(String classname) throws NotFoundException {
/*  643 */     return this.source.openClassfile(classname);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void writeClassfile(String classname, OutputStream out) throws NotFoundException, IOException, CannotCompileException {
/*  649 */     this.source.writeClassfile(classname, out);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtClass[] get(String[] classnames) throws NotFoundException {
/*  664 */     if (classnames == null) {
/*  665 */       return new CtClass[0];
/*      */     }
/*  667 */     int num = classnames.length;
/*  668 */     CtClass[] result = new CtClass[num];
/*  669 */     for (int i = 0; i < num; i++) {
/*  670 */       result[i] = get(classnames[i]);
/*      */     }
/*  672 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtMethod getMethod(String classname, String methodname) throws NotFoundException {
/*  685 */     CtClass c = get(classname);
/*  686 */     return c.getDeclaredMethod(methodname);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtClass makeClass(InputStream classfile) throws IOException, RuntimeException {
/*  707 */     return makeClass(classfile, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtClass makeClass(InputStream classfile, boolean ifNotFrozen) throws IOException, RuntimeException {
/*  727 */     compress();
/*  728 */     classfile = new BufferedInputStream(classfile);
/*  729 */     CtClass clazz = new CtClassType(classfile, this);
/*  730 */     clazz.checkModify();
/*  731 */     String classname = clazz.getName();
/*  732 */     if (ifNotFrozen) {
/*  733 */       checkNotFrozen(classname);
/*      */     }
/*  735 */     cacheCtClass(classname, clazz, true);
/*  736 */     return clazz;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtClass makeClass(ClassFile classfile) throws RuntimeException {
/*  756 */     return makeClass(classfile, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtClass makeClass(ClassFile classfile, boolean ifNotFrozen) throws RuntimeException {
/*  776 */     compress();
/*  777 */     CtClass clazz = new CtClassType(classfile, this);
/*  778 */     clazz.checkModify();
/*  779 */     String classname = clazz.getName();
/*  780 */     if (ifNotFrozen) {
/*  781 */       checkNotFrozen(classname);
/*      */     }
/*  783 */     cacheCtClass(classname, clazz, true);
/*  784 */     return clazz;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtClass makeClassIfNew(InputStream classfile) throws IOException, RuntimeException {
/*  805 */     compress();
/*  806 */     classfile = new BufferedInputStream(classfile);
/*  807 */     CtClass clazz = new CtClassType(classfile, this);
/*  808 */     clazz.checkModify();
/*  809 */     String classname = clazz.getName();
/*  810 */     CtClass found = checkNotExists(classname);
/*  811 */     if (found != null) {
/*  812 */       return found;
/*      */     }
/*  814 */     cacheCtClass(classname, clazz, true);
/*  815 */     return clazz;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtClass makeClass(String classname) throws RuntimeException {
/*  836 */     return makeClass(classname, (CtClass)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized CtClass makeClass(String classname, CtClass superclass) throws RuntimeException {
/*  859 */     checkNotFrozen(classname);
/*  860 */     CtClass clazz = new CtNewClass(classname, this, false, superclass);
/*  861 */     cacheCtClass(classname, clazz, true);
/*  862 */     return clazz;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   synchronized CtClass makeNestedClass(String classname) {
/*  873 */     checkNotFrozen(classname);
/*  874 */     CtClass clazz = new CtNewNestedClass(classname, this, false, null);
/*  875 */     cacheCtClass(classname, clazz, true);
/*  876 */     return clazz;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtClass makeInterface(String name) throws RuntimeException {
/*  888 */     return makeInterface(name, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized CtClass makeInterface(String name, CtClass superclass) throws RuntimeException {
/*  903 */     checkNotFrozen(name);
/*  904 */     CtClass clazz = new CtNewClass(name, this, true, superclass);
/*  905 */     cacheCtClass(name, clazz, true);
/*  906 */     return clazz;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtClass makeAnnotation(String name) throws RuntimeException {
/*      */     try {
/*  921 */       CtClass cc = makeInterface(name, get("java.lang.annotation.Annotation"));
/*  922 */       cc.setModifiers(cc.getModifiers() | 0x2000);
/*  923 */       return cc;
/*      */     }
/*  925 */     catch (NotFoundException e) {
/*      */       
/*  927 */       throw new RuntimeException(e.getMessage(), e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassPath appendSystemPath() {
/*  942 */     return this.source.appendSystemPath();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassPath insertClassPath(ClassPath cp) {
/*  955 */     return this.source.insertClassPath(cp);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassPath appendClassPath(ClassPath cp) {
/*  968 */     return this.source.appendClassPath(cp);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassPath insertClassPath(String pathname) throws NotFoundException {
/*  986 */     return this.source.insertClassPath(pathname);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassPath appendClassPath(String pathname) throws NotFoundException {
/* 1004 */     return this.source.appendClassPath(pathname);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeClassPath(ClassPath cp) {
/* 1013 */     this.source.removeClassPath(cp);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void appendPathList(String pathlist) throws NotFoundException {
/* 1029 */     char sep = File.pathSeparatorChar;
/* 1030 */     int i = 0;
/*      */     while (true) {
/* 1032 */       int j = pathlist.indexOf(sep, i);
/* 1033 */       if (j < 0) {
/* 1034 */         appendClassPath(pathlist.substring(i));
/*      */         
/*      */         break;
/*      */       } 
/* 1038 */       appendClassPath(pathlist.substring(i, j));
/* 1039 */       i = j + 1;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Class toClass(CtClass clazz) throws CannotCompileException {
/* 1071 */     return toClass(clazz, getClassLoader());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassLoader getClassLoader() {
/* 1085 */     return getContextClassLoader();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static ClassLoader getContextClassLoader() {
/* 1093 */     return Thread.currentThread().getContextClassLoader();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Class toClass(CtClass ct, ClassLoader loader) throws CannotCompileException {
/* 1113 */     return toClass(ct, loader, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Class toClass(CtClass ct, ClassLoader loader, ProtectionDomain domain) throws CannotCompileException {
/*      */     try {
/*      */       Method method;
/*      */       Object[] args;
/* 1150 */       byte[] b = ct.toBytecode();
/*      */ 
/*      */       
/* 1153 */       if (domain == null) {
/* 1154 */         method = defineClass1;
/* 1155 */         args = new Object[] { ct.getName(), b, new Integer(0), new Integer(b.length) };
/*      */       }
/*      */       else {
/*      */         
/* 1159 */         method = defineClass2;
/* 1160 */         args = new Object[] { ct.getName(), b, new Integer(0), new Integer(b.length), domain };
/*      */       } 
/*      */ 
/*      */       
/* 1164 */       return (Class)toClass2(method, loader, args);
/*      */     }
/* 1166 */     catch (RuntimeException e) {
/* 1167 */       throw e;
/*      */     }
/* 1169 */     catch (InvocationTargetException e) {
/* 1170 */       throw new CannotCompileException(e.getTargetException());
/*      */     }
/* 1172 */     catch (Exception e) {
/* 1173 */       throw new CannotCompileException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static synchronized Object toClass2(Method method, ClassLoader loader, Object[] args) throws Exception {
/* 1181 */     method.setAccessible(true);
/*      */     try {
/* 1183 */       return method.invoke(loader, args);
/*      */     } finally {
/*      */       
/* 1186 */       method.setAccessible(false);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void makePackage(ClassLoader loader, String name) throws CannotCompileException {
/*      */     Throwable t;
/* 1210 */     Object[] args = { name, null, null, null, null, null, null, null };
/*      */ 
/*      */     
/*      */     try {
/* 1214 */       toClass2(definePackage, loader, args);
/*      */       
/*      */       return;
/* 1217 */     } catch (InvocationTargetException e) {
/* 1218 */       t = e.getTargetException();
/* 1219 */       if (t == null) {
/* 1220 */         t = e;
/* 1221 */       } else if (t instanceof IllegalArgumentException) {
/*      */         
/*      */         return;
/*      */       }
/*      */     
/*      */     }
/* 1227 */     catch (Exception e) {
/* 1228 */       t = e;
/*      */     } 
/*      */     
/* 1231 */     throw new CannotCompileException(t);
/*      */   }
/*      */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\ClassPool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */