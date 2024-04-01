/*     */ package org.reflections;
/*     */ 
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import com.google.common.collect.HashMultimap;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Multimap;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.annotation.Inherited;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URL;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.annotation.Nullable;
/*     */ import org.reflections.scanners.FieldAnnotationsScanner;
/*     */ import org.reflections.scanners.MemberUsageScanner;
/*     */ import org.reflections.scanners.MethodAnnotationsScanner;
/*     */ import org.reflections.scanners.MethodParameterNamesScanner;
/*     */ import org.reflections.scanners.MethodParameterScanner;
/*     */ import org.reflections.scanners.ResourcesScanner;
/*     */ import org.reflections.scanners.Scanner;
/*     */ import org.reflections.scanners.SubTypesScanner;
/*     */ import org.reflections.scanners.TypeAnnotationsScanner;
/*     */ import org.reflections.serializers.Serializer;
/*     */ import org.reflections.serializers.XmlSerializer;
/*     */ import org.reflections.util.ClasspathHelper;
/*     */ import org.reflections.util.ConfigurationBuilder;
/*     */ import org.reflections.util.FilterBuilder;
/*     */ import org.reflections.util.Utils;
/*     */ import org.reflections.vfs.Vfs;
/*     */ import org.slf4j.Logger;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Reflections
/*     */ {
/*     */   @Nullable
/* 103 */   public static Logger log = Utils.findLogger(Reflections.class);
/*     */ 
/*     */   
/*     */   protected final transient Configuration configuration;
/*     */ 
/*     */   
/*     */   protected Store store;
/*     */ 
/*     */   
/*     */   public Reflections(Configuration configuration) {
/* 113 */     this.configuration = configuration;
/* 114 */     this.store = new Store(configuration);
/*     */     
/* 116 */     if (configuration.getScanners() != null && !configuration.getScanners().isEmpty()) {
/*     */       
/* 118 */       for (Scanner scanner : configuration.getScanners()) {
/* 119 */         scanner.setConfiguration(configuration);
/* 120 */         scanner.setStore(this.store.getOrCreate(scanner.getClass().getSimpleName()));
/*     */       } 
/*     */       
/* 123 */       scan();
/*     */       
/* 125 */       if (configuration.shouldExpandSuperTypes()) {
/* 126 */         expandSuperTypes();
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
/*     */   public Reflections(String prefix, @Nullable Scanner... scanners) {
/* 141 */     this(new Object[] { prefix, scanners });
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
/*     */   public Reflections(Object... params) {
/* 168 */     this((Configuration)ConfigurationBuilder.build(params));
/*     */   }
/*     */   
/*     */   protected Reflections() {
/* 172 */     this.configuration = (Configuration)new ConfigurationBuilder();
/* 173 */     this.store = new Store(this.configuration);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void scan() {
/* 178 */     if (this.configuration.getUrls() == null || this.configuration.getUrls().isEmpty()) {
/* 179 */       if (log != null) log.warn("given scan urls are empty. set urls in the configuration");
/*     */       
/*     */       return;
/*     */     } 
/* 183 */     if (log != null && log.isDebugEnabled()) {
/* 184 */       log.debug("going to scan these urls:\n" + Joiner.on("\n").join(this.configuration.getUrls()));
/*     */     }
/*     */     
/* 187 */     long time = System.currentTimeMillis();
/* 188 */     int scannedUrls = 0;
/* 189 */     ExecutorService executorService = this.configuration.getExecutorService();
/* 190 */     List<Future<?>> futures = Lists.newArrayList();
/*     */     
/* 192 */     for (URL url : this.configuration.getUrls()) {
/*     */       try {
/* 194 */         if (executorService != null) {
/* 195 */           futures.add(executorService.submit(new Runnable() {
/*     */                   public void run() {
/* 197 */                     if (Reflections.log != null && Reflections.log.isDebugEnabled()) Reflections.log.debug("[" + Thread.currentThread().toString() + "] scanning " + url); 
/* 198 */                     Reflections.this.scan(url);
/*     */                   }
/*     */                 }));
/*     */         } else {
/* 202 */           scan(url);
/*     */         } 
/* 204 */         scannedUrls++;
/* 205 */       } catch (ReflectionsException e) {
/* 206 */         if (log != null && log.isWarnEnabled()) log.warn("could not create Vfs.Dir from url. ignoring the exception and continuing", e);
/*     */       
/*     */       } 
/*     */     } 
/*     */     
/* 211 */     if (executorService != null) {
/* 212 */       for (Future<?> future : futures) { 
/* 213 */         try { future.get(); } catch (Exception e) { throw new RuntimeException(e); }
/*     */          }
/*     */     
/*     */     }
/* 217 */     time = System.currentTimeMillis() - time;
/*     */ 
/*     */     
/* 220 */     if (executorService != null) {
/* 221 */       executorService.shutdown();
/*     */     }
/*     */     
/* 224 */     if (log != null) {
/* 225 */       int keys = 0;
/* 226 */       int values = 0;
/* 227 */       for (String index : this.store.keySet()) {
/* 228 */         keys += this.store.get(index).keySet().size();
/* 229 */         values += this.store.get(index).size();
/*     */       } 
/*     */       
/* 232 */       log.info(String.format("Reflections took %d ms to scan %d urls, producing %d keys and %d values %s", new Object[] {
/* 233 */               Long.valueOf(time), Integer.valueOf(scannedUrls), Integer.valueOf(keys), Integer.valueOf(values), (executorService != null && executorService instanceof ThreadPoolExecutor) ? 
/*     */               
/* 235 */               String.format("[using %d cores]", new Object[] { Integer.valueOf(((ThreadPoolExecutor)executorService).getMaximumPoolSize()) }) : "" }));
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void scan(URL url) {
/* 240 */     Vfs.Dir dir = Vfs.fromURL(url);
/*     */     
/*     */     try {
/* 243 */       for (Vfs.File file : dir.getFiles()) {
/*     */         
/* 245 */         Predicate<String> inputsFilter = this.configuration.getInputsFilter();
/* 246 */         String path = file.getRelativePath();
/* 247 */         String fqn = path.replace('/', '.');
/* 248 */         if (inputsFilter == null || inputsFilter.apply(path) || inputsFilter.apply(fqn)) {
/* 249 */           Object classObject = null;
/* 250 */           for (Scanner scanner : this.configuration.getScanners()) {
/*     */             try {
/* 252 */               if (scanner.acceptsInput(path) || scanner.acceptResult(fqn)) {
/* 253 */                 classObject = scanner.scan(file, classObject);
/*     */               }
/* 255 */             } catch (Exception e) {
/* 256 */               if (log != null && log.isDebugEnabled())
/* 257 */                 log.debug("could not scan file " + file.getRelativePath() + " in url " + url.toExternalForm() + " with scanner " + scanner.getClass().getSimpleName(), e); 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 263 */       dir.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Reflections collect() {
/* 272 */     return collect("META-INF/reflections/", (Predicate<String>)(new FilterBuilder()).include(".*-reflections.xml"), new Serializer[0]);
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
/*     */   public static Reflections collect(String packagePrefix, Predicate<String> resourceNameFilter, @Nullable Serializer... optionalSerializer) {
/* 284 */     Serializer serializer = (optionalSerializer != null && optionalSerializer.length == 1) ? optionalSerializer[0] : (Serializer)new XmlSerializer();
/*     */     
/* 286 */     Collection<URL> urls = ClasspathHelper.forPackage(packagePrefix, new ClassLoader[0]);
/* 287 */     if (urls.isEmpty()) return null; 
/* 288 */     long start = System.currentTimeMillis();
/* 289 */     Reflections reflections = new Reflections();
/* 290 */     Iterable<Vfs.File> files = Vfs.findFiles(urls, packagePrefix, resourceNameFilter);
/* 291 */     for (Vfs.File file : files) {
/* 292 */       InputStream inputStream = null;
/*     */       try {
/* 294 */         inputStream = file.openInputStream();
/* 295 */         reflections.merge(serializer.read(inputStream));
/* 296 */       } catch (IOException e) {
/* 297 */         throw new ReflectionsException("could not merge " + file, e);
/*     */       } finally {
/* 299 */         Utils.close(inputStream);
/*     */       } 
/*     */     } 
/*     */     
/* 303 */     if (log != null) {
/* 304 */       Store store = reflections.getStore();
/* 305 */       int keys = 0;
/* 306 */       int values = 0;
/* 307 */       for (String index : store.keySet()) {
/* 308 */         keys += store.get(index).keySet().size();
/* 309 */         values += store.get(index).size();
/*     */       } 
/*     */       
/* 312 */       log.info(String.format("Reflections took %d ms to collect %d url%s, producing %d keys and %d values [%s]", new Object[] {
/* 313 */               Long.valueOf(System.currentTimeMillis() - start), Integer.valueOf(urls.size()), (urls.size() > 1) ? "s" : "", Integer.valueOf(keys), Integer.valueOf(values), Joiner.on(", ").join(urls) }));
/*     */     } 
/* 315 */     return reflections;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Reflections collect(InputStream inputStream) {
/*     */     try {
/* 323 */       merge(this.configuration.getSerializer().read(inputStream));
/* 324 */       if (log != null) log.info("Reflections collected metadata from input stream using serializer " + this.configuration.getSerializer().getClass().getName()); 
/* 325 */     } catch (Exception ex) {
/* 326 */       throw new ReflectionsException("could not merge input stream", ex);
/*     */     } 
/*     */     
/* 329 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Reflections collect(File file) {
/* 336 */     FileInputStream inputStream = null;
/*     */     try {
/* 338 */       inputStream = new FileInputStream(file);
/* 339 */       return collect(inputStream);
/* 340 */     } catch (FileNotFoundException e) {
/* 341 */       throw new ReflectionsException("could not obtain input stream from file " + file, e);
/*     */     } finally {
/* 343 */       Utils.close(inputStream);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Reflections merge(Reflections reflections) {
/* 351 */     if (reflections.store != null) {
/* 352 */       for (String indexName : reflections.store.keySet()) {
/* 353 */         Multimap<String, String> index = reflections.store.get(indexName);
/* 354 */         for (String key : index.keySet()) {
/* 355 */           for (String string : index.get(key)) {
/* 356 */             this.store.getOrCreate(indexName).put(key, string);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/* 361 */     return this;
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
/*     */   public void expandSuperTypes() {
/* 376 */     if (this.store.keySet().contains(index((Class)SubTypesScanner.class))) {
/* 377 */       Multimap<String, String> mmap = this.store.get(index((Class)SubTypesScanner.class));
/* 378 */       Sets.SetView<String> keys = Sets.difference(mmap.keySet(), Sets.newHashSet(mmap.values()));
/* 379 */       HashMultimap hashMultimap = HashMultimap.create();
/* 380 */       for (UnmodifiableIterator<String> unmodifiableIterator = keys.iterator(); unmodifiableIterator.hasNext(); ) { String key = unmodifiableIterator.next();
/* 381 */         Class<?> type = ReflectionUtils.forName(key, new ClassLoader[0]);
/* 382 */         if (type != null) {
/* 383 */           expandSupertypes((Multimap<String, String>)hashMultimap, key, type);
/*     */         } }
/*     */       
/* 386 */       mmap.putAll((Multimap)hashMultimap);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void expandSupertypes(Multimap<String, String> mmap, String key, Class<?> type) {
/* 391 */     for (Class<?> supertype : ReflectionUtils.getSuperTypes(type)) {
/* 392 */       if (mmap.put(supertype.getName(), key)) {
/* 393 */         if (log != null) log.debug("expanded subtype {} -> {}", supertype.getName(), key); 
/* 394 */         expandSupertypes(mmap, supertype.getName(), supertype);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> Set<Class<? extends T>> getSubTypesOf(Class<T> type) {
/* 405 */     return Sets.newHashSet(ReflectionUtils.forNames(this.store
/* 406 */           .getAll(index((Class)SubTypesScanner.class), Arrays.asList(new String[] { type.getName() })), loaders()));
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
/*     */   public Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation) {
/* 418 */     return getTypesAnnotatedWith(annotation, false);
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
/*     */   public Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation, boolean honorInherited) {
/* 431 */     Iterable<String> annotated = this.store.get(index((Class)TypeAnnotationsScanner.class), new String[] { annotation.getName() });
/* 432 */     Iterable<String> classes = getAllAnnotated(annotated, annotation.isAnnotationPresent((Class)Inherited.class), honorInherited);
/* 433 */     return Sets.newHashSet(Iterables.concat(ReflectionUtils.forNames(annotated, loaders()), ReflectionUtils.forNames(classes, loaders())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Class<?>> getTypesAnnotatedWith(Annotation annotation) {
/* 442 */     return getTypesAnnotatedWith(annotation, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Class<?>> getTypesAnnotatedWith(Annotation annotation, boolean honorInherited) {
/* 451 */     Iterable<String> annotated = this.store.get(index((Class)TypeAnnotationsScanner.class), new String[] { annotation.annotationType().getName() });
/* 452 */     Iterable<Class<?>> filter = ReflectionUtils.filter(ReflectionUtils.forNames(annotated, loaders()), (Predicate<? super Class<?>>[])new Predicate[] { ReflectionUtils.withAnnotation(annotation) });
/* 453 */     Iterable<String> classes = getAllAnnotated(Utils.names(filter), annotation.annotationType().isAnnotationPresent((Class)Inherited.class), honorInherited);
/* 454 */     return Sets.newHashSet(Iterables.concat(filter, ReflectionUtils.forNames(ReflectionUtils.filter(classes, (Predicate<? super String>[])new Predicate[] { Predicates.not(Predicates.in(Sets.newHashSet(annotated))) }), loaders())));
/*     */   }
/*     */   
/*     */   protected Iterable<String> getAllAnnotated(Iterable<String> annotated, boolean inherited, boolean honorInherited) {
/* 458 */     if (honorInherited) {
/* 459 */       if (inherited) {
/* 460 */         Iterable<String> iterable = this.store.get(index((Class)SubTypesScanner.class), ReflectionUtils.filter(annotated, (Predicate<? super String>[])new Predicate[] { new Predicate<String>() {
/*     */                   public boolean apply(@Nullable String input) {
/* 462 */                     Class<?> type = ReflectionUtils.forName(input, Reflections.this.loaders());
/* 463 */                     return (type != null && !type.isInterface());
/*     */                   }
/*     */                 } }));
/* 466 */         return Iterables.concat(iterable, this.store.getAll(index((Class)SubTypesScanner.class), iterable));
/*     */       } 
/* 468 */       return annotated;
/*     */     } 
/*     */     
/* 471 */     Iterable<String> subTypes = Iterables.concat(annotated, this.store.getAll(index((Class)TypeAnnotationsScanner.class), annotated));
/* 472 */     return Iterables.concat(subTypes, this.store.getAll(index((Class)SubTypesScanner.class), subTypes));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Method> getMethodsAnnotatedWith(Class<? extends Annotation> annotation) {
/* 481 */     Iterable<String> methods = this.store.get(index((Class)MethodAnnotationsScanner.class), new String[] { annotation.getName() });
/* 482 */     return Utils.getMethodsFromDescriptors(methods, loaders());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Method> getMethodsAnnotatedWith(Annotation annotation) {
/* 490 */     return ReflectionUtils.filter(getMethodsAnnotatedWith(annotation.annotationType()), (Predicate<? super Method>[])new Predicate[] { ReflectionUtils.withAnnotation(annotation) });
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Method> getMethodsMatchParams(Class<?>... types) {
/* 495 */     return Utils.getMethodsFromDescriptors(this.store.get(index((Class)MethodParameterScanner.class), new String[] { Utils.names(types).toString() }), loaders());
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Method> getMethodsReturn(Class returnType) {
/* 500 */     return Utils.getMethodsFromDescriptors(this.store.get(index((Class)MethodParameterScanner.class), Utils.names(new Class[] { returnType })), loaders());
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Method> getMethodsWithAnyParamAnnotated(Class<? extends Annotation> annotation) {
/* 505 */     return Utils.getMethodsFromDescriptors(this.store.get(index((Class)MethodParameterScanner.class), new String[] { annotation.getName() }), loaders());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Method> getMethodsWithAnyParamAnnotated(Annotation annotation) {
/* 511 */     return ReflectionUtils.filter(getMethodsWithAnyParamAnnotated(annotation.annotationType()), (Predicate<? super Method>[])new Predicate[] { ReflectionUtils.withAnyParameterAnnotation(annotation) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Constructor> getConstructorsAnnotatedWith(Class<? extends Annotation> annotation) {
/* 519 */     Iterable<String> methods = this.store.get(index((Class)MethodAnnotationsScanner.class), new String[] { annotation.getName() });
/* 520 */     return Utils.getConstructorsFromDescriptors(methods, loaders());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Constructor> getConstructorsAnnotatedWith(Annotation annotation) {
/* 528 */     return ReflectionUtils.filter(getConstructorsAnnotatedWith(annotation.annotationType()), (Predicate<? super Constructor>[])new Predicate[] { ReflectionUtils.withAnnotation(annotation) });
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Constructor> getConstructorsMatchParams(Class<?>... types) {
/* 533 */     return Utils.getConstructorsFromDescriptors(this.store.get(index((Class)MethodParameterScanner.class), new String[] { Utils.names(types).toString() }), loaders());
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Constructor> getConstructorsWithAnyParamAnnotated(Class<? extends Annotation> annotation) {
/* 538 */     return Utils.getConstructorsFromDescriptors(this.store.get(index((Class)MethodParameterScanner.class), new String[] { annotation.getName() }), loaders());
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Constructor> getConstructorsWithAnyParamAnnotated(Annotation annotation) {
/* 543 */     return ReflectionUtils.filter(getConstructorsWithAnyParamAnnotated(annotation.annotationType()), (Predicate<? super Constructor>[])new Predicate[] { ReflectionUtils.withAnyParameterAnnotation(annotation) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Field> getFieldsAnnotatedWith(Class<? extends Annotation> annotation) {
/* 551 */     Set<Field> result = Sets.newHashSet();
/* 552 */     for (String annotated : this.store.get(index((Class)FieldAnnotationsScanner.class), new String[] { annotation.getName() })) {
/* 553 */       result.add(Utils.getFieldFromString(annotated, loaders()));
/*     */     }
/* 555 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Field> getFieldsAnnotatedWith(Annotation annotation) {
/* 563 */     return ReflectionUtils.filter(getFieldsAnnotatedWith(annotation.annotationType()), (Predicate<? super Field>[])new Predicate[] { ReflectionUtils.withAnnotation(annotation) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getResources(Predicate<String> namePredicate) {
/* 570 */     Iterable<String> resources = Iterables.filter(this.store.get(index((Class)ResourcesScanner.class)).keySet(), namePredicate);
/* 571 */     return Sets.newHashSet(this.store.get(index((Class)ResourcesScanner.class), resources));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getResources(final Pattern pattern) {
/* 579 */     return getResources(new Predicate<String>() {
/*     */           public boolean apply(String input) {
/* 581 */             return pattern.matcher(input).matches();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getMethodParamNames(Method method) {
/* 590 */     Iterable<String> names = this.store.get(index((Class)MethodParameterNamesScanner.class), new String[] { Utils.name(method) });
/* 591 */     return !Iterables.isEmpty(names) ? Arrays.<String>asList(((String)Iterables.getOnlyElement(names)).split(", ")) : Arrays.<String>asList(new String[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getConstructorParamNames(Constructor constructor) {
/* 598 */     Iterable<String> names = this.store.get(index((Class)MethodParameterNamesScanner.class), new String[] { Utils.name(constructor) });
/* 599 */     return !Iterables.isEmpty(names) ? Arrays.<String>asList(((String)Iterables.getOnlyElement(names)).split(", ")) : Arrays.<String>asList(new String[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Member> getFieldUsage(Field field) {
/* 606 */     return Utils.getMembersFromDescriptors(this.store.get(index((Class)MemberUsageScanner.class), new String[] { Utils.name(field) }), new ClassLoader[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Member> getMethodUsage(Method method) {
/* 613 */     return Utils.getMembersFromDescriptors(this.store.get(index((Class)MemberUsageScanner.class), new String[] { Utils.name(method) }), new ClassLoader[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Member> getConstructorUsage(Constructor constructor) {
/* 620 */     return Utils.getMembersFromDescriptors(this.store.get(index((Class)MemberUsageScanner.class), new String[] { Utils.name(constructor) }), new ClassLoader[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getAllTypes() {
/* 630 */     Set<String> allTypes = Sets.newHashSet(this.store.getAll(index((Class)SubTypesScanner.class), Object.class.getName()));
/* 631 */     if (allTypes.isEmpty()) {
/* 632 */       throw new ReflectionsException("Couldn't find subtypes of Object. Make sure SubTypesScanner initialized to include Object class - new SubTypesScanner(false)");
/*     */     }
/*     */     
/* 635 */     return allTypes;
/*     */   }
/*     */ 
/*     */   
/*     */   public Store getStore() {
/* 640 */     return this.store;
/*     */   }
/*     */ 
/*     */   
/*     */   public Configuration getConfiguration() {
/* 645 */     return this.configuration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File save(String filename) {
/* 655 */     return save(filename, this.configuration.getSerializer());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File save(String filename, Serializer serializer) {
/* 664 */     File file = serializer.save(this, filename);
/* 665 */     if (log != null)
/* 666 */       log.info("Reflections successfully saved in " + file.getAbsolutePath() + " using " + serializer.getClass().getSimpleName()); 
/* 667 */     return file;
/*     */   }
/*     */   private static String index(Class<? extends Scanner> scannerClass) {
/* 670 */     return scannerClass.getSimpleName();
/*     */   } private ClassLoader[] loaders() {
/* 672 */     return this.configuration.getClassLoaders();
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\reflections\Reflections.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */