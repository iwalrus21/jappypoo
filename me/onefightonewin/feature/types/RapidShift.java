/*    */ package me.onefightonewin.feature.types;
/*    */ 
/*    */ import java.util.List;
/*    */ import me.onefightonewin.feature.Category;
/*    */ import me.onefightonewin.feature.Feature;
/*    */ import me.onefightonewin.gui.framework.MenuComponent;
/*    */ import me.onefightonewin.gui.framework.components.MenuLabel;
/*    */ import me.onefightonewin.gui.framework.components.MenuSlider;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.settings.KeyBinding;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RapidShift
/*    */   extends Feature
/*    */ {
/*    */   MenuLabel label;
/*    */   MenuSlider interval;
/*    */   long lastShift;
/*    */   boolean shiftValue;
/*    */   
/*    */   public RapidShift(Minecraft minecraft) {
/* 25 */     super(minecraft, "rapidshift", 0, Category.PAGE_7);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMenuInit() {
/* 30 */     this.label = new MenuLabel("Rapid shift interval (MS)", 0, 0);
/* 31 */     this.interval = new MenuSlider(120, 1, 250, 0, 0, 100, 6);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMenuAdd(List<MenuComponent> components) {
/* 36 */     components.add(this.label);
/* 37 */     components.add(this.interval);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 42 */     KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), false);
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/* 47 */     if (this.mc.world == null || this.mc.player == null) {
/*    */       return;
/*    */     }
/*    */     
/* 51 */     long time = System.currentTimeMillis();
/*    */     
/* 53 */     if ((float)(time - this.lastShift) > this.interval.getValue()) {
/* 54 */       this.lastShift = System.currentTimeMillis();
/*    */     } else {
/*    */       return;
/*    */     } 
/*    */     
/* 59 */     this.shiftValue = !this.shiftValue;
/* 60 */     KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), this.shiftValue);
/*    */   }
/*    */   
/*    */   public boolean shouldShift() {
/* 64 */     return this.shiftValue;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\RapidShift.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */