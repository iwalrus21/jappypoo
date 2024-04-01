/*     */ package javassist.bytecode;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public final class FieldInfo
/*     */ {
/*     */   ConstPool constPool;
/*     */   int accessFlags;
/*     */   int name;
/*     */   String cachedName;
/*     */   String cachedType;
/*     */   int descriptor;
/*     */   ArrayList attribute;
/*     */   
/*     */   private FieldInfo(ConstPool cp) {
/*  49 */     this.constPool = cp;
/*  50 */     this.accessFlags = 0;
/*  51 */     this.attribute = null;
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
/*     */   public FieldInfo(ConstPool cp, String fieldName, String desc) {
/*  64 */     this(cp);
/*  65 */     this.name = cp.addUtf8Info(fieldName);
/*  66 */     this.cachedName = fieldName;
/*  67 */     this.descriptor = cp.addUtf8Info(desc);
/*     */   }
/*     */   
/*     */   FieldInfo(ConstPool cp, DataInputStream in) throws IOException {
/*  71 */     this(cp);
/*  72 */     read(in);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  79 */     return getName() + " " + getDescriptor();
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
/*     */   void compact(ConstPool cp) {
/*  91 */     this.name = cp.addUtf8Info(getName());
/*  92 */     this.descriptor = cp.addUtf8Info(getDescriptor());
/*  93 */     this.attribute = AttributeInfo.copyAll(this.attribute, cp);
/*  94 */     this.constPool = cp;
/*     */   }
/*     */   
/*     */   void prune(ConstPool cp) {
/*  98 */     ArrayList<AttributeInfo> newAttributes = new ArrayList();
/*     */     
/* 100 */     AttributeInfo invisibleAnnotations = getAttribute("RuntimeInvisibleAnnotations");
/* 101 */     if (invisibleAnnotations != null) {
/* 102 */       invisibleAnnotations = invisibleAnnotations.copy(cp, null);
/* 103 */       newAttributes.add(invisibleAnnotations);
/*     */     } 
/*     */ 
/*     */     
/* 107 */     AttributeInfo visibleAnnotations = getAttribute("RuntimeVisibleAnnotations");
/* 108 */     if (visibleAnnotations != null) {
/* 109 */       visibleAnnotations = visibleAnnotations.copy(cp, null);
/* 110 */       newAttributes.add(visibleAnnotations);
/*     */     } 
/*     */ 
/*     */     
/* 114 */     AttributeInfo signature = getAttribute("Signature");
/* 115 */     if (signature != null) {
/* 116 */       signature = signature.copy(cp, null);
/* 117 */       newAttributes.add(signature);
/*     */     } 
/*     */     
/* 120 */     int index = getConstantValue();
/* 121 */     if (index != 0) {
/* 122 */       index = this.constPool.copy(index, cp, null);
/* 123 */       newAttributes.add(new ConstantAttribute(cp, index));
/*     */     } 
/*     */     
/* 126 */     this.attribute = newAttributes;
/* 127 */     this.name = cp.addUtf8Info(getName());
/* 128 */     this.descriptor = cp.addUtf8Info(getDescriptor());
/* 129 */     this.constPool = cp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstPool getConstPool() {
/* 137 */     return this.constPool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 144 */     if (this.cachedName == null) {
/* 145 */       this.cachedName = this.constPool.getUtf8Info(this.name);
/*     */     }
/* 147 */     return this.cachedName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String newName) {
/* 154 */     this.name = this.constPool.addUtf8Info(newName);
/* 155 */     this.cachedName = newName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAccessFlags() {
/* 164 */     return this.accessFlags;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAccessFlags(int acc) {
/* 173 */     this.accessFlags = acc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescriptor() {
/* 182 */     return this.constPool.getUtf8Info(this.descriptor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDescriptor(String desc) {
/* 191 */     if (!desc.equals(getDescriptor())) {
/* 192 */       this.descriptor = this.constPool.addUtf8Info(desc);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getConstantValue() {
/* 202 */     if ((this.accessFlags & 0x8) == 0) {
/* 203 */       return 0;
/*     */     }
/*     */     
/* 206 */     ConstantAttribute attr = (ConstantAttribute)getAttribute("ConstantValue");
/* 207 */     if (attr == null) {
/* 208 */       return 0;
/*     */     }
/* 210 */     return attr.getConstantValue();
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
/*     */   public List getAttributes() {
/* 224 */     if (this.attribute == null) {
/* 225 */       this.attribute = new ArrayList();
/*     */     }
/* 227 */     return this.attribute;
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
/*     */   public AttributeInfo getAttribute(String name) {
/* 243 */     return AttributeInfo.lookup(this.attribute, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AttributeInfo removeAttribute(String name) {
/* 254 */     return AttributeInfo.remove(this.attribute, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAttribute(AttributeInfo info) {
/* 264 */     if (this.attribute == null) {
/* 265 */       this.attribute = new ArrayList();
/*     */     }
/* 267 */     AttributeInfo.remove(this.attribute, info.getName());
/* 268 */     this.attribute.add(info);
/*     */   }
/*     */   
/*     */   private void read(DataInputStream in) throws IOException {
/* 272 */     this.accessFlags = in.readUnsignedShort();
/* 273 */     this.name = in.readUnsignedShort();
/* 274 */     this.descriptor = in.readUnsignedShort();
/* 275 */     int n = in.readUnsignedShort();
/* 276 */     this.attribute = new ArrayList();
/* 277 */     for (int i = 0; i < n; i++)
/* 278 */       this.attribute.add(AttributeInfo.read(this.constPool, in)); 
/*     */   }
/*     */   
/*     */   void write(DataOutputStream out) throws IOException {
/* 282 */     out.writeShort(this.accessFlags);
/* 283 */     out.writeShort(this.name);
/* 284 */     out.writeShort(this.descriptor);
/* 285 */     if (this.attribute == null) {
/* 286 */       out.writeShort(0);
/*     */     } else {
/* 288 */       out.writeShort(this.attribute.size());
/* 289 */       AttributeInfo.writeAll(this.attribute, out);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\FieldInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */