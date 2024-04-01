/*    */ package javassist.convert;
/*    */ 
/*    */ import javassist.CtClass;
/*    */ import javassist.CtField;
/*    */ import javassist.bytecode.BadBytecode;
/*    */ import javassist.bytecode.CodeAttribute;
/*    */ import javassist.bytecode.CodeIterator;
/*    */ import javassist.bytecode.ConstPool;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class TransformWriteField
/*    */   extends TransformReadField
/*    */ {
/*    */   public TransformWriteField(Transformer next, CtField field, String methodClassname, String methodName) {
/* 27 */     super(next, field, methodClassname, methodName);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int transform(CtClass tclazz, int pos, CodeIterator iterator, ConstPool cp) throws BadBytecode {
/* 33 */     int c = iterator.byteAt(pos);
/* 34 */     if (c == 181 || c == 179) {
/* 35 */       int index = iterator.u16bitAt(pos + 1);
/* 36 */       String typedesc = isField(tclazz.getClassPool(), cp, this.fieldClass, this.fieldname, this.isPrivate, index);
/*    */       
/* 38 */       if (typedesc != null) {
/* 39 */         if (c == 179) {
/* 40 */           CodeAttribute ca = iterator.get();
/* 41 */           iterator.move(pos);
/* 42 */           char c0 = typedesc.charAt(0);
/* 43 */           if (c0 == 'J' || c0 == 'D') {
/*    */             
/* 45 */             pos = iterator.insertGap(3);
/* 46 */             iterator.writeByte(1, pos);
/* 47 */             iterator.writeByte(91, pos + 1);
/* 48 */             iterator.writeByte(87, pos + 2);
/* 49 */             ca.setMaxStack(ca.getMaxStack() + 2);
/*    */           }
/*    */           else {
/*    */             
/* 53 */             pos = iterator.insertGap(2);
/* 54 */             iterator.writeByte(1, pos);
/* 55 */             iterator.writeByte(95, pos + 1);
/* 56 */             ca.setMaxStack(ca.getMaxStack() + 1);
/*    */           } 
/*    */           
/* 59 */           pos = iterator.next();
/*    */         } 
/*    */         
/* 62 */         int mi = cp.addClassInfo(this.methodClassname);
/* 63 */         String type = "(Ljava/lang/Object;" + typedesc + ")V";
/* 64 */         int methodref = cp.addMethodrefInfo(mi, this.methodName, type);
/* 65 */         iterator.writeByte(184, pos);
/* 66 */         iterator.write16bit(methodref, pos + 1);
/*    */       } 
/*    */     } 
/*    */     
/* 70 */     return pos;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\convert\TransformWriteField.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */