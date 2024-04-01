package javassist.scopedpool;

import java.util.Map;
import javassist.ClassPool;

public interface ScopedClassPoolRepository {
  void setClassPoolFactory(ScopedClassPoolFactory paramScopedClassPoolFactory);
  
  ScopedClassPoolFactory getClassPoolFactory();
  
  boolean isPrune();
  
  void setPrune(boolean paramBoolean);
  
  ScopedClassPool createScopedClassPool(ClassLoader paramClassLoader, ClassPool paramClassPool);
  
  ClassPool findClassPool(ClassLoader paramClassLoader);
  
  ClassPool registerClassLoader(ClassLoader paramClassLoader);
  
  Map getRegisteredCLs();
  
  void clearUnregisteredClassLoaders();
  
  void unregisterClassLoader(ClassLoader paramClassLoader);
}


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\scopedpool\ScopedClassPoolRepository.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */