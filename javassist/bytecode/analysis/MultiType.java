/*     */ package javassist.bytecode.analysis;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javassist.CtClass;
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
/*     */ public class MultiType
/*     */   extends Type
/*     */ {
/*     */   private Map interfaces;
/*     */   private Type resolved;
/*     */   private Type potentialClass;
/*     */   private MultiType mergeSource;
/*     */   private boolean changed = false;
/*     */   
/*     */   public MultiType(Map interfaces) {
/*  57 */     this(interfaces, (Type)null);
/*     */   }
/*     */   
/*     */   public MultiType(Map interfaces, Type potentialClass) {
/*  61 */     super(null);
/*  62 */     this.interfaces = interfaces;
/*  63 */     this.potentialClass = potentialClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtClass getCtClass() {
/*  71 */     if (this.resolved != null) {
/*  72 */       return this.resolved.getCtClass();
/*     */     }
/*  74 */     return Type.OBJECT.getCtClass();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type getComponent() {
/*  81 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSize() {
/*  88 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isArray() {
/*  95 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean popChanged() {
/* 102 */     boolean changed = this.changed;
/* 103 */     this.changed = false;
/* 104 */     return changed;
/*     */   }
/*     */   
/*     */   public boolean isAssignableFrom(Type type) {
/* 108 */     throw new UnsupportedOperationException("Not implemented");
/*     */   }
/*     */   
/*     */   public boolean isAssignableTo(Type type) {
/* 112 */     if (this.resolved != null) {
/* 113 */       return type.isAssignableFrom(this.resolved);
/*     */     }
/* 115 */     if (Type.OBJECT.equals(type)) {
/* 116 */       return true;
/*     */     }
/* 118 */     if (this.potentialClass != null && !type.isAssignableFrom(this.potentialClass)) {
/* 119 */       this.potentialClass = null;
/*     */     }
/* 121 */     Map map = mergeMultiAndSingle(this, type);
/*     */     
/* 123 */     if (map.size() == 1 && this.potentialClass == null) {
/*     */       
/* 125 */       this.resolved = Type.get(map.values().iterator().next());
/* 126 */       propogateResolved();
/*     */       
/* 128 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 132 */     if (map.size() >= 1) {
/* 133 */       this.interfaces = map;
/* 134 */       propogateState();
/*     */       
/* 136 */       return true;
/*     */     } 
/*     */     
/* 139 */     if (this.potentialClass != null) {
/* 140 */       this.resolved = this.potentialClass;
/* 141 */       propogateResolved();
/*     */       
/* 143 */       return true;
/*     */     } 
/*     */     
/* 146 */     return false;
/*     */   }
/*     */   
/*     */   private void propogateState() {
/* 150 */     MultiType source = this.mergeSource;
/* 151 */     while (source != null) {
/* 152 */       source.interfaces = this.interfaces;
/* 153 */       source.potentialClass = this.potentialClass;
/* 154 */       source = source.mergeSource;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void propogateResolved() {
/* 159 */     MultiType source = this.mergeSource;
/* 160 */     while (source != null) {
/* 161 */       source.resolved = this.resolved;
/* 162 */       source = source.mergeSource;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReference() {
/* 172 */     return true;
/*     */   }
/*     */   
/*     */   private Map getAllMultiInterfaces(MultiType type) {
/* 176 */     Map<Object, Object> map = new HashMap<Object, Object>();
/*     */     
/* 178 */     Iterator<CtClass> iter = type.interfaces.values().iterator();
/* 179 */     while (iter.hasNext()) {
/* 180 */       CtClass intf = iter.next();
/* 181 */       map.put(intf.getName(), intf);
/* 182 */       getAllInterfaces(intf, map);
/*     */     } 
/*     */     
/* 185 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   private Map mergeMultiInterfaces(MultiType type1, MultiType type2) {
/* 190 */     Map map1 = getAllMultiInterfaces(type1);
/* 191 */     Map map2 = getAllMultiInterfaces(type2);
/*     */     
/* 193 */     return findCommonInterfaces(map1, map2);
/*     */   }
/*     */   
/*     */   private Map mergeMultiAndSingle(MultiType multi, Type single) {
/* 197 */     Map map1 = getAllMultiInterfaces(multi);
/* 198 */     Map map2 = getAllInterfaces(single.getCtClass(), null);
/*     */     
/* 200 */     return findCommonInterfaces(map1, map2);
/*     */   }
/*     */   
/*     */   private boolean inMergeSource(MultiType source) {
/* 204 */     while (source != null) {
/* 205 */       if (source == this) {
/* 206 */         return true;
/*     */       }
/* 208 */       source = source.mergeSource;
/*     */     } 
/*     */     
/* 211 */     return false;
/*     */   }
/*     */   public Type merge(Type type) {
/*     */     Map merged;
/* 215 */     if (this == type) {
/* 216 */       return this;
/*     */     }
/* 218 */     if (type == UNINIT) {
/* 219 */       return this;
/*     */     }
/* 221 */     if (type == BOGUS) {
/* 222 */       return BOGUS;
/*     */     }
/* 224 */     if (type == null) {
/* 225 */       return this;
/*     */     }
/* 227 */     if (this.resolved != null) {
/* 228 */       return this.resolved.merge(type);
/*     */     }
/* 230 */     if (this.potentialClass != null) {
/* 231 */       Type mergePotential = this.potentialClass.merge(type);
/* 232 */       if (!mergePotential.equals(this.potentialClass) || mergePotential.popChanged()) {
/* 233 */         this.potentialClass = Type.OBJECT.equals(mergePotential) ? null : mergePotential;
/* 234 */         this.changed = true;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 240 */     if (type instanceof MultiType) {
/* 241 */       MultiType multi = (MultiType)type;
/*     */       
/* 243 */       if (multi.resolved != null) {
/* 244 */         merged = mergeMultiAndSingle(this, multi.resolved);
/*     */       } else {
/* 246 */         merged = mergeMultiInterfaces(multi, this);
/* 247 */         if (!inMergeSource(multi))
/* 248 */           this.mergeSource = multi; 
/*     */       } 
/*     */     } else {
/* 251 */       merged = mergeMultiAndSingle(this, type);
/*     */     } 
/*     */ 
/*     */     
/* 255 */     if (merged.size() > 1 || (merged.size() == 1 && this.potentialClass != null)) {
/*     */       
/* 257 */       if (merged.size() != this.interfaces.size()) {
/* 258 */         this.changed = true;
/* 259 */       } else if (!this.changed) {
/* 260 */         Iterator iter = merged.keySet().iterator();
/* 261 */         while (iter.hasNext()) {
/* 262 */           if (!this.interfaces.containsKey(iter.next()))
/* 263 */             this.changed = true; 
/*     */         } 
/*     */       } 
/* 266 */       this.interfaces = merged;
/* 267 */       propogateState();
/*     */       
/* 269 */       return this;
/*     */     } 
/*     */     
/* 272 */     if (merged.size() == 1) {
/* 273 */       this.resolved = Type.get(merged.values().iterator().next());
/* 274 */     } else if (this.potentialClass != null) {
/* 275 */       this.resolved = this.potentialClass;
/*     */     } else {
/* 277 */       this.resolved = OBJECT;
/*     */     } 
/*     */     
/* 280 */     propogateResolved();
/*     */     
/* 282 */     return this.resolved;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 286 */     if (!(o instanceof MultiType)) {
/* 287 */       return false;
/*     */     }
/* 289 */     MultiType multi = (MultiType)o;
/* 290 */     if (this.resolved != null)
/* 291 */       return this.resolved.equals(multi.resolved); 
/* 292 */     if (multi.resolved != null) {
/* 293 */       return false;
/*     */     }
/* 295 */     return this.interfaces.keySet().equals(multi.interfaces.keySet());
/*     */   }
/*     */   
/*     */   public String toString() {
/* 299 */     if (this.resolved != null) {
/* 300 */       return this.resolved.toString();
/*     */     }
/* 302 */     StringBuffer buffer = new StringBuffer("{");
/* 303 */     Iterator iter = this.interfaces.keySet().iterator();
/* 304 */     while (iter.hasNext()) {
/* 305 */       buffer.append(iter.next());
/* 306 */       buffer.append(", ");
/*     */     } 
/* 308 */     buffer.setLength(buffer.length() - 2);
/* 309 */     if (this.potentialClass != null)
/* 310 */       buffer.append(", *").append(this.potentialClass.toString()); 
/* 311 */     buffer.append("}");
/* 312 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\analysis\MultiType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */