package org.renjin.gcc.translate.var;

import org.renjin.gcc.gimple.expr.GimpleAddressOf;
import org.renjin.gcc.gimple.expr.GimpleConstant;
import org.renjin.gcc.gimple.expr.GimpleExpr;
import org.renjin.gcc.gimple.expr.GimpleFunctionRef;
import org.renjin.gcc.gimple.type.FunctionType;
import org.renjin.gcc.gimple.type.GimpleType;
import org.renjin.gcc.gimple.type.PointerType;
import org.renjin.gcc.jimple.Jimple;
import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.jimple.JimpleType;
import org.renjin.gcc.translate.FunctionContext;
import org.renjin.gcc.translate.assign.NullAssignable;
import org.renjin.gcc.translate.call.MethodRef;
import org.renjin.gcc.translate.types.FunPtrJimpleType;

public class FunPtrVar extends Variable implements NullAssignable {

  private String jimpleName;
  private FunctionContext context;
  private JimpleType jimpleType;
  private FunctionType functionType;

  public FunPtrVar(FunctionContext context, String gimpleName, FunctionType type) {
    this.context = context;
    this.jimpleName = Jimple.id(gimpleName);
      this.jimpleType = new FunPtrJimpleType(context.getTranslationContext().getFunctionPointerInterfaceName(type));
    this.functionType = type;
    
    context.getBuilder().addVarDecl(jimpleType, jimpleName);
  }
   

  @Override
  public GimpleType getGimpleType() {
    return new PointerType(functionType);
  }



  private void assignPointer(GimpleExpr param) {
    if (param instanceof GimpleAddressOf) {
      GimpleExpr value = ((GimpleAddressOf) param).getValue();
      if(value instanceof GimpleFunctionRef) { 
        assignNewInvoker((GimpleFunctionRef) value);
      } else {
        throw new UnsupportedOperationException(param.toString());
      }
      // } else if(param instanceof GimpleVar) {
      // assignExistingPointer((GimpleVar) param);

    } else {
      throw new UnsupportedOperationException(param.toString());
    }
  }

  private void assignNewInvoker(GimpleFunctionRef param) {
    MethodRef method = context.getTranslationContext().resolveMethod(param.getName());
    JimpleType invokerType = context.getTranslationContext().getInvokerType(method);
    String ptr = context.declareTemp(invokerType);
    context.getBuilder().addStatement(ptr + " = new " + invokerType);
    context.getBuilder().addStatement("specialinvoke " + ptr + ".<" + invokerType + ": void <init>()>()");
    context.getBuilder().addStatement(jimpleName + " = " + ptr);
  }

  private void assignNull(GimpleExpr gimpleExpr) {
    if (!(gimpleExpr instanceof GimpleConstant)) {
      throw new UnsupportedOperationException("Expected GimpleConstant, got " + gimpleExpr);
    }
    Object value = ((GimpleConstant) gimpleExpr).getValue();
    if (!(value instanceof Number) || ((Number) value).intValue() != 0) {
      throw new UnsupportedOperationException("Can only assign 0 to function pointer");
    }
    context.getBuilder().addStatement(Jimple.id(jimpleName) + " = null");
  }
  
  

  @Override
  public void setToNull() {
    context.getBuilder().addStatement(Jimple.id(jimpleName) + " = null"); 
  }


  public JimpleExpr getJimpleVariable() {
    return new JimpleExpr(jimpleName);
  }

  @Override
  public JimpleExpr returnExpr() {
    return new JimpleExpr(jimpleName);
  }
}
