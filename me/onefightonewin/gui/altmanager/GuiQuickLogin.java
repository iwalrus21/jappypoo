/*     */ package me.onefightonewin.gui.altmanager;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.NoSuchElementException;
/*     */ import me.onefightonewin.utils.AuthUtils;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiMainMenu;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiTextField;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.util.ChatAllowedCharacters;
/*     */ import net.minecraftforge.fml.relauncher.ReflectionHelper;
/*     */ import org.json.JSONObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GuiQuickLogin
/*     */   extends GuiScreen
/*     */ {
/*     */   GuiTextField email;
/*     */   GuiTextField password;
/*  24 */   String status = "";
/*     */   
/*  26 */   String passwordText = "";
/*     */ 
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  31 */     ScaledResolution sr = new ScaledResolution(this.mc);
/*     */     
/*  33 */     this.buttonList.add(new GuiButton(0, sr.getScaledWidth() / 2 - 49, sr.getScaledHeight() / 2, 98, 20, "Login"));
/*  34 */     this.buttonList.add(new GuiButton(1, sr.getScaledWidth() / 2 - 49, sr.getScaledHeight() / 2 + 22, 98, 20, "Go back"));
/*     */     
/*  36 */     this.email = new GuiTextField(1, this.mc.fontRenderer, sr.getScaledWidth() / 2 - 49, sr.getScaledHeight() / 2 - 55, 100, 20);
/*  37 */     this.password = new GuiTextField(0, this.mc.fontRenderer, sr.getScaledWidth() / 2 - 49, sr.getScaledHeight() / 2 - 30, 100, 20);
/*     */   }
/*     */   
/*     */   protected void actionPerformed(GuiButton button) {
/*  41 */     if (button.id == 0) {
/*  42 */       String username = "";
/*  43 */       String id = "";
/*  44 */       String accessToken = "";
/*     */       
/*  46 */       String error = "";
/*     */       
/*     */       try {
/*  49 */         JSONObject response = AuthUtils.authUser(this.email.getText(), this.passwordText);
/*     */         
/*  51 */         if (response.has("errorMessage")) {
/*  52 */           error = response.getString("errorMessage");
/*     */         } else {
/*  54 */           JSONObject profile = response.getJSONObject("selectedProfile");
/*     */           
/*  56 */           if (response.has("selectedProfile")) {
/*  57 */             username = profile.getString("name");
/*  58 */             id = profile.getString("id");
/*  59 */             accessToken = response.getString("accessToken");
/*     */           } else {
/*  61 */             error = "Account doesn't own minecraft.";
/*     */           } 
/*     */         } 
/*  64 */       } catch (NoSuchElementException|org.json.JSONException|IOException e) {
/*  65 */         e.printStackTrace();
/*  66 */         error = "An unknown error occurred, contact an administrator.";
/*     */       } 
/*     */       
/*  69 */       if (error.length() > 0) {
/*  70 */         this.status = error;
/*     */       } else {
/*  72 */         Field field = ReflectionHelper.findField(Minecraft.class, new String[] { "session", "field_71449_j" });
/*     */         try {
/*  74 */           field.set(this.mc, AuthUtils.getSession(username, id, accessToken));
/*  75 */           this.status = "You have successfully logged in.";
/*  76 */           this.mc.displayGuiScreen((GuiScreen)new GuiMainMenu());
/*  77 */         } catch (IllegalAccessException|IllegalArgumentException e) {
/*  78 */           e.printStackTrace();
/*  79 */           this.status = "Failed to swap session.";
/*     */         } 
/*     */       } 
/*  82 */     } else if (button.id == 1) {
/*  83 */       this.mc.displayGuiScreen(new GuiAltManager());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void keyTyped(char typedChar, int keyCode) throws IOException {
/*  88 */     if (this.email.isFocused()) {
/*  89 */       if (keyCode == 28) {
/*  90 */         this.email.setFocused(false);
/*  91 */         this.password.setFocused(true);
/*     */         return;
/*     */       } 
/*  94 */       this.email.textboxKeyTyped(typedChar, keyCode);
/*     */     } 
/*     */     
/*  97 */     if (this.password.isFocused()) {
/*  98 */       if (keyCode == 14) {
/*  99 */         if (this.password.getText().length() > 0 && this.passwordText.length() > 0)
/* 100 */           if (this.password.getCursorPosition() != this.password.getText().length()) {
/* 101 */             int cursorPos = this.password.getCursorPosition();
/*     */             
/* 103 */             if (cursorPos <= 0) {
/*     */               return;
/*     */             }
/* 106 */             this.passwordText = this.passwordText.substring(0, this.password.getCursorPosition() - 1) + this.passwordText.substring(this.password.getCursorPosition(), this.password.getText().length());
/* 107 */             this.password.setText(this.password.getText().substring(1));
/* 108 */             this.password.setCursorPosition(cursorPos - 1);
/*     */           } else {
/* 110 */             this.passwordText = this.passwordText.substring(0, this.passwordText.length() - 1);
/* 111 */             this.password.setText(this.password.getText().substring(1));
/*     */           }  
/*     */         return;
/*     */       } 
/* 115 */       if (keyCode == 28) {
/* 116 */         actionPerformed(this.buttonList.get(0)); return;
/*     */       } 
/* 118 */       if (keyCode == 203) {
/* 119 */         if (this.password.getCursorPosition() != 0) {
/* 120 */           this.password.setCursorPosition(this.password.getCursorPosition() - 1);
/*     */         }
/* 122 */       } else if (keyCode == 205 && 
/* 123 */         this.password.getCursorPosition() != this.password.getText().length()) {
/* 124 */         this.password.setCursorPosition(this.password.getCursorPosition() + 1);
/*     */       } 
/*     */ 
/*     */       
/* 128 */       if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
/* 129 */         if (this.password.getCursorPosition() != this.password.getText().length()) {
/* 130 */           int cursorPos = this.password.getCursorPosition();
/* 131 */           this.passwordText = this.passwordText.substring(0, this.password.getCursorPosition()) + typedChar + this.passwordText.substring(this.password.getCursorPosition());
/* 132 */           this.password.setText(this.password.getText() + "*");
/* 133 */           this.password.setCursorPosition(cursorPos + 1);
/*     */         } else {
/* 135 */           this.passwordText += typedChar;
/* 136 */           this.password.setText(this.password.getText() + "*");
/*     */         } 
/*     */       }
/*     */       
/* 140 */       if (GuiScreen.isKeyComboCtrlV(keyCode)) {
/* 141 */         String toAdd = GuiScreen.getClipboardString();
/* 142 */         this.passwordText += toAdd;
/*     */         
/* 144 */         for (int i = 0; i < toAdd.length(); i++) {
/* 145 */           this.password.setText(this.password.getText() + "*");
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 150 */     super.keyTyped(typedChar, keyCode);
/*     */   }
/*     */   
/*     */   public void mouseClicked(int x, int y, int mouseButton) throws IOException {
/* 154 */     this.email.mouseClicked(x, y, mouseButton);
/* 155 */     this.password.mouseClicked(x, y, mouseButton);
/* 156 */     super.mouseClicked(x, y, mouseButton);
/*     */   }
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 160 */     drawDefaultBackground();
/* 161 */     drawString(this.mc.fontRenderer, "Email: ", this.email.x - this.mc.fontRenderer.getStringWidth("Email: "), this.email.y + 5, -1);
/* 162 */     drawString(this.mc.fontRenderer, "Password: ", this.password.x - this.mc.fontRenderer.getStringWidth("Password: "), this.password.y + 5, -1);
/*     */     
/* 164 */     ScaledResolution sr = new ScaledResolution(this.mc);
/*     */     
/* 166 */     drawString(this.mc.fontRenderer, "Account manager - Quick login", sr.getScaledWidth() / 2 - this.mc.fontRenderer.getStringWidth("Account manager - Quick login") / 2, 20, -1);
/*     */     
/* 168 */     if (!this.status.isEmpty()) {
/* 169 */       drawString(this.mc.fontRenderer, this.status, sr.getScaledWidth() / 2 - this.mc.fontRenderer.getStringWidth(this.status) / 2, sr.getScaledHeight() / 2 - 80, -1);
/*     */     }
/* 171 */     this.email.drawTextBox();
/* 172 */     this.password.drawTextBox();
/*     */     
/* 174 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\gui\altmanager\GuiQuickLogin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */