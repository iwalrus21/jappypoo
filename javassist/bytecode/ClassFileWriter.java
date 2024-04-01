/*     */ package javassist.bytecode;
/*     */ 
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassFileWriter
/*     */ {
/*     */   private ByteStream output;
/*     */   private ConstPoolWriter constPool;
/*     */   private FieldWriter fields;
/*     */   private MethodWriter methods;
/*     */   int thisClass;
/*     */   int superClass;
/*     */   
/*     */   public ClassFileWriter(int major, int minor) {
/*  90 */     this.output = new ByteStream(512);
/*  91 */     this.output.writeInt(-889275714);
/*  92 */     this.output.writeShort(minor);
/*  93 */     this.output.writeShort(major);
/*  94 */     this.constPool = new ConstPoolWriter(this.output);
/*  95 */     this.fields = new FieldWriter(this.constPool);
/*  96 */     this.methods = new MethodWriter(this.constPool);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstPoolWriter getConstPool() {
/* 103 */     return this.constPool;
/*     */   }
/*     */ 
/*     */   
/*     */   public FieldWriter getFieldWriter() {
/* 108 */     return this.fields;
/*     */   }
/*     */ 
/*     */   
/*     */   public MethodWriter getMethodWriter() {
/* 113 */     return this.methods;
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
/*     */   public byte[] end(int accessFlags, int thisClass, int superClass, int[] interfaces, AttributeWriter aw) {
/* 130 */     this.constPool.end();
/* 131 */     this.output.writeShort(accessFlags);
/* 132 */     this.output.writeShort(thisClass);
/* 133 */     this.output.writeShort(superClass);
/* 134 */     if (interfaces == null) {
/* 135 */       this.output.writeShort(0);
/*     */     } else {
/* 137 */       int n = interfaces.length;
/* 138 */       this.output.writeShort(n);
/* 139 */       for (int i = 0; i < n; i++) {
/* 140 */         this.output.writeShort(interfaces[i]);
/*     */       }
/*     */     } 
/* 143 */     this.output.enlarge(this.fields.dataSize() + this.methods.dataSize() + 6);
/*     */     try {
/* 145 */       this.output.writeShort(this.fields.size());
/* 146 */       this.fields.write(this.output);
/*     */       
/* 148 */       this.output.writeShort(this.methods.numOfMethods());
/* 149 */       this.methods.write(this.output);
/*     */     }
/* 151 */     catch (IOException iOException) {}
/*     */     
/* 153 */     writeAttribute(this.output, aw, 0);
/* 154 */     return this.output.toByteArray();
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
/*     */   public void end(DataOutputStream out, int accessFlags, int thisClass, int superClass, int[] interfaces, AttributeWriter aw) throws IOException {
/* 176 */     this.constPool.end();
/* 177 */     this.output.writeTo(out);
/* 178 */     out.writeShort(accessFlags);
/* 179 */     out.writeShort(thisClass);
/* 180 */     out.writeShort(superClass);
/* 181 */     if (interfaces == null) {
/* 182 */       out.writeShort(0);
/*     */     } else {
/* 184 */       int n = interfaces.length;
/* 185 */       out.writeShort(n);
/* 186 */       for (int i = 0; i < n; i++) {
/* 187 */         out.writeShort(interfaces[i]);
/*     */       }
/*     */     } 
/* 190 */     out.writeShort(this.fields.size());
/* 191 */     this.fields.write(out);
/*     */     
/* 193 */     out.writeShort(this.methods.numOfMethods());
/* 194 */     this.methods.write(out);
/* 195 */     if (aw == null) {
/* 196 */       out.writeShort(0);
/*     */     } else {
/* 198 */       out.writeShort(aw.size());
/* 199 */       aw.write(out);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void writeAttribute(ByteStream bs, AttributeWriter aw, int attrCount) {
/* 237 */     if (aw == null) {
/* 238 */       bs.writeShort(attrCount);
/*     */       
/*     */       return;
/*     */     } 
/* 242 */     bs.writeShort(aw.size() + attrCount);
/* 243 */     DataOutputStream dos = new DataOutputStream(bs);
/*     */     try {
/* 245 */       aw.write(dos);
/* 246 */       dos.flush();
/*     */     }
/* 248 */     catch (IOException iOException) {}
/*     */   }
/*     */   
/*     */   public static interface AttributeWriter {
/*     */     int size();
/*     */     
/*     */     void write(DataOutputStream param1DataOutputStream) throws IOException; }
/*     */   
/*     */   public static final class FieldWriter {
/*     */     protected ByteStream output;
/*     */     
/*     */     FieldWriter(ClassFileWriter.ConstPoolWriter cp) {
/* 260 */       this.output = new ByteStream(128);
/* 261 */       this.constPool = cp;
/* 262 */       this.fieldCount = 0;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected ClassFileWriter.ConstPoolWriter constPool;
/*     */ 
/*     */     
/*     */     private int fieldCount;
/*     */ 
/*     */ 
/*     */     
/*     */     public void add(int accessFlags, String name, String descriptor, ClassFileWriter.AttributeWriter aw) {
/* 275 */       int nameIndex = this.constPool.addUtf8Info(name);
/* 276 */       int descIndex = this.constPool.addUtf8Info(descriptor);
/* 277 */       add(accessFlags, nameIndex, descIndex, aw);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void add(int accessFlags, int name, int descriptor, ClassFileWriter.AttributeWriter aw) {
/* 290 */       this.fieldCount++;
/* 291 */       this.output.writeShort(accessFlags);
/* 292 */       this.output.writeShort(name);
/* 293 */       this.output.writeShort(descriptor);
/* 294 */       ClassFileWriter.writeAttribute(this.output, aw, 0);
/*     */     }
/*     */     int size() {
/* 297 */       return this.fieldCount;
/*     */     } int dataSize() {
/* 299 */       return this.output.size();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void write(OutputStream out) throws IOException {
/* 305 */       this.output.writeTo(out);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class MethodWriter
/*     */   {
/*     */     protected ByteStream output;
/*     */     
/*     */     protected ClassFileWriter.ConstPoolWriter constPool;
/*     */     
/*     */     private int methodCount;
/*     */     protected int codeIndex;
/*     */     protected int throwsIndex;
/*     */     protected int stackIndex;
/*     */     private int startPos;
/*     */     private boolean isAbstract;
/*     */     private int catchPos;
/*     */     private int catchCount;
/*     */     
/*     */     MethodWriter(ClassFileWriter.ConstPoolWriter cp) {
/* 326 */       this.output = new ByteStream(256);
/* 327 */       this.constPool = cp;
/* 328 */       this.methodCount = 0;
/* 329 */       this.codeIndex = 0;
/* 330 */       this.throwsIndex = 0;
/* 331 */       this.stackIndex = 0;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void begin(int accessFlags, String name, String descriptor, String[] exceptions, ClassFileWriter.AttributeWriter aw) {
/* 347 */       int intfs[], nameIndex = this.constPool.addUtf8Info(name);
/* 348 */       int descIndex = this.constPool.addUtf8Info(descriptor);
/*     */       
/* 350 */       if (exceptions == null) {
/* 351 */         intfs = null;
/*     */       } else {
/* 353 */         intfs = this.constPool.addClassInfo(exceptions);
/*     */       } 
/* 355 */       begin(accessFlags, nameIndex, descIndex, intfs, aw);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void begin(int accessFlags, int name, int descriptor, int[] exceptions, ClassFileWriter.AttributeWriter aw) {
/* 369 */       this.methodCount++;
/* 370 */       this.output.writeShort(accessFlags);
/* 371 */       this.output.writeShort(name);
/* 372 */       this.output.writeShort(descriptor);
/* 373 */       this.isAbstract = ((accessFlags & 0x400) != 0);
/*     */       
/* 375 */       int attrCount = this.isAbstract ? 0 : 1;
/* 376 */       if (exceptions != null) {
/* 377 */         attrCount++;
/*     */       }
/* 379 */       ClassFileWriter.writeAttribute(this.output, aw, attrCount);
/*     */       
/* 381 */       if (exceptions != null) {
/* 382 */         writeThrows(exceptions);
/*     */       }
/* 384 */       if (!this.isAbstract) {
/* 385 */         if (this.codeIndex == 0) {
/* 386 */           this.codeIndex = this.constPool.addUtf8Info("Code");
/*     */         }
/* 388 */         this.startPos = this.output.getPos();
/* 389 */         this.output.writeShort(this.codeIndex);
/* 390 */         this.output.writeBlank(12);
/*     */       } 
/*     */       
/* 393 */       this.catchPos = -1;
/* 394 */       this.catchCount = 0;
/*     */     }
/*     */     
/*     */     private void writeThrows(int[] exceptions) {
/* 398 */       if (this.throwsIndex == 0) {
/* 399 */         this.throwsIndex = this.constPool.addUtf8Info("Exceptions");
/*     */       }
/* 401 */       this.output.writeShort(this.throwsIndex);
/* 402 */       this.output.writeInt(exceptions.length * 2 + 2);
/* 403 */       this.output.writeShort(exceptions.length);
/* 404 */       for (int i = 0; i < exceptions.length; i++) {
/* 405 */         this.output.writeShort(exceptions[i]);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void add(int b) {
/* 414 */       this.output.write(b);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void add16(int b) {
/* 421 */       this.output.writeShort(b);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void add32(int b) {
/* 428 */       this.output.writeInt(b);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void addInvoke(int opcode, String targetClass, String methodName, String descriptor) {
/* 438 */       int target = this.constPool.addClassInfo(targetClass);
/* 439 */       int nt = this.constPool.addNameAndTypeInfo(methodName, descriptor);
/* 440 */       int method = this.constPool.addMethodrefInfo(target, nt);
/* 441 */       add(opcode);
/* 442 */       add16(method);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void codeEnd(int maxStack, int maxLocals) {
/* 449 */       if (!this.isAbstract) {
/* 450 */         this.output.writeShort(this.startPos + 6, maxStack);
/* 451 */         this.output.writeShort(this.startPos + 8, maxLocals);
/* 452 */         this.output.writeInt(this.startPos + 10, this.output.getPos() - this.startPos - 14);
/* 453 */         this.catchPos = this.output.getPos();
/* 454 */         this.catchCount = 0;
/* 455 */         this.output.writeShort(0);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void addCatch(int startPc, int endPc, int handlerPc, int catchType) {
/* 467 */       this.catchCount++;
/* 468 */       this.output.writeShort(startPc);
/* 469 */       this.output.writeShort(endPc);
/* 470 */       this.output.writeShort(handlerPc);
/* 471 */       this.output.writeShort(catchType);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void end(StackMapTable.Writer smap, ClassFileWriter.AttributeWriter aw) {
/* 483 */       if (this.isAbstract) {
/*     */         return;
/*     */       }
/*     */       
/* 487 */       this.output.writeShort(this.catchPos, this.catchCount);
/*     */       
/* 489 */       int attrCount = (smap == null) ? 0 : 1;
/* 490 */       ClassFileWriter.writeAttribute(this.output, aw, attrCount);
/*     */       
/* 492 */       if (smap != null) {
/* 493 */         if (this.stackIndex == 0) {
/* 494 */           this.stackIndex = this.constPool.addUtf8Info("StackMapTable");
/*     */         }
/* 496 */         this.output.writeShort(this.stackIndex);
/* 497 */         byte[] data = smap.toByteArray();
/* 498 */         this.output.writeInt(data.length);
/* 499 */         this.output.write(data);
/*     */       } 
/*     */ 
/*     */       
/* 503 */       this.output.writeInt(this.startPos + 2, this.output.getPos() - this.startPos - 6);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int size() {
/* 512 */       return this.output.getPos() - this.startPos - 14;
/*     */     } int numOfMethods() {
/* 514 */       return this.methodCount;
/*     */     } int dataSize() {
/* 516 */       return this.output.size();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void write(OutputStream out) throws IOException {
/* 522 */       this.output.writeTo(out);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class ConstPoolWriter
/*     */   {
/*     */     ByteStream output;
/*     */     
/*     */     protected int startPos;
/*     */     protected int num;
/*     */     
/*     */     ConstPoolWriter(ByteStream out) {
/* 535 */       this.output = out;
/* 536 */       this.startPos = out.getPos();
/* 537 */       this.num = 1;
/* 538 */       this.output.writeShort(1);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int[] addClassInfo(String[] classNames) {
/* 547 */       int n = classNames.length;
/* 548 */       int[] result = new int[n];
/* 549 */       for (int i = 0; i < n; i++) {
/* 550 */         result[i] = addClassInfo(classNames[i]);
/*     */       }
/* 552 */       return result;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int addClassInfo(String jvmname) {
/* 566 */       int utf8 = addUtf8Info(jvmname);
/* 567 */       this.output.write(7);
/* 568 */       this.output.writeShort(utf8);
/* 569 */       return this.num++;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int addClassInfo(int name) {
/* 579 */       this.output.write(7);
/* 580 */       this.output.writeShort(name);
/* 581 */       return this.num++;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int addNameAndTypeInfo(String name, String type) {
/* 592 */       return addNameAndTypeInfo(addUtf8Info(name), addUtf8Info(type));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int addNameAndTypeInfo(int name, int type) {
/* 603 */       this.output.write(12);
/* 604 */       this.output.writeShort(name);
/* 605 */       this.output.writeShort(type);
/* 606 */       return this.num++;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int addFieldrefInfo(int classInfo, int nameAndTypeInfo) {
/* 617 */       this.output.write(9);
/* 618 */       this.output.writeShort(classInfo);
/* 619 */       this.output.writeShort(nameAndTypeInfo);
/* 620 */       return this.num++;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int addMethodrefInfo(int classInfo, int nameAndTypeInfo) {
/* 631 */       this.output.write(10);
/* 632 */       this.output.writeShort(classInfo);
/* 633 */       this.output.writeShort(nameAndTypeInfo);
/* 634 */       return this.num++;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int addInterfaceMethodrefInfo(int classInfo, int nameAndTypeInfo) {
/* 647 */       this.output.write(11);
/* 648 */       this.output.writeShort(classInfo);
/* 649 */       this.output.writeShort(nameAndTypeInfo);
/* 650 */       return this.num++;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int addMethodHandleInfo(int kind, int index) {
/* 665 */       this.output.write(15);
/* 666 */       this.output.write(kind);
/* 667 */       this.output.writeShort(index);
/* 668 */       return this.num++;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int addMethodTypeInfo(int desc) {
/* 681 */       this.output.write(16);
/* 682 */       this.output.writeShort(desc);
/* 683 */       return this.num++;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int addInvokeDynamicInfo(int bootstrap, int nameAndTypeInfo) {
/* 698 */       this.output.write(18);
/* 699 */       this.output.writeShort(bootstrap);
/* 700 */       this.output.writeShort(nameAndTypeInfo);
/* 701 */       return this.num++;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int addStringInfo(String str) {
/* 714 */       int utf8 = addUtf8Info(str);
/* 715 */       this.output.write(8);
/* 716 */       this.output.writeShort(utf8);
/* 717 */       return this.num++;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int addIntegerInfo(int i) {
/* 727 */       this.output.write(3);
/* 728 */       this.output.writeInt(i);
/* 729 */       return this.num++;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int addFloatInfo(float f) {
/* 739 */       this.output.write(4);
/* 740 */       this.output.writeFloat(f);
/* 741 */       return this.num++;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int addLongInfo(long l) {
/* 751 */       this.output.write(5);
/* 752 */       this.output.writeLong(l);
/* 753 */       int n = this.num;
/* 754 */       this.num += 2;
/* 755 */       return n;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int addDoubleInfo(double d) {
/* 765 */       this.output.write(6);
/* 766 */       this.output.writeDouble(d);
/* 767 */       int n = this.num;
/* 768 */       this.num += 2;
/* 769 */       return n;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int addUtf8Info(String utf8) {
/* 779 */       this.output.write(1);
/* 780 */       this.output.writeUTF(utf8);
/* 781 */       return this.num++;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void end() {
/* 788 */       this.output.writeShort(this.startPos, this.num);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\ClassFileWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */