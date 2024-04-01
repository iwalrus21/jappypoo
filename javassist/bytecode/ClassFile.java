/*     */ package javassist.bytecode;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Map;
/*     */ import javassist.CannotCompileException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ClassFile
/*     */ {
/*     */   int major;
/*     */   int minor;
/*     */   ConstPool constPool;
/*     */   int thisClass;
/*     */   int accessFlags;
/*     */   int superClass;
/*     */   int[] interfaces;
/*     */   ArrayList fields;
/*     */   ArrayList methods;
/*     */   ArrayList attributes;
/*     */   String thisclassname;
/*     */   String[] cachedInterfaces;
/*     */   String cachedSuperclass;
/*     */   public static final int JAVA_1 = 45;
/*     */   public static final int JAVA_2 = 46;
/*     */   public static final int JAVA_3 = 47;
/*     */   public static final int JAVA_4 = 48;
/*     */   public static final int JAVA_5 = 49;
/*     */   public static final int JAVA_6 = 50;
/*     */   public static final int JAVA_7 = 51;
/*     */   public static final int JAVA_8 = 52;
/*     */   public static final int MAJOR_VERSION;
/*     */   
/*     */   static {
/* 135 */     int ver = 47;
/*     */     try {
/* 137 */       Class.forName("java.lang.StringBuilder");
/* 138 */       ver = 49;
/* 139 */       Class.forName("java.util.zip.DeflaterInputStream");
/* 140 */       ver = 50;
/* 141 */       Class.forName("java.lang.invoke.CallSite");
/* 142 */       ver = 51;
/* 143 */       Class.forName("java.util.function.Function");
/* 144 */       ver = 52;
/*     */     }
/* 146 */     catch (Throwable throwable) {}
/* 147 */     MAJOR_VERSION = ver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassFile(DataInputStream in) throws IOException {
/* 154 */     read(in);
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
/*     */   public ClassFile(boolean isInterface, String classname, String superclass) {
/* 168 */     this.major = MAJOR_VERSION;
/* 169 */     this.minor = 0;
/* 170 */     this.constPool = new ConstPool(classname);
/* 171 */     this.thisClass = this.constPool.getThisClassInfo();
/* 172 */     if (isInterface) {
/* 173 */       this.accessFlags = 1536;
/*     */     } else {
/* 175 */       this.accessFlags = 32;
/*     */     } 
/* 177 */     initSuperclass(superclass);
/* 178 */     this.interfaces = null;
/* 179 */     this.fields = new ArrayList();
/* 180 */     this.methods = new ArrayList();
/* 181 */     this.thisclassname = classname;
/*     */     
/* 183 */     this.attributes = new ArrayList();
/* 184 */     this.attributes.add(new SourceFileAttribute(this.constPool, 
/* 185 */           getSourcefileName(this.thisclassname)));
/*     */   }
/*     */   
/*     */   private void initSuperclass(String superclass) {
/* 189 */     if (superclass != null) {
/* 190 */       this.superClass = this.constPool.addClassInfo(superclass);
/* 191 */       this.cachedSuperclass = superclass;
/*     */     } else {
/*     */       
/* 194 */       this.superClass = this.constPool.addClassInfo("java.lang.Object");
/* 195 */       this.cachedSuperclass = "java.lang.Object";
/*     */     } 
/*     */   }
/*     */   
/*     */   private static String getSourcefileName(String qname) {
/* 200 */     int index = qname.lastIndexOf('.');
/* 201 */     if (index >= 0) {
/* 202 */       qname = qname.substring(index + 1);
/*     */     }
/* 204 */     return qname + ".java";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void compact() {
/* 213 */     ConstPool cp = compact0();
/* 214 */     ArrayList<MethodInfo> list = this.methods;
/* 215 */     int n = list.size(); int i;
/* 216 */     for (i = 0; i < n; i++) {
/* 217 */       MethodInfo minfo = list.get(i);
/* 218 */       minfo.compact(cp);
/*     */     } 
/*     */     
/* 221 */     list = this.fields;
/* 222 */     n = list.size();
/* 223 */     for (i = 0; i < n; i++) {
/* 224 */       FieldInfo finfo = (FieldInfo)list.get(i);
/* 225 */       finfo.compact(cp);
/*     */     } 
/*     */     
/* 228 */     this.attributes = AttributeInfo.copyAll(this.attributes, cp);
/* 229 */     this.constPool = cp;
/*     */   }
/*     */   
/*     */   private ConstPool compact0() {
/* 233 */     ConstPool cp = new ConstPool(this.thisclassname);
/* 234 */     this.thisClass = cp.getThisClassInfo();
/* 235 */     String sc = getSuperclass();
/* 236 */     if (sc != null) {
/* 237 */       this.superClass = cp.addClassInfo(getSuperclass());
/*     */     }
/* 239 */     if (this.interfaces != null) {
/* 240 */       int n = this.interfaces.length;
/* 241 */       for (int i = 0; i < n; i++) {
/* 242 */         this.interfaces[i] = cp
/* 243 */           .addClassInfo(this.constPool.getClassInfo(this.interfaces[i]));
/*     */       }
/*     */     } 
/* 246 */     return cp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void prune() {
/* 256 */     ConstPool cp = compact0();
/* 257 */     ArrayList<AttributeInfo> newAttributes = new ArrayList();
/*     */     
/* 259 */     AttributeInfo invisibleAnnotations = getAttribute("RuntimeInvisibleAnnotations");
/* 260 */     if (invisibleAnnotations != null) {
/* 261 */       invisibleAnnotations = invisibleAnnotations.copy(cp, null);
/* 262 */       newAttributes.add(invisibleAnnotations);
/*     */     } 
/*     */ 
/*     */     
/* 266 */     AttributeInfo visibleAnnotations = getAttribute("RuntimeVisibleAnnotations");
/* 267 */     if (visibleAnnotations != null) {
/* 268 */       visibleAnnotations = visibleAnnotations.copy(cp, null);
/* 269 */       newAttributes.add(visibleAnnotations);
/*     */     } 
/*     */ 
/*     */     
/* 273 */     AttributeInfo signature = getAttribute("Signature");
/* 274 */     if (signature != null) {
/* 275 */       signature = signature.copy(cp, null);
/* 276 */       newAttributes.add(signature);
/*     */     } 
/*     */     
/* 279 */     ArrayList<MethodInfo> list = this.methods;
/* 280 */     int n = list.size(); int i;
/* 281 */     for (i = 0; i < n; i++) {
/* 282 */       MethodInfo minfo = list.get(i);
/* 283 */       minfo.prune(cp);
/*     */     } 
/*     */     
/* 286 */     list = this.fields;
/* 287 */     n = list.size();
/* 288 */     for (i = 0; i < n; i++) {
/* 289 */       FieldInfo finfo = (FieldInfo)list.get(i);
/* 290 */       finfo.prune(cp);
/*     */     } 
/*     */     
/* 293 */     this.attributes = newAttributes;
/* 294 */     this.constPool = cp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstPool getConstPool() {
/* 301 */     return this.constPool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInterface() {
/* 308 */     return ((this.accessFlags & 0x200) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFinal() {
/* 315 */     return ((this.accessFlags & 0x10) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAbstract() {
/* 322 */     return ((this.accessFlags & 0x400) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAccessFlags() {
/* 331 */     return this.accessFlags;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAccessFlags(int acc) {
/* 340 */     if ((acc & 0x200) == 0) {
/* 341 */       acc |= 0x20;
/*     */     }
/* 343 */     this.accessFlags = acc;
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
/*     */   public int getInnerAccessFlags() {
/* 356 */     InnerClassesAttribute ica = (InnerClassesAttribute)getAttribute("InnerClasses");
/* 357 */     if (ica == null) {
/* 358 */       return -1;
/*     */     }
/* 360 */     String name = getName();
/* 361 */     int n = ica.tableLength();
/* 362 */     for (int i = 0; i < n; i++) {
/* 363 */       if (name.equals(ica.innerClass(i)))
/* 364 */         return ica.accessFlags(i); 
/*     */     } 
/* 366 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 373 */     return this.thisclassname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/* 381 */     renameClass(this.thisclassname, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSuperclass() {
/* 388 */     if (this.cachedSuperclass == null) {
/* 389 */       this.cachedSuperclass = this.constPool.getClassInfo(this.superClass);
/*     */     }
/* 391 */     return this.cachedSuperclass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSuperclassId() {
/* 399 */     return this.superClass;
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
/*     */   public void setSuperclass(String superclass) throws CannotCompileException {
/* 411 */     if (superclass == null) {
/* 412 */       superclass = "java.lang.Object";
/*     */     }
/*     */     try {
/* 415 */       this.superClass = this.constPool.addClassInfo(superclass);
/* 416 */       ArrayList<MethodInfo> list = this.methods;
/* 417 */       int n = list.size();
/* 418 */       for (int i = 0; i < n; i++) {
/* 419 */         MethodInfo minfo = list.get(i);
/* 420 */         minfo.setSuperclass(superclass);
/*     */       }
/*     */     
/* 423 */     } catch (BadBytecode e) {
/* 424 */       throw new CannotCompileException(e);
/*     */     } 
/* 426 */     this.cachedSuperclass = superclass;
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
/*     */   public final void renameClass(String oldname, String newname) {
/* 447 */     if (oldname.equals(newname)) {
/*     */       return;
/*     */     }
/* 450 */     if (oldname.equals(this.thisclassname)) {
/* 451 */       this.thisclassname = newname;
/*     */     }
/* 453 */     oldname = Descriptor.toJvmName(oldname);
/* 454 */     newname = Descriptor.toJvmName(newname);
/* 455 */     this.constPool.renameClass(oldname, newname);
/*     */     
/* 457 */     AttributeInfo.renameClass(this.attributes, oldname, newname);
/* 458 */     ArrayList<MethodInfo> list = this.methods;
/* 459 */     int n = list.size(); int i;
/* 460 */     for (i = 0; i < n; i++) {
/* 461 */       MethodInfo minfo = list.get(i);
/* 462 */       String desc = minfo.getDescriptor();
/* 463 */       minfo.setDescriptor(Descriptor.rename(desc, oldname, newname));
/* 464 */       AttributeInfo.renameClass(minfo.getAttributes(), oldname, newname);
/*     */     } 
/*     */     
/* 467 */     list = this.fields;
/* 468 */     n = list.size();
/* 469 */     for (i = 0; i < n; i++) {
/* 470 */       FieldInfo finfo = (FieldInfo)list.get(i);
/* 471 */       String desc = finfo.getDescriptor();
/* 472 */       finfo.setDescriptor(Descriptor.rename(desc, oldname, newname));
/* 473 */       AttributeInfo.renameClass(finfo.getAttributes(), oldname, newname);
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
/*     */   public final void renameClass(Map classnames) {
/* 487 */     String jvmNewThisName = (String)classnames.get(
/* 488 */         Descriptor.toJvmName(this.thisclassname));
/* 489 */     if (jvmNewThisName != null) {
/* 490 */       this.thisclassname = Descriptor.toJavaName(jvmNewThisName);
/*     */     }
/* 492 */     this.constPool.renameClass(classnames);
/*     */     
/* 494 */     AttributeInfo.renameClass(this.attributes, classnames);
/* 495 */     ArrayList<MethodInfo> list = this.methods;
/* 496 */     int n = list.size(); int i;
/* 497 */     for (i = 0; i < n; i++) {
/* 498 */       MethodInfo minfo = list.get(i);
/* 499 */       String desc = minfo.getDescriptor();
/* 500 */       minfo.setDescriptor(Descriptor.rename(desc, classnames));
/* 501 */       AttributeInfo.renameClass(minfo.getAttributes(), classnames);
/*     */     } 
/*     */     
/* 504 */     list = this.fields;
/* 505 */     n = list.size();
/* 506 */     for (i = 0; i < n; i++) {
/* 507 */       FieldInfo finfo = (FieldInfo)list.get(i);
/* 508 */       String desc = finfo.getDescriptor();
/* 509 */       finfo.setDescriptor(Descriptor.rename(desc, classnames));
/* 510 */       AttributeInfo.renameClass(finfo.getAttributes(), classnames);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void getRefClasses(Map classnames) {
/* 519 */     this.constPool.renameClass(classnames);
/*     */     
/* 521 */     AttributeInfo.getRefClasses(this.attributes, classnames);
/* 522 */     ArrayList<MethodInfo> list = this.methods;
/* 523 */     int n = list.size(); int i;
/* 524 */     for (i = 0; i < n; i++) {
/* 525 */       MethodInfo minfo = list.get(i);
/* 526 */       String desc = minfo.getDescriptor();
/* 527 */       Descriptor.rename(desc, classnames);
/* 528 */       AttributeInfo.getRefClasses(minfo.getAttributes(), classnames);
/*     */     } 
/*     */     
/* 531 */     list = this.fields;
/* 532 */     n = list.size();
/* 533 */     for (i = 0; i < n; i++) {
/* 534 */       FieldInfo finfo = (FieldInfo)list.get(i);
/* 535 */       String desc = finfo.getDescriptor();
/* 536 */       Descriptor.rename(desc, classnames);
/* 537 */       AttributeInfo.getRefClasses(finfo.getAttributes(), classnames);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getInterfaces() {
/* 546 */     if (this.cachedInterfaces != null) {
/* 547 */       return this.cachedInterfaces;
/*     */     }
/* 549 */     String[] rtn = null;
/* 550 */     if (this.interfaces == null) {
/* 551 */       rtn = new String[0];
/*     */     } else {
/* 553 */       int n = this.interfaces.length;
/* 554 */       String[] list = new String[n];
/* 555 */       for (int i = 0; i < n; i++) {
/* 556 */         list[i] = this.constPool.getClassInfo(this.interfaces[i]);
/*     */       }
/* 558 */       rtn = list;
/*     */     } 
/*     */     
/* 561 */     this.cachedInterfaces = rtn;
/* 562 */     return rtn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInterfaces(String[] nameList) {
/* 572 */     this.cachedInterfaces = null;
/* 573 */     if (nameList != null) {
/* 574 */       int n = nameList.length;
/* 575 */       this.interfaces = new int[n];
/* 576 */       for (int i = 0; i < n; i++) {
/* 577 */         this.interfaces[i] = this.constPool.addClassInfo(nameList[i]);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addInterface(String name) {
/* 585 */     this.cachedInterfaces = null;
/* 586 */     int info = this.constPool.addClassInfo(name);
/* 587 */     if (this.interfaces == null) {
/* 588 */       this.interfaces = new int[1];
/* 589 */       this.interfaces[0] = info;
/*     */     } else {
/*     */       
/* 592 */       int n = this.interfaces.length;
/* 593 */       int[] newarray = new int[n + 1];
/* 594 */       System.arraycopy(this.interfaces, 0, newarray, 0, n);
/* 595 */       newarray[n] = info;
/* 596 */       this.interfaces = newarray;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List getFields() {
/* 607 */     return this.fields;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addField(FieldInfo finfo) throws DuplicateMemberException {
/* 616 */     testExistingField(finfo.getName(), finfo.getDescriptor());
/* 617 */     this.fields.add(finfo);
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
/*     */   public final void addField2(FieldInfo finfo) {
/* 629 */     this.fields.add(finfo);
/*     */   }
/*     */ 
/*     */   
/*     */   private void testExistingField(String name, String descriptor) throws DuplicateMemberException {
/* 634 */     ListIterator<FieldInfo> it = this.fields.listIterator(0);
/* 635 */     while (it.hasNext()) {
/* 636 */       FieldInfo minfo = it.next();
/* 637 */       if (minfo.getName().equals(name)) {
/* 638 */         throw new DuplicateMemberException("duplicate field: " + name);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List getMethods() {
/* 649 */     return this.methods;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodInfo getMethod(String name) {
/* 659 */     ArrayList<MethodInfo> list = this.methods;
/* 660 */     int n = list.size();
/* 661 */     for (int i = 0; i < n; i++) {
/* 662 */       MethodInfo minfo = list.get(i);
/* 663 */       if (minfo.getName().equals(name)) {
/* 664 */         return minfo;
/*     */       }
/*     */     } 
/* 667 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodInfo getStaticInitializer() {
/* 675 */     return getMethod("<clinit>");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMethod(MethodInfo minfo) throws DuplicateMemberException {
/* 686 */     testExistingMethod(minfo);
/* 687 */     this.methods.add(minfo);
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
/*     */   public final void addMethod2(MethodInfo minfo) {
/* 699 */     this.methods.add(minfo);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void testExistingMethod(MethodInfo newMinfo) throws DuplicateMemberException {
/* 705 */     String name = newMinfo.getName();
/* 706 */     String descriptor = newMinfo.getDescriptor();
/* 707 */     ListIterator<MethodInfo> it = this.methods.listIterator(0);
/* 708 */     while (it.hasNext()) {
/* 709 */       if (isDuplicated(newMinfo, name, descriptor, it.next(), it)) {
/* 710 */         throw new DuplicateMemberException("duplicate method: " + name + " in " + 
/* 711 */             getName());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isDuplicated(MethodInfo newMethod, String newName, String newDesc, MethodInfo minfo, ListIterator it) {
/* 718 */     if (!minfo.getName().equals(newName)) {
/* 719 */       return false;
/*     */     }
/* 721 */     String desc = minfo.getDescriptor();
/* 722 */     if (!Descriptor.eqParamTypes(desc, newDesc)) {
/* 723 */       return false;
/*     */     }
/* 725 */     if (desc.equals(newDesc)) {
/* 726 */       if (notBridgeMethod(minfo)) {
/* 727 */         return true;
/*     */       }
/*     */ 
/*     */       
/* 731 */       it.remove();
/* 732 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 736 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean notBridgeMethod(MethodInfo minfo) {
/* 743 */     return ((minfo.getAccessFlags() & 0x40) == 0);
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
/*     */   public List getAttributes() {
/* 757 */     return this.attributes;
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
/*     */   public AttributeInfo getAttribute(String name) {
/* 774 */     ArrayList<AttributeInfo> list = this.attributes;
/* 775 */     int n = list.size();
/* 776 */     for (int i = 0; i < n; i++) {
/* 777 */       AttributeInfo ai = list.get(i);
/* 778 */       if (ai.getName().equals(name)) {
/* 779 */         return ai;
/*     */       }
/*     */     } 
/* 782 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AttributeInfo removeAttribute(String name) {
/* 793 */     return AttributeInfo.remove(this.attributes, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAttribute(AttributeInfo info) {
/* 803 */     AttributeInfo.remove(this.attributes, info.getName());
/* 804 */     this.attributes.add(info);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSourceFile() {
/* 814 */     SourceFileAttribute sf = (SourceFileAttribute)getAttribute("SourceFile");
/* 815 */     if (sf == null) {
/* 816 */       return null;
/*     */     }
/* 818 */     return sf.getFileName();
/*     */   }
/*     */ 
/*     */   
/*     */   private void read(DataInputStream in) throws IOException {
/* 823 */     int magic = in.readInt();
/* 824 */     if (magic != -889275714) {
/* 825 */       throw new IOException("bad magic number: " + Integer.toHexString(magic));
/*     */     }
/* 827 */     this.minor = in.readUnsignedShort();
/* 828 */     this.major = in.readUnsignedShort();
/* 829 */     this.constPool = new ConstPool(in);
/* 830 */     this.accessFlags = in.readUnsignedShort();
/* 831 */     this.thisClass = in.readUnsignedShort();
/* 832 */     this.constPool.setThisClassInfo(this.thisClass);
/* 833 */     this.superClass = in.readUnsignedShort();
/* 834 */     int n = in.readUnsignedShort();
/* 835 */     if (n == 0) {
/* 836 */       this.interfaces = null;
/*     */     } else {
/* 838 */       this.interfaces = new int[n];
/* 839 */       for (int j = 0; j < n; j++) {
/* 840 */         this.interfaces[j] = in.readUnsignedShort();
/*     */       }
/*     */     } 
/* 843 */     ConstPool cp = this.constPool;
/* 844 */     n = in.readUnsignedShort();
/* 845 */     this.fields = new ArrayList(); int i;
/* 846 */     for (i = 0; i < n; i++) {
/* 847 */       addField2(new FieldInfo(cp, in));
/*     */     }
/* 849 */     n = in.readUnsignedShort();
/* 850 */     this.methods = new ArrayList();
/* 851 */     for (i = 0; i < n; i++) {
/* 852 */       addMethod2(new MethodInfo(cp, in));
/*     */     }
/* 854 */     this.attributes = new ArrayList();
/* 855 */     n = in.readUnsignedShort();
/* 856 */     for (i = 0; i < n; i++) {
/* 857 */       addAttribute(AttributeInfo.read(cp, in));
/*     */     }
/* 859 */     this.thisclassname = this.constPool.getClassInfo(this.thisClass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(DataOutputStream out) throws IOException {
/* 868 */     out.writeInt(-889275714);
/* 869 */     out.writeShort(this.minor);
/* 870 */     out.writeShort(this.major);
/* 871 */     this.constPool.write(out);
/* 872 */     out.writeShort(this.accessFlags);
/* 873 */     out.writeShort(this.thisClass);
/* 874 */     out.writeShort(this.superClass);
/*     */     
/* 876 */     if (this.interfaces == null) {
/* 877 */       n = 0;
/*     */     } else {
/* 879 */       n = this.interfaces.length;
/*     */     } 
/* 881 */     out.writeShort(n); int i;
/* 882 */     for (i = 0; i < n; i++) {
/* 883 */       out.writeShort(this.interfaces[i]);
/*     */     }
/* 885 */     ArrayList<FieldInfo> list = this.fields;
/* 886 */     int n = list.size();
/* 887 */     out.writeShort(n);
/* 888 */     for (i = 0; i < n; i++) {
/* 889 */       FieldInfo finfo = list.get(i);
/* 890 */       finfo.write(out);
/*     */     } 
/*     */     
/* 893 */     list = this.methods;
/* 894 */     n = list.size();
/* 895 */     out.writeShort(n);
/* 896 */     for (i = 0; i < n; i++) {
/* 897 */       MethodInfo minfo = (MethodInfo)list.get(i);
/* 898 */       minfo.write(out);
/*     */     } 
/*     */     
/* 901 */     out.writeShort(this.attributes.size());
/* 902 */     AttributeInfo.writeAll(this.attributes, out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMajorVersion() {
/* 911 */     return this.major;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMajorVersion(int major) {
/* 921 */     this.major = major;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMinorVersion() {
/* 930 */     return this.minor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMinorVersion(int minor) {
/* 940 */     this.minor = minor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVersionToJava5() {
/* 951 */     this.major = 49;
/* 952 */     this.minor = 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\ClassFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */