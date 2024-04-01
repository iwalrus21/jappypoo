/*     */ package org.reflections.adapters;
/*     */ 
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ import org.reflections.ReflectionUtils;
/*     */ import org.reflections.util.Utils;
/*     */ import org.reflections.vfs.Vfs;
/*     */ 
/*     */ public class JavaReflectionAdapter implements MetadataAdapter<Class, Field, Member> {
/*     */   public List<Field> getFields(Class cls) {
/*  21 */     return Lists.newArrayList((Object[])cls.getDeclaredFields());
/*     */   }
/*     */   
/*     */   public List<Member> getMethods(Class cls) {
/*  25 */     List<Member> methods = Lists.newArrayList();
/*  26 */     methods.addAll(Arrays.asList((Member[])cls.getDeclaredMethods()));
/*  27 */     methods.addAll(Arrays.asList((Member[])cls.getDeclaredConstructors()));
/*  28 */     return methods;
/*     */   }
/*     */   
/*     */   public String getMethodName(Member method) {
/*  32 */     return (method instanceof Method) ? method.getName() : ((method instanceof Constructor) ? "<init>" : null);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> getParameterNames(Member member) {
/*  37 */     List<String> result = Lists.newArrayList();
/*     */ 
/*     */     
/*  40 */     Class<?>[] parameterTypes = (member instanceof Method) ? ((Method)member).getParameterTypes() : ((member instanceof Constructor) ? ((Constructor)member).getParameterTypes() : null);
/*     */     
/*  42 */     if (parameterTypes != null) {
/*  43 */       for (Class<?> paramType : parameterTypes) {
/*  44 */         String name = getName(paramType);
/*  45 */         result.add(name);
/*     */       } 
/*     */     }
/*     */     
/*  49 */     return result;
/*     */   }
/*     */   
/*     */   public List<String> getClassAnnotationNames(Class aClass) {
/*  53 */     return getAnnotationNames(aClass.getDeclaredAnnotations());
/*     */   }
/*     */   
/*     */   public List<String> getFieldAnnotationNames(Field field) {
/*  57 */     return getAnnotationNames(field.getDeclaredAnnotations());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getMethodAnnotationNames(Member method) {
/*  63 */     Annotation[] annotations = (method instanceof Method) ? ((Method)method).getDeclaredAnnotations() : ((method instanceof Constructor) ? ((Constructor)method).getDeclaredAnnotations() : null);
/*  64 */     return getAnnotationNames(annotations);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getParameterAnnotationNames(Member method, int parameterIndex) {
/*  70 */     Annotation[][] annotations = (method instanceof Method) ? ((Method)method).getParameterAnnotations() : ((method instanceof Constructor) ? ((Constructor)method).getParameterAnnotations() : (Annotation[][])null);
/*     */     
/*  72 */     return getAnnotationNames((annotations != null) ? annotations[parameterIndex] : null);
/*     */   }
/*     */   
/*     */   public String getReturnTypeName(Member method) {
/*  76 */     return ((Method)method).getReturnType().getName();
/*     */   }
/*     */   
/*     */   public String getFieldName(Field field) {
/*  80 */     return field.getName();
/*     */   }
/*     */   
/*     */   public Class getOfCreateClassObject(Vfs.File file) throws Exception {
/*  84 */     return getOfCreateClassObject(file, null);
/*     */   }
/*     */   
/*     */   public Class getOfCreateClassObject(Vfs.File file, @Nullable ClassLoader... loaders) throws Exception {
/*  88 */     String name = file.getRelativePath().replace("/", ".").replace(".class", "");
/*  89 */     return ReflectionUtils.forName(name, loaders);
/*     */   }
/*     */   
/*     */   public String getMethodModifier(Member method) {
/*  93 */     return Modifier.toString(method.getModifiers());
/*     */   }
/*     */   
/*     */   public String getMethodKey(Class cls, Member method) {
/*  97 */     return getMethodName(method) + "(" + Joiner.on(", ").join(getParameterNames(method)) + ")";
/*     */   }
/*     */   
/*     */   public String getMethodFullKey(Class cls, Member method) {
/* 101 */     return getClassName(cls) + "." + getMethodKey(cls, method);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPublic(Object o) {
/* 106 */     Integer mod = Integer.valueOf((o instanceof Class) ? ((Class)o).getModifiers() : ((o instanceof Member) ? 
/* 107 */         Integer.valueOf(((Member)o).getModifiers()) : null).intValue());
/*     */     
/* 109 */     return (mod != null && Modifier.isPublic(mod.intValue()));
/*     */   }
/*     */   
/*     */   public String getClassName(Class cls) {
/* 113 */     return cls.getName();
/*     */   }
/*     */   
/*     */   public String getSuperclassName(Class cls) {
/* 117 */     Class superclass = cls.getSuperclass();
/* 118 */     return (superclass != null) ? superclass.getName() : "";
/*     */   }
/*     */   
/*     */   public List<String> getInterfacesNames(Class cls) {
/* 122 */     Class[] classes = cls.getInterfaces();
/* 123 */     List<String> names = new ArrayList<>((classes != null) ? classes.length : 0);
/* 124 */     if (classes != null) for (Class cls1 : classes) names.add(cls1.getName());  
/* 125 */     return names;
/*     */   }
/*     */   
/*     */   public boolean acceptsInput(String file) {
/* 129 */     return file.endsWith(".class");
/*     */   }
/*     */ 
/*     */   
/*     */   private List<String> getAnnotationNames(Annotation[] annotations) {
/* 134 */     List<String> names = new ArrayList<>(annotations.length);
/* 135 */     for (Annotation annotation : annotations) {
/* 136 */       names.add(annotation.annotationType().getName());
/*     */     }
/* 138 */     return names;
/*     */   }
/*     */   
/*     */   public static String getName(Class<?> type) {
/* 142 */     if (type.isArray()) {
/*     */       try {
/* 144 */         Class<?> cl = type; int dim;
/* 145 */         for (dim = 0; cl.isArray(); ) { dim++; cl = cl.getComponentType(); }
/* 146 */          return cl.getName() + Utils.repeat("[]", dim);
/* 147 */       } catch (Throwable throwable) {}
/*     */     }
/*     */ 
/*     */     
/* 151 */     return type.getName();
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\reflections\adapters\JavaReflectionAdapter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */