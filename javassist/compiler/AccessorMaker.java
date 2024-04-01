/*     */ package javassist.compiler;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtClass;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.bytecode.AttributeInfo;
/*     */ import javassist.bytecode.Bytecode;
/*     */ import javassist.bytecode.ClassFile;
/*     */ import javassist.bytecode.ConstPool;
/*     */ import javassist.bytecode.Descriptor;
/*     */ import javassist.bytecode.ExceptionsAttribute;
/*     */ import javassist.bytecode.FieldInfo;
/*     */ import javassist.bytecode.MethodInfo;
/*     */ import javassist.bytecode.SyntheticAttribute;
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
/*     */ public class AccessorMaker
/*     */ {
/*     */   private CtClass clazz;
/*     */   private int uniqueNumber;
/*     */   private HashMap accessors;
/*     */   static final String lastParamType = "javassist.runtime.Inner";
/*     */   
/*     */   public AccessorMaker(CtClass c) {
/*  35 */     this.clazz = c;
/*  36 */     this.uniqueNumber = 1;
/*  37 */     this.accessors = new HashMap<Object, Object>();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getConstructor(CtClass c, String desc, MethodInfo orig) throws CompileError {
/*  43 */     String key = "<init>:" + desc;
/*  44 */     String consDesc = (String)this.accessors.get(key);
/*  45 */     if (consDesc != null) {
/*  46 */       return consDesc;
/*     */     }
/*  48 */     consDesc = Descriptor.appendParameter("javassist.runtime.Inner", desc);
/*  49 */     ClassFile cf = this.clazz.getClassFile();
/*     */     try {
/*  51 */       ConstPool cp = cf.getConstPool();
/*  52 */       ClassPool pool = this.clazz.getClassPool();
/*  53 */       MethodInfo minfo = new MethodInfo(cp, "<init>", consDesc);
/*     */       
/*  55 */       minfo.setAccessFlags(0);
/*  56 */       minfo.addAttribute((AttributeInfo)new SyntheticAttribute(cp));
/*  57 */       ExceptionsAttribute ea = orig.getExceptionsAttribute();
/*  58 */       if (ea != null) {
/*  59 */         minfo.addAttribute(ea.copy(cp, null));
/*     */       }
/*  61 */       CtClass[] params = Descriptor.getParameterTypes(desc, pool);
/*  62 */       Bytecode code = new Bytecode(cp);
/*  63 */       code.addAload(0);
/*  64 */       int regno = 1;
/*  65 */       for (int i = 0; i < params.length; i++)
/*  66 */         regno += code.addLoad(regno, params[i]); 
/*  67 */       code.setMaxLocals(regno + 1);
/*  68 */       code.addInvokespecial(this.clazz, "<init>", desc);
/*     */       
/*  70 */       code.addReturn(null);
/*  71 */       minfo.setCodeAttribute(code.toCodeAttribute());
/*  72 */       cf.addMethod(minfo);
/*     */     }
/*  74 */     catch (CannotCompileException e) {
/*  75 */       throw new CompileError(e);
/*     */     }
/*  77 */     catch (NotFoundException e) {
/*  78 */       throw new CompileError(e);
/*     */     } 
/*     */     
/*  81 */     this.accessors.put(key, consDesc);
/*  82 */     return consDesc;
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
/*     */   public String getMethodAccessor(String name, String desc, String accDesc, MethodInfo orig) throws CompileError {
/* 102 */     String key = name + ":" + desc;
/* 103 */     String accName = (String)this.accessors.get(key);
/* 104 */     if (accName != null) {
/* 105 */       return accName;
/*     */     }
/* 107 */     ClassFile cf = this.clazz.getClassFile();
/* 108 */     accName = findAccessorName(cf);
/*     */     try {
/* 110 */       ConstPool cp = cf.getConstPool();
/* 111 */       ClassPool pool = this.clazz.getClassPool();
/* 112 */       MethodInfo minfo = new MethodInfo(cp, accName, accDesc);
/*     */       
/* 114 */       minfo.setAccessFlags(8);
/* 115 */       minfo.addAttribute((AttributeInfo)new SyntheticAttribute(cp));
/* 116 */       ExceptionsAttribute ea = orig.getExceptionsAttribute();
/* 117 */       if (ea != null) {
/* 118 */         minfo.addAttribute(ea.copy(cp, null));
/*     */       }
/* 120 */       CtClass[] params = Descriptor.getParameterTypes(accDesc, pool);
/* 121 */       int regno = 0;
/* 122 */       Bytecode code = new Bytecode(cp);
/* 123 */       for (int i = 0; i < params.length; i++) {
/* 124 */         regno += code.addLoad(regno, params[i]);
/*     */       }
/* 126 */       code.setMaxLocals(regno);
/* 127 */       if (desc == accDesc) {
/* 128 */         code.addInvokestatic(this.clazz, name, desc);
/*     */       } else {
/* 130 */         code.addInvokevirtual(this.clazz, name, desc);
/*     */       } 
/* 132 */       code.addReturn(Descriptor.getReturnType(desc, pool));
/* 133 */       minfo.setCodeAttribute(code.toCodeAttribute());
/* 134 */       cf.addMethod(minfo);
/*     */     }
/* 136 */     catch (CannotCompileException e) {
/* 137 */       throw new CompileError(e);
/*     */     }
/* 139 */     catch (NotFoundException e) {
/* 140 */       throw new CompileError(e);
/*     */     } 
/*     */     
/* 143 */     this.accessors.put(key, accName);
/* 144 */     return accName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodInfo getFieldGetter(FieldInfo finfo, boolean is_static) throws CompileError {
/* 153 */     String fieldName = finfo.getName();
/* 154 */     String key = fieldName + ":getter";
/* 155 */     Object res = this.accessors.get(key);
/* 156 */     if (res != null) {
/* 157 */       return (MethodInfo)res;
/*     */     }
/* 159 */     ClassFile cf = this.clazz.getClassFile();
/* 160 */     String accName = findAccessorName(cf); try {
/*     */       String accDesc;
/* 162 */       ConstPool cp = cf.getConstPool();
/* 163 */       ClassPool pool = this.clazz.getClassPool();
/* 164 */       String fieldType = finfo.getDescriptor();
/*     */       
/* 166 */       if (is_static) {
/* 167 */         accDesc = "()" + fieldType;
/*     */       } else {
/* 169 */         accDesc = "(" + Descriptor.of(this.clazz) + ")" + fieldType;
/*     */       } 
/* 171 */       MethodInfo minfo = new MethodInfo(cp, accName, accDesc);
/* 172 */       minfo.setAccessFlags(8);
/* 173 */       minfo.addAttribute((AttributeInfo)new SyntheticAttribute(cp));
/* 174 */       Bytecode code = new Bytecode(cp);
/* 175 */       if (is_static) {
/* 176 */         code.addGetstatic(Bytecode.THIS, fieldName, fieldType);
/*     */       } else {
/*     */         
/* 179 */         code.addAload(0);
/* 180 */         code.addGetfield(Bytecode.THIS, fieldName, fieldType);
/* 181 */         code.setMaxLocals(1);
/*     */       } 
/*     */       
/* 184 */       code.addReturn(Descriptor.toCtClass(fieldType, pool));
/* 185 */       minfo.setCodeAttribute(code.toCodeAttribute());
/* 186 */       cf.addMethod(minfo);
/* 187 */       this.accessors.put(key, minfo);
/* 188 */       return minfo;
/*     */     }
/* 190 */     catch (CannotCompileException e) {
/* 191 */       throw new CompileError(e);
/*     */     }
/* 193 */     catch (NotFoundException e) {
/* 194 */       throw new CompileError(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MethodInfo getFieldSetter(FieldInfo finfo, boolean is_static) throws CompileError {
/* 204 */     String fieldName = finfo.getName();
/* 205 */     String key = fieldName + ":setter";
/* 206 */     Object res = this.accessors.get(key);
/* 207 */     if (res != null) {
/* 208 */       return (MethodInfo)res;
/*     */     }
/* 210 */     ClassFile cf = this.clazz.getClassFile();
/* 211 */     String accName = findAccessorName(cf); try {
/*     */       String accDesc; int reg;
/* 213 */       ConstPool cp = cf.getConstPool();
/* 214 */       ClassPool pool = this.clazz.getClassPool();
/* 215 */       String fieldType = finfo.getDescriptor();
/*     */       
/* 217 */       if (is_static) {
/* 218 */         accDesc = "(" + fieldType + ")V";
/*     */       } else {
/* 220 */         accDesc = "(" + Descriptor.of(this.clazz) + fieldType + ")V";
/*     */       } 
/* 222 */       MethodInfo minfo = new MethodInfo(cp, accName, accDesc);
/* 223 */       minfo.setAccessFlags(8);
/* 224 */       minfo.addAttribute((AttributeInfo)new SyntheticAttribute(cp));
/* 225 */       Bytecode code = new Bytecode(cp);
/*     */       
/* 227 */       if (is_static) {
/* 228 */         reg = code.addLoad(0, Descriptor.toCtClass(fieldType, pool));
/* 229 */         code.addPutstatic(Bytecode.THIS, fieldName, fieldType);
/*     */       } else {
/*     */         
/* 232 */         code.addAload(0);
/* 233 */         reg = code.addLoad(1, Descriptor.toCtClass(fieldType, pool)) + 1;
/*     */         
/* 235 */         code.addPutfield(Bytecode.THIS, fieldName, fieldType);
/*     */       } 
/*     */       
/* 238 */       code.addReturn(null);
/* 239 */       code.setMaxLocals(reg);
/* 240 */       minfo.setCodeAttribute(code.toCodeAttribute());
/* 241 */       cf.addMethod(minfo);
/* 242 */       this.accessors.put(key, minfo);
/* 243 */       return minfo;
/*     */     }
/* 245 */     catch (CannotCompileException e) {
/* 246 */       throw new CompileError(e);
/*     */     }
/* 248 */     catch (NotFoundException e) {
/* 249 */       throw new CompileError(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private String findAccessorName(ClassFile cf) {
/*     */     while (true) {
/* 256 */       String accName = "access$" + this.uniqueNumber++;
/* 257 */       if (cf.getMethod(accName) == null)
/* 258 */         return accName; 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\AccessorMaker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */