/*     */ package javassist;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class URLClassPath
/*     */   implements ClassPath
/*     */ {
/*     */   protected String hostname;
/*     */   protected int port;
/*     */   protected String directory;
/*     */   protected String packageName;
/*     */   
/*     */   public URLClassPath(String host, int port, String directory, String packageName) {
/*  62 */     this.hostname = host;
/*  63 */     this.port = port;
/*  64 */     this.directory = directory;
/*  65 */     this.packageName = packageName;
/*     */   }
/*     */   
/*     */   public String toString() {
/*  69 */     return this.hostname + ":" + this.port + this.directory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream openClassfile(String classname) {
/*     */     try {
/*  79 */       URLConnection con = openClassfile0(classname);
/*  80 */       if (con != null) {
/*  81 */         return con.getInputStream();
/*     */       }
/*  83 */     } catch (IOException iOException) {}
/*  84 */     return null;
/*     */   }
/*     */   
/*     */   private URLConnection openClassfile0(String classname) throws IOException {
/*  88 */     if (this.packageName == null || classname.startsWith(this.packageName)) {
/*     */       
/*  90 */       String jarname = this.directory + classname.replace('.', '/') + ".class";
/*  91 */       return fetchClass0(this.hostname, this.port, jarname);
/*     */     } 
/*     */     
/*  94 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL find(String classname) {
/*     */     try {
/* 104 */       URLConnection con = openClassfile0(classname);
/* 105 */       InputStream is = con.getInputStream();
/* 106 */       if (is != null) {
/* 107 */         is.close();
/* 108 */         return con.getURL();
/*     */       }
/*     */     
/* 111 */     } catch (IOException iOException) {}
/* 112 */     return null;
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
/*     */   public void close() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] fetchClass(String host, int port, String directory, String classname) throws IOException {
/*     */     byte[] b;
/* 135 */     URLConnection con = fetchClass0(host, port, directory + classname
/* 136 */         .replace('.', '/') + ".class");
/* 137 */     int size = con.getContentLength();
/* 138 */     InputStream s = con.getInputStream();
/*     */     try {
/* 140 */       if (size <= 0) {
/* 141 */         b = ClassPoolTail.readStream(s);
/*     */       } else {
/* 143 */         b = new byte[size];
/* 144 */         int len = 0;
/*     */         do {
/* 146 */           int n = s.read(b, len, size - len);
/* 147 */           if (n < 0) {
/* 148 */             throw new IOException("the stream was closed: " + classname);
/*     */           }
/*     */           
/* 151 */           len += n;
/* 152 */         } while (len < size);
/*     */       } 
/*     */     } finally {
/*     */       
/* 156 */       s.close();
/*     */     } 
/*     */     
/* 159 */     return b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static URLConnection fetchClass0(String host, int port, String filename) throws IOException {
/*     */     URL url;
/*     */     try {
/* 168 */       url = new URL("http", host, port, filename);
/*     */     }
/* 170 */     catch (MalformedURLException e) {
/*     */       
/* 172 */       throw new IOException("invalid URL?");
/*     */     } 
/*     */     
/* 175 */     URLConnection con = url.openConnection();
/* 176 */     con.connect();
/* 177 */     return con;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\URLClassPath.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */