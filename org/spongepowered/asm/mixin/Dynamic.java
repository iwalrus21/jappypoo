package org.spongepowered.asm.mixin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.CLASS)
public @interface Dynamic {
  String value() default "";
  
  Class<?> mixin() default void.class;
}


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\spongepowered\asm\mixin\Dynamic.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */