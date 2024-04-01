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
/*    */ public final class AliasEvent
/*    */   extends NodeEvent
/*    */ {
/*    */   public AliasEvent(String anchor, Mark startMark, Mark endMark) {
/* 25 */     super(anchor, startMark, endMark);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean is(Event.ID id) {
/* 30 */     return (Event.ID.Alias == id);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\yaml\snakeyaml\events\AliasEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */