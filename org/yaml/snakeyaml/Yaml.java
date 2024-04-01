/*     */ package org.yaml.snakeyaml;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.regex.Pattern;
/*     */ import org.yaml.snakeyaml.composer.Composer;
/*     */ import org.yaml.snakeyaml.constructor.BaseConstructor;
/*     */ import org.yaml.snakeyaml.constructor.Constructor;
/*     */ import org.yaml.snakeyaml.emitter.Emitable;
/*     */ import org.yaml.snakeyaml.emitter.Emitter;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.events.Event;
/*     */ import org.yaml.snakeyaml.introspector.BeanAccess;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
/*     */ import org.yaml.snakeyaml.parser.Parser;
/*     */ import org.yaml.snakeyaml.parser.ParserImpl;
/*     */ import org.yaml.snakeyaml.reader.StreamReader;
/*     */ import org.yaml.snakeyaml.reader.UnicodeReader;
/*     */ import org.yaml.snakeyaml.representer.Representer;
/*     */ import org.yaml.snakeyaml.resolver.Resolver;
/*     */ import org.yaml.snakeyaml.serializer.Serializer;
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
/*     */ public class Yaml
/*     */ {
/*     */   protected final Resolver resolver;
/*     */   private String name;
/*     */   protected BaseConstructor constructor;
/*     */   protected Representer representer;
/*     */   protected DumperOptions dumperOptions;
/*     */   protected LoaderOptions loadingConfig;
/*     */   
/*     */   public Yaml() {
/*  64 */     this((BaseConstructor)new Constructor(), new Representer(), new DumperOptions(), new LoaderOptions(), new Resolver());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Yaml(DumperOptions dumperOptions) {
/*  75 */     this((BaseConstructor)new Constructor(), new Representer(), dumperOptions);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Yaml(LoaderOptions loadingConfig) {
/*  85 */     this((BaseConstructor)new Constructor(), new Representer(), new DumperOptions(), loadingConfig);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Yaml(Representer representer) {
/*  96 */     this((BaseConstructor)new Constructor(), representer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Yaml(BaseConstructor constructor) {
/* 107 */     this(constructor, new Representer());
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
/*     */   public Yaml(BaseConstructor constructor, Representer representer) {
/* 120 */     this(constructor, representer, initDumperOptions(representer));
/*     */   }
/*     */   
/*     */   private static DumperOptions initDumperOptions(Representer representer) {
/* 124 */     DumperOptions dumperOptions = new DumperOptions();
/* 125 */     dumperOptions.setDefaultFlowStyle(representer.getDefaultFlowStyle());
/* 126 */     dumperOptions.setDefaultScalarStyle(representer.getDefaultScalarStyle());
/* 127 */     dumperOptions.setAllowReadOnlyProperties(representer.getPropertyUtils().isAllowReadOnlyProperties());
/* 128 */     dumperOptions.setTimeZone(representer.getTimeZone());
/* 129 */     return dumperOptions;
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
/*     */   public Yaml(Representer representer, DumperOptions dumperOptions) {
/* 142 */     this((BaseConstructor)new Constructor(), representer, dumperOptions, new LoaderOptions(), new Resolver());
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
/*     */   public Yaml(BaseConstructor constructor, Representer representer, DumperOptions dumperOptions) {
/* 157 */     this(constructor, representer, dumperOptions, new LoaderOptions(), new Resolver());
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
/*     */   public Yaml(BaseConstructor constructor, Representer representer, DumperOptions dumperOptions, LoaderOptions loadingConfig) {
/* 175 */     this(constructor, representer, dumperOptions, loadingConfig, new Resolver());
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
/*     */   public Yaml(BaseConstructor constructor, Representer representer, DumperOptions dumperOptions, Resolver resolver) {
/* 193 */     this(constructor, representer, dumperOptions, new LoaderOptions(), resolver);
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
/*     */   
/*     */   public Yaml(BaseConstructor constructor, Representer representer, DumperOptions dumperOptions, LoaderOptions loadingConfig, Resolver resolver) {
/* 213 */     if (!constructor.isExplicitPropertyUtils()) {
/* 214 */       constructor.setPropertyUtils(representer.getPropertyUtils());
/* 215 */     } else if (!representer.isExplicitPropertyUtils()) {
/* 216 */       representer.setPropertyUtils(constructor.getPropertyUtils());
/*     */     } 
/* 218 */     this.constructor = constructor;
/* 219 */     this.constructor.setAllowDuplicateKeys(loadingConfig.isAllowDuplicateKeys());
/* 220 */     if (dumperOptions.getIndent() <= dumperOptions.getIndicatorIndent()) {
/* 221 */       throw new YAMLException("Indicator indent must be smaller then indent.");
/*     */     }
/* 223 */     representer.setDefaultFlowStyle(dumperOptions.getDefaultFlowStyle());
/* 224 */     representer.setDefaultScalarStyle(dumperOptions.getDefaultScalarStyle());
/* 225 */     representer.getPropertyUtils()
/* 226 */       .setAllowReadOnlyProperties(dumperOptions.isAllowReadOnlyProperties());
/* 227 */     representer.setTimeZone(dumperOptions.getTimeZone());
/* 228 */     this.representer = representer;
/* 229 */     this.dumperOptions = dumperOptions;
/* 230 */     this.loadingConfig = loadingConfig;
/* 231 */     this.resolver = resolver;
/* 232 */     this.name = "Yaml:" + System.identityHashCode(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String dump(Object data) {
/* 243 */     List<Object> list = new ArrayList(1);
/* 244 */     list.add(data);
/* 245 */     return dumpAll(list.iterator());
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
/*     */   public Node represent(Object data) {
/* 258 */     return this.representer.represent(data);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String dumpAll(Iterator<? extends Object> data) {
/* 269 */     StringWriter buffer = new StringWriter();
/* 270 */     dumpAll(data, buffer, null);
/* 271 */     return buffer.toString();
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
/*     */   public void dump(Object data, Writer output) {
/* 283 */     List<Object> list = new ArrayList(1);
/* 284 */     list.add(data);
/* 285 */     dumpAll(list.iterator(), output, null);
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
/*     */   public void dumpAll(Iterator<? extends Object> data, Writer output) {
/* 297 */     dumpAll(data, output, null);
/*     */   }
/*     */   
/*     */   private void dumpAll(Iterator<? extends Object> data, Writer output, Tag rootTag) {
/* 301 */     Serializer serializer = new Serializer((Emitable)new Emitter(output, this.dumperOptions), this.resolver, this.dumperOptions, rootTag);
/*     */     
/*     */     try {
/* 304 */       serializer.open();
/* 305 */       while (data.hasNext()) {
/* 306 */         Node node = this.representer.represent(data.next());
/* 307 */         serializer.serialize(node);
/*     */       } 
/* 309 */       serializer.close();
/* 310 */     } catch (IOException e) {
/* 311 */       throw new YAMLException(e);
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
/*     */   public String dumpAs(Object data, Tag rootTag, DumperOptions.FlowStyle flowStyle) {
/* 356 */     DumperOptions.FlowStyle oldStyle = this.representer.getDefaultFlowStyle();
/* 357 */     if (flowStyle != null) {
/* 358 */       this.representer.setDefaultFlowStyle(flowStyle);
/*     */     }
/* 360 */     List<Object> list = new ArrayList(1);
/* 361 */     list.add(data);
/* 362 */     StringWriter buffer = new StringWriter();
/* 363 */     dumpAll(list.iterator(), buffer, rootTag);
/* 364 */     this.representer.setDefaultFlowStyle(oldStyle);
/* 365 */     return buffer.toString();
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String dumpAsMap(Object data) {
/* 388 */     return dumpAs(data, Tag.MAP, DumperOptions.FlowStyle.BLOCK);
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
/*     */   public List<Event> serialize(Node data) {
/* 400 */     SilentEmitter emitter = new SilentEmitter();
/* 401 */     Serializer serializer = new Serializer(emitter, this.resolver, this.dumperOptions, null);
/*     */     try {
/* 403 */       serializer.open();
/* 404 */       serializer.serialize(data);
/* 405 */       serializer.close();
/* 406 */     } catch (IOException e) {
/* 407 */       throw new YAMLException(e);
/*     */     } 
/* 409 */     return emitter.getEvents();
/*     */   }
/*     */   
/*     */   private static class SilentEmitter implements Emitable {
/* 413 */     private List<Event> events = new ArrayList<Event>(100);
/*     */     
/*     */     public List<Event> getEvents() {
/* 416 */       return this.events;
/*     */     }
/*     */ 
/*     */     
/*     */     public void emit(Event event) throws IOException {
/* 421 */       this.events.add(event);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private SilentEmitter() {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T load(String yaml) {
/* 437 */     return (T)loadFromReader(new StreamReader(yaml), Object.class);
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
/*     */   public <T> T load(InputStream io) {
/* 452 */     return (T)loadFromReader(new StreamReader((Reader)new UnicodeReader(io)), Object.class);
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
/*     */   public <T> T load(Reader io) {
/* 467 */     return (T)loadFromReader(new StreamReader(io), Object.class);
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
/*     */   public <T> T loadAs(Reader io, Class<T> type) {
/* 484 */     return (T)loadFromReader(new StreamReader(io), type);
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
/*     */   public <T> T loadAs(String yaml, Class<T> type) {
/* 501 */     return (T)loadFromReader(new StreamReader(yaml), type);
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
/*     */   public <T> T loadAs(InputStream input, Class<T> type) {
/* 518 */     return (T)loadFromReader(new StreamReader((Reader)new UnicodeReader(input)), type);
/*     */   }
/*     */   
/*     */   private Object loadFromReader(StreamReader sreader, Class<?> type) {
/* 522 */     Composer composer = new Composer((Parser)new ParserImpl(sreader), this.resolver);
/* 523 */     this.constructor.setComposer(composer);
/* 524 */     return this.constructor.getSingleData(type);
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
/*     */   public Iterable<Object> loadAll(Reader yaml) {
/* 537 */     Composer composer = new Composer((Parser)new ParserImpl(new StreamReader(yaml)), this.resolver);
/* 538 */     this.constructor.setComposer(composer);
/* 539 */     Iterator<Object> result = new Iterator()
/*     */       {
/*     */         public boolean hasNext() {
/* 542 */           return Yaml.this.constructor.checkData();
/*     */         }
/*     */ 
/*     */         
/*     */         public Object next() {
/* 547 */           return Yaml.this.constructor.getData();
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/* 552 */           throw new UnsupportedOperationException();
/*     */         }
/*     */       };
/* 555 */     return new YamlIterable(result);
/*     */   }
/*     */   
/*     */   private static class YamlIterable implements Iterable<Object> {
/*     */     private Iterator<Object> iterator;
/*     */     
/*     */     public YamlIterable(Iterator<Object> iterator) {
/* 562 */       this.iterator = iterator;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<Object> iterator() {
/* 567 */       return this.iterator;
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
/*     */   
/*     */   public Iterable<Object> loadAll(String yaml) {
/* 582 */     return loadAll(new StringReader(yaml));
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
/*     */   public Iterable<Object> loadAll(InputStream yaml) {
/* 595 */     return loadAll((Reader)new UnicodeReader(yaml));
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
/*     */   public Node compose(Reader yaml) {
/* 609 */     Composer composer = new Composer((Parser)new ParserImpl(new StreamReader(yaml)), this.resolver);
/* 610 */     this.constructor.setComposer(composer);
/* 611 */     return composer.getSingleNode();
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
/*     */   public Iterable<Node> composeAll(Reader yaml) {
/* 624 */     final Composer composer = new Composer((Parser)new ParserImpl(new StreamReader(yaml)), this.resolver);
/* 625 */     this.constructor.setComposer(composer);
/* 626 */     Iterator<Node> result = new Iterator<Node>()
/*     */       {
/*     */         public boolean hasNext() {
/* 629 */           return composer.checkNode();
/*     */         }
/*     */ 
/*     */         
/*     */         public Node next() {
/* 634 */           return composer.getNode();
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/* 639 */           throw new UnsupportedOperationException();
/*     */         }
/*     */       };
/* 642 */     return new NodeIterable(result);
/*     */   }
/*     */   
/*     */   private static class NodeIterable implements Iterable<Node> {
/*     */     private Iterator<Node> iterator;
/*     */     
/*     */     public NodeIterable(Iterator<Node> iterator) {
/* 649 */       this.iterator = iterator;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<Node> iterator() {
/* 654 */       return this.iterator;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void addImplicitResolver(Tag tag, Pattern regexp, String first) {
/* 671 */     this.resolver.addImplicitResolver(tag, regexp, first);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 676 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 687 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/* 697 */     this.name = name;
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
/*     */   public Iterable<Event> parse(Reader yaml) {
/* 709 */     final ParserImpl parser = new ParserImpl(new StreamReader(yaml));
/* 710 */     Iterator<Event> result = new Iterator<Event>()
/*     */       {
/*     */         public boolean hasNext() {
/* 713 */           return (parser.peekEvent() != null);
/*     */         }
/*     */ 
/*     */         
/*     */         public Event next() {
/* 718 */           return parser.getEvent();
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/* 723 */           throw new UnsupportedOperationException();
/*     */         }
/*     */       };
/* 726 */     return new EventIterable(result);
/*     */   }
/*     */   
/*     */   private static class EventIterable implements Iterable<Event> {
/*     */     private Iterator<Event> iterator;
/*     */     
/*     */     public EventIterable(Iterator<Event> iterator) {
/* 733 */       this.iterator = iterator;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<Event> iterator() {
/* 738 */       return this.iterator;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setBeanAccess(BeanAccess beanAccess) {
/* 743 */     this.constructor.getPropertyUtils().setBeanAccess(beanAccess);
/* 744 */     this.representer.getPropertyUtils().setBeanAccess(beanAccess);
/*     */   }
/*     */   
/*     */   public void addTypeDescription(TypeDescription td) {
/* 748 */     this.constructor.addTypeDescription(td);
/* 749 */     this.representer.addTypeDescription(td);
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\yaml\snakeyaml\Yaml.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */