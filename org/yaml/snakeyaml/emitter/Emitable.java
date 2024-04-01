package org.yaml.snakeyaml.emitter;

import java.io.IOException;
import org.yaml.snakeyaml.events.Event;

public interface Emitable {
  void emit(Event paramEvent) throws IOException;
}


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\yaml\snakeyaml\emitter\Emitable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */