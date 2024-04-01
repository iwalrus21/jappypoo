/*     */ package javassist.tools.web;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.InvocationTargetException;
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
/*     */ public class Viewer
/*     */   extends ClassLoader
/*     */ {
/*     */   private String server;
/*     */   private int port;
/*     */   
/*     */   public static void main(String[] args) throws Throwable {
/*  59 */     if (args.length >= 3) {
/*  60 */       Viewer cl = new Viewer(args[0], Integer.parseInt(args[1]));
/*  61 */       String[] args2 = new String[args.length - 3];
/*  62 */       System.arraycopy(args, 3, args2, 0, args.length - 3);
/*  63 */       cl.run(args[2], args2);
/*     */     } else {
/*     */       
/*  66 */       System.err.println("Usage: java javassist.tools.web.Viewer <host> <port> class [args ...]");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Viewer(String host, int p) {
/*  77 */     this.server = host;
/*  78 */     this.port = p;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getServer() {
/*  84 */     return this.server;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPort() {
/*  89 */     return this.port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run(String classname, String[] args) throws Throwable {
/* 100 */     Class<?> c = loadClass(classname);
/*     */     try {
/* 102 */       c.getDeclaredMethod("main", new Class[] { String[].class
/* 103 */           }).invoke(null, new Object[] { args });
/*     */     }
/* 105 */     catch (InvocationTargetException e) {
/* 106 */       throw e.getTargetException();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
/* 116 */     Class<?> c = findLoadedClass(name);
/* 117 */     if (c == null) {
/* 118 */       c = findClass(name);
/*     */     }
/* 120 */     if (c == null) {
/* 121 */       throw new ClassNotFoundException(name);
/*     */     }
/* 123 */     if (resolve) {
/* 124 */       resolveClass(c);
/*     */     }
/* 126 */     return c;
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
/*     */   protected Class findClass(String name) throws ClassNotFoundException {
/* 140 */     Class<?> c = null;
/* 141 */     if (name.startsWith("java.") || name.startsWith("javax.") || name
/* 142 */       .equals("javassist.tools.web.Viewer")) {
/* 143 */       c = findSystemClass(name);
/*     */     }
/* 145 */     if (c == null) {
/*     */       try {
/* 147 */         byte[] b = fetchClass(name);
/* 148 */         if (b != null) {
/* 149 */           c = defineClass(name, b, 0, b.length);
/*     */         }
/* 151 */       } catch (Exception exception) {}
/*     */     }
/*     */     
/* 154 */     return c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected byte[] fetchClass(String classname) throws Exception {
/*     */     byte[] b;
/* 165 */     URL url = new URL("http", this.server, this.port, "/" + classname.replace('.', '/') + ".class");
/* 166 */     URLConnection con = url.openConnection();
/* 167 */     con.connect();
/* 168 */     int size = con.getContentLength();
/* 169 */     InputStream s = con.getInputStream();
/* 170 */     if (size <= 0) {
/* 171 */       b = readStream(s);
/*     */     } else {
/* 173 */       b = new byte[size];
/* 174 */       int len = 0;
/*     */       do {
/* 176 */         int n = s.read(b, len, size - len);
/* 177 */         if (n < 0) {
/* 178 */           s.close();
/* 179 */           throw new IOException("the stream was closed: " + classname);
/*     */         } 
/*     */         
/* 182 */         len += n;
/* 183 */       } while (len < size);
/*     */     } 
/*     */     
/* 186 */     s.close();
/* 187 */     return b;
/*     */   }
/*     */   
/*     */   private byte[] readStream(InputStream fin) throws IOException {
/* 191 */     byte[] buf = new byte[4096];
/* 192 */     int size = 0;
/* 193 */     int len = 0;
/*     */     do {
/* 195 */       size += len;
/* 196 */       if (buf.length - size <= 0) {
/* 197 */         byte[] newbuf = new byte[buf.length * 2];
/* 198 */         System.arraycopy(buf, 0, newbuf, 0, size);
/* 199 */         buf = newbuf;
/*     */       } 
/*     */       
/* 202 */       len = fin.read(buf, size, buf.length - size);
/* 203 */     } while (len >= 0);
/*     */     
/* 205 */     byte[] result = new byte[size];
/* 206 */     System.arraycopy(buf, 0, result, 0, size);
/* 207 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\tools\web\Viewer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */