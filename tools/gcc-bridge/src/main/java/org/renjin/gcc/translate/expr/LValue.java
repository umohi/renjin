package org.renjin.gcc.translate.expr;

import org.renjin.gcc.translate.FunctionContext;

/**
 * An intermediate expression to which other expressions
 * can be assigned
 */
public interface LValue extends Expr {

  /**
   * Writes the jimple statement necessary to assign {@code rhs} to this
   * value.
   *
   * @param context
   * @param rhs
   */
  void writeAssignment(FunctionContext context, Expr rhs);

}
