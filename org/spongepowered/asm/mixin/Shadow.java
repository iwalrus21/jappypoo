package org.spongepowered.asm.mixin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Shadow {
  String prefix() default "shadow$";
  
  boolean remap() default true;
  
  String[] aliases() default {};
}


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\spongepowered\asm\mixin\Shadow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */