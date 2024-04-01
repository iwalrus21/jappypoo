/*    */ package me.onefightonewin.feature.types;
/*    */ 
/*    */ import java.util.List;
/*    */ import me.onefightonewin.feature.Category;
/*    */ import me.onefightonewin.feature.Feature;
/*    */ import me.onefightonewin.gui.framework.MenuComponent;
/*    */ import me.onefightonewin.gui.framework.components.MenuLabel;
/*    */ import me.onefightonewin.gui.framework.components.MenuSlider;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Fly
/*    */   extends Feature
/*    */ {
/*    */   MenuLabel flyLabel;
/*    */   MenuSlider flySpeed;
/* 20 */   final float baseSpeed = 0.05F;
/*    */   
/*    */   public Fly(Minecraft minecraft) {
/* 23 */     super(minecraft, "fly", 0, Category.PAGE_3);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onMenuInit() {
/* 29 */     this.flyLabel = new MenuLabel("Fly speed", 0, 0);
/* 30 */     this.flySpeed = new MenuSlider(1, 1, 10, 0, 0, 100, 6);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMenuAdd(List<MenuComponent> components) {
/* 35 */     components.add(this.flyLabel);
/* 36 */     components.add(this.flySpeed);
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/* 41 */     if (this.mc.world == null || this.mc.player == null) {
/*    */       return;
/*    */     }
/*    */     
/* 45 */     if (!isEnabled()) {
/* 46 */       onDisable();
/*    */       
/*    */       return;
/*    */     } 
/* 50 */     this.mc.player.capabilities.setFlySpeed(this.flySpeed.getValue() * 0.05F);
/* 51 */     this.mc.player.capabilities.isFlying = true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 56 */     this.mc.player.capabilities.setFlySpeed(0.05F);
/* 57 */     this.mc.player.capabilities.isFlying = false;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\Fly.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */