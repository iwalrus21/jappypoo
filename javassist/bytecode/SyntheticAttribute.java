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
/*    */ public class SyntheticAttribute
/*    */   extends AttributeInfo
/*    */ {
/*    */   public static final String tag = "Synthetic";
/*    */   
/*    */   SyntheticAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
/* 35 */     super(cp, n, in);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SyntheticAttribute(ConstPool cp) {
/* 44 */     super(cp, "Synthetic", new byte[0]);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AttributeInfo copy(ConstPool newCp, Map classnames) {
/* 54 */     return new SyntheticAttribute(newCp);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\SyntheticAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */