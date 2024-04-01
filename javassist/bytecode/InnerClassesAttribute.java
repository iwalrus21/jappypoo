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
/*     */ public class InnerClassesAttribute
/*     */   extends AttributeInfo
/*     */ {
/*     */   public static final String tag = "InnerClasses";
/*     */   
/*     */   InnerClassesAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
/*  35 */     super(cp, n, in);
/*     */   }
/*     */   
/*     */   private InnerClassesAttribute(ConstPool cp, byte[] info) {
/*  39 */     super(cp, "InnerClasses", info);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InnerClassesAttribute(ConstPool cp) {
/*  48 */     super(cp, "InnerClasses", new byte[2]);
/*  49 */     ByteArray.write16bit(0, get(), 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int tableLength() {
/*  55 */     return ByteArray.readU16bit(get(), 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int innerClassIndex(int nth) {
/*  61 */     return ByteArray.readU16bit(get(), nth * 8 + 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String innerClass(int nth) {
/*  71 */     int i = innerClassIndex(nth);
/*  72 */     if (i == 0) {
/*  73 */       return null;
/*     */     }
/*  75 */     return this.constPool.getClassInfo(i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInnerClassIndex(int nth, int index) {
/*  83 */     ByteArray.write16bit(index, get(), nth * 8 + 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int outerClassIndex(int nth) {
/*  90 */     return ByteArray.readU16bit(get(), nth * 8 + 4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String outerClass(int nth) {
/* 100 */     int i = outerClassIndex(nth);
/* 101 */     if (i == 0) {
/* 102 */       return null;
/*     */     }
/* 104 */     return this.constPool.getClassInfo(i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOuterClassIndex(int nth, int index) {
/* 112 */     ByteArray.write16bit(index, get(), nth * 8 + 4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int innerNameIndex(int nth) {
/* 119 */     return ByteArray.readU16bit(get(), nth * 8 + 6);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String innerName(int nth) {
/* 129 */     int i = innerNameIndex(nth);
/* 130 */     if (i == 0) {
/* 131 */       return null;
/*     */     }
/* 133 */     return this.constPool.getUtf8Info(i);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInnerNameIndex(int nth, int index) {
/* 141 */     ByteArray.write16bit(index, get(), nth * 8 + 6);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int accessFlags(int nth) {
/* 148 */     return ByteArray.readU16bit(get(), nth * 8 + 8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAccessFlags(int nth, int flags) {
/* 156 */     ByteArray.write16bit(flags, get(), nth * 8 + 8);
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
/*     */   public void append(String inner, String outer, String name, int flags) {
/* 168 */     int i = this.constPool.addClassInfo(inner);
/* 169 */     int o = this.constPool.addClassInfo(outer);
/* 170 */     int n = this.constPool.addUtf8Info(name);
/* 171 */     append(i, o, n, flags);
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
/*     */   public void append(int inner, int outer, int name, int flags) {
/* 183 */     byte[] data = get();
/* 184 */     int len = data.length;
/* 185 */     byte[] newData = new byte[len + 8];
/* 186 */     for (int i = 2; i < len; i++) {
/* 187 */       newData[i] = data[i];
/*     */     }
/* 189 */     int n = ByteArray.readU16bit(data, 0);
/* 190 */     ByteArray.write16bit(n + 1, newData, 0);
/*     */     
/* 192 */     ByteArray.write16bit(inner, newData, len);
/* 193 */     ByteArray.write16bit(outer, newData, len + 2);
/* 194 */     ByteArray.write16bit(name, newData, len + 4);
/* 195 */     ByteArray.write16bit(flags, newData, len + 6);
/*     */     
/* 197 */     set(newData);
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
/*     */   public int remove(int nth) {
/* 211 */     byte[] data = get();
/* 212 */     int len = data.length;
/* 213 */     if (len < 10) {
/* 214 */       return 0;
/*     */     }
/* 216 */     int n = ByteArray.readU16bit(data, 0);
/* 217 */     int nthPos = 2 + nth * 8;
/* 218 */     if (n <= nth) {
/* 219 */       return n;
/*     */     }
/* 221 */     byte[] newData = new byte[len - 8];
/* 222 */     ByteArray.write16bit(n - 1, newData, 0);
/* 223 */     int i = 2, j = 2;
/* 224 */     while (i < len) {
/* 225 */       if (i == nthPos) {
/* 226 */         i += 8; continue;
/*     */       } 
/* 228 */       newData[j++] = data[i++];
/*     */     } 
/* 230 */     set(newData);
/* 231 */     return n - 1;
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
/* 243 */     byte[] src = get();
/* 244 */     byte[] dest = new byte[src.length];
/* 245 */     ConstPool cp = getConstPool();
/* 246 */     InnerClassesAttribute attr = new InnerClassesAttribute(newCp, dest);
/* 247 */     int n = ByteArray.readU16bit(src, 0);
/* 248 */     ByteArray.write16bit(n, dest, 0);
/* 249 */     int j = 2;
/* 250 */     for (int i = 0; i < n; i++) {
/* 251 */       int innerClass = ByteArray.readU16bit(src, j);
/* 252 */       int outerClass = ByteArray.readU16bit(src, j + 2);
/* 253 */       int innerName = ByteArray.readU16bit(src, j + 4);
/* 254 */       int innerAccess = ByteArray.readU16bit(src, j + 6);
/*     */       
/* 256 */       if (innerClass != 0) {
/* 257 */         innerClass = cp.copy(innerClass, newCp, classnames);
/*     */       }
/* 259 */       ByteArray.write16bit(innerClass, dest, j);
/*     */       
/* 261 */       if (outerClass != 0) {
/* 262 */         outerClass = cp.copy(outerClass, newCp, classnames);
/*     */       }
/* 264 */       ByteArray.write16bit(outerClass, dest, j + 2);
/*     */       
/* 266 */       if (innerName != 0) {
/* 267 */         innerName = cp.copy(innerName, newCp, classnames);
/*     */       }
/* 269 */       ByteArray.write16bit(innerName, dest, j + 4);
/* 270 */       ByteArray.write16bit(innerAccess, dest, j + 6);
/* 271 */       j += 8;
/*     */     } 
/*     */     
/* 274 */     return attr;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\InnerClassesAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */