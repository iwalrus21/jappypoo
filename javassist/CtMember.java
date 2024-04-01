/*     */ package javassist;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class CtMember
/*     */ {
/*     */   CtMember next;
/*     */   protected CtClass declaringClass;
/*     */   
/*     */   static class Cache
/*     */     extends CtMember
/*     */   {
/*     */     private CtMember methodTail;
/*     */     private CtMember consTail;
/*     */     private CtMember fieldTail;
/*     */     
/*     */     protected void extendToString(StringBuffer buffer) {}
/*     */     
/*     */     public boolean hasAnnotation(String clz) {
/*  33 */       return false;
/*     */     } public Object getAnnotation(Class clz) throws ClassNotFoundException {
/*  35 */       return null;
/*     */     }
/*  37 */     public Object[] getAnnotations() throws ClassNotFoundException { return null; }
/*  38 */     public byte[] getAttribute(String name) { return null; }
/*  39 */     public Object[] getAvailableAnnotations() { return null; }
/*  40 */     public int getModifiers() { return 0; }
/*  41 */     public String getName() { return null; } public String getSignature() {
/*  42 */       return null;
/*     */     } public void setAttribute(String name, byte[] data) {}
/*     */     public String getGenericSignature() {
/*  45 */       return null;
/*     */     }
/*     */     
/*     */     public void setModifiers(int mod) {}
/*     */     
/*     */     public void setGenericSignature(String sig) {}
/*     */     
/*     */     Cache(CtClassType decl) {
/*  53 */       super(decl);
/*  54 */       this.methodTail = this;
/*  55 */       this.consTail = this;
/*  56 */       this.fieldTail = this;
/*  57 */       this.fieldTail.next = this;
/*     */     }
/*     */     
/*  60 */     CtMember methodHead() { return this; }
/*  61 */     CtMember lastMethod() { return this.methodTail; }
/*  62 */     CtMember consHead() { return this.methodTail; }
/*  63 */     CtMember lastCons() { return this.consTail; }
/*  64 */     CtMember fieldHead() { return this.consTail; } CtMember lastField() {
/*  65 */       return this.fieldTail;
/*     */     }
/*     */     void addMethod(CtMember method) {
/*  68 */       method.next = this.methodTail.next;
/*  69 */       this.methodTail.next = method;
/*  70 */       if (this.methodTail == this.consTail) {
/*  71 */         this.consTail = method;
/*  72 */         if (this.methodTail == this.fieldTail) {
/*  73 */           this.fieldTail = method;
/*     */         }
/*     */       } 
/*  76 */       this.methodTail = method;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void addConstructor(CtMember cons) {
/*  82 */       cons.next = this.consTail.next;
/*  83 */       this.consTail.next = cons;
/*  84 */       if (this.consTail == this.fieldTail) {
/*  85 */         this.fieldTail = cons;
/*     */       }
/*  87 */       this.consTail = cons;
/*     */     }
/*     */     
/*     */     void addField(CtMember field) {
/*  91 */       field.next = this;
/*  92 */       this.fieldTail.next = field;
/*  93 */       this.fieldTail = field;
/*     */     }
/*     */     
/*     */     static int count(CtMember head, CtMember tail) {
/*  97 */       int n = 0;
/*  98 */       while (head != tail) {
/*  99 */         n++;
/* 100 */         head = head.next;
/*     */       } 
/*     */       
/* 103 */       return n;
/*     */     }
/*     */     
/*     */     void remove(CtMember mem) {
/* 107 */       CtMember m = this;
/*     */       CtMember node;
/* 109 */       while ((node = m.next) != this) {
/* 110 */         if (node == mem) {
/* 111 */           m.next = node.next;
/* 112 */           if (node == this.methodTail) {
/* 113 */             this.methodTail = m;
/*     */           }
/* 115 */           if (node == this.consTail) {
/* 116 */             this.consTail = m;
/*     */           }
/* 118 */           if (node == this.fieldTail) {
/* 119 */             this.fieldTail = m;
/*     */           }
/*     */           
/*     */           break;
/*     */         } 
/* 124 */         m = m.next;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   protected CtMember(CtClass clazz) {
/* 130 */     this.declaringClass = clazz;
/* 131 */     this.next = null;
/*     */   }
/*     */   final CtMember next() {
/* 134 */     return this.next;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void nameReplaced() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 145 */     StringBuffer buffer = new StringBuffer(getClass().getName());
/* 146 */     buffer.append("@");
/* 147 */     buffer.append(Integer.toHexString(hashCode()));
/* 148 */     buffer.append("[");
/* 149 */     buffer.append(Modifier.toString(getModifiers()));
/* 150 */     extendToString(buffer);
/* 151 */     buffer.append("]");
/* 152 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void extendToString(StringBuffer paramStringBuffer);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtClass getDeclaringClass() {
/* 167 */     return this.declaringClass;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean visibleFrom(CtClass clazz) {
/*     */     boolean visible;
/* 173 */     int mod = getModifiers();
/* 174 */     if (Modifier.isPublic(mod))
/* 175 */       return true; 
/* 176 */     if (Modifier.isPrivate(mod)) {
/* 177 */       return (clazz == this.declaringClass);
/*     */     }
/* 179 */     String declName = this.declaringClass.getPackageName();
/* 180 */     String fromName = clazz.getPackageName();
/*     */     
/* 182 */     if (declName == null) {
/* 183 */       visible = (fromName == null);
/*     */     } else {
/* 185 */       visible = declName.equals(fromName);
/*     */     } 
/* 187 */     if (!visible && Modifier.isProtected(mod)) {
/* 188 */       return clazz.subclassOf(this.declaringClass);
/*     */     }
/* 190 */     return visible;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract int getModifiers();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void setModifiers(int paramInt);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasAnnotation(Class clz) {
/* 218 */     return hasAnnotation(clz.getName());
/*     */   }
/*     */   
/*     */   public abstract boolean hasAnnotation(String paramString);
/*     */   
/*     */   public abstract Object getAnnotation(Class paramClass) throws ClassNotFoundException;
/*     */   
/*     */   public abstract Object[] getAnnotations() throws ClassNotFoundException;
/*     */   
/*     */   public abstract Object[] getAvailableAnnotations();
/*     */   
/*     */   public abstract String getName();
/*     */   
/*     */   public abstract String getSignature();
/*     */   
/*     */   public abstract String getGenericSignature();
/*     */   
/*     */   public abstract void setGenericSignature(String paramString);
/*     */   
/*     */   public abstract byte[] getAttribute(String paramString);
/*     */   
/*     */   public abstract void setAttribute(String paramString, byte[] paramArrayOfbyte);
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\CtMember.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */