/*     */ package javassist.compiler;
/*     */ 
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtClass;
/*     */ import javassist.CtPrimitiveType;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.bytecode.Bytecode;
/*     */ import javassist.bytecode.Descriptor;
/*     */ import javassist.compiler.ast.ASTList;
/*     */ import javassist.compiler.ast.ASTree;
/*     */ import javassist.compiler.ast.CallExpr;
/*     */ import javassist.compiler.ast.CastExpr;
/*     */ import javassist.compiler.ast.Declarator;
/*     */ import javassist.compiler.ast.Expr;
/*     */ import javassist.compiler.ast.Member;
/*     */ import javassist.compiler.ast.Stmnt;
/*     */ import javassist.compiler.ast.Symbol;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JvstCodeGen
/*     */   extends MemberCodeGen
/*     */ {
/*  27 */   String paramArrayName = null;
/*  28 */   String paramListName = null;
/*  29 */   CtClass[] paramTypeList = null;
/*  30 */   private int paramVarBase = 0;
/*     */   private boolean useParam0 = false;
/*  32 */   private String param0Type = null;
/*     */   public static final String sigName = "$sig";
/*     */   public static final String dollarTypeName = "$type";
/*     */   public static final String clazzName = "$class";
/*  36 */   private CtClass dollarType = null;
/*  37 */   CtClass returnType = null;
/*  38 */   String returnCastName = null;
/*  39 */   private String returnVarName = null;
/*     */   public static final String wrapperCastName = "$w";
/*  41 */   String proceedName = null;
/*     */   public static final String cflowName = "$cflow";
/*  43 */   ProceedHandler procHandler = null;
/*     */   
/*     */   public JvstCodeGen(Bytecode b, CtClass cc, ClassPool cp) {
/*  46 */     super(b, cc, cp);
/*  47 */     setTypeChecker(new JvstTypeChecker(cc, cp, this));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int indexOfParam1() {
/*  53 */     return this.paramVarBase + (this.useParam0 ? 1 : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProceedHandler(ProceedHandler h, String name) {
/*  62 */     this.proceedName = name;
/*  63 */     this.procHandler = h;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addNullIfVoid() {
/*  70 */     if (this.exprType == 344) {
/*  71 */       this.bytecode.addOpcode(1);
/*  72 */       this.exprType = 307;
/*  73 */       this.arrayDim = 0;
/*  74 */       this.className = "java/lang/Object";
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void atMember(Member mem) throws CompileError {
/*  82 */     String name = mem.get();
/*  83 */     if (name.equals(this.paramArrayName)) {
/*  84 */       compileParameterList(this.bytecode, this.paramTypeList, indexOfParam1());
/*  85 */       this.exprType = 307;
/*  86 */       this.arrayDim = 1;
/*  87 */       this.className = "java/lang/Object";
/*     */     }
/*  89 */     else if (name.equals("$sig")) {
/*  90 */       this.bytecode.addLdc(Descriptor.ofMethod(this.returnType, this.paramTypeList));
/*  91 */       this.bytecode.addInvokestatic("javassist/runtime/Desc", "getParams", "(Ljava/lang/String;)[Ljava/lang/Class;");
/*     */       
/*  93 */       this.exprType = 307;
/*  94 */       this.arrayDim = 1;
/*  95 */       this.className = "java/lang/Class";
/*     */     }
/*  97 */     else if (name.equals("$type")) {
/*  98 */       if (this.dollarType == null) {
/*  99 */         throw new CompileError("$type is not available");
/*     */       }
/* 101 */       this.bytecode.addLdc(Descriptor.of(this.dollarType));
/* 102 */       callGetType("getType");
/*     */     }
/* 104 */     else if (name.equals("$class")) {
/* 105 */       if (this.param0Type == null) {
/* 106 */         throw new CompileError("$class is not available");
/*     */       }
/* 108 */       this.bytecode.addLdc(this.param0Type);
/* 109 */       callGetType("getClazz");
/*     */     } else {
/*     */       
/* 112 */       super.atMember(mem);
/*     */     } 
/*     */   }
/*     */   private void callGetType(String method) {
/* 116 */     this.bytecode.addInvokestatic("javassist/runtime/Desc", method, "(Ljava/lang/String;)Ljava/lang/Class;");
/*     */     
/* 118 */     this.exprType = 307;
/* 119 */     this.arrayDim = 0;
/* 120 */     this.className = "java/lang/Class";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void atFieldAssign(Expr expr, int op, ASTree left, ASTree right, boolean doDup) throws CompileError {
/* 126 */     if (left instanceof Member && ((Member)left)
/* 127 */       .get().equals(this.paramArrayName)) {
/* 128 */       if (op != 61) {
/* 129 */         throw new CompileError("bad operator for " + this.paramArrayName);
/*     */       }
/* 131 */       right.accept(this);
/* 132 */       if (this.arrayDim != 1 || this.exprType != 307) {
/* 133 */         throw new CompileError("invalid type for " + this.paramArrayName);
/*     */       }
/* 135 */       atAssignParamList(this.paramTypeList, this.bytecode);
/* 136 */       if (!doDup) {
/* 137 */         this.bytecode.addOpcode(87);
/*     */       }
/*     */     } else {
/* 140 */       super.atFieldAssign(expr, op, left, right, doDup);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void atAssignParamList(CtClass[] params, Bytecode code) throws CompileError {
/* 146 */     if (params == null) {
/*     */       return;
/*     */     }
/* 149 */     int varNo = indexOfParam1();
/* 150 */     int n = params.length;
/* 151 */     for (int i = 0; i < n; i++) {
/* 152 */       code.addOpcode(89);
/* 153 */       code.addIconst(i);
/* 154 */       code.addOpcode(50);
/* 155 */       compileUnwrapValue(params[i], code);
/* 156 */       code.addStore(varNo, params[i]);
/* 157 */       varNo += is2word(this.exprType, this.arrayDim) ? 2 : 1;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void atCastExpr(CastExpr expr) throws CompileError {
/* 162 */     ASTList classname = expr.getClassName();
/* 163 */     if (classname != null && expr.getArrayDim() == 0) {
/* 164 */       ASTree p = classname.head();
/* 165 */       if (p instanceof Symbol && classname.tail() == null) {
/* 166 */         String typename = ((Symbol)p).get();
/* 167 */         if (typename.equals(this.returnCastName)) {
/* 168 */           atCastToRtype(expr);
/*     */           return;
/*     */         } 
/* 171 */         if (typename.equals("$w")) {
/* 172 */           atCastToWrapper(expr);
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/* 178 */     super.atCastExpr(expr);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void atCastToRtype(CastExpr expr) throws CompileError {
/* 186 */     expr.getOprand().accept(this);
/* 187 */     if (this.exprType == 344 || isRefType(this.exprType) || this.arrayDim > 0) {
/* 188 */       compileUnwrapValue(this.returnType, this.bytecode);
/* 189 */     } else if (this.returnType instanceof CtPrimitiveType) {
/* 190 */       CtPrimitiveType pt = (CtPrimitiveType)this.returnType;
/* 191 */       int destType = MemberResolver.descToType(pt.getDescriptor());
/* 192 */       atNumCastExpr(this.exprType, destType);
/* 193 */       this.exprType = destType;
/* 194 */       this.arrayDim = 0;
/* 195 */       this.className = null;
/*     */     } else {
/*     */       
/* 198 */       throw new CompileError("invalid cast");
/*     */     } 
/*     */   }
/*     */   protected void atCastToWrapper(CastExpr expr) throws CompileError {
/* 202 */     expr.getOprand().accept(this);
/* 203 */     if (isRefType(this.exprType) || this.arrayDim > 0) {
/*     */       return;
/*     */     }
/* 206 */     CtClass clazz = this.resolver.lookupClass(this.exprType, this.arrayDim, this.className);
/* 207 */     if (clazz instanceof CtPrimitiveType) {
/* 208 */       CtPrimitiveType pt = (CtPrimitiveType)clazz;
/* 209 */       String wrapper = pt.getWrapperName();
/* 210 */       this.bytecode.addNew(wrapper);
/* 211 */       this.bytecode.addOpcode(89);
/* 212 */       if (pt.getDataSize() > 1) {
/* 213 */         this.bytecode.addOpcode(94);
/*     */       } else {
/* 215 */         this.bytecode.addOpcode(93);
/*     */       } 
/* 217 */       this.bytecode.addOpcode(88);
/* 218 */       this.bytecode.addInvokespecial(wrapper, "<init>", "(" + pt
/* 219 */           .getDescriptor() + ")V");
/*     */       
/* 221 */       this.exprType = 307;
/* 222 */       this.arrayDim = 0;
/* 223 */       this.className = "java/lang/Object";
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void atCallExpr(CallExpr expr) throws CompileError {
/* 231 */     ASTree method = expr.oprand1();
/* 232 */     if (method instanceof Member) {
/* 233 */       String name = ((Member)method).get();
/* 234 */       if (this.procHandler != null && name.equals(this.proceedName)) {
/* 235 */         this.procHandler.doit(this, this.bytecode, (ASTList)expr.oprand2());
/*     */         return;
/*     */       } 
/* 238 */       if (name.equals("$cflow")) {
/* 239 */         atCflow((ASTList)expr.oprand2());
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 244 */     super.atCallExpr(expr);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void atCflow(ASTList cname) throws CompileError {
/* 250 */     StringBuffer sbuf = new StringBuffer();
/* 251 */     if (cname == null || cname.tail() != null) {
/* 252 */       throw new CompileError("bad $cflow");
/*     */     }
/* 254 */     makeCflowName(sbuf, cname.head());
/* 255 */     String name = sbuf.toString();
/* 256 */     Object[] names = this.resolver.getClassPool().lookupCflow(name);
/* 257 */     if (names == null) {
/* 258 */       throw new CompileError("no such $cflow: " + name);
/*     */     }
/* 260 */     this.bytecode.addGetstatic((String)names[0], (String)names[1], "Ljavassist/runtime/Cflow;");
/*     */     
/* 262 */     this.bytecode.addInvokevirtual("javassist.runtime.Cflow", "value", "()I");
/*     */     
/* 264 */     this.exprType = 324;
/* 265 */     this.arrayDim = 0;
/* 266 */     this.className = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void makeCflowName(StringBuffer sbuf, ASTree name) throws CompileError {
/* 277 */     if (name instanceof Symbol) {
/* 278 */       sbuf.append(((Symbol)name).get());
/*     */       return;
/*     */     } 
/* 281 */     if (name instanceof Expr) {
/* 282 */       Expr expr = (Expr)name;
/* 283 */       if (expr.getOperator() == 46) {
/* 284 */         makeCflowName(sbuf, expr.oprand1());
/* 285 */         sbuf.append('.');
/* 286 */         makeCflowName(sbuf, expr.oprand2());
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 291 */     throw new CompileError("bad $cflow");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isParamListName(ASTList args) {
/* 298 */     if (this.paramTypeList != null && args != null && args
/* 299 */       .tail() == null) {
/* 300 */       ASTree left = args.head();
/* 301 */       return (left instanceof Member && ((Member)left)
/* 302 */         .get().equals(this.paramListName));
/*     */     } 
/*     */     
/* 305 */     return false;
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
/*     */   public int getMethodArgsLength(ASTList args) {
/* 318 */     String pname = this.paramListName;
/* 319 */     int n = 0;
/* 320 */     while (args != null) {
/* 321 */       ASTree a = args.head();
/* 322 */       if (a instanceof Member && ((Member)a).get().equals(pname)) {
/* 323 */         if (this.paramTypeList != null) {
/* 324 */           n += this.paramTypeList.length;
/*     */         }
/*     */       } else {
/* 327 */         n++;
/*     */       } 
/* 329 */       args = args.tail();
/*     */     } 
/*     */     
/* 332 */     return n;
/*     */   }
/*     */ 
/*     */   
/*     */   public void atMethodArgs(ASTList args, int[] types, int[] dims, String[] cnames) throws CompileError {
/* 337 */     CtClass[] params = this.paramTypeList;
/* 338 */     String pname = this.paramListName;
/* 339 */     int i = 0;
/* 340 */     while (args != null) {
/* 341 */       ASTree a = args.head();
/* 342 */       if (a instanceof Member && ((Member)a).get().equals(pname)) {
/* 343 */         if (params != null) {
/* 344 */           int n = params.length;
/* 345 */           int regno = indexOfParam1();
/* 346 */           for (int k = 0; k < n; k++) {
/* 347 */             CtClass p = params[k];
/* 348 */             regno += this.bytecode.addLoad(regno, p);
/* 349 */             setType(p);
/* 350 */             types[i] = this.exprType;
/* 351 */             dims[i] = this.arrayDim;
/* 352 */             cnames[i] = this.className;
/* 353 */             i++;
/*     */           } 
/*     */         } 
/*     */       } else {
/*     */         
/* 358 */         a.accept(this);
/* 359 */         types[i] = this.exprType;
/* 360 */         dims[i] = this.arrayDim;
/* 361 */         cnames[i] = this.className;
/* 362 */         i++;
/*     */       } 
/*     */       
/* 365 */       args = args.tail();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void compileInvokeSpecial(ASTree target, int methodIndex, String descriptor, ASTList args) throws CompileError {
/* 400 */     target.accept(this);
/* 401 */     int nargs = getMethodArgsLength(args);
/* 402 */     atMethodArgs(args, new int[nargs], new int[nargs], new String[nargs]);
/*     */     
/* 404 */     this.bytecode.addInvokespecial(methodIndex, descriptor);
/* 405 */     setReturnType(descriptor, false, false);
/* 406 */     addNullIfVoid();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void atReturnStmnt(Stmnt st) throws CompileError {
/* 413 */     ASTree result = st.getLeft();
/* 414 */     if (result != null && this.returnType == CtClass.voidType) {
/* 415 */       compileExpr(result);
/* 416 */       if (is2word(this.exprType, this.arrayDim)) {
/* 417 */         this.bytecode.addOpcode(88);
/* 418 */       } else if (this.exprType != 344) {
/* 419 */         this.bytecode.addOpcode(87);
/*     */       } 
/* 421 */       result = null;
/*     */     } 
/*     */     
/* 424 */     atReturnStmnt2(result);
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
/*     */   public int recordReturnType(CtClass type, String castName, String resultName, SymbolTable tbl) throws CompileError {
/* 440 */     this.returnType = type;
/* 441 */     this.returnCastName = castName;
/* 442 */     this.returnVarName = resultName;
/* 443 */     if (resultName == null) {
/* 444 */       return -1;
/*     */     }
/* 446 */     int varNo = getMaxLocals();
/* 447 */     int locals = varNo + recordVar(type, resultName, varNo, tbl);
/* 448 */     setMaxLocals(locals);
/* 449 */     return varNo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void recordType(CtClass t) {
/* 457 */     this.dollarType = t;
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
/*     */   public int recordParams(CtClass[] params, boolean isStatic, String prefix, String paramVarName, String paramsName, SymbolTable tbl) throws CompileError {
/* 470 */     return recordParams(params, isStatic, prefix, paramVarName, paramsName, !isStatic, 0, 
/* 471 */         getThisName(), tbl);
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
/*     */   public int recordParams(CtClass[] params, boolean isStatic, String prefix, String paramVarName, String paramsName, boolean use0, int paramBase, String target, SymbolTable tbl) throws CompileError {
/* 501 */     this.paramTypeList = params;
/* 502 */     this.paramArrayName = paramVarName;
/* 503 */     this.paramListName = paramsName;
/* 504 */     this.paramVarBase = paramBase;
/* 505 */     this.useParam0 = use0;
/*     */     
/* 507 */     if (target != null) {
/* 508 */       this.param0Type = MemberResolver.jvmToJavaName(target);
/*     */     }
/* 510 */     this.inStaticMethod = isStatic;
/* 511 */     int varNo = paramBase;
/* 512 */     if (use0) {
/* 513 */       String varName = prefix + "0";
/*     */       
/* 515 */       Declarator decl = new Declarator(307, MemberResolver.javaToJvmName(target), 0, varNo++, new Symbol(varName));
/*     */       
/* 517 */       tbl.append(varName, decl);
/*     */     } 
/*     */     
/* 520 */     for (int i = 0; i < params.length; i++) {
/* 521 */       varNo += recordVar(params[i], prefix + (i + 1), varNo, tbl);
/*     */     }
/* 523 */     if (getMaxLocals() < varNo) {
/* 524 */       setMaxLocals(varNo);
/*     */     }
/* 526 */     return varNo;
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
/*     */   public int recordVariable(CtClass type, String varName, SymbolTable tbl) throws CompileError {
/* 538 */     if (varName == null) {
/* 539 */       return -1;
/*     */     }
/* 541 */     int varNo = getMaxLocals();
/* 542 */     int locals = varNo + recordVar(type, varName, varNo, tbl);
/* 543 */     setMaxLocals(locals);
/* 544 */     return varNo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int recordVar(CtClass cc, String varName, int varNo, SymbolTable tbl) throws CompileError {
/* 551 */     if (cc == CtClass.voidType) {
/* 552 */       this.exprType = 307;
/* 553 */       this.arrayDim = 0;
/* 554 */       this.className = "java/lang/Object";
/*     */     } else {
/*     */       
/* 557 */       setType(cc);
/*     */     } 
/* 559 */     Declarator decl = new Declarator(this.exprType, this.className, this.arrayDim, varNo, new Symbol(varName));
/*     */ 
/*     */     
/* 562 */     tbl.append(varName, decl);
/* 563 */     return is2word(this.exprType, this.arrayDim) ? 2 : 1;
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
/*     */   public void recordVariable(String typeDesc, String varName, int varNo, SymbolTable tbl) throws CompileError {
/* 577 */     int dim = 0; char c;
/* 578 */     while ((c = typeDesc.charAt(dim)) == '[') {
/* 579 */       dim++;
/*     */     }
/* 581 */     int type = MemberResolver.descToType(c);
/* 582 */     String cname = null;
/* 583 */     if (type == 307) {
/* 584 */       if (dim == 0) {
/* 585 */         cname = typeDesc.substring(1, typeDesc.length() - 1);
/*     */       } else {
/* 587 */         cname = typeDesc.substring(dim + 1, typeDesc.length() - 1);
/*     */       } 
/*     */     }
/* 590 */     Declarator decl = new Declarator(type, cname, dim, varNo, new Symbol(varName));
/*     */     
/* 592 */     tbl.append(varName, decl);
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
/*     */   public static int compileParameterList(Bytecode code, CtClass[] params, int regno) {
/* 606 */     if (params == null) {
/* 607 */       code.addIconst(0);
/* 608 */       code.addAnewarray("java.lang.Object");
/* 609 */       return 1;
/*     */     } 
/*     */     
/* 612 */     CtClass[] args = new CtClass[1];
/* 613 */     int n = params.length;
/* 614 */     code.addIconst(n);
/* 615 */     code.addAnewarray("java.lang.Object");
/* 616 */     for (int i = 0; i < n; i++) {
/* 617 */       code.addOpcode(89);
/* 618 */       code.addIconst(i);
/* 619 */       if (params[i].isPrimitive()) {
/* 620 */         CtPrimitiveType pt = (CtPrimitiveType)params[i];
/* 621 */         String wrapper = pt.getWrapperName();
/* 622 */         code.addNew(wrapper);
/* 623 */         code.addOpcode(89);
/* 624 */         int s = code.addLoad(regno, (CtClass)pt);
/* 625 */         regno += s;
/* 626 */         args[0] = (CtClass)pt;
/* 627 */         code.addInvokespecial(wrapper, "<init>", 
/* 628 */             Descriptor.ofMethod(CtClass.voidType, args));
/*     */       }
/*     */       else {
/*     */         
/* 632 */         code.addAload(regno);
/* 633 */         regno++;
/*     */       } 
/*     */       
/* 636 */       code.addOpcode(83);
/*     */     } 
/*     */     
/* 639 */     return 8;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void compileUnwrapValue(CtClass type, Bytecode code) throws CompileError {
/* 646 */     if (type == CtClass.voidType) {
/* 647 */       addNullIfVoid();
/*     */       
/*     */       return;
/*     */     } 
/* 651 */     if (this.exprType == 344) {
/* 652 */       throw new CompileError("invalid type for " + this.returnCastName);
/*     */     }
/* 654 */     if (type instanceof CtPrimitiveType) {
/* 655 */       CtPrimitiveType pt = (CtPrimitiveType)type;
/*     */       
/* 657 */       String wrapper = pt.getWrapperName();
/* 658 */       code.addCheckcast(wrapper);
/* 659 */       code.addInvokevirtual(wrapper, pt.getGetMethodName(), pt
/* 660 */           .getGetMethodDescriptor());
/* 661 */       setType(type);
/*     */     } else {
/*     */       
/* 664 */       code.addCheckcast(type);
/* 665 */       setType(type);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setType(CtClass type) throws CompileError {
/* 673 */     setType(type, 0);
/*     */   }
/*     */   
/*     */   private void setType(CtClass type, int dim) throws CompileError {
/* 677 */     if (type.isPrimitive()) {
/* 678 */       CtPrimitiveType pt = (CtPrimitiveType)type;
/* 679 */       this.exprType = MemberResolver.descToType(pt.getDescriptor());
/* 680 */       this.arrayDim = dim;
/* 681 */       this.className = null;
/*     */     }
/* 683 */     else if (type.isArray()) {
/*     */       try {
/* 685 */         setType(type.getComponentType(), dim + 1);
/*     */       }
/* 687 */       catch (NotFoundException e) {
/* 688 */         throw new CompileError("undefined type: " + type.getName());
/*     */       } 
/*     */     } else {
/* 691 */       this.exprType = 307;
/* 692 */       this.arrayDim = dim;
/* 693 */       this.className = MemberResolver.javaToJvmName(type.getName());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void doNumCast(CtClass type) throws CompileError {
/* 700 */     if (this.arrayDim == 0 && !isRefType(this.exprType))
/* 701 */       if (type instanceof CtPrimitiveType) {
/* 702 */         CtPrimitiveType pt = (CtPrimitiveType)type;
/* 703 */         atNumCastExpr(this.exprType, 
/* 704 */             MemberResolver.descToType(pt.getDescriptor()));
/*     */       } else {
/*     */         
/* 707 */         throw new CompileError("type mismatch");
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\JvstCodeGen.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */