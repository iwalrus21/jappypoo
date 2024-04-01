/*     */ package me.onefightonewin.feature.types;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.UUID;
/*     */ import me.onefightonewin.chat.ChatUtils;
/*     */ import me.onefightonewin.feature.Category;
/*     */ import me.onefightonewin.feature.Feature;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.network.NetworkPlayerInfo;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ 
/*     */ public class Killsult
/*     */   extends Feature {
/*  17 */   int index = -1;
/*     */   
/*  19 */   String[] insults = new String[] { "<PLAYER> Do you have chemo ?", "<PLAYER> Good Fight!", "<PLAYER> close fight!", "<PLAYER> is this tutorial mode?!", "<PLAYER> is this easy mode?!", "<PLAYER> bodied", "<PLAYER> MDMDMDMD", "<PLAYER> the dead cant talk", "<PLAYER> i oj simpsoned your mom", "<PLAYER> are you even playing the game?", "<PLAYER> oh my god stop embarassing yourself like that...", "Good fight <PLAYER>, you almost got me!", "<PLAYER> you think i'm cheating maybe go submit a clip to tenebrous and he'll tell you i'm legit", "<PLAYER> I can teach you how to w tap if you want?", "Whenever you run into a cheater, just remember they're not cheating because gwen would ban them.", "DUDE GET OF BADLION CLIENT ITS A RAT TENEBROUS SAID GET OFF GET OFF RN", "<PLAYER> probably uses sigma.  hey dude, i think you forget to turn your cheats on <PLAYER>", "Damn bro i just got the vicroy LLLLLLL", "OMG no way.. you died... <PLAYER> i care so much about what you think omg please keep speaking", "I'm not cheating its just a harry potter spell", "<PLAYER> are you having trouble", "You're blind deaf and helen keller", "You aim like the lovechild of Helen Keller and Stevie Wonder", "Sit, fat and the furious", "You look like you'd do anything for a little bit of crack", "<PLAYER> i hope you get nailed to the cross", "<PLAYER> your syndrome is down but your hopes are up.", "Make me a sandwich you stuttering gorilla", "<PLAYER> Just pulled a Kurt Cobain", "SYSTEM 32 IS A RAT PLEASE UNINSTALL RN TENEBROUS SAID SO", "BLACK CRIME IS AT AN ALL TIME HIGH", "GO RACE RIOTS!", "If god is omniscient he knows what thicc meaty sausage feels like in his tight cheeks", "<PLAYER> got touched by daddy Satans long hard heated rod", "uwu owo why are you so bad?", "For each death I donate $5 to tism speaks to help your cause", "You stutter more than my special ed nephew", "The grease from your sausage fingers is dirtying your keyboard, quit.", "You breathe out mountain dew vapor you troglodyte", "I've seen rocks that move more than your fat ass", "Wow you can't even run in game properly. How do you exercise irl?", "You dirty arab go fly into more towers", "Your last name is probably something like wong", "Go back to league of legends you vapid swine", "Call me George Floyd cause I can't breathe" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Killsult(Minecraft minecraft) {
/*  68 */     super(minecraft, "killsult", -1, Category.PAGE_7);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onAppendMessage(IChatComponent packet) {
/*  73 */     String text = packet.getUnformattedText();
/*  74 */     Object[] one = findPlayerStartIndex(text, 0);
/*     */     
/*  76 */     if (one.length != 3 || one[2] == null) {
/*  77 */       return true;
/*     */     }
/*     */     
/*  80 */     int nameOneStart = ((Integer)one[0]).intValue();
/*  81 */     int nameOneLength = ((Integer)one[1]).intValue();
/*     */     
/*  83 */     Object[] two = findPlayerStartIndex(text, nameOneStart + nameOneLength);
/*     */     
/*  85 */     if (two.length != 3 || two[2] == null) {
/*  86 */       return true;
/*     */     }
/*     */     
/*  89 */     EntityPlayer playerOne = (EntityPlayer)one[2];
/*  90 */     EntityPlayer playerTwo = (EntityPlayer)two[2];
/*     */     
/*  92 */     EntityPlayer killer = null;
/*  93 */     EntityPlayer target = null;
/*     */     
/*  95 */     String msg = text.substring(nameOneStart + nameOneLength, ((Integer)two[0]).intValue());
/*     */     
/*  97 */     if (msg.contains(":")) {
/*  98 */       return true;
/*     */     }
/*     */     
/* 101 */     if (msg.contains(" killed by ") || msg.contains(" eliminated by ")) {
/* 102 */       killer = playerTwo;
/* 103 */       target = playerOne;
/* 104 */     } else if (msg.contains(" killed ") || msg.contains(" eliminated ")) {
/* 105 */       killer = playerOne;
/* 106 */       target = playerTwo;
/*     */     } else {
/* 108 */       return true;
/*     */     } 
/*     */     
/* 111 */     if (killer != null && target != null && 
/* 112 */       killer.getUniqueID().equals(this.mc.player.getUniqueID())) {
/* 113 */       if (this.index == -1) {
/* 114 */         this.index = (new Random()).nextInt(this.insults.length);
/*     */       } else {
/* 116 */         this.index++;
/*     */       } 
/*     */       
/* 119 */       if (this.index >= this.insults.length) {
/* 120 */         this.index = 0;
/*     */       }
/*     */       
/* 123 */       ChatUtils.sendMessage(this.insults[this.index].replace("<PLAYER>", target.getName()));
/*     */     } 
/*     */     
/* 126 */     return true;
/*     */   }
/*     */   
/*     */   private Object[] findPlayerStartIndex(String text, int startIndex) {
/* 130 */     Minecraft mc = Minecraft.getMinecraft();
/*     */     
/* 132 */     List<UUID> entities = new ArrayList<>();
/*     */     
/* 134 */     if (mc.getConnection() != null) {
/* 135 */       for (NetworkPlayerInfo player : mc.getConnection().getPlayerInfoMap()) {
/* 136 */         entities.add(player.getGameProfile().getId());
/*     */       }
/*     */     }
/*     */     
/* 140 */     int nameLength = -1;
/* 141 */     int bestPlayerIndex = text.length();
/* 142 */     EntityPlayer bestPlayer = null;
/*     */     
/* 144 */     for (UUID uuid : entities) {
/* 145 */       EntityPlayer player = mc.world.getPlayerEntityByUUID(uuid);
/*     */       
/* 147 */       if (player == null) {
/*     */         continue;
/*     */       }
/* 150 */       String username = player.getDisplayName().getUnformattedText();
/* 151 */       String name = player.getName();
/*     */       
/* 153 */       int index = text.indexOf(username, startIndex);
/* 154 */       nameLength = username.length();
/*     */       
/* 156 */       if (index == -1) {
/* 157 */         index = text.indexOf(name, startIndex);
/* 158 */         nameLength = name.length();
/*     */       } 
/*     */       
/* 161 */       if (index != -1 && bestPlayerIndex > index) {
/* 162 */         bestPlayerIndex = index;
/* 163 */         bestPlayer = player;
/*     */       } 
/*     */     } 
/*     */     
/* 167 */     return new Object[] { Integer.valueOf(bestPlayerIndex), Integer.valueOf(nameLength), bestPlayer };
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\Killsult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */