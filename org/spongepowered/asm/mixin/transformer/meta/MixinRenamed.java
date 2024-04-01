package org.spongepowered.asm.mixin.transformer.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MixinRenamed {
  String originalName();
  
  boolean isInterfaceMember();
}


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\spongepowered\asm\mixin\transformer\meta\MixinRenamed.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */