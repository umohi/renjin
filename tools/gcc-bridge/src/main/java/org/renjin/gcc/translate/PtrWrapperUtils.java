package org.renjin.gcc.translate;

import org.renjin.gcc.gimple.type.GimplePrimitiveType;
import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.jimple.JimpleType;
import org.renjin.gcc.translate.expr.ArrayRef;
import org.renjin.gcc.translate.expr.IndirectExpr;
import org.renjin.gcc.translate.types.PrimitiveTypes;

public class PtrWrapperUtils {

  public static JimpleExpr wrapPointer(FunctionContext context, IndirectExpr ptr) {
    ArrayRef ref = ptr.translateToArrayRef(context);
    GimplePrimitiveType baseType = (GimplePrimitiveType) ptr.type().getBaseType();
    JimpleType wrapperType = PrimitiveTypes.getWrapperType(baseType);
    String tempWrapper = context.declareTemp(wrapperType);
    context.getBuilder().addStatement(tempWrapper + " = new " + wrapperType);
    context.getBuilder().addStatement(
        "specialinvoke " + tempWrapper + ".<" + wrapperType + ": void <init>("
            + PrimitiveTypes.getArrayType(baseType) + ", int)>(" + ref.getArrayExpr() + ", " + ref.getIndexExpr() + ")");

    return new JimpleExpr(tempWrapper);
  }
}
