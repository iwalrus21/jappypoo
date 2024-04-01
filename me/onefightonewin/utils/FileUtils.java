/*     */ package me.onefightonewin.utils;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Base64;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import me.onefightonewin.AntiAFK;
/*     */ import me.onefightonewin.feature.Feature;
/*     */ import me.onefightonewin.gui.GuiMenu;
/*     */ import me.onefightonewin.gui.framework.MenuComponent;
/*     */ import me.onefightonewin.gui.framework.components.MenuCheckbox;
/*     */ import me.onefightonewin.gui.framework.components.MenuColorPicker;
/*     */ import me.onefightonewin.gui.framework.components.MenuComboBox;
/*     */ import me.onefightonewin.gui.framework.components.MenuDataObject;
/*     */ import me.onefightonewin.gui.framework.components.MenuDropdown;
/*     */ import me.onefightonewin.gui.framework.components.MenuLabel;
/*     */ import me.onefightonewin.gui.framework.components.MenuSlider;
/*     */ import me.onefightonewin.gui.framework.components.MenuTextField;
/*     */ 
/*     */ public class FileUtils {
/*  29 */   public static final File accountdir = new File(System.getenv("LOCALAPPDATA") + File.separator + (AntiAFK.getInstance()).name + File.separator + "accounts");
/*     */   public static final String accountExtension = ".txt";
/*     */   
/*     */   static {
/*  33 */     if (!accountdir.exists())
/*  34 */       accountdir.mkdirs(); 
/*     */   }
/*     */   
/*     */   public static File[] getAccounts() {
/*  38 */     List<File> files = new ArrayList<>();
/*     */     
/*  40 */     for (File file : accountdir.listFiles()) {
/*  41 */       if (!file.isDirectory() && file.getName().endsWith(".txt")) {
/*  42 */         files.add(file);
/*     */       }
/*     */     } 
/*     */     
/*  46 */     return files.<File>toArray(new File[files.size()]);
/*     */   }
/*     */   
/*     */   public static void saveAccount(String account, String accessToken, String clientToken, String uuid) throws IOException {
/*  50 */     File file = new File(accountdir.getPath() + File.separator + account + ".txt");
/*     */     
/*  52 */     if (!file.exists()) {
/*  53 */       file.createNewFile();
/*     */     }
/*  55 */     try (FileWriter fw = new FileWriter(file)) {
/*  56 */       fw.write(Base64.getEncoder().encodeToString(accessToken.getBytes()) + "\r" + Base64.getEncoder().encodeToString(clientToken.getBytes()) + "\r" + Base64.getEncoder().encodeToString(uuid.getBytes()));
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String[] getAccountData(String account) throws IOException {
/*  61 */     File file = new File(accountdir.getPath() + File.separator + account + ".txt");
/*     */     
/*  63 */     if (!file.exists()) {
/*  64 */       return null;
/*     */     }
/*  66 */     try (BufferedReader br = new BufferedReader(new FileReader(file))) {
/*  67 */       String[] details = new String[3];
/*     */       
/*  69 */       for (int i = 0; i < details.length; i++) {
/*  70 */         String data = br.readLine();
/*     */         
/*  72 */         if (data == null) {
/*  73 */           return null;
/*     */         }
/*  75 */         details[i] = new String(Base64.getDecoder().decode(data));
/*     */       } 
/*     */       
/*  78 */       return details;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void removeAccount(String account) {
/*  83 */     File accountFile = new File(accountdir.getPath() + File.separator + account + ".txt");
/*  84 */     if (accountFile.exists())
/*  85 */       accountFile.delete(); 
/*     */   }
/*     */   
/*     */   public static String[] getConfigFromFile(String config) {
/*  89 */     File cfg = new File(System.getenv("LOCALAPPDATA") + File.separator + (AntiAFK.getInstance()).name + File.separator + config + ".cfg");
/*     */     
/*  91 */     if (!cfg.exists()) {
/*  92 */       return new String[0];
/*     */     }
/*     */     
/*  95 */     List<String> lines = new ArrayList<>();
/*     */     
/*  97 */     try (BufferedReader br = new BufferedReader(new FileReader(cfg))) {
/*  98 */       String line = "";
/*     */       
/* 100 */       while ((line = br.readLine()) != null) {
/* 101 */         lines.add(line);
/*     */       }
/* 103 */     } catch (FileNotFoundException e) {
/* 104 */       e.printStackTrace();
/* 105 */     } catch (IOException e) {
/* 106 */       e.printStackTrace();
/*     */     } 
/*     */     
/* 109 */     return lines.<String>toArray(new String[lines.size()]);
/*     */   }
/*     */   
/*     */   public static String[] getConfigDataFromString(String config) {
/*     */     try {
/* 114 */       return (new String(Base64.getDecoder().decode(config))).split("\r");
/* 115 */     } catch (IllegalArgumentException e) {
/* 116 */       return new String[0];
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void loadConfig(String[] data) {
/* 121 */     if (data == null || data.length == 0) {
/*     */       return;
/*     */     }
/* 124 */     System.out.println(data.length);
/* 125 */     (AntiAFK.getInstance()).friends.clear();
/*     */     
/* 127 */     List<MenuComponent> components = new ArrayList<>();
/* 128 */     for (Feature feature : (AntiAFK.getInstance()).features) {
/* 129 */       feature.onMenuInit();
/* 130 */       feature.onMenuAdd(components);
/*     */     } 
/*     */     
/* 133 */     int type = -1;
/*     */     
/* 135 */     for (String line : data) {
/* 136 */       if (line.equalsIgnoreCase("[Features]")) {
/* 137 */         type = 0;
/*     */       }
/* 139 */       else if (line.equalsIgnoreCase("[Misc]")) {
/* 140 */         type = 1;
/*     */       }
/* 142 */       else if (line.equalsIgnoreCase("[Friends]")) {
/* 143 */         type = 2;
/*     */       }
/* 145 */       else if (line.equalsIgnoreCase("[XRay]")) {
/* 146 */         type = 3;
/*     */       }
/* 148 */       else if (line.equalsIgnoreCase("[Data]")) {
/* 149 */         type = 4;
/*     */       }
/*     */       else {
/*     */         
/* 153 */         String[] args = line.split(":");
/*     */         
/* 155 */         if (type == 0) {
/* 156 */           if (args.length == 3) {
/* 157 */             String name = args[0];
/* 158 */             int key = Integer.parseInt(args[1]);
/* 159 */             boolean enabled = Boolean.valueOf(args[2]).booleanValue();
/*     */             
/* 161 */             for (Feature feature : (AntiAFK.getInstance()).features) {
/* 162 */               if (feature.getName().equals(name)) {
/* 163 */                 if (feature.getKey() != -1) {
/* 164 */                   feature.setKey(key);
/*     */                 }
/* 166 */                 feature.setEnabled(enabled);
/*     */                 break;
/*     */               } 
/*     */             } 
/*     */           } 
/* 171 */         } else if (type == 1) {
/* 172 */           if (args.length == 2) {
/* 173 */             String key = args[0];
/* 174 */             String value = args[1];
/*     */             
/* 176 */             if (key.equals("color")) {
/* 177 */               GuiMenu.theColor = new Color(Integer.parseInt(value), true);
/* 178 */             } else if (key.equals("color-hover")) {
/* 179 */               GuiMenu.theColorHover = new Color(Integer.parseInt(value), true);
/* 180 */             } else if (key.equals("background")) {
/* 181 */               GuiMenu.curBg = Integer.parseInt(value);
/*     */             } else {
/* 183 */               String lastLabel = "";
/*     */               
/* 185 */               for (MenuComponent component : components) {
/* 186 */                 if (component instanceof MenuLabel) {
/* 187 */                   lastLabel = ((MenuLabel)component).getText(); continue;
/*     */                 } 
/* 189 */                 if (component instanceof MenuCheckbox) {
/* 190 */                   lastLabel = ((MenuCheckbox)component).getText();
/*     */                 }
/*     */                 
/* 193 */                 if (args.length == 2 && lastLabel.length() > 0 && args[0].equalsIgnoreCase(lastLabel)) {
/* 194 */                   if (component instanceof MenuCheckbox) {
/* 195 */                     MenuCheckbox checkbox = (MenuCheckbox)component;
/* 196 */                     if (isBoolean(args[1]) && 
/* 197 */                       checkbox.getText().equalsIgnoreCase(args[0])) {
/* 198 */                       checkbox.setChecked(Boolean.valueOf(args[1]).booleanValue());
/* 199 */                       checkbox.onAction(); break;
/*     */                     } 
/*     */                     continue;
/*     */                   } 
/* 203 */                   if (component instanceof MenuComboBox) {
/* 204 */                     MenuComboBox combo = (MenuComboBox)component;
/* 205 */                     if (args[1].length() > 2) {
/* 206 */                       combo.setValues(args[1].substring(1, args[1].length() - 1).split(", "));
/* 207 */                       combo.onAction();
/*     */                     }  break;
/*     */                   } 
/* 210 */                   if (component instanceof MenuDropdown) {
/* 211 */                     MenuDropdown drop = (MenuDropdown)component;
/* 212 */                     drop.setValue(args[1]);
/* 213 */                     drop.onAction(); break;
/*     */                   } 
/* 215 */                   if (component instanceof MenuSlider) {
/* 216 */                     MenuSlider slider = (MenuSlider)component;
/*     */                     
/* 218 */                     if (isFloat(args[1])) {
/* 219 */                       float fValue = Float.valueOf(args[1]).floatValue();
/* 220 */                       if (fValue > slider.getMaxValue()) {
/* 221 */                         fValue = slider.getMaxValue();
/* 222 */                       } else if (fValue < slider.getMinValue()) {
/* 223 */                         fValue = slider.getMinValue();
/* 224 */                       }  slider.setValue(fValue);
/* 225 */                       slider.onAction();
/*     */                     }  break;
/*     */                   } 
/* 228 */                   if (component instanceof MenuTextField) {
/* 229 */                     MenuTextField textfield = (MenuTextField)component;
/* 230 */                     textfield.setText(args[1]); break;
/*     */                   }  continue;
/*     */                 } 
/* 233 */                 if (args[0].equalsIgnoreCase(lastLabel + "-color") && 
/* 234 */                   component instanceof MenuColorPicker) {
/* 235 */                   MenuColorPicker picker = (MenuColorPicker)component;
/* 236 */                   String[] parts = args[1].split(";");
/*     */                   
/* 238 */                   if (isInt(parts[0]) && isInt(parts[1])) {
/* 239 */                     Color color = new Color(Integer.valueOf(parts[0]).intValue(), true);
/* 240 */                     picker.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), Integer.valueOf(parts[1]).intValue()));
/* 241 */                     picker.onAction();
/*     */                   } 
/*     */                   
/*     */                   break;
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/* 249 */         } else if (type == 2) {
/* 250 */           if (args.length == 1) {
/* 251 */             (AntiAFK.getInstance()).friends.add(args[0]);
/*     */           }
/* 253 */         } else if (type == 3) {
/* 254 */           if (args.length == 1 && isInt(args[0])) {
/* 255 */             (AntiAFK.getInstance()).xray.add(Integer.valueOf(Integer.parseInt(args[0])));
/*     */           }
/* 257 */         } else if (type == 4 && 
/* 258 */           args.length == 2) {
/* 259 */           String key = args[0];
/* 260 */           String value = args[1];
/*     */           
/* 262 */           for (MenuComponent component : components) {
/* 263 */             if (component instanceof MenuDataObject) {
/* 264 */               MenuDataObject obj = (MenuDataObject)component;
/*     */               
/* 266 */               if (obj.getKey().equalsIgnoreCase(key)) {
/* 267 */                 obj.setValue(value);
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String getExportConfig() {
/* 277 */     return Base64.getEncoder().encodeToString(String.join("", (CharSequence[])getInternalConfig()).getBytes());
/*     */   }
/*     */   
/*     */   public static String[] getInternalConfig() {
/* 281 */     List<String> lines = new ArrayList<>();
/* 282 */     lines.add("[Features]\r");
/*     */     
/* 284 */     List<MenuComponent> components = new ArrayList<>();
/*     */     
/* 286 */     for (Feature feature : (AntiAFK.getInstance()).features) {
/* 287 */       lines.add(feature.getName() + ":" + feature.getKey() + ":" + feature.isEnabled() + "\r");
/* 288 */       feature.onMenuAdd(components);
/*     */     } 
/*     */     
/* 291 */     lines.add("[Misc]\r");
/*     */     
/* 293 */     String lastLabel = "";
/* 294 */     for (MenuComponent component : components) {
/* 295 */       if (component instanceof MenuLabel) {
/* 296 */         lastLabel = ((MenuLabel)component).getText();
/*     */         
/*     */         continue;
/*     */       } 
/* 300 */       if (component instanceof MenuCheckbox) {
/* 301 */         MenuCheckbox checkbox = (MenuCheckbox)component;
/* 302 */         lines.add(checkbox.getText() + ":" + checkbox.isChecked() + "\r");
/* 303 */         lastLabel = checkbox.getText() + "-color"; continue;
/* 304 */       }  if (component instanceof MenuComboBox) {
/* 305 */         MenuComboBox combo = (MenuComboBox)component;
/* 306 */         lines.add(lastLabel + ":" + Arrays.toString((Object[])combo.getValues()) + "\r"); continue;
/* 307 */       }  if (component instanceof MenuDropdown) {
/* 308 */         MenuDropdown drop = (MenuDropdown)component;
/* 309 */         lines.add(lastLabel + ":" + drop.getValue() + "\r"); continue;
/* 310 */       }  if (component instanceof MenuSlider) {
/* 311 */         MenuSlider slider = (MenuSlider)component;
/* 312 */         lines.add(lastLabel + ":" + slider.getValue() + "\r"); continue;
/* 313 */       }  if (component instanceof MenuTextField) {
/* 314 */         MenuTextField textBox = (MenuTextField)component;
/* 315 */         lines.add(lastLabel + ":" + textBox.getText() + "\r"); continue;
/* 316 */       }  if (component instanceof MenuColorPicker) {
/* 317 */         MenuColorPicker color = (MenuColorPicker)component;
/* 318 */         String tempLabel = lastLabel;
/*     */         
/* 320 */         if (!tempLabel.endsWith("-color")) {
/* 321 */           tempLabel = tempLabel + "-color";
/*     */         }
/* 323 */         lines.add(tempLabel + ":" + color.getColor().getRGB() + ";" + color.getColor().getAlpha() + "\r");
/*     */       } 
/*     */     } 
/*     */     
/* 327 */     lines.add("color:" + GuiMenu.theColor.getRGB() + "\r");
/* 328 */     lines.add("color-hover:" + GuiMenu.theColorHover.getRGB() + "\r");
/* 329 */     lines.add("background:" + GuiMenu.curBg + "\r");
/*     */     
/* 331 */     lines.add("[Friends]\r");
/*     */     
/* 333 */     for (String friend : (AntiAFK.getInstance()).friends) {
/* 334 */       lines.add(friend + "\r");
/*     */     }
/*     */     
/* 337 */     lines.add("[XRay]\r");
/*     */     
/* 339 */     for (null = (AntiAFK.getInstance()).xray.iterator(); null.hasNext(); ) { int block = ((Integer)null.next()).intValue();
/* 340 */       lines.add(block + "\r"); }
/*     */ 
/*     */     
/* 343 */     lines.add("[Data]\r");
/*     */     
/* 345 */     for (MenuComponent component : components) {
/* 346 */       if (component instanceof MenuDataObject) {
/* 347 */         MenuDataObject obj = (MenuDataObject)component;
/*     */         
/* 349 */         lines.add(obj.getKey() + ":" + obj.getValue() + "\r");
/*     */       } 
/*     */     } 
/*     */     
/* 353 */     return lines.<String>toArray(new String[lines.size()]);
/*     */   }
/*     */   
/*     */   public static void saveConfig(String name, String[] data) {
/* 357 */     if (data == null || data.length == 0) {
/*     */       return;
/*     */     }
/*     */     
/* 361 */     File folder = new File(System.getenv("LOCALAPPDATA") + File.separator + (AntiAFK.getInstance()).name);
/*     */     
/* 363 */     if (!folder.exists()) {
/* 364 */       folder.mkdirs();
/*     */     }
/*     */     
/* 367 */     File cfg = new File(System.getenv("LOCALAPPDATA") + File.separator + (AntiAFK.getInstance()).name + File.separator + name + ".cfg");
/*     */     
/* 369 */     if (!cfg.exists()) {
/*     */       try {
/* 371 */         cfg.createNewFile();
/* 372 */       } catch (IOException e) {
/* 373 */         e.printStackTrace();
/*     */       } 
/*     */     }
/*     */     
/* 377 */     try (FileWriter fw = new FileWriter(cfg)) {
/* 378 */       for (String line : data) {
/* 379 */         fw.write(line);
/*     */       }
/* 381 */     } catch (IOException e) {
/* 382 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isInt(String string) {
/*     */     try {
/* 389 */       Integer.valueOf(string);
/* 390 */       return true;
/* 391 */     } catch (NumberFormatException e) {
/* 392 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean isFloat(String string) {
/*     */     try {
/* 398 */       Float.valueOf(string);
/* 399 */       return true;
/* 400 */     } catch (NumberFormatException e) {
/* 401 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean isBoolean(String string) {
/* 406 */     return (string.equalsIgnoreCase("true") || string.equalsIgnoreCase("false"));
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewi\\utils\FileUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */