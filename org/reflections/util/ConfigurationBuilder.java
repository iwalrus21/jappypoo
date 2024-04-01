/*     */ package org.reflections.util;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.ObjectArrays;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.common.util.concurrent.ThreadFactoryBuilder;
/*     */ import java.net.URL;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import org.reflections.Configuration;
/*     */ import org.reflections.Reflections;
/*     */ import org.reflections.ReflectionsException;
/*     */ import org.reflections.adapters.JavaReflectionAdapter;
/*     */ import org.reflections.adapters.JavassistAdapter;
/*     */ import org.reflections.adapters.MetadataAdapter;
/*     */ import org.reflections.scanners.Scanner;
/*     */ import org.reflections.scanners.SubTypesScanner;
/*     */ import org.reflections.scanners.TypeAnnotationsScanner;
/*     */ import org.reflections.serializers.Serializer;
/*     */ import org.reflections.serializers.XmlSerializer;
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
/*     */ public class ConfigurationBuilder
/*     */   implements Configuration
/*     */ {
/*     */   @Nonnull
/*     */   private Set<Scanner> scanners;
/*     */   @Nonnull
/*     */   private Set<URL> urls;
/*     */   protected MetadataAdapter metadataAdapter;
/*     */   @Nullable
/*     */   private Predicate<String> inputsFilter;
/*     */   private Serializer serializer;
/*     */   @Nullable
/*     */   private ExecutorService executorService;
/*     */   @Nullable
/*     */   private ClassLoader[] classLoaders;
/*     */   private boolean expandSuperTypes = true;
/*     */   
/*     */   public ConfigurationBuilder() {
/*  56 */     this.scanners = Sets.newHashSet((Object[])new Scanner[] { (Scanner)new TypeAnnotationsScanner(), (Scanner)new SubTypesScanner() });
/*  57 */     this.urls = Sets.newHashSet();
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
/*     */   public static ConfigurationBuilder build(@Nullable Object... params) {
/*  76 */     ConfigurationBuilder builder = new ConfigurationBuilder();
/*     */ 
/*     */     
/*  79 */     List<Object> parameters = Lists.newArrayList();
/*  80 */     if (params != null) {
/*  81 */       for (Object param : params) {
/*  82 */         if (param != null) {
/*  83 */           if (param.getClass().isArray()) { for (Object p : (Object[])param) { if (p != null) parameters.add(p);  }  }
/*  84 */           else if (param instanceof Iterable) { for (Object p : param) { if (p != null) parameters.add(p);  }  }
/*  85 */           else { parameters.add(param); }
/*     */         
/*     */         }
/*     */       } 
/*     */     }
/*  90 */     List<ClassLoader> loaders = Lists.newArrayList();
/*  91 */     for (Object param : parameters) { if (param instanceof ClassLoader) loaders.add((ClassLoader)param);  }
/*     */     
/*  93 */     ClassLoader[] classLoaders = loaders.isEmpty() ? null : loaders.<ClassLoader>toArray(new ClassLoader[loaders.size()]);
/*  94 */     FilterBuilder filter = new FilterBuilder();
/*  95 */     List<Scanner> scanners = Lists.newArrayList();
/*     */     
/*  97 */     for (Object param : parameters) {
/*  98 */       if (param instanceof String) {
/*  99 */         builder.addUrls(ClasspathHelper.forPackage((String)param, classLoaders));
/* 100 */         filter.includePackage(new String[] { (String)param }); continue;
/*     */       } 
/* 102 */       if (param instanceof Class) {
/* 103 */         if (Scanner.class.isAssignableFrom((Class)param)) {
/* 104 */           try { builder.addScanners(new Scanner[] { ((Class<Scanner>)param).newInstance() }); } catch (Exception exception) {}
/*     */         }
/* 106 */         builder.addUrls(new URL[] { ClasspathHelper.forClass((Class)param, classLoaders) });
/* 107 */         filter.includePackage((Class)param); continue;
/*     */       } 
/* 109 */       if (param instanceof Scanner) { scanners.add((Scanner)param); continue; }
/* 110 */        if (param instanceof URL) { builder.addUrls(new URL[] { (URL)param }); continue; }
/* 111 */        if (param instanceof ClassLoader)
/* 112 */         continue;  if (param instanceof Predicate) { filter.add((Predicate<String>)param); continue; }
/* 113 */        if (param instanceof ExecutorService) { builder.setExecutorService((ExecutorService)param); continue; }
/* 114 */        if (Reflections.log != null) throw new ReflectionsException("could not use param " + param);
/*     */     
/*     */     } 
/* 117 */     if (builder.getUrls().isEmpty()) {
/* 118 */       if (classLoaders != null) {
/* 119 */         builder.addUrls(ClasspathHelper.forClassLoader(classLoaders));
/*     */       } else {
/* 121 */         builder.addUrls(ClasspathHelper.forClassLoader());
/*     */       } 
/*     */     }
/*     */     
/* 125 */     builder.filterInputsBy(filter);
/* 126 */     if (!scanners.isEmpty()) builder.setScanners(scanners.<Scanner>toArray(new Scanner[scanners.size()])); 
/* 127 */     if (!loaders.isEmpty()) builder.addClassLoaders(loaders);
/*     */     
/* 129 */     return builder;
/*     */   }
/*     */   
/*     */   public ConfigurationBuilder forPackages(String... packages) {
/* 133 */     for (String pkg : packages) {
/* 134 */       addUrls(ClasspathHelper.forPackage(pkg, new ClassLoader[0]));
/*     */     }
/* 136 */     return this;
/*     */   }
/*     */   
/*     */   @Nonnull
/*     */   public Set<Scanner> getScanners() {
/* 141 */     return this.scanners;
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder setScanners(@Nonnull Scanner... scanners) {
/* 146 */     this.scanners.clear();
/* 147 */     return addScanners(scanners);
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder addScanners(Scanner... scanners) {
/* 152 */     this.scanners.addAll(Sets.newHashSet((Object[])scanners));
/* 153 */     return this;
/*     */   }
/*     */   
/*     */   @Nonnull
/*     */   public Set<URL> getUrls() {
/* 158 */     return this.urls;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder setUrls(@Nonnull Collection<URL> urls) {
/* 165 */     this.urls = Sets.newHashSet(urls);
/* 166 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder setUrls(URL... urls) {
/* 173 */     this.urls = Sets.newHashSet((Object[])urls);
/* 174 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder addUrls(Collection<URL> urls) {
/* 181 */     this.urls.addAll(urls);
/* 182 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder addUrls(URL... urls) {
/* 189 */     this.urls.addAll(Sets.newHashSet((Object[])urls));
/* 190 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MetadataAdapter getMetadataAdapter() {
/* 197 */     if (this.metadataAdapter != null) return this.metadataAdapter;
/*     */     
/*     */     try {
/* 200 */       return this.metadataAdapter = (MetadataAdapter)new JavassistAdapter();
/* 201 */     } catch (Throwable e) {
/* 202 */       if (Reflections.log != null)
/* 203 */         Reflections.log.warn("could not create JavassistAdapter, using JavaReflectionAdapter", e); 
/* 204 */       return this.metadataAdapter = (MetadataAdapter)new JavaReflectionAdapter();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder setMetadataAdapter(MetadataAdapter metadataAdapter) {
/* 211 */     this.metadataAdapter = metadataAdapter;
/* 212 */     return this;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Predicate<String> getInputsFilter() {
/* 217 */     return this.inputsFilter;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInputsFilter(@Nullable Predicate<String> inputsFilter) {
/* 223 */     this.inputsFilter = inputsFilter;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder filterInputsBy(Predicate<String> inputsFilter) {
/* 229 */     this.inputsFilter = inputsFilter;
/* 230 */     return this;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public ExecutorService getExecutorService() {
/* 235 */     return this.executorService;
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder setExecutorService(@Nullable ExecutorService executorService) {
/* 240 */     this.executorService = executorService;
/* 241 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder useParallelExecutor() {
/* 247 */     return useParallelExecutor(Runtime.getRuntime().availableProcessors());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder useParallelExecutor(int availableProcessors) {
/* 254 */     ThreadFactory factory = (new ThreadFactoryBuilder()).setDaemon(true).setNameFormat("org.reflections-scanner-%d").build();
/* 255 */     setExecutorService(Executors.newFixedThreadPool(availableProcessors, factory));
/* 256 */     return this;
/*     */   }
/*     */   
/*     */   public Serializer getSerializer() {
/* 260 */     return (this.serializer != null) ? this.serializer : (this.serializer = (Serializer)new XmlSerializer());
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder setSerializer(Serializer serializer) {
/* 265 */     this.serializer = serializer;
/* 266 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ClassLoader[] getClassLoaders() {
/* 272 */     return this.classLoaders;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldExpandSuperTypes() {
/* 277 */     return this.expandSuperTypes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder setExpandSuperTypes(boolean expandSuperTypes) {
/* 285 */     this.expandSuperTypes = expandSuperTypes;
/* 286 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setClassLoaders(@Nullable ClassLoader[] classLoaders) {
/* 291 */     this.classLoaders = classLoaders;
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder addClassLoader(ClassLoader classLoader) {
/* 296 */     return addClassLoaders(new ClassLoader[] { classLoader });
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder addClassLoaders(ClassLoader... classLoaders) {
/* 301 */     this.classLoaders = (this.classLoaders == null) ? classLoaders : (ClassLoader[])ObjectArrays.concat((Object[])this.classLoaders, (Object[])classLoaders, ClassLoader.class);
/* 302 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ConfigurationBuilder addClassLoaders(Collection<ClassLoader> classLoaders) {
/* 307 */     return addClassLoaders(classLoaders.<ClassLoader>toArray(new ClassLoader[classLoaders.size()]));
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\reflection\\util\ConfigurationBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */