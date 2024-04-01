/*     */ package org.yaml.snakeyaml.representer;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.yaml.snakeyaml.DumperOptions;
/*     */ import org.yaml.snakeyaml.introspector.PropertyUtils;
/*     */ import org.yaml.snakeyaml.nodes.AnchorNode;
/*     */ import org.yaml.snakeyaml.nodes.MappingNode;
/*     */ import org.yaml.snakeyaml.nodes.Node;
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
/*     */ public abstract class BaseRepresenter
/*     */ {
/*  40 */   protected final Map<Class<?>, Represent> representers = new HashMap<Class<?>, Represent>();
/*     */ 
/*     */ 
/*     */   
/*     */   protected Represent nullRepresenter;
/*     */ 
/*     */ 
/*     */   
/*  48 */   protected final Map<Class<?>, Represent> multiRepresenters = new LinkedHashMap<Class<?>, Represent>();
/*     */   protected Character defaultScalarStyle;
/*  50 */   protected DumperOptions.FlowStyle defaultFlowStyle = DumperOptions.FlowStyle.AUTO;
/*  51 */   protected final Map<Object, Node> representedObjects = new IdentityHashMap<Object, Node>() {
/*     */       private static final long serialVersionUID = -5576159264232131854L;
/*     */       
/*     */       public Node put(Object key, Node value) {
/*  55 */         return (Node)super.put(key, new AnchorNode(value));
/*     */       }
/*     */     };
/*     */   
/*     */   protected Object objectToRepresent;
/*     */   private PropertyUtils propertyUtils;
/*     */   private boolean explicitPropertyUtils = false;
/*     */   
/*     */   public Node represent(Object data) {
/*  64 */     Node node = representData(data);
/*  65 */     this.representedObjects.clear();
/*  66 */     this.objectToRepresent = null;
/*  67 */     return node;
/*     */   }
/*     */   protected final Node representData(Object data) {
/*     */     Node node;
/*  71 */     this.objectToRepresent = data;
/*     */     
/*  73 */     if (this.representedObjects.containsKey(this.objectToRepresent)) {
/*  74 */       node = this.representedObjects.get(this.objectToRepresent);
/*  75 */       return node;
/*     */     } 
/*     */ 
/*     */     
/*  79 */     if (data == null) {
/*  80 */       node = this.nullRepresenter.representData(null);
/*  81 */       return node;
/*     */     } 
/*     */ 
/*     */     
/*  85 */     Class<?> clazz = data.getClass();
/*  86 */     if (this.representers.containsKey(clazz)) {
/*  87 */       Represent representer = this.representers.get(clazz);
/*  88 */       node = representer.representData(data);
/*     */     } else {
/*     */       
/*  91 */       for (Class<?> repr : this.multiRepresenters.keySet()) {
/*  92 */         if (repr != null && repr.isInstance(data)) {
/*  93 */           Represent representer = this.multiRepresenters.get(repr);
/*  94 */           node = representer.representData(data);
/*  95 */           return node;
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 100 */       if (this.multiRepresenters.containsKey(null)) {
/* 101 */         Represent representer = this.multiRepresenters.get(null);
/* 102 */         node = representer.representData(data);
/*     */       } else {
/* 104 */         Represent representer = this.representers.get(null);
/* 105 */         node = representer.representData(data);
/*     */       } 
/*     */     } 
/* 108 */     return node;
/*     */   }
/*     */   
/*     */   protected Node representScalar(Tag tag, String value, Character style) {
/* 112 */     if (style == null) {
/* 113 */       style = this.defaultScalarStyle;
/*     */     }
/* 115 */     return (Node)new ScalarNode(tag, value, null, null, style);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Node representScalar(Tag tag, String value) {
/* 120 */     return representScalar(tag, value, null);
/*     */   }
/*     */   
/*     */   protected Node representSequence(Tag tag, Iterable<?> sequence, Boolean flowStyle) {
/* 124 */     int size = 10;
/* 125 */     if (sequence instanceof List) {
/* 126 */       size = ((List)sequence).size();
/*     */     }
/* 128 */     List<Node> value = new ArrayList<Node>(size);
/* 129 */     SequenceNode node = new SequenceNode(tag, value, flowStyle);
/* 130 */     this.representedObjects.put(this.objectToRepresent, node);
/* 131 */     boolean bestStyle = true;
/* 132 */     for (Object item : sequence) {
/* 133 */       Node nodeItem = representData(item);
/* 134 */       if (!(nodeItem instanceof ScalarNode) || ((ScalarNode)nodeItem).getStyle() != null) {
/* 135 */         bestStyle = false;
/*     */       }
/* 137 */       value.add(nodeItem);
/*     */     } 
/* 139 */     if (flowStyle == null) {
/* 140 */       if (this.defaultFlowStyle != DumperOptions.FlowStyle.AUTO) {
/* 141 */         node.setFlowStyle(this.defaultFlowStyle.getStyleBoolean());
/*     */       } else {
/* 143 */         node.setFlowStyle(Boolean.valueOf(bestStyle));
/*     */       } 
/*     */     }
/* 146 */     return (Node)node;
/*     */   }
/*     */   
/*     */   protected Node representMapping(Tag tag, Map<?, ?> mapping, Boolean flowStyle) {
/* 150 */     List<NodeTuple> value = new ArrayList<NodeTuple>(mapping.size());
/* 151 */     MappingNode node = new MappingNode(tag, value, flowStyle);
/* 152 */     this.representedObjects.put(this.objectToRepresent, node);
/* 153 */     boolean bestStyle = true;
/* 154 */     for (Map.Entry<?, ?> entry : mapping.entrySet()) {
/* 155 */       Node nodeKey = representData(entry.getKey());
/* 156 */       Node nodeValue = representData(entry.getValue());
/* 157 */       if (!(nodeKey instanceof ScalarNode) || ((ScalarNode)nodeKey).getStyle() != null) {
/* 158 */         bestStyle = false;
/*     */       }
/* 160 */       if (!(nodeValue instanceof ScalarNode) || ((ScalarNode)nodeValue).getStyle() != null) {
/* 161 */         bestStyle = false;
/*     */       }
/* 163 */       value.add(new NodeTuple(nodeKey, nodeValue));
/*     */     } 
/* 165 */     if (flowStyle == null) {
/* 166 */       if (this.defaultFlowStyle != DumperOptions.FlowStyle.AUTO) {
/* 167 */         node.setFlowStyle(this.defaultFlowStyle.getStyleBoolean());
/*     */       } else {
/* 169 */         node.setFlowStyle(Boolean.valueOf(bestStyle));
/*     */       } 
/*     */     }
/* 172 */     return (Node)node;
/*     */   }
/*     */   
/*     */   public void setDefaultScalarStyle(DumperOptions.ScalarStyle defaultStyle) {
/* 176 */     this.defaultScalarStyle = defaultStyle.getChar();
/*     */   }
/*     */   
/*     */   public DumperOptions.ScalarStyle getDefaultScalarStyle() {
/* 180 */     return DumperOptions.ScalarStyle.createStyle(this.defaultScalarStyle);
/*     */   }
/*     */   
/*     */   public void setDefaultFlowStyle(DumperOptions.FlowStyle defaultFlowStyle) {
/* 184 */     this.defaultFlowStyle = defaultFlowStyle;
/*     */   }
/*     */   
/*     */   public DumperOptions.FlowStyle getDefaultFlowStyle() {
/* 188 */     return this.defaultFlowStyle;
/*     */   }
/*     */   
/*     */   public void setPropertyUtils(PropertyUtils propertyUtils) {
/* 192 */     this.propertyUtils = propertyUtils;
/* 193 */     this.explicitPropertyUtils = true;
/*     */   }
/*     */   
/*     */   public final PropertyUtils getPropertyUtils() {
/* 197 */     if (this.propertyUtils == null) {
/* 198 */       this.propertyUtils = new PropertyUtils();
/*     */     }
/* 200 */     return this.propertyUtils;
/*     */   }
/*     */   
/*     */   public final boolean isExplicitPropertyUtils() {
/* 204 */     return this.explicitPropertyUtils;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\yaml\snakeyaml\representer\BaseRepresenter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */