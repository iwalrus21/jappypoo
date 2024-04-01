/*      */ package javassist.compiler;
/*      */ import javassist.compiler.ast.ASTList;
/*      */ import javassist.compiler.ast.ASTree;
/*      */ import javassist.compiler.ast.ArrayInit;
/*      */ import javassist.compiler.ast.AssignExpr;
/*      */ import javassist.compiler.ast.CallExpr;
/*      */ import javassist.compiler.ast.CastExpr;
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
/*      */ import javassist.compiler.ast.Stmnt;
/*      */ import javassist.compiler.ast.StringL;
/*      */ import javassist.compiler.ast.Symbol;
/*      */ import javassist.compiler.ast.Variable;
/*      */ 
/*      */ public final class Parser implements TokenId {
/*      */   public Parser(Lex lex) {
/*   25 */     this.lex = lex;
/*      */   } private Lex lex;
/*      */   public boolean hasMore() {
/*   28 */     return (this.lex.lookAhead() >= 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ASTList parseMember(SymbolTable tbl) throws CompileError {
/*   34 */     ASTList mem = parseMember1(tbl);
/*   35 */     if (mem instanceof MethodDecl) {
/*   36 */       return (ASTList)parseMethod2(tbl, (MethodDecl)mem);
/*      */     }
/*   38 */     return mem;
/*      */   }
/*      */   
/*      */   public ASTList parseMember1(SymbolTable tbl) throws CompileError {
/*      */     Declarator d;
/*      */     String name;
/*   44 */     ASTList mods = parseMemberMods();
/*      */     
/*   46 */     boolean isConstructor = false;
/*   47 */     if (this.lex.lookAhead() == 400 && this.lex.lookAhead(1) == 40) {
/*   48 */       d = new Declarator(344, 0);
/*   49 */       isConstructor = true;
/*      */     } else {
/*      */       
/*   52 */       d = parseFormalType(tbl);
/*      */     } 
/*   54 */     if (this.lex.get() != 400) {
/*   55 */       throw new SyntaxError(this.lex);
/*      */     }
/*      */     
/*   58 */     if (isConstructor) {
/*   59 */       name = "<init>";
/*      */     } else {
/*   61 */       name = this.lex.getString();
/*      */     } 
/*   63 */     d.setVariable(new Symbol(name));
/*   64 */     if (isConstructor || this.lex.lookAhead() == 40) {
/*   65 */       return (ASTList)parseMethod1(tbl, isConstructor, mods, d);
/*      */     }
/*   67 */     return (ASTList)parseField(tbl, mods, d);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private FieldDecl parseField(SymbolTable tbl, ASTList mods, Declarator d) throws CompileError {
/*   78 */     ASTree expr = null;
/*   79 */     if (this.lex.lookAhead() == 61) {
/*   80 */       this.lex.get();
/*   81 */       expr = parseExpression(tbl);
/*      */     } 
/*      */     
/*   84 */     int c = this.lex.get();
/*   85 */     if (c == 59)
/*   86 */       return new FieldDecl((ASTree)mods, new ASTList((ASTree)d, new ASTList(expr))); 
/*   87 */     if (c == 44) {
/*   88 */       throw new CompileError("only one field can be declared in one declaration", this.lex);
/*      */     }
/*      */     
/*   91 */     throw new SyntaxError(this.lex);
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
/*      */   private MethodDecl parseMethod1(SymbolTable tbl, boolean isConstructor, ASTList mods, Declarator d) throws CompileError {
/*  108 */     if (this.lex.get() != 40) {
/*  109 */       throw new SyntaxError(this.lex);
/*      */     }
/*  111 */     ASTList parms = null;
/*  112 */     if (this.lex.lookAhead() != 41)
/*      */       while (true) {
/*  114 */         parms = ASTList.append(parms, (ASTree)parseFormalParam(tbl));
/*  115 */         int t = this.lex.lookAhead();
/*  116 */         if (t == 44) {
/*  117 */           this.lex.get(); continue;
/*  118 */         }  if (t == 41) {
/*      */           break;
/*      */         }
/*      */       }  
/*  122 */     this.lex.get();
/*  123 */     d.addArrayDim(parseArrayDimension());
/*  124 */     if (isConstructor && d.getArrayDim() > 0) {
/*  125 */       throw new SyntaxError(this.lex);
/*      */     }
/*  127 */     ASTList throwsList = null;
/*  128 */     if (this.lex.lookAhead() == 341) {
/*  129 */       this.lex.get();
/*      */       while (true) {
/*  131 */         throwsList = ASTList.append(throwsList, (ASTree)parseClassType(tbl));
/*  132 */         if (this.lex.lookAhead() == 44) {
/*  133 */           this.lex.get();
/*      */           continue;
/*      */         } 
/*      */         break;
/*      */       } 
/*      */     } 
/*  139 */     return new MethodDecl((ASTree)mods, new ASTList((ASTree)d, 
/*  140 */           ASTList.make((ASTree)parms, (ASTree)throwsList, null)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MethodDecl parseMethod2(SymbolTable tbl, MethodDecl md) throws CompileError {
/*  148 */     Stmnt body = null;
/*  149 */     if (this.lex.lookAhead() == 59) {
/*  150 */       this.lex.get();
/*      */     } else {
/*  152 */       body = parseBlock(tbl);
/*  153 */       if (body == null) {
/*  154 */         body = new Stmnt(66);
/*      */       }
/*      */     } 
/*  157 */     md.sublist(4).setHead((ASTree)body);
/*  158 */     return md;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ASTList parseMemberMods() {
/*  168 */     ASTList list = null;
/*      */     while (true) {
/*  170 */       int t = this.lex.lookAhead();
/*  171 */       if (t == 300 || t == 315 || t == 332 || t == 331 || t == 330 || t == 338 || t == 335 || t == 345 || t == 342 || t == 347) {
/*      */ 
/*      */         
/*  174 */         list = new ASTList((ASTree)new Keyword(this.lex.get()), list);
/*      */         continue;
/*      */       } 
/*      */       break;
/*      */     } 
/*  179 */     return list;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Declarator parseFormalType(SymbolTable tbl) throws CompileError {
/*  185 */     int t = this.lex.lookAhead();
/*  186 */     if (isBuiltinType(t) || t == 344) {
/*  187 */       this.lex.get();
/*  188 */       int i = parseArrayDimension();
/*  189 */       return new Declarator(t, i);
/*      */     } 
/*      */     
/*  192 */     ASTList name = parseClassType(tbl);
/*  193 */     int dim = parseArrayDimension();
/*  194 */     return new Declarator(name, dim);
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean isBuiltinType(int t) {
/*  199 */     return (t == 301 || t == 303 || t == 306 || t == 334 || t == 324 || t == 326 || t == 317 || t == 312);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Declarator parseFormalParam(SymbolTable tbl) throws CompileError {
/*  208 */     Declarator d = parseFormalType(tbl);
/*  209 */     if (this.lex.get() != 400) {
/*  210 */       throw new SyntaxError(this.lex);
/*      */     }
/*  212 */     String name = this.lex.getString();
/*  213 */     d.setVariable(new Symbol(name));
/*  214 */     d.addArrayDim(parseArrayDimension());
/*  215 */     tbl.append(name, d);
/*  216 */     return d;
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
/*      */   public Stmnt parseStatement(SymbolTable tbl) throws CompileError {
/*  241 */     int t = this.lex.lookAhead();
/*  242 */     if (t == 123)
/*  243 */       return parseBlock(tbl); 
/*  244 */     if (t == 59) {
/*  245 */       this.lex.get();
/*  246 */       return new Stmnt(66);
/*      */     } 
/*  248 */     if (t == 400 && this.lex.lookAhead(1) == 58) {
/*  249 */       this.lex.get();
/*  250 */       String label = this.lex.getString();
/*  251 */       this.lex.get();
/*  252 */       return Stmnt.make(76, (ASTree)new Symbol(label), (ASTree)parseStatement(tbl));
/*      */     } 
/*  254 */     if (t == 320)
/*  255 */       return parseIf(tbl); 
/*  256 */     if (t == 346)
/*  257 */       return parseWhile(tbl); 
/*  258 */     if (t == 311)
/*  259 */       return parseDo(tbl); 
/*  260 */     if (t == 318)
/*  261 */       return parseFor(tbl); 
/*  262 */     if (t == 343)
/*  263 */       return parseTry(tbl); 
/*  264 */     if (t == 337)
/*  265 */       return parseSwitch(tbl); 
/*  266 */     if (t == 338)
/*  267 */       return parseSynchronized(tbl); 
/*  268 */     if (t == 333)
/*  269 */       return parseReturn(tbl); 
/*  270 */     if (t == 340)
/*  271 */       return parseThrow(tbl); 
/*  272 */     if (t == 302)
/*  273 */       return parseBreak(tbl); 
/*  274 */     if (t == 309) {
/*  275 */       return parseContinue(tbl);
/*      */     }
/*  277 */     return parseDeclarationOrExpression(tbl, false);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Stmnt parseBlock(SymbolTable tbl) throws CompileError {
/*  283 */     if (this.lex.get() != 123) {
/*  284 */       throw new SyntaxError(this.lex);
/*      */     }
/*  286 */     Stmnt body = null;
/*  287 */     SymbolTable tbl2 = new SymbolTable(tbl);
/*  288 */     while (this.lex.lookAhead() != 125) {
/*  289 */       Stmnt s = parseStatement(tbl2);
/*  290 */       if (s != null) {
/*  291 */         body = (Stmnt)ASTList.concat((ASTList)body, (ASTList)new Stmnt(66, (ASTree)s));
/*      */       }
/*      */     } 
/*  294 */     this.lex.get();
/*  295 */     if (body == null) {
/*  296 */       return new Stmnt(66);
/*      */     }
/*  298 */     return body;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Stmnt parseIf(SymbolTable tbl) throws CompileError {
/*      */     Stmnt elsep;
/*  305 */     int t = this.lex.get();
/*  306 */     ASTree expr = parseParExpression(tbl);
/*  307 */     Stmnt thenp = parseStatement(tbl);
/*      */     
/*  309 */     if (this.lex.lookAhead() == 313) {
/*  310 */       this.lex.get();
/*  311 */       elsep = parseStatement(tbl);
/*      */     } else {
/*      */       
/*  314 */       elsep = null;
/*      */     } 
/*  316 */     return new Stmnt(t, expr, new ASTList((ASTree)thenp, new ASTList((ASTree)elsep)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Stmnt parseWhile(SymbolTable tbl) throws CompileError {
/*  324 */     int t = this.lex.get();
/*  325 */     ASTree expr = parseParExpression(tbl);
/*  326 */     Stmnt body = parseStatement(tbl);
/*  327 */     return new Stmnt(t, expr, (ASTList)body);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Stmnt parseDo(SymbolTable tbl) throws CompileError {
/*  333 */     int t = this.lex.get();
/*  334 */     Stmnt body = parseStatement(tbl);
/*  335 */     if (this.lex.get() != 346 || this.lex.get() != 40) {
/*  336 */       throw new SyntaxError(this.lex);
/*      */     }
/*  338 */     ASTree expr = parseExpression(tbl);
/*  339 */     if (this.lex.get() != 41 || this.lex.get() != 59) {
/*  340 */       throw new SyntaxError(this.lex);
/*      */     }
/*  342 */     return new Stmnt(t, expr, (ASTList)body);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Stmnt parseFor(SymbolTable tbl) throws CompileError {
/*      */     Stmnt expr1, expr3;
/*      */     ASTree expr2;
/*  351 */     int t = this.lex.get();
/*      */     
/*  353 */     SymbolTable tbl2 = new SymbolTable(tbl);
/*      */     
/*  355 */     if (this.lex.get() != 40) {
/*  356 */       throw new SyntaxError(this.lex);
/*      */     }
/*  358 */     if (this.lex.lookAhead() == 59) {
/*  359 */       this.lex.get();
/*  360 */       expr1 = null;
/*      */     } else {
/*      */       
/*  363 */       expr1 = parseDeclarationOrExpression(tbl2, true);
/*      */     } 
/*  365 */     if (this.lex.lookAhead() == 59) {
/*  366 */       expr2 = null;
/*      */     } else {
/*  368 */       expr2 = parseExpression(tbl2);
/*      */     } 
/*  370 */     if (this.lex.get() != 59) {
/*  371 */       throw new CompileError("; is missing", this.lex);
/*      */     }
/*  373 */     if (this.lex.lookAhead() == 41) {
/*  374 */       expr3 = null;
/*      */     } else {
/*  376 */       expr3 = parseExprList(tbl2);
/*      */     } 
/*  378 */     if (this.lex.get() != 41) {
/*  379 */       throw new CompileError(") is missing", this.lex);
/*      */     }
/*  381 */     Stmnt body = parseStatement(tbl2);
/*  382 */     return new Stmnt(t, (ASTree)expr1, new ASTList(expr2, new ASTList((ASTree)expr3, (ASTList)body)));
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
/*      */   private Stmnt parseSwitch(SymbolTable tbl) throws CompileError {
/*  394 */     int t = this.lex.get();
/*  395 */     ASTree expr = parseParExpression(tbl);
/*  396 */     Stmnt body = parseSwitchBlock(tbl);
/*  397 */     return new Stmnt(t, expr, (ASTList)body);
/*      */   }
/*      */   
/*      */   private Stmnt parseSwitchBlock(SymbolTable tbl) throws CompileError {
/*  401 */     if (this.lex.get() != 123) {
/*  402 */       throw new SyntaxError(this.lex);
/*      */     }
/*  404 */     SymbolTable tbl2 = new SymbolTable(tbl);
/*  405 */     Stmnt s = parseStmntOrCase(tbl2);
/*  406 */     if (s == null) {
/*  407 */       throw new CompileError("empty switch block", this.lex);
/*      */     }
/*  409 */     int op = s.getOperator();
/*  410 */     if (op != 304 && op != 310) {
/*  411 */       throw new CompileError("no case or default in a switch block", this.lex);
/*      */     }
/*      */     
/*  414 */     Stmnt body = new Stmnt(66, (ASTree)s);
/*  415 */     while (this.lex.lookAhead() != 125) {
/*  416 */       Stmnt s2 = parseStmntOrCase(tbl2);
/*  417 */       if (s2 != null) {
/*  418 */         int op2 = s2.getOperator();
/*  419 */         if (op2 == 304 || op2 == 310) {
/*  420 */           body = (Stmnt)ASTList.concat((ASTList)body, (ASTList)new Stmnt(66, (ASTree)s2));
/*  421 */           s = s2;
/*      */           continue;
/*      */         } 
/*  424 */         s = (Stmnt)ASTList.concat((ASTList)s, (ASTList)new Stmnt(66, (ASTree)s2));
/*      */       } 
/*      */     } 
/*      */     
/*  428 */     this.lex.get();
/*  429 */     return body;
/*      */   }
/*      */   private Stmnt parseStmntOrCase(SymbolTable tbl) throws CompileError {
/*      */     Stmnt s;
/*  433 */     int t = this.lex.lookAhead();
/*  434 */     if (t != 304 && t != 310) {
/*  435 */       return parseStatement(tbl);
/*      */     }
/*  437 */     this.lex.get();
/*      */     
/*  439 */     if (t == 304) {
/*  440 */       s = new Stmnt(t, parseExpression(tbl));
/*      */     } else {
/*  442 */       s = new Stmnt(310);
/*      */     } 
/*  444 */     if (this.lex.get() != 58) {
/*  445 */       throw new CompileError(": is missing", this.lex);
/*      */     }
/*  447 */     return s;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Stmnt parseSynchronized(SymbolTable tbl) throws CompileError {
/*  454 */     int t = this.lex.get();
/*  455 */     if (this.lex.get() != 40) {
/*  456 */       throw new SyntaxError(this.lex);
/*      */     }
/*  458 */     ASTree expr = parseExpression(tbl);
/*  459 */     if (this.lex.get() != 41) {
/*  460 */       throw new SyntaxError(this.lex);
/*      */     }
/*  462 */     Stmnt body = parseBlock(tbl);
/*  463 */     return new Stmnt(t, expr, (ASTList)body);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Stmnt parseTry(SymbolTable tbl) throws CompileError {
/*  472 */     this.lex.get();
/*  473 */     Stmnt block = parseBlock(tbl);
/*  474 */     ASTList catchList = null;
/*  475 */     while (this.lex.lookAhead() == 305) {
/*  476 */       this.lex.get();
/*  477 */       if (this.lex.get() != 40) {
/*  478 */         throw new SyntaxError(this.lex);
/*      */       }
/*  480 */       SymbolTable tbl2 = new SymbolTable(tbl);
/*  481 */       Declarator d = parseFormalParam(tbl2);
/*  482 */       if (d.getArrayDim() > 0 || d.getType() != 307) {
/*  483 */         throw new SyntaxError(this.lex);
/*      */       }
/*  485 */       if (this.lex.get() != 41) {
/*  486 */         throw new SyntaxError(this.lex);
/*      */       }
/*  488 */       Stmnt b = parseBlock(tbl2);
/*  489 */       catchList = ASTList.append(catchList, (ASTree)new Pair((ASTree)d, (ASTree)b));
/*      */     } 
/*      */     
/*  492 */     Stmnt finallyBlock = null;
/*  493 */     if (this.lex.lookAhead() == 316) {
/*  494 */       this.lex.get();
/*  495 */       finallyBlock = parseBlock(tbl);
/*      */     } 
/*      */     
/*  498 */     return Stmnt.make(343, (ASTree)block, (ASTree)catchList, (ASTree)finallyBlock);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Stmnt parseReturn(SymbolTable tbl) throws CompileError {
/*  504 */     int t = this.lex.get();
/*  505 */     Stmnt s = new Stmnt(t);
/*  506 */     if (this.lex.lookAhead() != 59) {
/*  507 */       s.setLeft(parseExpression(tbl));
/*      */     }
/*  509 */     if (this.lex.get() != 59) {
/*  510 */       throw new CompileError("; is missing", this.lex);
/*      */     }
/*  512 */     return s;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Stmnt parseThrow(SymbolTable tbl) throws CompileError {
/*  518 */     int t = this.lex.get();
/*  519 */     ASTree expr = parseExpression(tbl);
/*  520 */     if (this.lex.get() != 59) {
/*  521 */       throw new CompileError("; is missing", this.lex);
/*      */     }
/*  523 */     return new Stmnt(t, expr);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Stmnt parseBreak(SymbolTable tbl) throws CompileError {
/*  531 */     return parseContinue(tbl);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Stmnt parseContinue(SymbolTable tbl) throws CompileError {
/*  539 */     int t = this.lex.get();
/*  540 */     Stmnt s = new Stmnt(t);
/*  541 */     int t2 = this.lex.get();
/*  542 */     if (t2 == 400) {
/*  543 */       s.setLeft((ASTree)new Symbol(this.lex.getString()));
/*  544 */       t2 = this.lex.get();
/*      */     } 
/*      */     
/*  547 */     if (t2 != 59) {
/*  548 */       throw new CompileError("; is missing", this.lex);
/*      */     }
/*  550 */     return s;
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
/*      */   private Stmnt parseDeclarationOrExpression(SymbolTable tbl, boolean exprList) throws CompileError {
/*      */     Stmnt expr;
/*  566 */     int t = this.lex.lookAhead();
/*  567 */     while (t == 315) {
/*  568 */       this.lex.get();
/*  569 */       t = this.lex.lookAhead();
/*      */     } 
/*      */     
/*  572 */     if (isBuiltinType(t)) {
/*  573 */       t = this.lex.get();
/*  574 */       int dim = parseArrayDimension();
/*  575 */       return parseDeclarators(tbl, new Declarator(t, dim));
/*      */     } 
/*  577 */     if (t == 400) {
/*  578 */       int i = nextIsClassType(0);
/*  579 */       if (i >= 0 && 
/*  580 */         this.lex.lookAhead(i) == 400) {
/*  581 */         ASTList name = parseClassType(tbl);
/*  582 */         int dim = parseArrayDimension();
/*  583 */         return parseDeclarators(tbl, new Declarator(name, dim));
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  588 */     if (exprList) {
/*  589 */       expr = parseExprList(tbl);
/*      */     } else {
/*  591 */       expr = new Stmnt(69, parseExpression(tbl));
/*      */     } 
/*  593 */     if (this.lex.get() != 59) {
/*  594 */       throw new CompileError("; is missing", this.lex);
/*      */     }
/*  596 */     return expr;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Stmnt parseExprList(SymbolTable tbl) throws CompileError {
/*  602 */     Stmnt expr = null;
/*      */     while (true) {
/*  604 */       Stmnt e = new Stmnt(69, parseExpression(tbl));
/*  605 */       expr = (Stmnt)ASTList.concat((ASTList)expr, (ASTList)new Stmnt(66, (ASTree)e));
/*  606 */       if (this.lex.lookAhead() == 44) {
/*  607 */         this.lex.get(); continue;
/*      */       }  break;
/*  609 */     }  return expr;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Stmnt parseDeclarators(SymbolTable tbl, Declarator d) throws CompileError {
/*  618 */     Stmnt decl = null;
/*      */     while (true) {
/*  620 */       decl = (Stmnt)ASTList.concat((ASTList)decl, (ASTList)new Stmnt(68, (ASTree)
/*  621 */             parseDeclarator(tbl, d)));
/*  622 */       int t = this.lex.get();
/*  623 */       if (t == 59)
/*  624 */         return decl; 
/*  625 */       if (t != 44) {
/*  626 */         throw new CompileError("; is missing", this.lex);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Declarator parseDeclarator(SymbolTable tbl, Declarator d) throws CompileError {
/*  635 */     if (this.lex.get() != 400 || d.getType() == 344) {
/*  636 */       throw new SyntaxError(this.lex);
/*      */     }
/*  638 */     String name = this.lex.getString();
/*  639 */     Symbol symbol = new Symbol(name);
/*  640 */     int dim = parseArrayDimension();
/*  641 */     ASTree init = null;
/*  642 */     if (this.lex.lookAhead() == 61) {
/*  643 */       this.lex.get();
/*  644 */       init = parseInitializer(tbl);
/*      */     } 
/*      */     
/*  647 */     Declarator decl = d.make(symbol, dim, init);
/*  648 */     tbl.append(name, decl);
/*  649 */     return decl;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private ASTree parseInitializer(SymbolTable tbl) throws CompileError {
/*  655 */     if (this.lex.lookAhead() == 123) {
/*  656 */       return (ASTree)parseArrayInitializer(tbl);
/*      */     }
/*  658 */     return parseExpression(tbl);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ArrayInit parseArrayInitializer(SymbolTable tbl) throws CompileError {
/*  667 */     this.lex.get();
/*  668 */     ASTree expr = parseExpression(tbl);
/*  669 */     ArrayInit init = new ArrayInit(expr);
/*  670 */     while (this.lex.lookAhead() == 44) {
/*  671 */       this.lex.get();
/*  672 */       expr = parseExpression(tbl);
/*  673 */       ASTList.append((ASTList)init, expr);
/*      */     } 
/*      */     
/*  676 */     if (this.lex.get() != 125) {
/*  677 */       throw new SyntaxError(this.lex);
/*      */     }
/*  679 */     return init;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private ASTree parseParExpression(SymbolTable tbl) throws CompileError {
/*  685 */     if (this.lex.get() != 40) {
/*  686 */       throw new SyntaxError(this.lex);
/*      */     }
/*  688 */     ASTree expr = parseExpression(tbl);
/*  689 */     if (this.lex.get() != 41) {
/*  690 */       throw new SyntaxError(this.lex);
/*      */     }
/*  692 */     return expr;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ASTree parseExpression(SymbolTable tbl) throws CompileError {
/*  699 */     ASTree left = parseConditionalExpr(tbl);
/*  700 */     if (!isAssignOp(this.lex.lookAhead())) {
/*  701 */       return left;
/*      */     }
/*  703 */     int t = this.lex.get();
/*  704 */     ASTree right = parseExpression(tbl);
/*  705 */     return (ASTree)AssignExpr.makeAssign(t, left, right);
/*      */   }
/*      */   
/*      */   private static boolean isAssignOp(int t) {
/*  709 */     return (t == 61 || t == 351 || t == 352 || t == 353 || t == 354 || t == 355 || t == 356 || t == 360 || t == 361 || t == 365 || t == 367 || t == 371);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ASTree parseConditionalExpr(SymbolTable tbl) throws CompileError {
/*  719 */     ASTree cond = parseBinaryExpr(tbl);
/*  720 */     if (this.lex.lookAhead() == 63) {
/*  721 */       this.lex.get();
/*  722 */       ASTree thenExpr = parseExpression(tbl);
/*  723 */       if (this.lex.get() != 58) {
/*  724 */         throw new CompileError(": is missing", this.lex);
/*      */       }
/*  726 */       ASTree elseExpr = parseExpression(tbl);
/*  727 */       return (ASTree)new CondExpr(cond, thenExpr, elseExpr);
/*      */     } 
/*      */     
/*  730 */     return cond;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ASTree parseBinaryExpr(SymbolTable tbl) throws CompileError {
/*  775 */     ASTree expr = parseUnaryExpr(tbl);
/*      */     while (true) {
/*  777 */       int t = this.lex.lookAhead();
/*  778 */       int p = getOpPrecedence(t);
/*  779 */       if (p == 0) {
/*  780 */         return expr;
/*      */       }
/*  782 */       expr = binaryExpr2(tbl, expr, p);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private ASTree parseInstanceOf(SymbolTable tbl, ASTree expr) throws CompileError {
/*  789 */     int t = this.lex.lookAhead();
/*  790 */     if (isBuiltinType(t)) {
/*  791 */       this.lex.get();
/*  792 */       int i = parseArrayDimension();
/*  793 */       return (ASTree)new InstanceOfExpr(t, i, expr);
/*      */     } 
/*      */     
/*  796 */     ASTList name = parseClassType(tbl);
/*  797 */     int dim = parseArrayDimension();
/*  798 */     return (ASTree)new InstanceOfExpr(name, dim, expr);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ASTree binaryExpr2(SymbolTable tbl, ASTree expr, int prec) throws CompileError {
/*  805 */     int t = this.lex.get();
/*  806 */     if (t == 323) {
/*  807 */       return parseInstanceOf(tbl, expr);
/*      */     }
/*  809 */     ASTree expr2 = parseUnaryExpr(tbl);
/*      */     while (true) {
/*  811 */       int t2 = this.lex.lookAhead();
/*  812 */       int p2 = getOpPrecedence(t2);
/*  813 */       if (p2 != 0 && prec > p2) {
/*  814 */         expr2 = binaryExpr2(tbl, expr2, p2); continue;
/*      */       }  break;
/*  816 */     }  return (ASTree)BinExpr.makeBin(t, expr, expr2);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  821 */   private static final int[] binaryOpPrecedence = new int[] { 0, 0, 0, 0, 1, 6, 0, 0, 0, 1, 2, 0, 2, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 4, 0 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int getOpPrecedence(int c) {
/*  828 */     if (33 <= c && c <= 63)
/*  829 */       return binaryOpPrecedence[c - 33]; 
/*  830 */     if (c == 94)
/*  831 */       return 7; 
/*  832 */     if (c == 124)
/*  833 */       return 8; 
/*  834 */     if (c == 369)
/*  835 */       return 9; 
/*  836 */     if (c == 368)
/*  837 */       return 10; 
/*  838 */     if (c == 358 || c == 350)
/*  839 */       return 5; 
/*  840 */     if (c == 357 || c == 359 || c == 323)
/*  841 */       return 4; 
/*  842 */     if (c == 364 || c == 366 || c == 370) {
/*  843 */       return 3;
/*      */     }
/*  845 */     return 0;
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
/*      */   private ASTree parseUnaryExpr(SymbolTable tbl) throws CompileError {
/*      */     int t;
/*  859 */     switch (this.lex.lookAhead()) {
/*      */       case 33:
/*      */       case 43:
/*      */       case 45:
/*      */       case 126:
/*      */       case 362:
/*      */       case 363:
/*  866 */         t = this.lex.get();
/*  867 */         if (t == 45) {
/*  868 */           int t2 = this.lex.lookAhead();
/*  869 */           switch (t2) {
/*      */             case 401:
/*      */             case 402:
/*      */             case 403:
/*  873 */               this.lex.get();
/*  874 */               return (ASTree)new IntConst(-this.lex.getLong(), t2);
/*      */             case 404:
/*      */             case 405:
/*  877 */               this.lex.get();
/*  878 */               return (ASTree)new DoubleConst(-this.lex.getDouble(), t2);
/*      */           } 
/*      */ 
/*      */ 
/*      */         
/*      */         } 
/*  884 */         return (ASTree)Expr.make(t, parseUnaryExpr(tbl));
/*      */       case 40:
/*  886 */         return parseCast(tbl);
/*      */     } 
/*  888 */     return parsePostfix(tbl);
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
/*      */   private ASTree parseCast(SymbolTable tbl) throws CompileError {
/*  901 */     int t = this.lex.lookAhead(1);
/*  902 */     if (isBuiltinType(t) && nextIsBuiltinCast()) {
/*  903 */       this.lex.get();
/*  904 */       this.lex.get();
/*  905 */       int dim = parseArrayDimension();
/*  906 */       if (this.lex.get() != 41) {
/*  907 */         throw new CompileError(") is missing", this.lex);
/*      */       }
/*  909 */       return (ASTree)new CastExpr(t, dim, parseUnaryExpr(tbl));
/*      */     } 
/*  911 */     if (t == 400 && nextIsClassCast()) {
/*  912 */       this.lex.get();
/*  913 */       ASTList name = parseClassType(tbl);
/*  914 */       int dim = parseArrayDimension();
/*  915 */       if (this.lex.get() != 41) {
/*  916 */         throw new CompileError(") is missing", this.lex);
/*      */       }
/*  918 */       return (ASTree)new CastExpr(name, dim, parseUnaryExpr(tbl));
/*      */     } 
/*      */     
/*  921 */     return parsePostfix(tbl);
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean nextIsBuiltinCast() {
/*  926 */     int i = 2; int t;
/*  927 */     while ((t = this.lex.lookAhead(i++)) == 91) {
/*  928 */       if (this.lex.lookAhead(i++) != 93)
/*  929 */         return false; 
/*      */     } 
/*  931 */     return (this.lex.lookAhead(i - 1) == 41);
/*      */   }
/*      */   
/*      */   private boolean nextIsClassCast() {
/*  935 */     int i = nextIsClassType(1);
/*  936 */     if (i < 0) {
/*  937 */       return false;
/*      */     }
/*  939 */     int t = this.lex.lookAhead(i);
/*  940 */     if (t != 41) {
/*  941 */       return false;
/*      */     }
/*  943 */     t = this.lex.lookAhead(i + 1);
/*  944 */     return (t == 40 || t == 412 || t == 406 || t == 400 || t == 339 || t == 336 || t == 328 || t == 410 || t == 411 || t == 403 || t == 402 || t == 401 || t == 405 || t == 404);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int nextIsClassType(int i) {
/*  953 */     while (this.lex.lookAhead(++i) == 46) {
/*  954 */       if (this.lex.lookAhead(++i) != 400)
/*  955 */         return -1; 
/*      */     }  int t;
/*  957 */     while ((t = this.lex.lookAhead(i++)) == 91) {
/*  958 */       if (this.lex.lookAhead(i++) != 93)
/*  959 */         return -1; 
/*      */     } 
/*  961 */     return i - 1;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private int parseArrayDimension() throws CompileError {
/*  967 */     int arrayDim = 0;
/*  968 */     while (this.lex.lookAhead() == 91) {
/*  969 */       arrayDim++;
/*  970 */       this.lex.get();
/*  971 */       if (this.lex.get() != 93) {
/*  972 */         throw new CompileError("] is missing", this.lex);
/*      */       }
/*      */     } 
/*  975 */     return arrayDim;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private ASTList parseClassType(SymbolTable tbl) throws CompileError {
/*  981 */     ASTList list = null;
/*      */     while (true) {
/*  983 */       if (this.lex.get() != 400) {
/*  984 */         throw new SyntaxError(this.lex);
/*      */       }
/*  986 */       list = ASTList.append(list, (ASTree)new Symbol(this.lex.getString()));
/*  987 */       if (this.lex.lookAhead() == 46) {
/*  988 */         this.lex.get();
/*      */         continue;
/*      */       } 
/*      */       break;
/*      */     } 
/*  993 */     return list;
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
/*      */   private ASTree parsePostfix(SymbolTable tbl) throws CompileError {
/*      */     Expr expr1;
/* 1014 */     int token = this.lex.lookAhead();
/* 1015 */     switch (token) {
/*      */       case 401:
/*      */       case 402:
/*      */       case 403:
/* 1019 */         this.lex.get();
/* 1020 */         return (ASTree)new IntConst(this.lex.getLong(), token);
/*      */       case 404:
/*      */       case 405:
/* 1023 */         this.lex.get();
/* 1024 */         return (ASTree)new DoubleConst(this.lex.getDouble(), token);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1031 */     ASTree expr = parsePrimaryExpr(tbl); while (true) {
/*      */       String str; ASTree index; Expr expr2; ASTree aSTree1;
/*      */       int t;
/* 1034 */       switch (this.lex.lookAhead()) {
/*      */         case 40:
/* 1036 */           expr = parseMethodCall(tbl, expr);
/*      */           continue;
/*      */         case 91:
/* 1039 */           if (this.lex.lookAhead(1) == 93) {
/* 1040 */             int dim = parseArrayDimension();
/* 1041 */             if (this.lex.get() != 46 || this.lex.get() != 307) {
/* 1042 */               throw new SyntaxError(this.lex);
/*      */             }
/* 1044 */             expr = parseDotClass(expr, dim);
/*      */             continue;
/*      */           } 
/* 1047 */           index = parseArrayIndex(tbl);
/* 1048 */           if (index == null) {
/* 1049 */             throw new SyntaxError(this.lex);
/*      */           }
/* 1051 */           expr2 = Expr.make(65, expr, index);
/*      */           continue;
/*      */         
/*      */         case 362:
/*      */         case 363:
/* 1056 */           t = this.lex.get();
/* 1057 */           expr2 = Expr.make(t, null, (ASTree)expr2);
/*      */           continue;
/*      */         case 46:
/* 1060 */           this.lex.get();
/* 1061 */           t = this.lex.get();
/* 1062 */           if (t == 307) {
/* 1063 */             aSTree1 = parseDotClass((ASTree)expr2, 0); continue;
/* 1064 */           }  if (t == 336) {
/* 1065 */             expr1 = Expr.make(46, (ASTree)new Symbol(toClassName(aSTree1)), (ASTree)new Keyword(t)); continue;
/* 1066 */           }  if (t == 400) {
/* 1067 */             String str1 = this.lex.getString();
/* 1068 */             expr1 = Expr.make(46, (ASTree)expr1, (ASTree)new Member(str1));
/*      */             continue;
/*      */           } 
/* 1071 */           throw new CompileError("missing member name", this.lex);
/*      */         
/*      */         case 35:
/* 1074 */           this.lex.get();
/* 1075 */           t = this.lex.get();
/* 1076 */           if (t != 400) {
/* 1077 */             throw new CompileError("missing static member name", this.lex);
/*      */           }
/* 1079 */           str = this.lex.getString();
/* 1080 */           expr1 = Expr.make(35, (ASTree)new Symbol(toClassName((ASTree)expr1)), (ASTree)new Member(str)); continue;
/*      */       } 
/*      */       break;
/*      */     } 
/* 1084 */     return (ASTree)expr1;
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
/*      */   private ASTree parseDotClass(ASTree className, int dim) throws CompileError {
/* 1096 */     String cname = toClassName(className);
/* 1097 */     if (dim > 0) {
/* 1098 */       StringBuffer sbuf = new StringBuffer();
/* 1099 */       while (dim-- > 0) {
/* 1100 */         sbuf.append('[');
/*      */       }
/* 1102 */       sbuf.append('L').append(cname.replace('.', '/')).append(';');
/* 1103 */       cname = sbuf.toString();
/*      */     } 
/*      */     
/* 1106 */     return (ASTree)Expr.make(46, (ASTree)new Symbol(cname), (ASTree)new Member("class"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ASTree parseDotClass(int builtinType, int dim) throws CompileError {
/*      */     String cname;
/* 1116 */     if (dim > 0) {
/* 1117 */       String str = CodeGen.toJvmTypeName(builtinType, dim);
/* 1118 */       return (ASTree)Expr.make(46, (ASTree)new Symbol(str), (ASTree)new Member("class"));
/*      */     } 
/*      */ 
/*      */     
/* 1122 */     switch (builtinType) {
/*      */       case 301:
/* 1124 */         cname = "java.lang.Boolean";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1155 */         return (ASTree)Expr.make(35, (ASTree)new Symbol(cname), (ASTree)new Member("TYPE"));case 303: cname = "java.lang.Byte"; return (ASTree)Expr.make(35, (ASTree)new Symbol(cname), (ASTree)new Member("TYPE"));case 306: cname = "java.lang.Character"; return (ASTree)Expr.make(35, (ASTree)new Symbol(cname), (ASTree)new Member("TYPE"));case 334: cname = "java.lang.Short"; return (ASTree)Expr.make(35, (ASTree)new Symbol(cname), (ASTree)new Member("TYPE"));case 324: cname = "java.lang.Integer"; return (ASTree)Expr.make(35, (ASTree)new Symbol(cname), (ASTree)new Member("TYPE"));case 326: cname = "java.lang.Long"; return (ASTree)Expr.make(35, (ASTree)new Symbol(cname), (ASTree)new Member("TYPE"));case 317: cname = "java.lang.Float"; return (ASTree)Expr.make(35, (ASTree)new Symbol(cname), (ASTree)new Member("TYPE"));case 312: cname = "java.lang.Double"; return (ASTree)Expr.make(35, (ASTree)new Symbol(cname), (ASTree)new Member("TYPE"));case 344: cname = "java.lang.Void"; return (ASTree)Expr.make(35, (ASTree)new Symbol(cname), (ASTree)new Member("TYPE"));
/*      */     } 
/*      */     throw new CompileError("invalid builtin type: " + builtinType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ASTree parseMethodCall(SymbolTable tbl, ASTree expr) throws CompileError {
/* 1167 */     if (expr instanceof Keyword) {
/* 1168 */       int token = ((Keyword)expr).get();
/* 1169 */       if (token != 339 && token != 336) {
/* 1170 */         throw new SyntaxError(this.lex);
/*      */       }
/* 1172 */     } else if (!(expr instanceof Symbol)) {
/*      */       
/* 1174 */       if (expr instanceof Expr) {
/* 1175 */         int op = ((Expr)expr).getOperator();
/* 1176 */         if (op != 46 && op != 35)
/* 1177 */           throw new SyntaxError(this.lex); 
/*      */       } 
/*      */     } 
/* 1180 */     return (ASTree)CallExpr.makeCall(expr, (ASTree)parseArgumentList(tbl));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String toClassName(ASTree name) throws CompileError {
/* 1186 */     StringBuffer sbuf = new StringBuffer();
/* 1187 */     toClassName(name, sbuf);
/* 1188 */     return sbuf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void toClassName(ASTree name, StringBuffer sbuf) throws CompileError {
/* 1194 */     if (name instanceof Symbol) {
/* 1195 */       sbuf.append(((Symbol)name).get());
/*      */       return;
/*      */     } 
/* 1198 */     if (name instanceof Expr) {
/* 1199 */       Expr expr = (Expr)name;
/* 1200 */       if (expr.getOperator() == 46) {
/* 1201 */         toClassName(expr.oprand1(), sbuf);
/* 1202 */         sbuf.append('.');
/* 1203 */         toClassName(expr.oprand2(), sbuf);
/*      */         
/*      */         return;
/*      */       } 
/*      */     } 
/* 1208 */     throw new CompileError("bad static member access", this.lex);
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
/*      */   private ASTree parsePrimaryExpr(SymbolTable tbl) throws CompileError {
/*      */     String name;
/*      */     Declarator decl;
/*      */     ASTree expr;
/*      */     int t;
/* 1227 */     switch (t = this.lex.get()) {
/*      */       case 336:
/*      */       case 339:
/*      */       case 410:
/*      */       case 411:
/*      */       case 412:
/* 1233 */         return (ASTree)new Keyword(t);
/*      */       case 400:
/* 1235 */         name = this.lex.getString();
/* 1236 */         decl = tbl.lookup(name);
/* 1237 */         if (decl == null) {
/* 1238 */           return (ASTree)new Member(name);
/*      */         }
/* 1240 */         return (ASTree)new Variable(name, decl);
/*      */       case 406:
/* 1242 */         return (ASTree)new StringL(this.lex.getString());
/*      */       case 328:
/* 1244 */         return (ASTree)parseNew(tbl);
/*      */       case 40:
/* 1246 */         expr = parseExpression(tbl);
/* 1247 */         if (this.lex.get() == 41) {
/* 1248 */           return expr;
/*      */         }
/* 1250 */         throw new CompileError(") is missing", this.lex);
/*      */     } 
/* 1252 */     if (isBuiltinType(t) || t == 344) {
/* 1253 */       int dim = parseArrayDimension();
/* 1254 */       if (this.lex.get() == 46 && this.lex.get() == 307) {
/* 1255 */         return parseDotClass(t, dim);
/*      */       }
/*      */     } 
/* 1258 */     throw new SyntaxError(this.lex);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private NewExpr parseNew(SymbolTable tbl) throws CompileError {
/* 1267 */     ArrayInit init = null;
/* 1268 */     int t = this.lex.lookAhead();
/* 1269 */     if (isBuiltinType(t)) {
/* 1270 */       this.lex.get();
/* 1271 */       ASTList size = parseArraySize(tbl);
/* 1272 */       if (this.lex.lookAhead() == 123) {
/* 1273 */         init = parseArrayInitializer(tbl);
/*      */       }
/* 1275 */       return new NewExpr(t, size, init);
/*      */     } 
/* 1277 */     if (t == 400) {
/* 1278 */       ASTList name = parseClassType(tbl);
/* 1279 */       t = this.lex.lookAhead();
/* 1280 */       if (t == 40) {
/* 1281 */         ASTList args = parseArgumentList(tbl);
/* 1282 */         return new NewExpr(name, args);
/*      */       } 
/* 1284 */       if (t == 91) {
/* 1285 */         ASTList size = parseArraySize(tbl);
/* 1286 */         if (this.lex.lookAhead() == 123) {
/* 1287 */           init = parseArrayInitializer(tbl);
/*      */         }
/* 1289 */         return NewExpr.makeObjectArray(name, size, init);
/*      */       } 
/*      */     } 
/*      */     
/* 1293 */     throw new SyntaxError(this.lex);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private ASTList parseArraySize(SymbolTable tbl) throws CompileError {
/* 1299 */     ASTList list = null;
/* 1300 */     while (this.lex.lookAhead() == 91) {
/* 1301 */       list = ASTList.append(list, parseArrayIndex(tbl));
/*      */     }
/* 1303 */     return list;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private ASTree parseArrayIndex(SymbolTable tbl) throws CompileError {
/* 1309 */     this.lex.get();
/* 1310 */     if (this.lex.lookAhead() == 93) {
/* 1311 */       this.lex.get();
/* 1312 */       return null;
/*      */     } 
/*      */     
/* 1315 */     ASTree index = parseExpression(tbl);
/* 1316 */     if (this.lex.get() != 93) {
/* 1317 */       throw new CompileError("] is missing", this.lex);
/*      */     }
/* 1319 */     return index;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ASTList parseArgumentList(SymbolTable tbl) throws CompileError {
/* 1326 */     if (this.lex.get() != 40) {
/* 1327 */       throw new CompileError("( is missing", this.lex);
/*      */     }
/* 1329 */     ASTList list = null;
/* 1330 */     if (this.lex.lookAhead() != 41)
/*      */       while (true) {
/* 1332 */         list = ASTList.append(list, parseExpression(tbl));
/* 1333 */         if (this.lex.lookAhead() == 44) {
/* 1334 */           this.lex.get();
/*      */           continue;
/*      */         } 
/*      */         break;
/*      */       }  
/* 1339 */     if (this.lex.get() != 41) {
/* 1340 */       throw new CompileError(") is missing", this.lex);
/*      */     }
/* 1342 */     return list;
/*      */   }
/*      */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\Parser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */