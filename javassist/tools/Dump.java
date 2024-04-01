/*    */ package javassist.tools;
/*    */ 
/*    */ import java.io.DataInputStream;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.PrintWriter;
/*    */ import javassist.bytecode.ClassFile;
/*    */ import javassist.bytecode.ClassFilePrinter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Dump
/*    */ {
/*    */   public static void main(String[] args) throws Exception {
/* 43 */     if (args.length != 1) {
/* 44 */       System.err.println("Usage: java Dump <class file name>");
/*    */       
/*    */       return;
/*    */     } 
/* 48 */     DataInputStream in = new DataInputStream(new FileInputStream(args[0]));
/*    */     
/* 50 */     ClassFile w = new ClassFile(in);
/* 51 */     PrintWriter out = new PrintWriter(System.out, true);
/* 52 */     out.println("*** constant pool ***");
/* 53 */     w.getConstPool().print(out);
/* 54 */     out.println();
/* 55 */     out.println("*** members ***");
/* 56 */     ClassFilePrinter.print(w, out);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\tools\Dump.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */