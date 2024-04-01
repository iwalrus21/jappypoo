/*     */ package javassist.bytecode.analysis;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtClass;
/*     */ import javassist.NotFoundException;
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
/*     */ public class Type
/*     */ {
/*     */   private final CtClass clazz;
/*     */   private final boolean special;
/*  47 */   private static final Map prims = new IdentityHashMap<Object, Object>();
/*     */   
/*  49 */   public static final Type DOUBLE = new Type(CtClass.doubleType);
/*     */   
/*  51 */   public static final Type BOOLEAN = new Type(CtClass.booleanType);
/*     */   
/*  53 */   public static final Type LONG = new Type(CtClass.longType);
/*     */   
/*  55 */   public static final Type CHAR = new Type(CtClass.charType);
/*     */   
/*  57 */   public static final Type BYTE = new Type(CtClass.byteType);
/*     */   
/*  59 */   public static final Type SHORT = new Type(CtClass.shortType);
/*     */   
/*  61 */   public static final Type INTEGER = new Type(CtClass.intType);
/*     */   
/*  63 */   public static final Type FLOAT = new Type(CtClass.floatType);
/*     */   
/*  65 */   public static final Type VOID = new Type(CtClass.voidType);
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
/*  76 */   public static final Type UNINIT = new Type(null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   public static final Type RETURN_ADDRESS = new Type(null, true);
/*     */ 
/*     */   
/*  85 */   public static final Type TOP = new Type(null, true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   public static final Type BOGUS = new Type(null, true);
/*     */ 
/*     */   
/*  97 */   public static final Type OBJECT = lookupType("java.lang.Object");
/*     */   
/*  99 */   public static final Type SERIALIZABLE = lookupType("java.io.Serializable");
/*     */   
/* 101 */   public static final Type CLONEABLE = lookupType("java.lang.Cloneable");
/*     */   
/* 103 */   public static final Type THROWABLE = lookupType("java.lang.Throwable");
/*     */   
/*     */   static {
/* 106 */     prims.put(CtClass.doubleType, DOUBLE);
/* 107 */     prims.put(CtClass.longType, LONG);
/* 108 */     prims.put(CtClass.charType, CHAR);
/* 109 */     prims.put(CtClass.shortType, SHORT);
/* 110 */     prims.put(CtClass.intType, INTEGER);
/* 111 */     prims.put(CtClass.floatType, FLOAT);
/* 112 */     prims.put(CtClass.byteType, BYTE);
/* 113 */     prims.put(CtClass.booleanType, BOOLEAN);
/* 114 */     prims.put(CtClass.voidType, VOID);
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
/*     */   public static Type get(CtClass clazz) {
/* 127 */     Type type = (Type)prims.get(clazz);
/* 128 */     return (type != null) ? type : new Type(clazz);
/*     */   }
/*     */   
/*     */   private static Type lookupType(String name) {
/*     */     try {
/* 133 */       return new Type(ClassPool.getDefault().get(name));
/* 134 */     } catch (NotFoundException e) {
/* 135 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   Type(CtClass clazz) {
/* 140 */     this(clazz, false);
/*     */   }
/*     */   
/*     */   private Type(CtClass clazz, boolean special) {
/* 144 */     this.clazz = clazz;
/* 145 */     this.special = special;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean popChanged() {
/* 150 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSize() {
/* 160 */     return (this.clazz == CtClass.doubleType || this.clazz == CtClass.longType || this == TOP) ? 2 : 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtClass getCtClass() {
/* 169 */     return this.clazz;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReference() {
/* 178 */     return (!this.special && (this.clazz == null || !this.clazz.isPrimitive()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSpecial() {
/* 188 */     return this.special;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isArray() {
/* 197 */     return (this.clazz != null && this.clazz.isArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDimensions() {
/* 207 */     if (!isArray()) return 0;
/*     */     
/* 209 */     String name = this.clazz.getName();
/* 210 */     int pos = name.length() - 1;
/* 211 */     int count = 0;
/* 212 */     while (name.charAt(pos) == ']') {
/* 213 */       pos -= 2;
/* 214 */       count++;
/*     */     } 
/*     */     
/* 217 */     return count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type getComponent() {
/*     */     CtClass component;
/* 227 */     if (this.clazz == null || !this.clazz.isArray()) {
/* 228 */       return null;
/*     */     }
/*     */     
/*     */     try {
/* 232 */       component = this.clazz.getComponentType();
/* 233 */     } catch (NotFoundException e) {
/* 234 */       throw new RuntimeException(e);
/*     */     } 
/*     */     
/* 237 */     Type type = (Type)prims.get(component);
/* 238 */     return (type != null) ? type : new Type(component);
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
/*     */   public boolean isAssignableFrom(Type type) {
/* 250 */     if (this == type) {
/* 251 */       return true;
/*     */     }
/* 253 */     if ((type == UNINIT && isReference()) || (this == UNINIT && type.isReference())) {
/* 254 */       return true;
/*     */     }
/* 256 */     if (type instanceof MultiType) {
/* 257 */       return ((MultiType)type).isAssignableTo(this);
/*     */     }
/* 259 */     if (type instanceof MultiArrayType) {
/* 260 */       return ((MultiArrayType)type).isAssignableTo(this);
/*     */     }
/*     */ 
/*     */     
/* 264 */     if (this.clazz == null || this.clazz.isPrimitive()) {
/* 265 */       return false;
/*     */     }
/*     */     try {
/* 268 */       return type.clazz.subtypeOf(this.clazz);
/* 269 */     } catch (Exception e) {
/* 270 */       throw new RuntimeException(e);
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
/*     */   
/*     */   public Type merge(Type type) {
/* 286 */     if (type == this)
/* 287 */       return this; 
/* 288 */     if (type == null)
/* 289 */       return this; 
/* 290 */     if (type == UNINIT)
/* 291 */       return this; 
/* 292 */     if (this == UNINIT) {
/* 293 */       return type;
/*     */     }
/*     */     
/* 296 */     if (!type.isReference() || !isReference()) {
/* 297 */       return BOGUS;
/*     */     }
/*     */     
/* 300 */     if (type instanceof MultiType) {
/* 301 */       return type.merge(this);
/*     */     }
/* 303 */     if (type.isArray() && isArray()) {
/* 304 */       return mergeArray(type);
/*     */     }
/*     */     try {
/* 307 */       return mergeClasses(type);
/* 308 */     } catch (NotFoundException e) {
/* 309 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   Type getRootComponent(Type type) {
/* 314 */     while (type.isArray()) {
/* 315 */       type = type.getComponent();
/*     */     }
/* 317 */     return type;
/*     */   }
/*     */   private Type createArray(Type rootComponent, int dims) {
/*     */     Type type;
/* 321 */     if (rootComponent instanceof MultiType) {
/* 322 */       return new MultiArrayType((MultiType)rootComponent, dims);
/*     */     }
/* 324 */     String name = arrayName(rootComponent.clazz.getName(), dims);
/*     */ 
/*     */     
/*     */     try {
/* 328 */       type = get(getClassPool(rootComponent).get(name));
/* 329 */     } catch (NotFoundException e) {
/* 330 */       throw new RuntimeException(e);
/*     */     } 
/*     */     
/* 333 */     return type;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   String arrayName(String component, int dims) {
/* 339 */     int i = component.length();
/* 340 */     int size = i + dims * 2;
/* 341 */     char[] string = new char[size];
/* 342 */     component.getChars(0, i, string, 0);
/* 343 */     while (i < size) {
/* 344 */       string[i++] = '[';
/* 345 */       string[i++] = ']';
/*     */     } 
/* 347 */     component = new String(string);
/* 348 */     return component;
/*     */   }
/*     */   
/*     */   private ClassPool getClassPool(Type rootComponent) {
/* 352 */     ClassPool pool = rootComponent.clazz.getClassPool();
/* 353 */     return (pool != null) ? pool : ClassPool.getDefault();
/*     */   } private Type mergeArray(Type type) {
/*     */     Type targetRoot;
/*     */     int targetDims;
/* 357 */     Type typeRoot = getRootComponent(type);
/* 358 */     Type thisRoot = getRootComponent(this);
/* 359 */     int typeDims = type.getDimensions();
/* 360 */     int thisDims = getDimensions();
/*     */ 
/*     */     
/* 363 */     if (typeDims == thisDims) {
/* 364 */       Type mergedComponent = thisRoot.merge(typeRoot);
/*     */ 
/*     */ 
/*     */       
/* 368 */       if (mergedComponent == BOGUS) {
/* 369 */         return OBJECT;
/*     */       }
/* 371 */       return createArray(mergedComponent, thisDims);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 377 */     if (typeDims < thisDims) {
/* 378 */       targetRoot = typeRoot;
/* 379 */       targetDims = typeDims;
/*     */     } else {
/* 381 */       targetRoot = thisRoot;
/* 382 */       targetDims = thisDims;
/*     */     } 
/*     */ 
/*     */     
/* 386 */     if (eq(CLONEABLE.clazz, targetRoot.clazz) || eq(SERIALIZABLE.clazz, targetRoot.clazz)) {
/* 387 */       return createArray(targetRoot, targetDims);
/*     */     }
/* 389 */     return createArray(OBJECT, targetDims);
/*     */   }
/*     */   
/*     */   private static CtClass findCommonSuperClass(CtClass one, CtClass two) throws NotFoundException {
/* 393 */     CtClass deep = one;
/* 394 */     CtClass shallow = two;
/* 395 */     CtClass backupShallow = shallow;
/* 396 */     CtClass backupDeep = deep;
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 401 */       if (eq(deep, shallow) && deep.getSuperclass() != null) {
/* 402 */         return deep;
/*     */       }
/* 404 */       CtClass deepSuper = deep.getSuperclass();
/* 405 */       CtClass shallowSuper = shallow.getSuperclass();
/*     */       
/* 407 */       if (shallowSuper == null) {
/*     */         
/* 409 */         shallow = backupShallow;
/*     */         
/*     */         break;
/*     */       } 
/* 413 */       if (deepSuper == null) {
/*     */         
/* 415 */         deep = backupDeep;
/* 416 */         backupDeep = backupShallow;
/* 417 */         backupShallow = deep;
/*     */         
/* 419 */         deep = shallow;
/* 420 */         shallow = backupShallow;
/*     */         
/*     */         break;
/*     */       } 
/* 424 */       deep = deepSuper;
/* 425 */       shallow = shallowSuper;
/*     */     } 
/*     */ 
/*     */     
/*     */     while (true) {
/* 430 */       deep = deep.getSuperclass();
/* 431 */       if (deep == null) {
/*     */         break;
/*     */       }
/* 434 */       backupDeep = backupDeep.getSuperclass();
/*     */     } 
/*     */     
/* 437 */     deep = backupDeep;
/*     */ 
/*     */ 
/*     */     
/* 441 */     while (!eq(deep, shallow)) {
/* 442 */       deep = deep.getSuperclass();
/* 443 */       shallow = shallow.getSuperclass();
/*     */     } 
/*     */     
/* 446 */     return deep;
/*     */   }
/*     */   
/*     */   private Type mergeClasses(Type type) throws NotFoundException {
/* 450 */     CtClass superClass = findCommonSuperClass(this.clazz, type.clazz);
/*     */ 
/*     */     
/* 453 */     if (superClass.getSuperclass() == null) {
/* 454 */       Map interfaces = findCommonInterfaces(type);
/* 455 */       if (interfaces.size() == 1)
/* 456 */         return new Type(interfaces.values().iterator().next()); 
/* 457 */       if (interfaces.size() > 1) {
/* 458 */         return new MultiType(interfaces);
/*     */       }
/*     */       
/* 461 */       return new Type(superClass);
/*     */     } 
/*     */ 
/*     */     
/* 465 */     Map commonDeclared = findExclusiveDeclaredInterfaces(type, superClass);
/* 466 */     if (commonDeclared.size() > 0) {
/* 467 */       return new MultiType(commonDeclared, new Type(superClass));
/*     */     }
/*     */     
/* 470 */     return new Type(superClass);
/*     */   }
/*     */   
/*     */   private Map findCommonInterfaces(Type type) {
/* 474 */     Map typeMap = getAllInterfaces(type.clazz, null);
/* 475 */     Map thisMap = getAllInterfaces(this.clazz, null);
/*     */     
/* 477 */     return findCommonInterfaces(typeMap, thisMap);
/*     */   }
/*     */   
/*     */   private Map findExclusiveDeclaredInterfaces(Type type, CtClass exclude) {
/* 481 */     Map typeMap = getDeclaredInterfaces(type.clazz, null);
/* 482 */     Map thisMap = getDeclaredInterfaces(this.clazz, null);
/* 483 */     Map excludeMap = getAllInterfaces(exclude, null);
/*     */     
/* 485 */     Iterator i = excludeMap.keySet().iterator();
/* 486 */     while (i.hasNext()) {
/* 487 */       Object intf = i.next();
/* 488 */       typeMap.remove(intf);
/* 489 */       thisMap.remove(intf);
/*     */     } 
/*     */     
/* 492 */     return findCommonInterfaces(typeMap, thisMap);
/*     */   }
/*     */ 
/*     */   
/*     */   Map findCommonInterfaces(Map typeMap, Map alterMap) {
/* 497 */     Iterator<?> i = alterMap.keySet().iterator();
/* 498 */     while (i.hasNext()) {
/* 499 */       if (!typeMap.containsKey(i.next())) {
/* 500 */         i.remove();
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 506 */     i = (new ArrayList(alterMap.values())).iterator();
/* 507 */     while (i.hasNext()) {
/* 508 */       CtClass interfaces[], intf = (CtClass)i.next();
/*     */       
/*     */       try {
/* 511 */         interfaces = intf.getInterfaces();
/* 512 */       } catch (NotFoundException e) {
/* 513 */         throw new RuntimeException(e);
/*     */       } 
/*     */       
/* 516 */       for (int c = 0; c < interfaces.length; c++) {
/* 517 */         alterMap.remove(interfaces[c].getName());
/*     */       }
/*     */     } 
/* 520 */     return alterMap;
/*     */   }
/*     */   
/*     */   Map getAllInterfaces(CtClass clazz, Map<Object, Object> map) {
/* 524 */     if (map == null) {
/* 525 */       map = new HashMap<Object, Object>();
/*     */     }
/* 527 */     if (clazz.isInterface())
/* 528 */       map.put(clazz.getName(), clazz); 
/*     */     do {
/*     */       try {
/* 531 */         CtClass[] interfaces = clazz.getInterfaces();
/* 532 */         for (int i = 0; i < interfaces.length; i++) {
/* 533 */           CtClass intf = interfaces[i];
/* 534 */           map.put(intf.getName(), intf);
/* 535 */           getAllInterfaces(intf, map);
/*     */         } 
/*     */         
/* 538 */         clazz = clazz.getSuperclass();
/* 539 */       } catch (NotFoundException e) {
/* 540 */         throw new RuntimeException(e);
/*     */       } 
/* 542 */     } while (clazz != null);
/*     */     
/* 544 */     return map;
/*     */   }
/*     */   Map getDeclaredInterfaces(CtClass clazz, Map<Object, Object> map) {
/*     */     CtClass[] interfaces;
/* 548 */     if (map == null) {
/* 549 */       map = new HashMap<Object, Object>();
/*     */     }
/* 551 */     if (clazz.isInterface()) {
/* 552 */       map.put(clazz.getName(), clazz);
/*     */     }
/*     */     
/*     */     try {
/* 556 */       interfaces = clazz.getInterfaces();
/* 557 */     } catch (NotFoundException e) {
/* 558 */       throw new RuntimeException(e);
/*     */     } 
/*     */     
/* 561 */     for (int i = 0; i < interfaces.length; i++) {
/* 562 */       CtClass intf = interfaces[i];
/* 563 */       map.put(intf.getName(), intf);
/* 564 */       getDeclaredInterfaces(intf, map);
/*     */     } 
/*     */     
/* 567 */     return map;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 571 */     if (!(o instanceof Type)) {
/* 572 */       return false;
/*     */     }
/* 574 */     return (o.getClass() == getClass() && eq(this.clazz, ((Type)o).clazz));
/*     */   }
/*     */   
/*     */   static boolean eq(CtClass one, CtClass two) {
/* 578 */     return (one == two || (one != null && two != null && one.getName().equals(two.getName())));
/*     */   }
/*     */   
/*     */   public String toString() {
/* 582 */     if (this == BOGUS)
/* 583 */       return "BOGUS"; 
/* 584 */     if (this == UNINIT)
/* 585 */       return "UNINIT"; 
/* 586 */     if (this == RETURN_ADDRESS)
/* 587 */       return "RETURN ADDRESS"; 
/* 588 */     if (this == TOP) {
/* 589 */       return "TOP";
/*     */     }
/* 591 */     return (this.clazz == null) ? "null" : this.clazz.getName();
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\analysis\Type.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */