/*     */ package javassist;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
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
/*     */ public class Loader
/*     */   extends ClassLoader
/*     */ {
/*     */   private Hashtable notDefinedHere;
/*     */   private Vector notDefinedPackages;
/*     */   private ClassPool source;
/*     */   private Translator translator;
/*     */   private ProtectionDomain domain;
/*     */   public boolean doDelegation = true;
/*     */   
/*     */   public Loader() {
/* 160 */     this((ClassPool)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Loader(ClassPool cp) {
/* 169 */     init(cp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Loader(ClassLoader parent, ClassPool cp) {
/* 180 */     super(parent);
/* 181 */     init(cp);
/*     */   }
/*     */   
/*     */   private void init(ClassPool cp) {
/* 185 */     this.notDefinedHere = new Hashtable<Object, Object>();
/* 186 */     this.notDefinedPackages = new Vector();
/* 187 */     this.source = cp;
/* 188 */     this.translator = null;
/* 189 */     this.domain = null;
/* 190 */     delegateLoadingOf("javassist.Loader");
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
/*     */   public void delegateLoadingOf(String classname) {
/* 202 */     if (classname.endsWith(".")) {
/* 203 */       this.notDefinedPackages.addElement(classname);
/*     */     } else {
/* 205 */       this.notDefinedHere.put(classname, this);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDomain(ProtectionDomain d) {
/* 215 */     this.domain = d;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClassPool(ClassPool cp) {
/* 222 */     this.source = cp;
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
/*     */   public void addTranslator(ClassPool cp, Translator t) throws NotFoundException, CannotCompileException {
/* 236 */     this.source = cp;
/* 237 */     this.translator = t;
/* 238 */     t.start(cp);
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
/*     */   public static void main(String[] args) throws Throwable {
/* 255 */     Loader cl = new Loader();
/* 256 */     cl.run(args);
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
/*     */   public void run(String[] args) throws Throwable {
/* 269 */     int n = args.length - 1;
/* 270 */     if (n >= 0) {
/* 271 */       String[] args2 = new String[n];
/* 272 */       for (int i = 0; i < n; i++) {
/* 273 */         args2[i] = args[i + 1];
/*     */       }
/* 275 */       run(args[0], args2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run(String classname, String[] args) throws Throwable {
/* 286 */     Class<?> c = loadClass(classname);
/*     */     try {
/* 288 */       c.getDeclaredMethod("main", new Class[] { String[].class }).invoke(null, new Object[] { args });
/*     */ 
/*     */     
/*     */     }
/* 292 */     catch (InvocationTargetException e) {
/* 293 */       throw e.getTargetException();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class loadClass(String name, boolean resolve) throws ClassFormatError, ClassNotFoundException {
/* 302 */     name = name.intern();
/* 303 */     synchronized (name) {
/* 304 */       Class<?> c = findLoadedClass(name);
/* 305 */       if (c == null) {
/* 306 */         c = loadClassByDelegation(name);
/*     */       }
/* 308 */       if (c == null) {
/* 309 */         c = findClass(name);
/*     */       }
/* 311 */       if (c == null) {
/* 312 */         c = delegateToParent(name);
/*     */       }
/* 314 */       if (resolve) {
/* 315 */         resolveClass(c);
/*     */       }
/* 317 */       return c;
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
/*     */   
/*     */   protected Class findClass(String name) throws ClassNotFoundException {
/*     */     byte[] classfile;
/*     */     try {
/* 336 */       if (this.source != null) {
/* 337 */         if (this.translator != null) {
/* 338 */           this.translator.onLoad(this.source, name);
/*     */         }
/*     */         try {
/* 341 */           classfile = this.source.get(name).toBytecode();
/*     */         }
/* 343 */         catch (NotFoundException e) {
/* 344 */           return null;
/*     */         } 
/*     */       } else {
/*     */         
/* 348 */         String jarname = "/" + name.replace('.', '/') + ".class";
/* 349 */         InputStream in = getClass().getResourceAsStream(jarname);
/* 350 */         if (in == null) {
/* 351 */           return null;
/*     */         }
/* 353 */         classfile = ClassPoolTail.readStream(in);
/*     */       }
/*     */     
/* 356 */     } catch (Exception e) {
/* 357 */       throw new ClassNotFoundException("caught an exception while obtaining a class file for " + name, e);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 362 */     int i = name.lastIndexOf('.');
/* 363 */     if (i != -1) {
/* 364 */       String pname = name.substring(0, i);
/* 365 */       if (getPackage(pname) == null) {
/*     */         try {
/* 367 */           definePackage(pname, null, null, null, null, null, null, null);
/*     */         
/*     */         }
/* 370 */         catch (IllegalArgumentException illegalArgumentException) {}
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 376 */     if (this.domain == null) {
/* 377 */       return defineClass(name, classfile, 0, classfile.length);
/*     */     }
/* 379 */     return defineClass(name, classfile, 0, classfile.length, this.domain);
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
/*     */   protected Class loadClassByDelegation(String name) throws ClassNotFoundException {
/* 395 */     Class c = null;
/* 396 */     if (this.doDelegation && (
/* 397 */       name.startsWith("java.") || name
/* 398 */       .startsWith("javax.") || name
/* 399 */       .startsWith("sun.") || name
/* 400 */       .startsWith("com.sun.") || name
/* 401 */       .startsWith("org.w3c.") || name
/* 402 */       .startsWith("org.xml.") || 
/* 403 */       notDelegated(name))) {
/* 404 */       c = delegateToParent(name);
/*     */     }
/* 406 */     return c;
/*     */   }
/*     */   
/*     */   private boolean notDelegated(String name) {
/* 410 */     if (this.notDefinedHere.get(name) != null) {
/* 411 */       return true;
/*     */     }
/* 413 */     int n = this.notDefinedPackages.size();
/* 414 */     for (int i = 0; i < n; i++) {
/* 415 */       if (name.startsWith(this.notDefinedPackages.elementAt(i)))
/* 416 */         return true; 
/*     */     } 
/* 418 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class delegateToParent(String classname) throws ClassNotFoundException {
/* 424 */     ClassLoader cl = getParent();
/* 425 */     if (cl != null) {
/* 426 */       return cl.loadClass(classname);
/*     */     }
/* 428 */     return findSystemClass(classname);
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\Loader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */