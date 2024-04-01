/*     */ package javassist;
/*     */ 
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.CodeAttribute;
/*     */ import javassist.bytecode.CodeIterator;
/*     */ import javassist.bytecode.ConstPool;
/*     */ import javassist.bytecode.MethodInfo;
/*     */ import javassist.convert.TransformAccessArrayField;
/*     */ import javassist.convert.TransformAfter;
/*     */ import javassist.convert.TransformBefore;
/*     */ import javassist.convert.TransformCall;
/*     */ import javassist.convert.TransformFieldAccess;
/*     */ import javassist.convert.TransformNew;
/*     */ import javassist.convert.TransformNewClass;
/*     */ import javassist.convert.TransformReadField;
/*     */ import javassist.convert.TransformWriteField;
/*     */ import javassist.convert.Transformer;
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
/*     */ public class CodeConverter
/*     */ {
/*  51 */   protected Transformer transformers = null;
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
/*     */   public void replaceNew(CtClass newClass, CtClass calledClass, String calledMethod) {
/*  97 */     this
/*  98 */       .transformers = (Transformer)new TransformNew(this.transformers, newClass.getName(), calledClass.getName(), calledMethod);
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
/*     */   public void replaceNew(CtClass oldClass, CtClass newClass) {
/* 123 */     this
/* 124 */       .transformers = (Transformer)new TransformNewClass(this.transformers, oldClass.getName(), newClass.getName());
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
/*     */   public void redirectFieldAccess(CtField field, CtClass newClass, String newFieldname) {
/* 146 */     this
/* 147 */       .transformers = (Transformer)new TransformFieldAccess(this.transformers, field, newClass.getName(), newFieldname);
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
/*     */   public void replaceFieldRead(CtField field, CtClass calledClass, String calledMethod) {
/* 186 */     this
/* 187 */       .transformers = (Transformer)new TransformReadField(this.transformers, field, calledClass.getName(), calledMethod);
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
/*     */   
/*     */   public void replaceFieldWrite(CtField field, CtClass calledClass, String calledMethod) {
/* 227 */     this
/* 228 */       .transformers = (Transformer)new TransformWriteField(this.transformers, field, calledClass.getName(), calledMethod);
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
/*     */   public void replaceArrayAccess(CtClass calledClass, ArrayAccessReplacementMethodNames names) throws NotFoundException {
/* 330 */     this.transformers = (Transformer)new TransformAccessArrayField(this.transformers, calledClass.getName(), names);
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
/*     */   public void redirectMethodCall(CtMethod origMethod, CtMethod substMethod) throws CannotCompileException {
/* 352 */     String d1 = origMethod.getMethodInfo2().getDescriptor();
/* 353 */     String d2 = substMethod.getMethodInfo2().getDescriptor();
/* 354 */     if (!d1.equals(d2)) {
/* 355 */       throw new CannotCompileException("signature mismatch: " + substMethod
/* 356 */           .getLongName());
/*     */     }
/* 358 */     int mod1 = origMethod.getModifiers();
/* 359 */     int mod2 = substMethod.getModifiers();
/* 360 */     if (Modifier.isStatic(mod1) != Modifier.isStatic(mod2) || (
/* 361 */       Modifier.isPrivate(mod1) && !Modifier.isPrivate(mod2)) || origMethod
/* 362 */       .getDeclaringClass().isInterface() != substMethod
/* 363 */       .getDeclaringClass().isInterface()) {
/* 364 */       throw new CannotCompileException("invoke-type mismatch " + substMethod
/* 365 */           .getLongName());
/*     */     }
/* 367 */     this.transformers = (Transformer)new TransformCall(this.transformers, origMethod, substMethod);
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
/*     */   public void redirectMethodCall(String oldMethodName, CtMethod newMethod) throws CannotCompileException {
/* 392 */     this.transformers = (Transformer)new TransformCall(this.transformers, oldMethodName, newMethod);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void insertBeforeMethod(CtMethod origMethod, CtMethod beforeMethod) throws CannotCompileException {
/*     */     try {
/* 435 */       this.transformers = (Transformer)new TransformBefore(this.transformers, origMethod, beforeMethod);
/*     */     
/*     */     }
/* 438 */     catch (NotFoundException e) {
/* 439 */       throw new CannotCompileException(e);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void insertAfterMethod(CtMethod origMethod, CtMethod afterMethod) throws CannotCompileException {
/*     */     try {
/* 483 */       this.transformers = (Transformer)new TransformAfter(this.transformers, origMethod, afterMethod);
/*     */     
/*     */     }
/* 486 */     catch (NotFoundException e) {
/* 487 */       throw new CannotCompileException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doit(CtClass clazz, MethodInfo minfo, ConstPool cp) throws CannotCompileException {
/* 498 */     CodeAttribute codeAttr = minfo.getCodeAttribute();
/* 499 */     if (codeAttr == null || this.transformers == null)
/*     */       return;  Transformer t;
/* 501 */     for (t = this.transformers; t != null; t = t.getNext()) {
/* 502 */       t.initialize(cp, clazz, minfo);
/*     */     }
/* 504 */     CodeIterator iterator = codeAttr.iterator();
/* 505 */     while (iterator.hasNext()) {
/*     */       try {
/* 507 */         int pos = iterator.next();
/* 508 */         for (t = this.transformers; t != null; t = t.getNext()) {
/* 509 */           pos = t.transform(clazz, pos, iterator, cp);
/*     */         }
/* 511 */       } catch (BadBytecode e) {
/* 512 */         throw new CannotCompileException(e);
/*     */       } 
/*     */     } 
/*     */     
/* 516 */     int locals = 0;
/* 517 */     int stack = 0;
/* 518 */     for (t = this.transformers; t != null; t = t.getNext()) {
/* 519 */       int s = t.extraLocals();
/* 520 */       if (s > locals) {
/* 521 */         locals = s;
/*     */       }
/* 523 */       s = t.extraStack();
/* 524 */       if (s > stack) {
/* 525 */         stack = s;
/*     */       }
/*     */     } 
/* 528 */     for (t = this.transformers; t != null; t = t.getNext()) {
/* 529 */       t.clean();
/*     */     }
/* 531 */     if (locals > 0) {
/* 532 */       codeAttr.setMaxLocals(codeAttr.getMaxLocals() + locals);
/*     */     }
/* 534 */     if (stack > 0) {
/* 535 */       codeAttr.setMaxStack(codeAttr.getMaxStack() + stack);
/*     */     }
/*     */     try {
/* 538 */       minfo.rebuildStackMapIf6(clazz.getClassPool(), clazz
/* 539 */           .getClassFile2());
/*     */     }
/* 541 */     catch (BadBytecode b) {
/* 542 */       throw new CannotCompileException(b.getMessage(), b);
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
/*     */   public static class DefaultArrayAccessReplacementMethodNames
/*     */     implements ArrayAccessReplacementMethodNames
/*     */   {
/*     */     public String byteOrBooleanRead() {
/* 671 */       return "arrayReadByteOrBoolean";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String byteOrBooleanWrite() {
/* 680 */       return "arrayWriteByteOrBoolean";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String charRead() {
/* 689 */       return "arrayReadChar";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String charWrite() {
/* 698 */       return "arrayWriteChar";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String doubleRead() {
/* 707 */       return "arrayReadDouble";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String doubleWrite() {
/* 716 */       return "arrayWriteDouble";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String floatRead() {
/* 725 */       return "arrayReadFloat";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String floatWrite() {
/* 734 */       return "arrayWriteFloat";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String intRead() {
/* 743 */       return "arrayReadInt";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String intWrite() {
/* 752 */       return "arrayWriteInt";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String longRead() {
/* 761 */       return "arrayReadLong";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String longWrite() {
/* 770 */       return "arrayWriteLong";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String objectRead() {
/* 779 */       return "arrayReadObject";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String objectWrite() {
/* 788 */       return "arrayWriteObject";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String shortRead() {
/* 797 */       return "arrayReadShort";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String shortWrite() {
/* 806 */       return "arrayWriteShort";
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface ArrayAccessReplacementMethodNames {
/*     */     String byteOrBooleanRead();
/*     */     
/*     */     String byteOrBooleanWrite();
/*     */     
/*     */     String charRead();
/*     */     
/*     */     String charWrite();
/*     */     
/*     */     String doubleRead();
/*     */     
/*     */     String doubleWrite();
/*     */     
/*     */     String floatRead();
/*     */     
/*     */     String floatWrite();
/*     */     
/*     */     String intRead();
/*     */     
/*     */     String intWrite();
/*     */     
/*     */     String longRead();
/*     */     
/*     */     String longWrite();
/*     */     
/*     */     String objectRead();
/*     */     
/*     */     String objectWrite();
/*     */     
/*     */     String shortRead();
/*     */     
/*     */     String shortWrite();
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\CodeConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */