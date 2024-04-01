/*     */ package javassist.bytecode;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import javassist.bytecode.annotation.AnnotationsWriter;
/*     */ import javassist.bytecode.annotation.MemberValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnnotationDefaultAttribute
/*     */   extends AttributeInfo
/*     */ {
/*     */   public static final String tag = "AnnotationDefault";
/*     */   
/*     */   public AnnotationDefaultAttribute(ConstPool cp, byte[] info) {
/*  81 */     super(cp, "AnnotationDefault", info);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotationDefaultAttribute(ConstPool cp) {
/*  92 */     this(cp, new byte[] { 0, 0 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   AnnotationDefaultAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
/* 101 */     super(cp, n, in);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AttributeInfo copy(ConstPool newCp, Map classnames) {
/* 108 */     AnnotationsAttribute.Copier copier = new AnnotationsAttribute.Copier(this.info, this.constPool, newCp, classnames);
/*     */     
/*     */     try {
/* 111 */       copier.memberValue(0);
/* 112 */       return new AnnotationDefaultAttribute(newCp, copier.close());
/*     */     }
/* 114 */     catch (Exception e) {
/* 115 */       throw new RuntimeException(e.toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MemberValue getDefaultValue() {
/*     */     try {
/* 126 */       return (new AnnotationsAttribute.Parser(this.info, this.constPool)).parseMemberValue();
/*     */     }
/* 128 */     catch (Exception e) {
/* 129 */       throw new RuntimeException(e.toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultValue(MemberValue value) {
/* 140 */     ByteArrayOutputStream output = new ByteArrayOutputStream();
/* 141 */     AnnotationsWriter writer = new AnnotationsWriter(output, this.constPool);
/*     */     try {
/* 143 */       value.write(writer);
/* 144 */       writer.close();
/*     */     }
/* 146 */     catch (IOException e) {
/* 147 */       throw new RuntimeException(e);
/*     */     } 
/*     */     
/* 150 */     set(output.toByteArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 158 */     return getDefaultValue().toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\AnnotationDefaultAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */