/*     */ package org.yaml.snakeyaml;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.introspector.BeanAccess;
/*     */ import org.yaml.snakeyaml.introspector.Property;
/*     */ import org.yaml.snakeyaml.introspector.PropertySubstitute;
/*     */ import org.yaml.snakeyaml.introspector.PropertyUtils;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
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
/*     */ public class TypeDescription
/*     */ {
/*     */   private final Class<? extends Object> type;
/*     */   private Class<?> impl;
/*     */   private Tag tag;
/*     */   private transient Set<Property> dumpProperties;
/*     */   private transient PropertyUtils propertyUtils;
/*     */   private transient boolean delegatesChecked;
/*  57 */   private Map<String, PropertySubstitute> properties = Collections.emptyMap();
/*     */   
/*  59 */   protected Set<String> excludes = Collections.emptySet();
/*  60 */   protected String[] includes = null;
/*     */   protected BeanAccess beanAccess;
/*     */   
/*     */   public TypeDescription(Class<? extends Object> clazz, Tag tag) {
/*  64 */     this(clazz, tag, null);
/*     */   }
/*     */   
/*     */   public TypeDescription(Class<? extends Object> clazz, Tag tag, Class<?> impl) {
/*  68 */     this.type = clazz;
/*  69 */     this.tag = tag;
/*  70 */     this.impl = impl;
/*  71 */     this.beanAccess = null;
/*     */   }
/*     */   
/*     */   public TypeDescription(Class<? extends Object> clazz, String tag) {
/*  75 */     this(clazz, new Tag(tag), null);
/*     */   }
/*     */   
/*     */   public TypeDescription(Class<? extends Object> clazz) {
/*  79 */     this(clazz, (Tag)null, null);
/*     */   }
/*     */   
/*     */   public TypeDescription(Class<? extends Object> clazz, Class<?> impl) {
/*  83 */     this(clazz, null, impl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Tag getTag() {
/*  93 */     return this.tag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTag(Tag tag) {
/* 103 */     this.tag = tag;
/*     */   }
/*     */   
/*     */   public void setTag(String tag) {
/* 107 */     setTag(new Tag(tag));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<? extends Object> getType() {
/* 116 */     return this.type;
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
/*     */   @Deprecated
/*     */   public void putListPropertyType(String property, Class<? extends Object> type) {
/* 129 */     addPropertyParameters(property, new Class[] { type });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Class<? extends Object> getListPropertyType(String property) {
/* 141 */     if (this.properties.containsKey(property)) {
/* 142 */       Class<?>[] typeArguments = ((PropertySubstitute)this.properties.get(property)).getActualTypeArguments();
/* 143 */       if (typeArguments != null && typeArguments.length > 0) {
/* 144 */         return (Class)typeArguments[0];
/*     */       }
/*     */     } 
/* 147 */     return null;
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
/*     */   @Deprecated
/*     */   public void putMapPropertyType(String property, Class<? extends Object> key, Class<? extends Object> value) {
/* 163 */     addPropertyParameters(property, new Class[] { key, value });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Class<? extends Object> getMapKeyType(String property) {
/* 175 */     if (this.properties.containsKey(property)) {
/* 176 */       Class<?>[] typeArguments = ((PropertySubstitute)this.properties.get(property)).getActualTypeArguments();
/* 177 */       if (typeArguments != null && typeArguments.length > 0) {
/* 178 */         return (Class)typeArguments[0];
/*     */       }
/*     */     } 
/* 181 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Class<? extends Object> getMapValueType(String property) {
/* 193 */     if (this.properties.containsKey(property)) {
/* 194 */       Class<?>[] typeArguments = ((PropertySubstitute)this.properties.get(property)).getActualTypeArguments();
/* 195 */       if (typeArguments != null && typeArguments.length > 1) {
/* 196 */         return (Class)typeArguments[1];
/*     */       }
/*     */     } 
/* 199 */     return null;
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
/*     */   public void addPropertyParameters(String pName, Class<?>... classes) {
/* 212 */     if (!this.properties.containsKey(pName)) {
/* 213 */       substituteProperty(pName, null, null, null, classes);
/*     */     } else {
/* 215 */       PropertySubstitute pr = this.properties.get(pName);
/* 216 */       pr.setActualTypeArguments(classes);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 223 */     return "TypeDescription for " + getType() + " (tag='" + getTag() + "')";
/*     */   }
/*     */   
/*     */   private void checkDelegates() {
/* 227 */     Collection<PropertySubstitute> values = this.properties.values();
/* 228 */     for (PropertySubstitute p : values) {
/*     */       try {
/* 230 */         p.setDelegate(discoverProperty(p.getName()));
/* 231 */       } catch (YAMLException yAMLException) {}
/*     */     } 
/*     */     
/* 234 */     this.delegatesChecked = true;
/*     */   }
/*     */   
/*     */   private Property discoverProperty(String name) {
/* 238 */     if (this.propertyUtils != null) {
/* 239 */       if (this.beanAccess == null) {
/* 240 */         return this.propertyUtils.getProperty(this.type, name);
/*     */       }
/* 242 */       return this.propertyUtils.getProperty(this.type, name, this.beanAccess);
/*     */     } 
/* 244 */     return null;
/*     */   }
/*     */   
/*     */   public Property getProperty(String name) {
/* 248 */     if (!this.delegatesChecked) {
/* 249 */       checkDelegates();
/*     */     }
/* 251 */     return this.properties.containsKey(name) ? (Property)this.properties.get(name) : discoverProperty(name);
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
/*     */   public void substituteProperty(String pName, Class<?> pType, String getter, String setter, Class<?>... argParams) {
/* 270 */     substituteProperty(new PropertySubstitute(pName, pType, getter, setter, argParams));
/*     */   }
/*     */   
/*     */   public void substituteProperty(PropertySubstitute substitute) {
/* 274 */     if (Collections.EMPTY_MAP == this.properties) {
/* 275 */       this.properties = new LinkedHashMap<String, PropertySubstitute>();
/*     */     }
/* 277 */     substitute.setTargetType(this.type);
/* 278 */     this.properties.put(substitute.getName(), substitute);
/*     */   }
/*     */   
/*     */   public void setPropertyUtils(PropertyUtils propertyUtils) {
/* 282 */     this.propertyUtils = propertyUtils;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIncludes(String... propNames) {
/* 287 */     this.includes = (propNames != null && propNames.length > 0) ? propNames : null;
/*     */   }
/*     */   
/*     */   public void setExcludes(String... propNames) {
/* 291 */     if (propNames != null && propNames.length > 0) {
/* 292 */       this.excludes = new HashSet<String>();
/* 293 */       for (String name : propNames) {
/* 294 */         this.excludes.add(name);
/*     */       }
/*     */     } else {
/* 297 */       this.excludes = Collections.emptySet();
/*     */     } 
/*     */   }
/*     */   
/*     */   public Set<Property> getProperties() {
/* 302 */     if (this.dumpProperties != null) {
/* 303 */       return this.dumpProperties;
/*     */     }
/*     */     
/* 306 */     if (this.propertyUtils != null) {
/* 307 */       if (this.includes != null) {
/* 308 */         this.dumpProperties = new LinkedHashSet<Property>();
/* 309 */         for (String propertyName : this.includes) {
/* 310 */           if (!this.excludes.contains(propertyName)) {
/* 311 */             this.dumpProperties.add(getProperty(propertyName));
/*     */           }
/*     */         } 
/* 314 */         return this.dumpProperties;
/*     */       } 
/*     */ 
/*     */       
/* 318 */       Set<Property> readableProps = (this.beanAccess == null) ? this.propertyUtils.getProperties(this.type) : this.propertyUtils.getProperties(this.type, this.beanAccess);
/*     */       
/* 320 */       if (this.properties.isEmpty()) {
/* 321 */         if (this.excludes.isEmpty()) {
/* 322 */           return this.dumpProperties = readableProps;
/*     */         }
/* 324 */         this.dumpProperties = new LinkedHashSet<Property>();
/* 325 */         for (Property property : readableProps) {
/* 326 */           if (!this.excludes.contains(property.getName())) {
/* 327 */             this.dumpProperties.add(property);
/*     */           }
/*     */         } 
/* 330 */         return this.dumpProperties;
/*     */       } 
/*     */       
/* 333 */       if (!this.delegatesChecked) {
/* 334 */         checkDelegates();
/*     */       }
/*     */       
/* 337 */       this.dumpProperties = new LinkedHashSet<Property>();
/*     */       
/* 339 */       for (Property property : this.properties.values()) {
/* 340 */         if (!this.excludes.contains(property.getName()) && property.isReadable()) {
/* 341 */           this.dumpProperties.add(property);
/*     */         }
/*     */       } 
/*     */       
/* 345 */       for (Property property : readableProps) {
/* 346 */         if (!this.excludes.contains(property.getName())) {
/* 347 */           this.dumpProperties.add(property);
/*     */         }
/*     */       } 
/*     */       
/* 351 */       return this.dumpProperties;
/*     */     } 
/* 353 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setupPropertyType(String key, Node valueNode) {
/* 361 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setProperty(Object targetBean, String propertyName, Object value) throws Exception {
/* 366 */     return false;
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
/*     */   public Object newInstance(Node node) {
/* 379 */     if (this.impl != null) {
/*     */       try {
/* 381 */         Constructor<?> c = this.impl.getDeclaredConstructor(new Class[0]);
/* 382 */         c.setAccessible(true);
/* 383 */         return c.newInstance(new Object[0]);
/* 384 */       } catch (Exception e) {
/* 385 */         e.printStackTrace();
/* 386 */         this.impl = null;
/*     */       } 
/*     */     }
/* 389 */     return null;
/*     */   }
/*     */   
/*     */   public Object newInstance(String propertyName, Node node) {
/* 393 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object finalizeConstruction(Object obj) {
/* 402 */     return obj;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\yaml\snakeyaml\TypeDescription.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */