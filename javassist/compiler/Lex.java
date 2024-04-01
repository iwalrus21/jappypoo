/*     */ package javassist.compiler;
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
/*     */ public class Lex
/*     */   implements TokenId
/*     */ {
/*     */   private int lastChar;
/*     */   private StringBuffer textBuffer;
/*     */   private Token currentToken;
/*     */   private Token lookAheadTokens;
/*     */   private String input;
/*     */   private int position;
/*     */   private int maxlen;
/*     */   private int lineNumber;
/*     */   
/*     */   public Lex(String s) {
/*  41 */     this.lastChar = -1;
/*  42 */     this.textBuffer = new StringBuffer();
/*  43 */     this.currentToken = new Token();
/*  44 */     this.lookAheadTokens = null;
/*     */     
/*  46 */     this.input = s;
/*  47 */     this.position = 0;
/*  48 */     this.maxlen = s.length();
/*  49 */     this.lineNumber = 0;
/*     */   }
/*     */   
/*     */   public int get() {
/*  53 */     if (this.lookAheadTokens == null) {
/*  54 */       return get(this.currentToken);
/*     */     }
/*     */     
/*  57 */     Token t = this.lookAheadTokens;
/*  58 */     this.lookAheadTokens = this.lookAheadTokens.next;
/*  59 */     return t.tokenId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int lookAhead() {
/*  67 */     return lookAhead(0);
/*     */   }
/*     */   
/*     */   public int lookAhead(int i) {
/*  71 */     Token tk = this.lookAheadTokens;
/*  72 */     if (tk == null) {
/*  73 */       this.lookAheadTokens = tk = this.currentToken;
/*  74 */       tk.next = null;
/*  75 */       get(tk);
/*     */     } 
/*     */     
/*  78 */     for (; i-- > 0; tk = tk.next) {
/*  79 */       if (tk.next == null) {
/*     */         
/*  81 */         Token tk2 = new Token();
/*  82 */         get(tk2);
/*     */       } 
/*     */     } 
/*  85 */     this.currentToken = tk;
/*  86 */     return tk.tokenId;
/*     */   }
/*     */   
/*     */   public String getString() {
/*  90 */     return this.currentToken.textValue;
/*     */   }
/*     */   
/*     */   public long getLong() {
/*  94 */     return this.currentToken.longValue;
/*     */   }
/*     */   
/*     */   public double getDouble() {
/*  98 */     return this.currentToken.doubleValue;
/*     */   }
/*     */ 
/*     */   
/*     */   private int get(Token token) {
/*     */     while (true) {
/* 104 */       int t = readLine(token);
/* 105 */       if (t != 10) {
/* 106 */         token.tokenId = t;
/* 107 */         return t;
/*     */       } 
/*     */     } 
/*     */   } private int readLine(Token token) {
/* 111 */     int c = getNextNonWhiteChar();
/* 112 */     if (c < 0)
/* 113 */       return c; 
/* 114 */     if (c == 10) {
/* 115 */       this.lineNumber++;
/* 116 */       return 10;
/*     */     } 
/* 118 */     if (c == 39)
/* 119 */       return readCharConst(token); 
/* 120 */     if (c == 34)
/* 121 */       return readStringL(token); 
/* 122 */     if (48 <= c && c <= 57)
/* 123 */       return readNumber(c, token); 
/* 124 */     if (c == 46) {
/* 125 */       c = getc();
/* 126 */       if (48 <= c && c <= 57) {
/* 127 */         StringBuffer tbuf = this.textBuffer;
/* 128 */         tbuf.setLength(0);
/* 129 */         tbuf.append('.');
/* 130 */         return readDouble(tbuf, c, token);
/*     */       } 
/*     */       
/* 133 */       ungetc(c);
/* 134 */       return readSeparator(46);
/*     */     } 
/*     */     
/* 137 */     if (Character.isJavaIdentifierStart((char)c)) {
/* 138 */       return readIdentifier(c, token);
/*     */     }
/* 140 */     return readSeparator(c);
/*     */   }
/*     */ 
/*     */   
/*     */   private int getNextNonWhiteChar() {
/*     */     while (true) {
/* 146 */       int c = getc();
/* 147 */       if (c == 47) {
/* 148 */         c = getc();
/* 149 */         if (c == 47)
/*     */         { do {
/* 151 */             c = getc();
/* 152 */           } while (c != 10 && c != 13 && c != -1); }
/* 153 */         else if (c == 42)
/*     */         { while (true) {
/* 155 */             c = getc();
/* 156 */             if (c == -1)
/*     */               break; 
/* 158 */             if (c == 42) {
/* 159 */               if ((c = getc()) == 47) {
/* 160 */                 c = 32;
/*     */                 
/*     */                 break;
/*     */               } 
/* 164 */               ungetc(c);
/*     */             } 
/*     */           }  }
/* 167 */         else { ungetc(c);
/* 168 */           c = 47; }
/*     */       
/*     */       } 
/* 171 */       if (!isBlank(c))
/* 172 */         return c; 
/*     */     } 
/*     */   }
/*     */   
/*     */   private int readCharConst(Token token) {
/* 177 */     int value = 0; int c;
/* 178 */     while ((c = getc()) != 39) {
/* 179 */       if (c == 92) {
/* 180 */         value = readEscapeChar(); continue;
/* 181 */       }  if (c < 32) {
/* 182 */         if (c == 10) {
/* 183 */           this.lineNumber++;
/*     */         }
/* 185 */         return 500;
/*     */       } 
/*     */       
/* 188 */       value = c;
/*     */     } 
/* 190 */     token.longValue = value;
/* 191 */     return 401;
/*     */   }
/*     */   
/*     */   private int readEscapeChar() {
/* 195 */     int c = getc();
/* 196 */     if (c == 110) {
/* 197 */       c = 10;
/* 198 */     } else if (c == 116) {
/* 199 */       c = 9;
/* 200 */     } else if (c == 114) {
/* 201 */       c = 13;
/* 202 */     } else if (c == 102) {
/* 203 */       c = 12;
/* 204 */     } else if (c == 10) {
/* 205 */       this.lineNumber++;
/*     */     } 
/* 207 */     return c;
/*     */   }
/*     */ 
/*     */   
/*     */   private int readStringL(Token token) {
/* 212 */     StringBuffer tbuf = this.textBuffer;
/* 213 */     tbuf.setLength(0); while (true) {
/*     */       int c;
/* 215 */       while ((c = getc()) != 34) {
/* 216 */         if (c == 92) {
/* 217 */           c = readEscapeChar();
/* 218 */         } else if (c == 10 || c < 0) {
/* 219 */           this.lineNumber++;
/* 220 */           return 500;
/*     */         } 
/*     */         
/* 223 */         tbuf.append((char)c);
/*     */       } 
/*     */       
/*     */       while (true) {
/* 227 */         c = getc();
/* 228 */         if (c == 10) {
/* 229 */           this.lineNumber++; continue;
/* 230 */         }  if (!isBlank(c)) {
/*     */           break;
/*     */         }
/*     */       } 
/* 234 */       if (c != 34) {
/* 235 */         ungetc(c);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 240 */         token.textValue = tbuf.toString();
/* 241 */         return 406;
/*     */       } 
/*     */     } 
/*     */   } private int readNumber(int c, Token token) {
/* 245 */     long value = 0L;
/* 246 */     int c2 = getc();
/* 247 */     if (c == 48) {
/* 248 */       if (c2 == 88 || c2 == 120) {
/*     */         while (true) {
/* 250 */           c = getc();
/* 251 */           if (48 <= c && c <= 57) {
/* 252 */             value = value * 16L + (c - 48); continue;
/* 253 */           }  if (65 <= c && c <= 70) {
/* 254 */             value = value * 16L + (c - 65 + 10); continue;
/* 255 */           }  if (97 <= c && c <= 102) {
/* 256 */             value = value * 16L + (c - 97 + 10); continue;
/*     */           }  break;
/* 258 */         }  token.longValue = value;
/* 259 */         if (c == 76 || c == 108) {
/* 260 */           return 403;
/*     */         }
/* 262 */         ungetc(c);
/* 263 */         return 402;
/*     */       } 
/*     */ 
/*     */       
/* 267 */       if (48 <= c2 && c2 <= 55) {
/* 268 */         value = (c2 - 48);
/*     */         while (true) {
/* 270 */           c = getc();
/* 271 */           if (48 <= c && c <= 55) {
/* 272 */             value = value * 8L + (c - 48); continue;
/*     */           }  break;
/* 274 */         }  token.longValue = value;
/* 275 */         if (c == 76 || c == 108) {
/* 276 */           return 403;
/*     */         }
/* 278 */         ungetc(c);
/* 279 */         return 402;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 285 */     value = (c - 48);
/* 286 */     while (48 <= c2 && c2 <= 57) {
/* 287 */       value = value * 10L + c2 - 48L;
/* 288 */       c2 = getc();
/*     */     } 
/*     */     
/* 291 */     token.longValue = value;
/* 292 */     if (c2 == 70 || c2 == 102) {
/* 293 */       token.doubleValue = value;
/* 294 */       return 404;
/*     */     } 
/* 296 */     if (c2 == 69 || c2 == 101 || c2 == 68 || c2 == 100 || c2 == 46) {
/*     */       
/* 298 */       StringBuffer tbuf = this.textBuffer;
/* 299 */       tbuf.setLength(0);
/* 300 */       tbuf.append(value);
/* 301 */       return readDouble(tbuf, c2, token);
/*     */     } 
/* 303 */     if (c2 == 76 || c2 == 108) {
/* 304 */       return 403;
/*     */     }
/* 306 */     ungetc(c2);
/* 307 */     return 402;
/*     */   }
/*     */ 
/*     */   
/*     */   private int readDouble(StringBuffer sbuf, int c, Token token) {
/* 312 */     if (c != 69 && c != 101 && c != 68 && c != 100) {
/* 313 */       sbuf.append((char)c);
/*     */       while (true) {
/* 315 */         c = getc();
/* 316 */         if (48 <= c && c <= 57) {
/* 317 */           sbuf.append((char)c);
/*     */           continue;
/*     */         } 
/*     */         break;
/*     */       } 
/*     */     } 
/* 323 */     if (c == 69 || c == 101) {
/* 324 */       sbuf.append((char)c);
/* 325 */       c = getc();
/* 326 */       if (c == 43 || c == 45) {
/* 327 */         sbuf.append((char)c);
/* 328 */         c = getc();
/*     */       } 
/*     */       
/* 331 */       while (48 <= c && c <= 57) {
/* 332 */         sbuf.append((char)c);
/* 333 */         c = getc();
/*     */       } 
/*     */     } 
/*     */     
/*     */     try {
/* 338 */       token.doubleValue = Double.parseDouble(sbuf.toString());
/*     */     }
/* 340 */     catch (NumberFormatException e) {
/* 341 */       return 500;
/*     */     } 
/*     */     
/* 344 */     if (c == 70 || c == 102) {
/* 345 */       return 404;
/*     */     }
/* 347 */     if (c != 68 && c != 100) {
/* 348 */       ungetc(c);
/*     */     }
/* 350 */     return 405;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 355 */   private static final int[] equalOps = new int[] { 350, 0, 0, 0, 351, 352, 0, 0, 0, 353, 354, 0, 355, 0, 356, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 357, 358, 359, 0 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int readSeparator(int c) {
/*     */     int c2;
/* 363 */     if (33 <= c && c <= 63) {
/* 364 */       int t = equalOps[c - 33];
/* 365 */       if (t == 0) {
/* 366 */         return c;
/*     */       }
/* 368 */       c2 = getc();
/* 369 */       if (c == c2) {
/* 370 */         int c3; switch (c) {
/*     */           case 61:
/* 372 */             return 358;
/*     */           case 43:
/* 374 */             return 362;
/*     */           case 45:
/* 376 */             return 363;
/*     */           case 38:
/* 378 */             return 369;
/*     */           case 60:
/* 380 */             c3 = getc();
/* 381 */             if (c3 == 61) {
/* 382 */               return 365;
/*     */             }
/* 384 */             ungetc(c3);
/* 385 */             return 364;
/*     */           
/*     */           case 62:
/* 388 */             c3 = getc();
/* 389 */             if (c3 == 61)
/* 390 */               return 367; 
/* 391 */             if (c3 == 62) {
/* 392 */               c3 = getc();
/* 393 */               if (c3 == 61) {
/* 394 */                 return 371;
/*     */               }
/* 396 */               ungetc(c3);
/* 397 */               return 370;
/*     */             } 
/*     */ 
/*     */             
/* 401 */             ungetc(c3);
/* 402 */             return 366;
/*     */         } 
/*     */ 
/*     */ 
/*     */       
/* 407 */       } else if (c2 == 61) {
/* 408 */         return t;
/*     */       }
/*     */     
/* 411 */     } else if (c == 94) {
/* 412 */       c2 = getc();
/* 413 */       if (c2 == 61) {
/* 414 */         return 360;
/*     */       }
/* 416 */     } else if (c == 124) {
/* 417 */       c2 = getc();
/* 418 */       if (c2 == 61)
/* 419 */         return 361; 
/* 420 */       if (c2 == 124) {
/* 421 */         return 368;
/*     */       }
/*     */     } else {
/* 424 */       return c;
/*     */     } 
/* 426 */     ungetc(c2);
/* 427 */     return c;
/*     */   }
/*     */   
/*     */   private int readIdentifier(int c, Token token) {
/* 431 */     StringBuffer tbuf = this.textBuffer;
/* 432 */     tbuf.setLength(0);
/*     */     
/*     */     do {
/* 435 */       tbuf.append((char)c);
/* 436 */       c = getc();
/* 437 */     } while (Character.isJavaIdentifierPart((char)c));
/*     */     
/* 439 */     ungetc(c);
/*     */     
/* 441 */     String name = tbuf.toString();
/* 442 */     int t = ktable.lookup(name);
/* 443 */     if (t >= 0) {
/* 444 */       return t;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 453 */     token.textValue = name;
/* 454 */     return 400;
/*     */   }
/*     */ 
/*     */   
/* 458 */   private static final KeywordTable ktable = new KeywordTable();
/*     */   
/*     */   static {
/* 461 */     ktable.append("abstract", 300);
/* 462 */     ktable.append("boolean", 301);
/* 463 */     ktable.append("break", 302);
/* 464 */     ktable.append("byte", 303);
/* 465 */     ktable.append("case", 304);
/* 466 */     ktable.append("catch", 305);
/* 467 */     ktable.append("char", 306);
/* 468 */     ktable.append("class", 307);
/* 469 */     ktable.append("const", 308);
/* 470 */     ktable.append("continue", 309);
/* 471 */     ktable.append("default", 310);
/* 472 */     ktable.append("do", 311);
/* 473 */     ktable.append("double", 312);
/* 474 */     ktable.append("else", 313);
/* 475 */     ktable.append("extends", 314);
/* 476 */     ktable.append("false", 411);
/* 477 */     ktable.append("final", 315);
/* 478 */     ktable.append("finally", 316);
/* 479 */     ktable.append("float", 317);
/* 480 */     ktable.append("for", 318);
/* 481 */     ktable.append("goto", 319);
/* 482 */     ktable.append("if", 320);
/* 483 */     ktable.append("implements", 321);
/* 484 */     ktable.append("import", 322);
/* 485 */     ktable.append("instanceof", 323);
/* 486 */     ktable.append("int", 324);
/* 487 */     ktable.append("interface", 325);
/* 488 */     ktable.append("long", 326);
/* 489 */     ktable.append("native", 327);
/* 490 */     ktable.append("new", 328);
/* 491 */     ktable.append("null", 412);
/* 492 */     ktable.append("package", 329);
/* 493 */     ktable.append("private", 330);
/* 494 */     ktable.append("protected", 331);
/* 495 */     ktable.append("public", 332);
/* 496 */     ktable.append("return", 333);
/* 497 */     ktable.append("short", 334);
/* 498 */     ktable.append("static", 335);
/* 499 */     ktable.append("strictfp", 347);
/* 500 */     ktable.append("super", 336);
/* 501 */     ktable.append("switch", 337);
/* 502 */     ktable.append("synchronized", 338);
/* 503 */     ktable.append("this", 339);
/* 504 */     ktable.append("throw", 340);
/* 505 */     ktable.append("throws", 341);
/* 506 */     ktable.append("transient", 342);
/* 507 */     ktable.append("true", 410);
/* 508 */     ktable.append("try", 343);
/* 509 */     ktable.append("void", 344);
/* 510 */     ktable.append("volatile", 345);
/* 511 */     ktable.append("while", 346);
/*     */   }
/*     */   
/*     */   private static boolean isBlank(int c) {
/* 515 */     return (c == 32 || c == 9 || c == 12 || c == 13 || c == 10);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isDigit(int c) {
/* 520 */     return (48 <= c && c <= 57);
/*     */   }
/*     */   
/*     */   private void ungetc(int c) {
/* 524 */     this.lastChar = c;
/*     */   }
/*     */   
/*     */   public String getTextAround() {
/* 528 */     int begin = this.position - 10;
/* 529 */     if (begin < 0) {
/* 530 */       begin = 0;
/*     */     }
/* 532 */     int end = this.position + 10;
/* 533 */     if (end > this.maxlen) {
/* 534 */       end = this.maxlen;
/*     */     }
/* 536 */     return this.input.substring(begin, end);
/*     */   }
/*     */   
/*     */   private int getc() {
/* 540 */     if (this.lastChar < 0) {
/* 541 */       if (this.position < this.maxlen) {
/* 542 */         return this.input.charAt(this.position++);
/*     */       }
/* 544 */       return -1;
/*     */     } 
/* 546 */     int c = this.lastChar;
/* 547 */     this.lastChar = -1;
/* 548 */     return c;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\Lex.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */