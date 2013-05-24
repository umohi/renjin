package org.renjin.gcc.translate.expr;

import org.renjin.gcc.gimple.expr.GimpleConstant;
import org.renjin.gcc.gimple.expr.GimpleIntegerConstant;
import org.renjin.gcc.gimple.expr.GimpleRealConstant;
import org.renjin.gcc.gimple.type.BooleanType;
import org.renjin.gcc.gimple.type.GimpleType;
import org.renjin.gcc.gimple.type.PrimitiveType;
import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.translate.FunctionContext;
import org.renjin.gcc.translate.TypeChecker;
import org.renjin.gcc.translate.var.PrimitiveHeapVar;

/**
 * An expression that evaluations to a constant primitive value
 */
public class PrimitiveConstant extends AbstractExpr {

  private FunctionContext context;
  private GimpleConstant constant;
  
  
  public PrimitiveConstant(FunctionContext context, GimpleConstant constant) {
    super();
    this.context = context;
    this.constant = constant;
  }

  @Override
  public JimpleExpr translateToPrimitive(FunctionContext context) {
    if(TypeChecker.isInt(constant.getType()) || constant.getType() instanceof BooleanType) {
      return JimpleExpr.integerConstant(((GimpleIntegerConstant) constant).getValue());
    } else if(TypeChecker.isLong(constant.getType())) {
      return JimpleExpr.longConstant(((GimpleIntegerConstant) constant).getValue());
    } else if(TypeChecker.isDouble(constant.getType())) {
      return JimpleExpr.doubleConstant(((GimpleRealConstant)constant).getValue());
    } else {
      throw new UnsupportedOperationException();
    }
  }

  @Override
  public GimpleType type() {
    return constant.getType();
  }

  
  public Object getConstantValue() {
    return constant.getValue();
  }
  
  @Override
  public String toString() {
    return constant.toString();
  }

  @Override
  public Expr addressOf() {
    // in order to provide an address, we'll create a heap variable on the fly
    PrimitiveHeapVar var = new PrimitiveHeapVar(context, (PrimitiveType) constant.getType(),
            "__constant" + System.identityHashCode(this));
    var.writePrimitiveAssignment(translateToPrimitive(context));

    return var.addressOf();
  }

  @Override
  public boolean isNull() {
    if(getConstantValue() instanceof Number) {
      Number value = (Number) getConstantValue();
      return value.intValue() == 0;
    }
    return false;
  }
}
