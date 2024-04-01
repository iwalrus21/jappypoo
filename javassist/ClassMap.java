/*     */ package javassist;
/*     */ 
/*     */ import java.util.HashMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassMap
/*     */   extends HashMap
/*     */ {
/*     */   private ClassMap parent;
/*     */   
/*     */   public ClassMap() {
/*  56 */     this.parent = null;
/*     */   } ClassMap(ClassMap map) {
/*  58 */     this.parent = map;
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
/*     */   public void put(CtClass oldname, CtClass newname) {
/*  71 */     put(oldname.getName(), newname.getName());
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
/*     */   public void put(String oldname, String newname) {
/*  93 */     if (oldname == newname) {
/*     */       return;
/*     */     }
/*  96 */     String oldname2 = toJvmName(oldname);
/*  97 */     String s = (String)get(oldname2);
/*  98 */     if (s == null || !s.equals(oldname2)) {
/*  99 */       put((K)oldname2, (V)toJvmName(newname));
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
/*     */   public void putIfNone(String oldname, String newname) {
/* 112 */     if (oldname == newname) {
/*     */       return;
/*     */     }
/* 115 */     String oldname2 = toJvmName(oldname);
/* 116 */     String s = (String)get(oldname2);
/* 117 */     if (s == null)
/* 118 */       put((K)oldname2, (V)toJvmName(newname)); 
/*     */   }
/*     */   
/*     */   protected final void put0(Object oldname, Object newname) {
/* 122 */     put((K)oldname, (V)newname);
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
/*     */   public Object get(Object jvmClassName) {
/* 136 */     Object found = super.get(jvmClassName);
/* 137 */     if (found == null && this.parent != null) {
/* 138 */       return this.parent.get(jvmClassName);
/*     */     }
/* 140 */     return found;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fix(CtClass clazz) {
/* 147 */     fix(clazz.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fix(String name) {
/* 154 */     String name2 = toJvmName(name);
/* 155 */     put((K)name2, (V)name2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toJvmName(String classname) {
/* 163 */     return Descriptor.toJvmName(classname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toJavaName(String classname) {
/* 171 */     return Descriptor.toJavaName(classname);
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\ClassMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */