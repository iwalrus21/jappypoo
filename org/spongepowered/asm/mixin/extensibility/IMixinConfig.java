package org.spongepowered.asm.mixin.extensibility;

import java.util.Set;
import org.spongepowered.asm.mixin.MixinEnvironment;

public interface IMixinConfig {
  public static final int DEFAULT_PRIORITY = 1000;
  
  MixinEnvironment getEnvironment();
  
  String getName();
  
  String getMixinPackage();
  
  int getPriority();
  
  IMixinConfigPlugin getPlugin();
  
  boolean isRequired();
  
  Set<String> getTargets();
}


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\spongepowered\asm\mixin\extensibility\IMixinConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */