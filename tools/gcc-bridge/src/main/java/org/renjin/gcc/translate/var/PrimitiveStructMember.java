package org.renjin.gcc.translate.var;

import org.renjin.gcc.gimple.type.GimpleType;
import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.translate.FunctionContext;
import org.renjin.gcc.translate.expr.AbstractExpr;
import org.renjin.gcc.translate.expr.Expr;
import org.renjin.gcc.translate.struct.Struct;

public class PrimitiveStructMember extends AbstractExpr {

  private Struct struct;
  private String jimpleName;
  private String member;

  public PrimitiveStructMember(Struct struct, String jimpleName, String member) {
    this.struct = struct;
    this.jimpleName = jimpleName;
    this.member = member;
  }

  @Override
  public GimpleType type() {
    throw new UnsupportedOperationException();
  }

}
