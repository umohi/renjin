package org.renjin.gcc.gimple.expr;

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
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("\"");
    for(int i=0;i!=value.length();++i) {
      int cp = value.codePointAt(i);
      if(cp >= 32 && cp <= 126) {
        sb.appendCodePoint(cp);
      } else {
        sb.append("\\u");
        sb.append(String.format("%04x", cp));
      }
    }
    sb.append("\"");
    return sb.toString();
  }
}
