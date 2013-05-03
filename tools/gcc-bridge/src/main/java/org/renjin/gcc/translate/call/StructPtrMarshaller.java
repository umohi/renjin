package org.renjin.gcc.translate.call;

import org.renjin.gcc.gimple.expr.GimpleAddressOf;
import org.renjin.gcc.gimple.expr.GimpleExpr;
import org.renjin.gcc.gimple.expr.GimpleVariableRef;
import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.translate.FunctionContext;
import org.renjin.gcc.translate.var.StructVar;
import org.renjin.gcc.translate.var.Variable;

public class StructPtrMarshaller extends ParamMarshaller {

  @Override
  public JimpleExpr marshall(FunctionContext context, GimpleExpr expr,
      CallParam param) {
   
    if(expr instanceof GimpleAddressOf) {
      GimpleExpr innerValue = ((GimpleAddressOf) expr).getValue();
      if(innerValue instanceof GimpleVariableRef) {
        Variable var = context.lookupVar(innerValue);
        if(var instanceof StructVar) {
          StructVar structVar = (StructVar) var;
          return structVar.wrapPointer();
        }
      }
    }
    throw new CannotMarshallException();
 
  }

}
