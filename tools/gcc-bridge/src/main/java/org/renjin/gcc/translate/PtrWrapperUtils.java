package org.renjin.gcc.translate;

import org.renjin.gcc.gimple.type.PrimitiveType;
import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.jimple.JimpleType;
import org.renjin.gcc.translate.expr.IndirectExpr;
import org.renjin.gcc.translate.types.PrimitiveTypes;

public class PtrWrapperUtils {

  public static JimpleExpr wrapPointer(FunctionContext context, IndirectExpr ptr) {
    PrimitiveType baseType = (PrimitiveType) ptr.type().getBaseType();
    JimpleType wrapperType = PrimitiveTypes.getWrapperType(baseType);
    String tempWrapper = context.declareTemp(wrapperType);
    context.getBuilder().addStatement(tempWrapper + " = new " + wrapperType);
    context.getBuilder().addStatement(
        "specialinvoke " + tempWrapper + ".<" + wrapperType + ": void <init>("
            + PrimitiveTypes.getArrayType(baseType) + ", int)>(" + ptr.backingArray() + ", " + ptr.backingArrayIndex() + ")");

    return new JimpleExpr(tempWrapper);
  }
}
