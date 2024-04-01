/*     */ package javassist.bytecode;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javassist.bytecode.annotation.AnnotationsWriter;
/*     */ import javassist.bytecode.annotation.TypeAnnotationsWriter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TypeAnnotationsAttribute
/*     */   extends AttributeInfo
/*     */ {
/*     */   public static final String visibleTag = "RuntimeVisibleTypeAnnotations";
/*     */   public static final String invisibleTag = "RuntimeInvisibleTypeAnnotations";
/*     */   
/*     */   public TypeAnnotationsAttribute(ConstPool cp, String attrname, byte[] info) {
/*  38 */     super(cp, attrname, info);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   TypeAnnotationsAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
/*  47 */     super(cp, n, in);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int numAnnotations() {
/*  54 */     return ByteArray.readU16bit(this.info, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AttributeInfo copy(ConstPool newCp, Map classnames) {
/*  61 */     Copier copier = new Copier(this.info, this.constPool, newCp, classnames);
/*     */     try {
/*  63 */       copier.annotationArray();
/*  64 */       return new TypeAnnotationsAttribute(newCp, getName(), copier.close());
/*     */     }
/*  66 */     catch (Exception e) {
/*  67 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void renameClass(String oldname, String newname) {
/*  76 */     HashMap<Object, Object> map = new HashMap<Object, Object>();
/*  77 */     map.put(oldname, newname);
/*  78 */     renameClass(map);
/*     */   }
/*     */   
/*     */   void renameClass(Map classnames) {
/*  82 */     Renamer renamer = new Renamer(this.info, getConstPool(), classnames);
/*     */     try {
/*  84 */       renamer.annotationArray();
/*  85 */     } catch (Exception e) {
/*  86 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */   void getRefClasses(Map classnames) {
/*  90 */     renameClass(classnames);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class TAWalker
/*     */     extends AnnotationsAttribute.Walker
/*     */   {
/*     */     TypeAnnotationsAttribute.SubWalker subWalker;
/*     */ 
/*     */     
/*     */     TAWalker(byte[] attrInfo) {
/* 102 */       super(attrInfo);
/* 103 */       this.subWalker = new TypeAnnotationsAttribute.SubWalker(attrInfo);
/*     */     }
/*     */     
/*     */     int annotationArray(int pos, int num) throws Exception {
/* 107 */       for (int i = 0; i < num; i++) {
/* 108 */         int targetType = this.info[pos] & 0xFF;
/* 109 */         pos = this.subWalker.targetInfo(pos + 1, targetType);
/* 110 */         pos = this.subWalker.typePath(pos);
/* 111 */         pos = annotation(pos);
/*     */       } 
/*     */       
/* 114 */       return pos;
/*     */     }
/*     */   }
/*     */   
/*     */   static class SubWalker {
/*     */     byte[] info;
/*     */     
/*     */     SubWalker(byte[] attrInfo) {
/* 122 */       this.info = attrInfo; } final int targetInfo(int pos, int type) throws Exception { int j; int param; int i; int len; int index;
/*     */       int offset;
/*     */       int bound;
/*     */       int k;
/* 126 */       switch (type) {
/*     */         case 0:
/*     */         case 1:
/* 129 */           j = this.info[pos] & 0xFF;
/* 130 */           typeParameterTarget(pos, type, j);
/* 131 */           return pos + 1;
/*     */         case 16:
/* 133 */           j = ByteArray.readU16bit(this.info, pos);
/* 134 */           supertypeTarget(pos, j);
/* 135 */           return pos + 2;
/*     */         case 17:
/*     */         case 18:
/* 138 */           param = this.info[pos] & 0xFF;
/* 139 */           bound = this.info[pos + 1] & 0xFF;
/* 140 */           typeParameterBoundTarget(pos, type, param, bound);
/* 141 */           return pos + 2;
/*     */         case 19:
/*     */         case 20:
/*     */         case 21:
/* 145 */           emptyTarget(pos, type);
/* 146 */           return pos;
/*     */         case 22:
/* 148 */           i = this.info[pos] & 0xFF;
/* 149 */           formalParameterTarget(pos, i);
/* 150 */           return pos + 1;
/*     */         case 23:
/* 152 */           i = ByteArray.readU16bit(this.info, pos);
/* 153 */           throwsTarget(pos, i);
/* 154 */           return pos + 2;
/*     */         case 64:
/*     */         case 65:
/* 157 */           len = ByteArray.readU16bit(this.info, pos);
/* 158 */           return localvarTarget(pos + 2, type, len);
/*     */         case 66:
/* 160 */           index = ByteArray.readU16bit(this.info, pos);
/* 161 */           catchTarget(pos, index);
/* 162 */           return pos + 2;
/*     */         case 67:
/*     */         case 68:
/*     */         case 69:
/*     */         case 70:
/* 167 */           offset = ByteArray.readU16bit(this.info, pos);
/* 168 */           offsetTarget(pos, type, offset);
/* 169 */           return pos + 2;
/*     */         case 71:
/*     */         case 72:
/*     */         case 73:
/*     */         case 74:
/*     */         case 75:
/* 175 */           offset = ByteArray.readU16bit(this.info, pos);
/* 176 */           k = this.info[pos + 2] & 0xFF;
/* 177 */           typeArgumentTarget(pos, type, offset, k);
/* 178 */           return pos + 3;
/*     */       } 
/* 180 */       throw new RuntimeException("invalid target type: " + type); }
/*     */ 
/*     */ 
/*     */     
/*     */     void typeParameterTarget(int pos, int targetType, int typeParameterIndex) throws Exception {}
/*     */ 
/*     */     
/*     */     void supertypeTarget(int pos, int superTypeIndex) throws Exception {}
/*     */ 
/*     */     
/*     */     void typeParameterBoundTarget(int pos, int targetType, int typeParameterIndex, int boundIndex) throws Exception {}
/*     */     
/*     */     void emptyTarget(int pos, int targetType) throws Exception {}
/*     */     
/*     */     void formalParameterTarget(int pos, int formalParameterIndex) throws Exception {}
/*     */     
/*     */     void throwsTarget(int pos, int throwsTypeIndex) throws Exception {}
/*     */     
/*     */     int localvarTarget(int pos, int targetType, int tableLength) throws Exception {
/* 199 */       for (int i = 0; i < tableLength; i++) {
/* 200 */         int start = ByteArray.readU16bit(this.info, pos);
/* 201 */         int length = ByteArray.readU16bit(this.info, pos + 2);
/* 202 */         int index = ByteArray.readU16bit(this.info, pos + 4);
/* 203 */         localvarTarget(pos, targetType, start, length, index);
/* 204 */         pos += 6;
/*     */       } 
/*     */       
/* 207 */       return pos;
/*     */     }
/*     */ 
/*     */     
/*     */     void localvarTarget(int pos, int targetType, int startPc, int length, int index) throws Exception {}
/*     */ 
/*     */     
/*     */     void catchTarget(int pos, int exceptionTableIndex) throws Exception {}
/*     */     
/*     */     void offsetTarget(int pos, int targetType, int offset) throws Exception {}
/*     */     
/*     */     void typeArgumentTarget(int pos, int targetType, int offset, int typeArgumentIndex) throws Exception {}
/*     */     
/*     */     final int typePath(int pos) throws Exception {
/* 221 */       int len = this.info[pos++] & 0xFF;
/* 222 */       return typePath(pos, len);
/*     */     }
/*     */     
/*     */     int typePath(int pos, int pathLength) throws Exception {
/* 226 */       for (int i = 0; i < pathLength; i++) {
/* 227 */         int kind = this.info[pos] & 0xFF;
/* 228 */         int index = this.info[pos + 1] & 0xFF;
/* 229 */         typePath(pos, kind, index);
/* 230 */         pos += 2;
/*     */       } 
/*     */       
/* 233 */       return pos;
/*     */     }
/*     */     
/*     */     void typePath(int pos, int typePathKind, int typeArgumentIndex) throws Exception {}
/*     */   }
/*     */   
/*     */   static class Renamer extends AnnotationsAttribute.Renamer {
/*     */     TypeAnnotationsAttribute.SubWalker sub;
/*     */     
/*     */     Renamer(byte[] attrInfo, ConstPool cp, Map map) {
/* 243 */       super(attrInfo, cp, map);
/* 244 */       this.sub = new TypeAnnotationsAttribute.SubWalker(attrInfo);
/*     */     }
/*     */     
/*     */     int annotationArray(int pos, int num) throws Exception {
/* 248 */       for (int i = 0; i < num; i++) {
/* 249 */         int targetType = this.info[pos] & 0xFF;
/* 250 */         pos = this.sub.targetInfo(pos + 1, targetType);
/* 251 */         pos = this.sub.typePath(pos);
/* 252 */         pos = annotation(pos);
/*     */       } 
/*     */       
/* 255 */       return pos;
/*     */     }
/*     */   }
/*     */   
/*     */   static class Copier extends AnnotationsAttribute.Copier {
/*     */     TypeAnnotationsAttribute.SubCopier sub;
/*     */     
/*     */     Copier(byte[] attrInfo, ConstPool src, ConstPool dest, Map map) {
/* 263 */       super(attrInfo, src, dest, map, false);
/* 264 */       TypeAnnotationsWriter w = new TypeAnnotationsWriter(this.output, dest);
/* 265 */       this.writer = (AnnotationsWriter)w;
/* 266 */       this.sub = new TypeAnnotationsAttribute.SubCopier(attrInfo, src, dest, map, w);
/*     */     }
/*     */     
/*     */     int annotationArray(int pos, int num) throws Exception {
/* 270 */       this.writer.numAnnotations(num);
/* 271 */       for (int i = 0; i < num; i++) {
/* 272 */         int targetType = this.info[pos] & 0xFF;
/* 273 */         pos = this.sub.targetInfo(pos + 1, targetType);
/* 274 */         pos = this.sub.typePath(pos);
/* 275 */         pos = annotation(pos);
/*     */       } 
/*     */       
/* 278 */       return pos;
/*     */     }
/*     */   }
/*     */   
/*     */   static class SubCopier
/*     */     extends SubWalker {
/*     */     ConstPool srcPool;
/*     */     ConstPool destPool;
/*     */     Map classnames;
/*     */     TypeAnnotationsWriter writer;
/*     */     
/*     */     SubCopier(byte[] attrInfo, ConstPool src, ConstPool dest, Map map, TypeAnnotationsWriter w) {
/* 290 */       super(attrInfo);
/* 291 */       this.srcPool = src;
/* 292 */       this.destPool = dest;
/* 293 */       this.classnames = map;
/* 294 */       this.writer = w;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void typeParameterTarget(int pos, int targetType, int typeParameterIndex) throws Exception {
/* 300 */       this.writer.typeParameterTarget(targetType, typeParameterIndex);
/*     */     }
/*     */     
/*     */     void supertypeTarget(int pos, int superTypeIndex) throws Exception {
/* 304 */       this.writer.supertypeTarget(superTypeIndex);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void typeParameterBoundTarget(int pos, int targetType, int typeParameterIndex, int boundIndex) throws Exception {
/* 311 */       this.writer.typeParameterBoundTarget(targetType, typeParameterIndex, boundIndex);
/*     */     }
/*     */     
/*     */     void emptyTarget(int pos, int targetType) throws Exception {
/* 315 */       this.writer.emptyTarget(targetType);
/*     */     }
/*     */     
/*     */     void formalParameterTarget(int pos, int formalParameterIndex) throws Exception {
/* 319 */       this.writer.formalParameterTarget(formalParameterIndex);
/*     */     }
/*     */     
/*     */     void throwsTarget(int pos, int throwsTypeIndex) throws Exception {
/* 323 */       this.writer.throwsTarget(throwsTypeIndex);
/*     */     }
/*     */     
/*     */     int localvarTarget(int pos, int targetType, int tableLength) throws Exception {
/* 327 */       this.writer.localVarTarget(targetType, tableLength);
/* 328 */       return super.localvarTarget(pos, targetType, tableLength);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void localvarTarget(int pos, int targetType, int startPc, int length, int index) throws Exception {
/* 334 */       this.writer.localVarTargetTable(startPc, length, index);
/*     */     }
/*     */     
/*     */     void catchTarget(int pos, int exceptionTableIndex) throws Exception {
/* 338 */       this.writer.catchTarget(exceptionTableIndex);
/*     */     }
/*     */     
/*     */     void offsetTarget(int pos, int targetType, int offset) throws Exception {
/* 342 */       this.writer.offsetTarget(targetType, offset);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void typeArgumentTarget(int pos, int targetType, int offset, int typeArgumentIndex) throws Exception {
/* 348 */       this.writer.typeArgumentTarget(targetType, offset, typeArgumentIndex);
/*     */     }
/*     */     
/*     */     int typePath(int pos, int pathLength) throws Exception {
/* 352 */       this.writer.typePath(pathLength);
/* 353 */       return super.typePath(pos, pathLength);
/*     */     }
/*     */     
/*     */     void typePath(int pos, int typePathKind, int typeArgumentIndex) throws Exception {
/* 357 */       this.writer.typePathPath(typePathKind, typeArgumentIndex);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\TypeAnnotationsAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */