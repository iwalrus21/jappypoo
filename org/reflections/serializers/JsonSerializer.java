/*    */ package org.reflections.serializers;
/*    */ 
/*    */ import com.google.common.base.Supplier;
/*    */ import com.google.common.collect.Multimap;
/*    */ import com.google.common.collect.Multimaps;
/*    */ import com.google.common.collect.SetMultimap;
/*    */ import com.google.common.collect.Sets;
/*    */ import com.google.common.io.Files;
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.GsonBuilder;
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonDeserializer;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParseException;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.InputStreamReader;
/*    */ import java.lang.reflect.Type;
/*    */ import java.nio.charset.Charset;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import org.reflections.Reflections;
/*    */ import org.reflections.util.Utils;
/*    */ 
/*    */ public class JsonSerializer implements Serializer {
/*    */   private Gson gson;
/*    */   
/*    */   public Reflections read(InputStream inputStream) {
/* 33 */     return (Reflections)getGson().fromJson(new InputStreamReader(inputStream), Reflections.class);
/*    */   }
/*    */   
/*    */   public File save(Reflections reflections, String filename) {
/*    */     try {
/* 38 */       File file = Utils.prepareFile(filename);
/* 39 */       Files.write(toString(reflections), file, Charset.defaultCharset());
/* 40 */       return file;
/* 41 */     } catch (IOException e) {
/* 42 */       throw new RuntimeException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public String toString(Reflections reflections) {
/* 47 */     return getGson().toJson(reflections);
/*    */   }
/*    */   
/*    */   private Gson getGson() {
/* 51 */     if (this.gson == null) {
/* 52 */       this
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
/* 74 */         .gson = (new GsonBuilder()).registerTypeAdapter(Multimap.class, new com.google.gson.JsonSerializer<Multimap>() { public JsonElement serialize(Multimap multimap, Type type, JsonSerializationContext jsonSerializationContext) { return jsonSerializationContext.serialize(multimap.asMap()); } }).registerTypeAdapter(Multimap.class, new JsonDeserializer<Multimap>() { public Multimap deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException { SetMultimap<String, String> map = Multimaps.newSetMultimap(new HashMap<>(), new Supplier<Set<String>>() { public Set<String> get() { return Sets.newHashSet(); } }); for (Map.Entry<String, JsonElement> entry : (Iterable<Map.Entry<String, JsonElement>>)((JsonObject)jsonElement).entrySet()) { for (JsonElement element : entry.getValue()) map.get(entry.getKey()).add(element.getAsString());  }  return (Multimap)map; } }).setPrettyPrinting().create();
/*    */     }
/*    */     
/* 77 */     return this.gson;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\reflections\serializers\JsonSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */