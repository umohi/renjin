package org.renjin.gcc.translate.call;

import org.renjin.gcc.gimple.expr.GimpleExpr;
import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.translate.FunctionContext;
import org.renjin.gcc.translate.expr.Expr;

public abstract class ParamMarshaller {

  public abstract JimpleExpr marshall(FunctionContext context, Expr expr, CallParam param);

}
