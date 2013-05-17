package org.renjin.gcc.gimple.type;

public class PointerType extends AbstractGimpleType implements IndirectType {
  private GimpleType baseType;

  public PointerType() {
    
  }
  
  public PointerType(GimpleType baseType) {
    this.baseType = baseType;
  }

  @Override
  public <X extends GimpleType> X getBaseType() {
    return (X) baseType;
  }

  public void setBaseType(GimpleType baseType) {
    this.baseType = baseType;
  }

  @Override
  public String toString() {
    return baseType.toString() + "*";
  }

  @Override
  public boolean isPointerTo(Class<? extends GimpleType> clazz) {
    return clazz.isAssignableFrom(baseType.getClass());
  }
}
