/*    */ package me.onefightonewin.feature.types;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ import java.util.List;
/*    */ import me.onefightonewin.feature.Category;
/*    */ import me.onefightonewin.feature.Feature;
/*    */ import me.onefightonewin.gui.framework.MenuComponent;
/*    */ import me.onefightonewin.gui.framework.components.MenuLabel;
/*    */ import me.onefightonewin.gui.framework.components.MenuSlider;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.util.Timer;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ import net.minecraftforge.fml.relauncher.ReflectionHelper;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Bhop
/*    */   extends Feature
/*    */ {
/*    */   MenuLabel bhopSpeedLabel;
/*    */   MenuSlider bhopSpeed;
/*    */   Field field;
/*    */   
/*    */   public Bhop(Minecraft minecraft) {
/* 26 */     super(minecraft, "bhop", 0, Category.PAGE_3);
/* 27 */     this.field = ReflectionHelper.findField(Minecraft.class, new String[] { "timer", "field_71428_T" });
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMenuInit() {
/* 32 */     this.bhopSpeedLabel = new MenuLabel("Bhop speed", 0, 0);
/* 33 */     this.bhopSpeed = new MenuSlider(0.05F, 0.0F, 0.1F, 2, 0, 0, 100, 6);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMenuAdd(List<MenuComponent> components) {
/* 38 */     components.add(this.bhopSpeedLabel);
/* 39 */     components.add(this.bhopSpeed);
/*    */   }
/*    */   @SubscribeEvent
/*    */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/*    */     Timer timer;
/* 44 */     if (this.mc.world == null || this.mc.player == null) {
/*    */       return;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     try {
/* 51 */       timer = Timer.class.cast(this.field.get(this.mc));
/* 52 */     } catch (IllegalArgumentException|IllegalAccessException e1) {
/* 53 */       e1.printStackTrace();
/*    */       
/*    */       return;
/*    */     } 
/* 57 */     if (this.mc.gameSettings.keyBindSneak.isKeyDown() || this.mc.gameSettings.keyBindUseItem.isKeyDown() || this.mc.gameSettings.keyBindAttack.isKeyDown() || this.mc.player.isInWater() || this.mc.player.isInLava() || this.mc.player.onGround || this.mc.player.capabilities.isFlying || (this.mc.player.movementInput.field_78900_b == 0.0F && this.mc.player.movementInput.moveStrafe == 0.0F)) {
/* 58 */       timer.field_74278_d = 1.0F;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/*    */       return;
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 85 */     timer.field_74278_d = 1.0F + this.bhopSpeed.getValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\Bhop.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */