package org.renjin.gcc.translate.assign;

import org.renjin.gcc.translate.FunctionContext;
import org.renjin.gcc.translate.expr.Expr;
import org.renjin.gcc.translate.expr.StringConstant;
import org.renjin.gcc.translate.var.PrimitivePtrVar;

public class StringToPrimitivePtrVarAssigner implements Assigner {

  @Override
  public boolean assign(FunctionContext context, Expr lhs, Expr rhs) {
    if(lhs instanceof PrimitivePtrVar && rhs instanceof StringConstant.Pointer) {
      PrimitivePtrVar ptrVar = (PrimitivePtrVar) lhs;
      StringConstant.Pointer pointer = (StringConstant.Pointer) rhs;
      ptrVar.assignStringConstant(pointer.stringValue());
      return true;
    }
    return false;
  }

}
