package javassist;

import java.io.InputStream;
import java.net.URL;

public interface ClassPath {
  InputStream openClassfile(String paramString) throws NotFoundException;
  
  URL find(String paramString);
  
  void close();
}


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\ClassPath.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */