/*    */ package me.onefightonewin.mixin;
/*    */ 
/*    */ import me.onefightonewin.AntiAFK;
/*    */ import me.onefightonewin.feature.types.Crosshair;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiIngame;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.MovingObjectPosition;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.Overwrite;
/*    */ import org.spongepowered.asm.mixin.Shadow;
/*    */ 
/*    */ 
/*    */ @Mixin({GuiIngame.class})
/*    */ public class MixinGuiIngame
/*    */ {
/*    */   @Shadow
/*    */   protected Minecraft mc;
/*    */   
/*    */   @Overwrite
/*    */   protected boolean func_175183_b() {
/* 22 */     Crosshair mod = (Crosshair)AntiAFK.getInstance().getFeature(Crosshair.class);
/*    */     
/* 24 */     if (mod.isEnabled()) {
/* 25 */       return false;
/*    */     }
/*    */     
/* 28 */     if (this.mc.gameSettings.showDebugInfo && !this.mc.player.hasReducedDebug() && !this.mc.gameSettings.reducedDebugInfo)
/*    */     {
/* 30 */       return false;
/*    */     }
/* 32 */     if (this.mc.playerController.isSpectator()) {
/*    */       
/* 34 */       if (this.mc.pointedEntity != null)
/*    */       {
/* 36 */         return true;
/*    */       }
/*    */ 
/*    */       
/* 40 */       if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
/*    */         
/* 42 */         BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();
/*    */         
/* 44 */         if (this.mc.world.getTileEntity(blockpos) instanceof net.minecraft.inventory.IInventory)
/*    */         {
/* 46 */           return true;
/*    */         }
/*    */       } 
/*    */       
/* 50 */       return false;
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 55 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\mixin\MixinGuiIngame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */