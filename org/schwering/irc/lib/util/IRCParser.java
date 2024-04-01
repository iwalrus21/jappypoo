/*     */ package org.schwering.irc.lib.util;
/*     */ 
/*     */ import org.schwering.irc.lib.IRCUser;
/*     */ import org.schwering.irc.lib.impl.DefaultIRCUser;
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
/*     */ public class IRCParser
/*     */ {
/*     */   private StringBuilder buf;
/*     */   private int len;
/*     */   private String prefix;
/*     */   private String command;
/*     */   private String middle;
/*     */   private String trailing;
/*     */   private String[] parameters;
/*     */   
/*     */   public IRCParser(String line) {
/* 135 */     this(line, false);
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
/*     */   public IRCParser(String line, boolean stripColors) {
/* 147 */     int index = 0;
/*     */ 
/*     */     
/* 150 */     this.buf = new StringBuilder(line);
/* 151 */     if (stripColors)
/* 152 */       this.buf = IRCUtil.stripColorsAndCTCPDelimiters(this.buf); 
/* 153 */     this.len = this.buf.length();
/*     */ 
/*     */     
/* 156 */     if (this.buf.charAt(0) == ':') {
/* 157 */       this.prefix = this.buf.substring(1, index = indexOf(32, index));
/* 158 */       index++;
/*     */     } 
/*     */     
/* 161 */     while (this.buf.charAt(index) == ' ') {
/* 162 */       index++;
/*     */     }
/*     */     
/* 165 */     this.command = this.buf.substring(index, ((index = indexOf(32, index)) != -1) ? index : (index = this.len));
/*     */ 
/*     */     
/* 168 */     while (index < this.len && this.buf.charAt(index) == ' ')
/* 169 */       index++; 
/* 170 */     index--;
/*     */     
/*     */     int trail;
/* 173 */     if ((trail = indexOf(" :", index)) != -1) {
/* 174 */       this.trailing = this.buf.substring(trail + 2, this.len);
/* 175 */     } else if ((trail = lastIndexOf(32)) != -1 && trail >= index) {
/* 176 */       this.trailing = this.buf.substring(trail + 1, this.len);
/* 177 */     }  this.middle = (index < trail) ? this.buf.substring(index + 1, trail) : "";
/*     */ 
/*     */     
/* 180 */     this.prefix = (this.prefix != null) ? this.prefix : "";
/* 181 */     this.command = (this.command != null) ? this.command : "";
/* 182 */     this.middle = (this.middle != null) ? this.middle : "";
/* 183 */     this.trailing = (this.trailing != null) ? this.trailing : "";
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
/*     */   private int indexOf(int c, int i) {
/* 195 */     while (i < this.len) {
/* 196 */       if (this.buf.charAt(i++) == c)
/* 197 */         return --i; 
/* 198 */     }  return -1;
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
/*     */   private int indexOf(String str, int i) {
/* 210 */     int sublen = str.length();
/* 211 */     int index = -1;
/*     */     
/* 213 */     for (; i < this.len; i++) {
/* 214 */       int j; for (index = i, j = 0; i < this.len && j < sublen && 
/* 215 */         this.buf.charAt(i) == str.charAt(j); i++, j++)
/*     */       
/* 217 */       { if (j + 1 == sublen)
/* 218 */           return index;  } 
/* 219 */     }  return -1;
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
/*     */ 
/*     */ 
/*     */   
/*     */   private int lastIndexOf(int c) {
/* 238 */     int i = this.len;
/* 239 */     boolean ok = false;
/* 240 */     while (i > 0) {
/* 241 */       if (this.buf.charAt(--i) != c) {
/* 242 */         ok = true; continue;
/* 243 */       }  if (ok)
/* 244 */         return i; 
/* 245 */     }  return -1;
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
/*     */   private void initParameters() {
/* 259 */     this.parameters = IRCUtil.split(this.middle, 32, this.trailing);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPrefix() {
/* 270 */     return this.prefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCommand() {
/* 279 */     return this.command;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMiddle() {
/* 288 */     return this.middle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTrailing() {
/* 297 */     return this.trailing;
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
/* 308 */     return this.buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getParameters() {
/* 318 */     return this.middle + ((this.middle
/* 319 */       .length() != 0 && this.trailing.length() != 0) ? " " : "") + this.trailing;
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
/*     */   public String getNick() {
/* 349 */     int i = this.prefix.indexOf('!');
/* 350 */     if (i != -1 || (i = this.prefix.indexOf('@')) != -1)
/* 351 */       return this.prefix.substring(0, i); 
/* 352 */     return (this.prefix.length() != 0) ? this.prefix : null;
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
/*     */   public String getServername() {
/* 379 */     return getNick();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUsername() {
/* 403 */     int i = this.prefix.indexOf('!') + 1;
/* 404 */     if (i != 0) {
/* 405 */       int j = this.prefix.indexOf('@', i);
/* 406 */       return this.prefix.substring(i, (j != -1) ? j : this.prefix.length());
/*     */     } 
/* 408 */     return null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getHost() {
/* 432 */     int i = this.prefix.indexOf('@') + 1;
/* 433 */     if (i != 0)
/* 434 */       return this.prefix.substring(i, this.prefix.length()); 
/* 435 */     return null;
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
/*     */   
/*     */   public IRCUser getUser() {
/* 452 */     return (IRCUser)new DefaultIRCUser(getNick(), getUsername(), getHost());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getParameterCount() {
/* 463 */     if (this.parameters == null)
/* 464 */       initParameters(); 
/* 465 */     return this.parameters.length;
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
/*     */   public String getParameter(int i) {
/* 479 */     if (this.parameters == null)
/* 480 */       initParameters(); 
/* 481 */     i--;
/* 482 */     if (i >= 0 && i < this.parameters.length) {
/* 483 */       return this.parameters[i];
/*     */     }
/* 485 */     return "";
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
/*     */   public String getParametersFrom(int i) {
/* 499 */     if (this.parameters == null)
/* 500 */       initParameters(); 
/* 501 */     StringBuilder params = new StringBuilder();
/* 502 */     for (; --i < this.parameters.length; i++)
/* 503 */       params.append(this.parameters[i] + " "); 
/* 504 */     return params.toString();
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
/*     */   public String getParametersTo(int i) {
/* 519 */     if (this.parameters == null)
/* 520 */       initParameters(); 
/* 521 */     StringBuilder params = new StringBuilder();
/* 522 */     int max = (i < this.parameters.length) ? i : this.parameters.length;
/* 523 */     for (i = 0; i < max; i++)
/* 524 */       params.append(this.parameters[i] + " "); 
/* 525 */     return params.toString();
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
/* 536 */     return getClass().getName() + "[" + this.prefix + "," + this.command + "," + this.middle + "," + this.trailing + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\schwering\irc\li\\util\IRCParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */