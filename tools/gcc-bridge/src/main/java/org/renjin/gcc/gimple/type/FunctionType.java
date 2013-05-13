package org.renjin.gcc.gimple.type;

import java.util.List;

import com.google.common.collect.Lists;

public class FunctionType {
  private GimpleType returnType;
  private List<GimpleType> argumentTypes = Lists.newArrayList();

  public GimpleType getReturnType() {
    return returnType;
  }

  public void setReturnType(GimpleType returnType) {
    this.returnType = returnType;
  }

  public List<GimpleType> getArgumentTypes() {
    return argumentTypes;
  }
}
