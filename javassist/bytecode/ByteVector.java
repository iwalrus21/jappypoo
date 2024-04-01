/*    */ package javassist.bytecode;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class ByteVector
/*    */   implements Cloneable
/*    */ {
/* 27 */   private byte[] buffer = new byte[64];
/* 28 */   private int size = 0;
/*    */ 
/*    */   
/*    */   public Object clone() throws CloneNotSupportedException {
/* 32 */     ByteVector bv = (ByteVector)super.clone();
/* 33 */     bv.buffer = (byte[])this.buffer.clone();
/* 34 */     return bv;
/*    */   }
/*    */   public final int getSize() {
/* 37 */     return this.size;
/*    */   }
/*    */   public final byte[] copy() {
/* 40 */     byte[] b = new byte[this.size];
/* 41 */     System.arraycopy(this.buffer, 0, b, 0, this.size);
/* 42 */     return b;
/*    */   }
/*    */   
/*    */   public int read(int offset) {
/* 46 */     if (offset < 0 || this.size <= offset) {
/* 47 */       throw new ArrayIndexOutOfBoundsException(offset);
/*    */     }
/* 49 */     return this.buffer[offset];
/*    */   }
/*    */   
/*    */   public void write(int offset, int value) {
/* 53 */     if (offset < 0 || this.size <= offset) {
/* 54 */       throw new ArrayIndexOutOfBoundsException(offset);
/*    */     }
/* 56 */     this.buffer[offset] = (byte)value;
/*    */   }
/*    */   
/*    */   public void add(int code) {
/* 60 */     addGap(1);
/* 61 */     this.buffer[this.size - 1] = (byte)code;
/*    */   }
/*    */   
/*    */   public void add(int b1, int b2) {
/* 65 */     addGap(2);
/* 66 */     this.buffer[this.size - 2] = (byte)b1;
/* 67 */     this.buffer[this.size - 1] = (byte)b2;
/*    */   }
/*    */   
/*    */   public void add(int b1, int b2, int b3, int b4) {
/* 71 */     addGap(4);
/* 72 */     this.buffer[this.size - 4] = (byte)b1;
/* 73 */     this.buffer[this.size - 3] = (byte)b2;
/* 74 */     this.buffer[this.size - 2] = (byte)b3;
/* 75 */     this.buffer[this.size - 1] = (byte)b4;
/*    */   }
/*    */   
/*    */   public void addGap(int length) {
/* 79 */     if (this.size + length > this.buffer.length) {
/* 80 */       int newSize = this.size << 1;
/* 81 */       if (newSize < this.size + length) {
/* 82 */         newSize = this.size + length;
/*    */       }
/* 84 */       byte[] newBuf = new byte[newSize];
/* 85 */       System.arraycopy(this.buffer, 0, newBuf, 0, this.size);
/* 86 */       this.buffer = newBuf;
/*    */     } 
/*    */     
/* 89 */     this.size += length;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\ByteVector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */