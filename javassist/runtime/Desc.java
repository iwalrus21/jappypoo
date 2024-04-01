/*     */ package javassist.runtime;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Desc
/*     */ {
/*     */   public static boolean useContextClassLoader = false;
/*     */   
/*     */   private static Class getClassObject(String name) throws ClassNotFoundException {
/*  40 */     if (useContextClassLoader) {
/*  41 */       return Class.forName(name, true, Thread.currentThread().getContextClassLoader());
/*     */     }
/*  43 */     return Class.forName(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class getClazz(String name) {
/*     */     try {
/*  52 */       return getClassObject(name);
/*     */     }
/*  54 */     catch (ClassNotFoundException e) {
/*  55 */       throw new RuntimeException("$class: internal error, could not find class '" + name + "' (Desc.useContextClassLoader: " + 
/*     */ 
/*     */           
/*  58 */           Boolean.toString(useContextClassLoader) + ")", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class[] getParams(String desc) {
/*  67 */     if (desc.charAt(0) != '(') {
/*  68 */       throw new RuntimeException("$sig: internal error");
/*     */     }
/*  70 */     return getType(desc, desc.length(), 1, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class getType(String desc) {
/*  78 */     Class[] result = getType(desc, desc.length(), 0, 0);
/*  79 */     if (result == null || result.length != 1) {
/*  80 */       throw new RuntimeException("$type: internal error");
/*     */     }
/*  82 */     return result[0]; } private static Class[] getType(String desc, int descLen, int start, int num) { Class<boolean> clazz8; Class<char> clazz7; Class<byte> clazz6; Class<short> clazz5; Class<int> clazz4;
/*     */     Class<long> clazz3;
/*     */     Class<float> clazz2;
/*     */     Class<double> clazz1;
/*     */     Class<void> clazz;
/*     */     Class[] result;
/*  88 */     if (start >= descLen) {
/*  89 */       return new Class[num];
/*     */     }
/*  91 */     char c = desc.charAt(start);
/*  92 */     switch (c) {
/*     */       case 'Z':
/*  94 */         clazz8 = boolean.class;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 127 */         result = getType(desc, descLen, start + 1, num + 1);
/* 128 */         result[num] = clazz8;
/* 129 */         return result;case 'C': clazz7 = char.class; result = getType(desc, descLen, start + 1, num + 1); result[num] = clazz7; return result;case 'B': clazz6 = byte.class; result = getType(desc, descLen, start + 1, num + 1); result[num] = clazz6; return result;case 'S': clazz5 = short.class; result = getType(desc, descLen, start + 1, num + 1); result[num] = clazz5; return result;case 'I': clazz4 = int.class; result = getType(desc, descLen, start + 1, num + 1); result[num] = clazz4; return result;case 'J': clazz3 = long.class; result = getType(desc, descLen, start + 1, num + 1); result[num] = clazz3; return result;case 'F': clazz2 = float.class; result = getType(desc, descLen, start + 1, num + 1); result[num] = clazz2; return result;case 'D': clazz1 = double.class; result = getType(desc, descLen, start + 1, num + 1); result[num] = clazz1; return result;case 'V': clazz = void.class; result = getType(desc, descLen, start + 1, num + 1); result[num] = clazz; return result;
/*     */       case 'L':
/*     */       case '[':
/*     */         return getClassType(desc, descLen, start, num);
/*     */     }  return new Class[num]; } private static Class[] getClassType(String desc, int descLen, int start, int num) { String cname;
/* 134 */     int end = start;
/* 135 */     while (desc.charAt(end) == '[') {
/* 136 */       end++;
/*     */     }
/* 138 */     if (desc.charAt(end) == 'L') {
/* 139 */       end = desc.indexOf(';', end);
/* 140 */       if (end < 0) {
/* 141 */         throw new IndexOutOfBoundsException("bad descriptor");
/*     */       }
/*     */     } 
/*     */     
/* 145 */     if (desc.charAt(start) == 'L') {
/* 146 */       cname = desc.substring(start + 1, end);
/*     */     } else {
/* 148 */       cname = desc.substring(start, end + 1);
/*     */     } 
/* 150 */     Class[] result = getType(desc, descLen, end + 1, num + 1);
/*     */     try {
/* 152 */       result[num] = getClassObject(cname.replace('/', '.'));
/*     */     }
/* 154 */     catch (ClassNotFoundException e) {
/*     */       
/* 156 */       throw new RuntimeException(e.getMessage());
/*     */     } 
/*     */     
/* 159 */     return result; }
/*     */ 
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\runtime\Desc.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */