package org.renjin.gcc.translate.var;

import org.renjin.gcc.gimple.type.GimpleType;
import org.renjin.gcc.gimple.type.PrimitiveType;
import org.renjin.gcc.jimple.Jimple;
import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.translate.FunctionContext;
import org.renjin.gcc.translate.assign.PrimitiveAssignable;
import org.renjin.gcc.translate.types.PrimitiveTypes;

/**
 * Writes jimple instructions to store and retrieve a single primitive numeric
 * value in a local JVM variable, allocated on the stack.
 */
public class PrimitiveStackVar extends Variable implements PrimitiveAssignable {

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
  public JimpleExpr wrapPointer() {
    throw new UnsupportedOperationException(jimpleName + " is not addressable, stored on stack");
  }

  @Override
  public String toString() {
    return "stack:" + jimpleName;
  }

  @Override
  public void assignPrimitiveValue(JimpleExpr expr) {
    context.getBuilder().addStatement(jimpleName + " = " + expr); 
  }
  
  @Override
  public JimpleExpr asPrimitiveValue(FunctionContext context) {
    return new JimpleExpr(jimpleName);
  }

  @Override
  public PrimitiveType type() {
    return type;
  }

  @Override
  public JimpleExpr returnExpr() {
    return new JimpleExpr(jimpleName);
  }
}
