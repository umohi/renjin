package org.renjin.gcc.translate.assign;

import org.renjin.gcc.translate.FunctionContext;
import org.renjin.gcc.translate.expr.Expr;

/**
 * Class that knows how to move a particular kind of RHS into a
 * particular kind of LHS
 *
 */
public interface Assigner {

  /**
   * Attempts to write the assignment of rhs to lhs. 
   * @param context
   * @param lhs
   * @param rhs
   * @return true if the assignment is supported
   */
  boolean assign(FunctionContext context, Expr lhs, Expr rhs);
}
