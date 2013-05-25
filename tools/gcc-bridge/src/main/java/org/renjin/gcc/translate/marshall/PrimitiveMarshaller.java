package org.renjin.gcc.translate.marshall;


import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.jimple.JimpleType;
import org.renjin.gcc.translate.FunctionContext;
import org.renjin.gcc.translate.expr.ImExpr;
import org.renjin.gcc.translate.expr.ImIndirectExpr;

public class PrimitiveMarshaller implements Marshaller {

  private JimpleType targetType;

  public PrimitiveMarshaller(JimpleType targetType) {
    this.targetType = targetType;
  }

  @Override
  public JimpleExpr marshall(FunctionContext context, ImExpr expr) {
    
    // EXPERIMENT:
    // Allow implicit referencing of pointers to pass arguments by value
    if(expr instanceof ImIndirectExpr) {
      ImIndirectExpr ptr = (ImIndirectExpr) expr;
      return context.declareTemp(targetType, ptr.memref().translateToPrimitive(context));
    }

    // TODO: casting
    return expr.translateToPrimitive(context);
  }
}
