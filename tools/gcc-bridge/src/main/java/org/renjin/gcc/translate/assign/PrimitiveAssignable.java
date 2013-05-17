package org.renjin.gcc.translate.assign;

import org.renjin.gcc.jimple.JimpleExpr;

public interface PrimitiveAssignable {

  void assignPrimitiveValue(JimpleExpr expr);
}
