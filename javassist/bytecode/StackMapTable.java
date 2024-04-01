/*      */ package javassist.bytecode;
/*      */ 
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.DataInputStream;
/*      */ import java.io.DataOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.io.PrintWriter;
/*      */ import java.util.Map;
/*      */ import javassist.CannotCompileException;
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
/*      */ public class StackMapTable
/*      */   extends AttributeInfo
/*      */ {
/*      */   public static final String tag = "StackMapTable";
/*      */   public static final int TOP = 0;
/*      */   public static final int INTEGER = 1;
/*      */   public static final int FLOAT = 2;
/*      */   public static final int DOUBLE = 3;
/*      */   public static final int LONG = 4;
/*      */   public static final int NULL = 5;
/*      */   public static final int THIS = 6;
/*      */   public static final int OBJECT = 7;
/*      */   public static final int UNINIT = 8;
/*      */   
/*      */   StackMapTable(ConstPool cp, byte[] newInfo) {
/*   47 */     super(cp, "StackMapTable", newInfo);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   StackMapTable(ConstPool cp, int name_id, DataInputStream in) throws IOException {
/*   53 */     super(cp, name_id, in);
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
/*      */   public AttributeInfo copy(ConstPool newCp, Map classnames) throws RuntimeCopyException {
/*      */     try {
/*   69 */       return new StackMapTable(newCp, (new Copier(this.constPool, this.info, newCp, classnames))
/*   70 */           .doit());
/*      */     }
/*   72 */     catch (BadBytecode e) {
/*   73 */       throw new RuntimeCopyException("bad bytecode. fatal?");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class RuntimeCopyException
/*      */     extends RuntimeException
/*      */   {
/*      */     public RuntimeCopyException(String s) {
/*   86 */       super(s);
/*      */     }
/*      */   }
/*      */   
/*      */   void write(DataOutputStream out) throws IOException {
/*   91 */     super.write(out);
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
/*      */   public static class Walker
/*      */   {
/*      */     byte[] info;
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
/*      */     int numOfEntries;
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
/*      */     public Walker(StackMapTable smt) {
/*  153 */       this(smt.get());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Walker(byte[] data) {
/*  165 */       this.info = data;
/*  166 */       this.numOfEntries = ByteArray.readU16bit(data, 0);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public final int size() {
/*  172 */       return this.numOfEntries;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void parse() throws BadBytecode {
/*  178 */       int n = this.numOfEntries;
/*  179 */       int pos = 2;
/*  180 */       for (int i = 0; i < n; i++) {
/*  181 */         pos = stackMapFrames(pos, i);
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int stackMapFrames(int pos, int nth) throws BadBytecode {
/*  194 */       int type = this.info[pos] & 0xFF;
/*  195 */       if (type < 64)
/*  196 */       { sameFrame(pos, type);
/*  197 */         pos++; }
/*      */       
/*  199 */       else if (type < 128)
/*  200 */       { pos = sameLocals(pos, type); }
/*  201 */       else { if (type < 247)
/*  202 */           throw new BadBytecode("bad frame_type in StackMapTable"); 
/*  203 */         if (type == 247) {
/*  204 */           pos = sameLocals(pos, type);
/*  205 */         } else if (type < 251) {
/*  206 */           int offset = ByteArray.readU16bit(this.info, pos + 1);
/*  207 */           chopFrame(pos, offset, 251 - type);
/*  208 */           pos += 3;
/*      */         }
/*  210 */         else if (type == 251) {
/*  211 */           int offset = ByteArray.readU16bit(this.info, pos + 1);
/*  212 */           sameFrame(pos, offset);
/*  213 */           pos += 3;
/*      */         }
/*  215 */         else if (type < 255) {
/*  216 */           pos = appendFrame(pos, type);
/*      */         } else {
/*  218 */           pos = fullFrame(pos);
/*      */         }  }
/*  220 */        return pos;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void sameFrame(int pos, int offsetDelta) throws BadBytecode {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int sameLocals(int pos, int type) throws BadBytecode {
/*  234 */       int offset, top = pos;
/*      */       
/*  236 */       if (type < 128) {
/*  237 */         offset = type - 64;
/*      */       } else {
/*  239 */         offset = ByteArray.readU16bit(this.info, pos + 1);
/*  240 */         pos += 2;
/*      */       } 
/*      */       
/*  243 */       int tag = this.info[pos + 1] & 0xFF;
/*  244 */       int data = 0;
/*  245 */       if (tag == 7 || tag == 8) {
/*  246 */         data = ByteArray.readU16bit(this.info, pos + 2);
/*  247 */         objectOrUninitialized(tag, data, pos + 2);
/*  248 */         pos += 2;
/*      */       } 
/*      */       
/*  251 */       sameLocals(top, offset, tag, data);
/*  252 */       return pos + 2;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void sameLocals(int pos, int offsetDelta, int stackTag, int stackData) throws BadBytecode {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void chopFrame(int pos, int offsetDelta, int k) throws BadBytecode {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int appendFrame(int pos, int type) throws BadBytecode {
/*  280 */       int k = type - 251;
/*  281 */       int offset = ByteArray.readU16bit(this.info, pos + 1);
/*  282 */       int[] tags = new int[k];
/*  283 */       int[] data = new int[k];
/*  284 */       int p = pos + 3;
/*  285 */       for (int i = 0; i < k; i++) {
/*  286 */         int tag = this.info[p] & 0xFF;
/*  287 */         tags[i] = tag;
/*  288 */         if (tag == 7 || tag == 8) {
/*  289 */           data[i] = ByteArray.readU16bit(this.info, p + 1);
/*  290 */           objectOrUninitialized(tag, data[i], p + 1);
/*  291 */           p += 3;
/*      */         } else {
/*      */           
/*  294 */           data[i] = 0;
/*  295 */           p++;
/*      */         } 
/*      */       } 
/*      */       
/*  299 */       appendFrame(pos, offset, tags, data);
/*  300 */       return p;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendFrame(int pos, int offsetDelta, int[] tags, int[] data) throws BadBytecode {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int fullFrame(int pos) throws BadBytecode {
/*  316 */       int offset = ByteArray.readU16bit(this.info, pos + 1);
/*  317 */       int numOfLocals = ByteArray.readU16bit(this.info, pos + 3);
/*  318 */       int[] localsTags = new int[numOfLocals];
/*  319 */       int[] localsData = new int[numOfLocals];
/*  320 */       int p = verifyTypeInfo(pos + 5, numOfLocals, localsTags, localsData);
/*  321 */       int numOfItems = ByteArray.readU16bit(this.info, p);
/*  322 */       int[] itemsTags = new int[numOfItems];
/*  323 */       int[] itemsData = new int[numOfItems];
/*  324 */       p = verifyTypeInfo(p + 2, numOfItems, itemsTags, itemsData);
/*  325 */       fullFrame(pos, offset, localsTags, localsData, itemsTags, itemsData);
/*  326 */       return p;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void fullFrame(int pos, int offsetDelta, int[] localTags, int[] localData, int[] stackTags, int[] stackData) throws BadBytecode {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int verifyTypeInfo(int pos, int n, int[] tags, int[] data) {
/*  346 */       for (int i = 0; i < n; i++) {
/*  347 */         int tag = this.info[pos++] & 0xFF;
/*  348 */         tags[i] = tag;
/*  349 */         if (tag == 7 || tag == 8) {
/*  350 */           data[i] = ByteArray.readU16bit(this.info, pos);
/*  351 */           objectOrUninitialized(tag, data[i], pos);
/*  352 */           pos += 2;
/*      */         } 
/*      */       } 
/*      */       
/*  356 */       return pos;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void objectOrUninitialized(int tag, int data, int pos) {}
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static class SimpleCopy
/*      */     extends Walker
/*      */   {
/*      */     private StackMapTable.Writer writer;
/*      */ 
/*      */ 
/*      */     
/*      */     public SimpleCopy(byte[] data) {
/*  374 */       super(data);
/*  375 */       this.writer = new StackMapTable.Writer(data.length);
/*      */     }
/*      */     
/*      */     public byte[] doit() throws BadBytecode {
/*  379 */       parse();
/*  380 */       return this.writer.toByteArray();
/*      */     }
/*      */     
/*      */     public void sameFrame(int pos, int offsetDelta) {
/*  384 */       this.writer.sameFrame(offsetDelta);
/*      */     }
/*      */     
/*      */     public void sameLocals(int pos, int offsetDelta, int stackTag, int stackData) {
/*  388 */       this.writer.sameLocals(offsetDelta, stackTag, copyData(stackTag, stackData));
/*      */     }
/*      */     
/*      */     public void chopFrame(int pos, int offsetDelta, int k) {
/*  392 */       this.writer.chopFrame(offsetDelta, k);
/*      */     }
/*      */     
/*      */     public void appendFrame(int pos, int offsetDelta, int[] tags, int[] data) {
/*  396 */       this.writer.appendFrame(offsetDelta, tags, copyData(tags, data));
/*      */     }
/*      */ 
/*      */     
/*      */     public void fullFrame(int pos, int offsetDelta, int[] localTags, int[] localData, int[] stackTags, int[] stackData) {
/*  401 */       this.writer.fullFrame(offsetDelta, localTags, copyData(localTags, localData), stackTags, 
/*  402 */           copyData(stackTags, stackData));
/*      */     }
/*      */     
/*      */     protected int copyData(int tag, int data) {
/*  406 */       return data;
/*      */     }
/*      */     
/*      */     protected int[] copyData(int[] tags, int[] data) {
/*  410 */       return data;
/*      */     } }
/*      */   
/*      */   static class Copier extends SimpleCopy {
/*      */     private ConstPool srcPool;
/*      */     private ConstPool destPool;
/*      */     private Map classnames;
/*      */     
/*      */     public Copier(ConstPool src, byte[] data, ConstPool dest, Map names) {
/*  419 */       super(data);
/*  420 */       this.srcPool = src;
/*  421 */       this.destPool = dest;
/*  422 */       this.classnames = names;
/*      */     }
/*      */     
/*      */     protected int copyData(int tag, int data) {
/*  426 */       if (tag == 7) {
/*  427 */         return this.srcPool.copy(data, this.destPool, this.classnames);
/*      */       }
/*  429 */       return data;
/*      */     }
/*      */     
/*      */     protected int[] copyData(int[] tags, int[] data) {
/*  433 */       int[] newData = new int[data.length];
/*  434 */       for (int i = 0; i < data.length; i++) {
/*  435 */         if (tags[i] == 7) {
/*  436 */           newData[i] = this.srcPool.copy(data[i], this.destPool, this.classnames);
/*      */         } else {
/*  438 */           newData[i] = data[i];
/*      */         } 
/*  440 */       }  return newData;
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
/*      */   public void insertLocal(int index, int tag, int classInfo) throws BadBytecode {
/*  461 */     byte[] data = (new InsertLocal(get(), index, tag, classInfo)).doit();
/*  462 */     set(data);
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
/*      */   public static int typeTagOf(char descriptor) {
/*  475 */     switch (descriptor) {
/*      */       case 'D':
/*  477 */         return 3;
/*      */       case 'F':
/*  479 */         return 2;
/*      */       case 'J':
/*  481 */         return 4;
/*      */       case 'L':
/*      */       case '[':
/*  484 */         return 7;
/*      */     } 
/*      */     
/*  487 */     return 1;
/*      */   }
/*      */ 
/*      */   
/*      */   static class InsertLocal
/*      */     extends SimpleCopy
/*      */   {
/*      */     private int varIndex;
/*      */     
/*      */     private int varTag;
/*      */     
/*      */     private int varData;
/*      */     
/*      */     public InsertLocal(byte[] data, int varIndex, int varTag, int varData) {
/*  501 */       super(data);
/*  502 */       this.varIndex = varIndex;
/*  503 */       this.varTag = varTag;
/*  504 */       this.varData = varData;
/*      */     }
/*      */ 
/*      */     
/*      */     public void fullFrame(int pos, int offsetDelta, int[] localTags, int[] localData, int[] stackTags, int[] stackData) {
/*  509 */       int len = localTags.length;
/*  510 */       if (len < this.varIndex) {
/*  511 */         super.fullFrame(pos, offsetDelta, localTags, localData, stackTags, stackData);
/*      */         
/*      */         return;
/*      */       } 
/*  515 */       int typeSize = (this.varTag == 4 || this.varTag == 3) ? 2 : 1;
/*  516 */       int[] localTags2 = new int[len + typeSize];
/*  517 */       int[] localData2 = new int[len + typeSize];
/*  518 */       int index = this.varIndex;
/*  519 */       int j = 0;
/*  520 */       for (int i = 0; i < len; i++) {
/*  521 */         if (j == index) {
/*  522 */           j += typeSize;
/*      */         }
/*  524 */         localTags2[j] = localTags[i];
/*  525 */         localData2[j++] = localData[i];
/*      */       } 
/*      */       
/*  528 */       localTags2[index] = this.varTag;
/*  529 */       localData2[index] = this.varData;
/*  530 */       if (typeSize > 1) {
/*  531 */         localTags2[index + 1] = 0;
/*  532 */         localData2[index + 1] = 0;
/*      */       } 
/*      */       
/*  535 */       super.fullFrame(pos, offsetDelta, localTags2, localData2, stackTags, stackData);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class Writer
/*      */   {
/*      */     ByteArrayOutputStream output;
/*      */ 
/*      */     
/*      */     int numOfEntries;
/*      */ 
/*      */ 
/*      */     
/*      */     public Writer(int size) {
/*  551 */       this.output = new ByteArrayOutputStream(size);
/*  552 */       this.numOfEntries = 0;
/*  553 */       this.output.write(0);
/*  554 */       this.output.write(0);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] toByteArray() {
/*  561 */       byte[] b = this.output.toByteArray();
/*  562 */       ByteArray.write16bit(this.numOfEntries, b, 0);
/*  563 */       return b;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public StackMapTable toStackMapTable(ConstPool cp) {
/*  574 */       return new StackMapTable(cp, toByteArray());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void sameFrame(int offsetDelta) {
/*  581 */       this.numOfEntries++;
/*  582 */       if (offsetDelta < 64) {
/*  583 */         this.output.write(offsetDelta);
/*      */       } else {
/*  585 */         this.output.write(251);
/*  586 */         write16(offsetDelta);
/*      */       } 
/*      */     }
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
/*      */     public void sameLocals(int offsetDelta, int tag, int data) {
/*  602 */       this.numOfEntries++;
/*  603 */       if (offsetDelta < 64) {
/*  604 */         this.output.write(offsetDelta + 64);
/*      */       } else {
/*  606 */         this.output.write(247);
/*  607 */         write16(offsetDelta);
/*      */       } 
/*      */       
/*  610 */       writeTypeInfo(tag, data);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void chopFrame(int offsetDelta, int k) {
/*  619 */       this.numOfEntries++;
/*  620 */       this.output.write(251 - k);
/*  621 */       write16(offsetDelta);
/*      */     }
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
/*      */     public void appendFrame(int offsetDelta, int[] tags, int[] data) {
/*  638 */       this.numOfEntries++;
/*  639 */       int k = tags.length;
/*  640 */       this.output.write(k + 251);
/*  641 */       write16(offsetDelta);
/*  642 */       for (int i = 0; i < k; i++) {
/*  643 */         writeTypeInfo(tags[i], data[i]);
/*      */       }
/*      */     }
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
/*      */     public void fullFrame(int offsetDelta, int[] localTags, int[] localData, int[] stackTags, int[] stackData) {
/*  667 */       this.numOfEntries++;
/*  668 */       this.output.write(255);
/*  669 */       write16(offsetDelta);
/*  670 */       int n = localTags.length;
/*  671 */       write16(n); int i;
/*  672 */       for (i = 0; i < n; i++) {
/*  673 */         writeTypeInfo(localTags[i], localData[i]);
/*      */       }
/*  675 */       n = stackTags.length;
/*  676 */       write16(n);
/*  677 */       for (i = 0; i < n; i++)
/*  678 */         writeTypeInfo(stackTags[i], stackData[i]); 
/*      */     }
/*      */     
/*      */     private void writeTypeInfo(int tag, int data) {
/*  682 */       this.output.write(tag);
/*  683 */       if (tag == 7 || tag == 8)
/*  684 */         write16(data); 
/*      */     }
/*      */     
/*      */     private void write16(int value) {
/*  688 */       this.output.write(value >>> 8 & 0xFF);
/*  689 */       this.output.write(value & 0xFF);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void println(PrintWriter w) {
/*  697 */     Printer.print(this, w);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void println(PrintStream ps) {
/*  706 */     Printer.print(this, new PrintWriter(ps, true));
/*      */   }
/*      */ 
/*      */   
/*      */   static class Printer
/*      */     extends Walker
/*      */   {
/*      */     private PrintWriter writer;
/*      */     private int offset;
/*      */     
/*      */     public static void print(StackMapTable smt, PrintWriter writer) {
/*      */       try {
/*  718 */         (new Printer(smt.get(), writer)).parse();
/*      */       }
/*  720 */       catch (BadBytecode e) {
/*  721 */         writer.println(e.getMessage());
/*      */       } 
/*      */     }
/*      */     
/*      */     Printer(byte[] data, PrintWriter pw) {
/*  726 */       super(data);
/*  727 */       this.writer = pw;
/*  728 */       this.offset = -1;
/*      */     }
/*      */     
/*      */     public void sameFrame(int pos, int offsetDelta) {
/*  732 */       this.offset += offsetDelta + 1;
/*  733 */       this.writer.println(this.offset + " same frame: " + offsetDelta);
/*      */     }
/*      */     
/*      */     public void sameLocals(int pos, int offsetDelta, int stackTag, int stackData) {
/*  737 */       this.offset += offsetDelta + 1;
/*  738 */       this.writer.println(this.offset + " same locals: " + offsetDelta);
/*  739 */       printTypeInfo(stackTag, stackData);
/*      */     }
/*      */     
/*      */     public void chopFrame(int pos, int offsetDelta, int k) {
/*  743 */       this.offset += offsetDelta + 1;
/*  744 */       this.writer.println(this.offset + " chop frame: " + offsetDelta + ",    " + k + " last locals");
/*      */     }
/*      */     
/*      */     public void appendFrame(int pos, int offsetDelta, int[] tags, int[] data) {
/*  748 */       this.offset += offsetDelta + 1;
/*  749 */       this.writer.println(this.offset + " append frame: " + offsetDelta);
/*  750 */       for (int i = 0; i < tags.length; i++) {
/*  751 */         printTypeInfo(tags[i], data[i]);
/*      */       }
/*      */     }
/*      */     
/*      */     public void fullFrame(int pos, int offsetDelta, int[] localTags, int[] localData, int[] stackTags, int[] stackData) {
/*  756 */       this.offset += offsetDelta + 1;
/*  757 */       this.writer.println(this.offset + " full frame: " + offsetDelta);
/*  758 */       this.writer.println("[locals]"); int i;
/*  759 */       for (i = 0; i < localTags.length; i++) {
/*  760 */         printTypeInfo(localTags[i], localData[i]);
/*      */       }
/*  762 */       this.writer.println("[stack]");
/*  763 */       for (i = 0; i < stackTags.length; i++)
/*  764 */         printTypeInfo(stackTags[i], stackData[i]); 
/*      */     }
/*      */     
/*      */     private void printTypeInfo(int tag, int data) {
/*  768 */       String msg = null;
/*  769 */       switch (tag) {
/*      */         case 0:
/*  771 */           msg = "top";
/*      */           break;
/*      */         case 1:
/*  774 */           msg = "integer";
/*      */           break;
/*      */         case 2:
/*  777 */           msg = "float";
/*      */           break;
/*      */         case 3:
/*  780 */           msg = "double";
/*      */           break;
/*      */         case 4:
/*  783 */           msg = "long";
/*      */           break;
/*      */         case 5:
/*  786 */           msg = "null";
/*      */           break;
/*      */         case 6:
/*  789 */           msg = "this";
/*      */           break;
/*      */         case 7:
/*  792 */           msg = "object (cpool_index " + data + ")";
/*      */           break;
/*      */         case 8:
/*  795 */           msg = "uninitialized (offset " + data + ")";
/*      */           break;
/*      */       } 
/*      */       
/*  799 */       this.writer.print("    ");
/*  800 */       this.writer.println(msg);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void shiftPc(int where, int gapSize, boolean exclusive) throws BadBytecode {
/*  807 */     (new OffsetShifter(this, where, gapSize)).parse();
/*  808 */     (new Shifter(this, where, gapSize, exclusive)).doit();
/*      */   }
/*      */   
/*      */   static class OffsetShifter extends Walker { int where;
/*      */     int gap;
/*      */     
/*      */     public OffsetShifter(StackMapTable smt, int where, int gap) {
/*  815 */       super(smt);
/*  816 */       this.where = where;
/*  817 */       this.gap = gap;
/*      */     }
/*      */     
/*      */     public void objectOrUninitialized(int tag, int data, int pos) {
/*  821 */       if (tag == 8 && 
/*  822 */         this.where <= data)
/*  823 */         ByteArray.write16bit(data + this.gap, this.info, pos); 
/*      */     } }
/*      */   
/*      */   static class Shifter extends Walker {
/*      */     private StackMapTable stackMap;
/*      */     int where;
/*      */     int gap;
/*      */     int position;
/*      */     byte[] updatedInfo;
/*      */     boolean exclusive;
/*      */     
/*      */     public Shifter(StackMapTable smt, int where, int gap, boolean exclusive) {
/*  835 */       super(smt);
/*  836 */       this.stackMap = smt;
/*  837 */       this.where = where;
/*  838 */       this.gap = gap;
/*  839 */       this.position = 0;
/*  840 */       this.updatedInfo = null;
/*  841 */       this.exclusive = exclusive;
/*      */     }
/*      */     
/*      */     public void doit() throws BadBytecode {
/*  845 */       parse();
/*  846 */       if (this.updatedInfo != null)
/*  847 */         this.stackMap.set(this.updatedInfo); 
/*      */     }
/*      */     
/*      */     public void sameFrame(int pos, int offsetDelta) {
/*  851 */       update(pos, offsetDelta, 0, 251);
/*      */     }
/*      */     
/*      */     public void sameLocals(int pos, int offsetDelta, int stackTag, int stackData) {
/*  855 */       update(pos, offsetDelta, 64, 247);
/*      */     }
/*      */     void update(int pos, int offsetDelta, int base, int entry) {
/*      */       boolean match;
/*  859 */       int oldPos = this.position;
/*  860 */       this.position = oldPos + offsetDelta + ((oldPos == 0) ? 0 : 1);
/*      */       
/*  862 */       if (this.exclusive) {
/*  863 */         match = (oldPos < this.where && this.where <= this.position);
/*      */       } else {
/*  865 */         match = (oldPos <= this.where && this.where < this.position);
/*      */       } 
/*  867 */       if (match) {
/*  868 */         int newDelta = offsetDelta + this.gap;
/*  869 */         this.position += this.gap;
/*  870 */         if (newDelta < 64) {
/*  871 */           this.info[pos] = (byte)(newDelta + base);
/*  872 */         } else if (offsetDelta < 64) {
/*  873 */           byte[] newinfo = insertGap(this.info, pos, 2);
/*  874 */           newinfo[pos] = (byte)entry;
/*  875 */           ByteArray.write16bit(newDelta, newinfo, pos + 1);
/*  876 */           this.updatedInfo = newinfo;
/*      */         } else {
/*      */           
/*  879 */           ByteArray.write16bit(newDelta, this.info, pos + 1);
/*      */         } 
/*      */       } 
/*      */     }
/*      */     static byte[] insertGap(byte[] info, int where, int gap) {
/*  884 */       int len = info.length;
/*  885 */       byte[] newinfo = new byte[len + gap];
/*  886 */       for (int i = 0; i < len; i++) {
/*  887 */         newinfo[i + ((i < where) ? 0 : gap)] = info[i];
/*      */       }
/*  889 */       return newinfo;
/*      */     }
/*      */     
/*      */     public void chopFrame(int pos, int offsetDelta, int k) {
/*  893 */       update(pos, offsetDelta);
/*      */     }
/*      */     
/*      */     public void appendFrame(int pos, int offsetDelta, int[] tags, int[] data) {
/*  897 */       update(pos, offsetDelta);
/*      */     }
/*      */ 
/*      */     
/*      */     public void fullFrame(int pos, int offsetDelta, int[] localTags, int[] localData, int[] stackTags, int[] stackData) {
/*  902 */       update(pos, offsetDelta);
/*      */     }
/*      */     void update(int pos, int offsetDelta) {
/*      */       boolean match;
/*  906 */       int oldPos = this.position;
/*  907 */       this.position = oldPos + offsetDelta + ((oldPos == 0) ? 0 : 1);
/*      */       
/*  909 */       if (this.exclusive) {
/*  910 */         match = (oldPos < this.where && this.where <= this.position);
/*      */       } else {
/*  912 */         match = (oldPos <= this.where && this.where < this.position);
/*      */       } 
/*  914 */       if (match) {
/*  915 */         int newDelta = offsetDelta + this.gap;
/*  916 */         ByteArray.write16bit(newDelta, this.info, pos + 1);
/*  917 */         this.position += this.gap;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void shiftForSwitch(int where, int gapSize) throws BadBytecode {
/*  926 */     (new SwitchShifter(this, where, gapSize)).doit();
/*      */   }
/*      */   
/*      */   static class SwitchShifter extends Shifter {
/*      */     SwitchShifter(StackMapTable smt, int where, int gap) {
/*  931 */       super(smt, where, gap, false);
/*      */     }
/*      */     
/*      */     void update(int pos, int offsetDelta, int base, int entry) {
/*  935 */       int oldPos = this.position;
/*  936 */       this.position = oldPos + offsetDelta + ((oldPos == 0) ? 0 : 1);
/*  937 */       int newDelta = offsetDelta;
/*  938 */       if (this.where == this.position) {
/*  939 */         newDelta = offsetDelta - this.gap;
/*  940 */       } else if (this.where == oldPos) {
/*  941 */         newDelta = offsetDelta + this.gap;
/*      */       } else {
/*      */         return;
/*      */       } 
/*  945 */       if (offsetDelta < 64) {
/*  946 */         if (newDelta < 64) {
/*  947 */           this.info[pos] = (byte)(newDelta + base);
/*      */         } else {
/*  949 */           byte[] newinfo = insertGap(this.info, pos, 2);
/*  950 */           newinfo[pos] = (byte)entry;
/*  951 */           ByteArray.write16bit(newDelta, newinfo, pos + 1);
/*  952 */           this.updatedInfo = newinfo;
/*      */         }
/*      */       
/*  955 */       } else if (newDelta < 64) {
/*  956 */         byte[] newinfo = deleteGap(this.info, pos, 2);
/*  957 */         newinfo[pos] = (byte)(newDelta + base);
/*  958 */         this.updatedInfo = newinfo;
/*      */       } else {
/*      */         
/*  961 */         ByteArray.write16bit(newDelta, this.info, pos + 1);
/*      */       } 
/*      */     }
/*      */     static byte[] deleteGap(byte[] info, int where, int gap) {
/*  965 */       where += gap;
/*  966 */       int len = info.length;
/*  967 */       byte[] newinfo = new byte[len - gap];
/*  968 */       for (int i = 0; i < len; i++) {
/*  969 */         newinfo[i - ((i < where) ? 0 : gap)] = info[i];
/*      */       }
/*  971 */       return newinfo;
/*      */     }
/*      */     
/*      */     void update(int pos, int offsetDelta) {
/*  975 */       int oldPos = this.position;
/*  976 */       this.position = oldPos + offsetDelta + ((oldPos == 0) ? 0 : 1);
/*  977 */       int newDelta = offsetDelta;
/*  978 */       if (this.where == this.position) {
/*  979 */         newDelta = offsetDelta - this.gap;
/*  980 */       } else if (this.where == oldPos) {
/*  981 */         newDelta = offsetDelta + this.gap;
/*      */       } else {
/*      */         return;
/*      */       } 
/*  985 */       ByteArray.write16bit(newDelta, this.info, pos + 1);
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
/*      */   public void removeNew(int where) throws CannotCompileException {
/*      */     try {
/* 1000 */       byte[] data = (new NewRemover(get(), where)).doit();
/* 1001 */       set(data);
/*      */     }
/* 1003 */     catch (BadBytecode e) {
/* 1004 */       throw new CannotCompileException("bad stack map table", e);
/*      */     } 
/*      */   }
/*      */   
/*      */   static class NewRemover extends SimpleCopy {
/*      */     int posOfNew;
/*      */     
/*      */     public NewRemover(byte[] data, int pos) {
/* 1012 */       super(data);
/* 1013 */       this.posOfNew = pos;
/*      */     }
/*      */     
/*      */     public void sameLocals(int pos, int offsetDelta, int stackTag, int stackData) {
/* 1017 */       if (stackTag == 8 && stackData == this.posOfNew) {
/* 1018 */         sameFrame(pos, offsetDelta);
/*      */       } else {
/* 1020 */         super.sameLocals(pos, offsetDelta, stackTag, stackData);
/*      */       } 
/*      */     }
/*      */     
/*      */     public void fullFrame(int pos, int offsetDelta, int[] localTags, int[] localData, int[] stackTags, int[] stackData) {
/* 1025 */       int n = stackTags.length - 1;
/* 1026 */       for (int i = 0; i < n; i++) {
/* 1027 */         if (stackTags[i] == 8 && stackData[i] == this.posOfNew && stackTags[i + 1] == 8 && stackData[i + 1] == this.posOfNew) {
/*      */           
/* 1029 */           n++;
/* 1030 */           int[] stackTags2 = new int[n - 2];
/* 1031 */           int[] stackData2 = new int[n - 2];
/* 1032 */           int k = 0;
/* 1033 */           for (int j = 0; j < n; j++) {
/* 1034 */             if (j == i) {
/* 1035 */               j++;
/*      */             } else {
/* 1037 */               stackTags2[k] = stackTags[j];
/* 1038 */               stackData2[k++] = stackData[j];
/*      */             } 
/*      */           } 
/* 1041 */           stackTags = stackTags2;
/* 1042 */           stackData = stackData2;
/*      */           break;
/*      */         } 
/*      */       } 
/* 1046 */       super.fullFrame(pos, offsetDelta, localTags, localData, stackTags, stackData);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\StackMapTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */