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
/*    */ public class AirControl
/*    */   extends Feature
/*    */ {
/*    */   MenuLabel rightBeforeDisableLabel;
/*    */   MenuSlider rightBeforeDisable;
/*    */   MenuLabel aircontrolVerticalLabel;
/*    */   public MenuSlider aircontrolVertical;
/*    */   MenuLabel aircontrolHorizontalLabel;
/*    */   public MenuSlider aircontrolHorizontal;
/*    */   boolean wasEnabled = false;
/* 25 */   int counter = 0;
/*    */   
/*    */   public AirControl(Minecraft minecraft) {
/* 28 */     super(minecraft, "aircontrol", 0, Category.PAGE_3);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMenuInit() {
/* 33 */     this.rightBeforeDisableLabel = new MenuLabel("Right clicks before disable", 0, 0);
/* 34 */     this.rightBeforeDisable = new MenuSlider(5, 0, 10, 0, 0, 100, 6);
/* 35 */     this.aircontrolVerticalLabel = new MenuLabel("Aircontrol vertical", 0, 0);
/* 36 */     this.aircontrolVertical = new MenuSlider(100, 100, 200, 0, 0, 100, 6);
/* 37 */     this.aircontrolHorizontalLabel = new MenuLabel("Aircontrol horizontal", 0, 0);
/* 38 */     this.aircontrolHorizontal = new MenuSlider(100, 100, 200, 0, 0, 100, 6);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMenuAdd(List<MenuComponent> components) {
/* 43 */     components.add(this.rightBeforeDisableLabel);
/* 44 */     components.add(this.rightBeforeDisable);
/* 45 */     components.add(this.aircontrolVerticalLabel);
/* 46 */     components.add(this.aircontrolVertical);
/* 47 */     components.add(this.aircontrolHorizontalLabel);
/* 48 */     components.add(this.aircontrolHorizontal);
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/* 53 */     if (this.mc.world == null || this.mc.player == null) {
/*    */       return;
/*    */     }
/*    */     
/* 57 */     if (this.mc.gameSettings.keyBindUseItem.isKeyDown()) {
/* 58 */       if (!this.wasEnabled) {
/* 59 */         this.wasEnabled = true;
/* 60 */         this.counter++;
/*    */       } 
/*    */       
/* 63 */       if (isEnabled() && this.counter >= this.rightBeforeDisable.getIntValue()) {
/* 64 */         this.counter = 0;
/* 65 */         setEnabled(false);
/*    */       } 
/*    */     } else {
/* 68 */       this.wasEnabled = false;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\AirControl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */