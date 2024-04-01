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
/*     */ public class XML
/*     */ {
/*  39 */   public static final Character AMP = new Character('&');
/*     */ 
/*     */   
/*  42 */   public static final Character APOS = new Character('\'');
/*     */ 
/*     */   
/*  45 */   public static final Character BANG = new Character('!');
/*     */ 
/*     */   
/*  48 */   public static final Character EQ = new Character('=');
/*     */ 
/*     */   
/*  51 */   public static final Character GT = new Character('>');
/*     */ 
/*     */   
/*  54 */   public static final Character LT = new Character('<');
/*     */ 
/*     */   
/*  57 */   public static final Character QUEST = new Character('?');
/*     */ 
/*     */   
/*  60 */   public static final Character QUOT = new Character('"');
/*     */ 
/*     */   
/*  63 */   public static final Character SLASH = new Character('/');
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String escape(String string) {
/*  77 */     StringBuffer sb = new StringBuffer();
/*  78 */     for (int i = 0, length = string.length(); i < length; i++) {
/*  79 */       char c = string.charAt(i);
/*  80 */       switch (c) {
/*     */         case '&':
/*  82 */           sb.append("&amp;");
/*     */           break;
/*     */         case '<':
/*  85 */           sb.append("&lt;");
/*     */           break;
/*     */         case '>':
/*  88 */           sb.append("&gt;");
/*     */           break;
/*     */         case '"':
/*  91 */           sb.append("&quot;");
/*     */           break;
/*     */         case '\'':
/*  94 */           sb.append("&apos;");
/*     */           break;
/*     */         default:
/*  97 */           sb.append(c); break;
/*     */       } 
/*     */     } 
/* 100 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void noSpace(String string) throws JSONException {
/* 110 */     int length = string.length();
/* 111 */     if (length == 0) {
/* 112 */       throw new JSONException("Empty string.");
/*     */     }
/* 114 */     for (int i = 0; i < length; i++) {
/* 115 */       if (Character.isWhitespace(string.charAt(i))) {
/* 116 */         throw new JSONException("'" + string + "' contains a space character.");
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
/*     */   private static boolean parse(XMLTokener x, JSONObject context, String name) throws JSONException {
/* 134 */     JSONObject jsonobject = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 149 */     Object token = x.nextToken();
/*     */ 
/*     */ 
/*     */     
/* 153 */     if (token == BANG) {
/* 154 */       char c = x.next();
/* 155 */       if (c == '-') {
/* 156 */         if (x.next() == '-') {
/* 157 */           x.skipPast("-->");
/* 158 */           return false;
/*     */         } 
/* 160 */         x.back();
/* 161 */       } else if (c == '[') {
/* 162 */         token = x.nextToken();
/* 163 */         if (token.equals("CDATA") && 
/* 164 */           x.next() == '[') {
/* 165 */           String string = x.nextCDATA();
/* 166 */           if (string.length() > 0) {
/* 167 */             context.accumulate("content", string);
/*     */           }
/* 169 */           return false;
/*     */         } 
/*     */         
/* 172 */         throw x.syntaxError("Expected 'CDATA['");
/*     */       } 
/* 174 */       int i = 1;
/*     */       while (true)
/* 176 */       { token = x.nextMeta();
/* 177 */         if (token == null)
/* 178 */           throw x.syntaxError("Missing '>' after '<!'."); 
/* 179 */         if (token == LT) {
/* 180 */           i++;
/* 181 */         } else if (token == GT) {
/* 182 */           i--;
/*     */         } 
/* 184 */         if (i <= 0)
/* 185 */           return false;  } 
/* 186 */     }  if (token == QUEST) {
/*     */ 
/*     */ 
/*     */       
/* 190 */       x.skipPast("?>");
/* 191 */       return false;
/* 192 */     }  if (token == SLASH) {
/*     */ 
/*     */ 
/*     */       
/* 196 */       token = x.nextToken();
/* 197 */       if (name == null) {
/* 198 */         throw x.syntaxError("Mismatched close tag " + token);
/*     */       }
/* 200 */       if (!token.equals(name)) {
/* 201 */         throw x.syntaxError("Mismatched " + name + " and " + token);
/*     */       }
/* 203 */       if (x.nextToken() != GT) {
/* 204 */         throw x.syntaxError("Misshaped close tag");
/*     */       }
/* 206 */       return true;
/*     */     } 
/* 208 */     if (token instanceof Character) {
/* 209 */       throw x.syntaxError("Misshaped tag");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 214 */     String tagName = (String)token;
/* 215 */     token = null;
/* 216 */     jsonobject = new JSONObject();
/*     */     while (true) {
/* 218 */       if (token == null) {
/* 219 */         token = x.nextToken();
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 224 */       if (token instanceof String) {
/* 225 */         String string = (String)token;
/* 226 */         token = x.nextToken();
/* 227 */         if (token == EQ) {
/* 228 */           token = x.nextToken();
/* 229 */           if (!(token instanceof String)) {
/* 230 */             throw x.syntaxError("Missing value");
/*     */           }
/* 232 */           jsonobject.accumulate(string, 
/* 233 */               stringToValue((String)token));
/* 234 */           token = null; continue;
/*     */         } 
/* 236 */         jsonobject.accumulate(string, "");
/*     */         continue;
/*     */       } 
/*     */       break;
/*     */     } 
/* 241 */     if (token == SLASH) {
/* 242 */       if (x.nextToken() != GT) {
/* 243 */         throw x.syntaxError("Misshaped tag");
/*     */       }
/* 245 */       if (jsonobject.length() > 0) {
/* 246 */         context.accumulate(tagName, jsonobject);
/*     */       } else {
/* 248 */         context.accumulate(tagName, "");
/*     */       } 
/* 250 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 254 */     if (token == GT) {
/*     */       while (true) {
/* 256 */         token = x.nextContent();
/* 257 */         if (token == null) {
/* 258 */           if (tagName != null) {
/* 259 */             throw x.syntaxError("Unclosed tag " + tagName);
/*     */           }
/* 261 */           return false;
/* 262 */         }  if (token instanceof String) {
/* 263 */           String string = (String)token;
/* 264 */           if (string.length() > 0) {
/* 265 */             jsonobject.accumulate("content", 
/* 266 */                 stringToValue(string));
/*     */           }
/*     */           
/*     */           continue;
/*     */         } 
/* 271 */         if (token == LT && 
/* 272 */           parse(x, jsonobject, tagName)) {
/* 273 */           if (jsonobject.length() == 0) {
/* 274 */             context.accumulate(tagName, "");
/* 275 */           } else if (jsonobject.length() == 1 && jsonobject
/* 276 */             .opt("content") != null) {
/* 277 */             context.accumulate(tagName, jsonobject
/* 278 */                 .opt("content"));
/*     */           } else {
/* 280 */             context.accumulate(tagName, jsonobject);
/*     */           } 
/* 282 */           return false;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 287 */     throw x.syntaxError("Misshaped tag");
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
/*     */   public static Object stringToValue(String string) {
/* 304 */     if (string.equals("")) {
/* 305 */       return string;
/*     */     }
/* 307 */     if (string.equalsIgnoreCase("true")) {
/* 308 */       return Boolean.TRUE;
/*     */     }
/* 310 */     if (string.equalsIgnoreCase("false")) {
/* 311 */       return Boolean.FALSE;
/*     */     }
/* 313 */     if (string.equalsIgnoreCase("null")) {
/* 314 */       return JSONObject.NULL;
/*     */     }
/* 316 */     if (string.equals("0")) {
/* 317 */       return new Integer(0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 324 */       char initial = string.charAt(0);
/* 325 */       boolean negative = false;
/* 326 */       if (initial == '-') {
/* 327 */         initial = string.charAt(1);
/* 328 */         negative = true;
/*     */       } 
/* 330 */       if (initial == '0' && string.charAt(negative ? 2 : 1) == '0') {
/* 331 */         return string;
/*     */       }
/* 333 */       if (initial >= '0' && initial <= '9') {
/* 334 */         if (string.indexOf('.') >= 0)
/* 335 */           return Double.valueOf(string); 
/* 336 */         if (string.indexOf('e') < 0 && string.indexOf('E') < 0) {
/* 337 */           Long myLong = new Long(string);
/* 338 */           if (myLong.longValue() == myLong.intValue()) {
/* 339 */             return new Integer(myLong.intValue());
/*     */           }
/* 341 */           return myLong;
/*     */         }
/*     */       
/*     */       } 
/* 345 */     } catch (Exception exception) {}
/*     */     
/* 347 */     return string;
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
/*     */   public static JSONObject toJSONObject(String string) throws JSONException {
/* 366 */     JSONObject jo = new JSONObject();
/* 367 */     XMLTokener x = new XMLTokener(string);
/* 368 */     while (x.more() && x.skipPast("<")) {
/* 369 */       parse(x, jo, null);
/*     */     }
/* 371 */     return jo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toString(Object object) throws JSONException {
/* 382 */     return toString(object, null);
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
/*     */   public static String toString(Object object, String tagName) throws JSONException {
/* 395 */     StringBuffer sb = new StringBuffer();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 404 */     if (object instanceof JSONObject) {
/*     */ 
/*     */ 
/*     */       
/* 408 */       if (tagName != null) {
/* 409 */         sb.append('<');
/* 410 */         sb.append(tagName);
/* 411 */         sb.append('>');
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 416 */       JSONObject jo = (JSONObject)object;
/* 417 */       Iterator<E> keys = jo.keys();
/* 418 */       while (keys.hasNext()) {
/* 419 */         String key = keys.next().toString();
/* 420 */         Object value = jo.opt(key);
/* 421 */         if (value == null) {
/* 422 */           value = "";
/*     */         }
/* 424 */         if (value instanceof String) {
/* 425 */           String str = (String)value;
/*     */         } else {
/* 427 */           String str = null;
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 432 */         if (key.equals("content")) {
/* 433 */           if (value instanceof JSONArray) {
/* 434 */             JSONArray ja = (JSONArray)value;
/* 435 */             int length = ja.length();
/* 436 */             for (int i = 0; i < length; i++) {
/* 437 */               if (i > 0) {
/* 438 */                 sb.append('\n');
/*     */               }
/* 440 */               sb.append(escape(ja.get(i).toString()));
/*     */             }  continue;
/*     */           } 
/* 443 */           sb.append(escape(value.toString()));
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/* 448 */         if (value instanceof JSONArray) {
/* 449 */           JSONArray ja = (JSONArray)value;
/* 450 */           int length = ja.length();
/* 451 */           for (int i = 0; i < length; i++) {
/* 452 */             value = ja.get(i);
/* 453 */             if (value instanceof JSONArray) {
/* 454 */               sb.append('<');
/* 455 */               sb.append(key);
/* 456 */               sb.append('>');
/* 457 */               sb.append(toString(value));
/* 458 */               sb.append("</");
/* 459 */               sb.append(key);
/* 460 */               sb.append('>');
/*     */             } else {
/* 462 */               sb.append(toString(value, key));
/*     */             } 
/*     */           }  continue;
/* 465 */         }  if (value.equals("")) {
/* 466 */           sb.append('<');
/* 467 */           sb.append(key);
/* 468 */           sb.append("/>");
/*     */           
/*     */           continue;
/*     */         } 
/*     */         
/* 473 */         sb.append(toString(value, key));
/*     */       } 
/*     */       
/* 476 */       if (tagName != null) {
/*     */ 
/*     */ 
/*     */         
/* 480 */         sb.append("</");
/* 481 */         sb.append(tagName);
/* 482 */         sb.append('>');
/*     */       } 
/* 484 */       return sb.toString();
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 490 */     if (object.getClass().isArray()) {
/* 491 */       object = new JSONArray(object);
/*     */     }
/* 493 */     if (object instanceof JSONArray) {
/* 494 */       JSONArray ja = (JSONArray)object;
/* 495 */       int length = ja.length();
/* 496 */       for (int i = 0; i < length; i++) {
/* 497 */         sb.append(toString(ja.opt(i), (tagName == null) ? "array" : tagName));
/*     */       }
/* 499 */       return sb.toString();
/*     */     } 
/* 501 */     String string = (object == null) ? "null" : escape(object.toString());
/* 502 */     return (tagName == null) ? ("\"" + string + "\"") : (
/* 503 */       (string.length() == 0) ? ("<" + tagName + "/>") : ("<" + tagName + ">" + string + "</" + tagName + ">"));
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\json\XML.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */