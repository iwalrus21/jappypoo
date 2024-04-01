/*    */ package me.onefightonewin.feature.types;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ import java.util.List;
/*    */ import me.onefightonewin.feature.Category;
/*    */ import me.onefightonewin.feature.Feature;
/*    */ import me.onefightonewin.gui.framework.MenuComponent;
/*    */ import me.onefightonewin.gui.framework.components.MenuCheckbox;
/*    */ import me.onefightonewin.gui.framework.components.MenuLabel;
/*    */ import me.onefightonewin.gui.framework.components.MenuSlider;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ import net.minecraftforge.fml.relauncher.ReflectionHelper;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Fastplace
/*    */   extends Feature
/*    */ {
/*    */   MenuCheckbox blocksOnly;
/*    */   MenuLabel fastPlaceSpeedLabel;
/*    */   MenuSlider fastPlaceSpeed;
/*    */   final Field field;
/*    */   
/*    */   public Fastplace(Minecraft minecraft) {
/* 29 */     super(minecraft, "fastplace", 0, Category.PAGE_5);
/* 30 */     this.field = ReflectionHelper.findField(Minecraft.class, new String[] { "rightClickDelayTimer", "field_71467_ac" });
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMenuInit() {
/* 35 */     this.blocksOnly = new MenuCheckbox("Blocks only", 0, 0, 12, 12);
/* 36 */     this.fastPlaceSpeedLabel = new MenuLabel("Fastplace speed", 0, 0);
/* 37 */     this.fastPlaceSpeed = new MenuSlider(0, 0, 3, 0, 0, 100, 6);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMenuAdd(List<MenuComponent> components) {
/* 42 */     components.add(this.blocksOnly);
/* 43 */     components.add(this.fastPlaceSpeedLabel);
/* 44 */     components.add(this.fastPlaceSpeed);
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/* 49 */     if (this.mc.world == null || this.mc.player == null) {
/*    */       return;
/*    */     }
/*    */     
/* 53 */     if (this.blocksOnly.getRValue() && 
/* 54 */       this.mc.player.func_70694_bm() != null) {
/* 55 */       ItemStack item = this.mc.player.func_70694_bm();
/* 56 */       if (!(item.getItem() instanceof net.minecraft.item.ItemBlock)) {
/*    */         return;
/*    */       }
/*    */     } 
/*    */ 
/*    */     
/*    */     try {
/* 63 */       int delay = this.field.getInt(this.mc);
/* 64 */       if (this.fastPlaceSpeed.getIntValue() < delay) {
/* 65 */         this.field.setInt(this.mc, this.fastPlaceSpeed.getIntValue());
/*    */       }
/* 67 */     } catch (IllegalArgumentException|IllegalAccessException e) {
/* 68 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\Fastplace.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */