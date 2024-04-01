/*    */ package org.reflections.scanners;
/*    */ 
/*    */ import com.google.common.base.Predicate;
/*    */ import com.google.common.base.Predicates;
/*    */ import com.google.common.collect.Multimap;
/*    */ import org.reflections.Configuration;
/*    */ import org.reflections.ReflectionsException;
/*    */ import org.reflections.adapters.MetadataAdapter;
/*    */ import org.reflections.vfs.Vfs;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractScanner
/*    */   implements Scanner
/*    */ {
/*    */   private Configuration configuration;
/*    */   private Multimap<String, String> store;
/* 21 */   private Predicate<String> resultFilter = Predicates.alwaysTrue();
/*    */   
/*    */   public boolean acceptsInput(String file) {
/* 24 */     return getMetadataAdapter().acceptsInput(file);
/*    */   }
/*    */   
/*    */   public Object scan(Vfs.File file, Object classObject) {
/* 28 */     if (classObject == null) {
/*    */       try {
/* 30 */         classObject = this.configuration.getMetadataAdapter().getOfCreateClassObject(file);
/* 31 */       } catch (Exception e) {
/* 32 */         throw new ReflectionsException("could not create class object from file " + file.getRelativePath(), e);
/*    */       } 
/*    */     }
/* 35 */     scan(classObject);
/* 36 */     return classObject;
/*    */   }
/*    */ 
/*    */   
/*    */   public abstract void scan(Object paramObject);
/*    */   
/*    */   public Configuration getConfiguration() {
/* 43 */     return this.configuration;
/*    */   }
/*    */   
/*    */   public void setConfiguration(Configuration configuration) {
/* 47 */     this.configuration = configuration;
/*    */   }
/*    */   
/*    */   public Multimap<String, String> getStore() {
/* 51 */     return this.store;
/*    */   }
/*    */   
/*    */   public void setStore(Multimap<String, String> store) {
/* 55 */     this.store = store;
/*    */   }
/*    */   
/*    */   public Predicate<String> getResultFilter() {
/* 59 */     return this.resultFilter;
/*    */   }
/*    */   
/*    */   public void setResultFilter(Predicate<String> resultFilter) {
/* 63 */     this.resultFilter = resultFilter;
/*    */   }
/*    */   
/*    */   public Scanner filterResultsBy(Predicate<String> filter) {
/* 67 */     setResultFilter(filter); return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean acceptResult(String fqn) {
/* 72 */     return (fqn != null && this.resultFilter.apply(fqn));
/*    */   }
/*    */   
/*    */   protected MetadataAdapter getMetadataAdapter() {
/* 76 */     return this.configuration.getMetadataAdapter();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 81 */     return (this == o || (o != null && getClass() == o.getClass()));
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 85 */     return getClass().hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\reflections\scanners\AbstractScanner.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */