/*     */ package javassist.bytecode;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javassist.bytecode.annotation.Annotation;
/*     */ import javassist.bytecode.annotation.AnnotationsWriter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ParameterAnnotationsAttribute
/*     */   extends AttributeInfo
/*     */ {
/*     */   public static final String visibleTag = "RuntimeVisibleParameterAnnotations";
/*     */   public static final String invisibleTag = "RuntimeInvisibleParameterAnnotations";
/*     */   
/*     */   public ParameterAnnotationsAttribute(ConstPool cp, String attrname, byte[] info) {
/*  70 */     super(cp, attrname, info);
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
/*     */   public ParameterAnnotationsAttribute(ConstPool cp, String attrname) {
/*  85 */     this(cp, attrname, new byte[] { 0 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ParameterAnnotationsAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
/*  94 */     super(cp, n, in);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int numParameters() {
/* 101 */     return this.info[0] & 0xFF;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AttributeInfo copy(ConstPool newCp, Map classnames) {
/* 108 */     AnnotationsAttribute.Copier copier = new AnnotationsAttribute.Copier(this.info, this.constPool, newCp, classnames);
/*     */     try {
/* 110 */       copier.parameters();
/* 111 */       return new ParameterAnnotationsAttribute(newCp, getName(), copier
/* 112 */           .close());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Annotation[][] getAnnotations() {
/*     */     try {
/* 133 */       return (new AnnotationsAttribute.Parser(this.info, this.constPool)).parseParameters();
/*     */     }
/* 135 */     catch (Exception e) {
/* 136 */       throw new RuntimeException(e.toString());
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
/*     */   public void setAnnotations(Annotation[][] params) {
/* 150 */     ByteArrayOutputStream output = new ByteArrayOutputStream();
/* 151 */     AnnotationsWriter writer = new AnnotationsWriter(output, this.constPool);
/*     */     try {
/* 153 */       int n = params.length;
/* 154 */       writer.numParameters(n);
/* 155 */       for (int i = 0; i < n; i++) {
/* 156 */         Annotation[] anno = params[i];
/* 157 */         writer.numAnnotations(anno.length);
/* 158 */         for (int j = 0; j < anno.length; j++) {
/* 159 */           anno[j].write(writer);
/*     */         }
/*     */       } 
/* 162 */       writer.close();
/*     */     }
/* 164 */     catch (IOException e) {
/* 165 */       throw new RuntimeException(e);
/*     */     } 
/*     */     
/* 168 */     set(output.toByteArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void renameClass(String oldname, String newname) {
/* 176 */     HashMap<Object, Object> map = new HashMap<Object, Object>();
/* 177 */     map.put(oldname, newname);
/* 178 */     renameClass(map);
/*     */   }
/*     */   
/*     */   void renameClass(Map classnames) {
/* 182 */     AnnotationsAttribute.Renamer renamer = new AnnotationsAttribute.Renamer(this.info, getConstPool(), classnames);
/*     */     try {
/* 184 */       renamer.parameters();
/* 185 */     } catch (Exception e) {
/* 186 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */   void getRefClasses(Map classnames) {
/* 190 */     renameClass(classnames);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 196 */     Annotation[][] aa = getAnnotations();
/* 197 */     StringBuilder sbuf = new StringBuilder();
/* 198 */     int k = 0;
/* 199 */     while (k < aa.length) {
/* 200 */       Annotation[] a = aa[k++];
/* 201 */       int i = 0;
/* 202 */       while (i < a.length) {
/* 203 */         sbuf.append(a[i++].toString());
/* 204 */         if (i != a.length) {
/* 205 */           sbuf.append(" ");
/*     */         }
/*     */       } 
/* 208 */       if (k != aa.length) {
/* 209 */         sbuf.append(", ");
/*     */       }
/*     */     } 
/* 212 */     return sbuf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\ParameterAnnotationsAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */