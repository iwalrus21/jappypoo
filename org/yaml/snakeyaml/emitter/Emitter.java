/*      */ package org.yaml.snakeyaml.emitter;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.Writer;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.Map;
/*      */ import java.util.Queue;
/*      */ import java.util.Set;
/*      */ import java.util.TreeSet;
/*      */ import java.util.concurrent.ArrayBlockingQueue;
/*      */ import java.util.regex.Pattern;
/*      */ import org.yaml.snakeyaml.DumperOptions;
/*      */ import org.yaml.snakeyaml.error.YAMLException;
/*      */ import org.yaml.snakeyaml.events.CollectionStartEvent;
/*      */ import org.yaml.snakeyaml.events.DocumentEndEvent;
/*      */ import org.yaml.snakeyaml.events.DocumentStartEvent;
/*      */ import org.yaml.snakeyaml.events.Event;
/*      */ import org.yaml.snakeyaml.events.MappingStartEvent;
/*      */ import org.yaml.snakeyaml.events.NodeEvent;
/*      */ import org.yaml.snakeyaml.events.ScalarEvent;
/*      */ import org.yaml.snakeyaml.events.SequenceStartEvent;
/*      */ import org.yaml.snakeyaml.reader.StreamReader;
/*      */ import org.yaml.snakeyaml.scanner.Constant;
/*      */ import org.yaml.snakeyaml.util.ArrayStack;
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
/*      */ public final class Emitter
/*      */   implements Emitable
/*      */ {
/*   63 */   private static final Map<Character, String> ESCAPE_REPLACEMENTS = new HashMap<Character, String>();
/*      */   
/*      */   public static final int MIN_INDENT = 1;
/*      */   public static final int MAX_INDENT = 10;
/*   67 */   private static final char[] SPACE = new char[] { ' ' };
/*      */   
/*      */   static {
/*   70 */     ESCAPE_REPLACEMENTS.put(Character.valueOf(false), "0");
/*   71 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\007'), "a");
/*   72 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\b'), "b");
/*   73 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\t'), "t");
/*   74 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\n'), "n");
/*   75 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\013'), "v");
/*   76 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\f'), "f");
/*   77 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\r'), "r");
/*   78 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\033'), "e");
/*   79 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('"'), "\"");
/*   80 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\\'), "\\");
/*   81 */     ESCAPE_REPLACEMENTS.put(Character.valueOf(''), "N");
/*   82 */     ESCAPE_REPLACEMENTS.put(Character.valueOf(' '), "_");
/*   83 */     ESCAPE_REPLACEMENTS.put(Character.valueOf(' '), "L");
/*   84 */     ESCAPE_REPLACEMENTS.put(Character.valueOf(' '), "P");
/*      */   }
/*      */   
/*   87 */   private static final Map<String, String> DEFAULT_TAG_PREFIXES = new LinkedHashMap<String, String>();
/*      */   static {
/*   89 */     DEFAULT_TAG_PREFIXES.put("!", "!");
/*   90 */     DEFAULT_TAG_PREFIXES.put("tag:yaml.org,2002:", "!!");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private final Writer stream;
/*      */ 
/*      */   
/*      */   private final ArrayStack<EmitterState> states;
/*      */ 
/*      */   
/*      */   private EmitterState state;
/*      */ 
/*      */   
/*      */   private final Queue<Event> events;
/*      */   
/*      */   private Event event;
/*      */   
/*      */   private final ArrayStack<Integer> indents;
/*      */   
/*      */   private Integer indent;
/*      */   
/*      */   private int flowLevel;
/*      */   
/*      */   private boolean rootContext;
/*      */   
/*      */   private boolean mappingContext;
/*      */   
/*      */   private boolean simpleKeyContext;
/*      */   
/*      */   private int column;
/*      */   
/*      */   private boolean whitespace;
/*      */   
/*      */   private boolean indention;
/*      */   
/*      */   private boolean openEnded;
/*      */   
/*      */   private Boolean canonical;
/*      */   
/*      */   private Boolean prettyFlow;
/*      */   
/*      */   private boolean allowUnicode;
/*      */   
/*      */   private int bestIndent;
/*      */   
/*      */   private int indicatorIndent;
/*      */   
/*      */   private int bestWidth;
/*      */   
/*      */   private char[] bestLineBreak;
/*      */   
/*      */   private boolean splitLines;
/*      */   
/*      */   private Map<String, String> tagPrefixes;
/*      */   
/*      */   private String preparedAnchor;
/*      */   
/*      */   private String preparedTag;
/*      */   
/*      */   private ScalarAnalysis analysis;
/*      */   
/*      */   private Character style;
/*      */ 
/*      */   
/*      */   public Emitter(Writer stream, DumperOptions opts) {
/*  156 */     this.stream = stream;
/*      */ 
/*      */     
/*  159 */     this.states = new ArrayStack(100);
/*  160 */     this.state = new ExpectStreamStart();
/*      */     
/*  162 */     this.events = new ArrayBlockingQueue<Event>(100);
/*  163 */     this.event = null;
/*      */     
/*  165 */     this.indents = new ArrayStack(10);
/*  166 */     this.indent = null;
/*      */     
/*  168 */     this.flowLevel = 0;
/*      */     
/*  170 */     this.mappingContext = false;
/*  171 */     this.simpleKeyContext = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  179 */     this.column = 0;
/*  180 */     this.whitespace = true;
/*  181 */     this.indention = true;
/*      */ 
/*      */     
/*  184 */     this.openEnded = false;
/*      */ 
/*      */     
/*  187 */     this.canonical = Boolean.valueOf(opts.isCanonical());
/*  188 */     this.prettyFlow = Boolean.valueOf(opts.isPrettyFlow());
/*  189 */     this.allowUnicode = opts.isAllowUnicode();
/*  190 */     this.bestIndent = 2;
/*  191 */     if (opts.getIndent() > 1 && opts.getIndent() < 10) {
/*  192 */       this.bestIndent = opts.getIndent();
/*      */     }
/*  194 */     this.indicatorIndent = opts.getIndicatorIndent();
/*  195 */     this.bestWidth = 80;
/*  196 */     if (opts.getWidth() > this.bestIndent * 2) {
/*  197 */       this.bestWidth = opts.getWidth();
/*      */     }
/*  199 */     this.bestLineBreak = opts.getLineBreak().getString().toCharArray();
/*  200 */     this.splitLines = opts.getSplitLines();
/*      */ 
/*      */     
/*  203 */     this.tagPrefixes = new LinkedHashMap<String, String>();
/*      */ 
/*      */     
/*  206 */     this.preparedAnchor = null;
/*  207 */     this.preparedTag = null;
/*      */ 
/*      */     
/*  210 */     this.analysis = null;
/*  211 */     this.style = null;
/*      */   }
/*      */   
/*      */   public void emit(Event event) throws IOException {
/*  215 */     this.events.add(event);
/*  216 */     while (!needMoreEvents()) {
/*  217 */       this.event = this.events.poll();
/*  218 */       this.state.expect();
/*  219 */       this.event = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean needMoreEvents() {
/*  226 */     if (this.events.isEmpty()) {
/*  227 */       return true;
/*      */     }
/*  229 */     Event event = this.events.peek();
/*  230 */     if (event instanceof DocumentStartEvent)
/*  231 */       return needEvents(1); 
/*  232 */     if (event instanceof SequenceStartEvent)
/*  233 */       return needEvents(2); 
/*  234 */     if (event instanceof MappingStartEvent) {
/*  235 */       return needEvents(3);
/*      */     }
/*  237 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean needEvents(int count) {
/*  242 */     int level = 0;
/*  243 */     Iterator<Event> iter = this.events.iterator();
/*  244 */     iter.next();
/*  245 */     while (iter.hasNext()) {
/*  246 */       Event event = iter.next();
/*  247 */       if (event instanceof DocumentStartEvent || event instanceof CollectionStartEvent) {
/*  248 */         level++;
/*  249 */       } else if (event instanceof DocumentEndEvent || event instanceof org.yaml.snakeyaml.events.CollectionEndEvent) {
/*  250 */         level--;
/*  251 */       } else if (event instanceof org.yaml.snakeyaml.events.StreamEndEvent) {
/*  252 */         level = -1;
/*      */       } 
/*  254 */       if (level < 0) {
/*  255 */         return false;
/*      */       }
/*      */     } 
/*  258 */     return (this.events.size() < count + 1);
/*      */   }
/*      */   
/*      */   private void increaseIndent(boolean flow, boolean indentless) {
/*  262 */     this.indents.push(this.indent);
/*  263 */     if (this.indent == null) {
/*  264 */       if (flow) {
/*  265 */         this.indent = Integer.valueOf(this.bestIndent);
/*      */       } else {
/*  267 */         this.indent = Integer.valueOf(0);
/*      */       } 
/*  269 */     } else if (!indentless) {
/*  270 */       Emitter emitter = this; emitter.indent = Integer.valueOf(emitter.indent.intValue() + this.bestIndent);
/*      */     } 
/*      */   }
/*      */   
/*      */   private class ExpectStreamStart
/*      */     implements EmitterState
/*      */   {
/*      */     private ExpectStreamStart() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  280 */       if (Emitter.this.event instanceof org.yaml.snakeyaml.events.StreamStartEvent) {
/*  281 */         Emitter.this.writeStreamStart();
/*  282 */         Emitter.this.state = new Emitter.ExpectFirstDocumentStart();
/*      */       } else {
/*  284 */         throw new EmitterException("expected StreamStartEvent, but got " + Emitter.this.event);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private class ExpectNothing implements EmitterState {
/*      */     public void expect() throws IOException {
/*  291 */       throw new EmitterException("expecting nothing, but got " + Emitter.this.event);
/*      */     }
/*      */     
/*      */     private ExpectNothing() {} }
/*      */   
/*      */   private class ExpectFirstDocumentStart implements EmitterState { private ExpectFirstDocumentStart() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  299 */       (new Emitter.ExpectDocumentStart(true)).expect();
/*      */     } }
/*      */ 
/*      */   
/*      */   private class ExpectDocumentStart implements EmitterState {
/*      */     private boolean first;
/*      */     
/*      */     public ExpectDocumentStart(boolean first) {
/*  307 */       this.first = first;
/*      */     }
/*      */     
/*      */     public void expect() throws IOException {
/*  311 */       if (Emitter.this.event instanceof DocumentStartEvent) {
/*  312 */         DocumentStartEvent ev = (DocumentStartEvent)Emitter.this.event;
/*  313 */         if ((ev.getVersion() != null || ev.getTags() != null) && Emitter.this.openEnded) {
/*  314 */           Emitter.this.writeIndicator("...", true, false, false);
/*  315 */           Emitter.this.writeIndent();
/*      */         } 
/*  317 */         if (ev.getVersion() != null) {
/*  318 */           String versionText = Emitter.this.prepareVersion(ev.getVersion());
/*  319 */           Emitter.this.writeVersionDirective(versionText);
/*      */         } 
/*  321 */         Emitter.this.tagPrefixes = (Map)new LinkedHashMap<Object, Object>(Emitter.DEFAULT_TAG_PREFIXES);
/*  322 */         if (ev.getTags() != null) {
/*  323 */           Set<String> handles = new TreeSet<String>(ev.getTags().keySet());
/*  324 */           for (String handle : handles) {
/*  325 */             String prefix = (String)ev.getTags().get(handle);
/*  326 */             Emitter.this.tagPrefixes.put(prefix, handle);
/*  327 */             String handleText = Emitter.this.prepareTagHandle(handle);
/*  328 */             String prefixText = Emitter.this.prepareTagPrefix(prefix);
/*  329 */             Emitter.this.writeTagDirective(handleText, prefixText);
/*      */           } 
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/*  335 */         boolean implicit = (this.first && !ev.getExplicit() && !Emitter.this.canonical.booleanValue() && ev.getVersion() == null && (ev.getTags() == null || ev.getTags().isEmpty()) && !Emitter.this.checkEmptyDocument());
/*  336 */         if (!implicit) {
/*  337 */           Emitter.this.writeIndent();
/*  338 */           Emitter.this.writeIndicator("---", true, false, false);
/*  339 */           if (Emitter.this.canonical.booleanValue()) {
/*  340 */             Emitter.this.writeIndent();
/*      */           }
/*      */         } 
/*  343 */         Emitter.this.state = new Emitter.ExpectDocumentRoot();
/*  344 */       } else if (Emitter.this.event instanceof org.yaml.snakeyaml.events.StreamEndEvent) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  350 */         Emitter.this.writeStreamEnd();
/*  351 */         Emitter.this.state = new Emitter.ExpectNothing();
/*      */       } else {
/*  353 */         throw new EmitterException("expected DocumentStartEvent, but got " + Emitter.this.event);
/*      */       } 
/*      */     } }
/*      */   
/*      */   private class ExpectDocumentEnd implements EmitterState { private ExpectDocumentEnd() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  360 */       if (Emitter.this.event instanceof DocumentEndEvent) {
/*  361 */         Emitter.this.writeIndent();
/*  362 */         if (((DocumentEndEvent)Emitter.this.event).getExplicit()) {
/*  363 */           Emitter.this.writeIndicator("...", true, false, false);
/*  364 */           Emitter.this.writeIndent();
/*      */         } 
/*  366 */         Emitter.this.flushStream();
/*  367 */         Emitter.this.state = new Emitter.ExpectDocumentStart(false);
/*      */       } else {
/*  369 */         throw new EmitterException("expected DocumentEndEvent, but got " + Emitter.this.event);
/*      */       } 
/*      */     } }
/*      */   
/*      */   private class ExpectDocumentRoot implements EmitterState { private ExpectDocumentRoot() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  376 */       Emitter.this.states.push(new Emitter.ExpectDocumentEnd());
/*  377 */       Emitter.this.expectNode(true, false, false);
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void expectNode(boolean root, boolean mapping, boolean simpleKey) throws IOException {
/*  384 */     this.rootContext = root;
/*  385 */     this.mappingContext = mapping;
/*  386 */     this.simpleKeyContext = simpleKey;
/*  387 */     if (this.event instanceof org.yaml.snakeyaml.events.AliasEvent) {
/*  388 */       expectAlias();
/*  389 */     } else if (this.event instanceof ScalarEvent || this.event instanceof CollectionStartEvent) {
/*  390 */       processAnchor("&");
/*  391 */       processTag();
/*  392 */       if (this.event instanceof ScalarEvent) {
/*  393 */         expectScalar();
/*  394 */       } else if (this.event instanceof SequenceStartEvent) {
/*  395 */         if (this.flowLevel != 0 || this.canonical.booleanValue() || ((SequenceStartEvent)this.event).getFlowStyle().booleanValue() || 
/*  396 */           checkEmptySequence()) {
/*  397 */           expectFlowSequence();
/*      */         } else {
/*  399 */           expectBlockSequence();
/*      */         }
/*      */       
/*  402 */       } else if (this.flowLevel != 0 || this.canonical.booleanValue() || ((MappingStartEvent)this.event).getFlowStyle().booleanValue() || 
/*  403 */         checkEmptyMapping()) {
/*  404 */         expectFlowMapping();
/*      */       } else {
/*  406 */         expectBlockMapping();
/*      */       } 
/*      */     } else {
/*      */       
/*  410 */       throw new EmitterException("expected NodeEvent, but got " + this.event);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void expectAlias() throws IOException {
/*  415 */     if (((NodeEvent)this.event).getAnchor() == null) {
/*  416 */       throw new EmitterException("anchor is not specified for alias");
/*      */     }
/*  418 */     processAnchor("*");
/*  419 */     this.state = (EmitterState)this.states.pop();
/*      */   }
/*      */   
/*      */   private void expectScalar() throws IOException {
/*  423 */     increaseIndent(true, false);
/*  424 */     processScalar();
/*  425 */     this.indent = (Integer)this.indents.pop();
/*  426 */     this.state = (EmitterState)this.states.pop();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void expectFlowSequence() throws IOException {
/*  432 */     writeIndicator("[", true, true, false);
/*  433 */     this.flowLevel++;
/*  434 */     increaseIndent(true, false);
/*  435 */     if (this.prettyFlow.booleanValue()) {
/*  436 */       writeIndent();
/*      */     }
/*  438 */     this.state = new ExpectFirstFlowSequenceItem();
/*      */   }
/*      */   private class ExpectFirstFlowSequenceItem implements EmitterState { private ExpectFirstFlowSequenceItem() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  443 */       if (Emitter.this.event instanceof org.yaml.snakeyaml.events.SequenceEndEvent) {
/*  444 */         Emitter.this.indent = (Integer)Emitter.this.indents.pop();
/*  445 */         Emitter.this.flowLevel--;
/*  446 */         Emitter.this.writeIndicator("]", false, false, false);
/*  447 */         Emitter.this.state = (EmitterState)Emitter.this.states.pop();
/*      */       } else {
/*  449 */         if (Emitter.this.canonical.booleanValue() || (Emitter.this.column > Emitter.this.bestWidth && Emitter.this.splitLines) || Emitter.this.prettyFlow.booleanValue()) {
/*  450 */           Emitter.this.writeIndent();
/*      */         }
/*  452 */         Emitter.this.states.push(new Emitter.ExpectFlowSequenceItem());
/*  453 */         Emitter.this.expectNode(false, false, false);
/*      */       } 
/*      */     } }
/*      */   
/*      */   private class ExpectFlowSequenceItem implements EmitterState { private ExpectFlowSequenceItem() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  460 */       if (Emitter.this.event instanceof org.yaml.snakeyaml.events.SequenceEndEvent) {
/*  461 */         Emitter.this.indent = (Integer)Emitter.this.indents.pop();
/*  462 */         Emitter.this.flowLevel--;
/*  463 */         if (Emitter.this.canonical.booleanValue()) {
/*  464 */           Emitter.this.writeIndicator(",", false, false, false);
/*  465 */           Emitter.this.writeIndent();
/*      */         } 
/*  467 */         Emitter.this.writeIndicator("]", false, false, false);
/*  468 */         if (Emitter.this.prettyFlow.booleanValue()) {
/*  469 */           Emitter.this.writeIndent();
/*      */         }
/*  471 */         Emitter.this.state = (EmitterState)Emitter.this.states.pop();
/*      */       } else {
/*  473 */         Emitter.this.writeIndicator(",", false, false, false);
/*  474 */         if (Emitter.this.canonical.booleanValue() || (Emitter.this.column > Emitter.this.bestWidth && Emitter.this.splitLines) || Emitter.this.prettyFlow.booleanValue()) {
/*  475 */           Emitter.this.writeIndent();
/*      */         }
/*  477 */         Emitter.this.states.push(new ExpectFlowSequenceItem());
/*  478 */         Emitter.this.expectNode(false, false, false);
/*      */       } 
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void expectFlowMapping() throws IOException {
/*  486 */     writeIndicator("{", true, true, false);
/*  487 */     this.flowLevel++;
/*  488 */     increaseIndent(true, false);
/*  489 */     if (this.prettyFlow.booleanValue()) {
/*  490 */       writeIndent();
/*      */     }
/*  492 */     this.state = new ExpectFirstFlowMappingKey();
/*      */   }
/*      */   private class ExpectFirstFlowMappingKey implements EmitterState { private ExpectFirstFlowMappingKey() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  497 */       if (Emitter.this.event instanceof org.yaml.snakeyaml.events.MappingEndEvent) {
/*  498 */         Emitter.this.indent = (Integer)Emitter.this.indents.pop();
/*  499 */         Emitter.this.flowLevel--;
/*  500 */         Emitter.this.writeIndicator("}", false, false, false);
/*  501 */         Emitter.this.state = (EmitterState)Emitter.this.states.pop();
/*      */       } else {
/*  503 */         if (Emitter.this.canonical.booleanValue() || (Emitter.this.column > Emitter.this.bestWidth && Emitter.this.splitLines) || Emitter.this.prettyFlow.booleanValue()) {
/*  504 */           Emitter.this.writeIndent();
/*      */         }
/*  506 */         if (!Emitter.this.canonical.booleanValue() && Emitter.this.checkSimpleKey()) {
/*  507 */           Emitter.this.states.push(new Emitter.ExpectFlowMappingSimpleValue());
/*  508 */           Emitter.this.expectNode(false, true, true);
/*      */         } else {
/*  510 */           Emitter.this.writeIndicator("?", true, false, false);
/*  511 */           Emitter.this.states.push(new Emitter.ExpectFlowMappingValue());
/*  512 */           Emitter.this.expectNode(false, true, false);
/*      */         } 
/*      */       } 
/*      */     } }
/*      */   
/*      */   private class ExpectFlowMappingKey implements EmitterState { private ExpectFlowMappingKey() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  520 */       if (Emitter.this.event instanceof org.yaml.snakeyaml.events.MappingEndEvent) {
/*  521 */         Emitter.this.indent = (Integer)Emitter.this.indents.pop();
/*  522 */         Emitter.this.flowLevel--;
/*  523 */         if (Emitter.this.canonical.booleanValue()) {
/*  524 */           Emitter.this.writeIndicator(",", false, false, false);
/*  525 */           Emitter.this.writeIndent();
/*      */         } 
/*  527 */         if (Emitter.this.prettyFlow.booleanValue()) {
/*  528 */           Emitter.this.writeIndent();
/*      */         }
/*  530 */         Emitter.this.writeIndicator("}", false, false, false);
/*  531 */         Emitter.this.state = (EmitterState)Emitter.this.states.pop();
/*      */       } else {
/*  533 */         Emitter.this.writeIndicator(",", false, false, false);
/*  534 */         if (Emitter.this.canonical.booleanValue() || (Emitter.this.column > Emitter.this.bestWidth && Emitter.this.splitLines) || Emitter.this.prettyFlow.booleanValue()) {
/*  535 */           Emitter.this.writeIndent();
/*      */         }
/*  537 */         if (!Emitter.this.canonical.booleanValue() && Emitter.this.checkSimpleKey()) {
/*  538 */           Emitter.this.states.push(new Emitter.ExpectFlowMappingSimpleValue());
/*  539 */           Emitter.this.expectNode(false, true, true);
/*      */         } else {
/*  541 */           Emitter.this.writeIndicator("?", true, false, false);
/*  542 */           Emitter.this.states.push(new Emitter.ExpectFlowMappingValue());
/*  543 */           Emitter.this.expectNode(false, true, false);
/*      */         } 
/*      */       } 
/*      */     } }
/*      */   
/*      */   private class ExpectFlowMappingSimpleValue implements EmitterState { private ExpectFlowMappingSimpleValue() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  551 */       Emitter.this.writeIndicator(":", false, false, false);
/*  552 */       Emitter.this.states.push(new Emitter.ExpectFlowMappingKey());
/*  553 */       Emitter.this.expectNode(false, true, false);
/*      */     } }
/*      */   
/*      */   private class ExpectFlowMappingValue implements EmitterState { private ExpectFlowMappingValue() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  559 */       if (Emitter.this.canonical.booleanValue() || Emitter.this.column > Emitter.this.bestWidth || Emitter.this.prettyFlow.booleanValue()) {
/*  560 */         Emitter.this.writeIndent();
/*      */       }
/*  562 */       Emitter.this.writeIndicator(":", true, false, false);
/*  563 */       Emitter.this.states.push(new Emitter.ExpectFlowMappingKey());
/*  564 */       Emitter.this.expectNode(false, true, false);
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void expectBlockSequence() throws IOException {
/*  571 */     boolean indentless = (this.mappingContext && !this.indention);
/*  572 */     increaseIndent(false, indentless);
/*  573 */     this.state = new ExpectFirstBlockSequenceItem();
/*      */   }
/*      */   private class ExpectFirstBlockSequenceItem implements EmitterState { private ExpectFirstBlockSequenceItem() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  578 */       (new Emitter.ExpectBlockSequenceItem(true)).expect();
/*      */     } }
/*      */ 
/*      */   
/*      */   private class ExpectBlockSequenceItem implements EmitterState {
/*      */     private boolean first;
/*      */     
/*      */     public ExpectBlockSequenceItem(boolean first) {
/*  586 */       this.first = first;
/*      */     }
/*      */     
/*      */     public void expect() throws IOException {
/*  590 */       if (!this.first && Emitter.this.event instanceof org.yaml.snakeyaml.events.SequenceEndEvent) {
/*  591 */         Emitter.this.indent = (Integer)Emitter.this.indents.pop();
/*  592 */         Emitter.this.state = (EmitterState)Emitter.this.states.pop();
/*      */       } else {
/*  594 */         Emitter.this.writeIndent();
/*  595 */         Emitter.this.writeWhitespace(Emitter.this.indicatorIndent);
/*  596 */         Emitter.this.writeIndicator("-", true, false, true);
/*  597 */         Emitter.this.states.push(new ExpectBlockSequenceItem(false));
/*  598 */         Emitter.this.expectNode(false, false, false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void expectBlockMapping() throws IOException {
/*  605 */     increaseIndent(false, false);
/*  606 */     this.state = new ExpectFirstBlockMappingKey();
/*      */   }
/*      */   private class ExpectFirstBlockMappingKey implements EmitterState { private ExpectFirstBlockMappingKey() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  611 */       (new Emitter.ExpectBlockMappingKey(true)).expect();
/*      */     } }
/*      */ 
/*      */   
/*      */   private class ExpectBlockMappingKey implements EmitterState {
/*      */     private boolean first;
/*      */     
/*      */     public ExpectBlockMappingKey(boolean first) {
/*  619 */       this.first = first;
/*      */     }
/*      */     
/*      */     public void expect() throws IOException {
/*  623 */       if (!this.first && Emitter.this.event instanceof org.yaml.snakeyaml.events.MappingEndEvent) {
/*  624 */         Emitter.this.indent = (Integer)Emitter.this.indents.pop();
/*  625 */         Emitter.this.state = (EmitterState)Emitter.this.states.pop();
/*      */       } else {
/*  627 */         Emitter.this.writeIndent();
/*  628 */         if (Emitter.this.checkSimpleKey()) {
/*  629 */           Emitter.this.states.push(new Emitter.ExpectBlockMappingSimpleValue());
/*  630 */           Emitter.this.expectNode(false, true, true);
/*      */         } else {
/*  632 */           Emitter.this.writeIndicator("?", true, false, true);
/*  633 */           Emitter.this.states.push(new Emitter.ExpectBlockMappingValue());
/*  634 */           Emitter.this.expectNode(false, true, false);
/*      */         } 
/*      */       } 
/*      */     } }
/*      */   
/*      */   private class ExpectBlockMappingSimpleValue implements EmitterState { private ExpectBlockMappingSimpleValue() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  642 */       Emitter.this.writeIndicator(":", false, false, false);
/*  643 */       Emitter.this.states.push(new Emitter.ExpectBlockMappingKey(false));
/*  644 */       Emitter.this.expectNode(false, true, false);
/*      */     } }
/*      */   
/*      */   private class ExpectBlockMappingValue implements EmitterState { private ExpectBlockMappingValue() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  650 */       Emitter.this.writeIndent();
/*  651 */       Emitter.this.writeIndicator(":", true, false, true);
/*  652 */       Emitter.this.states.push(new Emitter.ExpectBlockMappingKey(false));
/*  653 */       Emitter.this.expectNode(false, true, false);
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkEmptySequence() {
/*  660 */     return (this.event instanceof SequenceStartEvent && !this.events.isEmpty() && this.events.peek() instanceof org.yaml.snakeyaml.events.SequenceEndEvent);
/*      */   }
/*      */   
/*      */   private boolean checkEmptyMapping() {
/*  664 */     return (this.event instanceof MappingStartEvent && !this.events.isEmpty() && this.events.peek() instanceof org.yaml.snakeyaml.events.MappingEndEvent);
/*      */   }
/*      */   
/*      */   private boolean checkEmptyDocument() {
/*  668 */     if (!(this.event instanceof DocumentStartEvent) || this.events.isEmpty()) {
/*  669 */       return false;
/*      */     }
/*  671 */     Event event = this.events.peek();
/*  672 */     if (event instanceof ScalarEvent) {
/*  673 */       ScalarEvent e = (ScalarEvent)event;
/*  674 */       return (e.getAnchor() == null && e.getTag() == null && e.getImplicit() != null && e
/*  675 */         .getValue().length() == 0);
/*      */     } 
/*  677 */     return false;
/*      */   }
/*      */   
/*      */   private boolean checkSimpleKey() {
/*  681 */     int length = 0;
/*  682 */     if (this.event instanceof NodeEvent && ((NodeEvent)this.event).getAnchor() != null) {
/*  683 */       if (this.preparedAnchor == null) {
/*  684 */         this.preparedAnchor = prepareAnchor(((NodeEvent)this.event).getAnchor());
/*      */       }
/*  686 */       length += this.preparedAnchor.length();
/*      */     } 
/*  688 */     String tag = null;
/*  689 */     if (this.event instanceof ScalarEvent) {
/*  690 */       tag = ((ScalarEvent)this.event).getTag();
/*  691 */     } else if (this.event instanceof CollectionStartEvent) {
/*  692 */       tag = ((CollectionStartEvent)this.event).getTag();
/*      */     } 
/*  694 */     if (tag != null) {
/*  695 */       if (this.preparedTag == null) {
/*  696 */         this.preparedTag = prepareTag(tag);
/*      */       }
/*  698 */       length += this.preparedTag.length();
/*      */     } 
/*  700 */     if (this.event instanceof ScalarEvent) {
/*  701 */       if (this.analysis == null) {
/*  702 */         this.analysis = analyzeScalar(((ScalarEvent)this.event).getValue());
/*      */       }
/*  704 */       length += this.analysis.scalar.length();
/*      */     } 
/*  706 */     return (length < 128 && (this.event instanceof org.yaml.snakeyaml.events.AliasEvent || (this.event instanceof ScalarEvent && !this.analysis.empty && !this.analysis.multiline) || 
/*      */       
/*  708 */       checkEmptySequence() || checkEmptyMapping()));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void processAnchor(String indicator) throws IOException {
/*  714 */     NodeEvent ev = (NodeEvent)this.event;
/*  715 */     if (ev.getAnchor() == null) {
/*  716 */       this.preparedAnchor = null;
/*      */       return;
/*      */     } 
/*  719 */     if (this.preparedAnchor == null) {
/*  720 */       this.preparedAnchor = prepareAnchor(ev.getAnchor());
/*      */     }
/*  722 */     writeIndicator(indicator + this.preparedAnchor, true, false, false);
/*  723 */     this.preparedAnchor = null;
/*      */   }
/*      */   
/*      */   private void processTag() throws IOException {
/*  727 */     String tag = null;
/*  728 */     if (this.event instanceof ScalarEvent) {
/*  729 */       ScalarEvent ev = (ScalarEvent)this.event;
/*  730 */       tag = ev.getTag();
/*  731 */       if (this.style == null) {
/*  732 */         this.style = chooseScalarStyle();
/*      */       }
/*  734 */       if ((!this.canonical.booleanValue() || tag == null) && ((this.style == null && ev.getImplicit()
/*  735 */         .canOmitTagInPlainScalar()) || (this.style != null && ev.getImplicit()
/*  736 */         .canOmitTagInNonPlainScalar()))) {
/*  737 */         this.preparedTag = null;
/*      */         return;
/*      */       } 
/*  740 */       if (ev.getImplicit().canOmitTagInPlainScalar() && tag == null) {
/*  741 */         tag = "!";
/*  742 */         this.preparedTag = null;
/*      */       } 
/*      */     } else {
/*  745 */       CollectionStartEvent ev = (CollectionStartEvent)this.event;
/*  746 */       tag = ev.getTag();
/*  747 */       if ((!this.canonical.booleanValue() || tag == null) && ev.getImplicit()) {
/*  748 */         this.preparedTag = null;
/*      */         return;
/*      */       } 
/*      */     } 
/*  752 */     if (tag == null) {
/*  753 */       throw new EmitterException("tag is not specified");
/*      */     }
/*  755 */     if (this.preparedTag == null) {
/*  756 */       this.preparedTag = prepareTag(tag);
/*      */     }
/*  758 */     writeIndicator(this.preparedTag, true, false, false);
/*  759 */     this.preparedTag = null;
/*      */   }
/*      */   
/*      */   private Character chooseScalarStyle() {
/*  763 */     ScalarEvent ev = (ScalarEvent)this.event;
/*  764 */     if (this.analysis == null) {
/*  765 */       this.analysis = analyzeScalar(ev.getValue());
/*      */     }
/*  767 */     if ((ev.getStyle() != null && ev.getStyle().charValue() == '"') || this.canonical.booleanValue()) {
/*  768 */       return Character.valueOf('"');
/*      */     }
/*  770 */     if (ev.getStyle() == null && ev.getImplicit().canOmitTagInPlainScalar() && (
/*  771 */       !this.simpleKeyContext || (!this.analysis.empty && !this.analysis.multiline)) && ((this.flowLevel != 0 && this.analysis.allowFlowPlain) || (this.flowLevel == 0 && this.analysis.allowBlockPlain)))
/*      */     {
/*  773 */       return null;
/*      */     }
/*      */     
/*  776 */     if (ev.getStyle() != null && (ev.getStyle().charValue() == '|' || ev.getStyle().charValue() == '>') && 
/*  777 */       this.flowLevel == 0 && !this.simpleKeyContext && this.analysis.allowBlock) {
/*  778 */       return ev.getStyle();
/*      */     }
/*      */     
/*  781 */     if ((ev.getStyle() == null || ev.getStyle().charValue() == '\'') && 
/*  782 */       this.analysis.allowSingleQuoted && (!this.simpleKeyContext || !this.analysis.multiline)) {
/*  783 */       return Character.valueOf('\'');
/*      */     }
/*      */     
/*  786 */     return Character.valueOf('"');
/*      */   }
/*      */   
/*      */   private void processScalar() throws IOException {
/*  790 */     ScalarEvent ev = (ScalarEvent)this.event;
/*  791 */     if (this.analysis == null) {
/*  792 */       this.analysis = analyzeScalar(ev.getValue());
/*      */     }
/*  794 */     if (this.style == null) {
/*  795 */       this.style = chooseScalarStyle();
/*      */     }
/*  797 */     boolean split = (!this.simpleKeyContext && this.splitLines);
/*  798 */     if (this.style == null) {
/*  799 */       writePlain(this.analysis.scalar, split);
/*      */     } else {
/*  801 */       switch (this.style.charValue()) {
/*      */         case '"':
/*  803 */           writeDoubleQuoted(this.analysis.scalar, split);
/*      */           break;
/*      */         case '\'':
/*  806 */           writeSingleQuoted(this.analysis.scalar, split);
/*      */           break;
/*      */         case '>':
/*  809 */           writeFolded(this.analysis.scalar, split);
/*      */           break;
/*      */         case '|':
/*  812 */           writeLiteral(this.analysis.scalar);
/*      */           break;
/*      */         default:
/*  815 */           throw new YAMLException("Unexpected style: " + this.style);
/*      */       } 
/*      */     } 
/*  818 */     this.analysis = null;
/*  819 */     this.style = null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String prepareVersion(DumperOptions.Version version) {
/*  825 */     if (version.major() != 1) {
/*  826 */       throw new EmitterException("unsupported YAML version: " + version);
/*      */     }
/*  828 */     return version.getRepresentation();
/*      */   }
/*      */   
/*  831 */   private static final Pattern HANDLE_FORMAT = Pattern.compile("^![-_\\w]*!$");
/*      */   
/*      */   private String prepareTagHandle(String handle) {
/*  834 */     if (handle.length() == 0)
/*  835 */       throw new EmitterException("tag handle must not be empty"); 
/*  836 */     if (handle.charAt(0) != '!' || handle.charAt(handle.length() - 1) != '!')
/*  837 */       throw new EmitterException("tag handle must start and end with '!': " + handle); 
/*  838 */     if (!"!".equals(handle) && !HANDLE_FORMAT.matcher(handle).matches()) {
/*  839 */       throw new EmitterException("invalid character in the tag handle: " + handle);
/*      */     }
/*  841 */     return handle;
/*      */   }
/*      */   
/*      */   private String prepareTagPrefix(String prefix) {
/*  845 */     if (prefix.length() == 0) {
/*  846 */       throw new EmitterException("tag prefix must not be empty");
/*      */     }
/*  848 */     StringBuilder chunks = new StringBuilder();
/*  849 */     int start = 0;
/*  850 */     int end = 0;
/*  851 */     if (prefix.charAt(0) == '!') {
/*  852 */       end = 1;
/*      */     }
/*  854 */     while (end < prefix.length()) {
/*  855 */       end++;
/*      */     }
/*  857 */     if (start < end) {
/*  858 */       chunks.append(prefix.substring(start, end));
/*      */     }
/*  860 */     return chunks.toString();
/*      */   }
/*      */   
/*      */   private String prepareTag(String tag) {
/*  864 */     if (tag.length() == 0) {
/*  865 */       throw new EmitterException("tag must not be empty");
/*      */     }
/*  867 */     if ("!".equals(tag)) {
/*  868 */       return tag;
/*      */     }
/*  870 */     String handle = null;
/*  871 */     String suffix = tag;
/*      */     
/*  873 */     for (String prefix : this.tagPrefixes.keySet()) {
/*  874 */       if (tag.startsWith(prefix) && ("!".equals(prefix) || prefix.length() < tag.length())) {
/*  875 */         handle = prefix;
/*      */       }
/*      */     } 
/*  878 */     if (handle != null) {
/*  879 */       suffix = tag.substring(handle.length());
/*  880 */       handle = this.tagPrefixes.get(handle);
/*      */     } 
/*      */     
/*  883 */     int end = suffix.length();
/*  884 */     String suffixText = (end > 0) ? suffix.substring(0, end) : "";
/*      */     
/*  886 */     if (handle != null) {
/*  887 */       return handle + suffixText;
/*      */     }
/*  889 */     return "!<" + suffixText + ">";
/*      */   }
/*      */   
/*  892 */   private static final Pattern ANCHOR_FORMAT = Pattern.compile("^[-_\\w]*$");
/*      */   
/*      */   static String prepareAnchor(String anchor) {
/*  895 */     if (anchor.length() == 0) {
/*  896 */       throw new EmitterException("anchor must not be empty");
/*      */     }
/*  898 */     if (!ANCHOR_FORMAT.matcher(anchor).matches()) {
/*  899 */       throw new EmitterException("invalid character in the anchor: " + anchor);
/*      */     }
/*  901 */     return anchor;
/*      */   }
/*      */ 
/*      */   
/*      */   private ScalarAnalysis analyzeScalar(String scalar) {
/*  906 */     if (scalar.length() == 0) {
/*  907 */       return new ScalarAnalysis(scalar, true, false, false, true, true, false);
/*      */     }
/*      */     
/*  910 */     boolean blockIndicators = false;
/*  911 */     boolean flowIndicators = false;
/*  912 */     boolean lineBreaks = false;
/*  913 */     boolean specialCharacters = false;
/*      */ 
/*      */     
/*  916 */     boolean leadingSpace = false;
/*  917 */     boolean leadingBreak = false;
/*  918 */     boolean trailingSpace = false;
/*  919 */     boolean trailingBreak = false;
/*  920 */     boolean breakSpace = false;
/*  921 */     boolean spaceBreak = false;
/*      */ 
/*      */     
/*  924 */     if (scalar.startsWith("---") || scalar.startsWith("...")) {
/*  925 */       blockIndicators = true;
/*  926 */       flowIndicators = true;
/*      */     } 
/*      */     
/*  929 */     boolean preceededByWhitespace = true;
/*  930 */     boolean followedByWhitespace = (scalar.length() == 1 || Constant.NULL_BL_T_LINEBR.has(scalar.codePointAt(1)));
/*      */     
/*  932 */     boolean previousSpace = false;
/*      */ 
/*      */     
/*  935 */     boolean previousBreak = false;
/*      */     
/*  937 */     int index = 0;
/*      */     
/*  939 */     while (index < scalar.length()) {
/*  940 */       int c = scalar.codePointAt(index);
/*      */       
/*  942 */       if (index == 0) {
/*      */         
/*  944 */         if ("#,[]{}&*!|>'\"%@`".indexOf(c) != -1) {
/*  945 */           flowIndicators = true;
/*  946 */           blockIndicators = true;
/*      */         } 
/*  948 */         if (c == 63 || c == 58) {
/*  949 */           flowIndicators = true;
/*  950 */           if (followedByWhitespace) {
/*  951 */             blockIndicators = true;
/*      */           }
/*      */         } 
/*  954 */         if (c == 45 && followedByWhitespace) {
/*  955 */           flowIndicators = true;
/*  956 */           blockIndicators = true;
/*      */         } 
/*      */       } else {
/*      */         
/*  960 */         if (",?[]{}".indexOf(c) != -1) {
/*  961 */           flowIndicators = true;
/*      */         }
/*  963 */         if (c == 58) {
/*  964 */           flowIndicators = true;
/*  965 */           if (followedByWhitespace) {
/*  966 */             blockIndicators = true;
/*      */           }
/*      */         } 
/*  969 */         if (c == 35 && preceededByWhitespace) {
/*  970 */           flowIndicators = true;
/*  971 */           blockIndicators = true;
/*      */         } 
/*      */       } 
/*      */       
/*  975 */       boolean isLineBreak = Constant.LINEBR.has(c);
/*  976 */       if (isLineBreak) {
/*  977 */         lineBreaks = true;
/*      */       }
/*  979 */       if (c != 10 && (32 > c || c > 126)) {
/*  980 */         if (c == 133 || (c >= 160 && c <= 55295) || (c >= 57344 && c <= 65533) || (c >= 65536 && c <= 1114111)) {
/*      */ 
/*      */ 
/*      */           
/*  984 */           if (!this.allowUnicode) {
/*  985 */             specialCharacters = true;
/*      */           }
/*      */         } else {
/*  988 */           specialCharacters = true;
/*      */         } 
/*      */       }
/*      */       
/*  992 */       if (c == 32) {
/*  993 */         if (index == 0) {
/*  994 */           leadingSpace = true;
/*      */         }
/*  996 */         if (index == scalar.length() - 1) {
/*  997 */           trailingSpace = true;
/*      */         }
/*  999 */         if (previousBreak) {
/* 1000 */           breakSpace = true;
/*      */         }
/* 1002 */         previousSpace = true;
/* 1003 */         previousBreak = false;
/* 1004 */       } else if (isLineBreak) {
/* 1005 */         if (index == 0) {
/* 1006 */           leadingBreak = true;
/*      */         }
/* 1008 */         if (index == scalar.length() - 1) {
/* 1009 */           trailingBreak = true;
/*      */         }
/* 1011 */         if (previousSpace) {
/* 1012 */           spaceBreak = true;
/*      */         }
/* 1014 */         previousSpace = false;
/* 1015 */         previousBreak = true;
/*      */       } else {
/* 1017 */         previousSpace = false;
/* 1018 */         previousBreak = false;
/*      */       } 
/*      */ 
/*      */       
/* 1022 */       index += Character.charCount(c);
/* 1023 */       preceededByWhitespace = (Constant.NULL_BL_T.has(c) || isLineBreak);
/* 1024 */       followedByWhitespace = true;
/* 1025 */       if (index + 1 < scalar.length()) {
/* 1026 */         int nextIndex = index + Character.charCount(scalar.codePointAt(index));
/* 1027 */         if (nextIndex < scalar.length()) {
/* 1028 */           followedByWhitespace = (Constant.NULL_BL_T.has(scalar.codePointAt(nextIndex)) || isLineBreak);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1033 */     boolean allowFlowPlain = true;
/* 1034 */     boolean allowBlockPlain = true;
/* 1035 */     boolean allowSingleQuoted = true;
/* 1036 */     boolean allowBlock = true;
/*      */     
/* 1038 */     if (leadingSpace || leadingBreak || trailingSpace || trailingBreak) {
/* 1039 */       allowFlowPlain = allowBlockPlain = false;
/*      */     }
/*      */     
/* 1042 */     if (trailingSpace) {
/* 1043 */       allowBlock = false;
/*      */     }
/*      */ 
/*      */     
/* 1047 */     if (breakSpace) {
/* 1048 */       allowFlowPlain = allowBlockPlain = allowSingleQuoted = false;
/*      */     }
/*      */ 
/*      */     
/* 1052 */     if (spaceBreak || specialCharacters) {
/* 1053 */       allowFlowPlain = allowBlockPlain = allowSingleQuoted = allowBlock = false;
/*      */     }
/*      */ 
/*      */     
/* 1057 */     if (lineBreaks) {
/* 1058 */       allowFlowPlain = false;
/*      */     }
/*      */     
/* 1061 */     if (flowIndicators) {
/* 1062 */       allowFlowPlain = false;
/*      */     }
/*      */     
/* 1065 */     if (blockIndicators) {
/* 1066 */       allowBlockPlain = false;
/*      */     }
/*      */     
/* 1069 */     return new ScalarAnalysis(scalar, false, lineBreaks, allowFlowPlain, allowBlockPlain, allowSingleQuoted, allowBlock);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void flushStream() throws IOException {
/* 1076 */     this.stream.flush();
/*      */   }
/*      */ 
/*      */   
/*      */   void writeStreamStart() {}
/*      */ 
/*      */   
/*      */   void writeStreamEnd() throws IOException {
/* 1084 */     flushStream();
/*      */   }
/*      */ 
/*      */   
/*      */   void writeIndicator(String indicator, boolean needWhitespace, boolean whitespace, boolean indentation) throws IOException {
/* 1089 */     if (!this.whitespace && needWhitespace) {
/* 1090 */       this.column++;
/* 1091 */       this.stream.write(SPACE);
/*      */     } 
/* 1093 */     this.whitespace = whitespace;
/* 1094 */     this.indention = (this.indention && indentation);
/* 1095 */     this.column += indicator.length();
/* 1096 */     this.openEnded = false;
/* 1097 */     this.stream.write(indicator);
/*      */   }
/*      */   
/*      */   void writeIndent() throws IOException {
/*      */     int indent;
/* 1102 */     if (this.indent != null) {
/* 1103 */       indent = this.indent.intValue();
/*      */     } else {
/* 1105 */       indent = 0;
/*      */     } 
/*      */     
/* 1108 */     if (!this.indention || this.column > indent || (this.column == indent && !this.whitespace)) {
/* 1109 */       writeLineBreak(null);
/*      */     }
/*      */     
/* 1112 */     writeWhitespace(indent - this.column);
/*      */   }
/*      */   
/*      */   private void writeWhitespace(int length) throws IOException {
/* 1116 */     if (length <= 0) {
/*      */       return;
/*      */     }
/* 1119 */     this.whitespace = true;
/* 1120 */     char[] data = new char[length];
/* 1121 */     for (int i = 0; i < data.length; i++) {
/* 1122 */       data[i] = ' ';
/*      */     }
/* 1124 */     this.column += length;
/* 1125 */     this.stream.write(data);
/*      */   }
/*      */   
/*      */   private void writeLineBreak(String data) throws IOException {
/* 1129 */     this.whitespace = true;
/* 1130 */     this.indention = true;
/* 1131 */     this.column = 0;
/* 1132 */     if (data == null) {
/* 1133 */       this.stream.write(this.bestLineBreak);
/*      */     } else {
/* 1135 */       this.stream.write(data);
/*      */     } 
/*      */   }
/*      */   
/*      */   void writeVersionDirective(String versionText) throws IOException {
/* 1140 */     this.stream.write("%YAML ");
/* 1141 */     this.stream.write(versionText);
/* 1142 */     writeLineBreak(null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void writeTagDirective(String handleText, String prefixText) throws IOException {
/* 1148 */     this.stream.write("%TAG ");
/* 1149 */     this.stream.write(handleText);
/* 1150 */     this.stream.write(SPACE);
/* 1151 */     this.stream.write(prefixText);
/* 1152 */     writeLineBreak(null);
/*      */   }
/*      */ 
/*      */   
/*      */   private void writeSingleQuoted(String text, boolean split) throws IOException {
/* 1157 */     writeIndicator("'", true, false, false);
/* 1158 */     boolean spaces = false;
/* 1159 */     boolean breaks = false;
/* 1160 */     int start = 0, end = 0;
/*      */     
/* 1162 */     while (end <= text.length()) {
/* 1163 */       char ch = Character.MIN_VALUE;
/* 1164 */       if (end < text.length()) {
/* 1165 */         ch = text.charAt(end);
/*      */       }
/* 1167 */       if (spaces) {
/* 1168 */         if (ch == '\000' || ch != ' ') {
/* 1169 */           if (start + 1 == end && this.column > this.bestWidth && split && start != 0 && end != text
/* 1170 */             .length()) {
/* 1171 */             writeIndent();
/*      */           } else {
/* 1173 */             int len = end - start;
/* 1174 */             this.column += len;
/* 1175 */             this.stream.write(text, start, len);
/*      */           } 
/* 1177 */           start = end;
/*      */         } 
/* 1179 */       } else if (breaks) {
/* 1180 */         if (ch == '\000' || Constant.LINEBR.hasNo(ch)) {
/* 1181 */           if (text.charAt(start) == '\n') {
/* 1182 */             writeLineBreak(null);
/*      */           }
/* 1184 */           String data = text.substring(start, end);
/* 1185 */           for (char br : data.toCharArray()) {
/* 1186 */             if (br == '\n') {
/* 1187 */               writeLineBreak(null);
/*      */             } else {
/* 1189 */               writeLineBreak(String.valueOf(br));
/*      */             } 
/*      */           } 
/* 1192 */           writeIndent();
/* 1193 */           start = end;
/*      */         }
/*      */       
/* 1196 */       } else if (Constant.LINEBR.has(ch, "\000 '") && 
/* 1197 */         start < end) {
/* 1198 */         int len = end - start;
/* 1199 */         this.column += len;
/* 1200 */         this.stream.write(text, start, len);
/* 1201 */         start = end;
/*      */       } 
/*      */ 
/*      */       
/* 1205 */       if (ch == '\'') {
/* 1206 */         this.column += 2;
/* 1207 */         this.stream.write("''");
/* 1208 */         start = end + 1;
/*      */       } 
/* 1210 */       if (ch != '\000') {
/* 1211 */         spaces = (ch == ' ');
/* 1212 */         breaks = Constant.LINEBR.has(ch);
/*      */       } 
/* 1214 */       end++;
/*      */     } 
/* 1216 */     writeIndicator("'", false, false, false);
/*      */   }
/*      */   
/*      */   private void writeDoubleQuoted(String text, boolean split) throws IOException {
/* 1220 */     writeIndicator("\"", true, false, false);
/* 1221 */     int start = 0;
/* 1222 */     int end = 0;
/* 1223 */     while (end <= text.length()) {
/* 1224 */       Character ch = null;
/* 1225 */       if (end < text.length()) {
/* 1226 */         ch = Character.valueOf(text.charAt(end));
/*      */       }
/* 1228 */       if (ch == null || "\"\\  ﻿".indexOf(ch.charValue()) != -1 || ' ' > ch
/* 1229 */         .charValue() || ch.charValue() > '~') {
/* 1230 */         if (start < end) {
/* 1231 */           int len = end - start;
/* 1232 */           this.column += len;
/* 1233 */           this.stream.write(text, start, len);
/* 1234 */           start = end;
/*      */         } 
/* 1236 */         if (ch != null) {
/*      */           String data;
/* 1238 */           if (ESCAPE_REPLACEMENTS.containsKey(ch)) {
/* 1239 */             data = "\\" + (String)ESCAPE_REPLACEMENTS.get(ch);
/* 1240 */           } else if (!this.allowUnicode || !StreamReader.isPrintable(ch.charValue())) {
/*      */ 
/*      */             
/* 1243 */             if (ch.charValue() <= 'ÿ') {
/* 1244 */               String s = "0" + Integer.toString(ch.charValue(), 16);
/* 1245 */               data = "\\x" + s.substring(s.length() - 2);
/* 1246 */             } else if (ch.charValue() >= '?' && ch.charValue() <= '?') {
/* 1247 */               if (end + 1 < text.length()) {
/* 1248 */                 Character ch2 = Character.valueOf(text.charAt(++end));
/* 1249 */                 String s = "000" + Long.toHexString(Character.toCodePoint(ch.charValue(), ch2.charValue()));
/* 1250 */                 data = "\\U" + s.substring(s.length() - 8);
/*      */               } else {
/* 1252 */                 String s = "000" + Integer.toString(ch.charValue(), 16);
/* 1253 */                 data = "\\u" + s.substring(s.length() - 4);
/*      */               } 
/*      */             } else {
/* 1256 */               String s = "000" + Integer.toString(ch.charValue(), 16);
/* 1257 */               data = "\\u" + s.substring(s.length() - 4);
/*      */             } 
/*      */           } else {
/* 1260 */             data = String.valueOf(ch);
/*      */           } 
/* 1262 */           this.column += data.length();
/* 1263 */           this.stream.write(data);
/* 1264 */           start = end + 1;
/*      */         } 
/*      */       } 
/* 1267 */       if (0 < end && end < text.length() - 1 && (ch.charValue() == ' ' || start >= end) && this.column + end - start > this.bestWidth && split) {
/*      */         String data;
/*      */         
/* 1270 */         if (start >= end) {
/* 1271 */           data = "\\";
/*      */         } else {
/* 1273 */           data = text.substring(start, end) + "\\";
/*      */         } 
/* 1275 */         if (start < end) {
/* 1276 */           start = end;
/*      */         }
/* 1278 */         this.column += data.length();
/* 1279 */         this.stream.write(data);
/* 1280 */         writeIndent();
/* 1281 */         this.whitespace = false;
/* 1282 */         this.indention = false;
/* 1283 */         if (text.charAt(start) == ' ') {
/* 1284 */           data = "\\";
/* 1285 */           this.column += data.length();
/* 1286 */           this.stream.write(data);
/*      */         } 
/*      */       } 
/* 1289 */       end++;
/*      */     } 
/* 1291 */     writeIndicator("\"", false, false, false);
/*      */   }
/*      */   
/*      */   private String determineBlockHints(String text) {
/* 1295 */     StringBuilder hints = new StringBuilder();
/* 1296 */     if (Constant.LINEBR.has(text.charAt(0), " ")) {
/* 1297 */       hints.append(this.bestIndent);
/*      */     }
/* 1299 */     char ch1 = text.charAt(text.length() - 1);
/* 1300 */     if (Constant.LINEBR.hasNo(ch1)) {
/* 1301 */       hints.append("-");
/* 1302 */     } else if (text.length() == 1 || Constant.LINEBR.has(text.charAt(text.length() - 2))) {
/* 1303 */       hints.append("+");
/*      */     } 
/* 1305 */     return hints.toString();
/*      */   }
/*      */   
/*      */   void writeFolded(String text, boolean split) throws IOException {
/* 1309 */     String hints = determineBlockHints(text);
/* 1310 */     writeIndicator(">" + hints, true, false, false);
/* 1311 */     if (hints.length() > 0 && hints.charAt(hints.length() - 1) == '+') {
/* 1312 */       this.openEnded = true;
/*      */     }
/* 1314 */     writeLineBreak(null);
/* 1315 */     boolean leadingSpace = true;
/* 1316 */     boolean spaces = false;
/* 1317 */     boolean breaks = true;
/* 1318 */     int start = 0, end = 0;
/* 1319 */     while (end <= text.length()) {
/* 1320 */       char ch = Character.MIN_VALUE;
/* 1321 */       if (end < text.length()) {
/* 1322 */         ch = text.charAt(end);
/*      */       }
/* 1324 */       if (breaks) {
/* 1325 */         if (ch == '\000' || Constant.LINEBR.hasNo(ch)) {
/* 1326 */           if (!leadingSpace && ch != '\000' && ch != ' ' && text.charAt(start) == '\n') {
/* 1327 */             writeLineBreak(null);
/*      */           }
/* 1329 */           leadingSpace = (ch == ' ');
/* 1330 */           String data = text.substring(start, end);
/* 1331 */           for (char br : data.toCharArray()) {
/* 1332 */             if (br == '\n') {
/* 1333 */               writeLineBreak(null);
/*      */             } else {
/* 1335 */               writeLineBreak(String.valueOf(br));
/*      */             } 
/*      */           } 
/* 1338 */           if (ch != '\000') {
/* 1339 */             writeIndent();
/*      */           }
/* 1341 */           start = end;
/*      */         } 
/* 1343 */       } else if (spaces) {
/* 1344 */         if (ch != ' ') {
/* 1345 */           if (start + 1 == end && this.column > this.bestWidth && split) {
/* 1346 */             writeIndent();
/*      */           } else {
/* 1348 */             int len = end - start;
/* 1349 */             this.column += len;
/* 1350 */             this.stream.write(text, start, len);
/*      */           } 
/* 1352 */           start = end;
/*      */         }
/*      */       
/* 1355 */       } else if (Constant.LINEBR.has(ch, "\000 ")) {
/* 1356 */         int len = end - start;
/* 1357 */         this.column += len;
/* 1358 */         this.stream.write(text, start, len);
/* 1359 */         if (ch == '\000') {
/* 1360 */           writeLineBreak(null);
/*      */         }
/* 1362 */         start = end;
/*      */       } 
/*      */       
/* 1365 */       if (ch != '\000') {
/* 1366 */         breaks = Constant.LINEBR.has(ch);
/* 1367 */         spaces = (ch == ' ');
/*      */       } 
/* 1369 */       end++;
/*      */     } 
/*      */   }
/*      */   
/*      */   void writeLiteral(String text) throws IOException {
/* 1374 */     String hints = determineBlockHints(text);
/* 1375 */     writeIndicator("|" + hints, true, false, false);
/* 1376 */     if (hints.length() > 0 && hints.charAt(hints.length() - 1) == '+') {
/* 1377 */       this.openEnded = true;
/*      */     }
/* 1379 */     writeLineBreak(null);
/* 1380 */     boolean breaks = true;
/* 1381 */     int start = 0, end = 0;
/* 1382 */     while (end <= text.length()) {
/* 1383 */       char ch = Character.MIN_VALUE;
/* 1384 */       if (end < text.length()) {
/* 1385 */         ch = text.charAt(end);
/*      */       }
/* 1387 */       if (breaks) {
/* 1388 */         if (ch == '\000' || Constant.LINEBR.hasNo(ch)) {
/* 1389 */           String data = text.substring(start, end);
/* 1390 */           for (char br : data.toCharArray()) {
/* 1391 */             if (br == '\n') {
/* 1392 */               writeLineBreak(null);
/*      */             } else {
/* 1394 */               writeLineBreak(String.valueOf(br));
/*      */             } 
/*      */           } 
/* 1397 */           if (ch != '\000') {
/* 1398 */             writeIndent();
/*      */           }
/* 1400 */           start = end;
/*      */         }
/*      */       
/* 1403 */       } else if (ch == '\000' || Constant.LINEBR.has(ch)) {
/* 1404 */         this.stream.write(text, start, end - start);
/* 1405 */         if (ch == '\000') {
/* 1406 */           writeLineBreak(null);
/*      */         }
/* 1408 */         start = end;
/*      */       } 
/*      */       
/* 1411 */       if (ch != '\000') {
/* 1412 */         breaks = Constant.LINEBR.has(ch);
/*      */       }
/* 1414 */       end++;
/*      */     } 
/*      */   }
/*      */   
/*      */   void writePlain(String text, boolean split) throws IOException {
/* 1419 */     if (this.rootContext) {
/* 1420 */       this.openEnded = true;
/*      */     }
/* 1422 */     if (text.length() == 0) {
/*      */       return;
/*      */     }
/* 1425 */     if (!this.whitespace) {
/* 1426 */       this.column++;
/* 1427 */       this.stream.write(SPACE);
/*      */     } 
/* 1429 */     this.whitespace = false;
/* 1430 */     this.indention = false;
/* 1431 */     boolean spaces = false;
/* 1432 */     boolean breaks = false;
/* 1433 */     int start = 0, end = 0;
/* 1434 */     while (end <= text.length()) {
/* 1435 */       char ch = Character.MIN_VALUE;
/* 1436 */       if (end < text.length()) {
/* 1437 */         ch = text.charAt(end);
/*      */       }
/* 1439 */       if (spaces) {
/* 1440 */         if (ch != ' ') {
/* 1441 */           if (start + 1 == end && this.column > this.bestWidth && split) {
/* 1442 */             writeIndent();
/* 1443 */             this.whitespace = false;
/* 1444 */             this.indention = false;
/*      */           } else {
/* 1446 */             int len = end - start;
/* 1447 */             this.column += len;
/* 1448 */             this.stream.write(text, start, len);
/*      */           } 
/* 1450 */           start = end;
/*      */         } 
/* 1452 */       } else if (breaks) {
/* 1453 */         if (Constant.LINEBR.hasNo(ch)) {
/* 1454 */           if (text.charAt(start) == '\n') {
/* 1455 */             writeLineBreak(null);
/*      */           }
/* 1457 */           String data = text.substring(start, end);
/* 1458 */           for (char br : data.toCharArray()) {
/* 1459 */             if (br == '\n') {
/* 1460 */               writeLineBreak(null);
/*      */             } else {
/* 1462 */               writeLineBreak(String.valueOf(br));
/*      */             } 
/*      */           } 
/* 1465 */           writeIndent();
/* 1466 */           this.whitespace = false;
/* 1467 */           this.indention = false;
/* 1468 */           start = end;
/*      */         }
/*      */       
/* 1471 */       } else if (Constant.LINEBR.has(ch, "\000 ")) {
/* 1472 */         int len = end - start;
/* 1473 */         this.column += len;
/* 1474 */         this.stream.write(text, start, len);
/* 1475 */         start = end;
/*      */       } 
/*      */       
/* 1478 */       if (ch != '\000') {
/* 1479 */         spaces = (ch == ' ');
/* 1480 */         breaks = Constant.LINEBR.has(ch);
/*      */       } 
/* 1482 */       end++;
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\yaml\snakeyaml\emitter\Emitter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */