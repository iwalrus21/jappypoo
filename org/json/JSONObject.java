/*      */ package org.json;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.Writer;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.util.Collection;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.ResourceBundle;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class JSONObject
/*      */ {
/*      */   private Map map;
/*      */   
/*      */   private static final class Null
/*      */   {
/*      */     private Null() {}
/*      */     
/*      */     protected final Object clone() {
/*  106 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object object) {
/*  116 */       return (object == null || object == this);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  124 */       return "null";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  141 */   public static final Object NULL = new Null();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject() {
/*  148 */     this.map = new HashMap<>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject(JSONObject jo, String[] names) {
/*  162 */     this();
/*  163 */     for (int i = 0; i < names.length; i++) {
/*      */       try {
/*  165 */         putOnce(names[i], jo.opt(names[i]));
/*  166 */       } catch (Exception exception) {}
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject(JSONTokener x) throws JSONException {
/*  179 */     this();
/*      */ 
/*      */ 
/*      */     
/*  183 */     if (x.nextClean() != '{') {
/*  184 */       throw x.syntaxError("A JSONObject text must begin with '{'");
/*      */     }
/*      */     while (true) {
/*  187 */       char c = x.nextClean();
/*  188 */       switch (c) {
/*      */         case '\000':
/*  190 */           throw x.syntaxError("A JSONObject text must end with '}'");
/*      */         case '}':
/*      */           return;
/*      */       } 
/*  194 */       x.back();
/*  195 */       String key = x.nextValue().toString();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  200 */       c = x.nextClean();
/*  201 */       if (c == '=') {
/*  202 */         if (x.next() != '>') {
/*  203 */           x.back();
/*      */         }
/*  205 */       } else if (c != ':') {
/*  206 */         throw x.syntaxError("Expected a ':' after a key");
/*      */       } 
/*  208 */       putOnce(key, x.nextValue());
/*      */ 
/*      */ 
/*      */       
/*  212 */       switch (x.nextClean()) {
/*      */         case ',':
/*      */         case ';':
/*  215 */           if (x.nextClean() == '}') {
/*      */             return;
/*      */           }
/*  218 */           x.back(); continue;
/*      */         case '}':
/*      */           return;
/*      */       }  break;
/*      */     } 
/*  223 */     throw x.syntaxError("Expected a ',' or '}'");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject(Map map) {
/*  237 */     this.map = new HashMap<>();
/*  238 */     if (map != null) {
/*  239 */       Iterator<Map.Entry> i = map.entrySet().iterator();
/*  240 */       while (i.hasNext()) {
/*  241 */         Map.Entry e = i.next();
/*  242 */         Object value = e.getValue();
/*  243 */         if (value != null) {
/*  244 */           this.map.put(e.getKey(), wrap(value));
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject(Object bean) {
/*  271 */     this();
/*  272 */     populateMap(bean);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject(Object object, String[] names) {
/*  288 */     this();
/*  289 */     Class<?> c = object.getClass();
/*  290 */     for (int i = 0; i < names.length; i++) {
/*  291 */       String name = names[i];
/*      */       try {
/*  293 */         putOpt(name, c.getField(name).get(object));
/*  294 */       } catch (Exception exception) {}
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject(String source) throws JSONException {
/*  310 */     this(new JSONTokener(source));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject(String baseName, Locale locale) throws JSONException {
/*  321 */     this();
/*  322 */     ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale, 
/*  323 */         Thread.currentThread().getContextClassLoader());
/*      */ 
/*      */ 
/*      */     
/*  327 */     Enumeration<String> keys = bundle.getKeys();
/*  328 */     while (keys.hasMoreElements()) {
/*  329 */       Object key = keys.nextElement();
/*  330 */       if (key instanceof String) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  336 */         String[] path = ((String)key).split("\\.");
/*  337 */         int last = path.length - 1;
/*  338 */         JSONObject target = this;
/*  339 */         for (int i = 0; i < last; i++) {
/*  340 */           String segment = path[i];
/*  341 */           JSONObject nextTarget = target.optJSONObject(segment);
/*  342 */           if (nextTarget == null) {
/*  343 */             nextTarget = new JSONObject();
/*  344 */             target.put(segment, nextTarget);
/*      */           } 
/*  346 */           target = nextTarget;
/*      */         } 
/*  348 */         target.put(path[last], bundle.getString((String)key));
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject accumulate(String key, Object value) throws JSONException {
/*  374 */     testValidity(value);
/*  375 */     Object object = opt(key);
/*  376 */     if (object == null) {
/*  377 */       put(key, (value instanceof JSONArray) ? (new JSONArray())
/*  378 */           .put(value) : value);
/*  379 */     } else if (object instanceof JSONArray) {
/*  380 */       ((JSONArray)object).put(value);
/*      */     } else {
/*  382 */       put(key, (new JSONArray()).put(object).put(value));
/*      */     } 
/*  384 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject append(String key, Object value) throws JSONException {
/*  400 */     testValidity(value);
/*  401 */     Object object = opt(key);
/*  402 */     if (object == null) {
/*  403 */       put(key, (new JSONArray()).put(value));
/*  404 */     } else if (object instanceof JSONArray) {
/*  405 */       put(key, ((JSONArray)object).put(value));
/*      */     } else {
/*  407 */       throw new JSONException("JSONObject[" + key + "] is not a JSONArray.");
/*      */     } 
/*      */     
/*  410 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String doubleToString(double d) {
/*  421 */     if (Double.isInfinite(d) || Double.isNaN(d)) {
/*  422 */       return "null";
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  427 */     String string = Double.toString(d);
/*  428 */     if (string.indexOf('.') > 0 && string.indexOf('e') < 0 && string
/*  429 */       .indexOf('E') < 0) {
/*  430 */       while (string.endsWith("0")) {
/*  431 */         string = string.substring(0, string.length() - 1);
/*      */       }
/*  433 */       if (string.endsWith(".")) {
/*  434 */         string = string.substring(0, string.length() - 1);
/*      */       }
/*      */     } 
/*  437 */     return string;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object get(String key) throws JSONException {
/*  449 */     if (key == null) {
/*  450 */       throw new JSONException("Null key.");
/*      */     }
/*  452 */     Object object = opt(key);
/*  453 */     if (object == null) {
/*  454 */       throw new JSONException("JSONObject[" + quote(key) + "] not found.");
/*      */     }
/*      */     
/*  457 */     return object;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getBoolean(String key) throws JSONException {
/*  470 */     Object object = get(key);
/*  471 */     if (object.equals(Boolean.FALSE) || (object instanceof String && ((String)object)
/*      */       
/*  473 */       .equalsIgnoreCase("false")))
/*  474 */       return false; 
/*  475 */     if (object.equals(Boolean.TRUE) || (object instanceof String && ((String)object)
/*      */       
/*  477 */       .equalsIgnoreCase("true"))) {
/*  478 */       return true;
/*      */     }
/*  480 */     throw new JSONException("JSONObject[" + quote(key) + "] is not a Boolean.");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getDouble(String key) throws JSONException {
/*  493 */     Object object = get(key);
/*      */     try {
/*  495 */       return (object instanceof Number) ? ((Number)object)
/*  496 */         .doubleValue() : 
/*  497 */         Double.parseDouble((String)object);
/*  498 */     } catch (Exception e) {
/*  499 */       throw new JSONException("JSONObject[" + quote(key) + "] is not a number.");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getInt(String key) throws JSONException {
/*  514 */     Object object = get(key);
/*      */     try {
/*  516 */       return (object instanceof Number) ? ((Number)object)
/*  517 */         .intValue() : 
/*  518 */         Integer.parseInt((String)object);
/*  519 */     } catch (Exception e) {
/*  520 */       throw new JSONException("JSONObject[" + quote(key) + "] is not an int.");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONArray getJSONArray(String key) throws JSONException {
/*  535 */     Object object = get(key);
/*  536 */     if (object instanceof JSONArray) {
/*  537 */       return (JSONArray)object;
/*      */     }
/*  539 */     throw new JSONException("JSONObject[" + quote(key) + "] is not a JSONArray.");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject getJSONObject(String key) throws JSONException {
/*  553 */     Object object = get(key);
/*  554 */     if (object instanceof JSONObject) {
/*  555 */       return (JSONObject)object;
/*      */     }
/*  557 */     throw new JSONException("JSONObject[" + quote(key) + "] is not a JSONObject.");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLong(String key) throws JSONException {
/*  571 */     Object object = get(key);
/*      */     try {
/*  573 */       return (object instanceof Number) ? ((Number)object)
/*  574 */         .longValue() : 
/*  575 */         Long.parseLong((String)object);
/*  576 */     } catch (Exception e) {
/*  577 */       throw new JSONException("JSONObject[" + quote(key) + "] is not a long.");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String[] getNames(JSONObject jo) {
/*  589 */     int length = jo.length();
/*  590 */     if (length == 0) {
/*  591 */       return null;
/*      */     }
/*  593 */     Iterator<String> iterator = jo.keys();
/*  594 */     String[] names = new String[length];
/*  595 */     int i = 0;
/*  596 */     while (iterator.hasNext()) {
/*  597 */       names[i] = iterator.next();
/*  598 */       i++;
/*      */     } 
/*  600 */     return names;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String[] getNames(Object object) {
/*  610 */     if (object == null) {
/*  611 */       return null;
/*      */     }
/*  613 */     Class<?> klass = object.getClass();
/*  614 */     Field[] fields = klass.getFields();
/*  615 */     int length = fields.length;
/*  616 */     if (length == 0) {
/*  617 */       return null;
/*      */     }
/*  619 */     String[] names = new String[length];
/*  620 */     for (int i = 0; i < length; i++) {
/*  621 */       names[i] = fields[i].getName();
/*      */     }
/*  623 */     return names;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getString(String key) throws JSONException {
/*  635 */     Object object = get(key);
/*  636 */     if (object instanceof String) {
/*  637 */       return (String)object;
/*      */     }
/*  639 */     throw new JSONException("JSONObject[" + quote(key) + "] not a string.");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean has(String key) {
/*  650 */     return this.map.containsKey(key);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject increment(String key) throws JSONException {
/*  664 */     Object value = opt(key);
/*  665 */     if (value == null) {
/*  666 */       put(key, 1);
/*  667 */     } else if (value instanceof Integer) {
/*  668 */       put(key, ((Integer)value).intValue() + 1);
/*  669 */     } else if (value instanceof Long) {
/*  670 */       put(key, ((Long)value).longValue() + 1L);
/*  671 */     } else if (value instanceof Double) {
/*  672 */       put(key, ((Double)value).doubleValue() + 1.0D);
/*  673 */     } else if (value instanceof Float) {
/*  674 */       put(key, (((Float)value).floatValue() + 1.0F));
/*      */     } else {
/*  676 */       throw new JSONException("Unable to increment [" + quote(key) + "].");
/*      */     } 
/*  678 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNull(String key) {
/*  690 */     return NULL.equals(opt(key));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Iterator keys() {
/*  700 */     return this.map.keySet().iterator();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int length() {
/*  710 */     return this.map.size();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONArray names() {
/*  721 */     JSONArray ja = new JSONArray();
/*  722 */     Iterator keys = keys();
/*  723 */     while (keys.hasNext()) {
/*  724 */       ja.put(keys.next());
/*      */     }
/*  726 */     return (ja.length() == 0) ? null : ja;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String numberToString(Number number) throws JSONException {
/*  737 */     if (number == null) {
/*  738 */       throw new JSONException("Null pointer");
/*      */     }
/*  740 */     testValidity(number);
/*      */ 
/*      */ 
/*      */     
/*  744 */     String string = number.toString();
/*  745 */     if (string.indexOf('.') > 0 && string.indexOf('e') < 0 && string
/*  746 */       .indexOf('E') < 0) {
/*  747 */       while (string.endsWith("0")) {
/*  748 */         string = string.substring(0, string.length() - 1);
/*      */       }
/*  750 */       if (string.endsWith(".")) {
/*  751 */         string = string.substring(0, string.length() - 1);
/*      */       }
/*      */     } 
/*  754 */     return string;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object opt(String key) {
/*  764 */     return (key == null) ? null : this.map.get(key);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean optBoolean(String key) {
/*  777 */     return optBoolean(key, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean optBoolean(String key, boolean defaultValue) {
/*      */     try {
/*  792 */       return getBoolean(key);
/*  793 */     } catch (Exception e) {
/*  794 */       return defaultValue;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double optDouble(String key) {
/*  809 */     return optDouble(key, Double.NaN);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double optDouble(String key, double defaultValue) {
/*      */     try {
/*  825 */       return getDouble(key);
/*  826 */     } catch (Exception e) {
/*  827 */       return defaultValue;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int optInt(String key) {
/*  842 */     return optInt(key, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int optInt(String key, int defaultValue) {
/*      */     try {
/*  858 */       return getInt(key);
/*  859 */     } catch (Exception e) {
/*  860 */       return defaultValue;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONArray optJSONArray(String key) {
/*  874 */     Object o = opt(key);
/*  875 */     return (o instanceof JSONArray) ? (JSONArray)o : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject optJSONObject(String key) {
/*  888 */     Object object = opt(key);
/*  889 */     return (object instanceof JSONObject) ? (JSONObject)object : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long optLong(String key) {
/*  903 */     return optLong(key, 0L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long optLong(String key, long defaultValue) {
/*      */     try {
/*  919 */       return getLong(key);
/*  920 */     } catch (Exception e) {
/*  921 */       return defaultValue;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String optString(String key) {
/*  935 */     return optString(key, "");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String optString(String key, String defaultValue) {
/*  948 */     Object object = opt(key);
/*  949 */     return NULL.equals(object) ? defaultValue : object.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private void populateMap(Object bean) {
/*  954 */     Class<?> klass = bean.getClass();
/*      */ 
/*      */ 
/*      */     
/*  958 */     boolean includeSuperClass = (klass.getClassLoader() != null);
/*      */ 
/*      */     
/*  961 */     Method[] methods = includeSuperClass ? klass.getMethods() : klass.getDeclaredMethods();
/*  962 */     for (int i = 0; i < methods.length; i++) {
/*      */       try {
/*  964 */         Method method = methods[i];
/*  965 */         if (Modifier.isPublic(method.getModifiers())) {
/*  966 */           String name = method.getName();
/*  967 */           String key = "";
/*  968 */           if (name.startsWith("get")) {
/*  969 */             if (name.equals("getClass") || name
/*  970 */               .equals("getDeclaringClass")) {
/*  971 */               key = "";
/*      */             } else {
/*  973 */               key = name.substring(3);
/*      */             } 
/*  975 */           } else if (name.startsWith("is")) {
/*  976 */             key = name.substring(2);
/*      */           } 
/*  978 */           if (key.length() > 0 && 
/*  979 */             Character.isUpperCase(key.charAt(0)) && (method
/*  980 */             .getParameterTypes()).length == 0) {
/*  981 */             if (key.length() == 1) {
/*  982 */               key = key.toLowerCase();
/*  983 */             } else if (!Character.isUpperCase(key.charAt(1))) {
/*      */               
/*  985 */               key = key.substring(0, 1).toLowerCase() + key.substring(1);
/*      */             } 
/*      */             
/*  988 */             Object result = method.invoke(bean, (Object[])null);
/*  989 */             if (result != null) {
/*  990 */               this.map.put(key, wrap(result));
/*      */             }
/*      */           } 
/*      */         } 
/*  994 */       } catch (Exception exception) {}
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject put(String key, boolean value) throws JSONException {
/* 1009 */     put(key, value ? Boolean.TRUE : Boolean.FALSE);
/* 1010 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject put(String key, Collection value) throws JSONException {
/* 1023 */     put(key, new JSONArray(value));
/* 1024 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject put(String key, double value) throws JSONException {
/* 1037 */     put(key, new Double(value));
/* 1038 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject put(String key, int value) throws JSONException {
/* 1051 */     put(key, new Integer(value));
/* 1052 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject put(String key, long value) throws JSONException {
/* 1065 */     put(key, new Long(value));
/* 1066 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject put(String key, Map value) throws JSONException {
/* 1079 */     put(key, new JSONObject(value));
/* 1080 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject put(String key, Object value) throws JSONException {
/* 1096 */     if (key == null) {
/* 1097 */       throw new JSONException("Null key.");
/*      */     }
/* 1099 */     if (value != null) {
/* 1100 */       testValidity(value);
/* 1101 */       this.map.put(key, value);
/*      */     } else {
/* 1103 */       remove(key);
/*      */     } 
/* 1105 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject putOnce(String key, Object value) throws JSONException {
/* 1119 */     if (key != null && value != null) {
/* 1120 */       if (opt(key) != null) {
/* 1121 */         throw new JSONException("Duplicate key \"" + key + "\"");
/*      */       }
/* 1123 */       put(key, value);
/*      */     } 
/* 1125 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONObject putOpt(String key, Object value) throws JSONException {
/* 1140 */     if (key != null && value != null) {
/* 1141 */       put(key, value);
/*      */     }
/* 1143 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String quote(String string) {
/* 1156 */     if (string == null || string.length() == 0) {
/* 1157 */       return "\"\"";
/*      */     }
/*      */ 
/*      */     
/* 1161 */     char c = Character.MIN_VALUE;
/*      */ 
/*      */     
/* 1164 */     int len = string.length();
/* 1165 */     StringBuffer sb = new StringBuffer(len + 4);
/*      */     
/* 1167 */     sb.append('"');
/* 1168 */     for (int i = 0; i < len; i++) {
/* 1169 */       char b = c;
/* 1170 */       c = string.charAt(i);
/* 1171 */       switch (c) {
/*      */         case '"':
/*      */         case '\\':
/* 1174 */           sb.append('\\');
/* 1175 */           sb.append(c);
/*      */           break;
/*      */         case '/':
/* 1178 */           if (b == '<') {
/* 1179 */             sb.append('\\');
/*      */           }
/* 1181 */           sb.append(c);
/*      */           break;
/*      */         case '\b':
/* 1184 */           sb.append("\\b");
/*      */           break;
/*      */         case '\t':
/* 1187 */           sb.append("\\t");
/*      */           break;
/*      */         case '\n':
/* 1190 */           sb.append("\\n");
/*      */           break;
/*      */         case '\f':
/* 1193 */           sb.append("\\f");
/*      */           break;
/*      */         case '\r':
/* 1196 */           sb.append("\\r");
/*      */           break;
/*      */         default:
/* 1199 */           if (c < ' ' || (c >= '' && c < ' ') || (c >= ' ' && c < '℀')) {
/*      */             
/* 1201 */             String hhhh = "000" + Integer.toHexString(c);
/* 1202 */             sb.append("\\u" + hhhh.substring(hhhh.length() - 4)); break;
/*      */           } 
/* 1204 */           sb.append(c);
/*      */           break;
/*      */       } 
/*      */     } 
/* 1208 */     sb.append('"');
/* 1209 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object remove(String key) {
/* 1219 */     return this.map.remove(key);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object stringToValue(String string) {
/* 1229 */     if (string.equals("")) {
/* 1230 */       return string;
/*      */     }
/* 1232 */     if (string.equalsIgnoreCase("true")) {
/* 1233 */       return Boolean.TRUE;
/*      */     }
/* 1235 */     if (string.equalsIgnoreCase("false")) {
/* 1236 */       return Boolean.FALSE;
/*      */     }
/* 1238 */     if (string.equalsIgnoreCase("null")) {
/* 1239 */       return NULL;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1251 */     char b = string.charAt(0);
/* 1252 */     if ((b >= '0' && b <= '9') || b == '.' || b == '-' || b == '+') {
/* 1253 */       if (b == '0' && string.length() > 2 && (string
/* 1254 */         .charAt(1) == 'x' || string.charAt(1) == 'X')) {
/*      */         try {
/* 1256 */           return new Integer(Integer.parseInt(string.substring(2), 16));
/* 1257 */         } catch (Exception exception) {}
/*      */       }
/*      */       
/*      */       try {
/* 1261 */         if (string.indexOf('.') > -1 || string
/* 1262 */           .indexOf('e') > -1 || string.indexOf('E') > -1) {
/* 1263 */           return Double.valueOf(string);
/*      */         }
/* 1265 */         Long myLong = new Long(string);
/* 1266 */         if (myLong.longValue() == myLong.intValue()) {
/* 1267 */           return new Integer(myLong.intValue());
/*      */         }
/* 1269 */         return myLong;
/*      */       
/*      */       }
/* 1272 */       catch (Exception exception) {}
/*      */     } 
/*      */     
/* 1275 */     return string;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void testValidity(Object o) throws JSONException {
/* 1285 */     if (o != null) {
/* 1286 */       if (o instanceof Double) {
/* 1287 */         if (((Double)o).isInfinite() || ((Double)o).isNaN()) {
/* 1288 */           throw new JSONException("JSON does not allow non-finite numbers.");
/*      */         }
/*      */       }
/* 1291 */       else if (o instanceof Float && ((
/* 1292 */         (Float)o).isInfinite() || ((Float)o).isNaN())) {
/* 1293 */         throw new JSONException("JSON does not allow non-finite numbers.");
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JSONArray toJSONArray(JSONArray names) throws JSONException {
/* 1310 */     if (names == null || names.length() == 0) {
/* 1311 */       return null;
/*      */     }
/* 1313 */     JSONArray ja = new JSONArray();
/* 1314 */     for (int i = 0; i < names.length(); i++) {
/* 1315 */       ja.put(opt(names.getString(i)));
/*      */     }
/* 1317 */     return ja;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*      */     try {
/* 1334 */       Iterator keys = keys();
/* 1335 */       StringBuffer sb = new StringBuffer("{");
/*      */       
/* 1337 */       while (keys.hasNext()) {
/* 1338 */         if (sb.length() > 1) {
/* 1339 */           sb.append(',');
/*      */         }
/* 1341 */         Object o = keys.next();
/* 1342 */         sb.append(quote(o.toString()));
/* 1343 */         sb.append(':');
/* 1344 */         sb.append(valueToString(this.map.get(o)));
/*      */       } 
/* 1346 */       sb.append('}');
/* 1347 */       return sb.toString();
/* 1348 */     } catch (Exception e) {
/* 1349 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString(int indentFactor) throws JSONException {
/* 1367 */     return toString(indentFactor, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   String toString(int indentFactor, int indent) throws JSONException {
/* 1386 */     int length = length();
/* 1387 */     if (length == 0) {
/* 1388 */       return "{}";
/*      */     }
/* 1390 */     Iterator keys = keys();
/* 1391 */     int newindent = indent + indentFactor;
/*      */     
/* 1393 */     StringBuffer sb = new StringBuffer("{");
/* 1394 */     if (length == 1) {
/* 1395 */       Object object = keys.next();
/* 1396 */       sb.append(quote(object.toString()));
/* 1397 */       sb.append(": ");
/* 1398 */       sb.append(valueToString(this.map.get(object), indentFactor, indent));
/*      */     } else {
/*      */       
/* 1401 */       while (keys.hasNext()) {
/* 1402 */         Object object = keys.next();
/* 1403 */         if (sb.length() > 1) {
/* 1404 */           sb.append(",\n");
/*      */         } else {
/* 1406 */           sb.append('\n');
/*      */         } 
/* 1408 */         for (int i = 0; i < newindent; i++) {
/* 1409 */           sb.append(' ');
/*      */         }
/* 1411 */         sb.append(quote(object.toString()));
/* 1412 */         sb.append(": ");
/* 1413 */         sb.append(valueToString(this.map.get(object), indentFactor, newindent));
/*      */       } 
/*      */       
/* 1416 */       if (sb.length() > 1) {
/* 1417 */         sb.append('\n');
/* 1418 */         for (int i = 0; i < indent; i++) {
/* 1419 */           sb.append(' ');
/*      */         }
/*      */       } 
/*      */     } 
/* 1423 */     sb.append('}');
/* 1424 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String valueToString(Object value) throws JSONException {
/* 1450 */     if (value == null || value.equals(null)) {
/* 1451 */       return "null";
/*      */     }
/* 1453 */     if (value instanceof JSONString) {
/*      */       Object object;
/*      */       try {
/* 1456 */         object = ((JSONString)value).toJSONString();
/* 1457 */       } catch (Exception e) {
/* 1458 */         throw new JSONException(e);
/*      */       } 
/* 1460 */       if (object instanceof String) {
/* 1461 */         return (String)object;
/*      */       }
/* 1463 */       throw new JSONException("Bad value from toJSONString: " + object);
/*      */     } 
/* 1465 */     if (value instanceof Number) {
/* 1466 */       return numberToString((Number)value);
/*      */     }
/* 1468 */     if (value instanceof Boolean || value instanceof JSONObject || value instanceof JSONArray)
/*      */     {
/* 1470 */       return value.toString();
/*      */     }
/* 1472 */     if (value instanceof Map) {
/* 1473 */       return (new JSONObject((Map)value)).toString();
/*      */     }
/* 1475 */     if (value instanceof Collection) {
/* 1476 */       return (new JSONArray((Collection)value)).toString();
/*      */     }
/* 1478 */     if (value.getClass().isArray()) {
/* 1479 */       return (new JSONArray(value)).toString();
/*      */     }
/* 1481 */     return quote(value.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static String valueToString(Object value, int indentFactor, int indent) throws JSONException {
/* 1504 */     if (value == null || value.equals(null)) {
/* 1505 */       return "null";
/*      */     }
/*      */     try {
/* 1508 */       if (value instanceof JSONString) {
/* 1509 */         Object o = ((JSONString)value).toJSONString();
/* 1510 */         if (o instanceof String) {
/* 1511 */           return (String)o;
/*      */         }
/*      */       } 
/* 1514 */     } catch (Exception exception) {}
/*      */     
/* 1516 */     if (value instanceof Number) {
/* 1517 */       return numberToString((Number)value);
/*      */     }
/* 1519 */     if (value instanceof Boolean) {
/* 1520 */       return value.toString();
/*      */     }
/* 1522 */     if (value instanceof JSONObject) {
/* 1523 */       return ((JSONObject)value).toString(indentFactor, indent);
/*      */     }
/* 1525 */     if (value instanceof JSONArray) {
/* 1526 */       return ((JSONArray)value).toString(indentFactor, indent);
/*      */     }
/* 1528 */     if (value instanceof Map) {
/* 1529 */       return (new JSONObject((Map)value)).toString(indentFactor, indent);
/*      */     }
/* 1531 */     if (value instanceof Collection) {
/* 1532 */       return (new JSONArray((Collection)value)).toString(indentFactor, indent);
/*      */     }
/* 1534 */     if (value.getClass().isArray()) {
/* 1535 */       return (new JSONArray(value)).toString(indentFactor, indent);
/*      */     }
/* 1537 */     return quote(value.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object wrap(Object object) {
/*      */     try {
/* 1555 */       if (object == null) {
/* 1556 */         return NULL;
/*      */       }
/* 1558 */       if (object instanceof JSONObject || object instanceof JSONArray || NULL
/* 1559 */         .equals(object) || object instanceof JSONString || object instanceof Byte || object instanceof Character || object instanceof Short || object instanceof Integer || object instanceof Long || object instanceof Boolean || object instanceof Float || object instanceof Double || object instanceof String)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1565 */         return object;
/*      */       }
/*      */       
/* 1568 */       if (object instanceof Collection) {
/* 1569 */         return new JSONArray((Collection)object);
/*      */       }
/* 1571 */       if (object.getClass().isArray()) {
/* 1572 */         return new JSONArray(object);
/*      */       }
/* 1574 */       if (object instanceof Map) {
/* 1575 */         return new JSONObject((Map)object);
/*      */       }
/* 1577 */       Package objectPackage = object.getClass().getPackage();
/*      */       
/* 1579 */       String objectPackageName = (objectPackage != null) ? objectPackage.getName() : "";
/* 1580 */       if (objectPackageName
/* 1581 */         .startsWith("java.") || objectPackageName
/* 1582 */         .startsWith("javax.") || object
/* 1583 */         .getClass().getClassLoader() == null)
/*      */       {
/* 1585 */         return object.toString();
/*      */       }
/* 1587 */       return new JSONObject(object);
/* 1588 */     } catch (Exception exception) {
/* 1589 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Writer write(Writer writer) throws JSONException {
/*      */     try {
/* 1605 */       boolean commanate = false;
/* 1606 */       Iterator keys = keys();
/* 1607 */       writer.write(123);
/*      */       
/* 1609 */       while (keys.hasNext()) {
/* 1610 */         if (commanate) {
/* 1611 */           writer.write(44);
/*      */         }
/* 1613 */         Object key = keys.next();
/* 1614 */         writer.write(quote(key.toString()));
/* 1615 */         writer.write(58);
/* 1616 */         Object value = this.map.get(key);
/* 1617 */         if (value instanceof JSONObject) {
/* 1618 */           ((JSONObject)value).write(writer);
/* 1619 */         } else if (value instanceof JSONArray) {
/* 1620 */           ((JSONArray)value).write(writer);
/*      */         } else {
/* 1622 */           writer.write(valueToString(value));
/*      */         } 
/* 1624 */         commanate = true;
/*      */       } 
/* 1626 */       writer.write(125);
/* 1627 */       return writer;
/* 1628 */     } catch (IOException exception) {
/* 1629 */       throw new JSONException(exception);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\json\JSONObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */