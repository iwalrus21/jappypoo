/*     */ package javassist.bytecode.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Set;
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtClass;
/*     */ import javassist.CtMethod;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.bytecode.ConstPool;
/*     */ import javassist.bytecode.Descriptor;
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
/*     */ public class Annotation
/*     */ {
/*     */   ConstPool pool;
/*     */   int typeIndex;
/*     */   LinkedHashMap members;
/*     */   
/*     */   static class Pair
/*     */   {
/*     */     int name;
/*     */     MemberValue value;
/*     */   }
/*     */   
/*     */   public Annotation(int type, ConstPool cp) {
/*  72 */     this.pool = cp;
/*  73 */     this.typeIndex = type;
/*  74 */     this.members = null;
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
/*     */   public Annotation(String typeName, ConstPool cp) {
/*  87 */     this(cp.addUtf8Info(Descriptor.of(typeName)), cp);
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
/*     */   public Annotation(ConstPool cp, CtClass clazz) throws NotFoundException {
/* 103 */     this(cp.addUtf8Info(Descriptor.of(clazz.getName())), cp);
/*     */     
/* 105 */     if (!clazz.isInterface()) {
/* 106 */       throw new RuntimeException("Only interfaces are allowed for Annotation creation.");
/*     */     }
/*     */     
/* 109 */     CtMethod[] methods = clazz.getDeclaredMethods();
/* 110 */     if (methods.length > 0) {
/* 111 */       this.members = new LinkedHashMap<Object, Object>();
/*     */     }
/*     */     
/* 114 */     for (int i = 0; i < methods.length; i++) {
/* 115 */       CtClass returnType = methods[i].getReturnType();
/* 116 */       addMemberValue(methods[i].getName(), 
/* 117 */           createMemberValue(cp, returnType));
/*     */     } 
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
/*     */   public static MemberValue createMemberValue(ConstPool cp, CtClass type) throws NotFoundException {
/* 133 */     if (type == CtClass.booleanType)
/* 134 */       return new BooleanMemberValue(cp); 
/* 135 */     if (type == CtClass.byteType)
/* 136 */       return new ByteMemberValue(cp); 
/* 137 */     if (type == CtClass.charType)
/* 138 */       return new CharMemberValue(cp); 
/* 139 */     if (type == CtClass.shortType)
/* 140 */       return new ShortMemberValue(cp); 
/* 141 */     if (type == CtClass.intType)
/* 142 */       return new IntegerMemberValue(cp); 
/* 143 */     if (type == CtClass.longType)
/* 144 */       return new LongMemberValue(cp); 
/* 145 */     if (type == CtClass.floatType)
/* 146 */       return new FloatMemberValue(cp); 
/* 147 */     if (type == CtClass.doubleType)
/* 148 */       return new DoubleMemberValue(cp); 
/* 149 */     if (type.getName().equals("java.lang.Class"))
/* 150 */       return new ClassMemberValue(cp); 
/* 151 */     if (type.getName().equals("java.lang.String"))
/* 152 */       return new StringMemberValue(cp); 
/* 153 */     if (type.isArray()) {
/* 154 */       CtClass arrayType = type.getComponentType();
/* 155 */       MemberValue member = createMemberValue(cp, arrayType);
/* 156 */       return new ArrayMemberValue(member, cp);
/*     */     } 
/* 158 */     if (type.isInterface()) {
/* 159 */       Annotation info = new Annotation(cp, type);
/* 160 */       return new AnnotationMemberValue(info, cp);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 166 */     EnumMemberValue emv = new EnumMemberValue(cp);
/* 167 */     emv.setType(type.getName());
/* 168 */     return emv;
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
/*     */   public void addMemberValue(int nameIndex, MemberValue value) {
/* 182 */     Pair p = new Pair();
/* 183 */     p.name = nameIndex;
/* 184 */     p.value = value;
/* 185 */     addMemberValue(p);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMemberValue(String name, MemberValue value) {
/* 195 */     Pair p = new Pair();
/* 196 */     p.name = this.pool.addUtf8Info(name);
/* 197 */     p.value = value;
/* 198 */     if (this.members == null) {
/* 199 */       this.members = new LinkedHashMap<Object, Object>();
/*     */     }
/* 201 */     this.members.put(name, p);
/*     */   }
/*     */   
/*     */   private void addMemberValue(Pair pair) {
/* 205 */     String name = this.pool.getUtf8Info(pair.name);
/* 206 */     if (this.members == null) {
/* 207 */       this.members = new LinkedHashMap<Object, Object>();
/*     */     }
/* 209 */     this.members.put(name, pair);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 216 */     StringBuffer buf = new StringBuffer("@");
/* 217 */     buf.append(getTypeName());
/* 218 */     if (this.members != null) {
/* 219 */       buf.append("(");
/* 220 */       Iterator<String> mit = this.members.keySet().iterator();
/* 221 */       while (mit.hasNext()) {
/* 222 */         String name = mit.next();
/* 223 */         buf.append(name).append("=").append(getMemberValue(name));
/* 224 */         if (mit.hasNext())
/* 225 */           buf.append(", "); 
/*     */       } 
/* 227 */       buf.append(")");
/*     */     } 
/*     */     
/* 230 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTypeName() {
/* 239 */     return Descriptor.toClassName(this.pool.getUtf8Info(this.typeIndex));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set getMemberNames() {
/* 248 */     if (this.members == null) {
/* 249 */       return null;
/*     */     }
/* 251 */     return this.members.keySet();
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
/*     */ 
/*     */   
/*     */   public MemberValue getMemberValue(String name) {
/* 270 */     if (this.members == null) {
/* 271 */       return null;
/*     */     }
/* 273 */     Pair p = (Pair)this.members.get(name);
/* 274 */     if (p == null) {
/* 275 */       return null;
/*     */     }
/* 277 */     return p.value;
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
/*     */   
/*     */   public Object toAnnotationType(ClassLoader cl, ClassPool cp) throws ClassNotFoundException, NoSuchClassError {
/* 295 */     return AnnotationImpl.make(cl, 
/* 296 */         MemberValue.loadClass(cl, getTypeName()), cp, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(AnnotationsWriter writer) throws IOException {
/* 307 */     String typeName = this.pool.getUtf8Info(this.typeIndex);
/* 308 */     if (this.members == null) {
/* 309 */       writer.annotation(typeName, 0);
/*     */       
/*     */       return;
/*     */     } 
/* 313 */     writer.annotation(typeName, this.members.size());
/* 314 */     Iterator<Pair> it = this.members.values().iterator();
/* 315 */     while (it.hasNext()) {
/* 316 */       Pair pair = it.next();
/* 317 */       writer.memberValuePair(pair.name);
/* 318 */       pair.value.write(writer);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 327 */     if (obj == this)
/* 328 */       return true; 
/* 329 */     if (obj == null || !(obj instanceof Annotation)) {
/* 330 */       return false;
/*     */     }
/* 332 */     Annotation other = (Annotation)obj;
/*     */     
/* 334 */     if (!getTypeName().equals(other.getTypeName())) {
/* 335 */       return false;
/*     */     }
/* 337 */     LinkedHashMap otherMembers = other.members;
/* 338 */     if (this.members == otherMembers)
/* 339 */       return true; 
/* 340 */     if (this.members == null) {
/* 341 */       return (otherMembers == null);
/*     */     }
/* 343 */     if (otherMembers == null) {
/* 344 */       return false;
/*     */     }
/* 346 */     return this.members.equals(otherMembers);
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\annotation\Annotation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */