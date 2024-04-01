/*    */ package me.onefightonewin.feature.types;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ import java.util.List;
/*    */ import me.onefightonewin.feature.Category;
/*    */ import me.onefightonewin.feature.Feature;
/*    */ import me.onefightonewin.gui.framework.MenuComponent;
/*    */ import me.onefightonewin.gui.framework.components.MenuComboBox;
/*    */ import me.onefightonewin.gui.framework.components.MenuLabel;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ import net.minecraftforge.fml.relauncher.ReflectionHelper;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NoSlowDown
/*    */   extends Feature
/*    */ {
/*    */   MenuLabel modesLabel;
/*    */   MenuComboBox modes;
/*    */   Field field;
/*    */   
/*    */   public NoSlowDown(Minecraft minecraft) {
/* 28 */     super(minecraft, "noslowdown", 0, Category.PAGE_5);
/* 29 */     this.field = ReflectionHelper.findField(Entity.class, new String[] { "isInWeb", "field_70134_J" });
/*    */   }
/*    */   
/*    */   enum Modes {
/* 33 */     ALL, WEAPONS_ONLY, COBWEBS_ONLY;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMenuInit() {
/* 38 */     this.modesLabel = new MenuLabel("NoSlowDown Modes", 0, 0);
/* 39 */     this.modes = new MenuComboBox(Modes.class, 0, 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMenuAdd(List<MenuComponent> components) {
/* 44 */     components.add(this.modesLabel);
/* 45 */     components.add(this.modes);
/*    */   }
/*    */   
/*    */   public boolean hasCondition(Modes cond) {
/* 49 */     for (String condition : this.modes.getValues()) {
/* 50 */       if (condition.equalsIgnoreCase(cond.toString()))
/* 51 */         return true; 
/*    */     } 
/* 53 */     return false;
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/* 58 */     if (this.mc.world == null || this.mc.player == null || (!hasCondition(Modes.ALL) && !hasCondition(Modes.COBWEBS_ONLY))) {
/*    */       return;
/*    */     }
/*    */     
/*    */     try {
/* 63 */       boolean inWeb = this.field.getBoolean(this.mc.player);
/*    */       
/* 65 */       if (inWeb) {
/* 66 */         this.field.setBoolean(this.mc.player, false);
/*    */       }
/* 68 */     } catch (IllegalArgumentException|IllegalAccessException e) {
/* 69 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean shouldApplyItemSlowdown() {
/* 76 */     if (hasCondition(Modes.COBWEBS_ONLY)) {
/* 77 */       return true;
/*    */     }
/*    */     
/* 80 */     if (hasCondition(Modes.WEAPONS_ONLY)) {
/* 81 */       ItemStack stack = this.mc.player.func_71045_bC();
/* 82 */       if (stack != null) {
/* 83 */         if (stack.getItem() == Items.DIAMOND_SWORD || stack.getItem() == Items.GOLDEN_SWORD || stack.getItem() == Items.IRON_SWORD || stack.getItem() == Items.STONE_SWORD || stack.getItem() == Items.WOODEN_SWORD) {
/* 84 */           return false;
/*    */         }
/* 86 */         return true;
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 91 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\NoSlowDown.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */