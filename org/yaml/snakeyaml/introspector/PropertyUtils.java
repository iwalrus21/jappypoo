/*     */ package org.yaml.snakeyaml.introspector;
/*     */ 
/*     */ import java.beans.FeatureDescriptor;
/*     */ import java.beans.IntrospectionException;
/*     */ import java.beans.Introspector;
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import java.util.logging.Logger;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.util.PlatformFeatureDetector;
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
/*     */ public class PropertyUtils
/*     */ {
/*  39 */   private static final Logger log = Logger.getLogger(PropertyUtils.class.getPackage().getName());
/*     */   
/*  41 */   private final Map<Class<?>, Map<String, Property>> propertiesCache = new HashMap<Class<?>, Map<String, Property>>();
/*  42 */   private final Map<Class<?>, Set<Property>> readableProperties = new HashMap<Class<?>, Set<Property>>();
/*  43 */   private BeanAccess beanAccess = BeanAccess.DEFAULT;
/*     */   
/*     */   private boolean allowReadOnlyProperties = false;
/*     */   
/*     */   private boolean skipMissingProperties = false;
/*     */   
/*     */   public PropertyUtils() {
/*  50 */     this(new PlatformFeatureDetector());
/*     */   }
/*     */   private PlatformFeatureDetector platformFeatureDetector; private boolean transientMethodChecked; private Method isTransientMethod;
/*     */   PropertyUtils(PlatformFeatureDetector platformFeatureDetector) {
/*  54 */     this.platformFeatureDetector = platformFeatureDetector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  60 */     if (platformFeatureDetector.isRunningOnAndroid())
/*  61 */       this.beanAccess = BeanAccess.FIELD; 
/*     */   }
/*     */   
/*     */   protected Map<String, Property> getPropertiesMap(Class<?> type, BeanAccess bAccess) {
/*     */     Class<?> c;
/*  66 */     if (this.propertiesCache.containsKey(type)) {
/*  67 */       return this.propertiesCache.get(type);
/*     */     }
/*     */     
/*  70 */     Map<String, Property> properties = new LinkedHashMap<String, Property>();
/*  71 */     boolean inaccessableFieldsExist = false;
/*  72 */     switch (bAccess) {
/*     */       case FIELD:
/*  74 */         for (c = type; c != null; c = c.getSuperclass()) {
/*  75 */           for (Field field : c.getDeclaredFields()) {
/*  76 */             int modifiers = field.getModifiers();
/*  77 */             if (!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers) && 
/*  78 */               !properties.containsKey(field.getName())) {
/*  79 */               properties.put(field.getName(), new FieldProperty(field));
/*     */             }
/*     */           } 
/*     */         } 
/*     */         break;
/*     */       
/*     */       default:
/*     */         try {
/*  87 */           for (PropertyDescriptor property : Introspector.getBeanInfo(type)
/*  88 */             .getPropertyDescriptors()) {
/*  89 */             Method readMethod = property.getReadMethod();
/*  90 */             if ((readMethod == null || !readMethod.getName().equals("getClass")) && 
/*  91 */               !isTransient(property)) {
/*  92 */               properties.put(property.getName(), new MethodProperty(property));
/*     */             }
/*     */           } 
/*  95 */         } catch (IntrospectionException e) {
/*  96 */           throw new YAMLException(e);
/*     */         } 
/*     */ 
/*     */         
/* 100 */         for (c = type; c != null; c = c.getSuperclass()) {
/* 101 */           for (Field field : c.getDeclaredFields()) {
/* 102 */             int modifiers = field.getModifiers();
/* 103 */             if (!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers)) {
/* 104 */               if (Modifier.isPublic(modifiers)) {
/* 105 */                 properties.put(field.getName(), new FieldProperty(field));
/*     */               } else {
/* 107 */                 inaccessableFieldsExist = true;
/*     */               } 
/*     */             }
/*     */           } 
/*     */         } 
/*     */         break;
/*     */     } 
/* 114 */     if (properties.isEmpty() && inaccessableFieldsExist) {
/* 115 */       throw new YAMLException("No JavaBean properties found in " + type.getName());
/*     */     }
/* 117 */     this.propertiesCache.put(type, properties);
/* 118 */     return properties;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isTransient(FeatureDescriptor fd) {
/* 125 */     if (!this.transientMethodChecked) {
/* 126 */       this.transientMethodChecked = true;
/*     */       try {
/* 128 */         this.isTransientMethod = FeatureDescriptor.class.getDeclaredMethod("isTransient", new Class[0]);
/* 129 */         this.isTransientMethod.setAccessible(true);
/* 130 */       } catch (NoSuchMethodException e) {
/* 131 */         log.fine("NoSuchMethod: FeatureDescriptor.isTransient(). Don't check it anymore.");
/* 132 */       } catch (SecurityException e) {
/* 133 */         e.printStackTrace();
/* 134 */         this.isTransientMethod = null;
/*     */       } 
/*     */     } 
/*     */     
/* 138 */     if (this.isTransientMethod != null) {
/*     */       try {
/* 140 */         return Boolean.TRUE.equals(this.isTransientMethod.invoke(fd, new Object[0]));
/* 141 */       } catch (IllegalAccessException e) {
/* 142 */         e.printStackTrace();
/* 143 */       } catch (IllegalArgumentException e) {
/* 144 */         e.printStackTrace();
/* 145 */       } catch (InvocationTargetException e) {
/* 146 */         e.printStackTrace();
/*     */       } 
/* 148 */       this.isTransientMethod = null;
/*     */     } 
/* 150 */     return false;
/*     */   }
/*     */   
/*     */   public Set<Property> getProperties(Class<? extends Object> type) {
/* 154 */     return getProperties(type, this.beanAccess);
/*     */   }
/*     */   
/*     */   public Set<Property> getProperties(Class<? extends Object> type, BeanAccess bAccess) {
/* 158 */     if (this.readableProperties.containsKey(type)) {
/* 159 */       return this.readableProperties.get(type);
/*     */     }
/* 161 */     Set<Property> properties = createPropertySet(type, bAccess);
/* 162 */     this.readableProperties.put(type, properties);
/* 163 */     return properties;
/*     */   }
/*     */   
/*     */   protected Set<Property> createPropertySet(Class<? extends Object> type, BeanAccess bAccess) {
/* 167 */     Set<Property> properties = new TreeSet<Property>();
/* 168 */     Collection<Property> props = getPropertiesMap(type, bAccess).values();
/* 169 */     for (Property property : props) {
/* 170 */       if (property.isReadable() && (this.allowReadOnlyProperties || property.isWritable())) {
/* 171 */         properties.add(property);
/*     */       }
/*     */     } 
/* 174 */     return properties;
/*     */   }
/*     */   
/*     */   public Property getProperty(Class<? extends Object> type, String name) {
/* 178 */     return getProperty(type, name, this.beanAccess);
/*     */   }
/*     */   
/*     */   public Property getProperty(Class<? extends Object> type, String name, BeanAccess bAccess) {
/* 182 */     Map<String, Property> properties = getPropertiesMap(type, bAccess);
/* 183 */     Property property = properties.get(name);
/* 184 */     if (property == null && this.skipMissingProperties) {
/* 185 */       property = new MissingProperty(name);
/*     */     }
/* 187 */     if (property == null) {
/* 188 */       throw new YAMLException("Unable to find property '" + name + "' on class: " + type
/* 189 */           .getName());
/*     */     }
/* 191 */     return property;
/*     */   }
/*     */   
/*     */   public void setBeanAccess(BeanAccess beanAccess) {
/* 195 */     if (this.platformFeatureDetector.isRunningOnAndroid() && beanAccess != BeanAccess.FIELD) {
/* 196 */       throw new IllegalArgumentException("JVM is Android - only BeanAccess.FIELD is available");
/*     */     }
/*     */     
/* 199 */     if (this.beanAccess != beanAccess) {
/* 200 */       this.beanAccess = beanAccess;
/* 201 */       this.propertiesCache.clear();
/* 202 */       this.readableProperties.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setAllowReadOnlyProperties(boolean allowReadOnlyProperties) {
/* 207 */     if (this.allowReadOnlyProperties != allowReadOnlyProperties) {
/* 208 */       this.allowReadOnlyProperties = allowReadOnlyProperties;
/* 209 */       this.readableProperties.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isAllowReadOnlyProperties() {
/* 214 */     return this.allowReadOnlyProperties;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSkipMissingProperties(boolean skipMissingProperties) {
/* 225 */     if (this.skipMissingProperties != skipMissingProperties) {
/* 226 */       this.skipMissingProperties = skipMissingProperties;
/* 227 */       this.readableProperties.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isSkipMissingProperties() {
/* 232 */     return this.skipMissingProperties;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\yaml\snakeyaml\introspector\PropertyUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */