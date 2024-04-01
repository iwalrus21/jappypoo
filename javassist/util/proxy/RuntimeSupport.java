/*     */ package javassist.util.proxy;
/*     */ 
/*     */ import java.io.InvalidClassException;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Method;
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
/*     */ public class RuntimeSupport
/*     */ {
/*  31 */   public static MethodHandler default_interceptor = new DefaultMethodHandler();
/*     */ 
/*     */   
/*     */   static class DefaultMethodHandler
/*     */     implements MethodHandler, Serializable
/*     */   {
/*     */     public Object invoke(Object self, Method m, Method proceed, Object[] args) throws Exception {
/*  38 */       return proceed.invoke(self, args);
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
/*     */   
/*     */   public static void find2Methods(Class clazz, String superMethod, String thisMethod, int index, String desc, Method[] methods) {
/*  53 */     methods[index + 1] = (thisMethod == null) ? null : 
/*  54 */       findMethod(clazz, thisMethod, desc);
/*  55 */     methods[index] = findSuperClassMethod(clazz, superMethod, desc);
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
/*     */   public static void find2Methods(Object self, String superMethod, String thisMethod, int index, String desc, Method[] methods) {
/*  72 */     methods[index + 1] = (thisMethod == null) ? null : 
/*  73 */       findMethod(self, thisMethod, desc);
/*  74 */     methods[index] = findSuperMethod(self, superMethod, desc);
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
/*     */   public static Method findMethod(Object self, String name, String desc) {
/*  87 */     Method m = findMethod2(self.getClass(), name, desc);
/*  88 */     if (m == null) {
/*  89 */       error(self.getClass(), name, desc);
/*     */     }
/*  91 */     return m;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Method findMethod(Class clazz, String name, String desc) {
/* 101 */     Method m = findMethod2(clazz, name, desc);
/* 102 */     if (m == null) {
/* 103 */       error(clazz, name, desc);
/*     */     }
/* 105 */     return m;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Method findSuperMethod(Object self, String name, String desc) {
/* 116 */     Class<?> clazz = self.getClass();
/* 117 */     return findSuperClassMethod(clazz, name, desc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Method findSuperClassMethod(Class clazz, String name, String desc) {
/* 127 */     Method m = findSuperMethod2(clazz.getSuperclass(), name, desc);
/* 128 */     if (m == null) {
/* 129 */       m = searchInterfaces(clazz, name, desc);
/*     */     }
/* 131 */     if (m == null) {
/* 132 */       error(clazz, name, desc);
/*     */     }
/* 134 */     return m;
/*     */   }
/*     */   
/*     */   private static void error(Class clazz, String name, String desc) {
/* 138 */     throw new RuntimeException("not found " + name + ":" + desc + " in " + clazz
/* 139 */         .getName());
/*     */   }
/*     */   
/*     */   private static Method findSuperMethod2(Class clazz, String name, String desc) {
/* 143 */     Method m = findMethod2(clazz, name, desc);
/* 144 */     if (m != null) {
/* 145 */       return m;
/*     */     }
/* 147 */     Class superClass = clazz.getSuperclass();
/* 148 */     if (superClass != null) {
/* 149 */       m = findSuperMethod2(superClass, name, desc);
/* 150 */       if (m != null) {
/* 151 */         return m;
/*     */       }
/*     */     } 
/* 154 */     return searchInterfaces(clazz, name, desc);
/*     */   }
/*     */   
/*     */   private static Method searchInterfaces(Class clazz, String name, String desc) {
/* 158 */     Method m = null;
/* 159 */     Class[] interfaces = clazz.getInterfaces();
/* 160 */     for (int i = 0; i < interfaces.length; i++) {
/* 161 */       m = findSuperMethod2(interfaces[i], name, desc);
/* 162 */       if (m != null) {
/* 163 */         return m;
/*     */       }
/*     */     } 
/* 166 */     return m;
/*     */   }
/*     */   
/*     */   private static Method findMethod2(Class clazz, String name, String desc) {
/* 170 */     Method[] methods = SecurityActions.getDeclaredMethods(clazz);
/* 171 */     int n = methods.length;
/* 172 */     for (int i = 0; i < n; i++) {
/* 173 */       if (methods[i].getName().equals(name) && 
/* 174 */         makeDescriptor(methods[i]).equals(desc))
/* 175 */         return methods[i]; 
/*     */     } 
/* 177 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String makeDescriptor(Method m) {
/* 184 */     Class[] params = m.getParameterTypes();
/* 185 */     return makeDescriptor(params, m.getReturnType());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String makeDescriptor(Class[] params, Class retType) {
/* 195 */     StringBuffer sbuf = new StringBuffer();
/* 196 */     sbuf.append('(');
/* 197 */     for (int i = 0; i < params.length; i++) {
/* 198 */       makeDesc(sbuf, params[i]);
/*     */     }
/* 200 */     sbuf.append(')');
/* 201 */     if (retType != null) {
/* 202 */       makeDesc(sbuf, retType);
/*     */     }
/* 204 */     return sbuf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String makeDescriptor(String params, Class retType) {
/* 214 */     StringBuffer sbuf = new StringBuffer(params);
/* 215 */     makeDesc(sbuf, retType);
/* 216 */     return sbuf.toString();
/*     */   }
/*     */   
/*     */   private static void makeDesc(StringBuffer sbuf, Class<void> type) {
/* 220 */     if (type.isArray()) {
/* 221 */       sbuf.append('[');
/* 222 */       makeDesc(sbuf, type.getComponentType());
/*     */     }
/* 224 */     else if (type.isPrimitive()) {
/* 225 */       if (type == void.class) {
/* 226 */         sbuf.append('V');
/* 227 */       } else if (type == int.class) {
/* 228 */         sbuf.append('I');
/* 229 */       } else if (type == byte.class) {
/* 230 */         sbuf.append('B');
/* 231 */       } else if (type == long.class) {
/* 232 */         sbuf.append('J');
/* 233 */       } else if (type == double.class) {
/* 234 */         sbuf.append('D');
/* 235 */       } else if (type == float.class) {
/* 236 */         sbuf.append('F');
/* 237 */       } else if (type == char.class) {
/* 238 */         sbuf.append('C');
/* 239 */       } else if (type == short.class) {
/* 240 */         sbuf.append('S');
/* 241 */       } else if (type == boolean.class) {
/* 242 */         sbuf.append('Z');
/*     */       } else {
/* 244 */         throw new RuntimeException("bad type: " + type.getName());
/*     */       } 
/*     */     } else {
/* 247 */       sbuf.append('L').append(type.getName().replace('.', '/'))
/* 248 */         .append(';');
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
/*     */   public static SerializedProxy makeSerializedProxy(Object proxy) throws InvalidClassException {
/* 261 */     Class<?> clazz = proxy.getClass();
/*     */     
/* 263 */     MethodHandler methodHandler = null;
/* 264 */     if (proxy instanceof ProxyObject) {
/* 265 */       methodHandler = ((ProxyObject)proxy).getHandler();
/* 266 */     } else if (proxy instanceof Proxy) {
/* 267 */       methodHandler = ProxyFactory.getHandler((Proxy)proxy);
/*     */     } 
/* 269 */     return new SerializedProxy(clazz, ProxyFactory.getFilterSignature(clazz), methodHandler);
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassis\\util\proxy\RuntimeSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */