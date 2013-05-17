package org.renjin.gcc.translate.assign;

import org.renjin.gcc.translate.FunctionContext;
import org.renjin.gcc.translate.expr.Expr;
import org.renjin.gcc.translate.var.PrimitivePtrVar;

public class PrimitivePtrAssigner implements Assigner {

  @Override
  public boolean assign(FunctionContext context, Expr lhs, Expr rhs) {
    if(lhs instanceof PrimitivePtrVar) {
      PrimitivePtrVar var = (PrimitivePtrVar) lhs;
      
      if(rhs instanceof PrimitivePtrVar) {
        var.assign((PrimitivePtrVar)rhs);
        return true;
        
      } else if(rhs instanceof PrimitivePtrVar.OffsetExpr) {
        var.assign((PrimitivePtrVar.OffsetExpr)rhs);
        return true;
      }

    }
    return false;
  }

}
