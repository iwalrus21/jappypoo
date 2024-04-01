/*    */ package org.yaml.snakeyaml.util;
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
/*    */ public class PlatformFeatureDetector
/*    */ {
/* 20 */   private Boolean isRunningOnAndroid = null;
/*    */   
/*    */   public boolean isRunningOnAndroid() {
/* 23 */     if (this.isRunningOnAndroid == null)
/*    */     {
/* 25 */       this.isRunningOnAndroid = Boolean.valueOf(System.getProperty("java.runtime.name").startsWith("Android Runtime"));
/*    */     }
/* 27 */     return this.isRunningOnAndroid.booleanValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\yaml\snakeyam\\util\PlatformFeatureDetector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */