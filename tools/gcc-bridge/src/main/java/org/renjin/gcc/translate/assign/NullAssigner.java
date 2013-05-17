package org.renjin.gcc.translate.assign;

import org.renjin.gcc.translate.FunctionContext;
import org.renjin.gcc.translate.expr.Expr;
import org.renjin.gcc.translate.expr.PrimitiveConstant;
import org.renjin.gcc.translate.var.FunPtrVar;

/**
 * Assigns 0 to a function pointer
 *
 */
public class NullAssigner implements Assigner {

  @Override
  public boolean assign(FunctionContext context, Expr lhs, Expr rhs) {
    if(lhs instanceof FunPtrVar && isNull(rhs)) {
      ((NullAssignable)lhs).setToNull();
      return true;
    }
    return false;
  }

  private boolean isNull(Expr rhs) {
    if( rhs instanceof PrimitiveConstant) {
      Object value = ((PrimitiveConstant) rhs).getConstantValue();
      if(value instanceof Integer) {
        return ((Integer)value) == 0;
      }
    }
    return false;
  }

}
