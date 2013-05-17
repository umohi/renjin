package org.renjin.gcc.translate.call;

import org.renjin.gcc.gimple.expr.GimpleLValue;
import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.jimple.JimpleType;
import org.renjin.gcc.translate.FunctionContext;
import org.renjin.gcc.translate.assign.PrimitiveAssignable;
import org.renjin.gcc.translate.expr.Expr;

public class PrimitiveUnmarshaller extends CallUnmarshaller {

  public boolean unmarshall(FunctionContext context, GimpleLValue lhs, JimpleType type, JimpleExpr callExpr) {
    if (type.isPrimitive()) {
      Expr lhsExpr = context.resolveExpr(lhs);
      if(lhsExpr instanceof PrimitiveAssignable) {
        ((PrimitiveAssignable) lhsExpr).assignPrimitiveValue(callExpr);
        return true;
      }
    }
    return false;
  }

}
