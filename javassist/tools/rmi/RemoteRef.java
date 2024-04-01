/*    */ package javassist.tools.rmi;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ public class RemoteRef
/*    */   implements Serializable
/*    */ {
/*    */   public int oid;
/*    */   public String classname;
/*    */   
/*    */   public RemoteRef(int i) {
/* 28 */     this.oid = i;
/* 29 */     this.classname = null;
/*    */   }
/*    */   
/*    */   public RemoteRef(int i, String name) {
/* 33 */     this.oid = i;
/* 34 */     this.classname = name;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\tools\rmi\RemoteRef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */