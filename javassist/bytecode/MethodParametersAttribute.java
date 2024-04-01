/*    */ package javassist.bytecode;
/*    */ 
/*    */ import java.io.DataInputStream;
/*    */ import java.io.IOException;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MethodParametersAttribute
/*    */   extends AttributeInfo
/*    */ {
/*    */   public static final String tag = "MethodParameters";
/*    */   
/*    */   MethodParametersAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
/* 19 */     super(cp, n, in);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MethodParametersAttribute(ConstPool cp, String[] names, int[] flags) {
/* 31 */     super(cp, "MethodParameters");
/* 32 */     byte[] data = new byte[names.length * 4 + 1];
/* 33 */     data[0] = (byte)names.length;
/* 34 */     for (int i = 0; i < names.length; i++) {
/* 35 */       ByteArray.write16bit(cp.addUtf8Info(names[i]), data, i * 4 + 1);
/* 36 */       ByteArray.write16bit(flags[i], data, i * 4 + 3);
/*    */     } 
/*    */     
/* 39 */     set(data);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int size() {
/* 47 */     return this.info[0] & 0xFF;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int name(int i) {
/* 56 */     return ByteArray.readU16bit(this.info, i * 4 + 1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int accessFlags(int i) {
/* 66 */     return ByteArray.readU16bit(this.info, i * 4 + 3);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AttributeInfo copy(ConstPool newCp, Map classnames) {
/* 76 */     int s = size();
/* 77 */     ConstPool cp = getConstPool();
/* 78 */     String[] names = new String[s];
/* 79 */     int[] flags = new int[s];
/* 80 */     for (int i = 0; i < s; i++) {
/* 81 */       names[i] = cp.getUtf8Info(name(i));
/* 82 */       flags[i] = accessFlags(i);
/*    */     } 
/*    */     
/* 85 */     return new MethodParametersAttribute(newCp, names, flags);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\MethodParametersAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */