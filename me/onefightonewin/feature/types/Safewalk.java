/*    */ package me.onefightonewin.feature.types;
/*    */ 
/*    */ import java.util.List;
/*    */ import me.onefightonewin.feature.Category;
/*    */ import me.onefightonewin.feature.Feature;
/*    */ import me.onefightonewin.gui.framework.MenuComponent;
/*    */ import me.onefightonewin.gui.framework.components.MenuCheckbox;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.settings.KeyBinding;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.Vec3;
/*    */ import net.minecraft.world.IBlockAccess;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ import org.lwjgl.input.Keyboard;
/*    */ 
/*    */ public class Safewalk
/*    */   extends Feature {
/*    */   public MenuCheckbox safeWalkSilent;
/*    */   public MenuCheckbox safeWalkSmart;
/*    */   
/*    */   public Safewalk(Minecraft minecraft) {
/* 24 */     super(minecraft, "safewalk", 0, Category.PAGE_3);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMenuInit() {
/* 29 */     this.safeWalkSilent = new MenuCheckbox("Safewalk - Silent", 0, 0, 12, 12);
/* 30 */     this.safeWalkSmart = new MenuCheckbox("Safewalk - Smart", 0, 0, 12, 12);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMenuAdd(List<MenuComponent> components) {
/* 35 */     components.add(this.safeWalkSilent);
/* 36 */     components.add(this.safeWalkSmart);
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/* 41 */     if (this.mc.world == null || this.mc.player == null) {
/*    */       return;
/*    */     }
/*    */     
/* 45 */     if (!this.safeWalkSilent.getRValue()) {
/* 46 */       if (this.safeWalkSmart.getRValue() && this.mc.gameSettings.keyBindForward.isKeyDown()) {
/* 47 */         KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), false);
/*    */         
/*    */         return;
/*    */       } 
/* 51 */       if (this.mc.player.onGround && !this.mc.gameSettings.keyBindJump.isKeyDown()) {
/* 52 */         Block block = this.mc.world.getBlockState(new BlockPos(this.mc.player.getPositionVector().add(new Vec3(0.0D, -0.5D, 0.0D)))).getBlock();
/*    */         
/*    */         try {
/* 55 */           boolean passable = block.isPassable((IBlockAccess)this.mc.world, this.mc.player.getPosition());
/* 56 */           KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), passable);
/* 57 */         } catch (IllegalArgumentException illegalArgumentException) {}
/*    */       
/*    */       }
/* 60 */       else if (!Keyboard.isKeyDown(this.mc.gameSettings.keyBindSneak.getKeyCode())) {
/* 61 */         KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), false);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\Safewalk.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */