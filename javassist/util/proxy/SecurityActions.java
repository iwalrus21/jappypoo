/*     */ package javassist.util.proxy;
/*     */ 
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class SecurityActions
/*     */ {
/*     */   static Method[] getDeclaredMethods(final Class clazz) {
/*  29 */     if (System.getSecurityManager() == null) {
/*  30 */       return clazz.getDeclaredMethods();
/*     */     }
/*     */     
/*  33 */     return AccessController.<Method[]>doPrivileged(new PrivilegedAction<Method>() {
/*     */           public Object run() {
/*  35 */             return clazz.getDeclaredMethods();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   static Constructor[] getDeclaredConstructors(final Class clazz) {
/*  42 */     if (System.getSecurityManager() == null) {
/*  43 */       return (Constructor[])clazz.getDeclaredConstructors();
/*     */     }
/*     */     
/*  46 */     return AccessController.<Constructor[]>doPrivileged(new PrivilegedAction<Constructor>() {
/*     */           public Object run() {
/*  48 */             return clazz.getDeclaredConstructors();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static Method getDeclaredMethod(final Class clazz, final String name, final Class[] types) throws NoSuchMethodException {
/*  56 */     if (System.getSecurityManager() == null) {
/*  57 */       return clazz.getDeclaredMethod(name, types);
/*     */     }
/*     */     
/*     */     try {
/*  61 */       return AccessController.<Method>doPrivileged(new PrivilegedExceptionAction<Method>() {
/*     */             public Object run() throws Exception {
/*  63 */               return clazz.getDeclaredMethod(name, types);
/*     */             }
/*     */           });
/*     */     }
/*  67 */     catch (PrivilegedActionException e) {
/*  68 */       if (e.getCause() instanceof NoSuchMethodException) {
/*  69 */         throw (NoSuchMethodException)e.getCause();
/*     */       }
/*  71 */       throw new RuntimeException(e.getCause());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Constructor getDeclaredConstructor(final Class clazz, final Class[] types) throws NoSuchMethodException {
/*  80 */     if (System.getSecurityManager() == null) {
/*  81 */       return clazz.getDeclaredConstructor(types);
/*     */     }
/*     */     
/*     */     try {
/*  85 */       return AccessController.<Constructor>doPrivileged(new PrivilegedExceptionAction<Constructor>() {
/*     */             public Object run() throws Exception {
/*  87 */               return clazz.getDeclaredConstructor(types);
/*     */             }
/*     */           });
/*     */     }
/*  91 */     catch (PrivilegedActionException e) {
/*  92 */       if (e.getCause() instanceof NoSuchMethodException) {
/*  93 */         throw (NoSuchMethodException)e.getCause();
/*     */       }
/*  95 */       throw new RuntimeException(e.getCause());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static void setAccessible(final AccessibleObject ao, final boolean accessible) {
/* 102 */     if (System.getSecurityManager() == null) {
/* 103 */       ao.setAccessible(accessible);
/*     */     } else {
/* 105 */       AccessController.doPrivileged(new PrivilegedAction() {
/*     */             public Object run() {
/* 107 */               ao.setAccessible(accessible);
/* 108 */               return null;
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static void set(final Field fld, final Object target, final Object value) throws IllegalAccessException {
/* 117 */     if (System.getSecurityManager() == null) {
/* 118 */       fld.set(target, value);
/*     */     } else {
/*     */       try {
/* 121 */         AccessController.doPrivileged(new PrivilegedExceptionAction() {
/*     */               public Object run() throws Exception {
/* 123 */                 fld.set(target, value);
/* 124 */                 return null;
/*     */               }
/*     */             });
/*     */       }
/* 128 */       catch (PrivilegedActionException e) {
/* 129 */         if (e.getCause() instanceof NoSuchMethodException) {
/* 130 */           throw (IllegalAccessException)e.getCause();
/*     */         }
/* 132 */         throw new RuntimeException(e.getCause());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassis\\util\proxy\SecurityActions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */