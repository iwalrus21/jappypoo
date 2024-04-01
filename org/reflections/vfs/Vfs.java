/*     */ package org.reflections.vfs;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.JarURLConnection;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.net.URLDecoder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.jar.JarFile;
/*     */ import javax.annotation.Nullable;
/*     */ import org.reflections.Reflections;
/*     */ import org.reflections.ReflectionsException;
/*     */ import org.reflections.util.ClasspathHelper;
/*     */ import org.reflections.util.Utils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Vfs
/*     */ {
/*  52 */   private static List<UrlType> defaultUrlTypes = Lists.newArrayList((Object[])DefaultUrlTypes.values());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<UrlType> getDefaultUrlTypes() {
/*  76 */     return defaultUrlTypes;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setDefaultURLTypes(List<UrlType> urlTypes) {
/*  81 */     defaultUrlTypes = urlTypes;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addDefaultURLTypes(UrlType urlType) {
/*  86 */     defaultUrlTypes.add(0, urlType);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Dir fromURL(URL url) {
/*  91 */     return fromURL(url, defaultUrlTypes);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Dir fromURL(URL url, List<UrlType> urlTypes) {
/*  96 */     for (UrlType type : urlTypes) {
/*     */       try {
/*  98 */         if (type.matches(url)) {
/*  99 */           Dir dir = type.createDir(url);
/* 100 */           if (dir != null) return dir; 
/*     */         } 
/* 102 */       } catch (Throwable e) {
/* 103 */         if (Reflections.log != null) {
/* 104 */           Reflections.log.warn("could not create Dir using " + type + " from url " + url.toExternalForm() + ". skipping.", e);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 109 */     throw new ReflectionsException("could not create Vfs.Dir from url, no matching UrlType was found [" + url.toExternalForm() + "]\neither use fromURL(final URL url, final List<UrlType> urlTypes) or use the static setDefaultURLTypes(final List<UrlType> urlTypes) or addDefaultURLTypes(UrlType urlType) with your specialized UrlType.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Dir fromURL(URL url, UrlType... urlTypes) {
/* 117 */     return fromURL(url, Lists.newArrayList((Object[])urlTypes));
/*     */   }
/*     */ 
/*     */   
/*     */   public static Iterable<File> findFiles(Collection<URL> inUrls, final String packagePrefix, final Predicate<String> nameFilter) {
/* 122 */     Predicate<File> fileNamePredicate = new Predicate<File>() {
/*     */         public boolean apply(Vfs.File file) {
/* 124 */           String path = file.getRelativePath();
/* 125 */           if (path.startsWith(packagePrefix)) {
/* 126 */             String filename = path.substring(path.indexOf(packagePrefix) + packagePrefix.length());
/* 127 */             return (!Utils.isEmpty(filename) && nameFilter.apply(filename.substring(1)));
/*     */           } 
/* 129 */           return false;
/*     */         }
/*     */       };
/*     */ 
/*     */     
/* 134 */     return findFiles(inUrls, fileNamePredicate);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Iterable<File> findFiles(Collection<URL> inUrls, Predicate<File> filePredicate) {
/* 139 */     Iterable<File> result = new ArrayList<>();
/*     */     
/* 141 */     for (URL url : inUrls) {
/*     */       try {
/* 143 */         result = Iterables.concat(result, 
/* 144 */             Iterables.filter(new Iterable<File>() {
/*     */                 public Iterator<Vfs.File> iterator() {
/* 146 */                   return Vfs.fromURL(url).getFiles().iterator();
/*     */                 }
/*     */               },  filePredicate));
/* 149 */       } catch (Throwable e) {
/* 150 */         if (Reflections.log != null) {
/* 151 */           Reflections.log.error("could not findFiles for url. continuing. [" + url + "]", e);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 156 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static java.io.File getFile(URL url) {
/*     */     try {
/* 165 */       String path = url.toURI().getSchemeSpecificPart(); java.io.File file;
/* 166 */       if ((file = new java.io.File(path)).exists()) return file; 
/* 167 */     } catch (URISyntaxException uRISyntaxException) {}
/*     */ 
/*     */     
/*     */     try {
/* 171 */       String path = URLDecoder.decode(url.getPath(), "UTF-8");
/* 172 */       if (path.contains(".jar!")) path = path.substring(0, path.lastIndexOf(".jar!") + ".jar".length());  java.io.File file;
/* 173 */       if ((file = new java.io.File(path)).exists()) return file;
/*     */     
/* 175 */     } catch (UnsupportedEncodingException unsupportedEncodingException) {}
/*     */ 
/*     */     
/*     */     try {
/* 179 */       String path = url.toExternalForm();
/* 180 */       if (path.startsWith("jar:")) path = path.substring("jar:".length()); 
/* 181 */       if (path.startsWith("wsjar:")) path = path.substring("wsjar:".length()); 
/* 182 */       if (path.startsWith("file:")) path = path.substring("file:".length()); 
/* 183 */       if (path.contains(".jar!")) path = path.substring(0, path.indexOf(".jar!") + ".jar".length());  java.io.File file;
/* 184 */       if ((file = new java.io.File(path)).exists()) return file;
/*     */       
/* 186 */       path = path.replace("%20", " ");
/* 187 */       if ((file = new java.io.File(path)).exists()) return file;
/*     */     
/* 189 */     } catch (Exception exception) {}
/*     */ 
/*     */     
/* 192 */     return null;
/*     */   } public static interface Dir {
/*     */     String getPath(); Iterable<Vfs.File> getFiles(); void close(); } public static interface File {
/*     */     String getName(); String getRelativePath(); InputStream openInputStream() throws IOException; } private static boolean hasJarFileInPath(URL url) {
/* 196 */     return url.toExternalForm().matches(".*\\.jar(\\!.*|$)");
/*     */   }
/*     */ 
/*     */   
/*     */   public static interface UrlType
/*     */   {
/*     */     boolean matches(URL param1URL) throws Exception;
/*     */     
/*     */     Vfs.Dir createDir(URL param1URL) throws Exception;
/*     */   }
/*     */   
/*     */   public enum DefaultUrlTypes
/*     */     implements UrlType
/*     */   {
/* 210 */     jarFile {
/*     */       public boolean matches(URL url) {
/* 212 */         return (url.getProtocol().equals("file") && Vfs.hasJarFileInPath(url));
/*     */       }
/*     */       
/*     */       public Vfs.Dir createDir(URL url) throws Exception {
/* 216 */         return new ZipDir(new JarFile(Vfs.getFile(url)));
/*     */       }
/*     */     },
/*     */     
/* 220 */     jarUrl {
/*     */       public boolean matches(URL url) {
/* 222 */         return ("jar".equals(url.getProtocol()) || "zip".equals(url.getProtocol()) || "wsjar".equals(url.getProtocol()));
/*     */       }
/*     */       
/*     */       public Vfs.Dir createDir(URL url) throws Exception {
/*     */         try {
/* 227 */           URLConnection urlConnection = url.openConnection();
/* 228 */           if (urlConnection instanceof JarURLConnection) {
/* 229 */             return new ZipDir(((JarURLConnection)urlConnection).getJarFile());
/*     */           }
/* 231 */         } catch (Throwable throwable) {}
/* 232 */         java.io.File file = Vfs.getFile(url);
/* 233 */         if (file != null) {
/* 234 */           return new ZipDir(new JarFile(file));
/*     */         }
/* 236 */         return null;
/*     */       }
/*     */     },
/*     */     
/* 240 */     directory {
/*     */       public boolean matches(URL url) {
/* 242 */         if (url.getProtocol().equals("file") && !Vfs.hasJarFileInPath(url)) {
/* 243 */           java.io.File file = Vfs.getFile(url);
/* 244 */           return (file != null && file.isDirectory());
/* 245 */         }  return false;
/*     */       }
/*     */       
/*     */       public Vfs.Dir createDir(URL url) throws Exception {
/* 249 */         return new SystemDir(Vfs.getFile(url));
/*     */       }
/*     */     },
/*     */     
/* 253 */     jboss_vfs {
/*     */       public boolean matches(URL url) {
/* 255 */         return url.getProtocol().equals("vfs");
/*     */       }
/*     */       
/*     */       public Vfs.Dir createDir(URL url) throws Exception {
/* 259 */         Object content = url.openConnection().getContent();
/* 260 */         Class<?> virtualFile = ClasspathHelper.contextClassLoader().loadClass("org.jboss.vfs.VirtualFile");
/* 261 */         java.io.File physicalFile = (java.io.File)virtualFile.getMethod("getPhysicalFile", new Class[0]).invoke(content, new Object[0]);
/* 262 */         String name = (String)virtualFile.getMethod("getName", new Class[0]).invoke(content, new Object[0]);
/* 263 */         java.io.File file = new java.io.File(physicalFile.getParentFile(), name);
/* 264 */         if (!file.exists() || !file.canRead()) file = physicalFile; 
/* 265 */         return file.isDirectory() ? new SystemDir(file) : new ZipDir(new JarFile(file));
/*     */       }
/*     */     },
/*     */     
/* 269 */     jboss_vfsfile {
/*     */       public boolean matches(URL url) throws Exception {
/* 271 */         return ("vfszip".equals(url.getProtocol()) || "vfsfile".equals(url.getProtocol()));
/*     */       }
/*     */       
/*     */       public Vfs.Dir createDir(URL url) throws Exception {
/* 275 */         return (new UrlTypeVFS()).createDir(url);
/*     */       }
/*     */     },
/*     */     
/* 279 */     bundle {
/*     */       public boolean matches(URL url) throws Exception {
/* 281 */         return url.getProtocol().startsWith("bundle");
/*     */       }
/*     */       
/*     */       public Vfs.Dir createDir(URL url) throws Exception {
/* 285 */         return Vfs.fromURL((URL)ClasspathHelper.contextClassLoader()
/* 286 */             .loadClass("org.eclipse.core.runtime.FileLocator").getMethod("resolve", new Class[] { URL.class }).invoke(null, new Object[] { url
/*     */               }));
/*     */       }
/*     */     },
/* 290 */     jarInputStream {
/*     */       public boolean matches(URL url) throws Exception {
/* 292 */         return url.toExternalForm().contains(".jar");
/*     */       }
/*     */       
/*     */       public Vfs.Dir createDir(URL url) throws Exception {
/* 296 */         return new JarInputDir(url);
/*     */       }
/*     */     };
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\reflections\vfs\Vfs.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */