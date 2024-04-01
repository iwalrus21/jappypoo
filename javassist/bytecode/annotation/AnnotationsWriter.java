/*     */ package javassist.bytecode.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import javassist.bytecode.ByteArray;
/*     */ import javassist.bytecode.ConstPool;
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
/*     */ public class AnnotationsWriter
/*     */ {
/*     */   protected OutputStream output;
/*     */   private ConstPool pool;
/*     */   
/*     */   public AnnotationsWriter(OutputStream os, ConstPool cp) {
/*  71 */     this.output = os;
/*  72 */     this.pool = cp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstPool getConstPool() {
/*  79 */     return this.pool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  87 */     this.output.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void numParameters(int num) throws IOException {
/*  97 */     this.output.write(num);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void numAnnotations(int num) throws IOException {
/* 107 */     write16bit(num);
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
/*     */   
/*     */   public void annotation(String type, int numMemberValuePairs) throws IOException {
/* 122 */     annotation(this.pool.addUtf8Info(type), numMemberValuePairs);
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
/*     */   
/*     */   public void annotation(int typeIndex, int numMemberValuePairs) throws IOException {
/* 137 */     write16bit(typeIndex);
/* 138 */     write16bit(numMemberValuePairs);
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
/*     */   public void memberValuePair(String memberName) throws IOException {
/* 151 */     memberValuePair(this.pool.addUtf8Info(memberName));
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
/*     */   public void memberValuePair(int memberNameIndex) throws IOException {
/* 165 */     write16bit(memberNameIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void constValueIndex(boolean value) throws IOException {
/* 175 */     constValueIndex(90, this.pool.addIntegerInfo(value ? 1 : 0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void constValueIndex(byte value) throws IOException {
/* 185 */     constValueIndex(66, this.pool.addIntegerInfo(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void constValueIndex(char value) throws IOException {
/* 195 */     constValueIndex(67, this.pool.addIntegerInfo(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void constValueIndex(short value) throws IOException {
/* 205 */     constValueIndex(83, this.pool.addIntegerInfo(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void constValueIndex(int value) throws IOException {
/* 215 */     constValueIndex(73, this.pool.addIntegerInfo(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void constValueIndex(long value) throws IOException {
/* 225 */     constValueIndex(74, this.pool.addLongInfo(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void constValueIndex(float value) throws IOException {
/* 235 */     constValueIndex(70, this.pool.addFloatInfo(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void constValueIndex(double value) throws IOException {
/* 245 */     constValueIndex(68, this.pool.addDoubleInfo(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void constValueIndex(String value) throws IOException {
/* 255 */     constValueIndex(115, this.pool.addUtf8Info(value));
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
/*     */   public void constValueIndex(int tag, int index) throws IOException {
/* 269 */     this.output.write(tag);
/* 270 */     write16bit(index);
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
/*     */   public void enumConstValue(String typeName, String constName) throws IOException {
/* 283 */     enumConstValue(this.pool.addUtf8Info(typeName), this.pool
/* 284 */         .addUtf8Info(constName));
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
/*     */   
/*     */   public void enumConstValue(int typeNameIndex, int constNameIndex) throws IOException {
/* 299 */     this.output.write(101);
/* 300 */     write16bit(typeNameIndex);
/* 301 */     write16bit(constNameIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void classInfoIndex(String name) throws IOException {
/* 311 */     classInfoIndex(this.pool.addUtf8Info(name));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void classInfoIndex(int index) throws IOException {
/* 321 */     this.output.write(99);
/* 322 */     write16bit(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void annotationValue() throws IOException {
/* 331 */     this.output.write(64);
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
/*     */   public void arrayValue(int numValues) throws IOException {
/* 345 */     this.output.write(91);
/* 346 */     write16bit(numValues);
/*     */   }
/*     */   
/*     */   protected void write16bit(int value) throws IOException {
/* 350 */     byte[] buf = new byte[2];
/* 351 */     ByteArray.write16bit(value, buf, 0);
/* 352 */     this.output.write(buf);
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\annotation\AnnotationsWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */