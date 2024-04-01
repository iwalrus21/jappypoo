/*     */ package javassist.tools.reflect;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtClass;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Compiler
/*     */ {
/*     */   public static void main(String[] args) throws Exception {
/*  74 */     if (args.length == 0) {
/*  75 */       help(System.err);
/*     */       
/*     */       return;
/*     */     } 
/*  79 */     CompiledClass[] entries = new CompiledClass[args.length];
/*  80 */     int n = parse(args, entries);
/*     */     
/*  82 */     if (n < 1) {
/*  83 */       System.err.println("bad parameter.");
/*     */       
/*     */       return;
/*     */     } 
/*  87 */     processClasses(entries, n);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void processClasses(CompiledClass[] entries, int n) throws Exception {
/*  93 */     Reflection implementor = new Reflection();
/*  94 */     ClassPool pool = ClassPool.getDefault();
/*  95 */     implementor.start(pool);
/*     */     int i;
/*  97 */     for (i = 0; i < n; i++) {
/*  98 */       CtClass c = pool.get((entries[i]).classname);
/*  99 */       if ((entries[i]).metaobject != null || (entries[i]).classobject != null) {
/*     */         String metaobj, classobj;
/*     */ 
/*     */         
/* 103 */         if ((entries[i]).metaobject == null) {
/* 104 */           metaobj = "javassist.tools.reflect.Metaobject";
/*     */         } else {
/* 106 */           metaobj = (entries[i]).metaobject;
/*     */         } 
/* 108 */         if ((entries[i]).classobject == null) {
/* 109 */           classobj = "javassist.tools.reflect.ClassMetaobject";
/*     */         } else {
/* 111 */           classobj = (entries[i]).classobject;
/*     */         } 
/* 113 */         if (!implementor.makeReflective(c, pool.get(metaobj), pool
/* 114 */             .get(classobj))) {
/* 115 */           System.err.println("Warning: " + c.getName() + " is reflective.  It was not changed.");
/*     */         }
/*     */         
/* 118 */         System.err.println(c.getName() + ": " + metaobj + ", " + classobj);
/*     */       }
/*     */       else {
/*     */         
/* 122 */         System.err.println(c.getName() + ": not reflective");
/*     */       } 
/*     */     } 
/* 125 */     for (i = 0; i < n; i++) {
/* 126 */       implementor.onLoad(pool, (entries[i]).classname);
/* 127 */       pool.get((entries[i]).classname).writeFile();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int parse(String[] args, CompiledClass[] result) {
/* 132 */     int n = -1;
/* 133 */     for (int i = 0; i < args.length; i++) {
/* 134 */       String a = args[i];
/* 135 */       if (a.equals("-m"))
/* 136 */       { if (n < 0 || i + 1 > args.length) {
/* 137 */           return -1;
/*     */         }
/* 139 */         (result[n]).metaobject = args[++i]; }
/* 140 */       else if (a.equals("-c"))
/* 141 */       { if (n < 0 || i + 1 > args.length) {
/* 142 */           return -1;
/*     */         }
/* 144 */         (result[n]).classobject = args[++i]; }
/* 145 */       else { if (a.charAt(0) == '-') {
/* 146 */           return -1;
/*     */         }
/* 148 */         CompiledClass cc = new CompiledClass();
/* 149 */         cc.classname = a;
/* 150 */         cc.metaobject = null;
/* 151 */         cc.classobject = null;
/* 152 */         result[++n] = cc; }
/*     */     
/*     */     } 
/*     */     
/* 156 */     return n + 1;
/*     */   }
/*     */   
/*     */   private static void help(PrintStream out) {
/* 160 */     out.println("Usage: java javassist.tools.reflect.Compiler");
/* 161 */     out.println("            (<class> [-m <metaobject>] [-c <class metaobject>])+");
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\tools\reflect\Compiler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */