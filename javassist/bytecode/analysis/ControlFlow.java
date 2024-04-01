/*     */ package javassist.bytecode.analysis;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import javassist.CtClass;
/*     */ import javassist.CtMethod;
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.MethodInfo;
/*     */ import javassist.bytecode.stackmap.BasicBlock;
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
/*     */ public class ControlFlow
/*     */ {
/*     */   private CtClass clazz;
/*     */   private MethodInfo methodInfo;
/*     */   private Block[] basicBlocks;
/*     */   private Frame[] frames;
/*     */   
/*     */   public ControlFlow(CtMethod method) throws BadBytecode {
/*  56 */     this(method.getDeclaringClass(), method.getMethodInfo2());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ControlFlow(CtClass ctclazz, MethodInfo minfo) throws BadBytecode {
/*  63 */     this.clazz = ctclazz;
/*  64 */     this.methodInfo = minfo;
/*  65 */     this.frames = null;
/*  66 */     this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  73 */       .basicBlocks = (Block[])(new BasicBlock.Maker() { protected BasicBlock makeBlock(int pos) { return new ControlFlow.Block(pos, ControlFlow.this.methodInfo); } protected BasicBlock[] makeArray(int size) { return (BasicBlock[])new ControlFlow.Block[size]; } }).make(minfo);
/*  74 */     if (this.basicBlocks == null)
/*  75 */       this.basicBlocks = new Block[0]; 
/*  76 */     int size = this.basicBlocks.length;
/*  77 */     int[] counters = new int[size]; int i;
/*  78 */     for (i = 0; i < size; i++) {
/*  79 */       Block b = this.basicBlocks[i];
/*  80 */       b.index = i;
/*  81 */       b.entrances = new Block[b.incomings()];
/*  82 */       counters[i] = 0;
/*     */     } 
/*     */     
/*  85 */     for (i = 0; i < size; i++) {
/*  86 */       Block b = this.basicBlocks[i];
/*  87 */       for (int k = 0; k < b.exits(); k++) {
/*  88 */         Block e = b.exit(k);
/*  89 */         counters[e.index] = counters[e.index] + 1; e.entrances[counters[e.index]] = b;
/*     */       } 
/*     */       
/*  92 */       Catcher[] catchers = b.catchers();
/*  93 */       for (int j = 0; j < catchers.length; j++) {
/*  94 */         Block catchBlock = (catchers[j]).node;
/*  95 */         counters[catchBlock.index] = counters[catchBlock.index] + 1; catchBlock.entrances[counters[catchBlock.index]] = b;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Block[] basicBlocks() {
/* 107 */     return this.basicBlocks;
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
/*     */   public Frame frameAt(int pos) throws BadBytecode {
/* 119 */     if (this.frames == null) {
/* 120 */       this.frames = (new Analyzer()).analyze(this.clazz, this.methodInfo);
/*     */     }
/* 122 */     return this.frames[pos];
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
/*     */   public Node[] dominatorTree() {
/* 146 */     int size = this.basicBlocks.length;
/* 147 */     if (size == 0) {
/* 148 */       return null;
/*     */     }
/* 150 */     Node[] nodes = new Node[size];
/* 151 */     boolean[] visited = new boolean[size];
/* 152 */     int[] distance = new int[size];
/* 153 */     for (int i = 0; i < size; i++) {
/* 154 */       nodes[i] = new Node(this.basicBlocks[i]);
/* 155 */       visited[i] = false;
/*     */     } 
/*     */     
/* 158 */     Access access = new Access(nodes) {
/* 159 */         BasicBlock[] exits(ControlFlow.Node n) { return n.block.getExit(); }
/* 160 */         BasicBlock[] entrances(ControlFlow.Node n) { return (BasicBlock[])n.block.entrances; }
/*     */       };
/* 162 */     nodes[0].makeDepth1stTree(null, visited, 0, distance, access);
/*     */     while (true) {
/* 164 */       for (int j = 0; j < size; j++)
/* 165 */         visited[j] = false; 
/* 166 */       if (!nodes[0].makeDominatorTree(visited, distance, access)) {
/* 167 */         Node.setChildren(nodes);
/* 168 */         return nodes;
/*     */       } 
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
/*     */   public Node[] postDominatorTree() {
/*     */     boolean changed;
/* 192 */     int size = this.basicBlocks.length;
/* 193 */     if (size == 0) {
/* 194 */       return null;
/*     */     }
/* 196 */     Node[] nodes = new Node[size];
/* 197 */     boolean[] visited = new boolean[size];
/* 198 */     int[] distance = new int[size];
/* 199 */     for (int i = 0; i < size; i++) {
/* 200 */       nodes[i] = new Node(this.basicBlocks[i]);
/* 201 */       visited[i] = false;
/*     */     } 
/*     */     
/* 204 */     Access access = new Access(nodes) {
/* 205 */         BasicBlock[] exits(ControlFlow.Node n) { return (BasicBlock[])n.block.entrances; } BasicBlock[] entrances(ControlFlow.Node n) {
/* 206 */           return n.block.getExit();
/*     */         }
/*     */       };
/* 209 */     int counter = 0;
/* 210 */     for (int j = 0; j < size; j++) {
/* 211 */       if ((nodes[j]).block.exits() == 0)
/* 212 */         counter = nodes[j].makeDepth1stTree(null, visited, counter, distance, access); 
/*     */     } 
/*     */     do {
/*     */       int k;
/* 216 */       for (k = 0; k < size; k++) {
/* 217 */         visited[k] = false;
/*     */       }
/* 219 */       changed = false;
/* 220 */       for (k = 0; k < size; k++)
/* 221 */       { if ((nodes[k]).block.exits() == 0 && 
/* 222 */           nodes[k].makeDominatorTree(visited, distance, access))
/* 223 */           changed = true;  } 
/* 224 */     } while (changed);
/*     */     
/* 226 */     Node.setChildren(nodes);
/* 227 */     return nodes;
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
/*     */   public static class Block
/*     */     extends BasicBlock
/*     */   {
/* 244 */     public Object clientData = null;
/*     */     
/*     */     int index;
/*     */     MethodInfo method;
/*     */     Block[] entrances;
/*     */     
/*     */     Block(int pos, MethodInfo minfo) {
/* 251 */       super(pos);
/* 252 */       this.method = minfo;
/*     */     }
/*     */     
/*     */     protected void toString2(StringBuffer sbuf) {
/* 256 */       super.toString2(sbuf);
/* 257 */       sbuf.append(", incoming{");
/* 258 */       for (int i = 0; i < this.entrances.length; i++) {
/* 259 */         sbuf.append((this.entrances[i]).position).append(", ");
/*     */       }
/* 261 */       sbuf.append("}");
/*     */     }
/*     */     BasicBlock[] getExit() {
/* 264 */       return this.exit;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int index() {
/* 273 */       return this.index;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int position() {
/* 279 */       return this.position;
/*     */     }
/*     */ 
/*     */     
/*     */     public int length() {
/* 284 */       return this.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public int incomings() {
/* 289 */       return this.incoming;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Block incoming(int n) {
/* 295 */       return this.entrances[n];
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int exits() {
/* 302 */       return (this.exit == null) ? 0 : this.exit.length;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Block exit(int n) {
/* 310 */       return (Block)this.exit[n];
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ControlFlow.Catcher[] catchers() {
/* 317 */       ArrayList<ControlFlow.Catcher> catchers = new ArrayList();
/* 318 */       BasicBlock.Catch c = this.toCatch;
/* 319 */       while (c != null) {
/* 320 */         catchers.add(new ControlFlow.Catcher(c));
/* 321 */         c = c.next;
/*     */       } 
/*     */       
/* 324 */       return catchers.<ControlFlow.Catcher>toArray(new ControlFlow.Catcher[catchers.size()]);
/*     */     }
/*     */   }
/*     */   
/*     */   static abstract class Access { ControlFlow.Node[] all;
/*     */     
/* 330 */     Access(ControlFlow.Node[] nodes) { this.all = nodes; } ControlFlow.Node node(BasicBlock b) {
/* 331 */       return this.all[((ControlFlow.Block)b).index];
/*     */     }
/*     */     
/*     */     abstract BasicBlock[] exits(ControlFlow.Node param1Node);
/*     */     
/*     */     abstract BasicBlock[] entrances(ControlFlow.Node param1Node); }
/*     */ 
/*     */   
/*     */   public static class Node {
/*     */     private ControlFlow.Block block;
/*     */     private Node parent;
/*     */     private Node[] children;
/*     */     
/*     */     Node(ControlFlow.Block b) {
/* 345 */       this.block = b;
/* 346 */       this.parent = null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 353 */       StringBuffer sbuf = new StringBuffer();
/* 354 */       sbuf.append("Node[pos=").append(block().position());
/* 355 */       sbuf.append(", parent=");
/* 356 */       sbuf.append((this.parent == null) ? "*" : Integer.toString(this.parent.block().position()));
/* 357 */       sbuf.append(", children{");
/* 358 */       for (int i = 0; i < this.children.length; i++) {
/* 359 */         sbuf.append(this.children[i].block().position()).append(", ");
/*     */       }
/* 361 */       sbuf.append("}]");
/* 362 */       return sbuf.toString();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public ControlFlow.Block block() {
/* 368 */       return this.block;
/*     */     }
/*     */ 
/*     */     
/*     */     public Node parent() {
/* 373 */       return this.parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public int children() {
/* 378 */       return this.children.length;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Node child(int n) {
/* 385 */       return this.children[n];
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int makeDepth1stTree(Node caller, boolean[] visited, int counter, int[] distance, ControlFlow.Access access) {
/* 393 */       int index = this.block.index;
/* 394 */       if (visited[index]) {
/* 395 */         return counter;
/*     */       }
/* 397 */       visited[index] = true;
/* 398 */       this.parent = caller;
/* 399 */       BasicBlock[] exits = access.exits(this);
/* 400 */       if (exits != null) {
/* 401 */         for (int i = 0; i < exits.length; i++) {
/* 402 */           Node n = access.node(exits[i]);
/* 403 */           counter = n.makeDepth1stTree(this, visited, counter, distance, access);
/*     */         } 
/*     */       }
/* 406 */       distance[index] = counter++;
/* 407 */       return counter;
/*     */     }
/*     */     
/*     */     boolean makeDominatorTree(boolean[] visited, int[] distance, ControlFlow.Access access) {
/* 411 */       int index = this.block.index;
/* 412 */       if (visited[index]) {
/* 413 */         return false;
/*     */       }
/* 415 */       visited[index] = true;
/* 416 */       boolean changed = false;
/* 417 */       BasicBlock[] exits = access.exits(this);
/* 418 */       if (exits != null)
/* 419 */         for (int i = 0; i < exits.length; i++) {
/* 420 */           Node n = access.node(exits[i]);
/* 421 */           if (n.makeDominatorTree(visited, distance, access)) {
/* 422 */             changed = true;
/*     */           }
/*     */         }  
/* 425 */       BasicBlock[] entrances = access.entrances(this);
/* 426 */       if (entrances != null) {
/* 427 */         for (int i = 0; i < entrances.length; i++) {
/* 428 */           if (this.parent != null) {
/* 429 */             Node n = getAncestor(this.parent, access.node(entrances[i]), distance);
/* 430 */             if (n != this.parent) {
/* 431 */               this.parent = n;
/* 432 */               changed = true;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       }
/* 437 */       return changed;
/*     */     }
/*     */     
/*     */     private static Node getAncestor(Node n1, Node n2, int[] distance) {
/* 441 */       while (n1 != n2) {
/* 442 */         if (distance[n1.block.index] < distance[n2.block.index]) {
/* 443 */           n1 = n1.parent;
/*     */         } else {
/* 445 */           n2 = n2.parent;
/*     */         } 
/* 447 */         if (n1 == null || n2 == null) {
/* 448 */           return null;
/*     */         }
/*     */       } 
/* 451 */       return n1;
/*     */     }
/*     */     
/*     */     private static void setChildren(Node[] all) {
/* 455 */       int size = all.length;
/* 456 */       int[] nchildren = new int[size]; int i;
/* 457 */       for (i = 0; i < size; i++) {
/* 458 */         nchildren[i] = 0;
/*     */       }
/* 460 */       for (i = 0; i < size; i++) {
/* 461 */         Node p = (all[i]).parent;
/* 462 */         if (p != null) {
/* 463 */           nchildren[p.block.index] = nchildren[p.block.index] + 1;
/*     */         }
/*     */       } 
/* 466 */       for (i = 0; i < size; i++) {
/* 467 */         (all[i]).children = new Node[nchildren[i]];
/*     */       }
/* 469 */       for (i = 0; i < size; i++) {
/* 470 */         nchildren[i] = 0;
/*     */       }
/* 472 */       for (i = 0; i < size; i++) {
/* 473 */         Node n = all[i];
/* 474 */         Node p = n.parent;
/* 475 */         if (p != null) {
/* 476 */           nchildren[p.block.index] = nchildren[p.block.index] + 1; p.children[nchildren[p.block.index]] = n;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Catcher
/*     */   {
/*     */     private ControlFlow.Block node;
/*     */     private int typeIndex;
/*     */     
/*     */     Catcher(BasicBlock.Catch c) {
/* 489 */       this.node = (ControlFlow.Block)c.body;
/* 490 */       this.typeIndex = c.typeIndex;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public ControlFlow.Block block() {
/* 496 */       return this.node;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String type() {
/* 503 */       if (this.typeIndex == 0) {
/* 504 */         return "java.lang.Throwable";
/*     */       }
/* 506 */       return this.node.method.getConstPool().getClassInfo(this.typeIndex);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\analysis\ControlFlow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */