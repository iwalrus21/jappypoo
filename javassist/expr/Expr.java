/*     */ package javassist.expr;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtBehavior;
/*     */ import javassist.CtClass;
/*     */ import javassist.CtConstructor;
/*     */ import javassist.CtPrimitiveType;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.Bytecode;
/*     */ import javassist.bytecode.ClassFile;
/*     */ import javassist.bytecode.CodeAttribute;
/*     */ import javassist.bytecode.CodeIterator;
/*     */ import javassist.bytecode.ConstPool;
/*     */ import javassist.bytecode.ExceptionTable;
/*     */ import javassist.bytecode.ExceptionsAttribute;
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
/*     */ public abstract class Expr
/*     */   implements Opcode
/*     */ {
/*     */   int currentPos;
/*     */   CodeIterator iterator;
/*     */   CtClass thisClass;
/*     */   MethodInfo thisMethod;
/*     */   boolean edited;
/*     */   int maxLocals;
/*     */   int maxStack;
/*     */   static final String javaLangObject = "java.lang.Object";
/*     */   
/*     */   protected Expr(int pos, CodeIterator i, CtClass declaring, MethodInfo m) {
/*  59 */     this.currentPos = pos;
/*  60 */     this.iterator = i;
/*  61 */     this.thisClass = declaring;
/*  62 */     this.thisMethod = m;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtClass getEnclosingClass() {
/*  71 */     return this.thisClass;
/*     */   }
/*     */   protected final ConstPool getConstPool() {
/*  74 */     return this.thisMethod.getConstPool();
/*     */   }
/*     */   
/*     */   protected final boolean edited() {
/*  78 */     return this.edited;
/*     */   }
/*     */   
/*     */   protected final int locals() {
/*  82 */     return this.maxLocals;
/*     */   }
/*     */   
/*     */   protected final int stack() {
/*  86 */     return this.maxStack;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final boolean withinStatic() {
/*  93 */     return ((this.thisMethod.getAccessFlags() & 0x8) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtBehavior where() {
/* 100 */     MethodInfo mi = this.thisMethod;
/* 101 */     CtBehavior[] cb = this.thisClass.getDeclaredBehaviors();
/* 102 */     for (int i = cb.length - 1; i >= 0; i--) {
/* 103 */       if (cb[i].getMethodInfo2() == mi)
/* 104 */         return cb[i]; 
/*     */     } 
/* 106 */     CtConstructor init = this.thisClass.getClassInitializer();
/* 107 */     if (init != null && init.getMethodInfo2() == mi) {
/* 108 */       return (CtBehavior)init;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 115 */     for (int j = cb.length - 1; j >= 0; j--) {
/* 116 */       if (this.thisMethod.getName().equals(cb[j].getMethodInfo2().getName()) && this.thisMethod
/* 117 */         .getDescriptor()
/* 118 */         .equals(cb[j].getMethodInfo2().getDescriptor())) {
/* 119 */         return cb[j];
/*     */       }
/*     */     } 
/*     */     
/* 123 */     throw new RuntimeException("fatal: not found");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtClass[] mayThrow() {
/* 133 */     ClassPool pool = this.thisClass.getClassPool();
/* 134 */     ConstPool cp = this.thisMethod.getConstPool();
/* 135 */     LinkedList list = new LinkedList();
/*     */     try {
/* 137 */       CodeAttribute ca = this.thisMethod.getCodeAttribute();
/* 138 */       ExceptionTable et = ca.getExceptionTable();
/* 139 */       int pos = this.currentPos;
/* 140 */       int n = et.size();
/* 141 */       for (int i = 0; i < n; i++) {
/* 142 */         if (et.startPc(i) <= pos && pos < et.endPc(i)) {
/* 143 */           int t = et.catchType(i);
/* 144 */           if (t > 0) {
/*     */             try {
/* 146 */               addClass(list, pool.get(cp.getClassInfo(t)));
/*     */             }
/* 148 */             catch (NotFoundException notFoundException) {}
/*     */           }
/*     */         } 
/*     */       } 
/* 152 */     } catch (NullPointerException nullPointerException) {}
/*     */ 
/*     */     
/* 155 */     ExceptionsAttribute ea = this.thisMethod.getExceptionsAttribute();
/* 156 */     if (ea != null) {
/* 157 */       String[] exceptions = ea.getExceptions();
/* 158 */       if (exceptions != null) {
/* 159 */         int n = exceptions.length;
/* 160 */         for (int i = 0; i < n; i++) {
/*     */           try {
/* 162 */             addClass(list, pool.get(exceptions[i]));
/*     */           }
/* 164 */           catch (NotFoundException notFoundException) {}
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 169 */     return (CtClass[])list.toArray((Object[])new CtClass[list.size()]);
/*     */   }
/*     */   
/*     */   private static void addClass(LinkedList<CtClass> list, CtClass c) {
/* 173 */     Iterator<CtClass> it = list.iterator();
/* 174 */     while (it.hasNext()) {
/* 175 */       if (it.next() == c)
/*     */         return; 
/*     */     } 
/* 178 */     list.add(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int indexOfBytecode() {
/* 187 */     return this.currentPos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLineNumber() {
/* 196 */     return this.thisMethod.getLineNumber(this.currentPos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/* 205 */     ClassFile cf = this.thisClass.getClassFile2();
/* 206 */     if (cf == null) {
/* 207 */       return null;
/*     */     }
/* 209 */     return cf.getSourceFile();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final boolean checkResultValue(CtClass retType, String prog) throws CannotCompileException {
/* 217 */     boolean hasIt = (prog.indexOf("$_") >= 0);
/* 218 */     if (!hasIt && retType != CtClass.voidType) {
/* 219 */       throw new CannotCompileException("the resulting value is not stored in $_");
/*     */     }
/*     */ 
/*     */     
/* 223 */     return hasIt;
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
/*     */   static final void storeStack(CtClass[] params, boolean isStaticCall, int regno, Bytecode bytecode) {
/* 235 */     storeStack0(0, params.length, params, regno + 1, bytecode);
/* 236 */     if (isStaticCall) {
/* 237 */       bytecode.addOpcode(1);
/*     */     }
/* 239 */     bytecode.addAstore(regno);
/*     */   }
/*     */   
/*     */   private static void storeStack0(int i, int n, CtClass[] params, int regno, Bytecode bytecode) {
/*     */     int size;
/* 244 */     if (i >= n) {
/*     */       return;
/*     */     }
/* 247 */     CtClass c = params[i];
/*     */     
/* 249 */     if (c instanceof CtPrimitiveType) {
/* 250 */       size = ((CtPrimitiveType)c).getDataSize();
/*     */     } else {
/* 252 */       size = 1;
/*     */     } 
/* 254 */     storeStack0(i + 1, n, params, regno + size, bytecode);
/* 255 */     bytecode.addStore(regno, c);
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
/*     */   public abstract void replace(String paramString) throws CannotCompileException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void replace(String statement, ExprEditor recursive) throws CannotCompileException {
/* 285 */     replace(statement);
/* 286 */     if (recursive != null) {
/* 287 */       runEditor(recursive, this.iterator);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void replace0(int pos, Bytecode bytecode, int size) throws BadBytecode {
/* 292 */     byte[] code = bytecode.get();
/* 293 */     this.edited = true;
/* 294 */     int gap = code.length - size;
/* 295 */     for (int i = 0; i < size; i++) {
/* 296 */       this.iterator.writeByte(0, pos + i);
/*     */     }
/* 298 */     if (gap > 0) {
/* 299 */       pos = (this.iterator.insertGapAt(pos, gap, false)).position;
/*     */     }
/* 301 */     this.iterator.write(code, pos);
/* 302 */     this.iterator.insert(bytecode.getExceptionTable(), pos);
/* 303 */     this.maxLocals = bytecode.getMaxLocals();
/* 304 */     this.maxStack = bytecode.getMaxStack();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void runEditor(ExprEditor ed, CodeIterator oldIterator) throws CannotCompileException {
/* 310 */     CodeAttribute codeAttr = oldIterator.get();
/* 311 */     int orgLocals = codeAttr.getMaxLocals();
/* 312 */     int orgStack = codeAttr.getMaxStack();
/* 313 */     int newLocals = locals();
/* 314 */     codeAttr.setMaxStack(stack());
/* 315 */     codeAttr.setMaxLocals(newLocals);
/* 316 */     ExprEditor.LoopContext context = new ExprEditor.LoopContext(newLocals);
/*     */     
/* 318 */     int size = oldIterator.getCodeLength();
/* 319 */     int endPos = oldIterator.lookAhead();
/* 320 */     oldIterator.move(this.currentPos);
/* 321 */     if (ed.doit(this.thisClass, this.thisMethod, context, oldIterator, endPos)) {
/* 322 */       this.edited = true;
/*     */     }
/* 324 */     oldIterator.move(endPos + oldIterator.getCodeLength() - size);
/* 325 */     codeAttr.setMaxLocals(orgLocals);
/* 326 */     codeAttr.setMaxStack(orgStack);
/* 327 */     this.maxLocals = context.maxLocals;
/* 328 */     this.maxStack += context.maxStack;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\expr\Expr.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */