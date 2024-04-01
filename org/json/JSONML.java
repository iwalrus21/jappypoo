/*     */ package org.json;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JSONML
/*     */ {
/*     */   private static Object parse(XMLTokener x, boolean arrayForm, JSONArray ja) throws JSONException {
/*  52 */     String closeTag = null;
/*     */     
/*  54 */     JSONArray newja = null;
/*  55 */     JSONObject newjo = null;
/*     */     
/*  57 */     String tagName = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     label108: while (true) {
/*  66 */       Object token = x.nextContent();
/*  67 */       if (token == XML.LT) {
/*  68 */         token = x.nextToken();
/*  69 */         if (token instanceof Character) {
/*  70 */           if (token == XML.SLASH) {
/*     */ 
/*     */ 
/*     */             
/*  74 */             token = x.nextToken();
/*  75 */             if (!(token instanceof String)) {
/*  76 */               throw new JSONException("Expected a closing name instead of '" + token + "'.");
/*     */             }
/*     */ 
/*     */             
/*  80 */             if (x.nextToken() != XML.GT) {
/*  81 */               throw x.syntaxError("Misshaped close tag");
/*     */             }
/*  83 */             return token;
/*  84 */           }  if (token == XML.BANG) {
/*     */ 
/*     */ 
/*     */             
/*  88 */             char c = x.next();
/*  89 */             if (c == '-') {
/*  90 */               if (x.next() == '-') {
/*  91 */                 x.skipPast("-->");
/*     */               }
/*  93 */               x.back(); continue;
/*  94 */             }  if (c == '[') {
/*  95 */               token = x.nextToken();
/*  96 */               if (token.equals("CDATA") && x.next() == '[') {
/*  97 */                 if (ja != null)
/*  98 */                   ja.put(x.nextCDATA()); 
/*     */                 continue;
/*     */               } 
/* 101 */               throw x.syntaxError("Expected 'CDATA['");
/*     */             } 
/*     */             
/* 104 */             int i = 1;
/*     */             while (true)
/* 106 */             { token = x.nextMeta();
/* 107 */               if (token == null)
/* 108 */                 throw x.syntaxError("Missing '>' after '<!'."); 
/* 109 */               if (token == XML.LT) {
/* 110 */                 i++;
/* 111 */               } else if (token == XML.GT) {
/* 112 */                 i--;
/*     */               } 
/* 114 */               if (i <= 0)
/*     */                 continue label108;  }  break;
/* 116 */           }  if (token == XML.QUEST) {
/*     */ 
/*     */ 
/*     */             
/* 120 */             x.skipPast("?>"); continue;
/*     */           } 
/* 122 */           throw x.syntaxError("Misshaped tag");
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 128 */         if (!(token instanceof String)) {
/* 129 */           throw x.syntaxError("Bad tagName '" + token + "'.");
/*     */         }
/* 131 */         tagName = (String)token;
/* 132 */         newja = new JSONArray();
/* 133 */         newjo = new JSONObject();
/* 134 */         if (arrayForm) {
/* 135 */           newja.put(tagName);
/* 136 */           if (ja != null) {
/* 137 */             ja.put(newja);
/*     */           }
/*     */         } else {
/* 140 */           newjo.put("tagName", tagName);
/* 141 */           if (ja != null) {
/* 142 */             ja.put(newjo);
/*     */           }
/*     */         } 
/* 145 */         token = null;
/*     */         while (true) {
/* 147 */           if (token == null) {
/* 148 */             token = x.nextToken();
/*     */           }
/* 150 */           if (token == null) {
/* 151 */             throw x.syntaxError("Misshaped tag");
/*     */           }
/* 153 */           if (!(token instanceof String)) {
/*     */             break;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 159 */           String attribute = (String)token;
/* 160 */           if (!arrayForm && (attribute == "tagName" || attribute == "childNode")) {
/* 161 */             throw x.syntaxError("Reserved attribute.");
/*     */           }
/* 163 */           token = x.nextToken();
/* 164 */           if (token == XML.EQ) {
/* 165 */             token = x.nextToken();
/* 166 */             if (!(token instanceof String)) {
/* 167 */               throw x.syntaxError("Missing value");
/*     */             }
/* 169 */             newjo.accumulate(attribute, XML.stringToValue((String)token));
/* 170 */             token = null; continue;
/*     */           } 
/* 172 */           newjo.accumulate(attribute, "");
/*     */         } 
/*     */         
/* 175 */         if (arrayForm && newjo.length() > 0) {
/* 176 */           newja.put(newjo);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 181 */         if (token == XML.SLASH) {
/* 182 */           if (x.nextToken() != XML.GT) {
/* 183 */             throw x.syntaxError("Misshaped tag");
/*     */           }
/* 185 */           if (ja == null) {
/* 186 */             if (arrayForm) {
/* 187 */               return newja;
/*     */             }
/* 189 */             return newjo;
/*     */           } 
/*     */ 
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/* 196 */         if (token != XML.GT) {
/* 197 */           throw x.syntaxError("Misshaped tag");
/*     */         }
/* 199 */         closeTag = (String)parse(x, arrayForm, newja);
/* 200 */         if (closeTag != null) {
/* 201 */           if (!closeTag.equals(tagName)) {
/* 202 */             throw x.syntaxError("Mismatched '" + tagName + "' and '" + closeTag + "'");
/*     */           }
/*     */           
/* 205 */           tagName = null;
/* 206 */           if (!arrayForm && newja.length() > 0) {
/* 207 */             newjo.put("childNodes", newja);
/*     */           }
/* 209 */           if (ja == null) {
/* 210 */             if (arrayForm) {
/* 211 */               return newja;
/*     */             }
/* 213 */             return newjo;
/*     */           } 
/*     */         } 
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 220 */       if (ja != null) {
/* 221 */         ja.put((token instanceof String) ? 
/* 222 */             XML.stringToValue((String)token) : token);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JSONArray toJSONArray(String string) throws JSONException {
/* 242 */     return toJSONArray(new XMLTokener(string));
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
/*     */   public static JSONArray toJSONArray(XMLTokener x) throws JSONException {
/* 259 */     return (JSONArray)parse(x, true, null);
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
/*     */   public static JSONObject toJSONObject(XMLTokener x) throws JSONException {
/* 277 */     return (JSONObject)parse(x, false, null);
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
/*     */   public static JSONObject toJSONObject(String string) throws JSONException {
/* 295 */     return toJSONObject(new XMLTokener(string));
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
/*     */   public static String toString(JSONArray ja) throws JSONException {
/*     */     int i;
/* 312 */     StringBuffer sb = new StringBuffer();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 318 */     String tagName = ja.getString(0);
/* 319 */     XML.noSpace(tagName);
/* 320 */     tagName = XML.escape(tagName);
/* 321 */     sb.append('<');
/* 322 */     sb.append(tagName);
/*     */     
/* 324 */     Object object = ja.opt(1);
/* 325 */     if (object instanceof JSONObject) {
/* 326 */       i = 2;
/* 327 */       JSONObject jo = (JSONObject)object;
/*     */ 
/*     */ 
/*     */       
/* 331 */       Iterator<E> keys = jo.keys();
/* 332 */       while (keys.hasNext()) {
/* 333 */         String key = keys.next().toString();
/* 334 */         XML.noSpace(key);
/* 335 */         String value = jo.optString(key);
/* 336 */         if (value != null) {
/* 337 */           sb.append(' ');
/* 338 */           sb.append(XML.escape(key));
/* 339 */           sb.append('=');
/* 340 */           sb.append('"');
/* 341 */           sb.append(XML.escape(value));
/* 342 */           sb.append('"');
/*     */         } 
/*     */       } 
/*     */     } else {
/* 346 */       i = 1;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 351 */     int length = ja.length();
/* 352 */     if (i >= length)
/* 353 */     { sb.append('/');
/* 354 */       sb.append('>'); }
/*     */     else
/* 356 */     { sb.append('>');
/*     */       while (true)
/* 358 */       { object = ja.get(i);
/* 359 */         i++;
/* 360 */         if (object != null) {
/* 361 */           if (object instanceof String) {
/* 362 */             sb.append(XML.escape(object.toString()));
/* 363 */           } else if (object instanceof JSONObject) {
/* 364 */             sb.append(toString((JSONObject)object));
/* 365 */           } else if (object instanceof JSONArray) {
/* 366 */             sb.append(toString((JSONArray)object));
/*     */           } 
/*     */         }
/* 369 */         if (i >= length)
/* 370 */         { sb.append('<');
/* 371 */           sb.append('/');
/* 372 */           sb.append(tagName);
/* 373 */           sb.append('>');
/*     */           
/* 375 */           return sb.toString(); }  }  }  return sb.toString();
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
/*     */   public static String toString(JSONObject jo) throws JSONException {
/* 388 */     StringBuffer sb = new StringBuffer();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 400 */     String tagName = jo.optString("tagName");
/* 401 */     if (tagName == null) {
/* 402 */       return XML.escape(jo.toString());
/*     */     }
/* 404 */     XML.noSpace(tagName);
/* 405 */     tagName = XML.escape(tagName);
/* 406 */     sb.append('<');
/* 407 */     sb.append(tagName);
/*     */ 
/*     */ 
/*     */     
/* 411 */     Iterator<E> keys = jo.keys();
/* 412 */     while (keys.hasNext()) {
/* 413 */       String key = keys.next().toString();
/* 414 */       if (!key.equals("tagName") && !key.equals("childNodes")) {
/* 415 */         XML.noSpace(key);
/* 416 */         String value = jo.optString(key);
/* 417 */         if (value != null) {
/* 418 */           sb.append(' ');
/* 419 */           sb.append(XML.escape(key));
/* 420 */           sb.append('=');
/* 421 */           sb.append('"');
/* 422 */           sb.append(XML.escape(value));
/* 423 */           sb.append('"');
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 430 */     JSONArray ja = jo.optJSONArray("childNodes");
/* 431 */     if (ja == null) {
/* 432 */       sb.append('/');
/* 433 */       sb.append('>');
/*     */     } else {
/* 435 */       sb.append('>');
/* 436 */       int length = ja.length();
/* 437 */       for (int i = 0; i < length; i++) {
/* 438 */         Object object = ja.get(i);
/* 439 */         if (object != null) {
/* 440 */           if (object instanceof String) {
/* 441 */             sb.append(XML.escape(object.toString()));
/* 442 */           } else if (object instanceof JSONObject) {
/* 443 */             sb.append(toString((JSONObject)object));
/* 444 */           } else if (object instanceof JSONArray) {
/* 445 */             sb.append(toString((JSONArray)object));
/*     */           } 
/*     */         }
/*     */       } 
/* 449 */       sb.append('<');
/* 450 */       sb.append('/');
/* 451 */       sb.append(tagName);
/* 452 */       sb.append('>');
/*     */     } 
/* 454 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\json\JSONML.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */