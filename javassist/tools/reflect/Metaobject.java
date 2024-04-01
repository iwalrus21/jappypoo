/*     */ package javassist.tools.reflect;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Metaobject
/*     */   implements Serializable
/*     */ {
/*     */   protected ClassMetaobject classmetaobject;
/*     */   protected Metalevel baseobject;
/*     */   protected Method[] methods;
/*     */   
/*     */   public Metaobject(Object self, Object[] args) {
/*  62 */     this.baseobject = (Metalevel)self;
/*  63 */     this.classmetaobject = this.baseobject._getClass();
/*  64 */     this.methods = this.classmetaobject.getReflectiveMethods();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Metaobject() {
/*  73 */     this.baseobject = null;
/*  74 */     this.classmetaobject = null;
/*  75 */     this.methods = null;
/*     */   }
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/*  79 */     out.writeObject(this.baseobject);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/*  85 */     this.baseobject = (Metalevel)in.readObject();
/*  86 */     this.classmetaobject = this.baseobject._getClass();
/*  87 */     this.methods = this.classmetaobject.getReflectiveMethods();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ClassMetaobject getClassMetaobject() {
/*  96 */     return this.classmetaobject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Object getObject() {
/* 103 */     return this.baseobject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setObject(Object self) {
/* 112 */     this.baseobject = (Metalevel)self;
/* 113 */     this.classmetaobject = this.baseobject._getClass();
/* 114 */     this.methods = this.classmetaobject.getReflectiveMethods();
/*     */ 
/*     */     
/* 117 */     this.baseobject._setMetaobject(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getMethodName(int identifier) {
/*     */     char c;
/* 125 */     String mname = this.methods[identifier].getName();
/* 126 */     int j = 3;
/*     */     do {
/* 128 */       c = mname.charAt(j++);
/* 129 */     } while (c >= '0' && '9' >= c);
/*     */ 
/*     */ 
/*     */     
/* 133 */     return mname.substring(j);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Class[] getParameterTypes(int identifier) {
/* 142 */     return this.methods[identifier].getParameterTypes();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Class getReturnType(int identifier) {
/* 150 */     return this.methods[identifier].getReturnType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object trapFieldRead(String name) {
/* 161 */     Class jc = getClassMetaobject().getJavaClass();
/*     */     try {
/* 163 */       return jc.getField(name).get(getObject());
/*     */     }
/* 165 */     catch (NoSuchFieldException e) {
/* 166 */       throw new RuntimeException(e.toString());
/*     */     }
/* 168 */     catch (IllegalAccessException e) {
/* 169 */       throw new RuntimeException(e.toString());
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
/*     */   public void trapFieldWrite(String name, Object value) {
/* 181 */     Class jc = getClassMetaobject().getJavaClass();
/*     */     try {
/* 183 */       jc.getField(name).set(getObject(), value);
/*     */     }
/* 185 */     catch (NoSuchFieldException e) {
/* 186 */       throw new RuntimeException(e.toString());
/*     */     }
/* 188 */     catch (IllegalAccessException e) {
/* 189 */       throw new RuntimeException(e.toString());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object trapMethodcall(int identifier, Object[] args) throws Throwable {
/*     */     try {
/* 230 */       return this.methods[identifier].invoke(getObject(), args);
/*     */     }
/* 232 */     catch (InvocationTargetException e) {
/* 233 */       throw e.getTargetException();
/*     */     }
/* 235 */     catch (IllegalAccessException e) {
/* 236 */       throw new CannotInvokeException(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\tools\reflect\Metaobject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */