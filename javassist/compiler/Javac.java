/*     */ package javassist.compiler;
/*     */ 
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.CtBehavior;
/*     */ import javassist.CtClass;
/*     */ import javassist.CtConstructor;
/*     */ import javassist.CtField;
/*     */ import javassist.CtMember;
/*     */ import javassist.CtMethod;
/*     */ import javassist.CtPrimitiveType;
/*     */ import javassist.Modifier;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.Bytecode;
/*     */ import javassist.bytecode.CodeAttribute;
/*     */ import javassist.bytecode.LocalVariableAttribute;
/*     */ import javassist.compiler.ast.ASTList;
/*     */ import javassist.compiler.ast.ASTree;
/*     */ import javassist.compiler.ast.CallExpr;
/*     */ import javassist.compiler.ast.Declarator;
/*     */ import javassist.compiler.ast.Expr;
/*     */ import javassist.compiler.ast.FieldDecl;
/*     */ import javassist.compiler.ast.Member;
/*     */ import javassist.compiler.ast.MethodDecl;
/*     */ import javassist.compiler.ast.Stmnt;
/*     */ import javassist.compiler.ast.Symbol;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Javac
/*     */ {
/*     */   JvstCodeGen gen;
/*     */   SymbolTable stable;
/*     */   private Bytecode bytecode;
/*     */   public static final String param0Name = "$0";
/*     */   public static final String resultVarName = "$_";
/*     */   public static final String proceedName = "$proceed";
/*     */   
/*     */   public Javac(CtClass thisClass) {
/*  53 */     this(new Bytecode(thisClass.getClassFile2().getConstPool(), 0, 0), thisClass);
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
/*     */   public Javac(Bytecode b, CtClass thisClass) {
/*  66 */     this.gen = new JvstCodeGen(b, thisClass, thisClass.getClassPool());
/*  67 */     this.stable = new SymbolTable();
/*  68 */     this.bytecode = b;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Bytecode getBytecode() {
/*  74 */     return this.bytecode;
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
/*     */   public CtMember compile(String src) throws CompileError {
/*  89 */     Parser p = new Parser(new Lex(src));
/*  90 */     ASTList mem = p.parseMember1(this.stable);
/*     */     try {
/*  92 */       if (mem instanceof FieldDecl) {
/*  93 */         return (CtMember)compileField((FieldDecl)mem);
/*     */       }
/*  95 */       CtBehavior cb = compileMethod(p, (MethodDecl)mem);
/*  96 */       CtClass decl = cb.getDeclaringClass();
/*  97 */       cb.getMethodInfo2()
/*  98 */         .rebuildStackMapIf6(decl.getClassPool(), decl
/*  99 */           .getClassFile2());
/* 100 */       return (CtMember)cb;
/*     */     
/*     */     }
/* 103 */     catch (BadBytecode bb) {
/* 104 */       throw new CompileError(bb.getMessage());
/*     */     }
/* 106 */     catch (CannotCompileException e) {
/* 107 */       throw new CompileError(e.getMessage());
/*     */     } 
/*     */   }
/*     */   
/*     */   public static class CtFieldWithInit
/*     */     extends CtField
/*     */   {
/*     */     private ASTree init;
/*     */     
/*     */     CtFieldWithInit(CtClass type, String name, CtClass declaring) throws CannotCompileException {
/* 117 */       super(type, name, declaring);
/* 118 */       this.init = null;
/*     */     }
/*     */     protected void setInit(ASTree i) {
/* 121 */       this.init = i;
/*     */     }
/*     */     protected ASTree getInitAST() {
/* 124 */       return this.init;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CtField compileField(FieldDecl fd) throws CompileError, CannotCompileException {
/* 132 */     Declarator d = fd.getDeclarator();
/*     */     
/* 134 */     CtFieldWithInit f = new CtFieldWithInit(this.gen.resolver.lookupClass(d), d.getVariable().get(), this.gen.getThisClass());
/* 135 */     f.setModifiers(MemberResolver.getModifiers(fd.getModifiers()));
/* 136 */     if (fd.getInit() != null) {
/* 137 */       f.setInit(fd.getInit());
/*     */     }
/* 139 */     return f;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private CtBehavior compileMethod(Parser p, MethodDecl md) throws CompileError {
/* 145 */     int mod = MemberResolver.getModifiers(md.getModifiers());
/* 146 */     CtClass[] plist = this.gen.makeParamList(md);
/* 147 */     CtClass[] tlist = this.gen.makeThrowsList(md);
/* 148 */     recordParams(plist, Modifier.isStatic(mod));
/* 149 */     md = p.parseMethod2(this.stable, md);
/*     */     try {
/* 151 */       if (md.isConstructor()) {
/*     */         
/* 153 */         CtConstructor cons = new CtConstructor(plist, this.gen.getThisClass());
/* 154 */         cons.setModifiers(mod);
/* 155 */         md.accept(this.gen);
/* 156 */         cons.getMethodInfo().setCodeAttribute(this.bytecode
/* 157 */             .toCodeAttribute());
/* 158 */         cons.setExceptionTypes(tlist);
/* 159 */         return (CtBehavior)cons;
/*     */       } 
/*     */       
/* 162 */       Declarator r = md.getReturn();
/* 163 */       CtClass rtype = this.gen.resolver.lookupClass(r);
/* 164 */       recordReturnType(rtype, false);
/*     */       
/* 166 */       CtMethod method = new CtMethod(rtype, r.getVariable().get(), plist, this.gen.getThisClass());
/* 167 */       method.setModifiers(mod);
/* 168 */       this.gen.setThisMethod(method);
/* 169 */       md.accept(this.gen);
/* 170 */       if (md.getBody() != null) {
/* 171 */         method.getMethodInfo().setCodeAttribute(this.bytecode
/* 172 */             .toCodeAttribute());
/*     */       } else {
/* 174 */         method.setModifiers(mod | 0x400);
/*     */       } 
/* 176 */       method.setExceptionTypes(tlist);
/* 177 */       return (CtBehavior)method;
/*     */     
/*     */     }
/* 180 */     catch (NotFoundException e) {
/* 181 */       throw new CompileError(e.toString());
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
/*     */   public Bytecode compileBody(CtBehavior method, String src) throws CompileError {
/*     */     try {
/*     */       CtClass rtype;
/* 195 */       int mod = method.getModifiers();
/* 196 */       recordParams(method.getParameterTypes(), Modifier.isStatic(mod));
/*     */ 
/*     */       
/* 199 */       if (method instanceof CtMethod) {
/* 200 */         this.gen.setThisMethod((CtMethod)method);
/* 201 */         rtype = ((CtMethod)method).getReturnType();
/*     */       } else {
/*     */         
/* 204 */         rtype = CtClass.voidType;
/*     */       } 
/* 206 */       recordReturnType(rtype, false);
/* 207 */       boolean isVoid = (rtype == CtClass.voidType);
/*     */       
/* 209 */       if (src == null) {
/* 210 */         makeDefaultBody(this.bytecode, rtype);
/*     */       } else {
/* 212 */         Parser p = new Parser(new Lex(src));
/* 213 */         SymbolTable stb = new SymbolTable(this.stable);
/* 214 */         Stmnt s = p.parseStatement(stb);
/* 215 */         if (p.hasMore()) {
/* 216 */           throw new CompileError("the method/constructor body must be surrounded by {}");
/*     */         }
/*     */         
/* 219 */         boolean callSuper = false;
/* 220 */         if (method instanceof CtConstructor) {
/* 221 */           callSuper = !((CtConstructor)method).isClassInitializer();
/*     */         }
/* 223 */         this.gen.atMethodBody(s, callSuper, isVoid);
/*     */       } 
/*     */       
/* 226 */       return this.bytecode;
/*     */     }
/* 228 */     catch (NotFoundException e) {
/* 229 */       throw new CompileError(e.toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void makeDefaultBody(Bytecode b, CtClass type) {
/*     */     int op, value;
/* 236 */     if (type instanceof CtPrimitiveType) {
/* 237 */       CtPrimitiveType pt = (CtPrimitiveType)type;
/* 238 */       op = pt.getReturnOp();
/* 239 */       if (op == 175) {
/* 240 */         value = 14;
/* 241 */       } else if (op == 174) {
/* 242 */         value = 11;
/* 243 */       } else if (op == 173) {
/* 244 */         value = 9;
/* 245 */       } else if (op == 177) {
/* 246 */         value = 0;
/*     */       } else {
/* 248 */         value = 3;
/*     */       } 
/*     */     } else {
/* 251 */       op = 176;
/* 252 */       value = 1;
/*     */     } 
/*     */     
/* 255 */     if (value != 0) {
/* 256 */       b.addOpcode(value);
/*     */     }
/* 258 */     b.addOpcode(op);
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
/*     */   public boolean recordLocalVariables(CodeAttribute ca, int pc) throws CompileError {
/* 275 */     LocalVariableAttribute va = (LocalVariableAttribute)ca.getAttribute("LocalVariableTable");
/* 276 */     if (va == null) {
/* 277 */       return false;
/*     */     }
/* 279 */     int n = va.tableLength();
/* 280 */     for (int i = 0; i < n; i++) {
/* 281 */       int start = va.startPc(i);
/* 282 */       int len = va.codeLength(i);
/* 283 */       if (start <= pc && pc < start + len) {
/* 284 */         this.gen.recordVariable(va.descriptor(i), va.variableName(i), va
/* 285 */             .index(i), this.stable);
/*     */       }
/*     */     } 
/* 288 */     return true;
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
/*     */   public boolean recordParamNames(CodeAttribute ca, int numOfLocalVars) throws CompileError {
/* 305 */     LocalVariableAttribute va = (LocalVariableAttribute)ca.getAttribute("LocalVariableTable");
/* 306 */     if (va == null) {
/* 307 */       return false;
/*     */     }
/* 309 */     int n = va.tableLength();
/* 310 */     for (int i = 0; i < n; i++) {
/* 311 */       int index = va.index(i);
/* 312 */       if (index < numOfLocalVars) {
/* 313 */         this.gen.recordVariable(va.descriptor(i), va.variableName(i), index, this.stable);
/*     */       }
/*     */     } 
/*     */     
/* 317 */     return true;
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
/*     */   public int recordParams(CtClass[] params, boolean isStatic) throws CompileError {
/* 334 */     return this.gen.recordParams(params, isStatic, "$", "$args", "$$", this.stable);
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
/*     */   public int recordParams(String target, CtClass[] params, boolean use0, int varNo, boolean isStatic) throws CompileError {
/* 362 */     return this.gen.recordParams(params, isStatic, "$", "$args", "$$", use0, varNo, target, this.stable);
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
/*     */   public void setMaxLocals(int max) {
/* 376 */     this.gen.setMaxLocals(max);
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
/*     */   public int recordReturnType(CtClass type, boolean useResultVar) throws CompileError {
/* 396 */     this.gen.recordType(type);
/* 397 */     return this.gen.recordReturnType(type, "$r", useResultVar ? "$_" : null, this.stable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void recordType(CtClass t) {
/* 408 */     this.gen.recordType(t);
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
/*     */   public int recordVariable(CtClass type, String name) throws CompileError {
/* 420 */     return this.gen.recordVariable(type, name, this.stable);
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
/*     */   public void recordProceed(String target, String method) throws CompileError {
/* 435 */     Parser p = new Parser(new Lex(target));
/* 436 */     final ASTree texpr = p.parseExpression(this.stable);
/* 437 */     final String m = method;
/*     */     
/* 439 */     ProceedHandler h = new ProceedHandler()
/*     */       {
/*     */         public void doit(JvstCodeGen gen, Bytecode b, ASTList args) throws CompileError {
/*     */           Expr expr;
/* 443 */           Member member = new Member(m);
/* 444 */           if (texpr != null) {
/* 445 */             expr = Expr.make(46, texpr, (ASTree)member);
/*     */           }
/* 447 */           CallExpr callExpr = CallExpr.makeCall((ASTree)expr, (ASTree)args);
/* 448 */           gen.compileExpr((ASTree)callExpr);
/* 449 */           gen.addNullIfVoid();
/*     */         }
/*     */ 
/*     */         
/*     */         public void setReturnType(JvstTypeChecker check, ASTList args) throws CompileError {
/*     */           Expr expr;
/* 455 */           Member member = new Member(m);
/* 456 */           if (texpr != null) {
/* 457 */             expr = Expr.make(46, texpr, (ASTree)member);
/*     */           }
/* 459 */           CallExpr callExpr = CallExpr.makeCall((ASTree)expr, (ASTree)args);
/* 460 */           callExpr.accept(check);
/* 461 */           check.addNullIfVoid();
/*     */         }
/*     */       };
/*     */     
/* 465 */     this.gen.setProceedHandler(h, "$proceed");
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
/*     */   public void recordStaticProceed(String targetClass, String method) throws CompileError {
/* 480 */     final String c = targetClass;
/* 481 */     final String m = method;
/*     */     
/* 483 */     ProceedHandler h = new ProceedHandler()
/*     */       {
/*     */         public void doit(JvstCodeGen gen, Bytecode b, ASTList args) throws CompileError
/*     */         {
/* 487 */           Expr expr = Expr.make(35, (ASTree)new Symbol(c), (ASTree)new Member(m));
/*     */           
/* 489 */           CallExpr callExpr = CallExpr.makeCall((ASTree)expr, (ASTree)args);
/* 490 */           gen.compileExpr((ASTree)callExpr);
/* 491 */           gen.addNullIfVoid();
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void setReturnType(JvstTypeChecker check, ASTList args) throws CompileError {
/* 497 */           Expr expr = Expr.make(35, (ASTree)new Symbol(c), (ASTree)new Member(m));
/*     */           
/* 499 */           CallExpr callExpr = CallExpr.makeCall((ASTree)expr, (ASTree)args);
/* 500 */           callExpr.accept(check);
/* 501 */           check.addNullIfVoid();
/*     */         }
/*     */       };
/*     */     
/* 505 */     this.gen.setProceedHandler(h, "$proceed");
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
/*     */   public void recordSpecialProceed(String target, final String classname, final String methodname, final String descriptor, final int methodIndex) throws CompileError {
/* 524 */     Parser p = new Parser(new Lex(target));
/* 525 */     final ASTree texpr = p.parseExpression(this.stable);
/*     */     
/* 527 */     ProceedHandler h = new ProceedHandler()
/*     */       {
/*     */         public void doit(JvstCodeGen gen, Bytecode b, ASTList args) throws CompileError
/*     */         {
/* 531 */           gen.compileInvokeSpecial(texpr, methodIndex, descriptor, args);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void setReturnType(JvstTypeChecker c, ASTList args) throws CompileError {
/* 537 */           c.compileInvokeSpecial(texpr, classname, methodname, descriptor, args);
/*     */         }
/*     */       };
/*     */ 
/*     */     
/* 542 */     this.gen.setProceedHandler(h, "$proceed");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void recordProceed(ProceedHandler h) {
/* 549 */     this.gen.setProceedHandler(h, "$proceed");
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
/*     */   public void compileStmnt(String src) throws CompileError {
/* 562 */     Parser p = new Parser(new Lex(src));
/* 563 */     SymbolTable stb = new SymbolTable(this.stable);
/* 564 */     while (p.hasMore()) {
/* 565 */       Stmnt s = p.parseStatement(stb);
/* 566 */       if (s != null) {
/* 567 */         s.accept(this.gen);
/*     */       }
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
/*     */   public void compileExpr(String src) throws CompileError {
/* 581 */     ASTree e = parseExpr(src, this.stable);
/* 582 */     compileExpr(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ASTree parseExpr(String src, SymbolTable st) throws CompileError {
/* 591 */     Parser p = new Parser(new Lex(src));
/* 592 */     return p.parseExpression(st);
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
/*     */   public void compileExpr(ASTree e) throws CompileError {
/* 605 */     if (e != null)
/* 606 */       this.gen.compileExpr(e); 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\Javac.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */