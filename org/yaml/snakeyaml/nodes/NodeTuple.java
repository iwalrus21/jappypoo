/*    */ package org.yaml.snakeyaml.nodes;
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
/*    */ public final class NodeTuple
/*    */ {
/*    */   private Node keyNode;
/*    */   private Node valueNode;
/*    */   
/*    */   public NodeTuple(Node keyNode, Node valueNode) {
/* 27 */     if (keyNode == null || valueNode == null) {
/* 28 */       throw new NullPointerException("Nodes must be provided.");
/*    */     }
/* 30 */     this.keyNode = keyNode;
/* 31 */     this.valueNode = valueNode;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Node getKeyNode() {
/* 40 */     return this.keyNode;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Node getValueNode() {
/* 49 */     return this.valueNode;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 54 */     return "<NodeTuple keyNode=" + this.keyNode.toString() + "; valueNode=" + this.valueNode.toString() + ">";
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\yaml\snakeyaml\nodes\NodeTuple.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */