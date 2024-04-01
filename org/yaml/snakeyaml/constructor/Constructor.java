/*     */ package org.yaml.snakeyaml.constructor;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import org.yaml.snakeyaml.TypeDescription;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.introspector.Property;
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
/*     */ public class Constructor
/*     */   extends SafeConstructor
/*     */ {
/*     */   public Constructor() {
/*  46 */     this(Object.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Constructor(Class<? extends Object> theRoot) {
/*  56 */     this(new TypeDescription(checkRoot(theRoot)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Class<? extends Object> checkRoot(Class<? extends Object> theRoot) {
/*  63 */     if (theRoot == null) {
/*  64 */       throw new NullPointerException("Root class must be provided.");
/*     */     }
/*  66 */     return theRoot;
/*     */   }
/*     */   
/*     */   public Constructor(TypeDescription theRoot) {
/*  70 */     this(theRoot, (Collection<TypeDescription>)null);
/*     */   }
/*     */   
/*     */   public Constructor(TypeDescription theRoot, Collection<TypeDescription> moreTDs) {
/*  74 */     if (theRoot == null) {
/*  75 */       throw new NullPointerException("Root type must be provided.");
/*     */     }
/*  77 */     this.yamlConstructors.put(null, new ConstructYamlObject());
/*  78 */     if (!Object.class.equals(theRoot.getType())) {
/*  79 */       this.rootTag = new Tag(theRoot.getType());
/*     */     }
/*  81 */     this.yamlClassConstructors.put(NodeId.scalar, new ConstructScalar());
/*  82 */     this.yamlClassConstructors.put(NodeId.mapping, new ConstructMapping());
/*  83 */     this.yamlClassConstructors.put(NodeId.sequence, new ConstructSequence());
/*  84 */     addTypeDescription(theRoot);
/*  85 */     if (moreTDs != null) {
/*  86 */       for (TypeDescription td : moreTDs) {
/*  87 */         addTypeDescription(td);
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
/*     */   public Constructor(String theRoot) throws ClassNotFoundException {
/* 102 */     this((Class)Class.forName(check(theRoot)));
/*     */   }
/*     */   
/*     */   private static final String check(String s) {
/* 106 */     if (s == null) {
/* 107 */       throw new NullPointerException("Root type must be provided.");
/*     */     }
/* 109 */     if (s.trim().length() == 0) {
/* 110 */       throw new YAMLException("Root type must be provided.");
/*     */     }
/* 112 */     return s;
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
/*     */   protected class ConstructMapping
/*     */     implements Construct
/*     */   {
/*     */     public Object construct(Node node) {
/* 131 */       MappingNode mnode = (MappingNode)node;
/* 132 */       if (Map.class.isAssignableFrom(node.getType())) {
/* 133 */         if (node.isTwoStepsConstruction()) {
/* 134 */           return Constructor.this.newMap(mnode);
/*     */         }
/* 136 */         return Constructor.this.constructMapping(mnode);
/*     */       } 
/* 138 */       if (Collection.class.isAssignableFrom(node.getType())) {
/* 139 */         if (node.isTwoStepsConstruction()) {
/* 140 */           return Constructor.this.newSet((CollectionNode<?>)mnode);
/*     */         }
/* 142 */         return Constructor.this.constructSet(mnode);
/*     */       } 
/*     */       
/* 145 */       Object obj = Constructor.this.newInstance((Node)mnode);
/* 146 */       if (node.isTwoStepsConstruction()) {
/* 147 */         return obj;
/*     */       }
/* 149 */       return constructJavaBean2ndStep(mnode, obj);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void construct2ndStep(Node node, Object object) {
/* 156 */       if (Map.class.isAssignableFrom(node.getType())) {
/* 157 */         Constructor.this.constructMapping2ndStep((MappingNode)node, (Map<Object, Object>)object);
/* 158 */       } else if (Set.class.isAssignableFrom(node.getType())) {
/* 159 */         Constructor.this.constructSet2ndStep((MappingNode)node, (Set<Object>)object);
/*     */       } else {
/* 161 */         constructJavaBean2ndStep((MappingNode)node, object);
/*     */       } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected Object constructJavaBean2ndStep(MappingNode node, Object object) {
/* 191 */       Constructor.this.flattenMapping(node);
/* 192 */       Class<? extends Object> beanType = node.getType();
/* 193 */       List<NodeTuple> nodeValue = node.getValue();
/* 194 */       for (NodeTuple tuple : nodeValue) {
/*     */         ScalarNode keyNode;
/* 196 */         if (tuple.getKeyNode() instanceof ScalarNode) {
/*     */           
/* 198 */           keyNode = (ScalarNode)tuple.getKeyNode();
/*     */         } else {
/* 200 */           throw new YAMLException("Keys must be scalars but found: " + tuple
/* 201 */               .getKeyNode());
/*     */         } 
/* 203 */         Node valueNode = tuple.getValueNode();
/*     */         
/* 205 */         keyNode.setType(String.class);
/* 206 */         String key = (String)Constructor.this.constructObject((Node)keyNode);
/*     */         try {
/* 208 */           TypeDescription memberDescription = Constructor.this.typeDefinitions.get(beanType);
/*     */           
/* 210 */           Property property = (memberDescription == null) ? getProperty(beanType, key) : memberDescription.getProperty(key);
/*     */           
/* 212 */           if (!property.isWritable()) {
/* 213 */             throw new YAMLException("No writable property '" + key + "' on class: " + beanType
/* 214 */                 .getName());
/*     */           }
/*     */           
/* 217 */           valueNode.setType(property.getType());
/*     */           
/* 219 */           boolean typeDetected = (memberDescription != null) ? memberDescription.setupPropertyType(key, valueNode) : false;
/* 220 */           if (!typeDetected && valueNode.getNodeId() != NodeId.scalar) {
/*     */             
/* 222 */             Class<?>[] arguments = property.getActualTypeArguments();
/* 223 */             if (arguments != null && arguments.length > 0)
/*     */             {
/*     */               
/* 226 */               if (valueNode.getNodeId() == NodeId.sequence) {
/* 227 */                 Class<?> t = arguments[0];
/* 228 */                 SequenceNode snode = (SequenceNode)valueNode;
/* 229 */                 snode.setListType(t);
/* 230 */               } else if (Set.class.isAssignableFrom(valueNode.getType())) {
/* 231 */                 Class<?> t = arguments[0];
/* 232 */                 MappingNode mnode = (MappingNode)valueNode;
/* 233 */                 mnode.setOnlyKeyType(t);
/* 234 */                 mnode.setUseClassConstructor(Boolean.valueOf(true));
/* 235 */               } else if (Map.class.isAssignableFrom(valueNode.getType())) {
/* 236 */                 Class<?> ketType = arguments[0];
/* 237 */                 Class<?> valueType = arguments[1];
/* 238 */                 MappingNode mnode = (MappingNode)valueNode;
/* 239 */                 mnode.setTypes(ketType, valueType);
/* 240 */                 mnode.setUseClassConstructor(Boolean.valueOf(true));
/*     */               } 
/*     */             }
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/* 247 */           Object value = (memberDescription != null) ? newInstance(memberDescription, key, valueNode) : Constructor.this.constructObject(valueNode);
/*     */ 
/*     */           
/* 250 */           if ((property.getType() == float.class || property.getType() == Float.class) && 
/* 251 */             value instanceof Double) {
/* 252 */             value = Float.valueOf(((Double)value).floatValue());
/*     */           }
/*     */ 
/*     */           
/* 256 */           if (property.getType() == String.class && Tag.BINARY.equals(valueNode.getTag()) && value instanceof byte[])
/*     */           {
/* 258 */             value = new String((byte[])value);
/*     */           }
/*     */           
/* 261 */           if (memberDescription == null || 
/* 262 */             !memberDescription.setProperty(object, key, value)) {
/* 263 */             property.set(object, value);
/*     */           }
/* 265 */         } catch (Exception e) {
/* 266 */           throw new ConstructorException("Cannot create property=" + key + " for JavaBean=" + object, node
/*     */               
/* 268 */               .getStartMark(), e.getMessage(), valueNode.getStartMark(), e);
/*     */         } 
/*     */       } 
/* 271 */       return object;
/*     */     }
/*     */ 
/*     */     
/*     */     private Object newInstance(TypeDescription memberDescription, String propertyName, Node node) {
/* 276 */       Object newInstance = memberDescription.newInstance(propertyName, node);
/* 277 */       if (newInstance != null) {
/* 278 */         Constructor.this.constructedObjects.put(node, newInstance);
/* 279 */         return Constructor.this.constructObjectNoCheck(node);
/*     */       } 
/* 281 */       return Constructor.this.constructObject(node);
/*     */     }
/*     */     
/*     */     protected Property getProperty(Class<? extends Object> type, String name) {
/* 285 */       return Constructor.this.getPropertyUtils().getProperty(type, name);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected class ConstructYamlObject
/*     */     implements Construct
/*     */   {
/*     */     private Construct getConstructor(Node node) {
/* 298 */       Class<?> cl = Constructor.this.getClassForNode(node);
/* 299 */       node.setType(cl);
/*     */       
/* 301 */       Construct constructor = Constructor.this.yamlClassConstructors.get(node.getNodeId());
/* 302 */       return constructor;
/*     */     }
/*     */     
/*     */     public Object construct(Node node) {
/* 306 */       Object result = null;
/*     */       try {
/* 308 */         result = getConstructor(node).construct(node);
/* 309 */       } catch (ConstructorException e) {
/* 310 */         throw e;
/* 311 */       } catch (Exception e) {
/* 312 */         throw new ConstructorException(null, null, "Can't construct a java object for " + node
/* 313 */             .getTag() + "; exception=" + e.getMessage(), node.getStartMark(), e);
/*     */       } 
/* 315 */       return result;
/*     */     }
/*     */     
/*     */     public void construct2ndStep(Node node, Object object) {
/*     */       try {
/* 320 */         getConstructor(node).construct2ndStep(node, object);
/* 321 */       } catch (Exception e) {
/* 322 */         throw new ConstructorException(null, null, "Can't construct a second step for a java object for " + node
/*     */             
/* 324 */             .getTag() + "; exception=" + e.getMessage(), node
/* 325 */             .getStartMark(), e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected class ConstructScalar
/*     */     extends AbstractConstruct
/*     */   {
/*     */     public Object construct(Node nnode) {
/* 336 */       ScalarNode node = (ScalarNode)nnode;
/* 337 */       Class<?> type = node.getType();
/*     */       
/*     */       try {
/* 340 */         return Constructor.this.newInstance(type, (Node)node, false);
/* 341 */       } catch (InstantiationException null) {
/*     */         Object result;
/*     */ 
/*     */         
/* 345 */         if (type.isPrimitive() || type == String.class || Number.class.isAssignableFrom(type) || type == Boolean.class || Date.class
/* 346 */           .isAssignableFrom(type) || type == Character.class || type == BigInteger.class || type == BigDecimal.class || Enum.class
/*     */           
/* 348 */           .isAssignableFrom(type) || Tag.BINARY
/* 349 */           .equals(node.getTag()) || Calendar.class.isAssignableFrom(type) || type == UUID.class) {
/*     */ 
/*     */           
/* 352 */           Object object = constructStandardJavaInstance(type, node);
/*     */         } else {
/*     */           Object argument;
/*     */           
/* 356 */           Constructor[] arrayOfConstructor = (Constructor[])type.getDeclaredConstructors();
/* 357 */           int oneArgCount = 0;
/* 358 */           Constructor<?> javaConstructor = null;
/* 359 */           for (Constructor<?> c : arrayOfConstructor) {
/* 360 */             if ((c.getParameterTypes()).length == 1) {
/* 361 */               oneArgCount++;
/* 362 */               javaConstructor = c;
/*     */             } 
/*     */           } 
/*     */           
/* 366 */           if (javaConstructor == null)
/*     */             try {
/* 368 */               return Constructor.this.newInstance(type, (Node)node, false);
/* 369 */             } catch (InstantiationException ie) {
/* 370 */               throw new YAMLException("No single argument constructor found for " + type + " : " + ie
/* 371 */                   .getMessage());
/*     */             }  
/* 373 */           if (oneArgCount == 1) {
/* 374 */             argument = constructStandardJavaInstance(javaConstructor.getParameterTypes()[0], node);
/*     */ 
/*     */ 
/*     */           
/*     */           }
/*     */           else {
/*     */ 
/*     */ 
/*     */             
/* 383 */             argument = Constructor.this.constructScalar(node);
/*     */             try {
/* 385 */               javaConstructor = type.getDeclaredConstructor(new Class[] { String.class });
/* 386 */             } catch (Exception e) {
/* 387 */               throw new YAMLException("Can't construct a java object for scalar " + node
/* 388 */                   .getTag() + "; No String constructor found. Exception=" + e
/* 389 */                   .getMessage(), e);
/*     */             } 
/*     */           } 
/*     */           try {
/* 393 */             javaConstructor.setAccessible(true);
/* 394 */             result = javaConstructor.newInstance(new Object[] { argument });
/* 395 */           } catch (Exception e) {
/* 396 */             throw new ConstructorException(null, null, "Can't construct a java object for scalar " + node
/* 397 */                 .getTag() + "; exception=" + e
/* 398 */                 .getMessage(), node
/* 399 */                 .getStartMark(), e);
/*     */           } 
/*     */         } 
/* 402 */         return result;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private Object constructStandardJavaInstance(Class<String> type, ScalarNode node) {
/*     */       Object result;
/* 409 */       if (type == String.class) {
/* 410 */         Construct stringConstructor = Constructor.this.yamlConstructors.get(Tag.STR);
/* 411 */         result = stringConstructor.construct((Node)node);
/* 412 */       } else if (type == Boolean.class || type == boolean.class) {
/* 413 */         Construct boolConstructor = Constructor.this.yamlConstructors.get(Tag.BOOL);
/* 414 */         result = boolConstructor.construct((Node)node);
/* 415 */       } else if (type == Character.class || type == char.class) {
/* 416 */         Construct charConstructor = Constructor.this.yamlConstructors.get(Tag.STR);
/* 417 */         String ch = (String)charConstructor.construct((Node)node);
/* 418 */         if (ch.length() == 0)
/* 419 */         { result = null; }
/* 420 */         else { if (ch.length() != 1) {
/* 421 */             throw new YAMLException("Invalid node Character: '" + ch + "'; length: " + ch
/* 422 */                 .length());
/*     */           }
/* 424 */           result = Character.valueOf(ch.charAt(0)); }
/*     */       
/* 426 */       } else if (Date.class.isAssignableFrom(type)) {
/* 427 */         Construct dateConstructor = Constructor.this.yamlConstructors.get(Tag.TIMESTAMP);
/* 428 */         Date date = (Date)dateConstructor.construct((Node)node);
/* 429 */         if (type == Date.class) {
/* 430 */           result = date;
/*     */         } else {
/*     */           try {
/* 433 */             Constructor<?> constr = type.getConstructor(new Class[] { long.class });
/* 434 */             result = constr.newInstance(new Object[] { Long.valueOf(date.getTime()) });
/* 435 */           } catch (RuntimeException e) {
/* 436 */             throw e;
/* 437 */           } catch (Exception e) {
/* 438 */             throw new YAMLException("Cannot construct: '" + type + "'");
/*     */           } 
/*     */         } 
/* 441 */       } else if (type == Float.class || type == Double.class || type == float.class || type == double.class || type == BigDecimal.class) {
/*     */         
/* 443 */         if (type == BigDecimal.class) {
/* 444 */           result = new BigDecimal(node.getValue());
/*     */         } else {
/* 446 */           Construct doubleConstructor = Constructor.this.yamlConstructors.get(Tag.FLOAT);
/* 447 */           result = doubleConstructor.construct((Node)node);
/* 448 */           if (type == Float.class || type == float.class) {
/* 449 */             result = new Float(((Double)result).doubleValue());
/*     */           }
/*     */         } 
/* 452 */       } else if (type == Byte.class || type == Short.class || type == Integer.class || type == Long.class || type == BigInteger.class || type == byte.class || type == short.class || type == int.class || type == long.class) {
/*     */ 
/*     */         
/* 455 */         Construct intConstructor = Constructor.this.yamlConstructors.get(Tag.INT);
/* 456 */         result = intConstructor.construct((Node)node);
/* 457 */         if (type == Byte.class || type == byte.class) {
/* 458 */           result = Byte.valueOf(result.toString());
/* 459 */         } else if (type == Short.class || type == short.class) {
/* 460 */           result = Short.valueOf(result.toString());
/* 461 */         } else if (type == Integer.class || type == int.class) {
/* 462 */           result = Integer.valueOf(Integer.parseInt(result.toString()));
/* 463 */         } else if (type == Long.class || type == long.class) {
/* 464 */           result = Long.valueOf(result.toString());
/*     */         } else {
/*     */           
/* 467 */           result = new BigInteger(result.toString());
/*     */         } 
/* 469 */       } else if (Enum.class.isAssignableFrom(type)) {
/* 470 */         String enumValueName = node.getValue();
/*     */         try {
/* 472 */           result = Enum.valueOf(type, enumValueName);
/* 473 */         } catch (Exception ex) {
/* 474 */           throw new YAMLException("Unable to find enum value '" + enumValueName + "' for enum class: " + type
/* 475 */               .getName());
/*     */         } 
/* 477 */       } else if (Calendar.class.isAssignableFrom(type)) {
/* 478 */         SafeConstructor.ConstructYamlTimestamp contr = new SafeConstructor.ConstructYamlTimestamp();
/* 479 */         contr.construct((Node)node);
/* 480 */         result = contr.getCalendar();
/* 481 */       } else if (Number.class.isAssignableFrom(type)) {
/*     */         
/* 483 */         SafeConstructor.ConstructYamlFloat contr = new SafeConstructor.ConstructYamlFloat(Constructor.this);
/* 484 */         result = contr.construct((Node)node);
/* 485 */       } else if (UUID.class == type) {
/* 486 */         result = UUID.fromString(node.getValue());
/*     */       }
/* 488 */       else if (Constructor.this.yamlConstructors.containsKey(node.getTag())) {
/* 489 */         result = ((Construct)Constructor.this.yamlConstructors.get(node.getTag())).construct((Node)node);
/*     */       } else {
/* 491 */         throw new YAMLException("Unsupported class: " + type);
/*     */       } 
/*     */       
/* 494 */       return result;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected class ConstructSequence
/*     */     implements Construct
/*     */   {
/*     */     public Object construct(Node node) {
/* 505 */       SequenceNode snode = (SequenceNode)node;
/* 506 */       if (Set.class.isAssignableFrom(node.getType())) {
/* 507 */         if (node.isTwoStepsConstruction()) {
/* 508 */           throw new YAMLException("Set cannot be recursive.");
/*     */         }
/* 510 */         return Constructor.this.constructSet(snode);
/*     */       } 
/* 512 */       if (Collection.class.isAssignableFrom(node.getType())) {
/* 513 */         if (node.isTwoStepsConstruction()) {
/* 514 */           return Constructor.this.newList(snode);
/*     */         }
/* 516 */         return Constructor.this.constructSequence(snode);
/*     */       } 
/* 518 */       if (node.getType().isArray()) {
/* 519 */         if (node.isTwoStepsConstruction()) {
/* 520 */           return Constructor.this.createArray(node.getType(), snode.getValue().size());
/*     */         }
/* 522 */         return Constructor.this.constructArray(snode);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 527 */       List<Constructor<?>> possibleConstructors = new ArrayList<Constructor<?>>(snode.getValue().size());
/* 528 */       for (Constructor<?> constructor : node.getType()
/* 529 */         .getDeclaredConstructors()) {
/* 530 */         if (snode.getValue().size() == (constructor.getParameterTypes()).length) {
/* 531 */           possibleConstructors.add(constructor);
/*     */         }
/*     */       } 
/* 534 */       if (!possibleConstructors.isEmpty()) {
/* 535 */         if (possibleConstructors.size() == 1) {
/* 536 */           Object[] arrayOfObject = new Object[snode.getValue().size()];
/* 537 */           Constructor<?> c = possibleConstructors.get(0);
/* 538 */           int i = 0;
/* 539 */           for (Node argumentNode : snode.getValue()) {
/* 540 */             Class<?> type = c.getParameterTypes()[i];
/*     */             
/* 542 */             argumentNode.setType(type);
/* 543 */             arrayOfObject[i++] = Constructor.this.constructObject(argumentNode);
/*     */           } 
/*     */           
/*     */           try {
/* 547 */             c.setAccessible(true);
/* 548 */             return c.newInstance(arrayOfObject);
/* 549 */           } catch (Exception e) {
/* 550 */             throw new YAMLException(e);
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 555 */         List<Object> argumentList = (List)Constructor.this.constructSequence(snode);
/* 556 */         Class<?>[] parameterTypes = new Class[argumentList.size()];
/* 557 */         int index = 0;
/* 558 */         for (Object parameter : argumentList) {
/* 559 */           parameterTypes[index] = parameter.getClass();
/* 560 */           index++;
/*     */         } 
/*     */         
/* 563 */         for (Constructor<?> c : possibleConstructors) {
/* 564 */           Class<?>[] argTypes = c.getParameterTypes();
/* 565 */           boolean foundConstructor = true;
/* 566 */           for (int i = 0; i < argTypes.length; i++) {
/* 567 */             if (!wrapIfPrimitive(argTypes[i]).isAssignableFrom(parameterTypes[i])) {
/* 568 */               foundConstructor = false;
/*     */               break;
/*     */             } 
/*     */           } 
/* 572 */           if (foundConstructor) {
/*     */             try {
/* 574 */               c.setAccessible(true);
/* 575 */               return c.newInstance(argumentList.toArray());
/* 576 */             } catch (Exception e) {
/* 577 */               throw new YAMLException(e);
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/* 582 */       throw new YAMLException("No suitable constructor with " + 
/* 583 */           String.valueOf(snode.getValue().size()) + " arguments found for " + node
/* 584 */           .getType());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private final Class<? extends Object> wrapIfPrimitive(Class<?> clazz) {
/* 590 */       if (!clazz.isPrimitive()) {
/* 591 */         return (Class)clazz;
/*     */       }
/* 593 */       if (clazz == int.class) {
/* 594 */         return (Class)Integer.class;
/*     */       }
/* 596 */       if (clazz == float.class) {
/* 597 */         return (Class)Float.class;
/*     */       }
/* 599 */       if (clazz == double.class) {
/* 600 */         return (Class)Double.class;
/*     */       }
/* 602 */       if (clazz == boolean.class) {
/* 603 */         return (Class)Boolean.class;
/*     */       }
/* 605 */       if (clazz == long.class) {
/* 606 */         return (Class)Long.class;
/*     */       }
/* 608 */       if (clazz == char.class) {
/* 609 */         return (Class)Character.class;
/*     */       }
/* 611 */       if (clazz == short.class) {
/* 612 */         return (Class)Short.class;
/*     */       }
/* 614 */       if (clazz == byte.class) {
/* 615 */         return (Class)Byte.class;
/*     */       }
/* 617 */       throw new YAMLException("Unexpected primitive " + clazz);
/*     */     }
/*     */ 
/*     */     
/*     */     public void construct2ndStep(Node node, Object object) {
/* 622 */       SequenceNode snode = (SequenceNode)node;
/* 623 */       if (List.class.isAssignableFrom(node.getType())) {
/* 624 */         List<Object> list = (List<Object>)object;
/* 625 */         Constructor.this.constructSequenceStep2(snode, list);
/* 626 */       } else if (node.getType().isArray()) {
/* 627 */         Constructor.this.constructArrayStep2(snode, object);
/*     */       } else {
/* 629 */         throw new YAMLException("Immutable objects cannot be recursive.");
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   protected Class<?> getClassForNode(Node node) {
/* 635 */     Class<? extends Object> classForTag = this.typeTags.get(node.getTag());
/* 636 */     if (classForTag == null) {
/* 637 */       Class<?> cl; String name = node.getTag().getClassName();
/*     */       
/*     */       try {
/* 640 */         cl = getClassForName(name);
/* 641 */       } catch (ClassNotFoundException e) {
/* 642 */         throw new YAMLException("Class not found: " + name);
/*     */       } 
/* 644 */       this.typeTags.put(node.getTag(), cl);
/* 645 */       return cl;
/*     */     } 
/* 647 */     return classForTag;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Class<?> getClassForName(String name) throws ClassNotFoundException {
/*     */     try {
/* 653 */       return Class.forName(name, true, Thread.currentThread().getContextClassLoader());
/* 654 */     } catch (ClassNotFoundException e) {
/* 655 */       return Class.forName(name);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\yaml\snakeyaml\constructor\Constructor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */