package org.schwering.irc.lib;

import java.util.EventListener;
import org.schwering.irc.lib.util.IRCConstants;
import org.schwering.irc.lib.util.IRCModeParser;

public interface IRCEventListener extends EventListener, IRCConstants {
  void onRegistered();
  
  void onDisconnected();
  
  void onError(String paramString);
  
  void onError(int paramInt, String paramString);
  
  void onInvite(String paramString1, IRCUser paramIRCUser, String paramString2);
  
  void onJoin(String paramString, IRCUser paramIRCUser);
  
  void onKick(String paramString1, IRCUser paramIRCUser, String paramString2, String paramString3);
  
  void onMode(String paramString, IRCUser paramIRCUser, IRCModeParser paramIRCModeParser);
  
  void onMode(IRCUser paramIRCUser, String paramString1, String paramString2);
  
  void onNick(IRCUser paramIRCUser, String paramString);
  
  void onNotice(String paramString1, IRCUser paramIRCUser, String paramString2);
  
  void onPart(String paramString1, IRCUser paramIRCUser, String paramString2);
  
  void onPing(String paramString);
  
  void onPrivmsg(String paramString1, IRCUser paramIRCUser, String paramString2);
  
  void onQuit(IRCUser paramIRCUser, String paramString);
  
  void onReply(int paramInt, String paramString1, String paramString2);
  
  void onTopic(String paramString1, IRCUser paramIRCUser, String paramString2);
  
  void unknown(String paramString1, String paramString2, String paramString3, String paramString4);
}


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\schwering\irc\lib\IRCEventListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */