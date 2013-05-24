package org.renjin.gcc.translate.marshall;


import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.translate.FunctionContext;
import org.renjin.gcc.translate.PtrWrapperUtils;
import org.renjin.gcc.translate.expr.Expr;
import org.renjin.gcc.translate.expr.IndirectExpr;

import java.util.Arrays;
import java.util.List;

public class PointerWrapperMarshaller implements Marshaller {
  
  @Override
  public JimpleExpr marshall(FunctionContext context, Expr expr) {
    if(expr instanceof IndirectExpr) {
      return PtrWrapperUtils.wrapPointer(context, (IndirectExpr) expr);
    }
    throw new UnsupportedOperationException(expr.toString());
  }
}
