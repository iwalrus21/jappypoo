/*     */ package javassist.convert;
/*     */ 
/*     */ import javassist.CtClass;
/*     */ import javassist.CtMethod;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.Bytecode;
/*     */ import javassist.bytecode.CodeAttribute;
/*     */ import javassist.bytecode.CodeIterator;
/*     */ import javassist.bytecode.ConstPool;
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
/*     */ public class TransformBefore
/*     */   extends TransformCall
/*     */ {
/*     */   protected CtClass[] parameterTypes;
/*     */   protected int locals;
/*     */   protected int maxLocals;
/*     */   protected byte[] saveCode;
/*     */   protected byte[] loadCode;
/*     */   
/*     */   public TransformBefore(Transformer next, CtMethod origMethod, CtMethod beforeMethod) throws NotFoundException {
/*  34 */     super(next, origMethod, beforeMethod);
/*     */ 
/*     */     
/*  37 */     this.methodDescriptor = origMethod.getMethodInfo2().getDescriptor();
/*     */     
/*  39 */     this.parameterTypes = origMethod.getParameterTypes();
/*  40 */     this.locals = 0;
/*  41 */     this.maxLocals = 0;
/*  42 */     this.saveCode = this.loadCode = null;
/*     */   }
/*     */   
/*     */   public void initialize(ConstPool cp, CodeAttribute attr) {
/*  46 */     super.initialize(cp, attr);
/*  47 */     this.locals = 0;
/*  48 */     this.maxLocals = attr.getMaxLocals();
/*  49 */     this.saveCode = this.loadCode = null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int match(int c, int pos, CodeIterator iterator, int typedesc, ConstPool cp) throws BadBytecode {
/*  55 */     if (this.newIndex == 0) {
/*  56 */       String desc = Descriptor.ofParameters(this.parameterTypes) + 'V';
/*  57 */       desc = Descriptor.insertParameter(this.classname, desc);
/*  58 */       int nt = cp.addNameAndTypeInfo(this.newMethodname, desc);
/*  59 */       int ci = cp.addClassInfo(this.newClassname);
/*  60 */       this.newIndex = cp.addMethodrefInfo(ci, nt);
/*  61 */       this.constPool = cp;
/*     */     } 
/*     */     
/*  64 */     if (this.saveCode == null) {
/*  65 */       makeCode(this.parameterTypes, cp);
/*     */     }
/*  67 */     return match2(pos, iterator);
/*     */   }
/*     */   
/*     */   protected int match2(int pos, CodeIterator iterator) throws BadBytecode {
/*  71 */     iterator.move(pos);
/*  72 */     iterator.insert(this.saveCode);
/*  73 */     iterator.insert(this.loadCode);
/*  74 */     int p = iterator.insertGap(3);
/*  75 */     iterator.writeByte(184, p);
/*  76 */     iterator.write16bit(this.newIndex, p + 1);
/*  77 */     iterator.insert(this.loadCode);
/*  78 */     return iterator.next();
/*     */   }
/*     */   public int extraLocals() {
/*  81 */     return this.locals;
/*     */   }
/*     */   protected void makeCode(CtClass[] paramTypes, ConstPool cp) {
/*  84 */     Bytecode save = new Bytecode(cp, 0, 0);
/*  85 */     Bytecode load = new Bytecode(cp, 0, 0);
/*     */     
/*  87 */     int var = this.maxLocals;
/*  88 */     int len = (paramTypes == null) ? 0 : paramTypes.length;
/*  89 */     load.addAload(var);
/*  90 */     makeCode2(save, load, 0, len, paramTypes, var + 1);
/*  91 */     save.addAstore(var);
/*     */     
/*  93 */     this.saveCode = save.get();
/*  94 */     this.loadCode = load.get();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void makeCode2(Bytecode save, Bytecode load, int i, int n, CtClass[] paramTypes, int var) {
/* 100 */     if (i < n) {
/* 101 */       int size = load.addLoad(var, paramTypes[i]);
/* 102 */       makeCode2(save, load, i + 1, n, paramTypes, var + size);
/* 103 */       save.addStore(var, paramTypes[i]);
/*     */     } else {
/*     */       
/* 106 */       this.locals = var - this.maxLocals;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\convert\TransformBefore.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */