/*    */ package javassist.bytecode.analysis;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
/*    */ import java.util.Set;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Subroutine
/*    */ {
/* 31 */   private List callers = new ArrayList();
/* 32 */   private Set access = new HashSet();
/*    */   private int start;
/*    */   
/*    */   public Subroutine(int start, int caller) {
/* 36 */     this.start = start;
/* 37 */     this.callers.add(new Integer(caller));
/*    */   }
/*    */   
/*    */   public void addCaller(int caller) {
/* 41 */     this.callers.add(new Integer(caller));
/*    */   }
/*    */   
/*    */   public int start() {
/* 45 */     return this.start;
/*    */   }
/*    */   
/*    */   public void access(int index) {
/* 49 */     this.access.add(new Integer(index));
/*    */   }
/*    */   
/*    */   public boolean isAccessed(int index) {
/* 53 */     return this.access.contains(new Integer(index));
/*    */   }
/*    */   
/*    */   public Collection accessed() {
/* 57 */     return this.access;
/*    */   }
/*    */   
/*    */   public Collection callers() {
/* 61 */     return this.callers;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 65 */     return "start = " + this.start + " callers = " + this.callers.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\analysis\Subroutine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */