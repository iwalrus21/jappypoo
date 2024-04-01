/*     */ package net.jodah.typetools;
/*     */ 
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ import sun.misc.Unsafe;
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
/*     */ public final class TypeResolver
/*     */ {
/*  50 */   private static final Map<Class<?>, Reference<Map<TypeVariable<?>, Type>>> TYPE_VARIABLE_CACHE = Collections.synchronizedMap(new WeakHashMap<Class<?>, Reference<Map<TypeVariable<?>, Type>>>());
/*     */   private static volatile boolean CACHE_ENABLED = true;
/*     */   private static boolean RESOLVES_LAMBDAS;
/*     */   private static Method GET_CONSTANT_POOL;
/*     */   private static Method GET_CONSTANT_POOL_SIZE;
/*     */   private static Method GET_CONSTANT_POOL_METHOD_AT;
/*  56 */   private static final Map<String, Method> OBJECT_METHODS = new HashMap<String, Method>();
/*     */ 
/*     */   
/*     */   private static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPERS;
/*     */   
/*  61 */   private static final Double JAVA_VERSION = Double.valueOf(Double.parseDouble(System.getProperty("java.specification.version", "0")));
/*     */   static {
/*     */     try {
/*  64 */       Unsafe unsafe = AccessController.<Unsafe>doPrivileged(new PrivilegedExceptionAction<Unsafe>()
/*     */           {
/*     */             public Unsafe run() throws Exception {
/*  67 */               Field f = Unsafe.class.getDeclaredField("theUnsafe");
/*  68 */               f.setAccessible(true);
/*     */               
/*  70 */               return (Unsafe)f.get((Object)null);
/*     */             }
/*     */           });
/*     */       
/*  74 */       GET_CONSTANT_POOL = Class.class.getDeclaredMethod("getConstantPool", new Class[0]);
/*  75 */       String constantPoolName = (JAVA_VERSION.doubleValue() < 9.0D) ? "sun.reflect.ConstantPool" : "jdk.internal.reflect.ConstantPool";
/*  76 */       Class<?> constantPoolClass = Class.forName(constantPoolName);
/*  77 */       GET_CONSTANT_POOL_SIZE = constantPoolClass.getDeclaredMethod("getSize", new Class[0]);
/*  78 */       GET_CONSTANT_POOL_METHOD_AT = constantPoolClass.getDeclaredMethod("getMethodAt", new Class[] { int.class });
/*     */ 
/*     */       
/*  81 */       Field overrideField = AccessibleObject.class.getDeclaredField("override");
/*  82 */       long overrideFieldOffset = unsafe.objectFieldOffset(overrideField);
/*  83 */       unsafe.putBoolean(GET_CONSTANT_POOL, overrideFieldOffset, true);
/*  84 */       unsafe.putBoolean(GET_CONSTANT_POOL_SIZE, overrideFieldOffset, true);
/*  85 */       unsafe.putBoolean(GET_CONSTANT_POOL_METHOD_AT, overrideFieldOffset, true);
/*     */ 
/*     */ 
/*     */       
/*  89 */       Object constantPool = GET_CONSTANT_POOL.invoke(Object.class, new Object[0]);
/*  90 */       GET_CONSTANT_POOL_SIZE.invoke(constantPool, new Object[0]);
/*     */       
/*  92 */       for (Method method : Object.class.getDeclaredMethods()) {
/*  93 */         OBJECT_METHODS.put(method.getName(), method);
/*     */       }
/*  95 */       RESOLVES_LAMBDAS = true;
/*  96 */     } catch (Exception exception) {}
/*     */ 
/*     */     
/*  99 */     Map<Class<?>, Class<?>> types = new HashMap<Class<?>, Class<?>>();
/* 100 */     types.put(boolean.class, Boolean.class);
/* 101 */     types.put(byte.class, Byte.class);
/* 102 */     types.put(char.class, Character.class);
/* 103 */     types.put(double.class, Double.class);
/* 104 */     types.put(float.class, Float.class);
/* 105 */     types.put(int.class, Integer.class);
/* 106 */     types.put(long.class, Long.class);
/* 107 */     types.put(short.class, Short.class);
/* 108 */     types.put(void.class, Void.class);
/* 109 */     PRIMITIVE_WRAPPERS = Collections.unmodifiableMap(types);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Unknown {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void enableCache() {
/* 125 */     CACHE_ENABLED = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void disableCache() {
/* 132 */     TYPE_VARIABLE_CACHE.clear();
/* 133 */     CACHE_ENABLED = false;
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
/*     */   public static <T, S extends T> Class<?> resolveRawArgument(Class<T> type, Class<S> subType) {
/* 146 */     return resolveRawArgument(resolveGenericType(type, subType), subType);
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
/*     */   public static Class<?> resolveRawArgument(Type genericType, Class<?> subType) {
/* 160 */     Class<?>[] arguments = resolveRawArguments(genericType, subType);
/* 161 */     if (arguments == null) {
/* 162 */       return Unknown.class;
/*     */     }
/* 164 */     if (arguments.length != 1) {
/* 165 */       throw new IllegalArgumentException("Expected 1 argument for generic type " + genericType + " but found " + arguments.length);
/*     */     }
/*     */     
/* 168 */     return arguments[0];
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
/*     */   public static <T, S extends T> Class<?>[] resolveRawArguments(Class<T> type, Class<S> subType) {
/* 182 */     return resolveRawArguments(resolveGenericType(type, subType), subType);
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
/*     */   public static Class<?>[] resolveRawArguments(Type genericType, Class<?> subType) {
/* 196 */     Class<?>[] result = null;
/* 197 */     Class<?> functionalInterface = null;
/*     */ 
/*     */     
/* 200 */     if (RESOLVES_LAMBDAS && subType.isSynthetic()) {
/*     */ 
/*     */       
/* 203 */       Class<?> fi = (genericType instanceof ParameterizedType && ((ParameterizedType)genericType).getRawType() instanceof Class) ? (Class)((ParameterizedType)genericType).getRawType() : ((genericType instanceof Class) ? (Class)genericType : null);
/*     */       
/* 205 */       if (fi != null && fi.isInterface()) {
/* 206 */         functionalInterface = fi;
/*     */       }
/*     */     } 
/* 209 */     if (genericType instanceof ParameterizedType) {
/* 210 */       ParameterizedType paramType = (ParameterizedType)genericType;
/* 211 */       Type[] arguments = paramType.getActualTypeArguments();
/* 212 */       result = new Class[arguments.length];
/* 213 */       for (int i = 0; i < arguments.length; i++)
/* 214 */         result[i] = resolveRawClass(arguments[i], subType, functionalInterface); 
/* 215 */     } else if (genericType instanceof TypeVariable) {
/* 216 */       result = new Class[1];
/* 217 */       result[0] = resolveRawClass(genericType, subType, functionalInterface);
/* 218 */     } else if (genericType instanceof Class) {
/* 219 */       TypeVariable[] arrayOfTypeVariable = ((Class)genericType).getTypeParameters();
/* 220 */       result = new Class[arrayOfTypeVariable.length];
/* 221 */       for (int i = 0; i < arrayOfTypeVariable.length; i++) {
/* 222 */         result[i] = resolveRawClass(arrayOfTypeVariable[i], subType, functionalInterface);
/*     */       }
/*     */     } 
/* 225 */     return result;
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
/*     */   public static Type resolveGenericType(Class<?> type, Type subType) {
/*     */     Class<?> rawType;
/* 238 */     if (subType instanceof ParameterizedType) {
/* 239 */       rawType = (Class)((ParameterizedType)subType).getRawType();
/*     */     } else {
/* 241 */       rawType = (Class)subType;
/*     */     } 
/* 243 */     if (type.equals(rawType)) {
/* 244 */       return subType;
/*     */     }
/*     */     
/* 247 */     if (type.isInterface())
/* 248 */       for (Type superInterface : rawType.getGenericInterfaces()) {
/* 249 */         Type type1; if (superInterface != null && !superInterface.equals(Object.class) && (
/* 250 */           type1 = resolveGenericType(type, superInterface)) != null) {
/* 251 */           return type1;
/*     */         }
/*     */       }  
/* 254 */     Type superClass = rawType.getGenericSuperclass(); Type result;
/* 255 */     if (superClass != null && !superClass.equals(Object.class) && (
/* 256 */       result = resolveGenericType(type, superClass)) != null) {
/* 257 */       return result;
/*     */     }
/* 259 */     return null;
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
/*     */   public static Class<?> resolveRawClass(Type genericType, Class<?> subType) {
/* 271 */     return resolveRawClass(genericType, subType, null);
/*     */   }
/*     */   
/*     */   private static Class<?> resolveRawClass(Type genericType, Class<?> subType, Class<?> functionalInterface) {
/* 275 */     if (genericType instanceof Class)
/* 276 */       return (Class)genericType; 
/* 277 */     if (genericType instanceof ParameterizedType)
/* 278 */       return resolveRawClass(((ParameterizedType)genericType).getRawType(), subType, functionalInterface); 
/* 279 */     if (genericType instanceof GenericArrayType) {
/* 280 */       GenericArrayType arrayType = (GenericArrayType)genericType;
/* 281 */       Class<?> component = resolveRawClass(arrayType.getGenericComponentType(), subType, functionalInterface);
/* 282 */       return Array.newInstance(component, 0).getClass();
/* 283 */     }  if (genericType instanceof TypeVariable) {
/* 284 */       TypeVariable<?> variable = (TypeVariable)genericType;
/* 285 */       genericType = getTypeVariableMap(subType, functionalInterface).get(variable);
/*     */       
/* 287 */       genericType = (genericType == null) ? resolveBound(variable) : resolveRawClass(genericType, subType, functionalInterface);
/*     */     } 
/*     */     
/* 290 */     return (genericType instanceof Class) ? (Class)genericType : Unknown.class;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Map<TypeVariable<?>, Type> getTypeVariableMap(Class<?> targetType, Class<?> functionalInterface) {
/* 295 */     Reference<Map<TypeVariable<?>, Type>> ref = TYPE_VARIABLE_CACHE.get(targetType);
/* 296 */     Map<TypeVariable<?>, Type> map = (ref != null) ? ref.get() : null;
/*     */     
/* 298 */     if (map == null) {
/* 299 */       map = new HashMap<TypeVariable<?>, Type>();
/*     */ 
/*     */       
/* 302 */       if (functionalInterface != null) {
/* 303 */         populateLambdaArgs(functionalInterface, targetType, map);
/*     */       }
/*     */       
/* 306 */       populateSuperTypeArgs(targetType.getGenericInterfaces(), map, (functionalInterface != null));
/*     */ 
/*     */       
/* 309 */       Type genericType = targetType.getGenericSuperclass();
/* 310 */       Class<?> type = targetType.getSuperclass();
/* 311 */       while (type != null && !Object.class.equals(type)) {
/* 312 */         if (genericType instanceof ParameterizedType)
/* 313 */           populateTypeArgs((ParameterizedType)genericType, map, false); 
/* 314 */         populateSuperTypeArgs(type.getGenericInterfaces(), map, false);
/*     */         
/* 316 */         genericType = type.getGenericSuperclass();
/* 317 */         type = type.getSuperclass();
/*     */       } 
/*     */ 
/*     */       
/* 321 */       type = targetType;
/* 322 */       while (type.isMemberClass()) {
/* 323 */         genericType = type.getGenericSuperclass();
/* 324 */         if (genericType instanceof ParameterizedType) {
/* 325 */           populateTypeArgs((ParameterizedType)genericType, map, (functionalInterface != null));
/*     */         }
/* 327 */         type = type.getEnclosingClass();
/*     */       } 
/*     */       
/* 330 */       if (CACHE_ENABLED) {
/* 331 */         TYPE_VARIABLE_CACHE.put(targetType, new WeakReference<Map<TypeVariable<?>, Type>>(map));
/*     */       }
/*     */     } 
/* 334 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void populateSuperTypeArgs(Type[] types, Map<TypeVariable<?>, Type> map, boolean depthFirst) {
/* 342 */     for (Type type : types) {
/* 343 */       if (type instanceof ParameterizedType) {
/* 344 */         ParameterizedType parameterizedType = (ParameterizedType)type;
/* 345 */         if (!depthFirst)
/* 346 */           populateTypeArgs(parameterizedType, map, depthFirst); 
/* 347 */         Type rawType = parameterizedType.getRawType();
/* 348 */         if (rawType instanceof Class)
/* 349 */           populateSuperTypeArgs(((Class)rawType).getGenericInterfaces(), map, depthFirst); 
/* 350 */         if (depthFirst)
/* 351 */           populateTypeArgs(parameterizedType, map, depthFirst); 
/* 352 */       } else if (type instanceof Class) {
/* 353 */         populateSuperTypeArgs(((Class)type).getGenericInterfaces(), map, depthFirst);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void populateTypeArgs(ParameterizedType type, Map<TypeVariable<?>, Type> map, boolean depthFirst) {
/* 362 */     if (type.getRawType() instanceof Class) {
/* 363 */       TypeVariable[] arrayOfTypeVariable = ((Class)type.getRawType()).getTypeParameters();
/* 364 */       Type[] typeArguments = type.getActualTypeArguments();
/*     */       
/* 366 */       if (type.getOwnerType() != null) {
/* 367 */         Type owner = type.getOwnerType();
/* 368 */         if (owner instanceof ParameterizedType) {
/* 369 */           populateTypeArgs((ParameterizedType)owner, map, depthFirst);
/*     */         }
/*     */       } 
/* 372 */       for (int i = 0; i < typeArguments.length; i++) {
/* 373 */         TypeVariable<?> variable = arrayOfTypeVariable[i];
/* 374 */         Type typeArgument = typeArguments[i];
/*     */         
/* 376 */         if (typeArgument instanceof Class) {
/* 377 */           map.put(variable, typeArgument); continue;
/* 378 */         }  if (typeArgument instanceof GenericArrayType) {
/* 379 */           map.put(variable, typeArgument); continue;
/* 380 */         }  if (typeArgument instanceof ParameterizedType) {
/* 381 */           map.put(variable, typeArgument); continue;
/* 382 */         }  if (typeArgument instanceof TypeVariable) {
/* 383 */           TypeVariable<?> typeVariableArgument = (TypeVariable)typeArgument;
/* 384 */           if (depthFirst) {
/* 385 */             Type existingType = map.get(variable);
/* 386 */             if (existingType != null) {
/* 387 */               map.put(typeVariableArgument, existingType);
/*     */               
/*     */               continue;
/*     */             } 
/*     */           } 
/* 392 */           Type resolvedType = map.get(typeVariableArgument);
/* 393 */           if (resolvedType == null)
/* 394 */             resolvedType = resolveBound(typeVariableArgument); 
/* 395 */           map.put(variable, resolvedType);
/*     */         } 
/*     */         continue;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Type resolveBound(TypeVariable<?> typeVariable) {
/* 405 */     Type[] bounds = typeVariable.getBounds();
/* 406 */     if (bounds.length == 0) {
/* 407 */       return Unknown.class;
/*     */     }
/* 409 */     Type bound = bounds[0];
/* 410 */     if (bound instanceof TypeVariable) {
/* 411 */       bound = resolveBound((TypeVariable)bound);
/*     */     }
/* 413 */     return (bound == Object.class) ? Unknown.class : bound;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void populateLambdaArgs(Class<?> functionalInterface, Class<?> lambdaType, Map<TypeVariable<?>, Type> map) {
/* 421 */     if (RESOLVES_LAMBDAS)
/*     */     {
/* 423 */       for (Method m : functionalInterface.getMethods()) {
/* 424 */         if (!isDefaultMethod(m) && !Modifier.isStatic(m.getModifiers()) && !m.isBridge()) {
/*     */           
/* 426 */           Method objectMethod = OBJECT_METHODS.get(m.getName());
/* 427 */           if (objectMethod == null || !Arrays.equals((Object[])m.getTypeParameters(), (Object[])objectMethod.getTypeParameters())) {
/*     */ 
/*     */ 
/*     */             
/* 431 */             Type returnTypeVar = m.getGenericReturnType();
/* 432 */             Type[] paramTypeVars = m.getGenericParameterTypes();
/*     */             
/* 434 */             Member member = getMemberRef(lambdaType);
/* 435 */             if (member == null) {
/*     */               return;
/*     */             }
/*     */             
/* 439 */             if (returnTypeVar instanceof TypeVariable) {
/*     */               
/* 441 */               Class<?> returnType = (member instanceof Method) ? ((Method)member).getReturnType() : ((Constructor)member).getDeclaringClass();
/* 442 */               returnType = wrapPrimitives(returnType);
/* 443 */               if (!returnType.equals(Void.class)) {
/* 444 */                 map.put((TypeVariable)returnTypeVar, returnType);
/*     */               }
/*     */             } 
/*     */             
/* 448 */             Class<?>[] arguments = (member instanceof Method) ? ((Method)member).getParameterTypes() : ((Constructor)member).getParameterTypes();
/*     */ 
/*     */             
/* 451 */             int paramOffset = 0;
/* 452 */             if (paramTypeVars.length > 0 && paramTypeVars[0] instanceof TypeVariable && paramTypeVars.length == arguments.length + 1) {
/*     */               
/* 454 */               Class<?> instanceType = member.getDeclaringClass();
/* 455 */               map.put((TypeVariable)paramTypeVars[0], instanceType);
/* 456 */               paramOffset = 1;
/*     */             } 
/*     */ 
/*     */             
/* 460 */             int argOffset = 0;
/* 461 */             if (paramTypeVars.length < arguments.length) {
/* 462 */               argOffset = arguments.length - paramTypeVars.length;
/*     */             }
/*     */ 
/*     */             
/* 466 */             for (int i = 0; i + argOffset < arguments.length; i++) {
/* 467 */               if (paramTypeVars[i] instanceof TypeVariable)
/* 468 */                 map.put((TypeVariable)paramTypeVars[i + paramOffset], wrapPrimitives(arguments[i + argOffset])); 
/*     */             } 
/*     */             return;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean isDefaultMethod(Method m) {
/* 478 */     return (JAVA_VERSION.doubleValue() >= 1.8D && m.isDefault());
/*     */   }
/*     */   
/*     */   private static Member getMemberRef(Class<?> type) {
/*     */     Object constantPool;
/*     */     try {
/* 484 */       constantPool = GET_CONSTANT_POOL.invoke(type, new Object[0]);
/* 485 */     } catch (Exception ignore) {
/* 486 */       return null;
/*     */     } 
/*     */     
/* 489 */     Member result = null;
/* 490 */     for (int i = getConstantPoolSize(constantPool) - 1; i >= 0; i--) {
/* 491 */       Member member = getConstantPoolMethodAt(constantPool, i);
/*     */       
/* 493 */       if (member != null && (!(member instanceof Constructor) || 
/*     */         
/* 495 */         !member.getDeclaringClass().getName().equals("java.lang.invoke.SerializedLambda")) && 
/* 496 */         !member.getDeclaringClass().isAssignableFrom(type)) {
/*     */ 
/*     */         
/* 499 */         result = member;
/*     */ 
/*     */         
/* 502 */         if (!(member instanceof Method) || !isAutoBoxingMethod((Method)member))
/*     */           break; 
/*     */       } 
/*     */     } 
/* 506 */     return result;
/*     */   }
/*     */   
/*     */   private static boolean isAutoBoxingMethod(Method method) {
/* 510 */     Class<?>[] parameters = method.getParameterTypes();
/* 511 */     return (method.getName().equals("valueOf") && parameters.length == 1 && parameters[0].isPrimitive() && 
/* 512 */       wrapPrimitives(parameters[0]).equals(method.getDeclaringClass()));
/*     */   }
/*     */   
/*     */   private static Class<?> wrapPrimitives(Class<?> clazz) {
/* 516 */     return clazz.isPrimitive() ? PRIMITIVE_WRAPPERS.get(clazz) : clazz;
/*     */   }
/*     */   
/*     */   private static int getConstantPoolSize(Object constantPool) {
/*     */     try {
/* 521 */       return ((Integer)GET_CONSTANT_POOL_SIZE.invoke(constantPool, new Object[0])).intValue();
/* 522 */     } catch (Exception ignore) {
/* 523 */       return 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Member getConstantPoolMethodAt(Object constantPool, int i) {
/*     */     try {
/* 529 */       return (Member)GET_CONSTANT_POOL_METHOD_AT.invoke(constantPool, new Object[] { Integer.valueOf(i) });
/* 530 */     } catch (Exception ignore) {
/* 531 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\net\jodah\typetools\TypeResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */