/*     */ package org.reflections.adapters;
/*     */ 
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import javassist.bytecode.AccessFlag;
/*     */ import javassist.bytecode.AnnotationsAttribute;
/*     */ import javassist.bytecode.ClassFile;
/*     */ import javassist.bytecode.Descriptor;
/*     */ import javassist.bytecode.FieldInfo;
/*     */ import javassist.bytecode.MethodInfo;
/*     */ import javassist.bytecode.ParameterAnnotationsAttribute;
/*     */ import javassist.bytecode.annotation.Annotation;
/*     */ import org.reflections.ReflectionsException;
/*     */ import org.reflections.util.Utils;
/*     */ import org.reflections.vfs.Vfs;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JavassistAdapter
/*     */   implements MetadataAdapter<ClassFile, FieldInfo, MethodInfo>
/*     */ {
/*     */   public static boolean includeInvisibleTag = true;
/*     */   
/*     */   public List<FieldInfo> getFields(ClassFile cls) {
/*  31 */     return cls.getFields();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<MethodInfo> getMethods(ClassFile cls) {
/*  36 */     return cls.getMethods();
/*     */   }
/*     */   
/*     */   public String getMethodName(MethodInfo method) {
/*  40 */     return method.getName();
/*     */   }
/*     */   
/*     */   public List<String> getParameterNames(MethodInfo method) {
/*  44 */     String descriptor = method.getDescriptor();
/*  45 */     descriptor = descriptor.substring(descriptor.indexOf("(") + 1, descriptor.lastIndexOf(")"));
/*  46 */     return splitDescriptorToTypeNames(descriptor);
/*     */   }
/*     */   
/*     */   public List<String> getClassAnnotationNames(ClassFile aClass) {
/*  50 */     return getAnnotationNames(new AnnotationsAttribute[] { (AnnotationsAttribute)aClass.getAttribute("RuntimeVisibleAnnotations"), includeInvisibleTag ? (AnnotationsAttribute)aClass
/*  51 */           .getAttribute("RuntimeInvisibleAnnotations") : null });
/*     */   }
/*     */   
/*     */   public List<String> getFieldAnnotationNames(FieldInfo field) {
/*  55 */     return getAnnotationNames(new AnnotationsAttribute[] { (AnnotationsAttribute)field.getAttribute("RuntimeVisibleAnnotations"), includeInvisibleTag ? (AnnotationsAttribute)field
/*  56 */           .getAttribute("RuntimeInvisibleAnnotations") : null });
/*     */   }
/*     */   
/*     */   public List<String> getMethodAnnotationNames(MethodInfo method) {
/*  60 */     return getAnnotationNames(new AnnotationsAttribute[] { (AnnotationsAttribute)method.getAttribute("RuntimeVisibleAnnotations"), includeInvisibleTag ? (AnnotationsAttribute)method
/*  61 */           .getAttribute("RuntimeInvisibleAnnotations") : null });
/*     */   }
/*     */   
/*     */   public List<String> getParameterAnnotationNames(MethodInfo method, int parameterIndex) {
/*  65 */     List<String> result = Lists.newArrayList();
/*     */     
/*  67 */     List<ParameterAnnotationsAttribute> parameterAnnotationsAttributes = Lists.newArrayList((Object[])new ParameterAnnotationsAttribute[] { (ParameterAnnotationsAttribute)method.getAttribute("RuntimeVisibleParameterAnnotations"), (ParameterAnnotationsAttribute)method
/*  68 */           .getAttribute("RuntimeInvisibleParameterAnnotations") });
/*     */     
/*  70 */     if (parameterAnnotationsAttributes != null) {
/*  71 */       for (ParameterAnnotationsAttribute parameterAnnotationsAttribute : parameterAnnotationsAttributes) {
/*  72 */         if (parameterAnnotationsAttribute != null) {
/*  73 */           Annotation[][] annotations = parameterAnnotationsAttribute.getAnnotations();
/*  74 */           if (parameterIndex < annotations.length) {
/*  75 */             Annotation[] annotation = annotations[parameterIndex];
/*  76 */             result.addAll(getAnnotationNames(annotation));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*  82 */     return result;
/*     */   }
/*     */   
/*     */   public String getReturnTypeName(MethodInfo method) {
/*  86 */     String descriptor = method.getDescriptor();
/*  87 */     descriptor = descriptor.substring(descriptor.lastIndexOf(")") + 1);
/*  88 */     return splitDescriptorToTypeNames(descriptor).get(0);
/*     */   }
/*     */   
/*     */   public String getFieldName(FieldInfo field) {
/*  92 */     return field.getName();
/*     */   }
/*     */   
/*     */   public ClassFile getOfCreateClassObject(Vfs.File file) {
/*  96 */     InputStream inputStream = null;
/*     */     try {
/*  98 */       inputStream = file.openInputStream();
/*  99 */       DataInputStream dis = new DataInputStream(new BufferedInputStream(inputStream));
/* 100 */       return new ClassFile(dis);
/* 101 */     } catch (IOException e) {
/* 102 */       throw new ReflectionsException("could not create class file from " + file.getName(), e);
/*     */     } finally {
/* 104 */       Utils.close(inputStream);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getMethodModifier(MethodInfo method) {
/* 109 */     int accessFlags = method.getAccessFlags();
/* 110 */     return AccessFlag.isPrivate(accessFlags) ? "private" : (
/* 111 */       AccessFlag.isProtected(accessFlags) ? "protected" : (
/* 112 */       isPublic(Integer.valueOf(accessFlags)) ? "public" : ""));
/*     */   }
/*     */   
/*     */   public String getMethodKey(ClassFile cls, MethodInfo method) {
/* 116 */     return getMethodName(method) + "(" + Joiner.on(", ").join(getParameterNames(method)) + ")";
/*     */   }
/*     */   
/*     */   public String getMethodFullKey(ClassFile cls, MethodInfo method) {
/* 120 */     return getClassName(cls) + "." + getMethodKey(cls, method);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPublic(Object o) {
/* 125 */     Integer accessFlags = Integer.valueOf((o instanceof ClassFile) ? ((ClassFile)o).getAccessFlags() : ((o instanceof FieldInfo) ? ((FieldInfo)o)
/* 126 */         .getAccessFlags() : ((o instanceof MethodInfo) ? 
/* 127 */         Integer.valueOf(((MethodInfo)o).getAccessFlags()) : null).intValue()));
/*     */     
/* 129 */     return (accessFlags != null && AccessFlag.isPublic(accessFlags.intValue()));
/*     */   }
/*     */ 
/*     */   
/*     */   public String getClassName(ClassFile cls) {
/* 134 */     return cls.getName();
/*     */   }
/*     */   
/*     */   public String getSuperclassName(ClassFile cls) {
/* 138 */     return cls.getSuperclass();
/*     */   }
/*     */   
/*     */   public List<String> getInterfacesNames(ClassFile cls) {
/* 142 */     return Arrays.asList(cls.getInterfaces());
/*     */   }
/*     */   
/*     */   public boolean acceptsInput(String file) {
/* 146 */     return file.endsWith(".class");
/*     */   }
/*     */ 
/*     */   
/*     */   private List<String> getAnnotationNames(AnnotationsAttribute... annotationsAttributes) {
/* 151 */     List<String> result = Lists.newArrayList();
/*     */     
/* 153 */     if (annotationsAttributes != null) {
/* 154 */       for (AnnotationsAttribute annotationsAttribute : annotationsAttributes) {
/* 155 */         if (annotationsAttribute != null) {
/* 156 */           for (Annotation annotation : annotationsAttribute.getAnnotations()) {
/* 157 */             result.add(annotation.getTypeName());
/*     */           }
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 163 */     return result;
/*     */   }
/*     */   
/*     */   private List<String> getAnnotationNames(Annotation[] annotations) {
/* 167 */     List<String> result = Lists.newArrayList();
/*     */     
/* 169 */     for (Annotation annotation : annotations) {
/* 170 */       result.add(annotation.getTypeName());
/*     */     }
/*     */     
/* 173 */     return result;
/*     */   }
/*     */   
/*     */   private List<String> splitDescriptorToTypeNames(String descriptors) {
/* 177 */     List<String> result = Lists.newArrayList();
/*     */     
/* 179 */     if (descriptors != null && descriptors.length() != 0) {
/*     */       
/* 181 */       List<Integer> indices = Lists.newArrayList();
/* 182 */       Descriptor.Iterator iterator = new Descriptor.Iterator(descriptors);
/* 183 */       while (iterator.hasNext()) {
/* 184 */         indices.add(Integer.valueOf(iterator.next()));
/*     */       }
/* 186 */       indices.add(Integer.valueOf(descriptors.length()));
/*     */       
/* 188 */       for (int i = 0; i < indices.size() - 1; i++) {
/* 189 */         String s1 = Descriptor.toString(descriptors.substring(((Integer)indices.get(i)).intValue(), ((Integer)indices.get(i + 1)).intValue()));
/* 190 */         result.add(s1);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 195 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\reflections\adapters\JavassistAdapter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */