package org.renjin.gcc.translate.var;

import org.renjin.gcc.gimple.type.GimpleFunctionType;
import org.renjin.gcc.gimple.type.GimplePointerType;
import org.renjin.gcc.gimple.type.GimpleType;
import org.renjin.gcc.jimple.Jimple;
import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.jimple.JimpleType;
import org.renjin.gcc.translate.FunctionContext;
import org.renjin.gcc.translate.expr.Expr;
import org.renjin.gcc.translate.expr.FunctionPtrExpr;
import org.renjin.gcc.translate.expr.LValue;
import org.renjin.gcc.translate.types.FunPtrJimpleType;

/**
 * A variable which holds a pointer to a function
 */
public class FunPtrVar extends Variable implements LValue, FunctionPtrExpr {

  private String jimpleName;
  private FunctionContext context;
  private JimpleType jimpleType;
  private GimpleFunctionType functionType;

  public FunPtrVar(FunctionContext context, String gimpleName, GimpleFunctionType type) {
    this.context = context;
    this.jimpleName = Jimple.id(gimpleName);
      this.jimpleType = new FunPtrJimpleType(context.getTranslationContext().getFunctionPointerInterfaceName(type));
    this.functionType = type;
    
    context.getBuilder().addVarDecl(jimpleType, jimpleName);
  }

  @Override
  public GimpleType type() {
    return new GimplePointerType(functionType);
  }

  @Override
  public void writeAssignment(FunctionContext context, Expr rhs) {
    if(rhs.isNull()) {
      context.getBuilder().addStatement(Jimple.id(jimpleName) + " = null");
    } else if (rhs instanceof FunctionPtrExpr) {
      context.getBuilder().addStatement(jimpleName + " = " + ((FunctionPtrExpr) rhs).invokerReference(context));
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
