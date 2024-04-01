package org.schwering.irc.lib;

import java.io.IOException;
import java.net.InetAddress;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public interface IRCConnection {
  public static final int INVALID_TIMEOUT = -1;
  
  void addIRCEventListener(IRCEventListener paramIRCEventListener);
  
  void close();
  
  void connect() throws IOException, KeyManagementException, NoSuchAlgorithmException;
  
  void doAway();
  
  void doAway(String paramString);
  
  void doInvite(String paramString1, String paramString2);
  
  void doIson(String paramString);
  
  void doJoin(String paramString);
  
  void doJoin(String paramString1, String paramString2);
  
  void doKick(String paramString1, String paramString2);
  
  void doKick(String paramString1, String paramString2, String paramString3);
  
  void doList();
  
  void doList(String paramString);
  
  void doMode(String paramString);
  
  void doMode(String paramString1, String paramString2);
  
  void doNames();
  
  void doNames(String paramString);
  
  void doNick(String paramString);
  
  void doNotice(String paramString1, String paramString2);
  
  void doPart(String paramString);
  
  void doPart(String paramString1, String paramString2);
  
  void doPong(String paramString);
  
  void doPrivmsg(String paramString1, String paramString2);
  
  void doQuit();
  
  void doQuit(String paramString);
  
  void doTopic(String paramString);
  
  void doTopic(String paramString1, String paramString2);
  
  void doUserhost(String paramString);
  
  void doWho(String paramString);
  
  void doWhois(String paramString);
  
  void doWhowas(String paramString);
  
  InetAddress getLocalAddress();
  
  String getNick();
  
  int getPort();
  
  int getTimeout();
  
  boolean isConnected();
  
  boolean isSSL();
  
  boolean removeIRCEventListener(IRCEventListener paramIRCEventListener);
  
  void send(String paramString);
}


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\schwering\irc\lib\IRCConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */