package org.renjin.gcc.translate.call;

import org.renjin.gcc.gimple.expr.GimpleAddressOf;
import org.renjin.gcc.gimple.expr.GimpleExpr;
import org.renjin.gcc.gimple.expr.GimpleStringConstant;

public class ParamUtils {

  public static String isStringConstant(GimpleExpr expr) {

    if (expr instanceof GimpleAddressOf) {
      GimpleExpr value = ((GimpleAddressOf) expr).getValue();
      if(value instanceof GimpleStringConstant) {
        return ((GimpleStringConstant) value).getValue();
      }
    }
    return null;
  }

}
