package org.renjin.gcc.translate.var;

import org.renjin.gcc.gimple.type.GimpleIntegerType;
import org.renjin.gcc.gimple.type.GimpleType;
import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.jimple.JimpleType;
import org.renjin.gcc.translate.FunctionContext;
import org.renjin.gcc.translate.expr.AbstractImExpr;
import org.renjin.gcc.translate.expr.ImExpr;
import org.renjin.gcc.translate.expr.ImLValue;
import org.renjin.gcc.translate.type.struct.ImRecordType;

public class PrimitiveFieldExpr extends AbstractImExpr implements ImLValue {
  private String instanceName;
  private JimpleType classType;
  private String member;
  private JimpleType memberType;

  public PrimitiveFieldExpr(String instanceName, JimpleType classType,
                            String member, JimpleType memberType) {
    this.instanceName = instanceName;
    this.classType = classType;
    this.member = member;
    this.memberType = memberType;
  }

  @Override
  public GimpleType type() {
    if(memberType.equals(JimpleType.INT)) {
      return new GimpleIntegerType(32);
    } else {
      throw new UnsupportedOperationException(memberType.toString());
    }
  }

  @Override
  public JimpleExpr translateToPrimitive(FunctionContext context) {
    return new JimpleExpr(String.format("%s.<%s: %s %s>",
        instanceName,
        classType,
        memberType,
        member));
  }

  @Override
  public void writeAssignment(FunctionContext context, ImExpr rhs) {

    JimpleExpr rhsExpr = rhs.translateToPrimitive(context);
    context.getBuilder().addStatement(String.format("%s.<%s: %s %s> = %s",
        instanceName,
        classType,
        memberType,
        member,
        rhsExpr));
  }
}
