package org.renjin.gcc.translate.type;

import org.renjin.gcc.gimple.type.GimpleArrayType;
import org.renjin.gcc.gimple.type.GimpleIndirectType;
import org.renjin.gcc.gimple.type.GimplePrimitiveType;
import org.renjin.gcc.jimple.JimpleType;
import org.renjin.gcc.translate.FunctionContext;
import org.renjin.gcc.translate.VarUsage;
import org.renjin.gcc.translate.var.PrimitivePtrVar;
import org.renjin.gcc.translate.var.Variable;

public class ImPrimitivePtrType extends ImType {

  private JimpleType wrapperClass;
  private GimplePrimitiveType primitiveType;
  private GimpleIndirectType pointerType;
  private boolean pointerToArray = false;

  public ImPrimitivePtrType(GimpleIndirectType type) {
    this.pointerType = type;
    if(type.getBaseType() instanceof GimpleArrayType) {
      this.primitiveType = (GimplePrimitiveType) ((GimpleArrayType) type.getBaseType()).getComponentType();
      this.pointerToArray = true;
    } else {
      this.primitiveType = type.getBaseType();
    }
    this.wrapperClass = PrimitiveTypes.getWrapperType(primitiveType);
  }

  @Override
  public JimpleType returnType() {
    return wrapperClass;
  }

  @Override
  public JimpleType paramType() {
    return wrapperClass;
  }

  @Override
  public Variable createLocalVariable(FunctionContext functionContext, String gimpleName, VarUsage usage) {
    return new PrimitivePtrVar(functionContext, gimpleName, pointerType);
  }
}
