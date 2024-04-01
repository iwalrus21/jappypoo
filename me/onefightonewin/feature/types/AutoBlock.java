/*     */ package me.onefightonewin.feature.types;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import me.onefightonewin.AntiAFK;
/*     */ import me.onefightonewin.feature.Category;
/*     */ import me.onefightonewin.feature.Feature;
/*     */ import me.onefightonewin.gui.framework.MenuComponent;
/*     */ import me.onefightonewin.gui.framework.components.MenuComboBox;
/*     */ import me.onefightonewin.gui.framework.components.MenuDropdown;
/*     */ import me.onefightonewin.gui.framework.components.MenuLabel;
/*     */ import me.onefightonewin.gui.framework.components.MenuSlider;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.settings.KeyBinding;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraftforge.event.entity.player.AttackEntityEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AutoBlock
/*     */   extends Feature
/*     */ {
/*     */   MenuLabel optionsLabel;
/*     */   MenuDropdown options;
/*     */   MenuLabel hitsBeforeBlockingLabel;
/*     */   MenuSlider hitsBeforeBlocking;
/*     */   MenuLabel hitsTakenBeforeBlockingLabel;
/*     */   MenuSlider hitsTakenBeforeBlocking;
/*     */   MenuLabel secondsToBlockLabel;
/*     */   MenuSlider secondsToBlock;
/*     */   MenuLabel secondsToSneakLabel;
/*     */   MenuSlider secondsToSneak;
/*     */   MenuLabel chanceLabel;
/*     */   MenuSlider chance;
/*     */   MenuLabel blockLabel;
/*     */   MenuSlider block;
/*     */   MenuLabel blockInfoLabel;
/*     */   MenuComboBox blockOptions;
/*     */   int hitsDelay;
/*     */   int attacksDelay;
/*     */   long attackReset;
/*     */   boolean shouldResetBlock;
/*     */   boolean shouldResetSneak;
/*     */   boolean shouldSneak;
/*     */   boolean shouldBlock;
/*     */   long stopBlockTime;
/*     */   long stopSneakTime;
/*     */   long deactivateTime;
/*     */   Random random;
/*     */   
/*     */   public AutoBlock(Minecraft minecraft) {
/*  58 */     super(minecraft, "autoblock", 0, Category.PAGE_2);
/*  59 */     this.random = new Random();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuInit() {
/*  64 */     this.optionsLabel = new MenuLabel("Autoblock sneak options", 0, 0);
/*  65 */     this.options = new MenuDropdown(Options.class, 0, 0);
/*  66 */     this.hitsBeforeBlockingLabel = new MenuLabel("Hits given before block", 0, 0);
/*  67 */     this.hitsBeforeBlocking = new MenuSlider(5, 1, 10, 0, 0, 100, 6);
/*  68 */     this.hitsTakenBeforeBlockingLabel = new MenuLabel("Hits taken before block", 0, 0);
/*  69 */     this.hitsTakenBeforeBlocking = new MenuSlider(5, 1, 10, 0, 0, 100, 6);
/*  70 */     this.secondsToBlockLabel = new MenuLabel("Seconds to block", 0, 0);
/*  71 */     this.secondsToBlock = new MenuSlider(5, 1, 10, 0, 0, 100, 6);
/*  72 */     this.chanceLabel = new MenuLabel("Sneak chance", 0, 0);
/*  73 */     this.chance = new MenuSlider(1, 1, 100, 0, 0, 100, 6);
/*  74 */     this.secondsToSneakLabel = new MenuLabel("Seconds to sneak", 0, 0);
/*  75 */     this.secondsToSneak = new MenuSlider(5, 1, 10, 0, 0, 100, 6);
/*     */     
/*  77 */     this.blockLabel = new MenuLabel("Block chance", 0, 0);
/*  78 */     this.block = new MenuSlider(1, 1, 100, 0, 0, 100, 6);
/*     */     
/*  80 */     this.blockInfoLabel = new MenuLabel("Block options", 0, 0);
/*  81 */     this.blockOptions = new MenuComboBox(BlockOptions.class, 0, 0);
/*     */   }
/*     */   
/*     */   enum Options {
/*  85 */     ALWAYS_ON, OFF, CHANCE;
/*     */   }
/*     */   
/*     */   enum BlockOptions {
/*  89 */     HITS_TAKEN_BEFORE_BLOCK, HITS_GIVEN_BEFORE_BLOCK, CHANCE;
/*     */   }
/*     */   
/*     */   public boolean hasCondition(BlockOptions cond) {
/*  93 */     for (String condition : this.blockOptions.getValues()) {
/*  94 */       if (condition.equalsIgnoreCase(cond.toString()))
/*  95 */         return true; 
/*     */     } 
/*  97 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onAppendMessage(IChatComponent packet) {
/* 102 */     if (packet.getUnformattedText().contains("You failed to evade")) {
/* 103 */       this.deactivateTime = System.currentTimeMillis() + 17000L;
/*     */     }
/* 105 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuAdd(List<MenuComponent> components) {
/* 110 */     components.add(this.optionsLabel);
/* 111 */     components.add(this.options);
/* 112 */     components.add(this.hitsBeforeBlockingLabel);
/* 113 */     components.add(this.hitsBeforeBlocking);
/* 114 */     components.add(this.hitsTakenBeforeBlockingLabel);
/* 115 */     components.add(this.hitsTakenBeforeBlocking);
/* 116 */     components.add(this.secondsToBlockLabel);
/* 117 */     components.add(this.secondsToBlock);
/* 118 */     components.add(this.secondsToSneakLabel);
/* 119 */     components.add(this.secondsToSneak);
/* 120 */     components.add(this.chanceLabel);
/* 121 */     components.add(this.chance);
/* 122 */     components.add(this.blockLabel);
/* 123 */     components.add(this.block);
/* 124 */     components.add(this.blockInfoLabel);
/* 125 */     components.add(this.blockOptions);
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onAttackEntity(AttackEntityEvent e) {
/* 130 */     if (e.entity != null && e.target != null) {
/* 131 */       if (!e.entity.world.isRemote) {
/*     */         return;
/*     */       }
/*     */       
/* 135 */       if (e.entity.getDistance(e.target) > 6.0F) {
/*     */         return;
/*     */       }
/*     */       
/* 139 */       if (e.entity.getUniqueID().equals(this.mc.player.getUniqueID())) {
/* 140 */         if (hasCondition(BlockOptions.HITS_GIVEN_BEFORE_BLOCK)) {
/* 141 */           this.attacksDelay++;
/*     */         }
/* 143 */       } else if (e.target.getUniqueID().equals(this.mc.player.getUniqueID()) && 
/* 144 */         hasCondition(BlockOptions.HITS_TAKEN_BEFORE_BLOCK)) {
/* 145 */         this.hitsDelay++;
/*     */       } 
/*     */ 
/*     */       
/* 149 */       if (this.attacksDelay >= this.hitsBeforeBlocking.getValue()) {
/* 150 */         this.attacksDelay = 0;
/* 151 */         onDamage();
/* 152 */       } else if (this.hitsDelay >= this.hitsTakenBeforeBlocking.getValue()) {
/* 153 */         this.hitsDelay = 0;
/* 154 */         onDamage();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/* 161 */     if (this.mc.world == null || this.mc.player == null) {
/*     */       return;
/*     */     }
/*     */     
/* 165 */     if (System.currentTimeMillis() < this.stopBlockTime && this.deactivateTime < System.currentTimeMillis()) {
/* 166 */       if (this.shouldBlock) {
/* 167 */         KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindUseItem.getKeyCode(), true);
/*     */       }
/*     */     }
/* 170 */     else if (this.shouldResetBlock) {
/* 171 */       this.shouldResetBlock = false;
/* 172 */       KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindUseItem.getKeyCode(), false);
/*     */     } 
/*     */ 
/*     */     
/* 176 */     if (System.currentTimeMillis() < this.stopSneakTime && this.deactivateTime < System.currentTimeMillis()) {
/* 177 */       if (this.shouldSneak) {
/* 178 */         KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), true);
/*     */       }
/*     */     }
/* 181 */     else if (this.shouldResetSneak) {
/* 182 */       this.shouldResetSneak = false;
/* 183 */       KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), false);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 188 */     if (this.attackReset < System.currentTimeMillis()) {
/* 189 */       this.attackReset = System.currentTimeMillis() + 3000L;
/* 190 */       this.attacksDelay = 0;
/* 191 */       this.hitsDelay = 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onDamage() {
/* 196 */     if (this.options.getValue().equalsIgnoreCase(Options.OFF.toString())) {
/* 197 */       this.shouldSneak = false;
/* 198 */     } else if (this.options.getValue().equalsIgnoreCase(Options.CHANCE.toString())) {
/* 199 */       if (1 + this.random.nextInt(99) > this.chance.getIntValue()) {
/* 200 */         this.shouldSneak = false;
/*     */       } else {
/* 202 */         this.shouldSneak = true;
/*     */       } 
/*     */     } else {
/* 205 */       this.shouldSneak = true;
/*     */     } 
/*     */     
/* 208 */     if (hasCondition(BlockOptions.CHANCE)) {
/* 209 */       if (1 + this.random.nextInt(99) > this.block.getIntValue()) {
/* 210 */         this.shouldBlock = false;
/*     */       } else {
/* 212 */         this.shouldBlock = true;
/*     */       } 
/*     */     } else {
/* 215 */       this.shouldBlock = true;
/*     */     } 
/*     */     
/* 218 */     if (this.shouldSneak) {
/* 219 */       this.shouldResetSneak = true;
/* 220 */       this.stopSneakTime = System.currentTimeMillis() + (this.secondsToSneak.getIntValue() * 1000);
/*     */     } 
/*     */     
/* 223 */     if (this.shouldBlock) {
/* 224 */       this.shouldResetBlock = true;
/* 225 */       this.stopBlockTime = System.currentTimeMillis() + (this.secondsToBlock.getIntValue() * 1000);
/*     */     } 
/*     */     
/* 228 */     Keystrokes strokes = (Keystrokes)AntiAFK.getInstance().getFeature(Keystrokes.class);
/*     */     
/* 230 */     if (strokes.isEnabled())
/* 231 */       strokes.rightClick(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\AutoBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */