/*    */ package me.onefightonewin.feature.types;
/*    */ 
/*    */ import me.onefightonewin.feature.Category;
/*    */ import me.onefightonewin.feature.Feature;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.C03PacketPlayer;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ 
/*    */ public class Nofall
/*    */   extends Feature {
/*    */   public Nofall(Minecraft minecraft) {
/* 14 */     super(minecraft, "nofall", 0, Category.PAGE_5);
/*    */   }
/*    */ 
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/* 20 */     if (this.mc.world == null || this.mc.player == null) {
/*    */       return;
/*    */     }
/*    */     
/* 24 */     if (this.mc.player.isSpectator() || this.mc.player.capabilities.isCreativeMode || this.mc.player.capabilities.isFlying) {
/*    */       return;
/*    */     }
/* 27 */     if (this.mc.player.fallDistance > 2.0F)
/* 28 */       for (int i = 0; i < 10; i++)
/* 29 */         this.mc.getConnection().sendPacket((Packet)new C03PacketPlayer(true));  
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\Nofall.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */