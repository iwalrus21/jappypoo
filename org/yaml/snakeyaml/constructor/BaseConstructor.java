/*     */ package org.yaml.snakeyaml.constructor;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumMap;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ import org.yaml.snakeyaml.TypeDescription;
/*     */ import org.yaml.snakeyaml.composer.Composer;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.introspector.PropertyUtils;
/*     */ import org.yaml.snakeyaml.nodes.CollectionNode;
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
/*     */ 
/*     */ public abstract class BaseConstructor
/*     */ {
/*  54 */   protected final Map<NodeId, Construct> yamlClassConstructors = new EnumMap<NodeId, Construct>(NodeId.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   protected final Map<Tag, Construct> yamlConstructors = new HashMap<Tag, Construct>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   protected final Map<String, Construct> yamlMultiConstructors = new HashMap<String, Construct>();
/*     */   
/*     */   protected Composer composer;
/*     */   
/*     */   final Map<Node, Object> constructedObjects;
/*     */   
/*     */   private final Set<Node> recursiveObjects;
/*     */   private final ArrayList<RecursiveTuple<Map<Object, Object>, RecursiveTuple<Object, Object>>> maps2fill;
/*     */   private final ArrayList<RecursiveTuple<Set<Object>, Object>> sets2fill;
/*     */   protected Tag rootTag;
/*     */   private PropertyUtils propertyUtils;
/*     */   private boolean explicitPropertyUtils;
/*     */   private boolean allowDuplicateKeys = true;
/*     */   protected final Map<Class<? extends Object>, TypeDescription> typeDefinitions;
/*     */   protected final Map<Tag, Class<? extends Object>> typeTags;
/*     */   
/*     */   public BaseConstructor() {
/*  85 */     this.constructedObjects = new HashMap<Node, Object>();
/*  86 */     this.recursiveObjects = new HashSet<Node>();
/*  87 */     this.maps2fill = new ArrayList<RecursiveTuple<Map<Object, Object>, RecursiveTuple<Object, Object>>>();
/*  88 */     this.sets2fill = new ArrayList<RecursiveTuple<Set<Object>, Object>>();
/*  89 */     this.typeDefinitions = new HashMap<Class<? extends Object>, TypeDescription>();
/*  90 */     this.typeTags = new HashMap<Tag, Class<? extends Object>>();
/*     */     
/*  92 */     this.rootTag = null;
/*  93 */     this.explicitPropertyUtils = false;
/*     */     
/*  95 */     this.typeDefinitions.put(SortedMap.class, new TypeDescription(SortedMap.class, Tag.OMAP, TreeMap.class));
/*     */     
/*  97 */     this.typeDefinitions.put(SortedSet.class, new TypeDescription(SortedSet.class, Tag.SET, TreeSet.class));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setComposer(Composer composer) {
/* 102 */     this.composer = composer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean checkData() {
/* 112 */     return this.composer.checkNode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getData() {
/* 122 */     this.composer.checkNode();
/* 123 */     Node node = this.composer.getNode();
/* 124 */     if (this.rootTag != null) {
/* 125 */       node.setTag(this.rootTag);
/*     */     }
/* 127 */     return constructDocument(node);
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
/*     */   public Object getSingleData(Class<?> type) {
/* 140 */     Node node = this.composer.getSingleNode();
/* 141 */     if (node != null && !Tag.NULL.equals(node.getTag())) {
/* 142 */       if (Object.class != type) {
/* 143 */         node.setTag(new Tag(type));
/* 144 */       } else if (this.rootTag != null) {
/* 145 */         node.setTag(this.rootTag);
/*     */       } 
/* 147 */       return constructDocument(node);
/*     */     } 
/* 149 */     return null;
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
/*     */   protected final Object constructDocument(Node node) {
/* 161 */     Object data = constructObject(node);
/* 162 */     fillRecursive();
/* 163 */     this.constructedObjects.clear();
/* 164 */     this.recursiveObjects.clear();
/* 165 */     return data;
/*     */   }
/*     */   
/*     */   private void fillRecursive() {
/* 169 */     if (!this.maps2fill.isEmpty()) {
/* 170 */       for (RecursiveTuple<Map<Object, Object>, RecursiveTuple<Object, Object>> entry : this.maps2fill) {
/* 171 */         RecursiveTuple<Object, Object> key_value = entry._2();
/* 172 */         ((Map)entry._1()).put(key_value._1(), key_value._2());
/*     */       } 
/* 174 */       this.maps2fill.clear();
/*     */     } 
/* 176 */     if (!this.sets2fill.isEmpty()) {
/* 177 */       for (RecursiveTuple<Set<Object>, Object> value : this.sets2fill) {
/* 178 */         ((Set)value._1()).add(value._2());
/*     */       }
/* 180 */       this.sets2fill.clear();
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
/*     */   protected Object constructObject(Node node) {
/* 193 */     if (this.constructedObjects.containsKey(node)) {
/* 194 */       return this.constructedObjects.get(node);
/*     */     }
/* 196 */     return constructObjectNoCheck(node);
/*     */   }
/*     */   
/*     */   protected Object constructObjectNoCheck(Node node) {
/* 200 */     if (this.recursiveObjects.contains(node)) {
/* 201 */       throw new ConstructorException(null, null, "found unconstructable recursive node", node
/* 202 */           .getStartMark());
/*     */     }
/* 204 */     this.recursiveObjects.add(node);
/* 205 */     Construct constructor = getConstructor(node);
/*     */     
/* 207 */     Object data = this.constructedObjects.containsKey(node) ? this.constructedObjects.get(node) : constructor.construct(node);
/*     */     
/* 209 */     finalizeConstruction(node, data);
/* 210 */     this.constructedObjects.put(node, data);
/* 211 */     this.recursiveObjects.remove(node);
/* 212 */     if (node.isTwoStepsConstruction()) {
/* 213 */       constructor.construct2ndStep(node, data);
/*     */     }
/* 215 */     return data;
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
/*     */   protected Construct getConstructor(Node node) {
/* 228 */     if (node.useClassConstructor()) {
/* 229 */       return this.yamlClassConstructors.get(node.getNodeId());
/*     */     }
/* 231 */     Construct constructor = this.yamlConstructors.get(node.getTag());
/* 232 */     if (constructor == null) {
/* 233 */       for (String prefix : this.yamlMultiConstructors.keySet()) {
/* 234 */         if (node.getTag().startsWith(prefix)) {
/* 235 */           return this.yamlMultiConstructors.get(prefix);
/*     */         }
/*     */       } 
/* 238 */       return this.yamlConstructors.get(null);
/*     */     } 
/* 240 */     return constructor;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object constructScalar(ScalarNode node) {
/* 245 */     return node.getValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<Object> createDefaultList(int initSize) {
/* 250 */     return new ArrayList(initSize);
/*     */   }
/*     */   
/*     */   protected Set<Object> createDefaultSet(int initSize) {
/* 254 */     return new LinkedHashSet(initSize);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Map<Object, Object> createDefaultMap() {
/* 259 */     return new LinkedHashMap<Object, Object>();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Set<Object> createDefaultSet() {
/* 264 */     return new LinkedHashSet();
/*     */   }
/*     */   
/*     */   protected Object createArray(Class<?> type, int size) {
/* 268 */     return Array.newInstance(type.getComponentType(), size);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object finalizeConstruction(Node node, Object data) {
/* 274 */     Class<? extends Object> type = node.getType();
/* 275 */     if (this.typeDefinitions.containsKey(type)) {
/* 276 */       return ((TypeDescription)this.typeDefinitions.get(type)).finalizeConstruction(data);
/*     */     }
/* 278 */     return data;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object newInstance(Node node) {
/*     */     try {
/* 284 */       return newInstance(Object.class, node);
/* 285 */     } catch (InstantiationException e) {
/* 286 */       throw new YAMLException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected final Object newInstance(Class<?> ancestor, Node node) throws InstantiationException {
/* 291 */     return newInstance(ancestor, node, true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object newInstance(Class<?> ancestor, Node node, boolean tryDefault) throws InstantiationException {
/* 296 */     Class<? extends Object> type = node.getType();
/* 297 */     if (this.typeDefinitions.containsKey(type)) {
/* 298 */       TypeDescription td = this.typeDefinitions.get(type);
/* 299 */       Object instance = td.newInstance(node);
/* 300 */       if (instance != null) {
/* 301 */         return instance;
/*     */       }
/*     */     } 
/* 304 */     if (tryDefault)
/*     */     {
/*     */ 
/*     */ 
/*     */       
/* 309 */       if (ancestor.isAssignableFrom(type) && !Modifier.isAbstract(type.getModifiers())) {
/*     */         try {
/* 311 */           Constructor<?> c = type.getDeclaredConstructor(new Class[0]);
/* 312 */           c.setAccessible(true);
/* 313 */           return c.newInstance(new Object[0]);
/* 314 */         } catch (NoSuchMethodException e) {
/* 315 */           throw new InstantiationException("NoSuchMethodException:" + e
/* 316 */               .getLocalizedMessage());
/* 317 */         } catch (Exception e) {
/* 318 */           throw new YAMLException(e);
/*     */         } 
/*     */       }
/*     */     }
/* 322 */     throw new InstantiationException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Set<Object> newSet(CollectionNode<?> node) {
/*     */     try {
/* 328 */       return (Set<Object>)newInstance(Set.class, (Node)node);
/* 329 */     } catch (InstantiationException e) {
/* 330 */       return createDefaultSet(node.getValue().size());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<Object> newList(SequenceNode node) {
/*     */     try {
/* 337 */       return (List<Object>)newInstance(List.class, (Node)node);
/* 338 */     } catch (InstantiationException e) {
/* 339 */       return createDefaultList(node.getValue().size());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected Map<Object, Object> newMap(MappingNode node) {
/*     */     try {
/* 346 */       return (Map<Object, Object>)newInstance(Map.class, (Node)node);
/* 347 */     } catch (InstantiationException e) {
/* 348 */       return createDefaultMap();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<? extends Object> constructSequence(SequenceNode node) {
/* 356 */     List<Object> result = newList(node);
/* 357 */     constructSequenceStep2(node, result);
/* 358 */     return result;
/*     */   }
/*     */   
/*     */   protected Set<? extends Object> constructSet(SequenceNode node) {
/* 362 */     Set<Object> result = newSet((CollectionNode<?>)node);
/* 363 */     constructSequenceStep2(node, result);
/* 364 */     return result;
/*     */   }
/*     */   
/*     */   protected Object constructArray(SequenceNode node) {
/* 368 */     return constructArrayStep2(node, createArray(node.getType(), node.getValue().size()));
/*     */   }
/*     */   
/*     */   protected void constructSequenceStep2(SequenceNode node, Collection<Object> collection) {
/* 372 */     for (Node child : node.getValue()) {
/* 373 */       collection.add(constructObject(child));
/*     */     }
/*     */   }
/*     */   
/*     */   protected Object constructArrayStep2(SequenceNode node, Object array) {
/* 378 */     Class<?> componentType = node.getType().getComponentType();
/*     */     
/* 380 */     int index = 0;
/* 381 */     for (Node child : node.getValue()) {
/*     */       
/* 383 */       if (child.getType() == Object.class) {
/* 384 */         child.setType(componentType);
/*     */       }
/*     */       
/* 387 */       Object value = constructObject(child);
/*     */       
/* 389 */       if (componentType.isPrimitive()) {
/*     */         
/* 391 */         if (value == null) {
/* 392 */           throw new NullPointerException("Unable to construct element value for " + child);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 397 */         if (byte.class.equals(componentType)) {
/* 398 */           Array.setByte(array, index, ((Number)value).byteValue());
/*     */         }
/* 400 */         else if (short.class.equals(componentType)) {
/* 401 */           Array.setShort(array, index, ((Number)value).shortValue());
/*     */         }
/* 403 */         else if (int.class.equals(componentType)) {
/* 404 */           Array.setInt(array, index, ((Number)value).intValue());
/*     */         }
/* 406 */         else if (long.class.equals(componentType)) {
/* 407 */           Array.setLong(array, index, ((Number)value).longValue());
/*     */         }
/* 409 */         else if (float.class.equals(componentType)) {
/* 410 */           Array.setFloat(array, index, ((Number)value).floatValue());
/*     */         }
/* 412 */         else if (double.class.equals(componentType)) {
/* 413 */           Array.setDouble(array, index, ((Number)value).doubleValue());
/*     */         }
/* 415 */         else if (char.class.equals(componentType)) {
/* 416 */           Array.setChar(array, index, ((Character)value).charValue());
/*     */         }
/* 418 */         else if (boolean.class.equals(componentType)) {
/* 419 */           Array.setBoolean(array, index, ((Boolean)value).booleanValue());
/*     */         } else {
/*     */           
/* 422 */           throw new YAMLException("unexpected primitive type");
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 427 */         Array.set(array, index, value);
/*     */       } 
/*     */       
/* 430 */       index++;
/*     */     } 
/* 432 */     return array;
/*     */   }
/*     */   
/*     */   protected Set<Object> constructSet(MappingNode node) {
/* 436 */     Set<Object> set = newSet((CollectionNode<?>)node);
/* 437 */     constructSet2ndStep(node, set);
/* 438 */     return set;
/*     */   }
/*     */   
/*     */   protected Map<Object, Object> constructMapping(MappingNode node) {
/* 442 */     Map<Object, Object> mapping = newMap(node);
/* 443 */     constructMapping2ndStep(node, mapping);
/* 444 */     return mapping;
/*     */   }
/*     */   
/*     */   protected void constructMapping2ndStep(MappingNode node, Map<Object, Object> mapping) {
/* 448 */     List<NodeTuple> nodeValue = node.getValue();
/* 449 */     for (NodeTuple tuple : nodeValue) {
/* 450 */       Node keyNode = tuple.getKeyNode();
/* 451 */       Node valueNode = tuple.getValueNode();
/* 452 */       Object key = constructObject(keyNode);
/* 453 */       if (key != null) {
/*     */         try {
/* 455 */           key.hashCode();
/* 456 */         } catch (Exception e) {
/* 457 */           throw new ConstructorException("while constructing a mapping", node
/* 458 */               .getStartMark(), "found unacceptable key " + key, tuple
/* 459 */               .getKeyNode().getStartMark(), e);
/*     */         } 
/*     */       }
/* 462 */       Object value = constructObject(valueNode);
/* 463 */       if (keyNode.isTwoStepsConstruction()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 470 */         this.maps2fill.add(0, new RecursiveTuple<Map<Object, Object>, RecursiveTuple<Object, Object>>(mapping, new RecursiveTuple<Object, Object>(key, value)));
/*     */         
/*     */         continue;
/*     */       } 
/* 474 */       mapping.put(key, value);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void constructSet2ndStep(MappingNode node, Set<Object> set) {
/* 480 */     List<NodeTuple> nodeValue = node.getValue();
/* 481 */     for (NodeTuple tuple : nodeValue) {
/* 482 */       Node keyNode = tuple.getKeyNode();
/* 483 */       Object key = constructObject(keyNode);
/* 484 */       if (key != null) {
/*     */         try {
/* 486 */           key.hashCode();
/* 487 */         } catch (Exception e) {
/* 488 */           throw new ConstructorException("while constructing a Set", node.getStartMark(), "found unacceptable key " + key, tuple
/* 489 */               .getKeyNode().getStartMark(), e);
/*     */         } 
/*     */       }
/* 492 */       if (keyNode.isTwoStepsConstruction()) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 499 */         this.sets2fill.add(0, new RecursiveTuple<Set<Object>, Object>(set, key)); continue;
/*     */       } 
/* 501 */       set.add(key);
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
/*     */   public void setPropertyUtils(PropertyUtils propertyUtils) {
/* 521 */     this.propertyUtils = propertyUtils;
/* 522 */     this.explicitPropertyUtils = true;
/* 523 */     Collection<TypeDescription> tds = this.typeDefinitions.values();
/* 524 */     for (TypeDescription typeDescription : tds) {
/* 525 */       typeDescription.setPropertyUtils(propertyUtils);
/*     */     }
/*     */   }
/*     */   
/*     */   public final PropertyUtils getPropertyUtils() {
/* 530 */     if (this.propertyUtils == null) {
/* 531 */       this.propertyUtils = new PropertyUtils();
/*     */     }
/* 533 */     return this.propertyUtils;
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
/*     */   public TypeDescription addTypeDescription(TypeDescription definition) {
/* 547 */     if (definition == null) {
/* 548 */       throw new NullPointerException("TypeDescription is required.");
/*     */     }
/* 550 */     Tag tag = definition.getTag();
/* 551 */     this.typeTags.put(tag, definition.getType());
/* 552 */     definition.setPropertyUtils(getPropertyUtils());
/* 553 */     return this.typeDefinitions.put(definition.getType(), definition);
/*     */   }
/*     */   
/*     */   private static class RecursiveTuple<T, K> {
/*     */     private final T _1;
/*     */     private final K _2;
/*     */     
/*     */     public RecursiveTuple(T _1, K _2) {
/* 561 */       this._1 = _1;
/* 562 */       this._2 = _2;
/*     */     }
/*     */     
/*     */     public K _2() {
/* 566 */       return this._2;
/*     */     }
/*     */     
/*     */     public T _1() {
/* 570 */       return this._1;
/*     */     }
/*     */   }
/*     */   
/*     */   public final boolean isExplicitPropertyUtils() {
/* 575 */     return this.explicitPropertyUtils;
/*     */   }
/*     */   
/*     */   public boolean isAllowDuplicateKeys() {
/* 579 */     return this.allowDuplicateKeys;
/*     */   }
/*     */   
/*     */   public void setAllowDuplicateKeys(boolean allowDuplicateKeys) {
/* 583 */     this.allowDuplicateKeys = allowDuplicateKeys;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\yaml\snakeyaml\constructor\BaseConstructor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */