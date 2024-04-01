/*     */ package org.reflections.serializers;
/*     */ 
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Multimaps;
/*     */ import com.google.common.collect.SetMultimap;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.common.io.Files;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.reflections.ReflectionUtils;
/*     */ import org.reflections.Reflections;
/*     */ import org.reflections.ReflectionsException;
/*     */ import org.reflections.scanners.TypeElementsScanner;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JavaCodeSerializer
/*     */   implements Serializer
/*     */ {
/*     */   private static final String pathSeparator = "_";
/*     */   private static final String doubleSeparator = "__";
/*     */   private static final String dotSeparator = ".";
/*     */   private static final String arrayDescriptor = "$$";
/*     */   private static final String tokenSeparator = "_";
/*     */   
/*     */   public Reflections read(InputStream inputStream) {
/*  76 */     throw new UnsupportedOperationException("read is not implemented on JavaCodeSerializer");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File save(Reflections reflections, String name) {
/*     */     String packageName, className;
/*  85 */     if (name.endsWith("/")) {
/*  86 */       name = name.substring(0, name.length() - 1);
/*     */     }
/*     */ 
/*     */     
/*  90 */     String filename = name.replace('.', '/').concat(".java");
/*  91 */     File file = Utils.prepareFile(filename);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  96 */     int lastDot = name.lastIndexOf('.');
/*  97 */     if (lastDot == -1) {
/*  98 */       packageName = "";
/*  99 */       className = name.substring(name.lastIndexOf('/') + 1);
/*     */     } else {
/* 101 */       packageName = name.substring(name.lastIndexOf('/') + 1, lastDot);
/* 102 */       className = name.substring(lastDot + 1);
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 107 */       StringBuilder sb = new StringBuilder();
/* 108 */       sb.append("//generated using Reflections JavaCodeSerializer")
/* 109 */         .append(" [").append(new Date()).append("]")
/* 110 */         .append("\n");
/* 111 */       if (packageName.length() != 0) {
/* 112 */         sb.append("package ").append(packageName).append(";\n");
/* 113 */         sb.append("\n");
/*     */       } 
/* 115 */       sb.append("public interface ").append(className).append(" {\n\n");
/* 116 */       sb.append(toString(reflections));
/* 117 */       sb.append("}\n");
/*     */       
/* 119 */       Files.write(sb.toString(), new File(filename), Charset.defaultCharset());
/*     */     }
/* 121 */     catch (IOException e) {
/* 122 */       throw new RuntimeException();
/*     */     } 
/*     */     
/* 125 */     return file;
/*     */   }
/*     */   
/*     */   public String toString(Reflections reflections) {
/* 129 */     if (reflections.getStore().get(TypeElementsScanner.class.getSimpleName()).isEmpty() && 
/* 130 */       Reflections.log != null) Reflections.log.warn("JavaCodeSerializer needs TypeElementsScanner configured");
/*     */ 
/*     */     
/* 133 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 135 */     List<String> prevPaths = Lists.newArrayList();
/* 136 */     int indent = 1;
/*     */     
/* 138 */     List<String> keys = Lists.newArrayList(reflections.getStore().get(TypeElementsScanner.class.getSimpleName()).keySet());
/* 139 */     Collections.sort(keys);
/* 140 */     for (String fqn : keys) {
/* 141 */       List<String> typePaths = Lists.newArrayList((Object[])fqn.split("\\."));
/*     */ 
/*     */       
/* 144 */       int i = 0;
/* 145 */       while (i < Math.min(typePaths.size(), prevPaths.size()) && ((String)typePaths.get(i)).equals(prevPaths.get(i))) {
/* 146 */         i++;
/*     */       }
/*     */       
/*     */       int k;
/* 150 */       for (k = prevPaths.size(); k > i; k--) {
/* 151 */         sb.append(Utils.repeat("\t", --indent)).append("}\n");
/*     */       }
/*     */ 
/*     */       
/* 155 */       for (k = i; k < typePaths.size() - 1; k++) {
/* 156 */         sb.append(Utils.repeat("\t", indent++)).append("public interface ").append(getNonDuplicateName(typePaths.get(k), typePaths, k)).append(" {\n");
/*     */       }
/*     */ 
/*     */       
/* 160 */       String className = typePaths.get(typePaths.size() - 1);
/*     */ 
/*     */       
/* 163 */       List<String> annotations = Lists.newArrayList();
/* 164 */       List<String> fields = Lists.newArrayList();
/* 165 */       SetMultimap setMultimap = Multimaps.newSetMultimap(new HashMap<>(), new Supplier<Set<String>>() {
/*     */             public Set<String> get() {
/* 167 */               return Sets.newHashSet();
/*     */             }
/*     */           });
/*     */       
/* 171 */       for (String element : reflections.getStore().get(TypeElementsScanner.class.getSimpleName(), new String[] { fqn })) {
/* 172 */         if (element.startsWith("@")) {
/* 173 */           annotations.add(element.substring(1)); continue;
/* 174 */         }  if (element.contains("(")) {
/*     */           
/* 176 */           if (!element.startsWith("<")) {
/* 177 */             int i1 = element.indexOf('(');
/* 178 */             String name = element.substring(0, i1);
/* 179 */             String params = element.substring(i1 + 1, element.indexOf(")"));
/*     */             
/* 181 */             String paramsDescriptor = "";
/* 182 */             if (params.length() != 0) {
/* 183 */               paramsDescriptor = "_" + params.replace(".", "_").replace(", ", "__").replace("[]", "$$");
/*     */             }
/* 185 */             String normalized = name + paramsDescriptor;
/* 186 */             setMultimap.put(name, normalized);
/*     */           }  continue;
/* 188 */         }  if (!Utils.isEmpty(element))
/*     */         {
/* 190 */           fields.add(element);
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 195 */       sb.append(Utils.repeat("\t", indent++)).append("public interface ").append(getNonDuplicateName(className, typePaths, typePaths.size() - 1)).append(" {\n");
/*     */ 
/*     */       
/* 198 */       if (!fields.isEmpty()) {
/* 199 */         sb.append(Utils.repeat("\t", indent++)).append("public interface fields {\n");
/* 200 */         for (String field : fields) {
/* 201 */           sb.append(Utils.repeat("\t", indent)).append("public interface ").append(getNonDuplicateName(field, typePaths)).append(" {}\n");
/*     */         }
/* 203 */         sb.append(Utils.repeat("\t", --indent)).append("}\n");
/*     */       } 
/*     */ 
/*     */       
/* 207 */       if (!setMultimap.isEmpty()) {
/* 208 */         sb.append(Utils.repeat("\t", indent++)).append("public interface methods {\n");
/* 209 */         for (Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)setMultimap.entries()) {
/* 210 */           String simpleName = entry.getKey();
/* 211 */           String normalized = entry.getValue();
/*     */           
/* 213 */           String methodName = (setMultimap.get(simpleName).size() == 1) ? simpleName : normalized;
/*     */           
/* 215 */           methodName = getNonDuplicateName(methodName, fields);
/*     */           
/* 217 */           sb.append(Utils.repeat("\t", indent)).append("public interface ").append(getNonDuplicateName(methodName, typePaths)).append(" {}\n");
/*     */         } 
/* 219 */         sb.append(Utils.repeat("\t", --indent)).append("}\n");
/*     */       } 
/*     */ 
/*     */       
/* 223 */       if (!annotations.isEmpty()) {
/* 224 */         sb.append(Utils.repeat("\t", indent++)).append("public interface annotations {\n");
/* 225 */         for (String annotation : annotations) {
/* 226 */           String nonDuplicateName = annotation;
/* 227 */           nonDuplicateName = getNonDuplicateName(nonDuplicateName, typePaths);
/* 228 */           sb.append(Utils.repeat("\t", indent)).append("public interface ").append(nonDuplicateName).append(" {}\n");
/*     */         } 
/* 230 */         sb.append(Utils.repeat("\t", --indent)).append("}\n");
/*     */       } 
/*     */       
/* 233 */       prevPaths = typePaths;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 238 */     for (int j = prevPaths.size(); j >= 1; j--) {
/* 239 */       sb.append(Utils.repeat("\t", j)).append("}\n");
/*     */     }
/*     */     
/* 242 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private String getNonDuplicateName(String candidate, List<String> prev, int offset) {
/* 246 */     String normalized = normalize(candidate);
/* 247 */     for (int i = 0; i < offset; i++) {
/* 248 */       if (normalized.equals(prev.get(i))) {
/* 249 */         return getNonDuplicateName(normalized + "_", prev, offset);
/*     */       }
/*     */     } 
/*     */     
/* 253 */     return normalized;
/*     */   }
/*     */   
/*     */   private String normalize(String candidate) {
/* 257 */     return candidate.replace(".", "_");
/*     */   }
/*     */   
/*     */   private String getNonDuplicateName(String candidate, List<String> prev) {
/* 261 */     return getNonDuplicateName(candidate, prev, prev.size());
/*     */   }
/*     */ 
/*     */   
/*     */   public static Class<?> resolveClassOf(Class<?> element) throws ClassNotFoundException {
/* 266 */     Class<?> cursor = element;
/* 267 */     LinkedList<String> ognl = Lists.newLinkedList();
/*     */     
/* 269 */     while (cursor != null) {
/* 270 */       ognl.addFirst(cursor.getSimpleName());
/* 271 */       cursor = cursor.getDeclaringClass();
/*     */     } 
/*     */     
/* 274 */     String classOgnl = Joiner.on(".").join(ognl.subList(1, ognl.size())).replace(".$", "$");
/* 275 */     return Class.forName(classOgnl);
/*     */   }
/*     */   
/*     */   public static Class<?> resolveClass(Class aClass) {
/*     */     try {
/* 280 */       return resolveClassOf(aClass);
/* 281 */     } catch (Exception e) {
/* 282 */       throw new ReflectionsException("could not resolve to class " + aClass.getName(), e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Field resolveField(Class aField) {
/*     */     try {
/* 288 */       String name = aField.getSimpleName();
/* 289 */       Class<?> declaringClass = aField.getDeclaringClass().getDeclaringClass();
/* 290 */       return resolveClassOf(declaringClass).getDeclaredField(name);
/* 291 */     } catch (Exception e) {
/* 292 */       throw new ReflectionsException("could not resolve to field " + aField.getName(), e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Annotation resolveAnnotation(Class annotation) {
/*     */     try {
/* 298 */       String name = annotation.getSimpleName().replace("_", ".");
/* 299 */       Class<?> declaringClass = annotation.getDeclaringClass().getDeclaringClass();
/* 300 */       Class<?> aClass = resolveClassOf(declaringClass);
/* 301 */       Class<? extends Annotation> aClass1 = ReflectionUtils.forName(name, new ClassLoader[0]);
/* 302 */       Annotation annotation1 = aClass.getAnnotation((Class)aClass1);
/* 303 */       return annotation1;
/* 304 */     } catch (Exception e) {
/* 305 */       throw new ReflectionsException("could not resolve to annotation " + annotation.getName(), e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Method resolveMethod(Class aMethod) {
/* 310 */     String methodOgnl = aMethod.getSimpleName();
/*     */     
/*     */     try {
/*     */       String methodName;
/*     */       Class<?>[] paramTypes;
/* 315 */       if (methodOgnl.contains("_")) {
/* 316 */         methodName = methodOgnl.substring(0, methodOgnl.indexOf("_"));
/* 317 */         String[] params = methodOgnl.substring(methodOgnl.indexOf("_") + 1).split("__");
/* 318 */         paramTypes = new Class[params.length];
/* 319 */         for (int i = 0; i < params.length; i++) {
/* 320 */           String typeName = params[i].replace("$$", "[]").replace("_", ".");
/* 321 */           paramTypes[i] = ReflectionUtils.forName(typeName, new ClassLoader[0]);
/*     */         } 
/*     */       } else {
/* 324 */         methodName = methodOgnl;
/* 325 */         paramTypes = null;
/*     */       } 
/*     */       
/* 328 */       Class<?> declaringClass = aMethod.getDeclaringClass().getDeclaringClass();
/* 329 */       return resolveClassOf(declaringClass).getDeclaredMethod(methodName, paramTypes);
/* 330 */     } catch (Exception e) {
/* 331 */       throw new ReflectionsException("could not resolve to method " + aMethod.getName(), e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\reflections\serializers\JavaCodeSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */