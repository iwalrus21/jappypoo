package me.zero.alpine;

public interface EventBus {
  void subscribe(Object paramObject);
  
  void subscribe(Object... paramVarArgs);
  
  void subscribe(Iterable<Object> paramIterable);
  
  void unsubscribe(Object paramObject);
  
  void unsubscribe(Object... paramVarArgs);
  
  void unsubscribe(Iterable<Object> paramIterable);
  
  void post(Object paramObject);
  
  void attach(EventBus paramEventBus);
  
  void detach(EventBus paramEventBus);
}


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\zero\alpine\EventBus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */