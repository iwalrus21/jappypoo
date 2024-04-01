/*    */ package me.onefightonewin.feature;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraftforge.common.MinecraftForge;
/*    */ 
/*    */ public class Feature
/*    */   implements FeatureImpl
/*    */ {
/*    */   protected Minecraft mc;
/*    */   private boolean state;
/*    */   private String name;
/*    */   private int key;
/*    */   private Category category;
/*    */   
/*    */   public Feature(Minecraft minecraft, String name, int key, Category category) {
/* 16 */     this.mc = minecraft;
/* 17 */     this.name = name;
/* 18 */     this.key = key;
/* 19 */     this.category = category;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setEnabled(boolean state) {
/* 24 */     if (this.state != state) {
/* 25 */       if (state) {
/* 26 */         onEnable();
/* 27 */         MinecraftForge.EVENT_BUS.register(this);
/*    */       } else {
/* 29 */         MinecraftForge.EVENT_BUS.unregister(this);
/* 30 */         onDisable();
/*    */       } 
/*    */     }
/*    */     
/* 34 */     this.state = state;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEnabled() {
/* 39 */     return this.state;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getKey() {
/* 44 */     return this.key;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setKey(int key) {
/* 49 */     this.key = key;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 54 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public Category getCategory() {
/* 59 */     return this.category;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\Feature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */