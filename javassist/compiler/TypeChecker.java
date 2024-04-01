/*      */ package javassist.compiler;
/*      */ 
/*      */ import javassist.ClassPool;
/*      */ import javassist.CtClass;
/*      */ import javassist.CtField;
/*      */ import javassist.Modifier;
/*      */ import javassist.NotFoundException;
/*      */ import javassist.bytecode.FieldInfo;
/*      */ import javassist.bytecode.MethodInfo;
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
/*      */ import javassist.compiler.ast.InstanceOfExpr;
/*      */ import javassist.compiler.ast.IntConst;
/*      */ import javassist.compiler.ast.Keyword;
/*      */ import javassist.compiler.ast.Member;
/*      */ import javassist.compiler.ast.NewExpr;
/*      */ import javassist.compiler.ast.StringL;
/*      */ import javassist.compiler.ast.Symbol;
/*      */ import javassist.compiler.ast.Variable;
/*      */ import javassist.compiler.ast.Visitor;
/*      */ 
/*      */ public class TypeChecker extends Visitor implements Opcode, TokenId {
/*      */   static final String javaLangObject = "java.lang.Object";
/*      */   static final String jvmJavaLangObject = "java/lang/Object";
/*      */   static final String jvmJavaLangString = "java/lang/String";
/*      */   static final String jvmJavaLangClass = "java/lang/Class";
/*      */   protected int exprType;
/*      */   protected int arrayDim;
/*      */   protected String className;
/*      */   protected MemberResolver resolver;
/*      */   protected CtClass thisClass;
/*      */   protected MethodInfo thisMethod;
/*      */   
/*      */   public TypeChecker(CtClass cc, ClassPool cp) {
/*   45 */     this.resolver = new MemberResolver(cp);
/*   46 */     this.thisClass = cc;
/*   47 */     this.thisMethod = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static String argTypesToString(int[] types, int[] dims, String[] cnames) {
/*   56 */     StringBuffer sbuf = new StringBuffer();
/*   57 */     sbuf.append('(');
/*   58 */     int n = types.length;
/*   59 */     if (n > 0) {
/*   60 */       int i = 0;
/*      */       while (true) {
/*   62 */         typeToString(sbuf, types[i], dims[i], cnames[i]);
/*   63 */         if (++i < n) {
/*   64 */           sbuf.append(',');
/*      */           continue;
/*      */         } 
/*      */         break;
/*      */       } 
/*      */     } 
/*   70 */     sbuf.append(')');
/*   71 */     return sbuf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static StringBuffer typeToString(StringBuffer sbuf, int type, int dim, String cname) {
/*      */     String s;
/*   81 */     if (type == 307) {
/*   82 */       s = MemberResolver.jvmToJavaName(cname);
/*   83 */     } else if (type == 412) {
/*   84 */       s = "Object";
/*      */     } else {
/*      */       try {
/*   87 */         s = MemberResolver.getTypeName(type);
/*      */       }
/*   89 */       catch (CompileError e) {
/*   90 */         s = "?";
/*      */       } 
/*      */     } 
/*   93 */     sbuf.append(s);
/*   94 */     while (dim-- > 0) {
/*   95 */       sbuf.append("[]");
/*      */     }
/*   97 */     return sbuf;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setThisMethod(MethodInfo m) {
/*  104 */     this.thisMethod = m;
/*      */   }
/*      */   
/*      */   protected static void fatal() throws CompileError {
/*  108 */     throw new CompileError("fatal");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getThisName() {
/*  115 */     return MemberResolver.javaToJvmName(this.thisClass.getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getSuperName() throws CompileError {
/*  122 */     return MemberResolver.javaToJvmName(
/*  123 */         MemberResolver.getSuperclass(this.thisClass).getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String resolveClassName(ASTList name) throws CompileError {
/*  132 */     return this.resolver.resolveClassName(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String resolveClassName(String jvmName) throws CompileError {
/*  139 */     return this.resolver.resolveJvmClassName(jvmName);
/*      */   }
/*      */   
/*      */   public void atNewExpr(NewExpr expr) throws CompileError {
/*  143 */     if (expr.isArray()) {
/*  144 */       atNewArrayExpr(expr);
/*      */     } else {
/*  146 */       CtClass clazz = this.resolver.lookupClassByName(expr.getClassName());
/*  147 */       String cname = clazz.getName();
/*  148 */       ASTList args = expr.getArguments();
/*  149 */       atMethodCallCore(clazz, "<init>", args);
/*  150 */       this.exprType = 307;
/*  151 */       this.arrayDim = 0;
/*  152 */       this.className = MemberResolver.javaToJvmName(cname);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void atNewArrayExpr(NewExpr expr) throws CompileError {
/*  157 */     int type = expr.getArrayType();
/*  158 */     ASTList size = expr.getArraySize();
/*  159 */     ASTList classname = expr.getClassName();
/*  160 */     ArrayInit arrayInit = expr.getInitializer();
/*  161 */     if (arrayInit != null) {
/*  162 */       arrayInit.accept(this);
/*      */     }
/*  164 */     if (size.length() > 1) {
/*  165 */       atMultiNewArray(type, classname, size);
/*      */     } else {
/*  167 */       ASTree sizeExpr = size.head();
/*  168 */       if (sizeExpr != null) {
/*  169 */         sizeExpr.accept(this);
/*      */       }
/*  171 */       this.exprType = type;
/*  172 */       this.arrayDim = 1;
/*  173 */       if (type == 307) {
/*  174 */         this.className = resolveClassName(classname);
/*      */       } else {
/*  176 */         this.className = null;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   public void atArrayInit(ArrayInit init) throws CompileError {
/*  181 */     ArrayInit arrayInit = init;
/*  182 */     while (arrayInit != null) {
/*  183 */       ASTree h = arrayInit.head();
/*  184 */       ASTList aSTList = arrayInit.tail();
/*  185 */       if (h != null) {
/*  186 */         h.accept(this);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void atMultiNewArray(int type, ASTList classname, ASTList size) throws CompileError {
/*  194 */     int dim = size.length();
/*  195 */     for (int count = 0; size != null; size = size.tail()) {
/*  196 */       ASTree s = size.head();
/*  197 */       if (s == null) {
/*      */         break;
/*      */       }
/*  200 */       count++;
/*  201 */       s.accept(this);
/*      */     } 
/*      */     
/*  204 */     this.exprType = type;
/*  205 */     this.arrayDim = dim;
/*  206 */     if (type == 307) {
/*  207 */       this.className = resolveClassName(classname);
/*      */     } else {
/*  209 */       this.className = null;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void atAssignExpr(AssignExpr expr) throws CompileError {
/*  214 */     int op = expr.getOperator();
/*  215 */     ASTree left = expr.oprand1();
/*  216 */     ASTree right = expr.oprand2();
/*  217 */     if (left instanceof Variable) {
/*  218 */       atVariableAssign((Expr)expr, op, (Variable)left, ((Variable)left)
/*  219 */           .getDeclarator(), right);
/*      */     } else {
/*      */       
/*  222 */       if (left instanceof Expr) {
/*  223 */         Expr e = (Expr)left;
/*  224 */         if (e.getOperator() == 65) {
/*  225 */           atArrayAssign((Expr)expr, op, (Expr)left, right);
/*      */           
/*      */           return;
/*      */         } 
/*      */       } 
/*  230 */       atFieldAssign((Expr)expr, op, left, right);
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
/*      */   private void atVariableAssign(Expr expr, int op, Variable var, Declarator d, ASTree right) throws CompileError {
/*  242 */     int varType = d.getType();
/*  243 */     int varArray = d.getArrayDim();
/*  244 */     String varClass = d.getClassName();
/*      */     
/*  246 */     if (op != 61) {
/*  247 */       atVariable(var);
/*      */     }
/*  249 */     right.accept(this);
/*  250 */     this.exprType = varType;
/*  251 */     this.arrayDim = varArray;
/*  252 */     this.className = varClass;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void atArrayAssign(Expr expr, int op, Expr array, ASTree right) throws CompileError {
/*  258 */     atArrayRead(array.oprand1(), array.oprand2());
/*  259 */     int aType = this.exprType;
/*  260 */     int aDim = this.arrayDim;
/*  261 */     String cname = this.className;
/*  262 */     right.accept(this);
/*  263 */     this.exprType = aType;
/*  264 */     this.arrayDim = aDim;
/*  265 */     this.className = cname;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void atFieldAssign(Expr expr, int op, ASTree left, ASTree right) throws CompileError {
/*  271 */     CtField f = fieldAccess(left);
/*  272 */     atFieldRead(f);
/*  273 */     int fType = this.exprType;
/*  274 */     int fDim = this.arrayDim;
/*  275 */     String cname = this.className;
/*  276 */     right.accept(this);
/*  277 */     this.exprType = fType;
/*  278 */     this.arrayDim = fDim;
/*  279 */     this.className = cname;
/*      */   }
/*      */   
/*      */   public void atCondExpr(CondExpr expr) throws CompileError {
/*  283 */     booleanExpr(expr.condExpr());
/*  284 */     expr.thenExpr().accept(this);
/*  285 */     int type1 = this.exprType;
/*  286 */     int dim1 = this.arrayDim;
/*  287 */     String cname1 = this.className;
/*  288 */     expr.elseExpr().accept(this);
/*      */     
/*  290 */     if (dim1 == 0 && dim1 == this.arrayDim) {
/*  291 */       if (CodeGen.rightIsStrong(type1, this.exprType)) {
/*  292 */         expr.setThen((ASTree)new CastExpr(this.exprType, 0, expr.thenExpr()));
/*  293 */       } else if (CodeGen.rightIsStrong(this.exprType, type1)) {
/*  294 */         expr.setElse((ASTree)new CastExpr(type1, 0, expr.elseExpr()));
/*  295 */         this.exprType = type1;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void atBinExpr(BinExpr expr) throws CompileError {
/*  306 */     int token = expr.getOperator();
/*  307 */     int k = CodeGen.lookupBinOp(token);
/*  308 */     if (k >= 0) {
/*      */ 
/*      */       
/*  311 */       if (token == 43) {
/*  312 */         Expr e = atPlusExpr(expr);
/*  313 */         if (e != null) {
/*      */ 
/*      */ 
/*      */           
/*  317 */           CallExpr callExpr = CallExpr.makeCall((ASTree)Expr.make(46, (ASTree)e, (ASTree)new Member("toString")), null);
/*      */           
/*  319 */           expr.setOprand1((ASTree)callExpr);
/*  320 */           expr.setOprand2(null);
/*  321 */           this.className = "java/lang/String";
/*      */         } 
/*      */       } else {
/*      */         
/*  325 */         ASTree left = expr.oprand1();
/*  326 */         ASTree right = expr.oprand2();
/*  327 */         left.accept(this);
/*  328 */         int type1 = this.exprType;
/*  329 */         right.accept(this);
/*  330 */         if (!isConstant(expr, token, left, right)) {
/*  331 */           computeBinExprType(expr, token, type1);
/*      */         }
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  337 */       booleanExpr((ASTree)expr);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Expr atPlusExpr(BinExpr expr) throws CompileError {
/*  346 */     ASTree left = expr.oprand1();
/*  347 */     ASTree right = expr.oprand2();
/*  348 */     if (right == null) {
/*      */ 
/*      */       
/*  351 */       left.accept(this);
/*  352 */       return null;
/*      */     } 
/*      */     
/*  355 */     if (isPlusExpr(left)) {
/*  356 */       Expr newExpr = atPlusExpr((BinExpr)left);
/*  357 */       if (newExpr != null) {
/*  358 */         right.accept(this);
/*  359 */         this.exprType = 307;
/*  360 */         this.arrayDim = 0;
/*  361 */         this.className = "java/lang/StringBuffer";
/*  362 */         return makeAppendCall((ASTree)newExpr, right);
/*      */       } 
/*      */     } else {
/*      */       
/*  366 */       left.accept(this);
/*      */     } 
/*  368 */     int type1 = this.exprType;
/*  369 */     int dim1 = this.arrayDim;
/*  370 */     String cname = this.className;
/*  371 */     right.accept(this);
/*      */     
/*  373 */     if (isConstant(expr, 43, left, right)) {
/*  374 */       return null;
/*      */     }
/*  376 */     if ((type1 == 307 && dim1 == 0 && "java/lang/String".equals(cname)) || (this.exprType == 307 && this.arrayDim == 0 && "java/lang/String"
/*      */       
/*  378 */       .equals(this.className))) {
/*  379 */       ASTList sbufClass = ASTList.make((ASTree)new Symbol("java"), (ASTree)new Symbol("lang"), (ASTree)new Symbol("StringBuffer"));
/*      */       
/*  381 */       NewExpr newExpr = new NewExpr(sbufClass, null);
/*  382 */       this.exprType = 307;
/*  383 */       this.arrayDim = 0;
/*  384 */       this.className = "java/lang/StringBuffer";
/*  385 */       return makeAppendCall((ASTree)makeAppendCall((ASTree)newExpr, left), right);
/*      */     } 
/*      */     
/*  388 */     computeBinExprType(expr, 43, type1);
/*  389 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isConstant(BinExpr expr, int op, ASTree left, ASTree right) throws CompileError {
/*  396 */     left = stripPlusExpr(left);
/*  397 */     right = stripPlusExpr(right);
/*  398 */     ASTree newExpr = null;
/*  399 */     if (left instanceof StringL && right instanceof StringL && op == 43) {
/*      */       
/*  401 */       StringL stringL = new StringL(((StringL)left).get() + ((StringL)right).get());
/*  402 */     } else if (left instanceof IntConst) {
/*  403 */       newExpr = ((IntConst)left).compute(op, right);
/*  404 */     } else if (left instanceof DoubleConst) {
/*  405 */       newExpr = ((DoubleConst)left).compute(op, right);
/*      */     } 
/*  407 */     if (newExpr == null) {
/*  408 */       return false;
/*      */     }
/*  410 */     expr.setOperator(43);
/*  411 */     expr.setOprand1(newExpr);
/*  412 */     expr.setOprand2(null);
/*  413 */     newExpr.accept(this);
/*  414 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static ASTree stripPlusExpr(ASTree expr) {
/*  421 */     if (expr instanceof BinExpr) {
/*  422 */       BinExpr e = (BinExpr)expr;
/*  423 */       if (e.getOperator() == 43 && e.oprand2() == null) {
/*  424 */         return e.getLeft();
/*      */       }
/*  426 */     } else if (expr instanceof Expr) {
/*  427 */       Expr e = (Expr)expr;
/*  428 */       int op = e.getOperator();
/*  429 */       if (op == 35) {
/*  430 */         ASTree cexpr = getConstantFieldValue((Member)e.oprand2());
/*  431 */         if (cexpr != null) {
/*  432 */           return cexpr;
/*      */         }
/*  434 */       } else if (op == 43 && e.getRight() == null) {
/*  435 */         return e.getLeft();
/*      */       } 
/*  437 */     } else if (expr instanceof Member) {
/*  438 */       ASTree cexpr = getConstantFieldValue((Member)expr);
/*  439 */       if (cexpr != null) {
/*  440 */         return cexpr;
/*      */       }
/*      */     } 
/*  443 */     return expr;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static ASTree getConstantFieldValue(Member mem) {
/*  451 */     return getConstantFieldValue(mem.getField());
/*      */   }
/*      */   
/*      */   public static ASTree getConstantFieldValue(CtField f) {
/*  455 */     if (f == null) {
/*  456 */       return null;
/*      */     }
/*  458 */     Object value = f.getConstantValue();
/*  459 */     if (value == null) {
/*  460 */       return null;
/*      */     }
/*  462 */     if (value instanceof String)
/*  463 */       return (ASTree)new StringL((String)value); 
/*  464 */     if (value instanceof Double || value instanceof Float) {
/*  465 */       int token = (value instanceof Double) ? 405 : 404;
/*      */       
/*  467 */       return (ASTree)new DoubleConst(((Number)value).doubleValue(), token);
/*      */     } 
/*  469 */     if (value instanceof Number) {
/*  470 */       int token = (value instanceof Long) ? 403 : 402;
/*  471 */       return (ASTree)new IntConst(((Number)value).longValue(), token);
/*      */     } 
/*  473 */     if (value instanceof Boolean) {
/*  474 */       return (ASTree)new Keyword(((Boolean)value).booleanValue() ? 410 : 411);
/*      */     }
/*      */     
/*  477 */     return null;
/*      */   }
/*      */   
/*      */   private static boolean isPlusExpr(ASTree expr) {
/*  481 */     if (expr instanceof BinExpr) {
/*  482 */       BinExpr bexpr = (BinExpr)expr;
/*  483 */       int token = bexpr.getOperator();
/*  484 */       return (token == 43);
/*      */     } 
/*      */     
/*  487 */     return false;
/*      */   }
/*      */   
/*      */   private static Expr makeAppendCall(ASTree target, ASTree arg) {
/*  491 */     return (Expr)CallExpr.makeCall((ASTree)Expr.make(46, target, (ASTree)new Member("append")), (ASTree)new ASTList(arg));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void computeBinExprType(BinExpr expr, int token, int type1) throws CompileError {
/*  499 */     int type2 = this.exprType;
/*  500 */     if (token == 364 || token == 366 || token == 370) {
/*  501 */       this.exprType = type1;
/*      */     } else {
/*  503 */       insertCast(expr, type1, type2);
/*      */     } 
/*  505 */     if (CodeGen.isP_INT(this.exprType) && this.exprType != 301) {
/*  506 */       this.exprType = 324;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void booleanExpr(ASTree expr) throws CompileError {
/*  512 */     int op = CodeGen.getCompOperator(expr);
/*  513 */     if (op == 358) {
/*  514 */       BinExpr bexpr = (BinExpr)expr;
/*  515 */       bexpr.oprand1().accept(this);
/*  516 */       int type1 = this.exprType;
/*  517 */       int dim1 = this.arrayDim;
/*  518 */       bexpr.oprand2().accept(this);
/*  519 */       if (dim1 == 0 && this.arrayDim == 0) {
/*  520 */         insertCast(bexpr, type1, this.exprType);
/*      */       }
/*  522 */     } else if (op == 33) {
/*  523 */       ((Expr)expr).oprand1().accept(this);
/*  524 */     } else if (op == 369 || op == 368) {
/*  525 */       BinExpr bexpr = (BinExpr)expr;
/*  526 */       bexpr.oprand1().accept(this);
/*  527 */       bexpr.oprand2().accept(this);
/*      */     } else {
/*      */       
/*  530 */       expr.accept(this);
/*      */     } 
/*  532 */     this.exprType = 301;
/*  533 */     this.arrayDim = 0;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void insertCast(BinExpr expr, int type1, int type2) throws CompileError {
/*  539 */     if (CodeGen.rightIsStrong(type1, type2)) {
/*  540 */       expr.setLeft((ASTree)new CastExpr(type2, 0, expr.oprand1()));
/*      */     } else {
/*  542 */       this.exprType = type1;
/*      */     } 
/*      */   }
/*      */   public void atCastExpr(CastExpr expr) throws CompileError {
/*  546 */     String cname = resolveClassName(expr.getClassName());
/*  547 */     expr.getOprand().accept(this);
/*  548 */     this.exprType = expr.getType();
/*  549 */     this.arrayDim = expr.getArrayDim();
/*  550 */     this.className = cname;
/*      */   }
/*      */   
/*      */   public void atInstanceOfExpr(InstanceOfExpr expr) throws CompileError {
/*  554 */     expr.getOprand().accept(this);
/*  555 */     this.exprType = 301;
/*  556 */     this.arrayDim = 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void atExpr(Expr expr) throws CompileError {
/*  563 */     int token = expr.getOperator();
/*  564 */     ASTree oprand = expr.oprand1();
/*  565 */     if (token == 46) {
/*  566 */       String member = ((Symbol)expr.oprand2()).get();
/*  567 */       if (member.equals("length")) {
/*      */         try {
/*  569 */           atArrayLength(expr);
/*      */         }
/*  571 */         catch (NoFieldException nfe) {
/*      */           
/*  573 */           atFieldRead((ASTree)expr);
/*      */         } 
/*  575 */       } else if (member.equals("class")) {
/*  576 */         atClassObject(expr);
/*      */       } else {
/*  578 */         atFieldRead((ASTree)expr);
/*      */       } 
/*  580 */     } else if (token == 35) {
/*  581 */       String member = ((Symbol)expr.oprand2()).get();
/*  582 */       if (member.equals("class")) {
/*  583 */         atClassObject(expr);
/*      */       } else {
/*  585 */         atFieldRead((ASTree)expr);
/*      */       } 
/*  587 */     } else if (token == 65) {
/*  588 */       atArrayRead(oprand, expr.oprand2());
/*  589 */     } else if (token == 362 || token == 363) {
/*  590 */       atPlusPlus(token, oprand, expr);
/*  591 */     } else if (token == 33) {
/*  592 */       booleanExpr((ASTree)expr);
/*  593 */     } else if (token == 67) {
/*  594 */       fatal();
/*      */     } else {
/*  596 */       oprand.accept(this);
/*  597 */       if (!isConstant(expr, token, oprand) && (
/*  598 */         token == 45 || token == 126) && 
/*  599 */         CodeGen.isP_INT(this.exprType))
/*  600 */         this.exprType = 324; 
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean isConstant(Expr expr, int op, ASTree oprand) {
/*  605 */     oprand = stripPlusExpr(oprand);
/*  606 */     if (oprand instanceof IntConst) {
/*  607 */       IntConst c = (IntConst)oprand;
/*  608 */       long v = c.get();
/*  609 */       if (op == 45) {
/*  610 */         v = -v;
/*  611 */       } else if (op == 126) {
/*  612 */         v ^= 0xFFFFFFFFFFFFFFFFL;
/*      */       } else {
/*  614 */         return false;
/*      */       } 
/*  616 */       c.set(v);
/*      */     }
/*  618 */     else if (oprand instanceof DoubleConst) {
/*  619 */       DoubleConst c = (DoubleConst)oprand;
/*  620 */       if (op == 45) {
/*  621 */         c.set(-c.get());
/*      */       } else {
/*  623 */         return false;
/*      */       } 
/*      */     } else {
/*  626 */       return false;
/*      */     } 
/*  628 */     expr.setOperator(43);
/*  629 */     return true;
/*      */   }
/*      */   
/*      */   public void atCallExpr(CallExpr expr) throws CompileError {
/*  633 */     String mname = null;
/*  634 */     CtClass targetClass = null;
/*  635 */     ASTree method = expr.oprand1();
/*  636 */     ASTList args = (ASTList)expr.oprand2();
/*      */     
/*  638 */     if (method instanceof Member) {
/*  639 */       mname = ((Member)method).get();
/*  640 */       targetClass = this.thisClass;
/*      */     }
/*  642 */     else if (method instanceof Keyword) {
/*  643 */       mname = "<init>";
/*  644 */       if (((Keyword)method).get() == 336) {
/*  645 */         targetClass = MemberResolver.getSuperclass(this.thisClass);
/*      */       } else {
/*  647 */         targetClass = this.thisClass;
/*      */       } 
/*  649 */     } else if (method instanceof Expr) {
/*  650 */       Expr e = (Expr)method;
/*  651 */       mname = ((Symbol)e.oprand2()).get();
/*  652 */       int op = e.getOperator();
/*  653 */       if (op == 35) {
/*      */         
/*  655 */         targetClass = this.resolver.lookupClass(((Symbol)e.oprand1()).get(), false);
/*      */       }
/*  657 */       else if (op == 46) {
/*  658 */         ASTree target = e.oprand1();
/*  659 */         String classFollowedByDotSuper = isDotSuper(target);
/*  660 */         if (classFollowedByDotSuper != null) {
/*  661 */           targetClass = MemberResolver.getSuperInterface(this.thisClass, classFollowedByDotSuper);
/*      */         } else {
/*      */           
/*      */           try {
/*  665 */             target.accept(this);
/*      */           }
/*  667 */           catch (NoFieldException nfe) {
/*  668 */             if (nfe.getExpr() != target) {
/*  669 */               throw nfe;
/*      */             }
/*      */             
/*  672 */             this.exprType = 307;
/*  673 */             this.arrayDim = 0;
/*  674 */             this.className = nfe.getField();
/*  675 */             e.setOperator(35);
/*  676 */             e.setOprand1((ASTree)new Symbol(MemberResolver.jvmToJavaName(this.className)));
/*      */           } 
/*      */ 
/*      */           
/*  680 */           if (this.arrayDim > 0) {
/*  681 */             targetClass = this.resolver.lookupClass("java.lang.Object", true);
/*  682 */           } else if (this.exprType == 307) {
/*  683 */             targetClass = this.resolver.lookupClassByJvmName(this.className);
/*      */           } else {
/*  685 */             badMethod();
/*      */           } 
/*      */         } 
/*      */       } else {
/*  689 */         badMethod();
/*      */       } 
/*      */     } else {
/*  692 */       fatal();
/*      */     } 
/*      */     
/*  695 */     MemberResolver.Method minfo = atMethodCallCore(targetClass, mname, args);
/*  696 */     expr.setMethod(minfo);
/*      */   }
/*      */   
/*      */   private static void badMethod() throws CompileError {
/*  700 */     throw new CompileError("bad method");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static String isDotSuper(ASTree target) {
/*  711 */     if (target instanceof Expr) {
/*  712 */       Expr e = (Expr)target;
/*  713 */       if (e.getOperator() == 46) {
/*  714 */         ASTree right = e.oprand2();
/*  715 */         if (right instanceof Keyword && ((Keyword)right).get() == 336) {
/*  716 */           return ((Symbol)e.oprand1()).get();
/*      */         }
/*      */       } 
/*      */     } 
/*  720 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MemberResolver.Method atMethodCallCore(CtClass targetClass, String mname, ASTList args) throws CompileError {
/*  731 */     int nargs = getMethodArgsLength(args);
/*  732 */     int[] types = new int[nargs];
/*  733 */     int[] dims = new int[nargs];
/*  734 */     String[] cnames = new String[nargs];
/*  735 */     atMethodArgs(args, types, dims, cnames);
/*      */ 
/*      */     
/*  738 */     MemberResolver.Method found = this.resolver.lookupMethod(targetClass, this.thisClass, this.thisMethod, mname, types, dims, cnames);
/*      */     
/*  740 */     if (found == null) {
/*  741 */       String msg, clazz = targetClass.getName();
/*  742 */       String signature = argTypesToString(types, dims, cnames);
/*      */       
/*  744 */       if (mname.equals("<init>")) {
/*  745 */         msg = "cannot find constructor " + clazz + signature;
/*      */       } else {
/*  747 */         msg = mname + signature + " not found in " + clazz;
/*      */       } 
/*  749 */       throw new CompileError(msg);
/*      */     } 
/*      */     
/*  752 */     String desc = found.info.getDescriptor();
/*  753 */     setReturnType(desc);
/*  754 */     return found;
/*      */   }
/*      */   
/*      */   public int getMethodArgsLength(ASTList args) {
/*  758 */     return ASTList.length(args);
/*      */   }
/*      */ 
/*      */   
/*      */   public void atMethodArgs(ASTList args, int[] types, int[] dims, String[] cnames) throws CompileError {
/*  763 */     int i = 0;
/*  764 */     while (args != null) {
/*  765 */       ASTree a = args.head();
/*  766 */       a.accept(this);
/*  767 */       types[i] = this.exprType;
/*  768 */       dims[i] = this.arrayDim;
/*  769 */       cnames[i] = this.className;
/*  770 */       i++;
/*  771 */       args = args.tail();
/*      */     } 
/*      */   }
/*      */   
/*      */   void setReturnType(String desc) throws CompileError {
/*  776 */     int i = desc.indexOf(')');
/*  777 */     if (i < 0) {
/*  778 */       badMethod();
/*      */     }
/*  780 */     char c = desc.charAt(++i);
/*  781 */     int dim = 0;
/*  782 */     while (c == '[') {
/*  783 */       dim++;
/*  784 */       c = desc.charAt(++i);
/*      */     } 
/*      */     
/*  787 */     this.arrayDim = dim;
/*  788 */     if (c == 'L') {
/*  789 */       int j = desc.indexOf(';', i + 1);
/*  790 */       if (j < 0) {
/*  791 */         badMethod();
/*      */       }
/*  793 */       this.exprType = 307;
/*  794 */       this.className = desc.substring(i + 1, j);
/*      */     } else {
/*      */       
/*  797 */       this.exprType = MemberResolver.descToType(c);
/*  798 */       this.className = null;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void atFieldRead(ASTree expr) throws CompileError {
/*  803 */     atFieldRead(fieldAccess(expr));
/*      */   }
/*      */   
/*      */   private void atFieldRead(CtField f) throws CompileError {
/*  807 */     FieldInfo finfo = f.getFieldInfo2();
/*  808 */     String type = finfo.getDescriptor();
/*      */     
/*  810 */     int i = 0;
/*  811 */     int dim = 0;
/*  812 */     char c = type.charAt(i);
/*  813 */     while (c == '[') {
/*  814 */       dim++;
/*  815 */       c = type.charAt(++i);
/*      */     } 
/*      */     
/*  818 */     this.arrayDim = dim;
/*  819 */     this.exprType = MemberResolver.descToType(c);
/*      */     
/*  821 */     if (c == 'L') {
/*  822 */       this.className = type.substring(i + 1, type.indexOf(';', i + 1));
/*      */     } else {
/*  824 */       this.className = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected CtField fieldAccess(ASTree expr) throws CompileError {
/*  833 */     if (expr instanceof Member) {
/*  834 */       Member mem = (Member)expr;
/*  835 */       String name = mem.get();
/*      */       try {
/*  837 */         CtField f = this.thisClass.getField(name);
/*  838 */         if (Modifier.isStatic(f.getModifiers())) {
/*  839 */           mem.setField(f);
/*      */         }
/*  841 */         return f;
/*      */       }
/*  843 */       catch (NotFoundException e) {
/*      */         
/*  845 */         throw new NoFieldException(name, expr);
/*      */       } 
/*      */     } 
/*  848 */     if (expr instanceof Expr) {
/*  849 */       Expr e = (Expr)expr;
/*  850 */       int op = e.getOperator();
/*  851 */       if (op == 35) {
/*  852 */         Member mem = (Member)e.oprand2();
/*      */         
/*  854 */         CtField f = this.resolver.lookupField(((Symbol)e.oprand1()).get(), (Symbol)mem);
/*  855 */         mem.setField(f);
/*  856 */         return f;
/*      */       } 
/*  858 */       if (op == 46) {
/*      */         try {
/*  860 */           e.oprand1().accept(this);
/*      */         }
/*  862 */         catch (NoFieldException nfe) {
/*  863 */           if (nfe.getExpr() != e.oprand1()) {
/*  864 */             throw nfe;
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  870 */           return fieldAccess2(e, nfe.getField());
/*      */         } 
/*      */         
/*  873 */         CompileError err = null;
/*      */         try {
/*  875 */           if (this.exprType == 307 && this.arrayDim == 0) {
/*  876 */             return this.resolver.lookupFieldByJvmName(this.className, (Symbol)e
/*  877 */                 .oprand2());
/*      */           }
/*  879 */         } catch (CompileError ce) {
/*  880 */           err = ce;
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  899 */         ASTree oprnd1 = e.oprand1();
/*  900 */         if (oprnd1 instanceof Symbol) {
/*  901 */           return fieldAccess2(e, ((Symbol)oprnd1).get());
/*      */         }
/*  903 */         if (err != null) {
/*  904 */           throw err;
/*      */         }
/*      */       } 
/*      */     } 
/*  908 */     throw new CompileError("bad filed access");
/*      */   }
/*      */   
/*      */   private CtField fieldAccess2(Expr e, String jvmClassName) throws CompileError {
/*  912 */     Member fname = (Member)e.oprand2();
/*  913 */     CtField f = this.resolver.lookupFieldByJvmName2(jvmClassName, (Symbol)fname, (ASTree)e);
/*  914 */     e.setOperator(35);
/*  915 */     e.setOprand1((ASTree)new Symbol(MemberResolver.jvmToJavaName(jvmClassName)));
/*  916 */     fname.setField(f);
/*  917 */     return f;
/*      */   }
/*      */   
/*      */   public void atClassObject(Expr expr) throws CompileError {
/*  921 */     this.exprType = 307;
/*  922 */     this.arrayDim = 0;
/*  923 */     this.className = "java/lang/Class";
/*      */   }
/*      */   
/*      */   public void atArrayLength(Expr expr) throws CompileError {
/*  927 */     expr.oprand1().accept(this);
/*  928 */     if (this.arrayDim == 0) {
/*  929 */       throw new NoFieldException("length", expr);
/*      */     }
/*  931 */     this.exprType = 324;
/*  932 */     this.arrayDim = 0;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void atArrayRead(ASTree array, ASTree index) throws CompileError {
/*  938 */     array.accept(this);
/*  939 */     int type = this.exprType;
/*  940 */     int dim = this.arrayDim;
/*  941 */     String cname = this.className;
/*  942 */     index.accept(this);
/*  943 */     this.exprType = type;
/*  944 */     this.arrayDim = dim - 1;
/*  945 */     this.className = cname;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void atPlusPlus(int token, ASTree oprand, Expr expr) throws CompileError {
/*  951 */     boolean isPost = (oprand == null);
/*  952 */     if (isPost) {
/*  953 */       oprand = expr.oprand2();
/*      */     }
/*  955 */     if (oprand instanceof Variable) {
/*  956 */       Declarator d = ((Variable)oprand).getDeclarator();
/*  957 */       this.exprType = d.getType();
/*  958 */       this.arrayDim = d.getArrayDim();
/*      */     } else {
/*      */       
/*  961 */       if (oprand instanceof Expr) {
/*  962 */         Expr e = (Expr)oprand;
/*  963 */         if (e.getOperator() == 65) {
/*  964 */           atArrayRead(e.oprand1(), e.oprand2());
/*      */           
/*  966 */           int t = this.exprType;
/*  967 */           if (t == 324 || t == 303 || t == 306 || t == 334) {
/*  968 */             this.exprType = 324;
/*      */           }
/*      */           
/*      */           return;
/*      */         } 
/*      */       } 
/*  974 */       atFieldPlusPlus(oprand);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void atFieldPlusPlus(ASTree oprand) throws CompileError {
/*  980 */     CtField f = fieldAccess(oprand);
/*  981 */     atFieldRead(f);
/*  982 */     int t = this.exprType;
/*  983 */     if (t == 324 || t == 303 || t == 306 || t == 334)
/*  984 */       this.exprType = 324; 
/*      */   }
/*      */   
/*      */   public void atMember(Member mem) throws CompileError {
/*  988 */     atFieldRead((ASTree)mem);
/*      */   }
/*      */   
/*      */   public void atVariable(Variable v) throws CompileError {
/*  992 */     Declarator d = v.getDeclarator();
/*  993 */     this.exprType = d.getType();
/*  994 */     this.arrayDim = d.getArrayDim();
/*  995 */     this.className = d.getClassName();
/*      */   }
/*      */   
/*      */   public void atKeyword(Keyword k) throws CompileError {
/*  999 */     this.arrayDim = 0;
/* 1000 */     int token = k.get();
/* 1001 */     switch (token) {
/*      */       case 410:
/*      */       case 411:
/* 1004 */         this.exprType = 301;
/*      */         return;
/*      */       case 412:
/* 1007 */         this.exprType = 412;
/*      */         return;
/*      */       case 336:
/*      */       case 339:
/* 1011 */         this.exprType = 307;
/* 1012 */         if (token == 339) {
/* 1013 */           this.className = getThisName();
/*      */         } else {
/* 1015 */           this.className = getSuperName();
/*      */         }  return;
/*      */     } 
/* 1018 */     fatal();
/*      */   }
/*      */ 
/*      */   
/*      */   public void atStringL(StringL s) throws CompileError {
/* 1023 */     this.exprType = 307;
/* 1024 */     this.arrayDim = 0;
/* 1025 */     this.className = "java/lang/String";
/*      */   }
/*      */   
/*      */   public void atIntConst(IntConst i) throws CompileError {
/* 1029 */     this.arrayDim = 0;
/* 1030 */     int type = i.getType();
/* 1031 */     if (type == 402 || type == 401) {
/* 1032 */       this.exprType = (type == 402) ? 324 : 306;
/*      */     } else {
/* 1034 */       this.exprType = 326;
/*      */     } 
/*      */   }
/*      */   public void atDoubleConst(DoubleConst d) throws CompileError {
/* 1038 */     this.arrayDim = 0;
/* 1039 */     if (d.getType() == 405) {
/* 1040 */       this.exprType = 312;
/*      */     } else {
/* 1042 */       this.exprType = 317;
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\TypeChecker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */