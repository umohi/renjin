package org.renjin.gcc.gimple.expr;

import org.renjin.gcc.gimple.type.ArrayType;
import org.renjin.gcc.gimple.type.GimpleType;

public class GimpleStringConstant extends GimpleConstant {

  private String value;
  
  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    return value;
  }

  @Override
  public void setType(GimpleType type) {
    if(!(type instanceof ArrayType)) {
      throw new RuntimeException("Expected array type for StringConstant, got: " + type);
    }
    super.setType(type);
  }

  @Override
  public ArrayType getType() {
    return (ArrayType) super.getType();
  }

  public String literal() {
    StringBuilder literal = new StringBuilder();
    literal.append("\"");
    for(int i=0;i!=value.length();++i) {
      int cp = value.codePointAt(i);
      if(cp == '"') {
        literal.append("\\\"");
      } else if(cp == '\\') {
        literal.append("\\\\");
      } else if(cp >= 32 && cp <= 126) {
        literal.appendCodePoint(cp);
      } else {
        literal.append(String.format("\\u%04x", cp));
      }
    }
    literal.append("\"");
    return literal.toString();
  }
  
  @Override
  public String toString() {
    return literal();
  }
}
