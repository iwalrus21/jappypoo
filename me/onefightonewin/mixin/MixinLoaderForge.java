/*    */ package me.onefightonewin.mixin;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
/*    */ import org.spongepowered.asm.launch.MixinBootstrap;
/*    */ import org.spongepowered.asm.mixin.MixinEnvironment;
/*    */ import org.spongepowered.asm.mixin.Mixins;
/*    */ 
/*    */ 
/*    */ public class MixinLoaderForge
/*    */   implements IFMLLoadingPlugin
/*    */ {
/*    */   public MixinLoaderForge() {
/* 14 */     MixinBootstrap.init();
/* 15 */     Mixins.addConfiguration("mixins.antiafk.json");
/* 16 */     MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
/*    */   }
/*    */ 
/*    */   
/*    */   public String[] getASMTransformerClass() {
/* 21 */     return new String[0];
/*    */   }
/*    */ 
/*    */   
/*    */   public String getModContainerClass() {
/* 26 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSetupClass() {
/* 31 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void injectData(Map<String, Object> data) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public String getAccessTransformerClass() {
/* 41 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\mixin\MixinLoaderForge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */