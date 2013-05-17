package org.renjin.gcc.translate.var;

import java.util.List;

import org.renjin.gcc.gimple.GimpleOp;
import org.renjin.gcc.gimple.expr.GimpleExpr;
import org.renjin.gcc.gimple.type.GimpleType;
import org.renjin.gcc.gimple.type.IndirectType;
import org.renjin.gcc.gimple.type.PointerType;
import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.translate.FunctionContext;
import org.renjin.gcc.translate.expr.AbstractExpr;
import org.renjin.gcc.translate.expr.Expr;
import org.renjin.gcc.translate.expr.IndirectExpr;

public abstract class Variable extends AbstractExpr {


  public GimpleType getGimpleType() {
    throw new UnsupportedOperationException("not implemented in " + getClass().getSimpleName());
  }

  public void initFromParameter() {

  }

  public abstract JimpleExpr returnExpr();

  @Override
  public Expr addressOf() {
    throw new UnsupportedOperationException(toString() + " (" + getClass().getSimpleName() + ")");
  }

  @Override
  public Expr value() {
    throw new UnsupportedOperationException(getClass().getSimpleName() + ": " + toString());
  }

  @Override
  public JimpleExpr asPrimitiveValue(FunctionContext context) {
    throw new UnsupportedOperationException(getClass().getSimpleName());

  }

  @Override
  public GimpleType type() {
    return getGimpleType();
  }

  public JimpleExpr wrapPointer() {
    throw new UnsupportedOperationException();
  }


}
