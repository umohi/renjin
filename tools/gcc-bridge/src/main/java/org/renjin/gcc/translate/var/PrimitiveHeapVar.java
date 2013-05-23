package org.renjin.gcc.translate.var;

import org.renjin.gcc.gimple.type.GimpleType;
import org.renjin.gcc.gimple.type.IndirectType;
import org.renjin.gcc.gimple.type.PointerType;
import org.renjin.gcc.gimple.type.PrimitiveType;
import org.renjin.gcc.jimple.Jimple;
import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.translate.FunctionContext;
import org.renjin.gcc.translate.assign.PrimitiveAssignable;
import org.renjin.gcc.translate.expr.AbstractExpr;
import org.renjin.gcc.translate.expr.Expr;
import org.renjin.gcc.translate.expr.IndirectExpr;
import org.renjin.gcc.translate.types.PrimitiveTypes;

/**
 * Writes jimple instructions to store and retrieve a single primitive numeric
 * value on the JVM heap, by allocating a unit array. Variables stored this way
 * can be addressed and passed by reference to other methods.
 * 
 */
public class PrimitiveHeapVar extends Variable implements PrimitiveAssignable {
  private FunctionContext context;
  private String jimpleName;
  private PrimitiveType type;

  public PrimitiveHeapVar(FunctionContext context, PrimitiveType type, String gimpleName) {
    this.context = context;
    this.jimpleName = Jimple.id(gimpleName);
    this.type = type;

    context.getBuilder().addVarDecl(PrimitiveTypes.getArrayType(type), jimpleName);
    context.getBuilder().addStatement(jimpleName + " = newarray (" + PrimitiveTypes.get(type) + ")[1]");
  }

  @Override
  public void assignPrimitiveValue(JimpleExpr expr) {
    context.getBuilder().addStatement(jimpleName + "[0] = " + expr);
  }

  @Override
  public JimpleExpr asPrimitiveValue(FunctionContext context) {
    return jimple();
  }

  @Override
  public JimpleExpr returnExpr() {
    return jimple();
  }

  public JimpleExpr jimple() {
    return new JimpleExpr(jimpleName + "[0]");
  }


  @Override
  public GimpleType type() {
    return type;
  }

  @Override
  public String toString() {
    return "heap:" + jimpleName;
  }

  @Override
  public Expr addressOf() {
    return new PointerTo();
  }

  private class PointerTo extends AbstractExpr implements IndirectExpr {

    @Override
    public JimpleExpr backingArray() {
      return new JimpleExpr(jimpleName);
    }

    @Override
    public JimpleExpr backingArrayIndex() {
      return JimpleExpr.integerConstant(0);
    }

    @Override
    public IndirectType type() {
      return new PointerType(PrimitiveHeapVar.this.type());
    }
  }
  
}
