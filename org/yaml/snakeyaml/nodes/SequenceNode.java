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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SequenceNode
/*    */   extends CollectionNode<Node>
/*    */ {
/*    */   private final List<Node> value;
/*    */   
/*    */   public SequenceNode(Tag tag, boolean resolved, List<Node> value, Mark startMark, Mark endMark, Boolean flowStyle) {
/* 33 */     super(tag, startMark, endMark, flowStyle);
/* 34 */     if (value == null) {
/* 35 */       throw new NullPointerException("value in a Node is required.");
/*    */     }
/* 37 */     this.value = value;
/* 38 */     this.resolved = resolved;
/*    */   }
/*    */   
/*    */   public SequenceNode(Tag tag, List<Node> value, Boolean flowStyle) {
/* 42 */     this(tag, true, value, (Mark)null, (Mark)null, flowStyle);
/*    */   }
/*    */ 
/*    */   
/*    */   public NodeId getNodeId() {
/* 47 */     return NodeId.sequence;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<Node> getValue() {
/* 56 */     return this.value;
/*    */   }
/*    */   
/*    */   public void setListType(Class<? extends Object> listType) {
/* 60 */     for (Node node : this.value) {
/* 61 */       node.setType(listType);
/*    */     }
/*    */   }
/*    */   
/*    */   public String toString() {
/* 66 */     return "<" + getClass().getName() + " (tag=" + getTag() + ", value=" + getValue() + ")>";
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\yaml\snakeyaml\nodes\SequenceNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */