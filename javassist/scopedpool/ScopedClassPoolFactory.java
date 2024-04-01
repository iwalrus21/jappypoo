package javassist.scopedpool;

import javassist.ClassPool;

public interface ScopedClassPoolFactory {
  ScopedClassPool create(ClassLoader paramClassLoader, ClassPool paramClassPool, ScopedClassPoolRepository paramScopedClassPoolRepository);
  
  ScopedClassPool create(ClassPool paramClassPool, ScopedClassPoolRepository paramScopedClassPoolRepository);
}


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\scopedpool\ScopedClassPoolFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */