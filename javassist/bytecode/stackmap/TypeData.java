/*     */ package javassist.bytecode.stackmap;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtClass;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.ConstPool;
/*     */ import javassist.bytecode.Descriptor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class TypeData
/*     */ {
/*     */   public static TypeData[] make(int size) {
/*  36 */     TypeData[] array = new TypeData[size];
/*  37 */     for (int i = 0; i < size; i++) {
/*  38 */       array[i] = TypeTag.TOP;
/*     */     }
/*  40 */     return array;
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
/*     */   private static void setType(TypeData td, String className, ClassPool cp) throws BadBytecode {
/*  53 */     td.setType(className, cp);
/*     */   }
/*     */   public abstract int getTypeTag();
/*     */   public abstract int getTypeData(ConstPool paramConstPool);
/*     */   
/*     */   public TypeData join() {
/*  59 */     return new TypeVar(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract BasicType isBasicType();
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean is2WordType();
/*     */ 
/*     */   
/*     */   public boolean isNullType() {
/*  72 */     return false;
/*     */   } public boolean isUninit() {
/*  74 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean eq(TypeData paramTypeData);
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String getName();
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void setType(String paramString, ClassPool paramClassPool) throws BadBytecode;
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract TypeData getArrayType(int paramInt) throws NotFoundException;
/*     */ 
/*     */   
/*     */   public int dfs(ArrayList order, int index, ClassPool cp) throws NotFoundException {
/*  95 */     return index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected TypeVar toTypeVar(int dim) {
/* 105 */     return null;
/*     */   }
/*     */   
/*     */   public void constructorCalled(int offset) {}
/*     */   
/*     */   public String toString() {
/* 111 */     return super.toString() + "(" + toString2(new HashSet()) + ")";
/*     */   }
/*     */ 
/*     */   
/*     */   abstract String toString2(HashSet paramHashSet);
/*     */   
/*     */   protected static class BasicType
/*     */     extends TypeData
/*     */   {
/*     */     private String name;
/*     */     private int typeTag;
/*     */     private char decodedName;
/*     */     
/*     */     public BasicType(String type, int tag, char decoded) {
/* 125 */       this.name = type;
/* 126 */       this.typeTag = tag;
/* 127 */       this.decodedName = decoded;
/*     */     }
/*     */     
/* 130 */     public int getTypeTag() { return this.typeTag; } public int getTypeData(ConstPool cp) {
/* 131 */       return 0;
/*     */     }
/*     */     public TypeData join() {
/* 134 */       if (this == TypeTag.TOP) {
/* 135 */         return this;
/*     */       }
/* 137 */       return super.join();
/*     */     }
/*     */     public BasicType isBasicType() {
/* 140 */       return this;
/*     */     }
/*     */     public boolean is2WordType() {
/* 143 */       return (this.typeTag == 4 || this.typeTag == 3);
/*     */     }
/*     */     
/*     */     public boolean eq(TypeData d) {
/* 147 */       return (this == d);
/*     */     }
/*     */     public String getName() {
/* 150 */       return this.name;
/*     */     }
/*     */     public char getDecodedName() {
/* 153 */       return this.decodedName;
/*     */     }
/*     */     public void setType(String s, ClassPool cp) throws BadBytecode {
/* 156 */       throw new BadBytecode("conflict: " + this.name + " and " + s);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public TypeData getArrayType(int dim) throws NotFoundException {
/* 163 */       if (this == TypeTag.TOP)
/* 164 */         return this; 
/* 165 */       if (dim < 0)
/* 166 */         throw new NotFoundException("no element type: " + this.name); 
/* 167 */       if (dim == 0) {
/* 168 */         return this;
/*     */       }
/* 170 */       char[] name = new char[dim + 1];
/* 171 */       for (int i = 0; i < dim; i++) {
/* 172 */         name[i] = '[';
/*     */       }
/* 174 */       name[dim] = this.decodedName;
/* 175 */       return new TypeData.ClassName(new String(name));
/*     */     }
/*     */     
/*     */     String toString2(HashSet set) {
/* 179 */       return this.name;
/*     */     } }
/*     */   
/*     */   public static abstract class AbsTypeVar extends TypeData {
/*     */     public abstract void merge(TypeData param1TypeData);
/*     */     
/*     */     public int getTypeTag() {
/* 186 */       return 7;
/*     */     }
/*     */     public int getTypeData(ConstPool cp) {
/* 189 */       return cp.addClassInfo(getName());
/*     */     }
/*     */     public boolean eq(TypeData d) {
/* 192 */       return getName().equals(d.getName());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class TypeVar
/*     */     extends AbsTypeVar
/*     */   {
/*     */     protected ArrayList lowers;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected ArrayList usedBy;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected ArrayList uppers;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected String fixedType;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean is2WordType;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private int visited;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private int smallest;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean inList;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private int dimension;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getName() {
/*     */       if (this.fixedType == null) {
/*     */         return ((TypeData)this.lowers.get(0)).getName();
/*     */       }
/*     */       return this.fixedType;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public TypeData.BasicType isBasicType() {
/*     */       if (this.fixedType == null) {
/*     */         return ((TypeData)this.lowers.get(0)).isBasicType();
/*     */       }
/*     */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean is2WordType() {
/*     */       if (this.fixedType == null) {
/*     */         return this.is2WordType;
/*     */       }
/*     */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public TypeVar(TypeData t) {
/* 280 */       this.visited = 0;
/* 281 */       this.smallest = 0;
/* 282 */       this.inList = false;
/* 283 */       this.dimension = 0; this.uppers = null; this.lowers = new ArrayList(2); this.usedBy = new ArrayList(2); merge(t); this.fixedType = null; this.is2WordType = t.is2WordType();
/*     */     } public boolean isNullType() { if (this.fixedType == null)
/*     */         return ((TypeData)this.lowers.get(0)).isNullType(); 
/* 286 */       return false; } protected TypeVar toTypeVar(int dim) { this.dimension = dim;
/* 287 */       return this; }
/*     */     public boolean isUninit() { if (this.fixedType == null)
/*     */         return ((TypeData)this.lowers.get(0)).isUninit(); 
/*     */       return false; }
/*     */     public void merge(TypeData t) { this.lowers.add(t);
/*     */       if (t instanceof TypeVar)
/*     */         ((TypeVar)t).usedBy.add(this);  }
/* 294 */     public TypeData getArrayType(int dim) throws NotFoundException { if (dim == 0) {
/* 295 */         return this;
/*     */       }
/* 297 */       TypeData.BasicType bt = isBasicType();
/* 298 */       if (bt == null) {
/* 299 */         if (isNullType()) {
/* 300 */           return new TypeData.NullType();
/*     */         }
/* 302 */         return (new TypeData.ClassName(getName())).getArrayType(dim);
/*     */       } 
/* 304 */       return bt.getArrayType(dim); }
/*     */     
/*     */     public int getTypeTag() {
/*     */       if (this.fixedType == null)
/*     */         return ((TypeData)this.lowers.get(0)).getTypeTag(); 
/*     */       return super.getTypeTag();
/* 310 */     } public int dfs(ArrayList<TypeVar> preOrder, int index, ClassPool cp) throws NotFoundException { if (this.visited > 0) {
/* 311 */         return index;
/*     */       }
/* 313 */       this.visited = this.smallest = ++index;
/* 314 */       preOrder.add(this);
/* 315 */       this.inList = true;
/* 316 */       int n = this.lowers.size();
/* 317 */       for (int i = 0; i < n; i++) {
/* 318 */         TypeVar child = ((TypeData)this.lowers.get(i)).toTypeVar(this.dimension);
/* 319 */         if (child != null)
/* 320 */           if (child.visited == 0) {
/* 321 */             index = child.dfs(preOrder, index, cp);
/* 322 */             if (child.smallest < this.smallest) {
/* 323 */               this.smallest = child.smallest;
/*     */             }
/* 325 */           } else if (child.inList && 
/* 326 */             child.visited < this.smallest) {
/* 327 */             this.smallest = child.visited;
/*     */           }  
/*     */       } 
/* 330 */       if (this.visited == this.smallest) {
/* 331 */         ArrayList<TypeVar> scc = new ArrayList();
/*     */         
/*     */         while (true) {
/* 334 */           TypeVar cv = preOrder.remove(preOrder.size() - 1);
/* 335 */           cv.inList = false;
/* 336 */           scc.add(cv);
/* 337 */           if (cv == this) {
/* 338 */             fixTypes(scc, cp); break;
/*     */           } 
/*     */         } 
/* 341 */       }  return index; }
/*     */     public int getTypeData(ConstPool cp) { if (this.fixedType == null)
/*     */         return ((TypeData)this.lowers.get(0)).getTypeData(cp);  return super.getTypeData(cp); }
/*     */     public void setType(String typeName, ClassPool cp) throws BadBytecode { if (this.uppers == null)
/* 345 */         this.uppers = new ArrayList();  this.uppers.add(typeName); } private void fixTypes(ArrayList<TypeVar> scc, ClassPool cp) throws NotFoundException { HashSet<String> lowersSet = new HashSet();
/* 346 */       boolean isBasicType = false;
/* 347 */       TypeData kind = null;
/* 348 */       int size = scc.size();
/* 349 */       for (int i = 0; i < size; i++) {
/* 350 */         TypeVar tvar = scc.get(i);
/* 351 */         ArrayList<TypeData> tds = tvar.lowers;
/* 352 */         int size2 = tds.size();
/* 353 */         for (int j = 0; j < size2; j++) {
/* 354 */           TypeData td = tds.get(j);
/* 355 */           TypeData d = td.getArrayType(tvar.dimension);
/* 356 */           TypeData.BasicType bt = d.isBasicType();
/* 357 */           if (kind == null) {
/* 358 */             if (bt == null) {
/* 359 */               isBasicType = false;
/* 360 */               kind = d;
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 365 */               if (d.isUninit()) {
/*     */                 break;
/*     */               }
/*     */             } else {
/* 369 */               isBasicType = true;
/* 370 */               kind = bt;
/*     */             }
/*     */           
/*     */           }
/* 374 */           else if ((bt == null && isBasicType) || (bt != null && kind != bt)) {
/* 375 */             isBasicType = true;
/* 376 */             kind = TypeTag.TOP;
/*     */             
/*     */             break;
/*     */           } 
/*     */           
/* 381 */           if (bt == null && !d.isNullType()) {
/* 382 */             lowersSet.add(d.getName());
/*     */           }
/*     */         } 
/*     */       } 
/* 386 */       if (isBasicType) {
/* 387 */         this.is2WordType = kind.is2WordType();
/* 388 */         fixTypes1(scc, kind);
/*     */       } else {
/*     */         
/* 391 */         String typeName = fixTypes2(scc, lowersSet, cp);
/* 392 */         fixTypes1(scc, new TypeData.ClassName(typeName));
/*     */       }  }
/*     */ 
/*     */     
/*     */     private void fixTypes1(ArrayList<TypeVar> scc, TypeData kind) throws NotFoundException {
/* 397 */       int size = scc.size();
/* 398 */       for (int i = 0; i < size; i++) {
/* 399 */         TypeVar cv = scc.get(i);
/* 400 */         TypeData kind2 = kind.getArrayType(-cv.dimension);
/* 401 */         if (kind2.isBasicType() == null) {
/* 402 */           cv.fixedType = kind2.getName();
/*     */         } else {
/* 404 */           cv.lowers.clear();
/* 405 */           cv.lowers.add(kind2);
/* 406 */           cv.is2WordType = kind2.is2WordType();
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private String fixTypes2(ArrayList scc, HashSet lowersSet, ClassPool cp) throws NotFoundException {
/* 412 */       Iterator<String> it = lowersSet.iterator();
/* 413 */       if (lowersSet.size() == 0)
/* 414 */         return null; 
/* 415 */       if (lowersSet.size() == 1) {
/* 416 */         return it.next();
/*     */       }
/* 418 */       CtClass cc = cp.get(it.next());
/* 419 */       while (it.hasNext()) {
/* 420 */         cc = commonSuperClassEx(cc, cp.get(it.next()));
/*     */       }
/* 422 */       if (cc.getSuperclass() == null || isObjectArray(cc)) {
/* 423 */         cc = fixByUppers(scc, cp, new HashSet(), cc);
/*     */       }
/* 425 */       if (cc.isArray()) {
/* 426 */         return Descriptor.toJvmName(cc);
/*     */       }
/* 428 */       return cc.getName();
/*     */     }
/*     */ 
/*     */     
/*     */     private static boolean isObjectArray(CtClass cc) throws NotFoundException {
/* 433 */       return (cc.isArray() && cc.getComponentType().getSuperclass() == null);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private CtClass fixByUppers(ArrayList<TypeVar> users, ClassPool cp, HashSet<TypeVar> visited, CtClass type) throws NotFoundException {
/* 439 */       if (users == null) {
/* 440 */         return type;
/*     */       }
/* 442 */       int size = users.size();
/* 443 */       for (int i = 0; i < size; i++) {
/* 444 */         TypeVar t = users.get(i);
/* 445 */         if (!visited.add(t)) {
/* 446 */           return type;
/*     */         }
/* 448 */         if (t.uppers != null) {
/* 449 */           int s = t.uppers.size();
/* 450 */           for (int k = 0; k < s; k++) {
/* 451 */             CtClass cc = cp.get(t.uppers.get(k));
/* 452 */             if (cc.subtypeOf(type)) {
/* 453 */               type = cc;
/*     */             }
/*     */           } 
/*     */         } 
/* 457 */         type = fixByUppers(t.usedBy, cp, visited, type);
/*     */       } 
/*     */       
/* 460 */       return type;
/*     */     }
/*     */     
/*     */     String toString2(HashSet<TypeVar> hash) {
/* 464 */       hash.add(this);
/* 465 */       if (this.lowers.size() > 0) {
/* 466 */         TypeData e = this.lowers.get(0);
/* 467 */         if (e != null && !hash.contains(e)) {
/* 468 */           return e.toString2(hash);
/*     */         }
/*     */       } 
/*     */       
/* 472 */       return "?";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CtClass commonSuperClassEx(CtClass one, CtClass two) throws NotFoundException {
/* 481 */     if (one == two)
/* 482 */       return one; 
/* 483 */     if (one.isArray() && two.isArray()) {
/* 484 */       CtClass ele1 = one.getComponentType();
/* 485 */       CtClass ele2 = two.getComponentType();
/* 486 */       CtClass element = commonSuperClassEx(ele1, ele2);
/* 487 */       if (element == ele1)
/* 488 */         return one; 
/* 489 */       if (element == ele2) {
/* 490 */         return two;
/*     */       }
/* 492 */       return one.getClassPool().get((element == null) ? "java.lang.Object" : (element
/* 493 */           .getName() + "[]"));
/*     */     } 
/* 495 */     if (one.isPrimitive() || two.isPrimitive())
/* 496 */       return null; 
/* 497 */     if (one.isArray() || two.isArray()) {
/* 498 */       return one.getClassPool().get("java.lang.Object");
/*     */     }
/* 500 */     return commonSuperClass(one, two);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CtClass commonSuperClass(CtClass one, CtClass two) throws NotFoundException {
/* 508 */     CtClass deep = one;
/* 509 */     CtClass shallow = two;
/* 510 */     CtClass backupShallow = shallow;
/* 511 */     CtClass backupDeep = deep;
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 516 */       if (eq(deep, shallow) && deep.getSuperclass() != null) {
/* 517 */         return deep;
/*     */       }
/* 519 */       CtClass deepSuper = deep.getSuperclass();
/* 520 */       CtClass shallowSuper = shallow.getSuperclass();
/*     */       
/* 522 */       if (shallowSuper == null) {
/*     */         
/* 524 */         shallow = backupShallow;
/*     */         
/*     */         break;
/*     */       } 
/* 528 */       if (deepSuper == null) {
/*     */         
/* 530 */         deep = backupDeep;
/* 531 */         backupDeep = backupShallow;
/* 532 */         backupShallow = deep;
/*     */         
/* 534 */         deep = shallow;
/* 535 */         shallow = backupShallow;
/*     */         
/*     */         break;
/*     */       } 
/* 539 */       deep = deepSuper;
/* 540 */       shallow = shallowSuper;
/*     */     } 
/*     */ 
/*     */     
/*     */     while (true) {
/* 545 */       deep = deep.getSuperclass();
/* 546 */       if (deep == null) {
/*     */         break;
/*     */       }
/* 549 */       backupDeep = backupDeep.getSuperclass();
/*     */     } 
/*     */     
/* 552 */     deep = backupDeep;
/*     */ 
/*     */ 
/*     */     
/* 556 */     while (!eq(deep, shallow)) {
/* 557 */       deep = deep.getSuperclass();
/* 558 */       shallow = shallow.getSuperclass();
/*     */     } 
/*     */     
/* 561 */     return deep;
/*     */   }
/*     */   
/*     */   static boolean eq(CtClass one, CtClass two) {
/* 565 */     return (one == two || (one != null && two != null && one.getName().equals(two.getName())));
/*     */   }
/*     */   
/*     */   public static void aastore(TypeData array, TypeData value, ClassPool cp) throws BadBytecode {
/* 569 */     if (array instanceof AbsTypeVar && 
/* 570 */       !value.isNullType()) {
/* 571 */       ((AbsTypeVar)array).merge(ArrayType.make(value));
/*     */     }
/* 573 */     if (value instanceof AbsTypeVar)
/* 574 */       if (array instanceof AbsTypeVar) {
/* 575 */         ArrayElement.make(array);
/* 576 */       } else if (array instanceof ClassName) {
/* 577 */         if (!array.isNullType()) {
/* 578 */           String type = ArrayElement.typeName(array.getName());
/* 579 */           value.setType(type, cp);
/*     */         } 
/*     */       } else {
/*     */         
/* 583 */         throw new BadBytecode("bad AASTORE: " + array);
/*     */       }  
/*     */   }
/*     */   
/*     */   public static class ArrayType
/*     */     extends AbsTypeVar
/*     */   {
/*     */     private TypeData.AbsTypeVar element;
/*     */     
/*     */     private ArrayType(TypeData.AbsTypeVar elementType) {
/* 593 */       this.element = elementType;
/*     */     }
/*     */     
/*     */     static TypeData make(TypeData element) throws BadBytecode {
/* 597 */       if (element instanceof TypeData.ArrayElement)
/* 598 */         return ((TypeData.ArrayElement)element).arrayType(); 
/* 599 */       if (element instanceof TypeData.AbsTypeVar)
/* 600 */         return new ArrayType((TypeData.AbsTypeVar)element); 
/* 601 */       if (element instanceof TypeData.ClassName && 
/* 602 */         !element.isNullType()) {
/* 603 */         return new TypeData.ClassName(typeName(element.getName()));
/*     */       }
/* 605 */       throw new BadBytecode("bad AASTORE: " + element);
/*     */     }
/*     */     
/*     */     public void merge(TypeData t) {
/*     */       try {
/* 610 */         if (!t.isNullType()) {
/* 611 */           this.element.merge(TypeData.ArrayElement.make(t));
/*     */         }
/* 613 */       } catch (BadBytecode e) {
/*     */         
/* 615 */         throw new RuntimeException("fatal: " + e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public String getName() {
/* 620 */       return typeName(this.element.getName());
/*     */     }
/*     */     public TypeData.AbsTypeVar elementType() {
/* 623 */       return this.element;
/*     */     }
/* 625 */     public TypeData.BasicType isBasicType() { return null; } public boolean is2WordType() {
/* 626 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public static String typeName(String elementType) {
/* 632 */       if (elementType.charAt(0) == '[') {
/* 633 */         return "[" + elementType;
/*     */       }
/* 635 */       return "[L" + elementType.replace('.', '/') + ";";
/*     */     }
/*     */     
/*     */     public void setType(String s, ClassPool cp) throws BadBytecode {
/* 639 */       this.element.setType(TypeData.ArrayElement.typeName(s), cp);
/*     */     }
/*     */     protected TypeData.TypeVar toTypeVar(int dim) {
/* 642 */       return this.element.toTypeVar(dim + 1);
/*     */     }
/*     */     public TypeData getArrayType(int dim) throws NotFoundException {
/* 645 */       return this.element.getArrayType(dim + 1);
/*     */     }
/*     */     
/*     */     public int dfs(ArrayList order, int index, ClassPool cp) throws NotFoundException {
/* 649 */       return this.element.dfs(order, index, cp);
/*     */     }
/*     */     
/*     */     String toString2(HashSet set) {
/* 653 */       return "[" + this.element.toString2(set);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class ArrayElement
/*     */     extends AbsTypeVar
/*     */   {
/*     */     private TypeData.AbsTypeVar array;
/*     */     
/*     */     private ArrayElement(TypeData.AbsTypeVar a) {
/* 664 */       this.array = a;
/*     */     }
/*     */     
/*     */     public static TypeData make(TypeData array) throws BadBytecode {
/* 668 */       if (array instanceof TypeData.ArrayType)
/* 669 */         return ((TypeData.ArrayType)array).elementType(); 
/* 670 */       if (array instanceof TypeData.AbsTypeVar)
/* 671 */         return new ArrayElement((TypeData.AbsTypeVar)array); 
/* 672 */       if (array instanceof TypeData.ClassName && 
/* 673 */         !array.isNullType()) {
/* 674 */         return new TypeData.ClassName(typeName(array.getName()));
/*     */       }
/* 676 */       throw new BadBytecode("bad AASTORE: " + array);
/*     */     }
/*     */     
/*     */     public void merge(TypeData t) {
/*     */       try {
/* 681 */         if (!t.isNullType()) {
/* 682 */           this.array.merge(TypeData.ArrayType.make(t));
/*     */         }
/* 684 */       } catch (BadBytecode e) {
/*     */         
/* 686 */         throw new RuntimeException("fatal: " + e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public String getName() {
/* 691 */       return typeName(this.array.getName());
/*     */     }
/*     */     public TypeData.AbsTypeVar arrayType() {
/* 694 */       return this.array;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public TypeData.BasicType isBasicType() {
/* 700 */       return null;
/*     */     } public boolean is2WordType() {
/* 702 */       return false;
/*     */     }
/*     */     private static String typeName(String arrayType) {
/* 705 */       if (arrayType.length() > 1 && arrayType.charAt(0) == '[') {
/* 706 */         char c = arrayType.charAt(1);
/* 707 */         if (c == 'L')
/* 708 */           return arrayType.substring(2, arrayType.length() - 1).replace('/', '.'); 
/* 709 */         if (c == '[') {
/* 710 */           return arrayType.substring(1);
/*     */         }
/*     */       } 
/* 713 */       return "java.lang.Object";
/*     */     }
/*     */     
/*     */     public void setType(String s, ClassPool cp) throws BadBytecode {
/* 717 */       this.array.setType(TypeData.ArrayType.typeName(s), cp);
/*     */     }
/*     */     protected TypeData.TypeVar toTypeVar(int dim) {
/* 720 */       return this.array.toTypeVar(dim - 1);
/*     */     }
/*     */     public TypeData getArrayType(int dim) throws NotFoundException {
/* 723 */       return this.array.getArrayType(dim - 1);
/*     */     }
/*     */     
/*     */     public int dfs(ArrayList order, int index, ClassPool cp) throws NotFoundException {
/* 727 */       return this.array.dfs(order, index, cp);
/*     */     }
/*     */     
/*     */     String toString2(HashSet set) {
/* 731 */       return "*" + this.array.toString2(set);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class UninitTypeVar extends AbsTypeVar {
/*     */     protected TypeData type;
/*     */     
/* 738 */     public UninitTypeVar(TypeData.UninitData t) { this.type = t; }
/* 739 */     public int getTypeTag() { return this.type.getTypeTag(); }
/* 740 */     public int getTypeData(ConstPool cp) { return this.type.getTypeData(cp); }
/* 741 */     public TypeData.BasicType isBasicType() { return this.type.isBasicType(); }
/* 742 */     public boolean is2WordType() { return this.type.is2WordType(); }
/* 743 */     public boolean isUninit() { return this.type.isUninit(); }
/* 744 */     public boolean eq(TypeData d) { return this.type.eq(d); } public String getName() {
/* 745 */       return this.type.getName();
/*     */     }
/* 747 */     protected TypeData.TypeVar toTypeVar(int dim) { return null; } public TypeData join() {
/* 748 */       return this.type.join();
/*     */     }
/*     */     public void setType(String s, ClassPool cp) throws BadBytecode {
/* 751 */       this.type.setType(s, cp);
/*     */     }
/*     */     
/*     */     public void merge(TypeData t) {
/* 755 */       if (!t.eq(this.type))
/* 756 */         this.type = TypeTag.TOP; 
/*     */     }
/*     */     
/*     */     public void constructorCalled(int offset) {
/* 760 */       this.type.constructorCalled(offset);
/*     */     }
/*     */     
/*     */     public int offset() {
/* 764 */       if (this.type instanceof TypeData.UninitData) {
/* 765 */         return ((TypeData.UninitData)this.type).offset;
/*     */       }
/* 767 */       throw new RuntimeException("not available");
/*     */     }
/*     */     
/*     */     public TypeData getArrayType(int dim) throws NotFoundException {
/* 771 */       return this.type.getArrayType(dim);
/*     */     }
/*     */     String toString2(HashSet set) {
/* 774 */       return "";
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ClassName
/*     */     extends TypeData
/*     */   {
/*     */     private String name;
/*     */     
/*     */     public ClassName(String n) {
/* 784 */       this.name = n;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 788 */       return this.name;
/*     */     }
/*     */     public TypeData.BasicType isBasicType() {
/* 791 */       return null;
/*     */     } public boolean is2WordType() {
/* 793 */       return false;
/*     */     } public int getTypeTag() {
/* 795 */       return 7;
/*     */     }
/*     */     public int getTypeData(ConstPool cp) {
/* 798 */       return cp.addClassInfo(getName());
/*     */     }
/*     */     public boolean eq(TypeData d) {
/* 801 */       return this.name.equals(d.getName());
/*     */     }
/*     */     public void setType(String typeName, ClassPool cp) throws BadBytecode {}
/*     */     
/*     */     public TypeData getArrayType(int dim) throws NotFoundException {
/* 806 */       if (dim == 0)
/* 807 */         return this; 
/* 808 */       if (dim > 0) {
/* 809 */         char[] dimType = new char[dim];
/* 810 */         for (int j = 0; j < dim; j++) {
/* 811 */           dimType[j] = '[';
/*     */         }
/* 813 */         String elementType = getName();
/* 814 */         if (elementType.charAt(0) != '[') {
/* 815 */           elementType = "L" + elementType.replace('.', '/') + ";";
/*     */         }
/* 817 */         return new ClassName(new String(dimType) + elementType);
/*     */       } 
/*     */       
/* 820 */       for (int i = 0; i < -dim; i++) {
/* 821 */         if (this.name.charAt(i) != '[')
/* 822 */           throw new NotFoundException("no " + dim + " dimensional array type: " + getName()); 
/*     */       } 
/* 824 */       char type = this.name.charAt(-dim);
/* 825 */       if (type == '[')
/* 826 */         return new ClassName(this.name.substring(-dim)); 
/* 827 */       if (type == 'L')
/* 828 */         return new ClassName(this.name.substring(-dim + 1, this.name.length() - 1).replace('/', '.')); 
/* 829 */       if (type == TypeTag.DOUBLE.decodedName)
/* 830 */         return TypeTag.DOUBLE; 
/* 831 */       if (type == TypeTag.FLOAT.decodedName)
/* 832 */         return TypeTag.FLOAT; 
/* 833 */       if (type == TypeTag.LONG.decodedName) {
/* 834 */         return TypeTag.LONG;
/*     */       }
/* 836 */       return TypeTag.INTEGER;
/*     */     }
/*     */ 
/*     */     
/*     */     String toString2(HashSet set) {
/* 841 */       return this.name;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class NullType
/*     */     extends ClassName
/*     */   {
/*     */     public NullType() {
/* 852 */       super("null-type");
/*     */     }
/*     */     
/*     */     public int getTypeTag() {
/* 856 */       return 5;
/*     */     }
/*     */     
/* 859 */     public boolean isNullType() { return true; } public int getTypeData(ConstPool cp) {
/* 860 */       return 0;
/*     */     } public TypeData getArrayType(int dim) {
/* 862 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class UninitData
/*     */     extends ClassName
/*     */   {
/*     */     int offset;
/*     */     boolean initialized;
/*     */     
/*     */     UninitData(int offset, String className) {
/* 873 */       super(className);
/* 874 */       this.offset = offset;
/* 875 */       this.initialized = false;
/*     */     }
/*     */     public UninitData copy() {
/* 878 */       return new UninitData(this.offset, getName());
/*     */     }
/*     */     public int getTypeTag() {
/* 881 */       return 8;
/*     */     }
/*     */     
/*     */     public int getTypeData(ConstPool cp) {
/* 885 */       return this.offset;
/*     */     }
/*     */     
/*     */     public TypeData join() {
/* 889 */       if (this.initialized) {
/* 890 */         return new TypeData.TypeVar(new TypeData.ClassName(getName()));
/*     */       }
/* 892 */       return new TypeData.UninitTypeVar(copy());
/*     */     }
/*     */     public boolean isUninit() {
/* 895 */       return true;
/*     */     }
/*     */     public boolean eq(TypeData d) {
/* 898 */       if (d instanceof UninitData) {
/* 899 */         UninitData ud = (UninitData)d;
/* 900 */         return (this.offset == ud.offset && getName().equals(ud.getName()));
/*     */       } 
/*     */       
/* 903 */       return false;
/*     */     }
/*     */     public int offset() {
/* 906 */       return this.offset;
/*     */     }
/*     */     public void constructorCalled(int offset) {
/* 909 */       if (offset == this.offset)
/* 910 */         this.initialized = true; 
/*     */     }
/*     */     String toString2(HashSet set) {
/* 913 */       return getName() + "," + this.offset;
/*     */     } }
/*     */   
/*     */   public static class UninitThis extends UninitData {
/*     */     UninitThis(String className) {
/* 918 */       super(-1, className);
/*     */     }
/*     */     public TypeData.UninitData copy() {
/* 921 */       return new UninitThis(getName());
/*     */     }
/*     */     public int getTypeTag() {
/* 924 */       return 6;
/*     */     }
/*     */     
/*     */     public int getTypeData(ConstPool cp) {
/* 928 */       return 0;
/*     */     }
/*     */     String toString2(HashSet set) {
/* 931 */       return "uninit:this";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\stackmap\TypeData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */