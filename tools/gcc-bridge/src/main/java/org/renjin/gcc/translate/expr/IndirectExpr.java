package org.renjin.gcc.translate.expr;

import org.renjin.gcc.gimple.type.IndirectType;
import org.renjin.gcc.jimple.JimpleExpr;

public interface IndirectExpr extends Expr {

  JimpleExpr backingArray();
  
  JimpleExpr backingArrayIndex();

  @Override
  IndirectType type();
}
