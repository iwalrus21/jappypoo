/*     */ package javassist.convert;
/*     */ 
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.CodeConverter;
/*     */ import javassist.CtClass;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.CodeIterator;
/*     */ import javassist.bytecode.ConstPool;
/*     */ import javassist.bytecode.Descriptor;
/*     */ import javassist.bytecode.MethodInfo;
/*     */ import javassist.bytecode.analysis.Analyzer;
/*     */ import javassist.bytecode.analysis.Frame;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class TransformAccessArrayField
/*     */   extends Transformer
/*     */ {
/*     */   private final String methodClassname;
/*     */   private final CodeConverter.ArrayAccessReplacementMethodNames names;
/*     */   private Frame[] frames;
/*     */   private int offset;
/*     */   
/*     */   public TransformAccessArrayField(Transformer next, String methodClassname, CodeConverter.ArrayAccessReplacementMethodNames names) throws NotFoundException {
/*  46 */     super(next);
/*  47 */     this.methodClassname = methodClassname;
/*  48 */     this.names = names;
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
/*     */   public void initialize(ConstPool cp, CtClass clazz, MethodInfo minfo) throws CannotCompileException {
/*  63 */     CodeIterator iterator = minfo.getCodeAttribute().iterator();
/*  64 */     while (iterator.hasNext()) {
/*     */       try {
/*  66 */         int pos = iterator.next();
/*  67 */         int c = iterator.byteAt(pos);
/*     */         
/*  69 */         if (c == 50) {
/*  70 */           initFrames(clazz, minfo);
/*     */         }
/*  72 */         if (c == 50 || c == 51 || c == 52 || c == 49 || c == 48 || c == 46 || c == 47 || c == 53) {
/*     */ 
/*     */           
/*  75 */           pos = replace(cp, iterator, pos, c, getLoadReplacementSignature(c)); continue;
/*  76 */         }  if (c == 83 || c == 84 || c == 85 || c == 82 || c == 81 || c == 79 || c == 80 || c == 86)
/*     */         {
/*     */           
/*  79 */           pos = replace(cp, iterator, pos, c, getStoreReplacementSignature(c));
/*     */         }
/*     */       }
/*  82 */       catch (Exception e) {
/*  83 */         throw new CannotCompileException(e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void clean() {
/*  89 */     this.frames = null;
/*  90 */     this.offset = -1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int transform(CtClass tclazz, int pos, CodeIterator iterator, ConstPool cp) throws BadBytecode {
/*  96 */     return pos;
/*     */   }
/*     */   
/*     */   private Frame getFrame(int pos) throws BadBytecode {
/* 100 */     return this.frames[pos - this.offset];
/*     */   }
/*     */   
/*     */   private void initFrames(CtClass clazz, MethodInfo minfo) throws BadBytecode {
/* 104 */     if (this.frames == null) {
/* 105 */       this.frames = (new Analyzer()).analyze(clazz, minfo);
/* 106 */       this.offset = 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   private int updatePos(int pos, int increment) {
/* 111 */     if (this.offset > -1) {
/* 112 */       this.offset += increment;
/*     */     }
/* 114 */     return pos + increment;
/*     */   }
/*     */   
/*     */   private String getTopType(int pos) throws BadBytecode {
/* 118 */     Frame frame = getFrame(pos);
/* 119 */     if (frame == null) {
/* 120 */       return null;
/*     */     }
/* 122 */     CtClass clazz = frame.peek().getCtClass();
/* 123 */     return (clazz != null) ? Descriptor.toJvmName(clazz) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   private int replace(ConstPool cp, CodeIterator iterator, int pos, int opcode, String signature) throws BadBytecode {
/* 128 */     String castType = null;
/* 129 */     String methodName = getMethodName(opcode);
/* 130 */     if (methodName != null) {
/*     */       
/* 132 */       if (opcode == 50) {
/* 133 */         castType = getTopType(iterator.lookAhead());
/*     */ 
/*     */ 
/*     */         
/* 137 */         if (castType == null)
/* 138 */           return pos; 
/* 139 */         if ("java/lang/Object".equals(castType)) {
/* 140 */           castType = null;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 145 */       iterator.writeByte(0, pos);
/*     */       
/* 147 */       CodeIterator.Gap gap = iterator.insertGapAt(pos, (castType != null) ? 5 : 2, false);
/* 148 */       pos = gap.position;
/* 149 */       int mi = cp.addClassInfo(this.methodClassname);
/* 150 */       int methodref = cp.addMethodrefInfo(mi, methodName, signature);
/* 151 */       iterator.writeByte(184, pos);
/* 152 */       iterator.write16bit(methodref, pos + 1);
/*     */       
/* 154 */       if (castType != null) {
/* 155 */         int index = cp.addClassInfo(castType);
/* 156 */         iterator.writeByte(192, pos + 3);
/* 157 */         iterator.write16bit(index, pos + 4);
/*     */       } 
/*     */       
/* 160 */       pos = updatePos(pos, gap.length);
/*     */     } 
/*     */     
/* 163 */     return pos;
/*     */   }
/*     */   
/*     */   private String getMethodName(int opcode) {
/* 167 */     String methodName = null;
/* 168 */     switch (opcode) {
/*     */       case 50:
/* 170 */         methodName = this.names.objectRead();
/*     */         break;
/*     */       case 51:
/* 173 */         methodName = this.names.byteOrBooleanRead();
/*     */         break;
/*     */       case 52:
/* 176 */         methodName = this.names.charRead();
/*     */         break;
/*     */       case 49:
/* 179 */         methodName = this.names.doubleRead();
/*     */         break;
/*     */       case 48:
/* 182 */         methodName = this.names.floatRead();
/*     */         break;
/*     */       case 46:
/* 185 */         methodName = this.names.intRead();
/*     */         break;
/*     */       case 53:
/* 188 */         methodName = this.names.shortRead();
/*     */         break;
/*     */       case 47:
/* 191 */         methodName = this.names.longRead();
/*     */         break;
/*     */       case 83:
/* 194 */         methodName = this.names.objectWrite();
/*     */         break;
/*     */       case 84:
/* 197 */         methodName = this.names.byteOrBooleanWrite();
/*     */         break;
/*     */       case 85:
/* 200 */         methodName = this.names.charWrite();
/*     */         break;
/*     */       case 82:
/* 203 */         methodName = this.names.doubleWrite();
/*     */         break;
/*     */       case 81:
/* 206 */         methodName = this.names.floatWrite();
/*     */         break;
/*     */       case 79:
/* 209 */         methodName = this.names.intWrite();
/*     */         break;
/*     */       case 86:
/* 212 */         methodName = this.names.shortWrite();
/*     */         break;
/*     */       case 80:
/* 215 */         methodName = this.names.longWrite();
/*     */         break;
/*     */     } 
/*     */     
/* 219 */     if (methodName.equals("")) {
/* 220 */       methodName = null;
/*     */     }
/* 222 */     return methodName;
/*     */   }
/*     */   
/*     */   private String getLoadReplacementSignature(int opcode) throws BadBytecode {
/* 226 */     switch (opcode) {
/*     */       case 50:
/* 228 */         return "(Ljava/lang/Object;I)Ljava/lang/Object;";
/*     */       case 51:
/* 230 */         return "(Ljava/lang/Object;I)B";
/*     */       case 52:
/* 232 */         return "(Ljava/lang/Object;I)C";
/*     */       case 49:
/* 234 */         return "(Ljava/lang/Object;I)D";
/*     */       case 48:
/* 236 */         return "(Ljava/lang/Object;I)F";
/*     */       case 46:
/* 238 */         return "(Ljava/lang/Object;I)I";
/*     */       case 53:
/* 240 */         return "(Ljava/lang/Object;I)S";
/*     */       case 47:
/* 242 */         return "(Ljava/lang/Object;I)J";
/*     */     } 
/*     */     
/* 245 */     throw new BadBytecode(opcode);
/*     */   }
/*     */   
/*     */   private String getStoreReplacementSignature(int opcode) throws BadBytecode {
/* 249 */     switch (opcode) {
/*     */       case 83:
/* 251 */         return "(Ljava/lang/Object;ILjava/lang/Object;)V";
/*     */       case 84:
/* 253 */         return "(Ljava/lang/Object;IB)V";
/*     */       case 85:
/* 255 */         return "(Ljava/lang/Object;IC)V";
/*     */       case 82:
/* 257 */         return "(Ljava/lang/Object;ID)V";
/*     */       case 81:
/* 259 */         return "(Ljava/lang/Object;IF)V";
/*     */       case 79:
/* 261 */         return "(Ljava/lang/Object;II)V";
/*     */       case 86:
/* 263 */         return "(Ljava/lang/Object;IS)V";
/*     */       case 80:
/* 265 */         return "(Ljava/lang/Object;IJ)V";
/*     */     } 
/*     */     
/* 268 */     throw new BadBytecode(opcode);
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\convert\TransformAccessArrayField.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */