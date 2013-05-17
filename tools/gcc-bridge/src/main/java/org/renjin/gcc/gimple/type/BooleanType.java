package org.renjin.gcc.gimple.type;

public class BooleanType extends PrimitiveType {

  @Override
  public String toString() {
    return "bool";
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof BooleanType;
  }

  @Override
  public int hashCode() {
    return 1;
  }
  
  
}
