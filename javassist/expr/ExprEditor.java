/*     */ package javassist.expr;
/*     */ 
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.CtClass;
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.CodeAttribute;
/*     */ import javassist.bytecode.CodeIterator;
/*     */ import javassist.bytecode.ExceptionTable;
/*     */ import javassist.bytecode.MethodInfo;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExprEditor
/*     */ {
/*     */   public boolean doit(CtClass clazz, MethodInfo minfo) throws CannotCompileException {
/*  82 */     CodeAttribute codeAttr = minfo.getCodeAttribute();
/*  83 */     if (codeAttr == null) {
/*  84 */       return false;
/*     */     }
/*  86 */     CodeIterator iterator = codeAttr.iterator();
/*  87 */     boolean edited = false;
/*  88 */     LoopContext context = new LoopContext(codeAttr.getMaxLocals());
/*     */     
/*  90 */     while (iterator.hasNext()) {
/*  91 */       if (loopBody(iterator, clazz, minfo, context))
/*  92 */         edited = true; 
/*     */     } 
/*  94 */     ExceptionTable et = codeAttr.getExceptionTable();
/*  95 */     int n = et.size();
/*  96 */     for (int i = 0; i < n; i++) {
/*  97 */       Handler h = new Handler(et, i, iterator, clazz, minfo);
/*  98 */       edit(h);
/*  99 */       if (h.edited()) {
/* 100 */         edited = true;
/* 101 */         context.updateMax(h.locals(), h.stack());
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 107 */     if (codeAttr.getMaxLocals() < context.maxLocals) {
/* 108 */       codeAttr.setMaxLocals(context.maxLocals);
/*     */     }
/* 110 */     codeAttr.setMaxStack(codeAttr.getMaxStack() + context.maxStack);
/*     */     try {
/* 112 */       if (edited) {
/* 113 */         minfo.rebuildStackMapIf6(clazz.getClassPool(), clazz
/* 114 */             .getClassFile2());
/*     */       }
/* 116 */     } catch (BadBytecode b) {
/* 117 */       throw new CannotCompileException(b.getMessage(), b);
/*     */     } 
/*     */     
/* 120 */     return edited;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean doit(CtClass clazz, MethodInfo minfo, LoopContext context, CodeIterator iterator, int endPos) throws CannotCompileException {
/* 130 */     boolean edited = false;
/* 131 */     while (iterator.hasNext() && iterator.lookAhead() < endPos) {
/* 132 */       int size = iterator.getCodeLength();
/* 133 */       if (loopBody(iterator, clazz, minfo, context)) {
/* 134 */         edited = true;
/* 135 */         int size2 = iterator.getCodeLength();
/* 136 */         if (size != size2) {
/* 137 */           endPos += size2 - size;
/*     */         }
/*     */       } 
/*     */     } 
/* 141 */     return edited;
/*     */   }
/*     */   
/*     */   static final class NewOp {
/*     */     NewOp next;
/*     */     int pos;
/*     */     String type;
/*     */     
/*     */     NewOp(NewOp n, int p, String t) {
/* 150 */       this.next = n;
/* 151 */       this.pos = p;
/* 152 */       this.type = t;
/*     */     }
/*     */   }
/*     */   
/*     */   static final class LoopContext {
/*     */     ExprEditor.NewOp newList;
/*     */     int maxLocals;
/*     */     int maxStack;
/*     */     
/*     */     LoopContext(int locals) {
/* 162 */       this.maxLocals = locals;
/* 163 */       this.maxStack = 0;
/* 164 */       this.newList = null;
/*     */     }
/*     */     
/*     */     void updateMax(int locals, int stack) {
/* 168 */       if (this.maxLocals < locals) {
/* 169 */         this.maxLocals = locals;
/*     */       }
/* 171 */       if (this.maxStack < stack) {
/* 172 */         this.maxStack = stack;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   final boolean loopBody(CodeIterator iterator, CtClass clazz, MethodInfo minfo, LoopContext context) throws CannotCompileException {
/*     */     try {
/* 181 */       Expr expr = null;
/* 182 */       int pos = iterator.next();
/* 183 */       int c = iterator.byteAt(pos);
/*     */       
/* 185 */       if (c >= 178)
/*     */       {
/* 187 */         if (c < 188) {
/* 188 */           if (c == 184 || c == 185 || c == 182) {
/*     */ 
/*     */             
/* 191 */             expr = new MethodCall(pos, iterator, clazz, minfo);
/* 192 */             edit((MethodCall)expr);
/*     */           }
/* 194 */           else if (c == 180 || c == 178 || c == 181 || c == 179) {
/*     */ 
/*     */             
/* 197 */             expr = new FieldAccess(pos, iterator, clazz, minfo, c);
/* 198 */             edit((FieldAccess)expr);
/*     */           }
/* 200 */           else if (c == 187) {
/* 201 */             int index = iterator.u16bitAt(pos + 1);
/* 202 */             context
/* 203 */               .newList = new NewOp(context.newList, pos, minfo.getConstPool().getClassInfo(index));
/*     */           }
/* 205 */           else if (c == 183) {
/* 206 */             NewOp newList = context.newList;
/* 207 */             if (newList != null && minfo
/* 208 */               .getConstPool().isConstructor(newList.type, iterator
/* 209 */                 .u16bitAt(pos + 1)) > 0) {
/* 210 */               expr = new NewExpr(pos, iterator, clazz, minfo, newList.type, newList.pos);
/*     */               
/* 212 */               edit((NewExpr)expr);
/* 213 */               context.newList = newList.next;
/*     */             } else {
/*     */               
/* 216 */               MethodCall mcall = new MethodCall(pos, iterator, clazz, minfo);
/* 217 */               if (mcall.getMethodName().equals("<init>")) {
/* 218 */                 ConstructorCall ccall = new ConstructorCall(pos, iterator, clazz, minfo);
/* 219 */                 expr = ccall;
/* 220 */                 edit(ccall);
/*     */               } else {
/*     */                 
/* 223 */                 expr = mcall;
/* 224 */                 edit(mcall);
/*     */               }
/*     */             
/*     */             }
/*     */           
/*     */           } 
/* 230 */         } else if (c == 188 || c == 189 || c == 197) {
/*     */           
/* 232 */           expr = new NewArray(pos, iterator, clazz, minfo, c);
/* 233 */           edit((NewArray)expr);
/*     */         }
/* 235 */         else if (c == 193) {
/* 236 */           expr = new Instanceof(pos, iterator, clazz, minfo);
/* 237 */           edit((Instanceof)expr);
/*     */         }
/* 239 */         else if (c == 192) {
/* 240 */           expr = new Cast(pos, iterator, clazz, minfo);
/* 241 */           edit((Cast)expr);
/*     */         } 
/*     */       }
/*     */       
/* 245 */       if (expr != null && expr.edited()) {
/* 246 */         context.updateMax(expr.locals(), expr.stack());
/* 247 */         return true;
/*     */       } 
/*     */       
/* 250 */       return false;
/*     */     }
/* 252 */     catch (BadBytecode e) {
/* 253 */       throw new CannotCompileException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void edit(NewExpr e) throws CannotCompileException {}
/*     */   
/*     */   public void edit(NewArray a) throws CannotCompileException {}
/*     */   
/*     */   public void edit(MethodCall m) throws CannotCompileException {}
/*     */   
/*     */   public void edit(ConstructorCall c) throws CannotCompileException {}
/*     */   
/*     */   public void edit(FieldAccess f) throws CannotCompileException {}
/*     */   
/*     */   public void edit(Instanceof i) throws CannotCompileException {}
/*     */   
/*     */   public void edit(Cast c) throws CannotCompileException {}
/*     */   
/*     */   public void edit(Handler h) throws CannotCompileException {}
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\expr\ExprEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */