/*     */ package org.yaml.snakeyaml.composer;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.yaml.snakeyaml.events.AliasEvent;
/*     */ import org.yaml.snakeyaml.events.Event;
/*     */ import org.yaml.snakeyaml.events.MappingStartEvent;
/*     */ import org.yaml.snakeyaml.events.NodeEvent;
/*     */ import org.yaml.snakeyaml.events.ScalarEvent;
/*     */ import org.yaml.snakeyaml.events.SequenceStartEvent;
/*     */ import org.yaml.snakeyaml.nodes.MappingNode;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.NodeId;
/*     */ import org.yaml.snakeyaml.nodes.NodeTuple;
/*     */ import org.yaml.snakeyaml.nodes.ScalarNode;
/*     */ import org.yaml.snakeyaml.nodes.SequenceNode;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
/*     */ import org.yaml.snakeyaml.parser.Parser;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Composer
/*     */ {
/*     */   protected final Parser parser;
/*     */   private final Resolver resolver;
/*     */   private final Map<String, Node> anchors;
/*     */   private final Set<Node> recursiveNodes;
/*     */   
/*     */   public Composer(Parser parser, Resolver resolver) {
/*  55 */     this.parser = parser;
/*  56 */     this.resolver = resolver;
/*  57 */     this.anchors = new HashMap<String, Node>();
/*  58 */     this.recursiveNodes = new HashSet<Node>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean checkNode() {
/*  68 */     if (this.parser.checkEvent(Event.ID.StreamStart)) {
/*  69 */       this.parser.getEvent();
/*     */     }
/*     */     
/*  72 */     return !this.parser.checkEvent(Event.ID.StreamEnd);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node getNode() {
/*  83 */     if (!this.parser.checkEvent(Event.ID.StreamEnd)) {
/*  84 */       return composeDocument();
/*     */     }
/*  86 */     return null;
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
/*     */   public Node getSingleNode() {
/* 101 */     this.parser.getEvent();
/*     */     
/* 103 */     Node document = null;
/* 104 */     if (!this.parser.checkEvent(Event.ID.StreamEnd)) {
/* 105 */       document = composeDocument();
/*     */     }
/*     */     
/* 108 */     if (!this.parser.checkEvent(Event.ID.StreamEnd)) {
/* 109 */       Event event = this.parser.getEvent();
/* 110 */       throw new ComposerException("expected a single document in the stream", document
/* 111 */           .getStartMark(), "but found another document", event.getStartMark());
/*     */     } 
/*     */     
/* 114 */     this.parser.getEvent();
/* 115 */     return document;
/*     */   }
/*     */ 
/*     */   
/*     */   private Node composeDocument() {
/* 120 */     this.parser.getEvent();
/*     */     
/* 122 */     Node node = composeNode(null);
/*     */     
/* 124 */     this.parser.getEvent();
/* 125 */     this.anchors.clear();
/* 126 */     this.recursiveNodes.clear();
/* 127 */     return node;
/*     */   }
/*     */   
/*     */   private Node composeNode(Node parent) {
/* 131 */     this.recursiveNodes.add(parent);
/* 132 */     Node node = null;
/* 133 */     if (this.parser.checkEvent(Event.ID.Alias)) {
/* 134 */       AliasEvent event = (AliasEvent)this.parser.getEvent();
/* 135 */       String anchor = event.getAnchor();
/* 136 */       if (!this.anchors.containsKey(anchor)) {
/* 137 */         throw new ComposerException(null, null, "found undefined alias " + anchor, event
/* 138 */             .getStartMark());
/*     */       }
/* 140 */       node = this.anchors.get(anchor);
/* 141 */       if (this.recursiveNodes.remove(node)) {
/* 142 */         node.setTwoStepsConstruction(true);
/*     */       }
/*     */     } else {
/* 145 */       NodeEvent event = (NodeEvent)this.parser.peekEvent();
/* 146 */       String anchor = null;
/* 147 */       anchor = event.getAnchor();
/*     */       
/* 149 */       if (this.parser.checkEvent(Event.ID.Scalar)) {
/* 150 */         node = composeScalarNode(anchor);
/* 151 */       } else if (this.parser.checkEvent(Event.ID.SequenceStart)) {
/* 152 */         node = composeSequenceNode(anchor);
/*     */       } else {
/* 154 */         node = composeMappingNode(anchor);
/*     */       } 
/*     */     } 
/* 157 */     this.recursiveNodes.remove(parent);
/* 158 */     return node;
/*     */   }
/*     */   protected Node composeScalarNode(String anchor) {
/*     */     Tag nodeTag;
/* 162 */     ScalarEvent ev = (ScalarEvent)this.parser.getEvent();
/* 163 */     String tag = ev.getTag();
/* 164 */     boolean resolved = false;
/*     */     
/* 166 */     if (tag == null || tag.equals("!")) {
/* 167 */       nodeTag = this.resolver.resolve(NodeId.scalar, ev.getValue(), ev
/* 168 */           .getImplicit().canOmitTagInPlainScalar());
/* 169 */       resolved = true;
/*     */     } else {
/* 171 */       nodeTag = new Tag(tag);
/*     */     } 
/*     */     
/* 174 */     ScalarNode scalarNode = new ScalarNode(nodeTag, resolved, ev.getValue(), ev.getStartMark(), ev.getEndMark(), ev.getStyle());
/* 175 */     if (anchor != null) {
/* 176 */       scalarNode.setAnchor(anchor);
/* 177 */       this.anchors.put(anchor, scalarNode);
/*     */     } 
/* 179 */     return (Node)scalarNode;
/*     */   }
/*     */   protected Node composeSequenceNode(String anchor) {
/*     */     Tag nodeTag;
/* 183 */     SequenceStartEvent startEvent = (SequenceStartEvent)this.parser.getEvent();
/* 184 */     String tag = startEvent.getTag();
/*     */     
/* 186 */     boolean resolved = false;
/* 187 */     if (tag == null || tag.equals("!")) {
/* 188 */       nodeTag = this.resolver.resolve(NodeId.sequence, null, startEvent.getImplicit());
/* 189 */       resolved = true;
/*     */     } else {
/* 191 */       nodeTag = new Tag(tag);
/*     */     } 
/* 193 */     ArrayList<Node> children = new ArrayList<Node>();
/*     */     
/* 195 */     SequenceNode node = new SequenceNode(nodeTag, resolved, children, startEvent.getStartMark(), null, startEvent.getFlowStyle());
/* 196 */     if (anchor != null) {
/* 197 */       node.setAnchor(anchor);
/* 198 */       this.anchors.put(anchor, node);
/*     */     } 
/* 200 */     while (!this.parser.checkEvent(Event.ID.SequenceEnd)) {
/* 201 */       children.add(composeNode((Node)node));
/*     */     }
/* 203 */     Event endEvent = this.parser.getEvent();
/* 204 */     node.setEndMark(endEvent.getEndMark());
/* 205 */     return (Node)node;
/*     */   }
/*     */   protected Node composeMappingNode(String anchor) {
/*     */     Tag nodeTag;
/* 209 */     MappingStartEvent startEvent = (MappingStartEvent)this.parser.getEvent();
/* 210 */     String tag = startEvent.getTag();
/*     */     
/* 212 */     boolean resolved = false;
/* 213 */     if (tag == null || tag.equals("!")) {
/* 214 */       nodeTag = this.resolver.resolve(NodeId.mapping, null, startEvent.getImplicit());
/* 215 */       resolved = true;
/*     */     } else {
/* 217 */       nodeTag = new Tag(tag);
/*     */     } 
/*     */     
/* 220 */     List<NodeTuple> children = new ArrayList<NodeTuple>();
/*     */     
/* 222 */     MappingNode node = new MappingNode(nodeTag, resolved, children, startEvent.getStartMark(), null, startEvent.getFlowStyle());
/* 223 */     if (anchor != null) {
/* 224 */       node.setAnchor(anchor);
/* 225 */       this.anchors.put(anchor, node);
/*     */     } 
/* 227 */     while (!this.parser.checkEvent(Event.ID.MappingEnd)) {
/* 228 */       composeMappingChildren(children, node);
/*     */     }
/* 230 */     Event endEvent = this.parser.getEvent();
/* 231 */     node.setEndMark(endEvent.getEndMark());
/* 232 */     return (Node)node;
/*     */   }
/*     */   
/*     */   protected void composeMappingChildren(List<NodeTuple> children, MappingNode node) {
/* 236 */     Node itemKey = composeKeyNode(node);
/* 237 */     if (itemKey.getTag().equals(Tag.MERGE)) {
/* 238 */       node.setMerged(true);
/*     */     }
/* 240 */     Node itemValue = composeValueNode(node);
/* 241 */     children.add(new NodeTuple(itemKey, itemValue));
/*     */   }
/*     */   
/*     */   protected Node composeKeyNode(MappingNode node) {
/* 245 */     return composeNode((Node)node);
/*     */   }
/*     */   
/*     */   protected Node composeValueNode(MappingNode node) {
/* 249 */     return composeNode((Node)node);
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\yaml\snakeyaml\composer\Composer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */