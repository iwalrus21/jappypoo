package org.schwering.irc.lib;

public interface IRCServerConfig {
  String getEncoding();
  
  String getHost();
  
  String getNick();
  
  String getPassword();
  
  int getPortAt(int paramInt);
  
  int[] getPorts();
  
  int getPortsCount();
  
  String getRealname();
  
  String getUsername();
}


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\schwering\irc\lib\IRCServerConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */