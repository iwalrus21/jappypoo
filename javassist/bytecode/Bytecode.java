/*      */ package javassist.bytecode;
/*      */ 
/*      */ import javassist.CtClass;
/*      */ import javassist.CtPrimitiveType;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Bytecode
/*      */   extends ByteVector
/*      */   implements Cloneable, Opcode
/*      */ {
/*  121 */   public static final CtClass THIS = ConstPool.THIS;
/*      */ 
/*      */ 
/*      */   
/*      */   ConstPool constPool;
/*      */ 
/*      */   
/*      */   int maxStack;
/*      */ 
/*      */   
/*      */   int maxLocals;
/*      */ 
/*      */   
/*      */   ExceptionTable tryblocks;
/*      */ 
/*      */   
/*      */   private int stackDepth;
/*      */ 
/*      */ 
/*      */   
/*      */   public Bytecode(ConstPool cp, int stacksize, int localvars) {
/*  142 */     this.constPool = cp;
/*  143 */     this.maxStack = stacksize;
/*  144 */     this.maxLocals = localvars;
/*  145 */     this.tryblocks = new ExceptionTable(cp);
/*  146 */     this.stackDepth = 0;
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
/*      */   public Bytecode(ConstPool cp) {
/*  159 */     this(cp, 0, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object clone() {
/*      */     try {
/*  169 */       Bytecode bc = (Bytecode)super.clone();
/*  170 */       bc.tryblocks = (ExceptionTable)this.tryblocks.clone();
/*  171 */       return bc;
/*      */     }
/*  173 */     catch (CloneNotSupportedException cnse) {
/*  174 */       throw new RuntimeException(cnse);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ConstPool getConstPool() {
/*  181 */     return this.constPool;
/*      */   }
/*      */ 
/*      */   
/*      */   public ExceptionTable getExceptionTable() {
/*  186 */     return this.tryblocks;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public CodeAttribute toCodeAttribute() {
/*  192 */     return new CodeAttribute(this.constPool, this.maxStack, this.maxLocals, 
/*  193 */         get(), this.tryblocks);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int length() {
/*  200 */     return getSize();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] get() {
/*  207 */     return copy();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxStack() {
/*  213 */     return this.maxStack;
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
/*      */   public void setMaxStack(int size) {
/*  230 */     this.maxStack = size;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxLocals() {
/*  236 */     return this.maxLocals;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMaxLocals(int size) {
/*  242 */     this.maxLocals = size;
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
/*      */   public void setMaxLocals(boolean isStatic, CtClass[] params, int locals) {
/*  260 */     if (!isStatic) {
/*  261 */       locals++;
/*      */     }
/*  263 */     if (params != null) {
/*  264 */       CtClass doubleType = CtClass.doubleType;
/*  265 */       CtClass longType = CtClass.longType;
/*  266 */       int n = params.length;
/*  267 */       for (int i = 0; i < n; i++) {
/*  268 */         CtClass type = params[i];
/*  269 */         if (type == doubleType || type == longType) {
/*  270 */           locals += 2;
/*      */         } else {
/*  272 */           locals++;
/*      */         } 
/*      */       } 
/*      */     } 
/*  276 */     this.maxLocals = locals;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void incMaxLocals(int diff) {
/*  283 */     this.maxLocals += diff;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addExceptionHandler(int start, int end, int handler, CtClass type) {
/*  291 */     addExceptionHandler(start, end, handler, this.constPool
/*  292 */         .addClassInfo(type));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addExceptionHandler(int start, int end, int handler, String type) {
/*  302 */     addExceptionHandler(start, end, handler, this.constPool
/*  303 */         .addClassInfo(type));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addExceptionHandler(int start, int end, int handler, int type) {
/*  311 */     this.tryblocks.add(start, end, handler, type);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int currentPc() {
/*  319 */     return getSize();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int read(int offset) {
/*  329 */     return super.read(offset);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int read16bit(int offset) {
/*  337 */     int v1 = read(offset);
/*  338 */     int v2 = read(offset + 1);
/*  339 */     return (v1 << 8) + (v2 & 0xFF);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int read32bit(int offset) {
/*  347 */     int v1 = read16bit(offset);
/*  348 */     int v2 = read16bit(offset + 2);
/*  349 */     return (v1 << 16) + (v2 & 0xFFFF);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void write(int offset, int value) {
/*  359 */     super.write(offset, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void write16bit(int offset, int value) {
/*  367 */     write(offset, value >> 8);
/*  368 */     write(offset + 1, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void write32bit(int offset, int value) {
/*  376 */     write16bit(offset, value >> 16);
/*  377 */     write16bit(offset + 2, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void add(int code) {
/*  384 */     super.add(code);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void add32bit(int value) {
/*  391 */     add(value >> 24, value >> 16, value >> 8, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addGap(int length) {
/*  400 */     super.addGap(length);
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
/*      */   public void addOpcode(int code) {
/*  415 */     add(code);
/*  416 */     growStack(STACK_GROW[code]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void growStack(int diff) {
/*  427 */     setStackDepth(this.stackDepth + diff);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getStackDepth() {
/*  433 */     return this.stackDepth;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setStackDepth(int depth) {
/*  443 */     this.stackDepth = depth;
/*  444 */     if (this.stackDepth > this.maxStack) {
/*  445 */       this.maxStack = this.stackDepth;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addIndex(int index) {
/*  453 */     add(index >> 8, index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addAload(int n) {
/*  462 */     if (n < 4) {
/*  463 */       addOpcode(42 + n);
/*  464 */     } else if (n < 256) {
/*  465 */       addOpcode(25);
/*  466 */       add(n);
/*      */     } else {
/*      */       
/*  469 */       addOpcode(196);
/*  470 */       addOpcode(25);
/*  471 */       addIndex(n);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addAstore(int n) {
/*  481 */     if (n < 4) {
/*  482 */       addOpcode(75 + n);
/*  483 */     } else if (n < 256) {
/*  484 */       addOpcode(58);
/*  485 */       add(n);
/*      */     } else {
/*      */       
/*  488 */       addOpcode(196);
/*  489 */       addOpcode(58);
/*  490 */       addIndex(n);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addIconst(int n) {
/*  500 */     if (n < 6 && -2 < n) {
/*  501 */       addOpcode(3 + n);
/*  502 */     } else if (n <= 127 && -128 <= n) {
/*  503 */       addOpcode(16);
/*  504 */       add(n);
/*      */     }
/*  506 */     else if (n <= 32767 && -32768 <= n) {
/*  507 */       addOpcode(17);
/*  508 */       add(n >> 8);
/*  509 */       add(n);
/*      */     } else {
/*      */       
/*  512 */       addLdc(this.constPool.addIntegerInfo(n));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addConstZero(CtClass type) {
/*  522 */     if (type.isPrimitive()) {
/*  523 */       if (type == CtClass.longType)
/*  524 */       { addOpcode(9); }
/*  525 */       else if (type == CtClass.floatType)
/*  526 */       { addOpcode(11); }
/*  527 */       else if (type == CtClass.doubleType)
/*  528 */       { addOpcode(14); }
/*  529 */       else { if (type == CtClass.voidType) {
/*  530 */           throw new RuntimeException("void type?");
/*      */         }
/*  532 */         addOpcode(3); }
/*      */     
/*      */     } else {
/*  535 */       addOpcode(1);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addIload(int n) {
/*  544 */     if (n < 4) {
/*  545 */       addOpcode(26 + n);
/*  546 */     } else if (n < 256) {
/*  547 */       addOpcode(21);
/*  548 */       add(n);
/*      */     } else {
/*      */       
/*  551 */       addOpcode(196);
/*  552 */       addOpcode(21);
/*  553 */       addIndex(n);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addIstore(int n) {
/*  563 */     if (n < 4) {
/*  564 */       addOpcode(59 + n);
/*  565 */     } else if (n < 256) {
/*  566 */       addOpcode(54);
/*  567 */       add(n);
/*      */     } else {
/*      */       
/*  570 */       addOpcode(196);
/*  571 */       addOpcode(54);
/*  572 */       addIndex(n);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addLconst(long n) {
/*  582 */     if (n == 0L || n == 1L) {
/*  583 */       addOpcode(9 + (int)n);
/*      */     } else {
/*  585 */       addLdc2w(n);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addLload(int n) {
/*  594 */     if (n < 4) {
/*  595 */       addOpcode(30 + n);
/*  596 */     } else if (n < 256) {
/*  597 */       addOpcode(22);
/*  598 */       add(n);
/*      */     } else {
/*      */       
/*  601 */       addOpcode(196);
/*  602 */       addOpcode(22);
/*  603 */       addIndex(n);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addLstore(int n) {
/*  613 */     if (n < 4) {
/*  614 */       addOpcode(63 + n);
/*  615 */     } else if (n < 256) {
/*  616 */       addOpcode(55);
/*  617 */       add(n);
/*      */     } else {
/*      */       
/*  620 */       addOpcode(196);
/*  621 */       addOpcode(55);
/*  622 */       addIndex(n);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addDconst(double d) {
/*  632 */     if (d == 0.0D || d == 1.0D) {
/*  633 */       addOpcode(14 + (int)d);
/*      */     } else {
/*  635 */       addLdc2w(d);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addDload(int n) {
/*  644 */     if (n < 4) {
/*  645 */       addOpcode(38 + n);
/*  646 */     } else if (n < 256) {
/*  647 */       addOpcode(24);
/*  648 */       add(n);
/*      */     } else {
/*      */       
/*  651 */       addOpcode(196);
/*  652 */       addOpcode(24);
/*  653 */       addIndex(n);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addDstore(int n) {
/*  663 */     if (n < 4) {
/*  664 */       addOpcode(71 + n);
/*  665 */     } else if (n < 256) {
/*  666 */       addOpcode(57);
/*  667 */       add(n);
/*      */     } else {
/*      */       
/*  670 */       addOpcode(196);
/*  671 */       addOpcode(57);
/*  672 */       addIndex(n);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addFconst(float f) {
/*  682 */     if (f == 0.0F || f == 1.0F || f == 2.0F) {
/*  683 */       addOpcode(11 + (int)f);
/*      */     } else {
/*  685 */       addLdc(this.constPool.addFloatInfo(f));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addFload(int n) {
/*  694 */     if (n < 4) {
/*  695 */       addOpcode(34 + n);
/*  696 */     } else if (n < 256) {
/*  697 */       addOpcode(23);
/*  698 */       add(n);
/*      */     } else {
/*      */       
/*  701 */       addOpcode(196);
/*  702 */       addOpcode(23);
/*  703 */       addIndex(n);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addFstore(int n) {
/*  713 */     if (n < 4) {
/*  714 */       addOpcode(67 + n);
/*  715 */     } else if (n < 256) {
/*  716 */       addOpcode(56);
/*  717 */       add(n);
/*      */     } else {
/*      */       
/*  720 */       addOpcode(196);
/*  721 */       addOpcode(56);
/*  722 */       addIndex(n);
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
/*      */   public int addLoad(int n, CtClass type) {
/*  735 */     if (type.isPrimitive())
/*  736 */     { if (type == CtClass.booleanType || type == CtClass.charType || type == CtClass.byteType || type == CtClass.shortType || type == CtClass.intType)
/*      */       
/*      */       { 
/*  739 */         addIload(n); }
/*  740 */       else { if (type == CtClass.longType) {
/*  741 */           addLload(n);
/*  742 */           return 2;
/*      */         } 
/*  744 */         if (type == CtClass.floatType)
/*  745 */         { addFload(n); }
/*  746 */         else { if (type == CtClass.doubleType) {
/*  747 */             addDload(n);
/*  748 */             return 2;
/*      */           } 
/*      */           
/*  751 */           throw new RuntimeException("void type?"); }
/*      */          }
/*      */        }
/*  754 */     else { addAload(n); }
/*      */     
/*  756 */     return 1;
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
/*      */   public int addStore(int n, CtClass type) {
/*  768 */     if (type.isPrimitive())
/*  769 */     { if (type == CtClass.booleanType || type == CtClass.charType || type == CtClass.byteType || type == CtClass.shortType || type == CtClass.intType)
/*      */       
/*      */       { 
/*  772 */         addIstore(n); }
/*  773 */       else { if (type == CtClass.longType) {
/*  774 */           addLstore(n);
/*  775 */           return 2;
/*      */         } 
/*  777 */         if (type == CtClass.floatType)
/*  778 */         { addFstore(n); }
/*  779 */         else { if (type == CtClass.doubleType) {
/*  780 */             addDstore(n);
/*  781 */             return 2;
/*      */           } 
/*      */           
/*  784 */           throw new RuntimeException("void type?"); }
/*      */          }
/*      */        }
/*  787 */     else { addAstore(n); }
/*      */     
/*  789 */     return 1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int addLoadParameters(CtClass[] params, int offset) {
/*  800 */     int stacksize = 0;
/*  801 */     if (params != null) {
/*  802 */       int n = params.length;
/*  803 */       for (int i = 0; i < n; i++) {
/*  804 */         stacksize += addLoad(stacksize + offset, params[i]);
/*      */       }
/*      */     } 
/*  807 */     return stacksize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addCheckcast(CtClass c) {
/*  816 */     addOpcode(192);
/*  817 */     addIndex(this.constPool.addClassInfo(c));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addCheckcast(String classname) {
/*  826 */     addOpcode(192);
/*  827 */     addIndex(this.constPool.addClassInfo(classname));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addInstanceof(String classname) {
/*  836 */     addOpcode(193);
/*  837 */     addIndex(this.constPool.addClassInfo(classname));
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
/*      */   public void addGetfield(CtClass c, String name, String type) {
/*  850 */     add(180);
/*  851 */     int ci = this.constPool.addClassInfo(c);
/*  852 */     addIndex(this.constPool.addFieldrefInfo(ci, name, type));
/*  853 */     growStack(Descriptor.dataSize(type) - 1);
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
/*      */   public void addGetfield(String c, String name, String type) {
/*  866 */     add(180);
/*  867 */     int ci = this.constPool.addClassInfo(c);
/*  868 */     addIndex(this.constPool.addFieldrefInfo(ci, name, type));
/*  869 */     growStack(Descriptor.dataSize(type) - 1);
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
/*      */   public void addGetstatic(CtClass c, String name, String type) {
/*  882 */     add(178);
/*  883 */     int ci = this.constPool.addClassInfo(c);
/*  884 */     addIndex(this.constPool.addFieldrefInfo(ci, name, type));
/*  885 */     growStack(Descriptor.dataSize(type));
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
/*      */   public void addGetstatic(String c, String name, String type) {
/*  898 */     add(178);
/*  899 */     int ci = this.constPool.addClassInfo(c);
/*  900 */     addIndex(this.constPool.addFieldrefInfo(ci, name, type));
/*  901 */     growStack(Descriptor.dataSize(type));
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
/*      */   public void addInvokespecial(CtClass clazz, String name, CtClass returnType, CtClass[] paramTypes) {
/*  914 */     String desc = Descriptor.ofMethod(returnType, paramTypes);
/*  915 */     addInvokespecial(clazz, name, desc);
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
/*      */   public void addInvokespecial(CtClass clazz, String name, String desc) {
/*  929 */     boolean isInterface = (clazz == null) ? false : clazz.isInterface();
/*  930 */     addInvokespecial(isInterface, this.constPool
/*  931 */         .addClassInfo(clazz), name, desc);
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
/*      */   public void addInvokespecial(String clazz, String name, String desc) {
/*  946 */     addInvokespecial(false, this.constPool.addClassInfo(clazz), name, desc);
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
/*      */   public void addInvokespecial(int clazz, String name, String desc) {
/*  962 */     addInvokespecial(false, clazz, name, desc);
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
/*      */   public void addInvokespecial(boolean isInterface, int clazz, String name, String desc) {
/*      */     int index;
/*  980 */     if (isInterface) {
/*  981 */       index = this.constPool.addInterfaceMethodrefInfo(clazz, name, desc);
/*      */     } else {
/*  983 */       index = this.constPool.addMethodrefInfo(clazz, name, desc);
/*      */     } 
/*  985 */     addInvokespecial(index, desc);
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
/*      */   public void addInvokespecial(int index, String desc) {
/*  999 */     add(183);
/* 1000 */     addIndex(index);
/* 1001 */     growStack(Descriptor.dataSize(desc) - 1);
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
/*      */   public void addInvokestatic(CtClass clazz, String name, CtClass returnType, CtClass[] paramTypes) {
/* 1014 */     String desc = Descriptor.ofMethod(returnType, paramTypes);
/* 1015 */     addInvokestatic(clazz, name, desc);
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
/*      */   public void addInvokestatic(CtClass clazz, String name, String desc) {
/*      */     boolean isInterface;
/* 1029 */     if (clazz == THIS) {
/* 1030 */       isInterface = false;
/*      */     } else {
/* 1032 */       isInterface = clazz.isInterface();
/*      */     } 
/* 1034 */     addInvokestatic(this.constPool.addClassInfo(clazz), name, desc, isInterface);
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
/*      */   public void addInvokestatic(String classname, String name, String desc) {
/* 1048 */     addInvokestatic(this.constPool.addClassInfo(classname), name, desc);
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
/*      */   public void addInvokestatic(int clazz, String name, String desc) {
/* 1062 */     addInvokestatic(clazz, name, desc, false);
/*      */   }
/*      */   
/*      */   private void addInvokestatic(int clazz, String name, String desc, boolean isInterface) {
/*      */     int index;
/* 1067 */     add(184);
/*      */     
/* 1069 */     if (isInterface) {
/* 1070 */       index = this.constPool.addInterfaceMethodrefInfo(clazz, name, desc);
/*      */     } else {
/* 1072 */       index = this.constPool.addMethodrefInfo(clazz, name, desc);
/*      */     } 
/* 1074 */     addIndex(index);
/* 1075 */     growStack(Descriptor.dataSize(desc));
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
/*      */   public void addInvokevirtual(CtClass clazz, String name, CtClass returnType, CtClass[] paramTypes) {
/* 1092 */     String desc = Descriptor.ofMethod(returnType, paramTypes);
/* 1093 */     addInvokevirtual(clazz, name, desc);
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
/*      */   public void addInvokevirtual(CtClass clazz, String name, String desc) {
/* 1110 */     addInvokevirtual(this.constPool.addClassInfo(clazz), name, desc);
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
/*      */   public void addInvokevirtual(String classname, String name, String desc) {
/* 1127 */     addInvokevirtual(this.constPool.addClassInfo(classname), name, desc);
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
/*      */   public void addInvokevirtual(int clazz, String name, String desc) {
/* 1145 */     add(182);
/* 1146 */     addIndex(this.constPool.addMethodrefInfo(clazz, name, desc));
/* 1147 */     growStack(Descriptor.dataSize(desc) - 1);
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
/*      */   public void addInvokeinterface(CtClass clazz, String name, CtClass returnType, CtClass[] paramTypes, int count) {
/* 1162 */     String desc = Descriptor.ofMethod(returnType, paramTypes);
/* 1163 */     addInvokeinterface(clazz, name, desc, count);
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
/*      */   public void addInvokeinterface(CtClass clazz, String name, String desc, int count) {
/* 1178 */     addInvokeinterface(this.constPool.addClassInfo(clazz), name, desc, count);
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
/*      */   public void addInvokeinterface(String classname, String name, String desc, int count) {
/* 1194 */     addInvokeinterface(this.constPool.addClassInfo(classname), name, desc, count);
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
/*      */   public void addInvokeinterface(int clazz, String name, String desc, int count) {
/* 1211 */     add(185);
/* 1212 */     addIndex(this.constPool.addInterfaceMethodrefInfo(clazz, name, desc));
/* 1213 */     add(count);
/* 1214 */     add(0);
/* 1215 */     growStack(Descriptor.dataSize(desc) - 1);
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
/*      */   public void addInvokedynamic(int bootstrap, String name, String desc) {
/* 1229 */     int nt = this.constPool.addNameAndTypeInfo(name, desc);
/* 1230 */     int dyn = this.constPool.addInvokeDynamicInfo(bootstrap, nt);
/* 1231 */     add(186);
/* 1232 */     addIndex(dyn);
/* 1233 */     add(0, 0);
/* 1234 */     growStack(Descriptor.dataSize(desc));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addLdc(String s) {
/* 1244 */     addLdc(this.constPool.addStringInfo(s));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addLdc(int i) {
/* 1253 */     if (i > 255) {
/* 1254 */       addOpcode(19);
/* 1255 */       addIndex(i);
/*      */     } else {
/*      */       
/* 1258 */       addOpcode(18);
/* 1259 */       add(i);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addLdc2w(long l) {
/* 1267 */     addOpcode(20);
/* 1268 */     addIndex(this.constPool.addLongInfo(l));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addLdc2w(double d) {
/* 1275 */     addOpcode(20);
/* 1276 */     addIndex(this.constPool.addDoubleInfo(d));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addNew(CtClass clazz) {
/* 1285 */     addOpcode(187);
/* 1286 */     addIndex(this.constPool.addClassInfo(clazz));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addNew(String classname) {
/* 1295 */     addOpcode(187);
/* 1296 */     addIndex(this.constPool.addClassInfo(classname));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addAnewarray(String classname) {
/* 1305 */     addOpcode(189);
/* 1306 */     addIndex(this.constPool.addClassInfo(classname));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addAnewarray(CtClass clazz, int length) {
/* 1316 */     addIconst(length);
/* 1317 */     addOpcode(189);
/* 1318 */     addIndex(this.constPool.addClassInfo(clazz));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addNewarray(int atype, int length) {
/* 1328 */     addIconst(length);
/* 1329 */     addOpcode(188);
/* 1330 */     add(atype);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int addMultiNewarray(CtClass clazz, int[] dimensions) {
/* 1341 */     int len = dimensions.length;
/* 1342 */     for (int i = 0; i < len; i++) {
/* 1343 */       addIconst(dimensions[i]);
/*      */     }
/* 1345 */     growStack(len);
/* 1346 */     return addMultiNewarray(clazz, len);
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
/*      */   public int addMultiNewarray(CtClass clazz, int dim) {
/* 1358 */     add(197);
/* 1359 */     addIndex(this.constPool.addClassInfo(clazz));
/* 1360 */     add(dim);
/* 1361 */     growStack(1 - dim);
/* 1362 */     return dim;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int addMultiNewarray(String desc, int dim) {
/* 1373 */     add(197);
/* 1374 */     addIndex(this.constPool.addClassInfo(desc));
/* 1375 */     add(dim);
/* 1376 */     growStack(1 - dim);
/* 1377 */     return dim;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addPutfield(CtClass c, String name, String desc) {
/* 1388 */     addPutfield0(c, (String)null, name, desc);
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
/*      */   public void addPutfield(String classname, String name, String desc) {
/* 1400 */     addPutfield0((CtClass)null, classname, name, desc);
/*      */   }
/*      */ 
/*      */   
/*      */   private void addPutfield0(CtClass target, String classname, String name, String desc) {
/* 1405 */     add(181);
/*      */ 
/*      */     
/* 1408 */     int ci = (classname == null) ? this.constPool.addClassInfo(target) : this.constPool.addClassInfo(classname);
/* 1409 */     addIndex(this.constPool.addFieldrefInfo(ci, name, desc));
/* 1410 */     growStack(-1 - Descriptor.dataSize(desc));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addPutstatic(CtClass c, String name, String desc) {
/* 1421 */     addPutstatic0(c, (String)null, name, desc);
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
/*      */   public void addPutstatic(String classname, String fieldName, String desc) {
/* 1433 */     addPutstatic0((CtClass)null, classname, fieldName, desc);
/*      */   }
/*      */ 
/*      */   
/*      */   private void addPutstatic0(CtClass target, String classname, String fieldName, String desc) {
/* 1438 */     add(179);
/*      */ 
/*      */     
/* 1441 */     int ci = (classname == null) ? this.constPool.addClassInfo(target) : this.constPool.addClassInfo(classname);
/* 1442 */     addIndex(this.constPool.addFieldrefInfo(ci, fieldName, desc));
/* 1443 */     growStack(-Descriptor.dataSize(desc));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addReturn(CtClass type) {
/* 1452 */     if (type == null) {
/* 1453 */       addOpcode(177);
/* 1454 */     } else if (type.isPrimitive()) {
/* 1455 */       CtPrimitiveType ptype = (CtPrimitiveType)type;
/* 1456 */       addOpcode(ptype.getReturnOp());
/*      */     } else {
/*      */       
/* 1459 */       addOpcode(176);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addRet(int var) {
/* 1468 */     if (var < 256) {
/* 1469 */       addOpcode(169);
/* 1470 */       add(var);
/*      */     } else {
/*      */       
/* 1473 */       addOpcode(196);
/* 1474 */       addOpcode(169);
/* 1475 */       addIndex(var);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addPrintln(String message) {
/* 1486 */     addGetstatic("java.lang.System", "err", "Ljava/io/PrintStream;");
/* 1487 */     addLdc(message);
/* 1488 */     addInvokevirtual("java.io.PrintStream", "println", "(Ljava/lang/String;)V");
/*      */   }
/*      */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\Bytecode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */