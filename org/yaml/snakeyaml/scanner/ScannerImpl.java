/*      */ package org.yaml.snakeyaml.scanner;
/*      */ 
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.charset.CharacterCodingException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.regex.Pattern;
/*      */ import org.yaml.snakeyaml.error.Mark;
/*      */ import org.yaml.snakeyaml.error.YAMLException;
/*      */ import org.yaml.snakeyaml.reader.StreamReader;
/*      */ import org.yaml.snakeyaml.tokens.AliasToken;
/*      */ import org.yaml.snakeyaml.tokens.AnchorToken;
/*      */ import org.yaml.snakeyaml.tokens.BlockEndToken;
/*      */ import org.yaml.snakeyaml.tokens.BlockEntryToken;
/*      */ import org.yaml.snakeyaml.tokens.BlockMappingStartToken;
/*      */ import org.yaml.snakeyaml.tokens.BlockSequenceStartToken;
/*      */ import org.yaml.snakeyaml.tokens.DirectiveToken;
/*      */ import org.yaml.snakeyaml.tokens.DocumentEndToken;
/*      */ import org.yaml.snakeyaml.tokens.DocumentStartToken;
/*      */ import org.yaml.snakeyaml.tokens.FlowEntryToken;
/*      */ import org.yaml.snakeyaml.tokens.FlowMappingEndToken;
/*      */ import org.yaml.snakeyaml.tokens.FlowMappingStartToken;
/*      */ import org.yaml.snakeyaml.tokens.FlowSequenceEndToken;
/*      */ import org.yaml.snakeyaml.tokens.FlowSequenceStartToken;
/*      */ import org.yaml.snakeyaml.tokens.KeyToken;
/*      */ import org.yaml.snakeyaml.tokens.ScalarToken;
/*      */ import org.yaml.snakeyaml.tokens.StreamEndToken;
/*      */ import org.yaml.snakeyaml.tokens.StreamStartToken;
/*      */ import org.yaml.snakeyaml.tokens.TagToken;
/*      */ import org.yaml.snakeyaml.tokens.TagTuple;
/*      */ import org.yaml.snakeyaml.tokens.Token;
/*      */ import org.yaml.snakeyaml.tokens.ValueToken;
/*      */ import org.yaml.snakeyaml.util.ArrayStack;
/*      */ import org.yaml.snakeyaml.util.UriEncoder;
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
/*      */ public final class ScannerImpl
/*      */   implements Scanner
/*      */ {
/*   87 */   private static final Pattern NOT_HEXA = Pattern.compile("[^0-9A-Fa-f]");
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
/*   98 */   public static final Map<Character, String> ESCAPE_REPLACEMENTS = new HashMap<Character, String>();
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
/*  114 */   public static final Map<Character, Integer> ESCAPE_CODES = new HashMap<Character, Integer>();
/*      */   private final StreamReader reader;
/*      */   
/*      */   static {
/*  118 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('0'), "\000");
/*      */     
/*  120 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('a'), "\007");
/*      */     
/*  122 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('b'), "\b");
/*      */     
/*  124 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('t'), "\t");
/*      */     
/*  126 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('n'), "\n");
/*      */     
/*  128 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('v'), "\013");
/*      */     
/*  130 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('f'), "\f");
/*      */     
/*  132 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('r'), "\r");
/*      */     
/*  134 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('e'), "\033");
/*      */     
/*  136 */     ESCAPE_REPLACEMENTS.put(Character.valueOf(' '), " ");
/*      */     
/*  138 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('"'), "\"");
/*      */     
/*  140 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\\'), "\\");
/*      */     
/*  142 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('N'), "");
/*      */     
/*  144 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('_'), " ");
/*      */     
/*  146 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('L'), " ");
/*      */     
/*  148 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('P'), " ");
/*      */ 
/*      */     
/*  151 */     ESCAPE_CODES.put(Character.valueOf('x'), Integer.valueOf(2));
/*      */     
/*  153 */     ESCAPE_CODES.put(Character.valueOf('u'), Integer.valueOf(4));
/*      */     
/*  155 */     ESCAPE_CODES.put(Character.valueOf('U'), Integer.valueOf(8));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean done = false;
/*      */ 
/*      */   
/*  163 */   private int flowLevel = 0;
/*      */ 
/*      */   
/*      */   private List<Token> tokens;
/*      */ 
/*      */   
/*  169 */   private int tokensTaken = 0;
/*      */ 
/*      */   
/*  172 */   private int indent = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ArrayStack<Integer> indents;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean allowSimpleKey = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Map<Integer, SimpleKey> possibleSimpleKeys;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ScannerImpl(StreamReader reader) {
/*  213 */     this.reader = reader;
/*  214 */     this.tokens = new ArrayList<Token>(100);
/*  215 */     this.indents = new ArrayStack(10);
/*      */     
/*  217 */     this.possibleSimpleKeys = new LinkedHashMap<Integer, SimpleKey>();
/*  218 */     fetchStreamStart();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean checkToken(Token.ID... choices) {
/*  225 */     while (needMoreTokens()) {
/*  226 */       fetchMoreTokens();
/*      */     }
/*  228 */     if (!this.tokens.isEmpty()) {
/*  229 */       if (choices.length == 0) {
/*  230 */         return true;
/*      */       }
/*      */ 
/*      */       
/*  234 */       Token.ID first = ((Token)this.tokens.get(0)).getTokenId();
/*  235 */       for (int i = 0; i < choices.length; i++) {
/*  236 */         if (first == choices[i]) {
/*  237 */           return true;
/*      */         }
/*      */       } 
/*      */     } 
/*  241 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Token peekToken() {
/*  248 */     while (needMoreTokens()) {
/*  249 */       fetchMoreTokens();
/*      */     }
/*  251 */     return this.tokens.get(0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Token getToken() {
/*  258 */     if (!this.tokens.isEmpty()) {
/*  259 */       this.tokensTaken++;
/*  260 */       return this.tokens.remove(0);
/*      */     } 
/*  262 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean needMoreTokens() {
/*  271 */     if (this.done) {
/*  272 */       return false;
/*      */     }
/*      */     
/*  275 */     if (this.tokens.isEmpty()) {
/*  276 */       return true;
/*      */     }
/*      */ 
/*      */     
/*  280 */     stalePossibleSimpleKeys();
/*  281 */     return (nextPossibleSimpleKey() == this.tokensTaken);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchMoreTokens() {
/*  289 */     scanToNextToken();
/*      */     
/*  291 */     stalePossibleSimpleKeys();
/*      */ 
/*      */     
/*  294 */     unwindIndent(this.reader.getColumn());
/*      */ 
/*      */     
/*  297 */     int c = this.reader.peek();
/*  298 */     switch (c) {
/*      */       
/*      */       case 0:
/*  301 */         fetchStreamEnd();
/*      */         return;
/*      */       
/*      */       case 37:
/*  305 */         if (checkDirective()) {
/*  306 */           fetchDirective();
/*      */           return;
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 45:
/*  312 */         if (checkDocumentStart()) {
/*  313 */           fetchDocumentStart();
/*      */           return;
/*      */         } 
/*  316 */         if (checkBlockEntry()) {
/*  317 */           fetchBlockEntry();
/*      */           return;
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 46:
/*  323 */         if (checkDocumentEnd()) {
/*  324 */           fetchDocumentEnd();
/*      */           return;
/*      */         } 
/*      */         break;
/*      */ 
/*      */       
/*      */       case 91:
/*  331 */         fetchFlowSequenceStart();
/*      */         return;
/*      */       
/*      */       case 123:
/*  335 */         fetchFlowMappingStart();
/*      */         return;
/*      */       
/*      */       case 93:
/*  339 */         fetchFlowSequenceEnd();
/*      */         return;
/*      */       
/*      */       case 125:
/*  343 */         fetchFlowMappingEnd();
/*      */         return;
/*      */       
/*      */       case 44:
/*  347 */         fetchFlowEntry();
/*      */         return;
/*      */ 
/*      */       
/*      */       case 63:
/*  352 */         if (checkKey()) {
/*  353 */           fetchKey();
/*      */           return;
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 58:
/*  359 */         if (checkValue()) {
/*  360 */           fetchValue();
/*      */           return;
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 42:
/*  366 */         fetchAlias();
/*      */         return;
/*      */       
/*      */       case 38:
/*  370 */         fetchAnchor();
/*      */         return;
/*      */       
/*      */       case 33:
/*  374 */         fetchTag();
/*      */         return;
/*      */       
/*      */       case 124:
/*  378 */         if (this.flowLevel == 0) {
/*  379 */           fetchLiteral();
/*      */           return;
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 62:
/*  385 */         if (this.flowLevel == 0) {
/*  386 */           fetchFolded();
/*      */           return;
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 39:
/*  392 */         fetchSingle();
/*      */         return;
/*      */       
/*      */       case 34:
/*  396 */         fetchDouble();
/*      */         return;
/*      */     } 
/*      */     
/*  400 */     if (checkPlain()) {
/*  401 */       fetchPlain();
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*  407 */     String chRepresentation = String.valueOf(Character.toChars(c));
/*  408 */     for (Character s : ESCAPE_REPLACEMENTS.keySet()) {
/*  409 */       String v = ESCAPE_REPLACEMENTS.get(s);
/*  410 */       if (v.equals(chRepresentation)) {
/*  411 */         chRepresentation = "\\" + s;
/*      */         break;
/*      */       } 
/*      */     } 
/*  415 */     if (c == 9) {
/*  416 */       chRepresentation = chRepresentation + "(TAB)";
/*      */     }
/*  418 */     String text = String.format("found character '%s' that cannot start any token. (Do not use %s for indentation)", new Object[] { chRepresentation, chRepresentation });
/*      */     
/*  420 */     throw new ScannerException("while scanning for the next token", null, text, this.reader
/*  421 */         .getMark());
/*      */   }
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
/*      */   private int nextPossibleSimpleKey() {
/*  435 */     if (!this.possibleSimpleKeys.isEmpty()) {
/*  436 */       return ((SimpleKey)this.possibleSimpleKeys.values().iterator().next()).getTokenNumber();
/*      */     }
/*  438 */     return -1;
/*      */   }
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
/*      */   private void stalePossibleSimpleKeys() {
/*  452 */     if (!this.possibleSimpleKeys.isEmpty()) {
/*  453 */       Iterator<SimpleKey> iterator = this.possibleSimpleKeys.values().iterator();
/*  454 */       while (iterator.hasNext()) {
/*  455 */         SimpleKey key = iterator.next();
/*  456 */         if (key.getLine() != this.reader.getLine() || this.reader
/*  457 */           .getIndex() - key.getIndex() > 1024) {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  462 */           if (key.isRequired())
/*      */           {
/*      */             
/*  465 */             throw new ScannerException("while scanning a simple key", key.getMark(), "could not find expected ':'", this.reader
/*  466 */                 .getMark());
/*      */           }
/*  468 */           iterator.remove();
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
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
/*      */   private void savePossibleSimpleKey() {
/*  487 */     boolean required = (this.flowLevel == 0 && this.indent == this.reader.getColumn());
/*      */     
/*  489 */     if (this.allowSimpleKey || !required) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  499 */       if (this.allowSimpleKey) {
/*  500 */         removePossibleSimpleKey();
/*  501 */         int tokenNumber = this.tokensTaken + this.tokens.size();
/*      */         
/*  503 */         SimpleKey key = new SimpleKey(tokenNumber, required, this.reader.getIndex(), this.reader.getLine(), this.reader.getColumn(), this.reader.getMark());
/*  504 */         this.possibleSimpleKeys.put(Integer.valueOf(this.flowLevel), key);
/*      */       } 
/*      */       return;
/*      */     } 
/*      */     throw new YAMLException("A simple key is required only if it is the first token in the current line");
/*      */   }
/*      */   
/*      */   private void removePossibleSimpleKey() {
/*  512 */     SimpleKey key = this.possibleSimpleKeys.remove(Integer.valueOf(this.flowLevel));
/*  513 */     if (key != null && key.isRequired()) {
/*  514 */       throw new ScannerException("while scanning a simple key", key.getMark(), "could not find expected ':'", this.reader
/*  515 */           .getMark());
/*      */     }
/*      */   }
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
/*      */   private void unwindIndent(int col) {
/*  544 */     if (this.flowLevel != 0) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  549 */     while (this.indent > col) {
/*  550 */       Mark mark = this.reader.getMark();
/*  551 */       this.indent = ((Integer)this.indents.pop()).intValue();
/*  552 */       this.tokens.add(new BlockEndToken(mark, mark));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean addIndent(int column) {
/*  560 */     if (this.indent < column) {
/*  561 */       this.indents.push(Integer.valueOf(this.indent));
/*  562 */       this.indent = column;
/*  563 */       return true;
/*      */     } 
/*  565 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchStreamStart() {
/*  576 */     Mark mark = this.reader.getMark();
/*      */ 
/*      */     
/*  579 */     StreamStartToken streamStartToken = new StreamStartToken(mark, mark);
/*  580 */     this.tokens.add(streamStartToken);
/*      */   }
/*      */ 
/*      */   
/*      */   private void fetchStreamEnd() {
/*  585 */     unwindIndent(-1);
/*      */ 
/*      */     
/*  588 */     removePossibleSimpleKey();
/*  589 */     this.allowSimpleKey = false;
/*  590 */     this.possibleSimpleKeys.clear();
/*      */ 
/*      */     
/*  593 */     Mark mark = this.reader.getMark();
/*      */ 
/*      */     
/*  596 */     StreamEndToken streamEndToken = new StreamEndToken(mark, mark);
/*  597 */     this.tokens.add(streamEndToken);
/*      */ 
/*      */     
/*  600 */     this.done = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchDirective() {
/*  612 */     unwindIndent(-1);
/*      */ 
/*      */     
/*  615 */     removePossibleSimpleKey();
/*  616 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/*  619 */     Token tok = scanDirective();
/*  620 */     this.tokens.add(tok);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchDocumentStart() {
/*  627 */     fetchDocumentIndicator(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchDocumentEnd() {
/*  634 */     fetchDocumentIndicator(false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchDocumentIndicator(boolean isDocumentStart) {
/*      */     DocumentEndToken documentEndToken;
/*  643 */     unwindIndent(-1);
/*      */ 
/*      */ 
/*      */     
/*  647 */     removePossibleSimpleKey();
/*  648 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/*  651 */     Mark startMark = this.reader.getMark();
/*  652 */     this.reader.forward(3);
/*  653 */     Mark endMark = this.reader.getMark();
/*      */     
/*  655 */     if (isDocumentStart) {
/*  656 */       DocumentStartToken documentStartToken = new DocumentStartToken(startMark, endMark);
/*      */     } else {
/*  658 */       documentEndToken = new DocumentEndToken(startMark, endMark);
/*      */     } 
/*  660 */     this.tokens.add(documentEndToken);
/*      */   }
/*      */   
/*      */   private void fetchFlowSequenceStart() {
/*  664 */     fetchFlowCollectionStart(false);
/*      */   }
/*      */   
/*      */   private void fetchFlowMappingStart() {
/*  668 */     fetchFlowCollectionStart(true);
/*      */   }
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
/*      */   private void fetchFlowCollectionStart(boolean isMappingStart) {
/*      */     FlowSequenceStartToken flowSequenceStartToken;
/*  685 */     savePossibleSimpleKey();
/*      */ 
/*      */     
/*  688 */     this.flowLevel++;
/*      */ 
/*      */     
/*  691 */     this.allowSimpleKey = true;
/*      */ 
/*      */     
/*  694 */     Mark startMark = this.reader.getMark();
/*  695 */     this.reader.forward(1);
/*  696 */     Mark endMark = this.reader.getMark();
/*      */     
/*  698 */     if (isMappingStart) {
/*  699 */       FlowMappingStartToken flowMappingStartToken = new FlowMappingStartToken(startMark, endMark);
/*      */     } else {
/*  701 */       flowSequenceStartToken = new FlowSequenceStartToken(startMark, endMark);
/*      */     } 
/*  703 */     this.tokens.add(flowSequenceStartToken);
/*      */   }
/*      */   
/*      */   private void fetchFlowSequenceEnd() {
/*  707 */     fetchFlowCollectionEnd(false);
/*      */   }
/*      */   
/*      */   private void fetchFlowMappingEnd() {
/*  711 */     fetchFlowCollectionEnd(true);
/*      */   }
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
/*      */   private void fetchFlowCollectionEnd(boolean isMappingEnd) {
/*      */     FlowSequenceEndToken flowSequenceEndToken;
/*  726 */     removePossibleSimpleKey();
/*      */ 
/*      */     
/*  729 */     this.flowLevel--;
/*      */ 
/*      */     
/*  732 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/*  735 */     Mark startMark = this.reader.getMark();
/*  736 */     this.reader.forward();
/*  737 */     Mark endMark = this.reader.getMark();
/*      */     
/*  739 */     if (isMappingEnd) {
/*  740 */       FlowMappingEndToken flowMappingEndToken = new FlowMappingEndToken(startMark, endMark);
/*      */     } else {
/*  742 */       flowSequenceEndToken = new FlowSequenceEndToken(startMark, endMark);
/*      */     } 
/*  744 */     this.tokens.add(flowSequenceEndToken);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchFlowEntry() {
/*  755 */     this.allowSimpleKey = true;
/*      */ 
/*      */     
/*  758 */     removePossibleSimpleKey();
/*      */ 
/*      */     
/*  761 */     Mark startMark = this.reader.getMark();
/*  762 */     this.reader.forward();
/*  763 */     Mark endMark = this.reader.getMark();
/*  764 */     FlowEntryToken flowEntryToken = new FlowEntryToken(startMark, endMark);
/*  765 */     this.tokens.add(flowEntryToken);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchBlockEntry() {
/*  775 */     if (this.flowLevel == 0) {
/*      */       
/*  777 */       if (!this.allowSimpleKey) {
/*  778 */         throw new ScannerException(null, null, "sequence entries are not allowed here", this.reader
/*  779 */             .getMark());
/*      */       }
/*      */ 
/*      */       
/*  783 */       if (addIndent(this.reader.getColumn())) {
/*  784 */         Mark mark = this.reader.getMark();
/*  785 */         this.tokens.add(new BlockSequenceStartToken(mark, mark));
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  792 */     this.allowSimpleKey = true;
/*      */ 
/*      */     
/*  795 */     removePossibleSimpleKey();
/*      */ 
/*      */     
/*  798 */     Mark startMark = this.reader.getMark();
/*  799 */     this.reader.forward();
/*  800 */     Mark endMark = this.reader.getMark();
/*  801 */     BlockEntryToken blockEntryToken = new BlockEntryToken(startMark, endMark);
/*  802 */     this.tokens.add(blockEntryToken);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchKey() {
/*  812 */     if (this.flowLevel == 0) {
/*      */       
/*  814 */       if (!this.allowSimpleKey) {
/*  815 */         throw new ScannerException(null, null, "mapping keys are not allowed here", this.reader
/*  816 */             .getMark());
/*      */       }
/*      */       
/*  819 */       if (addIndent(this.reader.getColumn())) {
/*  820 */         Mark mark = this.reader.getMark();
/*  821 */         this.tokens.add(new BlockMappingStartToken(mark, mark));
/*      */       } 
/*      */     } 
/*      */     
/*  825 */     this.allowSimpleKey = (this.flowLevel == 0);
/*      */ 
/*      */     
/*  828 */     removePossibleSimpleKey();
/*      */ 
/*      */     
/*  831 */     Mark startMark = this.reader.getMark();
/*  832 */     this.reader.forward();
/*  833 */     Mark endMark = this.reader.getMark();
/*  834 */     KeyToken keyToken = new KeyToken(startMark, endMark);
/*  835 */     this.tokens.add(keyToken);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchValue() {
/*  845 */     SimpleKey key = this.possibleSimpleKeys.remove(Integer.valueOf(this.flowLevel));
/*  846 */     if (key != null) {
/*      */       
/*  848 */       this.tokens.add(key.getTokenNumber() - this.tokensTaken, new KeyToken(key.getMark(), key
/*  849 */             .getMark()));
/*      */ 
/*      */ 
/*      */       
/*  853 */       if (this.flowLevel == 0 && 
/*  854 */         addIndent(key.getColumn())) {
/*  855 */         this.tokens.add(key.getTokenNumber() - this.tokensTaken, new BlockMappingStartToken(key
/*  856 */               .getMark(), key.getMark()));
/*      */       }
/*      */ 
/*      */       
/*  860 */       this.allowSimpleKey = false;
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/*  866 */       if (this.flowLevel == 0)
/*      */       {
/*      */ 
/*      */         
/*  870 */         if (!this.allowSimpleKey) {
/*  871 */           throw new ScannerException(null, null, "mapping values are not allowed here", this.reader
/*  872 */               .getMark());
/*      */         }
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  879 */       if (this.flowLevel == 0 && 
/*  880 */         addIndent(this.reader.getColumn())) {
/*  881 */         Mark mark = this.reader.getMark();
/*  882 */         this.tokens.add(new BlockMappingStartToken(mark, mark));
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  887 */       this.allowSimpleKey = (this.flowLevel == 0);
/*      */ 
/*      */       
/*  890 */       removePossibleSimpleKey();
/*      */     } 
/*      */     
/*  893 */     Mark startMark = this.reader.getMark();
/*  894 */     this.reader.forward();
/*  895 */     Mark endMark = this.reader.getMark();
/*  896 */     ValueToken valueToken = new ValueToken(startMark, endMark);
/*  897 */     this.tokens.add(valueToken);
/*      */   }
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
/*      */   private void fetchAlias() {
/*  912 */     savePossibleSimpleKey();
/*      */ 
/*      */     
/*  915 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/*  918 */     Token tok = scanAnchor(false);
/*  919 */     this.tokens.add(tok);
/*      */   }
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
/*      */   private void fetchAnchor() {
/*  933 */     savePossibleSimpleKey();
/*      */ 
/*      */     
/*  936 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/*  939 */     Token tok = scanAnchor(true);
/*  940 */     this.tokens.add(tok);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchTag() {
/*  950 */     savePossibleSimpleKey();
/*      */ 
/*      */     
/*  953 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/*  956 */     Token tok = scanTag();
/*  957 */     this.tokens.add(tok);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchLiteral() {
/*  968 */     fetchBlockScalar('|');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchFolded() {
/*  978 */     fetchBlockScalar('>');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchBlockScalar(char style) {
/*  990 */     this.allowSimpleKey = true;
/*      */ 
/*      */     
/*  993 */     removePossibleSimpleKey();
/*      */ 
/*      */     
/*  996 */     Token tok = scanBlockScalar(style);
/*  997 */     this.tokens.add(tok);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchSingle() {
/* 1004 */     fetchFlowScalar('\'');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchDouble() {
/* 1011 */     fetchFlowScalar('"');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchFlowScalar(char style) {
/* 1023 */     savePossibleSimpleKey();
/*      */ 
/*      */     
/* 1026 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/* 1029 */     Token tok = scanFlowScalar(style);
/* 1030 */     this.tokens.add(tok);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchPlain() {
/* 1038 */     savePossibleSimpleKey();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1043 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/* 1046 */     Token tok = scanPlain();
/* 1047 */     this.tokens.add(tok);
/*      */   }
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
/*      */   private boolean checkDirective() {
/* 1060 */     return (this.reader.getColumn() == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkDocumentStart() {
/* 1069 */     if (this.reader.getColumn() == 0 && 
/* 1070 */       "---".equals(this.reader.prefix(3)) && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(3))) {
/* 1071 */       return true;
/*      */     }
/*      */     
/* 1074 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkDocumentEnd() {
/* 1083 */     if (this.reader.getColumn() == 0 && 
/* 1084 */       "...".equals(this.reader.prefix(3)) && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(3))) {
/* 1085 */       return true;
/*      */     }
/*      */     
/* 1088 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkBlockEntry() {
/* 1096 */     return Constant.NULL_BL_T_LINEBR.has(this.reader.peek(1));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkKey() {
/* 1104 */     if (this.flowLevel != 0) {
/* 1105 */       return true;
/*      */     }
/*      */     
/* 1108 */     return Constant.NULL_BL_T_LINEBR.has(this.reader.peek(1));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkValue() {
/* 1117 */     if (this.flowLevel != 0) {
/* 1118 */       return true;
/*      */     }
/*      */     
/* 1121 */     return Constant.NULL_BL_T_LINEBR.has(this.reader.peek(1));
/*      */   }
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
/*      */   private boolean checkPlain() {
/* 1145 */     int c = this.reader.peek();
/*      */ 
/*      */     
/* 1148 */     return (Constant.NULL_BL_T_LINEBR.hasNo(c, "-?:,[]{}#&*!|>'\"%@`") || (Constant.NULL_BL_T_LINEBR
/* 1149 */       .hasNo(this.reader.peek(1)) && (c == 45 || (this.flowLevel == 0 && "?:"
/* 1150 */       .indexOf(c) != -1))));
/*      */   }
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
/*      */   private void scanToNextToken() {
/* 1179 */     if (this.reader.getIndex() == 0 && this.reader.peek() == 65279) {
/* 1180 */       this.reader.forward();
/*      */     }
/* 1182 */     boolean found = false;
/* 1183 */     while (!found) {
/* 1184 */       int ff = 0;
/*      */ 
/*      */       
/* 1187 */       while (this.reader.peek(ff) == 32) {
/* 1188 */         ff++;
/*      */       }
/* 1190 */       if (ff > 0) {
/* 1191 */         this.reader.forward(ff);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1197 */       if (this.reader.peek() == 35) {
/* 1198 */         ff = 0;
/* 1199 */         while (Constant.NULL_OR_LINEBR.hasNo(this.reader.peek(ff))) {
/* 1200 */           ff++;
/*      */         }
/* 1202 */         if (ff > 0) {
/* 1203 */           this.reader.forward(ff);
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/* 1208 */       if (scanLineBreak().length() != 0) {
/* 1209 */         if (this.flowLevel == 0)
/*      */         {
/*      */           
/* 1212 */           this.allowSimpleKey = true; } 
/*      */         continue;
/*      */       } 
/* 1215 */       found = true;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Token scanDirective() {
/* 1223 */     Mark endMark, startMark = this.reader.getMark();
/*      */     
/* 1225 */     this.reader.forward();
/* 1226 */     String name = scanDirectiveName(startMark);
/* 1227 */     List<?> value = null;
/* 1228 */     if ("YAML".equals(name)) {
/* 1229 */       value = scanYamlDirectiveValue(startMark);
/* 1230 */       endMark = this.reader.getMark();
/* 1231 */     } else if ("TAG".equals(name)) {
/* 1232 */       value = scanTagDirectiveValue(startMark);
/* 1233 */       endMark = this.reader.getMark();
/*      */     } else {
/* 1235 */       endMark = this.reader.getMark();
/* 1236 */       int ff = 0;
/* 1237 */       while (Constant.NULL_OR_LINEBR.hasNo(this.reader.peek(ff))) {
/* 1238 */         ff++;
/*      */       }
/* 1240 */       if (ff > 0) {
/* 1241 */         this.reader.forward(ff);
/*      */       }
/*      */     } 
/* 1244 */     scanDirectiveIgnoredLine(startMark);
/* 1245 */     return (Token)new DirectiveToken(name, value, startMark, endMark);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String scanDirectiveName(Mark startMark) {
/* 1256 */     int length = 0;
/*      */ 
/*      */ 
/*      */     
/* 1260 */     int c = this.reader.peek(length);
/* 1261 */     while (Constant.ALPHA.has(c)) {
/* 1262 */       length++;
/* 1263 */       c = this.reader.peek(length);
/*      */     } 
/*      */     
/* 1266 */     if (length == 0) {
/* 1267 */       String s = String.valueOf(Character.toChars(c));
/* 1268 */       throw new ScannerException("while scanning a directive", startMark, "expected alphabetic or numeric character, but found " + s + "(" + c + ")", this.reader
/*      */           
/* 1270 */           .getMark());
/*      */     } 
/* 1272 */     String value = this.reader.prefixForward(length);
/* 1273 */     c = this.reader.peek();
/* 1274 */     if (Constant.NULL_BL_LINEBR.hasNo(c)) {
/* 1275 */       String s = String.valueOf(Character.toChars(c));
/* 1276 */       throw new ScannerException("while scanning a directive", startMark, "expected alphabetic or numeric character, but found " + s + "(" + c + ")", this.reader
/*      */           
/* 1278 */           .getMark());
/*      */     } 
/* 1280 */     return value;
/*      */   }
/*      */ 
/*      */   
/*      */   private List<Integer> scanYamlDirectiveValue(Mark startMark) {
/* 1285 */     while (this.reader.peek() == 32) {
/* 1286 */       this.reader.forward();
/*      */     }
/* 1288 */     Integer major = scanYamlDirectiveNumber(startMark);
/* 1289 */     int c = this.reader.peek();
/* 1290 */     if (c != 46) {
/* 1291 */       String s = String.valueOf(Character.toChars(c));
/* 1292 */       throw new ScannerException("while scanning a directive", startMark, "expected a digit or '.', but found " + s + "(" + c + ")", this.reader
/*      */           
/* 1294 */           .getMark());
/*      */     } 
/* 1296 */     this.reader.forward();
/* 1297 */     Integer minor = scanYamlDirectiveNumber(startMark);
/* 1298 */     c = this.reader.peek();
/* 1299 */     if (Constant.NULL_BL_LINEBR.hasNo(c)) {
/* 1300 */       String s = String.valueOf(Character.toChars(c));
/* 1301 */       throw new ScannerException("while scanning a directive", startMark, "expected a digit or ' ', but found " + s + "(" + c + ")", this.reader
/*      */           
/* 1303 */           .getMark());
/*      */     } 
/* 1305 */     List<Integer> result = new ArrayList<Integer>(2);
/* 1306 */     result.add(major);
/* 1307 */     result.add(minor);
/* 1308 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Integer scanYamlDirectiveNumber(Mark startMark) {
/* 1320 */     int c = this.reader.peek();
/* 1321 */     if (!Character.isDigit(c)) {
/* 1322 */       String s = String.valueOf(Character.toChars(c));
/* 1323 */       throw new ScannerException("while scanning a directive", startMark, "expected a digit, but found " + s + "(" + c + ")", this.reader
/* 1324 */           .getMark());
/*      */     } 
/* 1326 */     int length = 0;
/* 1327 */     while (Character.isDigit(this.reader.peek(length))) {
/* 1328 */       length++;
/*      */     }
/* 1330 */     Integer value = Integer.valueOf(Integer.parseInt(this.reader.prefixForward(length)));
/* 1331 */     return value;
/*      */   }
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
/*      */   private List<String> scanTagDirectiveValue(Mark startMark) {
/* 1348 */     while (this.reader.peek() == 32) {
/* 1349 */       this.reader.forward();
/*      */     }
/* 1351 */     String handle = scanTagDirectiveHandle(startMark);
/* 1352 */     while (this.reader.peek() == 32) {
/* 1353 */       this.reader.forward();
/*      */     }
/* 1355 */     String prefix = scanTagDirectivePrefix(startMark);
/* 1356 */     List<String> result = new ArrayList<String>(2);
/* 1357 */     result.add(handle);
/* 1358 */     result.add(prefix);
/* 1359 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String scanTagDirectiveHandle(Mark startMark) {
/* 1371 */     String value = scanTagHandle("directive", startMark);
/* 1372 */     int c = this.reader.peek();
/* 1373 */     if (c != 32) {
/* 1374 */       String s = String.valueOf(Character.toChars(c));
/* 1375 */       throw new ScannerException("while scanning a directive", startMark, "expected ' ', but found " + s + "(" + c + ")", this.reader
/* 1376 */           .getMark());
/*      */     } 
/* 1378 */     return value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String scanTagDirectivePrefix(Mark startMark) {
/* 1388 */     String value = scanTagUri("directive", startMark);
/* 1389 */     int c = this.reader.peek();
/* 1390 */     if (Constant.NULL_BL_LINEBR.hasNo(c)) {
/* 1391 */       String s = String.valueOf(Character.toChars(c));
/* 1392 */       throw new ScannerException("while scanning a directive", startMark, "expected ' ', but found " + s + "(" + c + ")", this.reader
/*      */           
/* 1394 */           .getMark());
/*      */     } 
/* 1396 */     return value;
/*      */   }
/*      */ 
/*      */   
/*      */   private String scanDirectiveIgnoredLine(Mark startMark) {
/* 1401 */     while (this.reader.peek() == 32) {
/* 1402 */       this.reader.forward();
/*      */     }
/* 1404 */     if (this.reader.peek() == 35) {
/* 1405 */       while (Constant.NULL_OR_LINEBR.hasNo(this.reader.peek())) {
/* 1406 */         this.reader.forward();
/*      */       }
/*      */     }
/* 1409 */     int c = this.reader.peek();
/* 1410 */     String lineBreak = scanLineBreak();
/* 1411 */     if (lineBreak.length() == 0 && c != 0) {
/* 1412 */       String s = String.valueOf(Character.toChars(c));
/* 1413 */       throw new ScannerException("while scanning a directive", startMark, "expected a comment or a line break, but found " + s + "(" + c + ")", this.reader
/*      */           
/* 1415 */           .getMark());
/*      */     } 
/* 1417 */     return lineBreak;
/*      */   }
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
/*      */   private Token scanAnchor(boolean isAnchor) {
/*      */     AliasToken aliasToken;
/* 1433 */     Mark startMark = this.reader.getMark();
/* 1434 */     int indicator = this.reader.peek();
/* 1435 */     String name = (indicator == 42) ? "alias" : "anchor";
/* 1436 */     this.reader.forward();
/* 1437 */     int length = 0;
/* 1438 */     int c = this.reader.peek(length);
/* 1439 */     while (Constant.ALPHA.has(c)) {
/* 1440 */       length++;
/* 1441 */       c = this.reader.peek(length);
/*      */     } 
/* 1443 */     if (length == 0) {
/* 1444 */       String s = String.valueOf(Character.toChars(c));
/* 1445 */       throw new ScannerException("while scanning an " + name, startMark, "expected alphabetic or numeric character, but found " + s + "(" + c + ")", this.reader
/*      */           
/* 1447 */           .getMark());
/*      */     } 
/* 1449 */     String value = this.reader.prefixForward(length);
/* 1450 */     c = this.reader.peek();
/* 1451 */     if (Constant.NULL_BL_T_LINEBR.hasNo(c, "?:,]}%@`")) {
/* 1452 */       String s = String.valueOf(Character.toChars(c));
/* 1453 */       throw new ScannerException("while scanning an " + name, startMark, "expected alphabetic or numeric character, but found " + s + "(" + c + ")", this.reader
/*      */           
/* 1455 */           .getMark());
/*      */     } 
/* 1457 */     Mark endMark = this.reader.getMark();
/*      */     
/* 1459 */     if (isAnchor) {
/* 1460 */       AnchorToken anchorToken = new AnchorToken(value, startMark, endMark);
/*      */     } else {
/* 1462 */       aliasToken = new AliasToken(value, startMark, endMark);
/*      */     } 
/* 1464 */     return (Token)aliasToken;
/*      */   }
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
/*      */ 
/*      */   
/*      */   private Token scanTag() {
/* 1502 */     Mark startMark = this.reader.getMark();
/*      */ 
/*      */     
/* 1505 */     int c = this.reader.peek(1);
/* 1506 */     String handle = null;
/* 1507 */     String suffix = null;
/*      */     
/* 1509 */     if (c == 60) {
/*      */ 
/*      */       
/* 1512 */       this.reader.forward(2);
/* 1513 */       suffix = scanTagUri("tag", startMark);
/* 1514 */       c = this.reader.peek();
/* 1515 */       if (c != 62) {
/*      */ 
/*      */         
/* 1518 */         String s = String.valueOf(Character.toChars(c));
/* 1519 */         throw new ScannerException("while scanning a tag", startMark, "expected '>', but found '" + s + "' (" + c + ")", this.reader
/*      */             
/* 1521 */             .getMark());
/*      */       } 
/* 1523 */       this.reader.forward();
/* 1524 */     } else if (Constant.NULL_BL_T_LINEBR.has(c)) {
/*      */ 
/*      */       
/* 1527 */       suffix = "!";
/* 1528 */       this.reader.forward();
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/* 1534 */       int length = 1;
/* 1535 */       boolean useHandle = false;
/* 1536 */       while (Constant.NULL_BL_LINEBR.hasNo(c)) {
/* 1537 */         if (c == 33) {
/* 1538 */           useHandle = true;
/*      */           break;
/*      */         } 
/* 1541 */         length++;
/* 1542 */         c = this.reader.peek(length);
/*      */       } 
/* 1544 */       handle = "!";
/*      */ 
/*      */       
/* 1547 */       if (useHandle) {
/* 1548 */         handle = scanTagHandle("tag", startMark);
/*      */       } else {
/* 1550 */         handle = "!";
/* 1551 */         this.reader.forward();
/*      */       } 
/* 1553 */       suffix = scanTagUri("tag", startMark);
/*      */     } 
/* 1555 */     c = this.reader.peek();
/*      */ 
/*      */     
/* 1558 */     if (Constant.NULL_BL_LINEBR.hasNo(c)) {
/* 1559 */       String s = String.valueOf(Character.toChars(c));
/* 1560 */       throw new ScannerException("while scanning a tag", startMark, "expected ' ', but found '" + s + "' (" + c + ")", this.reader
/* 1561 */           .getMark());
/*      */     } 
/* 1563 */     TagTuple value = new TagTuple(handle, suffix);
/* 1564 */     Mark endMark = this.reader.getMark();
/* 1565 */     return (Token)new TagToken(value, startMark, endMark);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Token scanBlockScalar(char style) {
/*      */     boolean folded;
/*      */     Mark mark1;
/* 1573 */     if (style == '>') {
/* 1574 */       folded = true;
/*      */     } else {
/* 1576 */       folded = false;
/*      */     } 
/* 1578 */     StringBuilder chunks = new StringBuilder();
/* 1579 */     Mark startMark = this.reader.getMark();
/*      */     
/* 1581 */     this.reader.forward();
/* 1582 */     Chomping chompi = scanBlockScalarIndicators(startMark);
/* 1583 */     int increment = chompi.getIncrement();
/* 1584 */     scanBlockScalarIgnoredLine(startMark);
/*      */ 
/*      */     
/* 1587 */     int minIndent = this.indent + 1;
/* 1588 */     if (minIndent < 1) {
/* 1589 */       minIndent = 1;
/*      */     }
/* 1591 */     String breaks = null;
/* 1592 */     int maxIndent = 0;
/* 1593 */     int indent = 0;
/*      */     
/* 1595 */     if (increment == -1) {
/* 1596 */       Object[] brme = scanBlockScalarIndentation();
/* 1597 */       breaks = (String)brme[0];
/* 1598 */       maxIndent = ((Integer)brme[1]).intValue();
/* 1599 */       mark1 = (Mark)brme[2];
/* 1600 */       indent = Math.max(minIndent, maxIndent);
/*      */     } else {
/* 1602 */       indent = minIndent + increment - 1;
/* 1603 */       Object[] brme = scanBlockScalarBreaks(indent);
/* 1604 */       breaks = (String)brme[0];
/* 1605 */       mark1 = (Mark)brme[1];
/*      */     } 
/*      */     
/* 1608 */     String lineBreak = "";
/*      */ 
/*      */     
/* 1611 */     while (this.reader.getColumn() == indent && this.reader.peek() != 0) {
/* 1612 */       chunks.append(breaks);
/* 1613 */       boolean leadingNonSpace = (" \t".indexOf(this.reader.peek()) == -1);
/* 1614 */       int length = 0;
/* 1615 */       while (Constant.NULL_OR_LINEBR.hasNo(this.reader.peek(length))) {
/* 1616 */         length++;
/*      */       }
/* 1618 */       chunks.append(this.reader.prefixForward(length));
/* 1619 */       lineBreak = scanLineBreak();
/* 1620 */       Object[] brme = scanBlockScalarBreaks(indent);
/* 1621 */       breaks = (String)brme[0];
/* 1622 */       mark1 = (Mark)brme[1];
/* 1623 */       if (this.reader.getColumn() == indent && this.reader.peek() != 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1628 */         if (folded && "\n".equals(lineBreak) && leadingNonSpace && " \t"
/* 1629 */           .indexOf(this.reader.peek()) == -1) {
/* 1630 */           if (breaks.length() == 0)
/* 1631 */             chunks.append(" "); 
/*      */           continue;
/*      */         } 
/* 1634 */         chunks.append(lineBreak);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1643 */     if (chompi.chompTailIsNotFalse()) {
/* 1644 */       chunks.append(lineBreak);
/*      */     }
/* 1646 */     if (chompi.chompTailIsTrue()) {
/* 1647 */       chunks.append(breaks);
/*      */     }
/*      */     
/* 1650 */     return (Token)new ScalarToken(chunks.toString(), false, startMark, mark1, style);
/*      */   }
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
/*      */   private Chomping scanBlockScalarIndicators(Mark startMark) {
/* 1670 */     Boolean chomping = null;
/* 1671 */     int increment = -1;
/* 1672 */     int c = this.reader.peek();
/* 1673 */     if (c == 45 || c == 43) {
/* 1674 */       if (c == 43) {
/* 1675 */         chomping = Boolean.TRUE;
/*      */       } else {
/* 1677 */         chomping = Boolean.FALSE;
/*      */       } 
/* 1679 */       this.reader.forward();
/* 1680 */       c = this.reader.peek();
/* 1681 */       if (Character.isDigit(c)) {
/* 1682 */         String s = String.valueOf(Character.toChars(c));
/* 1683 */         increment = Integer.parseInt(s);
/* 1684 */         if (increment == 0) {
/* 1685 */           throw new ScannerException("while scanning a block scalar", startMark, "expected indentation indicator in the range 1-9, but found 0", this.reader
/*      */               
/* 1687 */               .getMark());
/*      */         }
/* 1689 */         this.reader.forward();
/*      */       } 
/* 1691 */     } else if (Character.isDigit(c)) {
/* 1692 */       String s = String.valueOf(Character.toChars(c));
/* 1693 */       increment = Integer.parseInt(s);
/* 1694 */       if (increment == 0) {
/* 1695 */         throw new ScannerException("while scanning a block scalar", startMark, "expected indentation indicator in the range 1-9, but found 0", this.reader
/*      */             
/* 1697 */             .getMark());
/*      */       }
/* 1699 */       this.reader.forward();
/* 1700 */       c = this.reader.peek();
/* 1701 */       if (c == 45 || c == 43) {
/* 1702 */         if (c == 43) {
/* 1703 */           chomping = Boolean.TRUE;
/*      */         } else {
/* 1705 */           chomping = Boolean.FALSE;
/*      */         } 
/* 1707 */         this.reader.forward();
/*      */       } 
/*      */     } 
/* 1710 */     c = this.reader.peek();
/* 1711 */     if (Constant.NULL_BL_LINEBR.hasNo(c)) {
/* 1712 */       String s = String.valueOf(Character.toChars(c));
/* 1713 */       throw new ScannerException("while scanning a block scalar", startMark, "expected chomping or indentation indicators, but found " + s + "(" + c + ")", this.reader
/*      */           
/* 1715 */           .getMark());
/*      */     } 
/* 1717 */     return new Chomping(chomping, increment);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String scanBlockScalarIgnoredLine(Mark startMark) {
/* 1728 */     while (this.reader.peek() == 32) {
/* 1729 */       this.reader.forward();
/*      */     }
/*      */ 
/*      */     
/* 1733 */     if (this.reader.peek() == 35) {
/* 1734 */       while (Constant.NULL_OR_LINEBR.hasNo(this.reader.peek())) {
/* 1735 */         this.reader.forward();
/*      */       }
/*      */     }
/*      */ 
/*      */     
/* 1740 */     int c = this.reader.peek();
/* 1741 */     String lineBreak = scanLineBreak();
/* 1742 */     if (lineBreak.length() == 0 && c != 0) {
/* 1743 */       String s = String.valueOf(Character.toChars(c));
/* 1744 */       throw new ScannerException("while scanning a block scalar", startMark, "expected a comment or a line break, but found " + s + "(" + c + ")", this.reader
/*      */           
/* 1746 */           .getMark());
/*      */     } 
/* 1748 */     return lineBreak;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Object[] scanBlockScalarIndentation() {
/* 1760 */     StringBuilder chunks = new StringBuilder();
/* 1761 */     int maxIndent = 0;
/* 1762 */     Mark endMark = this.reader.getMark();
/*      */ 
/*      */ 
/*      */     
/* 1766 */     while (Constant.LINEBR.has(this.reader.peek(), " \r")) {
/* 1767 */       if (this.reader.peek() != 32) {
/*      */ 
/*      */         
/* 1770 */         chunks.append(scanLineBreak());
/* 1771 */         endMark = this.reader.getMark();
/*      */         
/*      */         continue;
/*      */       } 
/*      */       
/* 1776 */       this.reader.forward();
/* 1777 */       if (this.reader.getColumn() > maxIndent) {
/* 1778 */         maxIndent = this.reader.getColumn();
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1783 */     return new Object[] { chunks.toString(), Integer.valueOf(maxIndent), endMark };
/*      */   }
/*      */ 
/*      */   
/*      */   private Object[] scanBlockScalarBreaks(int indent) {
/* 1788 */     StringBuilder chunks = new StringBuilder();
/* 1789 */     Mark endMark = this.reader.getMark();
/* 1790 */     int col = this.reader.getColumn();
/*      */ 
/*      */     
/* 1793 */     while (col < indent && this.reader.peek() == 32) {
/* 1794 */       this.reader.forward();
/* 1795 */       col++;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1800 */     String lineBreak = null;
/* 1801 */     while ((lineBreak = scanLineBreak()).length() != 0) {
/* 1802 */       chunks.append(lineBreak);
/* 1803 */       endMark = this.reader.getMark();
/*      */ 
/*      */       
/* 1806 */       col = this.reader.getColumn();
/* 1807 */       while (col < indent && this.reader.peek() == 32) {
/* 1808 */         this.reader.forward();
/* 1809 */         col++;
/*      */       } 
/*      */     } 
/*      */     
/* 1813 */     return new Object[] { chunks.toString(), endMark };
/*      */   }
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
/*      */   private Token scanFlowScalar(char style) {
/*      */     boolean _double;
/* 1836 */     if (style == '"') {
/* 1837 */       _double = true;
/*      */     } else {
/* 1839 */       _double = false;
/*      */     } 
/* 1841 */     StringBuilder chunks = new StringBuilder();
/* 1842 */     Mark startMark = this.reader.getMark();
/* 1843 */     int quote = this.reader.peek();
/* 1844 */     this.reader.forward();
/* 1845 */     chunks.append(scanFlowScalarNonSpaces(_double, startMark));
/* 1846 */     while (this.reader.peek() != quote) {
/* 1847 */       chunks.append(scanFlowScalarSpaces(startMark));
/* 1848 */       chunks.append(scanFlowScalarNonSpaces(_double, startMark));
/*      */     } 
/* 1850 */     this.reader.forward();
/* 1851 */     Mark endMark = this.reader.getMark();
/* 1852 */     return (Token)new ScalarToken(chunks.toString(), false, startMark, endMark, style);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String scanFlowScalarNonSpaces(boolean doubleQuoted, Mark startMark) {
/* 1860 */     StringBuilder chunks = new StringBuilder();
/*      */ 
/*      */     
/*      */     while (true) {
/* 1864 */       int length = 0;
/* 1865 */       while (Constant.NULL_BL_T_LINEBR.hasNo(this.reader.peek(length), "'\"\\")) {
/* 1866 */         length++;
/*      */       }
/* 1868 */       if (length != 0) {
/* 1869 */         chunks.append(this.reader.prefixForward(length));
/*      */       }
/*      */ 
/*      */       
/* 1873 */       int c = this.reader.peek();
/* 1874 */       if (!doubleQuoted && c == 39 && this.reader.peek(1) == 39) {
/* 1875 */         chunks.append("'");
/* 1876 */         this.reader.forward(2); continue;
/* 1877 */       }  if ((doubleQuoted && c == 39) || (!doubleQuoted && "\"\\".indexOf(c) != -1)) {
/* 1878 */         chunks.appendCodePoint(c);
/* 1879 */         this.reader.forward(); continue;
/* 1880 */       }  if (doubleQuoted && c == 92) {
/* 1881 */         this.reader.forward();
/* 1882 */         c = this.reader.peek();
/* 1883 */         if (!Character.isSupplementaryCodePoint(c) && ESCAPE_REPLACEMENTS.containsKey(Character.valueOf((char)c))) {
/*      */ 
/*      */ 
/*      */           
/* 1887 */           chunks.append(ESCAPE_REPLACEMENTS.get(Character.valueOf((char)c)));
/* 1888 */           this.reader.forward(); continue;
/* 1889 */         }  if (!Character.isSupplementaryCodePoint(c) && ESCAPE_CODES.containsKey(Character.valueOf((char)c))) {
/*      */ 
/*      */           
/* 1892 */           length = ((Integer)ESCAPE_CODES.get(Character.valueOf((char)c))).intValue();
/* 1893 */           this.reader.forward();
/* 1894 */           String hex = this.reader.prefix(length);
/* 1895 */           if (NOT_HEXA.matcher(hex).find()) {
/* 1896 */             throw new ScannerException("while scanning a double-quoted scalar", startMark, "expected escape sequence of " + length + " hexadecimal numbers, but found: " + hex, this.reader
/*      */ 
/*      */                 
/* 1899 */                 .getMark());
/*      */           }
/* 1901 */           int decimal = Integer.parseInt(hex, 16);
/* 1902 */           String unicode = new String(Character.toChars(decimal));
/* 1903 */           chunks.append(unicode);
/* 1904 */           this.reader.forward(length); continue;
/* 1905 */         }  if (scanLineBreak().length() != 0) {
/* 1906 */           chunks.append(scanFlowScalarBreaks(startMark)); continue;
/*      */         } 
/* 1908 */         String s = String.valueOf(Character.toChars(c));
/* 1909 */         throw new ScannerException("while scanning a double-quoted scalar", startMark, "found unknown escape character " + s + "(" + c + ")", this.reader
/*      */             
/* 1911 */             .getMark());
/*      */       }  break;
/*      */     } 
/* 1914 */     return chunks.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String scanFlowScalarSpaces(Mark startMark) {
/* 1921 */     StringBuilder chunks = new StringBuilder();
/* 1922 */     int length = 0;
/*      */ 
/*      */     
/* 1925 */     while (" \t".indexOf(this.reader.peek(length)) != -1) {
/* 1926 */       length++;
/*      */     }
/* 1928 */     String whitespaces = this.reader.prefixForward(length);
/* 1929 */     int c = this.reader.peek();
/* 1930 */     if (c == 0)
/*      */     {
/* 1932 */       throw new ScannerException("while scanning a quoted scalar", startMark, "found unexpected end of stream", this.reader
/* 1933 */           .getMark());
/*      */     }
/*      */     
/* 1936 */     String lineBreak = scanLineBreak();
/* 1937 */     if (lineBreak.length() != 0) {
/* 1938 */       String breaks = scanFlowScalarBreaks(startMark);
/* 1939 */       if (!"\n".equals(lineBreak)) {
/* 1940 */         chunks.append(lineBreak);
/* 1941 */       } else if (breaks.length() == 0) {
/* 1942 */         chunks.append(" ");
/*      */       } 
/* 1944 */       chunks.append(breaks);
/*      */     } else {
/* 1946 */       chunks.append(whitespaces);
/*      */     } 
/* 1948 */     return chunks.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String scanFlowScalarBreaks(Mark startMark) {
/* 1953 */     StringBuilder chunks = new StringBuilder();
/*      */ 
/*      */     
/*      */     while (true) {
/* 1957 */       String prefix = this.reader.prefix(3);
/* 1958 */       if (("---".equals(prefix) || "...".equals(prefix)) && Constant.NULL_BL_T_LINEBR
/* 1959 */         .has(this.reader.peek(3))) {
/* 1960 */         throw new ScannerException("while scanning a quoted scalar", startMark, "found unexpected document separator", this.reader
/* 1961 */             .getMark());
/*      */       }
/*      */       
/* 1964 */       while (" \t".indexOf(this.reader.peek()) != -1) {
/* 1965 */         this.reader.forward();
/*      */       }
/*      */ 
/*      */       
/* 1969 */       String lineBreak = scanLineBreak();
/* 1970 */       if (lineBreak.length() != 0) {
/* 1971 */         chunks.append(lineBreak); continue;
/*      */       }  break;
/* 1973 */     }  return chunks.toString();
/*      */   }
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
/*      */   private Token scanPlain() {
/* 1990 */     StringBuilder chunks = new StringBuilder();
/* 1991 */     Mark startMark = this.reader.getMark();
/* 1992 */     Mark endMark = startMark;
/* 1993 */     int indent = this.indent + 1;
/* 1994 */     String spaces = "";
/*      */     
/*      */     do {
/* 1997 */       int c, length = 0;
/*      */       
/* 1999 */       if (this.reader.peek() == 35) {
/*      */         break;
/*      */       }
/*      */       while (true) {
/* 2003 */         c = this.reader.peek(length);
/* 2004 */         if (Constant.NULL_BL_T_LINEBR.has(c) || (this.flowLevel == 0 && c == 58 && Constant.NULL_BL_T_LINEBR
/*      */           
/* 2006 */           .has(this.reader.peek(length + 1))) || (this.flowLevel != 0 && ",:?[]{}"
/* 2007 */           .indexOf(c) != -1)) {
/*      */           break;
/*      */         }
/* 2010 */         length++;
/*      */       } 
/*      */       
/* 2013 */       if (this.flowLevel != 0 && c == 58 && Constant.NULL_BL_T_LINEBR
/* 2014 */         .hasNo(this.reader.peek(length + 1), ",[]{}")) {
/* 2015 */         this.reader.forward(length);
/* 2016 */         throw new ScannerException("while scanning a plain scalar", startMark, "found unexpected ':'", this.reader
/* 2017 */             .getMark(), "Please check http://pyyaml.org/wiki/YAMLColonInFlowContext for details.");
/*      */       } 
/*      */       
/* 2020 */       if (length == 0) {
/*      */         break;
/*      */       }
/* 2023 */       this.allowSimpleKey = false;
/* 2024 */       chunks.append(spaces);
/* 2025 */       chunks.append(this.reader.prefixForward(length));
/* 2026 */       endMark = this.reader.getMark();
/* 2027 */       spaces = scanPlainSpaces();
/*      */     }
/* 2029 */     while (spaces.length() != 0 && this.reader.peek() != 35 && (this.flowLevel != 0 || this.reader
/* 2030 */       .getColumn() >= indent));
/*      */ 
/*      */ 
/*      */     
/* 2034 */     return (Token)new ScalarToken(chunks.toString(), startMark, endMark, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String scanPlainSpaces() {
/* 2042 */     int length = 0;
/* 2043 */     while (this.reader.peek(length) == 32 || this.reader.peek(length) == 9) {
/* 2044 */       length++;
/*      */     }
/* 2046 */     String whitespaces = this.reader.prefixForward(length);
/* 2047 */     String lineBreak = scanLineBreak();
/* 2048 */     if (lineBreak.length() != 0) {
/* 2049 */       this.allowSimpleKey = true;
/* 2050 */       String prefix = this.reader.prefix(3);
/* 2051 */       if ("---".equals(prefix) || ("...".equals(prefix) && Constant.NULL_BL_T_LINEBR
/* 2052 */         .has(this.reader.peek(3)))) {
/* 2053 */         return "";
/*      */       }
/* 2055 */       StringBuilder breaks = new StringBuilder();
/*      */       while (true) {
/* 2057 */         while (this.reader.peek() == 32) {
/* 2058 */           this.reader.forward();
/*      */         }
/* 2060 */         String lb = scanLineBreak();
/* 2061 */         if (lb.length() != 0) {
/* 2062 */           breaks.append(lb);
/* 2063 */           prefix = this.reader.prefix(3);
/* 2064 */           if ("---".equals(prefix) || ("...".equals(prefix) && Constant.NULL_BL_T_LINEBR
/* 2065 */             .has(this.reader.peek(3)))) {
/* 2066 */             return "";
/*      */           }
/*      */           
/*      */           continue;
/*      */         } 
/*      */         break;
/*      */       } 
/* 2073 */       if (!"\n".equals(lineBreak))
/* 2074 */         return lineBreak + breaks; 
/* 2075 */       if (breaks.length() == 0) {
/* 2076 */         return " ";
/*      */       }
/* 2078 */       return breaks.toString();
/*      */     } 
/* 2080 */     return whitespaces;
/*      */   }
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
/*      */   private String scanTagHandle(String name, Mark startMark) {
/* 2106 */     int c = this.reader.peek();
/* 2107 */     if (c != 33) {
/* 2108 */       String s = String.valueOf(Character.toChars(c));
/* 2109 */       throw new ScannerException("while scanning a " + name, startMark, "expected '!', but found " + s + "(" + c + ")", this.reader
/* 2110 */           .getMark());
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2115 */     int length = 1;
/* 2116 */     c = this.reader.peek(length);
/* 2117 */     if (c != 32) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2122 */       while (Constant.ALPHA.has(c)) {
/* 2123 */         length++;
/* 2124 */         c = this.reader.peek(length);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 2129 */       if (c != 33) {
/* 2130 */         this.reader.forward(length);
/* 2131 */         String s = String.valueOf(Character.toChars(c));
/* 2132 */         throw new ScannerException("while scanning a " + name, startMark, "expected '!', but found " + s + "(" + c + ")", this.reader
/* 2133 */             .getMark());
/*      */       } 
/* 2135 */       length++;
/*      */     } 
/* 2137 */     String value = this.reader.prefixForward(length);
/* 2138 */     return value;
/*      */   }
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
/*      */   private String scanTagUri(String name, Mark startMark) {
/* 2159 */     StringBuilder chunks = new StringBuilder();
/*      */ 
/*      */ 
/*      */     
/* 2163 */     int length = 0;
/* 2164 */     int c = this.reader.peek(length);
/* 2165 */     while (Constant.URI_CHARS.has(c)) {
/* 2166 */       if (c == 37) {
/* 2167 */         chunks.append(this.reader.prefixForward(length));
/* 2168 */         length = 0;
/* 2169 */         chunks.append(scanUriEscapes(name, startMark));
/*      */       } else {
/* 2171 */         length++;
/*      */       } 
/* 2173 */       c = this.reader.peek(length);
/*      */     } 
/*      */ 
/*      */     
/* 2177 */     if (length != 0) {
/* 2178 */       chunks.append(this.reader.prefixForward(length));
/* 2179 */       length = 0;
/*      */     } 
/* 2181 */     if (chunks.length() == 0) {
/*      */       
/* 2183 */       String s = String.valueOf(Character.toChars(c));
/* 2184 */       throw new ScannerException("while scanning a " + name, startMark, "expected URI, but found " + s + "(" + c + ")", this.reader
/* 2185 */           .getMark());
/*      */     } 
/* 2187 */     return chunks.toString();
/*      */   }
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
/*      */   private String scanUriEscapes(String name, Mark startMark) {
/* 2204 */     int length = 1;
/* 2205 */     while (this.reader.peek(length * 3) == 37) {
/* 2206 */       length++;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2212 */     Mark beginningMark = this.reader.getMark();
/* 2213 */     ByteBuffer buff = ByteBuffer.allocate(length);
/* 2214 */     while (this.reader.peek() == 37) {
/* 2215 */       this.reader.forward();
/*      */       try {
/* 2217 */         byte code = (byte)Integer.parseInt(this.reader.prefix(2), 16);
/* 2218 */         buff.put(code);
/* 2219 */       } catch (NumberFormatException nfe) {
/* 2220 */         int c1 = this.reader.peek();
/* 2221 */         String s1 = String.valueOf(Character.toChars(c1));
/* 2222 */         int c2 = this.reader.peek(1);
/* 2223 */         String s2 = String.valueOf(Character.toChars(c2));
/* 2224 */         throw new ScannerException("while scanning a " + name, startMark, "expected URI escape sequence of 2 hexadecimal numbers, but found " + s1 + "(" + c1 + ") and " + s2 + "(" + c2 + ")", this.reader
/*      */ 
/*      */ 
/*      */             
/* 2228 */             .getMark());
/*      */       } 
/* 2230 */       this.reader.forward(2);
/*      */     } 
/* 2232 */     buff.flip();
/*      */     try {
/* 2234 */       return UriEncoder.decode(buff);
/* 2235 */     } catch (CharacterCodingException e) {
/* 2236 */       throw new ScannerException("while scanning a " + name, startMark, "expected URI in UTF-8: " + e
/* 2237 */           .getMessage(), beginningMark);
/*      */     } 
/*      */   }
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
/*      */   private String scanLineBreak() {
/* 2259 */     int c = this.reader.peek();
/* 2260 */     if (c == 13 || c == 10 || c == 133) {
/* 2261 */       if (c == 13 && 10 == this.reader.peek(1)) {
/* 2262 */         this.reader.forward(2);
/*      */       } else {
/* 2264 */         this.reader.forward();
/*      */       } 
/* 2266 */       return "\n";
/* 2267 */     }  if (c == 8232 || c == 8233) {
/* 2268 */       this.reader.forward();
/* 2269 */       return String.valueOf(Character.toChars(c));
/*      */     } 
/* 2271 */     return "";
/*      */   }
/*      */ 
/*      */   
/*      */   private static class Chomping
/*      */   {
/*      */     private final Boolean value;
/*      */     
/*      */     private final int increment;
/*      */     
/*      */     public Chomping(Boolean value, int increment) {
/* 2282 */       this.value = value;
/* 2283 */       this.increment = increment;
/*      */     }
/*      */     
/*      */     public boolean chompTailIsNotFalse() {
/* 2287 */       return (this.value == null || this.value.booleanValue());
/*      */     }
/*      */     
/*      */     public boolean chompTailIsTrue() {
/* 2291 */       return (this.value != null && this.value.booleanValue());
/*      */     }
/*      */     
/*      */     public int getIncrement() {
/* 2295 */       return this.increment;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\yaml\snakeyaml\scanner\ScannerImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */