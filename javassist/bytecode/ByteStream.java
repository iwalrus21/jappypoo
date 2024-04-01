/*     */ package javassist.bytecode;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
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
/*     */ final class ByteStream
/*     */   extends OutputStream
/*     */ {
/*     */   private byte[] buf;
/*     */   private int count;
/*     */   
/*     */   public ByteStream() {
/*  26 */     this(32);
/*     */   }
/*     */   public ByteStream(int size) {
/*  29 */     this.buf = new byte[size];
/*  30 */     this.count = 0;
/*     */   }
/*     */   
/*  33 */   public int getPos() { return this.count; } public int size() {
/*  34 */     return this.count;
/*     */   }
/*     */   public void writeBlank(int len) {
/*  37 */     enlarge(len);
/*  38 */     this.count += len;
/*     */   }
/*     */   
/*     */   public void write(byte[] data) {
/*  42 */     write(data, 0, data.length);
/*     */   }
/*     */   
/*     */   public void write(byte[] data, int off, int len) {
/*  46 */     enlarge(len);
/*  47 */     System.arraycopy(data, off, this.buf, this.count, len);
/*  48 */     this.count += len;
/*     */   }
/*     */   
/*     */   public void write(int b) {
/*  52 */     enlarge(1);
/*  53 */     int oldCount = this.count;
/*  54 */     this.buf[oldCount] = (byte)b;
/*  55 */     this.count = oldCount + 1;
/*     */   }
/*     */   
/*     */   public void writeShort(int s) {
/*  59 */     enlarge(2);
/*  60 */     int oldCount = this.count;
/*  61 */     this.buf[oldCount] = (byte)(s >>> 8);
/*  62 */     this.buf[oldCount + 1] = (byte)s;
/*  63 */     this.count = oldCount + 2;
/*     */   }
/*     */   
/*     */   public void writeInt(int i) {
/*  67 */     enlarge(4);
/*  68 */     int oldCount = this.count;
/*  69 */     this.buf[oldCount] = (byte)(i >>> 24);
/*  70 */     this.buf[oldCount + 1] = (byte)(i >>> 16);
/*  71 */     this.buf[oldCount + 2] = (byte)(i >>> 8);
/*  72 */     this.buf[oldCount + 3] = (byte)i;
/*  73 */     this.count = oldCount + 4;
/*     */   }
/*     */   
/*     */   public void writeLong(long i) {
/*  77 */     enlarge(8);
/*  78 */     int oldCount = this.count;
/*  79 */     this.buf[oldCount] = (byte)(int)(i >>> 56L);
/*  80 */     this.buf[oldCount + 1] = (byte)(int)(i >>> 48L);
/*  81 */     this.buf[oldCount + 2] = (byte)(int)(i >>> 40L);
/*  82 */     this.buf[oldCount + 3] = (byte)(int)(i >>> 32L);
/*  83 */     this.buf[oldCount + 4] = (byte)(int)(i >>> 24L);
/*  84 */     this.buf[oldCount + 5] = (byte)(int)(i >>> 16L);
/*  85 */     this.buf[oldCount + 6] = (byte)(int)(i >>> 8L);
/*  86 */     this.buf[oldCount + 7] = (byte)(int)i;
/*  87 */     this.count = oldCount + 8;
/*     */   }
/*     */   
/*     */   public void writeFloat(float v) {
/*  91 */     writeInt(Float.floatToIntBits(v));
/*     */   }
/*     */   
/*     */   public void writeDouble(double v) {
/*  95 */     writeLong(Double.doubleToLongBits(v));
/*     */   }
/*     */   
/*     */   public void writeUTF(String s) {
/*  99 */     int sLen = s.length();
/* 100 */     int pos = this.count;
/* 101 */     enlarge(sLen + 2);
/*     */     
/* 103 */     byte[] buffer = this.buf;
/* 104 */     buffer[pos++] = (byte)(sLen >>> 8);
/* 105 */     buffer[pos++] = (byte)sLen;
/* 106 */     for (int i = 0; i < sLen; i++) {
/* 107 */       char c = s.charAt(i);
/* 108 */       if ('\001' <= c && c <= '') {
/* 109 */         buffer[pos++] = (byte)c;
/*     */       } else {
/* 111 */         writeUTF2(s, sLen, i);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 116 */     this.count = pos;
/*     */   }
/*     */   
/*     */   private void writeUTF2(String s, int sLen, int offset) {
/* 120 */     int size = sLen;
/* 121 */     for (int i = offset; i < sLen; i++) {
/* 122 */       int c = s.charAt(i);
/* 123 */       if (c > 2047) {
/* 124 */         size += 2;
/* 125 */       } else if (c == 0 || c > 127) {
/* 126 */         size++;
/*     */       } 
/*     */     } 
/* 129 */     if (size > 65535) {
/* 130 */       throw new RuntimeException("encoded string too long: " + sLen + size + " bytes");
/*     */     }
/*     */     
/* 133 */     enlarge(size + 2);
/* 134 */     int pos = this.count;
/* 135 */     byte[] buffer = this.buf;
/* 136 */     buffer[pos] = (byte)(size >>> 8);
/* 137 */     buffer[pos + 1] = (byte)size;
/* 138 */     pos += 2 + offset;
/* 139 */     for (int j = offset; j < sLen; j++) {
/* 140 */       int c = s.charAt(j);
/* 141 */       if (1 <= c && c <= 127) {
/* 142 */         buffer[pos++] = (byte)c;
/* 143 */       } else if (c > 2047) {
/* 144 */         buffer[pos] = (byte)(0xE0 | c >> 12 & 0xF);
/* 145 */         buffer[pos + 1] = (byte)(0x80 | c >> 6 & 0x3F);
/* 146 */         buffer[pos + 2] = (byte)(0x80 | c & 0x3F);
/* 147 */         pos += 3;
/*     */       } else {
/*     */         
/* 150 */         buffer[pos] = (byte)(0xC0 | c >> 6 & 0x1F);
/* 151 */         buffer[pos + 1] = (byte)(0x80 | c & 0x3F);
/* 152 */         pos += 2;
/*     */       } 
/*     */     } 
/*     */     
/* 156 */     this.count = pos;
/*     */   }
/*     */   
/*     */   public void write(int pos, int value) {
/* 160 */     this.buf[pos] = (byte)value;
/*     */   }
/*     */   
/*     */   public void writeShort(int pos, int value) {
/* 164 */     this.buf[pos] = (byte)(value >>> 8);
/* 165 */     this.buf[pos + 1] = (byte)value;
/*     */   }
/*     */   
/*     */   public void writeInt(int pos, int value) {
/* 169 */     this.buf[pos] = (byte)(value >>> 24);
/* 170 */     this.buf[pos + 1] = (byte)(value >>> 16);
/* 171 */     this.buf[pos + 2] = (byte)(value >>> 8);
/* 172 */     this.buf[pos + 3] = (byte)value;
/*     */   }
/*     */   
/*     */   public byte[] toByteArray() {
/* 176 */     byte[] buf2 = new byte[this.count];
/* 177 */     System.arraycopy(this.buf, 0, buf2, 0, this.count);
/* 178 */     return buf2;
/*     */   }
/*     */   
/*     */   public void writeTo(OutputStream out) throws IOException {
/* 182 */     out.write(this.buf, 0, this.count);
/*     */   }
/*     */   
/*     */   public void enlarge(int delta) {
/* 186 */     int newCount = this.count + delta;
/* 187 */     if (newCount > this.buf.length) {
/* 188 */       int newLen = this.buf.length << 1;
/* 189 */       byte[] newBuf = new byte[(newLen > newCount) ? newLen : newCount];
/* 190 */       System.arraycopy(this.buf, 0, newBuf, 0, this.count);
/* 191 */       this.buf = newBuf;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\ByteStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */