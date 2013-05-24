package org.renjin.gcc.translate.marshall;


import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.jimple.JimpleType;
import org.renjin.gcc.translate.FunctionContext;
import org.renjin.gcc.translate.expr.Expr;

import java.util.Arrays;
import java.util.List;

public class PrimitiveMarshaller implements Marshaller {

  private JimpleType targetType;

  public PrimitiveMarshaller(JimpleType targetType) {
    this.targetType = targetType;
  }

  @Override
  public JimpleExpr marshall(FunctionContext context, Expr expr) {
    // TODO: casting
    return expr.translateToPrimitive(context);
  }
}
