package org.schwering.irc.lib;

import java.net.Proxy;

public interface IRCRuntimeConfig {
  IRCExceptionHandler getExceptionHandler();
  
  Proxy getProxy();
  
  IRCSSLSupport getSSLSupport();
  
  int getTimeout();
  
  IRCTrafficLogger getTrafficLogger();
  
  boolean isAutoPong();
  
  boolean isStripColorsEnabled();
}


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\schwering\irc\lib\IRCRuntimeConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */