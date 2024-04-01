/*     */ package javassist.bytecode;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
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
/*     */ public class StackMap
/*     */   extends AttributeInfo
/*     */ {
/*     */   public static final String tag = "StackMap";
/*     */   public static final int TOP = 0;
/*     */   public static final int INTEGER = 1;
/*     */   public static final int FLOAT = 2;
/*     */   public static final int DOUBLE = 3;
/*     */   public static final int LONG = 4;
/*     */   public static final int NULL = 5;
/*     */   public static final int THIS = 6;
/*     */   public static final int OBJECT = 7;
/*     */   public static final int UNINIT = 8;
/*     */   
/*     */   StackMap(ConstPool cp, byte[] newInfo) {
/*  55 */     super(cp, "StackMap", newInfo);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   StackMap(ConstPool cp, int name_id, DataInputStream in) throws IOException {
/*  61 */     super(cp, name_id, in);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int numOfEntries() {
/*  68 */     return ByteArray.readU16bit(this.info, 0);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AttributeInfo copy(ConstPool newCp, Map classnames) {
/* 120 */     Copier copier = new Copier(this, newCp, classnames);
/* 121 */     copier.visit();
/* 122 */     return copier.getStackMap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Walker
/*     */   {
/*     */     byte[] info;
/*     */ 
/*     */ 
/*     */     
/*     */     public Walker(StackMap sm) {
/* 135 */       this.info = sm.get();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void visit() {
/* 142 */       int num = ByteArray.readU16bit(this.info, 0);
/* 143 */       int pos = 2;
/* 144 */       for (int i = 0; i < num; i++) {
/* 145 */         int offset = ByteArray.readU16bit(this.info, pos);
/* 146 */         int numLoc = ByteArray.readU16bit(this.info, pos + 2);
/* 147 */         pos = locals(pos + 4, offset, numLoc);
/* 148 */         int numStack = ByteArray.readU16bit(this.info, pos);
/* 149 */         pos = stack(pos + 2, offset, numStack);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int locals(int pos, int offset, int num) {
/* 158 */       return typeInfoArray(pos, offset, num, true);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int stack(int pos, int offset, int num) {
/* 166 */       return typeInfoArray(pos, offset, num, false);
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
/*     */     public int typeInfoArray(int pos, int offset, int num, boolean isLocals) {
/* 178 */       for (int k = 0; k < num; k++) {
/* 179 */         pos = typeInfoArray2(k, pos);
/*     */       }
/* 181 */       return pos;
/*     */     }
/*     */     
/*     */     int typeInfoArray2(int k, int pos) {
/* 185 */       byte tag = this.info[pos];
/* 186 */       if (tag == 7) {
/* 187 */         int clazz = ByteArray.readU16bit(this.info, pos + 1);
/* 188 */         objectVariable(pos, clazz);
/* 189 */         pos += 3;
/*     */       }
/* 191 */       else if (tag == 8) {
/* 192 */         int offsetOfNew = ByteArray.readU16bit(this.info, pos + 1);
/* 193 */         uninitialized(pos, offsetOfNew);
/* 194 */         pos += 3;
/*     */       } else {
/*     */         
/* 197 */         typeInfo(pos, tag);
/* 198 */         pos++;
/*     */       } 
/*     */       
/* 201 */       return pos;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void typeInfo(int pos, byte tag) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void objectVariable(int pos, int clazz) {}
/*     */ 
/*     */     
/*     */     public void uninitialized(int pos, int offset) {}
/*     */   }
/*     */ 
/*     */   
/*     */   static class Copier
/*     */     extends Walker
/*     */   {
/*     */     byte[] dest;
/*     */     
/*     */     ConstPool srcCp;
/*     */     
/*     */     ConstPool destCp;
/*     */     
/*     */     Map classnames;
/*     */ 
/*     */     
/*     */     Copier(StackMap map, ConstPool newCp, Map classnames) {
/* 230 */       super(map);
/* 231 */       this.srcCp = map.getConstPool();
/* 232 */       this.dest = new byte[this.info.length];
/* 233 */       this.destCp = newCp;
/* 234 */       this.classnames = classnames;
/*     */     }
/*     */     
/*     */     public void visit() {
/* 238 */       int num = ByteArray.readU16bit(this.info, 0);
/* 239 */       ByteArray.write16bit(num, this.dest, 0);
/* 240 */       super.visit();
/*     */     }
/*     */     
/*     */     public int locals(int pos, int offset, int num) {
/* 244 */       ByteArray.write16bit(offset, this.dest, pos - 4);
/* 245 */       return super.locals(pos, offset, num);
/*     */     }
/*     */     
/*     */     public int typeInfoArray(int pos, int offset, int num, boolean isLocals) {
/* 249 */       ByteArray.write16bit(num, this.dest, pos - 2);
/* 250 */       return super.typeInfoArray(pos, offset, num, isLocals);
/*     */     }
/*     */     
/*     */     public void typeInfo(int pos, byte tag) {
/* 254 */       this.dest[pos] = tag;
/*     */     }
/*     */     
/*     */     public void objectVariable(int pos, int clazz) {
/* 258 */       this.dest[pos] = 7;
/* 259 */       int newClazz = this.srcCp.copy(clazz, this.destCp, this.classnames);
/* 260 */       ByteArray.write16bit(newClazz, this.dest, pos + 1);
/*     */     }
/*     */     
/*     */     public void uninitialized(int pos, int offset) {
/* 264 */       this.dest[pos] = 8;
/* 265 */       ByteArray.write16bit(offset, this.dest, pos + 1);
/*     */     }
/*     */     
/*     */     public StackMap getStackMap() {
/* 269 */       return new StackMap(this.destCp, this.dest);
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
/*     */   public void insertLocal(int index, int tag, int classInfo) throws BadBytecode {
/* 291 */     byte[] data = (new InsertLocal(this, index, tag, classInfo)).doit();
/* 292 */     set(data);
/*     */   }
/*     */   
/*     */   static class SimpleCopy extends Walker {
/*     */     StackMap.Writer writer;
/*     */     
/*     */     SimpleCopy(StackMap map) {
/* 299 */       super(map);
/* 300 */       this.writer = new StackMap.Writer();
/*     */     }
/*     */     
/*     */     byte[] doit() {
/* 304 */       visit();
/* 305 */       return this.writer.toByteArray();
/*     */     }
/*     */     
/*     */     public void visit() {
/* 309 */       int num = ByteArray.readU16bit(this.info, 0);
/* 310 */       this.writer.write16bit(num);
/* 311 */       super.visit();
/*     */     }
/*     */     
/*     */     public int locals(int pos, int offset, int num) {
/* 315 */       this.writer.write16bit(offset);
/* 316 */       return super.locals(pos, offset, num);
/*     */     }
/*     */     
/*     */     public int typeInfoArray(int pos, int offset, int num, boolean isLocals) {
/* 320 */       this.writer.write16bit(num);
/* 321 */       return super.typeInfoArray(pos, offset, num, isLocals);
/*     */     }
/*     */     
/*     */     public void typeInfo(int pos, byte tag) {
/* 325 */       this.writer.writeVerifyTypeInfo(tag, 0);
/*     */     }
/*     */     
/*     */     public void objectVariable(int pos, int clazz) {
/* 329 */       this.writer.writeVerifyTypeInfo(7, clazz);
/*     */     }
/*     */     
/*     */     public void uninitialized(int pos, int offset) {
/* 333 */       this.writer.writeVerifyTypeInfo(8, offset);
/*     */     } }
/*     */   
/*     */   static class InsertLocal extends SimpleCopy {
/*     */     private int varIndex;
/*     */     private int varTag;
/*     */     private int varData;
/*     */     
/*     */     InsertLocal(StackMap map, int varIndex, int varTag, int varData) {
/* 342 */       super(map);
/* 343 */       this.varIndex = varIndex;
/* 344 */       this.varTag = varTag;
/* 345 */       this.varData = varData;
/*     */     }
/*     */     
/*     */     public int typeInfoArray(int pos, int offset, int num, boolean isLocals) {
/* 349 */       if (!isLocals || num < this.varIndex) {
/* 350 */         return super.typeInfoArray(pos, offset, num, isLocals);
/*     */       }
/* 352 */       this.writer.write16bit(num + 1);
/* 353 */       for (int k = 0; k < num; k++) {
/* 354 */         if (k == this.varIndex) {
/* 355 */           writeVarTypeInfo();
/*     */         }
/* 357 */         pos = typeInfoArray2(k, pos);
/*     */       } 
/*     */       
/* 360 */       if (num == this.varIndex) {
/* 361 */         writeVarTypeInfo();
/*     */       }
/* 363 */       return pos;
/*     */     }
/*     */     
/*     */     private void writeVarTypeInfo() {
/* 367 */       if (this.varTag == 7) {
/* 368 */         this.writer.writeVerifyTypeInfo(7, this.varData);
/* 369 */       } else if (this.varTag == 8) {
/* 370 */         this.writer.writeVerifyTypeInfo(8, this.varData);
/*     */       } else {
/* 372 */         this.writer.writeVerifyTypeInfo(this.varTag, 0);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   void shiftPc(int where, int gapSize, boolean exclusive) throws BadBytecode {
/* 379 */     (new Shifter(this, where, gapSize, exclusive)).visit();
/*     */   }
/*     */   
/*     */   static class Shifter
/*     */     extends Walker {
/*     */     private int where;
/*     */     
/*     */     public Shifter(StackMap smt, int where, int gap, boolean exclusive) {
/* 387 */       super(smt);
/* 388 */       this.where = where;
/* 389 */       this.gap = gap;
/* 390 */       this.exclusive = exclusive;
/*     */     }
/*     */     private int gap; private boolean exclusive;
/*     */     public int locals(int pos, int offset, int num) {
/* 394 */       if (this.exclusive ? (this.where <= offset) : (this.where < offset)) {
/* 395 */         ByteArray.write16bit(offset + this.gap, this.info, pos - 4);
/*     */       }
/* 397 */       return super.locals(pos, offset, num);
/*     */     }
/*     */     
/*     */     public void uninitialized(int pos, int offset) {
/* 401 */       if (this.where <= offset) {
/* 402 */         ByteArray.write16bit(offset + this.gap, this.info, pos + 1);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void shiftForSwitch(int where, int gapSize) throws BadBytecode {
/* 410 */     (new SwitchShifter(this, where, gapSize)).visit();
/*     */   }
/*     */   
/*     */   static class SwitchShifter extends Walker { private int where;
/*     */     private int gap;
/*     */     
/*     */     public SwitchShifter(StackMap smt, int where, int gap) {
/* 417 */       super(smt);
/* 418 */       this.where = where;
/* 419 */       this.gap = gap;
/*     */     }
/*     */     
/*     */     public int locals(int pos, int offset, int num) {
/* 423 */       if (this.where == pos + offset) {
/* 424 */         ByteArray.write16bit(offset - this.gap, this.info, pos - 4);
/* 425 */       } else if (this.where == pos) {
/* 426 */         ByteArray.write16bit(offset + this.gap, this.info, pos - 4);
/*     */       } 
/* 428 */       return super.locals(pos, offset, num);
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeNew(int where) throws CannotCompileException {
/* 442 */     byte[] data = (new NewRemover(this, where)).doit();
/* 443 */     set(data);
/*     */   }
/*     */   
/*     */   static class NewRemover extends SimpleCopy {
/*     */     int posOfNew;
/*     */     
/*     */     NewRemover(StackMap map, int where) {
/* 450 */       super(map);
/* 451 */       this.posOfNew = where;
/*     */     }
/*     */     
/*     */     public int stack(int pos, int offset, int num) {
/* 455 */       return stackTypeInfoArray(pos, offset, num);
/*     */     }
/*     */     
/*     */     private int stackTypeInfoArray(int pos, int offset, int num) {
/* 459 */       int p = pos;
/* 460 */       int count = 0; int k;
/* 461 */       for (k = 0; k < num; k++) {
/* 462 */         byte tag = this.info[p];
/* 463 */         if (tag == 7) {
/* 464 */           p += 3;
/* 465 */         } else if (tag == 8) {
/* 466 */           int offsetOfNew = ByteArray.readU16bit(this.info, p + 1);
/* 467 */           if (offsetOfNew == this.posOfNew) {
/* 468 */             count++;
/*     */           }
/* 470 */           p += 3;
/*     */         } else {
/*     */           
/* 473 */           p++;
/*     */         } 
/*     */       } 
/* 476 */       this.writer.write16bit(num - count);
/* 477 */       for (k = 0; k < num; k++) {
/* 478 */         byte tag = this.info[pos];
/* 479 */         if (tag == 7) {
/* 480 */           int clazz = ByteArray.readU16bit(this.info, pos + 1);
/* 481 */           objectVariable(pos, clazz);
/* 482 */           pos += 3;
/*     */         }
/* 484 */         else if (tag == 8) {
/* 485 */           int offsetOfNew = ByteArray.readU16bit(this.info, pos + 1);
/* 486 */           if (offsetOfNew != this.posOfNew) {
/* 487 */             uninitialized(pos, offsetOfNew);
/*     */           }
/* 489 */           pos += 3;
/*     */         } else {
/*     */           
/* 492 */           typeInfo(pos, tag);
/* 493 */           pos++;
/*     */         } 
/*     */       } 
/*     */       
/* 497 */       return pos;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(PrintWriter out) {
/* 505 */     (new Printer(this, out)).print();
/*     */   }
/*     */   
/*     */   static class Printer extends Walker {
/*     */     private PrintWriter writer;
/*     */     
/*     */     public Printer(StackMap map, PrintWriter out) {
/* 512 */       super(map);
/* 513 */       this.writer = out;
/*     */     }
/*     */     
/*     */     public void print() {
/* 517 */       int num = ByteArray.readU16bit(this.info, 0);
/* 518 */       this.writer.println(num + " entries");
/* 519 */       visit();
/*     */     }
/*     */     
/*     */     public int locals(int pos, int offset, int num) {
/* 523 */       this.writer.println("  * offset " + offset);
/* 524 */       return super.locals(pos, offset, num);
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
/*     */   public static class Writer
/*     */   {
/* 540 */     private ByteArrayOutputStream output = new ByteArrayOutputStream();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public byte[] toByteArray() {
/* 547 */       return this.output.toByteArray();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public StackMap toStackMap(ConstPool cp) {
/* 554 */       return new StackMap(cp, this.output.toByteArray());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void writeVerifyTypeInfo(int tag, int data) {
/* 563 */       this.output.write(tag);
/* 564 */       if (tag == 7 || tag == 8) {
/* 565 */         write16bit(data);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void write16bit(int value) {
/* 572 */       this.output.write(value >>> 8 & 0xFF);
/* 573 */       this.output.write(value & 0xFF);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\StackMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */