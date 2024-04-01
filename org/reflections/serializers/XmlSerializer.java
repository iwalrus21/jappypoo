/*     */ package org.reflections.serializers;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.StringWriter;
/*     */ import java.lang.reflect.Constructor;
/*     */ import org.dom4j.Document;
/*     */ import org.dom4j.DocumentException;
/*     */ import org.dom4j.DocumentFactory;
/*     */ import org.dom4j.Element;
/*     */ import org.dom4j.io.OutputFormat;
/*     */ import org.dom4j.io.SAXReader;
/*     */ import org.dom4j.io.XMLWriter;
/*     */ import org.reflections.Configuration;
/*     */ import org.reflections.Reflections;
/*     */ import org.reflections.ReflectionsException;
/*     */ import org.reflections.Store;
/*     */ import org.reflections.util.ConfigurationBuilder;
/*     */ import org.reflections.util.Utils;
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
/*     */ public class XmlSerializer
/*     */   implements Serializer
/*     */ {
/*     */   public Reflections read(InputStream inputStream) {
/*     */     Reflections reflections;
/*     */     try {
/*  40 */       Constructor<Reflections> constructor = Reflections.class.getDeclaredConstructor(new Class[0]);
/*  41 */       constructor.setAccessible(true);
/*  42 */       reflections = constructor.newInstance(new Object[0]);
/*  43 */     } catch (Exception e) {
/*  44 */       reflections = new Reflections((Configuration)new ConfigurationBuilder());
/*     */     } 
/*     */     
/*     */     try {
/*  48 */       Document document = (new SAXReader()).read(inputStream);
/*  49 */       for (Object e1 : document.getRootElement().elements()) {
/*  50 */         Element index = (Element)e1;
/*  51 */         for (Object e2 : index.elements()) {
/*  52 */           Element entry = (Element)e2;
/*  53 */           Element key = entry.element("key");
/*  54 */           Element values = entry.element("values");
/*  55 */           for (Object o3 : values.elements()) {
/*  56 */             Element value = (Element)o3;
/*  57 */             reflections.getStore().getOrCreate(index.getName()).put(key.getText(), value.getText());
/*     */           } 
/*     */         } 
/*     */       } 
/*  61 */     } catch (DocumentException e) {
/*  62 */       throw new ReflectionsException("could not read.", e);
/*  63 */     } catch (Throwable e) {
/*  64 */       throw new RuntimeException("Could not read. Make sure relevant dependencies exist on classpath.", e);
/*     */     } 
/*     */     
/*  67 */     return reflections;
/*     */   }
/*     */   
/*     */   public File save(Reflections reflections, String filename) {
/*  71 */     File file = Utils.prepareFile(filename);
/*     */ 
/*     */     
/*     */     try {
/*  75 */       Document document = createDocument(reflections);
/*  76 */       XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(file), OutputFormat.createPrettyPrint());
/*  77 */       xmlWriter.write(document);
/*  78 */       xmlWriter.close();
/*  79 */     } catch (IOException e) {
/*  80 */       throw new ReflectionsException("could not save to file " + filename, e);
/*  81 */     } catch (Throwable e) {
/*  82 */       throw new RuntimeException("Could not save to file " + filename + ". Make sure relevant dependencies exist on classpath.", e);
/*     */     } 
/*     */     
/*  85 */     return file;
/*     */   }
/*     */   
/*     */   public String toString(Reflections reflections) {
/*  89 */     Document document = createDocument(reflections);
/*     */     
/*     */     try {
/*  92 */       StringWriter writer = new StringWriter();
/*  93 */       XMLWriter xmlWriter = new XMLWriter(writer, OutputFormat.createPrettyPrint());
/*  94 */       xmlWriter.write(document);
/*  95 */       xmlWriter.close();
/*  96 */       return writer.toString();
/*  97 */     } catch (IOException e) {
/*  98 */       throw new RuntimeException();
/*     */     } 
/*     */   }
/*     */   
/*     */   private Document createDocument(Reflections reflections) {
/* 103 */     Store map = reflections.getStore();
/*     */     
/* 105 */     Document document = DocumentFactory.getInstance().createDocument();
/* 106 */     Element root = document.addElement("Reflections");
/* 107 */     for (String indexName : map.keySet()) {
/* 108 */       Element indexElement = root.addElement(indexName);
/* 109 */       for (String key : map.get(indexName).keySet()) {
/* 110 */         Element entryElement = indexElement.addElement("entry");
/* 111 */         entryElement.addElement("key").setText(key);
/* 112 */         Element valuesElement = entryElement.addElement("values");
/* 113 */         for (String value : map.get(indexName).get(key)) {
/* 114 */           valuesElement.addElement("value").setText(value);
/*     */         }
/*     */       } 
/*     */     } 
/* 118 */     return document;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\reflections\serializers\XmlSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */