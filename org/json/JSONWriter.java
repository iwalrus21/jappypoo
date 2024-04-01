/*     */ package org.json;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JSONWriter
/*     */ {
/*     */   private static final int maxdepth = 20;
/*     */   private boolean comma;
/*     */   protected char mode;
/*     */   private JSONObject[] stack;
/*     */   private int top;
/*     */   protected Writer writer;
/*     */   
/*     */   public JSONWriter(Writer w) {
/*  97 */     this.comma = false;
/*  98 */     this.mode = 'i';
/*  99 */     this.stack = new JSONObject[20];
/* 100 */     this.top = 0;
/* 101 */     this.writer = w;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JSONWriter append(String string) throws JSONException {
/* 111 */     if (string == null) {
/* 112 */       throw new JSONException("Null pointer");
/*     */     }
/* 114 */     if (this.mode == 'o' || this.mode == 'a') {
/*     */       try {
/* 116 */         if (this.comma && this.mode == 'a') {
/* 117 */           this.writer.write(44);
/*     */         }
/* 119 */         this.writer.write(string);
/* 120 */       } catch (IOException e) {
/* 121 */         throw new JSONException(e);
/*     */       } 
/* 123 */       if (this.mode == 'o') {
/* 124 */         this.mode = 'k';
/*     */       }
/* 126 */       this.comma = true;
/* 127 */       return this;
/*     */     } 
/* 129 */     throw new JSONException("Value out of sequence.");
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
/*     */   public JSONWriter array() throws JSONException {
/* 142 */     if (this.mode == 'i' || this.mode == 'o' || this.mode == 'a') {
/* 143 */       push(null);
/* 144 */       append("[");
/* 145 */       this.comma = false;
/* 146 */       return this;
/*     */     } 
/* 148 */     throw new JSONException("Misplaced array.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JSONWriter end(char mode, char c) throws JSONException {
/* 159 */     if (this.mode != mode) {
/* 160 */       throw new JSONException((mode == 'a') ? "Misplaced endArray." : "Misplaced endObject.");
/*     */     }
/*     */     
/* 163 */     pop(mode);
/*     */     try {
/* 165 */       this.writer.write(c);
/* 166 */     } catch (IOException e) {
/* 167 */       throw new JSONException(e);
/*     */     } 
/* 169 */     this.comma = true;
/* 170 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSONWriter endArray() throws JSONException {
/* 180 */     return end('a', ']');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSONWriter endObject() throws JSONException {
/* 190 */     return end('k', '}');
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
/*     */   public JSONWriter key(String string) throws JSONException {
/* 202 */     if (string == null) {
/* 203 */       throw new JSONException("Null key.");
/*     */     }
/* 205 */     if (this.mode == 'k') {
/*     */       try {
/* 207 */         this.stack[this.top - 1].putOnce(string, Boolean.TRUE);
/* 208 */         if (this.comma) {
/* 209 */           this.writer.write(44);
/*     */         }
/* 211 */         this.writer.write(JSONObject.quote(string));
/* 212 */         this.writer.write(58);
/* 213 */         this.comma = false;
/* 214 */         this.mode = 'o';
/* 215 */         return this;
/* 216 */       } catch (IOException e) {
/* 217 */         throw new JSONException(e);
/*     */       } 
/*     */     }
/* 220 */     throw new JSONException("Misplaced key.");
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
/*     */   public JSONWriter object() throws JSONException {
/* 234 */     if (this.mode == 'i') {
/* 235 */       this.mode = 'o';
/*     */     }
/* 237 */     if (this.mode == 'o' || this.mode == 'a') {
/* 238 */       append("{");
/* 239 */       push(new JSONObject());
/* 240 */       this.comma = false;
/* 241 */       return this;
/*     */     } 
/* 243 */     throw new JSONException("Misplaced object.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void pop(char c) throws JSONException {
/* 254 */     if (this.top <= 0) {
/* 255 */       throw new JSONException("Nesting error.");
/*     */     }
/* 257 */     char m = (this.stack[this.top - 1] == null) ? 'a' : 'k';
/* 258 */     if (m != c) {
/* 259 */       throw new JSONException("Nesting error.");
/*     */     }
/* 261 */     this.top--;
/* 262 */     this.mode = (this.top == 0) ? 'd' : ((this.stack[this.top - 1] == null) ? 'a' : 'k');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void push(JSONObject jo) throws JSONException {
/* 272 */     if (this.top >= 20) {
/* 273 */       throw new JSONException("Nesting too deep.");
/*     */     }
/* 275 */     this.stack[this.top] = jo;
/* 276 */     this.mode = (jo == null) ? 'a' : 'k';
/* 277 */     this.top++;
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
/*     */   public JSONWriter value(boolean b) throws JSONException {
/* 289 */     return append(b ? "true" : "false");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSONWriter value(double d) throws JSONException {
/* 299 */     return value(new Double(d));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSONWriter value(long l) throws JSONException {
/* 309 */     return append(Long.toString(l));
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
/*     */   public JSONWriter value(Object object) throws JSONException {
/* 321 */     return append(JSONObject.valueToString(object));
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\json\JSONWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */