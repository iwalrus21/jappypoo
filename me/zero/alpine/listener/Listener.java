/*    */ package me.zero.alpine.listener;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import net.jodah.typetools.TypeResolver;
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
/*    */ public final class Listener<T>
/*    */   implements EventHook<T>
/*    */ {
/*    */   private final Class<T> target;
/*    */   private final EventHook<T> hook;
/*    */   private final Predicate<T>[] filters;
/*    */   private final byte priority;
/*    */   
/*    */   @SafeVarargs
/*    */   public Listener(EventHook<T> hook, Predicate<T>... filters) {
/* 42 */     this(hook, (byte)3, filters);
/*    */   }
/*    */ 
/*    */   
/*    */   @SafeVarargs
/*    */   public Listener(EventHook<T> hook, byte priority, Predicate<T>... filters) {
/* 48 */     this.hook = hook;
/* 49 */     this.priority = priority;
/* 50 */     this.target = TypeResolver.resolveRawArgument(EventHook.class, hook.getClass());
/* 51 */     this.filters = filters;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final Class<T> getTarget() {
/* 61 */     return this.target;
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
/*    */   public final byte getPriority() {
/* 75 */     return this.priority;
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
/*    */   public final void invoke(T event) {
/* 88 */     if (this.filters.length > 0)
/* 89 */       for (Predicate<T> filter : this.filters) {
/* 90 */         if (!filter.test(event))
/*    */           return; 
/*    */       }  
/* 93 */     this.hook.invoke(event);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\zero\alpine\listener\Listener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */