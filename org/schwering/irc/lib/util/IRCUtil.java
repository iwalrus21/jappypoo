/*     */ package org.schwering.irc.lib.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ 
/*     */ public class IRCUtil
/*     */   implements IRCConstants
/*     */ {
/*     */   public static boolean isChan(String str) {
/*     */     int c;
/*  61 */     return (str.length() >= 2 && ((
/*  62 */       c = str.charAt(0)) == 35 || c == 38 || c == 33 || c == 43));
/*     */   }
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
/*     */   public static int parseInt(String str) {
/*     */     try {
/*  76 */       return Integer.parseInt(str);
/*  77 */     } catch (NumberFormatException exc) {
/*  78 */       return -1;
/*     */     } 
/*     */   }
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static String stripColors(String str) {
/*  97 */     return stripColors(new StringBuilder(str), false).toString();
/*     */   }
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StringBuilder stripColors(StringBuilder buf) {
/* 116 */     return stripColors(buf, false);
/*     */   }
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
/*     */ 
/*     */   
/*     */   public static StringBuilder stripColorsAndCTCPDelimiters(StringBuilder buf) {
/* 133 */     return stripColors(buf, true);
/*     */   }
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
/*     */ 
/*     */ 
/*     */   
/*     */   private static StringBuilder stripColors(StringBuilder buf, boolean removeCTCP) {
/* 151 */     int len = buf.length();
/*     */     int j;
/* 153 */     for (int i = 0; i < len; j = ++i) {
/* 154 */       char c = buf.charAt(i);
/*     */ 
/*     */       
/*     */       try {
/* 158 */         if (c == '\003') {
/* 159 */           c = buf.charAt(++j);
/* 160 */           if ('0' <= c && c <= '9') {
/* 161 */             c = buf.charAt(++j);
/* 162 */             if ('0' <= c && c <= '9')
/* 163 */               c = buf.charAt(++j); 
/*     */           } 
/* 165 */           if (c == ',')
/* 166 */             c = buf.charAt(++j); 
/* 167 */           if ('0' <= c && c <= '9') {
/* 168 */             c = buf.charAt(++j);
/* 169 */             if ('0' <= c && c <= '9') {
/* 170 */               c = buf.charAt(++j);
/*     */             }
/*     */           }
/*     */         
/* 174 */         } else if ((removeCTCP && c == '\001') || c == '\037' || c == '\002' || c == '\017' || c == '\026') {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 179 */           j++;
/*     */         } 
/* 181 */       } catch (StringIndexOutOfBoundsException stringIndexOutOfBoundsException) {}
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 186 */       if (j > i) {
/* 187 */         buf = buf.delete(i, j);
/* 188 */         len -= j - i;
/* 189 */         i--;
/*     */       } 
/*     */     } 
/* 192 */     return buf;
/*     */   }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String[] split(String str, int delim, String trailing) {
/* 213 */     List<String> items = new ArrayList<>(15);
/* 214 */     int last = 0;
/* 215 */     int index = 0;
/* 216 */     int len = str.length();
/* 217 */     while (index < len) {
/* 218 */       if (str.charAt(index) == delim) {
/* 219 */         items.add(str.substring(last, index));
/* 220 */         last = index + 1;
/*     */       } 
/* 222 */       index++;
/*     */     } 
/* 224 */     if (last != len)
/* 225 */       items.add(str.substring(last)); 
/* 226 */     if (trailing != null && trailing.length() != 0)
/* 227 */       items.add(trailing); 
/* 228 */     String[] result = items.<String>toArray(new String[0]);
/* 229 */     return result;
/*     */   }
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
/*     */   public static String[] split(String str, int delim) {
/* 242 */     return split(str, delim, null);
/*     */   }
/*     */   
/*     */   public static int[] toArray(Collection<Integer> list) {
/* 246 */     if (list == null || list.isEmpty()) {
/* 247 */       return new int[0];
/*     */     }
/* 249 */     int[] result = new int[list.size()];
/* 250 */     int i = 0;
/* 251 */     for (Iterator<Integer> iterator = list.iterator(); iterator.hasNext(); ) { int value = ((Integer)iterator.next()).intValue();
/* 252 */       result[i++] = value; }
/*     */     
/* 254 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\schwering\irc\li\\util\IRCUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */