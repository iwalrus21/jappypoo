/*     */ package javassist;
/*     */ 
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import javassist.bytecode.ClassFile;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class CtNewClass
/*     */   extends CtClassType
/*     */ {
/*     */   protected boolean hasConstructor;
/*     */   
/*     */   CtNewClass(String name, ClassPool cp, boolean isInterface, CtClass superclass) {
/*  30 */     super(name, cp); String superName;
/*  31 */     this.wasChanged = true;
/*     */     
/*  33 */     if (isInterface || superclass == null) {
/*  34 */       superName = null;
/*     */     } else {
/*  36 */       superName = superclass.getName();
/*     */     } 
/*  38 */     this.classfile = new ClassFile(isInterface, name, superName);
/*  39 */     if (isInterface && superclass != null) {
/*  40 */       this.classfile.setInterfaces(new String[] { superclass.getName() });
/*     */     }
/*  42 */     setModifiers(Modifier.setPublic(getModifiers()));
/*  43 */     this.hasConstructor = isInterface;
/*     */   }
/*     */   
/*     */   protected void extendToString(StringBuffer buffer) {
/*  47 */     if (this.hasConstructor) {
/*  48 */       buffer.append("hasConstructor ");
/*     */     }
/*  50 */     super.extendToString(buffer);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConstructor(CtConstructor c) throws CannotCompileException {
/*  56 */     this.hasConstructor = true;
/*  57 */     super.addConstructor(c);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void toBytecode(DataOutputStream out) throws CannotCompileException, IOException {
/*  63 */     if (!this.hasConstructor) {
/*     */       try {
/*  65 */         inheritAllConstructors();
/*  66 */         this.hasConstructor = true;
/*     */       }
/*  68 */       catch (NotFoundException e) {
/*  69 */         throw new CannotCompileException(e);
/*     */       } 
/*     */     }
/*  72 */     super.toBytecode(out);
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
/*     */   public void inheritAllConstructors() throws CannotCompileException, NotFoundException {
/*  88 */     CtClass superclazz = getSuperclass();
/*  89 */     CtConstructor[] cs = superclazz.getDeclaredConstructors();
/*     */     
/*  91 */     int n = 0;
/*  92 */     for (int i = 0; i < cs.length; i++) {
/*  93 */       CtConstructor c = cs[i];
/*  94 */       int mod = c.getModifiers();
/*  95 */       if (isInheritable(mod, superclazz)) {
/*     */         
/*  97 */         CtConstructor cons = CtNewConstructor.make(c.getParameterTypes(), c
/*  98 */             .getExceptionTypes(), this);
/*  99 */         cons.setModifiers(mod & 0x7);
/* 100 */         addConstructor(cons);
/* 101 */         n++;
/*     */       } 
/*     */     } 
/*     */     
/* 105 */     if (n < 1) {
/* 106 */       throw new CannotCompileException("no inheritable constructor in " + superclazz
/* 107 */           .getName());
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isInheritable(int mod, CtClass superclazz) {
/* 112 */     if (Modifier.isPrivate(mod)) {
/* 113 */       return false;
/*     */     }
/* 115 */     if (Modifier.isPackage(mod)) {
/* 116 */       String pname = getPackageName();
/* 117 */       String pname2 = superclazz.getPackageName();
/* 118 */       if (pname == null) {
/* 119 */         return (pname2 == null);
/*     */       }
/* 121 */       return pname.equals(pname2);
/*     */     } 
/*     */     
/* 124 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\CtNewClass.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */