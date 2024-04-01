/*    */ package org.schwering.irc.lib.util;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ import java.io.Writer;
/*    */ import org.schwering.irc.lib.IRCTrafficLogger;
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
/*    */ public class LoggingWriter
/*    */   extends PrintWriter
/*    */ {
/*    */   final IRCTrafficLogger trafficLogger;
/*    */   
/*    */   public LoggingWriter(Writer out, IRCTrafficLogger trafficLogger) {
/* 32 */     super(out);
/* 33 */     this.trafficLogger = trafficLogger;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void write(String s) {
/* 41 */     String trimmedLine = s;
/* 42 */     if (s != null && s.endsWith("\r\n")) {
/* 43 */       trimmedLine = s.substring(0, s.length() - 2);
/*    */     }
/* 45 */     this.trafficLogger.out(trimmedLine);
/* 46 */     super.write(s);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\schwering\irc\li\\util\LoggingWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */