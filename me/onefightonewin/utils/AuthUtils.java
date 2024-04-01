/*     */ package me.onefightonewin.utils;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.UUID;
/*     */ import javax.annotation.Nullable;
/*     */ import me.onefightonewin.requests.WebRequest;
/*     */ import net.minecraft.util.Session;
/*     */ import org.json.JSONException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AuthUtils
/*     */ {
/*     */   public static JSONObject authUser(String email, String password) throws JSONException, NoSuchElementException, IOException {
/*     */     WebRequest request;
/*     */     try {
/*  29 */       request = new WebRequest("https://authserver.mojang.com/authenticate", "POST");
/*  30 */     } catch (MalformedURLException e) {
/*  31 */       return (new JSONObject()).put("errorMessage", "Invalid url.");
/*     */     } 
/*     */     
/*  34 */     request.setHeader("Content-Type", "application/json");
/*     */     
/*  36 */     JSONObject agent = new JSONObject();
/*     */     try {
/*  38 */       agent.put("name", "Minecraft");
/*  39 */       agent.put("version", 1);
/*  40 */     } catch (JSONException e) {
/*  41 */       return (new JSONObject()).put("errorMessage", "Invalid agent.");
/*     */     } 
/*     */     
/*  44 */     request.getPayload().put("agent", agent);
/*  45 */     request.getPayload().put("username", email);
/*  46 */     request.getPayload().put("password", password);
/*  47 */     request.getPayload().put("clientToken", UUID.randomUUID().toString().replace("-", ""));
/*     */     
/*  49 */     request.connect();
/*     */     
/*  51 */     return new JSONObject(request.getData());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JSONObject refreshUser(String accessToken, String clientToken) throws JSONException, NoSuchElementException, IOException {
/*     */     WebRequest request;
/*     */     try {
/*  64 */       request = new WebRequest("https://authserver.mojang.com/refresh", "POST");
/*  65 */     } catch (MalformedURLException e) {
/*  66 */       return (new JSONObject()).put("errorMessage", "Invalid url.");
/*     */     } 
/*     */     
/*  69 */     request.setHeader("Content-Type", "application/json");
/*     */     
/*  71 */     request.getPayload().put("accessToken", accessToken);
/*  72 */     request.getPayload().put("clientToken", clientToken);
/*     */     
/*  74 */     request.connect();
/*     */     
/*  76 */     return new JSONObject(request.getData());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JSONObject isValidAccessToken(String accessToken, String clientToken) throws JSONException, NoSuchElementException, IOException {
/*     */     WebRequest request;
/*     */     try {
/*  89 */       request = new WebRequest("https://authserver.mojang.com/validate", "POST");
/*  90 */     } catch (MalformedURLException e) {
/*  91 */       return (new JSONObject()).put("errorMessage", "Invalid url.");
/*     */     } 
/*     */     
/*  94 */     request.setHeader("Content-Type", "application/json");
/*     */     
/*  96 */     request.getPayload().put("accessToken", accessToken);
/*  97 */     request.getPayload().put("clientToken", clientToken);
/*     */     
/*  99 */     request.connect();
/*     */     
/* 101 */     if (request.getResponseCode() == 204)
/* 102 */       return new JSONObject(); 
/* 103 */     if (request.getResponseCode() == 403) {
/* 104 */       return (new JSONObject()).put("errorMessage", "Access token is invalid.");
/*     */     }
/*     */     
/* 107 */     return new JSONObject(request.getData());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static JSONObject signOutUser(String email, String password) throws JSONException, NoSuchElementException, IOException {
/*     */     WebRequest request;
/*     */     try {
/* 121 */       request = new WebRequest("https://authserver.mojang.com/signout", "POST");
/* 122 */     } catch (MalformedURLException e) {
/* 123 */       return (new JSONObject()).put("errorMessage", "Invalid url.");
/*     */     } 
/*     */     
/* 126 */     request.setHeader("Content-Type", "application/json");
/*     */     
/* 128 */     request.getPayload().put("username", email);
/* 129 */     request.getPayload().put("password", password);
/*     */     
/* 131 */     request.connect();
/*     */     
/* 133 */     if (request.getResponseCode() == 204) {
/* 134 */       return new JSONObject();
/*     */     }
/*     */     
/* 137 */     return new JSONObject(request.getData());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static JSONObject invalidateToken(String accessToken, String clientToken) throws JSONException, NoSuchElementException, IOException {
/*     */     WebRequest request;
/*     */     try {
/* 151 */       request = new WebRequest("https://authserver.mojang.com/invalidate", "POST");
/* 152 */     } catch (MalformedURLException e) {
/* 153 */       return (new JSONObject()).put("errorMessage", "Invalid url.");
/*     */     } 
/*     */     
/* 156 */     request.setHeader("Content-Type", "application/json");
/*     */     
/* 158 */     request.getPayload().put("accessToken", accessToken);
/* 159 */     request.getPayload().put("clientToken", clientToken);
/*     */     
/* 161 */     request.connect();
/*     */     
/* 163 */     if (request.getResponseCode() == 204) {
/* 164 */       return new JSONObject();
/*     */     }
/*     */     
/* 167 */     return new JSONObject(request.getData());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Session getSession(String username, String id, String accessToken) {
/* 178 */     return new Session(username, id, accessToken, Session.Type.MOJANG.toString());
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewi\\utils\AuthUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */