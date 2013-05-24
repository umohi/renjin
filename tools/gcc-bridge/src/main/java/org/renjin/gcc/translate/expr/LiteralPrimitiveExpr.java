package org.renjin.gcc.translate.expr;


import org.renjin.gcc.gimple.type.BooleanType;
import org.renjin.gcc.gimple.type.GimpleType;
import org.renjin.gcc.gimple.type.IntegerType;
import org.renjin.gcc.gimple.type.RealType;
import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.jimple.JimpleType;
import org.renjin.gcc.translate.FunctionContext;

/**
 * Represents a primitive expression that has already been translated to Jimple
 */
public class LiteralPrimitiveExpr extends AbstractExpr {

  private GimpleType type;
  private JimpleExpr expr;

  public LiteralPrimitiveExpr(JimpleExpr expr, JimpleType type) {
    this.expr = expr;
    if(type.equals(JimpleType.BOOLEAN)) {
      this.type = new BooleanType();
    } else if(type.equals(JimpleType.INT)) {
      this.type = new IntegerType(32);
    } else if(type.equals(JimpleType.DOUBLE)) {
      this.type = new RealType(64);
    } else {
      throw new UnsupportedOperationException();
    }
  }

  @Override
  public GimpleType type() {
    return type;
  }

  @Override
  public JimpleExpr translateToPrimitive(FunctionContext context) {
    return expr;
  }
}
