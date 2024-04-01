/*    */ package me.onefightonewin.chat.commands;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import me.onefightonewin.chat.commands.types.Friend;
/*    */ import me.onefightonewin.chat.commands.types.Friends;
/*    */ import me.onefightonewin.chat.commands.types.HelpCommand;
/*    */ import me.onefightonewin.chat.commands.types.InventoryCleaner;
/*    */ import me.onefightonewin.chat.commands.types.SayCommand;
/*    */ import me.onefightonewin.chat.commands.types.Spam;
/*    */ import me.onefightonewin.chat.commands.types.Unfriend;
/*    */ import me.onefightonewin.chat.commands.types.XRay;
/*    */ 
/*    */ 
/*    */ public class CommandManager
/*    */ {
/*    */   private boolean enabled = true;
/*    */   private List<Command> commands;
/*    */   private List<String> exemptions;
/*    */   
/*    */   public CommandManager() {
/* 22 */     this.commands = new ArrayList<>();
/* 23 */     this.exemptions = new ArrayList<>();
/*    */     
/* 25 */     if (this.enabled) {
/* 26 */       init();
/*    */     }
/*    */   }
/*    */   
/*    */   private void init() {
/* 31 */     this.commands.add(new Friend());
/* 32 */     this.commands.add(new Friends());
/* 33 */     this.commands.add(new HelpCommand());
/* 34 */     this.commands.add(new InventoryCleaner());
/* 35 */     this.commands.add(new SayCommand());
/* 36 */     this.commands.add(new Spam());
/* 37 */     this.commands.add(new Unfriend());
/* 38 */     this.commands.add(new XRay());
/*    */   }
/*    */   
/*    */   public boolean processCommand(String command) {
/* 42 */     if (!this.enabled) {
/* 43 */       return true;
/*    */     }
/*    */     
/* 46 */     if (this.exemptions.contains(command)) {
/* 47 */       this.exemptions.remove(command);
/* 48 */       return true;
/*    */     } 
/*    */     
/* 51 */     String[] commandParts = command.split(" ", 2);
/*    */     
/* 53 */     for (Command cmd : this.commands) {
/* 54 */       boolean isAlone = !commandParts[0].startsWith(".");
/* 55 */       if (cmd.getCommand().equalsIgnoreCase(commandParts[0]) || (isAlone && commandParts[0].startsWith(cmd.getCommand()))) {
/* 56 */         (new String[1])[0] = commandParts[0].substring(1); String[] args = (commandParts.length == 2) ? (isAlone ? command.substring(1).split(" ") : commandParts[1].split(" ")) : (isAlone ? new String[1] : new String[0]);
/* 57 */         cmd.onRun(args);
/* 58 */         return false;
/*    */       } 
/*    */     } 
/*    */     
/* 62 */     return true;
/*    */   }
/*    */   
/*    */   public Command getCommandByName(String name) {
/* 66 */     if (!this.enabled) {
/* 67 */       return null;
/*    */     }
/*    */     
/* 70 */     for (Command command : this.commands) {
/* 71 */       if (command.getCommand().equalsIgnoreCase(name)) {
/* 72 */         return command;
/*    */       }
/*    */     } 
/* 75 */     return null;
/*    */   }
/*    */   
/*    */   public void addExemption(String string) {
/* 79 */     if (!this.enabled) {
/*    */       return;
/*    */     }
/*    */     
/* 83 */     if (!this.exemptions.contains(string)) {
/* 84 */       this.exemptions.add(string);
/*    */     }
/*    */   }
/*    */   
/*    */   public List<Command> getCommands() {
/* 89 */     return this.commands;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\chat\commands\CommandManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */