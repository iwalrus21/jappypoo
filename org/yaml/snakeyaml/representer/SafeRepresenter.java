/*     */ package org.yaml.snakeyaml.representer;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TimeZone;
/*     */ import java.util.UUID;
/*     */ import java.util.regex.Pattern;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
/*     */ import org.yaml.snakeyaml.reader.StreamReader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class SafeRepresenter
/*     */   extends BaseRepresenter
/*     */ {
/*     */   protected Map<Class<? extends Object>, Tag> classTags;
/*  46 */   protected TimeZone timeZone = null;
/*     */   
/*     */   public SafeRepresenter() {
/*  49 */     this.nullRepresenter = new RepresentNull();
/*  50 */     this.representers.put(String.class, new RepresentString());
/*  51 */     this.representers.put(Boolean.class, new RepresentBoolean());
/*  52 */     this.representers.put(Character.class, new RepresentString());
/*  53 */     this.representers.put(UUID.class, new RepresentUuid());
/*  54 */     this.representers.put(byte[].class, new RepresentByteArray());
/*     */     
/*  56 */     Represent primitiveArray = new RepresentPrimitiveArray();
/*  57 */     this.representers.put(short[].class, primitiveArray);
/*  58 */     this.representers.put(int[].class, primitiveArray);
/*  59 */     this.representers.put(long[].class, primitiveArray);
/*  60 */     this.representers.put(float[].class, primitiveArray);
/*  61 */     this.representers.put(double[].class, primitiveArray);
/*  62 */     this.representers.put(char[].class, primitiveArray);
/*  63 */     this.representers.put(boolean[].class, primitiveArray);
/*     */     
/*  65 */     this.multiRepresenters.put(Number.class, new RepresentNumber());
/*  66 */     this.multiRepresenters.put(List.class, new RepresentList());
/*  67 */     this.multiRepresenters.put(Map.class, new RepresentMap());
/*  68 */     this.multiRepresenters.put(Set.class, new RepresentSet());
/*  69 */     this.multiRepresenters.put(Iterator.class, new RepresentIterator());
/*  70 */     this.multiRepresenters.put((new Object[0]).getClass(), new RepresentArray());
/*  71 */     this.multiRepresenters.put(Date.class, new RepresentDate());
/*  72 */     this.multiRepresenters.put(Enum.class, new RepresentEnum());
/*  73 */     this.multiRepresenters.put(Calendar.class, new RepresentDate());
/*  74 */     this.classTags = new HashMap<Class<? extends Object>, Tag>();
/*     */   }
/*     */   
/*     */   protected Tag getTag(Class<?> clazz, Tag defaultTag) {
/*  78 */     if (this.classTags.containsKey(clazz)) {
/*  79 */       return this.classTags.get(clazz);
/*     */     }
/*  81 */     return defaultTag;
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
/*     */   public Tag addClassTag(Class<? extends Object> clazz, Tag tag) {
/*  96 */     if (tag == null) {
/*  97 */       throw new NullPointerException("Tag must be provided.");
/*     */     }
/*  99 */     return this.classTags.put(clazz, tag);
/*     */   }
/*     */   
/*     */   protected class RepresentNull implements Represent {
/*     */     public Node representData(Object data) {
/* 104 */       return SafeRepresenter.this.representScalar(Tag.NULL, "null");
/*     */     }
/*     */   }
/*     */   
/* 108 */   public static Pattern MULTILINE_PATTERN = Pattern.compile("\n|| | ");
/*     */   
/*     */   protected class RepresentString implements Represent {
/*     */     public Node representData(Object data) {
/* 112 */       Tag tag = Tag.STR;
/* 113 */       Character style = null;
/* 114 */       String value = data.toString();
/* 115 */       if (!StreamReader.isPrintable(value)) {
/* 116 */         char[] binary; tag = Tag.BINARY;
/*     */         
/*     */         try {
/* 119 */           byte[] bytes = value.getBytes("UTF-8");
/*     */ 
/*     */ 
/*     */           
/* 123 */           String checkValue = new String(bytes, "UTF-8");
/* 124 */           if (!checkValue.equals(value)) {
/* 125 */             throw new YAMLException("invalid string value has occurred");
/*     */           }
/* 127 */           binary = Base64Coder.encode(bytes);
/* 128 */         } catch (UnsupportedEncodingException e) {
/* 129 */           throw new YAMLException(e);
/*     */         } 
/* 131 */         value = String.valueOf(binary);
/* 132 */         style = Character.valueOf('|');
/*     */       } 
/*     */ 
/*     */       
/* 136 */       if (SafeRepresenter.this.defaultScalarStyle == null && SafeRepresenter.MULTILINE_PATTERN.matcher(value).find()) {
/* 137 */         style = Character.valueOf('|');
/*     */       }
/* 139 */       return SafeRepresenter.this.representScalar(tag, value, style);
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentBoolean implements Represent {
/*     */     public Node representData(Object data) {
/*     */       String value;
/* 146 */       if (Boolean.TRUE.equals(data)) {
/* 147 */         value = "true";
/*     */       } else {
/* 149 */         value = "false";
/*     */       } 
/* 151 */       return SafeRepresenter.this.representScalar(Tag.BOOL, value);
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentNumber implements Represent {
/*     */     public Node representData(Object data) {
/*     */       Tag tag;
/*     */       String value;
/* 159 */       if (data instanceof Byte || data instanceof Short || data instanceof Integer || data instanceof Long || data instanceof java.math.BigInteger) {
/*     */         
/* 161 */         tag = Tag.INT;
/* 162 */         value = data.toString();
/*     */       } else {
/* 164 */         Number number = (Number)data;
/* 165 */         tag = Tag.FLOAT;
/* 166 */         if (number.equals(Double.valueOf(Double.NaN))) {
/* 167 */           value = ".NaN";
/* 168 */         } else if (number.equals(Double.valueOf(Double.POSITIVE_INFINITY))) {
/* 169 */           value = ".inf";
/* 170 */         } else if (number.equals(Double.valueOf(Double.NEGATIVE_INFINITY))) {
/* 171 */           value = "-.inf";
/*     */         } else {
/* 173 */           value = number.toString();
/*     */         } 
/*     */       } 
/* 176 */       return SafeRepresenter.this.representScalar(SafeRepresenter.this.getTag(data.getClass(), tag), value);
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentList
/*     */     implements Represent {
/*     */     public Node representData(Object data) {
/* 183 */       return SafeRepresenter.this.representSequence(SafeRepresenter.this.getTag(data.getClass(), Tag.SEQ), (List)data, null);
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentIterator
/*     */     implements Represent {
/*     */     public Node representData(Object data) {
/* 190 */       Iterator<Object> iter = (Iterator<Object>)data;
/* 191 */       return SafeRepresenter.this.representSequence(SafeRepresenter.this.getTag(data.getClass(), Tag.SEQ), new SafeRepresenter.IteratorWrapper(iter), null);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class IteratorWrapper
/*     */     implements Iterable<Object> {
/*     */     private Iterator<Object> iter;
/*     */     
/*     */     public IteratorWrapper(Iterator<Object> iter) {
/* 200 */       this.iter = iter;
/*     */     }
/*     */     
/*     */     public Iterator<Object> iterator() {
/* 204 */       return this.iter;
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentArray implements Represent {
/*     */     public Node representData(Object data) {
/* 210 */       Object[] array = (Object[])data;
/* 211 */       List<Object> list = Arrays.asList(array);
/* 212 */       return SafeRepresenter.this.representSequence(Tag.SEQ, list, null);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected class RepresentPrimitiveArray
/*     */     implements Represent
/*     */   {
/*     */     public Node representData(Object data) {
/* 223 */       Class<?> type = data.getClass().getComponentType();
/*     */       
/* 225 */       if (byte.class == type)
/* 226 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asByteList(data), null); 
/* 227 */       if (short.class == type)
/* 228 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asShortList(data), null); 
/* 229 */       if (int.class == type)
/* 230 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asIntList(data), null); 
/* 231 */       if (long.class == type)
/* 232 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asLongList(data), null); 
/* 233 */       if (float.class == type)
/* 234 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asFloatList(data), null); 
/* 235 */       if (double.class == type)
/* 236 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asDoubleList(data), null); 
/* 237 */       if (char.class == type)
/* 238 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asCharList(data), null); 
/* 239 */       if (boolean.class == type) {
/* 240 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asBooleanList(data), null);
/*     */       }
/*     */       
/* 243 */       throw new YAMLException("Unexpected primitive '" + type.getCanonicalName() + "'");
/*     */     }
/*     */     
/*     */     private List<Byte> asByteList(Object in) {
/* 247 */       byte[] array = (byte[])in;
/* 248 */       List<Byte> list = new ArrayList<Byte>(array.length);
/* 249 */       for (int i = 0; i < array.length; i++)
/* 250 */         list.add(Byte.valueOf(array[i])); 
/* 251 */       return list;
/*     */     }
/*     */     
/*     */     private List<Short> asShortList(Object in) {
/* 255 */       short[] array = (short[])in;
/* 256 */       List<Short> list = new ArrayList<Short>(array.length);
/* 257 */       for (int i = 0; i < array.length; i++)
/* 258 */         list.add(Short.valueOf(array[i])); 
/* 259 */       return list;
/*     */     }
/*     */     
/*     */     private List<Integer> asIntList(Object in) {
/* 263 */       int[] array = (int[])in;
/* 264 */       List<Integer> list = new ArrayList<Integer>(array.length);
/* 265 */       for (int i = 0; i < array.length; i++)
/* 266 */         list.add(Integer.valueOf(array[i])); 
/* 267 */       return list;
/*     */     }
/*     */     
/*     */     private List<Long> asLongList(Object in) {
/* 271 */       long[] array = (long[])in;
/* 272 */       List<Long> list = new ArrayList<Long>(array.length);
/* 273 */       for (int i = 0; i < array.length; i++)
/* 274 */         list.add(Long.valueOf(array[i])); 
/* 275 */       return list;
/*     */     }
/*     */     
/*     */     private List<Float> asFloatList(Object in) {
/* 279 */       float[] array = (float[])in;
/* 280 */       List<Float> list = new ArrayList<Float>(array.length);
/* 281 */       for (int i = 0; i < array.length; i++)
/* 282 */         list.add(Float.valueOf(array[i])); 
/* 283 */       return list;
/*     */     }
/*     */     
/*     */     private List<Double> asDoubleList(Object in) {
/* 287 */       double[] array = (double[])in;
/* 288 */       List<Double> list = new ArrayList<Double>(array.length);
/* 289 */       for (int i = 0; i < array.length; i++)
/* 290 */         list.add(Double.valueOf(array[i])); 
/* 291 */       return list;
/*     */     }
/*     */     
/*     */     private List<Character> asCharList(Object in) {
/* 295 */       char[] array = (char[])in;
/* 296 */       List<Character> list = new ArrayList<Character>(array.length);
/* 297 */       for (int i = 0; i < array.length; i++)
/* 298 */         list.add(Character.valueOf(array[i])); 
/* 299 */       return list;
/*     */     }
/*     */     
/*     */     private List<Boolean> asBooleanList(Object in) {
/* 303 */       boolean[] array = (boolean[])in;
/* 304 */       List<Boolean> list = new ArrayList<Boolean>(array.length);
/* 305 */       for (int i = 0; i < array.length; i++)
/* 306 */         list.add(Boolean.valueOf(array[i])); 
/* 307 */       return list;
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentMap
/*     */     implements Represent {
/*     */     public Node representData(Object data) {
/* 314 */       return SafeRepresenter.this.representMapping(SafeRepresenter.this.getTag(data.getClass(), Tag.MAP), (Map<?, ?>)data, null);
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentSet
/*     */     implements Represent
/*     */   {
/*     */     public Node representData(Object data) {
/* 322 */       Map<Object, Object> value = new LinkedHashMap<Object, Object>();
/* 323 */       Set<Object> set = (Set<Object>)data;
/* 324 */       for (Object key : set) {
/* 325 */         value.put(key, null);
/*     */       }
/* 327 */       return SafeRepresenter.this.representMapping(SafeRepresenter.this.getTag(data.getClass(), Tag.SET), value, null);
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentDate
/*     */     implements Represent {
/*     */     public Node representData(Object data) {
/*     */       Calendar calendar;
/* 335 */       if (data instanceof Calendar) {
/* 336 */         calendar = (Calendar)data;
/*     */       } else {
/* 338 */         calendar = Calendar.getInstance((SafeRepresenter.this.getTimeZone() == null) ? TimeZone.getTimeZone("UTC") : SafeRepresenter.this.timeZone);
/*     */         
/* 340 */         calendar.setTime((Date)data);
/*     */       } 
/* 342 */       int years = calendar.get(1);
/* 343 */       int months = calendar.get(2) + 1;
/* 344 */       int days = calendar.get(5);
/* 345 */       int hour24 = calendar.get(11);
/* 346 */       int minutes = calendar.get(12);
/* 347 */       int seconds = calendar.get(13);
/* 348 */       int millis = calendar.get(14);
/* 349 */       StringBuilder buffer = new StringBuilder(String.valueOf(years));
/* 350 */       while (buffer.length() < 4)
/*     */       {
/* 352 */         buffer.insert(0, "0");
/*     */       }
/* 354 */       buffer.append("-");
/* 355 */       if (months < 10) {
/* 356 */         buffer.append("0");
/*     */       }
/* 358 */       buffer.append(String.valueOf(months));
/* 359 */       buffer.append("-");
/* 360 */       if (days < 10) {
/* 361 */         buffer.append("0");
/*     */       }
/* 363 */       buffer.append(String.valueOf(days));
/* 364 */       buffer.append("T");
/* 365 */       if (hour24 < 10) {
/* 366 */         buffer.append("0");
/*     */       }
/* 368 */       buffer.append(String.valueOf(hour24));
/* 369 */       buffer.append(":");
/* 370 */       if (minutes < 10) {
/* 371 */         buffer.append("0");
/*     */       }
/* 373 */       buffer.append(String.valueOf(minutes));
/* 374 */       buffer.append(":");
/* 375 */       if (seconds < 10) {
/* 376 */         buffer.append("0");
/*     */       }
/* 378 */       buffer.append(String.valueOf(seconds));
/* 379 */       if (millis > 0) {
/* 380 */         if (millis < 10) {
/* 381 */           buffer.append(".00");
/* 382 */         } else if (millis < 100) {
/* 383 */           buffer.append(".0");
/*     */         } else {
/* 385 */           buffer.append(".");
/*     */         } 
/* 387 */         buffer.append(String.valueOf(millis));
/*     */       } 
/*     */ 
/*     */       
/* 391 */       int gmtOffset = calendar.getTimeZone().getOffset(calendar.get(0), calendar
/* 392 */           .get(1), calendar.get(2), calendar
/* 393 */           .get(5), calendar.get(7), calendar
/* 394 */           .get(14));
/* 395 */       if (gmtOffset == 0) {
/* 396 */         buffer.append('Z');
/*     */       } else {
/* 398 */         if (gmtOffset < 0) {
/* 399 */           buffer.append('-');
/* 400 */           gmtOffset *= -1;
/*     */         } else {
/* 402 */           buffer.append('+');
/*     */         } 
/* 404 */         int minutesOffset = gmtOffset / 60000;
/* 405 */         int hoursOffset = minutesOffset / 60;
/* 406 */         int partOfHour = minutesOffset % 60;
/*     */         
/* 408 */         if (hoursOffset < 10) {
/* 409 */           buffer.append('0');
/*     */         }
/* 411 */         buffer.append(hoursOffset);
/* 412 */         buffer.append(':');
/* 413 */         if (partOfHour < 10) {
/* 414 */           buffer.append('0');
/*     */         }
/* 416 */         buffer.append(partOfHour);
/*     */       } 
/*     */       
/* 419 */       return SafeRepresenter.this.representScalar(SafeRepresenter.this.getTag(data.getClass(), Tag.TIMESTAMP), buffer.toString(), null);
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentEnum implements Represent {
/*     */     public Node representData(Object data) {
/* 425 */       Tag tag = new Tag(data.getClass());
/* 426 */       return SafeRepresenter.this.representScalar(SafeRepresenter.this.getTag(data.getClass(), tag), ((Enum)data).name());
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentByteArray implements Represent {
/*     */     public Node representData(Object data) {
/* 432 */       char[] binary = Base64Coder.encode((byte[])data);
/* 433 */       return SafeRepresenter.this.representScalar(Tag.BINARY, String.valueOf(binary), Character.valueOf('|'));
/*     */     }
/*     */   }
/*     */   
/*     */   public TimeZone getTimeZone() {
/* 438 */     return this.timeZone;
/*     */   }
/*     */   
/*     */   public void setTimeZone(TimeZone timeZone) {
/* 442 */     this.timeZone = timeZone;
/*     */   }
/*     */   
/*     */   protected class RepresentUuid implements Represent {
/*     */     public Node representData(Object data) {
/* 447 */       return SafeRepresenter.this.representScalar(SafeRepresenter.this.getTag(data.getClass(), new Tag(UUID.class)), data.toString());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\yaml\snakeyaml\representer\SafeRepresenter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */