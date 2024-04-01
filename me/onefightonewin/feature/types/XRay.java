/*    */ package me.onefightonewin.feature.types;
/*    */ 
/*    */ import me.onefightonewin.AntiAFK;
/*    */ import me.onefightonewin.feature.Category;
/*    */ import me.onefightonewin.feature.Feature;
/*    */ import net.minecraft.client.Minecraft;
/*    */ 
/*    */ public class XRay
/*    */   extends Feature
/*    */ {
/*    */   public XRay(Minecraft minecraft) {
/* 12 */     super(minecraft, "xray", 0, Category.PAGE_4);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 17 */     this.mc.renderGlobal.loadRenderers();
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 22 */     this.mc.renderGlobal.loadRenderers();
/*    */   }
/*    */   
/*    */   public boolean isXrayBlock(int block) {
/* 26 */     return (AntiAFK.getInstance()).xray.contains(Integer.valueOf(block));
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\XRay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */