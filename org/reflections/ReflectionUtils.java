/*     */ package org.reflections;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.annotation.Nullable;
/*     */ import org.reflections.util.ClasspathHelper;
/*     */ import org.reflections.util.Utils;
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
/*     */ public abstract class ReflectionUtils
/*     */ {
/*     */   public static boolean includeObject = false;
/*     */   private static List<String> primitiveNames;
/*     */   private static List<Class> primitiveTypes;
/*     */   private static List<String> primitiveDescriptors;
/*     */   
/*     */   public static Set<Class<?>> getAllSuperTypes(Class<?> type, Predicate<? super Class<?>>... predicates) {
/*  65 */     Set<Class<?>> result = Sets.newLinkedHashSet();
/*  66 */     if (type != null && (includeObject || !type.equals(Object.class))) {
/*  67 */       result.add(type);
/*  68 */       for (Class<?> supertype : getSuperTypes(type)) {
/*  69 */         result.addAll(getAllSuperTypes(supertype, (Predicate<? super Class<?>>[])new Predicate[0]));
/*     */       }
/*     */     } 
/*  72 */     return filter(result, predicates);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Set<Class<?>> getSuperTypes(Class<?> type) {
/*  77 */     Set<Class<?>> result = new LinkedHashSet<>();
/*  78 */     Class<?> superclass = type.getSuperclass();
/*  79 */     Class<?>[] interfaces = type.getInterfaces();
/*  80 */     if (superclass != null && (includeObject || !superclass.equals(Object.class))) result.add(superclass); 
/*  81 */     if (interfaces != null && interfaces.length > 0) result.addAll(Arrays.asList(interfaces)); 
/*  82 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Set<Method> getAllMethods(Class<?> type, Predicate<? super Method>... predicates) {
/*  87 */     Set<Method> result = Sets.newHashSet();
/*  88 */     for (Class<?> t : getAllSuperTypes(type, (Predicate<? super Class<?>>[])new Predicate[0])) {
/*  89 */       result.addAll(getMethods(t, predicates));
/*     */     }
/*  91 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Set<Method> getMethods(Class<?> t, Predicate<? super Method>... predicates) {
/*  96 */     return filter(t.isInterface() ? t.getMethods() : t.getDeclaredMethods(), predicates);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Set<Constructor> getAllConstructors(Class<?> type, Predicate<? super Constructor>... predicates) {
/* 101 */     Set<Constructor> result = Sets.newHashSet();
/* 102 */     for (Class<?> t : getAllSuperTypes(type, (Predicate<? super Class<?>>[])new Predicate[0])) {
/* 103 */       result.addAll(getConstructors(t, predicates));
/*     */     }
/* 105 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Set<Constructor> getConstructors(Class<?> t, Predicate<? super Constructor>... predicates) {
/* 110 */     return filter((Constructor[])t.getDeclaredConstructors(), predicates);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Set<Field> getAllFields(Class<?> type, Predicate<? super Field>... predicates) {
/* 115 */     Set<Field> result = Sets.newHashSet();
/* 116 */     for (Class<?> t : getAllSuperTypes(type, (Predicate<? super Class<?>>[])new Predicate[0])) result.addAll(getFields(t, predicates)); 
/* 117 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Set<Field> getFields(Class<?> type, Predicate<? super Field>... predicates) {
/* 122 */     return filter(type.getDeclaredFields(), predicates);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T extends java.lang.reflect.AnnotatedElement> Set<Annotation> getAllAnnotations(T type, Predicate<Annotation>... predicates) {
/* 127 */     Set<Annotation> result = Sets.newHashSet();
/* 128 */     if (type instanceof Class) {
/* 129 */       for (Class<?> t : getAllSuperTypes((Class)type, (Predicate<? super Class<?>>[])new Predicate[0])) {
/* 130 */         result.addAll(getAnnotations(t, predicates));
/*     */       }
/*     */     } else {
/* 133 */       result.addAll(getAnnotations(type, predicates));
/*     */     } 
/* 135 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T extends java.lang.reflect.AnnotatedElement> Set<Annotation> getAnnotations(T type, Predicate<Annotation>... predicates) {
/* 140 */     return filter(type.getDeclaredAnnotations(), (Predicate<? super Annotation>[])predicates);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T extends java.lang.reflect.AnnotatedElement> Set<T> getAll(Set<T> elements, Predicate<? super T>... predicates) {
/* 145 */     return Utils.isEmpty((Object[])predicates) ? elements : Sets.newHashSet(Iterables.filter(elements, Predicates.and((Predicate[])predicates)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends Member> Predicate<T> withName(final String name) {
/* 151 */     return new Predicate<T>() {
/*     */         public boolean apply(@Nullable T input) {
/* 153 */           return (input != null && input.getName().equals(name));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T extends Member> Predicate<T> withPrefix(final String prefix) {
/* 160 */     return new Predicate<T>() {
/*     */         public boolean apply(@Nullable T input) {
/* 162 */           return (input != null && input.getName().startsWith(prefix));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends java.lang.reflect.AnnotatedElement> Predicate<T> withPattern(final String regex) {
/* 174 */     return new Predicate<T>() {
/*     */         public boolean apply(@Nullable T input) {
/* 176 */           return Pattern.matches(regex, input.toString());
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T extends java.lang.reflect.AnnotatedElement> Predicate<T> withAnnotation(final Class<? extends Annotation> annotation) {
/* 183 */     return new Predicate<T>() {
/*     */         public boolean apply(@Nullable T input) {
/* 185 */           return (input != null && input.isAnnotationPresent(annotation));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T extends java.lang.reflect.AnnotatedElement> Predicate<T> withAnnotations(Class<? extends Annotation>... annotations) {
/* 192 */     return new Predicate<T>() {
/*     */         public boolean apply(@Nullable T input) {
/* 194 */           return (input != null && Arrays.equals((Object[])annotations, (Object[])ReflectionUtils.annotationTypes(input.getAnnotations())));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T extends java.lang.reflect.AnnotatedElement> Predicate<T> withAnnotation(final Annotation annotation) {
/* 201 */     return new Predicate<T>() {
/*     */         public boolean apply(@Nullable T input) {
/* 203 */           return (input != null && input.isAnnotationPresent(annotation.annotationType()) && ReflectionUtils
/* 204 */             .areAnnotationMembersMatching(input.getAnnotation((Class)annotation.annotationType()), annotation));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T extends java.lang.reflect.AnnotatedElement> Predicate<T> withAnnotations(Annotation... annotations) {
/* 211 */     return new Predicate<T>() {
/*     */         public boolean apply(@Nullable T input) {
/* 213 */           if (input != null) {
/* 214 */             Annotation[] inputAnnotations = input.getAnnotations();
/* 215 */             if (inputAnnotations.length == annotations.length)
/* 216 */               for (int i = 0; i < inputAnnotations.length; i++) {
/* 217 */                 if (!ReflectionUtils.areAnnotationMembersMatching(inputAnnotations[i], annotations[i])) return false;
/*     */               
/*     */               }  
/*     */           } 
/* 221 */           return true;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static Predicate<Member> withParameters(Class<?>... types) {
/* 228 */     return new Predicate<Member>() {
/*     */         public boolean apply(@Nullable Member input) {
/* 230 */           return Arrays.equals((Object[])ReflectionUtils.parameterTypes(input), (Object[])types);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static Predicate<Member> withParametersAssignableTo(Class... types) {
/* 237 */     return new Predicate<Member>() {
/*     */         public boolean apply(@Nullable Member input) {
/* 239 */           if (input != null) {
/* 240 */             Class<?>[] parameterTypes = ReflectionUtils.parameterTypes(input);
/* 241 */             if (parameterTypes.length == types.length) {
/* 242 */               for (int i = 0; i < parameterTypes.length; i++) {
/* 243 */                 if (!parameterTypes[i].isAssignableFrom(types[i]) || (parameterTypes[i] == Object.class && types[i] != Object.class))
/*     */                 {
/* 245 */                   return false;
/*     */                 }
/*     */               } 
/* 248 */               return true;
/*     */             } 
/*     */           } 
/* 251 */           return false;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static Predicate<Member> withParametersCount(final int count) {
/* 258 */     return new Predicate<Member>() {
/*     */         public boolean apply(@Nullable Member input) {
/* 260 */           return (input != null && (ReflectionUtils.parameterTypes(input)).length == count);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static Predicate<Member> withAnyParameterAnnotation(final Class<? extends Annotation> annotationClass) {
/* 267 */     return new Predicate<Member>() {
/*     */         public boolean apply(@Nullable Member input) {
/* 269 */           return (input != null && Iterables.any(ReflectionUtils.annotationTypes(ReflectionUtils.parameterAnnotations(input)), new Predicate<Class<? extends Annotation>>() {
/*     */                 public boolean apply(@Nullable Class<? extends Annotation> input) {
/* 271 */                   return input.equals(annotationClass);
/*     */                 }
/*     */               }));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static Predicate<Member> withAnyParameterAnnotation(final Annotation annotation) {
/* 280 */     return new Predicate<Member>() {
/*     */         public boolean apply(@Nullable Member input) {
/* 282 */           return (input != null && Iterables.any(ReflectionUtils.parameterAnnotations(input), new Predicate<Annotation>() {
/*     */                 public boolean apply(@Nullable Annotation input) {
/* 284 */                   return ReflectionUtils.areAnnotationMembersMatching(annotation, input);
/*     */                 }
/*     */               }));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> Predicate<Field> withType(final Class<T> type) {
/* 293 */     return new Predicate<Field>() {
/*     */         public boolean apply(@Nullable Field input) {
/* 295 */           return (input != null && input.getType().equals(type));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> Predicate<Field> withTypeAssignableTo(final Class<T> type) {
/* 302 */     return new Predicate<Field>() {
/*     */         public boolean apply(@Nullable Field input) {
/* 304 */           return (input != null && type.isAssignableFrom(input.getType()));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> Predicate<Method> withReturnType(final Class<T> type) {
/* 311 */     return new Predicate<Method>() {
/*     */         public boolean apply(@Nullable Method input) {
/* 313 */           return (input != null && input.getReturnType().equals(type));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> Predicate<Method> withReturnTypeAssignableTo(final Class<T> type) {
/* 320 */     return new Predicate<Method>() {
/*     */         public boolean apply(@Nullable Method input) {
/* 322 */           return (input != null && type.isAssignableFrom(input.getReturnType()));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends Member> Predicate<T> withModifier(final int mod) {
/* 334 */     return new Predicate<T>() {
/*     */         public boolean apply(@Nullable T input) {
/* 336 */           return (input != null && (input.getModifiers() & mod) != 0);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Predicate<Class<?>> withClassModifier(final int mod) {
/* 348 */     return new Predicate<Class<?>>() {
/*     */         public boolean apply(@Nullable Class<?> input) {
/* 350 */           return (input != null && (input.getModifiers() & mod) != 0);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class<?> forName(String typeName, ClassLoader... classLoaders) {
/*     */     String type;
/* 360 */     if (getPrimitiveNames().contains(typeName)) {
/* 361 */       return getPrimitiveTypes().get(getPrimitiveNames().indexOf(typeName));
/*     */     }
/*     */     
/* 364 */     if (typeName.contains("[")) {
/* 365 */       int i = typeName.indexOf("[");
/* 366 */       type = typeName.substring(0, i);
/* 367 */       String array = typeName.substring(i).replace("]", "");
/*     */       
/* 369 */       if (getPrimitiveNames().contains(type)) {
/* 370 */         type = getPrimitiveDescriptors().get(getPrimitiveNames().indexOf(type));
/*     */       } else {
/* 372 */         type = "L" + type + ";";
/*     */       } 
/*     */       
/* 375 */       type = array + type;
/*     */     } else {
/* 377 */       type = typeName;
/*     */     } 
/*     */     
/* 380 */     List<ReflectionsException> reflectionsExceptions = Lists.newArrayList();
/* 381 */     for (ClassLoader classLoader : ClasspathHelper.classLoaders(classLoaders)) {
/* 382 */       if (type.contains("["))
/* 383 */         try { return Class.forName(type, false, classLoader); }
/* 384 */         catch (Throwable e)
/* 385 */         { reflectionsExceptions.add(new ReflectionsException("could not get type for name " + typeName, e)); }
/*     */          
/*     */       try {
/* 388 */         return classLoader.loadClass(type);
/* 389 */       } catch (Throwable e) {
/* 390 */         reflectionsExceptions.add(new ReflectionsException("could not get type for name " + typeName, e));
/*     */       } 
/*     */     } 
/*     */     
/* 394 */     if (Reflections.log != null) {
/* 395 */       for (ReflectionsException reflectionsException : reflectionsExceptions) {
/* 396 */         Reflections.log.warn("could not get type for name " + typeName + " from any class loader", reflectionsException);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 401 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> List<Class<? extends T>> forNames(Iterable<String> classes, ClassLoader... classLoaders) {
/* 407 */     List<Class<? extends T>> result = new ArrayList<>();
/* 408 */     for (String className : classes) {
/* 409 */       Class<?> type = forName(className, classLoaders);
/* 410 */       if (type != null) {
/* 411 */         result.add(type);
/*     */       }
/*     */     } 
/* 414 */     return result;
/*     */   }
/*     */   
/*     */   private static Class[] parameterTypes(Member member) {
/* 418 */     return (member != null) ? (
/* 419 */       (member.getClass() == Method.class) ? ((Method)member).getParameterTypes() : (
/* 420 */       (member.getClass() == Constructor.class) ? ((Constructor)member).getParameterTypes() : null)) : null;
/*     */   }
/*     */   
/*     */   private static Set<Annotation> parameterAnnotations(Member member) {
/* 424 */     Set<Annotation> result = Sets.newHashSet();
/*     */ 
/*     */     
/* 427 */     Annotation[][] annotations = (member instanceof Method) ? ((Method)member).getParameterAnnotations() : ((member instanceof Constructor) ? ((Constructor)member).getParameterAnnotations() : (Annotation[][])null);
/* 428 */     for (Annotation[] annotation : annotations) Collections.addAll(result, annotation); 
/* 429 */     return result;
/*     */   }
/*     */   
/*     */   private static Set<Class<? extends Annotation>> annotationTypes(Iterable<Annotation> annotations) {
/* 433 */     Set<Class<? extends Annotation>> result = Sets.newHashSet();
/* 434 */     for (Annotation annotation : annotations) result.add(annotation.annotationType()); 
/* 435 */     return result;
/*     */   }
/*     */   
/*     */   private static Class<? extends Annotation>[] annotationTypes(Annotation[] annotations) {
/* 439 */     Class[] arrayOfClass = new Class[annotations.length];
/* 440 */     for (int i = 0; i < annotations.length; ) { arrayOfClass[i] = annotations[i].annotationType(); i++; }
/* 441 */      return (Class<? extends Annotation>[])arrayOfClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initPrimitives() {
/* 450 */     if (primitiveNames == null) {
/* 451 */       primitiveNames = Lists.newArrayList((Object[])new String[] { "boolean", "char", "byte", "short", "int", "long", "float", "double", "void" });
/* 452 */       primitiveTypes = Lists.newArrayList((Object[])new Class[] { boolean.class, char.class, byte.class, short.class, int.class, long.class, float.class, double.class, void.class });
/* 453 */       primitiveDescriptors = Lists.newArrayList((Object[])new String[] { "Z", "C", "B", "S", "I", "J", "F", "D", "V" });
/*     */     } 
/*     */   }
/*     */   
/* 457 */   private static List<String> getPrimitiveNames() { initPrimitives(); return primitiveNames; }
/* 458 */   private static List<Class> getPrimitiveTypes() { initPrimitives(); return primitiveTypes; } private static List<String> getPrimitiveDescriptors() {
/* 459 */     initPrimitives(); return primitiveDescriptors;
/*     */   }
/*     */   
/*     */   static <T> Set<T> filter(T[] elements, Predicate<? super T>... predicates) {
/* 463 */     return Utils.isEmpty((Object[])predicates) ? Sets.newHashSet((Object[])elements) : 
/* 464 */       Sets.newHashSet(Iterables.filter(Arrays.asList(elements), Predicates.and((Predicate[])predicates)));
/*     */   }
/*     */   
/*     */   static <T> Set<T> filter(Iterable<T> elements, Predicate<? super T>... predicates) {
/* 468 */     return Utils.isEmpty((Object[])predicates) ? Sets.newHashSet(elements) : 
/* 469 */       Sets.newHashSet(Iterables.filter(elements, Predicates.and((Predicate[])predicates)));
/*     */   }
/*     */   
/*     */   private static boolean areAnnotationMembersMatching(Annotation annotation1, Annotation annotation2) {
/* 473 */     if (annotation2 != null && annotation1.annotationType() == annotation2.annotationType()) {
/* 474 */       for (Method method : annotation1.annotationType().getDeclaredMethods()) {
/*     */         try {
/* 476 */           if (!method.invoke(annotation1, new Object[0]).equals(method.invoke(annotation2, new Object[0]))) return false; 
/* 477 */         } catch (Exception e) {
/* 478 */           throw new ReflectionsException(String.format("could not invoke method %s on annotation %s", new Object[] { method.getName(), annotation1.annotationType() }), e);
/*     */         } 
/*     */       } 
/* 481 */       return true;
/*     */     } 
/* 483 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\reflections\ReflectionUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */