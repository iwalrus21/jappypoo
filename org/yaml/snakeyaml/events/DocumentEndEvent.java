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
/*    */ public final class DocumentEndEvent
/*    */   extends Event
/*    */ {
/*    */   private final boolean explicit;
/*    */   
/*    */   public DocumentEndEvent(Mark startMark, Mark endMark, boolean explicit) {
/* 30 */     super(startMark, endMark);
/* 31 */     this.explicit = explicit;
/*    */   }
/*    */   
/*    */   public boolean getExplicit() {
/* 35 */     return this.explicit;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean is(Event.ID id) {
/* 40 */     return (Event.ID.DocumentEnd == id);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\yaml\snakeyaml\events\DocumentEndEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */