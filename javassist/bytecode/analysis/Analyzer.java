/*     */ package javassist.bytecode.analysis;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtClass;
/*     */ import javassist.CtMethod;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.CodeAttribute;
/*     */ import javassist.bytecode.CodeIterator;
/*     */ import javassist.bytecode.ConstPool;
/*     */ import javassist.bytecode.Descriptor;
/*     */ import javassist.bytecode.ExceptionTable;
/*     */ import javassist.bytecode.MethodInfo;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Analyzer
/*     */   implements Opcode
/*     */ {
/*  87 */   private final SubroutineScanner scanner = new SubroutineScanner();
/*     */   private CtClass clazz;
/*     */   private ExceptionInfo[] exceptions;
/*     */   private Frame[] frames;
/*     */   private Subroutine[] subroutines;
/*     */   
/*     */   private static class ExceptionInfo {
/*     */     private int end;
/*     */     private int handler;
/*     */     private int start;
/*     */     private Type type;
/*     */     
/*     */     private ExceptionInfo(int start, int end, int handler, Type type) {
/* 100 */       this.start = start;
/* 101 */       this.end = end;
/* 102 */       this.handler = handler;
/* 103 */       this.type = type;
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
/*     */   public Frame[] analyze(CtClass clazz, MethodInfo method) throws BadBytecode {
/* 123 */     this.clazz = clazz;
/* 124 */     CodeAttribute codeAttribute = method.getCodeAttribute();
/*     */     
/* 126 */     if (codeAttribute == null) {
/* 127 */       return null;
/*     */     }
/* 129 */     int maxLocals = codeAttribute.getMaxLocals();
/* 130 */     int maxStack = codeAttribute.getMaxStack();
/* 131 */     int codeLength = codeAttribute.getCodeLength();
/*     */     
/* 133 */     CodeIterator iter = codeAttribute.iterator();
/* 134 */     IntQueue queue = new IntQueue();
/*     */     
/* 136 */     this.exceptions = buildExceptionInfo(method);
/* 137 */     this.subroutines = this.scanner.scan(method);
/*     */     
/* 139 */     Executor executor = new Executor(clazz.getClassPool(), method.getConstPool());
/* 140 */     this.frames = new Frame[codeLength];
/* 141 */     this.frames[iter.lookAhead()] = firstFrame(method, maxLocals, maxStack);
/* 142 */     queue.add(iter.next());
/* 143 */     while (!queue.isEmpty()) {
/* 144 */       analyzeNextEntry(method, iter, queue, executor);
/*     */     }
/*     */     
/* 147 */     return this.frames;
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
/*     */   public Frame[] analyze(CtMethod method) throws BadBytecode {
/* 165 */     return analyze(method.getDeclaringClass(), method.getMethodInfo2());
/*     */   }
/*     */ 
/*     */   
/*     */   private void analyzeNextEntry(MethodInfo method, CodeIterator iter, IntQueue queue, Executor executor) throws BadBytecode {
/* 170 */     int pos = queue.take();
/* 171 */     iter.move(pos);
/* 172 */     iter.next();
/*     */     
/* 174 */     Frame frame = this.frames[pos].copy();
/* 175 */     Subroutine subroutine = this.subroutines[pos];
/*     */     
/*     */     try {
/* 178 */       executor.execute(method, pos, iter, frame, subroutine);
/* 179 */     } catch (RuntimeException e) {
/* 180 */       throw new BadBytecode(e.getMessage() + "[pos = " + pos + "]", e);
/*     */     } 
/*     */     
/* 183 */     int opcode = iter.byteAt(pos);
/*     */     
/* 185 */     if (opcode == 170) {
/* 186 */       mergeTableSwitch(queue, pos, iter, frame);
/* 187 */     } else if (opcode == 171) {
/* 188 */       mergeLookupSwitch(queue, pos, iter, frame);
/* 189 */     } else if (opcode == 169) {
/* 190 */       mergeRet(queue, iter, pos, frame, subroutine);
/* 191 */     } else if (Util.isJumpInstruction(opcode)) {
/* 192 */       int target = Util.getJumpTarget(pos, iter);
/*     */       
/* 194 */       if (Util.isJsr(opcode)) {
/*     */         
/* 196 */         mergeJsr(queue, this.frames[pos], this.subroutines[target], pos, lookAhead(iter, pos));
/* 197 */       } else if (!Util.isGoto(opcode)) {
/* 198 */         merge(queue, frame, lookAhead(iter, pos));
/*     */       } 
/*     */       
/* 201 */       merge(queue, frame, target);
/* 202 */     } else if (opcode != 191 && !Util.isReturn(opcode)) {
/*     */       
/* 204 */       merge(queue, frame, lookAhead(iter, pos));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 210 */     mergeExceptionHandlers(queue, method, pos, frame);
/*     */   }
/*     */   
/*     */   private ExceptionInfo[] buildExceptionInfo(MethodInfo method) {
/* 214 */     ConstPool constPool = method.getConstPool();
/* 215 */     ClassPool classes = this.clazz.getClassPool();
/*     */     
/* 217 */     ExceptionTable table = method.getCodeAttribute().getExceptionTable();
/* 218 */     ExceptionInfo[] exceptions = new ExceptionInfo[table.size()];
/* 219 */     for (int i = 0; i < table.size(); i++) {
/* 220 */       Type type; int index = table.catchType(i);
/*     */       
/*     */       try {
/* 223 */         type = (index == 0) ? Type.THROWABLE : Type.get(classes.get(constPool.getClassInfo(index)));
/* 224 */       } catch (NotFoundException e) {
/* 225 */         throw new IllegalStateException(e.getMessage());
/*     */       } 
/*     */       
/* 228 */       exceptions[i] = new ExceptionInfo(table.startPc(i), table.endPc(i), table.handlerPc(i), type);
/*     */     } 
/*     */     
/* 231 */     return exceptions;
/*     */   }
/*     */   private Frame firstFrame(MethodInfo method, int maxLocals, int maxStack) {
/*     */     CtClass[] parameters;
/* 235 */     int pos = 0;
/*     */     
/* 237 */     Frame first = new Frame(maxLocals, maxStack);
/* 238 */     if ((method.getAccessFlags() & 0x8) == 0) {
/* 239 */       first.setLocal(pos++, Type.get(this.clazz));
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 244 */       parameters = Descriptor.getParameterTypes(method.getDescriptor(), this.clazz.getClassPool());
/* 245 */     } catch (NotFoundException e) {
/* 246 */       throw new RuntimeException(e);
/*     */     } 
/*     */     
/* 249 */     for (int i = 0; i < parameters.length; i++) {
/* 250 */       Type type = zeroExtend(Type.get(parameters[i]));
/* 251 */       first.setLocal(pos++, type);
/* 252 */       if (type.getSize() == 2) {
/* 253 */         first.setLocal(pos++, Type.TOP);
/*     */       }
/*     */     } 
/* 256 */     return first;
/*     */   }
/*     */   
/*     */   private int getNext(CodeIterator iter, int of, int restore) throws BadBytecode {
/* 260 */     iter.move(of);
/* 261 */     iter.next();
/* 262 */     int next = iter.lookAhead();
/* 263 */     iter.move(restore);
/* 264 */     iter.next();
/*     */     
/* 266 */     return next;
/*     */   }
/*     */   
/*     */   private int lookAhead(CodeIterator iter, int pos) throws BadBytecode {
/* 270 */     if (!iter.hasNext()) {
/* 271 */       throw new BadBytecode("Execution falls off end! [pos = " + pos + "]");
/*     */     }
/* 273 */     return iter.lookAhead();
/*     */   }
/*     */   
/*     */   private void merge(IntQueue queue, Frame frame, int target) {
/*     */     boolean changed;
/* 278 */     Frame old = this.frames[target];
/*     */ 
/*     */     
/* 281 */     if (old == null) {
/* 282 */       this.frames[target] = frame.copy();
/* 283 */       changed = true;
/*     */     } else {
/* 285 */       changed = old.merge(frame);
/*     */     } 
/*     */     
/* 288 */     if (changed) {
/* 289 */       queue.add(target);
/*     */     }
/*     */   }
/*     */   
/*     */   private void mergeExceptionHandlers(IntQueue queue, MethodInfo method, int pos, Frame frame) {
/* 294 */     for (int i = 0; i < this.exceptions.length; i++) {
/* 295 */       ExceptionInfo exception = this.exceptions[i];
/*     */ 
/*     */       
/* 298 */       if (pos >= exception.start && pos < exception.end) {
/* 299 */         Frame newFrame = frame.copy();
/* 300 */         newFrame.clearStack();
/* 301 */         newFrame.push(exception.type);
/* 302 */         merge(queue, newFrame, exception.handler);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void mergeJsr(IntQueue queue, Frame frame, Subroutine sub, int pos, int next) throws BadBytecode {
/* 308 */     if (sub == null) {
/* 309 */       throw new BadBytecode("No subroutine at jsr target! [pos = " + pos + "]");
/*     */     }
/* 311 */     Frame old = this.frames[next];
/* 312 */     boolean changed = false;
/*     */     
/* 314 */     if (old == null) {
/* 315 */       old = this.frames[next] = frame.copy();
/* 316 */       changed = true;
/*     */     } else {
/* 318 */       for (int i = 0; i < frame.localsLength(); i++) {
/*     */         
/* 320 */         if (!sub.isAccessed(i)) {
/* 321 */           Type oldType = old.getLocal(i);
/* 322 */           Type newType = frame.getLocal(i);
/* 323 */           if (oldType == null) {
/* 324 */             old.setLocal(i, newType);
/* 325 */             changed = true;
/*     */           }
/*     */           else {
/*     */             
/* 329 */             newType = oldType.merge(newType);
/*     */             
/* 331 */             old.setLocal(i, newType);
/* 332 */             if (!newType.equals(oldType) || newType.popChanged())
/* 333 */               changed = true; 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 338 */     if (!old.isJsrMerged()) {
/* 339 */       old.setJsrMerged(true);
/* 340 */       changed = true;
/*     */     } 
/*     */     
/* 343 */     if (changed && old.isRetMerged()) {
/* 344 */       queue.add(next);
/*     */     }
/*     */   }
/*     */   
/*     */   private void mergeLookupSwitch(IntQueue queue, int pos, CodeIterator iter, Frame frame) throws BadBytecode {
/* 349 */     int index = (pos & 0xFFFFFFFC) + 4;
/*     */     
/* 351 */     merge(queue, frame, pos + iter.s32bitAt(index));
/* 352 */     index += 4; int npairs = iter.s32bitAt(index);
/* 353 */     index += 4; int end = npairs * 8 + index;
/*     */ 
/*     */     
/* 356 */     for (index += 4; index < end; index += 8) {
/* 357 */       int target = iter.s32bitAt(index) + pos;
/* 358 */       merge(queue, frame, target);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void mergeRet(IntQueue queue, CodeIterator iter, int pos, Frame frame, Subroutine subroutine) throws BadBytecode {
/* 363 */     if (subroutine == null) {
/* 364 */       throw new BadBytecode("Ret on no subroutine! [pos = " + pos + "]");
/*     */     }
/* 366 */     Iterator<Integer> callerIter = subroutine.callers().iterator();
/* 367 */     while (callerIter.hasNext()) {
/* 368 */       int caller = ((Integer)callerIter.next()).intValue();
/* 369 */       int returnLoc = getNext(iter, caller, pos);
/* 370 */       boolean changed = false;
/*     */       
/* 372 */       Frame old = this.frames[returnLoc];
/* 373 */       if (old == null) {
/* 374 */         old = this.frames[returnLoc] = frame.copyStack();
/* 375 */         changed = true;
/*     */       } else {
/* 377 */         changed = old.mergeStack(frame);
/*     */       } 
/*     */       
/* 380 */       for (Iterator<Integer> i = subroutine.accessed().iterator(); i.hasNext(); ) {
/* 381 */         int index = ((Integer)i.next()).intValue();
/* 382 */         Type oldType = old.getLocal(index);
/* 383 */         Type newType = frame.getLocal(index);
/* 384 */         if (oldType != newType) {
/* 385 */           old.setLocal(index, newType);
/* 386 */           changed = true;
/*     */         } 
/*     */       } 
/*     */       
/* 390 */       if (!old.isRetMerged()) {
/* 391 */         old.setRetMerged(true);
/* 392 */         changed = true;
/*     */       } 
/*     */       
/* 395 */       if (changed && old.isJsrMerged()) {
/* 396 */         queue.add(returnLoc);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void mergeTableSwitch(IntQueue queue, int pos, CodeIterator iter, Frame frame) throws BadBytecode {
/* 403 */     int index = (pos & 0xFFFFFFFC) + 4;
/*     */     
/* 405 */     merge(queue, frame, pos + iter.s32bitAt(index));
/* 406 */     index += 4; int low = iter.s32bitAt(index);
/* 407 */     index += 4; int high = iter.s32bitAt(index);
/* 408 */     index += 4; int end = (high - low + 1) * 4 + index;
/*     */ 
/*     */     
/* 411 */     for (; index < end; index += 4) {
/* 412 */       int target = iter.s32bitAt(index) + pos;
/* 413 */       merge(queue, frame, target);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Type zeroExtend(Type type) {
/* 418 */     if (type == Type.SHORT || type == Type.BYTE || type == Type.CHAR || type == Type.BOOLEAN) {
/* 419 */       return Type.INTEGER;
/*     */     }
/* 421 */     return type;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\analysis\Analyzer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */