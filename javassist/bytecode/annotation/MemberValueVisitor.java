package javassist.bytecode.annotation;

public interface MemberValueVisitor {
  void visitAnnotationMemberValue(AnnotationMemberValue paramAnnotationMemberValue);
  
  void visitArrayMemberValue(ArrayMemberValue paramArrayMemberValue);
  
  void visitBooleanMemberValue(BooleanMemberValue paramBooleanMemberValue);
  
  void visitByteMemberValue(ByteMemberValue paramByteMemberValue);
  
  void visitCharMemberValue(CharMemberValue paramCharMemberValue);
  
  void visitDoubleMemberValue(DoubleMemberValue paramDoubleMemberValue);
  
  void visitEnumMemberValue(EnumMemberValue paramEnumMemberValue);
  
  void visitFloatMemberValue(FloatMemberValue paramFloatMemberValue);
  
  void visitIntegerMemberValue(IntegerMemberValue paramIntegerMemberValue);
  
  void visitLongMemberValue(LongMemberValue paramLongMemberValue);
  
  void visitShortMemberValue(ShortMemberValue paramShortMemberValue);
  
  void visitStringMemberValue(StringMemberValue paramStringMemberValue);
  
  void visitClassMemberValue(ClassMemberValue paramClassMemberValue);
}


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\bytecode\annotation\MemberValueVisitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */