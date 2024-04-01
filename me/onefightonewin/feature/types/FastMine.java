/*    */ package me.onefightonewin.feature.types;
/*    */ 
/*    */ import me.onefightonewin.feature.Category;
/*    */ import me.onefightonewin.feature.Feature;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.potion.PotionEffect;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ 
/*    */ public class FastMine
/*    */   extends Feature
/*    */ {
/* 13 */   final int potionId = 3;
/* 14 */   final int amplifier = 1;
/*    */   
/*    */   public FastMine(Minecraft minecraft) {
/* 17 */     super(minecraft, "fastmine", 0, Category.PAGE_5);
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/* 22 */     if (this.mc.world == null || this.mc.player == null) {
/*    */       return;
/*    */     }
/*    */     
/* 26 */     if (!this.mc.player.func_82165_m(3))
/* 27 */       this.mc.player.addPotionEffect(new PotionEffect(3, 3, 1)); 
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\FastMine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */