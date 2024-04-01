/*     */ package javassist.bytecode.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
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
/*     */ public class TypeAnnotationsWriter
/*     */   extends AnnotationsWriter
/*     */ {
/*     */   public TypeAnnotationsWriter(OutputStream os, ConstPool cp) {
/*  23 */     super(os, cp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void numAnnotations(int num) throws IOException {
/*  32 */     super.numAnnotations(num);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void typeParameterTarget(int targetType, int typeParameterIndex) throws IOException {
/*  42 */     this.output.write(targetType);
/*  43 */     this.output.write(typeParameterIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void supertypeTarget(int supertypeIndex) throws IOException {
/*  53 */     this.output.write(16);
/*  54 */     write16bit(supertypeIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void typeParameterBoundTarget(int targetType, int typeParameterIndex, int boundIndex) throws IOException {
/*  64 */     this.output.write(targetType);
/*  65 */     this.output.write(typeParameterIndex);
/*  66 */     this.output.write(boundIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void emptyTarget(int targetType) throws IOException {
/*  74 */     this.output.write(targetType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void formalParameterTarget(int formalParameterIndex) throws IOException {
/*  84 */     this.output.write(22);
/*  85 */     this.output.write(formalParameterIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void throwsTarget(int throwsTypeIndex) throws IOException {
/*  95 */     this.output.write(23);
/*  96 */     write16bit(throwsTypeIndex);
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
/*     */   public void localVarTarget(int targetType, int tableLength) throws IOException {
/* 108 */     this.output.write(targetType);
/* 109 */     write16bit(tableLength);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void localVarTargetTable(int startPc, int length, int index) throws IOException {
/* 119 */     write16bit(startPc);
/* 120 */     write16bit(length);
/* 121 */     write16bit(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void catchTarget(int exceptionTableIndex) throws IOException {
/* 131 */     this.output.write(66);
/* 132 */     write16bit(exceptionTableIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void offsetTarget(int targetType, int offset) throws IOException {
/* 142 */     this.output.write(targetType);
/* 143 */     write16bit(offset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void typeArgumentTarget(int targetType, int offset, int type_argument_index) throws IOException {
/* 153 */     this.output.write(targetType);
/* 154 */     write16bit(offset);
/* 155 */     this.output.write(type_argument_index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void typePath(int pathLength) throws IOException {
/* 162 */     this.output.write(pathLength);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void typePathPath(int typePathKind, int typeArgumentIndex) throws IOException {
/* 171 */     this.output.write(typePathKind);
/* 172 */     this.output.write(typeArgumentIndex);
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\annotation\TypeAnnotationsWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */