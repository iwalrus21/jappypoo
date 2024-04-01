/*    */ package me.onefightonewin.mixin;
/*    */ 
/*    */ import me.onefightonewin.AntiAFK;
/*    */ import me.onefightonewin.feature.types.BlockOverlay;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.client.multiplayer.WorldClient;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.RenderGlobal;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.MovingObjectPosition;
/*    */ import net.minecraft.world.IBlockAccess;
/*    */ import net.minecraft.world.World;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.Overwrite;
/*    */ import org.spongepowered.asm.mixin.Shadow;
/*    */ 
/*    */ 
/*    */ @Mixin({RenderGlobal.class})
/*    */ public abstract class MixinRenderGlobal
/*    */ {
/*    */   @Shadow
/*    */   private WorldClient world;
/*    */   
/*    */   @Overwrite
/*    */   public void drawSelectionBox(EntityPlayer player, MovingObjectPosition movingObjectPositionIn, int p_72731_3_, float partialTicks) {
/* 29 */     BlockOverlay mod = (BlockOverlay)AntiAFK.getInstance().getFeature(BlockOverlay.class);
/*    */     
/* 31 */     if (p_72731_3_ == 0 && movingObjectPositionIn.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
/*    */       
/* 33 */       GlStateManager.enableBlend();
/* 34 */       GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*    */       
/* 36 */       if (mod.isEnabled()) {
/* 37 */         GL11.glColor4f(mod.color.getColor().getRed() / 255.0F, mod.color.getColor().getGreen() / 255.0F, mod.color.getColor().getBlue() / 255.0F, mod.color.getColor().getAlpha() / 255.0F);
/*    */       } else {
/* 39 */         GlStateManager.color(0.0F, 0.0F, 0.0F, 0.4F);
/*    */       } 
/*    */       
/* 42 */       GL11.glLineWidth(2.0F);
/* 43 */       GlStateManager.disableTexture2D();
/*    */       
/* 45 */       if (mod.isEnabled() && mod.opaqueBlock.getRValue()) {
/* 46 */         GL11.glDisable(2929);
/*    */       } else {
/* 48 */         GlStateManager.depthMask(false);
/*    */       } 
/*    */       
/* 51 */       float f = 0.002F;
/* 52 */       BlockPos blockpos = movingObjectPositionIn.getBlockPos();
/* 53 */       Block block = this.world.getBlockState(blockpos).getBlock();
/*    */       
/* 55 */       if (block.getMaterial() != Material.AIR && this.world.getWorldBorder().contains(blockpos)) {
/*    */         
/* 57 */         block.func_180654_a((IBlockAccess)this.world, blockpos);
/* 58 */         double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
/* 59 */         double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
/* 60 */         double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
/* 61 */         RenderGlobal.func_181561_a(block.getCollisionBoundingBox((World)this.world, blockpos).grow(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D).offset(-d0, -d1, -d2));
/*    */       } 
/*    */       
/* 64 */       if (mod.isEnabled() && mod.opaqueBlock.getRValue()) {
/* 65 */         GL11.glEnable(2929);
/*    */       } else {
/* 67 */         GlStateManager.depthMask(true);
/*    */       } 
/*    */       
/* 70 */       GlStateManager.enableTexture2D();
/* 71 */       GlStateManager.disableBlend();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\mixin\MixinRenderGlobal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */