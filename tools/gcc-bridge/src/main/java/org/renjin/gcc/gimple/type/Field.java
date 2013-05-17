package org.renjin.gcc.gimple.type;

public class Field {
  private String name;
  private GimpleType type;
  
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public GimpleType getType() {
    return type;
  }
  public void setType(GimpleType type) {
    this.type = type;
  }
  
  
}
