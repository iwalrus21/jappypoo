/*     */ package me.onefightonewin.gui.altmanager;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.NoSuchElementException;
/*     */ import me.onefightonewin.utils.AuthUtils;
/*     */ import me.onefightonewin.utils.FileUtils;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiTextField;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.util.ChatAllowedCharacters;
/*     */ import org.json.JSONObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GuiAddAlt
/*     */   extends GuiScreen
/*     */ {
/*     */   GuiTextField email;
/*     */   GuiTextField password;
/*  21 */   String status = "";
/*     */   
/*  23 */   String passwordText = "";
/*     */ 
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  28 */     ScaledResolution sr = new ScaledResolution(this.mc);
/*     */     
/*  30 */     this.buttonList.add(new GuiButton(0, sr.getScaledWidth() / 2 - 49, sr.getScaledHeight() / 2, 98, 20, "Add account"));
/*  31 */     this.buttonList.add(new GuiButton(1, sr.getScaledWidth() / 2 - 49, sr.getScaledHeight() / 2 + 22, 98, 20, "Go back"));
/*     */     
/*  33 */     this.email = new GuiTextField(1, this.mc.fontRenderer, sr.getScaledWidth() / 2 - 49, sr.getScaledHeight() / 2 - 55, 100, 20);
/*  34 */     this.password = new GuiTextField(0, this.mc.fontRenderer, sr.getScaledWidth() / 2 - 49, sr.getScaledHeight() / 2 - 30, 100, 20);
/*     */   }
/*     */   
/*     */   protected void actionPerformed(GuiButton button) {
/*  38 */     if (button.id == 0) {
/*  39 */       String username = "";
/*  40 */       String id = "";
/*  41 */       String accessToken = "";
/*  42 */       String clientToken = "";
/*     */       
/*  44 */       String error = "";
/*     */       
/*     */       try {
/*  47 */         JSONObject response = AuthUtils.authUser(this.email.getText(), this.passwordText);
/*     */         
/*  49 */         if (response.has("errorMessage")) {
/*  50 */           error = response.getString("errorMessage");
/*     */         } else {
/*  52 */           JSONObject profile = response.getJSONObject("selectedProfile");
/*     */           
/*  54 */           if (response.has("selectedProfile")) {
/*  55 */             username = profile.getString("name");
/*  56 */             id = profile.getString("id");
/*  57 */             accessToken = response.getString("accessToken");
/*  58 */             clientToken = response.getString("clientToken");
/*     */           } else {
/*  60 */             error = "Account doesn't own minecraft.";
/*     */           } 
/*     */         } 
/*  63 */       } catch (NoSuchElementException|org.json.JSONException|IOException e) {
/*  64 */         e.printStackTrace();
/*  65 */         error = "An unknown error occurred, contact an administrator.";
/*     */       } 
/*     */       
/*  68 */       if (error.length() > 0) {
/*  69 */         this.status = error;
/*     */       } else {
/*     */         try {
/*  72 */           FileUtils.saveAccount(username, accessToken, clientToken, id);
/*  73 */         } catch (IOException e) {
/*  74 */           e.printStackTrace();
/*     */         } 
/*  76 */         this.mc.displayGuiScreen(new GuiAltManager());
/*     */       } 
/*  78 */     } else if (button.id == 1) {
/*  79 */       this.mc.displayGuiScreen(new GuiAltManager());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void keyTyped(char typedChar, int keyCode) throws IOException {
/*  84 */     if (this.email.isFocused()) {
/*  85 */       if (keyCode == 28) {
/*  86 */         this.email.setFocused(false);
/*  87 */         this.password.setFocused(true);
/*     */         return;
/*     */       } 
/*  90 */       this.email.textboxKeyTyped(typedChar, keyCode);
/*     */     } 
/*     */     
/*  93 */     if (this.password.isFocused()) {
/*  94 */       if (keyCode == 14) {
/*  95 */         if (this.password.getText().length() > 0 && this.passwordText.length() > 0)
/*  96 */           if (this.password.getCursorPosition() != this.password.getText().length()) {
/*  97 */             int cursorPos = this.password.getCursorPosition();
/*     */             
/*  99 */             if (cursorPos <= 0) {
/*     */               return;
/*     */             }
/* 102 */             this.passwordText = this.passwordText.substring(0, this.password.getCursorPosition() - 1) + this.passwordText.substring(this.password.getCursorPosition(), this.password.getText().length());
/* 103 */             this.password.setText(this.password.getText().substring(1));
/* 104 */             this.password.setCursorPosition(cursorPos - 1);
/*     */           } else {
/* 106 */             this.passwordText = this.passwordText.substring(0, this.passwordText.length() - 1);
/* 107 */             this.password.setText(this.password.getText().substring(1));
/*     */           }  
/*     */         return;
/*     */       } 
/* 111 */       if (keyCode == 28) {
/* 112 */         actionPerformed(this.buttonList.get(0)); return;
/*     */       } 
/* 114 */       if (keyCode == 203) {
/* 115 */         if (this.password.getCursorPosition() != 0) {
/* 116 */           this.password.setCursorPosition(this.password.getCursorPosition() - 1);
/*     */         }
/* 118 */       } else if (keyCode == 205 && 
/* 119 */         this.password.getCursorPosition() != this.password.getText().length()) {
/* 120 */         this.password.setCursorPosition(this.password.getCursorPosition() + 1);
/*     */       } 
/*     */ 
/*     */       
/* 124 */       if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
/* 125 */         if (this.password.getCursorPosition() != this.password.getText().length()) {
/* 126 */           int cursorPos = this.password.getCursorPosition();
/* 127 */           this.passwordText = this.passwordText.substring(0, this.password.getCursorPosition()) + typedChar + this.passwordText.substring(this.password.getCursorPosition());
/* 128 */           this.password.setText(this.password.getText() + "*");
/* 129 */           this.password.setCursorPosition(cursorPos + 1);
/*     */         } else {
/* 131 */           this.passwordText += typedChar;
/* 132 */           this.password.setText(this.password.getText() + "*");
/*     */         } 
/*     */       }
/*     */       
/* 136 */       if (GuiScreen.isKeyComboCtrlV(keyCode)) {
/* 137 */         String toAdd = GuiScreen.getClipboardString();
/* 138 */         this.passwordText += toAdd;
/*     */         
/* 140 */         for (int i = 0; i < toAdd.length(); i++) {
/* 141 */           this.password.setText(this.password.getText() + "*");
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 146 */     super.keyTyped(typedChar, keyCode);
/*     */   }
/*     */   
/*     */   public void mouseClicked(int x, int y, int mouseButton) throws IOException {
/* 150 */     this.email.mouseClicked(x, y, mouseButton);
/* 151 */     this.password.mouseClicked(x, y, mouseButton);
/* 152 */     super.mouseClicked(x, y, mouseButton);
/*     */   }
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 156 */     drawDefaultBackground();
/* 157 */     drawString(this.mc.fontRenderer, "Email: ", this.email.x - this.mc.fontRenderer.getStringWidth("Email: "), this.email.y + 5, -1);
/* 158 */     drawString(this.mc.fontRenderer, "Password: ", this.password.x - this.mc.fontRenderer.getStringWidth("Password: "), this.password.y + 5, -1);
/*     */     
/* 160 */     ScaledResolution sr = new ScaledResolution(this.mc);
/*     */     
/* 162 */     drawString(this.mc.fontRenderer, "Account manager - Add account", sr.getScaledWidth() / 2 - this.mc.fontRenderer.getStringWidth("Account manager - Add account") / 2, 20, -1);
/*     */     
/* 164 */     if (!this.status.isEmpty()) {
/* 165 */       drawString(this.mc.fontRenderer, this.status, sr.getScaledWidth() / 2 - this.mc.fontRenderer.getStringWidth(this.status) / 2, sr.getScaledHeight() / 2 - 80, -1);
/*     */     }
/* 167 */     this.email.drawTextBox();
/* 168 */     this.password.drawTextBox();
/*     */     
/* 170 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\gui\altmanager\GuiAddAlt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */