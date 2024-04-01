/*     */ package me.zero.alpine;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.Collectors;
/*     */ import me.zero.alpine.listener.EventHandler;
/*     */ import me.zero.alpine.listener.Listener;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EventManager
/*     */   implements EventBus
/*     */ {
/*  23 */   private final Map<Object, List<Listener>> SUBSCRIPTION_CACHE = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  28 */   private final Map<Class<?>, List<Listener>> SUBSCRIPTION_MAP = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  33 */   private final List<EventBus> ATTACHED_BUSES = new ArrayList<>();
/*     */ 
/*     */   
/*     */   public void subscribe(Object object) {
/*  37 */     List<Listener> listeners = this.SUBSCRIPTION_CACHE.computeIfAbsent(object, o -> (List)Arrays.<Field>stream(o.getClass().getDeclaredFields()).filter(EventManager::isValidField).map(()).filter(Objects::nonNull).collect(Collectors.toList()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  44 */     listeners.forEach(this::subscribe);
/*     */ 
/*     */     
/*  47 */     if (!this.ATTACHED_BUSES.isEmpty()) {
/*  48 */       this.ATTACHED_BUSES.forEach(bus -> bus.subscribe(object));
/*     */     }
/*     */   }
/*     */   
/*     */   public void subscribe(Object... objects) {
/*  53 */     Arrays.<Object>stream(objects).forEach(this::subscribe);
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribe(Iterable<Object> objects) {
/*  58 */     objects.forEach(this::subscribe);
/*     */   }
/*     */ 
/*     */   
/*     */   public void unsubscribe(Object object) {
/*  63 */     List<Listener> objectListeners = this.SUBSCRIPTION_CACHE.get(object);
/*  64 */     if (objectListeners == null) {
/*     */       return;
/*     */     }
/*  67 */     this.SUBSCRIPTION_MAP.values().forEach(listeners -> {
/*     */           Objects.requireNonNull(objectListeners); listeners.removeIf(objectListeners::contains);
/*     */         });
/*  70 */     if (!this.ATTACHED_BUSES.isEmpty()) {
/*  71 */       this.ATTACHED_BUSES.forEach(bus -> bus.unsubscribe(object));
/*     */     }
/*     */   }
/*     */   
/*     */   public void unsubscribe(Object... objects) {
/*  76 */     Arrays.<Object>stream(objects).forEach(this::unsubscribe);
/*     */   }
/*     */ 
/*     */   
/*     */   public void unsubscribe(Iterable<Object> objects) {
/*  81 */     objects.forEach(this::unsubscribe);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void post(Object event) {
/*  87 */     List<Listener> listeners = this.SUBSCRIPTION_MAP.get(event.getClass());
/*  88 */     if (listeners != null) {
/*  89 */       listeners.forEach(listener -> listener.invoke(event));
/*     */     }
/*     */     
/*  92 */     if (!this.ATTACHED_BUSES.isEmpty()) {
/*  93 */       this.ATTACHED_BUSES.forEach(bus -> bus.post(event));
/*     */     }
/*     */   }
/*     */   
/*     */   public void attach(EventBus bus) {
/*  98 */     if (!this.ATTACHED_BUSES.contains(bus)) {
/*  99 */       this.ATTACHED_BUSES.add(bus);
/*     */     }
/*     */   }
/*     */   
/*     */   public void detach(EventBus bus) {
/* 104 */     if (this.ATTACHED_BUSES.contains(bus)) {
/* 105 */       this.ATTACHED_BUSES.remove(bus);
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
/*     */   private static boolean isValidField(Field field) {
/* 119 */     return (field.isAnnotationPresent((Class)EventHandler.class) && Listener.class.isAssignableFrom(field.getType()));
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
/*     */   private static Listener asListener(Object object, Field field) {
/*     */     try {
/* 134 */       boolean accessible = field.isAccessible();
/* 135 */       field.setAccessible(true);
/* 136 */       Listener listener = (Listener)field.get(object);
/* 137 */       field.setAccessible(accessible);
/*     */       
/* 139 */       if (listener == null) {
/* 140 */         return null;
/*     */       }
/* 142 */       if (listener.getPriority() > 5 || listener.getPriority() < 1) {
/* 143 */         throw new RuntimeException("Event Priority out of bounds! %s");
/*     */       }
/* 145 */       return listener;
/* 146 */     } catch (IllegalAccessException e) {
/* 147 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void subscribe(Listener listener) {
/* 157 */     List<Listener> listeners = this.SUBSCRIPTION_MAP.computeIfAbsent(listener.getTarget(), target -> new ArrayList());
/*     */     
/* 159 */     int index = 0;
/* 160 */     for (; index < listeners.size() && 
/* 161 */       listener.getPriority() >= ((Listener)listeners.get(index)).getPriority(); index++);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 166 */     listeners.add(index, listener);
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\zero\alpine\EventManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */