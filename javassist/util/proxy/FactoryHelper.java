/*     */ package javassist.util.proxy;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.ProtectionDomain;
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.bytecode.ClassFile;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FactoryHelper
/*     */ {
/*     */   private static Method defineClass1;
/*     */   private static Method defineClass2;
/*     */   
/*     */   static {
/*     */     try {
/*  42 */       Class<?> cl = Class.forName("java.lang.ClassLoader");
/*  43 */       defineClass1 = SecurityActions.getDeclaredMethod(cl, "defineClass", new Class[] { String.class, byte[].class, int.class, int.class });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  49 */       defineClass2 = SecurityActions.getDeclaredMethod(cl, "defineClass", new Class[] { String.class, byte[].class, int.class, int.class, ProtectionDomain.class });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*  55 */     catch (Exception e) {
/*  56 */       throw new RuntimeException("cannot initialize");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int typeIndex(Class type) {
/*  66 */     Class[] list = primitiveTypes;
/*  67 */     int n = list.length;
/*  68 */     for (int i = 0; i < n; i++) {
/*  69 */       if (list[i] == type)
/*  70 */         return i; 
/*     */     } 
/*  72 */     throw new RuntimeException("bad type:" + type.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   public static final Class[] primitiveTypes = new Class[] { boolean.class, byte.class, char.class, short.class, int.class, long.class, float.class, double.class, void.class };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  86 */   public static final String[] wrapperTypes = new String[] { "java.lang.Boolean", "java.lang.Byte", "java.lang.Character", "java.lang.Short", "java.lang.Integer", "java.lang.Long", "java.lang.Float", "java.lang.Double", "java.lang.Void" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   public static final String[] wrapperDesc = new String[] { "(Z)V", "(B)V", "(C)V", "(S)V", "(I)V", "(J)V", "(F)V", "(D)V" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   public static final String[] unwarpMethods = new String[] { "booleanValue", "byteValue", "charValue", "shortValue", "intValue", "longValue", "floatValue", "doubleValue" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 115 */   public static final String[] unwrapDesc = new String[] { "()Z", "()B", "()C", "()S", "()I", "()J", "()F", "()D" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 123 */   public static final int[] dataSize = new int[] { 1, 1, 1, 1, 1, 2, 1, 2 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class toClass(ClassFile cf, ClassLoader loader) throws CannotCompileException {
/* 137 */     return toClass(cf, loader, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class toClass(ClassFile cf, ClassLoader loader, ProtectionDomain domain) throws CannotCompileException {
/*     */     try {
/*     */       Method method;
/*     */       Object[] args;
/* 150 */       byte[] b = toBytecode(cf);
/*     */ 
/*     */       
/* 153 */       if (domain == null) {
/* 154 */         method = defineClass1;
/* 155 */         args = new Object[] { cf.getName(), b, new Integer(0), new Integer(b.length) };
/*     */       }
/*     */       else {
/*     */         
/* 159 */         method = defineClass2;
/* 160 */         args = new Object[] { cf.getName(), b, new Integer(0), new Integer(b.length), domain };
/*     */       } 
/*     */ 
/*     */       
/* 164 */       return toClass2(method, loader, args);
/*     */     }
/* 166 */     catch (RuntimeException e) {
/* 167 */       throw e;
/*     */     }
/* 169 */     catch (InvocationTargetException e) {
/* 170 */       throw new CannotCompileException(e.getTargetException());
/*     */     }
/* 172 */     catch (Exception e) {
/* 173 */       throw new CannotCompileException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static synchronized Class toClass2(Method method, ClassLoader loader, Object[] args) throws Exception {
/* 181 */     SecurityActions.setAccessible(method, true);
/* 182 */     Class clazz = (Class)method.invoke(loader, args);
/* 183 */     SecurityActions.setAccessible(method, false);
/* 184 */     return clazz;
/*     */   }
/*     */   
/*     */   private static byte[] toBytecode(ClassFile cf) throws IOException {
/* 188 */     ByteArrayOutputStream barray = new ByteArrayOutputStream();
/* 189 */     DataOutputStream out = new DataOutputStream(barray);
/*     */     try {
/* 191 */       cf.write(out);
/*     */     } finally {
/*     */       
/* 194 */       out.close();
/*     */     } 
/*     */     
/* 197 */     return barray.toByteArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeFile(ClassFile cf, String directoryName) throws CannotCompileException {
/*     */     try {
/* 206 */       writeFile0(cf, directoryName);
/*     */     }
/* 208 */     catch (IOException e) {
/* 209 */       throw new CannotCompileException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void writeFile0(ClassFile cf, String directoryName) throws CannotCompileException, IOException {
/* 215 */     String classname = cf.getName();
/*     */     
/* 217 */     String filename = directoryName + File.separatorChar + classname.replace('.', File.separatorChar) + ".class";
/* 218 */     int pos = filename.lastIndexOf(File.separatorChar);
/* 219 */     if (pos > 0) {
/* 220 */       String dir = filename.substring(0, pos);
/* 221 */       if (!dir.equals(".")) {
/* 222 */         (new File(dir)).mkdirs();
/*     */       }
/*     */     } 
/* 225 */     DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(filename)));
/*     */     
/*     */     try {
/* 228 */       cf.write(out);
/*     */     }
/* 230 */     catch (IOException e) {
/* 231 */       throw e;
/*     */     } finally {
/*     */       
/* 234 */       out.close();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassis\\util\proxy\FactoryHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */