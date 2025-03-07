/*    */ package org.json;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CookieList
/*    */ {
/*    */   public static JSONObject toJSONObject(String string) throws JSONException {
/* 50 */     JSONObject jo = new JSONObject();
/* 51 */     JSONTokener x = new JSONTokener(string);
/* 52 */     while (x.more()) {
/* 53 */       String name = Cookie.unescape(x.nextTo('='));
/* 54 */       x.next('=');
/* 55 */       jo.put(name, Cookie.unescape(x.nextTo(';')));
/* 56 */       x.next();
/*    */     } 
/* 58 */     return jo;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String toString(JSONObject jo) throws JSONException {
/* 72 */     boolean b = false;
/* 73 */     Iterator<E> keys = jo.keys();
/*    */     
/* 75 */     StringBuffer sb = new StringBuffer();
/* 76 */     while (keys.hasNext()) {
/* 77 */       String string = keys.next().toString();
/* 78 */       if (!jo.isNull(string)) {
/* 79 */         if (b) {
/* 80 */           sb.append(';');
/*    */         }
/* 82 */         sb.append(Cookie.escape(string));
/* 83 */         sb.append("=");
/* 84 */         sb.append(Cookie.escape(jo.getString(string)));
/* 85 */         b = true;
/*    */       } 
/*    */     } 
/* 88 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\json\CookieList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */