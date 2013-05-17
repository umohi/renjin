package org.renjin.gcc.translate.call;

import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.translate.FunctionContext;
import org.renjin.gcc.translate.expr.Expr;

public class PrimitiveParamMarshaller extends ParamMarshaller {

  @Override
  public JimpleExpr marshall(FunctionContext context, Expr expr, CallParam param) {
    if (param instanceof PrimitiveCallParam) {
      PrimitiveCallParam primitiveParam = (PrimitiveCallParam) param;

      return expr.asPrimitiveValue(context);
    } else {
      throw new CannotMarshallException();
    }
  }
}
