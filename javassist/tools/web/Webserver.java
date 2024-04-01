/*     */ package javassist.tools.web;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.util.Date;
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtClass;
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
/*     */ public class Webserver
/*     */ {
/*     */   private ServerSocket socket;
/*     */   private ClassPool classPool;
/*     */   protected Translator translator;
/*  42 */   private static final byte[] endofline = new byte[] { 13, 10 };
/*     */ 
/*     */   
/*     */   private static final int typeHtml = 1;
/*     */ 
/*     */   
/*     */   private static final int typeClass = 2;
/*     */   
/*     */   private static final int typeGif = 3;
/*     */   
/*     */   private static final int typeJpeg = 4;
/*     */   
/*     */   private static final int typeText = 5;
/*     */   
/*  56 */   public String debugDir = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   public String htmlfileBase = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) throws IOException {
/*  79 */     if (args.length == 1) {
/*  80 */       Webserver web = new Webserver(args[0]);
/*  81 */       web.run();
/*     */     } else {
/*     */       
/*  84 */       System.err.println("Usage: java javassist.tools.web.Webserver <port number>");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Webserver(String port) throws IOException {
/*  94 */     this(Integer.parseInt(port));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Webserver(int port) throws IOException {
/* 103 */     this.socket = new ServerSocket(port);
/* 104 */     this.classPool = null;
/* 105 */     this.translator = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClassPool(ClassPool loader) {
/* 113 */     this.classPool = loader;
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
/* 127 */     this.classPool = cp;
/* 128 */     this.translator = t;
/* 129 */     t.start(this.classPool);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void end() throws IOException {
/* 136 */     this.socket.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void logging(String msg) {
/* 143 */     System.out.println(msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void logging(String msg1, String msg2) {
/* 150 */     System.out.print(msg1);
/* 151 */     System.out.print(" ");
/* 152 */     System.out.println(msg2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void logging(String msg1, String msg2, String msg3) {
/* 159 */     System.out.print(msg1);
/* 160 */     System.out.print(" ");
/* 161 */     System.out.print(msg2);
/* 162 */     System.out.print(" ");
/* 163 */     System.out.println(msg3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void logging2(String msg) {
/* 170 */     System.out.print("    ");
/* 171 */     System.out.println(msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 178 */     System.err.println("ready to service..."); while (true) {
/*     */       try {
/*     */         while (true) {
/* 181 */           ServiceThread th = new ServiceThread(this, this.socket.accept());
/* 182 */           th.start();
/*     */         }  break;
/* 184 */       } catch (IOException e) {
/* 185 */         logging(e.toString());
/*     */       } 
/*     */     } 
/*     */   }
/*     */   final void process(Socket clnt) throws IOException {
/* 190 */     InputStream in = new BufferedInputStream(clnt.getInputStream());
/* 191 */     String cmd = readLine(in);
/* 192 */     logging(clnt.getInetAddress().getHostName(), (new Date())
/* 193 */         .toString(), cmd);
/* 194 */     while (skipLine(in) > 0);
/*     */ 
/*     */     
/* 197 */     OutputStream out = new BufferedOutputStream(clnt.getOutputStream());
/*     */     try {
/* 199 */       doReply(in, out, cmd);
/*     */     }
/* 201 */     catch (BadHttpRequest e) {
/* 202 */       replyError(out, e);
/*     */     } 
/*     */     
/* 205 */     out.flush();
/* 206 */     in.close();
/* 207 */     out.close();
/* 208 */     clnt.close();
/*     */   }
/*     */   
/*     */   private String readLine(InputStream in) throws IOException {
/* 212 */     StringBuffer buf = new StringBuffer();
/*     */     int c;
/* 214 */     while ((c = in.read()) >= 0 && c != 13) {
/* 215 */       buf.append((char)c);
/*     */     }
/* 217 */     in.read();
/* 218 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private int skipLine(InputStream in) throws IOException {
/* 223 */     int len = 0; int c;
/* 224 */     while ((c = in.read()) >= 0 && c != 13) {
/* 225 */       len++;
/*     */     }
/* 227 */     in.read();
/* 228 */     return len;
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
/*     */   public void doReply(InputStream in, OutputStream out, String cmd) throws IOException, BadHttpRequest {
/*     */     int fileType;
/*     */     String filename, urlName;
/* 244 */     if (cmd.startsWith("GET /")) {
/* 245 */       filename = urlName = cmd.substring(5, cmd.indexOf(' ', 5));
/*     */     } else {
/* 247 */       throw new BadHttpRequest();
/*     */     } 
/* 249 */     if (filename.endsWith(".class")) {
/* 250 */       fileType = 2;
/* 251 */     } else if (filename.endsWith(".html") || filename.endsWith(".htm")) {
/* 252 */       fileType = 1;
/* 253 */     } else if (filename.endsWith(".gif")) {
/* 254 */       fileType = 3;
/* 255 */     } else if (filename.endsWith(".jpg")) {
/* 256 */       fileType = 4;
/*     */     } else {
/* 258 */       fileType = 5;
/*     */     } 
/* 260 */     int len = filename.length();
/* 261 */     if (fileType == 2 && 
/* 262 */       letUsersSendClassfile(out, filename, len)) {
/*     */       return;
/*     */     }
/* 265 */     checkFilename(filename, len);
/* 266 */     if (this.htmlfileBase != null) {
/* 267 */       filename = this.htmlfileBase + filename;
/*     */     }
/* 269 */     if (File.separatorChar != '/') {
/* 270 */       filename = filename.replace('/', File.separatorChar);
/*     */     }
/* 272 */     File file = new File(filename);
/* 273 */     if (file.canRead()) {
/* 274 */       sendHeader(out, file.length(), fileType);
/* 275 */       FileInputStream fin = new FileInputStream(file);
/* 276 */       byte[] filebuffer = new byte[4096];
/*     */       while (true) {
/* 278 */         len = fin.read(filebuffer);
/* 279 */         if (len <= 0) {
/*     */           break;
/*     */         }
/* 282 */         out.write(filebuffer, 0, len);
/*     */       } 
/*     */       
/* 285 */       fin.close();
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */     
/* 292 */     if (fileType == 2) {
/*     */       
/* 294 */       InputStream fin = getClass().getResourceAsStream("/" + urlName);
/* 295 */       if (fin != null) {
/* 296 */         ByteArrayOutputStream barray = new ByteArrayOutputStream();
/* 297 */         byte[] filebuffer = new byte[4096];
/*     */         while (true) {
/* 299 */           len = fin.read(filebuffer);
/* 300 */           if (len <= 0) {
/*     */             break;
/*     */           }
/* 303 */           barray.write(filebuffer, 0, len);
/*     */         } 
/*     */         
/* 306 */         byte[] classfile = barray.toByteArray();
/* 307 */         sendHeader(out, classfile.length, 2);
/* 308 */         out.write(classfile);
/* 309 */         fin.close();
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 314 */     throw new BadHttpRequest();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkFilename(String filename, int len) throws BadHttpRequest {
/* 320 */     for (int i = 0; i < len; i++) {
/* 321 */       char c = filename.charAt(i);
/* 322 */       if (!Character.isJavaIdentifierPart(c) && c != '.' && c != '/') {
/* 323 */         throw new BadHttpRequest();
/*     */       }
/*     */     } 
/* 326 */     if (filename.indexOf("..") >= 0) {
/* 327 */       throw new BadHttpRequest();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean letUsersSendClassfile(OutputStream out, String filename, int length) throws IOException, BadHttpRequest {
/*     */     byte[] classfile;
/* 334 */     if (this.classPool == null) {
/* 335 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 339 */     String classname = filename.substring(0, length - 6).replace('/', '.');
/*     */     try {
/* 341 */       if (this.translator != null) {
/* 342 */         this.translator.onLoad(this.classPool, classname);
/*     */       }
/* 344 */       CtClass c = this.classPool.get(classname);
/* 345 */       classfile = c.toBytecode();
/* 346 */       if (this.debugDir != null) {
/* 347 */         c.writeFile(this.debugDir);
/*     */       }
/* 349 */     } catch (Exception e) {
/* 350 */       throw new BadHttpRequest(e);
/*     */     } 
/*     */     
/* 353 */     sendHeader(out, classfile.length, 2);
/* 354 */     out.write(classfile);
/* 355 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void sendHeader(OutputStream out, long dataLength, int filetype) throws IOException {
/* 361 */     out.write("HTTP/1.0 200 OK".getBytes());
/* 362 */     out.write(endofline);
/* 363 */     out.write("Content-Length: ".getBytes());
/* 364 */     out.write(Long.toString(dataLength).getBytes());
/* 365 */     out.write(endofline);
/* 366 */     if (filetype == 2) {
/* 367 */       out.write("Content-Type: application/octet-stream".getBytes());
/* 368 */     } else if (filetype == 1) {
/* 369 */       out.write("Content-Type: text/html".getBytes());
/* 370 */     } else if (filetype == 3) {
/* 371 */       out.write("Content-Type: image/gif".getBytes());
/* 372 */     } else if (filetype == 4) {
/* 373 */       out.write("Content-Type: image/jpg".getBytes());
/* 374 */     } else if (filetype == 5) {
/* 375 */       out.write("Content-Type: text/plain".getBytes());
/*     */     } 
/* 377 */     out.write(endofline);
/* 378 */     out.write(endofline);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void replyError(OutputStream out, BadHttpRequest e) throws IOException {
/* 384 */     logging2("bad request: " + e.toString());
/* 385 */     out.write("HTTP/1.0 400 Bad Request".getBytes());
/* 386 */     out.write(endofline);
/* 387 */     out.write(endofline);
/* 388 */     out.write("<H1>Bad Request</H1>".getBytes());
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\tools\web\Webserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */