/*    */ package me.onefightonewin.feature.types;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ import me.onefightonewin.feature.Category;
/*    */ import me.onefightonewin.feature.Feature;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.settings.KeyBinding;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ import net.minecraftforge.fml.relauncher.ReflectionHelper;
/*    */ import org.lwjgl.input.Keyboard;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InventoryMove
/*    */   extends Feature
/*    */ {
/*    */   Field field;
/*    */   
/*    */   public InventoryMove(Minecraft minecraft) {
/* 23 */     super(minecraft, "invmove", 0, Category.PAGE_3);
/* 24 */     this.field = ReflectionHelper.findField(KeyBinding.class, new String[] { "pressed", "field_74513_e" });
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/* 29 */     if (this.mc.world == null || this.mc.player == null) {
/*    */       return;
/*    */     }
/*    */     
/* 33 */     if (this.mc.currentScreen != null) {
/* 34 */       if (this.mc.currentScreen instanceof net.minecraft.client.gui.GuiChat) {
/*    */         return;
/*    */       }
/* 37 */       if (this.mc.currentScreen instanceof net.minecraft.client.gui.inventory.GuiEditSign) {
/*    */         return;
/*    */       }
/* 40 */       if (this.mc.currentScreen instanceof net.minecraft.client.gui.GuiCommandBlock) {
/*    */         return;
/*    */       }
/* 43 */       if (this.mc.currentScreen instanceof net.minecraft.client.gui.inventory.GuiContainerCreative) {
/*    */         return;
/*    */       }
/*    */       try {
/* 47 */         this.field.set(this.mc.gameSettings.keyBindForward, Boolean.valueOf(Keyboard.isKeyDown(this.mc.gameSettings.keyBindForward.getKeyCode())));
/* 48 */         this.field.set(this.mc.gameSettings.keyBindLeft, Boolean.valueOf(Keyboard.isKeyDown(this.mc.gameSettings.keyBindLeft.getKeyCode())));
/* 49 */         this.field.set(this.mc.gameSettings.keyBindBack, Boolean.valueOf(Keyboard.isKeyDown(this.mc.gameSettings.keyBindBack.getKeyCode())));
/* 50 */         this.field.set(this.mc.gameSettings.keyBindRight, Boolean.valueOf(Keyboard.isKeyDown(this.mc.gameSettings.keyBindRight.getKeyCode())));
/*    */ 
/*    */         
/* 53 */         this.field.set(this.mc.gameSettings.keyBindJump, Boolean.valueOf(Keyboard.isKeyDown(this.mc.gameSettings.keyBindJump.getKeyCode())));
/* 54 */       } catch (IllegalArgumentException|IllegalAccessException e) {
/* 55 */         e.printStackTrace();
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\InventoryMove.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */