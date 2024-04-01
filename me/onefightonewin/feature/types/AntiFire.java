/*     */ package me.onefightonewin.feature.types;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import javax.vecmath.Vector2f;
/*     */ import me.onefightonewin.feature.Category;
/*     */ import me.onefightonewin.feature.Feature;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.settings.KeyBinding;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ import net.minecraftforge.fml.relauncher.ReflectionHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AntiFire
/*     */   extends Feature
/*     */ {
/*     */   boolean placed;
/*     */   BlockPos placedBlock;
/*     */   Field field;
/*     */   
/*     */   public AntiFire(Minecraft minecraft) {
/*  31 */     super(minecraft, "antifire", 0, Category.PAGE_5);
/*  32 */     this.field = ReflectionHelper.findField(Minecraft.class, new String[] { "rightClickDelayTimer", "field_71467_ac" });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  37 */     this.placed = false;
/*  38 */     this.placedBlock = null;
/*     */   }
/*     */   @SubscribeEvent
/*     */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/*     */     int delay;
/*  43 */     if (this.mc.world == null || this.mc.player == null) {
/*     */       return;
/*     */     }
/*     */     
/*  47 */     int slot = -1;
/*     */     
/*  49 */     for (int i = 0; i < InventoryPlayer.getHotbarSize(); i++) {
/*  50 */       if (this.mc.player.inventory.getStackInSlot(i) != null && Item.getIdFromItem(this.mc.player.inventory.getStackInSlot(i).getItem()) == Item.getIdFromItem(Items.BUCKET)) {
/*  51 */         slot = i;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*  56 */     if (this.placedBlock != null && 
/*  57 */       this.mc.player.getDistanceSq(this.placedBlock) >= 3.0D) {
/*  58 */       this.placedBlock = null;
/*  59 */       this.placed = false;
/*  60 */       setUseItem(false);
/*     */     } 
/*     */ 
/*     */     
/*  64 */     boolean onFire = this.mc.player.isBurning();
/*     */     
/*  66 */     if (this.placed) {
/*  67 */       if (this.placedBlock == null && slot != -1) {
/*  68 */         int radius = 3;
/*     */         
/*  70 */         for (int x = -radius; x < radius; x++) {
/*  71 */           for (int y = -radius; y < radius; y++) {
/*  72 */             for (int z = -radius; z < radius; z++) {
/*  73 */               BlockPos pos = new BlockPos(this.mc.player.posX - x, this.mc.player.posY - y, this.mc.player.posZ - z);
/*  74 */               IBlockState curstate = this.mc.world.getBlockState(pos);
/*     */               
/*  76 */               if (curstate.getBlock() == Blocks.WATER) {
/*  77 */                 this.placedBlock = pos;
/*     */                 break;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*  83 */       } else if (this.placedBlock != null && slot != -1) {
/*  84 */         IBlockState state = this.mc.world.getBlockState(this.placedBlock);
/*     */         
/*  86 */         if (state.getBlock() != Blocks.WATER) {
/*  87 */           this.placed = false;
/*  88 */           setUseItem(false);
/*  89 */           this.placedBlock = null;
/*     */           
/*     */           return;
/*     */         } 
/*  93 */         setUseItem(true);
/*  94 */         setAngles(getAimPoint(this.placedBlock, EnumFacing.UP));
/*  95 */         this.mc.player.inventory.currentItem = slot;
/*     */       } else {
/*  97 */         this.placed = false;
/*  98 */         setUseItem(false);
/*  99 */         this.placedBlock = null;
/*     */       } 
/*     */     }
/*     */     
/* 103 */     if (!onFire) {
/*     */       return;
/*     */     }
/*     */     
/* 107 */     if (!this.mc.player.onGround) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 114 */       delay = this.field.getInt(this.mc);
/* 115 */     } catch (IllegalArgumentException|IllegalAccessException e) {
/* 116 */       e.printStackTrace();
/*     */       
/*     */       return;
/*     */     } 
/* 120 */     if (delay > 0) {
/*     */       return;
/*     */     }
/*     */     
/* 124 */     slot = -1;
/*     */     
/* 126 */     for (int j = 0; j < InventoryPlayer.getHotbarSize(); j++) {
/* 127 */       if (this.mc.player.inventory.getStackInSlot(j) != null && Item.getIdFromItem(this.mc.player.inventory.getStackInSlot(j).getItem()) == Item.getIdFromItem(Items.WATER_BUCKET)) {
/* 128 */         slot = j;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 133 */     if (slot != -1 && !this.placed) {
/* 134 */       BlockPos pos = getPos();
/* 135 */       this.placed = true;
/* 136 */       this.mc.player.inventory.currentItem = slot;
/* 137 */       Vector2f vec = getAimPoint(pos, EnumFacing.DOWN);
/* 138 */       vec.y = 90.0F;
/* 139 */       setAngles(vec);
/* 140 */       setUseItem(true);
/*     */     } 
/*     */   }
/*     */   
/*     */   public BlockPos getPos() {
/* 145 */     return new BlockPos(Math.round(this.mc.player.posX), Math.round(this.mc.player.posY), Math.round(this.mc.player.posZ));
/*     */   }
/*     */   
/*     */   public void setAngles(Vector2f vec) {
/* 149 */     this.mc.player.rotationYaw = vec.x;
/* 150 */     this.mc.player.rotationPitch = vec.y;
/*     */   }
/*     */   
/*     */   public void setUseItem(boolean useItem) {
/* 154 */     KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindUseItem.getKeyCode(), useItem);
/*     */   }
/*     */   
/*     */   private Vector2f getAimPoint(BlockPos block, EnumFacing facing) {
/* 158 */     double x = block.getX() - this.mc.player.posX;
/* 159 */     double y = block.getY() - this.mc.player.posY - this.mc.player.getEyeHeight() + 0.5D;
/* 160 */     double z = block.getZ() - this.mc.player.posZ;
/*     */     
/* 162 */     if (facing != null) {
/* 163 */       switch (facing) {
/*     */         case UP:
/*     */         case DOWN:
/* 166 */           x += 0.5D;
/* 167 */           z += 0.5D;
/*     */           break;
/*     */         
/*     */         case EAST:
/* 171 */           z += 0.5D;
/*     */           break;
/*     */         
/*     */         case NORTH:
/* 175 */           x += 0.5D;
/* 176 */           z++;
/*     */           break;
/*     */         
/*     */         case SOUTH:
/* 180 */           x += 0.5D;
/*     */           break;
/*     */         
/*     */         case WEST:
/* 184 */           x++;
/* 185 */           z += 0.5D;
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 193 */     double sq = Math.sqrt(x * x + z * z);
/* 194 */     float pitch = (float)-Math.toDegrees(Math.atan2(y, sq));
/* 195 */     float yaw = (float)Math.toDegrees(Math.atan2(z, x)) - 90.0F;
/*     */     
/* 197 */     return new Vector2f(yaw, pitch);
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\AntiFire.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */