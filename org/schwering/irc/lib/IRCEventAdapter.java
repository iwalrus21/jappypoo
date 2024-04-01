package org.schwering.irc.lib;

import org.schwering.irc.lib.util.IRCModeParser;

public class IRCEventAdapter implements IRCEventListener {
  public void onRegistered() {}
  
  public void onDisconnected() {}
  
  public void onError(String msg) {}
  
  public void onError(int num, String msg) {}
  
  public void onInvite(String chan, IRCUser user, String passiveNick) {}
  
  public void onJoin(String chan, IRCUser user) {}
  
  public void onKick(String chan, IRCUser user, String passiveNick, String msg) {}
  
  public void onMode(String chan, IRCUser user, IRCModeParser modeParser) {}
  
  public void onMode(IRCUser user, String passiveNick, String mode) {}
  
  public void onNick(IRCUser user, String newNick) {}
  
  public void onNotice(String target, IRCUser user, String msg) {}
  
  public void onPart(String chan, IRCUser user, String msg) {}
  
  public void onPing(String ping) {}
  
  public void onPrivmsg(String target, IRCUser user, String msg) {}
  
  public void onQuit(IRCUser user, String msg) {}
  
  public void onReply(int num, String value, String msg) {}
  
  public void onTopic(String chan, IRCUser user, String topic) {}
  
  public void unknown(String prefix, String command, String middle, String trailing) {}
}


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\schwering\irc\lib\IRCEventAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */