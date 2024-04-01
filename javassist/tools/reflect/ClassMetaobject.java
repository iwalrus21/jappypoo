/*     */ package javassist.tools.reflect;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
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
/*     */ 
/*     */ public class ClassMetaobject
/*     */   implements Serializable
/*     */ {
/*     */   static final String methodPrefix = "_m_";
/*     */   static final int methodPrefixLen = 3;
/*     */   private Class javaClass;
/*     */   private Constructor[] constructors;
/*     */   private Method[] methods;
/*     */   public static boolean useContextClassLoader = false;
/*     */   
/*     */   public ClassMetaobject(String[] params) {
/*     */     try {
/*  74 */       this.javaClass = getClassObject(params[0]);
/*     */     }
/*  76 */     catch (ClassNotFoundException e) {
/*  77 */       throw new RuntimeException("not found: " + params[0] + ", useContextClassLoader: " + 
/*     */           
/*  79 */           Boolean.toString(useContextClassLoader), e);
/*     */     } 
/*     */     
/*  82 */     this.constructors = (Constructor[])this.javaClass.getConstructors();
/*  83 */     this.methods = null;
/*     */   }
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/*  87 */     out.writeUTF(this.javaClass.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/*  93 */     this.javaClass = getClassObject(in.readUTF());
/*  94 */     this.constructors = (Constructor[])this.javaClass.getConstructors();
/*  95 */     this.methods = null;
/*     */   }
/*     */   
/*     */   private Class getClassObject(String name) throws ClassNotFoundException {
/*  99 */     if (useContextClassLoader)
/*     */     {
/* 101 */       return Thread.currentThread().getContextClassLoader().loadClass(name);
/*     */     }
/* 103 */     return Class.forName(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Class getJavaClass() {
/* 110 */     return this.javaClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getName() {
/* 117 */     return this.javaClass.getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isInstance(Object obj) {
/* 124 */     return this.javaClass.isInstance(obj);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Object newInstance(Object[] args) throws CannotCreateException {
/* 135 */     int n = this.constructors.length;
/* 136 */     for (int i = 0; i < n; i++) {
/*     */       try {
/* 138 */         return this.constructors[i].newInstance(args);
/*     */       }
/* 140 */       catch (IllegalArgumentException illegalArgumentException) {
/*     */ 
/*     */       
/* 143 */       } catch (InstantiationException e) {
/* 144 */         throw new CannotCreateException(e);
/*     */       }
/* 146 */       catch (IllegalAccessException e) {
/* 147 */         throw new CannotCreateException(e);
/*     */       }
/* 149 */       catch (InvocationTargetException e) {
/* 150 */         throw new CannotCreateException(e);
/*     */       } 
/*     */     } 
/*     */     
/* 154 */     throw new CannotCreateException("no constructor matches");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object trapFieldRead(String name) {
/* 165 */     Class jc = getJavaClass();
/*     */     try {
/* 167 */       return jc.getField(name).get(null);
/*     */     }
/* 169 */     catch (NoSuchFieldException e) {
/* 170 */       throw new RuntimeException(e.toString());
/*     */     }
/* 172 */     catch (IllegalAccessException e) {
/* 173 */       throw new RuntimeException(e.toString());
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
/*     */   public void trapFieldWrite(String name, Object value) {
/* 185 */     Class jc = getJavaClass();
/*     */     try {
/* 187 */       jc.getField(name).set(null, value);
/*     */     }
/* 189 */     catch (NoSuchFieldException e) {
/* 190 */       throw new RuntimeException(e.toString());
/*     */     }
/* 192 */     catch (IllegalAccessException e) {
/* 193 */       throw new RuntimeException(e.toString());
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
/*     */   public static Object invoke(Object target, int identifier, Object[] args) throws Throwable {
/* 206 */     Method[] allmethods = target.getClass().getMethods();
/* 207 */     int n = allmethods.length;
/* 208 */     String head = "_m_" + identifier;
/* 209 */     for (int i = 0; i < n; i++) {
/* 210 */       if (allmethods[i].getName().startsWith(head)) {
/*     */         try {
/* 212 */           return allmethods[i].invoke(target, args);
/* 213 */         } catch (InvocationTargetException e) {
/* 214 */           throw e.getTargetException();
/* 215 */         } catch (IllegalAccessException e) {
/* 216 */           throw new CannotInvokeException(e);
/*     */         } 
/*     */       }
/*     */     } 
/* 220 */     throw new CannotInvokeException("cannot find a method");
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
/*     */   public Object trapMethodcall(int identifier, Object[] args) throws Throwable {
/*     */     try {
/* 235 */       Method[] m = getReflectiveMethods();
/* 236 */       return m[identifier].invoke(null, args);
/*     */     }
/* 238 */     catch (InvocationTargetException e) {
/* 239 */       throw e.getTargetException();
/*     */     }
/* 241 */     catch (IllegalAccessException e) {
/* 242 */       throw new CannotInvokeException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Method[] getReflectiveMethods() {
/* 251 */     if (this.methods != null) {
/* 252 */       return this.methods;
/*     */     }
/* 254 */     Class baseclass = getJavaClass();
/* 255 */     Method[] allmethods = baseclass.getDeclaredMethods();
/* 256 */     int n = allmethods.length;
/* 257 */     int[] index = new int[n];
/* 258 */     int max = 0; int i;
/* 259 */     for (i = 0; i < n; i++) {
/* 260 */       Method m = allmethods[i];
/* 261 */       String mname = m.getName();
/* 262 */       if (mname.startsWith("_m_")) {
/* 263 */         int k = 0;
/* 264 */         int j = 3; while (true) {
/* 265 */           char c = mname.charAt(j);
/* 266 */           if ('0' <= c && c <= '9') {
/* 267 */             k = k * 10 + c - 48;
/*     */             j++;
/*     */           } 
/*     */           break;
/*     */         } 
/* 272 */         index[i] = ++k;
/* 273 */         if (k > max) {
/* 274 */           max = k;
/*     */         }
/*     */       } 
/*     */     } 
/* 278 */     this.methods = new Method[max];
/* 279 */     for (i = 0; i < n; i++) {
/* 280 */       if (index[i] > 0)
/* 281 */         this.methods[index[i] - 1] = allmethods[i]; 
/*     */     } 
/* 283 */     return this.methods;
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
/*     */   public final Method getMethod(int identifier) {
/* 299 */     return getReflectiveMethods()[identifier];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getMethodName(int identifier) {
/*     */     char c;
/* 307 */     String mname = getReflectiveMethods()[identifier].getName();
/* 308 */     int j = 3;
/*     */     do {
/* 310 */       c = mname.charAt(j++);
/* 311 */     } while (c >= '0' && '9' >= c);
/*     */ 
/*     */ 
/*     */     
/* 315 */     return mname.substring(j);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Class[] getParameterTypes(int identifier) {
/* 324 */     return getReflectiveMethods()[identifier].getParameterTypes();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Class getReturnType(int identifier) {
/* 332 */     return getReflectiveMethods()[identifier].getReturnType();
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
/*     */   public final int getMethodIndex(String originalName, Class[] argTypes) throws NoSuchMethodException {
/* 356 */     Method[] mthds = getReflectiveMethods();
/* 357 */     for (int i = 0; i < mthds.length; i++) {
/* 358 */       if (mthds[i] != null)
/*     */       {
/*     */ 
/*     */         
/* 362 */         if (getMethodName(i).equals(originalName) && 
/* 363 */           Arrays.equals((Object[])argTypes, (Object[])mthds[i].getParameterTypes()))
/* 364 */           return i; 
/*     */       }
/*     */     } 
/* 367 */     throw new NoSuchMethodException("Method " + originalName + " not found");
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\tools\reflect\ClassMetaobject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */