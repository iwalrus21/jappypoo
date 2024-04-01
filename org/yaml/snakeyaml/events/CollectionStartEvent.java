/*    */ package org.yaml.snakeyaml.events;
/*    */ 
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
/*    */ public abstract class CollectionStartEvent
/*    */   extends NodeEvent
/*    */ {
/*    */   private final String tag;
/*    */   private final boolean implicit;
/*    */   private final Boolean flowStyle;
/*    */   
/*    */   public CollectionStartEvent(String anchor, String tag, boolean implicit, Mark startMark, Mark endMark, Boolean flowStyle) {
/* 33 */     super(anchor, startMark, endMark);
/* 34 */     this.tag = tag;
/* 35 */     this.implicit = implicit;
/* 36 */     this.flowStyle = flowStyle;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getTag() {
/* 46 */     return this.tag;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean getImplicit() {
/* 56 */     return this.implicit;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Boolean getFlowStyle() {
/* 66 */     return this.flowStyle;
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getArguments() {
/* 71 */     return super.getArguments() + ", tag=" + this.tag + ", implicit=" + this.implicit;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\yaml\snakeyaml\events\CollectionStartEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */