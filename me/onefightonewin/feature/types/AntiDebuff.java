/*    */ package me.onefightonewin.feature.types;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import me.onefightonewin.feature.Category;
/*    */ import me.onefightonewin.feature.Feature;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.potion.PotionEffect;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ 
/*    */ public class AntiDebuff
/*    */   extends Feature
/*    */ {
/*    */   public AntiDebuff(Minecraft minecraft) {
/* 17 */     super(minecraft, "antidebuff", 0, Category.PAGE_4);
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/* 22 */     if (this.mc.world == null || this.mc.player == null) {
/*    */       return;
/*    */     }
/*    */     
/* 26 */     List<Integer> remove = new ArrayList<>();
/*    */     
/* 28 */     for (PotionEffect effect : this.mc.player.getActivePotionEffects()) {
/* 29 */       if (effect.func_76456_a() == 15 || effect.func_76456_a() == 9) {
/* 30 */         remove.add(Integer.valueOf(effect.func_76456_a()));
/*    */       }
/*    */     } 
/*    */     
/* 34 */     if (!remove.isEmpty())
/* 35 */       for (Iterator<Integer> iterator = remove.iterator(); iterator.hasNext(); ) { int toRemove = ((Integer)iterator.next()).intValue();
/* 36 */         this.mc.player.func_82170_o(toRemove); }
/*    */        
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\AntiDebuff.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */