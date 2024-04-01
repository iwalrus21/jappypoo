/*    */ package me.onefightonewin.feature.types;
/*    */ 
/*    */ import me.onefightonewin.feature.Category;
/*    */ import me.onefightonewin.feature.Feature;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ 
/*    */ public class Brightness extends Feature {
/* 10 */   float recover = -1.0F;
/*    */   
/*    */   public Brightness(Minecraft minecraft) {
/* 13 */     super(minecraft, "brightness", -1, Category.PAGE_7);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 18 */     this.recover = this.mc.gameSettings.gammaSetting;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 23 */     this.mc.gameSettings.gammaSetting = this.recover;
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/* 28 */     if (this.mc.world == null || this.mc.player == null) {
/*    */       return;
/*    */     }
/*    */     
/* 32 */     if (!isEnabled()) {
/* 33 */       onDisable();
/*    */       
/*    */       return;
/*    */     } 
/*    */     
/* 38 */     this.mc.gameSettings.gammaSetting = 10.0F;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\Brightness.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */