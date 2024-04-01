/*     */ package javassist;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class CtArray
/*     */   extends CtClass
/*     */ {
/*     */   protected ClassPool pool;
/*     */   private CtClass[] interfaces;
/*     */   
/*     */   CtArray(String name, ClassPool cp) {
/*  27 */     super(name);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  39 */     this.interfaces = null;
/*     */     this.pool = cp;
/*     */   } public ClassPool getClassPool() { return this.pool; } public int getModifiers() {
/*  42 */     int mod = 16;
/*     */     try {
/*  44 */       mod |= getComponentType().getModifiers() & 0x7;
/*     */     
/*     */     }
/*  47 */     catch (NotFoundException notFoundException) {}
/*  48 */     return mod;
/*     */   } public boolean isArray() {
/*     */     return true;
/*     */   } public CtClass[] getInterfaces() throws NotFoundException {
/*  52 */     if (this.interfaces == null) {
/*  53 */       Class[] intfs = Object[].class.getInterfaces();
/*     */ 
/*     */       
/*  56 */       this.interfaces = new CtClass[intfs.length];
/*  57 */       for (int i = 0; i < intfs.length; i++) {
/*  58 */         this.interfaces[i] = this.pool.get(intfs[i].getName());
/*     */       }
/*     */     } 
/*  61 */     return this.interfaces;
/*     */   }
/*     */   
/*     */   public boolean subtypeOf(CtClass clazz) throws NotFoundException {
/*  65 */     if (super.subtypeOf(clazz)) {
/*  66 */       return true;
/*     */     }
/*  68 */     String cname = clazz.getName();
/*  69 */     if (cname.equals("java.lang.Object")) {
/*  70 */       return true;
/*     */     }
/*  72 */     CtClass[] intfs = getInterfaces();
/*  73 */     for (int i = 0; i < intfs.length; i++) {
/*  74 */       if (intfs[i].subtypeOf(clazz))
/*  75 */         return true; 
/*     */     } 
/*  77 */     return (clazz.isArray() && 
/*  78 */       getComponentType().subtypeOf(clazz.getComponentType()));
/*     */   }
/*     */   
/*     */   public CtClass getComponentType() throws NotFoundException {
/*  82 */     String name = getName();
/*  83 */     return this.pool.get(name.substring(0, name.length() - 2));
/*     */   }
/*     */   
/*     */   public CtClass getSuperclass() throws NotFoundException {
/*  87 */     return this.pool.get("java.lang.Object");
/*     */   }
/*     */   
/*     */   public CtMethod[] getMethods() {
/*     */     try {
/*  92 */       return getSuperclass().getMethods();
/*     */     }
/*  94 */     catch (NotFoundException e) {
/*  95 */       return super.getMethods();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CtMethod getMethod(String name, String desc) throws NotFoundException {
/* 102 */     return getSuperclass().getMethod(name, desc);
/*     */   }
/*     */   
/*     */   public CtConstructor[] getConstructors() {
/*     */     try {
/* 107 */       return getSuperclass().getConstructors();
/*     */     }
/* 109 */     catch (NotFoundException e) {
/* 110 */       return super.getConstructors();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\CtArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */