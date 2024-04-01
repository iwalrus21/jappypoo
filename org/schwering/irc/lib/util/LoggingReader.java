/*    */ package org.schwering.irc.lib.util;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.IOException;
/*    */ import java.io.Reader;
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
/*    */ public class LoggingReader
/*    */   extends BufferedReader
/*    */ {
/*    */   final IRCTrafficLogger trafficLogger;
/*    */   
/*    */   public LoggingReader(Reader in, IRCTrafficLogger trafficLogger) {
/* 33 */     super(in);
/* 34 */     this.trafficLogger = trafficLogger;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String readLine() throws IOException {
/* 42 */     String line = super.readLine();
/* 43 */     this.trafficLogger.in(line);
/* 44 */     return line;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\schwering\irc\li\\util\LoggingReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */