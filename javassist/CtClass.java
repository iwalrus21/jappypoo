/*      */ package javassist;
/*      */ 
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.DataOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.net.URL;
/*      */ import java.security.ProtectionDomain;
/*      */ import java.util.Collection;
/*      */ import javassist.bytecode.ClassFile;
/*      */ import javassist.bytecode.Descriptor;
/*      */ import javassist.compiler.AccessorMaker;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class CtClass
/*      */ {
/*      */   protected String qualifiedName;
/*   67 */   public static String debugDump = null;
/*      */   
/*      */   public static final String version = "3.21.0-GA";
/*      */   static final String javaLangObject = "java.lang.Object";
/*      */   public static CtClass booleanType;
/*      */   public static CtClass charType;
/*      */   public static CtClass byteType;
/*      */   public static CtClass shortType;
/*      */   public static CtClass intType;
/*      */   public static CtClass longType;
/*      */   public static CtClass floatType;
/*      */   public static CtClass doubleType;
/*      */   public static CtClass voidType;
/*      */   
/*      */   public static void main(String[] args) {
/*   82 */     System.out.println("Javassist version 3.21.0-GA");
/*   83 */     System.out.println("Copyright (C) 1999-2016 Shigeru Chiba. All Rights Reserved.");
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
/*  146 */   static CtClass[] primitiveTypes = new CtClass[9];
/*      */   static {
/*  148 */     booleanType = new CtPrimitiveType("boolean", 'Z', "java.lang.Boolean", "booleanValue", "()Z", 172, 4, 1);
/*      */ 
/*      */ 
/*      */     
/*  152 */     primitiveTypes[0] = booleanType;
/*      */     
/*  154 */     charType = new CtPrimitiveType("char", 'C', "java.lang.Character", "charValue", "()C", 172, 5, 1);
/*      */ 
/*      */     
/*  157 */     primitiveTypes[1] = charType;
/*      */     
/*  159 */     byteType = new CtPrimitiveType("byte", 'B', "java.lang.Byte", "byteValue", "()B", 172, 8, 1);
/*      */ 
/*      */     
/*  162 */     primitiveTypes[2] = byteType;
/*      */     
/*  164 */     shortType = new CtPrimitiveType("short", 'S', "java.lang.Short", "shortValue", "()S", 172, 9, 1);
/*      */ 
/*      */     
/*  167 */     primitiveTypes[3] = shortType;
/*      */     
/*  169 */     intType = new CtPrimitiveType("int", 'I', "java.lang.Integer", "intValue", "()I", 172, 10, 1);
/*      */ 
/*      */     
/*  172 */     primitiveTypes[4] = intType;
/*      */     
/*  174 */     longType = new CtPrimitiveType("long", 'J', "java.lang.Long", "longValue", "()J", 173, 11, 2);
/*      */ 
/*      */     
/*  177 */     primitiveTypes[5] = longType;
/*      */     
/*  179 */     floatType = new CtPrimitiveType("float", 'F', "java.lang.Float", "floatValue", "()F", 174, 6, 1);
/*      */ 
/*      */     
/*  182 */     primitiveTypes[6] = floatType;
/*      */     
/*  184 */     doubleType = new CtPrimitiveType("double", 'D', "java.lang.Double", "doubleValue", "()D", 175, 7, 2);
/*      */ 
/*      */     
/*  187 */     primitiveTypes[7] = doubleType;
/*      */     
/*  189 */     voidType = new CtPrimitiveType("void", 'V', "java.lang.Void", null, null, 177, 0, 0);
/*      */     
/*  191 */     primitiveTypes[8] = voidType;
/*      */   }
/*      */   
/*      */   protected CtClass(String name) {
/*  195 */     this.qualifiedName = name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*  202 */     StringBuffer buf = new StringBuffer(getClass().getName());
/*  203 */     buf.append("@");
/*  204 */     buf.append(Integer.toHexString(hashCode()));
/*  205 */     buf.append("[");
/*  206 */     extendToString(buf);
/*  207 */     buf.append("]");
/*  208 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void extendToString(StringBuffer buffer) {
/*  216 */     buffer.append(getName());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassPool getClassPool() {
/*  222 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassFile getClassFile() {
/*  231 */     checkModify();
/*  232 */     return getClassFile2();
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
/*      */   public ClassFile getClassFile2() {
/*  253 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public AccessorMaker getAccessorMaker() {
/*  259 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public URL getURL() throws NotFoundException {
/*  266 */     throw new NotFoundException(getName());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isModified() {
/*  272 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isFrozen() {
/*  281 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void freeze() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void checkModify() throws RuntimeException {
/*  295 */     if (isFrozen()) {
/*  296 */       throw new RuntimeException(getName() + " class is frozen");
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
/*      */   public void defrost() {
/*  317 */     throw new RuntimeException("cannot defrost " + getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPrimitive() {
/*  325 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isArray() {
/*  331 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtClass getComponentType() throws NotFoundException {
/*  339 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean subtypeOf(CtClass clazz) throws NotFoundException {
/*  348 */     return (this == clazz || getName().equals(clazz.getName()));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String getName() {
/*  354 */     return this.qualifiedName;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getSimpleName() {
/*  360 */     String qname = this.qualifiedName;
/*  361 */     int index = qname.lastIndexOf('.');
/*  362 */     if (index < 0) {
/*  363 */       return qname;
/*      */     }
/*  365 */     return qname.substring(index + 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getPackageName() {
/*  372 */     String qname = this.qualifiedName;
/*  373 */     int index = qname.lastIndexOf('.');
/*  374 */     if (index < 0) {
/*  375 */       return null;
/*      */     }
/*  377 */     return qname.substring(0, index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setName(String name) {
/*  386 */     checkModify();
/*  387 */     if (name != null) {
/*  388 */       this.qualifiedName = name;
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
/*      */   public String getGenericSignature() {
/*  405 */     return null;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setGenericSignature(String sig) {
/*  477 */     checkModify();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void replaceClassName(String oldName, String newName) {
/*  487 */     checkModify();
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
/*      */   public void replaceClassName(ClassMap map) {
/*  508 */     checkModify();
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
/*      */   public synchronized Collection getRefClasses() {
/*  521 */     ClassFile cf = getClassFile2();
/*  522 */     if (cf != null) {
/*  523 */       ClassMap cm = new ClassMap() {
/*      */           public void put(String oldname, String newname) {
/*  525 */             put0(oldname, newname);
/*      */           }
/*      */           
/*      */           public Object get(Object jvmClassName) {
/*  529 */             String n = toJavaName((String)jvmClassName);
/*  530 */             put0(n, n);
/*  531 */             return null;
/*      */           }
/*      */           
/*      */           public void fix(String name) {}
/*      */         };
/*  536 */       cf.getRefClasses(cm);
/*  537 */       return cm.values();
/*      */     } 
/*      */     
/*  540 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isInterface() {
/*  548 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAnnotation() {
/*  558 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEnum() {
/*  568 */     return false;
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
/*      */   public int getModifiers() {
/*  581 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasAnnotation(Class annotationType) {
/*  592 */     return hasAnnotation(annotationType.getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasAnnotation(String annotationTypeName) {
/*  603 */     return false;
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
/*      */   public Object getAnnotation(Class clz) throws ClassNotFoundException {
/*  618 */     return null;
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
/*      */   public Object[] getAnnotations() throws ClassNotFoundException {
/*  633 */     return new Object[0];
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
/*      */   public Object[] getAvailableAnnotations() {
/*  648 */     return new Object[0];
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
/*      */   public CtClass[] getDeclaredClasses() throws NotFoundException {
/*  660 */     return getNestedClasses();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtClass[] getNestedClasses() throws NotFoundException {
/*  671 */     return new CtClass[0];
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
/*      */   public void setModifiers(int mod) {
/*  686 */     checkModify();
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
/*      */   public boolean subclassOf(CtClass superclass) {
/*  698 */     return false;
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
/*      */   public CtClass getSuperclass() throws NotFoundException {
/*  714 */     return null;
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
/*      */   public void setSuperclass(CtClass clazz) throws CannotCompileException {
/*  731 */     checkModify();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtClass[] getInterfaces() throws NotFoundException {
/*  740 */     return new CtClass[0];
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
/*      */   public void setInterfaces(CtClass[] list) {
/*  753 */     checkModify();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addInterface(CtClass anInterface) {
/*  762 */     checkModify();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtClass getDeclaringClass() throws NotFoundException {
/*  772 */     return null;
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
/*      */   public final CtMethod getEnclosingMethod() throws NotFoundException {
/*  786 */     CtBehavior b = getEnclosingBehavior();
/*  787 */     if (b == null)
/*  788 */       return null; 
/*  789 */     if (b instanceof CtMethod) {
/*  790 */       return (CtMethod)b;
/*      */     }
/*  792 */     throw new NotFoundException(b.getLongName() + " is enclosing " + getName());
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
/*      */   public CtBehavior getEnclosingBehavior() throws NotFoundException {
/*  804 */     return null;
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
/*      */   public CtClass makeNestedClass(String name, boolean isStatic) {
/*  819 */     throw new RuntimeException(getName() + " is not a class");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtField[] getFields() {
/*  828 */     return new CtField[0];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtField getField(String name) throws NotFoundException {
/*  835 */     return getField(name, null);
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
/*      */   public CtField getField(String name, String desc) throws NotFoundException {
/*  850 */     throw new NotFoundException(name);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   CtField getField2(String name, String desc) {
/*  856 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtField[] getDeclaredFields() {
/*  864 */     return new CtField[0];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtField getDeclaredField(String name) throws NotFoundException {
/*  873 */     throw new NotFoundException(name);
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
/*      */   public CtField getDeclaredField(String name, String desc) throws NotFoundException {
/*  889 */     throw new NotFoundException(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtBehavior[] getDeclaredBehaviors() {
/*  896 */     return new CtBehavior[0];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtConstructor[] getConstructors() {
/*  904 */     return new CtConstructor[0];
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
/*      */   public CtConstructor getConstructor(String desc) throws NotFoundException {
/*  920 */     throw new NotFoundException("no such constructor");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtConstructor[] getDeclaredConstructors() {
/*  929 */     return new CtConstructor[0];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtConstructor getDeclaredConstructor(CtClass[] params) throws NotFoundException {
/*  940 */     String desc = Descriptor.ofConstructor(params);
/*  941 */     return getConstructor(desc);
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
/*      */   public CtConstructor getClassInitializer() {
/*  954 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtMethod[] getMethods() {
/*  964 */     return new CtMethod[0];
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
/*      */   public CtMethod getMethod(String name, String desc) throws NotFoundException {
/*  982 */     throw new NotFoundException(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CtMethod[] getDeclaredMethods() {
/*  992 */     return new CtMethod[0];
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
/*      */   public CtMethod getDeclaredMethod(String name, CtClass[] params) throws NotFoundException {
/* 1008 */     throw new NotFoundException(name);
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
/*      */   public CtMethod[] getDeclaredMethods(String name) throws NotFoundException {
/* 1022 */     throw new NotFoundException(name);
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
/*      */   public CtMethod getDeclaredMethod(String name) throws NotFoundException {
/* 1035 */     throw new NotFoundException(name);
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
/*      */   public CtConstructor makeClassInitializer() throws CannotCompileException {
/* 1048 */     throw new CannotCompileException("not a class");
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
/*      */   public void addConstructor(CtConstructor c) throws CannotCompileException {
/* 1060 */     checkModify();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeConstructor(CtConstructor c) throws NotFoundException {
/* 1070 */     checkModify();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addMethod(CtMethod m) throws CannotCompileException {
/* 1077 */     checkModify();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeMethod(CtMethod m) throws NotFoundException {
/* 1087 */     checkModify();
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
/*      */   public void addField(CtField f) throws CannotCompileException {
/* 1100 */     addField(f, (CtField.Initializer)null);
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
/*      */   public void addField(CtField f, String init) throws CannotCompileException {
/* 1136 */     checkModify();
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
/*      */   public void addField(CtField f, CtField.Initializer init) throws CannotCompileException {
/* 1164 */     checkModify();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeField(CtField f) throws NotFoundException {
/* 1174 */     checkModify();
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
/*      */   public byte[] getAttribute(String name) {
/* 1195 */     return null;
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
/*      */   public void setAttribute(String name, byte[] data) {
/* 1221 */     checkModify();
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
/*      */   public void instrument(CodeConverter converter) throws CannotCompileException {
/* 1235 */     checkModify();
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
/*      */   public void instrument(ExprEditor editor) throws CannotCompileException {
/* 1249 */     checkModify();
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
/*      */   public Class toClass() throws CannotCompileException {
/* 1275 */     return getClassPool().toClass(this);
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
/*      */   public Class toClass(ClassLoader loader, ProtectionDomain domain) throws CannotCompileException {
/* 1312 */     ClassPool cp = getClassPool();
/* 1313 */     if (loader == null) {
/* 1314 */       loader = cp.getClassLoader();
/*      */     }
/* 1316 */     return cp.toClass(this, loader, domain);
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
/*      */   public final Class toClass(ClassLoader loader) throws CannotCompileException {
/* 1331 */     return getClassPool().toClass(this, loader);
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
/*      */   public void detach() {
/* 1347 */     ClassPool cp = getClassPool();
/* 1348 */     CtClass obj = cp.removeCached(getName());
/* 1349 */     if (obj != this) {
/* 1350 */       cp.cacheCtClass(getName(), obj, false);
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
/*      */   public boolean stopPruning(boolean stop) {
/* 1377 */     return true;
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
/*      */   public void prune() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void incGetCounter() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void rebuildClassFile() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] toBytecode() throws IOException, CannotCompileException {
/* 1439 */     ByteArrayOutputStream barray = new ByteArrayOutputStream();
/* 1440 */     DataOutputStream out = new DataOutputStream(barray);
/*      */     try {
/* 1442 */       toBytecode(out);
/*      */     } finally {
/*      */       
/* 1445 */       out.close();
/*      */     } 
/*      */     
/* 1448 */     return barray.toByteArray();
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
/*      */   public void writeFile() throws NotFoundException, IOException, CannotCompileException {
/* 1462 */     writeFile(".");
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
/*      */   public void writeFile(String directoryName) throws CannotCompileException, IOException {
/* 1477 */     DataOutputStream out = makeFileOutput(directoryName);
/*      */     try {
/* 1479 */       toBytecode(out);
/*      */     } finally {
/*      */       
/* 1482 */       out.close();
/*      */     } 
/*      */   }
/*      */   
/*      */   protected DataOutputStream makeFileOutput(String directoryName) {
/* 1487 */     String classname = getName();
/*      */     
/* 1489 */     String filename = directoryName + File.separatorChar + classname.replace('.', File.separatorChar) + ".class";
/* 1490 */     int pos = filename.lastIndexOf(File.separatorChar);
/* 1491 */     if (pos > 0) {
/* 1492 */       String dir = filename.substring(0, pos);
/* 1493 */       if (!dir.equals(".")) {
/* 1494 */         (new File(dir)).mkdirs();
/*      */       }
/*      */     } 
/* 1497 */     return new DataOutputStream(new BufferedOutputStream(new DelayedFileOutputStream(filename)));
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
/*      */   public void debugWriteFile() {
/* 1509 */     debugWriteFile(".");
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
/*      */   public void debugWriteFile(String directoryName) {
/*      */     try {
/* 1523 */       boolean p = stopPruning(true);
/* 1524 */       writeFile(directoryName);
/* 1525 */       defrost();
/* 1526 */       stopPruning(p);
/*      */     }
/* 1528 */     catch (Exception e) {
/* 1529 */       throw new RuntimeException(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   static class DelayedFileOutputStream extends OutputStream {
/*      */     private FileOutputStream file;
/*      */     private String filename;
/*      */     
/*      */     DelayedFileOutputStream(String name) {
/* 1538 */       this.file = null;
/* 1539 */       this.filename = name;
/*      */     }
/*      */     
/*      */     private void init() throws IOException {
/* 1543 */       if (this.file == null)
/* 1544 */         this.file = new FileOutputStream(this.filename); 
/*      */     }
/*      */     
/*      */     public void write(int b) throws IOException {
/* 1548 */       init();
/* 1549 */       this.file.write(b);
/*      */     }
/*      */     
/*      */     public void write(byte[] b) throws IOException {
/* 1553 */       init();
/* 1554 */       this.file.write(b);
/*      */     }
/*      */     
/*      */     public void write(byte[] b, int off, int len) throws IOException {
/* 1558 */       init();
/* 1559 */       this.file.write(b, off, len);
/*      */     }
/*      */ 
/*      */     
/*      */     public void flush() throws IOException {
/* 1564 */       init();
/* 1565 */       this.file.flush();
/*      */     }
/*      */     
/*      */     public void close() throws IOException {
/* 1569 */       init();
/* 1570 */       this.file.close();
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
/*      */   public void toBytecode(DataOutputStream out) throws CannotCompileException, IOException {
/* 1586 */     throw new CannotCompileException("not a class");
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
/*      */   public String makeUniqueName(String prefix) {
/* 1599 */     throw new RuntimeException("not available in " + getName());
/*      */   }
/*      */   
/*      */   void compress() {}
/*      */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\CtClass.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */