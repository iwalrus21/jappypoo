/*    */ package me.onefightonewin.chat;
/*    */ 
/*    */ public enum ChatColor {
/*  4 */   BLACK("0"), DARK_BLUE("1"), DARK_GREEN("2"), DARK_AQUA("3"), DARK_RED("4"), DARK_PURPLE("5"), GOLD("6"), GRAY("7"), DARK_GRAY("8"), INDIGO("9"), GREEN("A"), AQUA("B"), RED("C"), PINK("D"), YELLOW("E"), WHITE("F"), STRIKE_THROUGH("M"), UNDRLINE("N"), BOLD("L"), RANDOM("K"), ITALIC("O"), RESET("R");
/*    */   private String colorCode;
/*    */   
/*    */   ChatColor(String colorCode) {
/*  8 */     this.colorCode = colorCode;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 13 */     return "§" + this.colorCode;
/*    */   }
/*    */   
/*    */   public String getColorSuffix() {
/* 17 */     return this.colorCode;
/*    */   }
/*    */   
/*    */   public static String stripColors(String string) {
/* 21 */     String finalStr = string;
/*    */     
/* 23 */     for (ChatColor color : values()) {
/* 24 */       finalStr = finalStr.replaceAll("(?i)ï¿½" + color.getColorSuffix(), "");
/*    */     }
/* 26 */     return finalStr;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\chat\ChatColor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */