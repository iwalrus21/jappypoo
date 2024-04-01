/*    */ package me.onefightonewin.feature.types;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ import me.onefightonewin.feature.Category;
/*    */ import me.onefightonewin.feature.Feature;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ import net.minecraftforge.fml.relauncher.ReflectionHelper;
/*    */ 
/*    */ public class NoLeftClickDelay
/*    */   extends Feature {
/*    */   Field field;
/*    */   
/*    */   public NoLeftClickDelay(Minecraft minecraft) {
/* 16 */     super(minecraft, "noleftclickdelay", -1, Category.PAGE_7);
/* 17 */     this.field = ReflectionHelper.findField(Minecraft.class, new String[] { "leftClickCounter", "field_71429_W" });
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/* 22 */     if (this.mc.world == null || this.mc.player == null) {
/*    */       return;
/*    */     }
/*    */     
/*    */     try {
/* 27 */       this.field.setInt(this.mc, 0);
/* 28 */     } catch (IllegalAccessException|IllegalArgumentException e) {
/* 29 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\NoLeftClickDelay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */