/*     */ package javassist.bytecode.annotation;
/*     */ 
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtClass;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.bytecode.AnnotationDefaultAttribute;
/*     */ import javassist.bytecode.ClassFile;
/*     */ import javassist.bytecode.MethodInfo;
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
/*     */ public class AnnotationImpl
/*     */   implements InvocationHandler
/*     */ {
/*     */   private static final String JDK_ANNOTATION_CLASS_NAME = "java.lang.annotation.Annotation";
/*  40 */   private static Method JDK_ANNOTATION_TYPE_METHOD = null;
/*     */   
/*     */   private Annotation annotation;
/*     */   private ClassPool pool;
/*     */   private ClassLoader classLoader;
/*     */   private transient Class annotationType;
/*  46 */   private transient int cachedHashCode = Integer.MIN_VALUE;
/*     */ 
/*     */   
/*     */   static {
/*     */     try {
/*  51 */       Class<?> clazz = Class.forName("java.lang.annotation.Annotation");
/*  52 */       JDK_ANNOTATION_TYPE_METHOD = clazz.getMethod("annotationType", (Class[])null);
/*     */     }
/*  54 */     catch (Exception exception) {}
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
/*     */   public static Object make(ClassLoader cl, Class clazz, ClassPool cp, Annotation anon) {
/*  71 */     AnnotationImpl handler = new AnnotationImpl(anon, cp, cl);
/*  72 */     return Proxy.newProxyInstance(cl, new Class[] { clazz }, handler);
/*     */   }
/*     */   
/*     */   private AnnotationImpl(Annotation a, ClassPool cp, ClassLoader loader) {
/*  76 */     this.annotation = a;
/*  77 */     this.pool = cp;
/*  78 */     this.classLoader = loader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTypeName() {
/*  87 */     return this.annotation.getTypeName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Class getAnnotationType() {
/*  97 */     if (this.annotationType == null) {
/*  98 */       String typeName = this.annotation.getTypeName();
/*     */       try {
/* 100 */         this.annotationType = this.classLoader.loadClass(typeName);
/*     */       }
/* 102 */       catch (ClassNotFoundException e) {
/* 103 */         NoClassDefFoundError error = new NoClassDefFoundError("Error loading annotation class: " + typeName);
/* 104 */         error.setStackTrace(e.getStackTrace());
/* 105 */         throw error;
/*     */       } 
/*     */     } 
/* 108 */     return this.annotationType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Annotation getAnnotation() {
/* 117 */     return this.annotation;
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
/*     */   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/* 130 */     String name = method.getName();
/* 131 */     if (Object.class == method.getDeclaringClass()) {
/* 132 */       if ("equals".equals(name)) {
/* 133 */         Object obj = args[0];
/* 134 */         return new Boolean(checkEquals(obj));
/*     */       } 
/* 136 */       if ("toString".equals(name))
/* 137 */         return this.annotation.toString(); 
/* 138 */       if ("hashCode".equals(name)) {
/* 139 */         return new Integer(hashCode());
/*     */       }
/* 141 */     } else if ("annotationType".equals(name) && (method
/* 142 */       .getParameterTypes()).length == 0) {
/* 143 */       return getAnnotationType();
/*     */     } 
/* 145 */     MemberValue mv = this.annotation.getMemberValue(name);
/* 146 */     if (mv == null) {
/* 147 */       return getDefault(name, method);
/*     */     }
/* 149 */     return mv.getValue(this.classLoader, this.pool, method);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Object getDefault(String name, Method method) throws ClassNotFoundException, RuntimeException {
/* 155 */     String classname = this.annotation.getTypeName();
/* 156 */     if (this.pool != null) {
/*     */       try {
/* 158 */         CtClass cc = this.pool.get(classname);
/* 159 */         ClassFile cf = cc.getClassFile2();
/* 160 */         MethodInfo minfo = cf.getMethod(name);
/* 161 */         if (minfo != null) {
/*     */ 
/*     */           
/* 164 */           AnnotationDefaultAttribute ainfo = (AnnotationDefaultAttribute)minfo.getAttribute("AnnotationDefault");
/* 165 */           if (ainfo != null) {
/* 166 */             MemberValue mv = ainfo.getDefaultValue();
/* 167 */             return mv.getValue(this.classLoader, this.pool, method);
/*     */           }
/*     */         
/*     */         } 
/* 171 */       } catch (NotFoundException e) {
/* 172 */         throw new RuntimeException("cannot find a class file: " + classname);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 177 */     throw new RuntimeException("no default value: " + classname + "." + name + "()");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 185 */     if (this.cachedHashCode == Integer.MIN_VALUE) {
/* 186 */       int hashCode = 0;
/*     */ 
/*     */       
/* 189 */       getAnnotationType();
/*     */       
/* 191 */       Method[] methods = this.annotationType.getDeclaredMethods();
/* 192 */       for (int i = 0; i < methods.length; i++) {
/* 193 */         String name = methods[i].getName();
/* 194 */         int valueHashCode = 0;
/*     */ 
/*     */         
/* 197 */         MemberValue mv = this.annotation.getMemberValue(name);
/* 198 */         Object value = null;
/*     */         try {
/* 200 */           if (mv != null)
/* 201 */             value = mv.getValue(this.classLoader, this.pool, methods[i]); 
/* 202 */           if (value == null) {
/* 203 */             value = getDefault(name, methods[i]);
/*     */           }
/* 205 */         } catch (RuntimeException e) {
/* 206 */           throw e;
/*     */         }
/* 208 */         catch (Exception e) {
/* 209 */           throw new RuntimeException("Error retrieving value " + name + " for annotation " + this.annotation.getTypeName(), e);
/*     */         } 
/*     */ 
/*     */         
/* 213 */         if (value != null)
/* 214 */           if (value.getClass().isArray()) {
/* 215 */             valueHashCode = arrayHashCode(value);
/*     */           } else {
/* 217 */             valueHashCode = value.hashCode();
/*     */           }  
/* 219 */         hashCode += 127 * name.hashCode() ^ valueHashCode;
/*     */       } 
/*     */       
/* 222 */       this.cachedHashCode = hashCode;
/*     */     } 
/* 224 */     return this.cachedHashCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean checkEquals(Object obj) throws Exception {
/* 235 */     if (obj == null) {
/* 236 */       return false;
/*     */     }
/*     */     
/* 239 */     if (obj instanceof Proxy) {
/* 240 */       InvocationHandler ih = Proxy.getInvocationHandler(obj);
/* 241 */       if (ih instanceof AnnotationImpl) {
/* 242 */         AnnotationImpl other = (AnnotationImpl)ih;
/* 243 */         return this.annotation.equals(other.annotation);
/*     */       } 
/*     */     } 
/*     */     
/* 247 */     Class otherAnnotationType = (Class)JDK_ANNOTATION_TYPE_METHOD.invoke(obj, (Object[])null);
/* 248 */     if (!getAnnotationType().equals(otherAnnotationType)) {
/* 249 */       return false;
/*     */     }
/* 251 */     Method[] methods = this.annotationType.getDeclaredMethods();
/* 252 */     for (int i = 0; i < methods.length; i++) {
/* 253 */       String name = methods[i].getName();
/*     */ 
/*     */       
/* 256 */       MemberValue mv = this.annotation.getMemberValue(name);
/* 257 */       Object value = null;
/* 258 */       Object otherValue = null;
/*     */       try {
/* 260 */         if (mv != null)
/* 261 */           value = mv.getValue(this.classLoader, this.pool, methods[i]); 
/* 262 */         if (value == null)
/* 263 */           value = getDefault(name, methods[i]); 
/* 264 */         otherValue = methods[i].invoke(obj, (Object[])null);
/*     */       }
/* 266 */       catch (RuntimeException e) {
/* 267 */         throw e;
/*     */       }
/* 269 */       catch (Exception e) {
/* 270 */         throw new RuntimeException("Error retrieving value " + name + " for annotation " + this.annotation.getTypeName(), e);
/*     */       } 
/*     */       
/* 273 */       if (value == null && otherValue != null)
/* 274 */         return false; 
/* 275 */       if (value != null && !value.equals(otherValue)) {
/* 276 */         return false;
/*     */       }
/*     */     } 
/* 279 */     return true;
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
/*     */   private static int arrayHashCode(Object object) {
/* 291 */     if (object == null) {
/* 292 */       return 0;
/*     */     }
/* 294 */     int result = 1;
/*     */     
/* 296 */     Object[] array = (Object[])object;
/* 297 */     for (int i = 0; i < array.length; i++) {
/* 298 */       int elementHashCode = 0;
/* 299 */       if (array[i] != null)
/* 300 */         elementHashCode = array[i].hashCode(); 
/* 301 */       result = 31 * result + elementHashCode;
/*     */     } 
/* 303 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\annotation\AnnotationImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */