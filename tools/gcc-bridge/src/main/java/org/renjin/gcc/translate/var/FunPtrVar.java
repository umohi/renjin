package org.renjin.gcc.translate.var;

import org.renjin.gcc.gimple.type.GimpleFunctionType;
import org.renjin.gcc.gimple.type.GimplePointerType;
import org.renjin.gcc.gimple.type.GimpleType;
import org.renjin.gcc.jimple.Jimple;
import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.jimple.JimpleType;
import org.renjin.gcc.jimple.SyntheticJimpleType;
import org.renjin.gcc.translate.FunctionContext;
import org.renjin.gcc.translate.expr.AbstractImExpr;
import org.renjin.gcc.translate.expr.ImExpr;
import org.renjin.gcc.translate.expr.ImFunctionPtrExpr;
import org.renjin.gcc.translate.expr.ImLValue;

/**
 * A variable which holds a pointer to a function
 */
public class FunPtrVar extends AbstractImExpr implements Variable, ImFunctionPtrExpr {

  private String jimpleName;
  private FunctionContext context;
  private JimpleType jimpleType;
  private GimpleFunctionType functionType;

  public FunPtrVar(FunctionContext context, String gimpleName, GimpleFunctionType type) {
    this.context = context;
    this.jimpleName = Jimple.id(gimpleName);
    this.jimpleType = new SyntheticJimpleType(context.getTranslationContext().getFunctionPointerInterfaceName(type));
    this.functionType = type;
    
    context.getBuilder().addVarDecl(jimpleType, jimpleName);
  }

  @Override
  public GimpleType type() {
    return new GimplePointerType(functionType);
  }

  @Override
  public void writeAssignment(FunctionContext context, ImExpr rhs) {
    if(rhs.isNull()) {
      context.getBuilder().addStatement(Jimple.id(jimpleName) + " = null");
    } else if (rhs instanceof ImFunctionPtrExpr) {
      context.getBuilder().addStatement(jimpleName + " = " + ((ImFunctionPtrExpr) rhs).invokerReference(context));
    }
  }

  public JimpleExpr getJimpleVariable() {
    return new JimpleExpr(jimpleName);
  }

  @Override
  public JimpleExpr invokerReference(FunctionContext context) {
    return new JimpleExpr(jimpleName);
  }
}
