/*     */ package org.json;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JSONArray
/*     */ {
/*     */   private ArrayList myArrayList;
/*     */   
/*     */   public JSONArray() {
/*  96 */     this.myArrayList = new ArrayList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSONArray(JSONTokener x) throws JSONException {
/* 105 */     this();
/* 106 */     if (x.nextClean() != '[') {
/* 107 */       throw x.syntaxError("A JSONArray text must start with '['");
/*     */     }
/* 109 */     if (x.nextClean() != ']') {
/* 110 */       x.back();
/*     */       while (true) {
/* 112 */         if (x.nextClean() == ',') {
/* 113 */           x.back();
/* 114 */           this.myArrayList.add(JSONObject.NULL);
/*     */         } else {
/* 116 */           x.back();
/* 117 */           this.myArrayList.add(x.nextValue());
/*     */         } 
/* 119 */         switch (x.nextClean()) {
/*     */           case ',':
/*     */           case ';':
/* 122 */             if (x.nextClean() == ']') {
/*     */               return;
/*     */             }
/* 125 */             x.back(); continue;
/*     */           case ']':
/*     */             return;
/*     */         }  break;
/*     */       } 
/* 130 */       throw x.syntaxError("Expected a ',' or ']'");
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
/*     */   public JSONArray(String source) throws JSONException {
/* 145 */     this(new JSONTokener(source));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSONArray(Collection collection) {
/* 154 */     this.myArrayList = new ArrayList();
/* 155 */     if (collection != null) {
/* 156 */       Iterator iter = collection.iterator();
/* 157 */       while (iter.hasNext()) {
/* 158 */         this.myArrayList.add(JSONObject.wrap(iter.next()));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSONArray(Object array) throws JSONException {
/* 169 */     this();
/* 170 */     if (array.getClass().isArray()) {
/* 171 */       int length = Array.getLength(array);
/* 172 */       for (int i = 0; i < length; i++) {
/* 173 */         put(JSONObject.wrap(Array.get(array, i)));
/*     */       }
/*     */     } else {
/* 176 */       throw new JSONException("JSONArray initial value should be a string or collection or array.");
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
/*     */   public Object get(int index) throws JSONException {
/* 190 */     Object object = opt(index);
/* 191 */     if (object == null) {
/* 192 */       throw new JSONException("JSONArray[" + index + "] not found.");
/*     */     }
/* 194 */     return object;
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
/*     */   public boolean getBoolean(int index) throws JSONException {
/* 208 */     Object object = get(index);
/* 209 */     if (object.equals(Boolean.FALSE) || (object instanceof String && ((String)object)
/*     */       
/* 211 */       .equalsIgnoreCase("false")))
/* 212 */       return false; 
/* 213 */     if (object.equals(Boolean.TRUE) || (object instanceof String && ((String)object)
/*     */       
/* 215 */       .equalsIgnoreCase("true"))) {
/* 216 */       return true;
/*     */     }
/* 218 */     throw new JSONException("JSONArray[" + index + "] is not a boolean.");
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
/*     */   public double getDouble(int index) throws JSONException {
/* 231 */     Object object = get(index);
/*     */     try {
/* 233 */       return (object instanceof Number) ? ((Number)object)
/* 234 */         .doubleValue() : 
/* 235 */         Double.parseDouble((String)object);
/* 236 */     } catch (Exception e) {
/* 237 */       throw new JSONException("JSONArray[" + index + "] is not a number.");
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
/*     */   public int getInt(int index) throws JSONException {
/* 251 */     Object object = get(index);
/*     */     try {
/* 253 */       return (object instanceof Number) ? ((Number)object)
/* 254 */         .intValue() : 
/* 255 */         Integer.parseInt((String)object);
/* 256 */     } catch (Exception e) {
/* 257 */       throw new JSONException("JSONArray[" + index + "] is not a number.");
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
/*     */   public JSONArray getJSONArray(int index) throws JSONException {
/* 271 */     Object object = get(index);
/* 272 */     if (object instanceof JSONArray) {
/* 273 */       return (JSONArray)object;
/*     */     }
/* 275 */     throw new JSONException("JSONArray[" + index + "] is not a JSONArray.");
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
/*     */   public JSONObject getJSONObject(int index) throws JSONException {
/* 288 */     Object object = get(index);
/* 289 */     if (object instanceof JSONObject) {
/* 290 */       return (JSONObject)object;
/*     */     }
/* 292 */     throw new JSONException("JSONArray[" + index + "] is not a JSONObject.");
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
/*     */   public long getLong(int index) throws JSONException {
/* 306 */     Object object = get(index);
/*     */     try {
/* 308 */       return (object instanceof Number) ? ((Number)object)
/* 309 */         .longValue() : 
/* 310 */         Long.parseLong((String)object);
/* 311 */     } catch (Exception e) {
/* 312 */       throw new JSONException("JSONArray[" + index + "] is not a number.");
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
/*     */   public String getString(int index) throws JSONException {
/* 325 */     Object object = get(index);
/* 326 */     if (object instanceof String) {
/* 327 */       return (String)object;
/*     */     }
/* 329 */     throw new JSONException("JSONArray[" + index + "] not a string.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNull(int index) {
/* 339 */     return JSONObject.NULL.equals(opt(index));
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
/*     */   public String join(String separator) throws JSONException {
/* 352 */     int len = length();
/* 353 */     StringBuffer sb = new StringBuffer();
/*     */     
/* 355 */     for (int i = 0; i < len; i++) {
/* 356 */       if (i > 0) {
/* 357 */         sb.append(separator);
/*     */       }
/* 359 */       sb.append(JSONObject.valueToString(this.myArrayList.get(i)));
/*     */     } 
/* 361 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/* 371 */     return this.myArrayList.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object opt(int index) {
/* 382 */     return (index < 0 || index >= length()) ? null : this.myArrayList
/* 383 */       .get(index);
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
/*     */   public boolean optBoolean(int index) {
/* 396 */     return optBoolean(index, false);
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
/*     */   public boolean optBoolean(int index, boolean defaultValue) {
/*     */     try {
/* 411 */       return getBoolean(index);
/* 412 */     } catch (Exception e) {
/* 413 */       return defaultValue;
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
/*     */   public double optDouble(int index) {
/* 427 */     return optDouble(index, Double.NaN);
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
/*     */   public double optDouble(int index, double defaultValue) {
/*     */     try {
/* 442 */       return getDouble(index);
/* 443 */     } catch (Exception e) {
/* 444 */       return defaultValue;
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
/*     */   public int optInt(int index) {
/* 458 */     return optInt(index, 0);
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
/*     */   public int optInt(int index, int defaultValue) {
/*     */     try {
/* 472 */       return getInt(index);
/* 473 */     } catch (Exception e) {
/* 474 */       return defaultValue;
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
/*     */   public JSONArray optJSONArray(int index) {
/* 486 */     Object o = opt(index);
/* 487 */     return (o instanceof JSONArray) ? (JSONArray)o : null;
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
/*     */   public JSONObject optJSONObject(int index) {
/* 500 */     Object o = opt(index);
/* 501 */     return (o instanceof JSONObject) ? (JSONObject)o : null;
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
/*     */   public long optLong(int index) {
/* 514 */     return optLong(index, 0L);
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
/*     */   public long optLong(int index, long defaultValue) {
/*     */     try {
/* 528 */       return getLong(index);
/* 529 */     } catch (Exception e) {
/* 530 */       return defaultValue;
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
/*     */   public String optString(int index) {
/* 544 */     return optString(index, "");
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
/*     */   public String optString(int index, String defaultValue) {
/* 557 */     Object object = opt(index);
/* 558 */     return (object != null) ? object.toString() : defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSONArray put(boolean value) {
/* 569 */     put(value ? Boolean.TRUE : Boolean.FALSE);
/* 570 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSONArray put(Collection value) {
/* 581 */     put(new JSONArray(value));
/* 582 */     return this;
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
/*     */   public JSONArray put(double value) throws JSONException {
/* 594 */     Double d = new Double(value);
/* 595 */     JSONObject.testValidity(d);
/* 596 */     put(d);
/* 597 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSONArray put(int value) {
/* 608 */     put(new Integer(value));
/* 609 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSONArray put(long value) {
/* 620 */     put(new Long(value));
/* 621 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSONArray put(Map value) {
/* 632 */     put(new JSONObject(value));
/* 633 */     return this;
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
/*     */   public JSONArray put(Object value) {
/* 645 */     this.myArrayList.add(value);
/* 646 */     return this;
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
/*     */   public JSONArray put(int index, boolean value) throws JSONException {
/* 660 */     put(index, value ? Boolean.TRUE : Boolean.FALSE);
/* 661 */     return this;
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
/*     */   public JSONArray put(int index, Collection value) throws JSONException {
/* 675 */     put(index, new JSONArray(value));
/* 676 */     return this;
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
/*     */   public JSONArray put(int index, double value) throws JSONException {
/* 691 */     put(index, new Double(value));
/* 692 */     return this;
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
/*     */   public JSONArray put(int index, int value) throws JSONException {
/* 706 */     put(index, new Integer(value));
/* 707 */     return this;
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
/*     */   public JSONArray put(int index, long value) throws JSONException {
/* 721 */     put(index, new Long(value));
/* 722 */     return this;
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
/*     */   public JSONArray put(int index, Map value) throws JSONException {
/* 736 */     put(index, new JSONObject(value));
/* 737 */     return this;
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
/*     */   public JSONArray put(int index, Object value) throws JSONException {
/* 754 */     JSONObject.testValidity(value);
/* 755 */     if (index < 0) {
/* 756 */       throw new JSONException("JSONArray[" + index + "] not found.");
/*     */     }
/* 758 */     if (index < length()) {
/* 759 */       this.myArrayList.set(index, value);
/*     */     } else {
/* 761 */       while (index != length()) {
/* 762 */         put(JSONObject.NULL);
/*     */       }
/* 764 */       put(value);
/*     */     } 
/* 766 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object remove(int index) {
/* 777 */     Object o = opt(index);
/* 778 */     this.myArrayList.remove(index);
/* 779 */     return o;
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
/*     */   public JSONObject toJSONObject(JSONArray names) throws JSONException {
/* 793 */     if (names == null || names.length() == 0 || length() == 0) {
/* 794 */       return null;
/*     */     }
/* 796 */     JSONObject jo = new JSONObject();
/* 797 */     for (int i = 0; i < names.length(); i++) {
/* 798 */       jo.put(names.getString(i), opt(i));
/*     */     }
/* 800 */     return jo;
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
/*     */   public String toString() {
/*     */     try {
/* 817 */       return '[' + join(",") + ']';
/* 818 */     } catch (Exception e) {
/* 819 */       return null;
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
/*     */   public String toString(int indentFactor) throws JSONException {
/* 836 */     return toString(indentFactor, 0);
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
/*     */   String toString(int indentFactor, int indent) throws JSONException {
/* 851 */     int len = length();
/* 852 */     if (len == 0) {
/* 853 */       return "[]";
/*     */     }
/*     */     
/* 856 */     StringBuffer sb = new StringBuffer("[");
/* 857 */     if (len == 1) {
/* 858 */       sb.append(JSONObject.valueToString(this.myArrayList.get(0), indentFactor, indent));
/*     */     } else {
/*     */       
/* 861 */       int newindent = indent + indentFactor;
/* 862 */       sb.append('\n'); int i;
/* 863 */       for (i = 0; i < len; i++) {
/* 864 */         if (i > 0) {
/* 865 */           sb.append(",\n");
/*     */         }
/* 867 */         for (int j = 0; j < newindent; j++) {
/* 868 */           sb.append(' ');
/*     */         }
/* 870 */         sb.append(JSONObject.valueToString(this.myArrayList.get(i), indentFactor, newindent));
/*     */       } 
/*     */       
/* 873 */       sb.append('\n');
/* 874 */       for (i = 0; i < indent; i++) {
/* 875 */         sb.append(' ');
/*     */       }
/*     */     } 
/* 878 */     sb.append(']');
/* 879 */     return sb.toString();
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
/*     */   public Writer write(Writer writer) throws JSONException {
/*     */     try {
/* 894 */       boolean b = false;
/* 895 */       int len = length();
/*     */       
/* 897 */       writer.write(91);
/*     */       
/* 899 */       for (int i = 0; i < len; i++) {
/* 900 */         if (b) {
/* 901 */           writer.write(44);
/*     */         }
/* 903 */         Object v = this.myArrayList.get(i);
/* 904 */         if (v instanceof JSONObject) {
/* 905 */           ((JSONObject)v).write(writer);
/* 906 */         } else if (v instanceof JSONArray) {
/* 907 */           ((JSONArray)v).write(writer);
/*     */         } else {
/* 909 */           writer.write(JSONObject.valueToString(v));
/*     */         } 
/* 911 */         b = true;
/*     */       } 
/* 913 */       writer.write(93);
/* 914 */       return writer;
/* 915 */     } catch (IOException e) {
/* 916 */       throw new JSONException(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\json\JSONArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */