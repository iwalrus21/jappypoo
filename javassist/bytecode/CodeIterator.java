/*      */ package javassist.bytecode;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class CodeIterator
/*      */   implements Opcode
/*      */ {
/*      */   protected CodeAttribute codeAttr;
/*      */   protected byte[] bytecode;
/*      */   protected int endPos;
/*      */   protected int currentPos;
/*      */   protected int mark;
/*      */   
/*      */   protected CodeIterator(CodeAttribute ca) {
/*   57 */     this.codeAttr = ca;
/*   58 */     this.bytecode = ca.getCode();
/*   59 */     begin();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void begin() {
/*   66 */     this.currentPos = this.mark = 0;
/*   67 */     this.endPos = getCodeLength();
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
/*      */   public void move(int index) {
/*   83 */     this.currentPos = index;
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
/*      */   public void setMark(int index) {
/*   97 */     this.mark = index;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMark() {
/*  108 */     return this.mark;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public CodeAttribute get() {
/*  114 */     return this.codeAttr;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getCodeLength() {
/*  121 */     return this.bytecode.length;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int byteAt(int index) {
/*  127 */     return this.bytecode[index] & 0xFF;
/*      */   }
/*      */ 
/*      */   
/*      */   public int signedByteAt(int index) {
/*  132 */     return this.bytecode[index];
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeByte(int value, int index) {
/*  138 */     this.bytecode[index] = (byte)value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int u16bitAt(int index) {
/*  145 */     return ByteArray.readU16bit(this.bytecode, index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int s16bitAt(int index) {
/*  152 */     return ByteArray.readS16bit(this.bytecode, index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void write16bit(int value, int index) {
/*  159 */     ByteArray.write16bit(value, this.bytecode, index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int s32bitAt(int index) {
/*  166 */     return ByteArray.read32bit(this.bytecode, index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void write32bit(int value, int index) {
/*  173 */     ByteArray.write32bit(value, this.bytecode, index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void write(byte[] code, int index) {
/*  182 */     int len = code.length;
/*  183 */     for (int j = 0; j < len; j++) {
/*  184 */       this.bytecode[index++] = code[j];
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasNext() {
/*  190 */     return (this.currentPos < this.endPos);
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
/*      */   public int next() throws BadBytecode {
/*  203 */     int pos = this.currentPos;
/*  204 */     this.currentPos = nextOpcode(this.bytecode, pos);
/*  205 */     return pos;
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
/*      */   public int lookAhead() {
/*  217 */     return this.currentPos;
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
/*      */   public int skipConstructor() throws BadBytecode {
/*  239 */     return skipSuperConstructor0(-1);
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
/*      */   public int skipSuperConstructor() throws BadBytecode {
/*  261 */     return skipSuperConstructor0(0);
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
/*      */   public int skipThisConstructor() throws BadBytecode {
/*  283 */     return skipSuperConstructor0(1);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private int skipSuperConstructor0(int skipThis) throws BadBytecode {
/*  289 */     begin();
/*  290 */     ConstPool cp = this.codeAttr.getConstPool();
/*  291 */     String thisClassName = this.codeAttr.getDeclaringClass();
/*  292 */     int nested = 0;
/*  293 */     while (hasNext()) {
/*  294 */       int index = next();
/*  295 */       int c = byteAt(index);
/*  296 */       if (c == 187) {
/*  297 */         nested++; continue;
/*  298 */       }  if (c == 183) {
/*  299 */         int mref = ByteArray.readU16bit(this.bytecode, index + 1);
/*  300 */         if (cp.getMethodrefName(mref).equals("<init>") && 
/*  301 */           --nested < 0) {
/*  302 */           if (skipThis < 0) {
/*  303 */             return index;
/*      */           }
/*  305 */           String cname = cp.getMethodrefClassName(mref);
/*  306 */           if (cname.equals(thisClassName) == ((skipThis > 0))) {
/*  307 */             return index;
/*      */           }
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/*  314 */     begin();
/*  315 */     return -1;
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
/*      */   public int insert(byte[] code) throws BadBytecode {
/*  339 */     return insert0(this.currentPos, code, false);
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
/*      */   public void insert(int pos, byte[] code) throws BadBytecode {
/*  364 */     insert0(pos, code, false);
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
/*      */   public int insertAt(int pos, byte[] code) throws BadBytecode {
/*  388 */     return insert0(pos, code, false);
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
/*      */   public int insertEx(byte[] code) throws BadBytecode {
/*  412 */     return insert0(this.currentPos, code, true);
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
/*      */   public void insertEx(int pos, byte[] code) throws BadBytecode {
/*  437 */     insert0(pos, code, true);
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
/*      */   public int insertExAt(int pos, byte[] code) throws BadBytecode {
/*  461 */     return insert0(pos, code, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int insert0(int pos, byte[] code, boolean exclusive) throws BadBytecode {
/*  471 */     int len = code.length;
/*  472 */     if (len <= 0) {
/*  473 */       return pos;
/*      */     }
/*      */     
/*  476 */     pos = (insertGapAt(pos, len, exclusive)).position;
/*      */     
/*  478 */     int p = pos;
/*  479 */     for (int j = 0; j < len; j++) {
/*  480 */       this.bytecode[p++] = code[j];
/*      */     }
/*  482 */     return pos;
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
/*      */   public int insertGap(int length) throws BadBytecode {
/*  501 */     return (insertGapAt(this.currentPos, length, false)).position;
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
/*      */   public int insertGap(int pos, int length) throws BadBytecode {
/*  521 */     return (insertGapAt(pos, length, false)).length;
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
/*      */   public int insertExGap(int length) throws BadBytecode {
/*  540 */     return (insertGapAt(this.currentPos, length, true)).position;
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
/*      */   public int insertExGap(int pos, int length) throws BadBytecode {
/*  560 */     return (insertGapAt(pos, length, true)).length;
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
/*      */   public static class Gap
/*      */   {
/*      */     public int position;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int length;
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
/*      */   public Gap insertGapAt(int pos, int length, boolean exclusive) throws BadBytecode {
/*      */     byte[] c;
/*      */     int length2;
/*  619 */     Gap gap = new Gap();
/*  620 */     if (length <= 0) {
/*  621 */       gap.position = pos;
/*  622 */       gap.length = 0;
/*  623 */       return gap;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  628 */     if (this.bytecode.length + length > 32767) {
/*      */       
/*  630 */       c = insertGapCore0w(this.bytecode, pos, length, exclusive, 
/*  631 */           get().getExceptionTable(), this.codeAttr, gap);
/*  632 */       pos = gap.position;
/*  633 */       length2 = length;
/*      */     } else {
/*      */       
/*  636 */       int cur = this.currentPos;
/*  637 */       c = insertGapCore0(this.bytecode, pos, length, exclusive, 
/*  638 */           get().getExceptionTable(), this.codeAttr);
/*      */       
/*  640 */       length2 = c.length - this.bytecode.length;
/*  641 */       gap.position = pos;
/*  642 */       gap.length = length2;
/*  643 */       if (cur >= pos) {
/*  644 */         this.currentPos = cur + length2;
/*      */       }
/*  646 */       if (this.mark > pos || (this.mark == pos && exclusive)) {
/*  647 */         this.mark += length2;
/*      */       }
/*      */     } 
/*  650 */     this.codeAttr.setCode(c);
/*  651 */     this.bytecode = c;
/*  652 */     this.endPos = getCodeLength();
/*  653 */     updateCursors(pos, length2);
/*  654 */     return gap;
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
/*      */   protected void updateCursors(int pos, int length) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void insert(ExceptionTable et, int offset) {
/*  677 */     this.codeAttr.getExceptionTable().add(0, et, offset);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int append(byte[] code) {
/*  687 */     int size = getCodeLength();
/*  688 */     int len = code.length;
/*  689 */     if (len <= 0) {
/*  690 */       return size;
/*      */     }
/*  692 */     appendGap(len);
/*  693 */     byte[] dest = this.bytecode;
/*  694 */     for (int i = 0; i < len; i++) {
/*  695 */       dest[i + size] = code[i];
/*      */     }
/*  697 */     return size;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void appendGap(int gapLength) {
/*  706 */     byte[] code = this.bytecode;
/*  707 */     int codeLength = code.length;
/*  708 */     byte[] newcode = new byte[codeLength + gapLength];
/*      */     
/*      */     int i;
/*  711 */     for (i = 0; i < codeLength; i++) {
/*  712 */       newcode[i] = code[i];
/*      */     }
/*  714 */     for (i = codeLength; i < codeLength + gapLength; i++) {
/*  715 */       newcode[i] = 0;
/*      */     }
/*  717 */     this.codeAttr.setCode(newcode);
/*  718 */     this.bytecode = newcode;
/*  719 */     this.endPos = getCodeLength();
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
/*      */   public void append(ExceptionTable et, int offset) {
/*  731 */     ExceptionTable table = this.codeAttr.getExceptionTable();
/*  732 */     table.add(table.size(), et, offset);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  737 */   private static final int[] opcodeLength = new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 3, 2, 3, 3, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 0, 0, 1, 1, 1, 1, 1, 1, 3, 3, 3, 3, 3, 3, 3, 5, 5, 3, 2, 3, 1, 1, 3, 3, 1, 1, 0, 4, 3, 3, 5, 5 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int nextOpcode(byte[] code, int index) throws BadBytecode {
/*      */     int opcode;
/*      */     try {
/*  760 */       opcode = code[index] & 0xFF;
/*      */     }
/*  762 */     catch (IndexOutOfBoundsException e) {
/*  763 */       throw new BadBytecode("invalid opcode address");
/*      */     } 
/*      */     
/*      */     try {
/*  767 */       int len = opcodeLength[opcode];
/*  768 */       if (len > 0)
/*  769 */         return index + len; 
/*  770 */       if (opcode == 196) {
/*  771 */         if (code[index + 1] == -124) {
/*  772 */           return index + 6;
/*      */         }
/*  774 */         return index + 4;
/*      */       } 
/*  776 */       int index2 = (index & 0xFFFFFFFC) + 8;
/*  777 */       if (opcode == 171) {
/*  778 */         int npairs = ByteArray.read32bit(code, index2);
/*  779 */         return index2 + npairs * 8 + 4;
/*      */       } 
/*  781 */       if (opcode == 170) {
/*  782 */         int low = ByteArray.read32bit(code, index2);
/*  783 */         int high = ByteArray.read32bit(code, index2 + 4);
/*  784 */         return index2 + (high - low + 1) * 4 + 8;
/*      */       
/*      */       }
/*      */ 
/*      */     
/*      */     }
/*  790 */     catch (IndexOutOfBoundsException indexOutOfBoundsException) {}
/*      */ 
/*      */ 
/*      */     
/*  794 */     throw new BadBytecode(opcode);
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
/*      */   static class AlignmentException
/*      */     extends Exception {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static byte[] insertGapCore0(byte[] code, int where, int gapLength, boolean exclusive, ExceptionTable etable, CodeAttribute ca) throws BadBytecode {
/*  820 */     if (gapLength <= 0) {
/*  821 */       return code;
/*      */     }
/*      */     try {
/*  824 */       return insertGapCore1(code, where, gapLength, exclusive, etable, ca);
/*      */     }
/*  826 */     catch (AlignmentException e) {
/*      */       try {
/*  828 */         return insertGapCore1(code, where, gapLength + 3 & 0xFFFFFFFC, exclusive, etable, ca);
/*      */       
/*      */       }
/*  831 */       catch (AlignmentException e2) {
/*  832 */         throw new RuntimeException("fatal error?");
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte[] insertGapCore1(byte[] code, int where, int gapLength, boolean exclusive, ExceptionTable etable, CodeAttribute ca) throws BadBytecode, AlignmentException {
/*  842 */     int codeLength = code.length;
/*  843 */     byte[] newcode = new byte[codeLength + gapLength];
/*  844 */     insertGap2(code, where, gapLength, codeLength, newcode, exclusive);
/*  845 */     etable.shiftPc(where, gapLength, exclusive);
/*      */     
/*  847 */     LineNumberAttribute na = (LineNumberAttribute)ca.getAttribute("LineNumberTable");
/*  848 */     if (na != null) {
/*  849 */       na.shiftPc(where, gapLength, exclusive);
/*      */     }
/*  851 */     LocalVariableAttribute va = (LocalVariableAttribute)ca.getAttribute("LocalVariableTable");
/*      */     
/*  853 */     if (va != null) {
/*  854 */       va.shiftPc(where, gapLength, exclusive);
/*      */     }
/*      */     
/*  857 */     LocalVariableAttribute vta = (LocalVariableAttribute)ca.getAttribute("LocalVariableTypeTable");
/*      */     
/*  859 */     if (vta != null) {
/*  860 */       vta.shiftPc(where, gapLength, exclusive);
/*      */     }
/*  862 */     StackMapTable smt = (StackMapTable)ca.getAttribute("StackMapTable");
/*  863 */     if (smt != null) {
/*  864 */       smt.shiftPc(where, gapLength, exclusive);
/*      */     }
/*  866 */     StackMap sm = (StackMap)ca.getAttribute("StackMap");
/*  867 */     if (sm != null) {
/*  868 */       sm.shiftPc(where, gapLength, exclusive);
/*      */     }
/*  870 */     return newcode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void insertGap2(byte[] code, int where, int gapLength, int endPos, byte[] newcode, boolean exclusive) throws BadBytecode, AlignmentException {
/*  878 */     int i = 0;
/*  879 */     int j = 0;
/*  880 */     for (; i < endPos; i = nextPos) {
/*  881 */       if (i == where) {
/*  882 */         int j2 = j + gapLength;
/*  883 */         while (j < j2) {
/*  884 */           newcode[j++] = 0;
/*      */         }
/*      */       } 
/*  887 */       int nextPos = nextOpcode(code, i);
/*  888 */       int inst = code[i] & 0xFF;
/*      */       
/*  890 */       if ((153 <= inst && inst <= 168) || inst == 198 || inst == 199) {
/*      */ 
/*      */         
/*  893 */         int offset = code[i + 1] << 8 | code[i + 2] & 0xFF;
/*  894 */         offset = newOffset(i, offset, where, gapLength, exclusive);
/*  895 */         newcode[j] = code[i];
/*  896 */         ByteArray.write16bit(offset, newcode, j + 1);
/*  897 */         j += 3;
/*      */       }
/*  899 */       else if (inst == 200 || inst == 201) {
/*      */         
/*  901 */         int offset = ByteArray.read32bit(code, i + 1);
/*  902 */         offset = newOffset(i, offset, where, gapLength, exclusive);
/*  903 */         newcode[j++] = code[i];
/*  904 */         ByteArray.write32bit(offset, newcode, j);
/*  905 */         j += 4;
/*      */       }
/*  907 */       else if (inst == 170) {
/*  908 */         if (i != j && (gapLength & 0x3) != 0) {
/*  909 */           throw new AlignmentException();
/*      */         }
/*  911 */         int i2 = (i & 0xFFFFFFFC) + 4;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  918 */         j = copyGapBytes(newcode, j, code, i, i2);
/*      */         
/*  920 */         int defaultbyte = newOffset(i, ByteArray.read32bit(code, i2), where, gapLength, exclusive);
/*      */         
/*  922 */         ByteArray.write32bit(defaultbyte, newcode, j);
/*  923 */         int lowbyte = ByteArray.read32bit(code, i2 + 4);
/*  924 */         ByteArray.write32bit(lowbyte, newcode, j + 4);
/*  925 */         int highbyte = ByteArray.read32bit(code, i2 + 8);
/*  926 */         ByteArray.write32bit(highbyte, newcode, j + 8);
/*  927 */         j += 12;
/*  928 */         int i0 = i2 + 12;
/*  929 */         i2 = i0 + (highbyte - lowbyte + 1) * 4;
/*  930 */         while (i0 < i2) {
/*  931 */           int offset = newOffset(i, ByteArray.read32bit(code, i0), where, gapLength, exclusive);
/*      */           
/*  933 */           ByteArray.write32bit(offset, newcode, j);
/*  934 */           j += 4;
/*  935 */           i0 += 4;
/*      */         }
/*      */       
/*  938 */       } else if (inst == 171) {
/*  939 */         if (i != j && (gapLength & 0x3) != 0) {
/*  940 */           throw new AlignmentException();
/*      */         }
/*  942 */         int i2 = (i & 0xFFFFFFFC) + 4;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  950 */         j = copyGapBytes(newcode, j, code, i, i2);
/*      */         
/*  952 */         int defaultbyte = newOffset(i, ByteArray.read32bit(code, i2), where, gapLength, exclusive);
/*      */         
/*  954 */         ByteArray.write32bit(defaultbyte, newcode, j);
/*  955 */         int npairs = ByteArray.read32bit(code, i2 + 4);
/*  956 */         ByteArray.write32bit(npairs, newcode, j + 4);
/*  957 */         j += 8;
/*  958 */         int i0 = i2 + 8;
/*  959 */         i2 = i0 + npairs * 8;
/*  960 */         while (i0 < i2) {
/*  961 */           ByteArray.copy32bit(code, i0, newcode, j);
/*  962 */           int offset = newOffset(i, 
/*  963 */               ByteArray.read32bit(code, i0 + 4), where, gapLength, exclusive);
/*      */           
/*  965 */           ByteArray.write32bit(offset, newcode, j + 4);
/*  966 */           j += 8;
/*  967 */           i0 += 8;
/*      */         } 
/*      */       } else {
/*      */         
/*  971 */         while (i < nextPos)
/*  972 */           newcode[j++] = code[i++]; 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private static int copyGapBytes(byte[] newcode, int j, byte[] code, int i, int iEnd) {
/*  978 */     switch (iEnd - i) {
/*      */       case 4:
/*  980 */         newcode[j++] = code[i++];
/*      */       case 3:
/*  982 */         newcode[j++] = code[i++];
/*      */       case 2:
/*  984 */         newcode[j++] = code[i++];
/*      */       case 1:
/*  986 */         newcode[j++] = code[i++];
/*      */         break;
/*      */     } 
/*      */     
/*  990 */     return j;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int newOffset(int i, int offset, int where, int gapLength, boolean exclusive) {
/*  995 */     int target = i + offset;
/*  996 */     if (i < where) {
/*  997 */       if (where < target || (exclusive && where == target)) {
/*  998 */         offset += gapLength;
/*      */       }
/* 1000 */     } else if (i == where) {
/*      */ 
/*      */       
/* 1003 */       if (target < where) {
/* 1004 */         offset -= gapLength;
/*      */       }
/*      */     }
/* 1007 */     else if (target < where || (!exclusive && where == target)) {
/* 1008 */       offset -= gapLength;
/*      */     } 
/* 1010 */     return offset;
/*      */   }
/*      */   static class Pointers { int cursor;
/*      */     int mark0;
/*      */     int mark;
/*      */     ExceptionTable etable;
/*      */     LineNumberAttribute line;
/*      */     LocalVariableAttribute vars;
/*      */     LocalVariableAttribute types;
/*      */     StackMapTable stack;
/*      */     StackMap stack2;
/*      */     
/*      */     Pointers(int cur, int m, int m0, ExceptionTable et, CodeAttribute ca) {
/* 1023 */       this.cursor = cur;
/* 1024 */       this.mark = m;
/* 1025 */       this.mark0 = m0;
/* 1026 */       this.etable = et;
/* 1027 */       this.line = (LineNumberAttribute)ca.getAttribute("LineNumberTable");
/* 1028 */       this.vars = (LocalVariableAttribute)ca.getAttribute("LocalVariableTable");
/* 1029 */       this.types = (LocalVariableAttribute)ca.getAttribute("LocalVariableTypeTable");
/* 1030 */       this.stack = (StackMapTable)ca.getAttribute("StackMapTable");
/* 1031 */       this.stack2 = (StackMap)ca.getAttribute("StackMap");
/*      */     }
/*      */     
/*      */     void shiftPc(int where, int gapLength, boolean exclusive) throws BadBytecode {
/* 1035 */       if (where < this.cursor || (where == this.cursor && exclusive)) {
/* 1036 */         this.cursor += gapLength;
/*      */       }
/* 1038 */       if (where < this.mark || (where == this.mark && exclusive)) {
/* 1039 */         this.mark += gapLength;
/*      */       }
/* 1041 */       if (where < this.mark0 || (where == this.mark0 && exclusive)) {
/* 1042 */         this.mark0 += gapLength;
/*      */       }
/* 1044 */       this.etable.shiftPc(where, gapLength, exclusive);
/* 1045 */       if (this.line != null) {
/* 1046 */         this.line.shiftPc(where, gapLength, exclusive);
/*      */       }
/* 1048 */       if (this.vars != null) {
/* 1049 */         this.vars.shiftPc(where, gapLength, exclusive);
/*      */       }
/* 1051 */       if (this.types != null) {
/* 1052 */         this.types.shiftPc(where, gapLength, exclusive);
/*      */       }
/* 1054 */       if (this.stack != null) {
/* 1055 */         this.stack.shiftPc(where, gapLength, exclusive);
/*      */       }
/* 1057 */       if (this.stack2 != null)
/* 1058 */         this.stack2.shiftPc(where, gapLength, exclusive); 
/*      */     }
/*      */     
/*      */     void shiftForSwitch(int where, int gapLength) throws BadBytecode {
/* 1062 */       if (this.stack != null) {
/* 1063 */         this.stack.shiftForSwitch(where, gapLength);
/*      */       }
/* 1065 */       if (this.stack2 != null) {
/* 1066 */         this.stack2.shiftForSwitch(where, gapLength);
/*      */       }
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static byte[] changeLdcToLdcW(byte[] code, ExceptionTable etable, CodeAttribute ca, CodeAttribute.LdcEntry ldcs) throws BadBytecode {
/* 1077 */     Pointers pointers = new Pointers(0, 0, 0, etable, ca);
/* 1078 */     ArrayList jumps = makeJumpList(code, code.length, pointers);
/* 1079 */     while (ldcs != null) {
/* 1080 */       addLdcW(ldcs, jumps);
/* 1081 */       ldcs = ldcs.next;
/*      */     } 
/*      */     
/* 1084 */     byte[] r = insertGap2w(code, 0, 0, false, jumps, pointers);
/* 1085 */     return r;
/*      */   }
/*      */   
/*      */   private static void addLdcW(CodeAttribute.LdcEntry ldcs, ArrayList<LdcW> jumps) {
/* 1089 */     int where = ldcs.where;
/* 1090 */     LdcW ldcw = new LdcW(where, ldcs.index);
/* 1091 */     int s = jumps.size();
/* 1092 */     for (int i = 0; i < s; i++) {
/* 1093 */       if (where < ((Branch)jumps.get(i)).orgPos) {
/* 1094 */         jumps.add(i, ldcw);
/*      */         return;
/*      */       } 
/*      */     } 
/* 1098 */     jumps.add(ldcw);
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
/*      */   private byte[] insertGapCore0w(byte[] code, int where, int gapLength, boolean exclusive, ExceptionTable etable, CodeAttribute ca, Gap newWhere) throws BadBytecode {
/* 1118 */     if (gapLength <= 0) {
/* 1119 */       return code;
/*      */     }
/* 1121 */     Pointers pointers = new Pointers(this.currentPos, this.mark, where, etable, ca);
/* 1122 */     ArrayList jumps = makeJumpList(code, code.length, pointers);
/* 1123 */     byte[] r = insertGap2w(code, where, gapLength, exclusive, jumps, pointers);
/* 1124 */     this.currentPos = pointers.cursor;
/* 1125 */     this.mark = pointers.mark;
/* 1126 */     int where2 = pointers.mark0;
/* 1127 */     if (where2 == this.currentPos && !exclusive) {
/* 1128 */       this.currentPos += gapLength;
/*      */     }
/* 1130 */     if (exclusive) {
/* 1131 */       where2 -= gapLength;
/*      */     }
/* 1133 */     newWhere.position = where2;
/* 1134 */     newWhere.length = gapLength;
/* 1135 */     return r;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte[] insertGap2w(byte[] code, int where, int gapLength, boolean exclusive, ArrayList<Branch> jumps, Pointers ptrs) throws BadBytecode {
/* 1142 */     int n = jumps.size();
/* 1143 */     if (gapLength > 0) {
/* 1144 */       ptrs.shiftPc(where, gapLength, exclusive);
/* 1145 */       for (int i = 0; i < n; i++) {
/* 1146 */         ((Branch)jumps.get(i)).shift(where, gapLength, exclusive);
/*      */       }
/*      */     } 
/* 1149 */     boolean unstable = true;
/*      */     do {
/* 1151 */       while (unstable) {
/* 1152 */         unstable = false;
/* 1153 */         for (int j = 0; j < n; j++) {
/* 1154 */           Branch b = jumps.get(j);
/* 1155 */           if (b.expanded()) {
/* 1156 */             unstable = true;
/* 1157 */             int p = b.pos;
/* 1158 */             int delta = b.deltaSize();
/* 1159 */             ptrs.shiftPc(p, delta, false);
/* 1160 */             for (int k = 0; k < n; k++) {
/* 1161 */               ((Branch)jumps.get(k)).shift(p, delta, false);
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/* 1166 */       for (int i = 0; i < n; i++) {
/* 1167 */         Branch b = jumps.get(i);
/* 1168 */         int diff = b.gapChanged();
/* 1169 */         if (diff > 0) {
/* 1170 */           unstable = true;
/* 1171 */           int p = b.pos;
/* 1172 */           ptrs.shiftPc(p, diff, false);
/* 1173 */           for (int j = 0; j < n; j++)
/* 1174 */             ((Branch)jumps.get(j)).shift(p, diff, false); 
/*      */         } 
/*      */       } 
/* 1177 */     } while (unstable);
/*      */     
/* 1179 */     return makeExapndedCode(code, jumps, where, gapLength);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static ArrayList makeJumpList(byte[] code, int endPos, Pointers ptrs) throws BadBytecode {
/* 1185 */     ArrayList<Branch> jumps = new ArrayList();
/*      */     int i;
/* 1187 */     for (i = 0; i < endPos; i = nextPos) {
/* 1188 */       int nextPos = nextOpcode(code, i);
/* 1189 */       int inst = code[i] & 0xFF;
/*      */       
/* 1191 */       if ((153 <= inst && inst <= 168) || inst == 198 || inst == 199) {
/*      */         Branch b;
/*      */         
/* 1194 */         int offset = code[i + 1] << 8 | code[i + 2] & 0xFF;
/*      */         
/* 1196 */         if (inst == 167 || inst == 168) {
/* 1197 */           b = new Jump16(i, offset);
/*      */         } else {
/* 1199 */           b = new If16(i, offset);
/*      */         } 
/* 1201 */         jumps.add(b);
/*      */       }
/* 1203 */       else if (inst == 200 || inst == 201) {
/*      */         
/* 1205 */         int offset = ByteArray.read32bit(code, i + 1);
/* 1206 */         jumps.add(new Jump32(i, offset));
/*      */       }
/* 1208 */       else if (inst == 170) {
/* 1209 */         int i2 = (i & 0xFFFFFFFC) + 4;
/* 1210 */         int defaultbyte = ByteArray.read32bit(code, i2);
/* 1211 */         int lowbyte = ByteArray.read32bit(code, i2 + 4);
/* 1212 */         int highbyte = ByteArray.read32bit(code, i2 + 8);
/* 1213 */         int i0 = i2 + 12;
/* 1214 */         int size = highbyte - lowbyte + 1;
/* 1215 */         int[] offsets = new int[size];
/* 1216 */         for (int j = 0; j < size; j++) {
/* 1217 */           offsets[j] = ByteArray.read32bit(code, i0);
/* 1218 */           i0 += 4;
/*      */         } 
/*      */         
/* 1221 */         jumps.add(new Table(i, defaultbyte, lowbyte, highbyte, offsets, ptrs));
/*      */       }
/* 1223 */       else if (inst == 171) {
/* 1224 */         int i2 = (i & 0xFFFFFFFC) + 4;
/* 1225 */         int defaultbyte = ByteArray.read32bit(code, i2);
/* 1226 */         int npairs = ByteArray.read32bit(code, i2 + 4);
/* 1227 */         int i0 = i2 + 8;
/* 1228 */         int[] matches = new int[npairs];
/* 1229 */         int[] offsets = new int[npairs];
/* 1230 */         for (int j = 0; j < npairs; j++) {
/* 1231 */           matches[j] = ByteArray.read32bit(code, i0);
/* 1232 */           offsets[j] = ByteArray.read32bit(code, i0 + 4);
/* 1233 */           i0 += 8;
/*      */         } 
/*      */         
/* 1236 */         jumps.add(new Lookup(i, defaultbyte, matches, offsets, ptrs));
/*      */       } 
/*      */     } 
/*      */     
/* 1240 */     return jumps;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte[] makeExapndedCode(byte[] code, ArrayList<Branch> jumps, int where, int gapLength) throws BadBytecode {
/*      */     Branch b;
/* 1247 */     int bpos, n = jumps.size();
/* 1248 */     int size = code.length + gapLength;
/* 1249 */     for (int i = 0; i < n; i++) {
/* 1250 */       Branch branch = jumps.get(i);
/* 1251 */       size += branch.deltaSize();
/*      */     } 
/*      */     
/* 1254 */     byte[] newcode = new byte[size];
/* 1255 */     int src = 0, dest = 0, bindex = 0;
/* 1256 */     int len = code.length;
/*      */ 
/*      */     
/* 1259 */     if (0 < n) {
/* 1260 */       b = jumps.get(0);
/* 1261 */       bpos = b.orgPos;
/*      */     } else {
/*      */       
/* 1264 */       b = null;
/* 1265 */       bpos = len;
/*      */     } 
/*      */     
/* 1268 */     while (src < len) {
/* 1269 */       if (src == where) {
/* 1270 */         int pos2 = dest + gapLength;
/* 1271 */         while (dest < pos2) {
/* 1272 */           newcode[dest++] = 0;
/*      */         }
/*      */       } 
/* 1275 */       if (src != bpos) {
/* 1276 */         newcode[dest++] = code[src++]; continue;
/*      */       } 
/* 1278 */       int s = b.write(src, code, dest, newcode);
/* 1279 */       src += s;
/* 1280 */       dest += s + b.deltaSize();
/* 1281 */       if (++bindex < n) {
/* 1282 */         b = jumps.get(bindex);
/* 1283 */         bpos = b.orgPos;
/*      */         continue;
/*      */       } 
/* 1286 */       b = null;
/* 1287 */       bpos = len;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1292 */     return newcode;
/*      */   }
/*      */   static abstract class Branch { int pos;
/*      */     
/*      */     Branch(int p) {
/* 1297 */       this.pos = this.orgPos = p;
/*      */     } int orgPos; void shift(int where, int gapLength, boolean exclusive) {
/* 1299 */       if (where < this.pos || (where == this.pos && exclusive)) {
/* 1300 */         this.pos += gapLength;
/*      */       }
/*      */     }
/*      */     
/*      */     static int shiftOffset(int i, int offset, int where, int gapLength, boolean exclusive) {
/* 1305 */       int target = i + offset;
/* 1306 */       if (i < where) {
/* 1307 */         if (where < target || (exclusive && where == target)) {
/* 1308 */           offset += gapLength;
/*      */         }
/* 1310 */       } else if (i == where) {
/*      */ 
/*      */         
/* 1313 */         if (target < where && exclusive) {
/* 1314 */           offset -= gapLength;
/* 1315 */         } else if (where < target && !exclusive) {
/* 1316 */           offset += gapLength;
/*      */         }
/*      */       
/* 1319 */       } else if (target < where || (!exclusive && where == target)) {
/* 1320 */         offset -= gapLength;
/*      */       } 
/* 1322 */       return offset;
/*      */     }
/*      */     
/* 1325 */     boolean expanded() { return false; }
/* 1326 */     int gapChanged() { return 0; } int deltaSize() {
/* 1327 */       return 0;
/*      */     }
/*      */     
/*      */     abstract int write(int param1Int1, byte[] param1ArrayOfbyte1, int param1Int2, byte[] param1ArrayOfbyte2) throws BadBytecode; }
/*      */ 
/*      */   
/*      */   static class LdcW
/*      */     extends Branch {
/*      */     int index;
/*      */     boolean state;
/*      */     
/*      */     LdcW(int p, int i) {
/* 1339 */       super(p);
/* 1340 */       this.index = i;
/* 1341 */       this.state = true;
/*      */     }
/*      */     
/*      */     boolean expanded() {
/* 1345 */       if (this.state) {
/* 1346 */         this.state = false;
/* 1347 */         return true;
/*      */       } 
/*      */       
/* 1350 */       return false;
/*      */     }
/*      */     int deltaSize() {
/* 1353 */       return 1;
/*      */     }
/*      */     int write(int srcPos, byte[] code, int destPos, byte[] newcode) {
/* 1356 */       newcode[destPos] = 19;
/* 1357 */       ByteArray.write16bit(this.index, newcode, destPos + 1);
/* 1358 */       return 2;
/*      */     }
/*      */   }
/*      */   
/*      */   static abstract class Branch16 extends Branch {
/*      */     int offset;
/*      */     int state;
/*      */     static final int BIT16 = 0;
/*      */     static final int EXPAND = 1;
/*      */     static final int BIT32 = 2;
/*      */     
/*      */     Branch16(int p, int off) {
/* 1370 */       super(p);
/* 1371 */       this.offset = off;
/* 1372 */       this.state = 0;
/*      */     }
/*      */     
/*      */     void shift(int where, int gapLength, boolean exclusive) {
/* 1376 */       this.offset = shiftOffset(this.pos, this.offset, where, gapLength, exclusive);
/* 1377 */       super.shift(where, gapLength, exclusive);
/* 1378 */       if (this.state == 0 && (
/* 1379 */         this.offset < -32768 || 32767 < this.offset))
/* 1380 */         this.state = 1; 
/*      */     }
/*      */     
/*      */     boolean expanded() {
/* 1384 */       if (this.state == 1) {
/* 1385 */         this.state = 2;
/* 1386 */         return true;
/*      */       } 
/*      */       
/* 1389 */       return false;
/*      */     }
/*      */     abstract int deltaSize();
/*      */     
/*      */     abstract void write32(int param1Int1, byte[] param1ArrayOfbyte1, int param1Int2, byte[] param1ArrayOfbyte2);
/*      */     
/*      */     int write(int src, byte[] code, int dest, byte[] newcode) {
/* 1396 */       if (this.state == 2) {
/* 1397 */         write32(src, code, dest, newcode);
/*      */       } else {
/* 1399 */         newcode[dest] = code[src];
/* 1400 */         ByteArray.write16bit(this.offset, newcode, dest + 1);
/*      */       } 
/*      */       
/* 1403 */       return 3;
/*      */     }
/*      */   }
/*      */   
/*      */   static class Jump16
/*      */     extends Branch16 {
/*      */     Jump16(int p, int off) {
/* 1410 */       super(p, off);
/*      */     }
/*      */     
/*      */     int deltaSize() {
/* 1414 */       return (this.state == 2) ? 2 : 0;
/*      */     }
/*      */     
/*      */     void write32(int src, byte[] code, int dest, byte[] newcode) {
/* 1418 */       newcode[dest] = (byte)(((code[src] & 0xFF) == 167) ? 200 : 201);
/* 1419 */       ByteArray.write32bit(this.offset, newcode, dest + 1);
/*      */     }
/*      */   }
/*      */   
/*      */   static class If16
/*      */     extends Branch16 {
/*      */     If16(int p, int off) {
/* 1426 */       super(p, off);
/*      */     }
/*      */     
/*      */     int deltaSize() {
/* 1430 */       return (this.state == 2) ? 5 : 0;
/*      */     }
/*      */     
/*      */     void write32(int src, byte[] code, int dest, byte[] newcode) {
/* 1434 */       newcode[dest] = (byte)opcode(code[src] & 0xFF);
/* 1435 */       newcode[dest + 1] = 0;
/* 1436 */       newcode[dest + 2] = 8;
/* 1437 */       newcode[dest + 3] = -56;
/* 1438 */       ByteArray.write32bit(this.offset - 3, newcode, dest + 4);
/*      */     }
/*      */     
/*      */     int opcode(int op) {
/* 1442 */       if (op == 198)
/* 1443 */         return 199; 
/* 1444 */       if (op == 199) {
/* 1445 */         return 198;
/*      */       }
/* 1447 */       if ((op - 153 & 0x1) == 0) {
/* 1448 */         return op + 1;
/*      */       }
/* 1450 */       return op - 1;
/*      */     }
/*      */   }
/*      */   
/*      */   static class Jump32
/*      */     extends Branch {
/*      */     int offset;
/*      */     
/*      */     Jump32(int p, int off) {
/* 1459 */       super(p);
/* 1460 */       this.offset = off;
/*      */     }
/*      */     
/*      */     void shift(int where, int gapLength, boolean exclusive) {
/* 1464 */       this.offset = shiftOffset(this.pos, this.offset, where, gapLength, exclusive);
/* 1465 */       super.shift(where, gapLength, exclusive);
/*      */     }
/*      */     
/*      */     int write(int src, byte[] code, int dest, byte[] newcode) {
/* 1469 */       newcode[dest] = code[src];
/* 1470 */       ByteArray.write32bit(this.offset, newcode, dest + 1);
/* 1471 */       return 5;
/*      */     }
/*      */   }
/*      */   
/*      */   static abstract class Switcher extends Branch { int gap;
/*      */     int defaultByte;
/*      */     int[] offsets;
/*      */     CodeIterator.Pointers pointers;
/*      */     
/*      */     Switcher(int pos, int defaultByte, int[] offsets, CodeIterator.Pointers ptrs) {
/* 1481 */       super(pos);
/* 1482 */       this.gap = 3 - (pos & 0x3);
/* 1483 */       this.defaultByte = defaultByte;
/* 1484 */       this.offsets = offsets;
/* 1485 */       this.pointers = ptrs;
/*      */     }
/*      */     
/*      */     void shift(int where, int gapLength, boolean exclusive) {
/* 1489 */       int p = this.pos;
/* 1490 */       this.defaultByte = shiftOffset(p, this.defaultByte, where, gapLength, exclusive);
/* 1491 */       int num = this.offsets.length;
/* 1492 */       for (int i = 0; i < num; i++) {
/* 1493 */         this.offsets[i] = shiftOffset(p, this.offsets[i], where, gapLength, exclusive);
/*      */       }
/* 1495 */       super.shift(where, gapLength, exclusive);
/*      */     }
/*      */     
/*      */     int gapChanged() {
/* 1499 */       int newGap = 3 - (this.pos & 0x3);
/* 1500 */       if (newGap > this.gap) {
/* 1501 */         int diff = newGap - this.gap;
/* 1502 */         this.gap = newGap;
/* 1503 */         return diff;
/*      */       } 
/*      */       
/* 1506 */       return 0;
/*      */     }
/*      */     
/*      */     int deltaSize() {
/* 1510 */       return this.gap - 3 - (this.orgPos & 0x3);
/*      */     }
/*      */     
/*      */     int write(int src, byte[] code, int dest, byte[] newcode) throws BadBytecode {
/* 1514 */       int padding = 3 - (this.pos & 0x3);
/* 1515 */       int nops = this.gap - padding;
/* 1516 */       int bytecodeSize = 5 + 3 - (this.orgPos & 0x3) + tableSize();
/* 1517 */       if (nops > 0) {
/* 1518 */         adjustOffsets(bytecodeSize, nops);
/*      */       }
/* 1520 */       newcode[dest++] = code[src];
/* 1521 */       while (padding-- > 0) {
/* 1522 */         newcode[dest++] = 0;
/*      */       }
/* 1524 */       ByteArray.write32bit(this.defaultByte, newcode, dest);
/* 1525 */       int size = write2(dest + 4, newcode);
/* 1526 */       dest += size + 4;
/* 1527 */       while (nops-- > 0) {
/* 1528 */         newcode[dest++] = 0;
/*      */       }
/* 1530 */       return 5 + 3 - (this.orgPos & 0x3) + size;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract int write2(int param1Int, byte[] param1ArrayOfbyte);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract int tableSize();
/*      */ 
/*      */ 
/*      */     
/*      */     void adjustOffsets(int size, int nops) throws BadBytecode {
/* 1546 */       this.pointers.shiftForSwitch(this.pos + size, nops);
/* 1547 */       if (this.defaultByte == size) {
/* 1548 */         this.defaultByte -= nops;
/*      */       }
/* 1550 */       for (int i = 0; i < this.offsets.length; i++) {
/* 1551 */         if (this.offsets[i] == size)
/* 1552 */           this.offsets[i] = this.offsets[i] - nops; 
/*      */       } 
/*      */     } }
/*      */   
/*      */   static class Table extends Switcher { int low;
/*      */     int high;
/*      */     
/*      */     Table(int pos, int defaultByte, int low, int high, int[] offsets, CodeIterator.Pointers ptrs) {
/* 1560 */       super(pos, defaultByte, offsets, ptrs);
/* 1561 */       this.low = low;
/* 1562 */       this.high = high;
/*      */     }
/*      */     
/*      */     int write2(int dest, byte[] newcode) {
/* 1566 */       ByteArray.write32bit(this.low, newcode, dest);
/* 1567 */       ByteArray.write32bit(this.high, newcode, dest + 4);
/* 1568 */       int n = this.offsets.length;
/* 1569 */       dest += 8;
/* 1570 */       for (int i = 0; i < n; i++) {
/* 1571 */         ByteArray.write32bit(this.offsets[i], newcode, dest);
/* 1572 */         dest += 4;
/*      */       } 
/*      */       
/* 1575 */       return 8 + 4 * n;
/*      */     }
/*      */     int tableSize() {
/* 1578 */       return 8 + 4 * this.offsets.length;
/*      */     } }
/*      */   
/*      */   static class Lookup extends Switcher {
/*      */     int[] matches;
/*      */     
/*      */     Lookup(int pos, int defaultByte, int[] matches, int[] offsets, CodeIterator.Pointers ptrs) {
/* 1585 */       super(pos, defaultByte, offsets, ptrs);
/* 1586 */       this.matches = matches;
/*      */     }
/*      */     
/*      */     int write2(int dest, byte[] newcode) {
/* 1590 */       int n = this.matches.length;
/* 1591 */       ByteArray.write32bit(n, newcode, dest);
/* 1592 */       dest += 4;
/* 1593 */       for (int i = 0; i < n; i++) {
/* 1594 */         ByteArray.write32bit(this.matches[i], newcode, dest);
/* 1595 */         ByteArray.write32bit(this.offsets[i], newcode, dest + 4);
/* 1596 */         dest += 8;
/*      */       } 
/*      */       
/* 1599 */       return 4 + 8 * n;
/*      */     }
/*      */     int tableSize() {
/* 1602 */       return 4 + 8 * this.matches.length;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\CodeIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */