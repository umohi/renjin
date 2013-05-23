package org.renjin.gcc.translate.expr;

import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.translate.FunctionContext;

public abstract class AbstractExpr implements Expr {
  
  @Override
  public Expr addressOf() {
    throw new UnsupportedOperationException(this + " is not addressable");
  }
  
  @Override
  public Expr value() {
    throw new UnsupportedOperationException();
  }
  
  @Override
  public JimpleExpr asPrimitiveValue(FunctionContext context) {
    throw new UnsupportedOperationException();
  }
  
  @Override
  public void assign(Expr expr) {
    throw new UnsupportedOperationException();
  }
    
  @Override
  public Expr elementAt(Expr index) {
    throw new UnsupportedOperationException(this + " is not an array");
  }
  
  @Override
  public Expr pointerPlus(Expr resolveExpr) {
    throw new UnsupportedOperationException("Expression " + this + "  does not support pointer arithmatic");
  }

  @Override
  public Expr member(String member) {
    throw new UnsupportedOperationException("Expression " + this + "  does not support members");
  }

}
