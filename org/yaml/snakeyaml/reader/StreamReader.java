/*     */ package org.yaml.snakeyaml.reader;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.nio.charset.Charset;
/*     */ import org.yaml.snakeyaml.error.Mark;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
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
/*     */ public class StreamReader
/*     */ {
/*     */   private String name;
/*     */   private final Reader stream;
/*  32 */   private int pointer = 0;
/*     */   private boolean eof = true;
/*     */   private String buffer;
/*  35 */   private int index = 0;
/*  36 */   private int line = 0;
/*  37 */   private int column = 0;
/*     */   
/*     */   private char[] data;
/*     */   private static final int BUFFER_SIZE = 1025;
/*     */   
/*     */   public StreamReader(String stream) {
/*  43 */     this.name = "'string'";
/*  44 */     this.buffer = "";
/*  45 */     checkPrintable(stream);
/*  46 */     this.buffer = stream + "\000";
/*  47 */     this.stream = null;
/*  48 */     this.eof = true;
/*  49 */     this.data = null;
/*     */   }
/*     */   
/*     */   public StreamReader(Reader reader) {
/*  53 */     this.name = "'reader'";
/*  54 */     this.buffer = "";
/*  55 */     this.stream = reader;
/*  56 */     this.eof = false;
/*  57 */     this.data = new char[1025];
/*  58 */     update();
/*     */   }
/*     */   
/*     */   void checkPrintable(String data) {
/*  62 */     int length = data.length();
/*  63 */     for (int offset = 0; offset < length; ) {
/*  64 */       int codePoint = data.codePointAt(offset);
/*     */       
/*  66 */       if (!isPrintable(codePoint)) {
/*  67 */         throw new ReaderException(this.name, offset, codePoint, "special characters are not allowed");
/*     */       }
/*     */ 
/*     */       
/*  71 */       offset += Character.charCount(codePoint);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean isPrintable(String data) {
/*  76 */     int length = data.length();
/*  77 */     for (int offset = 0; offset < length; ) {
/*  78 */       int codePoint = data.codePointAt(offset);
/*     */       
/*  80 */       if (!isPrintable(codePoint)) {
/*  81 */         return false;
/*     */       }
/*     */       
/*  84 */       offset += Character.charCount(codePoint);
/*     */     } 
/*     */     
/*  87 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean isPrintable(int c) {
/*  91 */     return ((c >= 32 && c <= 126) || c == 9 || c == 10 || c == 13 || c == 133 || (c >= 160 && c <= 55295) || (c >= 57344 && c <= 65533) || (c >= 65536 && c <= 1114111));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mark getMark() {
/*  98 */     return new Mark(this.name, this.index, this.line, this.column, this.buffer, this.pointer);
/*     */   }
/*     */   
/*     */   public void forward() {
/* 102 */     forward(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void forward(int length) {
/* 113 */     for (int i = 0; i < length; i++) {
/* 114 */       if (this.pointer == this.buffer.length()) {
/* 115 */         update();
/*     */       }
/* 117 */       if (this.pointer == this.buffer.length()) {
/*     */         break;
/*     */       }
/*     */       
/* 121 */       int c = this.buffer.codePointAt(this.pointer);
/* 122 */       this.pointer += Character.charCount(c);
/* 123 */       this.index += Character.charCount(c);
/* 124 */       if (Constant.LINEBR.has(c) || (c == 13 && this.buffer.charAt(this.pointer) != '\n')) {
/* 125 */         this.line++;
/* 126 */         this.column = 0;
/* 127 */       } else if (c != 65279) {
/* 128 */         this.column++;
/*     */       } 
/*     */     } 
/*     */     
/* 132 */     if (this.pointer == this.buffer.length()) {
/* 133 */       update();
/*     */     }
/*     */   }
/*     */   
/*     */   public int peek() {
/* 138 */     if (this.pointer == this.buffer.length()) {
/* 139 */       update();
/*     */     }
/* 141 */     if (this.pointer == this.buffer.length()) {
/* 142 */       return -1;
/*     */     }
/*     */     
/* 145 */     return this.buffer.codePointAt(this.pointer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int peek(int index) {
/* 155 */     int codePoint, offset = 0;
/* 156 */     int nextIndex = 0;
/*     */     
/*     */     do {
/* 159 */       if (this.pointer + offset == this.buffer.length()) {
/* 160 */         update();
/*     */       }
/* 162 */       if (this.pointer + offset == this.buffer.length()) {
/* 163 */         return -1;
/*     */       }
/*     */       
/* 166 */       codePoint = this.buffer.codePointAt(this.pointer + offset);
/* 167 */       offset += Character.charCount(codePoint);
/* 168 */       ++nextIndex;
/*     */     }
/* 170 */     while (nextIndex <= index);
/*     */     
/* 172 */     return codePoint;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String prefix(int length) {
/* 182 */     StringBuilder builder = new StringBuilder();
/*     */     
/* 184 */     int offset = 0;
/* 185 */     int resultLength = 0;
/* 186 */     while (resultLength < length) {
/* 187 */       if (this.pointer + offset == this.buffer.length()) {
/* 188 */         update();
/*     */       }
/* 190 */       if (this.pointer + offset == this.buffer.length()) {
/*     */         break;
/*     */       }
/*     */       
/* 194 */       int c = this.buffer.codePointAt(this.pointer + offset);
/* 195 */       builder.appendCodePoint(c);
/* 196 */       offset += Character.charCount(c);
/* 197 */       resultLength++;
/*     */     } 
/*     */     
/* 200 */     return builder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String prefixForward(int length) {
/* 209 */     String prefix = prefix(length);
/* 210 */     this.pointer += prefix.length();
/* 211 */     this.index += prefix.length();
/*     */     
/* 213 */     this.column += length;
/* 214 */     return prefix;
/*     */   }
/*     */   
/*     */   private void update() {
/* 218 */     if (!this.eof) {
/* 219 */       this.buffer = this.buffer.substring(this.pointer);
/* 220 */       this.pointer = 0;
/*     */       try {
/* 222 */         boolean eofDetected = false;
/* 223 */         int converted = this.stream.read(this.data, 0, 1024);
/* 224 */         if (converted > 0) {
/* 225 */           if (Character.isHighSurrogate(this.data[converted - 1])) {
/* 226 */             int oneMore = this.stream.read(this.data, converted, 1);
/* 227 */             if (oneMore != -1) {
/* 228 */               converted += oneMore;
/*     */             } else {
/* 230 */               eofDetected = true;
/*     */             } 
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 242 */           StringBuilder builder = (new StringBuilder(this.buffer.length() + converted)).append(this.buffer).append(this.data, 0, converted);
/* 243 */           if (eofDetected) {
/* 244 */             this.eof = true;
/* 245 */             builder.append(false);
/*     */           } 
/* 247 */           this.buffer = builder.toString();
/* 248 */           checkPrintable(this.buffer);
/*     */         } else {
/* 250 */           this.eof = true;
/* 251 */           this.buffer += "\000";
/*     */         } 
/* 253 */       } catch (IOException ioe) {
/* 254 */         throw new YAMLException(ioe);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getColumn() {
/* 260 */     return this.column;
/*     */   }
/*     */   
/*     */   public Charset getEncoding() {
/* 264 */     return Charset.forName(((UnicodeReader)this.stream).getEncoding());
/*     */   }
/*     */   
/*     */   public int getIndex() {
/* 268 */     return this.index;
/*     */   }
/*     */   
/*     */   public int getLine() {
/* 272 */     return this.line;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\yaml\snakeyaml\reader\StreamReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */