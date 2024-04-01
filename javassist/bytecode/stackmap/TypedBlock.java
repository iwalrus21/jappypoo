/*     */ package javassist.bytecode.stackmap;
/*     */ 
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.CodeAttribute;
/*     */ import javassist.bytecode.ConstPool;
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
/*     */ public class TypedBlock
/*     */   extends BasicBlock
/*     */ {
/*     */   public int stackTop;
/*     */   public int numLocals;
/*     */   public TypeData[] localsTypes;
/*     */   public TypeData[] stackTypes;
/*     */   
/*     */   public static TypedBlock[] makeBlocks(MethodInfo minfo, CodeAttribute ca, boolean optimize) throws BadBytecode {
/*  39 */     TypedBlock[] blocks = (TypedBlock[])(new Maker()).make(minfo);
/*  40 */     if (optimize && blocks.length < 2 && (
/*  41 */       blocks.length == 0 || (blocks[0]).incoming == 0)) {
/*  42 */       return null;
/*     */     }
/*  44 */     ConstPool pool = minfo.getConstPool();
/*  45 */     boolean isStatic = ((minfo.getAccessFlags() & 0x8) != 0);
/*  46 */     blocks[0].initFirstBlock(ca.getMaxStack(), ca.getMaxLocals(), pool
/*  47 */         .getClassName(), minfo.getDescriptor(), isStatic, minfo
/*  48 */         .isConstructor());
/*  49 */     return blocks;
/*     */   }
/*     */   
/*     */   protected TypedBlock(int pos) {
/*  53 */     super(pos);
/*  54 */     this.localsTypes = null;
/*     */   }
/*     */   
/*     */   protected void toString2(StringBuffer sbuf) {
/*  58 */     super.toString2(sbuf);
/*  59 */     sbuf.append(",\n stack={");
/*  60 */     printTypes(sbuf, this.stackTop, this.stackTypes);
/*  61 */     sbuf.append("}, locals={");
/*  62 */     printTypes(sbuf, this.numLocals, this.localsTypes);
/*  63 */     sbuf.append('}');
/*     */   }
/*     */ 
/*     */   
/*     */   private void printTypes(StringBuffer sbuf, int size, TypeData[] types) {
/*  68 */     if (types == null) {
/*     */       return;
/*     */     }
/*  71 */     for (int i = 0; i < size; i++) {
/*  72 */       if (i > 0) {
/*  73 */         sbuf.append(", ");
/*     */       }
/*  75 */       TypeData td = types[i];
/*  76 */       sbuf.append((td == null) ? "<>" : td.toString());
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean alreadySet() {
/*  81 */     return (this.localsTypes != null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStackMap(int st, TypeData[] stack, int nl, TypeData[] locals) throws BadBytecode {
/*  87 */     this.stackTop = st;
/*  88 */     this.stackTypes = stack;
/*  89 */     this.numLocals = nl;
/*  90 */     this.localsTypes = locals;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetNumLocals() {
/*  97 */     if (this.localsTypes != null) {
/*  98 */       int nl = this.localsTypes.length;
/*  99 */       while (nl > 0 && this.localsTypes[nl - 1].isBasicType() == TypeTag.TOP && (
/* 100 */         nl <= 1 || 
/* 101 */         !this.localsTypes[nl - 2].is2WordType()))
/*     */       {
/*     */ 
/*     */         
/* 105 */         nl--;
/*     */       }
/*     */       
/* 108 */       this.numLocals = nl;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static class Maker extends BasicBlock.Maker {
/*     */     protected BasicBlock makeBlock(int pos) {
/* 114 */       return new TypedBlock(pos);
/*     */     }
/*     */     
/*     */     protected BasicBlock[] makeArray(int size) {
/* 118 */       return (BasicBlock[])new TypedBlock[size];
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
/*     */   void initFirstBlock(int maxStack, int maxLocals, String className, String methodDesc, boolean isStatic, boolean isConstructor) throws BadBytecode {
/* 136 */     if (methodDesc.charAt(0) != '(') {
/* 137 */       throw new BadBytecode("no method descriptor: " + methodDesc);
/*     */     }
/* 139 */     this.stackTop = 0;
/* 140 */     this.stackTypes = TypeData.make(maxStack);
/* 141 */     TypeData[] locals = TypeData.make(maxLocals);
/* 142 */     if (isConstructor) {
/* 143 */       locals[0] = new TypeData.UninitThis(className);
/* 144 */     } else if (!isStatic) {
/* 145 */       locals[0] = new TypeData.ClassName(className);
/*     */     } 
/* 147 */     int n = isStatic ? -1 : 0;
/* 148 */     int i = 1;
/*     */     try {
/* 150 */       while ((i = descToTag(methodDesc, i, ++n, locals)) > 0) {
/* 151 */         if (locals[n].is2WordType())
/* 152 */           locals[++n] = TypeTag.TOP; 
/*     */       } 
/* 154 */     } catch (StringIndexOutOfBoundsException e) {
/* 155 */       throw new BadBytecode("bad method descriptor: " + methodDesc);
/*     */     } 
/*     */ 
/*     */     
/* 159 */     this.numLocals = n;
/* 160 */     this.localsTypes = locals;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int descToTag(String desc, int i, int n, TypeData[] types) throws BadBytecode {
/* 167 */     int i0 = i;
/* 168 */     int arrayDim = 0;
/* 169 */     char c = desc.charAt(i);
/* 170 */     if (c == ')') {
/* 171 */       return 0;
/*     */     }
/* 173 */     while (c == '[') {
/* 174 */       arrayDim++;
/* 175 */       c = desc.charAt(++i);
/*     */     } 
/*     */     
/* 178 */     if (c == 'L') {
/* 179 */       int i2 = desc.indexOf(';', ++i);
/* 180 */       if (arrayDim > 0) {
/* 181 */         types[n] = new TypeData.ClassName(desc.substring(i0, ++i2));
/*     */       } else {
/* 183 */         types[n] = new TypeData.ClassName(desc.substring(i0 + 1, ++i2 - 1)
/* 184 */             .replace('/', '.'));
/* 185 */       }  return i2;
/*     */     } 
/* 187 */     if (arrayDim > 0) {
/* 188 */       types[n] = new TypeData.ClassName(desc.substring(i0, ++i));
/* 189 */       return i;
/*     */     } 
/*     */     
/* 192 */     TypeData t = toPrimitiveTag(c);
/* 193 */     if (t == null) {
/* 194 */       throw new BadBytecode("bad method descriptor: " + desc);
/*     */     }
/* 196 */     types[n] = t;
/* 197 */     return i + 1;
/*     */   }
/*     */ 
/*     */   
/*     */   private static TypeData toPrimitiveTag(char c) {
/* 202 */     switch (c) {
/*     */       case 'B':
/*     */       case 'C':
/*     */       case 'I':
/*     */       case 'S':
/*     */       case 'Z':
/* 208 */         return TypeTag.INTEGER;
/*     */       case 'J':
/* 210 */         return TypeTag.LONG;
/*     */       case 'F':
/* 212 */         return TypeTag.FLOAT;
/*     */       case 'D':
/* 214 */         return TypeTag.DOUBLE;
/*     */     } 
/*     */     
/* 217 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getRetType(String desc) {
/* 222 */     int i = desc.indexOf(')');
/* 223 */     if (i < 0) {
/* 224 */       return "java.lang.Object";
/*     */     }
/* 226 */     char c = desc.charAt(i + 1);
/* 227 */     if (c == '[')
/* 228 */       return desc.substring(i + 1); 
/* 229 */     if (c == 'L') {
/* 230 */       return desc.substring(i + 2, desc.length() - 1).replace('/', '.');
/*     */     }
/* 232 */     return "java.lang.Object";
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\stackmap\TypedBlock.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */