/*    */ package me.onefightonewin.feature;
/*    */ 
/*    */ import java.util.List;
/*    */ import me.onefightonewin.gui.framework.MenuComponent;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.scoreboard.ScoreObjective;
/*    */ import net.minecraft.scoreboard.Scoreboard;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface FeatureImpl
/*    */ {
/*    */   default void onEnable() {}
/*    */   
/*    */   default void onDisable() {}
/*    */   
/*    */   String getName();
/*    */   
/*    */   Category getCategory();
/*    */   
/*    */   default boolean onPacketSend(Packet<?> packet) {
/* 25 */     return true;
/*    */   } default void onMenuInit() {} default void onMenuAdd(List<MenuComponent> components) {} default void onRenderWorldPass() {} default void onRenderPlayerlist(Scoreboard scoreboardIn, ScoreObjective scoreObjectiveIn) {} default void onProcessPacket(Packet<?> packet) {} default void onAttacKEntity(Entity entity) {} default boolean onAppendMessage(IChatComponent packet) {
/* 27 */     return true;
/*    */   } default void onLeftClick() {}
/*    */   default boolean onRightClick() {
/* 30 */     return true;
/*    */   }
/* 32 */   default boolean shouldApplyItemSlowdown() { return true; } default boolean shouldApplyBlockSlowdown() {
/* 33 */     return true;
/*    */   }
/*    */   
/*    */   int getKey();
/*    */   
/*    */   void setKey(int paramInt);
/*    */   
/*    */   void setEnabled(boolean paramBoolean);
/*    */   
/*    */   boolean isEnabled();
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\FeatureImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */