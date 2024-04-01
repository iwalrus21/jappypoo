/*     */ package javassist.convert;
/*     */ 
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.CtClass;
/*     */ import javassist.bytecode.CodeAttribute;
/*     */ import javassist.bytecode.CodeIterator;
/*     */ import javassist.bytecode.ConstPool;
/*     */ import javassist.bytecode.Descriptor;
/*     */ import javassist.bytecode.StackMap;
/*     */ import javassist.bytecode.StackMapTable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class TransformNew
/*     */   extends Transformer
/*     */ {
/*     */   private int nested;
/*     */   private String classname;
/*     */   private String trapClass;
/*     */   private String trapMethod;
/*     */   
/*     */   public TransformNew(Transformer next, String classname, String trapClass, String trapMethod) {
/*  29 */     super(next);
/*  30 */     this.classname = classname;
/*  31 */     this.trapClass = trapClass;
/*  32 */     this.trapMethod = trapMethod;
/*     */   }
/*     */   
/*     */   public void initialize(ConstPool cp, CodeAttribute attr) {
/*  36 */     this.nested = 0;
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
/*     */   public int transform(CtClass clazz, int pos, CodeIterator iterator, ConstPool cp) throws CannotCompileException {
/*  55 */     int c = iterator.byteAt(pos);
/*  56 */     if (c == 187) {
/*  57 */       int index = iterator.u16bitAt(pos + 1);
/*  58 */       if (cp.getClassInfo(index).equals(this.classname)) {
/*  59 */         if (iterator.byteAt(pos + 3) != 89) {
/*  60 */           throw new CannotCompileException("NEW followed by no DUP was found");
/*     */         }
/*     */         
/*  63 */         iterator.writeByte(0, pos);
/*  64 */         iterator.writeByte(0, pos + 1);
/*  65 */         iterator.writeByte(0, pos + 2);
/*  66 */         iterator.writeByte(0, pos + 3);
/*  67 */         this.nested++;
/*     */ 
/*     */         
/*  70 */         StackMapTable smt = (StackMapTable)iterator.get().getAttribute("StackMapTable");
/*  71 */         if (smt != null) {
/*  72 */           smt.removeNew(pos);
/*     */         }
/*     */         
/*  75 */         StackMap sm = (StackMap)iterator.get().getAttribute("StackMap");
/*  76 */         if (sm != null) {
/*  77 */           sm.removeNew(pos);
/*     */         }
/*     */       } 
/*  80 */     } else if (c == 183) {
/*  81 */       int index = iterator.u16bitAt(pos + 1);
/*  82 */       int typedesc = cp.isConstructor(this.classname, index);
/*  83 */       if (typedesc != 0 && this.nested > 0) {
/*  84 */         int methodref = computeMethodref(typedesc, cp);
/*  85 */         iterator.writeByte(184, pos);
/*  86 */         iterator.write16bit(methodref, pos + 1);
/*  87 */         this.nested--;
/*     */       } 
/*     */     } 
/*     */     
/*  91 */     return pos;
/*     */   }
/*     */   
/*     */   private int computeMethodref(int typedesc, ConstPool cp) {
/*  95 */     int classIndex = cp.addClassInfo(this.trapClass);
/*  96 */     int mnameIndex = cp.addUtf8Info(this.trapMethod);
/*  97 */     typedesc = cp.addUtf8Info(
/*  98 */         Descriptor.changeReturnType(this.classname, cp
/*  99 */           .getUtf8Info(typedesc)));
/* 100 */     return cp.addMethodrefInfo(classIndex, cp
/* 101 */         .addNameAndTypeInfo(mnameIndex, typedesc));
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\convert\TransformNew.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */