package org.renjin.gcc.translate.var;

import org.renjin.gcc.gimple.type.GimpleType;
import org.renjin.gcc.gimple.type.RecordType;
import org.renjin.gcc.jimple.Jimple;
import org.renjin.gcc.translate.FunctionContext;
import org.renjin.gcc.translate.expr.Expr;
import org.renjin.gcc.translate.struct.Struct;

public class StructVar extends Variable {

  private RecordType type;
  private Struct struct;
  private String jimpleName;
  private FunctionContext context;

  public StructVar(FunctionContext context, String gimpleName, Struct struct) {
    this.context = context;
    this.struct = struct;
    this.jimpleName = Jimple.id(gimpleName);

    context.getBuilder().addVarDecl(struct.getJimpleType(), jimpleName);
    context.getBuilder().addStatement(jimpleName + " = new " + struct.getJimpleType());
    context.getBuilder().addStatement(
        "specialinvoke " + jimpleName + ".<" + struct.getJimpleType() + ": void <init>()>()");
  }


  @Override
  public Expr member(String member) {
    return new StructMember(struct, jimpleName, member);
  }


  @Override
  public GimpleType type() {
    return type;
  }


  @Override
  public void writeAssignment(FunctionContext context, Expr rhs) {
    throw new UnsupportedOperationException();
  }
}
