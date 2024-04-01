package javassist.compiler;

import javassist.bytecode.Bytecode;
import javassist.compiler.ast.ASTList;

public interface ProceedHandler {
  void doit(JvstCodeGen paramJvstCodeGen, Bytecode paramBytecode, ASTList paramASTList) throws CompileError;
  
  void setReturnType(JvstTypeChecker paramJvstTypeChecker, ASTList paramASTList) throws CompileError;
}


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\ProceedHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */