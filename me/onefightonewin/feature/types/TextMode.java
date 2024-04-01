/*     */ package me.onefightonewin.feature.types;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import me.onefightonewin.feature.Category;
/*     */ import me.onefightonewin.feature.Feature;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ 
/*     */ public class TextMode
/*     */   extends Feature
/*     */ {
/*     */   Map<String, String> textsOrginal;
/*     */   Map<String, String> textsSecondary;
/*     */   int textMode;
/*     */   
/*     */   public TextMode(Minecraft minecraft) {
/*  18 */     super(minecraft, "textmode", -1, Category.PAGE_MISC);
/*  19 */     this.textsOrginal = new HashMap<>();
/*  20 */     this.textsSecondary = new HashMap<>();
/*  21 */     initWords();
/*     */   }
/*     */   
/*     */   public void initWords() {
/*  25 */     addString("lockhud", "Lock HUD", "Lock HUD");
/*  26 */     addString("rapidshift", "Rapid shift", "Rapid shift");
/*  27 */     addString("antidisengage", "AntiDisengage", "ICE");
/*  28 */     addString("antievade", "AntiEvade", "HighIQ evade");
/*  29 */     addString("hitbox", "Hitbox", "Hitbox");
/*  30 */     addString("restocktimer", "Restock Timer", "Restock Timer");
/*  31 */     addString("nohurtcam", "NoHurtCam", "No Seizure");
/*  32 */     addString("antifire", "AntiFire", "Extingushier");
/*  33 */     addString("fakelag", "FakeLag", "Retard Mode");
/*  34 */     addString("zoom", "Zoom", "Zoom");
/*  35 */     addString("crosshair", "Crosshair", "Crosshair");
/*  36 */     addString("freecam", "Freecam", "GucciGoggles");
/*  37 */     addString("rodaimbot", "Rod aimbot", "360NOSCOPEROD");
/*  38 */     addString("inventorycleaner", "Inventory cleaner", "cleanthishoe");
/*  39 */     addString("quickshop", "QuickKit", "autoregen");
/*  40 */     addString("blockoverlay", "Block overlay", "Block overlay");
/*  41 */     addString("flashcounter", "Flash counter", "Flash counter");
/*  42 */     addString("fovstatic", "Fov static", "Fov static");
/*  43 */     addString("noleftclickdelay", "NoDelay", "NoDelay");
/*  44 */     addString("autowaterbottle", "AWB", "splish splash");
/*  45 */     addString("autosmokebomb", "Auto smoke bomb", "invisiblecloak");
/*  46 */     addString("autorecall", "Auto recall", "RUN NIGGA RUN");
/*  47 */     addString("hitcolor", "Hit color", "Hit color");
/*  48 */     addString("killsult", "Killsult", "DEATH JOKES");
/*  49 */     addString("mousedelayfix", "Mouse Delay Fix", "Mouse Delay Fix");
/*  50 */     addString("fastmine", "Fastmine", "Jackhammer");
/*  51 */     addString("killaura", "Killaura", "SWATTHREATS");
/*  52 */     addString("autosoup", "AutoSoup", "Fatnigga");
/*  53 */     addString("fastladders", "FastLadders", "Floater");
/*  54 */     addString("nofall", "Nofall", "Nofall");
/*  55 */     addString("coordinates", "Coordinates", "Coordinates");
/*  56 */     addString("fps", "FPS", "FPS");
/*  57 */     addString("statuseffect", "Status Effect", "Status Effect");
/*  58 */     addString("armorstatus", "Armor Status", "Armor Status");
/*  59 */     addString("autotool", "Auto Tool", "Auto Tool");
/*  60 */     addString("perspective", "Perspective Mod", "Perspective Mod");
/*  61 */     addString("antidebuff", "AntiDebuff", "AntiDebuff");
/*  62 */     addString("autoblock", "Autoblock", "Autoblock");
/*  63 */     addString("invmove", "Inven+", "UHAUL");
/*  64 */     addString("fly", "Fly", "Fly");
/*  65 */     addString("togglesprint", "ToggleSprint", "ToggleSprint");
/*  66 */     addString("jesus", "Jesus", "THISNIGGAAQUAMAN");
/*  67 */     addString("cheststealer", "Cheststealer", "BOONKGANG");
/*  68 */     addString("keystrokes", "CPS", "CPS");
/*  69 */     addString("noslowdown", "NoSlowDown", "GOTTAGOFAST");
/*  70 */     addString("autoarmor", "Autoarmor", "Merch");
/*  71 */     addString("chams", "Chams", "IMIGRANTTRACKER");
/*  72 */     addString("brightness", "Fullbright", "NightVision");
/*  73 */     addString("bhop", "Jumptimer", "BorderHoppper");
/*  74 */     addString("autoclicker", "Autoclicker", "CookieClicker");
/*  75 */     addString("reach", "Reach", "LankyArms");
/*  76 */     addString("aimassist", "Aim assist", "ZoomAim");
/*  77 */     addString("antibot", "Antibot", "NoBanLUL");
/*  78 */     addString("fastplace", "Fastplace", "BlockZoom");
/*  79 */     addString("xray", "XRay", "WeMineDiamonds");
/*  80 */     addString("antiafk", "Anti afk", "NoKickLUL");
/*  81 */     addString("safewalk", "Eagle", "SafeCock");
/*  82 */     addString("velocity", "Velocity", "WeightLoss");
/*  83 */     addString("triggerbot", "Triggerbot", "DoxThreats");
/*  84 */     addString("bowaimbot", "Bow aimbot", "360NOSCOPE");
/*  85 */     addString("nametags", "Nametags", "RetardAlert");
/*  86 */     addString("esp", "ESP", "ISEEYOASS");
/*  87 */     addString("aircontrol", "Aircontrol", "Avatar");
/*  88 */     addString("arraylist", "Arraylist", "Arraylist");
/*  89 */     addString("middleclickfriends", "MCF", "MCF");
/*  90 */     addString("customtab", "Custom tab", "Custom tab");
/*  91 */     addString("textmode", "Theme: Meme", "Theme: Meme");
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  96 */     this.textMode = 1;
/*     */     
/*  98 */     if (this.mc.currentScreen instanceof me.onefightonewin.gui.GuiMenu) {
/*  99 */       this.mc.displayGuiScreen((GuiScreen)null);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 105 */     this.textMode = 0;
/*     */     
/* 107 */     if (this.mc.currentScreen instanceof me.onefightonewin.gui.GuiMenu) {
/* 108 */       this.mc.displayGuiScreen((GuiScreen)null);
/*     */     }
/*     */   }
/*     */   
/*     */   public void addString(String id, String normal, String meme) {
/* 113 */     this.textsOrginal.put(id, normal);
/* 114 */     this.textsSecondary.put(id, meme);
/*     */   }
/*     */   
/*     */   public String getString(String id) {
/* 118 */     return (this.textMode == 0) ? this.textsOrginal.getOrDefault(id, id) : this.textsSecondary.getOrDefault(id, id);
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\TextMode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */