/*     */ package javassist.bytecode.analysis;
/*     */ 
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtClass;
/*     */ import javassist.NotFoundException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MultiArrayType
/*     */   extends Type
/*     */ {
/*     */   private MultiType component;
/*     */   private int dims;
/*     */   
/*     */   public MultiArrayType(MultiType component, int dims) {
/*  32 */     super(null);
/*  33 */     this.component = component;
/*  34 */     this.dims = dims;
/*     */   }
/*     */   
/*     */   public CtClass getCtClass() {
/*  38 */     CtClass clazz = this.component.getCtClass();
/*  39 */     if (clazz == null) {
/*  40 */       return null;
/*     */     }
/*  42 */     ClassPool pool = clazz.getClassPool();
/*  43 */     if (pool == null) {
/*  44 */       pool = ClassPool.getDefault();
/*     */     }
/*  46 */     String name = arrayName(clazz.getName(), this.dims);
/*     */     
/*     */     try {
/*  49 */       return pool.get(name);
/*  50 */     } catch (NotFoundException e) {
/*  51 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   boolean popChanged() {
/*  56 */     return this.component.popChanged();
/*     */   }
/*     */   
/*     */   public int getDimensions() {
/*  60 */     return this.dims;
/*     */   }
/*     */   
/*     */   public Type getComponent() {
/*  64 */     return (this.dims == 1) ? this.component : new MultiArrayType(this.component, this.dims - 1);
/*     */   }
/*     */   
/*     */   public int getSize() {
/*  68 */     return 1;
/*     */   }
/*     */   
/*     */   public boolean isArray() {
/*  72 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isAssignableFrom(Type type) {
/*  76 */     throw new UnsupportedOperationException("Not implemented");
/*     */   }
/*     */   
/*     */   public boolean isReference() {
/*  80 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isAssignableTo(Type type) {
/*  84 */     if (eq(type.getCtClass(), Type.OBJECT.getCtClass())) {
/*  85 */       return true;
/*     */     }
/*  87 */     if (eq(type.getCtClass(), Type.CLONEABLE.getCtClass())) {
/*  88 */       return true;
/*     */     }
/*  90 */     if (eq(type.getCtClass(), Type.SERIALIZABLE.getCtClass())) {
/*  91 */       return true;
/*     */     }
/*  93 */     if (!type.isArray()) {
/*  94 */       return false;
/*     */     }
/*  96 */     Type typeRoot = getRootComponent(type);
/*  97 */     int typeDims = type.getDimensions();
/*     */     
/*  99 */     if (typeDims > this.dims) {
/* 100 */       return false;
/*     */     }
/* 102 */     if (typeDims < this.dims) {
/* 103 */       if (eq(typeRoot.getCtClass(), Type.OBJECT.getCtClass())) {
/* 104 */         return true;
/*     */       }
/* 106 */       if (eq(typeRoot.getCtClass(), Type.CLONEABLE.getCtClass())) {
/* 107 */         return true;
/*     */       }
/* 109 */       if (eq(typeRoot.getCtClass(), Type.SERIALIZABLE.getCtClass())) {
/* 110 */         return true;
/*     */       }
/* 112 */       return false;
/*     */     } 
/*     */     
/* 115 */     return this.component.isAssignableTo(typeRoot);
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 119 */     if (!(o instanceof MultiArrayType))
/* 120 */       return false; 
/* 121 */     MultiArrayType multi = (MultiArrayType)o;
/*     */     
/* 123 */     return (this.component.equals(multi.component) && this.dims == multi.dims);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 128 */     return arrayName(this.component.toString(), this.dims);
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\analysis\MultiArrayType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */