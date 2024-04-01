/*    */ package me.onefightonewin.feature.types;
/*    */ 
/*    */ import me.onefightonewin.AntiAFK;
/*    */ import me.onefightonewin.feature.Category;
/*    */ import me.onefightonewin.feature.Feature;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.MovingObjectPosition;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ import org.lwjgl.input.Mouse;
/*    */ 
/*    */ public class MiddeClickFriends
/*    */   extends Feature {
/*    */   boolean ok = false;
/*    */   
/*    */   public MiddeClickFriends(Minecraft minecraft) {
/* 19 */     super(minecraft, "middleclickfriends", -1, Category.PAGE_MISC);
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/* 24 */     if (this.mc.world == null || this.mc.player == null) {
/*    */       return;
/*    */     }
/*    */     
/* 28 */     if (Mouse.isButtonDown(2)) {
/* 29 */       if (this.ok && 
/* 30 */         this.mc.objectMouseOver != null && 
/* 31 */         this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
/* 32 */         Entity entity = this.mc.objectMouseOver.entityHit;
/* 33 */         if (entity instanceof EntityPlayer) {
/* 34 */           EntityPlayer player = (EntityPlayer)entity;
/* 35 */           String name = player.getName();
/*    */           
/* 37 */           if ((AntiAFK.getInstance()).friends.contains(name)) {
/* 38 */             (AntiAFK.getInstance()).friends.remove(name);
/*    */           } else {
/* 40 */             (AntiAFK.getInstance()).friends.add(name);
/*    */           } 
/*    */           
/* 43 */           this.ok = false;
/*    */         }
/*    */       
/*    */       } 
/*    */     } else {
/*    */       
/* 49 */       this.ok = true;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\MiddeClickFriends.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */