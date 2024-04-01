package org.spongepowered.asm.launch.platform;

public interface IMixinPlatformAgent {
  String getPhaseProvider();
  
  void prepare();
  
  void initPrimaryContainer();
  
  void inject();
  
  String getLaunchTarget();
}


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\spongepowered\asm\launch\platform\IMixinPlatformAgent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */