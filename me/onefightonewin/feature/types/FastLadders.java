/*    */ package me.onefightonewin.feature.types;
/*    */ 
/*    */ import me.onefightonewin.feature.Category;
/*    */ import me.onefightonewin.feature.Feature;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ 
/*    */ public class FastLadders
/*    */   extends Feature
/*    */ {
/* 12 */   float speed = 2.0F;
/*    */   
/*    */   public FastLadders(Minecraft minecraft) {
/* 15 */     super(minecraft, "fastladders", 0, Category.PAGE_3);
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/* 20 */     if (this.mc.world == null || this.mc.player == null) {
/*    */       return;
/*    */     }
/*    */     
/* 24 */     if (this.mc.player.isSpectator()) {
/*    */       return;
/*    */     }
/* 27 */     if (this.mc.player.isOnLadder() && !this.mc.gameSettings.keyBindSneak.isKeyDown() && this.mc.gameSettings.keyBindForward.isKeyDown())
/* 28 */       this.mc.player.motionY = this.speed * 0.125D; 
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\FastLadders.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */