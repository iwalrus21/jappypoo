/*      */ package javassist.bytecode;
/*      */ 
/*      */ import java.io.DataInputStream;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ import javassist.CtClass;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class SignatureAttribute
/*      */   extends AttributeInfo
/*      */ {
/*      */   public static final String tag = "Signature";
/*      */   
/*      */   SignatureAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
/*   37 */     super(cp, n, in);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SignatureAttribute(ConstPool cp, String signature) {
/*   47 */     super(cp, "Signature");
/*   48 */     int index = cp.addUtf8Info(signature);
/*   49 */     byte[] bvalue = new byte[2];
/*   50 */     bvalue[0] = (byte)(index >>> 8);
/*   51 */     bvalue[1] = (byte)index;
/*   52 */     set(bvalue);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSignature() {
/*   63 */     return getConstPool().getUtf8Info(ByteArray.readU16bit(get(), 0));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSignature(String sig) {
/*   74 */     int index = getConstPool().addUtf8Info(sig);
/*   75 */     ByteArray.write16bit(index, this.info, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AttributeInfo copy(ConstPool newCp, Map classnames) {
/*   87 */     return new SignatureAttribute(newCp, getSignature());
/*      */   }
/*      */   
/*      */   void renameClass(String oldname, String newname) {
/*   91 */     String sig = renameClass(getSignature(), oldname, newname);
/*   92 */     setSignature(sig);
/*      */   }
/*      */   
/*      */   void renameClass(Map classnames) {
/*   96 */     String sig = renameClass(getSignature(), classnames);
/*   97 */     setSignature(sig);
/*      */   }
/*      */   
/*      */   static String renameClass(String desc, String oldname, String newname) {
/*  101 */     Map<Object, Object> map = new HashMap<Object, Object>();
/*  102 */     map.put(oldname, newname);
/*  103 */     return renameClass(desc, map);
/*      */   }
/*      */   
/*      */   static String renameClass(String desc, Map map) {
/*  107 */     if (map == null) {
/*  108 */       return desc;
/*      */     }
/*  110 */     StringBuilder newdesc = new StringBuilder();
/*  111 */     int head = 0;
/*  112 */     int i = 0; while (true) {
/*      */       char c;
/*  114 */       int j = desc.indexOf('L', i);
/*  115 */       if (j < 0) {
/*      */         break;
/*      */       }
/*  118 */       StringBuilder nameBuf = new StringBuilder();
/*  119 */       int k = j;
/*      */ 
/*      */       
/*  122 */       try { while ((c = desc.charAt(++k)) != ';') {
/*  123 */           nameBuf.append(c);
/*  124 */           if (c == '<') {
/*  125 */             while ((c = desc.charAt(++k)) != '>') {
/*  126 */               nameBuf.append(c);
/*      */             }
/*  128 */             nameBuf.append(c);
/*      */           }
/*      */         
/*      */         }  }
/*  132 */       catch (IndexOutOfBoundsException e) { break; }
/*  133 */        i = k + 1;
/*  134 */       String name = nameBuf.toString();
/*  135 */       String name2 = (String)map.get(name);
/*  136 */       if (name2 != null) {
/*  137 */         newdesc.append(desc.substring(head, j));
/*  138 */         newdesc.append('L');
/*  139 */         newdesc.append(name2);
/*  140 */         newdesc.append(c);
/*  141 */         head = i;
/*      */       } 
/*      */     } 
/*      */     
/*  145 */     if (head == 0) {
/*  146 */       return desc;
/*      */     }
/*  148 */     int len = desc.length();
/*  149 */     if (head < len) {
/*  150 */       newdesc.append(desc.substring(head, len));
/*      */     }
/*  152 */     return newdesc.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean isNamePart(int c) {
/*  157 */     return (c != 59 && c != 60);
/*      */   }
/*      */   
/*      */   private static class Cursor {
/*  161 */     int position = 0;
/*      */     
/*      */     int indexOf(String s, int ch) throws BadBytecode {
/*  164 */       int i = s.indexOf(ch, this.position);
/*  165 */       if (i < 0) {
/*  166 */         throw SignatureAttribute.error(s);
/*      */       }
/*  168 */       this.position = i + 1;
/*  169 */       return i;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private Cursor() {}
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class ClassSignature
/*      */   {
/*      */     SignatureAttribute.TypeParameter[] params;
/*      */ 
/*      */     
/*      */     SignatureAttribute.ClassType superClass;
/*      */     
/*      */     SignatureAttribute.ClassType[] interfaces;
/*      */ 
/*      */     
/*      */     public ClassSignature(SignatureAttribute.TypeParameter[] params, SignatureAttribute.ClassType superClass, SignatureAttribute.ClassType[] interfaces) {
/*  190 */       this.params = (params == null) ? new SignatureAttribute.TypeParameter[0] : params;
/*  191 */       this.superClass = (superClass == null) ? SignatureAttribute.ClassType.OBJECT : superClass;
/*  192 */       this.interfaces = (interfaces == null) ? new SignatureAttribute.ClassType[0] : interfaces;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ClassSignature(SignatureAttribute.TypeParameter[] p) {
/*  201 */       this(p, null, null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public SignatureAttribute.TypeParameter[] getParameters() {
/*  210 */       return this.params;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public SignatureAttribute.ClassType getSuperClass() {
/*  216 */       return this.superClass;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public SignatureAttribute.ClassType[] getInterfaces() {
/*  223 */       return this.interfaces;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  229 */       StringBuffer sbuf = new StringBuffer();
/*      */       
/*  231 */       SignatureAttribute.TypeParameter.toString(sbuf, this.params);
/*  232 */       sbuf.append(" extends ").append(this.superClass);
/*  233 */       if (this.interfaces.length > 0) {
/*  234 */         sbuf.append(" implements ");
/*  235 */         SignatureAttribute.Type.toString(sbuf, (SignatureAttribute.Type[])this.interfaces);
/*      */       } 
/*      */       
/*  238 */       return sbuf.toString();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String encode() {
/*  245 */       StringBuffer sbuf = new StringBuffer();
/*  246 */       if (this.params.length > 0) {
/*  247 */         sbuf.append('<');
/*  248 */         for (int j = 0; j < this.params.length; j++) {
/*  249 */           this.params[j].encode(sbuf);
/*      */         }
/*  251 */         sbuf.append('>');
/*      */       } 
/*      */       
/*  254 */       this.superClass.encode(sbuf);
/*  255 */       for (int i = 0; i < this.interfaces.length; i++) {
/*  256 */         this.interfaces[i].encode(sbuf);
/*      */       }
/*  258 */       return sbuf.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class MethodSignature
/*      */   {
/*      */     SignatureAttribute.TypeParameter[] typeParams;
/*      */ 
/*      */     
/*      */     SignatureAttribute.Type[] params;
/*      */ 
/*      */     
/*      */     SignatureAttribute.Type retType;
/*      */ 
/*      */     
/*      */     SignatureAttribute.ObjectType[] exceptions;
/*      */ 
/*      */ 
/*      */     
/*      */     public MethodSignature(SignatureAttribute.TypeParameter[] tp, SignatureAttribute.Type[] params, SignatureAttribute.Type ret, SignatureAttribute.ObjectType[] ex) {
/*  281 */       this.typeParams = (tp == null) ? new SignatureAttribute.TypeParameter[0] : tp;
/*  282 */       this.params = (params == null) ? new SignatureAttribute.Type[0] : params;
/*  283 */       this.retType = (ret == null) ? new SignatureAttribute.BaseType("void") : ret;
/*  284 */       this.exceptions = (ex == null) ? new SignatureAttribute.ObjectType[0] : ex;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public SignatureAttribute.TypeParameter[] getTypeParameters() {
/*  292 */       return this.typeParams;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public SignatureAttribute.Type[] getParameterTypes() {
/*  299 */       return this.params;
/*      */     }
/*      */ 
/*      */     
/*      */     public SignatureAttribute.Type getReturnType() {
/*  304 */       return this.retType;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public SignatureAttribute.ObjectType[] getExceptionTypes() {
/*  312 */       return this.exceptions;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  318 */       StringBuffer sbuf = new StringBuffer();
/*      */       
/*  320 */       SignatureAttribute.TypeParameter.toString(sbuf, this.typeParams);
/*  321 */       sbuf.append(" (");
/*  322 */       SignatureAttribute.Type.toString(sbuf, this.params);
/*  323 */       sbuf.append(") ");
/*  324 */       sbuf.append(this.retType);
/*  325 */       if (this.exceptions.length > 0) {
/*  326 */         sbuf.append(" throws ");
/*  327 */         SignatureAttribute.Type.toString(sbuf, (SignatureAttribute.Type[])this.exceptions);
/*      */       } 
/*      */       
/*  330 */       return sbuf.toString();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String encode() {
/*  337 */       StringBuffer sbuf = new StringBuffer();
/*  338 */       if (this.typeParams.length > 0) {
/*  339 */         sbuf.append('<');
/*  340 */         for (int j = 0; j < this.typeParams.length; j++) {
/*  341 */           this.typeParams[j].encode(sbuf);
/*      */         }
/*  343 */         sbuf.append('>');
/*      */       } 
/*      */       
/*  346 */       sbuf.append('('); int i;
/*  347 */       for (i = 0; i < this.params.length; i++) {
/*  348 */         this.params[i].encode(sbuf);
/*      */       }
/*  350 */       sbuf.append(')');
/*  351 */       this.retType.encode(sbuf);
/*  352 */       if (this.exceptions.length > 0) {
/*  353 */         for (i = 0; i < this.exceptions.length; i++) {
/*  354 */           sbuf.append('^');
/*  355 */           this.exceptions[i].encode(sbuf);
/*      */         } 
/*      */       }
/*  358 */       return sbuf.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static class TypeParameter
/*      */   {
/*      */     String name;
/*      */     
/*      */     SignatureAttribute.ObjectType superClass;
/*      */     
/*      */     SignatureAttribute.ObjectType[] superInterfaces;
/*      */ 
/*      */     
/*      */     TypeParameter(String sig, int nb, int ne, SignatureAttribute.ObjectType sc, SignatureAttribute.ObjectType[] si) {
/*  373 */       this.name = sig.substring(nb, ne);
/*  374 */       this.superClass = sc;
/*  375 */       this.superInterfaces = si;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public TypeParameter(String name, SignatureAttribute.ObjectType superClass, SignatureAttribute.ObjectType[] superInterfaces) {
/*  387 */       this.name = name;
/*  388 */       this.superClass = superClass;
/*  389 */       if (superInterfaces == null) {
/*  390 */         this.superInterfaces = new SignatureAttribute.ObjectType[0];
/*      */       } else {
/*  392 */         this.superInterfaces = superInterfaces;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public TypeParameter(String name) {
/*  402 */       this(name, null, null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getName() {
/*  409 */       return this.name;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public SignatureAttribute.ObjectType getClassBound() {
/*  415 */       return this.superClass;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public SignatureAttribute.ObjectType[] getInterfaceBound() {
/*  422 */       return this.superInterfaces;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  428 */       StringBuffer sbuf = new StringBuffer(getName());
/*  429 */       if (this.superClass != null) {
/*  430 */         sbuf.append(" extends ").append(this.superClass.toString());
/*      */       }
/*  432 */       int len = this.superInterfaces.length;
/*  433 */       if (len > 0) {
/*  434 */         for (int i = 0; i < len; i++) {
/*  435 */           if (i > 0 || this.superClass != null) {
/*  436 */             sbuf.append(" & ");
/*      */           } else {
/*  438 */             sbuf.append(" extends ");
/*      */           } 
/*  440 */           sbuf.append(this.superInterfaces[i].toString());
/*      */         } 
/*      */       }
/*      */       
/*  444 */       return sbuf.toString();
/*      */     }
/*      */     
/*      */     static void toString(StringBuffer sbuf, TypeParameter[] tp) {
/*  448 */       sbuf.append('<');
/*  449 */       for (int i = 0; i < tp.length; i++) {
/*  450 */         if (i > 0) {
/*  451 */           sbuf.append(", ");
/*      */         }
/*  453 */         sbuf.append(tp[i]);
/*      */       } 
/*      */       
/*  456 */       sbuf.append('>');
/*      */     }
/*      */     
/*      */     void encode(StringBuffer sb) {
/*  460 */       sb.append(this.name);
/*  461 */       if (this.superClass == null) {
/*  462 */         sb.append(":Ljava/lang/Object;");
/*      */       } else {
/*  464 */         sb.append(':');
/*  465 */         this.superClass.encode(sb);
/*      */       } 
/*      */       
/*  468 */       for (int i = 0; i < this.superInterfaces.length; i++) {
/*  469 */         sb.append(':');
/*  470 */         this.superInterfaces[i].encode(sb);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class TypeArgument
/*      */   {
/*      */     SignatureAttribute.ObjectType arg;
/*      */     
/*      */     char wildcard;
/*      */ 
/*      */     
/*      */     TypeArgument(SignatureAttribute.ObjectType a, char w) {
/*  485 */       this.arg = a;
/*  486 */       this.wildcard = w;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public TypeArgument(SignatureAttribute.ObjectType t) {
/*  497 */       this(t, ' ');
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public TypeArgument() {
/*  504 */       this(null, '*');
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static TypeArgument subclassOf(SignatureAttribute.ObjectType t) {
/*  514 */       return new TypeArgument(t, '+');
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static TypeArgument superOf(SignatureAttribute.ObjectType t) {
/*  524 */       return new TypeArgument(t, '-');
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public char getKind() {
/*  533 */       return this.wildcard;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isWildcard() {
/*  539 */       return (this.wildcard != ' ');
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public SignatureAttribute.ObjectType getType() {
/*  548 */       return this.arg;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  554 */       if (this.wildcard == '*') {
/*  555 */         return "?";
/*      */       }
/*  557 */       String type = this.arg.toString();
/*  558 */       if (this.wildcard == ' ')
/*  559 */         return type; 
/*  560 */       if (this.wildcard == '+') {
/*  561 */         return "? extends " + type;
/*      */       }
/*  563 */       return "? super " + type;
/*      */     }
/*      */     
/*      */     static void encode(StringBuffer sb, TypeArgument[] args) {
/*  567 */       sb.append('<');
/*  568 */       for (int i = 0; i < args.length; i++) {
/*  569 */         TypeArgument ta = args[i];
/*  570 */         if (ta.isWildcard()) {
/*  571 */           sb.append(ta.wildcard);
/*      */         }
/*  573 */         if (ta.getType() != null) {
/*  574 */           ta.getType().encode(sb);
/*      */         }
/*      */       } 
/*  577 */       sb.append('>');
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static abstract class Type
/*      */   {
/*      */     abstract void encode(StringBuffer param1StringBuffer);
/*      */     
/*      */     static void toString(StringBuffer sbuf, Type[] ts) {
/*  587 */       for (int i = 0; i < ts.length; i++) {
/*  588 */         if (i > 0) {
/*  589 */           sbuf.append(", ");
/*      */         }
/*  591 */         sbuf.append(ts[i]);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String jvmTypeName() {
/*  600 */       return toString();
/*      */     }
/*      */   }
/*      */   
/*      */   public static class BaseType extends Type {
/*      */     char descriptor;
/*      */     
/*      */     BaseType(char c) {
/*  608 */       this.descriptor = c;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public BaseType(String typeName) {
/*  616 */       this(Descriptor.of(typeName).charAt(0));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public char getDescriptor() {
/*  624 */       return this.descriptor;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public CtClass getCtlass() {
/*  631 */       return Descriptor.toPrimitiveClass(this.descriptor);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  638 */       return Descriptor.toClassName(Character.toString(this.descriptor));
/*      */     }
/*      */     
/*      */     void encode(StringBuffer sb) {
/*  642 */       sb.append(this.descriptor);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static abstract class ObjectType
/*      */     extends Type
/*      */   {
/*      */     public String encode() {
/*  655 */       StringBuffer sb = new StringBuffer();
/*  656 */       encode(sb);
/*  657 */       return sb.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static class ClassType
/*      */     extends ObjectType
/*      */   {
/*      */     String name;
/*      */     
/*      */     SignatureAttribute.TypeArgument[] arguments;
/*      */     
/*      */     static ClassType make(String s, int b, int e, SignatureAttribute.TypeArgument[] targs, ClassType parent) {
/*  670 */       if (parent == null) {
/*  671 */         return new ClassType(s, b, e, targs);
/*      */       }
/*  673 */       return new SignatureAttribute.NestedClassType(s, b, e, targs, parent);
/*      */     }
/*      */     
/*      */     ClassType(String signature, int begin, int end, SignatureAttribute.TypeArgument[] targs) {
/*  677 */       this.name = signature.substring(begin, end).replace('/', '.');
/*  678 */       this.arguments = targs;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  684 */     public static ClassType OBJECT = new ClassType("java.lang.Object", null);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ClassType(String className, SignatureAttribute.TypeArgument[] args) {
/*  694 */       this.name = className;
/*  695 */       this.arguments = args;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ClassType(String className) {
/*  705 */       this(className, null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getName() {
/*  712 */       return this.name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public SignatureAttribute.TypeArgument[] getTypeArguments() {
/*  720 */       return this.arguments;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ClassType getDeclaringClass() {
/*  728 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  734 */       StringBuffer sbuf = new StringBuffer();
/*  735 */       ClassType parent = getDeclaringClass();
/*  736 */       if (parent != null) {
/*  737 */         sbuf.append(parent.toString()).append('.');
/*      */       }
/*  739 */       return toString2(sbuf);
/*      */     }
/*      */     
/*      */     private String toString2(StringBuffer sbuf) {
/*  743 */       sbuf.append(this.name);
/*  744 */       if (this.arguments != null) {
/*  745 */         sbuf.append('<');
/*  746 */         int n = this.arguments.length;
/*  747 */         for (int i = 0; i < n; i++) {
/*  748 */           if (i > 0) {
/*  749 */             sbuf.append(", ");
/*      */           }
/*  751 */           sbuf.append(this.arguments[i].toString());
/*      */         } 
/*      */         
/*  754 */         sbuf.append('>');
/*      */       } 
/*      */       
/*  757 */       return sbuf.toString();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String jvmTypeName() {
/*  766 */       StringBuffer sbuf = new StringBuffer();
/*  767 */       ClassType parent = getDeclaringClass();
/*  768 */       if (parent != null) {
/*  769 */         sbuf.append(parent.jvmTypeName()).append('$');
/*      */       }
/*  771 */       return toString2(sbuf);
/*      */     }
/*      */     
/*      */     void encode(StringBuffer sb) {
/*  775 */       sb.append('L');
/*  776 */       encode2(sb);
/*  777 */       sb.append(';');
/*      */     }
/*      */     
/*      */     void encode2(StringBuffer sb) {
/*  781 */       ClassType parent = getDeclaringClass();
/*  782 */       if (parent != null) {
/*  783 */         parent.encode2(sb);
/*  784 */         sb.append('$');
/*      */       } 
/*      */       
/*  787 */       sb.append(this.name.replace('.', '/'));
/*  788 */       if (this.arguments != null) {
/*  789 */         SignatureAttribute.TypeArgument.encode(sb, this.arguments);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static class NestedClassType
/*      */     extends ClassType
/*      */   {
/*      */     SignatureAttribute.ClassType parent;
/*      */     
/*      */     NestedClassType(String s, int b, int e, SignatureAttribute.TypeArgument[] targs, SignatureAttribute.ClassType p) {
/*  800 */       super(s, b, e, targs);
/*  801 */       this.parent = p;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NestedClassType(SignatureAttribute.ClassType parent, String className, SignatureAttribute.TypeArgument[] args) {
/*  813 */       super(className, args);
/*  814 */       this.parent = parent;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public SignatureAttribute.ClassType getDeclaringClass() {
/*  821 */       return this.parent;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class ArrayType
/*      */     extends ObjectType
/*      */   {
/*      */     int dim;
/*      */ 
/*      */     
/*      */     SignatureAttribute.Type componentType;
/*      */ 
/*      */ 
/*      */     
/*      */     public ArrayType(int d, SignatureAttribute.Type comp) {
/*  838 */       this.dim = d;
/*  839 */       this.componentType = comp;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public int getDimension() {
/*  845 */       return this.dim;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public SignatureAttribute.Type getComponentType() {
/*  851 */       return this.componentType;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  858 */       StringBuffer sbuf = new StringBuffer(this.componentType.toString());
/*  859 */       for (int i = 0; i < this.dim; i++) {
/*  860 */         sbuf.append("[]");
/*      */       }
/*  862 */       return sbuf.toString();
/*      */     }
/*      */     
/*      */     void encode(StringBuffer sb) {
/*  866 */       for (int i = 0; i < this.dim; i++) {
/*  867 */         sb.append('[');
/*      */       }
/*  869 */       this.componentType.encode(sb);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static class TypeVariable
/*      */     extends ObjectType
/*      */   {
/*      */     String name;
/*      */     
/*      */     TypeVariable(String sig, int begin, int end) {
/*  880 */       this.name = sig.substring(begin, end);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public TypeVariable(String name) {
/*  889 */       this.name = name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getName() {
/*  896 */       return this.name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  903 */       return this.name;
/*      */     }
/*      */     
/*      */     void encode(StringBuffer sb) {
/*  907 */       sb.append('T').append(this.name).append(';');
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ClassSignature toClassSignature(String sig) throws BadBytecode {
/*      */     try {
/*  924 */       return parseSig(sig);
/*      */     }
/*  926 */     catch (IndexOutOfBoundsException e) {
/*  927 */       throw error(sig);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static MethodSignature toMethodSignature(String sig) throws BadBytecode {
/*      */     try {
/*  944 */       return parseMethodSig(sig);
/*      */     }
/*  946 */     catch (IndexOutOfBoundsException e) {
/*  947 */       throw error(sig);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ObjectType toFieldSignature(String sig) throws BadBytecode {
/*      */     try {
/*  963 */       return parseObjectType(sig, new Cursor(), false);
/*      */     }
/*  965 */     catch (IndexOutOfBoundsException e) {
/*  966 */       throw error(sig);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Type toTypeSignature(String sig) throws BadBytecode {
/*      */     try {
/*  980 */       return parseType(sig, new Cursor());
/*      */     }
/*  982 */     catch (IndexOutOfBoundsException e) {
/*  983 */       throw error(sig);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static ClassSignature parseSig(String sig) throws BadBytecode, IndexOutOfBoundsException {
/*  990 */     Cursor cur = new Cursor();
/*  991 */     TypeParameter[] tp = parseTypeParams(sig, cur);
/*  992 */     ClassType superClass = parseClassType(sig, cur);
/*  993 */     int sigLen = sig.length();
/*  994 */     ArrayList<ClassType> ifArray = new ArrayList();
/*  995 */     while (cur.position < sigLen && sig.charAt(cur.position) == 'L') {
/*  996 */       ifArray.add(parseClassType(sig, cur));
/*      */     }
/*      */     
/*  999 */     ClassType[] ifs = ifArray.<ClassType>toArray(new ClassType[ifArray.size()]);
/* 1000 */     return new ClassSignature(tp, superClass, ifs);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static MethodSignature parseMethodSig(String sig) throws BadBytecode {
/* 1006 */     Cursor cur = new Cursor();
/* 1007 */     TypeParameter[] tp = parseTypeParams(sig, cur);
/* 1008 */     if (sig.charAt(cur.position++) != '(') {
/* 1009 */       throw error(sig);
/*      */     }
/* 1011 */     ArrayList<Type> params = new ArrayList();
/* 1012 */     while (sig.charAt(cur.position) != ')') {
/* 1013 */       Type t = parseType(sig, cur);
/* 1014 */       params.add(t);
/*      */     } 
/*      */     
/* 1017 */     cur.position++;
/* 1018 */     Type ret = parseType(sig, cur);
/* 1019 */     int sigLen = sig.length();
/* 1020 */     ArrayList<ObjectType> exceptions = new ArrayList();
/* 1021 */     while (cur.position < sigLen && sig.charAt(cur.position) == '^') {
/* 1022 */       cur.position++;
/* 1023 */       ObjectType t = parseObjectType(sig, cur, false);
/* 1024 */       if (t instanceof ArrayType) {
/* 1025 */         throw error(sig);
/*      */       }
/* 1027 */       exceptions.add(t);
/*      */     } 
/*      */     
/* 1030 */     Type[] p = params.<Type>toArray(new Type[params.size()]);
/* 1031 */     ObjectType[] ex = exceptions.<ObjectType>toArray(new ObjectType[exceptions.size()]);
/* 1032 */     return new MethodSignature(tp, p, ret, ex);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static TypeParameter[] parseTypeParams(String sig, Cursor cur) throws BadBytecode {
/* 1038 */     ArrayList<TypeParameter> typeParam = new ArrayList();
/* 1039 */     if (sig.charAt(cur.position) == '<') {
/* 1040 */       cur.position++;
/* 1041 */       while (sig.charAt(cur.position) != '>') {
/* 1042 */         int nameBegin = cur.position;
/* 1043 */         int nameEnd = cur.indexOf(sig, 58);
/* 1044 */         ObjectType classBound = parseObjectType(sig, cur, true);
/* 1045 */         ArrayList<ObjectType> ifBound = new ArrayList();
/* 1046 */         while (sig.charAt(cur.position) == ':') {
/* 1047 */           cur.position++;
/* 1048 */           ObjectType t = parseObjectType(sig, cur, false);
/* 1049 */           ifBound.add(t);
/*      */         } 
/*      */ 
/*      */         
/* 1053 */         TypeParameter p = new TypeParameter(sig, nameBegin, nameEnd, classBound, ifBound.<ObjectType>toArray(new ObjectType[ifBound.size()]));
/* 1054 */         typeParam.add(p);
/*      */       } 
/*      */       
/* 1057 */       cur.position++;
/*      */     } 
/*      */     
/* 1060 */     return typeParam.<TypeParameter>toArray(new TypeParameter[typeParam.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static ObjectType parseObjectType(String sig, Cursor c, boolean dontThrow) throws BadBytecode {
/* 1067 */     int i, begin = c.position;
/* 1068 */     switch (sig.charAt(begin)) {
/*      */       case 'L':
/* 1070 */         return parseClassType2(sig, c, (ClassType)null);
/*      */       case 'T':
/* 1072 */         i = c.indexOf(sig, 59);
/* 1073 */         return new TypeVariable(sig, begin + 1, i);
/*      */       case '[':
/* 1075 */         return parseArray(sig, c);
/*      */     } 
/* 1077 */     if (dontThrow) {
/* 1078 */       return null;
/*      */     }
/* 1080 */     throw error(sig);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static ClassType parseClassType(String sig, Cursor c) throws BadBytecode {
/* 1087 */     if (sig.charAt(c.position) == 'L') {
/* 1088 */       return parseClassType2(sig, c, (ClassType)null);
/*      */     }
/* 1090 */     throw error(sig);
/*      */   }
/*      */   
/*      */   private static ClassType parseClassType2(String sig, Cursor c, ClassType parent) throws BadBytecode {
/*      */     char t;
/*      */     TypeArgument[] targs;
/* 1096 */     int start = ++c.position;
/*      */     
/*      */     do {
/* 1099 */       t = sig.charAt(c.position++);
/* 1100 */     } while (t != '$' && t != '<' && t != ';');
/* 1101 */     int end = c.position - 1;
/*      */     
/* 1103 */     if (t == '<') {
/* 1104 */       targs = parseTypeArgs(sig, c);
/* 1105 */       t = sig.charAt(c.position++);
/*      */     } else {
/*      */       
/* 1108 */       targs = null;
/*      */     } 
/* 1110 */     ClassType thisClass = ClassType.make(sig, start, end, targs, parent);
/* 1111 */     if (t == '$' || t == '.') {
/* 1112 */       c.position--;
/* 1113 */       return parseClassType2(sig, c, thisClass);
/*      */     } 
/*      */     
/* 1116 */     return thisClass;
/*      */   }
/*      */   
/*      */   private static TypeArgument[] parseTypeArgs(String sig, Cursor c) throws BadBytecode {
/* 1120 */     ArrayList<TypeArgument> args = new ArrayList();
/*      */     char t;
/* 1122 */     while ((t = sig.charAt(c.position++)) != '>') {
/*      */       TypeArgument ta;
/* 1124 */       if (t == '*') {
/* 1125 */         ta = new TypeArgument(null, '*');
/*      */       } else {
/* 1127 */         if (t != '+' && t != '-') {
/* 1128 */           t = ' ';
/* 1129 */           c.position--;
/*      */         } 
/*      */         
/* 1132 */         ta = new TypeArgument(parseObjectType(sig, c, false), t);
/*      */       } 
/*      */       
/* 1135 */       args.add(ta);
/*      */     } 
/*      */     
/* 1138 */     return args.<TypeArgument>toArray(new TypeArgument[args.size()]);
/*      */   }
/*      */   
/*      */   private static ObjectType parseArray(String sig, Cursor c) throws BadBytecode {
/* 1142 */     int dim = 1;
/* 1143 */     while (sig.charAt(++c.position) == '[') {
/* 1144 */       dim++;
/*      */     }
/* 1146 */     return new ArrayType(dim, parseType(sig, c));
/*      */   }
/*      */   
/*      */   private static Type parseType(String sig, Cursor c) throws BadBytecode {
/* 1150 */     Type t = parseObjectType(sig, c, true);
/* 1151 */     if (t == null) {
/* 1152 */       t = new BaseType(sig.charAt(c.position++));
/*      */     }
/* 1154 */     return t;
/*      */   }
/*      */   
/*      */   private static BadBytecode error(String sig) {
/* 1158 */     return new BadBytecode("bad signature: " + sig);
/*      */   }
/*      */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\SignatureAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */