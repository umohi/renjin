package org.renjin.gcc.translate.expr;

import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.jimple.JimpleType;
import org.renjin.gcc.translate.FunctionContext;


public class JvmExprs {


  /**
   *
   * @param expr
   * @param type
   * @param simple
   * @return
   */
  public static Expr toExpr(FunctionContext context, JimpleExpr expr, JimpleType type, boolean simple) {
    if(type.isPrimitive()) {
      return new LiteralPrimitiveExpr(expr, type);
    } else if(type.isPointerWrapper()) {
      if(simple) {
        return new WrappedPtrExpr(expr, type);
      } else {
        String tmp = context.declareTemp(type);
        context.getBuilder().addStatement(tmp + " = " + expr);
        return new WrappedPtrExpr(new JimpleExpr(tmp), type);
      }
    } else {
      throw new UnsupportedOperationException(type.toString());
    }
  }
}
