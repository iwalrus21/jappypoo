/*     */ package javassist.compiler.ast;
/*     */ 
/*     */ import javassist.compiler.CompileError;
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
/*     */ public class ASTList
/*     */   extends ASTree
/*     */ {
/*     */   private ASTree left;
/*     */   private ASTList right;
/*     */   
/*     */   public ASTList(ASTree _head, ASTList _tail) {
/*  30 */     this.left = _head;
/*  31 */     this.right = _tail;
/*     */   }
/*     */   
/*     */   public ASTList(ASTree _head) {
/*  35 */     this.left = _head;
/*  36 */     this.right = null;
/*     */   }
/*     */   
/*     */   public static ASTList make(ASTree e1, ASTree e2, ASTree e3) {
/*  40 */     return new ASTList(e1, new ASTList(e2, new ASTList(e3)));
/*     */   }
/*     */   public ASTree getLeft() {
/*  43 */     return this.left;
/*     */   } public ASTree getRight() {
/*  45 */     return this.right;
/*     */   } public void setLeft(ASTree _left) {
/*  47 */     this.left = _left;
/*     */   }
/*     */   public void setRight(ASTree _right) {
/*  50 */     this.right = (ASTList)_right;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ASTree head() {
/*  56 */     return this.left;
/*     */   }
/*     */   public void setHead(ASTree _head) {
/*  59 */     this.left = _head;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ASTList tail() {
/*  65 */     return this.right;
/*     */   }
/*     */   public void setTail(ASTList _tail) {
/*  68 */     this.right = _tail;
/*     */   }
/*     */   public void accept(Visitor v) throws CompileError {
/*  71 */     v.atASTList(this);
/*     */   }
/*     */   public String toString() {
/*  74 */     StringBuffer sbuf = new StringBuffer();
/*  75 */     sbuf.append("(<");
/*  76 */     sbuf.append(getTag());
/*  77 */     sbuf.append('>');
/*  78 */     ASTList list = this;
/*  79 */     while (list != null) {
/*  80 */       sbuf.append(' ');
/*  81 */       ASTree a = list.left;
/*  82 */       sbuf.append((a == null) ? "<null>" : a.toString());
/*  83 */       list = list.right;
/*     */     } 
/*     */     
/*  86 */     sbuf.append(')');
/*  87 */     return sbuf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/*  94 */     return length(this);
/*     */   }
/*     */   
/*     */   public static int length(ASTList list) {
/*  98 */     if (list == null) {
/*  99 */       return 0;
/*     */     }
/* 101 */     int n = 0;
/* 102 */     while (list != null) {
/* 103 */       list = list.right;
/* 104 */       n++;
/*     */     } 
/*     */     
/* 107 */     return n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ASTList sublist(int nth) {
/* 117 */     ASTList list = this;
/* 118 */     while (nth-- > 0) {
/* 119 */       list = list.right;
/*     */     }
/* 121 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean subst(ASTree newObj, ASTree oldObj) {
/* 129 */     for (ASTList list = this; list != null; list = list.right) {
/* 130 */       if (list.left == oldObj) {
/* 131 */         list.left = newObj;
/* 132 */         return true;
/*     */       } 
/*     */     } 
/* 135 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ASTList append(ASTList a, ASTree b) {
/* 142 */     return concat(a, new ASTList(b));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ASTList concat(ASTList a, ASTList b) {
/* 149 */     if (a == null) {
/* 150 */       return b;
/*     */     }
/* 152 */     ASTList list = a;
/* 153 */     while (list.right != null) {
/* 154 */       list = list.right;
/*     */     }
/* 156 */     list.right = b;
/* 157 */     return a;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\ast\ASTList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */