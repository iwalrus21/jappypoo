/*     */ package org.json;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JSONTokener
/*     */ {
/*     */   private int character;
/*     */   private boolean eof;
/*     */   private int index;
/*     */   private int line;
/*     */   private char previous;
/*     */   private Reader reader;
/*     */   private boolean usePrevious;
/*     */   
/*     */   public JSONTokener(Reader reader) {
/*  58 */     this.reader = reader.markSupported() ? reader : new BufferedReader(reader);
/*     */     
/*  60 */     this.eof = false;
/*  61 */     this.usePrevious = false;
/*  62 */     this.previous = Character.MIN_VALUE;
/*  63 */     this.index = 0;
/*  64 */     this.character = 1;
/*  65 */     this.line = 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSONTokener(InputStream inputStream) throws JSONException {
/*  73 */     this(new InputStreamReader(inputStream));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSONTokener(String s) {
/*  83 */     this(new StringReader(s));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void back() throws JSONException {
/*  93 */     if (this.usePrevious || this.index <= 0) {
/*  94 */       throw new JSONException("Stepping back two steps is not supported");
/*     */     }
/*  96 */     this.index--;
/*  97 */     this.character--;
/*  98 */     this.usePrevious = true;
/*  99 */     this.eof = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int dehexchar(char c) {
/* 110 */     if (c >= '0' && c <= '9') {
/* 111 */       return c - 48;
/*     */     }
/* 113 */     if (c >= 'A' && c <= 'F') {
/* 114 */       return c - 55;
/*     */     }
/* 116 */     if (c >= 'a' && c <= 'f') {
/* 117 */       return c - 87;
/*     */     }
/* 119 */     return -1;
/*     */   }
/*     */   
/*     */   public boolean end() {
/* 123 */     return (this.eof && !this.usePrevious);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean more() throws JSONException {
/* 133 */     next();
/* 134 */     if (end()) {
/* 135 */       return false;
/*     */     }
/* 137 */     back();
/* 138 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char next() throws JSONException {
/*     */     int c;
/* 149 */     if (this.usePrevious) {
/* 150 */       this.usePrevious = false;
/* 151 */       c = this.previous;
/*     */     } else {
/*     */       try {
/* 154 */         c = this.reader.read();
/* 155 */       } catch (IOException exception) {
/* 156 */         throw new JSONException(exception);
/*     */       } 
/*     */       
/* 159 */       if (c <= 0) {
/* 160 */         this.eof = true;
/* 161 */         c = 0;
/*     */       } 
/*     */     } 
/* 164 */     this.index++;
/* 165 */     if (this.previous == '\r') {
/* 166 */       this.line++;
/* 167 */       this.character = (c == 10) ? 0 : 1;
/* 168 */     } else if (c == 10) {
/* 169 */       this.line++;
/* 170 */       this.character = 0;
/*     */     } else {
/* 172 */       this.character++;
/*     */     } 
/* 174 */     this.previous = (char)c;
/* 175 */     return this.previous;
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
/*     */   public char next(char c) throws JSONException {
/* 187 */     char n = next();
/* 188 */     if (n != c) {
/* 189 */       throw syntaxError("Expected '" + c + "' and instead saw '" + n + "'");
/*     */     }
/*     */     
/* 192 */     return n;
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
/*     */   public String next(int n) throws JSONException {
/* 206 */     if (n == 0) {
/* 207 */       return "";
/*     */     }
/*     */     
/* 210 */     char[] chars = new char[n];
/* 211 */     int pos = 0;
/*     */     
/* 213 */     while (pos < n) {
/* 214 */       chars[pos] = next();
/* 215 */       if (end()) {
/* 216 */         throw syntaxError("Substring bounds error");
/*     */       }
/* 218 */       pos++;
/*     */     } 
/* 220 */     return new String(chars);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char nextClean() throws JSONException {
/*     */     char c;
/*     */     do {
/* 231 */       c = next();
/* 232 */     } while (c != '\000' && c <= ' ');
/* 233 */     return c;
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
/*     */   public String nextString(char quote) throws JSONException {
/* 252 */     StringBuffer sb = new StringBuffer();
/*     */     while (true) {
/* 254 */       char c = next();
/* 255 */       switch (c) {
/*     */         case '\000':
/*     */         case '\n':
/*     */         case '\r':
/* 259 */           throw syntaxError("Unterminated string");
/*     */         case '\\':
/* 261 */           c = next();
/* 262 */           switch (c) {
/*     */             case 'b':
/* 264 */               sb.append('\b');
/*     */               continue;
/*     */             case 't':
/* 267 */               sb.append('\t');
/*     */               continue;
/*     */             case 'n':
/* 270 */               sb.append('\n');
/*     */               continue;
/*     */             case 'f':
/* 273 */               sb.append('\f');
/*     */               continue;
/*     */             case 'r':
/* 276 */               sb.append('\r');
/*     */               continue;
/*     */             case 'u':
/* 279 */               sb.append((char)Integer.parseInt(next(4), 16));
/*     */               continue;
/*     */             case '"':
/*     */             case '\'':
/*     */             case '/':
/*     */             case '\\':
/* 285 */               sb.append(c);
/*     */               continue;
/*     */           } 
/* 288 */           throw syntaxError("Illegal escape.");
/*     */       } 
/*     */ 
/*     */       
/* 292 */       if (c == quote) {
/* 293 */         return sb.toString();
/*     */       }
/* 295 */       sb.append(c);
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
/*     */   public String nextTo(char delimiter) throws JSONException {
/* 308 */     StringBuffer sb = new StringBuffer();
/*     */     while (true) {
/* 310 */       char c = next();
/* 311 */       if (c == delimiter || c == '\000' || c == '\n' || c == '\r') {
/* 312 */         if (c != '\000') {
/* 313 */           back();
/*     */         }
/* 315 */         return sb.toString().trim();
/*     */       } 
/* 317 */       sb.append(c);
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
/*     */   public String nextTo(String delimiters) throws JSONException {
/* 330 */     StringBuffer sb = new StringBuffer();
/*     */     while (true) {
/* 332 */       char c = next();
/* 333 */       if (delimiters.indexOf(c) >= 0 || c == '\000' || c == '\n' || c == '\r') {
/*     */         
/* 335 */         if (c != '\000') {
/* 336 */           back();
/*     */         }
/* 338 */         return sb.toString().trim();
/*     */       } 
/* 340 */       sb.append(c);
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
/*     */   public Object nextValue() throws JSONException {
/* 353 */     char c = nextClean();
/*     */ 
/*     */     
/* 356 */     switch (c) {
/*     */       case '"':
/*     */       case '\'':
/* 359 */         return nextString(c);
/*     */       case '{':
/* 361 */         back();
/* 362 */         return new JSONObject(this);
/*     */       case '[':
/* 364 */         back();
/* 365 */         return new JSONArray(this);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 377 */     StringBuffer sb = new StringBuffer();
/* 378 */     while (c >= ' ' && ",:]}/\\\"[{;=#".indexOf(c) < 0) {
/* 379 */       sb.append(c);
/* 380 */       c = next();
/*     */     } 
/* 382 */     back();
/*     */     
/* 384 */     String string = sb.toString().trim();
/* 385 */     if (string.equals("")) {
/* 386 */       throw syntaxError("Missing value");
/*     */     }
/* 388 */     return JSONObject.stringToValue(string);
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
/*     */   public char skipTo(char to) throws JSONException {
/*     */     char c;
/*     */     try {
/* 402 */       int startIndex = this.index;
/* 403 */       int startCharacter = this.character;
/* 404 */       int startLine = this.line;
/* 405 */       this.reader.mark(2147483647);
/*     */       do {
/* 407 */         c = next();
/* 408 */         if (c == '\000') {
/* 409 */           this.reader.reset();
/* 410 */           this.index = startIndex;
/* 411 */           this.character = startCharacter;
/* 412 */           this.line = startLine;
/* 413 */           return c;
/*     */         } 
/* 415 */       } while (c != to);
/* 416 */     } catch (IOException exc) {
/* 417 */       throw new JSONException(exc);
/*     */     } 
/*     */     
/* 420 */     back();
/* 421 */     return c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSONException syntaxError(String message) {
/* 432 */     return new JSONException(message + toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 442 */     return " at " + this.index + " [character " + this.character + " line " + this.line + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\json\JSONTokener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */