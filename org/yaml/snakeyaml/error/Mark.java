/*     */ package org.yaml.snakeyaml.error;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.yaml.snakeyaml.scanner.Constant;
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
/*     */ public final class Mark
/*     */   implements Serializable
/*     */ {
/*     */   private String name;
/*     */   private int index;
/*     */   private int line;
/*     */   private int column;
/*     */   private String buffer;
/*     */   private int pointer;
/*     */   
/*     */   public Mark(String name, int index, int line, int column, String buffer, int pointer) {
/*  36 */     this.name = name;
/*  37 */     this.index = index;
/*  38 */     this.line = line;
/*  39 */     this.column = column;
/*  40 */     this.buffer = buffer;
/*  41 */     this.pointer = pointer;
/*     */   }
/*     */   
/*     */   private boolean isLineBreak(int c) {
/*  45 */     return Constant.NULL_OR_LINEBR.has(c);
/*     */   }
/*     */   
/*     */   public String get_snippet(int indent, int max_length) {
/*  49 */     if (this.buffer == null) {
/*  50 */       return null;
/*     */     }
/*  52 */     float half = (max_length / 2 - 1);
/*  53 */     int start = this.pointer;
/*  54 */     String head = "";
/*  55 */     while (start > 0 && !isLineBreak(this.buffer.codePointAt(start - 1))) {
/*  56 */       start--;
/*  57 */       if ((this.pointer - start) > half) {
/*  58 */         head = " ... ";
/*  59 */         start += 5;
/*     */         break;
/*     */       } 
/*     */     } 
/*  63 */     String tail = "";
/*  64 */     int end = this.pointer;
/*  65 */     while (end < this.buffer.length() && !isLineBreak(this.buffer.codePointAt(end))) {
/*  66 */       end++;
/*  67 */       if ((end - this.pointer) > half) {
/*  68 */         tail = " ... ";
/*  69 */         end -= 5;
/*     */         break;
/*     */       } 
/*     */     } 
/*  73 */     String snippet = this.buffer.substring(start, end);
/*  74 */     StringBuilder result = new StringBuilder(); int i;
/*  75 */     for (i = 0; i < indent; i++) {
/*  76 */       result.append(" ");
/*     */     }
/*  78 */     result.append(head);
/*  79 */     result.append(snippet);
/*  80 */     result.append(tail);
/*  81 */     result.append("\n");
/*  82 */     for (i = 0; i < indent + this.pointer - start + head.length(); i++) {
/*  83 */       result.append(" ");
/*     */     }
/*  85 */     result.append("^");
/*  86 */     return result.toString();
/*     */   }
/*     */   
/*     */   public String get_snippet() {
/*  90 */     return get_snippet(4, 75);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  95 */     String snippet = get_snippet();
/*  96 */     StringBuilder where = new StringBuilder(" in ");
/*  97 */     where.append(this.name);
/*  98 */     where.append(", line ");
/*  99 */     where.append(this.line + 1);
/* 100 */     where.append(", column ");
/* 101 */     where.append(this.column + 1);
/* 102 */     if (snippet != null) {
/* 103 */       where.append(":\n");
/* 104 */       where.append(snippet);
/*     */     } 
/* 106 */     return where.toString();
/*     */   }
/*     */   
/*     */   public String getName() {
/* 110 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLine() {
/* 118 */     return this.line;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColumn() {
/* 126 */     return this.column;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIndex() {
/* 134 */     return this.index;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\yaml\snakeyaml\error\Mark.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */