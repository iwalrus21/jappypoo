package org.yaml.snakeyaml.parser;

import org.yaml.snakeyaml.events.Event;

public interface Parser {
  boolean checkEvent(Event.ID paramID);
  
  Event peekEvent();
  
  Event getEvent();
}


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\yaml\snakeyaml\parser\Parser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */