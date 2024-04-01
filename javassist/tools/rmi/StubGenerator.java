/*     */ package javassist.tools.rmi;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Hashtable;
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtClass;
/*     */ import javassist.CtConstructor;
/*     */ import javassist.CtField;
/*     */ import javassist.CtMethod;
/*     */ import javassist.CtNewConstructor;
/*     */ import javassist.CtNewMethod;
/*     */ import javassist.Modifier;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.Translator;
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
/*     */ public class StubGenerator
/*     */   implements Translator
/*     */ {
/*     */   private static final String fieldImporter = "importer";
/*     */   private static final String fieldObjectId = "objectId";
/*     */   private static final String accessorObjectId = "_getObjectId";
/*     */   private static final String sampleClass = "javassist.tools.rmi.Sample";
/*     */   private ClassPool classPool;
/*  62 */   private Hashtable proxyClasses = new Hashtable<Object, Object>();
/*     */   
/*     */   private CtMethod forwardMethod;
/*     */   
/*     */   private CtMethod forwardStaticMethod;
/*     */   private CtClass[] proxyConstructorParamTypes;
/*     */   private CtClass[] interfacesForProxy;
/*     */   private CtClass[] exceptionForProxy;
/*     */   
/*     */   public void start(ClassPool pool) throws NotFoundException {
/*  72 */     this.classPool = pool;
/*  73 */     CtClass c = pool.get("javassist.tools.rmi.Sample");
/*  74 */     this.forwardMethod = c.getDeclaredMethod("forward");
/*  75 */     this.forwardStaticMethod = c.getDeclaredMethod("forwardStatic");
/*     */     
/*  77 */     this
/*  78 */       .proxyConstructorParamTypes = pool.get(new String[] { "javassist.tools.rmi.ObjectImporter", "int" });
/*     */     
/*  80 */     this
/*  81 */       .interfacesForProxy = pool.get(new String[] { "java.io.Serializable", "javassist.tools.rmi.Proxy" });
/*     */     
/*  83 */     this
/*  84 */       .exceptionForProxy = new CtClass[] { pool.get("javassist.tools.rmi.RemoteException") };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onLoad(ClassPool pool, String classname) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isProxyClass(String name) {
/* 101 */     return (this.proxyClasses.get(name) != null);
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
/*     */   public synchronized boolean makeProxyClass(Class clazz) throws CannotCompileException, NotFoundException {
/* 116 */     String classname = clazz.getName();
/* 117 */     if (this.proxyClasses.get(classname) != null) {
/* 118 */       return false;
/*     */     }
/* 120 */     CtClass ctclazz = produceProxyClass(this.classPool.get(classname), clazz);
/*     */     
/* 122 */     this.proxyClasses.put(classname, ctclazz);
/* 123 */     modifySuperclass(ctclazz);
/* 124 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CtClass produceProxyClass(CtClass orgclass, Class orgRtClass) throws CannotCompileException, NotFoundException {
/* 131 */     int modify = orgclass.getModifiers();
/* 132 */     if (Modifier.isAbstract(modify) || Modifier.isNative(modify) || 
/* 133 */       !Modifier.isPublic(modify)) {
/* 134 */       throw new CannotCompileException(orgclass.getName() + " must be public, non-native, and non-abstract.");
/*     */     }
/*     */     
/* 137 */     CtClass proxy = this.classPool.makeClass(orgclass.getName(), orgclass
/* 138 */         .getSuperclass());
/*     */     
/* 140 */     proxy.setInterfaces(this.interfacesForProxy);
/*     */ 
/*     */     
/* 143 */     CtField f = new CtField(this.classPool.get("javassist.tools.rmi.ObjectImporter"), "importer", proxy);
/*     */     
/* 145 */     f.setModifiers(2);
/* 146 */     proxy.addField(f, CtField.Initializer.byParameter(0));
/*     */     
/* 148 */     f = new CtField(CtClass.intType, "objectId", proxy);
/* 149 */     f.setModifiers(2);
/* 150 */     proxy.addField(f, CtField.Initializer.byParameter(1));
/*     */     
/* 152 */     proxy.addMethod(CtNewMethod.getter("_getObjectId", f));
/*     */     
/* 154 */     proxy.addConstructor(CtNewConstructor.defaultConstructor(proxy));
/*     */     
/* 156 */     CtConstructor cons = CtNewConstructor.skeleton(this.proxyConstructorParamTypes, null, proxy);
/*     */     
/* 158 */     proxy.addConstructor(cons);
/*     */     
/*     */     try {
/* 161 */       addMethods(proxy, orgRtClass.getMethods());
/* 162 */       return proxy;
/*     */     }
/* 164 */     catch (SecurityException e) {
/* 165 */       throw new CannotCompileException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private CtClass toCtClass(Class<?> rtclass) throws NotFoundException {
/*     */     String name;
/* 171 */     if (!rtclass.isArray())
/* 172 */     { name = rtclass.getName(); }
/*     */     else
/* 174 */     { StringBuffer sbuf = new StringBuffer();
/*     */       while (true)
/* 176 */       { sbuf.append("[]");
/* 177 */         rtclass = rtclass.getComponentType();
/* 178 */         if (!rtclass.isArray())
/* 179 */         { sbuf.insert(0, rtclass.getName());
/* 180 */           name = sbuf.toString();
/*     */ 
/*     */           
/* 183 */           return this.classPool.get(name); }  }  }  return this.classPool.get(name);
/*     */   }
/*     */   
/*     */   private CtClass[] toCtClass(Class[] rtclasses) throws NotFoundException {
/* 187 */     int n = rtclasses.length;
/* 188 */     CtClass[] ctclasses = new CtClass[n];
/* 189 */     for (int i = 0; i < n; i++) {
/* 190 */       ctclasses[i] = toCtClass(rtclasses[i]);
/*     */     }
/* 192 */     return ctclasses;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addMethods(CtClass proxy, Method[] ms) throws CannotCompileException, NotFoundException {
/* 202 */     for (int i = 0; i < ms.length; i++) {
/* 203 */       Method m = ms[i];
/* 204 */       int mod = m.getModifiers();
/* 205 */       if (m.getDeclaringClass() != Object.class && 
/* 206 */         !Modifier.isFinal(mod)) {
/* 207 */         if (Modifier.isPublic(mod)) {
/*     */           CtMethod body;
/* 209 */           if (Modifier.isStatic(mod)) {
/* 210 */             body = this.forwardStaticMethod;
/*     */           } else {
/* 212 */             body = this.forwardMethod;
/*     */           } 
/*     */           
/* 215 */           CtMethod wmethod = CtNewMethod.wrapped(toCtClass(m.getReturnType()), m
/* 216 */               .getName(), 
/* 217 */               toCtClass(m.getParameterTypes()), this.exceptionForProxy, body, 
/*     */ 
/*     */               
/* 220 */               CtMethod.ConstParameter.integer(i), proxy);
/*     */           
/* 222 */           wmethod.setModifiers(mod);
/* 223 */           proxy.addMethod(wmethod);
/*     */         }
/* 225 */         else if (!Modifier.isProtected(mod) && 
/* 226 */           !Modifier.isPrivate(mod)) {
/*     */           
/* 228 */           throw new CannotCompileException("the methods must be public, protected, or private.");
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void modifySuperclass(CtClass orgclass) throws CannotCompileException, NotFoundException {
/* 240 */     for (;; orgclass = superclazz) {
/* 241 */       CtClass superclazz = orgclass.getSuperclass();
/* 242 */       if (superclazz == null) {
/*     */         break;
/*     */       }
/*     */       try {
/* 246 */         superclazz.getDeclaredConstructor(null);
/*     */         
/*     */         break;
/* 249 */       } catch (NotFoundException notFoundException) {
/*     */ 
/*     */         
/* 252 */         superclazz.addConstructor(
/* 253 */             CtNewConstructor.defaultConstructor(superclazz));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\tools\rmi\StubGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */