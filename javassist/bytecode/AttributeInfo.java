/*     */ package javassist.bytecode;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
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
/*     */ public class AttributeInfo
/*     */ {
/*     */   protected ConstPool constPool;
/*     */   int name;
/*     */   byte[] info;
/*     */   
/*     */   protected AttributeInfo(ConstPool cp, int attrname, byte[] attrinfo) {
/*  44 */     this.constPool = cp;
/*  45 */     this.name = attrname;
/*  46 */     this.info = attrinfo;
/*     */   }
/*     */   
/*     */   protected AttributeInfo(ConstPool cp, String attrname) {
/*  50 */     this(cp, attrname, (byte[])null);
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
/*     */   public AttributeInfo(ConstPool cp, String attrname, byte[] attrinfo) {
/*  62 */     this(cp, cp.addUtf8Info(attrname), attrinfo);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected AttributeInfo(ConstPool cp, int n, DataInputStream in) throws IOException {
/*  68 */     this.constPool = cp;
/*  69 */     this.name = n;
/*  70 */     int len = in.readInt();
/*  71 */     this.info = new byte[len];
/*  72 */     if (len > 0) {
/*  73 */       in.readFully(this.info);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static AttributeInfo read(ConstPool cp, DataInputStream in) throws IOException {
/*  79 */     int name = in.readUnsignedShort();
/*  80 */     String nameStr = cp.getUtf8Info(name);
/*  81 */     char first = nameStr.charAt(0);
/*  82 */     if (first < 'M') {
/*  83 */       if (first < 'E') {
/*  84 */         if (nameStr.equals("AnnotationDefault"))
/*  85 */           return new AnnotationDefaultAttribute(cp, name, in); 
/*  86 */         if (nameStr.equals("BootstrapMethods"))
/*  87 */           return new BootstrapMethodsAttribute(cp, name, in); 
/*  88 */         if (nameStr.equals("Code"))
/*  89 */           return new CodeAttribute(cp, name, in); 
/*  90 */         if (nameStr.equals("ConstantValue"))
/*  91 */           return new ConstantAttribute(cp, name, in); 
/*  92 */         if (nameStr.equals("Deprecated")) {
/*  93 */           return new DeprecatedAttribute(cp, name, in);
/*     */         }
/*     */       } else {
/*  96 */         if (nameStr.equals("EnclosingMethod"))
/*  97 */           return new EnclosingMethodAttribute(cp, name, in); 
/*  98 */         if (nameStr.equals("Exceptions"))
/*  99 */           return new ExceptionsAttribute(cp, name, in); 
/* 100 */         if (nameStr.equals("InnerClasses"))
/* 101 */           return new InnerClassesAttribute(cp, name, in); 
/* 102 */         if (nameStr.equals("LineNumberTable"))
/* 103 */           return new LineNumberAttribute(cp, name, in); 
/* 104 */         if (nameStr.equals("LocalVariableTable"))
/* 105 */           return new LocalVariableAttribute(cp, name, in); 
/* 106 */         if (nameStr.equals("LocalVariableTypeTable")) {
/* 107 */           return new LocalVariableTypeAttribute(cp, name, in);
/*     */         }
/*     */       }
/*     */     
/* 111 */     } else if (first < 'S') {
/*     */ 
/*     */       
/* 114 */       if (nameStr.equals("MethodParameters"))
/* 115 */         return new MethodParametersAttribute(cp, name, in); 
/* 116 */       if (nameStr.equals("RuntimeVisibleAnnotations") || nameStr
/* 117 */         .equals("RuntimeInvisibleAnnotations"))
/*     */       {
/* 119 */         return new AnnotationsAttribute(cp, name, in);
/*     */       }
/* 121 */       if (nameStr.equals("RuntimeVisibleParameterAnnotations") || nameStr
/* 122 */         .equals("RuntimeInvisibleParameterAnnotations"))
/* 123 */         return new ParameterAnnotationsAttribute(cp, name, in); 
/* 124 */       if (nameStr.equals("RuntimeVisibleTypeAnnotations") || nameStr
/* 125 */         .equals("RuntimeInvisibleTypeAnnotations")) {
/* 126 */         return new TypeAnnotationsAttribute(cp, name, in);
/*     */       }
/*     */     } else {
/* 129 */       if (nameStr.equals("Signature"))
/* 130 */         return new SignatureAttribute(cp, name, in); 
/* 131 */       if (nameStr.equals("SourceFile"))
/* 132 */         return new SourceFileAttribute(cp, name, in); 
/* 133 */       if (nameStr.equals("Synthetic"))
/* 134 */         return new SyntheticAttribute(cp, name, in); 
/* 135 */       if (nameStr.equals("StackMap"))
/* 136 */         return new StackMap(cp, name, in); 
/* 137 */       if (nameStr.equals("StackMapTable")) {
/* 138 */         return new StackMapTable(cp, name, in);
/*     */       }
/*     */     } 
/*     */     
/* 142 */     return new AttributeInfo(cp, name, in);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 149 */     return this.constPool.getUtf8Info(this.name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstPool getConstPool() {
/* 155 */     return this.constPool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/* 163 */     return this.info.length + 6;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] get() {
/* 173 */     return this.info;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(byte[] newinfo) {
/* 182 */     this.info = newinfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AttributeInfo copy(ConstPool newCp, Map classnames) {
/* 193 */     int s = this.info.length;
/* 194 */     byte[] srcInfo = this.info;
/* 195 */     byte[] newInfo = new byte[s];
/* 196 */     for (int i = 0; i < s; i++) {
/* 197 */       newInfo[i] = srcInfo[i];
/*     */     }
/* 199 */     return new AttributeInfo(newCp, getName(), newInfo);
/*     */   }
/*     */   
/*     */   void write(DataOutputStream out) throws IOException {
/* 203 */     out.writeShort(this.name);
/* 204 */     out.writeInt(this.info.length);
/* 205 */     if (this.info.length > 0)
/* 206 */       out.write(this.info); 
/*     */   }
/*     */   
/*     */   static int getLength(ArrayList<AttributeInfo> list) {
/* 210 */     int size = 0;
/* 211 */     int n = list.size();
/* 212 */     for (int i = 0; i < n; i++) {
/* 213 */       AttributeInfo attr = list.get(i);
/* 214 */       size += attr.length();
/*     */     } 
/*     */     
/* 217 */     return size;
/*     */   }
/*     */   
/*     */   static AttributeInfo lookup(ArrayList list, String name) {
/* 221 */     if (list == null) {
/* 222 */       return null;
/*     */     }
/* 224 */     ListIterator<AttributeInfo> iterator = list.listIterator();
/* 225 */     while (iterator.hasNext()) {
/* 226 */       AttributeInfo ai = iterator.next();
/* 227 */       if (ai.getName().equals(name)) {
/* 228 */         return ai;
/*     */       }
/*     */     } 
/* 231 */     return null;
/*     */   }
/*     */   
/*     */   static synchronized AttributeInfo remove(ArrayList list, String name) {
/* 235 */     if (list == null) {
/* 236 */       return null;
/*     */     }
/* 238 */     AttributeInfo removed = null;
/* 239 */     ListIterator<AttributeInfo> iterator = list.listIterator();
/* 240 */     while (iterator.hasNext()) {
/* 241 */       AttributeInfo ai = iterator.next();
/* 242 */       if (ai.getName().equals(name)) {
/* 243 */         iterator.remove();
/* 244 */         removed = ai;
/*     */       } 
/*     */     } 
/*     */     
/* 248 */     return removed;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static void writeAll(ArrayList<AttributeInfo> list, DataOutputStream out) throws IOException {
/* 254 */     if (list == null) {
/*     */       return;
/*     */     }
/* 257 */     int n = list.size();
/* 258 */     for (int i = 0; i < n; i++) {
/* 259 */       AttributeInfo attr = list.get(i);
/* 260 */       attr.write(out);
/*     */     } 
/*     */   }
/*     */   
/*     */   static ArrayList copyAll(ArrayList<AttributeInfo> list, ConstPool cp) {
/* 265 */     if (list == null) {
/* 266 */       return null;
/*     */     }
/* 268 */     ArrayList<AttributeInfo> newList = new ArrayList();
/* 269 */     int n = list.size();
/* 270 */     for (int i = 0; i < n; i++) {
/* 271 */       AttributeInfo attr = list.get(i);
/* 272 */       newList.add(attr.copy(cp, null));
/*     */     } 
/*     */     
/* 275 */     return newList;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void renameClass(String oldname, String newname) {}
/*     */ 
/*     */ 
/*     */   
/*     */   void renameClass(Map classnames) {}
/*     */ 
/*     */   
/*     */   static void renameClass(List attributes, String oldname, String newname) {
/* 288 */     Iterator<AttributeInfo> iterator = attributes.iterator();
/* 289 */     while (iterator.hasNext()) {
/* 290 */       AttributeInfo ai = iterator.next();
/* 291 */       ai.renameClass(oldname, newname);
/*     */     } 
/*     */   }
/*     */   
/*     */   static void renameClass(List attributes, Map classnames) {
/* 296 */     Iterator<AttributeInfo> iterator = attributes.iterator();
/* 297 */     while (iterator.hasNext()) {
/* 298 */       AttributeInfo ai = iterator.next();
/* 299 */       ai.renameClass(classnames);
/*     */     } 
/*     */   }
/*     */   
/*     */   void getRefClasses(Map classnames) {}
/*     */   
/*     */   static void getRefClasses(List attributes, Map classnames) {
/* 306 */     Iterator<AttributeInfo> iterator = attributes.iterator();
/* 307 */     while (iterator.hasNext()) {
/* 308 */       AttributeInfo ai = iterator.next();
/* 309 */       ai.getRefClasses(classnames);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\AttributeInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */