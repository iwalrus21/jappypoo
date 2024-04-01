/*     */ package javassist.bytecode;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CodeAttribute
/*     */   extends AttributeInfo
/*     */   implements Opcode
/*     */ {
/*     */   public static final String tag = "Code";
/*     */   private int maxStack;
/*     */   private int maxLocals;
/*     */   private ExceptionTable exceptions;
/*     */   private ArrayList attributes;
/*     */   
/*     */   public CodeAttribute(ConstPool cp, int stack, int locals, byte[] code, ExceptionTable etable) {
/*  62 */     super(cp, "Code");
/*  63 */     this.maxStack = stack;
/*  64 */     this.maxLocals = locals;
/*  65 */     this.info = code;
/*  66 */     this.exceptions = etable;
/*  67 */     this.attributes = new ArrayList();
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
/*     */   private CodeAttribute(ConstPool cp, CodeAttribute src, Map classnames) throws BadBytecode {
/*  82 */     super(cp, "Code");
/*     */     
/*  84 */     this.maxStack = src.getMaxStack();
/*  85 */     this.maxLocals = src.getMaxLocals();
/*  86 */     this.exceptions = src.getExceptionTable().copy(cp, classnames);
/*  87 */     this.attributes = new ArrayList();
/*  88 */     List<AttributeInfo> src_attr = src.getAttributes();
/*  89 */     int num = src_attr.size();
/*  90 */     for (int i = 0; i < num; i++) {
/*  91 */       AttributeInfo ai = src_attr.get(i);
/*  92 */       this.attributes.add(ai.copy(cp, classnames));
/*     */     } 
/*     */     
/*  95 */     this.info = src.copyCode(cp, classnames, this.exceptions, this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   CodeAttribute(ConstPool cp, int name_id, DataInputStream in) throws IOException {
/* 101 */     super(cp, name_id, (byte[])null);
/* 102 */     int attr_len = in.readInt();
/*     */     
/* 104 */     this.maxStack = in.readUnsignedShort();
/* 105 */     this.maxLocals = in.readUnsignedShort();
/*     */     
/* 107 */     int code_len = in.readInt();
/* 108 */     this.info = new byte[code_len];
/* 109 */     in.readFully(this.info);
/*     */     
/* 111 */     this.exceptions = new ExceptionTable(cp, in);
/*     */     
/* 113 */     this.attributes = new ArrayList();
/* 114 */     int num = in.readUnsignedShort();
/* 115 */     for (int i = 0; i < num; i++) {
/* 116 */       this.attributes.add(AttributeInfo.read(cp, in));
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
/*     */   public AttributeInfo copy(ConstPool newCp, Map classnames) throws RuntimeCopyException {
/*     */     try {
/* 137 */       return new CodeAttribute(newCp, this, classnames);
/*     */     }
/* 139 */     catch (BadBytecode e) {
/* 140 */       throw new RuntimeCopyException("bad bytecode. fatal?");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class RuntimeCopyException
/*     */     extends RuntimeException
/*     */   {
/*     */     public RuntimeCopyException(String s) {
/* 153 */       super(s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/* 164 */     return 18 + this.info.length + this.exceptions.size() * 8 + AttributeInfo.getLength(this.attributes);
/*     */   }
/*     */   
/*     */   void write(DataOutputStream out) throws IOException {
/* 168 */     out.writeShort(this.name);
/* 169 */     out.writeInt(length() - 6);
/* 170 */     out.writeShort(this.maxStack);
/* 171 */     out.writeShort(this.maxLocals);
/* 172 */     out.writeInt(this.info.length);
/* 173 */     out.write(this.info);
/* 174 */     this.exceptions.write(out);
/* 175 */     out.writeShort(this.attributes.size());
/* 176 */     AttributeInfo.writeAll(this.attributes, out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] get() {
/* 185 */     throw new UnsupportedOperationException("CodeAttribute.get()");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(byte[] newinfo) {
/* 194 */     throw new UnsupportedOperationException("CodeAttribute.set()");
/*     */   }
/*     */   
/*     */   void renameClass(String oldname, String newname) {
/* 198 */     AttributeInfo.renameClass(this.attributes, oldname, newname);
/*     */   }
/*     */   
/*     */   void renameClass(Map classnames) {
/* 202 */     AttributeInfo.renameClass(this.attributes, classnames);
/*     */   }
/*     */   
/*     */   void getRefClasses(Map classnames) {
/* 206 */     AttributeInfo.getRefClasses(this.attributes, classnames);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDeclaringClass() {
/* 214 */     ConstPool cp = getConstPool();
/* 215 */     return cp.getClassName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxStack() {
/* 222 */     return this.maxStack;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxStack(int value) {
/* 229 */     this.maxStack = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int computeMaxStack() throws BadBytecode {
/* 240 */     this.maxStack = (new CodeAnalyzer(this)).computeMaxStack();
/* 241 */     return this.maxStack;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxLocals() {
/* 248 */     return this.maxLocals;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxLocals(int value) {
/* 255 */     this.maxLocals = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCodeLength() {
/* 262 */     return this.info.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getCode() {
/* 269 */     return this.info;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void setCode(byte[] newinfo) {
/* 275 */     super.set(newinfo);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CodeIterator iterator() {
/* 281 */     return new CodeIterator(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ExceptionTable getExceptionTable() {
/* 287 */     return this.exceptions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List getAttributes() {
/* 297 */     return this.attributes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AttributeInfo getAttribute(String name) {
/* 307 */     return AttributeInfo.lookup(this.attributes, name);
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
/*     */   public void setAttribute(StackMapTable smt) {
/* 319 */     AttributeInfo.remove(this.attributes, "StackMapTable");
/* 320 */     if (smt != null) {
/* 321 */       this.attributes.add(smt);
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
/*     */   public void setAttribute(StackMap sm) {
/* 334 */     AttributeInfo.remove(this.attributes, "StackMap");
/* 335 */     if (sm != null) {
/* 336 */       this.attributes.add(sm);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private byte[] copyCode(ConstPool destCp, Map classnames, ExceptionTable etable, CodeAttribute destCa) throws BadBytecode {
/* 346 */     int len = getCodeLength();
/* 347 */     byte[] newCode = new byte[len];
/* 348 */     destCa.info = newCode;
/* 349 */     LdcEntry ldc = copyCode(this.info, 0, len, getConstPool(), newCode, destCp, classnames);
/*     */     
/* 351 */     return LdcEntry.doit(newCode, ldc, etable, destCa);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static LdcEntry copyCode(byte[] code, int beginPos, int endPos, ConstPool srcCp, byte[] newcode, ConstPool destCp, Map classnameMap) throws BadBytecode {
/* 360 */     LdcEntry ldcEntry = null;
/*     */     int i;
/* 362 */     for (i = beginPos; i < endPos; i = i2) {
/* 363 */       int index; LdcEntry ldc; int i2 = CodeIterator.nextOpcode(code, i);
/* 364 */       byte c = code[i];
/* 365 */       newcode[i] = c;
/* 366 */       switch (c & 0xFF) {
/*     */         case 19:
/*     */         case 20:
/*     */         case 178:
/*     */         case 179:
/*     */         case 180:
/*     */         case 181:
/*     */         case 182:
/*     */         case 183:
/*     */         case 184:
/*     */         case 187:
/*     */         case 189:
/*     */         case 192:
/*     */         case 193:
/* 380 */           copyConstPoolInfo(i + 1, code, srcCp, newcode, destCp, classnameMap);
/*     */           break;
/*     */         
/*     */         case 18:
/* 384 */           index = code[i + 1] & 0xFF;
/* 385 */           index = srcCp.copy(index, destCp, classnameMap);
/* 386 */           if (index < 256) {
/* 387 */             newcode[i + 1] = (byte)index; break;
/*     */           } 
/* 389 */           newcode[i] = 0;
/* 390 */           newcode[i + 1] = 0;
/* 391 */           ldc = new LdcEntry();
/* 392 */           ldc.where = i;
/* 393 */           ldc.index = index;
/* 394 */           ldc.next = ldcEntry;
/* 395 */           ldcEntry = ldc;
/*     */           break;
/*     */         
/*     */         case 185:
/* 399 */           copyConstPoolInfo(i + 1, code, srcCp, newcode, destCp, classnameMap);
/*     */           
/* 401 */           newcode[i + 3] = code[i + 3];
/* 402 */           newcode[i + 4] = code[i + 4];
/*     */           break;
/*     */         case 186:
/* 405 */           copyConstPoolInfo(i + 1, code, srcCp, newcode, destCp, classnameMap);
/*     */           
/* 407 */           newcode[i + 3] = 0;
/* 408 */           newcode[i + 4] = 0;
/*     */           break;
/*     */         case 197:
/* 411 */           copyConstPoolInfo(i + 1, code, srcCp, newcode, destCp, classnameMap);
/*     */           
/* 413 */           newcode[i + 3] = code[i + 3];
/*     */           break;
/*     */         default:
/* 416 */           while (++i < i2) {
/* 417 */             newcode[i] = code[i];
/*     */           }
/*     */           break;
/*     */       } 
/*     */     
/*     */     } 
/* 423 */     return ldcEntry;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void copyConstPoolInfo(int i, byte[] code, ConstPool srcCp, byte[] newcode, ConstPool destCp, Map classnameMap) {
/* 429 */     int index = (code[i] & 0xFF) << 8 | code[i + 1] & 0xFF;
/* 430 */     index = srcCp.copy(index, destCp, classnameMap);
/* 431 */     newcode[i] = (byte)(index >> 8);
/* 432 */     newcode[i + 1] = (byte)index;
/*     */   }
/*     */ 
/*     */   
/*     */   static class LdcEntry
/*     */   {
/*     */     LdcEntry next;
/*     */     
/*     */     int where;
/*     */     int index;
/*     */     
/*     */     static byte[] doit(byte[] code, LdcEntry ldc, ExceptionTable etable, CodeAttribute ca) throws BadBytecode {
/* 444 */       if (ldc != null) {
/* 445 */         code = CodeIterator.changeLdcToLdcW(code, etable, ca, ldc);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 460 */       return code;
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
/*     */   public void insertLocalVar(int where, int size) throws BadBytecode {
/* 481 */     CodeIterator ci = iterator();
/* 482 */     while (ci.hasNext()) {
/* 483 */       shiftIndex(ci, where, size);
/*     */     }
/* 485 */     setMaxLocals(getMaxLocals() + size);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void shiftIndex(CodeIterator ci, int lessThan, int delta) throws BadBytecode {
/* 496 */     int index = ci.next();
/* 497 */     int opcode = ci.byteAt(index);
/* 498 */     if (opcode < 21)
/*     */       return; 
/* 500 */     if (opcode < 79) {
/* 501 */       if (opcode < 26) {
/*     */         
/* 503 */         shiftIndex8(ci, index, opcode, lessThan, delta);
/*     */       }
/* 505 */       else if (opcode < 46) {
/*     */         
/* 507 */         shiftIndex0(ci, index, opcode, lessThan, delta, 26, 21);
/*     */       } else {
/* 509 */         if (opcode < 54)
/*     */           return; 
/* 511 */         if (opcode < 59) {
/*     */           
/* 513 */           shiftIndex8(ci, index, opcode, lessThan, delta);
/*     */         }
/*     */         else {
/*     */           
/* 517 */           shiftIndex0(ci, index, opcode, lessThan, delta, 59, 54);
/*     */         } 
/*     */       } 
/* 520 */     } else if (opcode == 132) {
/* 521 */       int var = ci.byteAt(index + 1);
/* 522 */       if (var < lessThan) {
/*     */         return;
/*     */       }
/* 525 */       var += delta;
/* 526 */       if (var < 256) {
/* 527 */         ci.writeByte(var, index + 1);
/*     */       } else {
/* 529 */         int plus = (byte)ci.byteAt(index + 2);
/* 530 */         int pos = ci.insertExGap(3);
/* 531 */         ci.writeByte(196, pos - 3);
/* 532 */         ci.writeByte(132, pos - 2);
/* 533 */         ci.write16bit(var, pos - 1);
/* 534 */         ci.write16bit(plus, pos + 1);
/*     */       }
/*     */     
/* 537 */     } else if (opcode == 169) {
/* 538 */       shiftIndex8(ci, index, opcode, lessThan, delta);
/* 539 */     } else if (opcode == 196) {
/* 540 */       int var = ci.u16bitAt(index + 2);
/* 541 */       if (var < lessThan) {
/*     */         return;
/*     */       }
/* 544 */       var += delta;
/* 545 */       ci.write16bit(var, index + 2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void shiftIndex8(CodeIterator ci, int index, int opcode, int lessThan, int delta) throws BadBytecode {
/* 553 */     int var = ci.byteAt(index + 1);
/* 554 */     if (var < lessThan) {
/*     */       return;
/*     */     }
/* 557 */     var += delta;
/* 558 */     if (var < 256) {
/* 559 */       ci.writeByte(var, index + 1);
/*     */     } else {
/* 561 */       int pos = ci.insertExGap(2);
/* 562 */       ci.writeByte(196, pos - 2);
/* 563 */       ci.writeByte(opcode, pos - 1);
/* 564 */       ci.write16bit(var, pos);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void shiftIndex0(CodeIterator ci, int index, int opcode, int lessThan, int delta, int opcode_i_0, int opcode_i) throws BadBytecode {
/* 573 */     int var = (opcode - opcode_i_0) % 4;
/* 574 */     if (var < lessThan) {
/*     */       return;
/*     */     }
/* 577 */     var += delta;
/* 578 */     if (var < 4) {
/* 579 */       ci.writeByte(opcode + delta, index);
/*     */     } else {
/* 581 */       opcode = (opcode - opcode_i_0) / 4 + opcode_i;
/* 582 */       if (var < 256) {
/* 583 */         int pos = ci.insertExGap(1);
/* 584 */         ci.writeByte(opcode, pos - 1);
/* 585 */         ci.writeByte(var, pos);
/*     */       } else {
/*     */         
/* 588 */         int pos = ci.insertExGap(3);
/* 589 */         ci.writeByte(196, pos - 1);
/* 590 */         ci.writeByte(opcode, pos);
/* 591 */         ci.write16bit(var, pos + 1);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\CodeAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */