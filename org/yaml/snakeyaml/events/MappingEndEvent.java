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
/*    */ public final class MappingEndEvent
/*    */   extends CollectionEndEvent
/*    */ {
/*    */   public MappingEndEvent(Mark startMark, Mark endMark) {
/* 28 */     super(startMark, endMark);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean is(Event.ID id) {
/* 33 */     return (Event.ID.MappingEnd == id);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\yaml\snakeyaml\events\MappingEndEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */