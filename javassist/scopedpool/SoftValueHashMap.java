/*     */ package javassist.scopedpool;
/*     */ 
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public class SoftValueHashMap
/*     */   extends AbstractMap
/*     */   implements Map
/*     */ {
/*     */   private Map hash;
/*     */   
/*     */   private static class SoftValueRef
/*     */     extends SoftReference
/*     */   {
/*     */     public Object key;
/*     */     
/*     */     private SoftValueRef(Object key, Object val, ReferenceQueue<? super T> q) {
/*  38 */       super((T)val, q);
/*  39 */       this.key = key;
/*     */     }
/*     */ 
/*     */     
/*     */     private static SoftValueRef create(Object key, Object val, ReferenceQueue q) {
/*  44 */       if (val == null) {
/*  45 */         return null;
/*     */       }
/*  47 */       return new SoftValueRef(key, val, q);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set entrySet() {
/*  56 */     processQueue();
/*  57 */     return this.hash.entrySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  64 */   private ReferenceQueue queue = new ReferenceQueue();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processQueue() {
/*     */     SoftValueRef ref;
/*  72 */     while ((ref = (SoftValueRef)this.queue.poll()) != null) {
/*  73 */       if (ref == (SoftValueRef)this.hash.get(ref.key))
/*     */       {
/*     */         
/*  76 */         this.hash.remove(ref.key);
/*     */       }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SoftValueHashMap(int initialCapacity, float loadFactor) {
/*  98 */     this.hash = new HashMap<Object, Object>(initialCapacity, loadFactor);
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
/*     */   public SoftValueHashMap(int initialCapacity) {
/* 112 */     this.hash = new HashMap<Object, Object>(initialCapacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SoftValueHashMap() {
/* 120 */     this.hash = new HashMap<Object, Object>();
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
/*     */   public SoftValueHashMap(Map<? extends K, ? extends V> t) {
/* 133 */     this(Math.max(2 * t.size(), 11), 0.75F);
/* 134 */     putAll(t);
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
/*     */   public int size() {
/* 146 */     processQueue();
/* 147 */     return this.hash.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 154 */     processQueue();
/* 155 */     return this.hash.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/* 166 */     processQueue();
/* 167 */     return this.hash.containsKey(key);
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
/*     */   public Object get(Object key) {
/* 181 */     processQueue();
/* 182 */     SoftReference ref = (SoftReference)this.hash.get(key);
/* 183 */     if (ref != null)
/* 184 */       return ref.get(); 
/* 185 */     return null;
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
/*     */   public Object put(Object key, Object value) {
/* 204 */     processQueue();
/* 205 */     Object rtn = this.hash.put(key, SoftValueRef.create(key, value, this.queue));
/* 206 */     if (rtn != null)
/* 207 */       rtn = ((SoftReference)rtn).get(); 
/* 208 */     return rtn;
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
/*     */   public Object remove(Object key) {
/* 222 */     processQueue();
/* 223 */     return this.hash.remove(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 230 */     processQueue();
/* 231 */     this.hash.clear();
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\scopedpool\SoftValueHashMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */