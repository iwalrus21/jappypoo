package org.spongepowered.asm.mixin.injection;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Slice {
  String id() default "";
  
  At from() default @At("HEAD");
  
  At to() default @At("TAIL");
}


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\spongepowered\asm\mixin\injection\Slice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */