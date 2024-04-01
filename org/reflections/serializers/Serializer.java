package org.reflections.serializers;

import java.io.File;
import java.io.InputStream;
import org.reflections.Reflections;

public interface Serializer {
  Reflections read(InputStream paramInputStream);
  
  File save(Reflections paramReflections, String paramString);
  
  String toString(Reflections paramReflections);
}


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\reflections\serializers\Serializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */