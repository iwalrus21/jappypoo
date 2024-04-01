/*     */ package javassist.bytecode;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javassist.ClassPool;
/*     */ import javassist.bytecode.stackmap.MapMaker;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MethodInfo
/*     */ {
/*     */   ConstPool constPool;
/*     */   int accessFlags;
/*     */   int name;
/*     */   String cachedName;
/*     */   int descriptor;
/*     */   ArrayList attribute;
/*     */   public static boolean doPreverify = false;
/*     */   public static final String nameInit = "<init>";
/*     */   public static final String nameClinit = "<clinit>";
/*     */   
/*     */   private MethodInfo(ConstPool cp) {
/*  83 */     this.constPool = cp;
/*  84 */     this.attribute = null;
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
/*     */   public MethodInfo(ConstPool cp, String methodname, String desc) {
/* 100 */     this(cp);
/* 101 */     this.accessFlags = 0;
/* 102 */     this.name = cp.addUtf8Info(methodname);
/* 103 */     this.cachedName = methodname;
/* 104 */     this.descriptor = this.constPool.addUtf8Info(desc);
/*     */   }
/*     */   
/*     */   MethodInfo(ConstPool cp, DataInputStream in) throws IOException {
/* 108 */     this(cp);
/* 109 */     read(in);
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
/*     */   public MethodInfo(ConstPool cp, String methodname, MethodInfo src, Map classnameMap) throws BadBytecode {
/* 133 */     this(cp);
/* 134 */     read(src, methodname, classnameMap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 141 */     return getName() + " " + getDescriptor();
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
/*     */   void compact(ConstPool cp) {
/* 153 */     this.name = cp.addUtf8Info(getName());
/* 154 */     this.descriptor = cp.addUtf8Info(getDescriptor());
/* 155 */     this.attribute = AttributeInfo.copyAll(this.attribute, cp);
/* 156 */     this.constPool = cp;
/*     */   }
/*     */   
/*     */   void prune(ConstPool cp) {
/* 160 */     ArrayList<AttributeInfo> newAttributes = new ArrayList();
/*     */ 
/*     */     
/* 163 */     AttributeInfo invisibleAnnotations = getAttribute("RuntimeInvisibleAnnotations");
/* 164 */     if (invisibleAnnotations != null) {
/* 165 */       invisibleAnnotations = invisibleAnnotations.copy(cp, null);
/* 166 */       newAttributes.add(invisibleAnnotations);
/*     */     } 
/*     */ 
/*     */     
/* 170 */     AttributeInfo visibleAnnotations = getAttribute("RuntimeVisibleAnnotations");
/* 171 */     if (visibleAnnotations != null) {
/* 172 */       visibleAnnotations = visibleAnnotations.copy(cp, null);
/* 173 */       newAttributes.add(visibleAnnotations);
/*     */     } 
/*     */ 
/*     */     
/* 177 */     AttributeInfo parameterInvisibleAnnotations = getAttribute("RuntimeInvisibleParameterAnnotations");
/* 178 */     if (parameterInvisibleAnnotations != null) {
/* 179 */       parameterInvisibleAnnotations = parameterInvisibleAnnotations.copy(cp, null);
/* 180 */       newAttributes.add(parameterInvisibleAnnotations);
/*     */     } 
/*     */ 
/*     */     
/* 184 */     AttributeInfo parameterVisibleAnnotations = getAttribute("RuntimeVisibleParameterAnnotations");
/* 185 */     if (parameterVisibleAnnotations != null) {
/* 186 */       parameterVisibleAnnotations = parameterVisibleAnnotations.copy(cp, null);
/* 187 */       newAttributes.add(parameterVisibleAnnotations);
/*     */     } 
/*     */ 
/*     */     
/* 191 */     AnnotationDefaultAttribute defaultAttribute = (AnnotationDefaultAttribute)getAttribute("AnnotationDefault");
/* 192 */     if (defaultAttribute != null) {
/* 193 */       newAttributes.add(defaultAttribute);
/*     */     }
/* 195 */     ExceptionsAttribute ea = getExceptionsAttribute();
/* 196 */     if (ea != null) {
/* 197 */       newAttributes.add(ea);
/*     */     }
/*     */     
/* 200 */     AttributeInfo signature = getAttribute("Signature");
/* 201 */     if (signature != null) {
/* 202 */       signature = signature.copy(cp, null);
/* 203 */       newAttributes.add(signature);
/*     */     } 
/*     */     
/* 206 */     this.attribute = newAttributes;
/* 207 */     this.name = cp.addUtf8Info(getName());
/* 208 */     this.descriptor = cp.addUtf8Info(getDescriptor());
/* 209 */     this.constPool = cp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 216 */     if (this.cachedName == null) {
/* 217 */       this.cachedName = this.constPool.getUtf8Info(this.name);
/*     */     }
/* 219 */     return this.cachedName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String newName) {
/* 226 */     this.name = this.constPool.addUtf8Info(newName);
/* 227 */     this.cachedName = newName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMethod() {
/* 235 */     String n = getName();
/* 236 */     return (!n.equals("<init>") && !n.equals("<clinit>"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstPool getConstPool() {
/* 243 */     return this.constPool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConstructor() {
/* 250 */     return getName().equals("<init>");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStaticInitializer() {
/* 257 */     return getName().equals("<clinit>");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAccessFlags() {
/* 266 */     return this.accessFlags;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAccessFlags(int acc) {
/* 275 */     this.accessFlags = acc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescriptor() {
/* 284 */     return this.constPool.getUtf8Info(this.descriptor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDescriptor(String desc) {
/* 293 */     if (!desc.equals(getDescriptor())) {
/* 294 */       this.descriptor = this.constPool.addUtf8Info(desc);
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
/*     */   public List getAttributes() {
/* 308 */     if (this.attribute == null) {
/* 309 */       this.attribute = new ArrayList();
/*     */     }
/* 311 */     return this.attribute;
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
/* 328 */     return AttributeInfo.lookup(this.attribute, name);
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
/* 339 */     return AttributeInfo.remove(this.attribute, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAttribute(AttributeInfo info) {
/* 349 */     if (this.attribute == null) {
/* 350 */       this.attribute = new ArrayList();
/*     */     }
/* 352 */     AttributeInfo.remove(this.attribute, info.getName());
/* 353 */     this.attribute.add(info);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExceptionsAttribute getExceptionsAttribute() {
/* 362 */     AttributeInfo info = AttributeInfo.lookup(this.attribute, "Exceptions");
/*     */     
/* 364 */     return (ExceptionsAttribute)info;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CodeAttribute getCodeAttribute() {
/* 373 */     AttributeInfo info = AttributeInfo.lookup(this.attribute, "Code");
/* 374 */     return (CodeAttribute)info;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeExceptionsAttribute() {
/* 381 */     AttributeInfo.remove(this.attribute, "Exceptions");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExceptionsAttribute(ExceptionsAttribute cattr) {
/* 392 */     removeExceptionsAttribute();
/* 393 */     if (this.attribute == null) {
/* 394 */       this.attribute = new ArrayList();
/*     */     }
/* 396 */     this.attribute.add(cattr);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeCodeAttribute() {
/* 403 */     AttributeInfo.remove(this.attribute, "Code");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCodeAttribute(CodeAttribute cattr) {
/* 414 */     removeCodeAttribute();
/* 415 */     if (this.attribute == null) {
/* 416 */       this.attribute = new ArrayList();
/*     */     }
/* 418 */     this.attribute.add(cattr);
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
/*     */   public void rebuildStackMapIf6(ClassPool pool, ClassFile cf) throws BadBytecode {
/* 437 */     if (cf.getMajorVersion() >= 50) {
/* 438 */       rebuildStackMap(pool);
/*     */     }
/* 440 */     if (doPreverify) {
/* 441 */       rebuildStackMapForME(pool);
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
/*     */   public void rebuildStackMap(ClassPool pool) throws BadBytecode {
/* 454 */     CodeAttribute ca = getCodeAttribute();
/* 455 */     if (ca != null) {
/* 456 */       StackMapTable smt = MapMaker.make(pool, this);
/* 457 */       ca.setAttribute(smt);
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
/*     */   public void rebuildStackMapForME(ClassPool pool) throws BadBytecode {
/* 471 */     CodeAttribute ca = getCodeAttribute();
/* 472 */     if (ca != null) {
/* 473 */       StackMap sm = MapMaker.make2(pool, this);
/* 474 */       ca.setAttribute(sm);
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
/*     */   public int getLineNumber(int pos) {
/* 488 */     CodeAttribute ca = getCodeAttribute();
/* 489 */     if (ca == null) {
/* 490 */       return -1;
/*     */     }
/*     */     
/* 493 */     LineNumberAttribute ainfo = (LineNumberAttribute)ca.getAttribute("LineNumberTable");
/* 494 */     if (ainfo == null) {
/* 495 */       return -1;
/*     */     }
/* 497 */     return ainfo.toLineNumber(pos);
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
/*     */   public void setSuperclass(String superclass) throws BadBytecode {
/* 522 */     if (!isConstructor()) {
/*     */       return;
/*     */     }
/* 525 */     CodeAttribute ca = getCodeAttribute();
/* 526 */     byte[] code = ca.getCode();
/* 527 */     CodeIterator iterator = ca.iterator();
/* 528 */     int pos = iterator.skipSuperConstructor();
/* 529 */     if (pos >= 0) {
/* 530 */       ConstPool cp = this.constPool;
/* 531 */       int mref = ByteArray.readU16bit(code, pos + 1);
/* 532 */       int nt = cp.getMethodrefNameAndType(mref);
/* 533 */       int sc = cp.addClassInfo(superclass);
/* 534 */       int mref2 = cp.addMethodrefInfo(sc, nt);
/* 535 */       ByteArray.write16bit(mref2, code, pos + 1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void read(MethodInfo src, String methodname, Map classnames) throws BadBytecode {
/* 541 */     ConstPool destCp = this.constPool;
/* 542 */     this.accessFlags = src.accessFlags;
/* 543 */     this.name = destCp.addUtf8Info(methodname);
/* 544 */     this.cachedName = methodname;
/* 545 */     ConstPool srcCp = src.constPool;
/* 546 */     String desc = srcCp.getUtf8Info(src.descriptor);
/* 547 */     String desc2 = Descriptor.rename(desc, classnames);
/* 548 */     this.descriptor = destCp.addUtf8Info(desc2);
/*     */     
/* 550 */     this.attribute = new ArrayList();
/* 551 */     ExceptionsAttribute eattr = src.getExceptionsAttribute();
/* 552 */     if (eattr != null) {
/* 553 */       this.attribute.add(eattr.copy(destCp, classnames));
/*     */     }
/* 555 */     CodeAttribute cattr = src.getCodeAttribute();
/* 556 */     if (cattr != null)
/* 557 */       this.attribute.add(cattr.copy(destCp, classnames)); 
/*     */   }
/*     */   
/*     */   private void read(DataInputStream in) throws IOException {
/* 561 */     this.accessFlags = in.readUnsignedShort();
/* 562 */     this.name = in.readUnsignedShort();
/* 563 */     this.descriptor = in.readUnsignedShort();
/* 564 */     int n = in.readUnsignedShort();
/* 565 */     this.attribute = new ArrayList();
/* 566 */     for (int i = 0; i < n; i++)
/* 567 */       this.attribute.add(AttributeInfo.read(this.constPool, in)); 
/*     */   }
/*     */   
/*     */   void write(DataOutputStream out) throws IOException {
/* 571 */     out.writeShort(this.accessFlags);
/* 572 */     out.writeShort(this.name);
/* 573 */     out.writeShort(this.descriptor);
/*     */     
/* 575 */     if (this.attribute == null) {
/* 576 */       out.writeShort(0);
/*     */     } else {
/* 578 */       out.writeShort(this.attribute.size());
/* 579 */       AttributeInfo.writeAll(this.attribute, out);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\MethodInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */