/*     */ package me.onefightonewin.requests;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import org.json.JSONException;
/*     */ import org.json.JSONObject;
/*     */ 
/*     */ public class WebRequest
/*     */ {
/*     */   private URL url;
/*     */   private String requestMethod;
/*     */   private String data;
/*     */   private int responseCode;
/*     */   private JSONObject payload;
/*     */   private Map<String, String> headers;
/*     */   
/*     */   public WebRequest(String url, String requestMethod) throws MalformedURLException {
/*  26 */     this.url = new URL(url);
/*  27 */     this.requestMethod = requestMethod;
/*  28 */     this.payload = new JSONObject();
/*  29 */     this.headers = new HashMap<>();
/*     */   }
/*     */   
/*     */   public void setURL(String url) throws MalformedURLException {
/*  33 */     this.url = new URL(url);
/*     */   }
/*     */   
/*     */   public URL getURL() {
/*  37 */     return this.url;
/*     */   }
/*     */   
/*     */   public void clearHeaders() {
/*  41 */     this.headers.clear();
/*     */   }
/*     */   
/*     */   public void clearPayload() {
/*  45 */     this.payload = new JSONObject();
/*     */   }
/*     */   
/*     */   public void setHeader(String key, String value) {
/*  49 */     this.headers.put(key, value);
/*     */   }
/*     */   
/*     */   public JSONObject getPayload() {
/*  53 */     return this.payload;
/*     */   }
/*     */   
/*     */   public void connect() throws IOException, JSONException, NoSuchElementException {
/*  57 */     HttpURLConnection conn = (HttpURLConnection)this.url.openConnection();
/*     */     
/*  59 */     conn.setDoOutput(true);
/*     */     
/*  61 */     conn.setReadTimeout(5000);
/*  62 */     conn.setConnectTimeout(5000);
/*     */     
/*  64 */     conn.setRequestMethod(this.requestMethod);
/*     */     
/*  66 */     conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");
/*     */     
/*  68 */     for (String header : this.headers.keySet()) {
/*  69 */       conn.setRequestProperty(header, this.headers.get(header));
/*     */     }
/*     */     
/*  72 */     conn.setRequestProperty("Content-Type", "application/json");
/*     */     
/*  74 */     if (!this.requestMethod.equalsIgnoreCase("GET")) {
/*  75 */       conn.setRequestProperty("Content-Length", String.valueOf(this.payload.toString().length()));
/*  76 */       OutputStream os = conn.getOutputStream();
/*  77 */       os.write(this.payload.toString().getBytes());
/*     */     } 
/*     */     
/*  80 */     conn.connect();
/*     */     
/*  82 */     BufferedReader in = null;
/*     */     try {
/*  84 */       in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
/*  85 */     } catch (IOException e) {
/*  86 */       if (conn.getErrorStream() != null) {
/*  87 */         in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
/*     */       }
/*     */     } 
/*  90 */     if (in != null) {
/*  91 */       StringBuilder output = new StringBuilder();
/*  92 */       String input = "";
/*     */       
/*  94 */       while ((input = in.readLine()) != null) {
/*  95 */         if (!output.toString().isEmpty()) {
/*  96 */           output.append(System.getProperty("line.separator"));
/*     */         }
/*  98 */         output.append(input);
/*     */       } 
/*     */       
/* 101 */       this.data = output.toString();
/*     */     } else {
/* 103 */       throw new IOException("Failed to get input stream.");
/*     */     } 
/* 105 */     this.responseCode = conn.getResponseCode();
/*     */   }
/*     */   
/*     */   public String getData() {
/* 109 */     return this.data;
/*     */   }
/*     */   
/*     */   public int getResponseCode() {
/* 113 */     return this.responseCode;
/*     */   }
/*     */   
/*     */   public String getRequestMethod() {
/* 117 */     return this.requestMethod;
/*     */   }
/*     */   
/*     */   public void setRequestMethod(String requestMethod) {
/* 121 */     this.requestMethod = requestMethod;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\requests\WebRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */