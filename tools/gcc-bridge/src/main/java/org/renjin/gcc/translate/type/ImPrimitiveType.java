package org.renjin.gcc.translate.type;

import org.renjin.gcc.gimple.type.GimplePointerType;
import org.renjin.gcc.gimple.type.GimplePrimitiveType;
import org.renjin.gcc.jimple.JimpleType;
import org.renjin.gcc.translate.FunctionContext;
import org.renjin.gcc.translate.VarUsage;
import org.renjin.gcc.translate.var.PrimitiveHeapVar;
import org.renjin.gcc.translate.var.PrimitiveStackVar;
import org.renjin.gcc.translate.var.Variable;


public class ImPrimitiveType extends ImType {

  private GimplePrimitiveType type;
  private Class jimpleType;

  public ImPrimitiveType(GimplePrimitiveType type) {
    this.type = type;
  }

  @Override
  public JimpleType paramType() {
    return asJimple();
  }

  @Override
  public JimpleType returnType() {
    return PrimitiveTypes.get(type);
  }

  private JimpleType asJimple() {
    return PrimitiveTypes.get(type);
  }

  @Override
  public ImType pointerType() {
    return new ImPrimitivePtrType(new GimplePointerType(type));
  }

  @Override
  public Variable createLocalVariable(FunctionContext functionContext, String gimpleName, VarUsage usage) {
    if (usage.isAddressed()) {
      return new PrimitiveHeapVar(functionContext, type, gimpleName);
    } else {
      return new PrimitiveStackVar(functionContext, type, gimpleName);
    }
  }
}
