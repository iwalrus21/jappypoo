/*      */ package javassist.util.proxy;
/*      */ 
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Member;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.security.ProtectionDomain;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.WeakHashMap;
/*      */ import javassist.CannotCompileException;
/*      */ import javassist.bytecode.Bytecode;
/*      */ import javassist.bytecode.ClassFile;
/*      */ import javassist.bytecode.CodeAttribute;
/*      */ import javassist.bytecode.ConstPool;
/*      */ import javassist.bytecode.Descriptor;
/*      */ import javassist.bytecode.DuplicateMemberException;
/*      */ import javassist.bytecode.ExceptionsAttribute;
/*      */ import javassist.bytecode.FieldInfo;
/*      */ import javassist.bytecode.MethodInfo;
/*      */ import javassist.bytecode.StackMapTable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ProxyFactory
/*      */ {
/*      */   private Class superClass;
/*      */   private Class[] interfaces;
/*      */   private MethodFilter methodFilter;
/*      */   private MethodHandler handler;
/*      */   private List signatureMethods;
/*      */   private boolean hasGetHandler;
/*      */   private byte[] signature;
/*      */   private String classname;
/*      */   private String basename;
/*      */   private String superName;
/*      */   private Class thisClass;
/*      */   private boolean factoryUseCache;
/*      */   private boolean factoryWriteReplace;
/*      */   public String writeDirectory;
/*  189 */   private static final Class OBJECT_TYPE = Object.class;
/*      */   
/*      */   private static final String HOLDER = "_methods_";
/*      */   private static final String HOLDER_TYPE = "[Ljava/lang/reflect/Method;";
/*      */   private static final String FILTER_SIGNATURE_FIELD = "_filter_signature";
/*      */   private static final String FILTER_SIGNATURE_TYPE = "[B";
/*      */   private static final String HANDLER = "handler";
/*      */   private static final String NULL_INTERCEPTOR_HOLDER = "javassist.util.proxy.RuntimeSupport";
/*      */   private static final String DEFAULT_INTERCEPTOR = "default_interceptor";
/*  198 */   private static final String HANDLER_TYPE = 'L' + MethodHandler.class
/*  199 */     .getName().replace('.', '/') + ';';
/*      */   private static final String HANDLER_SETTER = "setHandler";
/*  201 */   private static final String HANDLER_SETTER_TYPE = "(" + HANDLER_TYPE + ")V";
/*      */   
/*      */   private static final String HANDLER_GETTER = "getHandler";
/*  204 */   private static final String HANDLER_GETTER_TYPE = "()" + HANDLER_TYPE;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String SERIAL_VERSION_UID_FIELD = "serialVersionUID";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String SERIAL_VERSION_UID_TYPE = "J";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final long SERIAL_VERSION_UID_VALUE = -1L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static volatile boolean useCache = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static volatile boolean useWriteReplace = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isUseCache() {
/*  254 */     return this.factoryUseCache;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseCache(boolean useCache) {
/*  266 */     if (this.handler != null && useCache) {
/*  267 */       throw new RuntimeException("caching cannot be enabled if the factory default interceptor has been set");
/*      */     }
/*  269 */     this.factoryUseCache = useCache;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isUseWriteReplace() {
/*  278 */     return this.factoryWriteReplace;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseWriteReplace(boolean useWriteReplace) {
/*  288 */     this.factoryWriteReplace = useWriteReplace;
/*      */   }
/*      */   
/*  291 */   private static WeakHashMap proxyCache = new WeakHashMap<Object, Object>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isProxyClass(Class<?> cl) {
/*  301 */     return Proxy.class.isAssignableFrom(cl);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class ProxyDetails
/*      */   {
/*      */     byte[] signature;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     WeakReference proxyClass;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean isUseWriteReplace;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     ProxyDetails(byte[] signature, Class<?> proxyClass, boolean isUseWriteReplace) {
/*  329 */       this.signature = signature;
/*  330 */       this.proxyClass = new WeakReference<Class<?>>(proxyClass);
/*  331 */       this.isUseWriteReplace = isUseWriteReplace;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ProxyFactory() {
/*  339 */     this.superClass = null;
/*  340 */     this.interfaces = null;
/*  341 */     this.methodFilter = null;
/*  342 */     this.handler = null;
/*  343 */     this.signature = null;
/*  344 */     this.signatureMethods = null;
/*  345 */     this.hasGetHandler = false;
/*  346 */     this.thisClass = null;
/*  347 */     this.writeDirectory = null;
/*  348 */     this.factoryUseCache = useCache;
/*  349 */     this.factoryWriteReplace = useWriteReplace;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSuperclass(Class clazz) {
/*  356 */     this.superClass = clazz;
/*      */     
/*  358 */     this.signature = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Class getSuperclass() {
/*  366 */     return this.superClass;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInterfaces(Class[] ifs) {
/*  372 */     this.interfaces = ifs;
/*      */     
/*  374 */     this.signature = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Class[] getInterfaces() {
/*  382 */     return this.interfaces;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFilter(MethodFilter mf) {
/*  388 */     this.methodFilter = mf;
/*      */     
/*  390 */     this.signature = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Class createClass() {
/*  397 */     if (this.signature == null) {
/*  398 */       computeSignature(this.methodFilter);
/*      */     }
/*  400 */     return createClass1();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Class createClass(MethodFilter filter) {
/*  407 */     computeSignature(filter);
/*  408 */     return createClass1();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Class createClass(byte[] signature) {
/*  419 */     installSignature(signature);
/*  420 */     return createClass1();
/*      */   }
/*      */   
/*      */   private Class createClass1() {
/*  424 */     Class result = this.thisClass;
/*  425 */     if (result == null) {
/*  426 */       ClassLoader cl = getClassLoader();
/*  427 */       synchronized (proxyCache) {
/*  428 */         if (this.factoryUseCache) {
/*  429 */           createClass2(cl);
/*      */         } else {
/*  431 */           createClass3(cl);
/*      */         } 
/*  433 */         result = this.thisClass;
/*      */         
/*  435 */         this.thisClass = null;
/*      */       } 
/*      */     } 
/*      */     
/*  439 */     return result;
/*      */   }
/*      */   
/*  442 */   private static char[] hexDigits = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getKey(Class superClass, Class[] interfaces, byte[] signature, boolean useWriteReplace) {
/*  448 */     StringBuffer sbuf = new StringBuffer();
/*  449 */     if (superClass != null) {
/*  450 */       sbuf.append(superClass.getName());
/*      */     }
/*  452 */     sbuf.append(":"); int i;
/*  453 */     for (i = 0; i < interfaces.length; i++) {
/*  454 */       sbuf.append(interfaces[i].getName());
/*  455 */       sbuf.append(":");
/*      */     } 
/*  457 */     for (i = 0; i < signature.length; i++) {
/*  458 */       byte b = signature[i];
/*  459 */       int lo = b & 0xF;
/*  460 */       int hi = b >> 4 & 0xF;
/*  461 */       sbuf.append(hexDigits[lo]);
/*  462 */       sbuf.append(hexDigits[hi]);
/*      */     } 
/*  464 */     if (useWriteReplace) {
/*  465 */       sbuf.append(":w");
/*      */     }
/*      */     
/*  468 */     return sbuf.toString();
/*      */   }
/*      */   
/*      */   private void createClass2(ClassLoader cl) {
/*  472 */     String key = getKey(this.superClass, this.interfaces, this.signature, this.factoryWriteReplace);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  479 */     HashMap<Object, Object> cacheForTheLoader = (HashMap)proxyCache.get(cl);
/*      */     
/*  481 */     if (cacheForTheLoader == null) {
/*  482 */       cacheForTheLoader = new HashMap<Object, Object>();
/*  483 */       proxyCache.put(cl, cacheForTheLoader);
/*      */     } 
/*  485 */     ProxyDetails details = (ProxyDetails)cacheForTheLoader.get(key);
/*  486 */     if (details != null) {
/*  487 */       WeakReference<Class<?>> reference = details.proxyClass;
/*  488 */       this.thisClass = reference.get();
/*  489 */       if (this.thisClass != null) {
/*      */         return;
/*      */       }
/*      */     } 
/*  493 */     createClass3(cl);
/*  494 */     details = new ProxyDetails(this.signature, this.thisClass, this.factoryWriteReplace);
/*  495 */     cacheForTheLoader.put(key, details);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void createClass3(ClassLoader cl) {
/*  501 */     allocateClassName();
/*      */     
/*      */     try {
/*  504 */       ClassFile cf = make();
/*  505 */       if (this.writeDirectory != null) {
/*  506 */         FactoryHelper.writeFile(cf, this.writeDirectory);
/*      */       }
/*  508 */       this.thisClass = FactoryHelper.toClass(cf, cl, getDomain());
/*  509 */       setField("_filter_signature", this.signature);
/*      */       
/*  511 */       if (!this.factoryUseCache) {
/*  512 */         setField("default_interceptor", this.handler);
/*      */       }
/*      */     }
/*  515 */     catch (CannotCompileException e) {
/*  516 */       throw new RuntimeException(e.getMessage(), e);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void setField(String fieldName, Object value) {
/*  522 */     if (this.thisClass != null && value != null)
/*      */       try {
/*  524 */         Field f = this.thisClass.getField(fieldName);
/*  525 */         SecurityActions.setAccessible(f, true);
/*  526 */         f.set((Object)null, value);
/*  527 */         SecurityActions.setAccessible(f, false);
/*      */       }
/*  529 */       catch (Exception e) {
/*  530 */         throw new RuntimeException(e);
/*      */       }  
/*      */   }
/*      */   
/*      */   static byte[] getFilterSignature(Class clazz) {
/*  535 */     return (byte[])getField(clazz, "_filter_signature");
/*      */   }
/*      */   
/*      */   private static Object getField(Class clazz, String fieldName) {
/*      */     try {
/*  540 */       Field f = clazz.getField(fieldName);
/*  541 */       f.setAccessible(true);
/*  542 */       Object value = f.get((Object)null);
/*  543 */       f.setAccessible(false);
/*  544 */       return value;
/*      */     }
/*  546 */     catch (Exception e) {
/*  547 */       throw new RuntimeException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static MethodHandler getHandler(Proxy p) {
/*      */     try {
/*  560 */       Field f = p.getClass().getDeclaredField("handler");
/*  561 */       f.setAccessible(true);
/*  562 */       Object value = f.get(p);
/*  563 */       f.setAccessible(false);
/*  564 */       return (MethodHandler)value;
/*      */     }
/*  566 */     catch (Exception e) {
/*  567 */       throw new RuntimeException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  606 */   public static ClassLoaderProvider classLoaderProvider = new ClassLoaderProvider()
/*      */     {
/*      */       public ClassLoader get(ProxyFactory pf) {
/*  609 */         return pf.getClassLoader0();
/*      */       }
/*      */     };
/*      */   
/*      */   protected ClassLoader getClassLoader() {
/*  614 */     return classLoaderProvider.get(this);
/*      */   }
/*      */   
/*      */   protected ClassLoader getClassLoader0() {
/*  618 */     ClassLoader loader = null;
/*  619 */     if (this.superClass != null && !this.superClass.getName().equals("java.lang.Object")) {
/*  620 */       loader = this.superClass.getClassLoader();
/*  621 */     } else if (this.interfaces != null && this.interfaces.length > 0) {
/*  622 */       loader = this.interfaces[0].getClassLoader();
/*      */     } 
/*  624 */     if (loader == null) {
/*  625 */       loader = getClass().getClassLoader();
/*      */       
/*  627 */       if (loader == null) {
/*  628 */         loader = Thread.currentThread().getContextClassLoader();
/*  629 */         if (loader == null) {
/*  630 */           loader = ClassLoader.getSystemClassLoader();
/*      */         }
/*      */       } 
/*      */     } 
/*  634 */     return loader;
/*      */   }
/*      */   
/*      */   protected ProtectionDomain getDomain() {
/*      */     Class<?> clazz;
/*  639 */     if (this.superClass != null && !this.superClass.getName().equals("java.lang.Object")) {
/*  640 */       clazz = this.superClass;
/*  641 */     } else if (this.interfaces != null && this.interfaces.length > 0) {
/*  642 */       clazz = this.interfaces[0];
/*      */     } else {
/*  644 */       clazz = getClass();
/*      */     } 
/*  646 */     return clazz.getProtectionDomain();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object create(Class[] paramTypes, Object[] args, MethodHandler mh) throws NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
/*  661 */     Object obj = create(paramTypes, args);
/*  662 */     ((Proxy)obj).setHandler(mh);
/*  663 */     return obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object create(Class[] paramTypes, Object[] args) throws NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
/*  676 */     Class c = createClass();
/*  677 */     Constructor cons = c.getConstructor(paramTypes);
/*  678 */     return cons.newInstance(args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHandler(MethodHandler mi) {
/*  693 */     if (this.factoryUseCache && mi != null) {
/*  694 */       this.factoryUseCache = false;
/*      */       
/*  696 */       this.thisClass = null;
/*      */     } 
/*  698 */     this.handler = mi;
/*      */ 
/*      */     
/*  701 */     setField("default_interceptor", this.handler);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  723 */   public static UniqueName nameGenerator = new UniqueName() {
/*  724 */       private final String sep = "_$$_jvst" + Integer.toHexString(hashCode() & 0xFFF) + "_";
/*  725 */       private int counter = 0;
/*      */       
/*      */       public String get(String classname) {
/*  728 */         return classname + this.sep + Integer.toHexString(this.counter++);
/*      */       }
/*      */     };
/*      */   
/*      */   private static String makeProxyName(String classname) {
/*  733 */     synchronized (nameGenerator) {
/*  734 */       return nameGenerator.get(classname);
/*      */     } 
/*      */   }
/*      */   
/*      */   private ClassFile make() throws CannotCompileException {
/*  739 */     ClassFile cf = new ClassFile(false, this.classname, this.superName);
/*  740 */     cf.setAccessFlags(1);
/*  741 */     setInterfaces(cf, this.interfaces, this.hasGetHandler ? Proxy.class : ProxyObject.class);
/*  742 */     ConstPool pool = cf.getConstPool();
/*      */ 
/*      */     
/*  745 */     if (!this.factoryUseCache) {
/*  746 */       FieldInfo finfo = new FieldInfo(pool, "default_interceptor", HANDLER_TYPE);
/*  747 */       finfo.setAccessFlags(9);
/*  748 */       cf.addField(finfo);
/*      */     } 
/*      */ 
/*      */     
/*  752 */     FieldInfo finfo2 = new FieldInfo(pool, "handler", HANDLER_TYPE);
/*  753 */     finfo2.setAccessFlags(2);
/*  754 */     cf.addField(finfo2);
/*      */ 
/*      */     
/*  757 */     FieldInfo finfo3 = new FieldInfo(pool, "_filter_signature", "[B");
/*  758 */     finfo3.setAccessFlags(9);
/*  759 */     cf.addField(finfo3);
/*      */ 
/*      */     
/*  762 */     FieldInfo finfo4 = new FieldInfo(pool, "serialVersionUID", "J");
/*  763 */     finfo4.setAccessFlags(25);
/*  764 */     cf.addField(finfo4);
/*      */ 
/*      */ 
/*      */     
/*  768 */     makeConstructors(this.classname, cf, pool, this.classname);
/*      */     
/*  770 */     ArrayList forwarders = new ArrayList();
/*  771 */     int s = overrideMethods(cf, pool, this.classname, forwarders);
/*  772 */     addClassInitializer(cf, pool, this.classname, s, forwarders);
/*  773 */     addSetter(this.classname, cf, pool);
/*  774 */     if (!this.hasGetHandler) {
/*  775 */       addGetter(this.classname, cf, pool);
/*      */     }
/*  777 */     if (this.factoryWriteReplace) {
/*      */       try {
/*  779 */         cf.addMethod(makeWriteReplace(pool));
/*      */       }
/*  781 */       catch (DuplicateMemberException duplicateMemberException) {}
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  786 */     this.thisClass = null;
/*  787 */     return cf;
/*      */   }
/*      */   
/*      */   private void checkClassAndSuperName() {
/*  791 */     if (this.interfaces == null) {
/*  792 */       this.interfaces = new Class[0];
/*      */     }
/*  794 */     if (this.superClass == null) {
/*  795 */       this.superClass = OBJECT_TYPE;
/*  796 */       this.superName = this.superClass.getName();
/*  797 */       this
/*  798 */         .basename = (this.interfaces.length == 0) ? this.superName : this.interfaces[0].getName();
/*      */     } else {
/*  800 */       this.superName = this.superClass.getName();
/*  801 */       this.basename = this.superName;
/*      */     } 
/*      */     
/*  804 */     if (Modifier.isFinal(this.superClass.getModifiers())) {
/*  805 */       throw new RuntimeException(this.superName + " is final");
/*      */     }
/*  807 */     if (this.basename.startsWith("java."))
/*  808 */       this.basename = "org.javassist.tmp." + this.basename; 
/*      */   }
/*      */   
/*      */   private void allocateClassName() {
/*  812 */     this.classname = makeProxyName(this.basename);
/*      */   }
/*      */   
/*  815 */   private static Comparator sorter = new Comparator()
/*      */     {
/*      */       public int compare(Object o1, Object o2) {
/*  818 */         Map.Entry e1 = (Map.Entry)o1;
/*  819 */         Map.Entry e2 = (Map.Entry)o2;
/*  820 */         String key1 = (String)e1.getKey();
/*  821 */         String key2 = (String)e2.getKey();
/*  822 */         return key1.compareTo(key2);
/*      */       }
/*      */     };
/*      */   
/*      */   private void makeSortedMethodList() {
/*  827 */     checkClassAndSuperName();
/*      */     
/*  829 */     this.hasGetHandler = false;
/*  830 */     HashMap allMethods = getMethods(this.superClass, this.interfaces);
/*  831 */     this.signatureMethods = new ArrayList(allMethods.entrySet());
/*  832 */     Collections.sort(this.signatureMethods, sorter);
/*      */   }
/*      */   private static final String HANDLER_GETTER_KEY = "getHandler:()";
/*      */   
/*      */   private void computeSignature(MethodFilter filter) {
/*  837 */     makeSortedMethodList();
/*      */     
/*  839 */     int l = this.signatureMethods.size();
/*  840 */     int maxBytes = l + 7 >> 3;
/*  841 */     this.signature = new byte[maxBytes];
/*  842 */     for (int idx = 0; idx < l; idx++) {
/*      */       
/*  844 */       Map.Entry e = this.signatureMethods.get(idx);
/*  845 */       Method m = (Method)e.getValue();
/*  846 */       int mod = m.getModifiers();
/*  847 */       if (!Modifier.isFinal(mod) && !Modifier.isStatic(mod) && 
/*  848 */         isVisible(mod, this.basename, m) && (filter == null || filter.isHandled(m))) {
/*  849 */         setBit(this.signature, idx);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void installSignature(byte[] signature) {
/*  856 */     makeSortedMethodList();
/*      */     
/*  858 */     int l = this.signatureMethods.size();
/*  859 */     int maxBytes = l + 7 >> 3;
/*  860 */     if (signature.length != maxBytes) {
/*  861 */       throw new RuntimeException("invalid filter signature length for deserialized proxy class");
/*      */     }
/*      */     
/*  864 */     this.signature = signature;
/*      */   }
/*      */   
/*      */   private boolean testBit(byte[] signature, int idx) {
/*  868 */     int byteIdx = idx >> 3;
/*  869 */     if (byteIdx > signature.length) {
/*  870 */       return false;
/*      */     }
/*  872 */     int bitIdx = idx & 0x7;
/*  873 */     int mask = 1 << bitIdx;
/*  874 */     int sigByte = signature[byteIdx];
/*  875 */     return ((sigByte & mask) != 0);
/*      */   }
/*      */ 
/*      */   
/*      */   private void setBit(byte[] signature, int idx) {
/*  880 */     int byteIdx = idx >> 3;
/*  881 */     if (byteIdx < signature.length) {
/*  882 */       int bitIdx = idx & 0x7;
/*  883 */       int mask = 1 << bitIdx;
/*  884 */       int sigByte = signature[byteIdx];
/*  885 */       signature[byteIdx] = (byte)(sigByte | mask);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void setInterfaces(ClassFile cf, Class[] interfaces, Class proxyClass) {
/*  890 */     String list[], setterIntf = proxyClass.getName();
/*      */     
/*  892 */     if (interfaces == null || interfaces.length == 0) {
/*  893 */       list = new String[] { setterIntf };
/*      */     } else {
/*  895 */       list = new String[interfaces.length + 1];
/*  896 */       for (int i = 0; i < interfaces.length; i++) {
/*  897 */         list[i] = interfaces[i].getName();
/*      */       }
/*  899 */       list[interfaces.length] = setterIntf;
/*      */     } 
/*      */     
/*  902 */     cf.setInterfaces(list);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void addClassInitializer(ClassFile cf, ConstPool cp, String classname, int size, ArrayList forwarders) throws CannotCompileException {
/*  909 */     FieldInfo finfo = new FieldInfo(cp, "_methods_", "[Ljava/lang/reflect/Method;");
/*  910 */     finfo.setAccessFlags(10);
/*  911 */     cf.addField(finfo);
/*  912 */     MethodInfo minfo = new MethodInfo(cp, "<clinit>", "()V");
/*  913 */     minfo.setAccessFlags(8);
/*  914 */     setThrows(minfo, cp, new Class[] { ClassNotFoundException.class });
/*      */     
/*  916 */     Bytecode code = new Bytecode(cp, 0, 2);
/*  917 */     code.addIconst(size * 2);
/*  918 */     code.addAnewarray("java.lang.reflect.Method");
/*  919 */     int varArray = 0;
/*  920 */     code.addAstore(0);
/*      */ 
/*      */ 
/*      */     
/*  924 */     code.addLdc(classname);
/*  925 */     code.addInvokestatic("java.lang.Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;");
/*      */     
/*  927 */     int varClass = 1;
/*  928 */     code.addAstore(1);
/*      */     
/*  930 */     Iterator<Find2MethodsArgs> it = forwarders.iterator();
/*  931 */     while (it.hasNext()) {
/*  932 */       Find2MethodsArgs args = it.next();
/*  933 */       callFind2Methods(code, args.methodName, args.delegatorName, args.origIndex, args.descriptor, 1, 0);
/*      */     } 
/*      */ 
/*      */     
/*  937 */     code.addAload(0);
/*  938 */     code.addPutstatic(classname, "_methods_", "[Ljava/lang/reflect/Method;");
/*      */     
/*  940 */     code.addLconst(-1L);
/*  941 */     code.addPutstatic(classname, "serialVersionUID", "J");
/*  942 */     code.addOpcode(177);
/*  943 */     minfo.setCodeAttribute(code.toCodeAttribute());
/*  944 */     cf.addMethod(minfo);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void callFind2Methods(Bytecode code, String superMethod, String thisMethod, int index, String desc, int classVar, int arrayVar) {
/*  952 */     String findClass = RuntimeSupport.class.getName();
/*  953 */     String findDesc = "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;[Ljava/lang/reflect/Method;)V";
/*      */ 
/*      */     
/*  956 */     code.addAload(classVar);
/*  957 */     code.addLdc(superMethod);
/*  958 */     if (thisMethod == null) {
/*  959 */       code.addOpcode(1);
/*      */     } else {
/*  961 */       code.addLdc(thisMethod);
/*      */     } 
/*  963 */     code.addIconst(index);
/*  964 */     code.addLdc(desc);
/*  965 */     code.addAload(arrayVar);
/*  966 */     code.addInvokestatic(findClass, "find2Methods", findDesc);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void addSetter(String classname, ClassFile cf, ConstPool cp) throws CannotCompileException {
/*  972 */     MethodInfo minfo = new MethodInfo(cp, "setHandler", HANDLER_SETTER_TYPE);
/*      */     
/*  974 */     minfo.setAccessFlags(1);
/*  975 */     Bytecode code = new Bytecode(cp, 2, 2);
/*  976 */     code.addAload(0);
/*  977 */     code.addAload(1);
/*  978 */     code.addPutfield(classname, "handler", HANDLER_TYPE);
/*  979 */     code.addOpcode(177);
/*  980 */     minfo.setCodeAttribute(code.toCodeAttribute());
/*  981 */     cf.addMethod(minfo);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void addGetter(String classname, ClassFile cf, ConstPool cp) throws CannotCompileException {
/*  987 */     MethodInfo minfo = new MethodInfo(cp, "getHandler", HANDLER_GETTER_TYPE);
/*      */     
/*  989 */     minfo.setAccessFlags(1);
/*  990 */     Bytecode code = new Bytecode(cp, 1, 1);
/*  991 */     code.addAload(0);
/*  992 */     code.addGetfield(classname, "handler", HANDLER_TYPE);
/*  993 */     code.addOpcode(176);
/*  994 */     minfo.setCodeAttribute(code.toCodeAttribute());
/*  995 */     cf.addMethod(minfo);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private int overrideMethods(ClassFile cf, ConstPool cp, String className, ArrayList forwarders) throws CannotCompileException {
/* 1001 */     String prefix = makeUniqueName("_d", this.signatureMethods);
/* 1002 */     Iterator<Map.Entry> it = this.signatureMethods.iterator();
/* 1003 */     int index = 0;
/* 1004 */     while (it.hasNext()) {
/* 1005 */       Map.Entry e = it.next();
/* 1006 */       String key = (String)e.getKey();
/* 1007 */       Method meth = (Method)e.getValue();
/* 1008 */       if ((ClassFile.MAJOR_VERSION < 49 || !isBridge(meth)) && 
/* 1009 */         testBit(this.signature, index)) {
/* 1010 */         override(className, meth, prefix, index, 
/* 1011 */             keyToDesc(key, meth), cf, cp, forwarders);
/*      */       }
/*      */       
/* 1014 */       index++;
/*      */     } 
/*      */     
/* 1017 */     return index;
/*      */   }
/*      */   
/*      */   private static boolean isBridge(Method m) {
/* 1021 */     return m.isBridge();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void override(String thisClassname, Method meth, String prefix, int index, String desc, ClassFile cf, ConstPool cp, ArrayList forwarders) throws CannotCompileException {
/* 1028 */     Class<?> declClass = meth.getDeclaringClass();
/* 1029 */     String delegatorName = prefix + index + meth.getName();
/* 1030 */     if (Modifier.isAbstract(meth.getModifiers())) {
/* 1031 */       delegatorName = null;
/*      */     } else {
/*      */       
/* 1034 */       MethodInfo delegator = makeDelegator(meth, desc, cp, declClass, delegatorName);
/*      */       
/* 1036 */       delegator.setAccessFlags(delegator.getAccessFlags() & 0xFFFFFFBF);
/* 1037 */       cf.addMethod(delegator);
/*      */     } 
/*      */ 
/*      */     
/* 1041 */     MethodInfo forwarder = makeForwarder(thisClassname, meth, desc, cp, declClass, delegatorName, index, forwarders);
/*      */     
/* 1043 */     cf.addMethod(forwarder);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void makeConstructors(String thisClassName, ClassFile cf, ConstPool cp, String classname) throws CannotCompileException {
/* 1049 */     Constructor[] cons = SecurityActions.getDeclaredConstructors(this.superClass);
/*      */     
/* 1051 */     boolean doHandlerInit = !this.factoryUseCache;
/* 1052 */     for (int i = 0; i < cons.length; i++) {
/* 1053 */       Constructor c = cons[i];
/* 1054 */       int mod = c.getModifiers();
/* 1055 */       if (!Modifier.isFinal(mod) && !Modifier.isPrivate(mod) && 
/* 1056 */         isVisible(mod, this.basename, c)) {
/* 1057 */         MethodInfo m = makeConstructor(thisClassName, c, cp, this.superClass, doHandlerInit);
/* 1058 */         cf.addMethod(m);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private static String makeUniqueName(String name, List sortedMethods) {
/* 1064 */     if (makeUniqueName0(name, sortedMethods.iterator())) {
/* 1065 */       return name;
/*      */     }
/* 1067 */     for (int i = 100; i < 999; i++) {
/* 1068 */       String s = name + i;
/* 1069 */       if (makeUniqueName0(s, sortedMethods.iterator())) {
/* 1070 */         return s;
/*      */       }
/*      */     } 
/* 1073 */     throw new RuntimeException("cannot make a unique method name");
/*      */   }
/*      */   
/*      */   private static boolean makeUniqueName0(String name, Iterator<Map.Entry> it) {
/* 1077 */     while (it.hasNext()) {
/* 1078 */       Map.Entry e = it.next();
/* 1079 */       String key = (String)e.getKey();
/* 1080 */       if (key.startsWith(name)) {
/* 1081 */         return false;
/*      */       }
/*      */     } 
/* 1084 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isVisible(int mod, String from, Member meth) {
/* 1093 */     if ((mod & 0x2) != 0)
/* 1094 */       return false; 
/* 1095 */     if ((mod & 0x5) != 0) {
/* 1096 */       return true;
/*      */     }
/* 1098 */     String p = getPackageName(from);
/* 1099 */     String q = getPackageName(meth.getDeclaringClass().getName());
/* 1100 */     if (p == null) {
/* 1101 */       return (q == null);
/*      */     }
/* 1103 */     return p.equals(q);
/*      */   }
/*      */ 
/*      */   
/*      */   private static String getPackageName(String name) {
/* 1108 */     int i = name.lastIndexOf('.');
/* 1109 */     if (i < 0) {
/* 1110 */       return null;
/*      */     }
/* 1112 */     return name.substring(0, i);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private HashMap getMethods(Class superClass, Class[] interfaceTypes) {
/* 1118 */     HashMap<Object, Object> hash = new HashMap<Object, Object>();
/* 1119 */     HashSet set = new HashSet();
/* 1120 */     for (int i = 0; i < interfaceTypes.length; i++) {
/* 1121 */       getMethods(hash, interfaceTypes[i], set);
/*      */     }
/* 1123 */     getMethods(hash, superClass, set);
/* 1124 */     return hash;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void getMethods(HashMap<String, Method> hash, Class<?> clazz, Set<Class<?>> visitedClasses) {
/* 1130 */     if (!visitedClasses.add(clazz)) {
/*      */       return;
/*      */     }
/* 1133 */     Class[] ifs = clazz.getInterfaces();
/* 1134 */     for (int i = 0; i < ifs.length; i++) {
/* 1135 */       getMethods(hash, ifs[i], visitedClasses);
/*      */     }
/* 1137 */     Class<?> parent = clazz.getSuperclass();
/* 1138 */     if (parent != null) {
/* 1139 */       getMethods(hash, parent, visitedClasses);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1146 */     Method[] methods = SecurityActions.getDeclaredMethods(clazz);
/* 1147 */     for (int j = 0; j < methods.length; j++) {
/* 1148 */       if (!Modifier.isPrivate(methods[j].getModifiers())) {
/* 1149 */         Method m = methods[j];
/* 1150 */         String key = m.getName() + ':' + RuntimeSupport.makeDescriptor(m);
/* 1151 */         if (key.startsWith("getHandler:()")) {
/* 1152 */           this.hasGetHandler = true;
/*      */         }
/*      */ 
/*      */         
/* 1156 */         Method oldMethod = hash.put(key, m);
/*      */ 
/*      */ 
/*      */         
/* 1160 */         if (null != oldMethod && isBridge(m) && 
/* 1161 */           !Modifier.isPublic(oldMethod.getDeclaringClass().getModifiers()) && 
/* 1162 */           !Modifier.isAbstract(oldMethod.getModifiers()) && !isOverloaded(j, methods)) {
/* 1163 */           hash.put(key, oldMethod);
/*      */         }
/*      */         
/* 1166 */         if (null != oldMethod && Modifier.isPublic(oldMethod.getModifiers()) && 
/* 1167 */           !Modifier.isPublic(m.getModifiers()))
/*      */         {
/*      */           
/* 1170 */           hash.put(key, oldMethod); } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private static boolean isOverloaded(int index, Method[] methods) {
/* 1176 */     String name = methods[index].getName();
/* 1177 */     for (int i = 0; i < methods.length; i++) {
/* 1178 */       if (i != index && 
/* 1179 */         name.equals(methods[i].getName()))
/* 1180 */         return true; 
/*      */     } 
/* 1182 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String keyToDesc(String key, Method m) {
/* 1189 */     return key.substring(key.indexOf(':') + 1);
/*      */   }
/*      */ 
/*      */   
/*      */   private static MethodInfo makeConstructor(String thisClassName, Constructor cons, ConstPool cp, Class superClass, boolean doHandlerInit) {
/* 1194 */     String desc = RuntimeSupport.makeDescriptor(cons.getParameterTypes(), void.class);
/*      */     
/* 1196 */     MethodInfo minfo = new MethodInfo(cp, "<init>", desc);
/* 1197 */     minfo.setAccessFlags(1);
/* 1198 */     setThrows(minfo, cp, cons.getExceptionTypes());
/* 1199 */     Bytecode code = new Bytecode(cp, 0, 0);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1204 */     if (doHandlerInit) {
/* 1205 */       code.addAload(0);
/* 1206 */       code.addGetstatic(thisClassName, "default_interceptor", HANDLER_TYPE);
/* 1207 */       code.addPutfield(thisClassName, "handler", HANDLER_TYPE);
/* 1208 */       code.addGetstatic(thisClassName, "default_interceptor", HANDLER_TYPE);
/* 1209 */       code.addOpcode(199);
/* 1210 */       code.addIndex(10);
/*      */     } 
/*      */ 
/*      */     
/* 1214 */     code.addAload(0);
/* 1215 */     code.addGetstatic("javassist.util.proxy.RuntimeSupport", "default_interceptor", HANDLER_TYPE);
/* 1216 */     code.addPutfield(thisClassName, "handler", HANDLER_TYPE);
/* 1217 */     int pc = code.currentPc();
/*      */     
/* 1219 */     code.addAload(0);
/* 1220 */     int s = addLoadParameters(code, cons.getParameterTypes(), 1);
/* 1221 */     code.addInvokespecial(superClass.getName(), "<init>", desc);
/* 1222 */     code.addOpcode(177);
/* 1223 */     code.setMaxLocals(s + 1);
/* 1224 */     CodeAttribute ca = code.toCodeAttribute();
/* 1225 */     minfo.setCodeAttribute(ca);
/*      */     
/* 1227 */     StackMapTable.Writer writer = new StackMapTable.Writer(32);
/* 1228 */     writer.sameFrame(pc);
/* 1229 */     ca.setAttribute(writer.toStackMapTable(cp));
/* 1230 */     return minfo;
/*      */   }
/*      */ 
/*      */   
/*      */   private MethodInfo makeDelegator(Method meth, String desc, ConstPool cp, Class declClass, String delegatorName) {
/* 1235 */     MethodInfo delegator = new MethodInfo(cp, delegatorName, desc);
/* 1236 */     delegator.setAccessFlags(0x11 | meth
/* 1237 */         .getModifiers() & 0xFFFFFAD9);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1242 */     setThrows(delegator, cp, meth);
/* 1243 */     Bytecode code = new Bytecode(cp, 0, 0);
/* 1244 */     code.addAload(0);
/* 1245 */     int s = addLoadParameters(code, meth.getParameterTypes(), 1);
/* 1246 */     Class targetClass = invokespecialTarget(declClass);
/* 1247 */     code.addInvokespecial(targetClass.isInterface(), cp.addClassInfo(targetClass.getName()), meth
/* 1248 */         .getName(), desc);
/* 1249 */     addReturn(code, meth.getReturnType());
/* 1250 */     code.setMaxLocals(++s);
/* 1251 */     delegator.setCodeAttribute(code.toCodeAttribute());
/* 1252 */     return delegator;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Class invokespecialTarget(Class declClass) {
/* 1261 */     if (declClass.isInterface())
/* 1262 */       for (Class<?> i : this.interfaces) {
/* 1263 */         if (declClass.isAssignableFrom(i))
/* 1264 */           return i; 
/*      */       }  
/* 1266 */     return this.superClass;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static MethodInfo makeForwarder(String thisClassName, Method meth, String desc, ConstPool cp, Class declClass, String delegatorName, int index, ArrayList<Find2MethodsArgs> forwarders) {
/* 1276 */     MethodInfo forwarder = new MethodInfo(cp, meth.getName(), desc);
/* 1277 */     forwarder.setAccessFlags(0x10 | meth
/* 1278 */         .getModifiers() & 0xFFFFFADF);
/*      */ 
/*      */     
/* 1281 */     setThrows(forwarder, cp, meth);
/* 1282 */     int args = Descriptor.paramSize(desc);
/* 1283 */     Bytecode code = new Bytecode(cp, 0, args + 2);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1296 */     int origIndex = index * 2;
/* 1297 */     int delIndex = index * 2 + 1;
/* 1298 */     int arrayVar = args + 1;
/* 1299 */     code.addGetstatic(thisClassName, "_methods_", "[Ljava/lang/reflect/Method;");
/* 1300 */     code.addAstore(arrayVar);
/*      */     
/* 1302 */     forwarders.add(new Find2MethodsArgs(meth.getName(), delegatorName, desc, origIndex));
/*      */     
/* 1304 */     code.addAload(0);
/* 1305 */     code.addGetfield(thisClassName, "handler", HANDLER_TYPE);
/* 1306 */     code.addAload(0);
/*      */     
/* 1308 */     code.addAload(arrayVar);
/* 1309 */     code.addIconst(origIndex);
/* 1310 */     code.addOpcode(50);
/*      */     
/* 1312 */     code.addAload(arrayVar);
/* 1313 */     code.addIconst(delIndex);
/* 1314 */     code.addOpcode(50);
/*      */     
/* 1316 */     makeParameterList(code, meth.getParameterTypes());
/* 1317 */     code.addInvokeinterface(MethodHandler.class.getName(), "invoke", "(Ljava/lang/Object;Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;[Ljava/lang/Object;)Ljava/lang/Object;", 5);
/*      */ 
/*      */     
/* 1320 */     Class<?> retType = meth.getReturnType();
/* 1321 */     addUnwrapper(code, retType);
/* 1322 */     addReturn(code, retType);
/*      */     
/* 1324 */     CodeAttribute ca = code.toCodeAttribute();
/* 1325 */     forwarder.setCodeAttribute(ca);
/* 1326 */     return forwarder;
/*      */   }
/*      */   
/*      */   static class Find2MethodsArgs {
/*      */     String methodName;
/*      */     String delegatorName;
/*      */     
/*      */     Find2MethodsArgs(String mname, String dname, String desc, int index) {
/* 1334 */       this.methodName = mname;
/* 1335 */       this.delegatorName = dname;
/* 1336 */       this.descriptor = desc;
/* 1337 */       this.origIndex = index;
/*      */     }
/*      */     String descriptor; int origIndex; }
/*      */   
/*      */   private static void setThrows(MethodInfo minfo, ConstPool cp, Method orig) {
/* 1342 */     Class[] exceptions = orig.getExceptionTypes();
/* 1343 */     setThrows(minfo, cp, exceptions);
/*      */   }
/*      */ 
/*      */   
/*      */   private static void setThrows(MethodInfo minfo, ConstPool cp, Class[] exceptions) {
/* 1348 */     if (exceptions.length == 0) {
/*      */       return;
/*      */     }
/* 1351 */     String[] list = new String[exceptions.length];
/* 1352 */     for (int i = 0; i < exceptions.length; i++) {
/* 1353 */       list[i] = exceptions[i].getName();
/*      */     }
/* 1355 */     ExceptionsAttribute ea = new ExceptionsAttribute(cp);
/* 1356 */     ea.setExceptions(list);
/* 1357 */     minfo.setExceptionsAttribute(ea);
/*      */   }
/*      */ 
/*      */   
/*      */   private static int addLoadParameters(Bytecode code, Class[] params, int offset) {
/* 1362 */     int stacksize = 0;
/* 1363 */     int n = params.length;
/* 1364 */     for (int i = 0; i < n; i++) {
/* 1365 */       stacksize += addLoad(code, stacksize + offset, params[i]);
/*      */     }
/* 1367 */     return stacksize;
/*      */   }
/*      */   
/*      */   private static int addLoad(Bytecode code, int n, Class<long> type) {
/* 1371 */     if (type.isPrimitive()) {
/* 1372 */       if (type == long.class) {
/* 1373 */         code.addLload(n);
/* 1374 */         return 2;
/*      */       } 
/* 1376 */       if (type == float.class)
/* 1377 */       { code.addFload(n); }
/* 1378 */       else { if (type == double.class) {
/* 1379 */           code.addDload(n);
/* 1380 */           return 2;
/*      */         } 
/*      */         
/* 1383 */         code.addIload(n); }
/*      */     
/*      */     } else {
/* 1386 */       code.addAload(n);
/*      */     } 
/* 1388 */     return 1;
/*      */   }
/*      */   
/*      */   private static int addReturn(Bytecode code, Class<long> type) {
/* 1392 */     if (type.isPrimitive()) {
/* 1393 */       if (type == long.class) {
/* 1394 */         code.addOpcode(173);
/* 1395 */         return 2;
/*      */       } 
/* 1397 */       if (type == float.class)
/* 1398 */       { code.addOpcode(174); }
/* 1399 */       else { if (type == double.class) {
/* 1400 */           code.addOpcode(175);
/* 1401 */           return 2;
/*      */         } 
/* 1403 */         if (type == void.class) {
/* 1404 */           code.addOpcode(177);
/* 1405 */           return 0;
/*      */         } 
/*      */         
/* 1408 */         code.addOpcode(172); }
/*      */     
/*      */     } else {
/* 1411 */       code.addOpcode(176);
/*      */     } 
/* 1413 */     return 1;
/*      */   }
/*      */   
/*      */   private static void makeParameterList(Bytecode code, Class[] params) {
/* 1417 */     int regno = 1;
/* 1418 */     int n = params.length;
/* 1419 */     code.addIconst(n);
/* 1420 */     code.addAnewarray("java/lang/Object");
/* 1421 */     for (int i = 0; i < n; i++) {
/* 1422 */       code.addOpcode(89);
/* 1423 */       code.addIconst(i);
/* 1424 */       Class type = params[i];
/* 1425 */       if (type.isPrimitive()) {
/* 1426 */         regno = makeWrapper(code, type, regno);
/*      */       } else {
/* 1428 */         code.addAload(regno);
/* 1429 */         regno++;
/*      */       } 
/*      */       
/* 1432 */       code.addOpcode(83);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static int makeWrapper(Bytecode code, Class type, int regno) {
/* 1437 */     int index = FactoryHelper.typeIndex(type);
/* 1438 */     String wrapper = FactoryHelper.wrapperTypes[index];
/* 1439 */     code.addNew(wrapper);
/* 1440 */     code.addOpcode(89);
/* 1441 */     addLoad(code, regno, type);
/* 1442 */     code.addInvokespecial(wrapper, "<init>", FactoryHelper.wrapperDesc[index]);
/*      */     
/* 1444 */     return regno + FactoryHelper.dataSize[index];
/*      */   }
/*      */   
/*      */   private static void addUnwrapper(Bytecode code, Class<void> type) {
/* 1448 */     if (type.isPrimitive()) {
/* 1449 */       if (type == void.class) {
/* 1450 */         code.addOpcode(87);
/*      */       } else {
/* 1452 */         int index = FactoryHelper.typeIndex(type);
/* 1453 */         String wrapper = FactoryHelper.wrapperTypes[index];
/* 1454 */         code.addCheckcast(wrapper);
/* 1455 */         code.addInvokevirtual(wrapper, FactoryHelper.unwarpMethods[index], FactoryHelper.unwrapDesc[index]);
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1461 */       code.addCheckcast(type.getName());
/*      */     } 
/*      */   }
/*      */   private static MethodInfo makeWriteReplace(ConstPool cp) {
/* 1465 */     MethodInfo minfo = new MethodInfo(cp, "writeReplace", "()Ljava/lang/Object;");
/* 1466 */     String[] list = new String[1];
/* 1467 */     list[0] = "java.io.ObjectStreamException";
/* 1468 */     ExceptionsAttribute ea = new ExceptionsAttribute(cp);
/* 1469 */     ea.setExceptions(list);
/* 1470 */     minfo.setExceptionsAttribute(ea);
/* 1471 */     Bytecode code = new Bytecode(cp, 0, 1);
/* 1472 */     code.addAload(0);
/* 1473 */     code.addInvokestatic("javassist.util.proxy.RuntimeSupport", "makeSerializedProxy", "(Ljava/lang/Object;)Ljavassist/util/proxy/SerializedProxy;");
/*      */ 
/*      */     
/* 1476 */     code.addOpcode(176);
/* 1477 */     minfo.setCodeAttribute(code.toCodeAttribute());
/* 1478 */     return minfo;
/*      */   }
/*      */   
/*      */   public static interface UniqueName {
/*      */     String get(String param1String);
/*      */   }
/*      */   
/*      */   public static interface ClassLoaderProvider {
/*      */     ClassLoader get(ProxyFactory param1ProxyFactory);
/*      */   }
/*      */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassis\\util\proxy\ProxyFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */