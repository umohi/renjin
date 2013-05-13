package org.renjin.gcc.gimple.type;

public class GimpleStructType extends AbstractGimpleType {
  private final String name;

  public GimpleStructType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return "struct " + name;
  }
}
