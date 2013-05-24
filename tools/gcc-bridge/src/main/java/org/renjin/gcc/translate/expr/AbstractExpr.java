package org.renjin.gcc.translate.expr;

import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.translate.FunctionContext;

/**
 * Provides default implementations for the {@link Expr} interface
 */
public abstract class AbstractExpr implements Expr {
  
  @Override
  public Expr addressOf() {
    throw new UnsupportedOperationException(this + " is not addressable");
  }
  
  @Override
  public Expr memref() {
    throw new UnsupportedOperationException();
  }
  
  @Override
  public JimpleExpr translateToPrimitive(FunctionContext context) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Expr elementAt(Expr index) {
    throw new UnsupportedOperationException(this + " is not an array");
  }
  
  @Override
  public Expr pointerPlus(Expr offset) {
    throw new UnsupportedOperationException("Expression " + this + "  does not support pointer arithmatic");
  }

  @Override
  public Expr member(String member) {
    throw new UnsupportedOperationException("Expression " + this + "  does not support members");
  }

  @Override
  public boolean isNull() {
    return false;
  }
}
