/*      */ package javassist.compiler;
/*      */ import java.util.ArrayList;
/*      */ import javassist.ClassPool;
/*      */ import javassist.CtClass;
/*      */ import javassist.CtField;
/*      */ import javassist.CtMethod;
/*      */ import javassist.Modifier;
/*      */ import javassist.bytecode.AccessFlag;
/*      */ import javassist.bytecode.Bytecode;
/*      */ import javassist.bytecode.ClassFile;
/*      */ import javassist.bytecode.ConstPool;
/*      */ import javassist.bytecode.FieldInfo;
/*      */ import javassist.bytecode.MethodInfo;
/*      */ import javassist.compiler.ast.ASTList;
/*      */ import javassist.compiler.ast.ASTree;
/*      */ import javassist.compiler.ast.ArrayInit;
/*      */ import javassist.compiler.ast.CallExpr;
/*      */ import javassist.compiler.ast.Declarator;
/*      */ import javassist.compiler.ast.Expr;
/*      */ import javassist.compiler.ast.Keyword;
/*      */ import javassist.compiler.ast.Member;
/*      */ import javassist.compiler.ast.MethodDecl;
/*      */ import javassist.compiler.ast.NewExpr;
/*      */ import javassist.compiler.ast.Pair;
/*      */ import javassist.compiler.ast.Stmnt;
/*      */ import javassist.compiler.ast.Symbol;
/*      */ 
/*      */ public class MemberCodeGen extends CodeGen {
/*      */   protected MemberResolver resolver;
/*      */   protected CtClass thisClass;
/*      */   protected MethodInfo thisMethod;
/*      */   protected boolean resultStatic;
/*      */   
/*      */   public MemberCodeGen(Bytecode b, CtClass cc, ClassPool cp) {
/*   35 */     super(b);
/*   36 */     this.resolver = new MemberResolver(cp);
/*   37 */     this.thisClass = cc;
/*   38 */     this.thisMethod = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMajorVersion() {
/*   46 */     ClassFile cf = this.thisClass.getClassFile2();
/*   47 */     if (cf == null) {
/*   48 */       return ClassFile.MAJOR_VERSION;
/*      */     }
/*   50 */     return cf.getMajorVersion();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setThisMethod(CtMethod m) {
/*   57 */     this.thisMethod = m.getMethodInfo2();
/*   58 */     if (this.typeChecker != null)
/*   59 */       this.typeChecker.setThisMethod(this.thisMethod); 
/*      */   }
/*      */   public CtClass getThisClass() {
/*   62 */     return this.thisClass;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getThisName() {
/*   68 */     return MemberResolver.javaToJvmName(this.thisClass.getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getSuperName() throws CompileError {
/*   75 */     return MemberResolver.javaToJvmName(
/*   76 */         MemberResolver.getSuperclass(this.thisClass).getName());
/*      */   }
/*      */   
/*      */   protected void insertDefaultSuperCall() throws CompileError {
/*   80 */     this.bytecode.addAload(0);
/*   81 */     this.bytecode.addInvokespecial(MemberResolver.getSuperclass(this.thisClass), "<init>", "()V");
/*      */   }
/*      */   
/*      */   static class JsrHook
/*      */     extends CodeGen.ReturnHook {
/*      */     ArrayList jsrList;
/*      */     CodeGen cgen;
/*      */     int var;
/*      */     
/*      */     JsrHook(CodeGen gen) {
/*   91 */       super(gen);
/*   92 */       this.jsrList = new ArrayList();
/*   93 */       this.cgen = gen;
/*   94 */       this.var = -1;
/*      */     }
/*      */     
/*      */     private int getVar(int size) {
/*   98 */       if (this.var < 0) {
/*   99 */         this.var = this.cgen.getMaxLocals();
/*  100 */         this.cgen.incMaxLocals(size);
/*      */       } 
/*      */       
/*  103 */       return this.var;
/*      */     }
/*      */     
/*      */     private void jsrJmp(Bytecode b) {
/*  107 */       b.addOpcode(167);
/*  108 */       this.jsrList.add(new int[] { b.currentPc(), this.var });
/*  109 */       b.addIndex(0);
/*      */     }
/*      */     
/*      */     protected boolean doit(Bytecode b, int opcode) {
/*  113 */       switch (opcode) {
/*      */         case 177:
/*  115 */           jsrJmp(b);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  146 */           return false;case 176: b.addAstore(getVar(1)); jsrJmp(b); b.addAload(this.var); return false;case 172: b.addIstore(getVar(1)); jsrJmp(b); b.addIload(this.var); return false;case 173: b.addLstore(getVar(2)); jsrJmp(b); b.addLload(this.var); return false;case 175: b.addDstore(getVar(2)); jsrJmp(b); b.addDload(this.var); return false;case 174: b.addFstore(getVar(1)); jsrJmp(b); b.addFload(this.var); return false;
/*      */       } 
/*      */       throw new RuntimeException("fatal");
/*      */     } }
/*      */   
/*      */   static class JsrHook2 extends CodeGen.ReturnHook { int var;
/*      */     int target;
/*      */     
/*      */     JsrHook2(CodeGen gen, int[] retTarget) {
/*  155 */       super(gen);
/*  156 */       this.target = retTarget[0];
/*  157 */       this.var = retTarget[1];
/*      */     }
/*      */     
/*      */     protected boolean doit(Bytecode b, int opcode) {
/*  161 */       switch (opcode)
/*      */       
/*      */       { 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case 177:
/*  183 */           b.addOpcode(167);
/*  184 */           b.addIndex(this.target - b.currentPc() + 3);
/*  185 */           return true;
/*      */         case 176: b.addAstore(this.var);
/*      */         case 172: b.addIstore(this.var);
/*      */         case 173: b.addLstore(this.var);
/*      */         case 175: b.addDstore(this.var);
/*  190 */         case 174: b.addFstore(this.var); }  throw new RuntimeException("fatal"); } } protected void atTryStmnt(Stmnt st) throws CompileError { Bytecode bc = this.bytecode;
/*  191 */     Stmnt body = (Stmnt)st.getLeft();
/*  192 */     if (body == null) {
/*      */       return;
/*      */     }
/*  195 */     ASTList catchList = (ASTList)st.getRight().getLeft();
/*  196 */     Stmnt finallyBlock = (Stmnt)st.getRight().getRight().getLeft();
/*  197 */     ArrayList<Integer> gotoList = new ArrayList();
/*      */     
/*  199 */     JsrHook jsrHook = null;
/*  200 */     if (finallyBlock != null) {
/*  201 */       jsrHook = new JsrHook(this);
/*      */     }
/*  203 */     int start = bc.currentPc();
/*  204 */     body.accept(this);
/*  205 */     int end = bc.currentPc();
/*  206 */     if (start == end) {
/*  207 */       throw new CompileError("empty try block");
/*      */     }
/*  209 */     boolean tryNotReturn = !this.hasReturned;
/*  210 */     if (tryNotReturn) {
/*  211 */       bc.addOpcode(167);
/*  212 */       gotoList.add(new Integer(bc.currentPc()));
/*  213 */       bc.addIndex(0);
/*      */     } 
/*      */     
/*  216 */     int var = getMaxLocals();
/*  217 */     incMaxLocals(1);
/*  218 */     while (catchList != null) {
/*      */       
/*  220 */       Pair p = (Pair)catchList.head();
/*  221 */       catchList = catchList.tail();
/*  222 */       Declarator decl = (Declarator)p.getLeft();
/*  223 */       Stmnt block = (Stmnt)p.getRight();
/*      */       
/*  225 */       decl.setLocalVar(var);
/*      */       
/*  227 */       CtClass type = this.resolver.lookupClassByJvmName(decl.getClassName());
/*  228 */       decl.setClassName(MemberResolver.javaToJvmName(type.getName()));
/*  229 */       bc.addExceptionHandler(start, end, bc.currentPc(), type);
/*  230 */       bc.growStack(1);
/*  231 */       bc.addAstore(var);
/*  232 */       this.hasReturned = false;
/*  233 */       if (block != null) {
/*  234 */         block.accept(this);
/*      */       }
/*  236 */       if (!this.hasReturned) {
/*  237 */         bc.addOpcode(167);
/*  238 */         gotoList.add(new Integer(bc.currentPc()));
/*  239 */         bc.addIndex(0);
/*  240 */         tryNotReturn = true;
/*      */       } 
/*      */     } 
/*      */     
/*  244 */     if (finallyBlock != null) {
/*  245 */       jsrHook.remove(this);
/*      */       
/*  247 */       int pcAnyCatch = bc.currentPc();
/*  248 */       bc.addExceptionHandler(start, pcAnyCatch, pcAnyCatch, 0);
/*  249 */       bc.growStack(1);
/*  250 */       bc.addAstore(var);
/*  251 */       this.hasReturned = false;
/*  252 */       finallyBlock.accept(this);
/*  253 */       if (!this.hasReturned) {
/*  254 */         bc.addAload(var);
/*  255 */         bc.addOpcode(191);
/*      */       } 
/*      */       
/*  258 */       addFinally(jsrHook.jsrList, finallyBlock);
/*      */     } 
/*      */     
/*  261 */     int pcEnd = bc.currentPc();
/*  262 */     patchGoto(gotoList, pcEnd);
/*  263 */     this.hasReturned = !tryNotReturn;
/*  264 */     if (finallyBlock != null && 
/*  265 */       tryNotReturn) {
/*  266 */       finallyBlock.accept(this);
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addFinally(ArrayList<int[]> returnList, Stmnt finallyBlock) throws CompileError {
/*  276 */     Bytecode bc = this.bytecode;
/*  277 */     int n = returnList.size();
/*  278 */     for (int i = 0; i < n; i++) {
/*  279 */       int[] ret = returnList.get(i);
/*  280 */       int pc = ret[0];
/*  281 */       bc.write16bit(pc, bc.currentPc() - pc + 1);
/*  282 */       CodeGen.ReturnHook hook = new JsrHook2(this, ret);
/*  283 */       finallyBlock.accept(this);
/*  284 */       hook.remove(this);
/*  285 */       if (!this.hasReturned) {
/*  286 */         bc.addOpcode(167);
/*  287 */         bc.addIndex(pc + 3 - bc.currentPc());
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void atNewExpr(NewExpr expr) throws CompileError {
/*  293 */     if (expr.isArray()) {
/*  294 */       atNewArrayExpr(expr);
/*      */     } else {
/*  296 */       CtClass clazz = this.resolver.lookupClassByName(expr.getClassName());
/*  297 */       String cname = clazz.getName();
/*  298 */       ASTList args = expr.getArguments();
/*  299 */       this.bytecode.addNew(cname);
/*  300 */       this.bytecode.addOpcode(89);
/*      */       
/*  302 */       atMethodCallCore(clazz, "<init>", args, false, true, -1, (MemberResolver.Method)null);
/*      */ 
/*      */       
/*  305 */       this.exprType = 307;
/*  306 */       this.arrayDim = 0;
/*  307 */       this.className = MemberResolver.javaToJvmName(cname);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void atNewArrayExpr(NewExpr expr) throws CompileError {
/*  312 */     int type = expr.getArrayType();
/*  313 */     ASTList size = expr.getArraySize();
/*  314 */     ASTList classname = expr.getClassName();
/*  315 */     ArrayInit init = expr.getInitializer();
/*  316 */     if (size.length() > 1) {
/*  317 */       if (init != null) {
/*  318 */         throw new CompileError("sorry, multi-dimensional array initializer for new is not supported");
/*      */       }
/*      */ 
/*      */       
/*  322 */       atMultiNewArray(type, classname, size);
/*      */       
/*      */       return;
/*      */     } 
/*  326 */     ASTree sizeExpr = size.head();
/*  327 */     atNewArrayExpr2(type, sizeExpr, Declarator.astToClassName(classname, '/'), init);
/*      */   }
/*      */   
/*      */   private void atNewArrayExpr2(int type, ASTree sizeExpr, String jvmClassname, ArrayInit init) throws CompileError {
/*      */     String elementClass;
/*  332 */     if (init == null) {
/*  333 */       if (sizeExpr == null) {
/*  334 */         throw new CompileError("no array size");
/*      */       }
/*  336 */       sizeExpr.accept(this);
/*      */     }
/*  338 */     else if (sizeExpr == null) {
/*  339 */       int s = init.length();
/*  340 */       this.bytecode.addIconst(s);
/*      */     } else {
/*      */       
/*  343 */       throw new CompileError("unnecessary array size specified for new");
/*      */     } 
/*      */     
/*  346 */     if (type == 307) {
/*  347 */       elementClass = resolveClassName(jvmClassname);
/*  348 */       this.bytecode.addAnewarray(MemberResolver.jvmToJavaName(elementClass));
/*      */     } else {
/*      */       
/*  351 */       elementClass = null;
/*  352 */       int atype = 0;
/*  353 */       switch (type) {
/*      */         case 301:
/*  355 */           atype = 4;
/*      */           break;
/*      */         case 306:
/*  358 */           atype = 5;
/*      */           break;
/*      */         case 317:
/*  361 */           atype = 6;
/*      */           break;
/*      */         case 312:
/*  364 */           atype = 7;
/*      */           break;
/*      */         case 303:
/*  367 */           atype = 8;
/*      */           break;
/*      */         case 334:
/*  370 */           atype = 9;
/*      */           break;
/*      */         case 324:
/*  373 */           atype = 10;
/*      */           break;
/*      */         case 326:
/*  376 */           atype = 11;
/*      */           break;
/*      */         default:
/*  379 */           badNewExpr();
/*      */           break;
/*      */       } 
/*      */       
/*  383 */       this.bytecode.addOpcode(188);
/*  384 */       this.bytecode.add(atype);
/*      */     } 
/*      */     
/*  387 */     if (init != null) {
/*  388 */       int s = init.length();
/*  389 */       ArrayInit arrayInit = init;
/*  390 */       for (int i = 0; i < s; i++) {
/*  391 */         this.bytecode.addOpcode(89);
/*  392 */         this.bytecode.addIconst(i);
/*  393 */         arrayInit.head().accept(this);
/*  394 */         if (!isRefType(type)) {
/*  395 */           atNumCastExpr(this.exprType, type);
/*      */         }
/*  397 */         this.bytecode.addOpcode(getArrayWriteOp(type, 0));
/*  398 */         ASTList aSTList = arrayInit.tail();
/*      */       } 
/*      */     } 
/*      */     
/*  402 */     this.exprType = type;
/*  403 */     this.arrayDim = 1;
/*  404 */     this.className = elementClass;
/*      */   }
/*      */   
/*      */   private static void badNewExpr() throws CompileError {
/*  408 */     throw new CompileError("bad new expression");
/*      */   }
/*      */ 
/*      */   
/*      */   protected void atArrayVariableAssign(ArrayInit init, int varType, int varArray, String varClass) throws CompileError {
/*  413 */     atNewArrayExpr2(varType, (ASTree)null, varClass, init);
/*      */   }
/*      */   
/*      */   public void atArrayInit(ArrayInit init) throws CompileError {
/*  417 */     throw new CompileError("array initializer is not supported");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void atMultiNewArray(int type, ASTList classname, ASTList size) throws CompileError {
/*      */     String desc;
/*  424 */     int dim = size.length(); int count;
/*  425 */     for (count = 0; size != null; size = size.tail()) {
/*  426 */       ASTree s = size.head();
/*  427 */       if (s == null) {
/*      */         break;
/*      */       }
/*  430 */       count++;
/*  431 */       s.accept(this);
/*  432 */       if (this.exprType != 324) {
/*  433 */         throw new CompileError("bad type for array size");
/*      */       }
/*      */     } 
/*      */     
/*  437 */     this.exprType = type;
/*  438 */     this.arrayDim = dim;
/*  439 */     if (type == 307) {
/*  440 */       this.className = resolveClassName(classname);
/*  441 */       desc = toJvmArrayName(this.className, dim);
/*      */     } else {
/*      */       
/*  444 */       desc = toJvmTypeName(type, dim);
/*      */     } 
/*  446 */     this.bytecode.addMultiNewarray(desc, count);
/*      */   }
/*      */   
/*      */   public void atCallExpr(CallExpr expr) throws CompileError {
/*  450 */     String mname = null;
/*  451 */     CtClass targetClass = null;
/*  452 */     ASTree method = expr.oprand1();
/*  453 */     ASTList args = (ASTList)expr.oprand2();
/*  454 */     boolean isStatic = false;
/*  455 */     boolean isSpecial = false;
/*  456 */     int aload0pos = -1;
/*      */     
/*  458 */     MemberResolver.Method cached = expr.getMethod();
/*  459 */     if (method instanceof Member) {
/*  460 */       mname = ((Member)method).get();
/*  461 */       targetClass = this.thisClass;
/*  462 */       if (this.inStaticMethod || (cached != null && cached.isStatic())) {
/*  463 */         isStatic = true;
/*      */       } else {
/*  465 */         aload0pos = this.bytecode.currentPc();
/*  466 */         this.bytecode.addAload(0);
/*      */       }
/*      */     
/*  469 */     } else if (method instanceof Keyword) {
/*  470 */       isSpecial = true;
/*  471 */       mname = "<init>";
/*  472 */       targetClass = this.thisClass;
/*  473 */       if (this.inStaticMethod) {
/*  474 */         throw new CompileError("a constructor cannot be static");
/*      */       }
/*  476 */       this.bytecode.addAload(0);
/*      */       
/*  478 */       if (((Keyword)method).get() == 336) {
/*  479 */         targetClass = MemberResolver.getSuperclass(targetClass);
/*      */       }
/*  481 */     } else if (method instanceof Expr) {
/*  482 */       Expr e = (Expr)method;
/*  483 */       mname = ((Symbol)e.oprand2()).get();
/*  484 */       int op = e.getOperator();
/*  485 */       if (op == 35) {
/*      */         
/*  487 */         targetClass = this.resolver.lookupClass(((Symbol)e.oprand1()).get(), false);
/*  488 */         isStatic = true;
/*      */       }
/*  490 */       else if (op == 46) {
/*  491 */         ASTree target = e.oprand1();
/*  492 */         String classFollowedByDotSuper = TypeChecker.isDotSuper(target);
/*  493 */         if (classFollowedByDotSuper != null) {
/*  494 */           isSpecial = true;
/*  495 */           targetClass = MemberResolver.getSuperInterface(this.thisClass, classFollowedByDotSuper);
/*      */           
/*  497 */           if (this.inStaticMethod || (cached != null && cached.isStatic())) {
/*  498 */             isStatic = true;
/*      */           } else {
/*  500 */             aload0pos = this.bytecode.currentPc();
/*  501 */             this.bytecode.addAload(0);
/*      */           } 
/*      */         } else {
/*      */           
/*  505 */           if (target instanceof Keyword && (
/*  506 */             (Keyword)target).get() == 336) {
/*  507 */             isSpecial = true;
/*      */           }
/*      */           try {
/*  510 */             target.accept(this);
/*      */           }
/*  512 */           catch (NoFieldException nfe) {
/*  513 */             if (nfe.getExpr() != target) {
/*  514 */               throw nfe;
/*      */             }
/*      */             
/*  517 */             this.exprType = 307;
/*  518 */             this.arrayDim = 0;
/*  519 */             this.className = nfe.getField();
/*  520 */             isStatic = true;
/*      */           } 
/*      */           
/*  523 */           if (this.arrayDim > 0) {
/*  524 */             targetClass = this.resolver.lookupClass("java.lang.Object", true);
/*  525 */           } else if (this.exprType == 307) {
/*  526 */             targetClass = this.resolver.lookupClassByJvmName(this.className);
/*      */           } else {
/*  528 */             badMethod();
/*      */           } 
/*      */         } 
/*      */       } else {
/*  532 */         badMethod();
/*      */       } 
/*      */     } else {
/*  535 */       fatal();
/*      */     } 
/*  537 */     atMethodCallCore(targetClass, mname, args, isStatic, isSpecial, aload0pos, cached);
/*      */   }
/*      */ 
/*      */   
/*      */   private static void badMethod() throws CompileError {
/*  542 */     throw new CompileError("bad method");
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
/*      */   public void atMethodCallCore(CtClass targetClass, String mname, ASTList args, boolean isStatic, boolean isSpecial, int aload0pos, MemberResolver.Method found) throws CompileError {
/*  556 */     int nargs = getMethodArgsLength(args);
/*  557 */     int[] types = new int[nargs];
/*  558 */     int[] dims = new int[nargs];
/*  559 */     String[] cnames = new String[nargs];
/*      */     
/*  561 */     if (!isStatic && found != null && found.isStatic()) {
/*  562 */       this.bytecode.addOpcode(87);
/*  563 */       isStatic = true;
/*      */     } 
/*      */     
/*  566 */     int stack = this.bytecode.getStackDepth();
/*      */ 
/*      */     
/*  569 */     atMethodArgs(args, types, dims, cnames);
/*      */     
/*  571 */     if (found == null) {
/*  572 */       found = this.resolver.lookupMethod(targetClass, this.thisClass, this.thisMethod, mname, types, dims, cnames);
/*      */     }
/*      */     
/*  575 */     if (found == null) {
/*      */       String msg;
/*  577 */       if (mname.equals("<init>")) {
/*  578 */         msg = "constructor not found";
/*      */       } else {
/*      */         
/*  581 */         msg = "Method " + mname + " not found in " + targetClass.getName();
/*      */       } 
/*  583 */       throw new CompileError(msg);
/*      */     } 
/*      */     
/*  586 */     atMethodCallCore2(targetClass, mname, isStatic, isSpecial, aload0pos, found);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void atMethodCallCore2(CtClass targetClass, String mname, boolean isStatic, boolean isSpecial, int aload0pos, MemberResolver.Method found) throws CompileError {
/*  596 */     CtClass declClass = found.declaring;
/*  597 */     MethodInfo minfo = found.info;
/*  598 */     String desc = minfo.getDescriptor();
/*  599 */     int acc = minfo.getAccessFlags();
/*      */     
/*  601 */     if (mname.equals("<init>")) {
/*  602 */       isSpecial = true;
/*  603 */       if (declClass != targetClass) {
/*  604 */         throw new CompileError("no such constructor: " + targetClass.getName());
/*      */       }
/*  606 */       if (declClass != this.thisClass && AccessFlag.isPrivate(acc)) {
/*  607 */         desc = getAccessibleConstructor(desc, declClass, minfo);
/*  608 */         this.bytecode.addOpcode(1);
/*      */       }
/*      */     
/*  611 */     } else if (AccessFlag.isPrivate(acc)) {
/*  612 */       if (declClass == this.thisClass) {
/*  613 */         isSpecial = true;
/*      */       } else {
/*  615 */         isSpecial = false;
/*  616 */         isStatic = true;
/*  617 */         String origDesc = desc;
/*  618 */         if ((acc & 0x8) == 0) {
/*  619 */           desc = Descriptor.insertParameter(declClass.getName(), origDesc);
/*      */         }
/*      */         
/*  622 */         acc = AccessFlag.setPackage(acc) | 0x8;
/*  623 */         mname = getAccessiblePrivate(mname, origDesc, desc, minfo, declClass);
/*      */       } 
/*      */     } 
/*      */     
/*  627 */     boolean popTarget = false;
/*  628 */     if ((acc & 0x8) != 0) {
/*  629 */       if (!isStatic) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  635 */         isStatic = true;
/*  636 */         if (aload0pos >= 0) {
/*  637 */           this.bytecode.write(aload0pos, 0);
/*      */         } else {
/*  639 */           popTarget = true;
/*      */         } 
/*      */       } 
/*  642 */       this.bytecode.addInvokestatic(declClass, mname, desc);
/*      */     }
/*  644 */     else if (isSpecial) {
/*  645 */       this.bytecode.addInvokespecial(targetClass, mname, desc);
/*      */     } else {
/*  647 */       if (!Modifier.isPublic(declClass.getModifiers()) || declClass
/*  648 */         .isInterface() != targetClass.isInterface()) {
/*  649 */         declClass = targetClass;
/*      */       }
/*  651 */       if (declClass.isInterface()) {
/*  652 */         int nargs = Descriptor.paramSize(desc) + 1;
/*  653 */         this.bytecode.addInvokeinterface(declClass, mname, desc, nargs);
/*      */       } else {
/*      */         
/*  656 */         if (isStatic) {
/*  657 */           throw new CompileError(mname + " is not static");
/*      */         }
/*  659 */         this.bytecode.addInvokevirtual(declClass, mname, desc);
/*      */       } 
/*      */     } 
/*  662 */     setReturnType(desc, isStatic, popTarget);
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
/*      */   protected String getAccessiblePrivate(String methodName, String desc, String newDesc, MethodInfo minfo, CtClass declClass) throws CompileError {
/*  677 */     if (isEnclosing(declClass, this.thisClass)) {
/*  678 */       AccessorMaker maker = declClass.getAccessorMaker();
/*  679 */       if (maker != null) {
/*  680 */         return maker.getMethodAccessor(methodName, desc, newDesc, minfo);
/*      */       }
/*      */     } 
/*      */     
/*  684 */     throw new CompileError("Method " + methodName + " is private");
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
/*      */   protected String getAccessibleConstructor(String desc, CtClass declClass, MethodInfo minfo) throws CompileError {
/*  701 */     if (isEnclosing(declClass, this.thisClass)) {
/*  702 */       AccessorMaker maker = declClass.getAccessorMaker();
/*  703 */       if (maker != null) {
/*  704 */         return maker.getConstructor(declClass, desc, minfo);
/*      */       }
/*      */     } 
/*  707 */     throw new CompileError("the called constructor is private in " + declClass
/*  708 */         .getName());
/*      */   }
/*      */   
/*      */   private boolean isEnclosing(CtClass outer, CtClass inner) {
/*      */     try {
/*  713 */       while (inner != null) {
/*  714 */         inner = inner.getDeclaringClass();
/*  715 */         if (inner == outer) {
/*  716 */           return true;
/*      */         }
/*      */       } 
/*  719 */     } catch (NotFoundException notFoundException) {}
/*  720 */     return false;
/*      */   }
/*      */   
/*      */   public int getMethodArgsLength(ASTList args) {
/*  724 */     return ASTList.length(args);
/*      */   }
/*      */ 
/*      */   
/*      */   public void atMethodArgs(ASTList args, int[] types, int[] dims, String[] cnames) throws CompileError {
/*  729 */     int i = 0;
/*  730 */     while (args != null) {
/*  731 */       ASTree a = args.head();
/*  732 */       a.accept(this);
/*  733 */       types[i] = this.exprType;
/*  734 */       dims[i] = this.arrayDim;
/*  735 */       cnames[i] = this.className;
/*  736 */       i++;
/*  737 */       args = args.tail();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void setReturnType(String desc, boolean isStatic, boolean popTarget) throws CompileError {
/*  744 */     int i = desc.indexOf(')');
/*  745 */     if (i < 0) {
/*  746 */       badMethod();
/*      */     }
/*  748 */     char c = desc.charAt(++i);
/*  749 */     int dim = 0;
/*  750 */     while (c == '[') {
/*  751 */       dim++;
/*  752 */       c = desc.charAt(++i);
/*      */     } 
/*      */     
/*  755 */     this.arrayDim = dim;
/*  756 */     if (c == 'L') {
/*  757 */       int j = desc.indexOf(';', i + 1);
/*  758 */       if (j < 0) {
/*  759 */         badMethod();
/*      */       }
/*  761 */       this.exprType = 307;
/*  762 */       this.className = desc.substring(i + 1, j);
/*      */     } else {
/*      */       
/*  765 */       this.exprType = MemberResolver.descToType(c);
/*  766 */       this.className = null;
/*      */     } 
/*      */     
/*  769 */     int etype = this.exprType;
/*  770 */     if (isStatic && 
/*  771 */       popTarget) {
/*  772 */       if (is2word(etype, dim)) {
/*  773 */         this.bytecode.addOpcode(93);
/*  774 */         this.bytecode.addOpcode(88);
/*  775 */         this.bytecode.addOpcode(87);
/*      */       }
/*  777 */       else if (etype == 344) {
/*  778 */         this.bytecode.addOpcode(87);
/*      */       } else {
/*  780 */         this.bytecode.addOpcode(95);
/*  781 */         this.bytecode.addOpcode(87);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void atFieldAssign(Expr expr, int op, ASTree left, ASTree right, boolean doDup) throws CompileError {
/*      */     int fi;
/*  790 */     CtField f = fieldAccess(left, false);
/*  791 */     boolean is_static = this.resultStatic;
/*  792 */     if (op != 61 && !is_static) {
/*  793 */       this.bytecode.addOpcode(89);
/*      */     }
/*      */     
/*  796 */     if (op == 61) {
/*  797 */       FieldInfo finfo = f.getFieldInfo2();
/*  798 */       setFieldType(finfo);
/*  799 */       AccessorMaker maker = isAccessibleField(f, finfo);
/*  800 */       if (maker == null) {
/*  801 */         fi = addFieldrefInfo(f, finfo);
/*      */       } else {
/*  803 */         fi = 0;
/*      */       } 
/*      */     } else {
/*  806 */       fi = atFieldRead(f, is_static);
/*      */     } 
/*  808 */     int fType = this.exprType;
/*  809 */     int fDim = this.arrayDim;
/*  810 */     String cname = this.className;
/*      */     
/*  812 */     atAssignCore(expr, op, right, fType, fDim, cname);
/*      */     
/*  814 */     boolean is2w = is2word(fType, fDim);
/*  815 */     if (doDup) {
/*      */       int dup_code;
/*  817 */       if (is_static) {
/*  818 */         dup_code = is2w ? 92 : 89;
/*      */       } else {
/*  820 */         dup_code = is2w ? 93 : 90;
/*      */       } 
/*  822 */       this.bytecode.addOpcode(dup_code);
/*      */     } 
/*      */     
/*  825 */     atFieldAssignCore(f, is_static, fi, is2w);
/*      */     
/*  827 */     this.exprType = fType;
/*  828 */     this.arrayDim = fDim;
/*  829 */     this.className = cname;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void atFieldAssignCore(CtField f, boolean is_static, int fi, boolean is2byte) throws CompileError {
/*  836 */     if (fi != 0) {
/*  837 */       if (is_static) {
/*  838 */         this.bytecode.add(179);
/*  839 */         this.bytecode.growStack(is2byte ? -2 : -1);
/*      */       } else {
/*      */         
/*  842 */         this.bytecode.add(181);
/*  843 */         this.bytecode.growStack(is2byte ? -3 : -2);
/*      */       } 
/*      */       
/*  846 */       this.bytecode.addIndex(fi);
/*      */     } else {
/*      */       
/*  849 */       CtClass declClass = f.getDeclaringClass();
/*  850 */       AccessorMaker maker = declClass.getAccessorMaker();
/*      */       
/*  852 */       FieldInfo finfo = f.getFieldInfo2();
/*  853 */       MethodInfo minfo = maker.getFieldSetter(finfo, is_static);
/*  854 */       this.bytecode.addInvokestatic(declClass, minfo.getName(), minfo
/*  855 */           .getDescriptor());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void atMember(Member mem) throws CompileError {
/*  862 */     atFieldRead((ASTree)mem);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void atFieldRead(ASTree expr) throws CompileError {
/*  867 */     CtField f = fieldAccess(expr, true);
/*  868 */     if (f == null) {
/*  869 */       atArrayLength(expr);
/*      */       
/*      */       return;
/*      */     } 
/*  873 */     boolean is_static = this.resultStatic;
/*  874 */     ASTree cexpr = TypeChecker.getConstantFieldValue(f);
/*  875 */     if (cexpr == null) {
/*  876 */       atFieldRead(f, is_static);
/*      */     } else {
/*  878 */       cexpr.accept(this);
/*  879 */       setFieldType(f.getFieldInfo2());
/*      */     } 
/*      */   }
/*      */   
/*      */   private void atArrayLength(ASTree expr) throws CompileError {
/*  884 */     if (this.arrayDim == 0) {
/*  885 */       throw new CompileError(".length applied to a non array");
/*      */     }
/*  887 */     this.bytecode.addOpcode(190);
/*  888 */     this.exprType = 324;
/*  889 */     this.arrayDim = 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int atFieldRead(CtField f, boolean isStatic) throws CompileError {
/*  898 */     FieldInfo finfo = f.getFieldInfo2();
/*  899 */     boolean is2byte = setFieldType(finfo);
/*  900 */     AccessorMaker maker = isAccessibleField(f, finfo);
/*  901 */     if (maker != null) {
/*  902 */       MethodInfo minfo = maker.getFieldGetter(finfo, isStatic);
/*  903 */       this.bytecode.addInvokestatic(f.getDeclaringClass(), minfo.getName(), minfo
/*  904 */           .getDescriptor());
/*  905 */       return 0;
/*      */     } 
/*      */     
/*  908 */     int fi = addFieldrefInfo(f, finfo);
/*  909 */     if (isStatic) {
/*  910 */       this.bytecode.add(178);
/*  911 */       this.bytecode.growStack(is2byte ? 2 : 1);
/*      */     } else {
/*      */       
/*  914 */       this.bytecode.add(180);
/*  915 */       this.bytecode.growStack(is2byte ? 1 : 0);
/*      */     } 
/*      */     
/*  918 */     this.bytecode.addIndex(fi);
/*  919 */     return fi;
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
/*      */   private AccessorMaker isAccessibleField(CtField f, FieldInfo finfo) throws CompileError {
/*  931 */     if (AccessFlag.isPrivate(finfo.getAccessFlags()) && f
/*  932 */       .getDeclaringClass() != this.thisClass) {
/*  933 */       CtClass declClass = f.getDeclaringClass();
/*  934 */       if (isEnclosing(declClass, this.thisClass)) {
/*  935 */         AccessorMaker maker = declClass.getAccessorMaker();
/*  936 */         if (maker != null) {
/*  937 */           return maker;
/*      */         }
/*  939 */         throw new CompileError("fatal error.  bug?");
/*      */       } 
/*      */       
/*  942 */       throw new CompileError("Field " + f.getName() + " in " + declClass
/*  943 */           .getName() + " is private.");
/*      */     } 
/*      */     
/*  946 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean setFieldType(FieldInfo finfo) throws CompileError {
/*  955 */     String type = finfo.getDescriptor();
/*      */     
/*  957 */     int i = 0;
/*  958 */     int dim = 0;
/*  959 */     char c = type.charAt(i);
/*  960 */     while (c == '[') {
/*  961 */       dim++;
/*  962 */       c = type.charAt(++i);
/*      */     } 
/*      */     
/*  965 */     this.arrayDim = dim;
/*  966 */     this.exprType = MemberResolver.descToType(c);
/*      */     
/*  968 */     if (c == 'L') {
/*  969 */       this.className = type.substring(i + 1, type.indexOf(';', i + 1));
/*      */     } else {
/*  971 */       this.className = null;
/*      */     } 
/*  973 */     boolean is2byte = (dim == 0 && (c == 'J' || c == 'D'));
/*  974 */     return is2byte;
/*      */   }
/*      */   
/*      */   private int addFieldrefInfo(CtField f, FieldInfo finfo) {
/*  978 */     ConstPool cp = this.bytecode.getConstPool();
/*  979 */     String cname = f.getDeclaringClass().getName();
/*  980 */     int ci = cp.addClassInfo(cname);
/*  981 */     String name = finfo.getName();
/*  982 */     String type = finfo.getDescriptor();
/*  983 */     return cp.addFieldrefInfo(ci, name, type);
/*      */   }
/*      */   
/*      */   protected void atClassObject2(String cname) throws CompileError {
/*  987 */     if (getMajorVersion() < 49) {
/*  988 */       super.atClassObject2(cname);
/*      */     } else {
/*  990 */       this.bytecode.addLdc(this.bytecode.getConstPool().addClassInfo(cname));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void atFieldPlusPlus(int token, boolean isPost, ASTree oprand, Expr expr, boolean doDup) throws CompileError {
/*      */     int dup_code;
/*  997 */     CtField f = fieldAccess(oprand, false);
/*  998 */     boolean is_static = this.resultStatic;
/*  999 */     if (!is_static) {
/* 1000 */       this.bytecode.addOpcode(89);
/*      */     }
/* 1002 */     int fi = atFieldRead(f, is_static);
/* 1003 */     int t = this.exprType;
/* 1004 */     boolean is2w = is2word(t, this.arrayDim);
/*      */ 
/*      */     
/* 1007 */     if (is_static) {
/* 1008 */       dup_code = is2w ? 92 : 89;
/*      */     } else {
/* 1010 */       dup_code = is2w ? 93 : 90;
/*      */     } 
/* 1012 */     atPlusPlusCore(dup_code, doDup, token, isPost, expr);
/* 1013 */     atFieldAssignCore(f, is_static, fi, is2w);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected CtField fieldAccess(ASTree expr, boolean acceptLength) throws CompileError {
/* 1023 */     if (expr instanceof Member) {
/* 1024 */       String name = ((Member)expr).get();
/* 1025 */       CtField f = null;
/*      */       try {
/* 1027 */         f = this.thisClass.getField(name);
/*      */       }
/* 1029 */       catch (NotFoundException e) {
/*      */         
/* 1031 */         throw new NoFieldException(name, expr);
/*      */       } 
/*      */       
/* 1034 */       boolean is_static = Modifier.isStatic(f.getModifiers());
/* 1035 */       if (!is_static) {
/* 1036 */         if (this.inStaticMethod) {
/* 1037 */           throw new CompileError("not available in a static method: " + name);
/*      */         }
/*      */         
/* 1040 */         this.bytecode.addAload(0);
/*      */       } 
/* 1042 */       this.resultStatic = is_static;
/* 1043 */       return f;
/*      */     } 
/* 1045 */     if (expr instanceof Expr) {
/* 1046 */       Expr e = (Expr)expr;
/* 1047 */       int op = e.getOperator();
/* 1048 */       if (op == 35) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1053 */         CtField f = this.resolver.lookupField(((Symbol)e.oprand1()).get(), (Symbol)e
/* 1054 */             .oprand2());
/* 1055 */         this.resultStatic = true;
/* 1056 */         return f;
/*      */       } 
/* 1058 */       if (op == 46) {
/* 1059 */         CtField f = null;
/*      */         try {
/* 1061 */           e.oprand1().accept(this);
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1066 */           if (this.exprType == 307 && this.arrayDim == 0)
/* 1067 */           { f = this.resolver.lookupFieldByJvmName(this.className, (Symbol)e
/* 1068 */                 .oprand2()); }
/* 1069 */           else { if (acceptLength && this.arrayDim > 0 && ((Symbol)e
/* 1070 */               .oprand2()).get().equals("length")) {
/* 1071 */               return null;
/*      */             }
/* 1073 */             badLvalue(); }
/*      */           
/* 1075 */           boolean is_static = Modifier.isStatic(f.getModifiers());
/* 1076 */           if (is_static) {
/* 1077 */             this.bytecode.addOpcode(87);
/*      */           }
/* 1079 */           this.resultStatic = is_static;
/* 1080 */           return f;
/*      */         }
/* 1082 */         catch (NoFieldException nfe) {
/* 1083 */           if (nfe.getExpr() != e.oprand1()) {
/* 1084 */             throw nfe;
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1090 */           Symbol fname = (Symbol)e.oprand2();
/* 1091 */           String cname = nfe.getField();
/* 1092 */           f = this.resolver.lookupFieldByJvmName2(cname, fname, expr);
/* 1093 */           this.resultStatic = true;
/* 1094 */           return f;
/*      */         } 
/*      */       } 
/*      */       
/* 1098 */       badLvalue();
/*      */     } else {
/*      */       
/* 1101 */       badLvalue();
/*      */     } 
/* 1103 */     this.resultStatic = false;
/* 1104 */     return null;
/*      */   }
/*      */   
/*      */   private static void badLvalue() throws CompileError {
/* 1108 */     throw new CompileError("bad l-value");
/*      */   }
/*      */   
/*      */   public CtClass[] makeParamList(MethodDecl md) throws CompileError {
/*      */     CtClass[] params;
/* 1113 */     ASTList plist = md.getParams();
/* 1114 */     if (plist == null) {
/* 1115 */       params = new CtClass[0];
/*      */     } else {
/* 1117 */       int i = 0;
/* 1118 */       params = new CtClass[plist.length()];
/* 1119 */       while (plist != null) {
/* 1120 */         params[i++] = this.resolver.lookupClass((Declarator)plist.head());
/* 1121 */         plist = plist.tail();
/*      */       } 
/*      */     } 
/*      */     
/* 1125 */     return params;
/*      */   }
/*      */ 
/*      */   
/*      */   public CtClass[] makeThrowsList(MethodDecl md) throws CompileError {
/* 1130 */     ASTList list = md.getThrows();
/* 1131 */     if (list == null) {
/* 1132 */       return null;
/*      */     }
/* 1134 */     int i = 0;
/* 1135 */     CtClass[] clist = new CtClass[list.length()];
/* 1136 */     while (list != null) {
/* 1137 */       clist[i++] = this.resolver.lookupClassByName((ASTList)list.head());
/* 1138 */       list = list.tail();
/*      */     } 
/*      */     
/* 1141 */     return clist;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String resolveClassName(ASTList name) throws CompileError {
/* 1151 */     return this.resolver.resolveClassName(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String resolveClassName(String jvmName) throws CompileError {
/* 1158 */     return this.resolver.resolveJvmClassName(jvmName);
/*      */   }
/*      */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\MemberCodeGen.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */