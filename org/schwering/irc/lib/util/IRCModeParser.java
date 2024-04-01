/*     */ package org.schwering.irc.lib.util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IRCModeParser
/*     */ {
/*     */   private String line;
/*     */   private char[] operatorsArr;
/*     */   private char[] modesArr;
/*     */   private String[] argsArr;
/*     */   
/*     */   public IRCModeParser(String line) {
/*  83 */     line = line.trim();
/*  84 */     this.line = line;
/*  85 */     int index = line.indexOf(' ');
/*  86 */     if (index >= 2) {
/*  87 */       String modes = line.substring(0, index);
/*  88 */       String args = line.substring(index + 1);
/*  89 */       parse(modes, args);
/*  90 */     } else if (line.length() >= 2) {
/*  91 */       String modes = line;
/*  92 */       String args = "";
/*  93 */       parse(modes, args);
/*     */     } else {
/*  95 */       this.argsArr = new String[0];
/*  96 */       this.operatorsArr = new char[0];
/*  97 */       this.modesArr = new char[0];
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IRCModeParser(String modes, String args) {
/* 111 */     this.line = modes + " " + args;
/* 112 */     parse(modes, args);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void parse(String modes, String args) {
/* 128 */     String[] argsTmp = IRCUtil.split(args, 32);
/* 129 */     int modesLen = modes.length();
/* 130 */     int modesCount = getModesCount(modes);
/*     */     
/* 132 */     char operator = '+';
/* 133 */     this.operatorsArr = new char[modesCount];
/* 134 */     this.modesArr = new char[modesCount];
/* 135 */     this.argsArr = new String[modesCount];
/*     */     
/* 137 */     for (int i = 0, j = 0, n = 0; i < modesLen; i++) {
/* 138 */       char c = modes.charAt(i);
/* 139 */       if (c == '+' || c == '-') {
/* 140 */         operator = c;
/*     */       } else {
/*     */         
/* 143 */         this.operatorsArr[n] = operator;
/* 144 */         this.modesArr[n] = c;
/* 145 */         if (c == 'o' || c == 'v' || c == 'b' || c == 'k' || (c == 'l' && operator == '+')) {
/*     */           
/* 147 */           this.argsArr[n] = (j < argsTmp.length) ? argsTmp[j++] : "";
/*     */         } else {
/* 149 */           this.argsArr[n] = "";
/*     */         } 
/* 151 */         n++;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getModesCount(String modes) {
/* 164 */     int count = 0;
/* 165 */     for (int i = 0, len = modes.length(); i < len; i++) {
/* 166 */       int c; if ((c = modes.charAt(i)) != 43 && c != 45)
/* 167 */         count++; 
/* 168 */     }  return count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCount() {
/* 180 */     return this.operatorsArr.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char getOperatorAt(int i) {
/* 194 */     return this.operatorsArr[i - 1];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char getModeAt(int i) {
/* 210 */     return this.modesArr[i - 1];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getArgAt(int i) {
/* 226 */     return this.argsArr[i - 1];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLine() {
/* 237 */     return this.line;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 248 */     return getClass().getName() + "[" + getLine() + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\schwering\irc\li\\util\IRCModeParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */