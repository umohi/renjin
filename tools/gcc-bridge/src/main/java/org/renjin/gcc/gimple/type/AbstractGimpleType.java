package org.renjin.gcc.gimple.type;

public class AbstractGimpleType implements GimpleType {
  private int size;

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }
}
