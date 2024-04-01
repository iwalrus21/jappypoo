/*     */ package javassist.bytecode;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
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
/*     */ public class ExceptionsAttribute
/*     */   extends AttributeInfo
/*     */ {
/*     */   public static final String tag = "Exceptions";
/*     */   
/*     */   ExceptionsAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
/*  35 */     super(cp, n, in);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ExceptionsAttribute(ConstPool cp, ExceptionsAttribute src, Map classnames) {
/*  46 */     super(cp, "Exceptions");
/*  47 */     copyFrom(src, classnames);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExceptionsAttribute(ConstPool cp) {
/*  56 */     super(cp, "Exceptions");
/*  57 */     byte[] data = new byte[2];
/*  58 */     data[1] = 0; data[0] = 0;
/*  59 */     this.info = data;
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
/*     */   public AttributeInfo copy(ConstPool newCp, Map classnames) {
/*  71 */     return new ExceptionsAttribute(newCp, this, classnames);
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
/*     */   private void copyFrom(ExceptionsAttribute srcAttr, Map classnames) {
/*  83 */     ConstPool srcCp = srcAttr.constPool;
/*  84 */     ConstPool destCp = this.constPool;
/*  85 */     byte[] src = srcAttr.info;
/*  86 */     int num = src.length;
/*  87 */     byte[] dest = new byte[num];
/*  88 */     dest[0] = src[0];
/*  89 */     dest[1] = src[1];
/*  90 */     for (int i = 2; i < num; i += 2) {
/*  91 */       int index = ByteArray.readU16bit(src, i);
/*  92 */       ByteArray.write16bit(srcCp.copy(index, destCp, classnames), dest, i);
/*     */     } 
/*     */ 
/*     */     
/*  96 */     this.info = dest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getExceptionIndexes() {
/* 103 */     byte[] blist = this.info;
/* 104 */     int n = blist.length;
/* 105 */     if (n <= 2) {
/* 106 */       return null;
/*     */     }
/* 108 */     int[] elist = new int[n / 2 - 1];
/* 109 */     int k = 0;
/* 110 */     for (int j = 2; j < n; j += 2) {
/* 111 */       elist[k++] = (blist[j] & 0xFF) << 8 | blist[j + 1] & 0xFF;
/*     */     }
/* 113 */     return elist;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getExceptions() {
/* 120 */     byte[] blist = this.info;
/* 121 */     int n = blist.length;
/* 122 */     if (n <= 2) {
/* 123 */       return null;
/*     */     }
/* 125 */     String[] elist = new String[n / 2 - 1];
/* 126 */     int k = 0;
/* 127 */     for (int j = 2; j < n; j += 2) {
/* 128 */       int index = (blist[j] & 0xFF) << 8 | blist[j + 1] & 0xFF;
/* 129 */       elist[k++] = this.constPool.getClassInfo(index);
/*     */     } 
/*     */     
/* 132 */     return elist;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExceptionIndexes(int[] elist) {
/* 139 */     int n = elist.length;
/* 140 */     byte[] blist = new byte[n * 2 + 2];
/* 141 */     ByteArray.write16bit(n, blist, 0);
/* 142 */     for (int i = 0; i < n; i++) {
/* 143 */       ByteArray.write16bit(elist[i], blist, i * 2 + 2);
/*     */     }
/* 145 */     this.info = blist;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExceptions(String[] elist) {
/* 152 */     int n = elist.length;
/* 153 */     byte[] blist = new byte[n * 2 + 2];
/* 154 */     ByteArray.write16bit(n, blist, 0);
/* 155 */     for (int i = 0; i < n; i++) {
/* 156 */       ByteArray.write16bit(this.constPool.addClassInfo(elist[i]), blist, i * 2 + 2);
/*     */     }
/*     */     
/* 159 */     this.info = blist;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int tableLength() {
/* 165 */     return this.info.length / 2 - 1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getException(int nth) {
/* 171 */     int index = nth * 2 + 2;
/* 172 */     return (this.info[index] & 0xFF) << 8 | this.info[index + 1] & 0xFF;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\ExceptionsAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */