/*     */ package javassist.scopedpool;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ import javassist.ClassPath;
/*     */ import javassist.ClassPool;
/*     */ import javassist.LoaderClassPath;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ScopedClassPoolRepositoryImpl
/*     */   implements ScopedClassPoolRepository
/*     */ {
/*  37 */   private static final ScopedClassPoolRepositoryImpl instance = new ScopedClassPoolRepositoryImpl();
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean prune = true;
/*     */ 
/*     */   
/*     */   boolean pruneWhenCached;
/*     */ 
/*     */   
/*  47 */   protected Map registeredCLs = Collections.synchronizedMap(new WeakHashMap<Object, Object>());
/*     */ 
/*     */   
/*     */   protected ClassPool classpool;
/*     */ 
/*     */   
/*  53 */   protected ScopedClassPoolFactory factory = new ScopedClassPoolFactoryImpl();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ScopedClassPoolRepository getInstance() {
/*  61 */     return instance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ScopedClassPoolRepositoryImpl() {
/*  68 */     this.classpool = ClassPool.getDefault();
/*     */     
/*  70 */     ClassLoader cl = Thread.currentThread().getContextClassLoader();
/*  71 */     this.classpool.insertClassPath((ClassPath)new LoaderClassPath(cl));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPrune() {
/*  80 */     return this.prune;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrune(boolean prune) {
/*  89 */     this.prune = prune;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ScopedClassPool createScopedClassPool(ClassLoader cl, ClassPool src) {
/* 100 */     return this.factory.create(cl, src, this);
/*     */   }
/*     */   
/*     */   public ClassPool findClassPool(ClassLoader cl) {
/* 104 */     if (cl == null) {
/* 105 */       return registerClassLoader(ClassLoader.getSystemClassLoader());
/*     */     }
/* 107 */     return registerClassLoader(cl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassPool registerClassLoader(ClassLoader ucl) {
/* 117 */     synchronized (this.registeredCLs) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 123 */       if (this.registeredCLs.containsKey(ucl)) {
/* 124 */         return (ClassPool)this.registeredCLs.get(ucl);
/*     */       }
/* 126 */       ScopedClassPool pool = createScopedClassPool(ucl, this.classpool);
/* 127 */       this.registeredCLs.put(ucl, pool);
/* 128 */       return pool;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map getRegisteredCLs() {
/* 136 */     clearUnregisteredClassLoaders();
/* 137 */     return this.registeredCLs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearUnregisteredClassLoaders() {
/* 145 */     ArrayList<ClassLoader> toUnregister = null;
/* 146 */     synchronized (this.registeredCLs) {
/* 147 */       Iterator<ScopedClassPool> it = this.registeredCLs.values().iterator();
/* 148 */       while (it.hasNext()) {
/* 149 */         ScopedClassPool pool = it.next();
/* 150 */         if (pool.isUnloadedClassLoader()) {
/* 151 */           it.remove();
/* 152 */           ClassLoader cl = pool.getClassLoader();
/* 153 */           if (cl != null) {
/* 154 */             if (toUnregister == null) {
/* 155 */               toUnregister = new ArrayList();
/*     */             }
/* 157 */             toUnregister.add(cl);
/*     */           } 
/*     */         } 
/*     */       } 
/* 161 */       if (toUnregister != null) {
/* 162 */         for (int i = 0; i < toUnregister.size(); i++) {
/* 163 */           unregisterClassLoader(toUnregister.get(i));
/*     */         }
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void unregisterClassLoader(ClassLoader cl) {
/* 170 */     synchronized (this.registeredCLs) {
/* 171 */       ScopedClassPool pool = (ScopedClassPool)this.registeredCLs.remove(cl);
/* 172 */       if (pool != null) {
/* 173 */         pool.close();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void insertDelegate(ScopedClassPoolRepository delegate) {}
/*     */   
/*     */   public void setClassPoolFactory(ScopedClassPoolFactory factory) {
/* 182 */     this.factory = factory;
/*     */   }
/*     */   
/*     */   public ScopedClassPoolFactory getClassPoolFactory() {
/* 186 */     return this.factory;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\scopedpool\ScopedClassPoolRepositoryImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */