/*    */ package org.schwering.irc.lib.util;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
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
/*    */ public enum CTCPCommand
/*    */ {
/* 27 */   ACTION,
/* 28 */   DCC,
/* 29 */   SED,
/* 30 */   FINGER,
/* 31 */   VERSION,
/* 32 */   SOURCE,
/* 33 */   USERINFO,
/* 34 */   CLIENTINFO,
/* 35 */   ERRMSG,
/* 36 */   PING,
/* 37 */   TIME;
/*    */   private static final Map<String, CTCPCommand> FAST_LOOKUP;
/*    */   public static final int SHORTEST_COMMAND_LENGTH;
/*    */   public static final char QUOTE_CHAR = '\001';
/*    */   
/*    */   static {
/* 43 */     int shortestLength = Integer.MAX_VALUE;
/* 44 */     Map<String, CTCPCommand> fastLookUp = new HashMap<>(64);
/* 45 */     CTCPCommand[] values = values();
/* 46 */     for (CTCPCommand value : values) {
/* 47 */       String name = value.name();
/* 48 */       int len = name.length();
/* 49 */       if (shortestLength > len) {
/* 50 */         shortestLength = len;
/*    */       }
/* 52 */       fastLookUp.put(name, value);
/*    */     } 
/* 54 */     FAST_LOOKUP = Collections.unmodifiableMap(fastLookUp);
/* 55 */     SHORTEST_COMMAND_LENGTH = shortestLength;
/*    */   }
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
/*    */   public static CTCPCommand fastValueOf(String command) {
/* 68 */     return FAST_LOOKUP.get(command);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\schwering\irc\li\\util\CTCPCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */