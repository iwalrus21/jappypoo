/*     */ package org.reflections.scanners;
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtBehavior;
/*     */ import javassist.CtClass;
/*     */ import javassist.CtMethod;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.bytecode.MethodInfo;
/*     */ import javassist.expr.ConstructorCall;
/*     */ import javassist.expr.ExprEditor;
/*     */ import javassist.expr.FieldAccess;
/*     */ import javassist.expr.MethodCall;
/*     */ import javassist.expr.NewExpr;
/*     */ import org.reflections.ReflectionsException;
/*     */ 
/*     */ public class MemberUsageScanner extends AbstractScanner {
/*     */   public void scan(Object cls) {
/*     */     try {
/*  19 */       CtClass ctClass = getClassPool().get(getMetadataAdapter().getClassName(cls));
/*  20 */       for (CtConstructor ctConstructor : ctClass.getDeclaredConstructors()) {
/*  21 */         scanMember((CtBehavior)ctConstructor);
/*     */       }
/*  23 */       for (CtMethod ctMethod : ctClass.getDeclaredMethods()) {
/*  24 */         scanMember((CtBehavior)ctMethod);
/*     */       }
/*  26 */       ctClass.detach();
/*  27 */     } catch (Exception e) {
/*  28 */       throw new ReflectionsException("Could not scan method usage for " + getMetadataAdapter().getClassName(cls), e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private ClassPool classPool;
/*     */   
/*     */   void scanMember(CtBehavior member) throws CannotCompileException {
/*  35 */     final String key = member.getDeclaringClass().getName() + "." + member.getMethodInfo().getName() + "(" + parameterNames(member.getMethodInfo()) + ")";
/*  36 */     member.instrument(new ExprEditor()
/*     */         {
/*     */           public void edit(NewExpr e) throws CannotCompileException {
/*     */             try {
/*  40 */               MemberUsageScanner.this.put(e.getConstructor().getDeclaringClass().getName() + ".<init>(" + MemberUsageScanner.this
/*  41 */                   .parameterNames(e.getConstructor().getMethodInfo()) + ")", e.getLineNumber(), key);
/*  42 */             } catch (NotFoundException e1) {
/*  43 */               throw new ReflectionsException("Could not find new instance usage in " + key, e1);
/*     */             } 
/*     */           }
/*     */ 
/*     */           
/*     */           public void edit(MethodCall m) throws CannotCompileException {
/*     */             try {
/*  50 */               MemberUsageScanner.this.put(m.getMethod().getDeclaringClass().getName() + "." + m.getMethodName() + "(" + MemberUsageScanner.this
/*  51 */                   .parameterNames(m.getMethod().getMethodInfo()) + ")", m.getLineNumber(), key);
/*  52 */             } catch (NotFoundException e) {
/*  53 */               throw new ReflectionsException("Could not find member " + m.getClassName() + " in " + key, e);
/*     */             } 
/*     */           }
/*     */ 
/*     */           
/*     */           public void edit(ConstructorCall c) throws CannotCompileException {
/*     */             try {
/*  60 */               MemberUsageScanner.this.put(c.getConstructor().getDeclaringClass().getName() + ".<init>(" + MemberUsageScanner.this
/*  61 */                   .parameterNames(c.getConstructor().getMethodInfo()) + ")", c.getLineNumber(), key);
/*  62 */             } catch (NotFoundException e) {
/*  63 */               throw new ReflectionsException("Could not find member " + c.getClassName() + " in " + key, e);
/*     */             } 
/*     */           }
/*     */ 
/*     */           
/*     */           public void edit(FieldAccess f) throws CannotCompileException {
/*     */             try {
/*  70 */               MemberUsageScanner.this.put(f.getField().getDeclaringClass().getName() + "." + f.getFieldName(), f.getLineNumber(), key);
/*  71 */             } catch (NotFoundException e) {
/*  72 */               throw new ReflectionsException("Could not find member " + f.getFieldName() + " in " + key, e);
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private void put(String key, int lineNumber, String value) {
/*  79 */     if (acceptResult(key)) {
/*  80 */       getStore().put(key, value + " #" + lineNumber);
/*     */     }
/*     */   }
/*     */   
/*     */   String parameterNames(MethodInfo info) {
/*  85 */     return Joiner.on(", ").join(getMetadataAdapter().getParameterNames(info));
/*     */   }
/*     */   
/*     */   private ClassPool getClassPool() {
/*  89 */     if (this.classPool == null) {
/*  90 */       synchronized (this) {
/*  91 */         this.classPool = new ClassPool();
/*  92 */         ClassLoader[] classLoaders = getConfiguration().getClassLoaders();
/*  93 */         if (classLoaders == null) {
/*  94 */           classLoaders = ClasspathHelper.classLoaders(new ClassLoader[0]);
/*     */         }
/*  96 */         for (ClassLoader classLoader : classLoaders) {
/*  97 */           this.classPool.appendClassPath((ClassPath)new LoaderClassPath(classLoader));
/*     */         }
/*     */       } 
/*     */     }
/* 101 */     return this.classPool;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\reflections\scanners\MemberUsageScanner.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */