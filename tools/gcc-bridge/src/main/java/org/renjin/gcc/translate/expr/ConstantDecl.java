package org.renjin.gcc.translate.expr;

import org.renjin.gcc.gimple.expr.GimpleConstant;
import org.renjin.gcc.gimple.type.GimpleType;

public class ConstantDecl extends AbstractExpr {

  private GimpleConstant value;
  
  @Override
  public GimpleType type() {
    return value.getType();
  }

}
