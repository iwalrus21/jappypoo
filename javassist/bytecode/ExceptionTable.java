/*     */ package javassist.bytecode;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Map;
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
/*     */ public class ExceptionTable
/*     */   implements Cloneable
/*     */ {
/*     */   private ConstPool constPool;
/*     */   private ArrayList entries;
/*     */   
/*     */   public ExceptionTable(ConstPool cp) {
/*  52 */     this.constPool = cp;
/*  53 */     this.entries = new ArrayList();
/*     */   }
/*     */   
/*     */   ExceptionTable(ConstPool cp, DataInputStream in) throws IOException {
/*  57 */     this.constPool = cp;
/*  58 */     int length = in.readUnsignedShort();
/*  59 */     ArrayList<ExceptionTableEntry> list = new ArrayList(length);
/*  60 */     for (int i = 0; i < length; i++) {
/*  61 */       int start = in.readUnsignedShort();
/*  62 */       int end = in.readUnsignedShort();
/*  63 */       int handle = in.readUnsignedShort();
/*  64 */       int type = in.readUnsignedShort();
/*  65 */       list.add(new ExceptionTableEntry(start, end, handle, type));
/*     */     } 
/*     */     
/*  68 */     this.entries = list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/*  77 */     ExceptionTable r = (ExceptionTable)super.clone();
/*  78 */     r.entries = new ArrayList(this.entries);
/*  79 */     return r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/*  87 */     return this.entries.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int startPc(int nth) {
/*  96 */     ExceptionTableEntry e = this.entries.get(nth);
/*  97 */     return e.startPc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStartPc(int nth, int value) {
/* 107 */     ExceptionTableEntry e = this.entries.get(nth);
/* 108 */     e.startPc = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int endPc(int nth) {
/* 117 */     ExceptionTableEntry e = this.entries.get(nth);
/* 118 */     return e.endPc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEndPc(int nth, int value) {
/* 128 */     ExceptionTableEntry e = this.entries.get(nth);
/* 129 */     e.endPc = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int handlerPc(int nth) {
/* 138 */     ExceptionTableEntry e = this.entries.get(nth);
/* 139 */     return e.handlerPc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHandlerPc(int nth, int value) {
/* 149 */     ExceptionTableEntry e = this.entries.get(nth);
/* 150 */     e.handlerPc = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int catchType(int nth) {
/* 161 */     ExceptionTableEntry e = this.entries.get(nth);
/* 162 */     return e.catchType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCatchType(int nth, int value) {
/* 172 */     ExceptionTableEntry e = this.entries.get(nth);
/* 173 */     e.catchType = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(int index, ExceptionTable table, int offset) {
/* 184 */     int len = table.size();
/* 185 */     while (--len >= 0) {
/*     */       
/* 187 */       ExceptionTableEntry e = table.entries.get(len);
/* 188 */       add(index, e.startPc + offset, e.endPc + offset, e.handlerPc + offset, e.catchType);
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
/*     */   public void add(int index, int start, int end, int handler, int type) {
/* 203 */     if (start < end) {
/* 204 */       this.entries.add(index, new ExceptionTableEntry(start, end, handler, type));
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
/*     */   public void add(int start, int end, int handler, int type) {
/* 217 */     if (start < end) {
/* 218 */       this.entries.add(new ExceptionTableEntry(start, end, handler, type));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove(int index) {
/* 227 */     this.entries.remove(index);
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
/*     */   public ExceptionTable copy(ConstPool newCp, Map classnames) {
/* 240 */     ExceptionTable et = new ExceptionTable(newCp);
/* 241 */     ConstPool srcCp = this.constPool;
/* 242 */     int len = size();
/* 243 */     for (int i = 0; i < len; i++) {
/* 244 */       ExceptionTableEntry e = this.entries.get(i);
/* 245 */       int type = srcCp.copy(e.catchType, newCp, classnames);
/* 246 */       et.add(e.startPc, e.endPc, e.handlerPc, type);
/*     */     } 
/*     */     
/* 249 */     return et;
/*     */   }
/*     */   
/*     */   void shiftPc(int where, int gapLength, boolean exclusive) {
/* 253 */     int len = size();
/* 254 */     for (int i = 0; i < len; i++) {
/* 255 */       ExceptionTableEntry e = this.entries.get(i);
/* 256 */       e.startPc = shiftPc(e.startPc, where, gapLength, exclusive);
/* 257 */       e.endPc = shiftPc(e.endPc, where, gapLength, exclusive);
/* 258 */       e.handlerPc = shiftPc(e.handlerPc, where, gapLength, exclusive);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static int shiftPc(int pc, int where, int gapLength, boolean exclusive) {
/* 264 */     if (pc > where || (exclusive && pc == where)) {
/* 265 */       pc += gapLength;
/*     */     }
/* 267 */     return pc;
/*     */   }
/*     */   
/*     */   void write(DataOutputStream out) throws IOException {
/* 271 */     int len = size();
/* 272 */     out.writeShort(len);
/* 273 */     for (int i = 0; i < len; i++) {
/* 274 */       ExceptionTableEntry e = this.entries.get(i);
/* 275 */       out.writeShort(e.startPc);
/* 276 */       out.writeShort(e.endPc);
/* 277 */       out.writeShort(e.handlerPc);
/* 278 */       out.writeShort(e.catchType);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\ExceptionTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */