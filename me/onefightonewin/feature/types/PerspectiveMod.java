/*    */ package me.onefightonewin.feature.types;
/*    */ 
/*    */ import java.util.List;
/*    */ import me.onefightonewin.feature.Category;
/*    */ import me.onefightonewin.feature.Feature;
/*    */ import me.onefightonewin.gui.framework.MenuComponent;
/*    */ import me.onefightonewin.gui.framework.components.MenuCheckbox;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import org.lwjgl.opengl.Display;
/*    */ 
/*    */ 
/*    */ public class PerspectiveMod
/*    */   extends Feature
/*    */ {
/* 15 */   private static float cameraYaw = 0.0F;
/* 16 */   private static float cameraPitch = 0.0F;
/* 17 */   private static int previousPerspective = 0;
/*    */   
/*    */   public MenuCheckbox holdType;
/*    */   
/*    */   public PerspectiveMod(Minecraft minecraft) {
/* 22 */     super(minecraft, "perspective", 0, Category.PAGE_6);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 27 */     previousPerspective = this.mc.gameSettings.thirdPersonView;
/* 28 */     this.mc.gameSettings.thirdPersonView = 1;
/*    */     
/* 30 */     cameraYaw = this.mc.player.rotationYaw;
/* 31 */     cameraPitch = this.mc.player.rotationPitch;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 36 */     this.mc.gameSettings.thirdPersonView = previousPerspective;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMenuInit() {
/* 41 */     this.holdType = new MenuCheckbox("Use on hold bind", 0, 0, 12, 12);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMenuAdd(List<MenuComponent> components) {
/* 46 */     components.add(this.holdType);
/*    */   }
/*    */   
/*    */   public float getCameraYaw() {
/* 50 */     return isEnabled() ? cameraYaw : this.mc.player.rotationYaw;
/*    */   }
/*    */   
/*    */   public float getCameraPitch() {
/* 54 */     return isEnabled() ? cameraPitch : this.mc.player.rotationPitch;
/*    */   }
/*    */   
/*    */   public boolean overrideMouse() {
/* 58 */     if (this.mc.inGameHasFocus && Display.isActive()) {
/* 59 */       if (!isEnabled()) {
/* 60 */         return true;
/*    */       }
/*    */       
/* 63 */       this.mc.mouseHelper.mouseXYChange();
/* 64 */       float f1 = this.mc.gameSettings.mouseSensitivity * 0.4F + 0.2F;
/* 65 */       float f2 = f1 * f1 * f1 * 8.0F;
/* 66 */       float f3 = this.mc.mouseHelper.deltaX * f2;
/* 67 */       float f4 = this.mc.mouseHelper.deltaY * f2;
/*    */       
/* 69 */       cameraYaw += f3 * 0.15F;
/* 70 */       cameraPitch += f4 * 0.15F;
/*    */       
/* 72 */       if (cameraPitch > 90.0F) {
/* 73 */         cameraPitch = 90.0F;
/*    */       }
/*    */       
/* 76 */       if (cameraPitch < -90.0F) {
/* 77 */         cameraPitch = -90.0F;
/*    */       }
/*    */     } 
/*    */     
/* 81 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\PerspectiveMod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */