/*      */ package javassist;
/*      */ 
/*      */ import java.util.ListIterator;
/*      */ import javassist.bytecode.AccessFlag;
/*      */ import javassist.bytecode.AnnotationsAttribute;
/*      */ import javassist.bytecode.AttributeInfo;
/*      */ import javassist.bytecode.Bytecode;
/*      */ import javassist.bytecode.ClassFile;
/*      */ import javassist.bytecode.ConstPool;
/*      */ import javassist.bytecode.Descriptor;
/*      */ import javassist.bytecode.FieldInfo;
/*      */ import javassist.bytecode.SignatureAttribute;
/*      */ import javassist.compiler.CompileError;
/*      */ import javassist.compiler.Javac;
/*      */ import javassist.compiler.SymbolTable;
/*      */ import javassist.compiler.ast.ASTree;
/*      */ import javassist.compiler.ast.DoubleConst;
/*      */ import javassist.compiler.ast.IntConst;
/*      */ import javassist.compiler.ast.StringL;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class CtField
/*      */   extends CtMember
/*      */ {
/*      */   static final String javaLangString = "java.lang.String";
/*      */   protected FieldInfo fieldInfo;
/*      */   
/*      */   public CtField(CtClass type, String name, CtClass declaring) throws CannotCompileException {
/*   61 */     this(Descriptor.of(type), name, declaring);
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
/*      */   public CtField(CtField src, CtClass declaring) throws CannotCompileException {
/*   84 */     this(src.fieldInfo.getDescriptor(), src.fieldInfo.getName(), declaring);
/*      */ 
/*      */     
/*   87 */     ListIterator<AttributeInfo> iterator = src.fieldInfo.getAttributes().listIterator();
/*   88 */     FieldInfo fi = this.fieldInfo;
/*   89 */     fi.setAccessFlags(src.fieldInfo.getAccessFlags());
/*   90 */     ConstPool cp = fi.getConstPool();
/*   91 */     while (iterator.hasNext()) {
/*   92 */       AttributeInfo ainfo = iterator.next();
/*   93 */       fi.addAttribute(ainfo.copy(cp, null));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private CtField(String typeDesc, String name, CtClass clazz) throws CannotCompileException {
/*  100 */     super(clazz);
/*  101 */     ClassFile cf = clazz.getClassFile2();
/*  102 */     if (cf == null) {
/*  103 */       throw new CannotCompileException("bad declaring class: " + clazz
/*  104 */           .getName());
/*      */     }
/*  106 */     this.fieldInfo = new FieldInfo(cf.getConstPool(), name, typeDesc);
/*      */   }
/*      */   
/*      */   CtField(FieldInfo fi, CtClass clazz) {
/*  110 */     super(clazz);
/*  111 */     this.fieldInfo = fi;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*  118 */     return getDeclaringClass().getName() + "." + getName() + ":" + this.fieldInfo
/*  119 */       .getDescriptor();
/*      */   }
/*      */   
/*      */   protected void extendToString(StringBuffer buffer) {
/*  123 */     buffer.append(' ');
/*  124 */     buffer.append(getName());
/*  125 */     buffer.append(' ');
/*  126 */     buffer.append(this.fieldInfo.getDescriptor());
/*      */   }
/*      */ 
/*      */   
/*      */   protected ASTree getInitAST() {
/*  131 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   Initializer getInit() {
/*  136 */     ASTree tree = getInitAST();
/*  137 */     if (tree == null) {
/*  138 */       return null;
/*      */     }
/*  140 */     return Initializer.byExpr(tree);
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
/*      */   public static CtField make(String src, CtClass declaring) throws CannotCompileException {
/*  160 */     Javac compiler = new Javac(declaring);
/*      */     try {
/*  162 */       CtMember obj = compiler.compile(src);
/*  163 */       if (obj instanceof CtField) {
/*  164 */         return (CtField)obj;
/*      */       }
/*  166 */     } catch (CompileError e) {
/*  167 */       throw new CannotCompileException(e);
/*      */     } 
/*      */     
/*  170 */     throw new CannotCompileException("not a field");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FieldInfo getFieldInfo() {
/*  177 */     this.declaringClass.checkModify();
/*  178 */     return this.fieldInfo;
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
/*      */   public FieldInfo getFieldInfo2() {
/*  200 */     return this.fieldInfo;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtClass getDeclaringClass() {
/*  207 */     return super.getDeclaringClass();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getName() {
/*  214 */     return this.fieldInfo.getName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setName(String newName) {
/*  221 */     this.declaringClass.checkModify();
/*  222 */     this.fieldInfo.setName(newName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getModifiers() {
/*  231 */     return AccessFlag.toModifier(this.fieldInfo.getAccessFlags());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setModifiers(int mod) {
/*  240 */     this.declaringClass.checkModify();
/*  241 */     this.fieldInfo.setAccessFlags(AccessFlag.of(mod));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasAnnotation(String typeName) {
/*  252 */     FieldInfo fi = getFieldInfo2();
/*      */     
/*  254 */     AnnotationsAttribute ainfo = (AnnotationsAttribute)fi.getAttribute("RuntimeInvisibleAnnotations");
/*      */     
/*  256 */     AnnotationsAttribute ainfo2 = (AnnotationsAttribute)fi.getAttribute("RuntimeVisibleAnnotations");
/*  257 */     return CtClassType.hasAnnotationType(typeName, getDeclaringClass().getClassPool(), ainfo, ainfo2);
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
/*  273 */     FieldInfo fi = getFieldInfo2();
/*      */     
/*  275 */     AnnotationsAttribute ainfo = (AnnotationsAttribute)fi.getAttribute("RuntimeInvisibleAnnotations");
/*      */     
/*  277 */     AnnotationsAttribute ainfo2 = (AnnotationsAttribute)fi.getAttribute("RuntimeVisibleAnnotations");
/*  278 */     return CtClassType.getAnnotationType(clz, getDeclaringClass().getClassPool(), ainfo, ainfo2);
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
/*  290 */     return getAnnotations(false);
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
/*  304 */       return getAnnotations(true);
/*      */     }
/*  306 */     catch (ClassNotFoundException e) {
/*  307 */       throw new RuntimeException("Unexpected exception", e);
/*      */     } 
/*      */   }
/*      */   
/*      */   private Object[] getAnnotations(boolean ignoreNotFound) throws ClassNotFoundException {
/*  312 */     FieldInfo fi = getFieldInfo2();
/*      */     
/*  314 */     AnnotationsAttribute ainfo = (AnnotationsAttribute)fi.getAttribute("RuntimeInvisibleAnnotations");
/*      */     
/*  316 */     AnnotationsAttribute ainfo2 = (AnnotationsAttribute)fi.getAttribute("RuntimeVisibleAnnotations");
/*  317 */     return CtClassType.toAnnotationType(ignoreNotFound, getDeclaringClass().getClassPool(), ainfo, ainfo2);
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
/*      */   public String getSignature() {
/*  336 */     return this.fieldInfo.getDescriptor();
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
/*  348 */     SignatureAttribute sa = (SignatureAttribute)this.fieldInfo.getAttribute("Signature");
/*  349 */     return (sa == null) ? null : sa.getSignature();
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
/*  363 */     this.declaringClass.checkModify();
/*  364 */     this.fieldInfo.addAttribute((AttributeInfo)new SignatureAttribute(this.fieldInfo.getConstPool(), sig));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtClass getType() throws NotFoundException {
/*  371 */     return Descriptor.toCtClass(this.fieldInfo.getDescriptor(), this.declaringClass
/*  372 */         .getClassPool());
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
/*      */   public void setType(CtClass clazz) {
/*  390 */     this.declaringClass.checkModify();
/*  391 */     this.fieldInfo.setDescriptor(Descriptor.of(clazz));
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
/*      */   public Object getConstantValue() {
/*  412 */     int value, index = this.fieldInfo.getConstantValue();
/*  413 */     if (index == 0) {
/*  414 */       return null;
/*      */     }
/*  416 */     ConstPool cp = this.fieldInfo.getConstPool();
/*  417 */     switch (cp.getTag(index)) {
/*      */       case 5:
/*  419 */         return new Long(cp.getLongInfo(index));
/*      */       case 4:
/*  421 */         return new Float(cp.getFloatInfo(index));
/*      */       case 6:
/*  423 */         return new Double(cp.getDoubleInfo(index));
/*      */       case 3:
/*  425 */         value = cp.getIntegerInfo(index);
/*      */         
/*  427 */         if ("Z".equals(this.fieldInfo.getDescriptor())) {
/*  428 */           return new Boolean((value != 0));
/*      */         }
/*  430 */         return new Integer(value);
/*      */       case 8:
/*  432 */         return cp.getStringInfo(index);
/*      */     } 
/*  434 */     throw new RuntimeException("bad tag: " + cp.getTag(index) + " at " + index);
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
/*      */   public byte[] getAttribute(String name) {
/*  451 */     AttributeInfo ai = this.fieldInfo.getAttribute(name);
/*  452 */     if (ai == null) {
/*  453 */       return null;
/*      */     }
/*  455 */     return ai.get();
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
/*  469 */     this.declaringClass.checkModify();
/*  470 */     this.fieldInfo.addAttribute(new AttributeInfo(this.fieldInfo.getConstPool(), name, data));
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
/*      */   public static abstract class Initializer
/*      */   {
/*      */     public static Initializer constant(int i) {
/*  495 */       return new CtField.IntInitializer(i);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Initializer constant(boolean b) {
/*  503 */       return new CtField.IntInitializer(b ? 1 : 0);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Initializer constant(long l) {
/*  511 */       return new CtField.LongInitializer(l);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Initializer constant(float l) {
/*  519 */       return new CtField.FloatInitializer(l);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Initializer constant(double d) {
/*  527 */       return new CtField.DoubleInitializer(d);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Initializer constant(String s) {
/*  535 */       return new CtField.StringInitializer(s);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Initializer byParameter(int nth) {
/*  553 */       CtField.ParamInitializer i = new CtField.ParamInitializer();
/*  554 */       i.nthParam = nth;
/*  555 */       return i;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Initializer byNew(CtClass objectType) {
/*  573 */       CtField.NewInitializer i = new CtField.NewInitializer();
/*  574 */       i.objectType = objectType;
/*  575 */       i.stringParams = null;
/*  576 */       i.withConstructorParams = false;
/*  577 */       return i;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Initializer byNew(CtClass objectType, String[] stringParams) {
/*  600 */       CtField.NewInitializer i = new CtField.NewInitializer();
/*  601 */       i.objectType = objectType;
/*  602 */       i.stringParams = stringParams;
/*  603 */       i.withConstructorParams = false;
/*  604 */       return i;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Initializer byNewWithParams(CtClass objectType) {
/*  628 */       CtField.NewInitializer i = new CtField.NewInitializer();
/*  629 */       i.objectType = objectType;
/*  630 */       i.stringParams = null;
/*  631 */       i.withConstructorParams = true;
/*  632 */       return i;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Initializer byNewWithParams(CtClass objectType, String[] stringParams) {
/*  658 */       CtField.NewInitializer i = new CtField.NewInitializer();
/*  659 */       i.objectType = objectType;
/*  660 */       i.stringParams = stringParams;
/*  661 */       i.withConstructorParams = true;
/*  662 */       return i;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Initializer byCall(CtClass methodClass, String methodName) {
/*  686 */       CtField.MethodInitializer i = new CtField.MethodInitializer();
/*  687 */       i.objectType = methodClass;
/*  688 */       i.methodName = methodName;
/*  689 */       i.stringParams = null;
/*  690 */       i.withConstructorParams = false;
/*  691 */       return i;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Initializer byCall(CtClass methodClass, String methodName, String[] stringParams) {
/*  720 */       CtField.MethodInitializer i = new CtField.MethodInitializer();
/*  721 */       i.objectType = methodClass;
/*  722 */       i.methodName = methodName;
/*  723 */       i.stringParams = stringParams;
/*  724 */       i.withConstructorParams = false;
/*  725 */       return i;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Initializer byCallWithParams(CtClass methodClass, String methodName) {
/*  752 */       CtField.MethodInitializer i = new CtField.MethodInitializer();
/*  753 */       i.objectType = methodClass;
/*  754 */       i.methodName = methodName;
/*  755 */       i.stringParams = null;
/*  756 */       i.withConstructorParams = true;
/*  757 */       return i;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Initializer byCallWithParams(CtClass methodClass, String methodName, String[] stringParams) {
/*  788 */       CtField.MethodInitializer i = new CtField.MethodInitializer();
/*  789 */       i.objectType = methodClass;
/*  790 */       i.methodName = methodName;
/*  791 */       i.stringParams = stringParams;
/*  792 */       i.withConstructorParams = true;
/*  793 */       return i;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Initializer byNewArray(CtClass type, int size) throws NotFoundException {
/*  807 */       return new CtField.ArrayInitializer(type.getComponentType(), size);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Initializer byNewArray(CtClass type, int[] sizes) {
/*  820 */       return new CtField.MultiArrayInitializer(type, sizes);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Initializer byExpr(String source) {
/*  829 */       return new CtField.CodeInitializer(source);
/*      */     }
/*      */     
/*      */     static Initializer byExpr(ASTree source) {
/*  833 */       return new CtField.PtreeInitializer(source);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     void check(String desc) throws CannotCompileException {}
/*      */ 
/*      */ 
/*      */     
/*      */     abstract int compile(CtClass param1CtClass, String param1String, Bytecode param1Bytecode, CtClass[] param1ArrayOfCtClass, Javac param1Javac) throws CannotCompileException;
/*      */ 
/*      */ 
/*      */     
/*      */     abstract int compileIfStatic(CtClass param1CtClass, String param1String, Bytecode param1Bytecode, Javac param1Javac) throws CannotCompileException;
/*      */ 
/*      */ 
/*      */     
/*      */     int getConstantValue(ConstPool cp, CtClass type) {
/*  851 */       return 0;
/*      */     }
/*      */   }
/*      */   
/*      */   static abstract class CodeInitializer0
/*      */     extends Initializer
/*      */   {
/*      */     abstract void compileExpr(Javac param1Javac) throws CompileError;
/*      */     
/*      */     int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
/*      */       try {
/*  862 */         code.addAload(0);
/*  863 */         compileExpr(drv);
/*  864 */         code.addPutfield(Bytecode.THIS, name, Descriptor.of(type));
/*  865 */         return code.getMaxStack();
/*      */       }
/*  867 */       catch (CompileError e) {
/*  868 */         throw new CannotCompileException(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
/*      */       try {
/*  876 */         compileExpr(drv);
/*  877 */         code.addPutstatic(Bytecode.THIS, name, Descriptor.of(type));
/*  878 */         return code.getMaxStack();
/*      */       }
/*  880 */       catch (CompileError e) {
/*  881 */         throw new CannotCompileException(e);
/*      */       } 
/*      */     }
/*      */     
/*      */     int getConstantValue2(ConstPool cp, CtClass type, ASTree tree) {
/*  886 */       if (type.isPrimitive()) {
/*  887 */         if (tree instanceof IntConst) {
/*  888 */           long value = ((IntConst)tree).get();
/*  889 */           if (type == CtClass.doubleType)
/*  890 */             return cp.addDoubleInfo(value); 
/*  891 */           if (type == CtClass.floatType)
/*  892 */             return cp.addFloatInfo((float)value); 
/*  893 */           if (type == CtClass.longType)
/*  894 */             return cp.addLongInfo(value); 
/*  895 */           if (type != CtClass.voidType) {
/*  896 */             return cp.addIntegerInfo((int)value);
/*      */           }
/*  898 */         } else if (tree instanceof DoubleConst) {
/*  899 */           double value = ((DoubleConst)tree).get();
/*  900 */           if (type == CtClass.floatType)
/*  901 */             return cp.addFloatInfo((float)value); 
/*  902 */           if (type == CtClass.doubleType) {
/*  903 */             return cp.addDoubleInfo(value);
/*      */           }
/*      */         } 
/*  906 */       } else if (tree instanceof StringL && type
/*  907 */         .getName().equals("java.lang.String")) {
/*  908 */         return cp.addStringInfo(((StringL)tree).get());
/*      */       } 
/*  910 */       return 0;
/*      */     }
/*      */   }
/*      */   
/*      */   static class CodeInitializer
/*      */     extends CodeInitializer0 {
/*      */     CodeInitializer(String expr) {
/*  917 */       this.expression = expr;
/*      */     } private String expression;
/*      */     void compileExpr(Javac drv) throws CompileError {
/*  920 */       drv.compileExpr(this.expression);
/*      */     }
/*      */     
/*      */     int getConstantValue(ConstPool cp, CtClass type) {
/*      */       try {
/*  925 */         ASTree t = Javac.parseExpr(this.expression, new SymbolTable());
/*  926 */         return getConstantValue2(cp, type, t);
/*      */       }
/*  928 */       catch (CompileError e) {
/*  929 */         return 0;
/*      */       } 
/*      */     } }
/*      */   
/*      */   static class PtreeInitializer extends CodeInitializer0 {
/*      */     private ASTree expression;
/*      */     
/*      */     PtreeInitializer(ASTree expr) {
/*  937 */       this.expression = expr;
/*      */     }
/*      */     void compileExpr(Javac drv) throws CompileError {
/*  940 */       drv.compileExpr(this.expression);
/*      */     }
/*      */     
/*      */     int getConstantValue(ConstPool cp, CtClass type) {
/*  944 */       return getConstantValue2(cp, type, this.expression);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class ParamInitializer
/*      */     extends Initializer
/*      */   {
/*      */     int nthParam;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
/*  961 */       if (parameters != null && this.nthParam < parameters.length) {
/*  962 */         code.addAload(0);
/*  963 */         int nth = nthParamToLocal(this.nthParam, parameters, false);
/*  964 */         int s = code.addLoad(nth, type) + 1;
/*  965 */         code.addPutfield(Bytecode.THIS, name, Descriptor.of(type));
/*  966 */         return s;
/*      */       } 
/*      */       
/*  969 */       return 0;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static int nthParamToLocal(int nth, CtClass[] params, boolean isStatic) {
/*      */       int k;
/*  982 */       CtClass longType = CtClass.longType;
/*  983 */       CtClass doubleType = CtClass.doubleType;
/*      */       
/*  985 */       if (isStatic) {
/*  986 */         k = 0;
/*      */       } else {
/*  988 */         k = 1;
/*      */       } 
/*  990 */       for (int i = 0; i < nth; i++) {
/*  991 */         CtClass type = params[i];
/*  992 */         if (type == longType || type == doubleType) {
/*  993 */           k += 2;
/*      */         } else {
/*  995 */           k++;
/*      */         } 
/*      */       } 
/*  998 */       return k;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
/* 1004 */       return 0;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class NewInitializer
/*      */     extends Initializer
/*      */   {
/*      */     CtClass objectType;
/*      */ 
/*      */ 
/*      */     
/*      */     String[] stringParams;
/*      */ 
/*      */ 
/*      */     
/*      */     boolean withConstructorParams;
/*      */ 
/*      */ 
/*      */     
/*      */     int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
/*      */       int stacksize;
/* 1028 */       code.addAload(0);
/* 1029 */       code.addNew(this.objectType);
/* 1030 */       code.add(89);
/* 1031 */       code.addAload(0);
/*      */       
/* 1033 */       if (this.stringParams == null) {
/* 1034 */         stacksize = 4;
/*      */       } else {
/* 1036 */         stacksize = compileStringParameter(code) + 4;
/*      */       } 
/* 1038 */       if (this.withConstructorParams) {
/* 1039 */         stacksize += CtNewWrappedMethod.compileParameterList(code, parameters, 1);
/*      */       }
/*      */       
/* 1042 */       code.addInvokespecial(this.objectType, "<init>", getDescriptor());
/* 1043 */       code.addPutfield(Bytecode.THIS, name, Descriptor.of(type));
/* 1044 */       return stacksize;
/*      */     }
/*      */     
/*      */     private String getDescriptor() {
/* 1048 */       String desc3 = "(Ljava/lang/Object;[Ljava/lang/String;[Ljava/lang/Object;)V";
/*      */ 
/*      */       
/* 1051 */       if (this.stringParams == null) {
/* 1052 */         if (this.withConstructorParams) {
/* 1053 */           return "(Ljava/lang/Object;[Ljava/lang/Object;)V";
/*      */         }
/* 1055 */         return "(Ljava/lang/Object;)V";
/*      */       } 
/* 1057 */       if (this.withConstructorParams) {
/* 1058 */         return "(Ljava/lang/Object;[Ljava/lang/String;[Ljava/lang/Object;)V";
/*      */       }
/* 1060 */       return "(Ljava/lang/Object;[Ljava/lang/String;)V";
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
/*      */       String desc;
/* 1071 */       code.addNew(this.objectType);
/* 1072 */       code.add(89);
/*      */       
/* 1074 */       int stacksize = 2;
/* 1075 */       if (this.stringParams == null) {
/* 1076 */         desc = "()V";
/*      */       } else {
/* 1078 */         desc = "([Ljava/lang/String;)V";
/* 1079 */         stacksize += compileStringParameter(code);
/*      */       } 
/*      */       
/* 1082 */       code.addInvokespecial(this.objectType, "<init>", desc);
/* 1083 */       code.addPutstatic(Bytecode.THIS, name, Descriptor.of(type));
/* 1084 */       return stacksize;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     protected final int compileStringParameter(Bytecode code) throws CannotCompileException {
/* 1090 */       int nparam = this.stringParams.length;
/* 1091 */       code.addIconst(nparam);
/* 1092 */       code.addAnewarray("java.lang.String");
/* 1093 */       for (int j = 0; j < nparam; j++) {
/* 1094 */         code.add(89);
/* 1095 */         code.addIconst(j);
/* 1096 */         code.addLdc(this.stringParams[j]);
/* 1097 */         code.add(83);
/*      */       } 
/*      */       
/* 1100 */       return 4;
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
/*      */   static class MethodInitializer
/*      */     extends NewInitializer
/*      */   {
/*      */     String methodName;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
/*      */       int stacksize;
/* 1124 */       code.addAload(0);
/* 1125 */       code.addAload(0);
/*      */       
/* 1127 */       if (this.stringParams == null) {
/* 1128 */         stacksize = 2;
/*      */       } else {
/* 1130 */         stacksize = compileStringParameter(code) + 2;
/*      */       } 
/* 1132 */       if (this.withConstructorParams) {
/* 1133 */         stacksize += CtNewWrappedMethod.compileParameterList(code, parameters, 1);
/*      */       }
/*      */       
/* 1136 */       String typeDesc = Descriptor.of(type);
/* 1137 */       String mDesc = getDescriptor() + typeDesc;
/* 1138 */       code.addInvokestatic(this.objectType, this.methodName, mDesc);
/* 1139 */       code.addPutfield(Bytecode.THIS, name, typeDesc);
/* 1140 */       return stacksize;
/*      */     }
/*      */     
/*      */     private String getDescriptor() {
/* 1144 */       String desc3 = "(Ljava/lang/Object;[Ljava/lang/String;[Ljava/lang/Object;)";
/*      */ 
/*      */       
/* 1147 */       if (this.stringParams == null) {
/* 1148 */         if (this.withConstructorParams) {
/* 1149 */           return "(Ljava/lang/Object;[Ljava/lang/Object;)";
/*      */         }
/* 1151 */         return "(Ljava/lang/Object;)";
/*      */       } 
/* 1153 */       if (this.withConstructorParams) {
/* 1154 */         return "(Ljava/lang/Object;[Ljava/lang/String;[Ljava/lang/Object;)";
/*      */       }
/* 1156 */       return "(Ljava/lang/Object;[Ljava/lang/String;)";
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
/*      */       String desc;
/* 1167 */       int stacksize = 1;
/* 1168 */       if (this.stringParams == null) {
/* 1169 */         desc = "()";
/*      */       } else {
/* 1171 */         desc = "([Ljava/lang/String;)";
/* 1172 */         stacksize += compileStringParameter(code);
/*      */       } 
/*      */       
/* 1175 */       String typeDesc = Descriptor.of(type);
/* 1176 */       code.addInvokestatic(this.objectType, this.methodName, desc + typeDesc);
/* 1177 */       code.addPutstatic(Bytecode.THIS, name, typeDesc);
/* 1178 */       return stacksize;
/*      */     } }
/*      */   
/*      */   static class IntInitializer extends Initializer {
/*      */     int value;
/*      */     
/*      */     IntInitializer(int v) {
/* 1185 */       this.value = v;
/*      */     }
/*      */     void check(String desc) throws CannotCompileException {
/* 1188 */       char c = desc.charAt(0);
/* 1189 */       if (c != 'I' && c != 'S' && c != 'B' && c != 'C' && c != 'Z') {
/* 1190 */         throw new CannotCompileException("type mismatch");
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
/* 1197 */       code.addAload(0);
/* 1198 */       code.addIconst(this.value);
/* 1199 */       code.addPutfield(Bytecode.THIS, name, Descriptor.of(type));
/* 1200 */       return 2;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
/* 1206 */       code.addIconst(this.value);
/* 1207 */       code.addPutstatic(Bytecode.THIS, name, Descriptor.of(type));
/* 1208 */       return 1;
/*      */     }
/*      */     
/*      */     int getConstantValue(ConstPool cp, CtClass type) {
/* 1212 */       return cp.addIntegerInfo(this.value);
/*      */     }
/*      */   }
/*      */   
/*      */   static class LongInitializer extends Initializer { long value;
/*      */     
/*      */     LongInitializer(long v) {
/* 1219 */       this.value = v;
/*      */     }
/*      */     void check(String desc) throws CannotCompileException {
/* 1222 */       if (!desc.equals("J")) {
/* 1223 */         throw new CannotCompileException("type mismatch");
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
/* 1230 */       code.addAload(0);
/* 1231 */       code.addLdc2w(this.value);
/* 1232 */       code.addPutfield(Bytecode.THIS, name, Descriptor.of(type));
/* 1233 */       return 3;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
/* 1239 */       code.addLdc2w(this.value);
/* 1240 */       code.addPutstatic(Bytecode.THIS, name, Descriptor.of(type));
/* 1241 */       return 2;
/*      */     }
/*      */     
/*      */     int getConstantValue(ConstPool cp, CtClass type) {
/* 1245 */       if (type == CtClass.longType) {
/* 1246 */         return cp.addLongInfo(this.value);
/*      */       }
/* 1248 */       return 0;
/*      */     } }
/*      */   
/*      */   static class FloatInitializer extends Initializer {
/*      */     float value;
/*      */     
/*      */     FloatInitializer(float v) {
/* 1255 */       this.value = v;
/*      */     }
/*      */     void check(String desc) throws CannotCompileException {
/* 1258 */       if (!desc.equals("F")) {
/* 1259 */         throw new CannotCompileException("type mismatch");
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
/* 1266 */       code.addAload(0);
/* 1267 */       code.addFconst(this.value);
/* 1268 */       code.addPutfield(Bytecode.THIS, name, Descriptor.of(type));
/* 1269 */       return 3;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
/* 1275 */       code.addFconst(this.value);
/* 1276 */       code.addPutstatic(Bytecode.THIS, name, Descriptor.of(type));
/* 1277 */       return 2;
/*      */     }
/*      */     
/*      */     int getConstantValue(ConstPool cp, CtClass type) {
/* 1281 */       if (type == CtClass.floatType) {
/* 1282 */         return cp.addFloatInfo(this.value);
/*      */       }
/* 1284 */       return 0;
/*      */     }
/*      */   }
/*      */   
/*      */   static class DoubleInitializer extends Initializer { double value;
/*      */     
/*      */     DoubleInitializer(double v) {
/* 1291 */       this.value = v;
/*      */     }
/*      */     void check(String desc) throws CannotCompileException {
/* 1294 */       if (!desc.equals("D")) {
/* 1295 */         throw new CannotCompileException("type mismatch");
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
/* 1302 */       code.addAload(0);
/* 1303 */       code.addLdc2w(this.value);
/* 1304 */       code.addPutfield(Bytecode.THIS, name, Descriptor.of(type));
/* 1305 */       return 3;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
/* 1311 */       code.addLdc2w(this.value);
/* 1312 */       code.addPutstatic(Bytecode.THIS, name, Descriptor.of(type));
/* 1313 */       return 2;
/*      */     }
/*      */     
/*      */     int getConstantValue(ConstPool cp, CtClass type) {
/* 1317 */       if (type == CtClass.doubleType) {
/* 1318 */         return cp.addDoubleInfo(this.value);
/*      */       }
/* 1320 */       return 0;
/*      */     } }
/*      */   
/*      */   static class StringInitializer extends Initializer {
/*      */     String value;
/*      */     
/*      */     StringInitializer(String v) {
/* 1327 */       this.value = v;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
/* 1333 */       code.addAload(0);
/* 1334 */       code.addLdc(this.value);
/* 1335 */       code.addPutfield(Bytecode.THIS, name, Descriptor.of(type));
/* 1336 */       return 2;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
/* 1342 */       code.addLdc(this.value);
/* 1343 */       code.addPutstatic(Bytecode.THIS, name, Descriptor.of(type));
/* 1344 */       return 1;
/*      */     }
/*      */     
/*      */     int getConstantValue(ConstPool cp, CtClass type) {
/* 1348 */       if (type.getName().equals("java.lang.String")) {
/* 1349 */         return cp.addStringInfo(this.value);
/*      */       }
/* 1351 */       return 0;
/*      */     }
/*      */   }
/*      */   
/*      */   static class ArrayInitializer extends Initializer { CtClass type;
/*      */     int size;
/*      */     
/*      */     ArrayInitializer(CtClass t, int s) {
/* 1359 */       this.type = t; this.size = s;
/*      */     }
/*      */     private void addNewarray(Bytecode code) {
/* 1362 */       if (this.type.isPrimitive()) {
/* 1363 */         code.addNewarray(((CtPrimitiveType)this.type).getArrayType(), this.size);
/*      */       } else {
/*      */         
/* 1366 */         code.addAnewarray(this.type, this.size);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
/* 1373 */       code.addAload(0);
/* 1374 */       addNewarray(code);
/* 1375 */       code.addPutfield(Bytecode.THIS, name, Descriptor.of(type));
/* 1376 */       return 2;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
/* 1382 */       addNewarray(code);
/* 1383 */       code.addPutstatic(Bytecode.THIS, name, Descriptor.of(type));
/* 1384 */       return 1;
/*      */     } }
/*      */   
/*      */   static class MultiArrayInitializer extends Initializer {
/*      */     CtClass type;
/*      */     int[] dim;
/*      */     
/*      */     MultiArrayInitializer(CtClass t, int[] d) {
/* 1392 */       this.type = t; this.dim = d;
/*      */     }
/*      */     void check(String desc) throws CannotCompileException {
/* 1395 */       if (desc.charAt(0) != '[') {
/* 1396 */         throw new CannotCompileException("type mismatch");
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
/* 1403 */       code.addAload(0);
/* 1404 */       int s = code.addMultiNewarray(type, this.dim);
/* 1405 */       code.addPutfield(Bytecode.THIS, name, Descriptor.of(type));
/* 1406 */       return s + 1;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
/* 1412 */       int s = code.addMultiNewarray(type, this.dim);
/* 1413 */       code.addPutstatic(Bytecode.THIS, name, Descriptor.of(type));
/* 1414 */       return s;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\CtField.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */