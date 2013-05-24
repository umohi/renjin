package org.renjin.gcc.translate.marshall;


import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.translate.FunctionContext;
import org.renjin.gcc.translate.expr.Expr;
import org.renjin.gcc.translate.expr.FunctionPtrExpr;

import java.util.List;

public class FunPtrMarshaller implements Marshaller {

  @Override
  public JimpleExpr marshall(FunctionContext context, Expr expr) {
    if(expr.isNull()) {
      return new JimpleExpr("null");
    } else if(expr instanceof FunctionPtrExpr) {
      return ((FunctionPtrExpr) expr).invokerReference(context);
    } else {
      throw new UnsupportedOperationException(expr.toString());
    }
  }
}
