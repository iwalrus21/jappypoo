/*     */ package javassist;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URL;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class ClassPoolTail
/*     */ {
/* 183 */   protected ClassPathList pathList = null;
/*     */ 
/*     */   
/*     */   public String toString() {
/* 187 */     StringBuffer buf = new StringBuffer();
/* 188 */     buf.append("[class path: ");
/* 189 */     ClassPathList list = this.pathList;
/* 190 */     while (list != null) {
/* 191 */       buf.append(list.path.toString());
/* 192 */       buf.append(File.pathSeparatorChar);
/* 193 */       list = list.next;
/*     */     } 
/*     */     
/* 196 */     buf.append(']');
/* 197 */     return buf.toString();
/*     */   }
/*     */   
/*     */   public synchronized ClassPath insertClassPath(ClassPath cp) {
/* 201 */     this.pathList = new ClassPathList(cp, this.pathList);
/* 202 */     return cp;
/*     */   }
/*     */   
/*     */   public synchronized ClassPath appendClassPath(ClassPath cp) {
/* 206 */     ClassPathList tail = new ClassPathList(cp, null);
/* 207 */     ClassPathList list = this.pathList;
/* 208 */     if (list == null) {
/* 209 */       this.pathList = tail;
/*     */     } else {
/* 211 */       while (list.next != null) {
/* 212 */         list = list.next;
/*     */       }
/* 214 */       list.next = tail;
/*     */     } 
/*     */     
/* 217 */     return cp;
/*     */   }
/*     */   
/*     */   public synchronized void removeClassPath(ClassPath cp) {
/* 221 */     ClassPathList list = this.pathList;
/* 222 */     if (list != null)
/* 223 */       if (list.path == cp) {
/* 224 */         this.pathList = list.next;
/*     */       } else {
/* 226 */         while (list.next != null) {
/* 227 */           if (list.next.path == cp) {
/* 228 */             list.next = list.next.next; continue;
/*     */           } 
/* 230 */           list = list.next;
/*     */         } 
/*     */       }  
/* 233 */     cp.close();
/*     */   }
/*     */   
/*     */   public ClassPath appendSystemPath() {
/* 237 */     return appendClassPath(new ClassClassPath());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassPath insertClassPath(String pathname) throws NotFoundException {
/* 243 */     return insertClassPath(makePathObject(pathname));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassPath appendClassPath(String pathname) throws NotFoundException {
/* 249 */     return appendClassPath(makePathObject(pathname));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static ClassPath makePathObject(String pathname) throws NotFoundException {
/* 255 */     String lower = pathname.toLowerCase();
/* 256 */     if (lower.endsWith(".jar") || lower.endsWith(".zip")) {
/* 257 */       return new JarClassPath(pathname);
/*     */     }
/* 259 */     int len = pathname.length();
/* 260 */     if (len > 2 && pathname.charAt(len - 1) == '*' && (pathname
/* 261 */       .charAt(len - 2) == '/' || pathname
/* 262 */       .charAt(len - 2) == File.separatorChar)) {
/* 263 */       String dir = pathname.substring(0, len - 2);
/* 264 */       return new JarDirClassPath(dir);
/*     */     } 
/*     */     
/* 267 */     return new DirClassPath(pathname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void writeClassfile(String classname, OutputStream out) throws NotFoundException, IOException, CannotCompileException {
/* 276 */     InputStream fin = openClassfile(classname);
/* 277 */     if (fin == null) {
/* 278 */       throw new NotFoundException(classname);
/*     */     }
/*     */     try {
/* 281 */       copyStream(fin, out);
/*     */     } finally {
/*     */       
/* 284 */       fin.close();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   InputStream openClassfile(String classname) throws NotFoundException {
/* 318 */     ClassPathList list = this.pathList;
/* 319 */     InputStream ins = null;
/* 320 */     NotFoundException error = null;
/* 321 */     while (list != null) {
/*     */       try {
/* 323 */         ins = list.path.openClassfile(classname);
/*     */       }
/* 325 */       catch (NotFoundException e) {
/* 326 */         if (error == null) {
/* 327 */           error = e;
/*     */         }
/*     */       } 
/* 330 */       if (ins == null) {
/* 331 */         list = list.next; continue;
/*     */       } 
/* 333 */       return ins;
/*     */     } 
/*     */     
/* 336 */     if (error != null) {
/* 337 */       throw error;
/*     */     }
/* 339 */     return null;
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
/*     */   public URL find(String classname) {
/* 351 */     ClassPathList list = this.pathList;
/* 352 */     URL url = null;
/* 353 */     while (list != null) {
/* 354 */       url = list.path.find(classname);
/* 355 */       if (url == null) {
/* 356 */         list = list.next; continue;
/*     */       } 
/* 358 */       return url;
/*     */     } 
/*     */     
/* 361 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] readStream(InputStream fin) throws IOException {
/* 370 */     byte[][] bufs = new byte[8][];
/* 371 */     int bufsize = 4096;
/*     */     
/* 373 */     for (int i = 0; i < 8; i++) {
/* 374 */       bufs[i] = new byte[bufsize];
/* 375 */       int size = 0;
/* 376 */       int len = 0;
/*     */       while (true) {
/* 378 */         len = fin.read(bufs[i], size, bufsize - size);
/* 379 */         if (len >= 0) {
/* 380 */           size += len;
/*     */         } else {
/* 382 */           byte[] result = new byte[bufsize - 4096 + size];
/* 383 */           int s = 0;
/* 384 */           for (int j = 0; j < i; j++) {
/* 385 */             System.arraycopy(bufs[j], 0, result, s, s + 4096);
/* 386 */             s = s + s + 4096;
/*     */           } 
/*     */           
/* 389 */           System.arraycopy(bufs[i], 0, result, s, size);
/* 390 */           return result;
/*     */         } 
/* 392 */         if (size >= bufsize) {
/* 393 */           bufsize *= 2; break;
/*     */         } 
/*     */       } 
/* 396 */     }  throw new IOException("too much data");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void copyStream(InputStream fin, OutputStream fout) throws IOException {
/* 407 */     int bufsize = 4096;
/* 408 */     byte[] buf = null;
/* 409 */     for (int i = 0; i < 64; i++) {
/* 410 */       if (i < 8) {
/* 411 */         bufsize *= 2;
/* 412 */         buf = new byte[bufsize];
/*     */       } 
/* 414 */       int size = 0;
/* 415 */       int len = 0;
/*     */       while (true) {
/* 417 */         len = fin.read(buf, size, bufsize - size);
/* 418 */         if (len >= 0) {
/* 419 */           size += len;
/*     */         } else {
/* 421 */           fout.write(buf, 0, size);
/*     */           return;
/*     */         } 
/* 424 */         if (size >= bufsize) {
/* 425 */           fout.write(buf); break;
/*     */         } 
/*     */       } 
/* 428 */     }  throw new IOException("too much data");
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\ClassPoolTail.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */