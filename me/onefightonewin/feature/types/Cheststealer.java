/*    */ package me.onefightonewin.feature.types;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ import java.util.List;
/*    */ import me.onefightonewin.AntiAFK;
/*    */ import me.onefightonewin.feature.Category;
/*    */ import me.onefightonewin.feature.Feature;
/*    */ import me.onefightonewin.gui.framework.MenuComponent;
/*    */ import me.onefightonewin.gui.framework.components.MenuLabel;
/*    */ import me.onefightonewin.gui.framework.components.MenuSlider;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import net.minecraft.client.gui.inventory.GuiChest;
/*    */ import net.minecraft.client.gui.inventory.GuiContainer;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ import net.minecraftforge.fml.relauncher.ReflectionHelper;
/*    */ 
/*    */ 
/*    */ public class Cheststealer
/*    */   extends Feature
/*    */ {
/*    */   MenuLabel moveSpeedLabel;
/*    */   MenuSlider moveSpeed;
/*    */   Field field;
/*    */   
/*    */   public Cheststealer(Minecraft minecraft) {
/* 30 */     super(minecraft, "cheststealer", 0, Category.PAGE_5);
/* 31 */     this.field = ReflectionHelper.findField(GuiChest.class, new String[] { "inventoryRows", "field_147018_x" });
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMenuInit() {
/* 36 */     this.moveSpeedLabel = new MenuLabel("Move speed (Ticks)", 0, 0);
/* 37 */     this.moveSpeed = new MenuSlider(5, 1, 40, 0, 0, 100, 6);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMenuAdd(List<MenuComponent> components) {
/* 42 */     components.add(this.moveSpeedLabel);
/* 43 */     components.add(this.moveSpeed);
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/* 48 */     if (this.mc.world == null || this.mc.player == null) {
/*    */       return;
/*    */     }
/*    */     
/* 52 */     if (this.mc.currentScreen instanceof GuiChest) {
/* 53 */       int rows; GuiChest chest = (GuiChest)this.mc.currentScreen;
/*    */       
/* 55 */       if ((AntiAFK.getInstance()).inventoryManager.hasPendingActions(this)) {
/*    */         return;
/*    */       }
/*    */ 
/*    */       
/*    */       try {
/* 61 */         rows = this.field.getInt(chest);
/* 62 */       } catch (IllegalArgumentException|IllegalAccessException e) {
/* 63 */         e.printStackTrace();
/*    */         
/*    */         return;
/*    */       } 
/* 67 */       boolean stoleAny = false;
/*    */       
/* 69 */       for (int item = 0; item <= rows * 9 - 1; item++) {
/* 70 */         ItemStack targetItem = chest.inventorySlots.getSlot(item).getStack();
/* 71 */         if (targetItem != null && targetItem.getItem() != null) {
/* 72 */           (AntiAFK.getInstance()).inventoryManager.quickMove(this, chest.inventorySlots.windowId, item);
/* 73 */           stoleAny = true;
/*    */           
/*    */           break;
/*    */         } 
/*    */       } 
/* 78 */       if (!stoleAny && 
/* 79 */         this.mc.currentScreen instanceof GuiContainer) {
/* 80 */         ((GuiContainer)this.mc.currentScreen).inventorySlots.onContainerClosed((EntityPlayer)this.mc.player);
/* 81 */         this.mc.displayGuiScreen((GuiScreen)null);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\Cheststealer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */