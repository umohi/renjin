package org.renjin.gcc.translate.call;

import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.translate.FunctionContext;
import org.renjin.gcc.translate.PtrWrapperUtils;
import org.renjin.gcc.translate.expr.Expr;
import org.renjin.gcc.translate.expr.IndirectExpr;

public class WrappedPtrParamMarshaller extends ParamMarshaller {

  @Override
  public JimpleExpr marshall(FunctionContext context, Expr expr, CallParam param) {

    if (param instanceof WrappedPtrCallParam && expr instanceof IndirectExpr) {
      // TODO: check types
      return PtrWrapperUtils.wrapPointer(context, (IndirectExpr)expr);
    }
    throw new CannotMarshallException();

  }

}
