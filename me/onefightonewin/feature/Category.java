/*    */ package me.onefightonewin.feature;
/*    */ 
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public enum Category {
/*  6 */   PAGE_1("icon1", 0), PAGE_2("icon2", 1), PAGE_3("icon3", 2), PAGE_4("icon4", 3), PAGE_5("icon5", 4), PAGE_6("icon6", 5), PAGE_7("icon7", 6), PAGE_MISC("iconmisc", 7);
/*    */   
/*    */   int id;
/*    */   final ResourceLocation resource;
/*    */   
/*    */   Category(String fileName, int id) {
/* 12 */     this.resource = new ResourceLocation("antiafk", "textures/gui/" + fileName + ".png");
/* 13 */     this.id = id;
/*    */   }
/*    */   
/*    */   public ResourceLocation getResource() {
/* 17 */     return this.resource;
/*    */   }
/*    */   
/*    */   public int getId() {
/* 21 */     return this.id;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\Category.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */