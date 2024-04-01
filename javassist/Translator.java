package javassist;

public interface Translator {
  void start(ClassPool paramClassPool) throws NotFoundException, CannotCompileException;
  
  void onLoad(ClassPool paramClassPool, String paramString) throws NotFoundException, CannotCompileException;
}


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\Translator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */