/*     */ package javassist.bytecode;
/*     */ 
/*     */ import java.util.Map;
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtClass;
/*     */ import javassist.CtPrimitiveType;
/*     */ import javassist.NotFoundException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Descriptor
/*     */ {
/*     */   public static String toJvmName(String classname) {
/*  39 */     return classname.replace('.', '/');
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
/*     */   public static String toJavaName(String classname) {
/*  52 */     return classname.replace('/', '.');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toJvmName(CtClass clazz) {
/*  60 */     if (clazz.isArray()) {
/*  61 */       return of(clazz);
/*     */     }
/*  63 */     return toJvmName(clazz.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toClassName(String descriptor) {
/*     */     String name;
/*  72 */     int arrayDim = 0;
/*  73 */     int i = 0;
/*  74 */     char c = descriptor.charAt(0);
/*  75 */     while (c == '[') {
/*  76 */       arrayDim++;
/*  77 */       c = descriptor.charAt(++i);
/*     */     } 
/*     */ 
/*     */     
/*  81 */     if (c == 'L') {
/*  82 */       int i2 = descriptor.indexOf(';', i++);
/*  83 */       name = descriptor.substring(i, i2).replace('/', '.');
/*  84 */       i = i2;
/*     */     }
/*  86 */     else if (c == 'V') {
/*  87 */       name = "void";
/*  88 */     } else if (c == 'I') {
/*  89 */       name = "int";
/*  90 */     } else if (c == 'B') {
/*  91 */       name = "byte";
/*  92 */     } else if (c == 'J') {
/*  93 */       name = "long";
/*  94 */     } else if (c == 'D') {
/*  95 */       name = "double";
/*  96 */     } else if (c == 'F') {
/*  97 */       name = "float";
/*  98 */     } else if (c == 'C') {
/*  99 */       name = "char";
/* 100 */     } else if (c == 'S') {
/* 101 */       name = "short";
/* 102 */     } else if (c == 'Z') {
/* 103 */       name = "boolean";
/*     */     } else {
/* 105 */       throw new RuntimeException("bad descriptor: " + descriptor);
/*     */     } 
/* 107 */     if (i + 1 != descriptor.length()) {
/* 108 */       throw new RuntimeException("multiple descriptors?: " + descriptor);
/*     */     }
/* 110 */     if (arrayDim == 0) {
/* 111 */       return name;
/*     */     }
/* 113 */     StringBuffer sbuf = new StringBuffer(name);
/*     */     do {
/* 115 */       sbuf.append("[]");
/* 116 */     } while (--arrayDim > 0);
/*     */     
/* 118 */     return sbuf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String of(String classname) {
/* 126 */     if (classname.equals("void"))
/* 127 */       return "V"; 
/* 128 */     if (classname.equals("int"))
/* 129 */       return "I"; 
/* 130 */     if (classname.equals("byte"))
/* 131 */       return "B"; 
/* 132 */     if (classname.equals("long"))
/* 133 */       return "J"; 
/* 134 */     if (classname.equals("double"))
/* 135 */       return "D"; 
/* 136 */     if (classname.equals("float"))
/* 137 */       return "F"; 
/* 138 */     if (classname.equals("char"))
/* 139 */       return "C"; 
/* 140 */     if (classname.equals("short"))
/* 141 */       return "S"; 
/* 142 */     if (classname.equals("boolean")) {
/* 143 */       return "Z";
/*     */     }
/* 145 */     return "L" + toJvmName(classname) + ";";
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
/*     */   public static String rename(String desc, String oldname, String newname) {
/* 159 */     if (desc.indexOf(oldname) < 0) {
/* 160 */       return desc;
/*     */     }
/* 162 */     StringBuffer newdesc = new StringBuffer();
/* 163 */     int head = 0;
/* 164 */     int i = 0;
/*     */     while (true) {
/* 166 */       int j = desc.indexOf('L', i);
/* 167 */       if (j < 0)
/*     */         break; 
/* 169 */       if (desc.startsWith(oldname, j + 1) && desc
/* 170 */         .charAt(j + oldname.length() + 1) == ';') {
/* 171 */         newdesc.append(desc.substring(head, j));
/* 172 */         newdesc.append('L');
/* 173 */         newdesc.append(newname);
/* 174 */         newdesc.append(';');
/* 175 */         head = i = j + oldname.length() + 2;
/*     */         continue;
/*     */       } 
/* 178 */       i = desc.indexOf(';', j) + 1;
/* 179 */       if (i < 1) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/* 184 */     if (head == 0) {
/* 185 */       return desc;
/*     */     }
/* 187 */     int len = desc.length();
/* 188 */     if (head < len) {
/* 189 */       newdesc.append(desc.substring(head, len));
/*     */     }
/* 191 */     return newdesc.toString();
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
/*     */   public static String rename(String desc, Map map) {
/* 204 */     if (map == null) {
/* 205 */       return desc;
/*     */     }
/* 207 */     StringBuffer newdesc = new StringBuffer();
/* 208 */     int head = 0;
/* 209 */     int i = 0;
/*     */     while (true) {
/* 211 */       int j = desc.indexOf('L', i);
/* 212 */       if (j < 0) {
/*     */         break;
/*     */       }
/* 215 */       int k = desc.indexOf(';', j);
/* 216 */       if (k < 0) {
/*     */         break;
/*     */       }
/* 219 */       i = k + 1;
/* 220 */       String name = desc.substring(j + 1, k);
/* 221 */       String name2 = (String)map.get(name);
/* 222 */       if (name2 != null) {
/* 223 */         newdesc.append(desc.substring(head, j));
/* 224 */         newdesc.append('L');
/* 225 */         newdesc.append(name2);
/* 226 */         newdesc.append(';');
/* 227 */         head = i;
/*     */       } 
/*     */     } 
/*     */     
/* 231 */     if (head == 0) {
/* 232 */       return desc;
/*     */     }
/* 234 */     int len = desc.length();
/* 235 */     if (head < len) {
/* 236 */       newdesc.append(desc.substring(head, len));
/*     */     }
/* 238 */     return newdesc.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String of(CtClass type) {
/* 246 */     StringBuffer sbuf = new StringBuffer();
/* 247 */     toDescriptor(sbuf, type);
/* 248 */     return sbuf.toString();
/*     */   }
/*     */   
/*     */   private static void toDescriptor(StringBuffer desc, CtClass type) {
/* 252 */     if (type.isArray()) {
/* 253 */       desc.append('[');
/*     */       try {
/* 255 */         toDescriptor(desc, type.getComponentType());
/*     */       }
/* 257 */       catch (NotFoundException e) {
/* 258 */         desc.append('L');
/* 259 */         String name = type.getName();
/* 260 */         desc.append(toJvmName(name.substring(0, name.length() - 2)));
/* 261 */         desc.append(';');
/*     */       }
/*     */     
/* 264 */     } else if (type.isPrimitive()) {
/* 265 */       CtPrimitiveType pt = (CtPrimitiveType)type;
/* 266 */       desc.append(pt.getDescriptor());
/*     */     } else {
/*     */       
/* 269 */       desc.append('L');
/* 270 */       desc.append(type.getName().replace('.', '/'));
/* 271 */       desc.append(';');
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String ofConstructor(CtClass[] paramTypes) {
/* 282 */     return ofMethod(CtClass.voidType, paramTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String ofMethod(CtClass returnType, CtClass[] paramTypes) {
/* 293 */     StringBuffer desc = new StringBuffer();
/* 294 */     desc.append('(');
/* 295 */     if (paramTypes != null) {
/* 296 */       int n = paramTypes.length;
/* 297 */       for (int i = 0; i < n; i++) {
/* 298 */         toDescriptor(desc, paramTypes[i]);
/*     */       }
/*     */     } 
/* 301 */     desc.append(')');
/* 302 */     if (returnType != null) {
/* 303 */       toDescriptor(desc, returnType);
/*     */     }
/* 305 */     return desc.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String ofParameters(CtClass[] paramTypes) {
/* 316 */     return ofMethod(null, paramTypes);
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
/*     */   public static String appendParameter(String classname, String desc) {
/* 329 */     int i = desc.indexOf(')');
/* 330 */     if (i < 0) {
/* 331 */       return desc;
/*     */     }
/* 333 */     StringBuffer newdesc = new StringBuffer();
/* 334 */     newdesc.append(desc.substring(0, i));
/* 335 */     newdesc.append('L');
/* 336 */     newdesc.append(classname.replace('.', '/'));
/* 337 */     newdesc.append(';');
/* 338 */     newdesc.append(desc.substring(i));
/* 339 */     return newdesc.toString();
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
/*     */   public static String insertParameter(String classname, String desc) {
/* 354 */     if (desc.charAt(0) != '(') {
/* 355 */       return desc;
/*     */     }
/* 357 */     return "(L" + classname.replace('.', '/') + ';' + desc
/* 358 */       .substring(1);
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
/*     */   public static String appendParameter(CtClass type, String descriptor) {
/* 370 */     int i = descriptor.indexOf(')');
/* 371 */     if (i < 0) {
/* 372 */       return descriptor;
/*     */     }
/* 374 */     StringBuffer newdesc = new StringBuffer();
/* 375 */     newdesc.append(descriptor.substring(0, i));
/* 376 */     toDescriptor(newdesc, type);
/* 377 */     newdesc.append(descriptor.substring(i));
/* 378 */     return newdesc.toString();
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
/*     */   public static String insertParameter(CtClass type, String descriptor) {
/* 392 */     if (descriptor.charAt(0) != '(') {
/* 393 */       return descriptor;
/*     */     }
/* 395 */     return "(" + of(type) + descriptor.substring(1);
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
/*     */   public static String changeReturnType(String classname, String desc) {
/* 407 */     int i = desc.indexOf(')');
/* 408 */     if (i < 0) {
/* 409 */       return desc;
/*     */     }
/* 411 */     StringBuffer newdesc = new StringBuffer();
/* 412 */     newdesc.append(desc.substring(0, i + 1));
/* 413 */     newdesc.append('L');
/* 414 */     newdesc.append(classname.replace('.', '/'));
/* 415 */     newdesc.append(';');
/* 416 */     return newdesc.toString();
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
/*     */   public static CtClass[] getParameterTypes(String desc, ClassPool cp) throws NotFoundException {
/* 431 */     if (desc.charAt(0) != '(') {
/* 432 */       return null;
/*     */     }
/* 434 */     int num = numOfParameters(desc);
/* 435 */     CtClass[] args = new CtClass[num];
/* 436 */     int n = 0;
/* 437 */     int i = 1;
/*     */     while (true) {
/* 439 */       i = toCtClass(cp, desc, i, args, n++);
/* 440 */       if (i <= 0) {
/* 441 */         return args;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean eqParamTypes(String desc1, String desc2) {
/* 451 */     if (desc1.charAt(0) != '(') {
/* 452 */       return false;
/*     */     }
/* 454 */     for (int i = 0;; i++) {
/* 455 */       char c = desc1.charAt(i);
/* 456 */       if (c != desc2.charAt(i)) {
/* 457 */         return false;
/*     */       }
/* 459 */       if (c == ')') {
/* 460 */         return true;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getParamDescriptor(String decl) {
/* 470 */     return decl.substring(0, decl.indexOf(')') + 1);
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
/*     */   public static CtClass getReturnType(String desc, ClassPool cp) throws NotFoundException {
/* 484 */     int i = desc.indexOf(')');
/* 485 */     if (i < 0) {
/* 486 */       return null;
/*     */     }
/* 488 */     CtClass[] type = new CtClass[1];
/* 489 */     toCtClass(cp, desc, i + 1, type, 0);
/* 490 */     return type[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int numOfParameters(String desc) {
/* 501 */     int n = 0;
/* 502 */     int i = 1;
/*     */     while (true) {
/* 504 */       char c = desc.charAt(i);
/* 505 */       if (c == ')') {
/*     */         break;
/*     */       }
/* 508 */       while (c == '[') {
/* 509 */         c = desc.charAt(++i);
/*     */       }
/* 511 */       if (c == 'L') {
/* 512 */         i = desc.indexOf(';', i) + 1;
/* 513 */         if (i <= 0) {
/* 514 */           throw new IndexOutOfBoundsException("bad descriptor");
/*     */         }
/*     */       } else {
/* 517 */         i++;
/*     */       } 
/* 519 */       n++;
/*     */     } 
/*     */     
/* 522 */     return n;
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
/*     */ 
/*     */   
/*     */   public static CtClass toCtClass(String desc, ClassPool cp) throws NotFoundException {
/* 541 */     CtClass[] clazz = new CtClass[1];
/* 542 */     int res = toCtClass(cp, desc, 0, clazz, 0);
/* 543 */     if (res >= 0) {
/* 544 */       return clazz[0];
/*     */     }
/*     */ 
/*     */     
/* 548 */     return cp.get(desc.replace('/', '.'));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int toCtClass(ClassPool cp, String desc, int i, CtClass[] args, int n) throws NotFoundException {
/*     */     int i2;
/*     */     String name;
/* 559 */     int arrayDim = 0;
/* 560 */     char c = desc.charAt(i);
/* 561 */     while (c == '[') {
/* 562 */       arrayDim++;
/* 563 */       c = desc.charAt(++i);
/*     */     } 
/*     */     
/* 566 */     if (c == 'L') {
/* 567 */       i2 = desc.indexOf(';', ++i);
/* 568 */       name = desc.substring(i, i2++).replace('/', '.');
/*     */     } else {
/*     */       
/* 571 */       CtClass type = toPrimitiveClass(c);
/* 572 */       if (type == null) {
/* 573 */         return -1;
/*     */       }
/* 575 */       i2 = i + 1;
/* 576 */       if (arrayDim == 0) {
/* 577 */         args[n] = type;
/* 578 */         return i2;
/*     */       } 
/*     */       
/* 581 */       name = type.getName();
/*     */     } 
/*     */     
/* 584 */     if (arrayDim > 0) {
/* 585 */       StringBuffer sbuf = new StringBuffer(name);
/* 586 */       while (arrayDim-- > 0) {
/* 587 */         sbuf.append("[]");
/*     */       }
/* 589 */       name = sbuf.toString();
/*     */     } 
/*     */     
/* 592 */     args[n] = cp.get(name);
/* 593 */     return i2;
/*     */   }
/*     */   
/*     */   static CtClass toPrimitiveClass(char c) {
/* 597 */     CtClass type = null;
/* 598 */     switch (c) {
/*     */       case 'Z':
/* 600 */         type = CtClass.booleanType;
/*     */         break;
/*     */       case 'C':
/* 603 */         type = CtClass.charType;
/*     */         break;
/*     */       case 'B':
/* 606 */         type = CtClass.byteType;
/*     */         break;
/*     */       case 'S':
/* 609 */         type = CtClass.shortType;
/*     */         break;
/*     */       case 'I':
/* 612 */         type = CtClass.intType;
/*     */         break;
/*     */       case 'J':
/* 615 */         type = CtClass.longType;
/*     */         break;
/*     */       case 'F':
/* 618 */         type = CtClass.floatType;
/*     */         break;
/*     */       case 'D':
/* 621 */         type = CtClass.doubleType;
/*     */         break;
/*     */       case 'V':
/* 624 */         type = CtClass.voidType;
/*     */         break;
/*     */     } 
/*     */     
/* 628 */     return type;
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
/*     */   public static int arrayDimension(String desc) {
/* 640 */     int dim = 0;
/* 641 */     while (desc.charAt(dim) == '[') {
/* 642 */       dim++;
/*     */     }
/* 644 */     return dim;
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
/*     */   public static String toArrayComponent(String desc, int dim) {
/* 657 */     return desc.substring(dim);
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
/*     */   public static int dataSize(String desc) {
/* 672 */     return dataSize(desc, true);
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
/*     */   public static int paramSize(String desc) {
/* 685 */     return -dataSize(desc, false);
/*     */   }
/*     */   
/*     */   private static int dataSize(String desc, boolean withRet) {
/* 689 */     int n = 0;
/* 690 */     char c = desc.charAt(0);
/* 691 */     if (c == '(') {
/* 692 */       int i = 1;
/*     */       while (true) {
/* 694 */         c = desc.charAt(i);
/* 695 */         if (c == ')') {
/* 696 */           c = desc.charAt(i + 1);
/*     */           
/*     */           break;
/*     */         } 
/* 700 */         boolean array = false;
/* 701 */         while (c == '[') {
/* 702 */           array = true;
/* 703 */           c = desc.charAt(++i);
/*     */         } 
/*     */         
/* 706 */         if (c == 'L') {
/* 707 */           i = desc.indexOf(';', i) + 1;
/* 708 */           if (i <= 0) {
/* 709 */             throw new IndexOutOfBoundsException("bad descriptor");
/*     */           }
/*     */         } else {
/* 712 */           i++;
/*     */         } 
/* 714 */         if (!array && (c == 'J' || c == 'D')) {
/* 715 */           n -= 2; continue;
/*     */         } 
/* 717 */         n--;
/*     */       } 
/*     */     } 
/*     */     
/* 721 */     if (withRet)
/* 722 */       if (c == 'J' || c == 'D') {
/* 723 */         n += 2;
/* 724 */       } else if (c != 'V') {
/* 725 */         n++;
/*     */       }  
/* 727 */     return n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toString(String desc) {
/* 738 */     return PrettyPrinter.toString(desc);
/*     */   }
/*     */   
/*     */   static class PrettyPrinter {
/*     */     static String toString(String desc) {
/* 743 */       StringBuffer sbuf = new StringBuffer();
/* 744 */       if (desc.charAt(0) == '(') {
/* 745 */         int pos = 1;
/* 746 */         sbuf.append('(');
/* 747 */         while (desc.charAt(pos) != ')') {
/* 748 */           if (pos > 1) {
/* 749 */             sbuf.append(',');
/*     */           }
/* 751 */           pos = readType(sbuf, pos, desc);
/*     */         } 
/*     */         
/* 754 */         sbuf.append(')');
/*     */       } else {
/*     */         
/* 757 */         readType(sbuf, 0, desc);
/*     */       } 
/* 759 */       return sbuf.toString();
/*     */     }
/*     */     
/*     */     static int readType(StringBuffer sbuf, int pos, String desc) {
/* 763 */       char c = desc.charAt(pos);
/* 764 */       int arrayDim = 0;
/* 765 */       while (c == '[') {
/* 766 */         arrayDim++;
/* 767 */         c = desc.charAt(++pos);
/*     */       } 
/*     */       
/* 770 */       if (c == 'L') {
/*     */         while (true) {
/* 772 */           c = desc.charAt(++pos);
/* 773 */           if (c == ';') {
/*     */             break;
/*     */           }
/* 776 */           if (c == '/') {
/* 777 */             c = '.';
/*     */           }
/* 779 */           sbuf.append(c);
/*     */         } 
/*     */       } else {
/* 782 */         CtClass t = Descriptor.toPrimitiveClass(c);
/* 783 */         sbuf.append(t.getName());
/*     */       } 
/*     */       
/* 786 */       while (arrayDim-- > 0) {
/* 787 */         sbuf.append("[]");
/*     */       }
/* 789 */       return pos + 1;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Iterator
/*     */   {
/*     */     private String desc;
/*     */     
/*     */     private int index;
/*     */     
/*     */     private int curPos;
/*     */     
/*     */     private boolean param;
/*     */ 
/*     */     
/*     */     public Iterator(String s) {
/* 807 */       this.desc = s;
/* 808 */       this.index = this.curPos = 0;
/* 809 */       this.param = false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 816 */       return (this.index < this.desc.length());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isParameter() {
/* 822 */       return this.param;
/*     */     }
/*     */ 
/*     */     
/*     */     public char currentChar() {
/* 827 */       return this.desc.charAt(this.curPos);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean is2byte() {
/* 833 */       char c = currentChar();
/* 834 */       return (c == 'D' || c == 'J');
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int next() {
/* 842 */       int nextPos = this.index;
/* 843 */       char c = this.desc.charAt(nextPos);
/* 844 */       if (c == '(') {
/* 845 */         this.index++;
/* 846 */         c = this.desc.charAt(++nextPos);
/* 847 */         this.param = true;
/*     */       } 
/*     */       
/* 850 */       if (c == ')') {
/* 851 */         this.index++;
/* 852 */         c = this.desc.charAt(++nextPos);
/* 853 */         this.param = false;
/*     */       } 
/*     */       
/* 856 */       while (c == '[') {
/* 857 */         c = this.desc.charAt(++nextPos);
/*     */       }
/* 859 */       if (c == 'L') {
/* 860 */         nextPos = this.desc.indexOf(';', nextPos) + 1;
/* 861 */         if (nextPos <= 0) {
/* 862 */           throw new IndexOutOfBoundsException("bad descriptor");
/*     */         }
/*     */       } else {
/* 865 */         nextPos++;
/*     */       } 
/* 867 */       this.curPos = this.index;
/* 868 */       this.index = nextPos;
/* 869 */       return this.curPos;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\Descriptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */