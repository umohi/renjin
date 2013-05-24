package org.renjin.gcc.translate.call;


import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.jimple.JimpleType;
import org.renjin.gcc.translate.FunctionContext;
import org.renjin.gcc.translate.expr.Expr;
import org.renjin.gcc.translate.marshall.Marshallers;

/**
 * A simple call parameter
 */
public class SimpleParam implements CallParam {
  private final JimpleType type;

  public SimpleParam(JimpleType type) {
    this.type = type;
  }

  @Override
  public JimpleExpr marshall(FunctionContext context, Expr expr) {
    return Marshallers.marshall(context, expr, type);
  }
}
