package org.renjin.gcc.translate.var;

import org.renjin.gcc.gimple.type.GimpleRecordType;
import org.renjin.gcc.gimple.type.GimpleType;
import org.renjin.gcc.jimple.Jimple;
import org.renjin.gcc.translate.FunctionContext;
import org.renjin.gcc.translate.expr.AbstractImExpr;
import org.renjin.gcc.translate.expr.ImExpr;
import org.renjin.gcc.translate.type.struct.ImRecordType;

public class StructPtrVar extends AbstractImExpr implements Variable {

  private GimpleRecordType type;
  private ImRecordType struct;
  private String gimpleName;
  private String jimpleName;
  private FunctionContext context;

  public StructPtrVar(FunctionContext context, String gimpleName, ImRecordType struct) {
    this.struct = struct;
    this.gimpleName = gimpleName;
    this.jimpleName = Jimple.id(gimpleName);
    this.context = context;

    context.getBuilder().addVarDecl(struct.getJimpleType(), jimpleName);
  }

  @Override
  public void writeAssignment(FunctionContext context, ImExpr rhs) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GimpleType type() {
    return type;
  }
}
