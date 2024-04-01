/*     */ package org.reflections.util;
/*     */ 
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.regex.Pattern;
/*     */ import org.reflections.ReflectionsException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FilterBuilder
/*     */   implements Predicate<String>
/*     */ {
/*     */   private final List<Predicate<String>> chain;
/*     */   
/*     */   public FilterBuilder() {
/*  24 */     this.chain = Lists.newArrayList(); } private FilterBuilder(Iterable<Predicate<String>> filters) {
/*  25 */     this.chain = Lists.newArrayList(filters);
/*     */   }
/*     */   public FilterBuilder include(String regex) {
/*  28 */     return add(new Include(regex));
/*     */   }
/*     */   public FilterBuilder exclude(String regex) {
/*  31 */     add(new Exclude(regex)); return this;
/*     */   }
/*     */   public FilterBuilder add(Predicate<String> filter) {
/*  34 */     this.chain.add(filter); return this;
/*     */   }
/*     */   public FilterBuilder includePackage(Class<?> aClass) {
/*  37 */     return add(new Include(packageNameRegex(aClass)));
/*     */   }
/*     */   public FilterBuilder excludePackage(Class<?> aClass) {
/*  40 */     return add(new Exclude(packageNameRegex(aClass)));
/*     */   }
/*     */   
/*     */   public FilterBuilder includePackage(String... prefixes) {
/*  44 */     for (String prefix : prefixes) {
/*  45 */       add(new Include(prefix(prefix)));
/*     */     }
/*  47 */     return this;
/*     */   }
/*     */   
/*     */   public FilterBuilder excludePackage(String prefix) {
/*  51 */     return add(new Exclude(prefix(prefix)));
/*     */   } private static String packageNameRegex(Class<?> aClass) {
/*  53 */     return prefix(aClass.getPackage().getName() + ".");
/*     */   } public static String prefix(String qualifiedName) {
/*  55 */     return qualifiedName.replace(".", "\\.") + ".*";
/*     */   } public String toString() {
/*  57 */     return Joiner.on(", ").join(this.chain);
/*     */   }
/*     */   public boolean apply(String regex) {
/*  60 */     boolean accept = (this.chain == null || this.chain.isEmpty() || this.chain.get(0) instanceof Exclude);
/*     */     
/*  62 */     if (this.chain != null)
/*  63 */       for (Predicate<String> filter : this.chain) {
/*  64 */         if ((accept && filter instanceof Include) || (
/*  65 */           !accept && filter instanceof Exclude))
/*  66 */           continue;  accept = filter.apply(regex);
/*  67 */         if (!accept && filter instanceof Exclude)
/*     */           break; 
/*     */       }  
/*  70 */     return accept;
/*     */   }
/*     */   public static abstract class Matcher implements Predicate<String> { final Pattern pattern;
/*     */     
/*     */     public Matcher(String regex) {
/*  75 */       this.pattern = Pattern.compile(regex);
/*     */     } public abstract boolean apply(String param1String); public String toString() {
/*  77 */       return this.pattern.pattern();
/*     */     } }
/*     */   
/*     */   public static class Include extends Matcher { public Include(String patternString) {
/*  81 */       super(patternString); }
/*  82 */     public boolean apply(String regex) { return this.pattern.matcher(regex).matches(); } public String toString() {
/*  83 */       return "+" + super.toString();
/*     */     } }
/*     */ 
/*     */   
/*  87 */   public static class Exclude extends Matcher { public Exclude(String patternString) { super(patternString); }
/*  88 */     public boolean apply(String regex) { return !this.pattern.matcher(regex).matches(); } public String toString() {
/*  89 */       return "-" + super.toString();
/*     */     } }
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
/*     */   public static FilterBuilder parse(String includeExcludeString) {
/* 105 */     List<Predicate<String>> filters = new ArrayList<>();
/*     */     
/* 107 */     if (!Utils.isEmpty(includeExcludeString)) {
/* 108 */       for (String string : includeExcludeString.split(",")) {
/* 109 */         Predicate<String> filter; String trimmed = string.trim();
/* 110 */         char prefix = trimmed.charAt(0);
/* 111 */         String pattern = trimmed.substring(1);
/*     */ 
/*     */         
/* 114 */         switch (prefix) {
/*     */           case '+':
/* 116 */             filter = new Include(pattern);
/*     */             break;
/*     */           case '-':
/* 119 */             filter = new Exclude(pattern);
/*     */             break;
/*     */           default:
/* 122 */             throw new ReflectionsException("includeExclude should start with either + or -");
/*     */         } 
/*     */         
/* 125 */         filters.add(filter);
/*     */       } 
/*     */       
/* 128 */       return new FilterBuilder(filters);
/*     */     } 
/* 130 */     return new FilterBuilder();
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
/*     */   public static FilterBuilder parsePackages(String includeExcludeString) {
/* 146 */     List<Predicate<String>> filters = new ArrayList<>();
/*     */     
/* 148 */     if (!Utils.isEmpty(includeExcludeString)) {
/* 149 */       for (String string : includeExcludeString.split(",")) {
/* 150 */         Predicate<String> filter; String trimmed = string.trim();
/* 151 */         char prefix = trimmed.charAt(0);
/* 152 */         String pattern = trimmed.substring(1);
/* 153 */         if (!pattern.endsWith(".")) {
/* 154 */           pattern = pattern + ".";
/*     */         }
/* 156 */         pattern = prefix(pattern);
/*     */ 
/*     */         
/* 159 */         switch (prefix) {
/*     */           case '+':
/* 161 */             filter = new Include(pattern);
/*     */             break;
/*     */           case '-':
/* 164 */             filter = new Exclude(pattern);
/*     */             break;
/*     */           default:
/* 167 */             throw new ReflectionsException("includeExclude should start with either + or -");
/*     */         } 
/*     */         
/* 170 */         filters.add(filter);
/*     */       } 
/*     */       
/* 173 */       return new FilterBuilder(filters);
/*     */     } 
/* 175 */     return new FilterBuilder();
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\reflection\\util\FilterBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */