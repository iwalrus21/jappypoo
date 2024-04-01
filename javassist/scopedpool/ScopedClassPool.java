/*     */ package javassist.scopedpool;
/*     */ 
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.ClassPath;
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtClass;
/*     */ import javassist.LoaderClassPath;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ScopedClassPool
/*     */   extends ClassPool
/*     */ {
/*     */   protected ScopedClassPoolRepository repository;
/*     */   protected WeakReference classLoader;
/*     */   protected LoaderClassPath classPath;
/*  44 */   protected SoftValueHashMap softcache = new SoftValueHashMap();
/*     */   
/*     */   boolean isBootstrapCl = true;
/*     */   
/*     */   static {
/*  49 */     ClassPool.doPruning = false;
/*  50 */     ClassPool.releaseUnmodifiedClassFile = false;
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
/*     */   protected ScopedClassPool(ClassLoader cl, ClassPool src, ScopedClassPoolRepository repository) {
/*  66 */     this(cl, src, repository, false);
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
/*     */   protected ScopedClassPool(ClassLoader cl, ClassPool src, ScopedClassPoolRepository repository, boolean isTemp) {
/*  83 */     super(src);
/*  84 */     this.repository = repository;
/*  85 */     this.classLoader = new WeakReference<ClassLoader>(cl);
/*  86 */     if (cl != null) {
/*  87 */       this.classPath = new LoaderClassPath(cl);
/*  88 */       insertClassPath((ClassPath)this.classPath);
/*     */     } 
/*  90 */     this.childFirstLookup = true;
/*  91 */     if (!isTemp && cl == null)
/*     */     {
/*  93 */       this.isBootstrapCl = true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassLoader getClassLoader() {
/* 103 */     ClassLoader cl = getClassLoader0();
/* 104 */     if (cl == null && !this.isBootstrapCl)
/*     */     {
/* 106 */       throw new IllegalStateException("ClassLoader has been garbage collected");
/*     */     }
/*     */     
/* 109 */     return cl;
/*     */   }
/*     */   
/*     */   protected ClassLoader getClassLoader0() {
/* 113 */     return this.classLoader.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 120 */     removeClassPath((ClassPath)this.classPath);
/* 121 */     this.classPath.close();
/* 122 */     this.classes.clear();
/* 123 */     this.softcache.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void flushClass(String classname) {
/* 133 */     this.classes.remove(classname);
/* 134 */     this.softcache.remove(classname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void soften(CtClass clazz) {
/* 144 */     if (this.repository.isPrune())
/* 145 */       clazz.prune(); 
/* 146 */     this.classes.remove(clazz.getName());
/* 147 */     this.softcache.put(clazz.getName(), clazz);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUnloadedClassLoader() {
/* 156 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CtClass getCached(String classname) {
/* 167 */     CtClass clazz = getCachedLocally(classname);
/* 168 */     if (clazz == null) {
/* 169 */       boolean isLocal = false;
/*     */       
/* 171 */       ClassLoader dcl = getClassLoader0();
/* 172 */       if (dcl != null) {
/* 173 */         int lastIndex = classname.lastIndexOf('$');
/* 174 */         String classResourceName = null;
/* 175 */         if (lastIndex < 0) {
/* 176 */           classResourceName = classname.replaceAll("[\\.]", "/") + ".class";
/*     */         
/*     */         }
/*     */         else {
/*     */ 
/*     */           
/* 182 */           classResourceName = classname.substring(0, lastIndex).replaceAll("[\\.]", "/") + classname.substring(lastIndex) + ".class";
/*     */         } 
/*     */         
/* 185 */         isLocal = (dcl.getResource(classResourceName) != null);
/*     */       } 
/*     */       
/* 188 */       if (!isLocal) {
/* 189 */         Map registeredCLs = this.repository.getRegisteredCLs();
/* 190 */         synchronized (registeredCLs) {
/* 191 */           Iterator<ScopedClassPool> it = registeredCLs.values().iterator();
/* 192 */           while (it.hasNext()) {
/* 193 */             ScopedClassPool pool = it.next();
/* 194 */             if (pool.isUnloadedClassLoader()) {
/* 195 */               this.repository.unregisterClassLoader(pool
/* 196 */                   .getClassLoader());
/*     */               
/*     */               continue;
/*     */             } 
/* 200 */             clazz = pool.getCachedLocally(classname);
/* 201 */             if (clazz != null) {
/* 202 */               return clazz;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 209 */     return clazz;
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
/*     */   protected void cacheCtClass(String classname, CtClass c, boolean dynamic) {
/* 223 */     if (dynamic) {
/* 224 */       super.cacheCtClass(classname, c, dynamic);
/*     */     } else {
/*     */       
/* 227 */       if (this.repository.isPrune())
/* 228 */         c.prune(); 
/* 229 */       this.softcache.put(classname, c);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void lockInCache(CtClass c) {
/* 240 */     super.cacheCtClass(c.getName(), c, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CtClass getCachedLocally(String classname) {
/* 251 */     CtClass cached = (CtClass)this.classes.get(classname);
/* 252 */     if (cached != null)
/* 253 */       return cached; 
/* 254 */     synchronized (this.softcache) {
/* 255 */       return (CtClass)this.softcache.get(classname);
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
/*     */   public synchronized CtClass getLocally(String classname) throws NotFoundException {
/* 270 */     this.softcache.remove(classname);
/* 271 */     CtClass clazz = (CtClass)this.classes.get(classname);
/* 272 */     if (clazz == null) {
/* 273 */       clazz = createCtClass(classname, true);
/* 274 */       if (clazz == null)
/* 275 */         throw new NotFoundException(classname); 
/* 276 */       super.cacheCtClass(classname, clazz, false);
/*     */     } 
/*     */     
/* 279 */     return clazz;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class toClass(CtClass ct, ClassLoader loader, ProtectionDomain domain) throws CannotCompileException {
/* 306 */     lockInCache(ct);
/* 307 */     return super.toClass(ct, getClassLoader0(), domain);
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\scopedpool\ScopedClassPool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */