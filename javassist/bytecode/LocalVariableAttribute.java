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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LocalVariableAttribute
/*     */   extends AttributeInfo
/*     */ {
/*     */   public static final String tag = "LocalVariableTable";
/*     */   public static final String typeTag = "LocalVariableTypeTable";
/*     */   
/*     */   public LocalVariableAttribute(ConstPool cp) {
/*  41 */     super(cp, "LocalVariableTable", new byte[2]);
/*  42 */     ByteArray.write16bit(0, this.info, 0);
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
/*     */   public LocalVariableAttribute(ConstPool cp, String name) {
/*  57 */     super(cp, name, new byte[2]);
/*  58 */     ByteArray.write16bit(0, this.info, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   LocalVariableAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
/*  64 */     super(cp, n, in);
/*     */   }
/*     */   
/*     */   LocalVariableAttribute(ConstPool cp, String name, byte[] i) {
/*  68 */     super(cp, name, i);
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
/*     */   public void addEntry(int startPc, int length, int nameIndex, int descriptorIndex, int index) {
/*  82 */     int size = this.info.length;
/*  83 */     byte[] newInfo = new byte[size + 10];
/*  84 */     ByteArray.write16bit(tableLength() + 1, newInfo, 0);
/*  85 */     for (int i = 2; i < size; i++) {
/*  86 */       newInfo[i] = this.info[i];
/*     */     }
/*  88 */     ByteArray.write16bit(startPc, newInfo, size);
/*  89 */     ByteArray.write16bit(length, newInfo, size + 2);
/*  90 */     ByteArray.write16bit(nameIndex, newInfo, size + 4);
/*  91 */     ByteArray.write16bit(descriptorIndex, newInfo, size + 6);
/*  92 */     ByteArray.write16bit(index, newInfo, size + 8);
/*  93 */     this.info = newInfo;
/*     */   }
/*     */   
/*     */   void renameClass(String oldname, String newname) {
/*  97 */     ConstPool cp = getConstPool();
/*  98 */     int n = tableLength();
/*  99 */     for (int i = 0; i < n; i++) {
/* 100 */       int pos = i * 10 + 2;
/* 101 */       int index = ByteArray.readU16bit(this.info, pos + 6);
/* 102 */       if (index != 0) {
/* 103 */         String desc = cp.getUtf8Info(index);
/* 104 */         desc = renameEntry(desc, oldname, newname);
/* 105 */         ByteArray.write16bit(cp.addUtf8Info(desc), this.info, pos + 6);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   String renameEntry(String desc, String oldname, String newname) {
/* 111 */     return Descriptor.rename(desc, oldname, newname);
/*     */   }
/*     */   
/*     */   void renameClass(Map classnames) {
/* 115 */     ConstPool cp = getConstPool();
/* 116 */     int n = tableLength();
/* 117 */     for (int i = 0; i < n; i++) {
/* 118 */       int pos = i * 10 + 2;
/* 119 */       int index = ByteArray.readU16bit(this.info, pos + 6);
/* 120 */       if (index != 0) {
/* 121 */         String desc = cp.getUtf8Info(index);
/* 122 */         desc = renameEntry(desc, classnames);
/* 123 */         ByteArray.write16bit(cp.addUtf8Info(desc), this.info, pos + 6);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   String renameEntry(String desc, Map classnames) {
/* 129 */     return Descriptor.rename(desc, classnames);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void shiftIndex(int lessThan, int delta) {
/* 140 */     int size = this.info.length;
/* 141 */     for (int i = 2; i < size; i += 10) {
/* 142 */       int org = ByteArray.readU16bit(this.info, i + 8);
/* 143 */       if (org >= lessThan) {
/* 144 */         ByteArray.write16bit(org + delta, this.info, i + 8);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int tableLength() {
/* 153 */     return ByteArray.readU16bit(this.info, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int startPc(int i) {
/* 164 */     return ByteArray.readU16bit(this.info, i * 10 + 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int codeLength(int i) {
/* 175 */     return ByteArray.readU16bit(this.info, i * 10 + 4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void shiftPc(int where, int gapLength, boolean exclusive) {
/* 182 */     int n = tableLength();
/* 183 */     for (int i = 0; i < n; i++) {
/* 184 */       int pos = i * 10 + 2;
/* 185 */       int pc = ByteArray.readU16bit(this.info, pos);
/* 186 */       int len = ByteArray.readU16bit(this.info, pos + 2);
/*     */ 
/*     */ 
/*     */       
/* 190 */       if (pc > where || (exclusive && pc == where && pc != 0)) {
/* 191 */         ByteArray.write16bit(pc + gapLength, this.info, pos);
/* 192 */       } else if (pc + len > where || (exclusive && pc + len == where)) {
/* 193 */         ByteArray.write16bit(len + gapLength, this.info, pos + 2);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int nameIndex(int i) {
/* 204 */     return ByteArray.readU16bit(this.info, i * 10 + 6);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String variableName(int i) {
/* 214 */     return getConstPool().getUtf8Info(nameIndex(i));
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
/*     */   
/*     */   public int descriptorIndex(int i) {
/* 230 */     return ByteArray.readU16bit(this.info, i * 10 + 8);
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
/*     */   public int signatureIndex(int i) {
/* 244 */     return descriptorIndex(i);
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
/*     */   public String descriptor(int i) {
/* 258 */     return getConstPool().getUtf8Info(descriptorIndex(i));
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
/*     */ 
/*     */   
/*     */   public String signature(int i) {
/* 275 */     return descriptor(i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int index(int i) {
/* 285 */     return ByteArray.readU16bit(this.info, i * 10 + 10);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AttributeInfo copy(ConstPool newCp, Map classnames) {
/* 295 */     byte[] src = get();
/* 296 */     byte[] dest = new byte[src.length];
/* 297 */     ConstPool cp = getConstPool();
/* 298 */     LocalVariableAttribute attr = makeThisAttr(newCp, dest);
/* 299 */     int n = ByteArray.readU16bit(src, 0);
/* 300 */     ByteArray.write16bit(n, dest, 0);
/* 301 */     int j = 2;
/* 302 */     for (int i = 0; i < n; i++) {
/* 303 */       int start = ByteArray.readU16bit(src, j);
/* 304 */       int len = ByteArray.readU16bit(src, j + 2);
/* 305 */       int name = ByteArray.readU16bit(src, j + 4);
/* 306 */       int type = ByteArray.readU16bit(src, j + 6);
/* 307 */       int index = ByteArray.readU16bit(src, j + 8);
/*     */       
/* 309 */       ByteArray.write16bit(start, dest, j);
/* 310 */       ByteArray.write16bit(len, dest, j + 2);
/* 311 */       if (name != 0) {
/* 312 */         name = cp.copy(name, newCp, null);
/*     */       }
/* 314 */       ByteArray.write16bit(name, dest, j + 4);
/*     */       
/* 316 */       if (type != 0) {
/* 317 */         String sig = cp.getUtf8Info(type);
/* 318 */         sig = Descriptor.rename(sig, classnames);
/* 319 */         type = newCp.addUtf8Info(sig);
/*     */       } 
/*     */       
/* 322 */       ByteArray.write16bit(type, dest, j + 6);
/* 323 */       ByteArray.write16bit(index, dest, j + 8);
/* 324 */       j += 10;
/*     */     } 
/*     */     
/* 327 */     return attr;
/*     */   }
/*     */ 
/*     */   
/*     */   LocalVariableAttribute makeThisAttr(ConstPool cp, byte[] dest) {
/* 332 */     return new LocalVariableAttribute(cp, "LocalVariableTable", dest);
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\LocalVariableAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */