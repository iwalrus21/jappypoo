/*     */ package org.reflections.util;
/*     */ 
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Nullable;
/*     */ import org.reflections.ReflectionUtils;
/*     */ import org.reflections.Reflections;
/*     */ import org.reflections.ReflectionsException;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Utils
/*     */ {
/*     */   public static String repeat(String string, int times) {
/*  31 */     StringBuilder sb = new StringBuilder();
/*     */     
/*  33 */     for (int i = 0; i < times; i++) {
/*  34 */       sb.append(string);
/*     */     }
/*     */     
/*  37 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isEmpty(String s) {
/*  44 */     return (s == null || s.length() == 0);
/*     */   }
/*     */   
/*     */   public static boolean isEmpty(Object[] objects) {
/*  48 */     return (objects == null || objects.length == 0);
/*     */   }
/*     */   
/*     */   public static File prepareFile(String filename) {
/*  52 */     File file = new File(filename);
/*  53 */     File parent = file.getAbsoluteFile().getParentFile();
/*  54 */     if (!parent.exists())
/*     */     {
/*  56 */       parent.mkdirs();
/*     */     }
/*  58 */     return file;
/*     */   }
/*     */   
/*     */   public static Member getMemberFromDescriptor(String descriptor, ClassLoader... classLoaders) throws ReflectionsException {
/*  62 */     int p0 = descriptor.lastIndexOf('(');
/*  63 */     String memberKey = (p0 != -1) ? descriptor.substring(0, p0) : descriptor;
/*  64 */     String methodParameters = (p0 != -1) ? descriptor.substring(p0 + 1, descriptor.lastIndexOf(')')) : "";
/*     */     
/*  66 */     int p1 = Math.max(memberKey.lastIndexOf('.'), memberKey.lastIndexOf("$"));
/*  67 */     String className = memberKey.substring(memberKey.lastIndexOf(' ') + 1, p1);
/*  68 */     String memberName = memberKey.substring(p1 + 1);
/*     */     
/*  70 */     Class<?>[] parameterTypes = null;
/*  71 */     if (!isEmpty(methodParameters)) {
/*  72 */       String[] parameterNames = methodParameters.split(",");
/*  73 */       List<Class<?>> result = new ArrayList<>(parameterNames.length);
/*  74 */       for (String name : parameterNames) {
/*  75 */         result.add(ReflectionUtils.forName(name.trim(), classLoaders));
/*     */       }
/*  77 */       parameterTypes = (Class[])result.<Class<?>[]>toArray((Class<?>[][])new Class[result.size()]);
/*     */     } 
/*     */     
/*  80 */     Class<?> aClass = ReflectionUtils.forName(className, classLoaders);
/*  81 */     while (aClass != null) {
/*     */       try {
/*  83 */         if (!descriptor.contains("("))
/*  84 */           return aClass.isInterface() ? aClass.getField(memberName) : aClass.getDeclaredField(memberName); 
/*  85 */         if (isConstructor(descriptor)) {
/*  86 */           return aClass.isInterface() ? aClass.getConstructor(parameterTypes) : aClass.getDeclaredConstructor(parameterTypes);
/*     */         }
/*  88 */         return aClass.isInterface() ? aClass.getMethod(memberName, parameterTypes) : aClass.getDeclaredMethod(memberName, parameterTypes);
/*     */       }
/*  90 */       catch (Exception e) {
/*  91 */         aClass = aClass.getSuperclass();
/*     */       } 
/*     */     } 
/*  94 */     throw new ReflectionsException("Can't resolve member named " + memberName + " for class " + className);
/*     */   }
/*     */   
/*     */   public static Set<Method> getMethodsFromDescriptors(Iterable<String> annotatedWith, ClassLoader... classLoaders) {
/*  98 */     Set<Method> result = Sets.newHashSet();
/*  99 */     for (String annotated : annotatedWith) {
/* 100 */       if (!isConstructor(annotated)) {
/* 101 */         Method member = (Method)getMemberFromDescriptor(annotated, classLoaders);
/* 102 */         if (member != null) result.add(member); 
/*     */       } 
/*     */     } 
/* 105 */     return result;
/*     */   }
/*     */   
/*     */   public static Set<Constructor> getConstructorsFromDescriptors(Iterable<String> annotatedWith, ClassLoader... classLoaders) {
/* 109 */     Set<Constructor> result = Sets.newHashSet();
/* 110 */     for (String annotated : annotatedWith) {
/* 111 */       if (isConstructor(annotated)) {
/* 112 */         Constructor member = (Constructor)getMemberFromDescriptor(annotated, classLoaders);
/* 113 */         if (member != null) result.add(member); 
/*     */       } 
/*     */     } 
/* 116 */     return result;
/*     */   }
/*     */   
/*     */   public static Set<Member> getMembersFromDescriptors(Iterable<String> values, ClassLoader... classLoaders) {
/* 120 */     Set<Member> result = Sets.newHashSet();
/* 121 */     for (String value : values) {
/*     */       try {
/* 123 */         result.add(getMemberFromDescriptor(value, classLoaders));
/* 124 */       } catch (ReflectionsException e) {
/* 125 */         throw new ReflectionsException("Can't resolve member named " + value, e);
/*     */       } 
/*     */     } 
/* 128 */     return result;
/*     */   }
/*     */   
/*     */   public static Field getFieldFromString(String field, ClassLoader... classLoaders) {
/* 132 */     String className = field.substring(0, field.lastIndexOf('.'));
/* 133 */     String fieldName = field.substring(field.lastIndexOf('.') + 1);
/*     */     
/*     */     try {
/* 136 */       return ReflectionUtils.forName(className, classLoaders).getDeclaredField(fieldName);
/* 137 */     } catch (NoSuchFieldException e) {
/* 138 */       throw new ReflectionsException("Can't resolve field named " + fieldName, e);
/*     */     } 
/*     */   }
/*     */   public static void close(InputStream closeable) {
/*     */     try {
/* 143 */       if (closeable != null) closeable.close(); 
/* 144 */     } catch (IOException e) {
/* 145 */       if (Reflections.log != null) {
/* 146 */         Reflections.log.warn("Could not close InputStream", e);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static Logger findLogger(Class<?> aClass) {
/*     */     try {
/* 154 */       Class.forName("org.slf4j.impl.StaticLoggerBinder");
/* 155 */       return LoggerFactory.getLogger(aClass);
/* 156 */     } catch (Throwable e) {
/* 157 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean isConstructor(String fqn) {
/* 162 */     return fqn.contains("init>");
/*     */   }
/*     */   
/*     */   public static String name(Class<?> type) {
/* 166 */     if (!type.isArray()) {
/* 167 */       return type.getName();
/*     */     }
/* 169 */     int dim = 0;
/* 170 */     while (type.isArray()) {
/* 171 */       dim++;
/* 172 */       type = type.getComponentType();
/*     */     } 
/* 174 */     return type.getName() + repeat("[]", dim);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<String> names(Iterable<Class<?>> types) {
/* 180 */     List<String> result = new ArrayList<>();
/* 181 */     for (Class<?> type : types) result.add(name(type)); 
/* 182 */     return result;
/*     */   }
/*     */   
/*     */   public static List<String> names(Class<?>... types) {
/* 186 */     return names(Arrays.asList(types));
/*     */   }
/*     */   
/*     */   public static String name(Constructor constructor) {
/* 190 */     return constructor.getName() + ".<init>(" + Joiner.on(", ").join(names(constructor.getParameterTypes())) + ")";
/*     */   }
/*     */   
/*     */   public static String name(Method method) {
/* 194 */     return method.getDeclaringClass().getName() + "." + method.getName() + "(" + Joiner.on(", ").join(names(method.getParameterTypes())) + ")";
/*     */   }
/*     */   
/*     */   public static String name(Field field) {
/* 198 */     return field.getDeclaringClass().getName() + "." + field.getName();
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\reflection\\util\Utils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */