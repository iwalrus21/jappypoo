/*    */ package me.onefightonewin.mixin;
/*    */ 
/*    */ import me.onefightonewin.AntiAFK;
/*    */ import me.onefightonewin.feature.types.Freecam;
/*    */ import me.onefightonewin.feature.types.XRay;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.util.AxisAlignedBB;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.util.Vec3i;
/*    */ import net.minecraft.world.IBlockAccess;
/*    */ import net.minecraft.world.World;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.Overwrite;
/*    */ import org.spongepowered.asm.mixin.Shadow;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Mixin({Block.class})
/*    */ public class MixinBlock
/*    */ {
/*    */   @Shadow
/*    */   protected double field_149759_B;
/*    */   @Shadow
/*    */   protected double field_149760_C;
/*    */   @Shadow
/*    */   protected double field_149754_D;
/*    */   @Shadow
/*    */   protected double field_149755_E;
/*    */   @Shadow
/*    */   protected double field_149756_F;
/*    */   @Shadow
/*    */   protected double field_149757_G;
/*    */   
/*    */   @Overwrite
/*    */   public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
/* 46 */     Freecam freecam = (Freecam)AntiAFK.getInstance().getFeature(Freecam.class);
/* 47 */     if (freecam.isEnabled() && freecam.fakePlayer != null) {
/* 48 */       return null;
/*    */     }
/*    */     
/* 51 */     return new AxisAlignedBB(pos.getX() + this.field_149759_B, pos.getY() + this.field_149760_C, pos.getZ() + this.field_149754_D, pos.getX() + this.field_149755_E, pos.getY() + this.field_149756_F, pos.getZ() + this.field_149757_G);
/*    */   }
/*    */   
/*    */   @Inject(at = {@At("RETURN")}, method = {"shouldSideBeRendered(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/BlockPos;Lnet/minecraft/util/EnumFacing;)Z"})
/*    */   public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side, CallbackInfoReturnable<Boolean> info) {
/* 56 */     if ((Minecraft.getMinecraft()).player == null || (Minecraft.getMinecraft()).world == null) {
/* 57 */       return ((Boolean)info.getReturnValue()).booleanValue();
/*    */     }
/*    */     
/* 60 */     if (((XRay)AntiAFK.getInstance().getFeature(XRay.class)).isEnabled()) {
/* 61 */       XRay feature = (XRay)AntiAFK.getInstance().getFeature(XRay.class);
/* 62 */       switch (side) {
/*    */         case DOWN:
/* 64 */           return feature.isXrayBlock(Block.getIdFromBlock((Minecraft.getMinecraft()).world.getBlockState(pos.add(new Vec3i(0, 1, 0))).getBlock()));
/*    */         
/*    */         case EAST:
/* 67 */           return feature.isXrayBlock(Block.getIdFromBlock((Minecraft.getMinecraft()).world.getBlockState(pos.subtract(new Vec3i(1, 0, 0))).getBlock()));
/*    */         
/*    */         case NORTH:
/* 70 */           return feature.isXrayBlock(Block.getIdFromBlock((Minecraft.getMinecraft()).world.getBlockState(pos.add(new Vec3i(0, 0, 1))).getBlock()));
/*    */         
/*    */         case SOUTH:
/* 73 */           return feature.isXrayBlock(Block.getIdFromBlock((Minecraft.getMinecraft()).world.getBlockState(pos.subtract(new Vec3i(0, 0, 1))).getBlock()));
/*    */         
/*    */         case UP:
/* 76 */           return feature.isXrayBlock(Block.getIdFromBlock((Minecraft.getMinecraft()).world.getBlockState(pos.subtract(new Vec3i(0, 1, 0))).getBlock()));
/*    */         
/*    */         case WEST:
/* 79 */           return feature.isXrayBlock(Block.getIdFromBlock((Minecraft.getMinecraft()).world.getBlockState(pos.add(new Vec3i(1, 0, 0))).getBlock()));
/*    */       } 
/*    */       
/* 82 */       return feature.isXrayBlock(Block.getIdFromBlock((Minecraft.getMinecraft()).world.getBlockState(pos).getBlock()));
/*    */     } 
/*    */ 
/*    */     
/* 86 */     return ((Boolean)info.getReturnValue()).booleanValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\mixin\MixinBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */