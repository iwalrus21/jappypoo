/*    */ package javassist.convert;
/*    */ 
/*    */ import javassist.CtMethod;
/*    */ import javassist.NotFoundException;
/*    */ import javassist.bytecode.BadBytecode;
/*    */ import javassist.bytecode.CodeIterator;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TransformAfter
/*    */   extends TransformBefore
/*    */ {
/*    */   public TransformAfter(Transformer next, CtMethod origMethod, CtMethod afterMethod) throws NotFoundException {
/* 28 */     super(next, origMethod, afterMethod);
/*    */   }
/*    */   
/*    */   protected int match2(int pos, CodeIterator iterator) throws BadBytecode {
/* 32 */     iterator.move(pos);
/* 33 */     iterator.insert(this.saveCode);
/* 34 */     iterator.insert(this.loadCode);
/* 35 */     int p = iterator.insertGap(3);
/* 36 */     iterator.setMark(p);
/* 37 */     iterator.insert(this.loadCode);
/* 38 */     pos = iterator.next();
/* 39 */     p = iterator.getMark();
/* 40 */     iterator.writeByte(iterator.byteAt(pos), p);
/* 41 */     iterator.write16bit(iterator.u16bitAt(pos + 1), p + 1);
/* 42 */     iterator.writeByte(184, pos);
/* 43 */     iterator.write16bit(this.newIndex, pos + 1);
/* 44 */     iterator.move(p);
/* 45 */     return iterator.next();
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\convert\TransformAfter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */