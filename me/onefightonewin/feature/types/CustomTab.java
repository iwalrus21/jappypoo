/*     */ package me.onefightonewin.feature.types;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import me.onefightonewin.feature.Category;
/*     */ import me.onefightonewin.feature.Feature;
/*     */ import me.onefightonewin.gui.framework.DrawImpl;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.network.NetworkPlayerInfo;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.scoreboard.ScoreObjective;
/*     */ import net.minecraft.scoreboard.Scoreboard;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ 
/*     */ public class CustomTab
/*     */   extends Feature
/*     */   implements DrawImpl {
/*  30 */   int bg = (new Color(0, 0, 0, 100)).getRGB();
/*  31 */   int text = (new Color(255, 255, 255, 255)).getRGB();
/*  32 */   int textDead = (new Color(200, 200, 200, 255)).getRGB();
/*     */   
/*  34 */   final long maxKeepTime = 1000L;
/*     */   
/*     */   Map<UUID, Long> counted;
/*     */   
/*     */   World world;
/*     */   
/*     */   Map<UUID, Integer> kills;
/*     */   Map<UUID, Integer> deaths;
/*  42 */   private static final ResourceLocation DEAD_ICON = new ResourceLocation("antiafk", "textures/gui/dead.png");
/*  43 */   private static final ResourceLocation ALIVE_ICON = new ResourceLocation("antiafk", "textures/gui/alive.png");
/*     */   
/*     */   public CustomTab(Minecraft minecraft) {
/*  46 */     super(minecraft, "customtab", -1, Category.PAGE_7);
/*  47 */     this.kills = new HashMap<>();
/*  48 */     this.deaths = new HashMap<>();
/*     */     
/*  50 */     this.counted = new HashMap<>();
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/*  55 */     if (this.mc.world == null || this.mc.player == null || this.world != this.mc.world) {
/*  56 */       this.kills = new HashMap<>();
/*  57 */       this.deaths = new HashMap<>();
/*  58 */       this.counted = new HashMap<>();
/*  59 */       this.world = (World)this.mc.world;
/*     */       
/*     */       return;
/*     */     } 
/*  63 */     Iterator<Map.Entry<UUID, Long>> iterator = this.counted.entrySet().iterator();
/*     */     
/*  65 */     while (iterator.hasNext()) {
/*  66 */       Map.Entry<UUID, Long> next = iterator.next();
/*     */       
/*  68 */       if (System.currentTimeMillis() - ((Long)next.getValue()).longValue() > 1000L) {
/*  69 */         iterator.remove();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRenderPlayerlist(Scoreboard scoreboardIn, ScoreObjective scoreObjectiveIn) {
/*  76 */     if (this.mc.getConnection() == null || this.mc.getConnection().getPlayerInfoMap() == null) {
/*     */       return;
/*     */     }
/*     */     
/*  80 */     List<NetworkPlayerInfo> players = new ArrayList<>(this.mc.getConnection().getPlayerInfoMap());
/*  81 */     ScaledResolution res = new ScaledResolution(this.mc);
/*     */     
/*  83 */     Collections.sort(players, new Comparator<NetworkPlayerInfo>()
/*     */         {
/*     */           public int compare(NetworkPlayerInfo p1, NetworkPlayerInfo p2) {
/*  86 */             if (p1 == null || p2 == null || p1.getPlayerTeam() == null || p2.getPlayerTeam() == null) {
/*  87 */               return 0;
/*     */             }
/*     */             
/*  90 */             String one = p1.getPlayerTeam().getPrefix();
/*  91 */             String two = p2.getPlayerTeam().getPrefix();
/*     */             
/*  93 */             return CustomTab.this.getLastColor(one).compareTo(CustomTab.this.getLastColor(two));
/*     */           }
/*     */         });
/*     */     
/*  97 */     int nameOffset = 15;
/*  98 */     int kdOffset = 30;
/*  99 */     int width = 220;
/* 100 */     int size = 0;
/*     */     
/* 102 */     for (NetworkPlayerInfo player : players) {
/* 103 */       if (player.getGameProfile() == null) {
/*     */         continue;
/*     */       }
/* 106 */       int textWidth = getStringWidth(player.getGameProfile().getName());
/*     */       
/* 108 */       if (textWidth > width) {
/* 109 */         width = textWidth + 50;
/*     */       }
/*     */       
/* 112 */       size++;
/*     */     } 
/*     */     
/* 115 */     int height = size * 14;
/*     */     
/* 117 */     int x = res.getScaledWidth() / 2 - width / 2;
/* 118 */     int y = res.getScaledHeight() / 2 - height / 2;
/*     */     
/* 120 */     drawRect(x, y, width, height, this.bg);
/* 121 */     drawRect(x, y - 14, width, 14, this.bg);
/* 122 */     drawText("K/D", x + width - getStringWidth("K/D") - 5 - kdOffset, y - 12, this.text);
/* 123 */     drawText("NAME", x + 5 + nameOffset, y - 12, this.text);
/* 124 */     drawText("PING", x + width - getStringWidth("K/D") - 5, y - 12, this.text);
/*     */     
/* 126 */     for (NetworkPlayerInfo player : players) {
/* 127 */       if (player.getGameProfile() == null) {
/*     */         continue;
/*     */       }
/* 130 */       boolean dead = true;
/*     */       
/* 132 */       String ping = player.getResponseTime() + "";
/* 133 */       EntityPlayer entity = this.mc.world.getPlayerEntityByUUID(player.getGameProfile().getId());
/*     */       
/* 135 */       if (entity != null) {
/* 136 */         dead = entity.isDead;
/*     */       }
/*     */       
/* 139 */       if (!dead) {
/* 140 */         drawRect(x, y, width, 14, this.bg);
/*     */       }
/*     */       
/* 143 */       drawImage(dead ? DEAD_ICON : ALIVE_ICON, x + 5, y + 2, 10, 10);
/* 144 */       drawText(((player.getPlayerTeam() != null) ? player.getPlayerTeam().getPrefix() : "") + player.getGameProfile().getName(), x + nameOffset + 5, y + 2, dead ? this.textDead : this.text);
/*     */ 
/*     */       
/* 147 */       String data = (new StringBuilder()).append(this.deaths.getOrDefault(player.getGameProfile().getId(), Integer.valueOf(0))).append("/").append(this.kills.getOrDefault(player.getGameProfile().getId(), Integer.valueOf(0))).toString();
/*     */       
/* 149 */       drawText(ping, x + width - 5 - getStringWidth(ping), y + 2, dead ? this.textDead : this.text);
/* 150 */       drawText(data, x + width - 5 - getStringWidth(data) - kdOffset, y + 2, dead ? this.textDead : this.text);
/* 151 */       y += 14;
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getLastColor(String input) {
/* 156 */     String lastColor = "";
/* 157 */     boolean nextColor = false;
/*     */     
/* 159 */     for (char character : input.toCharArray()) {
/* 160 */       if (character == '&') {
/* 161 */         nextColor = true;
/*     */ 
/*     */       
/*     */       }
/* 165 */       else if (nextColor) {
/* 166 */         lastColor = "&" + character;
/* 167 */         nextColor = false;
/*     */       } 
/*     */     } 
/*     */     
/* 171 */     return lastColor;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onAppendMessage(IChatComponent packet) {
/* 176 */     String text = packet.getUnformattedText();
/* 177 */     Object[] one = findPlayerStartIndex(text, 0);
/*     */     
/* 179 */     if (one.length != 3 || one[2] == null) {
/* 180 */       return true;
/*     */     }
/*     */     
/* 183 */     int nameOneStart = ((Integer)one[0]).intValue();
/* 184 */     int nameOneLength = ((Integer)one[1]).intValue();
/*     */     
/* 186 */     Object[] two = findPlayerStartIndex(text, nameOneStart + nameOneLength);
/*     */     
/* 188 */     if (two.length != 3 || two[2] == null) {
/* 189 */       return true;
/*     */     }
/*     */     
/* 192 */     EntityPlayer playerOne = (EntityPlayer)one[2];
/* 193 */     EntityPlayer playerTwo = (EntityPlayer)two[2];
/*     */     
/* 195 */     EntityPlayer killer = null;
/* 196 */     EntityPlayer target = null;
/*     */     
/* 198 */     if (isDead(playerOne)) {
/* 199 */       killer = playerTwo;
/* 200 */       target = playerOne;
/* 201 */     } else if (isDead(playerTwo)) {
/* 202 */       killer = playerOne;
/* 203 */       target = playerTwo;
/*     */     } else {
/* 205 */       String msg = text.substring(nameOneStart + nameOneLength, ((Integer)two[0]).intValue());
/*     */       
/* 207 */       if (msg.contains(">") || msg.contains(":")) {
/* 208 */         return true;
/*     */       }
/*     */       
/* 211 */       if (msg.contains(" killed by ") || msg.contains(" eliminated by ")) {
/* 212 */         killer = playerTwo;
/* 213 */         target = playerOne;
/* 214 */       } else if (msg.contains(" killed ") || msg.contains(" eliminated ")) {
/* 215 */         killer = playerOne;
/* 216 */         target = playerTwo;
/*     */       } else {
/* 218 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 222 */     if (killer != null && target != null && 
/* 223 */       !this.counted.containsKey(target.getUniqueID())) {
/* 224 */       this.counted.put(target.getUniqueID(), Long.valueOf(System.currentTimeMillis()));
/* 225 */       increaseKill(killer.getUniqueID());
/* 226 */       increaseDeath(target.getUniqueID());
/*     */     } 
/*     */ 
/*     */     
/* 230 */     return true;
/*     */   }
/*     */   
/*     */   private Object[] findPlayerStartIndex(String text, int startIndex) {
/* 234 */     Minecraft mc = Minecraft.getMinecraft();
/*     */     
/* 236 */     List<UUID> entities = new ArrayList<>();
/*     */     
/* 238 */     if (mc.getConnection() != null) {
/* 239 */       for (NetworkPlayerInfo player : mc.getConnection().getPlayerInfoMap()) {
/* 240 */         entities.add(player.getGameProfile().getId());
/*     */       }
/*     */     }
/*     */     
/* 244 */     int nameLength = -1;
/* 245 */     int bestPlayerIndex = text.length();
/* 246 */     EntityPlayer bestPlayer = null;
/*     */     
/* 248 */     for (UUID uuid : entities) {
/* 249 */       EntityPlayer player = mc.world.getPlayerEntityByUUID(uuid);
/*     */       
/* 251 */       if (player == null) {
/*     */         continue;
/*     */       }
/* 254 */       String username = player.getDisplayName().getUnformattedText();
/* 255 */       String name = player.getName();
/*     */       
/* 257 */       int index = text.indexOf(username, startIndex);
/* 258 */       nameLength = username.length();
/*     */       
/* 260 */       if (index == -1) {
/* 261 */         index = text.indexOf(name, startIndex);
/* 262 */         nameLength = name.length();
/*     */       } 
/*     */       
/* 265 */       if (index != -1 && bestPlayerIndex > index) {
/* 266 */         bestPlayerIndex = index;
/* 267 */         bestPlayer = player;
/*     */       } 
/*     */     } 
/*     */     
/* 271 */     return new Object[] { Integer.valueOf(bestPlayerIndex), Integer.valueOf(nameLength), bestPlayer };
/*     */   }
/*     */   
/*     */   private boolean isDead(EntityPlayer player) {
/* 275 */     if (player == null) {
/* 276 */       return false;
/*     */     }
/* 278 */     return (player.isDead || player.isSpectator() || player.capabilities.isCreativeMode || !player.capabilities.allowEdit);
/*     */   }
/*     */   
/*     */   public void increaseKill(UUID uuid) {
/* 282 */     this.kills.put(uuid, Integer.valueOf(((Integer)this.kills.getOrDefault(uuid, Integer.valueOf(0))).intValue() + 1));
/*     */   }
/*     */   
/*     */   public void increaseDeath(UUID uuid) {
/* 286 */     this.deaths.put(uuid, Integer.valueOf(((Integer)this.deaths.getOrDefault(uuid, Integer.valueOf(0))).intValue() + 1));
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\CustomTab.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */