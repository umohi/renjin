package org.renjin.gcc.translate.expr;

import org.renjin.gcc.gimple.type.GimpleType;
import org.renjin.gcc.translate.call.MethodRef;

public class FunctionExpr extends AbstractExpr {
  
  private MethodRef methodRef;

  public FunctionExpr(MethodRef methodRef) {
    super();
    this.methodRef = methodRef;
  }

  @Override
  public GimpleType type() {
    throw new UnsupportedOperationException();
  }
  

  @Override
  public Expr addressOf() {
    return new Pointer();
  }


  public class Pointer extends AbstractExpr {

    @Override
    public GimpleType type() {
      throw new UnsupportedOperationException();
    }
    
    @Override
    public String toString() {
      return "&" + methodRef.getMethodName();
    }
    
  }
  
}

