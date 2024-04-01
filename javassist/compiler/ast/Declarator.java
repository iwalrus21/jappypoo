/*     */ package javassist.compiler.ast;
/*     */ 
/*     */ import javassist.compiler.CompileError;
/*     */ import javassist.compiler.TokenId;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Declarator
/*     */   extends ASTList
/*     */   implements TokenId
/*     */ {
/*     */   protected int varType;
/*     */   protected int arrayDim;
/*     */   protected int localVar;
/*     */   protected String qualifiedClass;
/*     */   
/*     */   public Declarator(int type, int dim) {
/*  32 */     super(null);
/*  33 */     this.varType = type;
/*  34 */     this.arrayDim = dim;
/*  35 */     this.localVar = -1;
/*  36 */     this.qualifiedClass = null;
/*     */   }
/*     */   
/*     */   public Declarator(ASTList className, int dim) {
/*  40 */     super(null);
/*  41 */     this.varType = 307;
/*  42 */     this.arrayDim = dim;
/*  43 */     this.localVar = -1;
/*  44 */     this.qualifiedClass = astToClassName(className, '/');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Declarator(int type, String jvmClassName, int dim, int var, Symbol sym) {
/*  51 */     super(null);
/*  52 */     this.varType = type;
/*  53 */     this.arrayDim = dim;
/*  54 */     this.localVar = var;
/*  55 */     this.qualifiedClass = jvmClassName;
/*  56 */     setLeft(sym);
/*  57 */     append(this, null);
/*     */   }
/*     */   
/*     */   public Declarator make(Symbol sym, int dim, ASTree init) {
/*  61 */     Declarator d = new Declarator(this.varType, this.arrayDim + dim);
/*  62 */     d.qualifiedClass = this.qualifiedClass;
/*  63 */     d.setLeft(sym);
/*  64 */     append(d, init);
/*  65 */     return d;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getType() {
/*  71 */     return this.varType;
/*     */   } public int getArrayDim() {
/*  73 */     return this.arrayDim;
/*     */   } public void addArrayDim(int d) {
/*  75 */     this.arrayDim += d;
/*     */   } public String getClassName() {
/*  77 */     return this.qualifiedClass;
/*     */   } public void setClassName(String s) {
/*  79 */     this.qualifiedClass = s;
/*     */   } public Symbol getVariable() {
/*  81 */     return (Symbol)getLeft();
/*     */   } public void setVariable(Symbol sym) {
/*  83 */     setLeft(sym);
/*     */   }
/*     */   public ASTree getInitializer() {
/*  86 */     ASTList t = tail();
/*  87 */     if (t != null) {
/*  88 */       return t.head();
/*     */     }
/*  90 */     return null;
/*     */   }
/*     */   public void setLocalVar(int n) {
/*  93 */     this.localVar = n;
/*     */   } public int getLocalVar() {
/*  95 */     return this.localVar;
/*     */   } public String getTag() {
/*  97 */     return "decl";
/*     */   }
/*     */   public void accept(Visitor v) throws CompileError {
/* 100 */     v.atDeclarator(this);
/*     */   }
/*     */   
/*     */   public static String astToClassName(ASTList name, char sep) {
/* 104 */     if (name == null) {
/* 105 */       return null;
/*     */     }
/* 107 */     StringBuffer sbuf = new StringBuffer();
/* 108 */     astToClassName(sbuf, name, sep);
/* 109 */     return sbuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static void astToClassName(StringBuffer sbuf, ASTList name, char sep) {
/*     */     while (true) {
/* 115 */       ASTree h = name.head();
/* 116 */       if (h instanceof Symbol) {
/* 117 */         sbuf.append(((Symbol)h).get());
/* 118 */       } else if (h instanceof ASTList) {
/* 119 */         astToClassName(sbuf, (ASTList)h, sep);
/*     */       } 
/* 121 */       name = name.tail();
/* 122 */       if (name == null) {
/*     */         break;
/*     */       }
/* 125 */       sbuf.append(sep);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\ast\Declarator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */