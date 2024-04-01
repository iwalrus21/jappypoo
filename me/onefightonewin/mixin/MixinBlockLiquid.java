/*    */ package me.onefightonewin.mixin;
/*    */ 
/*    */ import me.onefightonewin.AntiAFK;
/*    */ import me.onefightonewin.feature.types.Freecam;
/*    */ import me.onefightonewin.feature.types.Jesus;
/*    */ import net.minecraft.block.BlockLiquid;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.util.AxisAlignedBB;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.Overwrite;
/*    */ 
/*    */ @Mixin({BlockLiquid.class})
/*    */ public abstract class MixinBlockLiquid
/*    */ {
/* 17 */   protected double minX = 0.0D;
/* 18 */   protected double minY = 0.0D;
/* 19 */   protected double minZ = 0.0D;
/* 20 */   protected double maxX = 1.0D;
/* 21 */   protected double maxY = 1.0D;
/* 22 */   protected double maxZ = 1.0D;
/*    */ 
/*    */   
/*    */   @Overwrite
/*    */   public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
/* 27 */     if (((Jesus)AntiAFK.getInstance().getFeature(Jesus.class)).isEnabled() && !((Freecam)AntiAFK.getInstance().getFeature(Freecam.class)).isEnabled()) {
/* 28 */       return new AxisAlignedBB(pos.getX() + this.minX, pos.getY() + this.minY, pos.getZ() + this.minZ, pos.getX() + this.maxX, pos.getY() + this.maxY, pos.getZ() + this.maxZ);
/*    */     }
/* 30 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\mixin\MixinBlockLiquid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */