/*      */ package javassist;
/*      */ 
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.DataInputStream;
/*      */ import java.io.DataOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.net.URL;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Hashtable;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import javassist.bytecode.AccessFlag;
/*      */ import javassist.bytecode.AnnotationsAttribute;
/*      */ import javassist.bytecode.AttributeInfo;
/*      */ import javassist.bytecode.BadBytecode;
/*      */ import javassist.bytecode.Bytecode;
/*      */ import javassist.bytecode.ClassFile;
/*      */ import javassist.bytecode.CodeAttribute;
/*      */ import javassist.bytecode.CodeIterator;
/*      */ import javassist.bytecode.ConstPool;
/*      */ import javassist.bytecode.ConstantAttribute;
/*      */ import javassist.bytecode.Descriptor;
/*      */ import javassist.bytecode.EnclosingMethodAttribute;
/*      */ import javassist.bytecode.FieldInfo;
/*      */ import javassist.bytecode.InnerClassesAttribute;
/*      */ import javassist.bytecode.MethodInfo;
/*      */ import javassist.bytecode.ParameterAnnotationsAttribute;
/*      */ import javassist.bytecode.SignatureAttribute;
/*      */ import javassist.bytecode.annotation.Annotation;
/*      */ import javassist.bytecode.annotation.AnnotationImpl;
/*      */ import javassist.compiler.AccessorMaker;
/*      */ import javassist.compiler.CompileError;
/*      */ import javassist.compiler.Javac;
/*      */ import javassist.expr.ExprEditor;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class CtClassType
/*      */   extends CtClass
/*      */ {
/*      */   ClassPool classPool;
/*      */   boolean wasChanged;
/*      */   private boolean wasFrozen;
/*      */   boolean wasPruned;
/*      */   boolean gcConstPool;
/*      */   ClassFile classfile;
/*      */   byte[] rawClassfile;
/*      */   private WeakReference memberCache;
/*      */   private AccessorMaker accessors;
/*      */   private FieldInitLink fieldInitializers;
/*      */   private Hashtable hiddenMethods;
/*      */   private int uniqueNumberSeed;
/*   76 */   private boolean doPruning = ClassPool.doPruning;
/*      */   private int getCount;
/*      */   private static final int GET_THRESHOLD = 2;
/*      */   
/*      */   CtClassType(String name, ClassPool cp) {
/*   81 */     super(name);
/*   82 */     this.classPool = cp;
/*   83 */     this.wasChanged = this.wasFrozen = this.wasPruned = this.gcConstPool = false;
/*   84 */     this.classfile = null;
/*   85 */     this.rawClassfile = null;
/*   86 */     this.memberCache = null;
/*   87 */     this.accessors = null;
/*   88 */     this.fieldInitializers = null;
/*   89 */     this.hiddenMethods = null;
/*   90 */     this.uniqueNumberSeed = 0;
/*   91 */     this.getCount = 0;
/*      */   }
/*      */   
/*      */   CtClassType(InputStream ins, ClassPool cp) throws IOException {
/*   95 */     this((String)null, cp);
/*   96 */     this.classfile = new ClassFile(new DataInputStream(ins));
/*   97 */     this.qualifiedName = this.classfile.getName();
/*      */   }
/*      */   
/*      */   CtClassType(ClassFile cf, ClassPool cp) {
/*  101 */     this((String)null, cp);
/*  102 */     this.classfile = cf;
/*  103 */     this.qualifiedName = this.classfile.getName();
/*      */   }
/*      */   
/*      */   protected void extendToString(StringBuffer buffer) {
/*  107 */     if (this.wasChanged) {
/*  108 */       buffer.append("changed ");
/*      */     }
/*  110 */     if (this.wasFrozen) {
/*  111 */       buffer.append("frozen ");
/*      */     }
/*  113 */     if (this.wasPruned) {
/*  114 */       buffer.append("pruned ");
/*      */     }
/*  116 */     buffer.append(Modifier.toString(getModifiers()));
/*  117 */     buffer.append(" class ");
/*  118 */     buffer.append(getName());
/*      */     
/*      */     try {
/*  121 */       CtClass ext = getSuperclass();
/*  122 */       if (ext != null) {
/*  123 */         String name = ext.getName();
/*  124 */         if (!name.equals("java.lang.Object")) {
/*  125 */           buffer.append(" extends " + ext.getName());
/*      */         }
/*      */       } 
/*  128 */     } catch (NotFoundException e) {
/*  129 */       buffer.append(" extends ??");
/*      */     } 
/*      */     
/*      */     try {
/*  133 */       CtClass[] intf = getInterfaces();
/*  134 */       if (intf.length > 0) {
/*  135 */         buffer.append(" implements ");
/*      */       }
/*  137 */       for (int i = 0; i < intf.length; i++) {
/*  138 */         buffer.append(intf[i].getName());
/*  139 */         buffer.append(", ");
/*      */       }
/*      */     
/*  142 */     } catch (NotFoundException e) {
/*  143 */       buffer.append(" extends ??");
/*      */     } 
/*      */     
/*  146 */     CtMember.Cache memCache = getMembers();
/*  147 */     exToString(buffer, " fields=", memCache
/*  148 */         .fieldHead(), memCache.lastField());
/*  149 */     exToString(buffer, " constructors=", memCache
/*  150 */         .consHead(), memCache.lastCons());
/*  151 */     exToString(buffer, " methods=", memCache
/*  152 */         .methodHead(), memCache.lastMethod());
/*      */   }
/*      */ 
/*      */   
/*      */   private void exToString(StringBuffer buffer, String msg, CtMember head, CtMember tail) {
/*  157 */     buffer.append(msg);
/*  158 */     while (head != tail) {
/*  159 */       head = head.next();
/*  160 */       buffer.append(head);
/*  161 */       buffer.append(", ");
/*      */     } 
/*      */   }
/*      */   
/*      */   public AccessorMaker getAccessorMaker() {
/*  166 */     if (this.accessors == null) {
/*  167 */       this.accessors = new AccessorMaker(this);
/*      */     }
/*  169 */     return this.accessors;
/*      */   }
/*      */   
/*      */   public ClassFile getClassFile2() {
/*  173 */     return getClassFile3(true);
/*      */   }
/*      */   
/*      */   public ClassFile getClassFile3(boolean doCompress) {
/*  177 */     ClassFile cfile = this.classfile;
/*  178 */     if (cfile != null) {
/*  179 */       return cfile;
/*      */     }
/*  181 */     if (doCompress) {
/*  182 */       this.classPool.compress();
/*      */     }
/*  184 */     if (this.rawClassfile != null) {
/*      */       try {
/*  186 */         ClassFile cf = new ClassFile(new DataInputStream(new ByteArrayInputStream(this.rawClassfile)));
/*      */         
/*  188 */         this.rawClassfile = null;
/*  189 */         this.getCount = 2;
/*  190 */         return setClassFile(cf);
/*      */       }
/*  192 */       catch (IOException e) {
/*  193 */         throw new RuntimeException(e.toString(), e);
/*      */       } 
/*      */     }
/*      */     
/*  197 */     InputStream fin = null;
/*      */     try {
/*  199 */       fin = this.classPool.openClassfile(getName());
/*  200 */       if (fin == null) {
/*  201 */         throw new NotFoundException(getName());
/*      */       }
/*  203 */       fin = new BufferedInputStream(fin);
/*  204 */       ClassFile cf = new ClassFile(new DataInputStream(fin));
/*  205 */       if (!cf.getName().equals(this.qualifiedName)) {
/*  206 */         throw new RuntimeException("cannot find " + this.qualifiedName + ": " + cf
/*  207 */             .getName() + " found in " + this.qualifiedName
/*  208 */             .replace('.', '/') + ".class");
/*      */       }
/*  210 */       return setClassFile(cf);
/*      */     }
/*  212 */     catch (NotFoundException e) {
/*  213 */       throw new RuntimeException(e.toString(), e);
/*      */     }
/*  215 */     catch (IOException e) {
/*  216 */       throw new RuntimeException(e.toString(), e);
/*      */     } finally {
/*      */       
/*  219 */       if (fin != null) {
/*      */         try {
/*  221 */           fin.close();
/*      */         }
/*  223 */         catch (IOException iOException) {}
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void incGetCounter() {
/*  232 */     this.getCount++;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void compress() {
/*  240 */     if (this.getCount < 2)
/*  241 */       if (!isModified() && ClassPool.releaseUnmodifiedClassFile) {
/*  242 */         removeClassFile();
/*  243 */       } else if (isFrozen() && !this.wasPruned) {
/*  244 */         saveClassFile();
/*      */       }  
/*  246 */     this.getCount = 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private synchronized void saveClassFile() {
/*  256 */     if (this.classfile == null || hasMemberCache() != null) {
/*      */       return;
/*      */     }
/*  259 */     ByteArrayOutputStream barray = new ByteArrayOutputStream();
/*  260 */     DataOutputStream out = new DataOutputStream(barray);
/*      */     try {
/*  262 */       this.classfile.write(out);
/*  263 */       barray.close();
/*  264 */       this.rawClassfile = barray.toByteArray();
/*  265 */       this.classfile = null;
/*      */     }
/*  267 */     catch (IOException iOException) {}
/*      */   }
/*      */   
/*      */   private synchronized void removeClassFile() {
/*  271 */     if (this.classfile != null && !isModified() && hasMemberCache() == null) {
/*  272 */       this.classfile = null;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private synchronized ClassFile setClassFile(ClassFile cf) {
/*  279 */     if (this.classfile == null) {
/*  280 */       this.classfile = cf;
/*      */     }
/*  282 */     return this.classfile;
/*      */   }
/*      */   public ClassPool getClassPool() {
/*  285 */     return this.classPool;
/*      */   } void setClassPool(ClassPool cp) {
/*  287 */     this.classPool = cp;
/*      */   }
/*      */   public URL getURL() throws NotFoundException {
/*  290 */     URL url = this.classPool.find(getName());
/*  291 */     if (url == null) {
/*  292 */       throw new NotFoundException(getName());
/*      */     }
/*  294 */     return url;
/*      */   }
/*      */   public boolean isModified() {
/*  297 */     return this.wasChanged;
/*      */   } public boolean isFrozen() {
/*  299 */     return this.wasFrozen;
/*      */   } public void freeze() {
/*  301 */     this.wasFrozen = true;
/*      */   }
/*      */   void checkModify() throws RuntimeException {
/*  304 */     if (isFrozen()) {
/*  305 */       String msg = getName() + " class is frozen";
/*  306 */       if (this.wasPruned) {
/*  307 */         msg = msg + " and pruned";
/*      */       }
/*  309 */       throw new RuntimeException(msg);
/*      */     } 
/*      */     
/*  312 */     this.wasChanged = true;
/*      */   }
/*      */   
/*      */   public void defrost() {
/*  316 */     checkPruned("defrost");
/*  317 */     this.wasFrozen = false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean subtypeOf(CtClass clazz) throws NotFoundException {
/*  322 */     String cname = clazz.getName();
/*  323 */     if (this == clazz || getName().equals(cname)) {
/*  324 */       return true;
/*      */     }
/*  326 */     ClassFile file = getClassFile2();
/*  327 */     String supername = file.getSuperclass();
/*  328 */     if (supername != null && supername.equals(cname)) {
/*  329 */       return true;
/*      */     }
/*  331 */     String[] ifs = file.getInterfaces();
/*  332 */     int num = ifs.length; int i;
/*  333 */     for (i = 0; i < num; i++) {
/*  334 */       if (ifs[i].equals(cname))
/*  335 */         return true; 
/*      */     } 
/*  337 */     if (supername != null && this.classPool.get(supername).subtypeOf(clazz)) {
/*  338 */       return true;
/*      */     }
/*  340 */     for (i = 0; i < num; i++) {
/*  341 */       if (this.classPool.get(ifs[i]).subtypeOf(clazz))
/*  342 */         return true; 
/*      */     } 
/*  344 */     return false;
/*      */   }
/*      */   
/*      */   public void setName(String name) throws RuntimeException {
/*  348 */     String oldname = getName();
/*  349 */     if (name.equals(oldname)) {
/*      */       return;
/*      */     }
/*      */     
/*  353 */     this.classPool.checkNotFrozen(name);
/*  354 */     ClassFile cf = getClassFile2();
/*  355 */     super.setName(name);
/*  356 */     cf.setName(name);
/*  357 */     nameReplaced();
/*  358 */     this.classPool.classNameChanged(oldname, this);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getGenericSignature() {
/*  363 */     SignatureAttribute sa = (SignatureAttribute)getClassFile2().getAttribute("Signature");
/*  364 */     return (sa == null) ? null : sa.getSignature();
/*      */   }
/*      */   
/*      */   public void setGenericSignature(String sig) {
/*  368 */     ClassFile cf = getClassFile();
/*  369 */     SignatureAttribute sa = new SignatureAttribute(cf.getConstPool(), sig);
/*  370 */     cf.addAttribute((AttributeInfo)sa);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void replaceClassName(ClassMap classnames) throws RuntimeException {
/*  376 */     String oldClassName = getName();
/*      */     
/*  378 */     String newClassName = (String)classnames.get(Descriptor.toJvmName(oldClassName));
/*  379 */     if (newClassName != null) {
/*  380 */       newClassName = Descriptor.toJavaName(newClassName);
/*      */       
/*  382 */       this.classPool.checkNotFrozen(newClassName);
/*      */     } 
/*      */     
/*  385 */     super.replaceClassName(classnames);
/*  386 */     ClassFile cf = getClassFile2();
/*  387 */     cf.renameClass(classnames);
/*  388 */     nameReplaced();
/*      */     
/*  390 */     if (newClassName != null) {
/*  391 */       super.setName(newClassName);
/*  392 */       this.classPool.classNameChanged(oldClassName, this);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void replaceClassName(String oldname, String newname) throws RuntimeException {
/*  399 */     String thisname = getName();
/*  400 */     if (thisname.equals(oldname)) {
/*  401 */       setName(newname);
/*      */     } else {
/*  403 */       super.replaceClassName(oldname, newname);
/*  404 */       getClassFile2().renameClass(oldname, newname);
/*  405 */       nameReplaced();
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean isInterface() {
/*  410 */     return Modifier.isInterface(getModifiers());
/*      */   }
/*      */   
/*      */   public boolean isAnnotation() {
/*  414 */     return Modifier.isAnnotation(getModifiers());
/*      */   }
/*      */   
/*      */   public boolean isEnum() {
/*  418 */     return Modifier.isEnum(getModifiers());
/*      */   }
/*      */   
/*      */   public int getModifiers() {
/*  422 */     ClassFile cf = getClassFile2();
/*  423 */     int acc = cf.getAccessFlags();
/*  424 */     acc = AccessFlag.clear(acc, 32);
/*  425 */     int inner = cf.getInnerAccessFlags();
/*  426 */     if (inner != -1 && (inner & 0x8) != 0) {
/*  427 */       acc |= 0x8;
/*      */     }
/*  429 */     return AccessFlag.toModifier(acc);
/*      */   }
/*      */   
/*      */   public CtClass[] getNestedClasses() throws NotFoundException {
/*  433 */     ClassFile cf = getClassFile2();
/*      */     
/*  435 */     InnerClassesAttribute ica = (InnerClassesAttribute)cf.getAttribute("InnerClasses");
/*  436 */     if (ica == null) {
/*  437 */       return new CtClass[0];
/*      */     }
/*  439 */     String thisName = cf.getName() + "$";
/*  440 */     int n = ica.tableLength();
/*  441 */     ArrayList<CtClass> list = new ArrayList(n);
/*  442 */     for (int i = 0; i < n; i++) {
/*  443 */       String name = ica.innerClass(i);
/*  444 */       if (name != null && 
/*  445 */         name.startsWith(thisName))
/*      */       {
/*  447 */         if (name.lastIndexOf('$') < thisName.length()) {
/*  448 */           list.add(this.classPool.get(name));
/*      */         }
/*      */       }
/*      */     } 
/*  452 */     return list.<CtClass>toArray(new CtClass[list.size()]);
/*      */   }
/*      */   
/*      */   public void setModifiers(int mod) {
/*  456 */     ClassFile cf = getClassFile2();
/*  457 */     if (Modifier.isStatic(mod)) {
/*  458 */       int flags = cf.getInnerAccessFlags();
/*  459 */       if (flags != -1 && (flags & 0x8) != 0) {
/*  460 */         mod &= 0xFFFFFFF7;
/*      */       } else {
/*  462 */         throw new RuntimeException("cannot change " + getName() + " into a static class");
/*      */       } 
/*      */     } 
/*  465 */     checkModify();
/*  466 */     cf.setAccessFlags(AccessFlag.of(mod));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasAnnotation(String annotationName) {
/*  471 */     ClassFile cf = getClassFile2();
/*      */     
/*  473 */     AnnotationsAttribute ainfo = (AnnotationsAttribute)cf.getAttribute("RuntimeInvisibleAnnotations");
/*      */     
/*  475 */     AnnotationsAttribute ainfo2 = (AnnotationsAttribute)cf.getAttribute("RuntimeVisibleAnnotations");
/*  476 */     return hasAnnotationType(annotationName, getClassPool(), ainfo, ainfo2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean hasAnnotationType(Class clz, ClassPool cp, AnnotationsAttribute a1, AnnotationsAttribute a2) {
/*  486 */     return hasAnnotationType(clz.getName(), cp, a1, a2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean hasAnnotationType(String annotationTypeName, ClassPool cp, AnnotationsAttribute a1, AnnotationsAttribute a2) {
/*      */     Annotation[] anno1;
/*      */     Annotation[] anno2;
/*  495 */     if (a1 == null) {
/*  496 */       anno1 = null;
/*      */     } else {
/*  498 */       anno1 = a1.getAnnotations();
/*      */     } 
/*  500 */     if (a2 == null) {
/*  501 */       anno2 = null;
/*      */     } else {
/*  503 */       anno2 = a2.getAnnotations();
/*      */     } 
/*  505 */     if (anno1 != null)
/*  506 */       for (int i = 0; i < anno1.length; i++) {
/*  507 */         if (anno1[i].getTypeName().equals(annotationTypeName))
/*  508 */           return true; 
/*      */       }  
/*  510 */     if (anno2 != null)
/*  511 */       for (int i = 0; i < anno2.length; i++) {
/*  512 */         if (anno2[i].getTypeName().equals(annotationTypeName))
/*  513 */           return true; 
/*      */       }  
/*  515 */     return false;
/*      */   }
/*      */   
/*      */   public Object getAnnotation(Class clz) throws ClassNotFoundException {
/*  519 */     ClassFile cf = getClassFile2();
/*      */     
/*  521 */     AnnotationsAttribute ainfo = (AnnotationsAttribute)cf.getAttribute("RuntimeInvisibleAnnotations");
/*      */     
/*  523 */     AnnotationsAttribute ainfo2 = (AnnotationsAttribute)cf.getAttribute("RuntimeVisibleAnnotations");
/*  524 */     return getAnnotationType(clz, getClassPool(), ainfo, ainfo2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Object getAnnotationType(Class clz, ClassPool cp, AnnotationsAttribute a1, AnnotationsAttribute a2) throws ClassNotFoundException {
/*      */     Annotation[] anno1, anno2;
/*  533 */     if (a1 == null) {
/*  534 */       anno1 = null;
/*      */     } else {
/*  536 */       anno1 = a1.getAnnotations();
/*      */     } 
/*  538 */     if (a2 == null) {
/*  539 */       anno2 = null;
/*      */     } else {
/*  541 */       anno2 = a2.getAnnotations();
/*      */     } 
/*  543 */     String typeName = clz.getName();
/*  544 */     if (anno1 != null)
/*  545 */       for (int i = 0; i < anno1.length; i++) {
/*  546 */         if (anno1[i].getTypeName().equals(typeName))
/*  547 */           return toAnnoType(anno1[i], cp); 
/*      */       }  
/*  549 */     if (anno2 != null)
/*  550 */       for (int i = 0; i < anno2.length; i++) {
/*  551 */         if (anno2[i].getTypeName().equals(typeName))
/*  552 */           return toAnnoType(anno2[i], cp); 
/*      */       }  
/*  554 */     return null;
/*      */   }
/*      */   
/*      */   public Object[] getAnnotations() throws ClassNotFoundException {
/*  558 */     return getAnnotations(false);
/*      */   }
/*      */   
/*      */   public Object[] getAvailableAnnotations() {
/*      */     try {
/*  563 */       return getAnnotations(true);
/*      */     }
/*  565 */     catch (ClassNotFoundException e) {
/*  566 */       throw new RuntimeException("Unexpected exception ", e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Object[] getAnnotations(boolean ignoreNotFound) throws ClassNotFoundException {
/*  573 */     ClassFile cf = getClassFile2();
/*      */     
/*  575 */     AnnotationsAttribute ainfo = (AnnotationsAttribute)cf.getAttribute("RuntimeInvisibleAnnotations");
/*      */     
/*  577 */     AnnotationsAttribute ainfo2 = (AnnotationsAttribute)cf.getAttribute("RuntimeVisibleAnnotations");
/*  578 */     return toAnnotationType(ignoreNotFound, getClassPool(), ainfo, ainfo2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Object[] toAnnotationType(boolean ignoreNotFound, ClassPool cp, AnnotationsAttribute a1, AnnotationsAttribute a2) throws ClassNotFoundException {
/*      */     Annotation[] anno1, anno2;
/*      */     int size1, size2;
/*  588 */     if (a1 == null) {
/*  589 */       anno1 = null;
/*  590 */       size1 = 0;
/*      */     } else {
/*      */       
/*  593 */       anno1 = a1.getAnnotations();
/*  594 */       size1 = anno1.length;
/*      */     } 
/*      */     
/*  597 */     if (a2 == null) {
/*  598 */       anno2 = null;
/*  599 */       size2 = 0;
/*      */     } else {
/*      */       
/*  602 */       anno2 = a2.getAnnotations();
/*  603 */       size2 = anno2.length;
/*      */     } 
/*      */     
/*  606 */     if (!ignoreNotFound) {
/*  607 */       Object[] result = new Object[size1 + size2];
/*  608 */       for (int m = 0; m < size1; m++) {
/*  609 */         result[m] = toAnnoType(anno1[m], cp);
/*      */       }
/*  611 */       for (int k = 0; k < size2; k++) {
/*  612 */         result[k + size1] = toAnnoType(anno2[k], cp);
/*      */       }
/*  614 */       return result;
/*      */     } 
/*      */     
/*  617 */     ArrayList<Object> annotations = new ArrayList();
/*  618 */     for (int i = 0; i < size1; i++) {
/*      */       try {
/*  620 */         annotations.add(toAnnoType(anno1[i], cp));
/*      */       }
/*  622 */       catch (ClassNotFoundException classNotFoundException) {}
/*      */     } 
/*  624 */     for (int j = 0; j < size2; j++) {
/*      */       try {
/*  626 */         annotations.add(toAnnoType(anno2[j], cp));
/*      */       }
/*  628 */       catch (ClassNotFoundException classNotFoundException) {}
/*      */     } 
/*      */     
/*  631 */     return annotations.toArray();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Object[][] toAnnotationType(boolean ignoreNotFound, ClassPool cp, ParameterAnnotationsAttribute a1, ParameterAnnotationsAttribute a2, MethodInfo minfo) throws ClassNotFoundException {
/*  641 */     int numParameters = 0;
/*  642 */     if (a1 != null) {
/*  643 */       numParameters = a1.numParameters();
/*  644 */     } else if (a2 != null) {
/*  645 */       numParameters = a2.numParameters();
/*      */     } else {
/*  647 */       numParameters = Descriptor.numOfParameters(minfo.getDescriptor());
/*      */     } 
/*  649 */     Object[][] result = new Object[numParameters][];
/*  650 */     for (int i = 0; i < numParameters; i++) {
/*      */       Annotation[] anno1; Annotation[] anno2;
/*      */       int size1;
/*      */       int size2;
/*  654 */       if (a1 == null) {
/*  655 */         anno1 = null;
/*  656 */         size1 = 0;
/*      */       } else {
/*      */         
/*  659 */         anno1 = a1.getAnnotations()[i];
/*  660 */         size1 = anno1.length;
/*      */       } 
/*      */       
/*  663 */       if (a2 == null) {
/*  664 */         anno2 = null;
/*  665 */         size2 = 0;
/*      */       } else {
/*      */         
/*  668 */         anno2 = a2.getAnnotations()[i];
/*  669 */         size2 = anno2.length;
/*      */       } 
/*      */       
/*  672 */       if (!ignoreNotFound) {
/*  673 */         result[i] = new Object[size1 + size2]; int j;
/*  674 */         for (j = 0; j < size1; j++) {
/*  675 */           result[i][j] = toAnnoType(anno1[j], cp);
/*      */         }
/*  677 */         for (j = 0; j < size2; j++) {
/*  678 */           result[i][j + size1] = toAnnoType(anno2[j], cp);
/*      */         }
/*      */       } else {
/*  681 */         ArrayList<Object> annotations = new ArrayList(); int j;
/*  682 */         for (j = 0; j < size1; j++) {
/*      */           try {
/*  684 */             annotations.add(toAnnoType(anno1[j], cp));
/*      */           }
/*  686 */           catch (ClassNotFoundException classNotFoundException) {}
/*      */         } 
/*  688 */         for (j = 0; j < size2; j++) {
/*      */           try {
/*  690 */             annotations.add(toAnnoType(anno2[j], cp));
/*      */           }
/*  692 */           catch (ClassNotFoundException classNotFoundException) {}
/*      */         } 
/*      */         
/*  695 */         result[i] = annotations.toArray();
/*      */       } 
/*      */     } 
/*      */     
/*  699 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static Object toAnnoType(Annotation anno, ClassPool cp) throws ClassNotFoundException {
/*      */     try {
/*  706 */       ClassLoader cl = cp.getClassLoader();
/*  707 */       return anno.toAnnotationType(cl, cp);
/*      */     }
/*  709 */     catch (ClassNotFoundException e) {
/*  710 */       ClassLoader cl2 = cp.getClass().getClassLoader();
/*      */       try {
/*  712 */         return anno.toAnnotationType(cl2, cp);
/*      */       }
/*  714 */       catch (ClassNotFoundException e2) {
/*      */         try {
/*  716 */           Class clazz = cp.get(anno.getTypeName()).toClass();
/*  717 */           return AnnotationImpl.make(clazz
/*  718 */               .getClassLoader(), clazz, cp, anno);
/*      */         
/*      */         }
/*  721 */         catch (Throwable e3) {
/*  722 */           throw new ClassNotFoundException(anno.getTypeName());
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean subclassOf(CtClass superclass) {
/*  729 */     if (superclass == null) {
/*  730 */       return false;
/*      */     }
/*  732 */     String superName = superclass.getName();
/*  733 */     CtClass curr = this;
/*      */     try {
/*  735 */       while (curr != null) {
/*  736 */         if (curr.getName().equals(superName)) {
/*  737 */           return true;
/*      */         }
/*  739 */         curr = curr.getSuperclass();
/*      */       }
/*      */     
/*  742 */     } catch (Exception exception) {}
/*  743 */     return false;
/*      */   }
/*      */   
/*      */   public CtClass getSuperclass() throws NotFoundException {
/*  747 */     String supername = getClassFile2().getSuperclass();
/*  748 */     if (supername == null) {
/*  749 */       return null;
/*      */     }
/*  751 */     return this.classPool.get(supername);
/*      */   }
/*      */   
/*      */   public void setSuperclass(CtClass clazz) throws CannotCompileException {
/*  755 */     checkModify();
/*  756 */     if (isInterface()) {
/*  757 */       addInterface(clazz);
/*      */     } else {
/*  759 */       getClassFile2().setSuperclass(clazz.getName());
/*      */     } 
/*      */   }
/*      */   public CtClass[] getInterfaces() throws NotFoundException {
/*  763 */     String[] ifs = getClassFile2().getInterfaces();
/*  764 */     int num = ifs.length;
/*  765 */     CtClass[] ifc = new CtClass[num];
/*  766 */     for (int i = 0; i < num; i++) {
/*  767 */       ifc[i] = this.classPool.get(ifs[i]);
/*      */     }
/*  769 */     return ifc;
/*      */   }
/*      */   public void setInterfaces(CtClass[] list) {
/*      */     String[] ifs;
/*  773 */     checkModify();
/*      */     
/*  775 */     if (list == null) {
/*  776 */       ifs = new String[0];
/*      */     } else {
/*  778 */       int num = list.length;
/*  779 */       ifs = new String[num];
/*  780 */       for (int i = 0; i < num; i++) {
/*  781 */         ifs[i] = list[i].getName();
/*      */       }
/*      */     } 
/*  784 */     getClassFile2().setInterfaces(ifs);
/*      */   }
/*      */   
/*      */   public void addInterface(CtClass anInterface) {
/*  788 */     checkModify();
/*  789 */     if (anInterface != null)
/*  790 */       getClassFile2().addInterface(anInterface.getName()); 
/*      */   }
/*      */   
/*      */   public CtClass getDeclaringClass() throws NotFoundException {
/*  794 */     ClassFile cf = getClassFile2();
/*  795 */     InnerClassesAttribute ica = (InnerClassesAttribute)cf.getAttribute("InnerClasses");
/*      */     
/*  797 */     if (ica == null) {
/*  798 */       return null;
/*      */     }
/*  800 */     String name = getName();
/*  801 */     int n = ica.tableLength();
/*  802 */     for (int i = 0; i < n; i++) {
/*  803 */       if (name.equals(ica.innerClass(i))) {
/*  804 */         String outName = ica.outerClass(i);
/*  805 */         if (outName != null) {
/*  806 */           return this.classPool.get(outName);
/*      */         }
/*      */ 
/*      */         
/*  810 */         EnclosingMethodAttribute ema = (EnclosingMethodAttribute)cf.getAttribute("EnclosingMethod");
/*      */         
/*  812 */         if (ema != null) {
/*  813 */           return this.classPool.get(ema.className());
/*      */         }
/*      */       } 
/*      */     } 
/*  817 */     return null;
/*      */   }
/*      */   
/*      */   public CtBehavior getEnclosingBehavior() throws NotFoundException {
/*  821 */     ClassFile cf = getClassFile2();
/*      */     
/*  823 */     EnclosingMethodAttribute ema = (EnclosingMethodAttribute)cf.getAttribute("EnclosingMethod");
/*      */     
/*  825 */     if (ema == null) {
/*  826 */       return null;
/*      */     }
/*  828 */     CtClass enc = this.classPool.get(ema.className());
/*  829 */     String name = ema.methodName();
/*  830 */     if ("<init>".equals(name))
/*  831 */       return enc.getConstructor(ema.methodDescriptor()); 
/*  832 */     if ("<clinit>".equals(name)) {
/*  833 */       return enc.getClassInitializer();
/*      */     }
/*  835 */     return enc.getMethod(name, ema.methodDescriptor());
/*      */   }
/*      */ 
/*      */   
/*      */   public CtClass makeNestedClass(String name, boolean isStatic) {
/*  840 */     if (!isStatic) {
/*  841 */       throw new RuntimeException("sorry, only nested static class is supported");
/*      */     }
/*      */     
/*  844 */     checkModify();
/*  845 */     CtClass c = this.classPool.makeNestedClass(getName() + "$" + name);
/*  846 */     ClassFile cf = getClassFile2();
/*  847 */     ClassFile cf2 = c.getClassFile2();
/*  848 */     InnerClassesAttribute ica = (InnerClassesAttribute)cf.getAttribute("InnerClasses");
/*      */     
/*  850 */     if (ica == null) {
/*  851 */       ica = new InnerClassesAttribute(cf.getConstPool());
/*  852 */       cf.addAttribute((AttributeInfo)ica);
/*      */     } 
/*      */     
/*  855 */     ica.append(c.getName(), getName(), name, cf2
/*  856 */         .getAccessFlags() & 0xFFFFFFDF | 0x8);
/*  857 */     cf2.addAttribute(ica.copy(cf2.getConstPool(), null));
/*  858 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void nameReplaced() {
/*  864 */     CtMember.Cache cache = hasMemberCache();
/*  865 */     if (cache != null) {
/*  866 */       CtMember mth = cache.methodHead();
/*  867 */       CtMember tail = cache.lastMethod();
/*  868 */       while (mth != tail) {
/*  869 */         mth = mth.next();
/*  870 */         mth.nameReplaced();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected CtMember.Cache hasMemberCache() {
/*  879 */     WeakReference<CtMember.Cache> cache = this.memberCache;
/*  880 */     if (cache != null) {
/*  881 */       return cache.get();
/*      */     }
/*  883 */     return null;
/*      */   }
/*      */   
/*      */   protected synchronized CtMember.Cache getMembers() {
/*  887 */     CtMember.Cache cache = null;
/*  888 */     if (this.memberCache == null || (
/*  889 */       cache = this.memberCache.get()) == null) {
/*  890 */       cache = new CtMember.Cache(this);
/*  891 */       makeFieldCache(cache);
/*  892 */       makeBehaviorCache(cache);
/*  893 */       this.memberCache = new WeakReference<CtMember.Cache>(cache);
/*      */     } 
/*      */     
/*  896 */     return cache;
/*      */   }
/*      */   
/*      */   private void makeFieldCache(CtMember.Cache cache) {
/*  900 */     List<FieldInfo> list = getClassFile3(false).getFields();
/*  901 */     int n = list.size();
/*  902 */     for (int i = 0; i < n; i++) {
/*  903 */       FieldInfo finfo = list.get(i);
/*  904 */       CtField newField = new CtField(finfo, this);
/*  905 */       cache.addField(newField);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void makeBehaviorCache(CtMember.Cache cache) {
/*  910 */     List<MethodInfo> list = getClassFile3(false).getMethods();
/*  911 */     int n = list.size();
/*  912 */     for (int i = 0; i < n; i++) {
/*  913 */       MethodInfo minfo = list.get(i);
/*  914 */       if (minfo.isMethod()) {
/*  915 */         CtMethod newMethod = new CtMethod(minfo, this);
/*  916 */         cache.addMethod(newMethod);
/*      */       } else {
/*      */         
/*  919 */         CtConstructor newCons = new CtConstructor(minfo, this);
/*  920 */         cache.addConstructor(newCons);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public CtField[] getFields() {
/*  926 */     ArrayList alist = new ArrayList();
/*  927 */     getFields(alist, this);
/*  928 */     return (CtField[])alist.toArray((Object[])new CtField[alist.size()]);
/*      */   }
/*      */ 
/*      */   
/*      */   private static void getFields(ArrayList<CtMember> alist, CtClass cc) {
/*  933 */     if (cc == null) {
/*      */       return;
/*      */     }
/*      */     try {
/*  937 */       getFields(alist, cc.getSuperclass());
/*      */     }
/*  939 */     catch (NotFoundException notFoundException) {}
/*      */     
/*      */     try {
/*  942 */       CtClass[] ifs = cc.getInterfaces();
/*  943 */       int num = ifs.length;
/*  944 */       for (int i = 0; i < num; i++) {
/*  945 */         getFields(alist, ifs[i]);
/*      */       }
/*  947 */     } catch (NotFoundException notFoundException) {}
/*      */     
/*  949 */     CtMember.Cache memCache = ((CtClassType)cc).getMembers();
/*  950 */     CtMember field = memCache.fieldHead();
/*  951 */     CtMember tail = memCache.lastField();
/*  952 */     while (field != tail) {
/*  953 */       field = field.next();
/*  954 */       if (!Modifier.isPrivate(field.getModifiers()))
/*  955 */         alist.add(field); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public CtField getField(String name, String desc) throws NotFoundException {
/*  960 */     CtField f = getField2(name, desc);
/*  961 */     return checkGetField(f, name, desc);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private CtField checkGetField(CtField f, String name, String desc) throws NotFoundException {
/*  967 */     if (f == null) {
/*  968 */       String msg = "field: " + name;
/*  969 */       if (desc != null) {
/*  970 */         msg = msg + " type " + desc;
/*      */       }
/*  972 */       throw new NotFoundException(msg + " in " + getName());
/*      */     } 
/*      */     
/*  975 */     return f;
/*      */   }
/*      */   
/*      */   CtField getField2(String name, String desc) {
/*  979 */     CtField df = getDeclaredField2(name, desc);
/*  980 */     if (df != null) {
/*  981 */       return df;
/*      */     }
/*      */     try {
/*  984 */       CtClass[] ifs = getInterfaces();
/*  985 */       int num = ifs.length;
/*  986 */       for (int i = 0; i < num; i++) {
/*  987 */         CtField f = ifs[i].getField2(name, desc);
/*  988 */         if (f != null) {
/*  989 */           return f;
/*      */         }
/*      */       } 
/*  992 */       CtClass s = getSuperclass();
/*  993 */       if (s != null) {
/*  994 */         return s.getField2(name, desc);
/*      */       }
/*  996 */     } catch (NotFoundException notFoundException) {}
/*  997 */     return null;
/*      */   }
/*      */   
/*      */   public CtField[] getDeclaredFields() {
/* 1001 */     CtMember.Cache memCache = getMembers();
/* 1002 */     CtMember field = memCache.fieldHead();
/* 1003 */     CtMember tail = memCache.lastField();
/* 1004 */     int num = CtMember.Cache.count(field, tail);
/* 1005 */     CtField[] cfs = new CtField[num];
/* 1006 */     int i = 0;
/* 1007 */     while (field != tail) {
/* 1008 */       field = field.next();
/* 1009 */       cfs[i++] = (CtField)field;
/*      */     } 
/*      */     
/* 1012 */     return cfs;
/*      */   }
/*      */   
/*      */   public CtField getDeclaredField(String name) throws NotFoundException {
/* 1016 */     return getDeclaredField(name, (String)null);
/*      */   }
/*      */   
/*      */   public CtField getDeclaredField(String name, String desc) throws NotFoundException {
/* 1020 */     CtField f = getDeclaredField2(name, desc);
/* 1021 */     return checkGetField(f, name, desc);
/*      */   }
/*      */   
/*      */   private CtField getDeclaredField2(String name, String desc) {
/* 1025 */     CtMember.Cache memCache = getMembers();
/* 1026 */     CtMember field = memCache.fieldHead();
/* 1027 */     CtMember tail = memCache.lastField();
/* 1028 */     while (field != tail) {
/* 1029 */       field = field.next();
/* 1030 */       if (field.getName().equals(name) && (desc == null || desc
/* 1031 */         .equals(field.getSignature()))) {
/* 1032 */         return (CtField)field;
/*      */       }
/*      */     } 
/* 1035 */     return null;
/*      */   }
/*      */   
/*      */   public CtBehavior[] getDeclaredBehaviors() {
/* 1039 */     CtMember.Cache memCache = getMembers();
/* 1040 */     CtMember cons = memCache.consHead();
/* 1041 */     CtMember consTail = memCache.lastCons();
/* 1042 */     int cnum = CtMember.Cache.count(cons, consTail);
/* 1043 */     CtMember mth = memCache.methodHead();
/* 1044 */     CtMember mthTail = memCache.lastMethod();
/* 1045 */     int mnum = CtMember.Cache.count(mth, mthTail);
/*      */     
/* 1047 */     CtBehavior[] cb = new CtBehavior[cnum + mnum];
/* 1048 */     int i = 0;
/* 1049 */     while (cons != consTail) {
/* 1050 */       cons = cons.next();
/* 1051 */       cb[i++] = (CtBehavior)cons;
/*      */     } 
/*      */     
/* 1054 */     while (mth != mthTail) {
/* 1055 */       mth = mth.next();
/* 1056 */       cb[i++] = (CtBehavior)mth;
/*      */     } 
/*      */     
/* 1059 */     return cb;
/*      */   }
/*      */   
/*      */   public CtConstructor[] getConstructors() {
/* 1063 */     CtMember.Cache memCache = getMembers();
/* 1064 */     CtMember cons = memCache.consHead();
/* 1065 */     CtMember consTail = memCache.lastCons();
/*      */     
/* 1067 */     int n = 0;
/* 1068 */     CtMember mem = cons;
/* 1069 */     while (mem != consTail) {
/* 1070 */       mem = mem.next();
/* 1071 */       if (isPubCons((CtConstructor)mem)) {
/* 1072 */         n++;
/*      */       }
/*      */     } 
/* 1075 */     CtConstructor[] result = new CtConstructor[n];
/* 1076 */     int i = 0;
/* 1077 */     mem = cons;
/* 1078 */     while (mem != consTail) {
/* 1079 */       mem = mem.next();
/* 1080 */       CtConstructor cc = (CtConstructor)mem;
/* 1081 */       if (isPubCons(cc)) {
/* 1082 */         result[i++] = cc;
/*      */       }
/*      */     } 
/* 1085 */     return result;
/*      */   }
/*      */   
/*      */   private static boolean isPubCons(CtConstructor cons) {
/* 1089 */     return (!Modifier.isPrivate(cons.getModifiers()) && cons
/* 1090 */       .isConstructor());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public CtConstructor getConstructor(String desc) throws NotFoundException {
/* 1096 */     CtMember.Cache memCache = getMembers();
/* 1097 */     CtMember cons = memCache.consHead();
/* 1098 */     CtMember consTail = memCache.lastCons();
/*      */     
/* 1100 */     while (cons != consTail) {
/* 1101 */       cons = cons.next();
/* 1102 */       CtConstructor cc = (CtConstructor)cons;
/* 1103 */       if (cc.getMethodInfo2().getDescriptor().equals(desc) && cc
/* 1104 */         .isConstructor()) {
/* 1105 */         return cc;
/*      */       }
/*      */     } 
/* 1108 */     return super.getConstructor(desc);
/*      */   }
/*      */   
/*      */   public CtConstructor[] getDeclaredConstructors() {
/* 1112 */     CtMember.Cache memCache = getMembers();
/* 1113 */     CtMember cons = memCache.consHead();
/* 1114 */     CtMember consTail = memCache.lastCons();
/*      */     
/* 1116 */     int n = 0;
/* 1117 */     CtMember mem = cons;
/* 1118 */     while (mem != consTail) {
/* 1119 */       mem = mem.next();
/* 1120 */       CtConstructor cc = (CtConstructor)mem;
/* 1121 */       if (cc.isConstructor()) {
/* 1122 */         n++;
/*      */       }
/*      */     } 
/* 1125 */     CtConstructor[] result = new CtConstructor[n];
/* 1126 */     int i = 0;
/* 1127 */     mem = cons;
/* 1128 */     while (mem != consTail) {
/* 1129 */       mem = mem.next();
/* 1130 */       CtConstructor cc = (CtConstructor)mem;
/* 1131 */       if (cc.isConstructor()) {
/* 1132 */         result[i++] = cc;
/*      */       }
/*      */     } 
/* 1135 */     return result;
/*      */   }
/*      */   
/*      */   public CtConstructor getClassInitializer() {
/* 1139 */     CtMember.Cache memCache = getMembers();
/* 1140 */     CtMember cons = memCache.consHead();
/* 1141 */     CtMember consTail = memCache.lastCons();
/*      */     
/* 1143 */     while (cons != consTail) {
/* 1144 */       cons = cons.next();
/* 1145 */       CtConstructor cc = (CtConstructor)cons;
/* 1146 */       if (cc.isClassInitializer()) {
/* 1147 */         return cc;
/*      */       }
/*      */     } 
/* 1150 */     return null;
/*      */   }
/*      */   
/*      */   public CtMethod[] getMethods() {
/* 1154 */     HashMap<Object, Object> h = new HashMap<Object, Object>();
/* 1155 */     getMethods0(h, this);
/* 1156 */     return (CtMethod[])h.values().toArray((Object[])new CtMethod[h.size()]);
/*      */   }
/*      */   
/*      */   private static void getMethods0(HashMap<String, CtMember> h, CtClass cc) {
/*      */     try {
/* 1161 */       CtClass[] ifs = cc.getInterfaces();
/* 1162 */       int size = ifs.length;
/* 1163 */       for (int i = 0; i < size; i++) {
/* 1164 */         getMethods0(h, ifs[i]);
/*      */       }
/* 1166 */     } catch (NotFoundException notFoundException) {}
/*      */     
/*      */     try {
/* 1169 */       CtClass s = cc.getSuperclass();
/* 1170 */       if (s != null) {
/* 1171 */         getMethods0(h, s);
/*      */       }
/* 1173 */     } catch (NotFoundException notFoundException) {}
/*      */     
/* 1175 */     if (cc instanceof CtClassType) {
/* 1176 */       CtMember.Cache memCache = ((CtClassType)cc).getMembers();
/* 1177 */       CtMember mth = memCache.methodHead();
/* 1178 */       CtMember mthTail = memCache.lastMethod();
/*      */       
/* 1180 */       while (mth != mthTail) {
/* 1181 */         mth = mth.next();
/* 1182 */         if (!Modifier.isPrivate(mth.getModifiers())) {
/* 1183 */           h.put(((CtMethod)mth).getStringRep(), mth);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public CtMethod getMethod(String name, String desc) throws NotFoundException {
/* 1191 */     CtMethod m = getMethod0(this, name, desc);
/* 1192 */     if (m != null) {
/* 1193 */       return m;
/*      */     }
/* 1195 */     throw new NotFoundException(name + "(..) is not found in " + 
/* 1196 */         getName());
/*      */   }
/*      */ 
/*      */   
/*      */   private static CtMethod getMethod0(CtClass cc, String name, String desc) {
/* 1201 */     if (cc instanceof CtClassType) {
/* 1202 */       CtMember.Cache memCache = ((CtClassType)cc).getMembers();
/* 1203 */       CtMember mth = memCache.methodHead();
/* 1204 */       CtMember mthTail = memCache.lastMethod();
/*      */       
/* 1206 */       while (mth != mthTail) {
/* 1207 */         mth = mth.next();
/* 1208 */         if (mth.getName().equals(name) && ((CtMethod)mth)
/* 1209 */           .getMethodInfo2().getDescriptor().equals(desc)) {
/* 1210 */           return (CtMethod)mth;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     try {
/* 1215 */       CtClass s = cc.getSuperclass();
/* 1216 */       if (s != null) {
/* 1217 */         CtMethod m = getMethod0(s, name, desc);
/* 1218 */         if (m != null) {
/* 1219 */           return m;
/*      */         }
/*      */       } 
/* 1222 */     } catch (NotFoundException notFoundException) {}
/*      */     
/*      */     try {
/* 1225 */       CtClass[] ifs = cc.getInterfaces();
/* 1226 */       int size = ifs.length;
/* 1227 */       for (int i = 0; i < size; i++) {
/* 1228 */         CtMethod m = getMethod0(ifs[i], name, desc);
/* 1229 */         if (m != null) {
/* 1230 */           return m;
/*      */         }
/*      */       } 
/* 1233 */     } catch (NotFoundException notFoundException) {}
/* 1234 */     return null;
/*      */   }
/*      */   
/*      */   public CtMethod[] getDeclaredMethods() {
/* 1238 */     CtMember.Cache memCache = getMembers();
/* 1239 */     CtMember mth = memCache.methodHead();
/* 1240 */     CtMember mthTail = memCache.lastMethod();
/* 1241 */     int num = CtMember.Cache.count(mth, mthTail);
/* 1242 */     CtMethod[] cms = new CtMethod[num];
/* 1243 */     int i = 0;
/* 1244 */     while (mth != mthTail) {
/* 1245 */       mth = mth.next();
/* 1246 */       cms[i++] = (CtMethod)mth;
/*      */     } 
/*      */     
/* 1249 */     return cms;
/*      */   }
/*      */   
/*      */   public CtMethod[] getDeclaredMethods(String name) throws NotFoundException {
/* 1253 */     CtMember.Cache memCache = getMembers();
/* 1254 */     CtMember mth = memCache.methodHead();
/* 1255 */     CtMember mthTail = memCache.lastMethod();
/* 1256 */     ArrayList<CtMethod> methods = new ArrayList();
/* 1257 */     while (mth != mthTail) {
/* 1258 */       mth = mth.next();
/* 1259 */       if (mth.getName().equals(name)) {
/* 1260 */         methods.add((CtMethod)mth);
/*      */       }
/*      */     } 
/* 1263 */     return methods.<CtMethod>toArray(new CtMethod[methods.size()]);
/*      */   }
/*      */   
/*      */   public CtMethod getDeclaredMethod(String name) throws NotFoundException {
/* 1267 */     CtMember.Cache memCache = getMembers();
/* 1268 */     CtMember mth = memCache.methodHead();
/* 1269 */     CtMember mthTail = memCache.lastMethod();
/* 1270 */     while (mth != mthTail) {
/* 1271 */       mth = mth.next();
/* 1272 */       if (mth.getName().equals(name)) {
/* 1273 */         return (CtMethod)mth;
/*      */       }
/*      */     } 
/* 1276 */     throw new NotFoundException(name + "(..) is not found in " + 
/* 1277 */         getName());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public CtMethod getDeclaredMethod(String name, CtClass[] params) throws NotFoundException {
/* 1283 */     String desc = Descriptor.ofParameters(params);
/* 1284 */     CtMember.Cache memCache = getMembers();
/* 1285 */     CtMember mth = memCache.methodHead();
/* 1286 */     CtMember mthTail = memCache.lastMethod();
/*      */     
/* 1288 */     while (mth != mthTail) {
/* 1289 */       mth = mth.next();
/* 1290 */       if (mth.getName().equals(name) && ((CtMethod)mth)
/* 1291 */         .getMethodInfo2().getDescriptor().startsWith(desc)) {
/* 1292 */         return (CtMethod)mth;
/*      */       }
/*      */     } 
/* 1295 */     throw new NotFoundException(name + "(..) is not found in " + 
/* 1296 */         getName());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void addField(CtField f, String init) throws CannotCompileException {
/* 1302 */     addField(f, CtField.Initializer.byExpr(init));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void addField(CtField f, CtField.Initializer init) throws CannotCompileException {
/* 1308 */     checkModify();
/* 1309 */     if (f.getDeclaringClass() != this) {
/* 1310 */       throw new CannotCompileException("cannot add");
/*      */     }
/* 1312 */     if (init == null) {
/* 1313 */       init = f.getInit();
/*      */     }
/* 1315 */     if (init != null) {
/* 1316 */       init.check(f.getSignature());
/* 1317 */       int mod = f.getModifiers();
/* 1318 */       if (Modifier.isStatic(mod) && Modifier.isFinal(mod)) {
/*      */         try {
/* 1320 */           ConstPool cp = getClassFile2().getConstPool();
/* 1321 */           int index = init.getConstantValue(cp, f.getType());
/* 1322 */           if (index != 0) {
/* 1323 */             f.getFieldInfo2().addAttribute((AttributeInfo)new ConstantAttribute(cp, index));
/* 1324 */             init = null;
/*      */           }
/*      */         
/* 1327 */         } catch (NotFoundException notFoundException) {}
/*      */       }
/*      */     } 
/* 1330 */     getMembers().addField(f);
/* 1331 */     getClassFile2().addField(f.getFieldInfo2());
/*      */     
/* 1333 */     if (init != null) {
/* 1334 */       FieldInitLink fil = new FieldInitLink(f, init);
/* 1335 */       FieldInitLink link = this.fieldInitializers;
/* 1336 */       if (link == null) {
/* 1337 */         this.fieldInitializers = fil;
/*      */       } else {
/* 1339 */         while (link.next != null) {
/* 1340 */           link = link.next;
/*      */         }
/* 1342 */         link.next = fil;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void removeField(CtField f) throws NotFoundException {
/* 1348 */     checkModify();
/* 1349 */     FieldInfo fi = f.getFieldInfo2();
/* 1350 */     ClassFile cf = getClassFile2();
/* 1351 */     if (cf.getFields().remove(fi)) {
/* 1352 */       getMembers().remove(f);
/* 1353 */       this.gcConstPool = true;
/*      */     } else {
/*      */       
/* 1356 */       throw new NotFoundException(f.toString());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public CtConstructor makeClassInitializer() throws CannotCompileException {
/* 1362 */     CtConstructor clinit = getClassInitializer();
/* 1363 */     if (clinit != null) {
/* 1364 */       return clinit;
/*      */     }
/* 1366 */     checkModify();
/* 1367 */     ClassFile cf = getClassFile2();
/* 1368 */     Bytecode code = new Bytecode(cf.getConstPool(), 0, 0);
/* 1369 */     modifyClassConstructor(cf, code, 0, 0);
/* 1370 */     return getClassInitializer();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void addConstructor(CtConstructor c) throws CannotCompileException {
/* 1376 */     checkModify();
/* 1377 */     if (c.getDeclaringClass() != this) {
/* 1378 */       throw new CannotCompileException("cannot add");
/*      */     }
/* 1380 */     getMembers().addConstructor(c);
/* 1381 */     getClassFile2().addMethod(c.getMethodInfo2());
/*      */   }
/*      */   
/*      */   public void removeConstructor(CtConstructor m) throws NotFoundException {
/* 1385 */     checkModify();
/* 1386 */     MethodInfo mi = m.getMethodInfo2();
/* 1387 */     ClassFile cf = getClassFile2();
/* 1388 */     if (cf.getMethods().remove(mi)) {
/* 1389 */       getMembers().remove(m);
/* 1390 */       this.gcConstPool = true;
/*      */     } else {
/*      */       
/* 1393 */       throw new NotFoundException(m.toString());
/*      */     } 
/*      */   }
/*      */   public void addMethod(CtMethod m) throws CannotCompileException {
/* 1397 */     checkModify();
/* 1398 */     if (m.getDeclaringClass() != this) {
/* 1399 */       throw new CannotCompileException("bad declaring class");
/*      */     }
/* 1401 */     int mod = m.getModifiers();
/* 1402 */     if ((getModifiers() & 0x200) != 0) {
/* 1403 */       if (Modifier.isProtected(mod) || Modifier.isPrivate(mod)) {
/* 1404 */         throw new CannotCompileException("an interface method must be public: " + m
/* 1405 */             .toString());
/*      */       }
/* 1407 */       m.setModifiers(mod | 0x1);
/*      */     } 
/*      */     
/* 1410 */     getMembers().addMethod(m);
/* 1411 */     getClassFile2().addMethod(m.getMethodInfo2());
/* 1412 */     if ((mod & 0x400) != 0)
/* 1413 */       setModifiers(getModifiers() | 0x400); 
/*      */   }
/*      */   
/*      */   public void removeMethod(CtMethod m) throws NotFoundException {
/* 1417 */     checkModify();
/* 1418 */     MethodInfo mi = m.getMethodInfo2();
/* 1419 */     ClassFile cf = getClassFile2();
/* 1420 */     if (cf.getMethods().remove(mi)) {
/* 1421 */       getMembers().remove(m);
/* 1422 */       this.gcConstPool = true;
/*      */     } else {
/*      */       
/* 1425 */       throw new NotFoundException(m.toString());
/*      */     } 
/*      */   }
/*      */   public byte[] getAttribute(String name) {
/* 1429 */     AttributeInfo ai = getClassFile2().getAttribute(name);
/* 1430 */     if (ai == null) {
/* 1431 */       return null;
/*      */     }
/* 1433 */     return ai.get();
/*      */   }
/*      */   
/*      */   public void setAttribute(String name, byte[] data) {
/* 1437 */     checkModify();
/* 1438 */     ClassFile cf = getClassFile2();
/* 1439 */     cf.addAttribute(new AttributeInfo(cf.getConstPool(), name, data));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void instrument(CodeConverter converter) throws CannotCompileException {
/* 1445 */     checkModify();
/* 1446 */     ClassFile cf = getClassFile2();
/* 1447 */     ConstPool cp = cf.getConstPool();
/* 1448 */     List<MethodInfo> list = cf.getMethods();
/* 1449 */     int n = list.size();
/* 1450 */     for (int i = 0; i < n; i++) {
/* 1451 */       MethodInfo minfo = list.get(i);
/* 1452 */       converter.doit(this, minfo, cp);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void instrument(ExprEditor editor) throws CannotCompileException {
/* 1459 */     checkModify();
/* 1460 */     ClassFile cf = getClassFile2();
/* 1461 */     List<MethodInfo> list = cf.getMethods();
/* 1462 */     int n = list.size();
/* 1463 */     for (int i = 0; i < n; i++) {
/* 1464 */       MethodInfo minfo = list.get(i);
/* 1465 */       editor.doit(this, minfo);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void prune() {
/* 1474 */     if (this.wasPruned) {
/*      */       return;
/*      */     }
/* 1477 */     this.wasPruned = this.wasFrozen = true;
/* 1478 */     getClassFile2().prune();
/*      */   }
/*      */   public void rebuildClassFile() {
/* 1481 */     this.gcConstPool = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void toBytecode(DataOutputStream out) throws CannotCompileException, IOException {
/*      */     try {
/* 1487 */       if (isModified()) {
/* 1488 */         checkPruned("toBytecode");
/* 1489 */         ClassFile cf = getClassFile2();
/* 1490 */         if (this.gcConstPool) {
/* 1491 */           cf.compact();
/* 1492 */           this.gcConstPool = false;
/*      */         } 
/*      */         
/* 1495 */         modifyClassConstructor(cf);
/* 1496 */         modifyConstructors(cf);
/* 1497 */         if (debugDump != null) {
/* 1498 */           dumpClassFile(cf);
/*      */         }
/* 1500 */         cf.write(out);
/* 1501 */         out.flush();
/* 1502 */         this.fieldInitializers = null;
/* 1503 */         if (this.doPruning) {
/*      */           
/* 1505 */           cf.prune();
/* 1506 */           this.wasPruned = true;
/*      */         } 
/*      */       } else {
/*      */         
/* 1510 */         this.classPool.writeClassfile(getName(), out);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1515 */       this.getCount = 0;
/* 1516 */       this.wasFrozen = true;
/*      */     }
/* 1518 */     catch (NotFoundException e) {
/* 1519 */       throw new CannotCompileException(e);
/*      */     }
/* 1521 */     catch (IOException e) {
/* 1522 */       throw new CannotCompileException(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void dumpClassFile(ClassFile cf) throws IOException {
/* 1527 */     DataOutputStream dump = makeFileOutput(debugDump);
/*      */     try {
/* 1529 */       cf.write(dump);
/*      */     } finally {
/*      */       
/* 1532 */       dump.close();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkPruned(String method) {
/* 1539 */     if (this.wasPruned) {
/* 1540 */       throw new RuntimeException(method + "(): " + getName() + " was pruned.");
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean stopPruning(boolean stop) {
/* 1545 */     boolean prev = !this.doPruning;
/* 1546 */     this.doPruning = !stop;
/* 1547 */     return prev;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void modifyClassConstructor(ClassFile cf) throws CannotCompileException, NotFoundException {
/* 1553 */     if (this.fieldInitializers == null) {
/*      */       return;
/*      */     }
/* 1556 */     Bytecode code = new Bytecode(cf.getConstPool(), 0, 0);
/* 1557 */     Javac jv = new Javac(code, this);
/* 1558 */     int stacksize = 0;
/* 1559 */     boolean doInit = false;
/* 1560 */     for (FieldInitLink fi = this.fieldInitializers; fi != null; fi = fi.next) {
/* 1561 */       CtField f = fi.field;
/* 1562 */       if (Modifier.isStatic(f.getModifiers())) {
/* 1563 */         doInit = true;
/* 1564 */         int s = fi.init.compileIfStatic(f.getType(), f.getName(), code, jv);
/*      */         
/* 1566 */         if (stacksize < s) {
/* 1567 */           stacksize = s;
/*      */         }
/*      */       } 
/*      */     } 
/* 1571 */     if (doInit) {
/* 1572 */       modifyClassConstructor(cf, code, stacksize, 0);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void modifyClassConstructor(ClassFile cf, Bytecode code, int stacksize, int localsize) throws CannotCompileException {
/* 1579 */     MethodInfo m = cf.getStaticInitializer();
/* 1580 */     if (m == null) {
/* 1581 */       code.add(177);
/* 1582 */       code.setMaxStack(stacksize);
/* 1583 */       code.setMaxLocals(localsize);
/* 1584 */       m = new MethodInfo(cf.getConstPool(), "<clinit>", "()V");
/* 1585 */       m.setAccessFlags(8);
/* 1586 */       m.setCodeAttribute(code.toCodeAttribute());
/* 1587 */       cf.addMethod(m);
/* 1588 */       CtMember.Cache cache = hasMemberCache();
/* 1589 */       if (cache != null) {
/* 1590 */         cache.addConstructor(new CtConstructor(m, this));
/*      */       }
/*      */     } else {
/* 1593 */       CodeAttribute codeAttr = m.getCodeAttribute();
/* 1594 */       if (codeAttr == null) {
/* 1595 */         throw new CannotCompileException("empty <clinit>");
/*      */       }
/*      */       try {
/* 1598 */         CodeIterator it = codeAttr.iterator();
/* 1599 */         int pos = it.insertEx(code.get());
/* 1600 */         it.insert(code.getExceptionTable(), pos);
/* 1601 */         int maxstack = codeAttr.getMaxStack();
/* 1602 */         if (maxstack < stacksize) {
/* 1603 */           codeAttr.setMaxStack(stacksize);
/*      */         }
/* 1605 */         int maxlocals = codeAttr.getMaxLocals();
/* 1606 */         if (maxlocals < localsize) {
/* 1607 */           codeAttr.setMaxLocals(localsize);
/*      */         }
/* 1609 */       } catch (BadBytecode e) {
/* 1610 */         throw new CannotCompileException(e);
/*      */       } 
/*      */     } 
/*      */     
/*      */     try {
/* 1615 */       m.rebuildStackMapIf6(this.classPool, cf);
/*      */     }
/* 1617 */     catch (BadBytecode e) {
/* 1618 */       throw new CannotCompileException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void modifyConstructors(ClassFile cf) throws CannotCompileException, NotFoundException {
/* 1625 */     if (this.fieldInitializers == null) {
/*      */       return;
/*      */     }
/* 1628 */     ConstPool cp = cf.getConstPool();
/* 1629 */     List<MethodInfo> list = cf.getMethods();
/* 1630 */     int n = list.size();
/* 1631 */     for (int i = 0; i < n; i++) {
/* 1632 */       MethodInfo minfo = list.get(i);
/* 1633 */       if (minfo.isConstructor()) {
/* 1634 */         CodeAttribute codeAttr = minfo.getCodeAttribute();
/* 1635 */         if (codeAttr != null) {
/*      */           
/*      */           try {
/* 1638 */             Bytecode init = new Bytecode(cp, 0, codeAttr.getMaxLocals());
/*      */             
/* 1640 */             CtClass[] params = Descriptor.getParameterTypes(minfo
/* 1641 */                 .getDescriptor(), this.classPool);
/*      */             
/* 1643 */             int stacksize = makeFieldInitializer(init, params);
/* 1644 */             insertAuxInitializer(codeAttr, init, stacksize);
/* 1645 */             minfo.rebuildStackMapIf6(this.classPool, cf);
/*      */           }
/* 1647 */           catch (BadBytecode e) {
/* 1648 */             throw new CannotCompileException(e);
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void insertAuxInitializer(CodeAttribute codeAttr, Bytecode initializer, int stacksize) throws BadBytecode {
/* 1659 */     CodeIterator it = codeAttr.iterator();
/* 1660 */     int index = it.skipSuperConstructor();
/* 1661 */     if (index < 0) {
/* 1662 */       index = it.skipThisConstructor();
/* 1663 */       if (index >= 0) {
/*      */         return;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1669 */     int pos = it.insertEx(initializer.get());
/* 1670 */     it.insert(initializer.getExceptionTable(), pos);
/* 1671 */     int maxstack = codeAttr.getMaxStack();
/* 1672 */     if (maxstack < stacksize) {
/* 1673 */       codeAttr.setMaxStack(stacksize);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private int makeFieldInitializer(Bytecode code, CtClass[] parameters) throws CannotCompileException, NotFoundException {
/* 1679 */     int stacksize = 0;
/* 1680 */     Javac jv = new Javac(code, this);
/*      */     try {
/* 1682 */       jv.recordParams(parameters, false);
/*      */     }
/* 1684 */     catch (CompileError e) {
/* 1685 */       throw new CannotCompileException(e);
/*      */     } 
/*      */     
/* 1688 */     for (FieldInitLink fi = this.fieldInitializers; fi != null; fi = fi.next) {
/* 1689 */       CtField f = fi.field;
/* 1690 */       if (!Modifier.isStatic(f.getModifiers())) {
/* 1691 */         int s = fi.init.compile(f.getType(), f.getName(), code, parameters, jv);
/*      */         
/* 1693 */         if (stacksize < s) {
/* 1694 */           stacksize = s;
/*      */         }
/*      */       } 
/*      */     } 
/* 1698 */     return stacksize;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   Hashtable getHiddenMethods() {
/* 1704 */     if (this.hiddenMethods == null) {
/* 1705 */       this.hiddenMethods = new Hashtable<Object, Object>();
/*      */     }
/* 1707 */     return this.hiddenMethods;
/*      */   }
/*      */   int getUniqueNumber() {
/* 1710 */     return this.uniqueNumberSeed++;
/*      */   }
/*      */   public String makeUniqueName(String prefix) {
/* 1713 */     HashMap<Object, Object> table = new HashMap<Object, Object>();
/* 1714 */     makeMemberList(table);
/* 1715 */     Set keys = table.keySet();
/* 1716 */     String[] methods = new String[keys.size()];
/* 1717 */     keys.toArray((Object[])methods);
/*      */     
/* 1719 */     if (notFindInArray(prefix, methods)) {
/* 1720 */       return prefix;
/*      */     }
/* 1722 */     int i = 100;
/*      */     
/*      */     while (true) {
/* 1725 */       if (i > 999) {
/* 1726 */         throw new RuntimeException("too many unique name");
/*      */       }
/* 1728 */       String name = prefix + i++;
/* 1729 */       if (notFindInArray(name, methods))
/* 1730 */         return name; 
/*      */     } 
/*      */   }
/*      */   private static boolean notFindInArray(String prefix, String[] values) {
/* 1734 */     int len = values.length;
/* 1735 */     for (int i = 0; i < len; i++) {
/* 1736 */       if (values[i].startsWith(prefix))
/* 1737 */         return false; 
/*      */     } 
/* 1739 */     return true;
/*      */   }
/*      */   
/*      */   private void makeMemberList(HashMap<String, CtClassType> table) {
/* 1743 */     int mod = getModifiers();
/* 1744 */     if (Modifier.isAbstract(mod) || Modifier.isInterface(mod)) {
/*      */       try {
/* 1746 */         CtClass[] ifs = getInterfaces();
/* 1747 */         int size = ifs.length;
/* 1748 */         for (int j = 0; j < size; j++) {
/* 1749 */           CtClass ic = ifs[j];
/* 1750 */           if (ic != null && ic instanceof CtClassType) {
/* 1751 */             ((CtClassType)ic).makeMemberList(table);
/*      */           }
/*      */         } 
/* 1754 */       } catch (NotFoundException notFoundException) {}
/*      */     }
/*      */     try {
/* 1757 */       CtClass s = getSuperclass();
/* 1758 */       if (s != null && s instanceof CtClassType) {
/* 1759 */         ((CtClassType)s).makeMemberList(table);
/*      */       }
/* 1761 */     } catch (NotFoundException notFoundException) {}
/*      */     
/* 1763 */     List<MethodInfo> list = getClassFile2().getMethods();
/* 1764 */     int n = list.size(); int i;
/* 1765 */     for (i = 0; i < n; i++) {
/* 1766 */       MethodInfo minfo = list.get(i);
/* 1767 */       table.put(minfo.getName(), this);
/*      */     } 
/*      */     
/* 1770 */     list = getClassFile2().getFields();
/* 1771 */     n = list.size();
/* 1772 */     for (i = 0; i < n; i++) {
/* 1773 */       FieldInfo finfo = (FieldInfo)list.get(i);
/* 1774 */       table.put(finfo.getName(), this);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\CtClassType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */