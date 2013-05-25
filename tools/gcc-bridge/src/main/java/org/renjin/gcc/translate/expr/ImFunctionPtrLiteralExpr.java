package org.renjin.gcc.translate.expr;

import org.renjin.gcc.gimple.type.GimpleType;
import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.translate.FunctionContext;


public class ImFunctionPtrLiteralExpr extends AbstractImExpr implements ImFunctionPtrExpr {

  private final JimpleExpr expr;

  public ImFunctionPtrLiteralExpr(JimpleExpr expr) {
    this.expr = expr;
  }

  @Override
  public GimpleType type() {
    throw new UnsupportedOperationException();
  }

  @Override
  public JimpleExpr invokerReference(FunctionContext context) {
    return expr;
  }
}
