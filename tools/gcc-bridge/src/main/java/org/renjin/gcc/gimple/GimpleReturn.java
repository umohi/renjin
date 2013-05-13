package org.renjin.gcc.gimple;

import org.renjin.gcc.gimple.expr.GimpleExpr;

public class GimpleReturn extends GimpleIns {
  private GimpleExpr value;

  public void setValue(GimpleExpr value) {
    this.value = value;
  }

  public GimpleExpr getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "gimple_return <" + value + ">";
  }

  @Override
  public void visit(GimpleVisitor visitor) {
    visitor.visitReturn(this);
  }
}
