/*     */ package javassist;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import javassist.bytecode.ClassFile;
/*     */ import javassist.bytecode.Descriptor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SerialVersionUID
/*     */ {
/*     */   public static void setSerialVersionUID(CtClass clazz) throws CannotCompileException, NotFoundException {
/*     */     try {
/*  43 */       clazz.getDeclaredField("serialVersionUID");
/*     */       
/*     */       return;
/*  46 */     } catch (NotFoundException notFoundException) {
/*     */ 
/*     */       
/*  49 */       if (!isSerializable(clazz)) {
/*     */         return;
/*     */       }
/*     */       
/*  53 */       CtField field = new CtField(CtClass.longType, "serialVersionUID", clazz);
/*     */       
/*  55 */       field.setModifiers(26);
/*     */       
/*  57 */       clazz.addField(field, calculateDefault(clazz) + "L");
/*     */       return;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isSerializable(CtClass clazz) throws NotFoundException {
/*  66 */     ClassPool pool = clazz.getClassPool();
/*  67 */     return clazz.subtypeOf(pool.get("java.io.Serializable"));
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
/*     */   public static long calculateDefault(CtClass clazz) throws CannotCompileException {
/*     */     try {
/*  80 */       ByteArrayOutputStream bout = new ByteArrayOutputStream();
/*  81 */       DataOutputStream out = new DataOutputStream(bout);
/*  82 */       ClassFile classFile = clazz.getClassFile();
/*     */ 
/*     */       
/*  85 */       String javaName = javaName(clazz);
/*  86 */       out.writeUTF(javaName);
/*     */       
/*  88 */       CtMethod[] methods = clazz.getDeclaredMethods();
/*     */ 
/*     */       
/*  91 */       int classMods = clazz.getModifiers();
/*  92 */       if ((classMods & 0x200) != 0)
/*  93 */         if (methods.length > 0) {
/*  94 */           classMods |= 0x400;
/*     */         } else {
/*  96 */           classMods &= 0xFFFFFBFF;
/*     */         }  
/*  98 */       out.writeInt(classMods);
/*     */ 
/*     */       
/* 101 */       String[] interfaces = classFile.getInterfaces(); int i;
/* 102 */       for (i = 0; i < interfaces.length; i++) {
/* 103 */         interfaces[i] = javaName(interfaces[i]);
/*     */       }
/* 105 */       Arrays.sort((Object[])interfaces);
/* 106 */       for (i = 0; i < interfaces.length; i++) {
/* 107 */         out.writeUTF(interfaces[i]);
/*     */       }
/*     */       
/* 110 */       CtField[] fields = clazz.getDeclaredFields();
/* 111 */       Arrays.sort(fields, new Comparator<CtField>() {
/*     */             public int compare(Object o1, Object o2) {
/* 113 */               CtField field1 = (CtField)o1;
/* 114 */               CtField field2 = (CtField)o2;
/* 115 */               return field1.getName().compareTo(field2.getName());
/*     */             }
/*     */           });
/*     */       
/* 119 */       for (int j = 0; j < fields.length; j++) {
/* 120 */         CtField field = fields[j];
/* 121 */         int mods = field.getModifiers();
/* 122 */         if ((mods & 0x2) == 0 || (mods & 0x88) == 0) {
/*     */           
/* 124 */           out.writeUTF(field.getName());
/* 125 */           out.writeInt(mods);
/* 126 */           out.writeUTF(field.getFieldInfo2().getDescriptor());
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 131 */       if (classFile.getStaticInitializer() != null) {
/* 132 */         out.writeUTF("<clinit>");
/* 133 */         out.writeInt(8);
/* 134 */         out.writeUTF("()V");
/*     */       } 
/*     */ 
/*     */       
/* 138 */       CtConstructor[] constructors = clazz.getDeclaredConstructors();
/* 139 */       Arrays.sort(constructors, new Comparator<CtConstructor>() {
/*     */             public int compare(Object o1, Object o2) {
/* 141 */               CtConstructor c1 = (CtConstructor)o1;
/* 142 */               CtConstructor c2 = (CtConstructor)o2;
/* 143 */               return c1.getMethodInfo2().getDescriptor().compareTo(c2
/* 144 */                   .getMethodInfo2().getDescriptor());
/*     */             }
/*     */           });
/*     */       int k;
/* 148 */       for (k = 0; k < constructors.length; k++) {
/* 149 */         CtConstructor constructor = constructors[k];
/* 150 */         int mods = constructor.getModifiers();
/* 151 */         if ((mods & 0x2) == 0) {
/* 152 */           out.writeUTF("<init>");
/* 153 */           out.writeInt(mods);
/* 154 */           out.writeUTF(constructor.getMethodInfo2()
/* 155 */               .getDescriptor().replace('/', '.'));
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 160 */       Arrays.sort(methods, new Comparator<CtMethod>() {
/*     */             public int compare(Object o1, Object o2) {
/* 162 */               CtMethod m1 = (CtMethod)o1;
/* 163 */               CtMethod m2 = (CtMethod)o2;
/* 164 */               int value = m1.getName().compareTo(m2.getName());
/* 165 */               if (value == 0)
/*     */               {
/* 167 */                 value = m1.getMethodInfo2().getDescriptor().compareTo(m2.getMethodInfo2().getDescriptor());
/*     */               }
/* 169 */               return value;
/*     */             }
/*     */           });
/*     */       
/* 173 */       for (k = 0; k < methods.length; k++) {
/* 174 */         CtMethod method = methods[k];
/* 175 */         int mods = method.getModifiers() & 0xD3F;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 180 */         if ((mods & 0x2) == 0) {
/* 181 */           out.writeUTF(method.getName());
/* 182 */           out.writeInt(mods);
/* 183 */           out.writeUTF(method.getMethodInfo2()
/* 184 */               .getDescriptor().replace('/', '.'));
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 189 */       out.flush();
/* 190 */       MessageDigest digest = MessageDigest.getInstance("SHA");
/* 191 */       byte[] digested = digest.digest(bout.toByteArray());
/* 192 */       long hash = 0L;
/* 193 */       for (int m = Math.min(digested.length, 8) - 1; m >= 0; m--) {
/* 194 */         hash = hash << 8L | (digested[m] & 0xFF);
/*     */       }
/* 196 */       return hash;
/*     */     }
/* 198 */     catch (IOException e) {
/* 199 */       throw new CannotCompileException(e);
/*     */     }
/* 201 */     catch (NoSuchAlgorithmException e) {
/* 202 */       throw new CannotCompileException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static String javaName(CtClass clazz) {
/* 207 */     return Descriptor.toJavaName(Descriptor.toJvmName(clazz));
/*     */   }
/*     */   
/*     */   private static String javaName(String name) {
/* 211 */     return Descriptor.toJavaName(Descriptor.toJvmName(name));
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\SerialVersionUID.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */