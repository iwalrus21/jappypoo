/*     */ package javassist.bytecode;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.List;
/*     */ import javassist.Modifier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassFilePrinter
/*     */ {
/*     */   public static void print(ClassFile cf) {
/*  33 */     print(cf, new PrintWriter(System.out, true));
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
/*     */   public static void print(ClassFile cf, PrintWriter out) {
/*  47 */     int mod = AccessFlag.toModifier(cf.getAccessFlags() & 0xFFFFFFDF);
/*     */     
/*  49 */     out.println("major: " + cf.major + ", minor: " + cf.minor + " modifiers: " + 
/*  50 */         Integer.toHexString(cf.getAccessFlags()));
/*  51 */     out.println(Modifier.toString(mod) + " class " + cf
/*  52 */         .getName() + " extends " + cf.getSuperclass());
/*     */     
/*  54 */     String[] infs = cf.getInterfaces();
/*  55 */     if (infs != null && infs.length > 0) {
/*  56 */       out.print("    implements ");
/*  57 */       out.print(infs[0]);
/*  58 */       for (int j = 1; j < infs.length; j++) {
/*  59 */         out.print(", " + infs[j]);
/*     */       }
/*  61 */       out.println();
/*     */     } 
/*     */     
/*  64 */     out.println();
/*  65 */     List<FieldInfo> list = cf.getFields();
/*  66 */     int n = list.size(); int i;
/*  67 */     for (i = 0; i < n; i++) {
/*  68 */       FieldInfo finfo = list.get(i);
/*  69 */       int acc = finfo.getAccessFlags();
/*  70 */       out.println(Modifier.toString(AccessFlag.toModifier(acc)) + " " + finfo
/*  71 */           .getName() + "\t" + finfo
/*  72 */           .getDescriptor());
/*  73 */       printAttributes(finfo.getAttributes(), out, 'f');
/*     */     } 
/*     */     
/*  76 */     out.println();
/*  77 */     list = cf.getMethods();
/*  78 */     n = list.size();
/*  79 */     for (i = 0; i < n; i++) {
/*  80 */       MethodInfo minfo = (MethodInfo)list.get(i);
/*  81 */       int acc = minfo.getAccessFlags();
/*  82 */       out.println(Modifier.toString(AccessFlag.toModifier(acc)) + " " + minfo
/*  83 */           .getName() + "\t" + minfo
/*  84 */           .getDescriptor());
/*  85 */       printAttributes(minfo.getAttributes(), out, 'm');
/*  86 */       out.println();
/*     */     } 
/*     */     
/*  89 */     out.println();
/*  90 */     printAttributes(cf.getAttributes(), out, 'c');
/*     */   }
/*     */   
/*     */   static void printAttributes(List<AttributeInfo> list, PrintWriter out, char kind) {
/*  94 */     if (list == null) {
/*     */       return;
/*     */     }
/*  97 */     int n = list.size();
/*  98 */     for (int i = 0; i < n; i++) {
/*  99 */       AttributeInfo ai = list.get(i);
/* 100 */       if (ai instanceof CodeAttribute) {
/* 101 */         CodeAttribute ca = (CodeAttribute)ai;
/* 102 */         out.println("attribute: " + ai.getName() + ": " + ai
/* 103 */             .getClass().getName());
/* 104 */         out.println("max stack " + ca.getMaxStack() + ", max locals " + ca
/* 105 */             .getMaxLocals() + ", " + ca
/* 106 */             .getExceptionTable().size() + " catch blocks");
/*     */         
/* 108 */         out.println("<code attribute begin>");
/* 109 */         printAttributes(ca.getAttributes(), out, kind);
/* 110 */         out.println("<code attribute end>");
/*     */       }
/* 112 */       else if (ai instanceof AnnotationsAttribute) {
/* 113 */         out.println("annnotation: " + ai.toString());
/*     */       }
/* 115 */       else if (ai instanceof ParameterAnnotationsAttribute) {
/* 116 */         out.println("parameter annnotations: " + ai.toString());
/*     */       }
/* 118 */       else if (ai instanceof StackMapTable) {
/* 119 */         out.println("<stack map table begin>");
/* 120 */         StackMapTable.Printer.print((StackMapTable)ai, out);
/* 121 */         out.println("<stack map table end>");
/*     */       }
/* 123 */       else if (ai instanceof StackMap) {
/* 124 */         out.println("<stack map begin>");
/* 125 */         ((StackMap)ai).print(out);
/* 126 */         out.println("<stack map end>");
/*     */       }
/* 128 */       else if (ai instanceof SignatureAttribute) {
/* 129 */         SignatureAttribute sa = (SignatureAttribute)ai;
/* 130 */         String sig = sa.getSignature();
/* 131 */         out.println("signature: " + sig);
/*     */         try {
/*     */           String s;
/* 134 */           if (kind == 'c') {
/* 135 */             s = SignatureAttribute.toClassSignature(sig).toString();
/* 136 */           } else if (kind == 'm') {
/* 137 */             s = SignatureAttribute.toMethodSignature(sig).toString();
/*     */           } else {
/* 139 */             s = SignatureAttribute.toFieldSignature(sig).toString();
/*     */           } 
/* 141 */           out.println("           " + s);
/*     */         }
/* 143 */         catch (BadBytecode e) {
/* 144 */           out.println("           syntax error");
/*     */         } 
/*     */       } else {
/*     */         
/* 148 */         out.println("attribute: " + ai.getName() + " (" + (ai
/* 149 */             .get()).length + " byte): " + ai
/* 150 */             .getClass().getName());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\ClassFilePrinter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */