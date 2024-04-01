/*      */ package javassist.bytecode;
/*      */ 
/*      */ import java.io.DataInputStream;
/*      */ import java.io.DataOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintWriter;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import javassist.CtClass;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class ConstPool
/*      */ {
/*      */   LongVector items;
/*      */   int numOfItems;
/*      */   int thisClassInfo;
/*      */   HashMap itemsCache;
/*      */   public static final int CONST_Class = 7;
/*      */   public static final int CONST_Fieldref = 9;
/*      */   public static final int CONST_Methodref = 10;
/*      */   public static final int CONST_InterfaceMethodref = 11;
/*      */   public static final int CONST_String = 8;
/*      */   public static final int CONST_Integer = 3;
/*      */   public static final int CONST_Float = 4;
/*      */   public static final int CONST_Long = 5;
/*      */   public static final int CONST_Double = 6;
/*      */   public static final int CONST_NameAndType = 12;
/*      */   public static final int CONST_Utf8 = 1;
/*      */   public static final int CONST_MethodHandle = 15;
/*      */   public static final int CONST_MethodType = 16;
/*      */   public static final int CONST_InvokeDynamic = 18;
/*  114 */   public static final CtClass THIS = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int REF_getField = 1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int REF_getStatic = 2;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int REF_putField = 3;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int REF_putStatic = 4;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int REF_invokeVirtual = 5;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int REF_invokeStatic = 6;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int REF_invokeSpecial = 7;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int REF_newInvokeSpecial = 8;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int REF_invokeInterface = 9;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConstPool(String thisclass) {
/*  168 */     this.items = new LongVector();
/*  169 */     this.itemsCache = null;
/*  170 */     this.numOfItems = 0;
/*  171 */     addItem0(null);
/*  172 */     this.thisClassInfo = addClassInfo(thisclass);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ConstPool(DataInputStream in) throws IOException {
/*  181 */     this.itemsCache = null;
/*  182 */     this.thisClassInfo = 0;
/*      */ 
/*      */     
/*  185 */     read(in);
/*      */   }
/*      */   
/*      */   void prune() {
/*  189 */     this.itemsCache = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getSize() {
/*  196 */     return this.numOfItems;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getClassName() {
/*  203 */     return getClassInfo(this.thisClassInfo);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getThisClassInfo() {
/*  211 */     return this.thisClassInfo;
/*      */   }
/*      */   
/*      */   void setThisClassInfo(int i) {
/*  215 */     this.thisClassInfo = i;
/*      */   }
/*      */   
/*      */   ConstInfo getItem(int n) {
/*  219 */     return this.items.elementAt(n);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getTag(int index) {
/*  230 */     return getItem(index).getTag();
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
/*      */   public String getClassInfo(int index) {
/*  245 */     ClassInfo c = (ClassInfo)getItem(index);
/*  246 */     if (c == null) {
/*  247 */       return null;
/*      */     }
/*  249 */     return Descriptor.toJavaName(getUtf8Info(c.name));
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
/*      */   public String getClassInfoByDescriptor(int index) {
/*  262 */     ClassInfo c = (ClassInfo)getItem(index);
/*  263 */     if (c == null) {
/*  264 */       return null;
/*      */     }
/*  266 */     String className = getUtf8Info(c.name);
/*  267 */     if (className.charAt(0) == '[') {
/*  268 */       return className;
/*      */     }
/*  270 */     return Descriptor.of(className);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getNameAndTypeName(int index) {
/*  280 */     NameAndTypeInfo ntinfo = (NameAndTypeInfo)getItem(index);
/*  281 */     return ntinfo.memberName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getNameAndTypeDescriptor(int index) {
/*  290 */     NameAndTypeInfo ntinfo = (NameAndTypeInfo)getItem(index);
/*  291 */     return ntinfo.typeDescriptor;
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
/*      */   public int getMemberClass(int index) {
/*  304 */     MemberrefInfo minfo = (MemberrefInfo)getItem(index);
/*  305 */     return minfo.classIndex;
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
/*      */   public int getMemberNameAndType(int index) {
/*  318 */     MemberrefInfo minfo = (MemberrefInfo)getItem(index);
/*  319 */     return minfo.nameAndTypeIndex;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getFieldrefClass(int index) {
/*  328 */     FieldrefInfo finfo = (FieldrefInfo)getItem(index);
/*  329 */     return finfo.classIndex;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getFieldrefClassName(int index) {
/*  340 */     FieldrefInfo f = (FieldrefInfo)getItem(index);
/*  341 */     if (f == null) {
/*  342 */       return null;
/*      */     }
/*  344 */     return getClassInfo(f.classIndex);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getFieldrefNameAndType(int index) {
/*  353 */     FieldrefInfo finfo = (FieldrefInfo)getItem(index);
/*  354 */     return finfo.nameAndTypeIndex;
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
/*      */   public String getFieldrefName(int index) {
/*  366 */     FieldrefInfo f = (FieldrefInfo)getItem(index);
/*  367 */     if (f == null) {
/*  368 */       return null;
/*      */     }
/*  370 */     NameAndTypeInfo n = (NameAndTypeInfo)getItem(f.nameAndTypeIndex);
/*  371 */     if (n == null) {
/*  372 */       return null;
/*      */     }
/*  374 */     return getUtf8Info(n.memberName);
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
/*      */   public String getFieldrefType(int index) {
/*  387 */     FieldrefInfo f = (FieldrefInfo)getItem(index);
/*  388 */     if (f == null) {
/*  389 */       return null;
/*      */     }
/*  391 */     NameAndTypeInfo n = (NameAndTypeInfo)getItem(f.nameAndTypeIndex);
/*  392 */     if (n == null) {
/*  393 */       return null;
/*      */     }
/*  395 */     return getUtf8Info(n.typeDescriptor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMethodrefClass(int index) {
/*  405 */     MemberrefInfo minfo = (MemberrefInfo)getItem(index);
/*  406 */     return minfo.classIndex;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getMethodrefClassName(int index) {
/*  417 */     MemberrefInfo minfo = (MemberrefInfo)getItem(index);
/*  418 */     if (minfo == null) {
/*  419 */       return null;
/*      */     }
/*  421 */     return getClassInfo(minfo.classIndex);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMethodrefNameAndType(int index) {
/*  430 */     MemberrefInfo minfo = (MemberrefInfo)getItem(index);
/*  431 */     return minfo.nameAndTypeIndex;
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
/*      */   public String getMethodrefName(int index) {
/*  443 */     MemberrefInfo minfo = (MemberrefInfo)getItem(index);
/*  444 */     if (minfo == null) {
/*  445 */       return null;
/*      */     }
/*      */     
/*  448 */     NameAndTypeInfo n = (NameAndTypeInfo)getItem(minfo.nameAndTypeIndex);
/*  449 */     if (n == null) {
/*  450 */       return null;
/*      */     }
/*  452 */     return getUtf8Info(n.memberName);
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
/*      */   public String getMethodrefType(int index) {
/*  465 */     MemberrefInfo minfo = (MemberrefInfo)getItem(index);
/*  466 */     if (minfo == null) {
/*  467 */       return null;
/*      */     }
/*      */     
/*  470 */     NameAndTypeInfo n = (NameAndTypeInfo)getItem(minfo.nameAndTypeIndex);
/*  471 */     if (n == null) {
/*  472 */       return null;
/*      */     }
/*  474 */     return getUtf8Info(n.typeDescriptor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getInterfaceMethodrefClass(int index) {
/*  484 */     MemberrefInfo minfo = (MemberrefInfo)getItem(index);
/*  485 */     return minfo.classIndex;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getInterfaceMethodrefClassName(int index) {
/*  496 */     MemberrefInfo minfo = (MemberrefInfo)getItem(index);
/*  497 */     return getClassInfo(minfo.classIndex);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getInterfaceMethodrefNameAndType(int index) {
/*  506 */     MemberrefInfo minfo = (MemberrefInfo)getItem(index);
/*  507 */     return minfo.nameAndTypeIndex;
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
/*      */   public String getInterfaceMethodrefName(int index) {
/*  520 */     MemberrefInfo minfo = (MemberrefInfo)getItem(index);
/*  521 */     if (minfo == null) {
/*  522 */       return null;
/*      */     }
/*      */     
/*  525 */     NameAndTypeInfo n = (NameAndTypeInfo)getItem(minfo.nameAndTypeIndex);
/*  526 */     if (n == null) {
/*  527 */       return null;
/*      */     }
/*  529 */     return getUtf8Info(n.memberName);
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
/*      */   public String getInterfaceMethodrefType(int index) {
/*  543 */     MemberrefInfo minfo = (MemberrefInfo)getItem(index);
/*  544 */     if (minfo == null) {
/*  545 */       return null;
/*      */     }
/*      */     
/*  548 */     NameAndTypeInfo n = (NameAndTypeInfo)getItem(minfo.nameAndTypeIndex);
/*  549 */     if (n == null) {
/*  550 */       return null;
/*      */     }
/*  552 */     return getUtf8Info(n.typeDescriptor);
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
/*      */   public Object getLdcValue(int index) {
/*  565 */     ConstInfo constInfo = getItem(index);
/*  566 */     Object value = null;
/*  567 */     if (constInfo instanceof StringInfo) {
/*  568 */       value = getStringInfo(index);
/*  569 */     } else if (constInfo instanceof FloatInfo) {
/*  570 */       value = new Float(getFloatInfo(index));
/*  571 */     } else if (constInfo instanceof IntegerInfo) {
/*  572 */       value = new Integer(getIntegerInfo(index));
/*  573 */     } else if (constInfo instanceof LongInfo) {
/*  574 */       value = new Long(getLongInfo(index));
/*  575 */     } else if (constInfo instanceof DoubleInfo) {
/*  576 */       value = new Double(getDoubleInfo(index));
/*      */     } else {
/*  578 */       value = null;
/*      */     } 
/*  580 */     return value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getIntegerInfo(int index) {
/*  590 */     IntegerInfo i = (IntegerInfo)getItem(index);
/*  591 */     return i.value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getFloatInfo(int index) {
/*  601 */     FloatInfo i = (FloatInfo)getItem(index);
/*  602 */     return i.value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLongInfo(int index) {
/*  612 */     LongInfo i = (LongInfo)getItem(index);
/*  613 */     return i.value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getDoubleInfo(int index) {
/*  623 */     DoubleInfo i = (DoubleInfo)getItem(index);
/*  624 */     return i.value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getStringInfo(int index) {
/*  634 */     StringInfo si = (StringInfo)getItem(index);
/*  635 */     return getUtf8Info(si.string);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getUtf8Info(int index) {
/*  645 */     Utf8Info utf = (Utf8Info)getItem(index);
/*  646 */     return utf.string;
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
/*      */   public int getMethodHandleKind(int index) {
/*  666 */     MethodHandleInfo mhinfo = (MethodHandleInfo)getItem(index);
/*  667 */     return mhinfo.refKind;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMethodHandleIndex(int index) {
/*  678 */     MethodHandleInfo mhinfo = (MethodHandleInfo)getItem(index);
/*  679 */     return mhinfo.refIndex;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMethodTypeInfo(int index) {
/*  690 */     MethodTypeInfo mtinfo = (MethodTypeInfo)getItem(index);
/*  691 */     return mtinfo.descriptor;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getInvokeDynamicBootstrap(int index) {
/*  702 */     InvokeDynamicInfo iv = (InvokeDynamicInfo)getItem(index);
/*  703 */     return iv.bootstrap;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getInvokeDynamicNameAndType(int index) {
/*  714 */     InvokeDynamicInfo iv = (InvokeDynamicInfo)getItem(index);
/*  715 */     return iv.nameAndType;
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
/*      */   public String getInvokeDynamicType(int index) {
/*  728 */     InvokeDynamicInfo iv = (InvokeDynamicInfo)getItem(index);
/*  729 */     if (iv == null) {
/*  730 */       return null;
/*      */     }
/*  732 */     NameAndTypeInfo n = (NameAndTypeInfo)getItem(iv.nameAndType);
/*  733 */     if (n == null) {
/*  734 */       return null;
/*      */     }
/*  736 */     return getUtf8Info(n.typeDescriptor);
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
/*      */   public int isConstructor(String classname, int index) {
/*  751 */     return isMember(classname, "<init>", index);
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
/*      */   public int isMember(String classname, String membername, int index) {
/*  771 */     MemberrefInfo minfo = (MemberrefInfo)getItem(index);
/*  772 */     if (getClassInfo(minfo.classIndex).equals(classname)) {
/*      */       
/*  774 */       NameAndTypeInfo ntinfo = (NameAndTypeInfo)getItem(minfo.nameAndTypeIndex);
/*  775 */       if (getUtf8Info(ntinfo.memberName).equals(membername)) {
/*  776 */         return ntinfo.typeDescriptor;
/*      */       }
/*      */     } 
/*  779 */     return 0;
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
/*      */   public String eqMember(String membername, String desc, int index) {
/*  800 */     MemberrefInfo minfo = (MemberrefInfo)getItem(index);
/*      */     
/*  802 */     NameAndTypeInfo ntinfo = (NameAndTypeInfo)getItem(minfo.nameAndTypeIndex);
/*  803 */     if (getUtf8Info(ntinfo.memberName).equals(membername) && 
/*  804 */       getUtf8Info(ntinfo.typeDescriptor).equals(desc)) {
/*  805 */       return getClassInfo(minfo.classIndex);
/*      */     }
/*  807 */     return null;
/*      */   }
/*      */   
/*      */   private int addItem0(ConstInfo info) {
/*  811 */     this.items.addElement(info);
/*  812 */     return this.numOfItems++;
/*      */   }
/*      */   
/*      */   private int addItem(ConstInfo info) {
/*  816 */     if (this.itemsCache == null) {
/*  817 */       this.itemsCache = makeItemsCache(this.items);
/*      */     }
/*  819 */     ConstInfo found = (ConstInfo)this.itemsCache.get(info);
/*  820 */     if (found != null) {
/*  821 */       return found.index;
/*      */     }
/*  823 */     this.items.addElement(info);
/*  824 */     this.itemsCache.put(info, info);
/*  825 */     return this.numOfItems++;
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
/*      */   public int copy(int n, ConstPool dest, Map classnames) {
/*  841 */     if (n == 0) {
/*  842 */       return 0;
/*      */     }
/*  844 */     ConstInfo info = getItem(n);
/*  845 */     return info.copy(this, dest, classnames);
/*      */   }
/*      */   
/*      */   int addConstInfoPadding() {
/*  849 */     return addItem0(new ConstInfoPadding(this.numOfItems));
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
/*      */   public int addClassInfo(CtClass c) {
/*  861 */     if (c == THIS)
/*  862 */       return this.thisClassInfo; 
/*  863 */     if (!c.isArray()) {
/*  864 */       return addClassInfo(c.getName());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  871 */     return addClassInfo(Descriptor.toJvmName(c));
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
/*      */   public int addClassInfo(String qname) {
/*  886 */     int utf8 = addUtf8Info(Descriptor.toJvmName(qname));
/*  887 */     return addItem(new ClassInfo(utf8, this.numOfItems));
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
/*      */   public int addNameAndTypeInfo(String name, String type) {
/*  900 */     return addNameAndTypeInfo(addUtf8Info(name), addUtf8Info(type));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int addNameAndTypeInfo(int name, int type) {
/*  911 */     return addItem(new NameAndTypeInfo(name, type, this.numOfItems));
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
/*      */   public int addFieldrefInfo(int classInfo, String name, String type) {
/*  928 */     int nt = addNameAndTypeInfo(name, type);
/*  929 */     return addFieldrefInfo(classInfo, nt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int addFieldrefInfo(int classInfo, int nameAndTypeInfo) {
/*  940 */     return addItem(new FieldrefInfo(classInfo, nameAndTypeInfo, this.numOfItems));
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
/*      */   public int addMethodrefInfo(int classInfo, String name, String type) {
/*  957 */     int nt = addNameAndTypeInfo(name, type);
/*  958 */     return addMethodrefInfo(classInfo, nt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int addMethodrefInfo(int classInfo, int nameAndTypeInfo) {
/*  969 */     return addItem(new MethodrefInfo(classInfo, nameAndTypeInfo, this.numOfItems));
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
/*      */   public int addInterfaceMethodrefInfo(int classInfo, String name, String type) {
/*  988 */     int nt = addNameAndTypeInfo(name, type);
/*  989 */     return addInterfaceMethodrefInfo(classInfo, nt);
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
/*      */   public int addInterfaceMethodrefInfo(int classInfo, int nameAndTypeInfo) {
/* 1002 */     return addItem(new InterfaceMethodrefInfo(classInfo, nameAndTypeInfo, this.numOfItems));
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
/*      */   public int addStringInfo(String str) {
/* 1016 */     int utf = addUtf8Info(str);
/* 1017 */     return addItem(new StringInfo(utf, this.numOfItems));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int addIntegerInfo(int i) {
/* 1027 */     return addItem(new IntegerInfo(i, this.numOfItems));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int addFloatInfo(float f) {
/* 1037 */     return addItem(new FloatInfo(f, this.numOfItems));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int addLongInfo(long l) {
/* 1047 */     int i = addItem(new LongInfo(l, this.numOfItems));
/* 1048 */     if (i == this.numOfItems - 1) {
/* 1049 */       addConstInfoPadding();
/*      */     }
/* 1051 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int addDoubleInfo(double d) {
/* 1061 */     int i = addItem(new DoubleInfo(d, this.numOfItems));
/* 1062 */     if (i == this.numOfItems - 1) {
/* 1063 */       addConstInfoPadding();
/*      */     }
/* 1065 */     return i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int addUtf8Info(String utf8) {
/* 1075 */     return addItem(new Utf8Info(utf8, this.numOfItems));
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
/*      */   public int addMethodHandleInfo(int kind, int index) {
/* 1090 */     return addItem(new MethodHandleInfo(kind, index, this.numOfItems));
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
/*      */   public int addMethodTypeInfo(int desc) {
/* 1103 */     return addItem(new MethodTypeInfo(desc, this.numOfItems));
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
/*      */   public int addInvokeDynamicInfo(int bootstrap, int nameAndType) {
/* 1117 */     return addItem(new InvokeDynamicInfo(bootstrap, nameAndType, this.numOfItems));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set getClassNames() {
/* 1126 */     HashSet<String> result = new HashSet();
/* 1127 */     LongVector v = this.items;
/* 1128 */     int size = this.numOfItems;
/* 1129 */     for (int i = 1; i < size; i++) {
/* 1130 */       String className = v.elementAt(i).getClassName(this);
/* 1131 */       if (className != null)
/* 1132 */         result.add(className); 
/*      */     } 
/* 1134 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void renameClass(String oldName, String newName) {
/* 1144 */     LongVector v = this.items;
/* 1145 */     int size = this.numOfItems;
/* 1146 */     for (int i = 1; i < size; i++) {
/* 1147 */       ConstInfo ci = v.elementAt(i);
/* 1148 */       ci.renameClass(this, oldName, newName, this.itemsCache);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void renameClass(Map classnames) {
/* 1159 */     LongVector v = this.items;
/* 1160 */     int size = this.numOfItems;
/* 1161 */     for (int i = 1; i < size; i++) {
/* 1162 */       ConstInfo ci = v.elementAt(i);
/* 1163 */       ci.renameClass(this, classnames, this.itemsCache);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void read(DataInputStream in) throws IOException {
/* 1168 */     int n = in.readUnsignedShort();
/*      */     
/* 1170 */     this.items = new LongVector(n);
/* 1171 */     this.numOfItems = 0;
/* 1172 */     addItem0(null);
/*      */     
/* 1174 */     while (--n > 0) {
/* 1175 */       int tag = readOne(in);
/* 1176 */       if (tag == 5 || tag == 6) {
/* 1177 */         addConstInfoPadding();
/* 1178 */         n--;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private static HashMap makeItemsCache(LongVector items) {
/* 1184 */     HashMap<Object, Object> cache = new HashMap<Object, Object>();
/* 1185 */     int i = 1;
/*      */     while (true) {
/* 1187 */       ConstInfo info = items.elementAt(i++);
/* 1188 */       if (info == null) {
/*      */         break;
/*      */       }
/* 1191 */       cache.put(info, info);
/*      */     } 
/*      */     
/* 1194 */     return cache;
/*      */   }
/*      */   
/*      */   private int readOne(DataInputStream in) throws IOException {
/*      */     ConstInfo info;
/* 1199 */     int tag = in.readUnsignedByte();
/* 1200 */     switch (tag) {
/*      */       case 1:
/* 1202 */         info = new Utf8Info(in, this.numOfItems);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1247 */         addItem0(info);
/* 1248 */         return tag;case 3: info = new IntegerInfo(in, this.numOfItems); addItem0(info); return tag;case 4: info = new FloatInfo(in, this.numOfItems); addItem0(info); return tag;case 5: info = new LongInfo(in, this.numOfItems); addItem0(info); return tag;case 6: info = new DoubleInfo(in, this.numOfItems); addItem0(info); return tag;case 7: info = new ClassInfo(in, this.numOfItems); addItem0(info); return tag;case 8: info = new StringInfo(in, this.numOfItems); addItem0(info); return tag;case 9: info = new FieldrefInfo(in, this.numOfItems); addItem0(info); return tag;case 10: info = new MethodrefInfo(in, this.numOfItems); addItem0(info); return tag;case 11: info = new InterfaceMethodrefInfo(in, this.numOfItems); addItem0(info); return tag;case 12: info = new NameAndTypeInfo(in, this.numOfItems); addItem0(info); return tag;case 15: info = new MethodHandleInfo(in, this.numOfItems); addItem0(info); return tag;case 16: info = new MethodTypeInfo(in, this.numOfItems); addItem0(info); return tag;case 18: info = new InvokeDynamicInfo(in, this.numOfItems); addItem0(info); return tag;
/*      */     } 
/*      */     throw new IOException("invalid constant type: " + tag + " at " + this.numOfItems);
/*      */   }
/*      */ 
/*      */   
/*      */   public void write(DataOutputStream out) throws IOException {
/* 1255 */     out.writeShort(this.numOfItems);
/* 1256 */     LongVector v = this.items;
/* 1257 */     int size = this.numOfItems;
/* 1258 */     for (int i = 1; i < size; i++) {
/* 1259 */       v.elementAt(i).write(out);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void print() {
/* 1266 */     print(new PrintWriter(System.out, true));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void print(PrintWriter out) {
/* 1273 */     int size = this.numOfItems;
/* 1274 */     for (int i = 1; i < size; i++) {
/* 1275 */       out.print(i);
/* 1276 */       out.print(" ");
/* 1277 */       this.items.elementAt(i).print(out);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\ConstPool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */