/*     */ package me.onefightonewin;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import me.onefightonewin.chat.commands.CommandManager;
/*     */ import me.onefightonewin.feature.Feature;
/*     */ import me.onefightonewin.feature.types.Aimassist;
/*     */ import me.onefightonewin.feature.types.AirControl;
/*     */ import me.onefightonewin.feature.types.AntiDebuff;
/*     */ import me.onefightonewin.feature.types.AntiDisengage;
/*     */ import me.onefightonewin.feature.types.AntiEvade;
/*     */ import me.onefightonewin.feature.types.AntiFire;
/*     */ import me.onefightonewin.feature.types.Antiafk;
/*     */ import me.onefightonewin.feature.types.Antibot;
/*     */ import me.onefightonewin.feature.types.ArmorStatus;
/*     */ import me.onefightonewin.feature.types.ArrayListFeature;
/*     */ import me.onefightonewin.feature.types.AutoArmor;
/*     */ import me.onefightonewin.feature.types.AutoRecall;
/*     */ import me.onefightonewin.feature.types.AutoSmokeBomb;
/*     */ import me.onefightonewin.feature.types.AutoSoup;
/*     */ import me.onefightonewin.feature.types.AutoTool;
/*     */ import me.onefightonewin.feature.types.AutoWaterBottle;
/*     */ import me.onefightonewin.feature.types.Autoclicker;
/*     */ import me.onefightonewin.feature.types.Bhop;
/*     */ import me.onefightonewin.feature.types.BlockOverlay;
/*     */ import me.onefightonewin.feature.types.Brightness;
/*     */ import me.onefightonewin.feature.types.Chams;
/*     */ import me.onefightonewin.feature.types.Cheststealer;
/*     */ import me.onefightonewin.feature.types.Coordinates;
/*     */ import me.onefightonewin.feature.types.Crosshair;
/*     */ import me.onefightonewin.feature.types.CustomTab;
/*     */ import me.onefightonewin.feature.types.ESP;
/*     */ import me.onefightonewin.feature.types.FPS;
/*     */ import me.onefightonewin.feature.types.FakeLag;
/*     */ import me.onefightonewin.feature.types.FastLadders;
/*     */ import me.onefightonewin.feature.types.FastMine;
/*     */ import me.onefightonewin.feature.types.Fastplace;
/*     */ import me.onefightonewin.feature.types.FlashCounter;
/*     */ import me.onefightonewin.feature.types.Fly;
/*     */ import me.onefightonewin.feature.types.FovStatic;
/*     */ import me.onefightonewin.feature.types.Freecam;
/*     */ import me.onefightonewin.feature.types.HitColor;
/*     */ import me.onefightonewin.feature.types.Hitbox;
/*     */ import me.onefightonewin.feature.types.InventoryCleaner;
/*     */ import me.onefightonewin.feature.types.InventoryMove;
/*     */ import me.onefightonewin.feature.types.Jesus;
/*     */ import me.onefightonewin.feature.types.Keystrokes;
/*     */ import me.onefightonewin.feature.types.Killaura;
/*     */ import me.onefightonewin.feature.types.Killsult;
/*     */ import me.onefightonewin.feature.types.LockHud;
/*     */ import me.onefightonewin.feature.types.MiddeClickFriends;
/*     */ import me.onefightonewin.feature.types.MouseDelayFix;
/*     */ import me.onefightonewin.feature.types.Nametags;
/*     */ import me.onefightonewin.feature.types.NoHurtCam;
/*     */ import me.onefightonewin.feature.types.NoLeftClickDelay;
/*     */ import me.onefightonewin.feature.types.NoSlowDown;
/*     */ import me.onefightonewin.feature.types.Nofall;
/*     */ import me.onefightonewin.feature.types.PerspectiveMod;
/*     */ import me.onefightonewin.feature.types.QuickShop;
/*     */ import me.onefightonewin.feature.types.RapidShift;
/*     */ import me.onefightonewin.feature.types.Reach;
/*     */ import me.onefightonewin.feature.types.RestockTimer;
/*     */ import me.onefightonewin.feature.types.Safewalk;
/*     */ import me.onefightonewin.feature.types.StatusEffect;
/*     */ import me.onefightonewin.feature.types.TextMode;
/*     */ import me.onefightonewin.feature.types.Togglesprint;
/*     */ import me.onefightonewin.feature.types.Triggerbot;
/*     */ import me.onefightonewin.feature.types.Velocity;
/*     */ import me.onefightonewin.feature.types.XRay;
/*     */ import me.onefightonewin.feature.types.Zoom;
/*     */ import me.onefightonewin.gui.CustomMainMenu;
/*     */ import me.onefightonewin.gui.GuiMenu;
/*     */ import me.onefightonewin.gui.altmanager.GuiAltManager;
/*     */ import me.onefightonewin.gui.framework.Menu;
/*     */ import me.onefightonewin.inventory.InventoryManager;
/*     */ import me.onefightonewin.irc.IrcClient;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.resources.IReloadableResourceManager;
/*     */ import net.minecraft.client.resources.IResourceManagerReloadListener;
/*     */ import net.minecraft.client.settings.KeyBinding;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraftforge.client.event.GuiOpenEvent;
/*     */ import net.minecraftforge.client.event.GuiScreenEvent;
/*     */ import net.minecraftforge.common.MinecraftForge;
/*     */ import net.minecraftforge.fml.client.registry.ClientRegistry;
/*     */ import net.minecraftforge.fml.common.Mod;
/*     */ import net.minecraftforge.fml.common.Mod.EventHandler;
/*     */ import net.minecraftforge.fml.common.event.FMLInitializationEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import org.spongepowered.asm.launch.MixinBootstrap;
/*     */ import org.spongepowered.asm.mixin.Mixins;
/*     */ 
/*     */ 
/*     */ @Mod(modid = "antiafk")
/*     */ public class AntiAFK
/*     */ {
/* 106 */   private static final ResourceLocation font = new ResourceLocation("antiafk", "textures/font/custom.png");
/*     */   
/*     */   public static FontRenderer FONT_RENDERER;
/* 109 */   private final String keyBindCategory = "key.antiafk.category";
/*     */   
/*     */   public String name;
/*     */   
/*     */   public boolean legit;
/*     */   
/*     */   private static AntiAFK instance;
/*     */   
/*     */   private Minecraft mc;
/*     */   
/*     */   public boolean[] presses;
/*     */   
/*     */   public List<Feature> features;
/*     */   public InventoryManager inventoryManager;
/*     */   public CommandManager commandManager;
/*     */   public IrcClient ircBot;
/*     */   public List<Integer> xray;
/*     */   public List<String> friends;
/*     */   public List<Entity> bots;
/*     */   private KeyBinding toggleMenu;
/*     */   public GuiMenu menu;
/* 130 */   public ResourceLocation[] backgrounds = new ResourceLocation[17];
/*     */   
/* 132 */   public int rainbowSpeedNerf = 0;
/* 133 */   public int rainbowSpeedNerfProg = 0;
/*     */   
/* 135 */   public Color rainbowColor = new Color(0, 0, 0, 0);
/* 136 */   public int rainbowR = 255;
/* 137 */   public int rainbowG = 0;
/* 138 */   public int rainbowB = 0;
/*     */   
/*     */   @EventHandler
/*     */   public void init(FMLInitializationEvent event) {
/* 142 */     instance = this;
/* 143 */     this.name = "CheatBreaker";
/*     */     
/* 145 */     for (int i = 0; i < this.backgrounds.length; i++) {
/* 146 */       this.backgrounds[i] = new ResourceLocation("antiafk", "textures/gui/background-" + (i + 1) + ".png");
/*     */     }
/*     */     
/* 149 */     this.inventoryManager = new InventoryManager();
/*     */     
/* 151 */     this.ircBot = new IrcClient();
/* 152 */     this.ircBot.init();
/*     */     
/* 154 */     this.commandManager = new CommandManager();
/*     */     
/* 156 */     this.toggleMenu = new KeyBinding("key.antiafk.toggle", 54, "key.antiafk.category");
/* 157 */     ClientRegistry.registerKeyBinding(this.toggleMenu);
/*     */     
/* 159 */     this.menu = new GuiMenu(new Menu("", 500, 520), this.toggleMenu);
/*     */     
/* 161 */     this.bots = new ArrayList<>();
/* 162 */     this.friends = new ArrayList<>();
/* 163 */     this.features = new ArrayList<>();
/* 164 */     this.xray = new ArrayList<>();
/* 165 */     this.mc = Minecraft.getMinecraft();
/* 166 */     this.features.add(new AirControl(this.mc));
/* 167 */     this.features.add(new Bhop(this.mc));
/* 168 */     this.features.add(new Aimassist(this.mc));
/* 169 */     this.features.add(new Antiafk(this.mc));
/* 170 */     this.features.add(new Antibot(this.mc));
/* 171 */     this.features.add(new AntiDebuff(this.mc));
/* 172 */     this.features.add(new AntiDisengage(this.mc));
/* 173 */     this.features.add(new AntiEvade(this.mc));
/* 174 */     this.features.add(new AntiFire(this.mc));
/* 175 */     this.features.add(new ArmorStatus(this.mc));
/*     */     
/* 177 */     this.features.add(new Autoclicker(this.mc));
/* 178 */     this.features.add(new AutoRecall(this.mc));
/* 179 */     this.features.add(new AutoSmokeBomb(this.mc));
/*     */     
/* 181 */     this.features.add(new AutoSoup(this.mc));
/* 182 */     this.features.add(new AutoTool(this.mc));
/* 183 */     this.features.add(new Fastplace(this.mc));
/* 184 */     this.features.add(new FastLadders(this.mc));
/* 185 */     this.features.add(new FastMine(this.mc));
/* 186 */     this.features.add(new FlashCounter(this.mc));
/* 187 */     this.features.add(new Fly(this.mc));
/* 188 */     this.features.add(new FPS(this.mc));
/* 189 */     this.features.add(new Freecam(this.mc));
/* 190 */     this.features.add(new FovStatic(this.mc));
/* 191 */     this.features.add(new Hitbox(this.mc));
/* 192 */     this.features.add(new HitColor(this.mc));
/* 193 */     this.features.add(new InventoryCleaner(this.mc));
/* 194 */     this.features.add(new InventoryMove(this.mc));
/* 195 */     this.features.add(new Jesus(this.mc));
/* 196 */     this.features.add(new Keystrokes(this.mc));
/* 197 */     this.features.add(new Killaura(this.mc));
/* 198 */     this.features.add(new Killsult(this.mc));
/* 199 */     this.features.add(new LockHud(this.mc));
/* 200 */     this.features.add(new RapidShift(this.mc));
/* 201 */     this.features.add(new Reach(this.mc));
/* 202 */     this.features.add(new RestockTimer(this.mc));
/*     */     
/* 204 */     this.features.add(new Safewalk(this.mc));
/* 205 */     this.features.add(new XRay(this.mc));
/* 206 */     this.features.add(new StatusEffect(this.mc));
/* 207 */     this.features.add(new TextMode(this.mc));
/* 208 */     this.features.add(new Togglesprint(this.mc));
/* 209 */     this.features.add(new MiddeClickFriends(this.mc));
/* 210 */     this.features.add(new MouseDelayFix(this.mc));
/* 211 */     this.features.add(new ArrayListFeature(this.mc));
/* 212 */     this.features.add(new AutoArmor(this.mc));
/* 213 */     this.features.add(new Triggerbot(this.mc));
/* 214 */     this.features.add(new ESP(this.mc));
/* 215 */     this.features.add(new FakeLag(this.mc));
/* 216 */     this.features.add(new Velocity(this.mc));
/* 217 */     this.features.add(new Nametags(this.mc));
/* 218 */     this.features.add(new Nofall(this.mc));
/* 219 */     this.features.add(new NoHurtCam(this.mc));
/* 220 */     this.features.add(new NoLeftClickDelay(this.mc));
/* 221 */     this.features.add(new NoSlowDown(this.mc));
/* 222 */     this.features.add(new PerspectiveMod(this.mc));
/* 223 */     this.features.add(new QuickShop(this.mc));
/* 224 */     this.features.add(new Coordinates(this.mc));
/* 225 */     this.features.add(new Crosshair(this.mc));
/* 226 */     this.features.add(new CustomTab(this.mc));
/* 227 */     this.features.add(new BlockOverlay(this.mc));
/* 228 */     this.features.add(new AutoWaterBottle(this.mc));
/* 229 */     this.features.add(new Chams(this.mc));
/* 230 */     this.features.add(new Brightness(this.mc));
/* 231 */     this.features.add(new Cheststealer(this.mc));
/* 232 */     this.features.add(new Zoom(this.mc));
/*     */     
/* 234 */     for (Feature feature : this.features) {
/* 235 */       feature.onMenuInit();
/*     */     }
/*     */     
/* 238 */     this.presses = new boolean[this.features.size()];
/*     */     
/* 240 */     MixinBootstrap.init();
/* 241 */     Mixins.addConfiguration("mixins.antiafk.json");
/*     */     
/* 243 */     File cfg = new File(System.getenv("LOCALAPPDATA") + File.separator + this.name + File.separator + "destructed");
/*     */     
/* 245 */     if (cfg.exists()) {
/* 246 */       cfg.delete();
/* 247 */       selfDestruct(false);
/*     */     } 
/*     */     
/* 250 */     MinecraftForge.EVENT_BUS.register(this);
/*     */   }
/*     */   
/*     */   public static AntiAFK getInstance() {
/* 254 */     return instance;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onMainMenuInit(GuiScreenEvent.InitGuiEvent event) {
/* 259 */     if (!(event.gui instanceof net.minecraft.client.gui.GuiMainMenu)) {
/*     */       return;
/*     */     }
/*     */     
/* 263 */     event.buttonList.add(new GuiButton(-100, event.gui.width - 100 - 5, 5, 100, 20, this.mc.getSession().getProfile().getName()));
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onMainMenuInit(GuiOpenEvent event) {
/* 268 */     if (event.gui instanceof net.minecraft.client.gui.GuiMainMenu) {
/* 269 */       event.setCanceled(true);
/*     */       
/* 271 */       this.mc.displayGuiScreen((GuiScreen)new CustomMainMenu());
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onMainMenuClick(GuiScreenEvent.ActionPerformedEvent event) {
/* 277 */     if (!(event.gui instanceof net.minecraft.client.gui.GuiMainMenu) || event.button.id != -100) {
/*     */       return;
/*     */     }
/*     */     
/* 281 */     this.mc.displayGuiScreen((GuiScreen)new GuiAltManager());
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/* 286 */     if (FONT_RENDERER == null) {
/* 287 */       FontRenderer fontRenderer = new FontRenderer(this.mc.gameSettings, font, this.mc.renderEngine, false);
/* 288 */       ((IReloadableResourceManager)this.mc.getResourceManager()).registerReloadListener((IResourceManagerReloadListener)fontRenderer);
/* 289 */       FONT_RENDERER = fontRenderer;
/*     */     } 
/*     */     
/* 292 */     if (this.toggleMenu.isPressed()) {
/* 293 */       this.mc.displayGuiScreen((GuiScreen)this.menu);
/*     */     }
/*     */     
/* 296 */     if (this.rainbowSpeedNerfProg >= this.rainbowSpeedNerf) {
/* 297 */       if (this.rainbowR == 255 && this.rainbowG != 255 && this.rainbowB == 0) {
/* 298 */         this.rainbowG++;
/* 299 */       } else if (this.rainbowR != 0 && this.rainbowG == 255 && this.rainbowB == 0) {
/* 300 */         this.rainbowR--;
/* 301 */       } else if (this.rainbowR == 0 && this.rainbowG == 255 && this.rainbowB != 255) {
/* 302 */         this.rainbowB++;
/* 303 */       } else if (this.rainbowR == 0 && this.rainbowG != 0 && this.rainbowB == 255) {
/* 304 */         this.rainbowG--;
/* 305 */       } else if (this.rainbowR != 255 && this.rainbowG == 0 && this.rainbowB == 255) {
/* 306 */         this.rainbowR++;
/* 307 */       } else if (this.rainbowR == 255 && this.rainbowG == 0 && this.rainbowB != 0) {
/* 308 */         this.rainbowB--;
/*     */       } 
/* 310 */       this.rainbowColor = new Color(this.rainbowR, this.rainbowG, this.rainbowB);
/* 311 */       this.rainbowSpeedNerfProg = 0;
/*     */     } else {
/* 313 */       this.rainbowSpeedNerfProg++;
/*     */     } 
/*     */     
/* 316 */     if (this.mc.currentScreen == null) {
/* 317 */       for (int i = 0; i < this.features.size(); i++) {
/* 318 */         Feature feature = this.features.get(i);
/*     */         
/* 320 */         if (feature.getKey() != -1) {
/*     */ 
/*     */ 
/*     */           
/* 324 */           boolean valid = true;
/*     */           
/* 326 */           if (feature instanceof PerspectiveMod) {
/* 327 */             PerspectiveMod mod = (PerspectiveMod)feature;
/*     */             
/* 329 */             if (mod.holdType.getRValue()) {
/* 330 */               mod.setEnabled(Keyboard.isKeyDown(feature.getKey()));
/* 331 */               valid = false;
/*     */             } 
/* 333 */           } else if (feature instanceof Zoom) {
/* 334 */             Zoom mod = (Zoom)feature;
/*     */             
/* 336 */             if (mod.holdType.getRValue()) {
/* 337 */               mod.setEnabled(Keyboard.isKeyDown(feature.getKey()));
/* 338 */               valid = false;
/*     */             } 
/* 340 */           } else if (feature instanceof QuickShop) {
/* 341 */             QuickShop mod = (QuickShop)feature;
/*     */             
/* 343 */             mod.setEnabled(Keyboard.isKeyDown(feature.getKey()));
/* 344 */             valid = false;
/*     */           } 
/*     */           
/* 347 */           if (valid) {
/* 348 */             if (Keyboard.isKeyDown(feature.getKey())) {
/* 349 */               if (this.presses[i]) {
/* 350 */                 this.presses[i] = false;
/* 351 */                 feature.setEnabled(!feature.isEnabled());
/*     */                 
/* 353 */                 if (!feature.isEnabled()) {
/* 354 */                   feature.onDisable();
/*     */                 }
/*     */               } 
/*     */             } else {
/* 358 */               this.presses[i] = true;
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/* 364 */     if (this.mc.world != null && this.mc.player != null) {
/* 365 */       this.inventoryManager.runHandler();
/*     */     }
/*     */   }
/*     */   
/*     */   public AntiAFK getMod() {
/* 370 */     return instance;
/*     */   }
/*     */   
/*     */   public void selfDestruct(boolean first) {
/* 374 */     this.legit = true;
/*     */     
/* 376 */     this.menu.setMenu(new Menu("", 200, 200));
/* 377 */     this.menu.initd = false;
/* 378 */     this.menu.savedWidth = -1;
/* 379 */     this.menu.savedHeight = -1;
/* 380 */     this.menu.initGui();
/* 381 */     this.menu.initGui();
/*     */     
/* 383 */     if (first) {
/* 384 */       File cfg = new File(System.getenv("LOCALAPPDATA") + File.separator + (getInstance()).name + File.separator + "destructed");
/*     */       
/*     */       try {
/* 387 */         cfg.createNewFile();
/* 388 */       } catch (IOException e) {
/* 389 */         e.printStackTrace();
/*     */       } 
/*     */       
/* 392 */       for (int i = 0; i < this.features.size(); i++) {
/* 393 */         Feature feature = this.features.get(i);
/* 394 */         feature.setKey(0);
/* 395 */         feature.setEnabled(false);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends Feature> T getFeature(Class<T> clazz) {
/* 402 */     for (Feature feature : this.features) {
/* 403 */       if (clazz == feature.getClass()) {
/* 404 */         return (T)feature;
/*     */       }
/*     */     } 
/*     */     
/* 408 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\AntiAFK.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */