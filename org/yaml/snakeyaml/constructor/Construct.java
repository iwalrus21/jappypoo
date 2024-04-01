package org.yaml.snakeyaml.constructor;

import org.yaml.snakeyaml.nodes.Node;

public interface Construct {
  Object construct(Node paramNode);
  
  void construct2ndStep(Node paramNode, Object paramObject);
}


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\yaml\snakeyaml\constructor\Construct.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */