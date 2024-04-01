/*     */ package javassist.convert;
/*     */ 
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtClass;
/*     */ import javassist.CtMethod;
/*     */ import javassist.Modifier;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.CodeAttribute;
/*     */ import javassist.bytecode.CodeIterator;
/*     */ import javassist.bytecode.ConstPool;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TransformCall
/*     */   extends Transformer
/*     */ {
/*     */   protected String classname;
/*     */   protected String methodname;
/*     */   protected String methodDescriptor;
/*     */   protected String newClassname;
/*     */   protected String newMethodname;
/*     */   protected boolean newMethodIsPrivate;
/*     */   protected int newIndex;
/*     */   protected ConstPool constPool;
/*     */   
/*     */   public TransformCall(Transformer next, CtMethod origMethod, CtMethod substMethod) {
/*  38 */     this(next, origMethod.getName(), substMethod);
/*  39 */     this.classname = origMethod.getDeclaringClass().getName();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TransformCall(Transformer next, String oldMethodName, CtMethod substMethod) {
/*  45 */     super(next);
/*  46 */     this.methodname = oldMethodName;
/*  47 */     this.methodDescriptor = substMethod.getMethodInfo2().getDescriptor();
/*  48 */     this.classname = this.newClassname = substMethod.getDeclaringClass().getName();
/*  49 */     this.newMethodname = substMethod.getName();
/*  50 */     this.constPool = null;
/*  51 */     this.newMethodIsPrivate = Modifier.isPrivate(substMethod.getModifiers());
/*     */   }
/*     */   
/*     */   public void initialize(ConstPool cp, CodeAttribute attr) {
/*  55 */     if (this.constPool != cp) {
/*  56 */       this.newIndex = 0;
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
/*     */   public int transform(CtClass clazz, int pos, CodeIterator iterator, ConstPool cp) throws BadBytecode {
/*  69 */     int c = iterator.byteAt(pos);
/*  70 */     if (c == 185 || c == 183 || c == 184 || c == 182) {
/*     */       
/*  72 */       int index = iterator.u16bitAt(pos + 1);
/*  73 */       String cname = cp.eqMember(this.methodname, this.methodDescriptor, index);
/*  74 */       if (cname != null && matchClass(cname, clazz.getClassPool())) {
/*  75 */         int ntinfo = cp.getMemberNameAndType(index);
/*  76 */         pos = match(c, pos, iterator, cp
/*  77 */             .getNameAndTypeDescriptor(ntinfo), cp);
/*     */       } 
/*     */     } 
/*     */     
/*  81 */     return pos;
/*     */   }
/*     */   
/*     */   private boolean matchClass(String name, ClassPool pool) {
/*  85 */     if (this.classname.equals(name)) {
/*  86 */       return true;
/*     */     }
/*     */     try {
/*  89 */       CtClass clazz = pool.get(name);
/*  90 */       CtClass declClazz = pool.get(this.classname);
/*  91 */       if (clazz.subtypeOf(declClazz)) {
/*     */         try {
/*  93 */           CtMethod m = clazz.getMethod(this.methodname, this.methodDescriptor);
/*  94 */           return m.getDeclaringClass().getName().equals(this.classname);
/*     */         }
/*  96 */         catch (NotFoundException e) {
/*     */           
/*  98 */           return true;
/*     */         } 
/*     */       }
/* 101 */     } catch (NotFoundException e) {
/* 102 */       return false;
/*     */     } 
/*     */     
/* 105 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int match(int c, int pos, CodeIterator iterator, int typedesc, ConstPool cp) throws BadBytecode {
/* 111 */     if (this.newIndex == 0) {
/* 112 */       int nt = cp.addNameAndTypeInfo(cp.addUtf8Info(this.newMethodname), typedesc);
/*     */       
/* 114 */       int ci = cp.addClassInfo(this.newClassname);
/* 115 */       if (c == 185) {
/* 116 */         this.newIndex = cp.addInterfaceMethodrefInfo(ci, nt);
/*     */       } else {
/* 118 */         if (this.newMethodIsPrivate && c == 182) {
/* 119 */           iterator.writeByte(183, pos);
/*     */         }
/* 121 */         this.newIndex = cp.addMethodrefInfo(ci, nt);
/*     */       } 
/*     */       
/* 124 */       this.constPool = cp;
/*     */     } 
/*     */     
/* 127 */     iterator.write16bit(this.newIndex, pos + 1);
/* 128 */     return pos;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\convert\TransformCall.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */