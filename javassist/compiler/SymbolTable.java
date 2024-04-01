/*    */ package javassist.compiler;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import javassist.compiler.ast.Declarator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SymbolTable
/*    */   extends HashMap
/*    */ {
/*    */   private SymbolTable parent;
/*    */   
/*    */   public SymbolTable() {
/* 25 */     this((SymbolTable)null);
/*    */   }
/*    */   
/*    */   public SymbolTable(SymbolTable p) {
/* 29 */     this.parent = p;
/*    */   }
/*    */   public SymbolTable getParent() {
/* 32 */     return this.parent;
/*    */   }
/*    */   public Declarator lookup(String name) {
/* 35 */     Declarator found = (Declarator)get(name);
/* 36 */     if (found == null && this.parent != null) {
/* 37 */       return this.parent.lookup(name);
/*    */     }
/* 39 */     return found;
/*    */   }
/*    */   
/*    */   public void append(String name, Declarator value) {
/* 43 */     put((K)name, (V)value);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\SymbolTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */