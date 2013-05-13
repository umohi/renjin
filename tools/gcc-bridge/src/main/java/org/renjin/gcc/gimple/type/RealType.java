package org.renjin.gcc.gimple.type;

public class RealType extends PrimitiveType {
  private int precision;

  /**
   * 
   * @return The number of bits of precision
   */
  public int getPrecision() {
    return precision;
  }

  public void setPrecision(int precision) {
    this.precision = precision;
  }

  @Override
  public String toString() {
    return "real" + precision;
  }

}
