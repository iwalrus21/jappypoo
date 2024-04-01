/*     */ package javassist.bytecode.stackmap;
/*     */ 
/*     */ import javassist.ClassPool;
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.ByteArray;
/*     */ import javassist.bytecode.ConstPool;
/*     */ import javassist.bytecode.Descriptor;
/*     */ import javassist.bytecode.Opcode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Tracer
/*     */   implements TypeTag
/*     */ {
/*     */   protected ClassPool classPool;
/*     */   protected ConstPool cpool;
/*     */   protected String returnType;
/*     */   protected int stackTop;
/*     */   protected TypeData[] stackTypes;
/*     */   protected TypeData[] localsTypes;
/*     */   
/*     */   public Tracer(ClassPool classes, ConstPool cp, int maxStack, int maxLocals, String retType) {
/*  42 */     this.classPool = classes;
/*  43 */     this.cpool = cp;
/*  44 */     this.returnType = retType;
/*  45 */     this.stackTop = 0;
/*  46 */     this.stackTypes = TypeData.make(maxStack);
/*  47 */     this.localsTypes = TypeData.make(maxLocals);
/*     */   }
/*     */   
/*     */   public Tracer(Tracer t) {
/*  51 */     this.classPool = t.classPool;
/*  52 */     this.cpool = t.cpool;
/*  53 */     this.returnType = t.returnType;
/*  54 */     this.stackTop = t.stackTop;
/*  55 */     this.stackTypes = TypeData.make(t.stackTypes.length);
/*  56 */     this.localsTypes = TypeData.make(t.localsTypes.length);
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
/*     */   protected int doOpcode(int pos, byte[] code) throws BadBytecode {
/*     */     try {
/*  71 */       int op = code[pos] & 0xFF;
/*  72 */       if (op < 96) {
/*  73 */         if (op < 54) {
/*  74 */           return doOpcode0_53(pos, code, op);
/*     */         }
/*  76 */         return doOpcode54_95(pos, code, op);
/*     */       } 
/*  78 */       if (op < 148) {
/*  79 */         return doOpcode96_147(pos, code, op);
/*     */       }
/*  81 */       return doOpcode148_201(pos, code, op);
/*     */     }
/*  83 */     catch (ArrayIndexOutOfBoundsException e) {
/*  84 */       throw new BadBytecode("inconsistent stack height " + e.getMessage(), e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void visitBranch(int pos, byte[] code, int offset) throws BadBytecode {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void visitGoto(int pos, byte[] code, int offset) throws BadBytecode {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void visitReturn(int pos, byte[] code) throws BadBytecode {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void visitThrow(int pos, byte[] code) throws BadBytecode {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void visitTableSwitch(int pos, byte[] code, int n, int offsetPos, int defaultOffset) throws BadBytecode {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void visitLookupSwitch(int pos, byte[] code, int n, int pairsPos, int defaultOffset) throws BadBytecode {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void visitJSR(int pos, byte[] code) throws BadBytecode {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void visitRET(int pos, byte[] code) throws BadBytecode {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int doOpcode0_53(int pos, byte[] code, int op) throws BadBytecode {
/*     */     int reg, s;
/* 134 */     TypeData data, stackTypes[] = this.stackTypes;
/* 135 */     switch (op)
/*     */     
/*     */     { 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 0:
/* 247 */         return 1;case 1: stackTypes[this.stackTop++] = new TypeData.NullType();case 2: case 3: case 4: case 5: case 6: case 7: case 8: stackTypes[this.stackTop++] = INTEGER;case 9: case 10: stackTypes[this.stackTop++] = LONG; stackTypes[this.stackTop++] = TOP;case 11: case 12: case 13: stackTypes[this.stackTop++] = FLOAT;case 14: case 15: stackTypes[this.stackTop++] = DOUBLE; stackTypes[this.stackTop++] = TOP;case 16: case 17: stackTypes[this.stackTop++] = INTEGER; return (op == 17) ? 3 : 2;case 18: doLDC(code[pos + 1] & 0xFF); return 2;case 19: case 20: doLDC(ByteArray.readU16bit(code, pos + 1)); return 3;case 21: return doXLOAD(INTEGER, code, pos);case 22: return doXLOAD(LONG, code, pos);case 23: return doXLOAD(FLOAT, code, pos);case 24: return doXLOAD(DOUBLE, code, pos);case 25: return doALOAD(code[pos + 1] & 0xFF);case 26: case 27: case 28: case 29: stackTypes[this.stackTop++] = INTEGER;case 30: case 31: case 32: case 33: stackTypes[this.stackTop++] = LONG; stackTypes[this.stackTop++] = TOP;case 34: case 35: case 36: case 37: stackTypes[this.stackTop++] = FLOAT;case 38: case 39: case 40: case 41: stackTypes[this.stackTop++] = DOUBLE; stackTypes[this.stackTop++] = TOP;case 42: case 43: case 44: case 45: reg = op - 42; stackTypes[this.stackTop++] = this.localsTypes[reg];case 46: stackTypes[--this.stackTop - 1] = INTEGER;case 47: stackTypes[this.stackTop - 2] = LONG; stackTypes[this.stackTop - 1] = TOP;
/*     */       case 48: stackTypes[--this.stackTop - 1] = FLOAT;
/*     */       case 49: stackTypes[this.stackTop - 2] = DOUBLE; stackTypes[this.stackTop - 1] = TOP;
/*     */       case 50: s = --this.stackTop - 1; data = stackTypes[s]; stackTypes[s] = TypeData.ArrayElement.make(data);
/* 251 */       case 51: case 52: case 53: stackTypes[--this.stackTop - 1] = INTEGER; }  throw new RuntimeException("fatal"); } private void doLDC(int index) { TypeData[] stackTypes = this.stackTypes;
/* 252 */     int tag = this.cpool.getTag(index);
/* 253 */     if (tag == 8) {
/* 254 */       stackTypes[this.stackTop++] = new TypeData.ClassName("java.lang.String");
/* 255 */     } else if (tag == 3) {
/* 256 */       stackTypes[this.stackTop++] = INTEGER;
/* 257 */     } else if (tag == 4) {
/* 258 */       stackTypes[this.stackTop++] = FLOAT;
/* 259 */     } else if (tag == 5) {
/* 260 */       stackTypes[this.stackTop++] = LONG;
/* 261 */       stackTypes[this.stackTop++] = TOP;
/*     */     }
/* 263 */     else if (tag == 6) {
/* 264 */       stackTypes[this.stackTop++] = DOUBLE;
/* 265 */       stackTypes[this.stackTop++] = TOP;
/*     */     }
/* 267 */     else if (tag == 7) {
/* 268 */       stackTypes[this.stackTop++] = new TypeData.ClassName("java.lang.Class");
/*     */     } else {
/* 270 */       throw new RuntimeException("bad LDC: " + tag);
/*     */     }  }
/*     */   
/*     */   private int doXLOAD(TypeData type, byte[] code, int pos) {
/* 274 */     int localVar = code[pos + 1] & 0xFF;
/* 275 */     return doXLOAD(localVar, type);
/*     */   }
/*     */   
/*     */   private int doXLOAD(int localVar, TypeData type) {
/* 279 */     this.stackTypes[this.stackTop++] = type;
/* 280 */     if (type.is2WordType()) {
/* 281 */       this.stackTypes[this.stackTop++] = TOP;
/*     */     }
/* 283 */     return 2;
/*     */   }
/*     */   
/*     */   private int doALOAD(int localVar) {
/* 287 */     this.stackTypes[this.stackTop++] = this.localsTypes[localVar];
/* 288 */     return 2; } private int doOpcode54_95(int pos, byte[] code, int op) throws BadBytecode { int var; int i; int len;
/*     */     int sp;
/*     */     int j;
/*     */     TypeData t;
/* 292 */     switch (op) {
/*     */       case 54:
/* 294 */         return doXSTORE(pos, code, INTEGER);
/*     */       case 55:
/* 296 */         return doXSTORE(pos, code, LONG);
/*     */       case 56:
/* 298 */         return doXSTORE(pos, code, FLOAT);
/*     */       case 57:
/* 300 */         return doXSTORE(pos, code, DOUBLE);
/*     */       case 58:
/* 302 */         return doASTORE(code[pos + 1] & 0xFF);
/*     */       case 59:
/*     */       case 60:
/*     */       case 61:
/*     */       case 62:
/* 307 */         var = op - 59;
/* 308 */         this.localsTypes[var] = INTEGER;
/* 309 */         this.stackTop--;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 403 */         return 1;case 63: case 64: case 65: case 66: var = op - 63; this.localsTypes[var] = LONG; this.localsTypes[var + 1] = TOP; this.stackTop -= 2; return 1;case 67: case 68: case 69: case 70: var = op - 67; this.localsTypes[var] = FLOAT; this.stackTop--; return 1;case 71: case 72: case 73: case 74: var = op - 71; this.localsTypes[var] = DOUBLE; this.localsTypes[var + 1] = TOP; this.stackTop -= 2; return 1;case 75: case 76: case 77: case 78: var = op - 75; doASTORE(var); return 1;case 79: case 80: case 81: case 82: this.stackTop -= (op == 80 || op == 82) ? 4 : 3; return 1;case 83: TypeData.ArrayElement.aastore(this.stackTypes[this.stackTop - 3], this.stackTypes[this.stackTop - 1], this.classPool); this.stackTop -= 3; return 1;case 84: case 85: case 86: this.stackTop -= 3; return 1;case 87: this.stackTop--; return 1;case 88: this.stackTop -= 2; return 1;case 89: i = this.stackTop; this.stackTypes[i] = this.stackTypes[i - 1]; this.stackTop = i + 1; return 1;case 90: case 91: len = op - 90 + 2; doDUP_XX(1, len); j = this.stackTop; this.stackTypes[j - len] = this.stackTypes[j]; this.stackTop = j + 1; return 1;case 92: doDUP_XX(2, 2); this.stackTop += 2; return 1;case 93: case 94: len = op - 93 + 3; doDUP_XX(2, len); j = this.stackTop; this.stackTypes[j - len] = this.stackTypes[j]; this.stackTypes[j - len + 1] = this.stackTypes[j + 1]; this.stackTop = j + 2; return 1;case 95: sp = this.stackTop - 1; t = this.stackTypes[sp]; this.stackTypes[sp] = this.stackTypes[sp - 1]; this.stackTypes[sp - 1] = t; return 1;
/*     */     } 
/*     */     throw new RuntimeException("fatal"); }
/*     */    private int doXSTORE(int pos, byte[] code, TypeData type) {
/* 407 */     int index = code[pos + 1] & 0xFF;
/* 408 */     return doXSTORE(index, type);
/*     */   }
/*     */   
/*     */   private int doXSTORE(int index, TypeData type) {
/* 412 */     this.stackTop--;
/* 413 */     this.localsTypes[index] = type;
/* 414 */     if (type.is2WordType()) {
/* 415 */       this.stackTop--;
/* 416 */       this.localsTypes[index + 1] = TOP;
/*     */     } 
/*     */     
/* 419 */     return 2;
/*     */   }
/*     */   
/*     */   private int doASTORE(int index) {
/* 423 */     this.stackTop--;
/*     */     
/* 425 */     this.localsTypes[index] = this.stackTypes[this.stackTop];
/* 426 */     return 2;
/*     */   }
/*     */   
/*     */   private void doDUP_XX(int delta, int len) {
/* 430 */     TypeData[] types = this.stackTypes;
/* 431 */     int sp = this.stackTop - 1;
/* 432 */     int end = sp - len;
/* 433 */     while (sp > end) {
/* 434 */       types[sp + delta] = types[sp];
/* 435 */       sp--;
/*     */     } 
/*     */   }
/*     */   
/*     */   private int doOpcode96_147(int pos, byte[] code, int op) {
/* 440 */     if (op <= 131) {
/* 441 */       this.stackTop += Opcode.STACK_GROW[op];
/* 442 */       return 1;
/*     */     } 
/*     */     
/* 445 */     switch (op) {
/*     */       
/*     */       case 132:
/* 448 */         return 3;
/*     */       case 133:
/* 450 */         this.stackTypes[this.stackTop - 1] = LONG;
/* 451 */         this.stackTypes[this.stackTop] = TOP;
/* 452 */         this.stackTop++;
/*     */       
/*     */       case 134:
/* 455 */         this.stackTypes[this.stackTop - 1] = FLOAT;
/*     */       
/*     */       case 135:
/* 458 */         this.stackTypes[this.stackTop - 1] = DOUBLE;
/* 459 */         this.stackTypes[this.stackTop] = TOP;
/* 460 */         this.stackTop++;
/*     */       
/*     */       case 136:
/* 463 */         this.stackTypes[--this.stackTop - 1] = INTEGER;
/*     */       
/*     */       case 137:
/* 466 */         this.stackTypes[--this.stackTop - 1] = FLOAT;
/*     */       
/*     */       case 138:
/* 469 */         this.stackTypes[this.stackTop - 2] = DOUBLE;
/*     */       
/*     */       case 139:
/* 472 */         this.stackTypes[this.stackTop - 1] = INTEGER;
/*     */       
/*     */       case 140:
/* 475 */         this.stackTypes[this.stackTop - 1] = LONG;
/* 476 */         this.stackTypes[this.stackTop] = TOP;
/* 477 */         this.stackTop++;
/*     */       
/*     */       case 141:
/* 480 */         this.stackTypes[this.stackTop - 1] = DOUBLE;
/* 481 */         this.stackTypes[this.stackTop] = TOP;
/* 482 */         this.stackTop++;
/*     */       
/*     */       case 142:
/* 485 */         this.stackTypes[--this.stackTop - 1] = INTEGER;
/*     */       
/*     */       case 143:
/* 488 */         this.stackTypes[this.stackTop - 2] = LONG;
/*     */       
/*     */       case 144:
/* 491 */         this.stackTypes[--this.stackTop - 1] = FLOAT;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 145:
/*     */       case 146:
/*     */       case 147:
/* 501 */         return 1;
/*     */     }  throw new RuntimeException("fatal"); } private int doOpcode148_201(int pos, byte[] code, int op) throws BadBytecode { int pos2; int i; int low; int n; String type;
/*     */     int high;
/*     */     int j;
/* 505 */     switch (op) {
/*     */       case 148:
/* 507 */         this.stackTypes[this.stackTop - 4] = INTEGER;
/* 508 */         this.stackTop -= 3;
/*     */         break;
/*     */       case 149:
/*     */       case 150:
/* 512 */         this.stackTypes[--this.stackTop - 1] = INTEGER;
/*     */         break;
/*     */       case 151:
/*     */       case 152:
/* 516 */         this.stackTypes[this.stackTop - 4] = INTEGER;
/* 517 */         this.stackTop -= 3;
/*     */         break;
/*     */       case 153:
/*     */       case 154:
/*     */       case 155:
/*     */       case 156:
/*     */       case 157:
/*     */       case 158:
/* 525 */         this.stackTop--;
/* 526 */         visitBranch(pos, code, ByteArray.readS16bit(code, pos + 1));
/* 527 */         return 3;
/*     */       case 159:
/*     */       case 160:
/*     */       case 161:
/*     */       case 162:
/*     */       case 163:
/*     */       case 164:
/*     */       case 165:
/*     */       case 166:
/* 536 */         this.stackTop -= 2;
/* 537 */         visitBranch(pos, code, ByteArray.readS16bit(code, pos + 1));
/* 538 */         return 3;
/*     */       case 167:
/* 540 */         visitGoto(pos, code, ByteArray.readS16bit(code, pos + 1));
/* 541 */         return 3;
/*     */       case 168:
/* 543 */         visitJSR(pos, code);
/* 544 */         return 3;
/*     */       case 169:
/* 546 */         visitRET(pos, code);
/* 547 */         return 2;
/*     */       case 170:
/* 549 */         this.stackTop--;
/* 550 */         pos2 = (pos & 0xFFFFFFFC) + 8;
/* 551 */         low = ByteArray.read32bit(code, pos2);
/* 552 */         high = ByteArray.read32bit(code, pos2 + 4);
/* 553 */         j = high - low + 1;
/* 554 */         visitTableSwitch(pos, code, j, pos2 + 8, ByteArray.read32bit(code, pos2 - 4));
/* 555 */         return j * 4 + 16 - (pos & 0x3);
/*     */       case 171:
/* 557 */         this.stackTop--;
/* 558 */         pos2 = (pos & 0xFFFFFFFC) + 8;
/* 559 */         n = ByteArray.read32bit(code, pos2);
/* 560 */         visitLookupSwitch(pos, code, n, pos2 + 4, ByteArray.read32bit(code, pos2 - 4));
/* 561 */         return n * 8 + 12 - (pos & 0x3);
/*     */       case 172:
/* 563 */         this.stackTop--;
/* 564 */         visitReturn(pos, code);
/*     */         break;
/*     */       case 173:
/* 567 */         this.stackTop -= 2;
/* 568 */         visitReturn(pos, code);
/*     */         break;
/*     */       case 174:
/* 571 */         this.stackTop--;
/* 572 */         visitReturn(pos, code);
/*     */         break;
/*     */       case 175:
/* 575 */         this.stackTop -= 2;
/* 576 */         visitReturn(pos, code);
/*     */         break;
/*     */       case 176:
/* 579 */         this.stackTypes[--this.stackTop].setType(this.returnType, this.classPool);
/* 580 */         visitReturn(pos, code);
/*     */         break;
/*     */       case 177:
/* 583 */         visitReturn(pos, code);
/*     */         break;
/*     */       case 178:
/* 586 */         return doGetField(pos, code, false);
/*     */       case 179:
/* 588 */         return doPutField(pos, code, false);
/*     */       case 180:
/* 590 */         return doGetField(pos, code, true);
/*     */       case 181:
/* 592 */         return doPutField(pos, code, true);
/*     */       case 182:
/*     */       case 183:
/* 595 */         return doInvokeMethod(pos, code, true);
/*     */       case 184:
/* 597 */         return doInvokeMethod(pos, code, false);
/*     */       case 185:
/* 599 */         return doInvokeIntfMethod(pos, code);
/*     */       case 186:
/* 601 */         return doInvokeDynamic(pos, code);
/*     */       case 187:
/* 603 */         i = ByteArray.readU16bit(code, pos + 1);
/* 604 */         this.stackTypes[this.stackTop++] = new TypeData.UninitData(pos, this.cpool
/* 605 */             .getClassInfo(i));
/* 606 */         return 3;
/*     */       case 188:
/* 608 */         return doNEWARRAY(pos, code);
/*     */       case 189:
/* 610 */         i = ByteArray.readU16bit(code, pos + 1);
/* 611 */         type = this.cpool.getClassInfo(i).replace('.', '/');
/* 612 */         if (type.charAt(0) == '[') {
/* 613 */           type = "[" + type;
/*     */         } else {
/* 615 */           type = "[L" + type + ";";
/*     */         } 
/* 617 */         this.stackTypes[this.stackTop - 1] = new TypeData.ClassName(type);
/*     */         
/* 619 */         return 3;
/*     */       case 190:
/* 621 */         this.stackTypes[this.stackTop - 1].setType("[Ljava.lang.Object;", this.classPool);
/* 622 */         this.stackTypes[this.stackTop - 1] = INTEGER;
/*     */         break;
/*     */       case 191:
/* 625 */         this.stackTypes[--this.stackTop].setType("java.lang.Throwable", this.classPool);
/* 626 */         visitThrow(pos, code);
/*     */         break;
/*     */       
/*     */       case 192:
/* 630 */         i = ByteArray.readU16bit(code, pos + 1);
/* 631 */         type = this.cpool.getClassInfo(i);
/* 632 */         if (type.charAt(0) == '[') {
/* 633 */           type = type.replace('.', '/');
/*     */         }
/* 635 */         this.stackTypes[this.stackTop - 1] = new TypeData.ClassName(type);
/* 636 */         return 3;
/*     */       
/*     */       case 193:
/* 639 */         this.stackTypes[this.stackTop - 1] = INTEGER;
/* 640 */         return 3;
/*     */       case 194:
/*     */       case 195:
/* 643 */         this.stackTop--;
/*     */         break;
/*     */       
/*     */       case 196:
/* 647 */         return doWIDE(pos, code);
/*     */       case 197:
/* 649 */         return doMultiANewArray(pos, code);
/*     */       case 198:
/*     */       case 199:
/* 652 */         this.stackTop--;
/* 653 */         visitBranch(pos, code, ByteArray.readS16bit(code, pos + 1));
/* 654 */         return 3;
/*     */       case 200:
/* 656 */         visitGoto(pos, code, ByteArray.read32bit(code, pos + 1));
/* 657 */         return 5;
/*     */       case 201:
/* 659 */         visitJSR(pos, code);
/* 660 */         return 5;
/*     */     } 
/* 662 */     return 1; }
/*     */ 
/*     */   
/*     */   private int doWIDE(int pos, byte[] code) throws BadBytecode {
/* 666 */     int index, op = code[pos + 1] & 0xFF;
/* 667 */     switch (op) {
/*     */       case 21:
/* 669 */         doWIDE_XLOAD(pos, code, INTEGER);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 710 */         return 4;case 22: doWIDE_XLOAD(pos, code, LONG); return 4;case 23: doWIDE_XLOAD(pos, code, FLOAT); return 4;case 24: doWIDE_XLOAD(pos, code, DOUBLE); return 4;case 25: index = ByteArray.readU16bit(code, pos + 2); doALOAD(index); return 4;case 54: doWIDE_STORE(pos, code, INTEGER); return 4;case 55: doWIDE_STORE(pos, code, LONG); return 4;case 56: doWIDE_STORE(pos, code, FLOAT); return 4;case 57: doWIDE_STORE(pos, code, DOUBLE); return 4;case 58: index = ByteArray.readU16bit(code, pos + 2); doASTORE(index); return 4;case 132: return 6;case 169: visitRET(pos, code); return 4;
/*     */     } 
/*     */     throw new RuntimeException("bad WIDE instruction: " + op);
/*     */   } private void doWIDE_XLOAD(int pos, byte[] code, TypeData type) {
/* 714 */     int index = ByteArray.readU16bit(code, pos + 2);
/* 715 */     doXLOAD(index, type);
/*     */   }
/*     */   
/*     */   private void doWIDE_STORE(int pos, byte[] code, TypeData type) {
/* 719 */     int index = ByteArray.readU16bit(code, pos + 2);
/* 720 */     doXSTORE(index, type);
/*     */   }
/*     */   
/*     */   private int doPutField(int pos, byte[] code, boolean notStatic) throws BadBytecode {
/* 724 */     int index = ByteArray.readU16bit(code, pos + 1);
/* 725 */     String desc = this.cpool.getFieldrefType(index);
/* 726 */     this.stackTop -= Descriptor.dataSize(desc);
/* 727 */     char c = desc.charAt(0);
/* 728 */     if (c == 'L') {
/* 729 */       this.stackTypes[this.stackTop].setType(getFieldClassName(desc, 0), this.classPool);
/* 730 */     } else if (c == '[') {
/* 731 */       this.stackTypes[this.stackTop].setType(desc, this.classPool);
/*     */     } 
/* 733 */     setFieldTarget(notStatic, index);
/* 734 */     return 3;
/*     */   }
/*     */   
/*     */   private int doGetField(int pos, byte[] code, boolean notStatic) throws BadBytecode {
/* 738 */     int index = ByteArray.readU16bit(code, pos + 1);
/* 739 */     setFieldTarget(notStatic, index);
/* 740 */     String desc = this.cpool.getFieldrefType(index);
/* 741 */     pushMemberType(desc);
/* 742 */     return 3;
/*     */   }
/*     */   
/*     */   private void setFieldTarget(boolean notStatic, int index) throws BadBytecode {
/* 746 */     if (notStatic) {
/* 747 */       String className = this.cpool.getFieldrefClassName(index);
/* 748 */       this.stackTypes[--this.stackTop].setType(className, this.classPool);
/*     */     } 
/*     */   }
/*     */   private int doNEWARRAY(int pos, byte[] code) {
/*     */     String type;
/* 753 */     int s = this.stackTop - 1;
/*     */     
/* 755 */     switch (code[pos + 1] & 0xFF) {
/*     */       case 4:
/* 757 */         type = "[Z";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 784 */         this.stackTypes[s] = new TypeData.ClassName(type);
/* 785 */         return 2;case 5: type = "[C"; this.stackTypes[s] = new TypeData.ClassName(type); return 2;case 6: type = "[F"; this.stackTypes[s] = new TypeData.ClassName(type); return 2;case 7: type = "[D"; this.stackTypes[s] = new TypeData.ClassName(type); return 2;case 8: type = "[B"; this.stackTypes[s] = new TypeData.ClassName(type); return 2;case 9: type = "[S"; this.stackTypes[s] = new TypeData.ClassName(type); return 2;case 10: type = "[I"; this.stackTypes[s] = new TypeData.ClassName(type); return 2;case 11: type = "[J"; this.stackTypes[s] = new TypeData.ClassName(type); return 2;
/*     */     } 
/*     */     throw new RuntimeException("bad newarray");
/*     */   } private int doMultiANewArray(int pos, byte[] code) {
/* 789 */     int i = ByteArray.readU16bit(code, pos + 1);
/* 790 */     int dim = code[pos + 3] & 0xFF;
/* 791 */     this.stackTop -= dim - 1;
/*     */     
/* 793 */     String type = this.cpool.getClassInfo(i).replace('.', '/');
/* 794 */     this.stackTypes[this.stackTop - 1] = new TypeData.ClassName(type);
/* 795 */     return 4;
/*     */   }
/*     */   
/*     */   private int doInvokeMethod(int pos, byte[] code, boolean notStatic) throws BadBytecode {
/* 799 */     int i = ByteArray.readU16bit(code, pos + 1);
/* 800 */     String desc = this.cpool.getMethodrefType(i);
/* 801 */     checkParamTypes(desc, 1);
/* 802 */     if (notStatic) {
/* 803 */       String className = this.cpool.getMethodrefClassName(i);
/* 804 */       TypeData target = this.stackTypes[--this.stackTop];
/* 805 */       if (target instanceof TypeData.UninitTypeVar && target.isUninit()) {
/* 806 */         constructorCalled(target, ((TypeData.UninitTypeVar)target).offset());
/* 807 */       } else if (target instanceof TypeData.UninitData) {
/* 808 */         constructorCalled(target, ((TypeData.UninitData)target).offset());
/*     */       } 
/* 810 */       target.setType(className, this.classPool);
/*     */     } 
/*     */     
/* 813 */     pushMemberType(desc);
/* 814 */     return 3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void constructorCalled(TypeData target, int offset) {
/* 823 */     target.constructorCalled(offset); int i;
/* 824 */     for (i = 0; i < this.stackTop; i++) {
/* 825 */       this.stackTypes[i].constructorCalled(offset);
/*     */     }
/* 827 */     for (i = 0; i < this.localsTypes.length; i++)
/* 828 */       this.localsTypes[i].constructorCalled(offset); 
/*     */   }
/*     */   
/*     */   private int doInvokeIntfMethod(int pos, byte[] code) throws BadBytecode {
/* 832 */     int i = ByteArray.readU16bit(code, pos + 1);
/* 833 */     String desc = this.cpool.getInterfaceMethodrefType(i);
/* 834 */     checkParamTypes(desc, 1);
/* 835 */     String className = this.cpool.getInterfaceMethodrefClassName(i);
/* 836 */     this.stackTypes[--this.stackTop].setType(className, this.classPool);
/* 837 */     pushMemberType(desc);
/* 838 */     return 5;
/*     */   }
/*     */   
/*     */   private int doInvokeDynamic(int pos, byte[] code) throws BadBytecode {
/* 842 */     int i = ByteArray.readU16bit(code, pos + 1);
/* 843 */     String desc = this.cpool.getInvokeDynamicType(i);
/* 844 */     checkParamTypes(desc, 1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 852 */     pushMemberType(desc);
/* 853 */     return 5;
/*     */   }
/*     */   
/*     */   private void pushMemberType(String descriptor) {
/* 857 */     int top = 0;
/* 858 */     if (descriptor.charAt(0) == '(') {
/* 859 */       top = descriptor.indexOf(')') + 1;
/* 860 */       if (top < 1) {
/* 861 */         throw new IndexOutOfBoundsException("bad descriptor: " + descriptor);
/*     */       }
/*     */     } 
/*     */     
/* 865 */     TypeData[] types = this.stackTypes;
/* 866 */     int index = this.stackTop;
/* 867 */     switch (descriptor.charAt(top)) {
/*     */       case '[':
/* 869 */         types[index] = new TypeData.ClassName(descriptor.substring(top));
/*     */         break;
/*     */       case 'L':
/* 872 */         types[index] = new TypeData.ClassName(getFieldClassName(descriptor, top));
/*     */         break;
/*     */       case 'J':
/* 875 */         types[index] = LONG;
/* 876 */         types[index + 1] = TOP;
/* 877 */         this.stackTop += 2;
/*     */         return;
/*     */       case 'F':
/* 880 */         types[index] = FLOAT;
/*     */         break;
/*     */       case 'D':
/* 883 */         types[index] = DOUBLE;
/* 884 */         types[index + 1] = TOP;
/* 885 */         this.stackTop += 2;
/*     */         return;
/*     */       case 'V':
/*     */         return;
/*     */       default:
/* 890 */         types[index] = INTEGER;
/*     */         break;
/*     */     } 
/*     */     
/* 894 */     this.stackTop++;
/*     */   }
/*     */   
/*     */   private static String getFieldClassName(String desc, int index) {
/* 898 */     return desc.substring(index + 1, desc.length() - 1).replace('/', '.');
/*     */   }
/*     */   
/*     */   private void checkParamTypes(String desc, int i) throws BadBytecode {
/* 902 */     char c = desc.charAt(i);
/* 903 */     if (c == ')') {
/*     */       return;
/*     */     }
/* 906 */     int k = i;
/* 907 */     boolean array = false;
/* 908 */     while (c == '[') {
/* 909 */       array = true;
/* 910 */       c = desc.charAt(++k);
/*     */     } 
/*     */     
/* 913 */     if (c == 'L') {
/* 914 */       k = desc.indexOf(';', k) + 1;
/* 915 */       if (k <= 0) {
/* 916 */         throw new IndexOutOfBoundsException("bad descriptor");
/*     */       }
/*     */     } else {
/* 919 */       k++;
/*     */     } 
/* 921 */     checkParamTypes(desc, k);
/* 922 */     if (!array && (c == 'J' || c == 'D')) {
/* 923 */       this.stackTop -= 2;
/*     */     } else {
/* 925 */       this.stackTop--;
/*     */     } 
/* 927 */     if (array) {
/* 928 */       this.stackTypes[this.stackTop].setType(desc.substring(i, k), this.classPool);
/* 929 */     } else if (c == 'L') {
/* 930 */       this.stackTypes[this.stackTop].setType(desc.substring(i + 1, k - 1).replace('/', '.'), this.classPool);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\stackmap\Tracer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */