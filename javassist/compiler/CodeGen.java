/*      */ package javassist.compiler;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import javassist.bytecode.Bytecode;
/*      */ import javassist.bytecode.Opcode;
/*      */ import javassist.compiler.ast.ASTList;
/*      */ import javassist.compiler.ast.ASTree;
/*      */ import javassist.compiler.ast.ArrayInit;
/*      */ import javassist.compiler.ast.AssignExpr;
/*      */ import javassist.compiler.ast.BinExpr;
/*      */ import javassist.compiler.ast.CallExpr;
/*      */ import javassist.compiler.ast.CastExpr;
/*      */ import javassist.compiler.ast.CondExpr;
/*      */ import javassist.compiler.ast.Declarator;
/*      */ import javassist.compiler.ast.DoubleConst;
/*      */ import javassist.compiler.ast.Expr;
/*      */ import javassist.compiler.ast.FieldDecl;
/*      */ import javassist.compiler.ast.InstanceOfExpr;
/*      */ import javassist.compiler.ast.IntConst;
/*      */ import javassist.compiler.ast.Keyword;
/*      */ import javassist.compiler.ast.Member;
/*      */ import javassist.compiler.ast.MethodDecl;
/*      */ import javassist.compiler.ast.NewExpr;
/*      */ import javassist.compiler.ast.Pair;
/*      */ import javassist.compiler.ast.Stmnt;
/*      */ import javassist.compiler.ast.StringL;
/*      */ import javassist.compiler.ast.Symbol;
/*      */ import javassist.compiler.ast.Variable;
/*      */ import javassist.compiler.ast.Visitor;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class CodeGen
/*      */   extends Visitor
/*      */   implements Opcode, TokenId
/*      */ {
/*      */   static final String javaLangObject = "java.lang.Object";
/*      */   static final String jvmJavaLangObject = "java/lang/Object";
/*      */   static final String javaLangString = "java.lang.String";
/*      */   static final String jvmJavaLangString = "java/lang/String";
/*      */   protected Bytecode bytecode;
/*      */   private int tempVar;
/*      */   TypeChecker typeChecker;
/*      */   protected boolean hasReturned;
/*      */   public boolean inStaticMethod;
/*      */   protected ArrayList breakList;
/*      */   protected ArrayList continueList;
/*      */   protected ReturnHook returnHooks;
/*      */   protected int exprType;
/*      */   protected int arrayDim;
/*      */   protected String className;
/*      */   
/*      */   protected static abstract class ReturnHook
/*      */   {
/*      */     ReturnHook next;
/*      */     
/*      */     protected abstract boolean doit(Bytecode param1Bytecode, int param1Int);
/*      */     
/*      */     protected ReturnHook(CodeGen gen) {
/*   65 */       this.next = gen.returnHooks;
/*   66 */       gen.returnHooks = this;
/*      */     }
/*      */     
/*      */     protected void remove(CodeGen gen) {
/*   70 */       gen.returnHooks = this.next;
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
/*      */   
/*      */   public CodeGen(Bytecode b) {
/*   84 */     this.bytecode = b;
/*   85 */     this.tempVar = -1;
/*   86 */     this.typeChecker = null;
/*   87 */     this.hasReturned = false;
/*   88 */     this.inStaticMethod = false;
/*   89 */     this.breakList = null;
/*   90 */     this.continueList = null;
/*   91 */     this.returnHooks = null;
/*      */   }
/*      */   
/*      */   public void setTypeChecker(TypeChecker checker) {
/*   95 */     this.typeChecker = checker;
/*      */   }
/*      */   
/*      */   protected static void fatal() throws CompileError {
/*   99 */     throw new CompileError("fatal");
/*      */   }
/*      */   
/*      */   public static boolean is2word(int type, int dim) {
/*  103 */     return (dim == 0 && (type == 312 || type == 326));
/*      */   }
/*      */   public int getMaxLocals() {
/*  106 */     return this.bytecode.getMaxLocals();
/*      */   }
/*      */   public void setMaxLocals(int n) {
/*  109 */     this.bytecode.setMaxLocals(n);
/*      */   }
/*      */   
/*      */   protected void incMaxLocals(int size) {
/*  113 */     this.bytecode.incMaxLocals(size);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int getTempVar() {
/*  121 */     if (this.tempVar < 0) {
/*  122 */       this.tempVar = getMaxLocals();
/*  123 */       incMaxLocals(2);
/*      */     } 
/*      */     
/*  126 */     return this.tempVar;
/*      */   }
/*      */   
/*      */   protected int getLocalVar(Declarator d) {
/*  130 */     int v = d.getLocalVar();
/*  131 */     if (v < 0) {
/*  132 */       v = getMaxLocals();
/*  133 */       d.setLocalVar(v);
/*  134 */       incMaxLocals(1);
/*      */     } 
/*      */     
/*  137 */     return v;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract String getThisName();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract String getSuperName() throws CompileError;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract String resolveClassName(ASTList paramASTList) throws CompileError;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract String resolveClassName(String paramString) throws CompileError;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static String toJvmArrayName(String name, int dim) {
/*  169 */     if (name == null) {
/*  170 */       return null;
/*      */     }
/*  172 */     if (dim == 0) {
/*  173 */       return name;
/*      */     }
/*  175 */     StringBuffer sbuf = new StringBuffer();
/*  176 */     int d = dim;
/*  177 */     while (d-- > 0) {
/*  178 */       sbuf.append('[');
/*      */     }
/*  180 */     sbuf.append('L');
/*  181 */     sbuf.append(name);
/*  182 */     sbuf.append(';');
/*      */     
/*  184 */     return sbuf.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   protected static String toJvmTypeName(int type, int dim) {
/*  189 */     char c = 'I';
/*  190 */     switch (type) {
/*      */       case 301:
/*  192 */         c = 'Z';
/*      */         break;
/*      */       case 303:
/*  195 */         c = 'B';
/*      */         break;
/*      */       case 306:
/*  198 */         c = 'C';
/*      */         break;
/*      */       case 334:
/*  201 */         c = 'S';
/*      */         break;
/*      */       case 324:
/*  204 */         c = 'I';
/*      */         break;
/*      */       case 326:
/*  207 */         c = 'J';
/*      */         break;
/*      */       case 317:
/*  210 */         c = 'F';
/*      */         break;
/*      */       case 312:
/*  213 */         c = 'D';
/*      */         break;
/*      */       case 344:
/*  216 */         c = 'V';
/*      */         break;
/*      */     } 
/*      */     
/*  220 */     StringBuffer sbuf = new StringBuffer();
/*  221 */     while (dim-- > 0) {
/*  222 */       sbuf.append('[');
/*      */     }
/*  224 */     sbuf.append(c);
/*  225 */     return sbuf.toString();
/*      */   }
/*      */   
/*      */   public void compileExpr(ASTree expr) throws CompileError {
/*  229 */     doTypeCheck(expr);
/*  230 */     expr.accept(this);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean compileBooleanExpr(boolean branchIf, ASTree expr) throws CompileError {
/*  236 */     doTypeCheck(expr);
/*  237 */     return booleanExpr(branchIf, expr);
/*      */   }
/*      */   
/*      */   public void doTypeCheck(ASTree expr) throws CompileError {
/*  241 */     if (this.typeChecker != null)
/*  242 */       expr.accept(this.typeChecker); 
/*      */   }
/*      */   public void atASTList(ASTList n) throws CompileError {
/*  245 */     fatal();
/*      */   } public void atPair(Pair n) throws CompileError {
/*  247 */     fatal();
/*      */   } public void atSymbol(Symbol n) throws CompileError {
/*  249 */     fatal();
/*      */   }
/*      */   public void atFieldDecl(FieldDecl field) throws CompileError {
/*  252 */     field.getInit().accept(this);
/*      */   }
/*      */   
/*      */   public void atMethodDecl(MethodDecl method) throws CompileError {
/*  256 */     ASTList mods = method.getModifiers();
/*  257 */     setMaxLocals(1);
/*  258 */     while (mods != null) {
/*  259 */       Keyword k = (Keyword)mods.head();
/*  260 */       mods = mods.tail();
/*  261 */       if (k.get() == 335) {
/*  262 */         setMaxLocals(0);
/*  263 */         this.inStaticMethod = true;
/*      */       } 
/*      */     } 
/*      */     
/*  267 */     ASTList params = method.getParams();
/*  268 */     while (params != null) {
/*  269 */       atDeclarator((Declarator)params.head());
/*  270 */       params = params.tail();
/*      */     } 
/*      */     
/*  273 */     Stmnt s = method.getBody();
/*  274 */     atMethodBody(s, method.isConstructor(), 
/*  275 */         (method.getReturn().getType() == 344));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void atMethodBody(Stmnt s, boolean isCons, boolean isVoid) throws CompileError {
/*  285 */     if (s == null) {
/*      */       return;
/*      */     }
/*  288 */     if (isCons && needsSuperCall(s)) {
/*  289 */       insertDefaultSuperCall();
/*      */     }
/*  291 */     this.hasReturned = false;
/*  292 */     s.accept(this);
/*  293 */     if (!this.hasReturned)
/*  294 */       if (isVoid) {
/*  295 */         this.bytecode.addOpcode(177);
/*  296 */         this.hasReturned = true;
/*      */       } else {
/*      */         
/*  299 */         throw new CompileError("no return statement");
/*      */       }  
/*      */   }
/*      */   private boolean needsSuperCall(Stmnt body) throws CompileError {
/*  303 */     if (body.getOperator() == 66) {
/*  304 */       body = (Stmnt)body.head();
/*      */     }
/*  306 */     if (body != null && body.getOperator() == 69) {
/*  307 */       ASTree expr = body.head();
/*  308 */       if (expr != null && expr instanceof Expr && ((Expr)expr)
/*  309 */         .getOperator() == 67) {
/*  310 */         ASTree target = ((Expr)expr).head();
/*  311 */         if (target instanceof Keyword) {
/*  312 */           int token = ((Keyword)target).get();
/*  313 */           return (token != 339 && token != 336);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  318 */     return true;
/*      */   }
/*      */   
/*      */   protected abstract void insertDefaultSuperCall() throws CompileError;
/*      */   
/*      */   public void atStmnt(Stmnt st) throws CompileError {
/*  324 */     if (st == null) {
/*      */       return;
/*      */     }
/*  327 */     int op = st.getOperator();
/*  328 */     if (op == 69) {
/*  329 */       ASTree expr = st.getLeft();
/*  330 */       doTypeCheck(expr);
/*  331 */       if (expr instanceof AssignExpr) {
/*  332 */         atAssignExpr((AssignExpr)expr, false);
/*  333 */       } else if (isPlusPlusExpr(expr)) {
/*  334 */         Expr e = (Expr)expr;
/*  335 */         atPlusPlus(e.getOperator(), e.oprand1(), e, false);
/*      */       } else {
/*      */         
/*  338 */         expr.accept(this);
/*  339 */         if (is2word(this.exprType, this.arrayDim)) {
/*  340 */           this.bytecode.addOpcode(88);
/*  341 */         } else if (this.exprType != 344) {
/*  342 */           this.bytecode.addOpcode(87);
/*      */         } 
/*      */       } 
/*  345 */     } else if (op == 68 || op == 66) {
/*  346 */       Stmnt stmnt = st;
/*  347 */       while (stmnt != null) {
/*  348 */         ASTree h = stmnt.head();
/*  349 */         ASTList aSTList = stmnt.tail();
/*  350 */         if (h != null) {
/*  351 */           h.accept(this);
/*      */         }
/*      */       } 
/*  354 */     } else if (op == 320) {
/*  355 */       atIfStmnt(st);
/*  356 */     } else if (op == 346 || op == 311) {
/*  357 */       atWhileStmnt(st, (op == 346));
/*  358 */     } else if (op == 318) {
/*  359 */       atForStmnt(st);
/*  360 */     } else if (op == 302 || op == 309) {
/*  361 */       atBreakStmnt(st, (op == 302));
/*  362 */     } else if (op == 333) {
/*  363 */       atReturnStmnt(st);
/*  364 */     } else if (op == 340) {
/*  365 */       atThrowStmnt(st);
/*  366 */     } else if (op == 343) {
/*  367 */       atTryStmnt(st);
/*  368 */     } else if (op == 337) {
/*  369 */       atSwitchStmnt(st);
/*  370 */     } else if (op == 338) {
/*  371 */       atSyncStmnt(st);
/*      */     } else {
/*      */       
/*  374 */       this.hasReturned = false;
/*  375 */       throw new CompileError("sorry, not supported statement: TokenId " + op);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void atIfStmnt(Stmnt st) throws CompileError {
/*  381 */     ASTree expr = st.head();
/*  382 */     Stmnt thenp = (Stmnt)st.tail().head();
/*  383 */     Stmnt elsep = (Stmnt)st.tail().tail().head();
/*  384 */     if (compileBooleanExpr(false, expr)) {
/*  385 */       this.hasReturned = false;
/*  386 */       if (elsep != null) {
/*  387 */         elsep.accept(this);
/*      */       }
/*      */       
/*      */       return;
/*      */     } 
/*  392 */     int pc = this.bytecode.currentPc();
/*  393 */     int pc2 = 0;
/*  394 */     this.bytecode.addIndex(0);
/*      */     
/*  396 */     this.hasReturned = false;
/*  397 */     if (thenp != null) {
/*  398 */       thenp.accept(this);
/*      */     }
/*  400 */     boolean thenHasReturned = this.hasReturned;
/*  401 */     this.hasReturned = false;
/*      */     
/*  403 */     if (elsep != null && !thenHasReturned) {
/*  404 */       this.bytecode.addOpcode(167);
/*  405 */       pc2 = this.bytecode.currentPc();
/*  406 */       this.bytecode.addIndex(0);
/*      */     } 
/*      */     
/*  409 */     this.bytecode.write16bit(pc, this.bytecode.currentPc() - pc + 1);
/*  410 */     if (elsep != null) {
/*  411 */       elsep.accept(this);
/*  412 */       if (!thenHasReturned) {
/*  413 */         this.bytecode.write16bit(pc2, this.bytecode.currentPc() - pc2 + 1);
/*      */       }
/*  415 */       this.hasReturned = (thenHasReturned && this.hasReturned);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void atWhileStmnt(Stmnt st, boolean notDo) throws CompileError {
/*  420 */     ArrayList prevBreakList = this.breakList;
/*  421 */     ArrayList prevContList = this.continueList;
/*  422 */     this.breakList = new ArrayList();
/*  423 */     this.continueList = new ArrayList();
/*      */     
/*  425 */     ASTree expr = st.head();
/*  426 */     Stmnt body = (Stmnt)st.tail();
/*      */     
/*  428 */     int pc = 0;
/*  429 */     if (notDo) {
/*  430 */       this.bytecode.addOpcode(167);
/*  431 */       pc = this.bytecode.currentPc();
/*  432 */       this.bytecode.addIndex(0);
/*      */     } 
/*      */     
/*  435 */     int pc2 = this.bytecode.currentPc();
/*  436 */     if (body != null) {
/*  437 */       body.accept(this);
/*      */     }
/*  439 */     int pc3 = this.bytecode.currentPc();
/*  440 */     if (notDo) {
/*  441 */       this.bytecode.write16bit(pc, pc3 - pc + 1);
/*      */     }
/*  443 */     boolean alwaysBranch = compileBooleanExpr(true, expr);
/*  444 */     if (alwaysBranch) {
/*  445 */       this.bytecode.addOpcode(167);
/*  446 */       alwaysBranch = (this.breakList.size() == 0);
/*      */     } 
/*      */     
/*  449 */     this.bytecode.addIndex(pc2 - this.bytecode.currentPc() + 1);
/*  450 */     patchGoto(this.breakList, this.bytecode.currentPc());
/*  451 */     patchGoto(this.continueList, pc3);
/*  452 */     this.continueList = prevContList;
/*  453 */     this.breakList = prevBreakList;
/*  454 */     this.hasReturned = alwaysBranch;
/*      */   }
/*      */   
/*      */   protected void patchGoto(ArrayList<Integer> list, int targetPc) {
/*  458 */     int n = list.size();
/*  459 */     for (int i = 0; i < n; i++) {
/*  460 */       int pc = ((Integer)list.get(i)).intValue();
/*  461 */       this.bytecode.write16bit(pc, targetPc - pc + 1);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void atForStmnt(Stmnt st) throws CompileError {
/*  466 */     ArrayList prevBreakList = this.breakList;
/*  467 */     ArrayList prevContList = this.continueList;
/*  468 */     this.breakList = new ArrayList();
/*  469 */     this.continueList = new ArrayList();
/*      */     
/*  471 */     Stmnt init = (Stmnt)st.head();
/*  472 */     ASTList p = st.tail();
/*  473 */     ASTree expr = p.head();
/*  474 */     p = p.tail();
/*  475 */     Stmnt update = (Stmnt)p.head();
/*  476 */     Stmnt body = (Stmnt)p.tail();
/*      */     
/*  478 */     if (init != null) {
/*  479 */       init.accept(this);
/*      */     }
/*  481 */     int pc = this.bytecode.currentPc();
/*  482 */     int pc2 = 0;
/*  483 */     if (expr != null) {
/*  484 */       if (compileBooleanExpr(false, expr)) {
/*      */         
/*  486 */         this.continueList = prevContList;
/*  487 */         this.breakList = prevBreakList;
/*  488 */         this.hasReturned = false;
/*      */         
/*      */         return;
/*      */       } 
/*  492 */       pc2 = this.bytecode.currentPc();
/*  493 */       this.bytecode.addIndex(0);
/*      */     } 
/*      */     
/*  496 */     if (body != null) {
/*  497 */       body.accept(this);
/*      */     }
/*  499 */     int pc3 = this.bytecode.currentPc();
/*  500 */     if (update != null) {
/*  501 */       update.accept(this);
/*      */     }
/*  503 */     this.bytecode.addOpcode(167);
/*  504 */     this.bytecode.addIndex(pc - this.bytecode.currentPc() + 1);
/*      */     
/*  506 */     int pc4 = this.bytecode.currentPc();
/*  507 */     if (expr != null) {
/*  508 */       this.bytecode.write16bit(pc2, pc4 - pc2 + 1);
/*      */     }
/*  510 */     patchGoto(this.breakList, pc4);
/*  511 */     patchGoto(this.continueList, pc3);
/*  512 */     this.continueList = prevContList;
/*  513 */     this.breakList = prevBreakList;
/*  514 */     this.hasReturned = false;
/*      */   }
/*      */   
/*      */   private void atSwitchStmnt(Stmnt st) throws CompileError {
/*  518 */     compileExpr(st.head());
/*      */     
/*  520 */     ArrayList prevBreakList = this.breakList;
/*  521 */     this.breakList = new ArrayList();
/*  522 */     int opcodePc = this.bytecode.currentPc();
/*  523 */     this.bytecode.addOpcode(171);
/*  524 */     int npads = 3 - (opcodePc & 0x3);
/*  525 */     while (npads-- > 0) {
/*  526 */       this.bytecode.add(0);
/*      */     }
/*  528 */     Stmnt body = (Stmnt)st.tail();
/*  529 */     int npairs = 0;
/*  530 */     for (Stmnt stmnt1 = body; stmnt1 != null; aSTList = stmnt1.tail()) {
/*  531 */       ASTList aSTList; if (((Stmnt)stmnt1.head()).getOperator() == 304) {
/*  532 */         npairs++;
/*      */       }
/*      */     } 
/*  535 */     int opcodePc2 = this.bytecode.currentPc();
/*  536 */     this.bytecode.addGap(4);
/*  537 */     this.bytecode.add32bit(npairs);
/*  538 */     this.bytecode.addGap(npairs * 8);
/*      */     
/*  540 */     long[] pairs = new long[npairs];
/*  541 */     int ipairs = 0;
/*  542 */     int defaultPc = -1;
/*  543 */     for (Stmnt stmnt2 = body; stmnt2 != null; aSTList = stmnt2.tail()) {
/*  544 */       ASTList aSTList; Stmnt label = (Stmnt)stmnt2.head();
/*  545 */       int op = label.getOperator();
/*  546 */       if (op == 310) {
/*  547 */         defaultPc = this.bytecode.currentPc();
/*  548 */       } else if (op != 304) {
/*  549 */         fatal();
/*      */       } else {
/*  551 */         pairs[ipairs++] = (
/*  552 */           computeLabel(label.head()) << 32L) + ((this.bytecode
/*  553 */           .currentPc() - opcodePc) & 0xFFFFFFFFFFFFFFFFL);
/*      */       } 
/*      */       
/*  556 */       this.hasReturned = false;
/*  557 */       ((Stmnt)label.tail()).accept(this);
/*      */     } 
/*      */     
/*  560 */     Arrays.sort(pairs);
/*  561 */     int pc = opcodePc2 + 8;
/*  562 */     for (int i = 0; i < npairs; i++) {
/*  563 */       this.bytecode.write32bit(pc, (int)(pairs[i] >>> 32L));
/*  564 */       this.bytecode.write32bit(pc + 4, (int)pairs[i]);
/*  565 */       pc += 8;
/*      */     } 
/*      */     
/*  568 */     if (defaultPc < 0 || this.breakList.size() > 0) {
/*  569 */       this.hasReturned = false;
/*      */     }
/*  571 */     int endPc = this.bytecode.currentPc();
/*  572 */     if (defaultPc < 0) {
/*  573 */       defaultPc = endPc;
/*      */     }
/*  575 */     this.bytecode.write32bit(opcodePc2, defaultPc - opcodePc);
/*      */     
/*  577 */     patchGoto(this.breakList, endPc);
/*  578 */     this.breakList = prevBreakList;
/*      */   }
/*      */   
/*      */   private int computeLabel(ASTree expr) throws CompileError {
/*  582 */     doTypeCheck(expr);
/*  583 */     expr = TypeChecker.stripPlusExpr(expr);
/*  584 */     if (expr instanceof IntConst) {
/*  585 */       return (int)((IntConst)expr).get();
/*      */     }
/*  587 */     throw new CompileError("bad case label");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void atBreakStmnt(Stmnt st, boolean notCont) throws CompileError {
/*  593 */     if (st.head() != null) {
/*  594 */       throw new CompileError("sorry, not support labeled break or continue");
/*      */     }
/*      */     
/*  597 */     this.bytecode.addOpcode(167);
/*  598 */     Integer pc = new Integer(this.bytecode.currentPc());
/*  599 */     this.bytecode.addIndex(0);
/*  600 */     if (notCont) {
/*  601 */       this.breakList.add(pc);
/*      */     } else {
/*  603 */       this.continueList.add(pc);
/*      */     } 
/*      */   }
/*      */   protected void atReturnStmnt(Stmnt st) throws CompileError {
/*  607 */     atReturnStmnt2(st.getLeft());
/*      */   }
/*      */   
/*      */   protected final void atReturnStmnt2(ASTree result) throws CompileError {
/*      */     int op;
/*  612 */     if (result == null) {
/*  613 */       op = 177;
/*      */     } else {
/*  615 */       compileExpr(result);
/*  616 */       if (this.arrayDim > 0) {
/*  617 */         op = 176;
/*      */       } else {
/*  619 */         int type = this.exprType;
/*  620 */         if (type == 312) {
/*  621 */           op = 175;
/*  622 */         } else if (type == 317) {
/*  623 */           op = 174;
/*  624 */         } else if (type == 326) {
/*  625 */           op = 173;
/*  626 */         } else if (isRefType(type)) {
/*  627 */           op = 176;
/*      */         } else {
/*  629 */           op = 172;
/*      */         } 
/*      */       } 
/*      */     } 
/*  633 */     for (ReturnHook har = this.returnHooks; har != null; har = har.next) {
/*  634 */       if (har.doit(this.bytecode, op)) {
/*  635 */         this.hasReturned = true;
/*      */         return;
/*      */       } 
/*      */     } 
/*  639 */     this.bytecode.addOpcode(op);
/*  640 */     this.hasReturned = true;
/*      */   }
/*      */   
/*      */   private void atThrowStmnt(Stmnt st) throws CompileError {
/*  644 */     ASTree e = st.getLeft();
/*  645 */     compileExpr(e);
/*  646 */     if (this.exprType != 307 || this.arrayDim > 0) {
/*  647 */       throw new CompileError("bad throw statement");
/*      */     }
/*  649 */     this.bytecode.addOpcode(191);
/*  650 */     this.hasReturned = true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void atTryStmnt(Stmnt st) throws CompileError {
/*  656 */     this.hasReturned = false;
/*      */   }
/*      */   
/*      */   private void atSyncStmnt(Stmnt st) throws CompileError {
/*  660 */     int nbreaks = getListSize(this.breakList);
/*  661 */     int ncontinues = getListSize(this.continueList);
/*      */     
/*  663 */     compileExpr(st.head());
/*  664 */     if (this.exprType != 307 && this.arrayDim == 0) {
/*  665 */       throw new CompileError("bad type expr for synchronized block");
/*      */     }
/*  667 */     Bytecode bc = this.bytecode;
/*  668 */     final int var = bc.getMaxLocals();
/*  669 */     bc.incMaxLocals(1);
/*  670 */     bc.addOpcode(89);
/*  671 */     bc.addAstore(var);
/*  672 */     bc.addOpcode(194);
/*      */     
/*  674 */     ReturnHook rh = new ReturnHook(this) {
/*      */         protected boolean doit(Bytecode b, int opcode) {
/*  676 */           b.addAload(var);
/*  677 */           b.addOpcode(195);
/*  678 */           return false;
/*      */         }
/*      */       };
/*      */     
/*  682 */     int pc = bc.currentPc();
/*  683 */     Stmnt body = (Stmnt)st.tail();
/*  684 */     if (body != null) {
/*  685 */       body.accept(this);
/*      */     }
/*  687 */     int pc2 = bc.currentPc();
/*  688 */     int pc3 = 0;
/*  689 */     if (!this.hasReturned) {
/*  690 */       rh.doit(bc, 0);
/*  691 */       bc.addOpcode(167);
/*  692 */       pc3 = bc.currentPc();
/*  693 */       bc.addIndex(0);
/*      */     } 
/*      */     
/*  696 */     if (pc < pc2) {
/*  697 */       int pc4 = bc.currentPc();
/*  698 */       rh.doit(bc, 0);
/*  699 */       bc.addOpcode(191);
/*  700 */       bc.addExceptionHandler(pc, pc2, pc4, 0);
/*      */     } 
/*      */     
/*  703 */     if (!this.hasReturned) {
/*  704 */       bc.write16bit(pc3, bc.currentPc() - pc3 + 1);
/*      */     }
/*  706 */     rh.remove(this);
/*      */     
/*  708 */     if (getListSize(this.breakList) != nbreaks || 
/*  709 */       getListSize(this.continueList) != ncontinues) {
/*  710 */       throw new CompileError("sorry, cannot break/continue in synchronized block");
/*      */     }
/*      */   }
/*      */   
/*      */   private static int getListSize(ArrayList list) {
/*  715 */     return (list == null) ? 0 : list.size();
/*      */   }
/*      */   
/*      */   private static boolean isPlusPlusExpr(ASTree expr) {
/*  719 */     if (expr instanceof Expr) {
/*  720 */       int op = ((Expr)expr).getOperator();
/*  721 */       return (op == 362 || op == 363);
/*      */     } 
/*      */     
/*  724 */     return false;
/*      */   }
/*      */   public void atDeclarator(Declarator d) throws CompileError {
/*      */     int size;
/*  728 */     d.setLocalVar(getMaxLocals());
/*  729 */     d.setClassName(resolveClassName(d.getClassName()));
/*      */ 
/*      */     
/*  732 */     if (is2word(d.getType(), d.getArrayDim())) {
/*  733 */       size = 2;
/*      */     } else {
/*  735 */       size = 1;
/*      */     } 
/*  737 */     incMaxLocals(size);
/*      */ 
/*      */ 
/*      */     
/*  741 */     ASTree init = d.getInitializer();
/*  742 */     if (init != null) {
/*  743 */       doTypeCheck(init);
/*  744 */       atVariableAssign(null, 61, null, d, init, false);
/*      */     } 
/*      */   }
/*      */   
/*      */   public abstract void atNewExpr(NewExpr paramNewExpr) throws CompileError;
/*      */   
/*      */   public abstract void atArrayInit(ArrayInit paramArrayInit) throws CompileError;
/*      */   
/*      */   public void atAssignExpr(AssignExpr expr) throws CompileError {
/*  753 */     atAssignExpr(expr, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void atAssignExpr(AssignExpr expr, boolean doDup) throws CompileError {
/*  760 */     int op = expr.getOperator();
/*  761 */     ASTree left = expr.oprand1();
/*  762 */     ASTree right = expr.oprand2();
/*  763 */     if (left instanceof Variable) {
/*  764 */       atVariableAssign((Expr)expr, op, (Variable)left, ((Variable)left)
/*  765 */           .getDeclarator(), right, doDup);
/*      */     } else {
/*      */       
/*  768 */       if (left instanceof Expr) {
/*  769 */         Expr e = (Expr)left;
/*  770 */         if (e.getOperator() == 65) {
/*  771 */           atArrayAssign((Expr)expr, op, (Expr)left, right, doDup);
/*      */           
/*      */           return;
/*      */         } 
/*      */       } 
/*  776 */       atFieldAssign((Expr)expr, op, left, right, doDup);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected static void badAssign(Expr expr) throws CompileError {
/*      */     String msg;
/*  782 */     if (expr == null) {
/*  783 */       msg = "incompatible type for assignment";
/*      */     } else {
/*  785 */       msg = "incompatible type for " + expr.getName();
/*      */     } 
/*  787 */     throw new CompileError(msg);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void atVariableAssign(Expr expr, int op, Variable var, Declarator d, ASTree right, boolean doDup) throws CompileError {
/*  798 */     int varType = d.getType();
/*  799 */     int varArray = d.getArrayDim();
/*  800 */     String varClass = d.getClassName();
/*  801 */     int varNo = getLocalVar(d);
/*      */     
/*  803 */     if (op != 61) {
/*  804 */       atVariable(var);
/*      */     }
/*      */     
/*  807 */     if (expr == null && right instanceof ArrayInit) {
/*  808 */       atArrayVariableAssign((ArrayInit)right, varType, varArray, varClass);
/*      */     } else {
/*  810 */       atAssignCore(expr, op, right, varType, varArray, varClass);
/*      */     } 
/*  812 */     if (doDup)
/*  813 */       if (is2word(varType, varArray)) {
/*  814 */         this.bytecode.addOpcode(92);
/*      */       } else {
/*  816 */         this.bytecode.addOpcode(89);
/*      */       }  
/*  818 */     if (varArray > 0) {
/*  819 */       this.bytecode.addAstore(varNo);
/*  820 */     } else if (varType == 312) {
/*  821 */       this.bytecode.addDstore(varNo);
/*  822 */     } else if (varType == 317) {
/*  823 */       this.bytecode.addFstore(varNo);
/*  824 */     } else if (varType == 326) {
/*  825 */       this.bytecode.addLstore(varNo);
/*  826 */     } else if (isRefType(varType)) {
/*  827 */       this.bytecode.addAstore(varNo);
/*      */     } else {
/*  829 */       this.bytecode.addIstore(varNo);
/*      */     } 
/*  831 */     this.exprType = varType;
/*  832 */     this.arrayDim = varArray;
/*  833 */     this.className = varClass;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract void atArrayVariableAssign(ArrayInit paramArrayInit, int paramInt1, int paramInt2, String paramString) throws CompileError;
/*      */ 
/*      */   
/*      */   private void atArrayAssign(Expr expr, int op, Expr array, ASTree right, boolean doDup) throws CompileError {
/*  842 */     arrayAccess(array.oprand1(), array.oprand2());
/*      */     
/*  844 */     if (op != 61) {
/*  845 */       this.bytecode.addOpcode(92);
/*  846 */       this.bytecode.addOpcode(getArrayReadOp(this.exprType, this.arrayDim));
/*      */     } 
/*      */     
/*  849 */     int aType = this.exprType;
/*  850 */     int aDim = this.arrayDim;
/*  851 */     String cname = this.className;
/*      */     
/*  853 */     atAssignCore(expr, op, right, aType, aDim, cname);
/*      */     
/*  855 */     if (doDup)
/*  856 */       if (is2word(aType, aDim)) {
/*  857 */         this.bytecode.addOpcode(94);
/*      */       } else {
/*  859 */         this.bytecode.addOpcode(91);
/*      */       }  
/*  861 */     this.bytecode.addOpcode(getArrayWriteOp(aType, aDim));
/*  862 */     this.exprType = aType;
/*  863 */     this.arrayDim = aDim;
/*  864 */     this.className = cname;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract void atFieldAssign(Expr paramExpr, int paramInt, ASTree paramASTree1, ASTree paramASTree2, boolean paramBoolean) throws CompileError;
/*      */ 
/*      */ 
/*      */   
/*      */   protected void atAssignCore(Expr expr, int op, ASTree right, int type, int dim, String cname) throws CompileError {
/*  874 */     if (op == 354 && dim == 0 && type == 307) {
/*  875 */       atStringPlusEq(expr, type, dim, cname, right);
/*      */     } else {
/*  877 */       right.accept(this);
/*  878 */       if (invalidDim(this.exprType, this.arrayDim, this.className, type, dim, cname, false) || (op != 61 && dim > 0))
/*      */       {
/*  880 */         badAssign(expr);
/*      */       }
/*  882 */       if (op != 61) {
/*  883 */         int token = assignOps[op - 351];
/*  884 */         int k = lookupBinOp(token);
/*  885 */         if (k < 0) {
/*  886 */           fatal();
/*      */         }
/*  888 */         atArithBinExpr(expr, token, k, type);
/*      */       } 
/*      */     } 
/*      */     
/*  892 */     if (op != 61 || (dim == 0 && !isRefType(type))) {
/*  893 */       atNumCastExpr(this.exprType, type);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void atStringPlusEq(Expr expr, int type, int dim, String cname, ASTree right) throws CompileError {
/*  902 */     if (!"java/lang/String".equals(cname)) {
/*  903 */       badAssign(expr);
/*      */     }
/*  905 */     convToString(type, dim);
/*  906 */     right.accept(this);
/*  907 */     convToString(this.exprType, this.arrayDim);
/*  908 */     this.bytecode.addInvokevirtual("java.lang.String", "concat", "(Ljava/lang/String;)Ljava/lang/String;");
/*      */     
/*  910 */     this.exprType = 307;
/*  911 */     this.arrayDim = 0;
/*  912 */     this.className = "java/lang/String";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean invalidDim(int srcType, int srcDim, String srcClass, int destType, int destDim, String destClass, boolean isCast) {
/*  919 */     if (srcDim != destDim) {
/*  920 */       if (srcType == 412)
/*  921 */         return false; 
/*  922 */       if (destDim == 0 && destType == 307 && "java/lang/Object"
/*  923 */         .equals(destClass))
/*  924 */         return false; 
/*  925 */       if (isCast && srcDim == 0 && srcType == 307 && "java/lang/Object"
/*  926 */         .equals(srcClass)) {
/*  927 */         return false;
/*      */       }
/*  929 */       return true;
/*      */     } 
/*  931 */     return false;
/*      */   }
/*      */   
/*      */   public void atCondExpr(CondExpr expr) throws CompileError {
/*  935 */     if (booleanExpr(false, expr.condExpr())) {
/*  936 */       expr.elseExpr().accept(this);
/*      */     } else {
/*  938 */       int pc = this.bytecode.currentPc();
/*  939 */       this.bytecode.addIndex(0);
/*  940 */       expr.thenExpr().accept(this);
/*  941 */       int dim1 = this.arrayDim;
/*  942 */       this.bytecode.addOpcode(167);
/*  943 */       int pc2 = this.bytecode.currentPc();
/*  944 */       this.bytecode.addIndex(0);
/*  945 */       this.bytecode.write16bit(pc, this.bytecode.currentPc() - pc + 1);
/*  946 */       expr.elseExpr().accept(this);
/*  947 */       if (dim1 != this.arrayDim) {
/*  948 */         throw new CompileError("type mismatch in ?:");
/*      */       }
/*  950 */       this.bytecode.write16bit(pc2, this.bytecode.currentPc() - pc2 + 1);
/*      */     } 
/*      */   }
/*      */   
/*  954 */   static final int[] binOp = new int[] { 43, 99, 98, 97, 96, 45, 103, 102, 101, 100, 42, 107, 106, 105, 104, 47, 111, 110, 109, 108, 37, 115, 114, 113, 112, 124, 0, 0, 129, 128, 94, 0, 0, 131, 130, 38, 0, 0, 127, 126, 364, 0, 0, 121, 120, 366, 0, 0, 123, 122, 370, 0, 0, 125, 124 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int lookupBinOp(int token) {
/*  968 */     int[] code = binOp;
/*  969 */     int s = code.length;
/*  970 */     for (int k = 0; k < s; k += 5) {
/*  971 */       if (code[k] == token)
/*  972 */         return k; 
/*      */     } 
/*  974 */     return -1;
/*      */   }
/*      */   
/*      */   public void atBinExpr(BinExpr expr) throws CompileError {
/*  978 */     int token = expr.getOperator();
/*      */ 
/*      */ 
/*      */     
/*  982 */     int k = lookupBinOp(token);
/*  983 */     if (k >= 0) {
/*  984 */       expr.oprand1().accept(this);
/*  985 */       ASTree right = expr.oprand2();
/*  986 */       if (right == null) {
/*      */         return;
/*      */       }
/*  989 */       int type1 = this.exprType;
/*  990 */       int dim1 = this.arrayDim;
/*  991 */       String cname1 = this.className;
/*  992 */       right.accept(this);
/*  993 */       if (dim1 != this.arrayDim) {
/*  994 */         throw new CompileError("incompatible array types");
/*      */       }
/*  996 */       if (token == 43 && dim1 == 0 && (type1 == 307 || this.exprType == 307)) {
/*      */         
/*  998 */         atStringConcatExpr((Expr)expr, type1, dim1, cname1);
/*      */       } else {
/* 1000 */         atArithBinExpr((Expr)expr, token, k, type1);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 1005 */       if (!booleanExpr(true, (ASTree)expr)) {
/* 1006 */         this.bytecode.addIndex(7);
/* 1007 */         this.bytecode.addIconst(0);
/* 1008 */         this.bytecode.addOpcode(167);
/* 1009 */         this.bytecode.addIndex(4);
/*      */       } 
/*      */       
/* 1012 */       this.bytecode.addIconst(1);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void atArithBinExpr(Expr expr, int token, int index, int type1) throws CompileError {
/* 1023 */     if (this.arrayDim != 0) {
/* 1024 */       badTypes(expr);
/*      */     }
/* 1026 */     int type2 = this.exprType;
/* 1027 */     if (token == 364 || token == 366 || token == 370)
/* 1028 */     { if (type2 == 324 || type2 == 334 || type2 == 306 || type2 == 303) {
/*      */         
/* 1030 */         this.exprType = type1;
/*      */       } else {
/* 1032 */         badTypes(expr);
/*      */       }  }
/* 1034 */     else { convertOprandTypes(type1, type2, expr); }
/*      */     
/* 1036 */     int p = typePrecedence(this.exprType);
/* 1037 */     if (p >= 0) {
/* 1038 */       int op = binOp[index + p + 1];
/* 1039 */       if (op != 0) {
/* 1040 */         if (p == 3 && this.exprType != 301) {
/* 1041 */           this.exprType = 324;
/*      */         }
/* 1043 */         this.bytecode.addOpcode(op);
/*      */         
/*      */         return;
/*      */       } 
/*      */     } 
/* 1048 */     badTypes(expr);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void atStringConcatExpr(Expr expr, int type1, int dim1, String cname1) throws CompileError {
/* 1054 */     int type2 = this.exprType;
/* 1055 */     int dim2 = this.arrayDim;
/* 1056 */     boolean type2Is2 = is2word(type2, dim2);
/*      */     
/* 1058 */     boolean type2IsString = (type2 == 307 && "java/lang/String".equals(this.className));
/*      */     
/* 1060 */     if (type2Is2) {
/* 1061 */       convToString(type2, dim2);
/*      */     }
/* 1063 */     if (is2word(type1, dim1)) {
/* 1064 */       this.bytecode.addOpcode(91);
/* 1065 */       this.bytecode.addOpcode(87);
/*      */     } else {
/*      */       
/* 1068 */       this.bytecode.addOpcode(95);
/*      */     } 
/*      */     
/* 1071 */     convToString(type1, dim1);
/* 1072 */     this.bytecode.addOpcode(95);
/*      */     
/* 1074 */     if (!type2Is2 && !type2IsString) {
/* 1075 */       convToString(type2, dim2);
/*      */     }
/* 1077 */     this.bytecode.addInvokevirtual("java.lang.String", "concat", "(Ljava/lang/String;)Ljava/lang/String;");
/*      */     
/* 1079 */     this.exprType = 307;
/* 1080 */     this.arrayDim = 0;
/* 1081 */     this.className = "java/lang/String";
/*      */   }
/*      */   
/*      */   private void convToString(int type, int dim) throws CompileError {
/* 1085 */     String method = "valueOf";
/*      */     
/* 1087 */     if (isRefType(type) || dim > 0) {
/* 1088 */       this.bytecode.addInvokestatic("java.lang.String", "valueOf", "(Ljava/lang/Object;)Ljava/lang/String;");
/*      */     }
/* 1090 */     else if (type == 312) {
/* 1091 */       this.bytecode.addInvokestatic("java.lang.String", "valueOf", "(D)Ljava/lang/String;");
/*      */     }
/* 1093 */     else if (type == 317) {
/* 1094 */       this.bytecode.addInvokestatic("java.lang.String", "valueOf", "(F)Ljava/lang/String;");
/*      */     }
/* 1096 */     else if (type == 326) {
/* 1097 */       this.bytecode.addInvokestatic("java.lang.String", "valueOf", "(J)Ljava/lang/String;");
/*      */     }
/* 1099 */     else if (type == 301) {
/* 1100 */       this.bytecode.addInvokestatic("java.lang.String", "valueOf", "(Z)Ljava/lang/String;");
/*      */     }
/* 1102 */     else if (type == 306) {
/* 1103 */       this.bytecode.addInvokestatic("java.lang.String", "valueOf", "(C)Ljava/lang/String;");
/*      */     } else {
/* 1105 */       if (type == 344) {
/* 1106 */         throw new CompileError("void type expression");
/*      */       }
/* 1108 */       this.bytecode.addInvokestatic("java.lang.String", "valueOf", "(I)Ljava/lang/String;");
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
/*      */   
/*      */   private boolean booleanExpr(boolean branchIf, ASTree expr) throws CompileError {
/* 1122 */     int op = getCompOperator(expr);
/* 1123 */     if (op == 358) {
/* 1124 */       BinExpr bexpr = (BinExpr)expr;
/* 1125 */       int type1 = compileOprands(bexpr);
/*      */ 
/*      */       
/* 1128 */       compareExpr(branchIf, bexpr.getOperator(), type1, bexpr);
/*      */     } else {
/* 1130 */       if (op == 33)
/* 1131 */         return booleanExpr(!branchIf, ((Expr)expr).oprand1());  boolean isAndAnd;
/* 1132 */       if ((isAndAnd = (op == 369)) || op == 368) {
/* 1133 */         BinExpr bexpr = (BinExpr)expr;
/* 1134 */         if (booleanExpr(!isAndAnd, bexpr.oprand1())) {
/* 1135 */           this.exprType = 301;
/* 1136 */           this.arrayDim = 0;
/* 1137 */           return true;
/*      */         } 
/*      */         
/* 1140 */         int pc = this.bytecode.currentPc();
/* 1141 */         this.bytecode.addIndex(0);
/* 1142 */         if (booleanExpr(isAndAnd, bexpr.oprand2())) {
/* 1143 */           this.bytecode.addOpcode(167);
/*      */         }
/* 1145 */         this.bytecode.write16bit(pc, this.bytecode.currentPc() - pc + 3);
/* 1146 */         if (branchIf != isAndAnd) {
/* 1147 */           this.bytecode.addIndex(6);
/* 1148 */           this.bytecode.addOpcode(167);
/*      */         } 
/*      */       } else {
/*      */         
/* 1152 */         if (isAlwaysBranch(expr, branchIf)) {
/*      */           
/* 1154 */           this.exprType = 301;
/* 1155 */           this.arrayDim = 0;
/* 1156 */           return true;
/*      */         } 
/*      */         
/* 1159 */         expr.accept(this);
/* 1160 */         if (this.exprType != 301 || this.arrayDim != 0) {
/* 1161 */           throw new CompileError("boolean expr is required");
/*      */         }
/* 1163 */         this.bytecode.addOpcode(branchIf ? 154 : 153);
/*      */       } 
/*      */     } 
/* 1166 */     this.exprType = 301;
/* 1167 */     this.arrayDim = 0;
/* 1168 */     return false;
/*      */   }
/*      */   
/*      */   private static boolean isAlwaysBranch(ASTree expr, boolean branchIf) {
/* 1172 */     if (expr instanceof Keyword) {
/* 1173 */       int t = ((Keyword)expr).get();
/* 1174 */       return branchIf ? ((t == 410)) : ((t == 411));
/*      */     } 
/*      */     
/* 1177 */     return false;
/*      */   }
/*      */   
/*      */   static int getCompOperator(ASTree expr) throws CompileError {
/* 1181 */     if (expr instanceof Expr) {
/* 1182 */       Expr bexpr = (Expr)expr;
/* 1183 */       int token = bexpr.getOperator();
/* 1184 */       if (token == 33)
/* 1185 */         return 33; 
/* 1186 */       if (bexpr instanceof BinExpr && token != 368 && token != 369 && token != 38 && token != 124)
/*      */       {
/*      */         
/* 1189 */         return 358;
/*      */       }
/* 1191 */       return token;
/*      */     } 
/*      */     
/* 1194 */     return 32;
/*      */   }
/*      */   
/*      */   private int compileOprands(BinExpr expr) throws CompileError {
/* 1198 */     expr.oprand1().accept(this);
/* 1199 */     int type1 = this.exprType;
/* 1200 */     int dim1 = this.arrayDim;
/* 1201 */     expr.oprand2().accept(this);
/* 1202 */     if (dim1 != this.arrayDim) {
/* 1203 */       if (type1 != 412 && this.exprType != 412)
/* 1204 */         throw new CompileError("incompatible array types"); 
/* 1205 */       if (this.exprType == 412)
/* 1206 */         this.arrayDim = dim1; 
/*      */     } 
/* 1208 */     if (type1 == 412) {
/* 1209 */       return this.exprType;
/*      */     }
/* 1211 */     return type1;
/*      */   }
/*      */   
/* 1214 */   private static final int[] ifOp = new int[] { 358, 159, 160, 350, 160, 159, 357, 164, 163, 359, 162, 161, 60, 161, 162, 62, 163, 164 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1221 */   private static final int[] ifOp2 = new int[] { 358, 153, 154, 350, 154, 153, 357, 158, 157, 359, 156, 155, 60, 155, 156, 62, 157, 158 };
/*      */ 
/*      */   
/*      */   private static final int P_DOUBLE = 0;
/*      */ 
/*      */   
/*      */   private static final int P_FLOAT = 1;
/*      */   
/*      */   private static final int P_LONG = 2;
/*      */   
/*      */   private static final int P_INT = 3;
/*      */   
/*      */   private static final int P_OTHER = -1;
/*      */ 
/*      */   
/*      */   private void compareExpr(boolean branchIf, int token, int type1, BinExpr expr) throws CompileError {
/* 1237 */     if (this.arrayDim == 0) {
/* 1238 */       convertOprandTypes(type1, this.exprType, (Expr)expr);
/*      */     }
/* 1240 */     int p = typePrecedence(this.exprType);
/* 1241 */     if (p == -1 || this.arrayDim > 0) {
/* 1242 */       if (token == 358) {
/* 1243 */         this.bytecode.addOpcode(branchIf ? 165 : 166);
/* 1244 */       } else if (token == 350) {
/* 1245 */         this.bytecode.addOpcode(branchIf ? 166 : 165);
/*      */       } else {
/* 1247 */         badTypes((Expr)expr);
/*      */       } 
/* 1249 */     } else if (p == 3) {
/* 1250 */       int[] op = ifOp;
/* 1251 */       for (int i = 0; i < op.length; i += 3) {
/* 1252 */         if (op[i] == token) {
/* 1253 */           this.bytecode.addOpcode(op[i + (branchIf ? 1 : 2)]);
/*      */           return;
/*      */         } 
/*      */       } 
/* 1257 */       badTypes((Expr)expr);
/*      */     } else {
/*      */       
/* 1260 */       if (p == 0) {
/* 1261 */         if (token == 60 || token == 357)
/* 1262 */         { this.bytecode.addOpcode(152); }
/*      */         else
/* 1264 */         { this.bytecode.addOpcode(151); } 
/* 1265 */       } else if (p == 1) {
/* 1266 */         if (token == 60 || token == 357)
/* 1267 */         { this.bytecode.addOpcode(150); }
/*      */         else
/* 1269 */         { this.bytecode.addOpcode(149); } 
/* 1270 */       } else if (p == 2) {
/* 1271 */         this.bytecode.addOpcode(148);
/*      */       } else {
/* 1273 */         fatal();
/*      */       } 
/* 1275 */       int[] op = ifOp2;
/* 1276 */       for (int i = 0; i < op.length; i += 3) {
/* 1277 */         if (op[i] == token) {
/* 1278 */           this.bytecode.addOpcode(op[i + (branchIf ? 1 : 2)]);
/*      */           return;
/*      */         } 
/*      */       } 
/* 1282 */       badTypes((Expr)expr);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected static void badTypes(Expr expr) throws CompileError {
/* 1287 */     throw new CompileError("invalid types for " + expr.getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static boolean isRefType(int type) {
/* 1297 */     return (type == 307 || type == 412);
/*      */   }
/*      */   
/*      */   private static int typePrecedence(int type) {
/* 1301 */     if (type == 312)
/* 1302 */       return 0; 
/* 1303 */     if (type == 317)
/* 1304 */       return 1; 
/* 1305 */     if (type == 326)
/* 1306 */       return 2; 
/* 1307 */     if (isRefType(type))
/* 1308 */       return -1; 
/* 1309 */     if (type == 344) {
/* 1310 */       return -1;
/*      */     }
/* 1312 */     return 3;
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean isP_INT(int type) {
/* 1317 */     return (typePrecedence(type) == 3);
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean rightIsStrong(int type1, int type2) {
/* 1322 */     int type1_p = typePrecedence(type1);
/* 1323 */     int type2_p = typePrecedence(type2);
/* 1324 */     return (type1_p >= 0 && type2_p >= 0 && type1_p > type2_p);
/*      */   }
/*      */   
/* 1327 */   private static final int[] castOp = new int[] { 0, 144, 143, 142, 141, 0, 140, 139, 138, 137, 0, 136, 135, 134, 133, 0 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void convertOprandTypes(int type1, int type2, Expr expr) throws CompileError {
/*      */     boolean rightStrong;
/* 1341 */     int op, result_type, type1_p = typePrecedence(type1);
/* 1342 */     int type2_p = typePrecedence(type2);
/*      */     
/* 1344 */     if (type2_p < 0 && type1_p < 0) {
/*      */       return;
/*      */     }
/* 1347 */     if (type2_p < 0 || type1_p < 0) {
/* 1348 */       badTypes(expr);
/*      */     }
/*      */     
/* 1351 */     if (type1_p <= type2_p) {
/* 1352 */       rightStrong = false;
/* 1353 */       this.exprType = type1;
/* 1354 */       op = castOp[type2_p * 4 + type1_p];
/* 1355 */       result_type = type1_p;
/*      */     } else {
/*      */       
/* 1358 */       rightStrong = true;
/* 1359 */       op = castOp[type1_p * 4 + type2_p];
/* 1360 */       result_type = type2_p;
/*      */     } 
/*      */     
/* 1363 */     if (rightStrong) {
/* 1364 */       if (result_type == 0 || result_type == 2) {
/* 1365 */         if (type1_p == 0 || type1_p == 2) {
/* 1366 */           this.bytecode.addOpcode(94);
/*      */         } else {
/* 1368 */           this.bytecode.addOpcode(93);
/*      */         } 
/* 1370 */         this.bytecode.addOpcode(88);
/* 1371 */         this.bytecode.addOpcode(op);
/* 1372 */         this.bytecode.addOpcode(94);
/* 1373 */         this.bytecode.addOpcode(88);
/*      */       }
/* 1375 */       else if (result_type == 1) {
/* 1376 */         if (type1_p == 2) {
/* 1377 */           this.bytecode.addOpcode(91);
/* 1378 */           this.bytecode.addOpcode(87);
/*      */         } else {
/*      */           
/* 1381 */           this.bytecode.addOpcode(95);
/*      */         } 
/* 1383 */         this.bytecode.addOpcode(op);
/* 1384 */         this.bytecode.addOpcode(95);
/*      */       } else {
/*      */         
/* 1387 */         fatal();
/*      */       } 
/* 1389 */     } else if (op != 0) {
/* 1390 */       this.bytecode.addOpcode(op);
/*      */     } 
/*      */   }
/*      */   public void atCastExpr(CastExpr expr) throws CompileError {
/* 1394 */     String cname = resolveClassName(expr.getClassName());
/* 1395 */     String toClass = checkCastExpr(expr, cname);
/* 1396 */     int srcType = this.exprType;
/* 1397 */     this.exprType = expr.getType();
/* 1398 */     this.arrayDim = expr.getArrayDim();
/* 1399 */     this.className = cname;
/* 1400 */     if (toClass == null) {
/* 1401 */       atNumCastExpr(srcType, this.exprType);
/*      */     } else {
/* 1403 */       this.bytecode.addCheckcast(toClass);
/*      */     } 
/*      */   }
/*      */   public void atInstanceOfExpr(InstanceOfExpr expr) throws CompileError {
/* 1407 */     String cname = resolveClassName(expr.getClassName());
/* 1408 */     String toClass = checkCastExpr((CastExpr)expr, cname);
/* 1409 */     this.bytecode.addInstanceof(toClass);
/* 1410 */     this.exprType = 301;
/* 1411 */     this.arrayDim = 0;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String checkCastExpr(CastExpr expr, String name) throws CompileError {
/* 1417 */     String msg = "invalid cast";
/* 1418 */     ASTree oprand = expr.getOprand();
/* 1419 */     int dim = expr.getArrayDim();
/* 1420 */     int type = expr.getType();
/* 1421 */     oprand.accept(this);
/* 1422 */     int srcType = this.exprType;
/* 1423 */     int srcDim = this.arrayDim;
/* 1424 */     if (invalidDim(srcType, this.arrayDim, this.className, type, dim, name, true) || srcType == 344 || type == 344)
/*      */     {
/* 1426 */       throw new CompileError("invalid cast");
/*      */     }
/* 1428 */     if (type == 307) {
/* 1429 */       if (!isRefType(srcType) && srcDim == 0) {
/* 1430 */         throw new CompileError("invalid cast");
/*      */       }
/* 1432 */       return toJvmArrayName(name, dim);
/*      */     } 
/*      */     
/* 1435 */     if (dim > 0) {
/* 1436 */       return toJvmTypeName(type, dim);
/*      */     }
/* 1438 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   void atNumCastExpr(int srcType, int destType) throws CompileError {
/*      */     int op, op2;
/* 1444 */     if (srcType == destType) {
/*      */       return;
/*      */     }
/*      */     
/* 1448 */     int stype = typePrecedence(srcType);
/* 1449 */     int dtype = typePrecedence(destType);
/* 1450 */     if (0 <= stype && stype < 3) {
/* 1451 */       op = castOp[stype * 4 + dtype];
/*      */     } else {
/* 1453 */       op = 0;
/*      */     } 
/* 1455 */     if (destType == 312) {
/* 1456 */       op2 = 135;
/* 1457 */     } else if (destType == 317) {
/* 1458 */       op2 = 134;
/* 1459 */     } else if (destType == 326) {
/* 1460 */       op2 = 133;
/* 1461 */     } else if (destType == 334) {
/* 1462 */       op2 = 147;
/* 1463 */     } else if (destType == 306) {
/* 1464 */       op2 = 146;
/* 1465 */     } else if (destType == 303) {
/* 1466 */       op2 = 145;
/*      */     } else {
/* 1468 */       op2 = 0;
/*      */     } 
/* 1470 */     if (op != 0) {
/* 1471 */       this.bytecode.addOpcode(op);
/*      */     }
/* 1473 */     if ((op == 0 || op == 136 || op == 139 || op == 142) && 
/* 1474 */       op2 != 0) {
/* 1475 */       this.bytecode.addOpcode(op2);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void atExpr(Expr expr) throws CompileError {
/* 1482 */     int token = expr.getOperator();
/* 1483 */     ASTree oprand = expr.oprand1();
/* 1484 */     if (token == 46) {
/* 1485 */       String member = ((Symbol)expr.oprand2()).get();
/* 1486 */       if (member.equals("class")) {
/* 1487 */         atClassObject(expr);
/*      */       } else {
/* 1489 */         atFieldRead((ASTree)expr);
/*      */       } 
/* 1491 */     } else if (token == 35) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1496 */       atFieldRead((ASTree)expr);
/*      */     }
/* 1498 */     else if (token == 65) {
/* 1499 */       atArrayRead(oprand, expr.oprand2());
/* 1500 */     } else if (token == 362 || token == 363) {
/* 1501 */       atPlusPlus(token, oprand, expr, true);
/* 1502 */     } else if (token == 33) {
/* 1503 */       if (!booleanExpr(false, (ASTree)expr)) {
/* 1504 */         this.bytecode.addIndex(7);
/* 1505 */         this.bytecode.addIconst(1);
/* 1506 */         this.bytecode.addOpcode(167);
/* 1507 */         this.bytecode.addIndex(4);
/*      */       } 
/*      */       
/* 1510 */       this.bytecode.addIconst(0);
/*      */     }
/* 1512 */     else if (token == 67) {
/* 1513 */       fatal();
/*      */     } else {
/* 1515 */       expr.oprand1().accept(this);
/* 1516 */       int type = typePrecedence(this.exprType);
/* 1517 */       if (this.arrayDim > 0) {
/* 1518 */         badType(expr);
/*      */       }
/* 1520 */       if (token == 45) {
/* 1521 */         if (type == 0) {
/* 1522 */           this.bytecode.addOpcode(119);
/* 1523 */         } else if (type == 1) {
/* 1524 */           this.bytecode.addOpcode(118);
/* 1525 */         } else if (type == 2) {
/* 1526 */           this.bytecode.addOpcode(117);
/* 1527 */         } else if (type == 3) {
/* 1528 */           this.bytecode.addOpcode(116);
/* 1529 */           this.exprType = 324;
/*      */         } else {
/*      */           
/* 1532 */           badType(expr);
/*      */         } 
/* 1534 */       } else if (token == 126) {
/* 1535 */         if (type == 3) {
/* 1536 */           this.bytecode.addIconst(-1);
/* 1537 */           this.bytecode.addOpcode(130);
/* 1538 */           this.exprType = 324;
/*      */         }
/* 1540 */         else if (type == 2) {
/* 1541 */           this.bytecode.addLconst(-1L);
/* 1542 */           this.bytecode.addOpcode(131);
/*      */         } else {
/*      */           
/* 1545 */           badType(expr);
/*      */         }
/*      */       
/* 1548 */       } else if (token == 43) {
/* 1549 */         if (type == -1) {
/* 1550 */           badType(expr);
/*      */         }
/*      */       }
/*      */       else {
/*      */         
/* 1555 */         fatal();
/*      */       } 
/*      */     } 
/*      */   }
/*      */   protected static void badType(Expr expr) throws CompileError {
/* 1560 */     throw new CompileError("invalid type for " + expr.getName());
/*      */   }
/*      */   
/*      */   public abstract void atCallExpr(CallExpr paramCallExpr) throws CompileError;
/*      */   
/*      */   protected abstract void atFieldRead(ASTree paramASTree) throws CompileError;
/*      */   
/*      */   public void atClassObject(Expr expr) throws CompileError {
/* 1568 */     ASTree op1 = expr.oprand1();
/* 1569 */     if (!(op1 instanceof Symbol)) {
/* 1570 */       throw new CompileError("fatal error: badly parsed .class expr");
/*      */     }
/* 1572 */     String cname = ((Symbol)op1).get();
/* 1573 */     if (cname.startsWith("[")) {
/* 1574 */       int i = cname.indexOf("[L");
/* 1575 */       if (i >= 0) {
/* 1576 */         String name = cname.substring(i + 2, cname.length() - 1);
/* 1577 */         String name2 = resolveClassName(name);
/* 1578 */         if (!name.equals(name2)) {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1583 */           name2 = MemberResolver.jvmToJavaName(name2);
/* 1584 */           StringBuffer sbuf = new StringBuffer();
/* 1585 */           while (i-- >= 0) {
/* 1586 */             sbuf.append('[');
/*      */           }
/* 1588 */           sbuf.append('L').append(name2).append(';');
/* 1589 */           cname = sbuf.toString();
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */       
/* 1594 */       cname = resolveClassName(MemberResolver.javaToJvmName(cname));
/* 1595 */       cname = MemberResolver.jvmToJavaName(cname);
/*      */     } 
/*      */     
/* 1598 */     atClassObject2(cname);
/* 1599 */     this.exprType = 307;
/* 1600 */     this.arrayDim = 0;
/* 1601 */     this.className = "java/lang/Class";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void atClassObject2(String cname) throws CompileError {
/* 1607 */     int start = this.bytecode.currentPc();
/* 1608 */     this.bytecode.addLdc(cname);
/* 1609 */     this.bytecode.addInvokestatic("java.lang.Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;");
/*      */     
/* 1611 */     int end = this.bytecode.currentPc();
/* 1612 */     this.bytecode.addOpcode(167);
/* 1613 */     int pc = this.bytecode.currentPc();
/* 1614 */     this.bytecode.addIndex(0);
/*      */     
/* 1616 */     this.bytecode.addExceptionHandler(start, end, this.bytecode.currentPc(), "java.lang.ClassNotFoundException");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1635 */     this.bytecode.growStack(1);
/* 1636 */     this.bytecode.addInvokestatic("javassist.runtime.DotClass", "fail", "(Ljava/lang/ClassNotFoundException;)Ljava/lang/NoClassDefFoundError;");
/*      */ 
/*      */     
/* 1639 */     this.bytecode.addOpcode(191);
/* 1640 */     this.bytecode.write16bit(pc, this.bytecode.currentPc() - pc + 1);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void atArrayRead(ASTree array, ASTree index) throws CompileError {
/* 1646 */     arrayAccess(array, index);
/* 1647 */     this.bytecode.addOpcode(getArrayReadOp(this.exprType, this.arrayDim));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void arrayAccess(ASTree array, ASTree index) throws CompileError {
/* 1653 */     array.accept(this);
/* 1654 */     int type = this.exprType;
/* 1655 */     int dim = this.arrayDim;
/* 1656 */     if (dim == 0) {
/* 1657 */       throw new CompileError("bad array access");
/*      */     }
/* 1659 */     String cname = this.className;
/*      */     
/* 1661 */     index.accept(this);
/* 1662 */     if (typePrecedence(this.exprType) != 3 || this.arrayDim > 0) {
/* 1663 */       throw new CompileError("bad array index");
/*      */     }
/* 1665 */     this.exprType = type;
/* 1666 */     this.arrayDim = dim - 1;
/* 1667 */     this.className = cname;
/*      */   }
/*      */   
/*      */   protected static int getArrayReadOp(int type, int dim) {
/* 1671 */     if (dim > 0) {
/* 1672 */       return 50;
/*      */     }
/* 1674 */     switch (type) {
/*      */       case 312:
/* 1676 */         return 49;
/*      */       case 317:
/* 1678 */         return 48;
/*      */       case 326:
/* 1680 */         return 47;
/*      */       case 324:
/* 1682 */         return 46;
/*      */       case 334:
/* 1684 */         return 53;
/*      */       case 306:
/* 1686 */         return 52;
/*      */       case 301:
/*      */       case 303:
/* 1689 */         return 51;
/*      */     } 
/* 1691 */     return 50;
/*      */   }
/*      */ 
/*      */   
/*      */   protected static int getArrayWriteOp(int type, int dim) {
/* 1696 */     if (dim > 0) {
/* 1697 */       return 83;
/*      */     }
/* 1699 */     switch (type) {
/*      */       case 312:
/* 1701 */         return 82;
/*      */       case 317:
/* 1703 */         return 81;
/*      */       case 326:
/* 1705 */         return 80;
/*      */       case 324:
/* 1707 */         return 79;
/*      */       case 334:
/* 1709 */         return 86;
/*      */       case 306:
/* 1711 */         return 85;
/*      */       case 301:
/*      */       case 303:
/* 1714 */         return 84;
/*      */     } 
/* 1716 */     return 83;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void atPlusPlus(int token, ASTree oprand, Expr expr, boolean doDup) throws CompileError {
/* 1723 */     boolean isPost = (oprand == null);
/* 1724 */     if (isPost) {
/* 1725 */       oprand = expr.oprand2();
/*      */     }
/* 1727 */     if (oprand instanceof Variable) {
/* 1728 */       Declarator d = ((Variable)oprand).getDeclarator();
/* 1729 */       int t = this.exprType = d.getType();
/* 1730 */       this.arrayDim = d.getArrayDim();
/* 1731 */       int var = getLocalVar(d);
/* 1732 */       if (this.arrayDim > 0) {
/* 1733 */         badType(expr);
/*      */       }
/* 1735 */       if (t == 312) {
/* 1736 */         this.bytecode.addDload(var);
/* 1737 */         if (doDup && isPost) {
/* 1738 */           this.bytecode.addOpcode(92);
/*      */         }
/* 1740 */         this.bytecode.addDconst(1.0D);
/* 1741 */         this.bytecode.addOpcode((token == 362) ? 99 : 103);
/* 1742 */         if (doDup && !isPost) {
/* 1743 */           this.bytecode.addOpcode(92);
/*      */         }
/* 1745 */         this.bytecode.addDstore(var);
/*      */       }
/* 1747 */       else if (t == 326) {
/* 1748 */         this.bytecode.addLload(var);
/* 1749 */         if (doDup && isPost) {
/* 1750 */           this.bytecode.addOpcode(92);
/*      */         }
/* 1752 */         this.bytecode.addLconst(1L);
/* 1753 */         this.bytecode.addOpcode((token == 362) ? 97 : 101);
/* 1754 */         if (doDup && !isPost) {
/* 1755 */           this.bytecode.addOpcode(92);
/*      */         }
/* 1757 */         this.bytecode.addLstore(var);
/*      */       }
/* 1759 */       else if (t == 317) {
/* 1760 */         this.bytecode.addFload(var);
/* 1761 */         if (doDup && isPost) {
/* 1762 */           this.bytecode.addOpcode(89);
/*      */         }
/* 1764 */         this.bytecode.addFconst(1.0F);
/* 1765 */         this.bytecode.addOpcode((token == 362) ? 98 : 102);
/* 1766 */         if (doDup && !isPost) {
/* 1767 */           this.bytecode.addOpcode(89);
/*      */         }
/* 1769 */         this.bytecode.addFstore(var);
/*      */       }
/* 1771 */       else if (t == 303 || t == 306 || t == 334 || t == 324) {
/* 1772 */         if (doDup && isPost) {
/* 1773 */           this.bytecode.addIload(var);
/*      */         }
/* 1775 */         int delta = (token == 362) ? 1 : -1;
/* 1776 */         if (var > 255) {
/* 1777 */           this.bytecode.addOpcode(196);
/* 1778 */           this.bytecode.addOpcode(132);
/* 1779 */           this.bytecode.addIndex(var);
/* 1780 */           this.bytecode.addIndex(delta);
/*      */         } else {
/*      */           
/* 1783 */           this.bytecode.addOpcode(132);
/* 1784 */           this.bytecode.add(var);
/* 1785 */           this.bytecode.add(delta);
/*      */         } 
/*      */         
/* 1788 */         if (doDup && !isPost) {
/* 1789 */           this.bytecode.addIload(var);
/*      */         }
/*      */       } else {
/* 1792 */         badType(expr);
/*      */       } 
/*      */     } else {
/* 1795 */       if (oprand instanceof Expr) {
/* 1796 */         Expr e = (Expr)oprand;
/* 1797 */         if (e.getOperator() == 65) {
/* 1798 */           atArrayPlusPlus(token, isPost, e, doDup);
/*      */           
/*      */           return;
/*      */         } 
/*      */       } 
/* 1803 */       atFieldPlusPlus(token, isPost, oprand, expr, doDup);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void atArrayPlusPlus(int token, boolean isPost, Expr expr, boolean doDup) throws CompileError {
/* 1810 */     arrayAccess(expr.oprand1(), expr.oprand2());
/* 1811 */     int t = this.exprType;
/* 1812 */     int dim = this.arrayDim;
/* 1813 */     if (dim > 0) {
/* 1814 */       badType(expr);
/*      */     }
/* 1816 */     this.bytecode.addOpcode(92);
/* 1817 */     this.bytecode.addOpcode(getArrayReadOp(t, this.arrayDim));
/* 1818 */     int dup_code = is2word(t, dim) ? 94 : 91;
/* 1819 */     atPlusPlusCore(dup_code, doDup, token, isPost, expr);
/* 1820 */     this.bytecode.addOpcode(getArrayWriteOp(t, dim));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void atPlusPlusCore(int dup_code, boolean doDup, int token, boolean isPost, Expr expr) throws CompileError {
/* 1827 */     int t = this.exprType;
/*      */     
/* 1829 */     if (doDup && isPost) {
/* 1830 */       this.bytecode.addOpcode(dup_code);
/*      */     }
/* 1832 */     if (t == 324 || t == 303 || t == 306 || t == 334) {
/* 1833 */       this.bytecode.addIconst(1);
/* 1834 */       this.bytecode.addOpcode((token == 362) ? 96 : 100);
/* 1835 */       this.exprType = 324;
/*      */     }
/* 1837 */     else if (t == 326) {
/* 1838 */       this.bytecode.addLconst(1L);
/* 1839 */       this.bytecode.addOpcode((token == 362) ? 97 : 101);
/*      */     }
/* 1841 */     else if (t == 317) {
/* 1842 */       this.bytecode.addFconst(1.0F);
/* 1843 */       this.bytecode.addOpcode((token == 362) ? 98 : 102);
/*      */     }
/* 1845 */     else if (t == 312) {
/* 1846 */       this.bytecode.addDconst(1.0D);
/* 1847 */       this.bytecode.addOpcode((token == 362) ? 99 : 103);
/*      */     } else {
/*      */       
/* 1850 */       badType(expr);
/*      */     } 
/* 1852 */     if (doDup && !isPost) {
/* 1853 */       this.bytecode.addOpcode(dup_code);
/*      */     }
/*      */   }
/*      */   
/*      */   protected abstract void atFieldPlusPlus(int paramInt, boolean paramBoolean1, ASTree paramASTree, Expr paramExpr, boolean paramBoolean2) throws CompileError;
/*      */   
/*      */   public abstract void atMember(Member paramMember) throws CompileError;
/*      */   
/*      */   public void atVariable(Variable v) throws CompileError {
/* 1862 */     Declarator d = v.getDeclarator();
/* 1863 */     this.exprType = d.getType();
/* 1864 */     this.arrayDim = d.getArrayDim();
/* 1865 */     this.className = d.getClassName();
/* 1866 */     int var = getLocalVar(d);
/*      */     
/* 1868 */     if (this.arrayDim > 0) {
/* 1869 */       this.bytecode.addAload(var);
/*      */     } else {
/* 1871 */       switch (this.exprType) {
/*      */         case 307:
/* 1873 */           this.bytecode.addAload(var);
/*      */           return;
/*      */         case 326:
/* 1876 */           this.bytecode.addLload(var);
/*      */           return;
/*      */         case 317:
/* 1879 */           this.bytecode.addFload(var);
/*      */           return;
/*      */         case 312:
/* 1882 */           this.bytecode.addDload(var);
/*      */           return;
/*      */       } 
/* 1885 */       this.bytecode.addIload(var);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void atKeyword(Keyword k) throws CompileError {
/* 1891 */     this.arrayDim = 0;
/* 1892 */     int token = k.get();
/* 1893 */     switch (token) {
/*      */       case 410:
/* 1895 */         this.bytecode.addIconst(1);
/* 1896 */         this.exprType = 301;
/*      */         return;
/*      */       case 411:
/* 1899 */         this.bytecode.addIconst(0);
/* 1900 */         this.exprType = 301;
/*      */         return;
/*      */       case 412:
/* 1903 */         this.bytecode.addOpcode(1);
/* 1904 */         this.exprType = 412;
/*      */         return;
/*      */       case 336:
/*      */       case 339:
/* 1908 */         if (this.inStaticMethod) {
/* 1909 */           throw new CompileError("not-available: " + ((token == 339) ? "this" : "super"));
/*      */         }
/*      */         
/* 1912 */         this.bytecode.addAload(0);
/* 1913 */         this.exprType = 307;
/* 1914 */         if (token == 339) {
/* 1915 */           this.className = getThisName();
/*      */         } else {
/* 1917 */           this.className = getSuperName();
/*      */         }  return;
/*      */     } 
/* 1920 */     fatal();
/*      */   }
/*      */ 
/*      */   
/*      */   public void atStringL(StringL s) throws CompileError {
/* 1925 */     this.exprType = 307;
/* 1926 */     this.arrayDim = 0;
/* 1927 */     this.className = "java/lang/String";
/* 1928 */     this.bytecode.addLdc(s.get());
/*      */   }
/*      */   
/*      */   public void atIntConst(IntConst i) throws CompileError {
/* 1932 */     this.arrayDim = 0;
/* 1933 */     long value = i.get();
/* 1934 */     int type = i.getType();
/* 1935 */     if (type == 402 || type == 401) {
/* 1936 */       this.exprType = (type == 402) ? 324 : 306;
/* 1937 */       this.bytecode.addIconst((int)value);
/*      */     } else {
/*      */       
/* 1940 */       this.exprType = 326;
/* 1941 */       this.bytecode.addLconst(value);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void atDoubleConst(DoubleConst d) throws CompileError {
/* 1946 */     this.arrayDim = 0;
/* 1947 */     if (d.getType() == 405) {
/* 1948 */       this.exprType = 312;
/* 1949 */       this.bytecode.addDconst(d.get());
/*      */     } else {
/*      */       
/* 1952 */       this.exprType = 317;
/* 1953 */       this.bytecode.addFconst((float)d.get());
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\CodeGen.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */