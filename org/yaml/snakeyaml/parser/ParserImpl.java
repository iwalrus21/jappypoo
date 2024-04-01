/*     */ package org.yaml.snakeyaml.parser;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.yaml.snakeyaml.DumperOptions;
/*     */ import org.yaml.snakeyaml.error.Mark;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
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
/*     */ import org.yaml.snakeyaml.reader.StreamReader;
/*     */ import org.yaml.snakeyaml.scanner.Scanner;
/*     */ import org.yaml.snakeyaml.scanner.ScannerImpl;
/*     */ import org.yaml.snakeyaml.tokens.AliasToken;
/*     */ import org.yaml.snakeyaml.tokens.AnchorToken;
/*     */ import org.yaml.snakeyaml.tokens.BlockEntryToken;
/*     */ import org.yaml.snakeyaml.tokens.DirectiveToken;
/*     */ import org.yaml.snakeyaml.tokens.ScalarToken;
/*     */ import org.yaml.snakeyaml.tokens.StreamEndToken;
/*     */ import org.yaml.snakeyaml.tokens.StreamStartToken;
/*     */ import org.yaml.snakeyaml.tokens.TagToken;
/*     */ import org.yaml.snakeyaml.tokens.TagTuple;
/*     */ import org.yaml.snakeyaml.tokens.Token;
/*     */ import org.yaml.snakeyaml.util.ArrayStack;
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
/*     */ 
/*     */ public class ParserImpl
/*     */   implements Parser
/*     */ {
/* 117 */   private static final Map<String, String> DEFAULT_TAGS = new HashMap<String, String>();
/*     */   static {
/* 119 */     DEFAULT_TAGS.put("!", "!");
/* 120 */     DEFAULT_TAGS.put("!!", "tag:yaml.org,2002:");
/*     */   }
/*     */   
/*     */   protected final Scanner scanner;
/*     */   private Event currentEvent;
/*     */   private final ArrayStack<Production> states;
/*     */   private final ArrayStack<Mark> marks;
/*     */   private Production state;
/*     */   private VersionTagsTuple directives;
/*     */   
/*     */   public ParserImpl(StreamReader reader) {
/* 131 */     this((Scanner)new ScannerImpl(reader));
/*     */   }
/*     */   
/*     */   public ParserImpl(Scanner scanner) {
/* 135 */     this.scanner = scanner;
/* 136 */     this.currentEvent = null;
/* 137 */     this.directives = new VersionTagsTuple(null, new HashMap<String, String>(DEFAULT_TAGS));
/* 138 */     this.states = new ArrayStack(100);
/* 139 */     this.marks = new ArrayStack(10);
/* 140 */     this.state = new ParseStreamStart();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean checkEvent(Event.ID choice) {
/* 147 */     peekEvent();
/* 148 */     return (this.currentEvent != null && this.currentEvent.is(choice));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Event peekEvent() {
/* 155 */     if (this.currentEvent == null && 
/* 156 */       this.state != null) {
/* 157 */       this.currentEvent = this.state.produce();
/*     */     }
/*     */     
/* 160 */     return this.currentEvent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Event getEvent() {
/* 167 */     peekEvent();
/* 168 */     Event value = this.currentEvent;
/* 169 */     this.currentEvent = null;
/* 170 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class ParseStreamStart
/*     */     implements Production
/*     */   {
/*     */     private ParseStreamStart() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public Event produce() {
/* 183 */       StreamStartToken token = (StreamStartToken)ParserImpl.this.scanner.getToken();
/* 184 */       StreamStartEvent streamStartEvent = new StreamStartEvent(token.getStartMark(), token.getEndMark());
/*     */       
/* 186 */       ParserImpl.this.state = new ParserImpl.ParseImplicitDocumentStart();
/* 187 */       return (Event)streamStartEvent;
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseImplicitDocumentStart implements Production { private ParseImplicitDocumentStart() {}
/*     */     
/*     */     public Event produce() {
/* 194 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Directive, Token.ID.DocumentStart, Token.ID.StreamEnd })) {
/* 195 */         ParserImpl.this.directives = new VersionTagsTuple(null, ParserImpl.DEFAULT_TAGS);
/* 196 */         Token token = ParserImpl.this.scanner.peekToken();
/* 197 */         Mark startMark = token.getStartMark();
/* 198 */         Mark endMark = startMark;
/* 199 */         DocumentStartEvent documentStartEvent = new DocumentStartEvent(startMark, endMark, false, null, null);
/*     */         
/* 201 */         ParserImpl.this.states.push(new ParserImpl.ParseDocumentEnd());
/* 202 */         ParserImpl.this.state = new ParserImpl.ParseBlockNode();
/* 203 */         return (Event)documentStartEvent;
/*     */       } 
/* 205 */       Production p = new ParserImpl.ParseDocumentStart();
/* 206 */       return p.produce();
/*     */     } }
/*     */   
/*     */   private class ParseDocumentStart implements Production {
/*     */     private ParseDocumentStart() {}
/*     */     
/*     */     public Event produce() {
/*     */       StreamEndEvent streamEndEvent;
/* 214 */       while (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.DocumentEnd })) {
/* 215 */         ParserImpl.this.scanner.getToken();
/*     */       }
/*     */ 
/*     */       
/* 219 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.StreamEnd })) {
/* 220 */         Token token = ParserImpl.this.scanner.peekToken();
/* 221 */         Mark startMark = token.getStartMark();
/* 222 */         VersionTagsTuple tuple = ParserImpl.this.processDirectives();
/* 223 */         if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.DocumentStart })) {
/* 224 */           throw new ParserException(null, null, "expected '<document start>', but found " + ParserImpl.this.scanner
/* 225 */               .peekToken().getTokenId(), ParserImpl.this.scanner.peekToken().getStartMark());
/*     */         }
/* 227 */         token = ParserImpl.this.scanner.getToken();
/* 228 */         Mark endMark = token.getEndMark();
/*     */         
/* 230 */         DocumentStartEvent documentStartEvent = new DocumentStartEvent(startMark, endMark, true, tuple.getVersion(), tuple.getTags());
/* 231 */         ParserImpl.this.states.push(new ParserImpl.ParseDocumentEnd());
/* 232 */         ParserImpl.this.state = new ParserImpl.ParseDocumentContent();
/*     */       } else {
/*     */         
/* 235 */         StreamEndToken token = (StreamEndToken)ParserImpl.this.scanner.getToken();
/* 236 */         streamEndEvent = new StreamEndEvent(token.getStartMark(), token.getEndMark());
/* 237 */         if (!ParserImpl.this.states.isEmpty()) {
/* 238 */           throw new YAMLException("Unexpected end of stream. States left: " + ParserImpl.this.states);
/*     */         }
/* 240 */         if (!ParserImpl.this.marks.isEmpty()) {
/* 241 */           throw new YAMLException("Unexpected end of stream. Marks left: " + ParserImpl.this.marks);
/*     */         }
/* 243 */         ParserImpl.this.state = null;
/*     */       } 
/* 245 */       return (Event)streamEndEvent;
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseDocumentEnd implements Production { private ParseDocumentEnd() {}
/*     */     
/*     */     public Event produce() {
/* 252 */       Token token = ParserImpl.this.scanner.peekToken();
/* 253 */       Mark startMark = token.getStartMark();
/* 254 */       Mark endMark = startMark;
/* 255 */       boolean explicit = false;
/* 256 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.DocumentEnd })) {
/* 257 */         token = ParserImpl.this.scanner.getToken();
/* 258 */         endMark = token.getEndMark();
/* 259 */         explicit = true;
/*     */       } 
/* 261 */       DocumentEndEvent documentEndEvent = new DocumentEndEvent(startMark, endMark, explicit);
/*     */       
/* 263 */       ParserImpl.this.state = new ParserImpl.ParseDocumentStart();
/* 264 */       return (Event)documentEndEvent;
/*     */     } }
/*     */   
/*     */   private class ParseDocumentContent implements Production {
/*     */     private ParseDocumentContent() {}
/*     */     
/*     */     public Event produce() {
/* 271 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Directive, Token.ID.DocumentStart, Token.ID.DocumentEnd, Token.ID.StreamEnd })) {
/*     */         
/* 273 */         Event event = ParserImpl.this.processEmptyScalar(ParserImpl.this.scanner.peekToken().getStartMark());
/* 274 */         ParserImpl.this.state = (Production)ParserImpl.this.states.pop();
/* 275 */         return event;
/*     */       } 
/* 277 */       Production p = new ParserImpl.ParseBlockNode();
/* 278 */       return p.produce();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private VersionTagsTuple processDirectives() {
/* 285 */     DumperOptions.Version yamlVersion = null;
/* 286 */     HashMap<String, String> tagHandles = new HashMap<String, String>();
/* 287 */     while (this.scanner.checkToken(new Token.ID[] { Token.ID.Directive })) {
/*     */       
/* 289 */       DirectiveToken token = (DirectiveToken)this.scanner.getToken();
/* 290 */       if (token.getName().equals("YAML")) {
/* 291 */         if (yamlVersion != null) {
/* 292 */           throw new ParserException(null, null, "found duplicate YAML directive", token
/* 293 */               .getStartMark());
/*     */         }
/* 295 */         List<Integer> value = token.getValue();
/* 296 */         Integer major = value.get(0);
/* 297 */         if (major.intValue() != 1) {
/* 298 */           throw new ParserException(null, null, "found incompatible YAML document (version 1.* is required)", token
/*     */               
/* 300 */               .getStartMark());
/*     */         }
/* 302 */         Integer minor = value.get(1);
/* 303 */         switch (minor.intValue()) {
/*     */           case 0:
/* 305 */             yamlVersion = DumperOptions.Version.V1_0;
/*     */             continue;
/*     */         } 
/*     */         
/* 309 */         yamlVersion = DumperOptions.Version.V1_1;
/*     */         continue;
/*     */       } 
/* 312 */       if (token.getName().equals("TAG")) {
/* 313 */         List<String> value = token.getValue();
/* 314 */         String handle = value.get(0);
/* 315 */         String prefix = value.get(1);
/* 316 */         if (tagHandles.containsKey(handle)) {
/* 317 */           throw new ParserException(null, null, "duplicate tag handle " + handle, token
/* 318 */               .getStartMark());
/*     */         }
/* 320 */         tagHandles.put(handle, prefix);
/*     */       } 
/*     */     } 
/* 323 */     if (yamlVersion != null || !tagHandles.isEmpty()) {
/*     */       
/* 325 */       for (String key : DEFAULT_TAGS.keySet()) {
/*     */         
/* 327 */         if (!tagHandles.containsKey(key)) {
/* 328 */           tagHandles.put(key, DEFAULT_TAGS.get(key));
/*     */         }
/*     */       } 
/* 331 */       this.directives = new VersionTagsTuple(yamlVersion, tagHandles);
/*     */     } 
/* 333 */     return this.directives;
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
/*     */   private class ParseBlockNode
/*     */     implements Production
/*     */   {
/*     */     private ParseBlockNode() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Event produce() {
/* 358 */       return ParserImpl.this.parseNode(true, false);
/*     */     }
/*     */   }
/*     */   
/*     */   private Event parseFlowNode() {
/* 363 */     return parseNode(false, false);
/*     */   }
/*     */   
/*     */   private Event parseBlockNodeOrIndentlessSequence() {
/* 367 */     return parseNode(true, true);
/*     */   }
/*     */   
/*     */   private Event parseNode(boolean block, boolean indentlessSequence) {
/*     */     ScalarEvent scalarEvent;
/* 372 */     Mark startMark = null;
/* 373 */     Mark endMark = null;
/* 374 */     Mark tagMark = null;
/* 375 */     if (this.scanner.checkToken(new Token.ID[] { Token.ID.Alias })) {
/* 376 */       AliasToken token = (AliasToken)this.scanner.getToken();
/* 377 */       AliasEvent aliasEvent = new AliasEvent(token.getValue(), token.getStartMark(), token.getEndMark());
/* 378 */       this.state = (Production)this.states.pop();
/*     */     } else {
/* 380 */       String anchor = null;
/* 381 */       TagTuple tagTokenTag = null;
/* 382 */       if (this.scanner.checkToken(new Token.ID[] { Token.ID.Anchor })) {
/* 383 */         AnchorToken token = (AnchorToken)this.scanner.getToken();
/* 384 */         startMark = token.getStartMark();
/* 385 */         endMark = token.getEndMark();
/* 386 */         anchor = token.getValue();
/* 387 */         if (this.scanner.checkToken(new Token.ID[] { Token.ID.Tag })) {
/* 388 */           TagToken tagToken = (TagToken)this.scanner.getToken();
/* 389 */           tagMark = tagToken.getStartMark();
/* 390 */           endMark = tagToken.getEndMark();
/* 391 */           tagTokenTag = tagToken.getValue();
/*     */         } 
/*     */       } else {
/* 394 */         TagToken tagToken = (TagToken)this.scanner.getToken();
/* 395 */         startMark = tagToken.getStartMark();
/* 396 */         tagMark = startMark;
/* 397 */         endMark = tagToken.getEndMark();
/* 398 */         tagTokenTag = tagToken.getValue();
/* 399 */         if (this.scanner.checkToken(new Token.ID[] { Token.ID.Tag }) && this.scanner.checkToken(new Token.ID[] { Token.ID.Anchor })) {
/* 400 */           AnchorToken token = (AnchorToken)this.scanner.getToken();
/* 401 */           endMark = token.getEndMark();
/* 402 */           anchor = token.getValue();
/*     */         } 
/*     */       } 
/* 405 */       String tag = null;
/* 406 */       if (tagTokenTag != null) {
/* 407 */         String handle = tagTokenTag.getHandle();
/* 408 */         String suffix = tagTokenTag.getSuffix();
/* 409 */         if (handle != null) {
/* 410 */           if (!this.directives.getTags().containsKey(handle)) {
/* 411 */             throw new ParserException("while parsing a node", startMark, "found undefined tag handle " + handle, tagMark);
/*     */           }
/*     */           
/* 414 */           tag = (String)this.directives.getTags().get(handle) + suffix;
/*     */         } else {
/* 416 */           tag = suffix;
/*     */         } 
/*     */       } 
/* 419 */       if (startMark == null) {
/* 420 */         startMark = this.scanner.peekToken().getStartMark();
/* 421 */         endMark = startMark;
/*     */       } 
/* 423 */       Event event = null;
/* 424 */       boolean implicit = (tag == null || tag.equals("!"));
/* 425 */       if (indentlessSequence && this.scanner.checkToken(new Token.ID[] { Token.ID.BlockEntry })) {
/* 426 */         endMark = this.scanner.peekToken().getEndMark();
/* 427 */         SequenceStartEvent sequenceStartEvent = new SequenceStartEvent(anchor, tag, implicit, startMark, endMark, Boolean.FALSE);
/*     */         
/* 429 */         this.state = new ParseIndentlessSequenceEntry();
/*     */       }
/* 431 */       else if (this.scanner.checkToken(new Token.ID[] { Token.ID.Scalar })) {
/* 432 */         ImplicitTuple implicitValues; ScalarToken token = (ScalarToken)this.scanner.getToken();
/* 433 */         endMark = token.getEndMark();
/*     */         
/* 435 */         if ((token.getPlain() && tag == null) || "!".equals(tag)) {
/* 436 */           implicitValues = new ImplicitTuple(true, false);
/* 437 */         } else if (tag == null) {
/* 438 */           implicitValues = new ImplicitTuple(false, true);
/*     */         } else {
/* 440 */           implicitValues = new ImplicitTuple(false, false);
/*     */         } 
/*     */         
/* 443 */         scalarEvent = new ScalarEvent(anchor, tag, implicitValues, token.getValue(), startMark, endMark, Character.valueOf(token.getStyle()));
/* 444 */         this.state = (Production)this.states.pop();
/* 445 */       } else if (this.scanner.checkToken(new Token.ID[] { Token.ID.FlowSequenceStart })) {
/* 446 */         endMark = this.scanner.peekToken().getEndMark();
/* 447 */         SequenceStartEvent sequenceStartEvent = new SequenceStartEvent(anchor, tag, implicit, startMark, endMark, Boolean.TRUE);
/*     */         
/* 449 */         this.state = new ParseFlowSequenceFirstEntry();
/* 450 */       } else if (this.scanner.checkToken(new Token.ID[] { Token.ID.FlowMappingStart })) {
/* 451 */         endMark = this.scanner.peekToken().getEndMark();
/* 452 */         MappingStartEvent mappingStartEvent = new MappingStartEvent(anchor, tag, implicit, startMark, endMark, Boolean.TRUE);
/*     */         
/* 454 */         this.state = new ParseFlowMappingFirstKey();
/* 455 */       } else if (block && this.scanner.checkToken(new Token.ID[] { Token.ID.BlockSequenceStart })) {
/* 456 */         endMark = this.scanner.peekToken().getStartMark();
/* 457 */         SequenceStartEvent sequenceStartEvent = new SequenceStartEvent(anchor, tag, implicit, startMark, endMark, Boolean.FALSE);
/*     */         
/* 459 */         this.state = new ParseBlockSequenceFirstEntry();
/* 460 */       } else if (block && this.scanner.checkToken(new Token.ID[] { Token.ID.BlockMappingStart })) {
/* 461 */         endMark = this.scanner.peekToken().getStartMark();
/* 462 */         MappingStartEvent mappingStartEvent = new MappingStartEvent(anchor, tag, implicit, startMark, endMark, Boolean.FALSE);
/*     */         
/* 464 */         this.state = new ParseBlockMappingFirstKey();
/* 465 */       } else if (anchor != null || tag != null) {
/*     */ 
/*     */ 
/*     */         
/* 469 */         scalarEvent = new ScalarEvent(anchor, tag, new ImplicitTuple(implicit, false), "", startMark, endMark, Character.valueOf(false));
/* 470 */         this.state = (Production)this.states.pop();
/*     */       } else {
/*     */         String node;
/* 473 */         if (block) {
/* 474 */           node = "block";
/*     */         } else {
/* 476 */           node = "flow";
/*     */         } 
/* 478 */         Token token = this.scanner.peekToken();
/* 479 */         throw new ParserException("while parsing a " + node + " node", startMark, "expected the node content, but found " + token
/* 480 */             .getTokenId(), token
/* 481 */             .getStartMark());
/*     */       } 
/*     */     } 
/*     */     
/* 485 */     return (Event)scalarEvent;
/*     */   }
/*     */   
/*     */   private class ParseBlockSequenceFirstEntry
/*     */     implements Production {
/*     */     private ParseBlockSequenceFirstEntry() {}
/*     */     
/*     */     public Event produce() {
/* 493 */       Token token = ParserImpl.this.scanner.getToken();
/* 494 */       ParserImpl.this.marks.push(token.getStartMark());
/* 495 */       return (new ParserImpl.ParseBlockSequenceEntry()).produce();
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseBlockSequenceEntry implements Production {
/*     */     public Event produce() {
/* 501 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.BlockEntry })) {
/* 502 */         BlockEntryToken blockEntryToken = (BlockEntryToken)ParserImpl.this.scanner.getToken();
/* 503 */         if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.BlockEntry, Token.ID.BlockEnd })) {
/* 504 */           ParserImpl.this.states.push(new ParseBlockSequenceEntry());
/* 505 */           return (new ParserImpl.ParseBlockNode()).produce();
/*     */         } 
/* 507 */         ParserImpl.this.state = new ParseBlockSequenceEntry();
/* 508 */         return ParserImpl.this.processEmptyScalar(blockEntryToken.getEndMark());
/*     */       } 
/*     */       
/* 511 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.BlockEnd })) {
/* 512 */         Token token1 = ParserImpl.this.scanner.peekToken();
/* 513 */         throw new ParserException("while parsing a block collection", (Mark)ParserImpl.this.marks.pop(), "expected <block end>, but found " + token1
/* 514 */             .getTokenId(), token1
/* 515 */             .getStartMark());
/*     */       } 
/* 517 */       Token token = ParserImpl.this.scanner.getToken();
/* 518 */       SequenceEndEvent sequenceEndEvent = new SequenceEndEvent(token.getStartMark(), token.getEndMark());
/* 519 */       ParserImpl.this.state = (Production)ParserImpl.this.states.pop();
/* 520 */       ParserImpl.this.marks.pop();
/* 521 */       return (Event)sequenceEndEvent;
/*     */     }
/*     */     
/*     */     private ParseBlockSequenceEntry() {} }
/*     */   
/*     */   private class ParseIndentlessSequenceEntry implements Production { private ParseIndentlessSequenceEntry() {}
/*     */     
/*     */     public Event produce() {
/* 529 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.BlockEntry })) {
/* 530 */         Token token1 = ParserImpl.this.scanner.getToken();
/* 531 */         if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.BlockEntry, Token.ID.Key, Token.ID.Value, Token.ID.BlockEnd })) {
/*     */           
/* 533 */           ParserImpl.this.states.push(new ParseIndentlessSequenceEntry());
/* 534 */           return (new ParserImpl.ParseBlockNode()).produce();
/*     */         } 
/* 536 */         ParserImpl.this.state = new ParseIndentlessSequenceEntry();
/* 537 */         return ParserImpl.this.processEmptyScalar(token1.getEndMark());
/*     */       } 
/*     */       
/* 540 */       Token token = ParserImpl.this.scanner.peekToken();
/* 541 */       SequenceEndEvent sequenceEndEvent = new SequenceEndEvent(token.getStartMark(), token.getEndMark());
/* 542 */       ParserImpl.this.state = (Production)ParserImpl.this.states.pop();
/* 543 */       return (Event)sequenceEndEvent;
/*     */     } }
/*     */   
/*     */   private class ParseBlockMappingFirstKey implements Production { private ParseBlockMappingFirstKey() {}
/*     */     
/*     */     public Event produce() {
/* 549 */       Token token = ParserImpl.this.scanner.getToken();
/* 550 */       ParserImpl.this.marks.push(token.getStartMark());
/* 551 */       return (new ParserImpl.ParseBlockMappingKey()).produce();
/*     */     } }
/*     */   
/*     */   private class ParseBlockMappingKey implements Production { private ParseBlockMappingKey() {}
/*     */     
/*     */     public Event produce() {
/* 557 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Key })) {
/* 558 */         Token token1 = ParserImpl.this.scanner.getToken();
/* 559 */         if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Key, Token.ID.Value, Token.ID.BlockEnd })) {
/* 560 */           ParserImpl.this.states.push(new ParserImpl.ParseBlockMappingValue());
/* 561 */           return ParserImpl.this.parseBlockNodeOrIndentlessSequence();
/*     */         } 
/* 563 */         ParserImpl.this.state = new ParserImpl.ParseBlockMappingValue();
/* 564 */         return ParserImpl.this.processEmptyScalar(token1.getEndMark());
/*     */       } 
/*     */       
/* 567 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.BlockEnd })) {
/* 568 */         Token token1 = ParserImpl.this.scanner.peekToken();
/* 569 */         throw new ParserException("while parsing a block mapping", (Mark)ParserImpl.this.marks.pop(), "expected <block end>, but found " + token1
/* 570 */             .getTokenId(), token1
/* 571 */             .getStartMark());
/*     */       } 
/* 573 */       Token token = ParserImpl.this.scanner.getToken();
/* 574 */       MappingEndEvent mappingEndEvent = new MappingEndEvent(token.getStartMark(), token.getEndMark());
/* 575 */       ParserImpl.this.state = (Production)ParserImpl.this.states.pop();
/* 576 */       ParserImpl.this.marks.pop();
/* 577 */       return (Event)mappingEndEvent;
/*     */     } }
/*     */   
/*     */   private class ParseBlockMappingValue implements Production { private ParseBlockMappingValue() {}
/*     */     
/*     */     public Event produce() {
/* 583 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Value })) {
/* 584 */         Token token1 = ParserImpl.this.scanner.getToken();
/* 585 */         if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Key, Token.ID.Value, Token.ID.BlockEnd })) {
/* 586 */           ParserImpl.this.states.push(new ParserImpl.ParseBlockMappingKey());
/* 587 */           return ParserImpl.this.parseBlockNodeOrIndentlessSequence();
/*     */         } 
/* 589 */         ParserImpl.this.state = new ParserImpl.ParseBlockMappingKey();
/* 590 */         return ParserImpl.this.processEmptyScalar(token1.getEndMark());
/*     */       } 
/*     */       
/* 593 */       ParserImpl.this.state = new ParserImpl.ParseBlockMappingKey();
/* 594 */       Token token = ParserImpl.this.scanner.peekToken();
/* 595 */       return ParserImpl.this.processEmptyScalar(token.getStartMark());
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class ParseFlowSequenceFirstEntry
/*     */     implements Production
/*     */   {
/*     */     private ParseFlowSequenceFirstEntry() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Event produce() {
/* 614 */       Token token = ParserImpl.this.scanner.getToken();
/* 615 */       ParserImpl.this.marks.push(token.getStartMark());
/* 616 */       return (new ParserImpl.ParseFlowSequenceEntry(true)).produce();
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseFlowSequenceEntry implements Production {
/*     */     private boolean first = false;
/*     */     
/*     */     public ParseFlowSequenceEntry(boolean first) {
/* 624 */       this.first = first;
/*     */     }
/*     */     
/*     */     public Event produce() {
/* 628 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowSequenceEnd })) {
/* 629 */         if (!this.first) {
/* 630 */           if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowEntry })) {
/* 631 */             ParserImpl.this.scanner.getToken();
/*     */           } else {
/* 633 */             Token token1 = ParserImpl.this.scanner.peekToken();
/* 634 */             throw new ParserException("while parsing a flow sequence", (Mark)ParserImpl.this.marks.pop(), "expected ',' or ']', but got " + token1
/* 635 */                 .getTokenId(), token1
/* 636 */                 .getStartMark());
/*     */           } 
/*     */         }
/* 639 */         if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Key })) {
/* 640 */           Token token1 = ParserImpl.this.scanner.peekToken();
/*     */           
/* 642 */           MappingStartEvent mappingStartEvent = new MappingStartEvent(null, null, true, token1.getStartMark(), token1.getEndMark(), Boolean.TRUE);
/* 643 */           ParserImpl.this.state = new ParserImpl.ParseFlowSequenceEntryMappingKey();
/* 644 */           return (Event)mappingStartEvent;
/* 645 */         }  if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowSequenceEnd })) {
/* 646 */           ParserImpl.this.states.push(new ParseFlowSequenceEntry(false));
/* 647 */           return ParserImpl.this.parseFlowNode();
/*     */         } 
/*     */       } 
/* 650 */       Token token = ParserImpl.this.scanner.getToken();
/* 651 */       SequenceEndEvent sequenceEndEvent = new SequenceEndEvent(token.getStartMark(), token.getEndMark());
/* 652 */       ParserImpl.this.state = (Production)ParserImpl.this.states.pop();
/* 653 */       ParserImpl.this.marks.pop();
/* 654 */       return (Event)sequenceEndEvent;
/*     */     } }
/*     */   
/*     */   private class ParseFlowSequenceEntryMappingKey implements Production { private ParseFlowSequenceEntryMappingKey() {}
/*     */     
/*     */     public Event produce() {
/* 660 */       Token token = ParserImpl.this.scanner.getToken();
/* 661 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Value, Token.ID.FlowEntry, Token.ID.FlowSequenceEnd })) {
/* 662 */         ParserImpl.this.states.push(new ParserImpl.ParseFlowSequenceEntryMappingValue());
/* 663 */         return ParserImpl.this.parseFlowNode();
/*     */       } 
/* 665 */       ParserImpl.this.state = new ParserImpl.ParseFlowSequenceEntryMappingValue();
/* 666 */       return ParserImpl.this.processEmptyScalar(token.getEndMark());
/*     */     } }
/*     */ 
/*     */   
/*     */   private class ParseFlowSequenceEntryMappingValue implements Production { private ParseFlowSequenceEntryMappingValue() {}
/*     */     
/*     */     public Event produce() {
/* 673 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Value })) {
/* 674 */         Token token1 = ParserImpl.this.scanner.getToken();
/* 675 */         if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowEntry, Token.ID.FlowSequenceEnd })) {
/* 676 */           ParserImpl.this.states.push(new ParserImpl.ParseFlowSequenceEntryMappingEnd());
/* 677 */           return ParserImpl.this.parseFlowNode();
/*     */         } 
/* 679 */         ParserImpl.this.state = new ParserImpl.ParseFlowSequenceEntryMappingEnd();
/* 680 */         return ParserImpl.this.processEmptyScalar(token1.getEndMark());
/*     */       } 
/*     */       
/* 683 */       ParserImpl.this.state = new ParserImpl.ParseFlowSequenceEntryMappingEnd();
/* 684 */       Token token = ParserImpl.this.scanner.peekToken();
/* 685 */       return ParserImpl.this.processEmptyScalar(token.getStartMark());
/*     */     } }
/*     */   
/*     */   private class ParseFlowSequenceEntryMappingEnd implements Production {
/*     */     private ParseFlowSequenceEntryMappingEnd() {}
/*     */     
/*     */     public Event produce() {
/* 692 */       ParserImpl.this.state = new ParserImpl.ParseFlowSequenceEntry(false);
/* 693 */       Token token = ParserImpl.this.scanner.peekToken();
/* 694 */       return (Event)new MappingEndEvent(token.getStartMark(), token.getEndMark());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class ParseFlowMappingFirstKey
/*     */     implements Production
/*     */   {
/*     */     private ParseFlowMappingFirstKey() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public Event produce() {
/* 709 */       Token token = ParserImpl.this.scanner.getToken();
/* 710 */       ParserImpl.this.marks.push(token.getStartMark());
/* 711 */       return (new ParserImpl.ParseFlowMappingKey(true)).produce();
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseFlowMappingKey implements Production {
/*     */     private boolean first = false;
/*     */     
/*     */     public ParseFlowMappingKey(boolean first) {
/* 719 */       this.first = first;
/*     */     }
/*     */     
/*     */     public Event produce() {
/* 723 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowMappingEnd })) {
/* 724 */         if (!this.first) {
/* 725 */           if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowEntry })) {
/* 726 */             ParserImpl.this.scanner.getToken();
/*     */           } else {
/* 728 */             Token token1 = ParserImpl.this.scanner.peekToken();
/* 729 */             throw new ParserException("while parsing a flow mapping", (Mark)ParserImpl.this.marks.pop(), "expected ',' or '}', but got " + token1
/* 730 */                 .getTokenId(), token1
/* 731 */                 .getStartMark());
/*     */           } 
/*     */         }
/* 734 */         if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Key })) {
/* 735 */           Token token1 = ParserImpl.this.scanner.getToken();
/* 736 */           if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Value, Token.ID.FlowEntry, Token.ID.FlowMappingEnd })) {
/*     */             
/* 738 */             ParserImpl.this.states.push(new ParserImpl.ParseFlowMappingValue());
/* 739 */             return ParserImpl.this.parseFlowNode();
/*     */           } 
/* 741 */           ParserImpl.this.state = new ParserImpl.ParseFlowMappingValue();
/* 742 */           return ParserImpl.this.processEmptyScalar(token1.getEndMark());
/*     */         } 
/* 744 */         if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowMappingEnd })) {
/* 745 */           ParserImpl.this.states.push(new ParserImpl.ParseFlowMappingEmptyValue());
/* 746 */           return ParserImpl.this.parseFlowNode();
/*     */         } 
/*     */       } 
/* 749 */       Token token = ParserImpl.this.scanner.getToken();
/* 750 */       MappingEndEvent mappingEndEvent = new MappingEndEvent(token.getStartMark(), token.getEndMark());
/* 751 */       ParserImpl.this.state = (Production)ParserImpl.this.states.pop();
/* 752 */       ParserImpl.this.marks.pop();
/* 753 */       return (Event)mappingEndEvent;
/*     */     } }
/*     */   
/*     */   private class ParseFlowMappingValue implements Production { private ParseFlowMappingValue() {}
/*     */     
/*     */     public Event produce() {
/* 759 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Value })) {
/* 760 */         Token token1 = ParserImpl.this.scanner.getToken();
/* 761 */         if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowEntry, Token.ID.FlowMappingEnd })) {
/* 762 */           ParserImpl.this.states.push(new ParserImpl.ParseFlowMappingKey(false));
/* 763 */           return ParserImpl.this.parseFlowNode();
/*     */         } 
/* 765 */         ParserImpl.this.state = new ParserImpl.ParseFlowMappingKey(false);
/* 766 */         return ParserImpl.this.processEmptyScalar(token1.getEndMark());
/*     */       } 
/*     */       
/* 769 */       ParserImpl.this.state = new ParserImpl.ParseFlowMappingKey(false);
/* 770 */       Token token = ParserImpl.this.scanner.peekToken();
/* 771 */       return ParserImpl.this.processEmptyScalar(token.getStartMark());
/*     */     } }
/*     */   
/*     */   private class ParseFlowMappingEmptyValue implements Production {
/*     */     private ParseFlowMappingEmptyValue() {}
/*     */     
/*     */     public Event produce() {
/* 778 */       ParserImpl.this.state = new ParserImpl.ParseFlowMappingKey(false);
/* 779 */       return ParserImpl.this.processEmptyScalar(ParserImpl.this.scanner.peekToken().getStartMark());
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
/*     */   private Event processEmptyScalar(Mark mark) {
/* 792 */     return (Event)new ScalarEvent(null, null, new ImplicitTuple(true, false), "", mark, mark, Character.valueOf(false));
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\yaml\snakeyaml\parser\ParserImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */