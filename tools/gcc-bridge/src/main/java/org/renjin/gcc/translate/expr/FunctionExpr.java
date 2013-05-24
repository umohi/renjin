package org.renjin.gcc.translate.expr;

import org.renjin.gcc.gimple.type.GimpleType;
import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.jimple.JimpleType;
import org.renjin.gcc.translate.FunctionContext;
import org.renjin.gcc.translate.call.MethodRef;

/**
 * An expression that evaluates to a function
 */
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

  public MethodRef getMethodRef() {
    return methodRef;
  }

  @Override
  public Expr addressOf() {
    return new Pointer();
  }


  public class Pointer extends AbstractExpr implements FunctionPtrExpr {

    @Override
    public GimpleType type() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Expr memref() {
      return FunctionExpr.this;
    }

    @Override
    public JimpleExpr invokerReference(FunctionContext context) {
      JimpleType invokerType = context.getTranslationContext().getInvokerType(getMethodRef());
      String ptr = context.declareTemp(invokerType);
      context.getBuilder().addStatement(ptr + " = new " + invokerType);
      context.getBuilder().addStatement("specialinvoke " + ptr + ".<" + invokerType + ": void <init>()>()");
      return new JimpleExpr(ptr);
    }

    @Override
    public String toString() {
      return "&" + methodRef.getMethodName();
    }
    
  }
  
}

