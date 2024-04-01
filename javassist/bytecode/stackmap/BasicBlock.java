/*     */ package javassist.bytecode.stackmap;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.CodeAttribute;
/*     */ import javassist.bytecode.CodeIterator;
/*     */ import javassist.bytecode.ExceptionTable;
/*     */ import javassist.bytecode.MethodInfo;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BasicBlock
/*     */ {
/*     */   protected int position;
/*     */   protected int length;
/*     */   protected int incoming;
/*     */   protected BasicBlock[] exit;
/*     */   protected boolean stop;
/*     */   protected Catch toCatch;
/*     */   
/*     */   static class JsrBytecode
/*     */     extends BadBytecode
/*     */   {
/*     */     JsrBytecode() {
/*  31 */       super("JSR");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BasicBlock(int pos) {
/*  41 */     this.position = pos;
/*  42 */     this.length = 0;
/*  43 */     this.incoming = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static BasicBlock find(BasicBlock[] blocks, int pos) throws BadBytecode {
/*  49 */     for (int i = 0; i < blocks.length; i++) {
/*  50 */       int iPos = (blocks[i]).position;
/*  51 */       if (iPos <= pos && pos < iPos + (blocks[i]).length) {
/*  52 */         return blocks[i];
/*     */       }
/*     */     } 
/*  55 */     throw new BadBytecode("no basic block at " + pos);
/*     */   }
/*     */   
/*     */   public static class Catch { public Catch next;
/*     */     public BasicBlock body;
/*     */     public int typeIndex;
/*     */     
/*     */     Catch(BasicBlock b, int i, Catch c) {
/*  63 */       this.body = b;
/*  64 */       this.typeIndex = i;
/*  65 */       this.next = c;
/*     */     } }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  70 */     StringBuffer sbuf = new StringBuffer();
/*  71 */     String cname = getClass().getName();
/*  72 */     int i = cname.lastIndexOf('.');
/*  73 */     sbuf.append((i < 0) ? cname : cname.substring(i + 1));
/*  74 */     sbuf.append("[");
/*  75 */     toString2(sbuf);
/*  76 */     sbuf.append("]");
/*  77 */     return sbuf.toString();
/*     */   }
/*     */   
/*     */   protected void toString2(StringBuffer sbuf) {
/*  81 */     sbuf.append("pos=").append(this.position).append(", len=")
/*  82 */       .append(this.length).append(", in=").append(this.incoming)
/*  83 */       .append(", exit{");
/*  84 */     if (this.exit != null) {
/*  85 */       for (int i = 0; i < this.exit.length; i++) {
/*  86 */         sbuf.append((this.exit[i]).position).append(",");
/*     */       }
/*     */     }
/*  89 */     sbuf.append("}, {");
/*  90 */     Catch th = this.toCatch;
/*  91 */     while (th != null) {
/*  92 */       sbuf.append("(").append(th.body.position).append(", ")
/*  93 */         .append(th.typeIndex).append("), ");
/*  94 */       th = th.next;
/*     */     } 
/*     */     
/*  97 */     sbuf.append("}");
/*     */   }
/*     */ 
/*     */   
/*     */   static class Mark
/*     */     implements Comparable
/*     */   {
/*     */     int position;
/*     */     
/*     */     BasicBlock block;
/*     */     BasicBlock[] jump;
/*     */     boolean alwaysJmp;
/*     */     int size;
/*     */     BasicBlock.Catch catcher;
/*     */     
/*     */     Mark(int p) {
/* 113 */       this.position = p;
/* 114 */       this.block = null;
/* 115 */       this.jump = null;
/* 116 */       this.alwaysJmp = false;
/* 117 */       this.size = 0;
/* 118 */       this.catcher = null;
/*     */     }
/*     */     
/*     */     public int compareTo(Object obj) {
/* 122 */       if (obj instanceof Mark) {
/* 123 */         int pos = ((Mark)obj).position;
/* 124 */         return this.position - pos;
/*     */       } 
/*     */       
/* 127 */       return -1;
/*     */     }
/*     */     
/*     */     void setJump(BasicBlock[] bb, int s, boolean always) {
/* 131 */       this.jump = bb;
/* 132 */       this.size = s;
/* 133 */       this.alwaysJmp = always;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Maker
/*     */   {
/*     */     protected BasicBlock makeBlock(int pos) {
/* 142 */       return new BasicBlock(pos);
/*     */     }
/*     */     
/*     */     protected BasicBlock[] makeArray(int size) {
/* 146 */       return new BasicBlock[size];
/*     */     }
/*     */     
/*     */     private BasicBlock[] makeArray(BasicBlock b) {
/* 150 */       BasicBlock[] array = makeArray(1);
/* 151 */       array[0] = b;
/* 152 */       return array;
/*     */     }
/*     */     
/*     */     private BasicBlock[] makeArray(BasicBlock b1, BasicBlock b2) {
/* 156 */       BasicBlock[] array = makeArray(2);
/* 157 */       array[0] = b1;
/* 158 */       array[1] = b2;
/* 159 */       return array;
/*     */     }
/*     */     
/*     */     public BasicBlock[] make(MethodInfo minfo) throws BadBytecode {
/* 163 */       CodeAttribute ca = minfo.getCodeAttribute();
/* 164 */       if (ca == null) {
/* 165 */         return null;
/*     */       }
/* 167 */       CodeIterator ci = ca.iterator();
/* 168 */       return make(ci, 0, ci.getCodeLength(), ca.getExceptionTable());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public BasicBlock[] make(CodeIterator ci, int begin, int end, ExceptionTable et) throws BadBytecode {
/* 175 */       HashMap marks = makeMarks(ci, begin, end, et);
/* 176 */       BasicBlock[] bb = makeBlocks(marks);
/* 177 */       addCatchers(bb, et);
/* 178 */       return bb;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private BasicBlock.Mark makeMark(HashMap table, int pos) {
/* 184 */       return makeMark0(table, pos, true, true);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private BasicBlock.Mark makeMark(HashMap table, int pos, BasicBlock[] jump, int size, boolean always) {
/* 192 */       BasicBlock.Mark m = makeMark0(table, pos, false, false);
/* 193 */       m.setJump(jump, size, always);
/* 194 */       return m;
/*     */     }
/*     */ 
/*     */     
/*     */     private BasicBlock.Mark makeMark0(HashMap<Integer, BasicBlock.Mark> table, int pos, boolean isBlockBegin, boolean isTarget) {
/* 199 */       Integer p = new Integer(pos);
/* 200 */       BasicBlock.Mark m = (BasicBlock.Mark)table.get(p);
/* 201 */       if (m == null) {
/* 202 */         m = new BasicBlock.Mark(pos);
/* 203 */         table.put(p, m);
/*     */       } 
/*     */       
/* 206 */       if (isBlockBegin) {
/* 207 */         if (m.block == null) {
/* 208 */           m.block = makeBlock(pos);
/*     */         }
/* 210 */         if (isTarget) {
/* 211 */           m.block.incoming++;
/*     */         }
/*     */       } 
/* 214 */       return m;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private HashMap makeMarks(CodeIterator ci, int begin, int end, ExceptionTable et) throws BadBytecode {
/* 221 */       ci.begin();
/* 222 */       ci.move(begin);
/* 223 */       HashMap<Object, Object> marks = new HashMap<Object, Object>();
/* 224 */       while (ci.hasNext()) {
/* 225 */         int index = ci.next();
/* 226 */         if (index >= end) {
/*     */           break;
/*     */         }
/* 229 */         int op = ci.byteAt(index);
/* 230 */         if ((153 <= op && op <= 166) || op == 198 || op == 199) {
/*     */           
/* 232 */           BasicBlock.Mark to = makeMark(marks, index + ci.s16bitAt(index + 1));
/* 233 */           BasicBlock.Mark next = makeMark(marks, index + 3);
/* 234 */           makeMark(marks, index, makeArray(to.block, next.block), 3, false); continue;
/*     */         } 
/* 236 */         if (167 <= op && op <= 171) {
/* 237 */           int pos; int low; int ncases; int high; BasicBlock[] to; int i; int p; BasicBlock[] arrayOfBasicBlock1; int n; int j; int k; int m; int i1; switch (op) {
/*     */             case 167:
/* 239 */               makeGoto(marks, index, index + ci.s16bitAt(index + 1), 3);
/*     */               continue;
/*     */             case 168:
/* 242 */               makeJsr(marks, index, index + ci.s16bitAt(index + 1), 3);
/*     */               continue;
/*     */             case 169:
/* 245 */               makeMark(marks, index, null, 2, true);
/*     */               continue;
/*     */             case 170:
/* 248 */               pos = (index & 0xFFFFFFFC) + 4;
/* 249 */               low = ci.s32bitAt(pos + 4);
/* 250 */               high = ci.s32bitAt(pos + 8);
/* 251 */               i = high - low + 1;
/* 252 */               arrayOfBasicBlock1 = makeArray(i + 1);
/* 253 */               arrayOfBasicBlock1[0] = (makeMark(marks, index + ci.s32bitAt(pos))).block;
/* 254 */               j = pos + 12;
/* 255 */               m = j + i * 4;
/* 256 */               i1 = 1;
/* 257 */               while (j < m) {
/* 258 */                 arrayOfBasicBlock1[i1++] = (makeMark(marks, index + ci.s32bitAt(j))).block;
/* 259 */                 j += 4;
/*     */               } 
/* 261 */               makeMark(marks, index, arrayOfBasicBlock1, m - index, true);
/*     */               continue;
/*     */             case 171:
/* 264 */               pos = (index & 0xFFFFFFFC) + 4;
/* 265 */               ncases = ci.s32bitAt(pos + 4);
/* 266 */               to = makeArray(ncases + 1);
/* 267 */               to[0] = (makeMark(marks, index + ci.s32bitAt(pos))).block;
/* 268 */               p = pos + 8 + 4;
/* 269 */               n = p + ncases * 8 - 4;
/* 270 */               k = 1;
/* 271 */               while (p < n) {
/* 272 */                 to[k++] = (makeMark(marks, index + ci.s32bitAt(p))).block;
/* 273 */                 p += 8;
/*     */               } 
/* 275 */               makeMark(marks, index, to, n - index, true); continue;
/*     */           }  continue;
/*     */         } 
/* 278 */         if ((172 <= op && op <= 177) || op == 191) {
/* 279 */           makeMark(marks, index, null, 1, true); continue;
/* 280 */         }  if (op == 200) {
/* 281 */           makeGoto(marks, index, index + ci.s32bitAt(index + 1), 5); continue;
/* 282 */         }  if (op == 201) {
/* 283 */           makeJsr(marks, index, index + ci.s32bitAt(index + 1), 5); continue;
/* 284 */         }  if (op == 196 && ci.byteAt(index + 1) == 169) {
/* 285 */           makeMark(marks, index, null, 4, true);
/*     */         }
/*     */       } 
/* 288 */       if (et != null) {
/* 289 */         int i = et.size();
/* 290 */         while (--i >= 0) {
/* 291 */           makeMark0(marks, et.startPc(i), true, false);
/* 292 */           makeMark(marks, et.handlerPc(i));
/*     */         } 
/*     */       } 
/*     */       
/* 296 */       return marks;
/*     */     }
/*     */     
/*     */     private void makeGoto(HashMap marks, int pos, int target, int size) {
/* 300 */       BasicBlock.Mark to = makeMark(marks, target);
/* 301 */       BasicBlock[] jumps = makeArray(to.block);
/* 302 */       makeMark(marks, pos, jumps, size, true);
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
/*     */     protected void makeJsr(HashMap marks, int pos, int target, int size) throws BadBytecode {
/* 316 */       throw new BasicBlock.JsrBytecode();
/*     */     }
/*     */     
/*     */     private BasicBlock[] makeBlocks(HashMap markTable) {
/*     */       BasicBlock prev;
/* 321 */       BasicBlock.Mark[] marks = (BasicBlock.Mark[])markTable.values().toArray((Object[])new BasicBlock.Mark[markTable.size()]);
/* 322 */       Arrays.sort((Object[])marks);
/* 323 */       ArrayList<BasicBlock> blocks = new ArrayList();
/* 324 */       int i = 0;
/*     */       
/* 326 */       if (marks.length > 0 && (marks[0]).position == 0 && (marks[0]).block != null) {
/* 327 */         prev = getBBlock(marks[i++]);
/*     */       } else {
/* 329 */         prev = makeBlock(0);
/*     */       } 
/* 331 */       blocks.add(prev);
/* 332 */       while (i < marks.length) {
/* 333 */         BasicBlock.Mark m = marks[i++];
/* 334 */         BasicBlock bb = getBBlock(m);
/* 335 */         if (bb == null) {
/*     */           
/* 337 */           if (prev.length > 0) {
/*     */             
/* 339 */             prev = makeBlock(prev.position + prev.length);
/* 340 */             blocks.add(prev);
/*     */           } 
/*     */           
/* 343 */           prev.length = m.position + m.size - prev.position;
/* 344 */           prev.exit = m.jump;
/* 345 */           prev.stop = m.alwaysJmp;
/*     */           
/*     */           continue;
/*     */         } 
/* 349 */         if (prev.length == 0) {
/* 350 */           prev.length = m.position - prev.position;
/* 351 */           bb.incoming++;
/* 352 */           prev.exit = makeArray(bb);
/*     */ 
/*     */         
/*     */         }
/* 356 */         else if (prev.position + prev.length < m.position) {
/*     */           
/* 358 */           prev = makeBlock(prev.position + prev.length);
/* 359 */           blocks.add(prev);
/* 360 */           prev.length = m.position - prev.position;
/*     */ 
/*     */           
/* 363 */           prev.stop = true;
/* 364 */           prev.exit = makeArray(bb);
/*     */         } 
/*     */ 
/*     */         
/* 368 */         blocks.add(bb);
/* 369 */         prev = bb;
/*     */       } 
/*     */ 
/*     */       
/* 373 */       return blocks.<BasicBlock>toArray(makeArray(blocks.size()));
/*     */     }
/*     */     
/*     */     private static BasicBlock getBBlock(BasicBlock.Mark m) {
/* 377 */       BasicBlock b = m.block;
/* 378 */       if (b != null && m.size > 0) {
/* 379 */         b.exit = m.jump;
/* 380 */         b.length = m.size;
/* 381 */         b.stop = m.alwaysJmp;
/*     */       } 
/*     */       
/* 384 */       return b;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private void addCatchers(BasicBlock[] blocks, ExceptionTable et) throws BadBytecode {
/* 390 */       if (et == null) {
/*     */         return;
/*     */       }
/* 393 */       int i = et.size();
/* 394 */       while (--i >= 0) {
/* 395 */         BasicBlock handler = BasicBlock.find(blocks, et.handlerPc(i));
/* 396 */         int start = et.startPc(i);
/* 397 */         int end = et.endPc(i);
/* 398 */         int type = et.catchType(i);
/* 399 */         handler.incoming--;
/* 400 */         for (int k = 0; k < blocks.length; k++) {
/* 401 */           BasicBlock bb = blocks[k];
/* 402 */           int iPos = bb.position;
/* 403 */           if (start <= iPos && iPos < end) {
/* 404 */             bb.toCatch = new BasicBlock.Catch(handler, type, bb.toCatch);
/* 405 */             handler.incoming++;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\stackmap\BasicBlock.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */