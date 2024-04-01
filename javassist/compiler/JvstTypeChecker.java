/*     */ package javassist.compiler;
/*     */ 
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtClass;
/*     */ import javassist.CtPrimitiveType;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.compiler.ast.ASTList;
/*     */ import javassist.compiler.ast.ASTree;
/*     */ import javassist.compiler.ast.CallExpr;
/*     */ import javassist.compiler.ast.CastExpr;
/*     */ import javassist.compiler.ast.Expr;
/*     */ import javassist.compiler.ast.Member;
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
/*     */ public class JvstTypeChecker
/*     */   extends TypeChecker
/*     */ {
/*     */   private JvstCodeGen codeGen;
/*     */   
/*     */   public JvstTypeChecker(CtClass cc, ClassPool cp, JvstCodeGen gen) {
/*  29 */     super(cc, cp);
/*  30 */     this.codeGen = gen;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addNullIfVoid() {
/*  37 */     if (this.exprType == 344) {
/*  38 */       this.exprType = 307;
/*  39 */       this.arrayDim = 0;
/*  40 */       this.className = "java/lang/Object";
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void atMember(Member mem) throws CompileError {
/*  48 */     String name = mem.get();
/*  49 */     if (name.equals(this.codeGen.paramArrayName)) {
/*  50 */       this.exprType = 307;
/*  51 */       this.arrayDim = 1;
/*  52 */       this.className = "java/lang/Object";
/*     */     }
/*  54 */     else if (name.equals("$sig")) {
/*  55 */       this.exprType = 307;
/*  56 */       this.arrayDim = 1;
/*  57 */       this.className = "java/lang/Class";
/*     */     }
/*  59 */     else if (name.equals("$type") || name
/*  60 */       .equals("$class")) {
/*  61 */       this.exprType = 307;
/*  62 */       this.arrayDim = 0;
/*  63 */       this.className = "java/lang/Class";
/*     */     } else {
/*     */       
/*  66 */       super.atMember(mem);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void atFieldAssign(Expr expr, int op, ASTree left, ASTree right) throws CompileError {
/*  72 */     if (left instanceof Member && ((Member)left)
/*  73 */       .get().equals(this.codeGen.paramArrayName)) {
/*  74 */       right.accept(this);
/*  75 */       CtClass[] params = this.codeGen.paramTypeList;
/*  76 */       if (params == null) {
/*     */         return;
/*     */       }
/*  79 */       int n = params.length;
/*  80 */       for (int i = 0; i < n; i++) {
/*  81 */         compileUnwrapValue(params[i]);
/*     */       }
/*     */     } else {
/*  84 */       super.atFieldAssign(expr, op, left, right);
/*     */     } 
/*     */   }
/*     */   public void atCastExpr(CastExpr expr) throws CompileError {
/*  88 */     ASTList classname = expr.getClassName();
/*  89 */     if (classname != null && expr.getArrayDim() == 0) {
/*  90 */       ASTree p = classname.head();
/*  91 */       if (p instanceof Symbol && classname.tail() == null) {
/*  92 */         String typename = ((Symbol)p).get();
/*  93 */         if (typename.equals(this.codeGen.returnCastName)) {
/*  94 */           atCastToRtype(expr);
/*     */           return;
/*     */         } 
/*  97 */         if (typename.equals("$w")) {
/*  98 */           atCastToWrapper(expr);
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/* 104 */     super.atCastExpr(expr);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void atCastToRtype(CastExpr expr) throws CompileError {
/* 112 */     CtClass returnType = this.codeGen.returnType;
/* 113 */     expr.getOprand().accept(this);
/* 114 */     if (this.exprType == 344 || CodeGen.isRefType(this.exprType) || this.arrayDim > 0) {
/* 115 */       compileUnwrapValue(returnType);
/* 116 */     } else if (returnType instanceof CtPrimitiveType) {
/* 117 */       CtPrimitiveType pt = (CtPrimitiveType)returnType;
/* 118 */       int destType = MemberResolver.descToType(pt.getDescriptor());
/* 119 */       this.exprType = destType;
/* 120 */       this.arrayDim = 0;
/* 121 */       this.className = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void atCastToWrapper(CastExpr expr) throws CompileError {
/* 126 */     expr.getOprand().accept(this);
/* 127 */     if (CodeGen.isRefType(this.exprType) || this.arrayDim > 0) {
/*     */       return;
/*     */     }
/* 130 */     CtClass clazz = this.resolver.lookupClass(this.exprType, this.arrayDim, this.className);
/* 131 */     if (clazz instanceof CtPrimitiveType) {
/* 132 */       this.exprType = 307;
/* 133 */       this.arrayDim = 0;
/* 134 */       this.className = "java/lang/Object";
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void atCallExpr(CallExpr expr) throws CompileError {
/* 142 */     ASTree method = expr.oprand1();
/* 143 */     if (method instanceof Member) {
/* 144 */       String name = ((Member)method).get();
/* 145 */       if (this.codeGen.procHandler != null && name
/* 146 */         .equals(this.codeGen.proceedName)) {
/* 147 */         this.codeGen.procHandler.setReturnType(this, (ASTList)expr
/* 148 */             .oprand2());
/*     */         return;
/*     */       } 
/* 151 */       if (name.equals("$cflow")) {
/* 152 */         atCflow((ASTList)expr.oprand2());
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 157 */     super.atCallExpr(expr);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void atCflow(ASTList cname) throws CompileError {
/* 163 */     this.exprType = 324;
/* 164 */     this.arrayDim = 0;
/* 165 */     this.className = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isParamListName(ASTList args) {
/* 172 */     if (this.codeGen.paramTypeList != null && args != null && args
/* 173 */       .tail() == null) {
/* 174 */       ASTree left = args.head();
/* 175 */       return (left instanceof Member && ((Member)left)
/* 176 */         .get().equals(this.codeGen.paramListName));
/*     */     } 
/*     */     
/* 179 */     return false;
/*     */   }
/*     */   
/*     */   public int getMethodArgsLength(ASTList args) {
/* 183 */     String pname = this.codeGen.paramListName;
/* 184 */     int n = 0;
/* 185 */     while (args != null) {
/* 186 */       ASTree a = args.head();
/* 187 */       if (a instanceof Member && ((Member)a).get().equals(pname)) {
/* 188 */         if (this.codeGen.paramTypeList != null) {
/* 189 */           n += this.codeGen.paramTypeList.length;
/*     */         }
/*     */       } else {
/* 192 */         n++;
/*     */       } 
/* 194 */       args = args.tail();
/*     */     } 
/*     */     
/* 197 */     return n;
/*     */   }
/*     */ 
/*     */   
/*     */   public void atMethodArgs(ASTList args, int[] types, int[] dims, String[] cnames) throws CompileError {
/* 202 */     CtClass[] params = this.codeGen.paramTypeList;
/* 203 */     String pname = this.codeGen.paramListName;
/* 204 */     int i = 0;
/* 205 */     while (args != null) {
/* 206 */       ASTree a = args.head();
/* 207 */       if (a instanceof Member && ((Member)a).get().equals(pname)) {
/* 208 */         if (params != null) {
/* 209 */           int n = params.length;
/* 210 */           for (int k = 0; k < n; k++) {
/* 211 */             CtClass p = params[k];
/* 212 */             setType(p);
/* 213 */             types[i] = this.exprType;
/* 214 */             dims[i] = this.arrayDim;
/* 215 */             cnames[i] = this.className;
/* 216 */             i++;
/*     */           } 
/*     */         } 
/*     */       } else {
/*     */         
/* 221 */         a.accept(this);
/* 222 */         types[i] = this.exprType;
/* 223 */         dims[i] = this.arrayDim;
/* 224 */         cnames[i] = this.className;
/* 225 */         i++;
/*     */       } 
/*     */       
/* 228 */       args = args.tail();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void compileInvokeSpecial(ASTree target, String classname, String methodname, String descriptor, ASTList args) throws CompileError {
/* 239 */     target.accept(this);
/* 240 */     int nargs = getMethodArgsLength(args);
/* 241 */     atMethodArgs(args, new int[nargs], new int[nargs], new String[nargs]);
/*     */     
/* 243 */     setReturnType(descriptor);
/* 244 */     addNullIfVoid();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void compileUnwrapValue(CtClass type) throws CompileError {
/* 249 */     if (type == CtClass.voidType) {
/* 250 */       addNullIfVoid();
/*     */     } else {
/* 252 */       setType(type);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setType(CtClass type) throws CompileError {
/* 259 */     setType(type, 0);
/*     */   }
/*     */   
/*     */   private void setType(CtClass type, int dim) throws CompileError {
/* 263 */     if (type.isPrimitive()) {
/* 264 */       CtPrimitiveType pt = (CtPrimitiveType)type;
/* 265 */       this.exprType = MemberResolver.descToType(pt.getDescriptor());
/* 266 */       this.arrayDim = dim;
/* 267 */       this.className = null;
/*     */     }
/* 269 */     else if (type.isArray()) {
/*     */       try {
/* 271 */         setType(type.getComponentType(), dim + 1);
/*     */       }
/* 273 */       catch (NotFoundException e) {
/* 274 */         throw new CompileError("undefined type: " + type.getName());
/*     */       } 
/*     */     } else {
/* 277 */       this.exprType = 307;
/* 278 */       this.arrayDim = dim;
/* 279 */       this.className = MemberResolver.javaToJvmName(type.getName());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\JvstTypeChecker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */