/*    */ package org.yaml.snakeyaml.nodes;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.yaml.snakeyaml.error.Mark;
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
/*    */ public abstract class CollectionNode<T>
/*    */   extends Node
/*    */ {
/*    */   private Boolean flowStyle;
/*    */   
/*    */   public CollectionNode(Tag tag, Mark startMark, Mark endMark, Boolean flowStyle) {
/* 30 */     super(tag, startMark, endMark);
/* 31 */     this.flowStyle = flowStyle;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract List<T> getValue();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Boolean getFlowStyle() {
/* 48 */     return this.flowStyle;
/*    */   }
/*    */   
/*    */   public void setFlowStyle(Boolean flowStyle) {
/* 52 */     this.flowStyle = flowStyle;
/*    */   }
/*    */   
/*    */   public void setEndMark(Mark endMark) {
/* 56 */     this.endMark = endMark;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\yaml\snakeyaml\nodes\CollectionNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */