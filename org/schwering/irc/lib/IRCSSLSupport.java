package org.schwering.irc.lib;

import java.security.SecureRandom;
import javax.net.ssl.KeyManager;
import javax.net.ssl.TrustManager;

public interface IRCSSLSupport {
  KeyManager[] getKeyManagers();
  
  TrustManager[] getTrustManagers();
  
  SecureRandom getSecureRandom();
}


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\schwering\irc\lib\IRCSSLSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */