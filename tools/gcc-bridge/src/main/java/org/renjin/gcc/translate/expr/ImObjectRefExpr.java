package org.renjin.gcc.translate.expr;


import org.renjin.gcc.gimple.type.GimpleType;
import org.renjin.gcc.jimple.Jimple;
import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.jimple.JimpleType;
import org.renjin.gcc.translate.FunctionContext;

public class ImObjectRefExpr extends AbstractImExpr {

  private JimpleExpr expr;
  private JimpleType type;

  public ImObjectRefExpr(JimpleExpr expr, JimpleType type) {
    this.expr = expr;
    this.type = type;
  }

  @Override
  public JimpleExpr translateToObjectReference(FunctionContext context, String className) {
    if(className.equals(type.toString())) {
      return expr;
    }
    return super.translateToObjectReference(context, className);
  }

  @Override
  public GimpleType type() {
    throw new UnsupportedOperationException();
  }
}
