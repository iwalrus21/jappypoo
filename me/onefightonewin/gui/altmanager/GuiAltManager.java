/*     */ package me.onefightonewin.gui.altmanager;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.ArrayList;
/*     */ import java.util.NoSuchElementException;
/*     */ import me.onefightonewin.utils.AuthUtils;
/*     */ import me.onefightonewin.utils.FileUtils;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiMainMenu;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraftforge.fml.relauncher.ReflectionHelper;
/*     */ import org.json.JSONObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GuiAltManager
/*     */   extends GuiScreen
/*     */ {
/*     */   GuiAltList list;
/*     */   GuiButton back;
/*     */   GuiButton quickLogin;
/*     */   GuiButton addAccount;
/*     */   GuiButton removeAccount;
/*     */   GuiButton login;
/*     */   String status;
/*     */   
/*     */   public void initGui() {
/*  38 */     this.list = new GuiAltList(this.mc, this.width, this.height, new ArrayList<>());
/*     */     
/*  40 */     for (File file : FileUtils.getAccounts()) {
/*  41 */       String fileName = file.getName();
/*     */       
/*  43 */       if (fileName.contains(".")) {
/*  44 */         fileName = fileName.substring(0, fileName.indexOf('.'));
/*     */       }
/*     */       
/*  47 */       this.list.accounts.add(new GuiAltEntry(this.list, fileName));
/*     */     } 
/*     */     
/*  50 */     this.quickLogin = new GuiButton(0, this.width / 2 - 105, this.height - 48, 100, 20, "Quick login");
/*  51 */     this.addAccount = new GuiButton(1, this.width / 2 + 5, this.height - 24, 100, 20, "Add account");
/*  52 */     this.removeAccount = new GuiButton(2, this.width / 2 - 105, this.height - 24, 100, 20, "Remove account");
/*  53 */     this.login = new GuiButton(3, this.width / 2 + 5, this.height - 48, 100, 20, "Login");
/*     */     
/*  55 */     this.removeAccount.enabled = false;
/*  56 */     this.login.enabled = false;
/*     */     
/*  58 */     this.buttonList.add(this.quickLogin);
/*  59 */     this.buttonList.add(this.addAccount);
/*  60 */     this.buttonList.add(this.removeAccount);
/*  61 */     this.buttonList.add(this.login);
/*  62 */     this.buttonList.add(new GuiButton(4, 5, 5, 100, 20, "Go back"));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actionPerformed(GuiButton button) {
/*  67 */     if (!button.enabled) {
/*     */       return;
/*     */     }
/*  70 */     if (button.id == 0) {
/*  71 */       this.mc.displayGuiScreen(new GuiQuickLogin());
/*  72 */     } else if (button.id == 1) {
/*  73 */       this.mc.displayGuiScreen(new GuiAddAlt());
/*  74 */     } else if (button.id == 2) {
/*  75 */       if (this.list.getIndex() != -1 || this.list.accounts.size() <= this.list.getIndex()) {
/*  76 */         FileUtils.removeAccount(((GuiAltEntry)this.list.accounts.get(this.list.getIndex())).string);
/*  77 */         this.mc.displayGuiScreen(new GuiAltManager());
/*     */       } 
/*  79 */     } else if (button.id == 3) {
/*  80 */       if (this.list.getIndex() != -1 || this.list.accounts.size() <= this.list.getIndex()) {
/*  81 */         String error = "";
/*     */         
/*     */         try {
/*  84 */           String[] account = FileUtils.getAccountData(((GuiAltEntry)this.list.accounts.get(this.list.getIndex())).string);
/*     */           
/*  86 */           if (account == null) {
/*     */             return;
/*     */           }
/*  89 */           JSONObject response = AuthUtils.isValidAccessToken(account[0], account[1]);
/*     */           
/*  91 */           if (response.has("errorMessage")) {
/*  92 */             response = AuthUtils.refreshUser(account[0], account[1]);
/*     */             
/*  94 */             if (response.has("errorMessage")) {
/*  95 */               error = response.getString("errorMessage");
/*     */               
/*  97 */               if (error.equalsIgnoreCase("Invalid token.")) {
/*  98 */                 error = "Invalid account, try re-adding the account.";
/*     */               }
/*     */             } else {
/* 101 */               JSONObject profile = response.getJSONObject("selectedProfile");
/*     */               
/* 103 */               if (response.has("selectedProfile")) {
/* 104 */                 FileUtils.saveAccount(((GuiAltEntry)this.list.accounts.get(this.list.getIndex())).string, response.getString("accessToken"), account[1], profile.getString("id"));
/* 105 */                 account = FileUtils.getAccountData(((GuiAltEntry)this.list.accounts.get(this.list.getIndex())).string);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */           
/* 110 */           if (error.length() > 0) {
/* 111 */             this.status = error;
/*     */           } else {
/* 113 */             Field field = ReflectionHelper.findField(Minecraft.class, new String[] { "session", "field_71449_j" });
/*     */             try {
/* 115 */               field.set(this.mc, AuthUtils.getSession(((GuiAltEntry)this.list.accounts.get(this.list.getIndex())).string, account[2], account[0]));
/* 116 */               this.status = "You have successfully logged in.";
/* 117 */               this.mc.displayGuiScreen((GuiScreen)new GuiMainMenu());
/* 118 */             } catch (IllegalAccessException|IllegalArgumentException e) {
/* 119 */               e.printStackTrace();
/* 120 */               this.status = "Failed to swap session.";
/*     */             } 
/*     */           } 
/* 123 */         } catch (NoSuchElementException|org.json.JSONException|IOException e) {
/* 124 */           e.printStackTrace();
/* 125 */           error = "An unknown error occurred, contact an administrator.";
/*     */         } 
/*     */       } 
/* 128 */     } else if (button.id == 4) {
/* 129 */       this.mc.displayGuiScreen((GuiScreen)new GuiMainMenu());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleMouseInput() throws IOException {
/* 135 */     super.handleMouseInput();
/* 136 */     this.list.handleMouseInput();
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 141 */     drawDefaultBackground();
/* 142 */     this.list.drawScreen(mouseX, mouseY, partialTicks);
/* 143 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */     
/* 145 */     ScaledResolution sr = new ScaledResolution(this.mc);
/* 146 */     String curUser = "Current user: " + this.mc.getSession().getUsername() + ".";
/*     */     
/* 148 */     drawString(this.mc.fontRenderer, this.status, sr.getScaledWidth() / 2 - this.mc.fontRenderer.getStringWidth(this.status) / 2, 20, -1);
/*     */     
/* 150 */     drawString(this.mc.fontRenderer, curUser, sr.getScaledWidth() / 2 - this.mc.fontRenderer.getStringWidth(curUser) / 2, 5, -1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/* 155 */     if (mouseButton != 0 || !this.list.mouseClicked(mouseX, mouseY, mouseButton)) {
/* 156 */       super.mouseClicked(mouseX, mouseY, mouseButton);
/*     */     }
/*     */     
/* 159 */     if (this.list.getIndex() != -1) {
/* 160 */       this.login.enabled = true;
/* 161 */       this.removeAccount.enabled = true;
/*     */     } else {
/* 163 */       this.login.enabled = false;
/* 164 */       this.removeAccount.enabled = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseReleased(int mouseX, int mouseY, int state) {
/* 170 */     if (state != 0 || !this.list.mouseReleased(mouseX, mouseY, state))
/* 171 */       super.mouseReleased(mouseX, mouseY, state); 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\gui\altmanager\GuiAltManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */