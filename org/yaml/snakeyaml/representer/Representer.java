/*     */ package org.yaml.snakeyaml.representer;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TimeZone;
/*     */ import org.yaml.snakeyaml.DumperOptions;
/*     */ import org.yaml.snakeyaml.TypeDescription;
/*     */ import org.yaml.snakeyaml.introspector.Property;
/*     */ import org.yaml.snakeyaml.introspector.PropertyUtils;
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
/*     */ public class Representer
/*     */   extends SafeRepresenter
/*     */ {
/*  46 */   protected Map<Class<? extends Object>, TypeDescription> typeDefinitions = Collections.emptyMap();
/*     */   
/*     */   public Representer() {
/*  49 */     this.representers.put(null, new RepresentJavaBean());
/*     */   }
/*     */   
/*     */   public TypeDescription addTypeDescription(TypeDescription td) {
/*  53 */     if (Collections.EMPTY_MAP == this.typeDefinitions) {
/*  54 */       this.typeDefinitions = new HashMap<Class<? extends Object>, TypeDescription>();
/*     */     }
/*  56 */     if (td.getTag() != null) {
/*  57 */       addClassTag(td.getType(), td.getTag());
/*     */     }
/*  59 */     td.setPropertyUtils(getPropertyUtils());
/*  60 */     return this.typeDefinitions.put(td.getType(), td);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPropertyUtils(PropertyUtils propertyUtils) {
/*  65 */     super.setPropertyUtils(propertyUtils);
/*  66 */     Collection<TypeDescription> tds = this.typeDefinitions.values();
/*  67 */     for (TypeDescription typeDescription : tds)
/*  68 */       typeDescription.setPropertyUtils(propertyUtils); 
/*     */   }
/*     */   
/*     */   protected class RepresentJavaBean
/*     */     implements Represent {
/*     */     public Node representData(Object data) {
/*  74 */       return (Node)Representer.this.representJavaBean(Representer.this.getProperties((Class)data.getClass()), data);
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
/*     */   protected MappingNode representJavaBean(Set<Property> properties, Object javaBean) {
/*  93 */     List<NodeTuple> value = new ArrayList<NodeTuple>(properties.size());
/*     */     
/*  95 */     Tag customTag = this.classTags.get(javaBean.getClass());
/*  96 */     Tag tag = (customTag != null) ? customTag : new Tag(javaBean.getClass());
/*     */     
/*  98 */     MappingNode node = new MappingNode(tag, value, null);
/*  99 */     this.representedObjects.put(javaBean, node);
/* 100 */     boolean bestStyle = true;
/* 101 */     for (Property property : properties) {
/* 102 */       Object memberValue = property.get(javaBean);
/*     */       
/* 104 */       Tag customPropertyTag = (memberValue == null) ? null : this.classTags.get(memberValue.getClass());
/* 105 */       NodeTuple tuple = representJavaBeanProperty(javaBean, property, memberValue, customPropertyTag);
/*     */       
/* 107 */       if (tuple == null) {
/*     */         continue;
/*     */       }
/* 110 */       if (((ScalarNode)tuple.getKeyNode()).getStyle() != null) {
/* 111 */         bestStyle = false;
/*     */       }
/* 113 */       Node nodeValue = tuple.getValueNode();
/* 114 */       if (!(nodeValue instanceof ScalarNode) || ((ScalarNode)nodeValue).getStyle() != null) {
/* 115 */         bestStyle = false;
/*     */       }
/* 117 */       value.add(tuple);
/*     */     } 
/* 119 */     if (this.defaultFlowStyle != DumperOptions.FlowStyle.AUTO) {
/* 120 */       node.setFlowStyle(this.defaultFlowStyle.getStyleBoolean());
/*     */     } else {
/* 122 */       node.setFlowStyle(Boolean.valueOf(bestStyle));
/*     */     } 
/* 124 */     return node;
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
/*     */   protected NodeTuple representJavaBeanProperty(Object javaBean, Property property, Object propertyValue, Tag customTag) {
/* 143 */     ScalarNode nodeKey = (ScalarNode)representData(property.getName());
/*     */     
/* 145 */     boolean hasAlias = this.representedObjects.containsKey(propertyValue);
/*     */     
/* 147 */     Node nodeValue = representData(propertyValue);
/*     */     
/* 149 */     if (propertyValue != null && !hasAlias) {
/* 150 */       NodeId nodeId = nodeValue.getNodeId();
/* 151 */       if (customTag == null) {
/* 152 */         if (nodeId == NodeId.scalar) {
/* 153 */           if (property.getType() == propertyValue.getClass() && 
/* 154 */             propertyValue instanceof Enum) {
/* 155 */             nodeValue.setTag(Tag.STR);
/*     */           }
/*     */         } else {
/*     */           
/* 159 */           if (nodeId == NodeId.mapping && 
/* 160 */             property.getType() == propertyValue.getClass() && 
/* 161 */             !(propertyValue instanceof Map) && 
/* 162 */             !nodeValue.getTag().equals(Tag.SET)) {
/* 163 */             nodeValue.setTag(Tag.MAP);
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 168 */           checkGlobalTag(property, nodeValue, propertyValue);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 173 */     return new NodeTuple((Node)nodeKey, nodeValue);
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
/*     */   protected void checkGlobalTag(Property property, Node node, Object object) {
/* 190 */     if (object.getClass().isArray() && object.getClass().getComponentType().isPrimitive()) {
/*     */       return;
/*     */     }
/*     */     
/* 194 */     Class<?>[] arguments = property.getActualTypeArguments();
/* 195 */     if (arguments != null) {
/* 196 */       if (node.getNodeId() == NodeId.sequence) {
/*     */         
/* 198 */         Class<? extends Object> t = (Class)arguments[0];
/* 199 */         SequenceNode snode = (SequenceNode)node;
/* 200 */         Iterable<Object> memberList = Collections.EMPTY_LIST;
/* 201 */         if (object.getClass().isArray()) {
/* 202 */           memberList = Arrays.asList((Object[])object);
/* 203 */         } else if (object instanceof Iterable) {
/*     */           
/* 205 */           memberList = (Iterable<Object>)object;
/*     */         } 
/* 207 */         Iterator<Object> iter = memberList.iterator();
/* 208 */         if (iter.hasNext()) {
/* 209 */           for (Node childNode : snode.getValue()) {
/* 210 */             Object member = iter.next();
/* 211 */             if (member != null && 
/* 212 */               t.equals(member.getClass()) && 
/* 213 */               childNode.getNodeId() == NodeId.mapping) {
/* 214 */               childNode.setTag(Tag.MAP);
/*     */             }
/*     */           }
/*     */         
/*     */         }
/* 219 */       } else if (object instanceof Set) {
/* 220 */         Class<?> t = arguments[0];
/* 221 */         MappingNode mnode = (MappingNode)node;
/* 222 */         Iterator<NodeTuple> iter = mnode.getValue().iterator();
/* 223 */         Set<?> set = (Set)object;
/* 224 */         for (Object member : set) {
/* 225 */           NodeTuple tuple = iter.next();
/* 226 */           Node keyNode = tuple.getKeyNode();
/* 227 */           if (t.equals(member.getClass()) && 
/* 228 */             keyNode.getNodeId() == NodeId.mapping) {
/* 229 */             keyNode.setTag(Tag.MAP);
/*     */           }
/*     */         }
/*     */       
/* 233 */       } else if (object instanceof Map) {
/* 234 */         Class<?> keyType = arguments[0];
/* 235 */         Class<?> valueType = arguments[1];
/* 236 */         MappingNode mnode = (MappingNode)node;
/* 237 */         for (NodeTuple tuple : mnode.getValue()) {
/* 238 */           resetTag((Class)keyType, tuple.getKeyNode());
/* 239 */           resetTag((Class)valueType, tuple.getValueNode());
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void resetTag(Class<? extends Object> type, Node node) {
/* 249 */     Tag tag = node.getTag();
/* 250 */     if (tag.matches(type)) {
/* 251 */       if (Enum.class.isAssignableFrom(type)) {
/* 252 */         node.setTag(Tag.STR);
/*     */       } else {
/* 254 */         node.setTag(Tag.MAP);
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
/*     */   protected Set<Property> getProperties(Class<? extends Object> type) {
/* 268 */     if (this.typeDefinitions.containsKey(type)) {
/* 269 */       return ((TypeDescription)this.typeDefinitions.get(type)).getProperties();
/*     */     }
/* 271 */     return getPropertyUtils().getProperties(type);
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\yaml\snakeyaml\representer\Representer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */