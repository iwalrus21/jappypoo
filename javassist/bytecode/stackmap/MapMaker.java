/*     */ package javassist.bytecode.stackmap;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import javassist.ClassPool;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.ByteArray;
/*     */ import javassist.bytecode.CodeAttribute;
/*     */ import javassist.bytecode.ConstPool;
/*     */ import javassist.bytecode.MethodInfo;
/*     */ import javassist.bytecode.StackMap;
/*     */ import javassist.bytecode.StackMapTable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MapMaker
/*     */   extends Tracer
/*     */ {
/*     */   public static StackMapTable make(ClassPool classes, MethodInfo minfo) throws BadBytecode {
/*     */     TypedBlock[] blocks;
/*  91 */     CodeAttribute ca = minfo.getCodeAttribute();
/*  92 */     if (ca == null) {
/*  93 */       return null;
/*     */     }
/*     */     
/*     */     try {
/*  97 */       blocks = TypedBlock.makeBlocks(minfo, ca, true);
/*     */     }
/*  99 */     catch (JsrBytecode e) {
/* 100 */       return null;
/*     */     } 
/*     */     
/* 103 */     if (blocks == null) {
/* 104 */       return null;
/*     */     }
/* 106 */     MapMaker mm = new MapMaker(classes, minfo, ca);
/*     */     try {
/* 108 */       mm.make(blocks, ca.getCode());
/*     */     }
/* 110 */     catch (BadBytecode bb) {
/* 111 */       throw new BadBytecode(minfo, bb);
/*     */     } 
/*     */     
/* 114 */     return mm.toStackMap(blocks);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StackMap make2(ClassPool classes, MethodInfo minfo) throws BadBytecode {
/*     */     TypedBlock[] blocks;
/* 125 */     CodeAttribute ca = minfo.getCodeAttribute();
/* 126 */     if (ca == null) {
/* 127 */       return null;
/*     */     }
/*     */     
/*     */     try {
/* 131 */       blocks = TypedBlock.makeBlocks(minfo, ca, true);
/*     */     }
/* 133 */     catch (JsrBytecode e) {
/* 134 */       return null;
/*     */     } 
/*     */     
/* 137 */     if (blocks == null) {
/* 138 */       return null;
/*     */     }
/* 140 */     MapMaker mm = new MapMaker(classes, minfo, ca);
/*     */     try {
/* 142 */       mm.make(blocks, ca.getCode());
/*     */     }
/* 144 */     catch (BadBytecode bb) {
/* 145 */       throw new BadBytecode(minfo, bb);
/*     */     } 
/* 147 */     return mm.toStackMap2(minfo.getConstPool(), blocks);
/*     */   }
/*     */   
/*     */   public MapMaker(ClassPool classes, MethodInfo minfo, CodeAttribute ca) {
/* 151 */     super(classes, minfo.getConstPool(), ca
/* 152 */         .getMaxStack(), ca.getMaxLocals(), 
/* 153 */         TypedBlock.getRetType(minfo.getDescriptor()));
/*     */   }
/*     */   protected MapMaker(MapMaker old) {
/* 156 */     super(old);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void make(TypedBlock[] blocks, byte[] code) throws BadBytecode {
/* 164 */     make(code, blocks[0]);
/* 165 */     findDeadCatchers(code, blocks);
/*     */     try {
/* 167 */       fixTypes(code, blocks);
/* 168 */     } catch (NotFoundException e) {
/* 169 */       throw new BadBytecode("failed to resolve types", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void make(byte[] code, TypedBlock tb) throws BadBytecode {
/* 178 */     copyTypeData(tb.stackTop, tb.stackTypes, this.stackTypes);
/* 179 */     this.stackTop = tb.stackTop;
/* 180 */     copyTypeData(tb.localsTypes.length, tb.localsTypes, this.localsTypes);
/*     */     
/* 182 */     traceException(code, tb.toCatch);
/*     */     
/* 184 */     int pos = tb.position;
/* 185 */     int end = pos + tb.length;
/* 186 */     while (pos < end) {
/* 187 */       pos += doOpcode(pos, code);
/* 188 */       traceException(code, tb.toCatch);
/*     */     } 
/*     */     
/* 191 */     if (tb.exit != null) {
/* 192 */       for (int i = 0; i < tb.exit.length; i++) {
/* 193 */         TypedBlock e = (TypedBlock)tb.exit[i];
/* 194 */         if (e.alreadySet()) {
/* 195 */           mergeMap(e, true);
/*     */         } else {
/* 197 */           recordStackMap(e);
/* 198 */           MapMaker maker = new MapMaker(this);
/* 199 */           maker.make(code, e);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void traceException(byte[] code, BasicBlock.Catch handler) throws BadBytecode {
/* 208 */     while (handler != null) {
/* 209 */       TypedBlock tb = (TypedBlock)handler.body;
/* 210 */       if (tb.alreadySet()) {
/* 211 */         mergeMap(tb, false);
/* 212 */         if (tb.stackTop < 1) {
/* 213 */           throw new BadBytecode("bad catch clause: " + handler.typeIndex);
/*     */         }
/* 215 */         tb.stackTypes[0] = merge(toExceptionType(handler.typeIndex), tb.stackTypes[0]);
/*     */       }
/*     */       else {
/*     */         
/* 219 */         recordStackMap(tb, handler.typeIndex);
/* 220 */         MapMaker maker = new MapMaker(this);
/* 221 */         maker.make(code, tb);
/*     */       } 
/*     */       
/* 224 */       handler = handler.next;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void mergeMap(TypedBlock dest, boolean mergeStack) throws BadBytecode {
/* 229 */     int n = this.localsTypes.length; int i;
/* 230 */     for (i = 0; i < n; i++) {
/* 231 */       dest.localsTypes[i] = merge(validateTypeData(this.localsTypes, n, i), dest.localsTypes[i]);
/*     */     }
/*     */     
/* 234 */     if (mergeStack) {
/* 235 */       n = this.stackTop;
/* 236 */       for (i = 0; i < n; i++)
/* 237 */         dest.stackTypes[i] = merge(this.stackTypes[i], dest.stackTypes[i]); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private TypeData merge(TypeData src, TypeData target) throws BadBytecode {
/* 242 */     if (src == target)
/* 243 */       return target; 
/* 244 */     if (target instanceof TypeData.ClassName || target instanceof TypeData.BasicType)
/*     */     {
/* 246 */       return target; } 
/* 247 */     if (target instanceof TypeData.AbsTypeVar) {
/* 248 */       ((TypeData.AbsTypeVar)target).merge(src);
/* 249 */       return target;
/*     */     } 
/*     */     
/* 252 */     throw new RuntimeException("fatal: this should never happen");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void recordStackMap(TypedBlock target) throws BadBytecode {
/* 258 */     TypeData[] tStackTypes = TypeData.make(this.stackTypes.length);
/* 259 */     int st = this.stackTop;
/* 260 */     recordTypeData(st, this.stackTypes, tStackTypes);
/* 261 */     recordStackMap0(target, st, tStackTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void recordStackMap(TypedBlock target, int exceptionType) throws BadBytecode {
/* 267 */     TypeData[] tStackTypes = TypeData.make(this.stackTypes.length);
/* 268 */     tStackTypes[0] = toExceptionType(exceptionType).join();
/* 269 */     recordStackMap0(target, 1, tStackTypes);
/*     */   }
/*     */   
/*     */   private TypeData.ClassName toExceptionType(int exceptionType) {
/*     */     String type;
/* 274 */     if (exceptionType == 0) {
/* 275 */       type = "java.lang.Throwable";
/*     */     } else {
/* 277 */       type = this.cpool.getClassInfo(exceptionType);
/*     */     } 
/* 279 */     return new TypeData.ClassName(type);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void recordStackMap0(TypedBlock target, int st, TypeData[] tStackTypes) throws BadBytecode {
/* 285 */     int n = this.localsTypes.length;
/* 286 */     TypeData[] tLocalsTypes = TypeData.make(n);
/* 287 */     int k = recordTypeData(n, this.localsTypes, tLocalsTypes);
/* 288 */     target.setStackMap(st, tStackTypes, k, tLocalsTypes);
/*     */   }
/*     */   
/*     */   protected static int recordTypeData(int n, TypeData[] srcTypes, TypeData[] destTypes) {
/* 292 */     int k = -1;
/* 293 */     for (int i = 0; i < n; i++) {
/* 294 */       TypeData t = validateTypeData(srcTypes, n, i);
/* 295 */       destTypes[i] = t.join();
/* 296 */       if (t != TOP) {
/* 297 */         k = i + 1;
/*     */       }
/*     */     } 
/* 300 */     return k + 1;
/*     */   }
/*     */   
/*     */   protected static void copyTypeData(int n, TypeData[] srcTypes, TypeData[] destTypes) {
/* 304 */     for (int i = 0; i < n; i++)
/* 305 */       destTypes[i] = srcTypes[i]; 
/*     */   }
/*     */   
/*     */   private static TypeData validateTypeData(TypeData[] data, int length, int index) {
/* 309 */     TypeData td = data[index];
/* 310 */     if (td.is2WordType() && index + 1 < length && 
/* 311 */       data[index + 1] != TOP) {
/* 312 */       return TOP;
/*     */     }
/* 314 */     return td;
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
/*     */   private void findDeadCatchers(byte[] code, TypedBlock[] blocks) throws BadBytecode {
/* 326 */     int len = blocks.length;
/* 327 */     for (int i = 0; i < len; i++) {
/* 328 */       TypedBlock block = blocks[i];
/* 329 */       if (!block.alreadySet()) {
/* 330 */         fixDeadcode(code, block);
/* 331 */         BasicBlock.Catch handler = block.toCatch;
/* 332 */         if (handler != null) {
/* 333 */           TypedBlock tb = (TypedBlock)handler.body;
/* 334 */           if (!tb.alreadySet()) {
/*     */ 
/*     */             
/* 337 */             recordStackMap(tb, handler.typeIndex);
/* 338 */             fixDeadcode(code, tb);
/* 339 */             tb.incoming = 1;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void fixDeadcode(byte[] code, TypedBlock block) throws BadBytecode {
/* 348 */     int pos = block.position;
/* 349 */     int len = block.length - 3;
/* 350 */     if (len < 0) {
/*     */       
/* 352 */       if (len == -1) {
/* 353 */         code[pos] = 0;
/*     */       }
/* 355 */       code[pos + block.length - 1] = -65;
/* 356 */       block.incoming = 1;
/* 357 */       recordStackMap(block, 0);
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 363 */     block.incoming = 0;
/*     */     
/* 365 */     for (int k = 0; k < len; k++) {
/* 366 */       code[pos + k] = 0;
/*     */     }
/* 368 */     code[pos + len] = -89;
/* 369 */     ByteArray.write16bit(-len, code, pos + len + 1);
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
/*     */   private void fixTypes(byte[] code, TypedBlock[] blocks) throws NotFoundException, BadBytecode {
/* 382 */     ArrayList preOrder = new ArrayList();
/* 383 */     int len = blocks.length;
/* 384 */     int index = 0;
/* 385 */     for (int i = 0; i < len; i++) {
/* 386 */       TypedBlock block = blocks[i];
/* 387 */       if (block.alreadySet()) {
/* 388 */         int n = block.localsTypes.length; int j;
/* 389 */         for (j = 0; j < n; j++) {
/* 390 */           index = block.localsTypes[j].dfs(preOrder, index, this.classPool);
/*     */         }
/* 392 */         n = block.stackTop;
/* 393 */         for (j = 0; j < n; j++) {
/* 394 */           index = block.stackTypes[j].dfs(preOrder, index, this.classPool);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public StackMapTable toStackMap(TypedBlock[] blocks) {
/* 402 */     StackMapTable.Writer writer = new StackMapTable.Writer(32);
/* 403 */     int n = blocks.length;
/* 404 */     TypedBlock prev = blocks[0];
/* 405 */     int offsetDelta = prev.length;
/* 406 */     if (prev.incoming > 0) {
/* 407 */       writer.sameFrame(0);
/* 408 */       offsetDelta--;
/*     */     } 
/*     */     
/* 411 */     for (int i = 1; i < n; i++) {
/* 412 */       TypedBlock bb = blocks[i];
/* 413 */       if (isTarget(bb, blocks[i - 1])) {
/* 414 */         bb.resetNumLocals();
/* 415 */         int diffL = stackMapDiff(prev.numLocals, prev.localsTypes, bb.numLocals, bb.localsTypes);
/*     */         
/* 417 */         toStackMapBody(writer, bb, diffL, offsetDelta, prev);
/* 418 */         offsetDelta = bb.length - 1;
/* 419 */         prev = bb;
/*     */       }
/* 421 */       else if (bb.incoming == 0) {
/*     */         
/* 423 */         writer.sameFrame(offsetDelta);
/* 424 */         offsetDelta = bb.length - 1;
/* 425 */         prev = bb;
/*     */       } else {
/*     */         
/* 428 */         offsetDelta += bb.length;
/*     */       } 
/*     */     } 
/* 431 */     return writer.toStackMapTable(this.cpool);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isTarget(TypedBlock cur, TypedBlock prev) {
/* 438 */     int in = cur.incoming;
/* 439 */     if (in > 1)
/* 440 */       return true; 
/* 441 */     if (in < 1) {
/* 442 */       return false;
/*     */     }
/* 444 */     return prev.stop;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void toStackMapBody(StackMapTable.Writer writer, TypedBlock bb, int diffL, int offsetDelta, TypedBlock prev) {
/* 452 */     int stackTop = bb.stackTop;
/* 453 */     if (stackTop == 0) {
/* 454 */       if (diffL == 0) {
/* 455 */         writer.sameFrame(offsetDelta);
/*     */         return;
/*     */       } 
/* 458 */       if (0 > diffL && diffL >= -3) {
/* 459 */         writer.chopFrame(offsetDelta, -diffL);
/*     */         return;
/*     */       } 
/* 462 */       if (0 < diffL && diffL <= 3) {
/* 463 */         int[] data = new int[diffL];
/* 464 */         int[] tags = fillStackMap(bb.numLocals - prev.numLocals, prev.numLocals, data, bb.localsTypes);
/*     */ 
/*     */         
/* 467 */         writer.appendFrame(offsetDelta, tags, data);
/*     */         return;
/*     */       } 
/*     */     } else {
/* 471 */       if (stackTop == 1 && diffL == 0) {
/* 472 */         TypeData td = bb.stackTypes[0];
/* 473 */         writer.sameLocals(offsetDelta, td.getTypeTag(), td.getTypeData(this.cpool));
/*     */         return;
/*     */       } 
/* 476 */       if (stackTop == 2 && diffL == 0) {
/* 477 */         TypeData td = bb.stackTypes[0];
/* 478 */         if (td.is2WordType()) {
/*     */           
/* 480 */           writer.sameLocals(offsetDelta, td.getTypeTag(), td.getTypeData(this.cpool));
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/* 485 */     int[] sdata = new int[stackTop];
/* 486 */     int[] stags = fillStackMap(stackTop, 0, sdata, bb.stackTypes);
/* 487 */     int[] ldata = new int[bb.numLocals];
/* 488 */     int[] ltags = fillStackMap(bb.numLocals, 0, ldata, bb.localsTypes);
/* 489 */     writer.fullFrame(offsetDelta, ltags, ldata, stags, sdata);
/*     */   }
/*     */   
/*     */   private int[] fillStackMap(int num, int offset, int[] data, TypeData[] types) {
/* 493 */     int realNum = diffSize(types, offset, offset + num);
/* 494 */     ConstPool cp = this.cpool;
/* 495 */     int[] tags = new int[realNum];
/* 496 */     int j = 0;
/* 497 */     for (int i = 0; i < num; i++) {
/* 498 */       TypeData td = types[offset + i];
/* 499 */       tags[j] = td.getTypeTag();
/* 500 */       data[j] = td.getTypeData(cp);
/* 501 */       if (td.is2WordType()) {
/* 502 */         i++;
/*     */       }
/* 504 */       j++;
/*     */     } 
/*     */     
/* 507 */     return tags;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int stackMapDiff(int oldTdLen, TypeData[] oldTd, int newTdLen, TypeData[] newTd) {
/* 513 */     int len, diff = newTdLen - oldTdLen;
/*     */     
/* 515 */     if (diff > 0) {
/* 516 */       len = oldTdLen;
/*     */     } else {
/* 518 */       len = newTdLen;
/*     */     } 
/* 520 */     if (stackMapEq(oldTd, newTd, len)) {
/* 521 */       if (diff > 0) {
/* 522 */         return diffSize(newTd, len, newTdLen);
/*     */       }
/* 524 */       return -diffSize(oldTd, len, oldTdLen);
/*     */     } 
/* 526 */     return -100;
/*     */   }
/*     */   
/*     */   private static boolean stackMapEq(TypeData[] oldTd, TypeData[] newTd, int len) {
/* 530 */     for (int i = 0; i < len; i++) {
/* 531 */       if (!oldTd[i].eq(newTd[i])) {
/* 532 */         return false;
/*     */       }
/*     */     } 
/* 535 */     return true;
/*     */   }
/*     */   
/*     */   private static int diffSize(TypeData[] types, int offset, int len) {
/* 539 */     int num = 0;
/* 540 */     while (offset < len) {
/* 541 */       TypeData td = types[offset++];
/* 542 */       num++;
/* 543 */       if (td.is2WordType()) {
/* 544 */         offset++;
/*     */       }
/*     */     } 
/* 547 */     return num;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public StackMap toStackMap2(ConstPool cp, TypedBlock[] blocks) {
/* 553 */     StackMap.Writer writer = new StackMap.Writer();
/* 554 */     int n = blocks.length;
/* 555 */     boolean[] effective = new boolean[n];
/* 556 */     TypedBlock prev = blocks[0];
/*     */ 
/*     */     
/* 559 */     effective[0] = (prev.incoming > 0);
/*     */     
/* 561 */     int num = effective[0] ? 1 : 0; int i;
/* 562 */     for (i = 1; i < n; i++) {
/* 563 */       TypedBlock bb = blocks[i];
/* 564 */       effective[i] = isTarget(bb, blocks[i - 1]); if (isTarget(bb, blocks[i - 1])) {
/* 565 */         bb.resetNumLocals();
/* 566 */         prev = bb;
/* 567 */         num++;
/*     */       } 
/*     */     } 
/*     */     
/* 571 */     if (num == 0) {
/* 572 */       return null;
/*     */     }
/* 574 */     writer.write16bit(num);
/* 575 */     for (i = 0; i < n; i++) {
/* 576 */       if (effective[i])
/* 577 */         writeStackFrame(writer, cp, (blocks[i]).position, blocks[i]); 
/*     */     } 
/* 579 */     return writer.toStackMap(cp);
/*     */   }
/*     */   
/*     */   private void writeStackFrame(StackMap.Writer writer, ConstPool cp, int offset, TypedBlock tb) {
/* 583 */     writer.write16bit(offset);
/* 584 */     writeVerifyTypeInfo(writer, cp, tb.localsTypes, tb.numLocals);
/* 585 */     writeVerifyTypeInfo(writer, cp, tb.stackTypes, tb.stackTop);
/*     */   }
/*     */   
/*     */   private void writeVerifyTypeInfo(StackMap.Writer writer, ConstPool cp, TypeData[] types, int num) {
/* 589 */     int numDWord = 0; int i;
/* 590 */     for (i = 0; i < num; i++) {
/* 591 */       TypeData td = types[i];
/* 592 */       if (td != null && td.is2WordType()) {
/* 593 */         numDWord++;
/* 594 */         i++;
/*     */       } 
/*     */     } 
/*     */     
/* 598 */     writer.write16bit(num - numDWord);
/* 599 */     for (i = 0; i < num; i++) {
/* 600 */       TypeData td = types[i];
/* 601 */       writer.writeVerifyTypeInfo(td.getTypeTag(), td.getTypeData(cp));
/* 602 */       if (td.is2WordType())
/* 603 */         i++; 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\stackmap\MapMaker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */