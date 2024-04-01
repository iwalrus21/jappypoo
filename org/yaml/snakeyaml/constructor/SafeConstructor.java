/*     */ package org.yaml.snakeyaml.constructor;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TimeZone;
/*     */ import java.util.TreeSet;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
/*     */ import org.yaml.snakeyaml.nodes.MappingNode;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.NodeId;
/*     */ import org.yaml.snakeyaml.nodes.NodeTuple;
/*     */ import org.yaml.snakeyaml.nodes.ScalarNode;
/*     */ import org.yaml.snakeyaml.nodes.SequenceNode;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SafeConstructor
/*     */   extends BaseConstructor
/*     */ {
/*  47 */   public static final ConstructUndefined undefinedConstructor = new ConstructUndefined();
/*     */   
/*     */   public SafeConstructor() {
/*  50 */     this.yamlConstructors.put(Tag.NULL, new ConstructYamlNull());
/*  51 */     this.yamlConstructors.put(Tag.BOOL, new ConstructYamlBool());
/*  52 */     this.yamlConstructors.put(Tag.INT, new ConstructYamlInt());
/*  53 */     this.yamlConstructors.put(Tag.FLOAT, new ConstructYamlFloat());
/*  54 */     this.yamlConstructors.put(Tag.BINARY, new ConstructYamlBinary());
/*  55 */     this.yamlConstructors.put(Tag.TIMESTAMP, new ConstructYamlTimestamp());
/*  56 */     this.yamlConstructors.put(Tag.OMAP, new ConstructYamlOmap());
/*  57 */     this.yamlConstructors.put(Tag.PAIRS, new ConstructYamlPairs());
/*  58 */     this.yamlConstructors.put(Tag.SET, new ConstructYamlSet());
/*  59 */     this.yamlConstructors.put(Tag.STR, new ConstructYamlStr());
/*  60 */     this.yamlConstructors.put(Tag.SEQ, new ConstructYamlSeq());
/*  61 */     this.yamlConstructors.put(Tag.MAP, new ConstructYamlMap());
/*  62 */     this.yamlConstructors.put(null, undefinedConstructor);
/*  63 */     this.yamlClassConstructors.put(NodeId.scalar, undefinedConstructor);
/*  64 */     this.yamlClassConstructors.put(NodeId.sequence, undefinedConstructor);
/*  65 */     this.yamlClassConstructors.put(NodeId.mapping, undefinedConstructor);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void flattenMapping(MappingNode node) {
/*  70 */     processDuplicateKeys(node);
/*  71 */     if (node.isMerged()) {
/*  72 */       node.setValue(mergeNode(node, true, new HashMap<Object, Integer>(), new ArrayList<NodeTuple>()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void processDuplicateKeys(MappingNode node) {
/*  78 */     List<NodeTuple> nodeValue = node.getValue();
/*  79 */     Map<Object, Integer> keys = new HashMap<Object, Integer>(nodeValue.size());
/*  80 */     TreeSet<Integer> toRemove = new TreeSet<Integer>();
/*  81 */     int i = 0;
/*  82 */     for (NodeTuple tuple : nodeValue) {
/*  83 */       Node keyNode = tuple.getKeyNode();
/*  84 */       if (!keyNode.getTag().equals(Tag.MERGE)) {
/*  85 */         Object key = constructObject(keyNode);
/*  86 */         if (key != null) {
/*     */           try {
/*  88 */             key.hashCode();
/*  89 */           } catch (Exception e) {
/*  90 */             throw new ConstructorException("while constructing a mapping", node
/*  91 */                 .getStartMark(), "found unacceptable key " + key, tuple
/*  92 */                 .getKeyNode().getStartMark(), e);
/*     */           } 
/*     */         }
/*     */         
/*  96 */         Integer prevIndex = keys.put(key, Integer.valueOf(i));
/*  97 */         if (prevIndex != null) {
/*  98 */           if (!isAllowDuplicateKeys()) {
/*  99 */             throw new IllegalStateException("duplicate key: " + key);
/*     */           }
/* 101 */           toRemove.add(prevIndex);
/*     */         } 
/*     */       } 
/* 104 */       i++;
/*     */     } 
/*     */     
/* 107 */     Iterator<Integer> indicies2remove = toRemove.descendingIterator();
/* 108 */     while (indicies2remove.hasNext()) {
/* 109 */       nodeValue.remove(((Integer)indicies2remove.next()).intValue());
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
/*     */   
/*     */   private List<NodeTuple> mergeNode(MappingNode node, boolean isPreffered, Map<Object, Integer> key2index, List<NodeTuple> values) {
/* 129 */     Iterator<NodeTuple> iter = node.getValue().iterator();
/* 130 */     while (iter.hasNext()) {
/* 131 */       NodeTuple nodeTuple = iter.next();
/* 132 */       Node keyNode = nodeTuple.getKeyNode();
/* 133 */       Node valueNode = nodeTuple.getValueNode();
/* 134 */       if (keyNode.getTag().equals(Tag.MERGE)) {
/* 135 */         MappingNode mn; SequenceNode sn; List<Node> vals; iter.remove();
/* 136 */         switch (valueNode.getNodeId()) {
/*     */           case mapping:
/* 138 */             mn = (MappingNode)valueNode;
/* 139 */             mergeNode(mn, false, key2index, values);
/*     */             continue;
/*     */           case sequence:
/* 142 */             sn = (SequenceNode)valueNode;
/* 143 */             vals = sn.getValue();
/* 144 */             for (Node subnode : vals) {
/* 145 */               if (!(subnode instanceof MappingNode)) {
/* 146 */                 throw new ConstructorException("while constructing a mapping", node
/* 147 */                     .getStartMark(), "expected a mapping for merging, but found " + subnode
/*     */                     
/* 149 */                     .getNodeId(), subnode
/* 150 */                     .getStartMark());
/*     */               }
/* 152 */               MappingNode mnode = (MappingNode)subnode;
/* 153 */               mergeNode(mnode, false, key2index, values);
/*     */             } 
/*     */             continue;
/*     */         } 
/* 157 */         throw new ConstructorException("while constructing a mapping", node
/* 158 */             .getStartMark(), "expected a mapping or list of mappings for merging, but found " + valueNode
/*     */             
/* 160 */             .getNodeId(), valueNode
/* 161 */             .getStartMark());
/*     */       } 
/*     */ 
/*     */       
/* 165 */       Object key = constructObject(keyNode);
/* 166 */       if (!key2index.containsKey(key)) {
/* 167 */         values.add(nodeTuple);
/*     */         
/* 169 */         key2index.put(key, Integer.valueOf(values.size() - 1)); continue;
/* 170 */       }  if (isPreffered)
/*     */       {
/*     */         
/* 173 */         values.set(((Integer)key2index.get(key)).intValue(), nodeTuple);
/*     */       }
/*     */     } 
/*     */     
/* 177 */     return values;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void constructMapping2ndStep(MappingNode node, Map<Object, Object> mapping) {
/* 182 */     flattenMapping(node);
/* 183 */     super.constructMapping2ndStep(node, mapping);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void constructSet2ndStep(MappingNode node, Set<Object> set) {
/* 188 */     flattenMapping(node);
/* 189 */     super.constructSet2ndStep(node, set);
/*     */   }
/*     */   
/*     */   public class ConstructYamlNull
/*     */     extends AbstractConstruct {
/*     */     public Object construct(Node node) {
/* 195 */       SafeConstructor.this.constructScalar((ScalarNode)node);
/* 196 */       return null;
/*     */     }
/*     */   }
/*     */   
/* 200 */   private static final Map<String, Boolean> BOOL_VALUES = new HashMap<String, Boolean>();
/*     */   static {
/* 202 */     BOOL_VALUES.put("yes", Boolean.TRUE);
/* 203 */     BOOL_VALUES.put("no", Boolean.FALSE);
/* 204 */     BOOL_VALUES.put("true", Boolean.TRUE);
/* 205 */     BOOL_VALUES.put("false", Boolean.FALSE);
/* 206 */     BOOL_VALUES.put("on", Boolean.TRUE);
/* 207 */     BOOL_VALUES.put("off", Boolean.FALSE);
/*     */   }
/*     */   
/*     */   public class ConstructYamlBool
/*     */     extends AbstractConstruct {
/*     */     public Object construct(Node node) {
/* 213 */       String val = (String)SafeConstructor.this.constructScalar((ScalarNode)node);
/* 214 */       return SafeConstructor.BOOL_VALUES.get(val.toLowerCase());
/*     */     }
/*     */   }
/*     */   
/*     */   public class ConstructYamlInt
/*     */     extends AbstractConstruct {
/*     */     public Object construct(Node node) {
/* 221 */       String value = SafeConstructor.this.constructScalar((ScalarNode)node).toString().replaceAll("_", "");
/* 222 */       int sign = 1;
/* 223 */       char first = value.charAt(0);
/* 224 */       if (first == '-') {
/* 225 */         sign = -1;
/* 226 */         value = value.substring(1);
/* 227 */       } else if (first == '+') {
/* 228 */         value = value.substring(1);
/*     */       } 
/* 230 */       int base = 10;
/* 231 */       if ("0".equals(value))
/* 232 */         return Integer.valueOf(0); 
/* 233 */       if (value.startsWith("0b"))
/* 234 */       { value = value.substring(2);
/* 235 */         base = 2; }
/* 236 */       else if (value.startsWith("0x"))
/* 237 */       { value = value.substring(2);
/* 238 */         base = 16; }
/* 239 */       else if (value.startsWith("0"))
/* 240 */       { value = value.substring(1);
/* 241 */         base = 8; }
/* 242 */       else { if (value.indexOf(':') != -1) {
/* 243 */           String[] digits = value.split(":");
/* 244 */           int bes = 1;
/* 245 */           int val = 0;
/* 246 */           for (int i = 0, j = digits.length; i < j; i++) {
/* 247 */             val = (int)(val + Long.parseLong(digits[j - i - 1]) * bes);
/* 248 */             bes *= 60;
/*     */           } 
/* 250 */           return SafeConstructor.this.createNumber(sign, String.valueOf(val), 10);
/*     */         } 
/* 252 */         return SafeConstructor.this.createNumber(sign, value, 10); }
/*     */       
/* 254 */       return SafeConstructor.this.createNumber(sign, value, base);
/*     */     }
/*     */   }
/*     */   
/*     */   private Number createNumber(int sign, String number, int radix) {
/*     */     Number result;
/* 260 */     if (sign < 0) {
/* 261 */       number = "-" + number;
/*     */     }
/*     */     try {
/* 264 */       result = Integer.valueOf(number, radix);
/* 265 */     } catch (NumberFormatException e) {
/*     */       try {
/* 267 */         result = Long.valueOf(number, radix);
/* 268 */       } catch (NumberFormatException e1) {
/* 269 */         result = new BigInteger(number, radix);
/*     */       } 
/*     */     } 
/* 272 */     return result;
/*     */   }
/*     */   
/*     */   public class ConstructYamlFloat
/*     */     extends AbstractConstruct {
/*     */     public Object construct(Node node) {
/* 278 */       String value = SafeConstructor.this.constructScalar((ScalarNode)node).toString().replaceAll("_", "");
/* 279 */       int sign = 1;
/* 280 */       char first = value.charAt(0);
/* 281 */       if (first == '-') {
/* 282 */         sign = -1;
/* 283 */         value = value.substring(1);
/* 284 */       } else if (first == '+') {
/* 285 */         value = value.substring(1);
/*     */       } 
/* 287 */       String valLower = value.toLowerCase();
/* 288 */       if (".inf".equals(valLower))
/* 289 */         return new Double((sign == -1) ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY); 
/* 290 */       if (".nan".equals(valLower))
/* 291 */         return new Double(Double.NaN); 
/* 292 */       if (value.indexOf(':') != -1) {
/* 293 */         String[] digits = value.split(":");
/* 294 */         int bes = 1;
/* 295 */         double val = 0.0D;
/* 296 */         for (int i = 0, j = digits.length; i < j; i++) {
/* 297 */           val += Double.parseDouble(digits[j - i - 1]) * bes;
/* 298 */           bes *= 60;
/*     */         } 
/* 300 */         return new Double(sign * val);
/*     */       } 
/* 302 */       Double d = Double.valueOf(value);
/* 303 */       return new Double(d.doubleValue() * sign);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public class ConstructYamlBinary
/*     */     extends AbstractConstruct
/*     */   {
/*     */     public Object construct(Node node) {
/* 312 */       String noWhiteSpaces = SafeConstructor.this.constructScalar((ScalarNode)node).toString().replaceAll("\\s", "");
/*     */       
/* 314 */       byte[] decoded = Base64Coder.decode(noWhiteSpaces.toCharArray());
/* 315 */       return decoded;
/*     */     }
/*     */   }
/*     */   
/* 319 */   private static final Pattern TIMESTAMP_REGEXP = Pattern.compile("^([0-9][0-9][0-9][0-9])-([0-9][0-9]?)-([0-9][0-9]?)(?:(?:[Tt]|[ \t]+)([0-9][0-9]?):([0-9][0-9]):([0-9][0-9])(?:\\.([0-9]*))?(?:[ \t]*(?:Z|([-+][0-9][0-9]?)(?::([0-9][0-9])?)?))?)?$");
/*     */ 
/*     */   
/* 322 */   private static final Pattern YMD_REGEXP = Pattern.compile("^([0-9][0-9][0-9][0-9])-([0-9][0-9]?)-([0-9][0-9]?)$");
/*     */   
/*     */   public static class ConstructYamlTimestamp extends AbstractConstruct {
/*     */     private Calendar calendar;
/*     */     
/*     */     public Calendar getCalendar() {
/* 328 */       return this.calendar;
/*     */     }
/*     */     
/*     */     public Object construct(Node node) {
/*     */       TimeZone timeZone;
/* 333 */       ScalarNode scalar = (ScalarNode)node;
/* 334 */       String nodeValue = scalar.getValue();
/* 335 */       Matcher match = SafeConstructor.YMD_REGEXP.matcher(nodeValue);
/* 336 */       if (match.matches()) {
/* 337 */         String str1 = match.group(1);
/* 338 */         String str2 = match.group(2);
/* 339 */         String str3 = match.group(3);
/* 340 */         this.calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
/* 341 */         this.calendar.clear();
/* 342 */         this.calendar.set(1, Integer.parseInt(str1));
/*     */         
/* 344 */         this.calendar.set(2, Integer.parseInt(str2) - 1);
/* 345 */         this.calendar.set(5, Integer.parseInt(str3));
/* 346 */         return this.calendar.getTime();
/*     */       } 
/* 348 */       match = SafeConstructor.TIMESTAMP_REGEXP.matcher(nodeValue);
/* 349 */       if (!match.matches()) {
/* 350 */         throw new YAMLException("Unexpected timestamp: " + nodeValue);
/*     */       }
/* 352 */       String year_s = match.group(1);
/* 353 */       String month_s = match.group(2);
/* 354 */       String day_s = match.group(3);
/* 355 */       String hour_s = match.group(4);
/* 356 */       String min_s = match.group(5);
/*     */       
/* 358 */       String seconds = match.group(6);
/* 359 */       String millis = match.group(7);
/* 360 */       if (millis != null) {
/* 361 */         seconds = seconds + "." + millis;
/*     */       }
/* 363 */       double fractions = Double.parseDouble(seconds);
/* 364 */       int sec_s = (int)Math.round(Math.floor(fractions));
/* 365 */       int usec = (int)Math.round((fractions - sec_s) * 1000.0D);
/*     */       
/* 367 */       String timezoneh_s = match.group(8);
/* 368 */       String timezonem_s = match.group(9);
/*     */       
/* 370 */       if (timezoneh_s != null) {
/* 371 */         String time = (timezonem_s != null) ? (":" + timezonem_s) : "00";
/* 372 */         timeZone = TimeZone.getTimeZone("GMT" + timezoneh_s + time);
/*     */       } else {
/*     */         
/* 375 */         timeZone = TimeZone.getTimeZone("UTC");
/*     */       } 
/* 377 */       this.calendar = Calendar.getInstance(timeZone);
/* 378 */       this.calendar.set(1, Integer.parseInt(year_s));
/*     */       
/* 380 */       this.calendar.set(2, Integer.parseInt(month_s) - 1);
/* 381 */       this.calendar.set(5, Integer.parseInt(day_s));
/* 382 */       this.calendar.set(11, Integer.parseInt(hour_s));
/* 383 */       this.calendar.set(12, Integer.parseInt(min_s));
/* 384 */       this.calendar.set(13, sec_s);
/* 385 */       this.calendar.set(14, usec);
/* 386 */       return this.calendar.getTime();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public class ConstructYamlOmap
/*     */     extends AbstractConstruct
/*     */   {
/*     */     public Object construct(Node node) {
/* 396 */       Map<Object, Object> omap = new LinkedHashMap<Object, Object>();
/* 397 */       if (!(node instanceof SequenceNode)) {
/* 398 */         throw new ConstructorException("while constructing an ordered map", node
/* 399 */             .getStartMark(), "expected a sequence, but found " + node.getNodeId(), node
/* 400 */             .getStartMark());
/*     */       }
/* 402 */       SequenceNode snode = (SequenceNode)node;
/* 403 */       for (Node subnode : snode.getValue()) {
/* 404 */         if (!(subnode instanceof MappingNode)) {
/* 405 */           throw new ConstructorException("while constructing an ordered map", node
/* 406 */               .getStartMark(), "expected a mapping of length 1, but found " + subnode
/* 407 */               .getNodeId(), subnode
/* 408 */               .getStartMark());
/*     */         }
/* 410 */         MappingNode mnode = (MappingNode)subnode;
/* 411 */         if (mnode.getValue().size() != 1) {
/* 412 */           throw new ConstructorException("while constructing an ordered map", node
/* 413 */               .getStartMark(), "expected a single mapping item, but found " + mnode
/* 414 */               .getValue().size() + " items", mnode
/* 415 */               .getStartMark());
/*     */         }
/* 417 */         Node keyNode = ((NodeTuple)mnode.getValue().get(0)).getKeyNode();
/* 418 */         Node valueNode = ((NodeTuple)mnode.getValue().get(0)).getValueNode();
/* 419 */         Object key = SafeConstructor.this.constructObject(keyNode);
/* 420 */         Object value = SafeConstructor.this.constructObject(valueNode);
/* 421 */         omap.put(key, value);
/*     */       } 
/* 423 */       return omap;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public class ConstructYamlPairs
/*     */     extends AbstractConstruct
/*     */   {
/*     */     public Object construct(Node node) {
/* 433 */       if (!(node instanceof SequenceNode)) {
/* 434 */         throw new ConstructorException("while constructing pairs", node.getStartMark(), "expected a sequence, but found " + node
/* 435 */             .getNodeId(), node.getStartMark());
/*     */       }
/* 437 */       SequenceNode snode = (SequenceNode)node;
/* 438 */       List<Object[]> pairs = new ArrayList(snode.getValue().size());
/* 439 */       for (Node subnode : snode.getValue()) {
/* 440 */         if (!(subnode instanceof MappingNode)) {
/* 441 */           throw new ConstructorException("while constructingpairs", node.getStartMark(), "expected a mapping of length 1, but found " + subnode
/* 442 */               .getNodeId(), subnode
/* 443 */               .getStartMark());
/*     */         }
/* 445 */         MappingNode mnode = (MappingNode)subnode;
/* 446 */         if (mnode.getValue().size() != 1) {
/* 447 */           throw new ConstructorException("while constructing pairs", node.getStartMark(), "expected a single mapping item, but found " + mnode
/* 448 */               .getValue().size() + " items", mnode
/*     */               
/* 450 */               .getStartMark());
/*     */         }
/* 452 */         Node keyNode = ((NodeTuple)mnode.getValue().get(0)).getKeyNode();
/* 453 */         Node valueNode = ((NodeTuple)mnode.getValue().get(0)).getValueNode();
/* 454 */         Object key = SafeConstructor.this.constructObject(keyNode);
/* 455 */         Object value = SafeConstructor.this.constructObject(valueNode);
/* 456 */         pairs.add(new Object[] { key, value });
/*     */       } 
/* 458 */       return pairs;
/*     */     }
/*     */   }
/*     */   
/*     */   public class ConstructYamlSet
/*     */     implements Construct {
/*     */     public Object construct(Node node) {
/* 465 */       if (node.isTwoStepsConstruction()) {
/* 466 */         return SafeConstructor.this.constructedObjects.containsKey(node) ? SafeConstructor.this.constructedObjects.get(node) : SafeConstructor.this
/* 467 */           .createDefaultSet();
/*     */       }
/* 469 */       return SafeConstructor.this.constructSet((MappingNode)node);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void construct2ndStep(Node node, Object object) {
/* 476 */       if (node.isTwoStepsConstruction()) {
/* 477 */         SafeConstructor.this.constructSet2ndStep((MappingNode)node, (Set<Object>)object);
/*     */       } else {
/* 479 */         throw new YAMLException("Unexpected recursive set structure. Node: " + node);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public class ConstructYamlStr
/*     */     extends AbstractConstruct {
/*     */     public Object construct(Node node) {
/* 487 */       return SafeConstructor.this.constructScalar((ScalarNode)node);
/*     */     }
/*     */   }
/*     */   
/*     */   public class ConstructYamlSeq
/*     */     implements Construct {
/*     */     public Object construct(Node node) {
/* 494 */       SequenceNode seqNode = (SequenceNode)node;
/* 495 */       if (node.isTwoStepsConstruction()) {
/* 496 */         return SafeConstructor.this.newList(seqNode);
/*     */       }
/* 498 */       return SafeConstructor.this.constructSequence(seqNode);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void construct2ndStep(Node node, Object data) {
/* 505 */       if (node.isTwoStepsConstruction()) {
/* 506 */         SafeConstructor.this.constructSequenceStep2((SequenceNode)node, (List)data);
/*     */       } else {
/* 508 */         throw new YAMLException("Unexpected recursive sequence structure. Node: " + node);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public class ConstructYamlMap
/*     */     implements Construct {
/*     */     public Object construct(Node node) {
/* 516 */       if (node.isTwoStepsConstruction()) {
/* 517 */         return SafeConstructor.this.createDefaultMap();
/*     */       }
/* 519 */       return SafeConstructor.this.constructMapping((MappingNode)node);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void construct2ndStep(Node node, Object object) {
/* 526 */       if (node.isTwoStepsConstruction()) {
/* 527 */         SafeConstructor.this.constructMapping2ndStep((MappingNode)node, (Map<Object, Object>)object);
/*     */       } else {
/* 529 */         throw new YAMLException("Unexpected recursive mapping structure. Node: " + node);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class ConstructUndefined
/*     */     extends AbstractConstruct {
/*     */     public Object construct(Node node) {
/* 537 */       throw new ConstructorException(null, null, "could not determine a constructor for the tag " + node
/* 538 */           .getTag(), node
/* 539 */           .getStartMark());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\yaml\snakeyaml\constructor\SafeConstructor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */