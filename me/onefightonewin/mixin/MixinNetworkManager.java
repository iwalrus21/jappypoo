/*    */ package me.onefightonewin.mixin;
/*    */ 
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import me.onefightonewin.AntiAFK;
/*    */ import me.onefightonewin.feature.Feature;
/*    */ import me.onefightonewin.feature.types.Freecam;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.network.NetworkManager;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.C01PacketChatMessage;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ 
/*    */ @Mixin({NetworkManager.class})
/*    */ public class MixinNetworkManager
/*    */ {
/*    */   @Inject(method = {"channelRead0"}, at = {@At("HEAD")})
/*    */   private void read(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callback) {
/* 21 */     if ((Minecraft.getMinecraft()).world == null || (Minecraft.getMinecraft()).player == null) {
/*    */       return;
/*    */     }
/*    */     
/* 25 */     if (!(AntiAFK.getInstance()).legit) {
/* 26 */       for (Feature feature : (AntiAFK.getInstance()).features) {
/* 27 */         if (feature.isEnabled()) {
/* 28 */           feature.onProcessPacket(packet);
/*    */         }
/*    */       } 
/*    */     }
/*    */   }
/*    */   
/*    */   @Inject(method = {"sendPacket"}, at = {@At("HEAD")}, cancellable = true)
/*    */   public void sendPacket(Packet<?> packet, CallbackInfo info) {
/* 36 */     if ((AntiAFK.getInstance()).legit) {
/*    */       return;
/*    */     }
/*    */     
/* 40 */     if (packet instanceof C01PacketChatMessage) {
/* 41 */       C01PacketChatMessage message = (C01PacketChatMessage)packet;
/*    */       
/* 43 */       if (!(AntiAFK.getInstance()).commandManager.processCommand(message.getMessage())) {
/* 44 */         info.cancel();
/*    */       }
/*    */     } 
/*    */     
/* 48 */     Freecam freecam = (Freecam)AntiAFK.getInstance().getFeature(Freecam.class);
/*    */     
/* 50 */     if (!freecam.isEnabled() && 
/* 51 */       freecam.fakePlayer != null) {
/* 52 */       info.cancel();
/*    */     }
/*    */ 
/*    */     
/* 56 */     boolean ok = true;
/*    */     
/* 58 */     for (Feature feature : (AntiAFK.getInstance()).features) {
/* 59 */       if (feature.isEnabled() && 
/* 60 */         !feature.onPacketSend(packet)) {
/* 61 */         ok = false;
/*    */       }
/*    */     } 
/*    */ 
/*    */     
/* 66 */     if (!ok && 
/* 67 */       !info.isCancelled())
/* 68 */       info.cancel(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\mixin\MixinNetworkManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */