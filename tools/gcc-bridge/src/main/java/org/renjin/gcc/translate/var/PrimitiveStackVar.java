package org.renjin.gcc.translate.var;

import org.renjin.gcc.gimple.type.PrimitiveType;
import org.renjin.gcc.jimple.Jimple;
import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.translate.FunctionContext;
import org.renjin.gcc.translate.assign.PrimitiveAssignment;
import org.renjin.gcc.translate.expr.Expr;
import org.renjin.gcc.translate.expr.LValue;
import org.renjin.gcc.translate.expr.PrimitiveLValue;
import org.renjin.gcc.translate.types.PrimitiveTypes;

/**
 * Writes jimple instructions to store and retrieve a single primitive numeric
 * value in a local JVM variable, allocated on the stack.
 */
public class PrimitiveStackVar extends Variable implements PrimitiveLValue, LValue {

  private FunctionContext context;
  private String jimpleName;
  private PrimitiveType type;

  public PrimitiveStackVar(FunctionContext context, PrimitiveType type, String gimpleName) {
    this.context = context;
    this.type = type;
    this.jimpleName = Jimple.id(gimpleName);

    context.getBuilder().addVarDecl(PrimitiveTypes.get(type), jimpleName);
  }

  @Override
  public String toString() {
    return "stack:" + jimpleName;
  }

  @Override
  public void writePrimitiveAssignment(JimpleExpr expr) {
    context.getBuilder().addStatement(jimpleName + " = " + expr); 
  }

  @Override
  public void writeAssignment(FunctionContext context, Expr rhs) {
    PrimitiveAssignment.assign(context, this, rhs);
  }

  @Override
  public JimpleExpr translateToPrimitive(FunctionContext context) {
    return new JimpleExpr(jimpleName);
  }

  @Override
  public PrimitiveType type() {
    return type;
  }

}
