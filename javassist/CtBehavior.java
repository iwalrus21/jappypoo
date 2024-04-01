/*      */ package javassist;
/*      */ 
/*      */ import javassist.bytecode.AccessFlag;
/*      */ import javassist.bytecode.AnnotationsAttribute;
/*      */ import javassist.bytecode.AttributeInfo;
/*      */ import javassist.bytecode.BadBytecode;
/*      */ import javassist.bytecode.Bytecode;
/*      */ import javassist.bytecode.CodeAttribute;
/*      */ import javassist.bytecode.CodeIterator;
/*      */ import javassist.bytecode.ConstPool;
/*      */ import javassist.bytecode.Descriptor;
/*      */ import javassist.bytecode.ExceptionsAttribute;
/*      */ import javassist.bytecode.LineNumberAttribute;
/*      */ import javassist.bytecode.LocalVariableAttribute;
/*      */ import javassist.bytecode.LocalVariableTypeAttribute;
/*      */ import javassist.bytecode.MethodInfo;
/*      */ import javassist.bytecode.ParameterAnnotationsAttribute;
/*      */ import javassist.bytecode.SignatureAttribute;
/*      */ import javassist.bytecode.StackMap;
/*      */ import javassist.bytecode.StackMapTable;
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
/*      */ public abstract class CtBehavior
/*      */   extends CtMember
/*      */ {
/*      */   protected MethodInfo methodInfo;
/*      */   
/*      */   protected CtBehavior(CtClass clazz, MethodInfo minfo) {
/*   39 */     super(clazz);
/*   40 */     this.methodInfo = minfo;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void copy(CtBehavior src, boolean isCons, ClassMap map) throws CannotCompileException {
/*   49 */     CtClass declaring = this.declaringClass;
/*   50 */     MethodInfo srcInfo = src.methodInfo;
/*   51 */     CtClass srcClass = src.getDeclaringClass();
/*   52 */     ConstPool cp = declaring.getClassFile2().getConstPool();
/*      */     
/*   54 */     map = new ClassMap(map);
/*   55 */     map.put(srcClass.getName(), declaring.getName());
/*      */     try {
/*   57 */       boolean patch = false;
/*   58 */       CtClass srcSuper = srcClass.getSuperclass();
/*   59 */       CtClass destSuper = declaring.getSuperclass();
/*   60 */       String destSuperName = null;
/*   61 */       if (srcSuper != null && destSuper != null) {
/*   62 */         String srcSuperName = srcSuper.getName();
/*   63 */         destSuperName = destSuper.getName();
/*   64 */         if (!srcSuperName.equals(destSuperName)) {
/*   65 */           if (srcSuperName.equals("java.lang.Object")) {
/*   66 */             patch = true;
/*      */           } else {
/*   68 */             map.putIfNone(srcSuperName, destSuperName);
/*      */           } 
/*      */         }
/*      */       } 
/*   72 */       this.methodInfo = new MethodInfo(cp, srcInfo.getName(), srcInfo, map);
/*   73 */       if (isCons && patch) {
/*   74 */         this.methodInfo.setSuperclass(destSuperName);
/*      */       }
/*   76 */     } catch (NotFoundException e) {
/*   77 */       throw new CannotCompileException(e);
/*      */     }
/*   79 */     catch (BadBytecode e) {
/*   80 */       throw new CannotCompileException(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void extendToString(StringBuffer buffer) {
/*   85 */     buffer.append(' ');
/*   86 */     buffer.append(getName());
/*   87 */     buffer.append(' ');
/*   88 */     buffer.append(this.methodInfo.getDescriptor());
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
/*      */   public abstract String getLongName();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MethodInfo getMethodInfo() {
/*  111 */     this.declaringClass.checkModify();
/*  112 */     return this.methodInfo;
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
/*      */   public MethodInfo getMethodInfo2() {
/*  134 */     return this.methodInfo;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getModifiers() {
/*  144 */     return AccessFlag.toModifier(this.methodInfo.getAccessFlags());
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
/*      */   public void setModifiers(int mod) {
/*  157 */     this.declaringClass.checkModify();
/*  158 */     this.methodInfo.setAccessFlags(AccessFlag.of(mod));
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
/*      */   public boolean hasAnnotation(String typeName) {
/*  170 */     MethodInfo mi = getMethodInfo2();
/*      */     
/*  172 */     AnnotationsAttribute ainfo = (AnnotationsAttribute)mi.getAttribute("RuntimeInvisibleAnnotations");
/*      */     
/*  174 */     AnnotationsAttribute ainfo2 = (AnnotationsAttribute)mi.getAttribute("RuntimeVisibleAnnotations");
/*  175 */     return CtClassType.hasAnnotationType(typeName, 
/*  176 */         getDeclaringClass().getClassPool(), ainfo, ainfo2);
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
/*      */   public Object getAnnotation(Class clz) throws ClassNotFoundException {
/*  192 */     MethodInfo mi = getMethodInfo2();
/*      */     
/*  194 */     AnnotationsAttribute ainfo = (AnnotationsAttribute)mi.getAttribute("RuntimeInvisibleAnnotations");
/*      */     
/*  196 */     AnnotationsAttribute ainfo2 = (AnnotationsAttribute)mi.getAttribute("RuntimeVisibleAnnotations");
/*  197 */     return CtClassType.getAnnotationType(clz, 
/*  198 */         getDeclaringClass().getClassPool(), ainfo, ainfo2);
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
/*      */   public Object[] getAnnotations() throws ClassNotFoundException {
/*  210 */     return getAnnotations(false);
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
/*      */   public Object[] getAvailableAnnotations() {
/*      */     try {
/*  224 */       return getAnnotations(true);
/*      */     }
/*  226 */     catch (ClassNotFoundException e) {
/*  227 */       throw new RuntimeException("Unexpected exception", e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Object[] getAnnotations(boolean ignoreNotFound) throws ClassNotFoundException {
/*  234 */     MethodInfo mi = getMethodInfo2();
/*      */     
/*  236 */     AnnotationsAttribute ainfo = (AnnotationsAttribute)mi.getAttribute("RuntimeInvisibleAnnotations");
/*      */     
/*  238 */     AnnotationsAttribute ainfo2 = (AnnotationsAttribute)mi.getAttribute("RuntimeVisibleAnnotations");
/*  239 */     return CtClassType.toAnnotationType(ignoreNotFound, 
/*  240 */         getDeclaringClass().getClassPool(), ainfo, ainfo2);
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
/*      */   public Object[][] getParameterAnnotations() throws ClassNotFoundException {
/*  256 */     return getParameterAnnotations(false);
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
/*      */   public Object[][] getAvailableParameterAnnotations() {
/*      */     try {
/*  274 */       return getParameterAnnotations(true);
/*      */     }
/*  276 */     catch (ClassNotFoundException e) {
/*  277 */       throw new RuntimeException("Unexpected exception", e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   Object[][] getParameterAnnotations(boolean ignoreNotFound) throws ClassNotFoundException {
/*  284 */     MethodInfo mi = getMethodInfo2();
/*      */     
/*  286 */     ParameterAnnotationsAttribute ainfo = (ParameterAnnotationsAttribute)mi.getAttribute("RuntimeInvisibleParameterAnnotations");
/*      */     
/*  288 */     ParameterAnnotationsAttribute ainfo2 = (ParameterAnnotationsAttribute)mi.getAttribute("RuntimeVisibleParameterAnnotations");
/*  289 */     return CtClassType.toAnnotationType(ignoreNotFound, 
/*  290 */         getDeclaringClass().getClassPool(), ainfo, ainfo2, mi);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtClass[] getParameterTypes() throws NotFoundException {
/*  298 */     return Descriptor.getParameterTypes(this.methodInfo.getDescriptor(), this.declaringClass
/*  299 */         .getClassPool());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   CtClass getReturnType0() throws NotFoundException {
/*  306 */     return Descriptor.getReturnType(this.methodInfo.getDescriptor(), this.declaringClass
/*  307 */         .getClassPool());
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
/*      */   public String getSignature() {
/*  328 */     return this.methodInfo.getDescriptor();
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
/*      */   public String getGenericSignature() {
/*  340 */     SignatureAttribute sa = (SignatureAttribute)this.methodInfo.getAttribute("Signature");
/*  341 */     return (sa == null) ? null : sa.getSignature();
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
/*      */   public void setGenericSignature(String sig) {
/*  355 */     this.declaringClass.checkModify();
/*  356 */     this.methodInfo.addAttribute((AttributeInfo)new SignatureAttribute(this.methodInfo.getConstPool(), sig));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtClass[] getExceptionTypes() throws NotFoundException {
/*      */     String[] exceptions;
/*  366 */     ExceptionsAttribute ea = this.methodInfo.getExceptionsAttribute();
/*  367 */     if (ea == null) {
/*  368 */       exceptions = null;
/*      */     } else {
/*  370 */       exceptions = ea.getExceptions();
/*      */     } 
/*  372 */     return this.declaringClass.getClassPool().get(exceptions);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setExceptionTypes(CtClass[] types) throws NotFoundException {
/*  379 */     this.declaringClass.checkModify();
/*  380 */     if (types == null || types.length == 0) {
/*  381 */       this.methodInfo.removeExceptionsAttribute();
/*      */       
/*      */       return;
/*      */     } 
/*  385 */     String[] names = new String[types.length];
/*  386 */     for (int i = 0; i < types.length; i++) {
/*  387 */       names[i] = types[i].getName();
/*      */     }
/*  389 */     ExceptionsAttribute ea = this.methodInfo.getExceptionsAttribute();
/*  390 */     if (ea == null) {
/*  391 */       ea = new ExceptionsAttribute(this.methodInfo.getConstPool());
/*  392 */       this.methodInfo.setExceptionsAttribute(ea);
/*      */     } 
/*      */     
/*  395 */     ea.setExceptions(names);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract boolean isEmpty();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBody(String src) throws CannotCompileException {
/*  412 */     setBody(src, (String)null, (String)null);
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
/*      */   public void setBody(String src, String delegateObj, String delegateMethod) throws CannotCompileException {
/*  431 */     CtClass cc = this.declaringClass;
/*  432 */     cc.checkModify();
/*      */     try {
/*  434 */       Javac jv = new Javac(cc);
/*  435 */       if (delegateMethod != null) {
/*  436 */         jv.recordProceed(delegateObj, delegateMethod);
/*      */       }
/*  438 */       Bytecode b = jv.compileBody(this, src);
/*  439 */       this.methodInfo.setCodeAttribute(b.toCodeAttribute());
/*  440 */       this.methodInfo.setAccessFlags(this.methodInfo.getAccessFlags() & 0xFFFFFBFF);
/*      */       
/*  442 */       this.methodInfo.rebuildStackMapIf6(cc.getClassPool(), cc.getClassFile2());
/*  443 */       this.declaringClass.rebuildClassFile();
/*      */     }
/*  445 */     catch (CompileError e) {
/*  446 */       throw new CannotCompileException(e);
/*  447 */     } catch (BadBytecode e) {
/*  448 */       throw new CannotCompileException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void setBody0(CtClass srcClass, MethodInfo srcInfo, CtClass destClass, MethodInfo destInfo, ClassMap map) throws CannotCompileException {
/*  457 */     destClass.checkModify();
/*      */     
/*  459 */     map = new ClassMap(map);
/*  460 */     map.put(srcClass.getName(), destClass.getName());
/*      */     try {
/*  462 */       CodeAttribute cattr = srcInfo.getCodeAttribute();
/*  463 */       if (cattr != null) {
/*  464 */         ConstPool cp = destInfo.getConstPool();
/*  465 */         CodeAttribute ca = (CodeAttribute)cattr.copy(cp, map);
/*  466 */         destInfo.setCodeAttribute(ca);
/*      */       }
/*      */     
/*      */     }
/*  470 */     catch (javassist.bytecode.CodeAttribute.RuntimeCopyException e) {
/*      */ 
/*      */       
/*  473 */       throw new CannotCompileException(e);
/*      */     } 
/*      */     
/*  476 */     destInfo.setAccessFlags(destInfo.getAccessFlags() & 0xFFFFFBFF);
/*      */     
/*  478 */     destClass.rebuildClassFile();
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
/*      */   public byte[] getAttribute(String name) {
/*  493 */     AttributeInfo ai = this.methodInfo.getAttribute(name);
/*  494 */     if (ai == null) {
/*  495 */       return null;
/*      */     }
/*  497 */     return ai.get();
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
/*      */   public void setAttribute(String name, byte[] data) {
/*  511 */     this.declaringClass.checkModify();
/*  512 */     this.methodInfo.addAttribute(new AttributeInfo(this.methodInfo.getConstPool(), name, data));
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
/*      */   public void useCflow(String name) throws CannotCompileException {
/*      */     String fname;
/*  534 */     CtClass cc = this.declaringClass;
/*  535 */     cc.checkModify();
/*  536 */     ClassPool pool = cc.getClassPool();
/*      */     
/*  538 */     int i = 0;
/*      */     while (true) {
/*  540 */       fname = "_cflow$" + i++;
/*      */       try {
/*  542 */         cc.getDeclaredField(fname);
/*      */       }
/*  544 */       catch (NotFoundException e) {
/*      */         break;
/*      */       } 
/*      */     } 
/*      */     
/*  549 */     pool.recordCflow(name, this.declaringClass.getName(), fname);
/*      */     try {
/*  551 */       CtClass type = pool.get("javassist.runtime.Cflow");
/*  552 */       CtField field = new CtField(type, fname, cc);
/*  553 */       field.setModifiers(9);
/*  554 */       cc.addField(field, CtField.Initializer.byNew(type));
/*  555 */       insertBefore(fname + ".enter();", false);
/*  556 */       String src = fname + ".exit();";
/*  557 */       insertAfter(src, true);
/*      */     }
/*  559 */     catch (NotFoundException e) {
/*  560 */       throw new CannotCompileException(e);
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
/*      */   public void addLocalVariable(String name, CtClass type) throws CannotCompileException {
/*  582 */     this.declaringClass.checkModify();
/*  583 */     ConstPool cp = this.methodInfo.getConstPool();
/*  584 */     CodeAttribute ca = this.methodInfo.getCodeAttribute();
/*  585 */     if (ca == null) {
/*  586 */       throw new CannotCompileException("no method body");
/*      */     }
/*  588 */     LocalVariableAttribute va = (LocalVariableAttribute)ca.getAttribute("LocalVariableTable");
/*      */     
/*  590 */     if (va == null) {
/*  591 */       va = new LocalVariableAttribute(cp);
/*  592 */       ca.getAttributes().add(va);
/*      */     } 
/*      */     
/*  595 */     int maxLocals = ca.getMaxLocals();
/*  596 */     String desc = Descriptor.of(type);
/*  597 */     va.addEntry(0, ca.getCodeLength(), cp
/*  598 */         .addUtf8Info(name), cp.addUtf8Info(desc), maxLocals);
/*  599 */     ca.setMaxLocals(maxLocals + Descriptor.dataSize(desc));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void insertParameter(CtClass type) throws CannotCompileException {
/*  608 */     this.declaringClass.checkModify();
/*  609 */     String desc = this.methodInfo.getDescriptor();
/*  610 */     String desc2 = Descriptor.insertParameter(type, desc);
/*      */     try {
/*  612 */       addParameter2(Modifier.isStatic(getModifiers()) ? 0 : 1, type, desc);
/*      */     }
/*  614 */     catch (BadBytecode e) {
/*  615 */       throw new CannotCompileException(e);
/*      */     } 
/*      */     
/*  618 */     this.methodInfo.setDescriptor(desc2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addParameter(CtClass type) throws CannotCompileException {
/*  627 */     this.declaringClass.checkModify();
/*  628 */     String desc = this.methodInfo.getDescriptor();
/*  629 */     String desc2 = Descriptor.appendParameter(type, desc);
/*  630 */     int offset = Modifier.isStatic(getModifiers()) ? 0 : 1;
/*      */     try {
/*  632 */       addParameter2(offset + Descriptor.paramSize(desc), type, desc);
/*      */     }
/*  634 */     catch (BadBytecode e) {
/*  635 */       throw new CannotCompileException(e);
/*      */     } 
/*      */     
/*  638 */     this.methodInfo.setDescriptor(desc2);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void addParameter2(int where, CtClass type, String desc) throws BadBytecode {
/*  644 */     CodeAttribute ca = this.methodInfo.getCodeAttribute();
/*  645 */     if (ca != null) {
/*  646 */       int size = 1;
/*  647 */       char typeDesc = 'L';
/*  648 */       int classInfo = 0;
/*  649 */       if (type.isPrimitive()) {
/*  650 */         CtPrimitiveType cpt = (CtPrimitiveType)type;
/*  651 */         size = cpt.getDataSize();
/*  652 */         typeDesc = cpt.getDescriptor();
/*      */       } else {
/*      */         
/*  655 */         classInfo = this.methodInfo.getConstPool().addClassInfo(type);
/*      */       } 
/*  657 */       ca.insertLocalVar(where, size);
/*      */       
/*  659 */       LocalVariableAttribute va = (LocalVariableAttribute)ca.getAttribute("LocalVariableTable");
/*  660 */       if (va != null) {
/*  661 */         va.shiftIndex(where, size);
/*      */       }
/*      */       
/*  664 */       LocalVariableTypeAttribute lvta = (LocalVariableTypeAttribute)ca.getAttribute("LocalVariableTypeTable");
/*  665 */       if (lvta != null) {
/*  666 */         lvta.shiftIndex(where, size);
/*      */       }
/*  668 */       StackMapTable smt = (StackMapTable)ca.getAttribute("StackMapTable");
/*  669 */       if (smt != null) {
/*  670 */         smt.insertLocal(where, StackMapTable.typeTagOf(typeDesc), classInfo);
/*      */       }
/*  672 */       StackMap sm = (StackMap)ca.getAttribute("StackMap");
/*  673 */       if (sm != null) {
/*  674 */         sm.insertLocal(where, StackMapTable.typeTagOf(typeDesc), classInfo);
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
/*      */   public void instrument(CodeConverter converter) throws CannotCompileException {
/*  686 */     this.declaringClass.checkModify();
/*  687 */     ConstPool cp = this.methodInfo.getConstPool();
/*  688 */     converter.doit(getDeclaringClass(), this.methodInfo, cp);
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
/*      */   public void instrument(ExprEditor editor) throws CannotCompileException {
/*  709 */     if (this.declaringClass.isFrozen()) {
/*  710 */       this.declaringClass.checkModify();
/*      */     }
/*  712 */     if (editor.doit(this.declaringClass, this.methodInfo)) {
/*  713 */       this.declaringClass.checkModify();
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
/*      */   public void insertBefore(String src) throws CannotCompileException {
/*  734 */     insertBefore(src, true);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void insertBefore(String src, boolean rebuild) throws CannotCompileException {
/*  740 */     CtClass cc = this.declaringClass;
/*  741 */     cc.checkModify();
/*  742 */     CodeAttribute ca = this.methodInfo.getCodeAttribute();
/*  743 */     if (ca == null) {
/*  744 */       throw new CannotCompileException("no method body");
/*      */     }
/*  746 */     CodeIterator iterator = ca.iterator();
/*  747 */     Javac jv = new Javac(cc);
/*      */     try {
/*  749 */       int nvars = jv.recordParams(getParameterTypes(), 
/*  750 */           Modifier.isStatic(getModifiers()));
/*  751 */       jv.recordParamNames(ca, nvars);
/*  752 */       jv.recordLocalVariables(ca, 0);
/*  753 */       jv.recordType(getReturnType0());
/*  754 */       jv.compileStmnt(src);
/*  755 */       Bytecode b = jv.getBytecode();
/*  756 */       int stack = b.getMaxStack();
/*  757 */       int locals = b.getMaxLocals();
/*      */       
/*  759 */       if (stack > ca.getMaxStack()) {
/*  760 */         ca.setMaxStack(stack);
/*      */       }
/*  762 */       if (locals > ca.getMaxLocals()) {
/*  763 */         ca.setMaxLocals(locals);
/*      */       }
/*  765 */       int pos = iterator.insertEx(b.get());
/*  766 */       iterator.insert(b.getExceptionTable(), pos);
/*  767 */       if (rebuild) {
/*  768 */         this.methodInfo.rebuildStackMapIf6(cc.getClassPool(), cc.getClassFile2());
/*      */       }
/*  770 */     } catch (NotFoundException e) {
/*  771 */       throw new CannotCompileException(e);
/*      */     }
/*  773 */     catch (CompileError e) {
/*  774 */       throw new CannotCompileException(e);
/*      */     }
/*  776 */     catch (BadBytecode e) {
/*  777 */       throw new CannotCompileException(e);
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
/*      */   public void insertAfter(String src) throws CannotCompileException {
/*  792 */     insertAfter(src, false);
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
/*      */   public void insertAfter(String src, boolean asFinally) throws CannotCompileException {
/*  810 */     CtClass cc = this.declaringClass;
/*  811 */     cc.checkModify();
/*  812 */     ConstPool pool = this.methodInfo.getConstPool();
/*  813 */     CodeAttribute ca = this.methodInfo.getCodeAttribute();
/*  814 */     if (ca == null) {
/*  815 */       throw new CannotCompileException("no method body");
/*      */     }
/*  817 */     CodeIterator iterator = ca.iterator();
/*  818 */     int retAddr = ca.getMaxLocals();
/*  819 */     Bytecode b = new Bytecode(pool, 0, retAddr + 1);
/*  820 */     b.setStackDepth(ca.getMaxStack() + 1);
/*  821 */     Javac jv = new Javac(b, cc);
/*      */     try {
/*  823 */       int nvars = jv.recordParams(getParameterTypes(), 
/*  824 */           Modifier.isStatic(getModifiers()));
/*  825 */       jv.recordParamNames(ca, nvars);
/*  826 */       CtClass rtype = getReturnType0();
/*  827 */       int varNo = jv.recordReturnType(rtype, true);
/*  828 */       jv.recordLocalVariables(ca, 0);
/*      */ 
/*      */       
/*  831 */       int handlerLen = insertAfterHandler(asFinally, b, rtype, varNo, jv, src);
/*      */       
/*  833 */       int handlerPos = iterator.getCodeLength();
/*  834 */       if (asFinally) {
/*  835 */         ca.getExceptionTable().add(getStartPosOfBody(ca), handlerPos, handlerPos, 0);
/*      */       }
/*  837 */       int adviceLen = 0;
/*  838 */       int advicePos = 0;
/*  839 */       boolean noReturn = true;
/*  840 */       while (iterator.hasNext()) {
/*  841 */         int pos = iterator.next();
/*  842 */         if (pos >= handlerPos) {
/*      */           break;
/*      */         }
/*  845 */         int c = iterator.byteAt(pos);
/*  846 */         if (c == 176 || c == 172 || c == 174 || c == 173 || c == 175 || c == 177) {
/*      */ 
/*      */           
/*  849 */           if (noReturn) {
/*      */             
/*  851 */             adviceLen = insertAfterAdvice(b, jv, src, pool, rtype, varNo);
/*  852 */             handlerPos = iterator.append(b.get());
/*  853 */             iterator.append(b.getExceptionTable(), handlerPos);
/*  854 */             advicePos = iterator.getCodeLength() - adviceLen;
/*  855 */             handlerLen = advicePos - handlerPos;
/*  856 */             noReturn = false;
/*      */           } 
/*  858 */           insertGoto(iterator, advicePos, pos);
/*  859 */           advicePos = iterator.getCodeLength() - adviceLen;
/*  860 */           handlerPos = advicePos - handlerLen;
/*      */         } 
/*      */       } 
/*      */       
/*  864 */       if (noReturn) {
/*  865 */         handlerPos = iterator.append(b.get());
/*  866 */         iterator.append(b.getExceptionTable(), handlerPos);
/*      */       } 
/*      */       
/*  869 */       ca.setMaxStack(b.getMaxStack());
/*  870 */       ca.setMaxLocals(b.getMaxLocals());
/*  871 */       this.methodInfo.rebuildStackMapIf6(cc.getClassPool(), cc.getClassFile2());
/*      */     }
/*  873 */     catch (NotFoundException e) {
/*  874 */       throw new CannotCompileException(e);
/*      */     }
/*  876 */     catch (CompileError e) {
/*  877 */       throw new CannotCompileException(e);
/*      */     }
/*  879 */     catch (BadBytecode e) {
/*  880 */       throw new CannotCompileException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int insertAfterAdvice(Bytecode code, Javac jv, String src, ConstPool cp, CtClass rtype, int varNo) throws CompileError {
/*  888 */     int pc = code.currentPc();
/*  889 */     if (rtype == CtClass.voidType) {
/*  890 */       code.addOpcode(1);
/*  891 */       code.addAstore(varNo);
/*  892 */       jv.compileStmnt(src);
/*  893 */       code.addOpcode(177);
/*  894 */       if (code.getMaxLocals() < 1) {
/*  895 */         code.setMaxLocals(1);
/*      */       }
/*      */     } else {
/*  898 */       code.addStore(varNo, rtype);
/*  899 */       jv.compileStmnt(src);
/*  900 */       code.addLoad(varNo, rtype);
/*  901 */       if (rtype.isPrimitive()) {
/*  902 */         code.addOpcode(((CtPrimitiveType)rtype).getReturnOp());
/*      */       } else {
/*  904 */         code.addOpcode(176);
/*      */       } 
/*      */     } 
/*  907 */     return code.currentPc() - pc;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void insertGoto(CodeIterator iterator, int subr, int pos) throws BadBytecode {
/*  916 */     iterator.setMark(subr);
/*      */     
/*  918 */     iterator.writeByte(0, pos);
/*  919 */     boolean wide = (subr + 2 - pos > 32767);
/*  920 */     int len = wide ? 4 : 2;
/*  921 */     CodeIterator.Gap gap = iterator.insertGapAt(pos, len, false);
/*  922 */     pos = gap.position + gap.length - len;
/*  923 */     int offset = iterator.getMark() - pos;
/*  924 */     if (wide) {
/*  925 */       iterator.writeByte(200, pos);
/*  926 */       iterator.write32bit(offset, pos + 1);
/*      */     }
/*  928 */     else if (offset <= 32767) {
/*  929 */       iterator.writeByte(167, pos);
/*  930 */       iterator.write16bit(offset, pos + 1);
/*      */     } else {
/*      */       
/*  933 */       if (gap.length < 4) {
/*  934 */         CodeIterator.Gap gap2 = iterator.insertGapAt(gap.position, 2, false);
/*  935 */         pos = gap2.position + gap2.length + gap.length - 4;
/*      */       } 
/*      */       
/*  938 */       iterator.writeByte(200, pos);
/*  939 */       iterator.write32bit(iterator.getMark() - pos, pos + 1);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int insertAfterHandler(boolean asFinally, Bytecode b, CtClass rtype, int returnVarNo, Javac javac, String src) throws CompileError {
/*  950 */     if (!asFinally) {
/*  951 */       return 0;
/*      */     }
/*  953 */     int var = b.getMaxLocals();
/*  954 */     b.incMaxLocals(1);
/*  955 */     int pc = b.currentPc();
/*  956 */     b.addAstore(var);
/*  957 */     if (rtype.isPrimitive()) {
/*  958 */       char c = ((CtPrimitiveType)rtype).getDescriptor();
/*  959 */       if (c == 'D') {
/*  960 */         b.addDconst(0.0D);
/*  961 */         b.addDstore(returnVarNo);
/*      */       }
/*  963 */       else if (c == 'F') {
/*  964 */         b.addFconst(0.0F);
/*  965 */         b.addFstore(returnVarNo);
/*      */       }
/*  967 */       else if (c == 'J') {
/*  968 */         b.addLconst(0L);
/*  969 */         b.addLstore(returnVarNo);
/*      */       }
/*  971 */       else if (c == 'V') {
/*  972 */         b.addOpcode(1);
/*  973 */         b.addAstore(returnVarNo);
/*      */       } else {
/*      */         
/*  976 */         b.addIconst(0);
/*  977 */         b.addIstore(returnVarNo);
/*      */       } 
/*      */     } else {
/*      */       
/*  981 */       b.addOpcode(1);
/*  982 */       b.addAstore(returnVarNo);
/*      */     } 
/*      */     
/*  985 */     javac.compileStmnt(src);
/*  986 */     b.addAload(var);
/*  987 */     b.addOpcode(191);
/*  988 */     return b.currentPc() - pc;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addCatch(String src, CtClass exceptionType) throws CannotCompileException {
/* 1054 */     addCatch(src, exceptionType, "$e");
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
/*      */   public void addCatch(String src, CtClass exceptionType, String exceptionName) throws CannotCompileException {
/* 1073 */     CtClass cc = this.declaringClass;
/* 1074 */     cc.checkModify();
/* 1075 */     ConstPool cp = this.methodInfo.getConstPool();
/* 1076 */     CodeAttribute ca = this.methodInfo.getCodeAttribute();
/* 1077 */     CodeIterator iterator = ca.iterator();
/* 1078 */     Bytecode b = new Bytecode(cp, ca.getMaxStack(), ca.getMaxLocals());
/* 1079 */     b.setStackDepth(1);
/* 1080 */     Javac jv = new Javac(b, cc);
/*      */     try {
/* 1082 */       jv.recordParams(getParameterTypes(), 
/* 1083 */           Modifier.isStatic(getModifiers()));
/* 1084 */       int var = jv.recordVariable(exceptionType, exceptionName);
/* 1085 */       b.addAstore(var);
/* 1086 */       jv.compileStmnt(src);
/*      */       
/* 1088 */       int stack = b.getMaxStack();
/* 1089 */       int locals = b.getMaxLocals();
/*      */       
/* 1091 */       if (stack > ca.getMaxStack()) {
/* 1092 */         ca.setMaxStack(stack);
/*      */       }
/* 1094 */       if (locals > ca.getMaxLocals()) {
/* 1095 */         ca.setMaxLocals(locals);
/*      */       }
/* 1097 */       int len = iterator.getCodeLength();
/* 1098 */       int pos = iterator.append(b.get());
/* 1099 */       ca.getExceptionTable().add(getStartPosOfBody(ca), len, len, cp
/* 1100 */           .addClassInfo(exceptionType));
/* 1101 */       iterator.append(b.getExceptionTable(), pos);
/* 1102 */       this.methodInfo.rebuildStackMapIf6(cc.getClassPool(), cc.getClassFile2());
/*      */     }
/* 1104 */     catch (NotFoundException e) {
/* 1105 */       throw new CannotCompileException(e);
/*      */     }
/* 1107 */     catch (CompileError e) {
/* 1108 */       throw new CannotCompileException(e);
/* 1109 */     } catch (BadBytecode e) {
/* 1110 */       throw new CannotCompileException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   int getStartPosOfBody(CodeAttribute ca) throws CannotCompileException {
/* 1117 */     return 0;
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
/*      */   public int insertAt(int lineNum, String src) throws CannotCompileException {
/* 1140 */     return insertAt(lineNum, true, src);
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
/*      */   public int insertAt(int lineNum, boolean modify, String src) throws CannotCompileException {
/* 1168 */     CodeAttribute ca = this.methodInfo.getCodeAttribute();
/* 1169 */     if (ca == null) {
/* 1170 */       throw new CannotCompileException("no method body");
/*      */     }
/*      */     
/* 1173 */     LineNumberAttribute ainfo = (LineNumberAttribute)ca.getAttribute("LineNumberTable");
/* 1174 */     if (ainfo == null) {
/* 1175 */       throw new CannotCompileException("no line number info");
/*      */     }
/* 1177 */     LineNumberAttribute.Pc pc = ainfo.toNearPc(lineNum);
/* 1178 */     lineNum = pc.line;
/* 1179 */     int index = pc.index;
/* 1180 */     if (!modify) {
/* 1181 */       return lineNum;
/*      */     }
/* 1183 */     CtClass cc = this.declaringClass;
/* 1184 */     cc.checkModify();
/* 1185 */     CodeIterator iterator = ca.iterator();
/* 1186 */     Javac jv = new Javac(cc);
/*      */     try {
/* 1188 */       jv.recordLocalVariables(ca, index);
/* 1189 */       jv.recordParams(getParameterTypes(), 
/* 1190 */           Modifier.isStatic(getModifiers()));
/* 1191 */       jv.setMaxLocals(ca.getMaxLocals());
/* 1192 */       jv.compileStmnt(src);
/* 1193 */       Bytecode b = jv.getBytecode();
/* 1194 */       int locals = b.getMaxLocals();
/* 1195 */       int stack = b.getMaxStack();
/* 1196 */       ca.setMaxLocals(locals);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1201 */       if (stack > ca.getMaxStack()) {
/* 1202 */         ca.setMaxStack(stack);
/*      */       }
/* 1204 */       index = iterator.insertAt(index, b.get());
/* 1205 */       iterator.insert(b.getExceptionTable(), index);
/* 1206 */       this.methodInfo.rebuildStackMapIf6(cc.getClassPool(), cc.getClassFile2());
/* 1207 */       return lineNum;
/*      */     }
/* 1209 */     catch (NotFoundException e) {
/* 1210 */       throw new CannotCompileException(e);
/*      */     }
/* 1212 */     catch (CompileError e) {
/* 1213 */       throw new CannotCompileException(e);
/*      */     }
/* 1215 */     catch (BadBytecode e) {
/* 1216 */       throw new CannotCompileException(e);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\CtBehavior.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */