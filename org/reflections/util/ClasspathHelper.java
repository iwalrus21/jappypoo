/*     */ package org.reflections.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.net.URLDecoder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Enumeration;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.Manifest;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.reflections.Reflections;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ClasspathHelper
/*     */ {
/*     */   public static ClassLoader contextClassLoader() {
/*  30 */     return Thread.currentThread().getContextClassLoader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ClassLoader staticClassLoader() {
/*  40 */     return Reflections.class.getClassLoader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ClassLoader[] classLoaders(ClassLoader... classLoaders) {
/*  51 */     if (classLoaders != null && classLoaders.length != 0) {
/*  52 */       return classLoaders;
/*     */     }
/*  54 */     ClassLoader contextClassLoader = contextClassLoader(), staticClassLoader = staticClassLoader();
/*  55 */     (new ClassLoader[2])[0] = contextClassLoader; (new ClassLoader[2])[1] = staticClassLoader; (new ClassLoader[1])[0] = contextClassLoader; return (contextClassLoader != null) ? ((staticClassLoader != null && contextClassLoader != staticClassLoader) ? new ClassLoader[2] : new ClassLoader[1]) : new ClassLoader[0];
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
/*     */   public static Collection<URL> forPackage(String name, ClassLoader... classLoaders) {
/*  79 */     return forResource(resourceName(name), classLoaders);
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
/*     */   public static Collection<URL> forResource(String resourceName, ClassLoader... classLoaders) {
/*  97 */     List<URL> result = new ArrayList<>();
/*  98 */     ClassLoader[] loaders = classLoaders(classLoaders);
/*  99 */     for (ClassLoader classLoader : loaders) {
/*     */       try {
/* 101 */         Enumeration<URL> urls = classLoader.getResources(resourceName);
/* 102 */         while (urls.hasMoreElements()) {
/* 103 */           URL url = urls.nextElement();
/* 104 */           int index = url.toExternalForm().lastIndexOf(resourceName);
/* 105 */           if (index != -1) {
/*     */             
/* 107 */             result.add(new URL(url, url.toExternalForm().substring(0, index))); continue;
/*     */           } 
/* 109 */           result.add(url);
/*     */         }
/*     */       
/* 112 */       } catch (IOException e) {
/* 113 */         if (Reflections.log != null) {
/* 114 */           Reflections.log.error("error getting resources for " + resourceName, e);
/*     */         }
/*     */       } 
/*     */     } 
/* 118 */     return distinctUrls(result);
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
/*     */   public static URL forClass(Class<?> aClass, ClassLoader... classLoaders) {
/* 132 */     ClassLoader[] loaders = classLoaders(classLoaders);
/* 133 */     String resourceName = aClass.getName().replace(".", "/") + ".class";
/* 134 */     for (ClassLoader classLoader : loaders) {
/*     */       try {
/* 136 */         URL url = classLoader.getResource(resourceName);
/* 137 */         if (url != null) {
/* 138 */           String normalizedUrl = url.toExternalForm().substring(0, url.toExternalForm().lastIndexOf(aClass.getPackage().getName().replace(".", "/")));
/* 139 */           return new URL(normalizedUrl);
/*     */         } 
/* 141 */       } catch (MalformedURLException e) {
/* 142 */         if (Reflections.log != null) {
/* 143 */           Reflections.log.warn("Could not get URL", e);
/*     */         }
/*     */       } 
/*     */     } 
/* 147 */     return null;
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
/*     */   public static Collection<URL> forClassLoader() {
/* 161 */     return forClassLoader(classLoaders(new ClassLoader[0]));
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
/*     */   public static Collection<URL> forClassLoader(ClassLoader... classLoaders) {
/* 178 */     Collection<URL> result = new ArrayList<>();
/* 179 */     ClassLoader[] loaders = classLoaders(classLoaders);
/* 180 */     for (ClassLoader classLoader : loaders) {
/* 181 */       while (classLoader != null) {
/* 182 */         if (classLoader instanceof URLClassLoader) {
/* 183 */           URL[] urls = ((URLClassLoader)classLoader).getURLs();
/* 184 */           if (urls != null) {
/* 185 */             result.addAll(Arrays.asList(urls));
/*     */           }
/*     */         } 
/* 188 */         classLoader = classLoader.getParent();
/*     */       } 
/*     */     } 
/* 191 */     return distinctUrls(result);
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
/*     */   public static Collection<URL> forJavaClassPath() {
/* 204 */     Collection<URL> urls = new ArrayList<>();
/* 205 */     String javaClassPath = System.getProperty("java.class.path");
/* 206 */     if (javaClassPath != null) {
/* 207 */       for (String path : javaClassPath.split(File.pathSeparator)) {
/*     */         try {
/* 209 */           urls.add((new File(path)).toURI().toURL());
/* 210 */         } catch (Exception e) {
/* 211 */           if (Reflections.log != null) {
/* 212 */             Reflections.log.warn("Could not get URL", e);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/* 217 */     return distinctUrls(urls);
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
/*     */   public static Collection<URL> forWebInfLib(ServletContext servletContext) {
/* 230 */     Collection<URL> urls = new ArrayList<>();
/* 231 */     Set<?> resourcePaths = servletContext.getResourcePaths("/WEB-INF/lib");
/* 232 */     if (resourcePaths == null) {
/* 233 */       return urls;
/*     */     }
/* 235 */     for (Object urlString : resourcePaths) {
/*     */       try {
/* 237 */         urls.add(servletContext.getResource((String)urlString));
/* 238 */       } catch (MalformedURLException malformedURLException) {}
/*     */     } 
/* 240 */     return distinctUrls(urls);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static URL forWebInfClasses(ServletContext servletContext) {
/*     */     try {
/* 252 */       String path = servletContext.getRealPath("/WEB-INF/classes");
/* 253 */       if (path != null) {
/* 254 */         File file = new File(path);
/* 255 */         if (file.exists())
/* 256 */           return file.toURL(); 
/*     */       } else {
/* 258 */         return servletContext.getResource("/WEB-INF/classes");
/*     */       } 
/* 260 */     } catch (MalformedURLException malformedURLException) {}
/* 261 */     return null;
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
/*     */   public static Collection<URL> forManifest() {
/* 275 */     return forManifest(forClassLoader());
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
/*     */   public static Collection<URL> forManifest(URL url) {
/* 289 */     Collection<URL> result = new ArrayList<>();
/* 290 */     result.add(url);
/*     */     try {
/* 292 */       String part = cleanPath(url);
/* 293 */       File jarFile = new File(part);
/* 294 */       JarFile myJar = new JarFile(part);
/* 295 */       URL validUrl = tryToGetValidUrl(jarFile.getPath(), (new File(part)).getParent(), part);
/* 296 */       if (validUrl != null) result.add(validUrl); 
/* 297 */       Manifest manifest = myJar.getManifest();
/* 298 */       if (manifest != null) {
/* 299 */         String classPath = manifest.getMainAttributes().getValue(new Attributes.Name("Class-Path"));
/* 300 */         if (classPath != null)
/* 301 */           for (String jar : classPath.split(" ")) {
/* 302 */             validUrl = tryToGetValidUrl(jarFile.getPath(), (new File(part)).getParent(), jar);
/* 303 */             if (validUrl != null) result.add(validUrl);
/*     */           
/*     */           }  
/*     */       } 
/* 307 */     } catch (IOException iOException) {}
/*     */ 
/*     */     
/* 310 */     return distinctUrls(result);
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
/*     */   public static Collection<URL> forManifest(Iterable<URL> urls) {
/* 326 */     Collection<URL> result = new ArrayList<>();
/*     */     
/* 328 */     for (URL url : urls) {
/* 329 */       result.addAll(forManifest(url));
/*     */     }
/* 331 */     return distinctUrls(result);
/*     */   }
/*     */ 
/*     */   
/*     */   static URL tryToGetValidUrl(String workingDir, String path, String filename) {
/*     */     try {
/* 337 */       if ((new File(filename)).exists())
/* 338 */         return (new File(filename)).toURI().toURL(); 
/* 339 */       if ((new File(path + File.separator + filename)).exists())
/* 340 */         return (new File(path + File.separator + filename)).toURI().toURL(); 
/* 341 */       if ((new File(workingDir + File.separator + filename)).exists())
/* 342 */         return (new File(workingDir + File.separator + filename)).toURI().toURL(); 
/* 343 */       if ((new File((new URL(filename)).getFile())).exists())
/* 344 */         return (new File((new URL(filename)).getFile())).toURI().toURL(); 
/* 345 */     } catch (MalformedURLException malformedURLException) {}
/*     */ 
/*     */     
/* 348 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String cleanPath(URL url) {
/* 358 */     String path = url.getPath();
/*     */     try {
/* 360 */       path = URLDecoder.decode(path, "UTF-8");
/* 361 */     } catch (UnsupportedEncodingException unsupportedEncodingException) {}
/* 362 */     if (path.startsWith("jar:")) {
/* 363 */       path = path.substring("jar:".length());
/*     */     }
/* 365 */     if (path.startsWith("file:")) {
/* 366 */       path = path.substring("file:".length());
/*     */     }
/* 368 */     if (path.endsWith("!/")) {
/* 369 */       path = path.substring(0, path.lastIndexOf("!/")) + "/";
/*     */     }
/* 371 */     return path;
/*     */   }
/*     */   
/*     */   private static String resourceName(String name) {
/* 375 */     if (name != null) {
/* 376 */       String resourceName = name.replace(".", "/");
/* 377 */       resourceName = resourceName.replace("\\", "/");
/* 378 */       if (resourceName.startsWith("/")) {
/* 379 */         resourceName = resourceName.substring(1);
/*     */       }
/* 381 */       return resourceName;
/*     */     } 
/* 383 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Collection<URL> distinctUrls(Collection<URL> urls) {
/* 388 */     Map<String, URL> distinct = new LinkedHashMap<>(urls.size());
/* 389 */     for (URL url : urls) {
/* 390 */       distinct.put(url.toExternalForm(), url);
/*     */     }
/* 392 */     return distinct.values();
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\reflection\\util\ClasspathHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */