/*    */ package javassist.bytecode.annotation;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.Method;
/*    */ import javassist.ClassPool;
/*    */ import javassist.bytecode.ConstPool;
/*    */ import javassist.bytecode.Descriptor;
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
/*    */ public abstract class MemberValue
/*    */ {
/*    */   ConstPool cp;
/*    */   char tag;
/*    */   
/*    */   MemberValue(char tag, ConstPool cp) {
/* 39 */     this.cp = cp;
/* 40 */     this.tag = tag;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   abstract Object getValue(ClassLoader paramClassLoader, ClassPool paramClassPool, Method paramMethod) throws ClassNotFoundException;
/*    */ 
/*    */ 
/*    */   
/*    */   abstract Class getType(ClassLoader paramClassLoader) throws ClassNotFoundException;
/*    */ 
/*    */ 
/*    */   
/*    */   static Class loadClass(ClassLoader cl, String classname) throws ClassNotFoundException, NoSuchClassError {
/*    */     try {
/* 56 */       return Class.forName(convertFromArray(classname), true, cl);
/*    */     }
/* 58 */     catch (LinkageError e) {
/* 59 */       throw new NoSuchClassError(classname, e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private static String convertFromArray(String classname) {
/* 65 */     int index = classname.indexOf("[]");
/* 66 */     if (index != -1) {
/* 67 */       String rawType = classname.substring(0, index);
/* 68 */       StringBuffer sb = new StringBuffer(Descriptor.of(rawType));
/* 69 */       while (index != -1) {
/* 70 */         sb.insert(0, "[");
/* 71 */         index = classname.indexOf("[]", index + 1);
/*    */       } 
/* 73 */       return sb.toString().replace('/', '.');
/*    */     } 
/* 75 */     return classname;
/*    */   }
/*    */   
/*    */   public abstract void accept(MemberValueVisitor paramMemberValueVisitor);
/*    */   
/*    */   public abstract void write(AnnotationsWriter paramAnnotationsWriter) throws IOException;
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\annotation\MemberValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */