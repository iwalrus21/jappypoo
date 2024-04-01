/*     */ package org.yaml.snakeyaml.serializer;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.yaml.snakeyaml.DumperOptions;
/*     */ import org.yaml.snakeyaml.emitter.Emitable;
/*     */ import org.yaml.snakeyaml.events.AliasEvent;
/*     */ import org.yaml.snakeyaml.events.DocumentEndEvent;
/*     */ import org.yaml.snakeyaml.events.DocumentStartEvent;
/*     */ import org.yaml.snakeyaml.events.Event;
/*     */ import org.yaml.snakeyaml.events.ImplicitTuple;
/*     */ import org.yaml.snakeyaml.events.MappingEndEvent;
/*     */ import org.yaml.snakeyaml.events.MappingStartEvent;
/*     */ import org.yaml.snakeyaml.events.ScalarEvent;
/*     */ import org.yaml.snakeyaml.events.SequenceEndEvent;
/*     */ import org.yaml.snakeyaml.events.SequenceStartEvent;
/*     */ import org.yaml.snakeyaml.events.StreamEndEvent;
/*     */ import org.yaml.snakeyaml.events.StreamStartEvent;
/*     */ import org.yaml.snakeyaml.nodes.AnchorNode;
/*     */ import org.yaml.snakeyaml.nodes.CollectionNode;
/*     */ import org.yaml.snakeyaml.nodes.MappingNode;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.NodeId;
/*     */ import org.yaml.snakeyaml.nodes.NodeTuple;
/*     */ import org.yaml.snakeyaml.nodes.ScalarNode;
/*     */ import org.yaml.snakeyaml.nodes.SequenceNode;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
/*     */ import org.yaml.snakeyaml.resolver.Resolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Serializer
/*     */ {
/*     */   private final Emitable emitter;
/*     */   private final Resolver resolver;
/*     */   private boolean explicitStart;
/*     */   private boolean explicitEnd;
/*     */   private DumperOptions.Version useVersion;
/*     */   private Map<String, String> useTags;
/*     */   private Set<Node> serializedNodes;
/*     */   private Map<Node, String> anchors;
/*     */   private AnchorGenerator anchorGenerator;
/*     */   private Boolean closed;
/*     */   private Tag explicitRoot;
/*     */   
/*     */   public Serializer(Emitable emitter, Resolver resolver, DumperOptions opts, Tag rootTag) {
/*  64 */     this.emitter = emitter;
/*  65 */     this.resolver = resolver;
/*  66 */     this.explicitStart = opts.isExplicitStart();
/*  67 */     this.explicitEnd = opts.isExplicitEnd();
/*  68 */     if (opts.getVersion() != null) {
/*  69 */       this.useVersion = opts.getVersion();
/*     */     }
/*  71 */     this.useTags = opts.getTags();
/*  72 */     this.serializedNodes = new HashSet<Node>();
/*  73 */     this.anchors = new HashMap<Node, String>();
/*  74 */     this.anchorGenerator = opts.getAnchorGenerator();
/*  75 */     this.closed = null;
/*  76 */     this.explicitRoot = rootTag;
/*     */   }
/*     */   
/*     */   public void open() throws IOException {
/*  80 */     if (this.closed == null)
/*  81 */     { this.emitter.emit((Event)new StreamStartEvent(null, null));
/*  82 */       this.closed = Boolean.FALSE; }
/*  83 */     else { if (Boolean.TRUE.equals(this.closed)) {
/*  84 */         throw new SerializerException("serializer is closed");
/*     */       }
/*  86 */       throw new SerializerException("serializer is already opened"); }
/*     */   
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/*  91 */     if (this.closed == null)
/*  92 */       throw new SerializerException("serializer is not opened"); 
/*  93 */     if (!Boolean.TRUE.equals(this.closed)) {
/*  94 */       this.emitter.emit((Event)new StreamEndEvent(null, null));
/*  95 */       this.closed = Boolean.TRUE;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void serialize(Node node) throws IOException {
/* 100 */     if (this.closed == null)
/* 101 */       throw new SerializerException("serializer is not opened"); 
/* 102 */     if (this.closed.booleanValue()) {
/* 103 */       throw new SerializerException("serializer is closed");
/*     */     }
/* 105 */     this.emitter.emit((Event)new DocumentStartEvent(null, null, this.explicitStart, this.useVersion, this.useTags));
/*     */     
/* 107 */     anchorNode(node);
/* 108 */     if (this.explicitRoot != null) {
/* 109 */       node.setTag(this.explicitRoot);
/*     */     }
/* 111 */     serializeNode(node, null);
/* 112 */     this.emitter.emit((Event)new DocumentEndEvent(null, null, this.explicitEnd));
/* 113 */     this.serializedNodes.clear();
/* 114 */     this.anchors.clear();
/*     */   }
/*     */   
/*     */   private void anchorNode(Node node) {
/* 118 */     if (node.getNodeId() == NodeId.anchor) {
/* 119 */       node = ((AnchorNode)node).getRealNode();
/*     */     }
/* 121 */     if (this.anchors.containsKey(node)) {
/* 122 */       String anchor = this.anchors.get(node);
/* 123 */       if (null == anchor) {
/* 124 */         anchor = this.anchorGenerator.nextAnchor(node);
/* 125 */         this.anchors.put(node, anchor);
/*     */       } 
/*     */     } else {
/* 128 */       SequenceNode seqNode; List<Node> list; MappingNode mnode; List<NodeTuple> map; this.anchors.put(node, null);
/* 129 */       switch (node.getNodeId()) {
/*     */         case sequence:
/* 131 */           seqNode = (SequenceNode)node;
/* 132 */           list = seqNode.getValue();
/* 133 */           for (Node item : list) {
/* 134 */             anchorNode(item);
/*     */           }
/*     */           break;
/*     */         case mapping:
/* 138 */           mnode = (MappingNode)node;
/* 139 */           map = mnode.getValue();
/* 140 */           for (NodeTuple object : map) {
/* 141 */             Node key = object.getKeyNode();
/* 142 */             Node value = object.getValueNode();
/* 143 */             anchorNode(key);
/* 144 */             anchorNode(value);
/*     */           } 
/*     */           break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void serializeNode(Node node, Node parent) throws IOException {
/* 152 */     if (node.getNodeId() == NodeId.anchor) {
/* 153 */       node = ((AnchorNode)node).getRealNode();
/*     */     }
/* 155 */     String tAlias = this.anchors.get(node);
/* 156 */     if (this.serializedNodes.contains(node)) {
/* 157 */       this.emitter.emit((Event)new AliasEvent(tAlias, null, null));
/*     */     } else {
/* 159 */       ScalarNode scalarNode; Tag detectedTag, defaultTag; ImplicitTuple tuple; ScalarEvent event; SequenceNode seqNode; boolean implicitS; List<Node> list; this.serializedNodes.add(node);
/* 160 */       switch (node.getNodeId()) {
/*     */         case scalar:
/* 162 */           scalarNode = (ScalarNode)node;
/* 163 */           detectedTag = this.resolver.resolve(NodeId.scalar, scalarNode.getValue(), true);
/* 164 */           defaultTag = this.resolver.resolve(NodeId.scalar, scalarNode.getValue(), false);
/*     */           
/* 166 */           tuple = new ImplicitTuple(node.getTag().equals(detectedTag), node.getTag().equals(defaultTag));
/*     */           
/* 168 */           event = new ScalarEvent(tAlias, node.getTag().getValue(), tuple, scalarNode.getValue(), null, null, scalarNode.getStyle());
/* 169 */           this.emitter.emit((Event)event);
/*     */           return;
/*     */         case sequence:
/* 172 */           seqNode = (SequenceNode)node;
/* 173 */           implicitS = node.getTag().equals(this.resolver.resolve(NodeId.sequence, null, true));
/*     */           
/* 175 */           this.emitter.emit((Event)new SequenceStartEvent(tAlias, node.getTag().getValue(), implicitS, null, null, seqNode
/* 176 */                 .getFlowStyle()));
/* 177 */           list = seqNode.getValue();
/* 178 */           for (Node item : list) {
/* 179 */             serializeNode(item, node);
/*     */           }
/* 181 */           this.emitter.emit((Event)new SequenceEndEvent(null, null));
/*     */           return;
/*     */       } 
/* 184 */       Tag implicitTag = this.resolver.resolve(NodeId.mapping, null, true);
/* 185 */       boolean implicitM = node.getTag().equals(implicitTag);
/* 186 */       this.emitter.emit((Event)new MappingStartEvent(tAlias, node.getTag().getValue(), implicitM, null, null, ((CollectionNode)node)
/* 187 */             .getFlowStyle()));
/* 188 */       MappingNode mnode = (MappingNode)node;
/* 189 */       List<NodeTuple> map = mnode.getValue();
/* 190 */       for (NodeTuple row : map) {
/* 191 */         Node key = row.getKeyNode();
/* 192 */         Node value = row.getValueNode();
/* 193 */         serializeNode(key, (Node)mnode);
/* 194 */         serializeNode(value, (Node)mnode);
/*     */       } 
/* 196 */       this.emitter.emit((Event)new MappingEndEvent(null, null));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\yaml\snakeyaml\serializer\Serializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */