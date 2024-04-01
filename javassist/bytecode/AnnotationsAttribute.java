/*     */ package javassist.bytecode;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javassist.bytecode.annotation.Annotation;
/*     */ import javassist.bytecode.annotation.AnnotationMemberValue;
/*     */ import javassist.bytecode.annotation.AnnotationsWriter;
/*     */ import javassist.bytecode.annotation.ArrayMemberValue;
/*     */ import javassist.bytecode.annotation.BooleanMemberValue;
/*     */ import javassist.bytecode.annotation.ByteMemberValue;
/*     */ import javassist.bytecode.annotation.CharMemberValue;
/*     */ import javassist.bytecode.annotation.ClassMemberValue;
/*     */ import javassist.bytecode.annotation.DoubleMemberValue;
/*     */ import javassist.bytecode.annotation.EnumMemberValue;
/*     */ import javassist.bytecode.annotation.FloatMemberValue;
/*     */ import javassist.bytecode.annotation.IntegerMemberValue;
/*     */ import javassist.bytecode.annotation.LongMemberValue;
/*     */ import javassist.bytecode.annotation.MemberValue;
/*     */ import javassist.bytecode.annotation.ShortMemberValue;
/*     */ import javassist.bytecode.annotation.StringMemberValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnnotationsAttribute
/*     */   extends AttributeInfo
/*     */ {
/*     */   public static final String visibleTag = "RuntimeVisibleAnnotations";
/*     */   public static final String invisibleTag = "RuntimeInvisibleAnnotations";
/*     */   
/*     */   public AnnotationsAttribute(ConstPool cp, String attrname, byte[] info) {
/* 126 */     super(cp, attrname, info);
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
/*     */   public AnnotationsAttribute(ConstPool cp, String attrname) {
/* 141 */     this(cp, attrname, new byte[] { 0, 0 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   AnnotationsAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
/* 150 */     super(cp, n, in);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int numAnnotations() {
/* 157 */     return ByteArray.readU16bit(this.info, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AttributeInfo copy(ConstPool newCp, Map classnames) {
/* 164 */     Copier copier = new Copier(this.info, this.constPool, newCp, classnames);
/*     */     try {
/* 166 */       copier.annotationArray();
/* 167 */       return new AnnotationsAttribute(newCp, getName(), copier.close());
/*     */     }
/* 169 */     catch (Exception e) {
/* 170 */       throw new RuntimeException(e);
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
/*     */   public Annotation getAnnotation(String type) {
/* 184 */     Annotation[] annotations = getAnnotations();
/* 185 */     for (int i = 0; i < annotations.length; i++) {
/* 186 */       if (annotations[i].getTypeName().equals(type)) {
/* 187 */         return annotations[i];
/*     */       }
/*     */     } 
/* 190 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAnnotation(Annotation annotation) {
/* 200 */     String type = annotation.getTypeName();
/* 201 */     Annotation[] annotations = getAnnotations();
/* 202 */     for (int i = 0; i < annotations.length; i++) {
/* 203 */       if (annotations[i].getTypeName().equals(type)) {
/* 204 */         annotations[i] = annotation;
/* 205 */         setAnnotations(annotations);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 210 */     Annotation[] newlist = new Annotation[annotations.length + 1];
/* 211 */     System.arraycopy(annotations, 0, newlist, 0, annotations.length);
/* 212 */     newlist[annotations.length] = annotation;
/* 213 */     setAnnotations(newlist);
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
/*     */   public boolean removeAnnotation(String type) {
/* 226 */     Annotation[] annotations = getAnnotations();
/* 227 */     for (int i = 0; i < annotations.length; i++) {
/* 228 */       if (annotations[i].getTypeName().equals(type)) {
/* 229 */         Annotation[] newlist = new Annotation[annotations.length - 1];
/* 230 */         System.arraycopy(annotations, 0, newlist, 0, i);
/* 231 */         if (i < annotations.length - 1) {
/* 232 */           System.arraycopy(annotations, i + 1, newlist, i, annotations.length - i - 1);
/*     */         }
/*     */         
/* 235 */         setAnnotations(newlist);
/* 236 */         return true;
/*     */       } 
/*     */     } 
/* 239 */     return false;
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
/*     */   public Annotation[] getAnnotations() {
/*     */     try {
/* 253 */       return (new Parser(this.info, this.constPool)).parseAnnotations();
/*     */     }
/* 255 */     catch (Exception e) {
/* 256 */       throw new RuntimeException(e);
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
/*     */   public void setAnnotations(Annotation[] annotations) {
/* 268 */     ByteArrayOutputStream output = new ByteArrayOutputStream();
/* 269 */     AnnotationsWriter writer = new AnnotationsWriter(output, this.constPool);
/*     */     try {
/* 271 */       int n = annotations.length;
/* 272 */       writer.numAnnotations(n);
/* 273 */       for (int i = 0; i < n; i++) {
/* 274 */         annotations[i].write(writer);
/*     */       }
/* 276 */       writer.close();
/*     */     }
/* 278 */     catch (IOException e) {
/* 279 */       throw new RuntimeException(e);
/*     */     } 
/*     */     
/* 282 */     set(output.toByteArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAnnotation(Annotation annotation) {
/* 293 */     setAnnotations(new Annotation[] { annotation });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void renameClass(String oldname, String newname) {
/* 301 */     HashMap<Object, Object> map = new HashMap<Object, Object>();
/* 302 */     map.put(oldname, newname);
/* 303 */     renameClass(map);
/*     */   }
/*     */   
/*     */   void renameClass(Map classnames) {
/* 307 */     Renamer renamer = new Renamer(this.info, getConstPool(), classnames);
/*     */     try {
/* 309 */       renamer.annotationArray();
/* 310 */     } catch (Exception e) {
/* 311 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */   void getRefClasses(Map classnames) {
/* 315 */     renameClass(classnames);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 321 */     Annotation[] a = getAnnotations();
/* 322 */     StringBuilder sbuf = new StringBuilder();
/* 323 */     int i = 0;
/* 324 */     while (i < a.length) {
/* 325 */       sbuf.append(a[i++].toString());
/* 326 */       if (i != a.length) {
/* 327 */         sbuf.append(", ");
/*     */       }
/*     */     } 
/* 330 */     return sbuf.toString();
/*     */   }
/*     */   
/*     */   static class Walker {
/*     */     byte[] info;
/*     */     
/*     */     Walker(byte[] attrInfo) {
/* 337 */       this.info = attrInfo;
/*     */     }
/*     */     
/*     */     final void parameters() throws Exception {
/* 341 */       int numParam = this.info[0] & 0xFF;
/* 342 */       parameters(numParam, 1);
/*     */     }
/*     */     
/*     */     void parameters(int numParam, int pos) throws Exception {
/* 346 */       for (int i = 0; i < numParam; i++)
/* 347 */         pos = annotationArray(pos); 
/*     */     }
/*     */     
/*     */     final void annotationArray() throws Exception {
/* 351 */       annotationArray(0);
/*     */     }
/*     */     
/*     */     final int annotationArray(int pos) throws Exception {
/* 355 */       int num = ByteArray.readU16bit(this.info, pos);
/* 356 */       return annotationArray(pos + 2, num);
/*     */     }
/*     */     
/*     */     int annotationArray(int pos, int num) throws Exception {
/* 360 */       for (int i = 0; i < num; i++) {
/* 361 */         pos = annotation(pos);
/*     */       }
/* 363 */       return pos;
/*     */     }
/*     */     
/*     */     final int annotation(int pos) throws Exception {
/* 367 */       int type = ByteArray.readU16bit(this.info, pos);
/* 368 */       int numPairs = ByteArray.readU16bit(this.info, pos + 2);
/* 369 */       return annotation(pos + 4, type, numPairs);
/*     */     }
/*     */     
/*     */     int annotation(int pos, int type, int numPairs) throws Exception {
/* 373 */       for (int j = 0; j < numPairs; j++) {
/* 374 */         pos = memberValuePair(pos);
/*     */       }
/* 376 */       return pos;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final int memberValuePair(int pos) throws Exception {
/* 383 */       int nameIndex = ByteArray.readU16bit(this.info, pos);
/* 384 */       return memberValuePair(pos + 2, nameIndex);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int memberValuePair(int pos, int nameIndex) throws Exception {
/* 391 */       return memberValue(pos);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final int memberValue(int pos) throws Exception {
/* 398 */       int tag = this.info[pos] & 0xFF;
/* 399 */       if (tag == 101) {
/* 400 */         int typeNameIndex = ByteArray.readU16bit(this.info, pos + 1);
/* 401 */         int constNameIndex = ByteArray.readU16bit(this.info, pos + 3);
/* 402 */         enumMemberValue(pos, typeNameIndex, constNameIndex);
/* 403 */         return pos + 5;
/*     */       } 
/* 405 */       if (tag == 99) {
/* 406 */         int i = ByteArray.readU16bit(this.info, pos + 1);
/* 407 */         classMemberValue(pos, i);
/* 408 */         return pos + 3;
/*     */       } 
/* 410 */       if (tag == 64)
/* 411 */         return annotationMemberValue(pos + 1); 
/* 412 */       if (tag == 91) {
/* 413 */         int num = ByteArray.readU16bit(this.info, pos + 1);
/* 414 */         return arrayMemberValue(pos + 3, num);
/*     */       } 
/*     */       
/* 417 */       int index = ByteArray.readU16bit(this.info, pos + 1);
/* 418 */       constValueMember(tag, index);
/* 419 */       return pos + 3;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void constValueMember(int tag, int index) throws Exception {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void enumMemberValue(int pos, int typeNameIndex, int constNameIndex) throws Exception {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void classMemberValue(int pos, int index) throws Exception {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int annotationMemberValue(int pos) throws Exception {
/* 444 */       return annotation(pos);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int arrayMemberValue(int pos, int num) throws Exception {
/* 451 */       for (int i = 0; i < num; i++) {
/* 452 */         pos = memberValue(pos);
/*     */       }
/*     */       
/* 455 */       return pos;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class Renamer
/*     */     extends Walker
/*     */   {
/*     */     ConstPool cpool;
/*     */ 
/*     */     
/*     */     Map classnames;
/*     */ 
/*     */ 
/*     */     
/*     */     Renamer(byte[] info, ConstPool cp, Map map) {
/* 473 */       super(info);
/* 474 */       this.cpool = cp;
/* 475 */       this.classnames = map;
/*     */     }
/*     */     
/*     */     int annotation(int pos, int type, int numPairs) throws Exception {
/* 479 */       renameType(pos - 4, type);
/* 480 */       return super.annotation(pos, type, numPairs);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void enumMemberValue(int pos, int typeNameIndex, int constNameIndex) throws Exception {
/* 486 */       renameType(pos + 1, typeNameIndex);
/* 487 */       super.enumMemberValue(pos, typeNameIndex, constNameIndex);
/*     */     }
/*     */     
/*     */     void classMemberValue(int pos, int index) throws Exception {
/* 491 */       renameType(pos + 1, index);
/* 492 */       super.classMemberValue(pos, index);
/*     */     }
/*     */     
/*     */     private void renameType(int pos, int index) {
/* 496 */       String name = this.cpool.getUtf8Info(index);
/* 497 */       String newName = Descriptor.rename(name, this.classnames);
/* 498 */       if (!name.equals(newName)) {
/* 499 */         int index2 = this.cpool.addUtf8Info(newName);
/* 500 */         ByteArray.write16bit(index2, this.info, pos);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class Copier
/*     */     extends Walker
/*     */   {
/*     */     ByteArrayOutputStream output;
/*     */ 
/*     */     
/*     */     AnnotationsWriter writer;
/*     */     
/*     */     ConstPool srcPool;
/*     */     
/*     */     ConstPool destPool;
/*     */     
/*     */     Map classnames;
/*     */ 
/*     */     
/*     */     Copier(byte[] info, ConstPool src, ConstPool dest, Map map) {
/* 523 */       this(info, src, dest, map, true);
/*     */     }
/*     */     
/*     */     Copier(byte[] info, ConstPool src, ConstPool dest, Map map, boolean makeWriter) {
/* 527 */       super(info);
/* 528 */       this.output = new ByteArrayOutputStream();
/* 529 */       if (makeWriter) {
/* 530 */         this.writer = new AnnotationsWriter(this.output, dest);
/*     */       }
/* 532 */       this.srcPool = src;
/* 533 */       this.destPool = dest;
/* 534 */       this.classnames = map;
/*     */     }
/*     */     
/*     */     byte[] close() throws IOException {
/* 538 */       this.writer.close();
/* 539 */       return this.output.toByteArray();
/*     */     }
/*     */     
/*     */     void parameters(int numParam, int pos) throws Exception {
/* 543 */       this.writer.numParameters(numParam);
/* 544 */       super.parameters(numParam, pos);
/*     */     }
/*     */     
/*     */     int annotationArray(int pos, int num) throws Exception {
/* 548 */       this.writer.numAnnotations(num);
/* 549 */       return super.annotationArray(pos, num);
/*     */     }
/*     */     
/*     */     int annotation(int pos, int type, int numPairs) throws Exception {
/* 553 */       this.writer.annotation(copyType(type), numPairs);
/* 554 */       return super.annotation(pos, type, numPairs);
/*     */     }
/*     */     
/*     */     int memberValuePair(int pos, int nameIndex) throws Exception {
/* 558 */       this.writer.memberValuePair(copy(nameIndex));
/* 559 */       return super.memberValuePair(pos, nameIndex);
/*     */     }
/*     */     
/*     */     void constValueMember(int tag, int index) throws Exception {
/* 563 */       this.writer.constValueIndex(tag, copy(index));
/* 564 */       super.constValueMember(tag, index);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void enumMemberValue(int pos, int typeNameIndex, int constNameIndex) throws Exception {
/* 570 */       this.writer.enumConstValue(copyType(typeNameIndex), copy(constNameIndex));
/* 571 */       super.enumMemberValue(pos, typeNameIndex, constNameIndex);
/*     */     }
/*     */     
/*     */     void classMemberValue(int pos, int index) throws Exception {
/* 575 */       this.writer.classInfoIndex(copyType(index));
/* 576 */       super.classMemberValue(pos, index);
/*     */     }
/*     */     
/*     */     int annotationMemberValue(int pos) throws Exception {
/* 580 */       this.writer.annotationValue();
/* 581 */       return super.annotationMemberValue(pos);
/*     */     }
/*     */     
/*     */     int arrayMemberValue(int pos, int num) throws Exception {
/* 585 */       this.writer.arrayValue(num);
/* 586 */       return super.arrayMemberValue(pos, num);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int copy(int srcIndex) {
/* 599 */       return this.srcPool.copy(srcIndex, this.destPool, this.classnames);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int copyType(int srcIndex) {
/* 613 */       String name = this.srcPool.getUtf8Info(srcIndex);
/* 614 */       String newName = Descriptor.rename(name, this.classnames);
/* 615 */       return this.destPool.addUtf8Info(newName);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class Parser
/*     */     extends Walker
/*     */   {
/*     */     ConstPool pool;
/*     */     
/*     */     Annotation[][] allParams;
/*     */     
/*     */     Annotation[] allAnno;
/*     */     
/*     */     Annotation currentAnno;
/*     */     
/*     */     MemberValue currentMember;
/*     */     
/*     */     Parser(byte[] info, ConstPool cp) {
/* 634 */       super(info);
/* 635 */       this.pool = cp;
/*     */     }
/*     */     
/*     */     Annotation[][] parseParameters() throws Exception {
/* 639 */       parameters();
/* 640 */       return this.allParams;
/*     */     }
/*     */     
/*     */     Annotation[] parseAnnotations() throws Exception {
/* 644 */       annotationArray();
/* 645 */       return this.allAnno;
/*     */     }
/*     */     
/*     */     MemberValue parseMemberValue() throws Exception {
/* 649 */       memberValue(0);
/* 650 */       return this.currentMember;
/*     */     }
/*     */     
/*     */     void parameters(int numParam, int pos) throws Exception {
/* 654 */       Annotation[][] params = new Annotation[numParam][];
/* 655 */       for (int i = 0; i < numParam; i++) {
/* 656 */         pos = annotationArray(pos);
/* 657 */         params[i] = this.allAnno;
/*     */       } 
/*     */       
/* 660 */       this.allParams = params;
/*     */     }
/*     */     
/*     */     int annotationArray(int pos, int num) throws Exception {
/* 664 */       Annotation[] array = new Annotation[num];
/* 665 */       for (int i = 0; i < num; i++) {
/* 666 */         pos = annotation(pos);
/* 667 */         array[i] = this.currentAnno;
/*     */       } 
/*     */       
/* 670 */       this.allAnno = array;
/* 671 */       return pos;
/*     */     }
/*     */     
/*     */     int annotation(int pos, int type, int numPairs) throws Exception {
/* 675 */       this.currentAnno = new Annotation(type, this.pool);
/* 676 */       return super.annotation(pos, type, numPairs);
/*     */     }
/*     */     
/*     */     int memberValuePair(int pos, int nameIndex) throws Exception {
/* 680 */       pos = super.memberValuePair(pos, nameIndex);
/* 681 */       this.currentAnno.addMemberValue(nameIndex, this.currentMember);
/* 682 */       return pos; } void constValueMember(int tag, int index) throws Exception { ByteMemberValue byteMemberValue; CharMemberValue charMemberValue; DoubleMemberValue doubleMemberValue; FloatMemberValue floatMemberValue; IntegerMemberValue integerMemberValue;
/*     */       LongMemberValue longMemberValue;
/*     */       ShortMemberValue shortMemberValue;
/*     */       BooleanMemberValue booleanMemberValue;
/*     */       StringMemberValue stringMemberValue;
/* 687 */       ConstPool cp = this.pool;
/* 688 */       switch (tag) {
/*     */         case 66:
/* 690 */           byteMemberValue = new ByteMemberValue(index, cp);
/*     */           break;
/*     */         case 67:
/* 693 */           charMemberValue = new CharMemberValue(index, cp);
/*     */           break;
/*     */         case 68:
/* 696 */           doubleMemberValue = new DoubleMemberValue(index, cp);
/*     */           break;
/*     */         case 70:
/* 699 */           floatMemberValue = new FloatMemberValue(index, cp);
/*     */           break;
/*     */         case 73:
/* 702 */           integerMemberValue = new IntegerMemberValue(index, cp);
/*     */           break;
/*     */         case 74:
/* 705 */           longMemberValue = new LongMemberValue(index, cp);
/*     */           break;
/*     */         case 83:
/* 708 */           shortMemberValue = new ShortMemberValue(index, cp);
/*     */           break;
/*     */         case 90:
/* 711 */           booleanMemberValue = new BooleanMemberValue(index, cp);
/*     */           break;
/*     */         case 115:
/* 714 */           stringMemberValue = new StringMemberValue(index, cp);
/*     */           break;
/*     */         default:
/* 717 */           throw new RuntimeException("unknown tag:" + tag);
/*     */       } 
/*     */       
/* 720 */       this.currentMember = (MemberValue)stringMemberValue;
/* 721 */       super.constValueMember(tag, index); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void enumMemberValue(int pos, int typeNameIndex, int constNameIndex) throws Exception {
/* 727 */       this.currentMember = (MemberValue)new EnumMemberValue(typeNameIndex, constNameIndex, this.pool);
/*     */       
/* 729 */       super.enumMemberValue(pos, typeNameIndex, constNameIndex);
/*     */     }
/*     */     
/*     */     void classMemberValue(int pos, int index) throws Exception {
/* 733 */       this.currentMember = (MemberValue)new ClassMemberValue(index, this.pool);
/* 734 */       super.classMemberValue(pos, index);
/*     */     }
/*     */     
/*     */     int annotationMemberValue(int pos) throws Exception {
/* 738 */       Annotation anno = this.currentAnno;
/* 739 */       pos = super.annotationMemberValue(pos);
/* 740 */       this.currentMember = (MemberValue)new AnnotationMemberValue(this.currentAnno, this.pool);
/* 741 */       this.currentAnno = anno;
/* 742 */       return pos;
/*     */     }
/*     */     
/*     */     int arrayMemberValue(int pos, int num) throws Exception {
/* 746 */       ArrayMemberValue amv = new ArrayMemberValue(this.pool);
/* 747 */       MemberValue[] elements = new MemberValue[num];
/* 748 */       for (int i = 0; i < num; i++) {
/* 749 */         pos = memberValue(pos);
/* 750 */         elements[i] = this.currentMember;
/*     */       } 
/*     */       
/* 753 */       amv.setValue(elements);
/* 754 */       this.currentMember = (MemberValue)amv;
/* 755 */       return pos;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\AnnotationsAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */