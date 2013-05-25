package org.renjin.gcc.translate;

import org.renjin.gcc.gimple.type.*;
import org.renjin.gcc.gimple.type.GimpleIntegerType;
import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.jimple.JimpleType;
import org.renjin.gcc.translate.expr.ImExpr;
import org.renjin.gcc.translate.expr.PrimitiveLValue;
import org.renjin.gcc.translate.type.PrimitiveTypes;

public class PrimitiveAssignment {

  public static void assign(FunctionContext context, ImExpr lhs, ImExpr rhs) {
    if(lhs instanceof PrimitiveLValue && typesCompatible(lhs.type(), rhs.type())) {
      JimpleExpr jimpleExpr = rhs.translateToPrimitive(context);
      if(requiresCast(lhs, rhs)) {
        jimpleExpr = JimpleExpr.cast(jimpleExpr, PrimitiveTypes.get(lhs.type()));
      }
      ((PrimitiveLValue) lhs).writePrimitiveAssignment(jimpleExpr);
    } else {
      throw new UnsupportedOperationException(String.format("Unable to assign %s to %s", rhs, lhs));
    }
  }

  private static boolean requiresCast(ImExpr lhs, ImExpr rhs) {
    // we need to insert a cast if the *jimple* type of the arguments
    // differ. (this is different than the gimple type
    // not matching, because we ignore signed/unsigned right now)

    JimpleType ltype = PrimitiveTypes.get(lhs.type());
    JimpleType rtype = PrimitiveTypes.get(rhs.type());
    
    return !ltype.equals(rtype);
  }

  private static boolean typesCompatible(GimpleType lhs, GimpleType rhs) {
    if(lhs instanceof GimpleIntegerType) {
      return (rhs instanceof GimpleIntegerType || rhs instanceof GimpleBooleanType) &&
          precision(lhs) >= precision(rhs);
          
    } else if(lhs instanceof GimpleRealType) {
      return rhs instanceof GimplePrimitiveType && precision(lhs) >= precision(rhs);
      
    } else {
      return lhs.equals(rhs);
    }
  }

  private static int precision(GimpleType type) {
    if(type instanceof GimpleIntegerType) {
      return ((GimpleIntegerType) type).getPrecision();
    } else if(type instanceof GimpleRealType) {
      return ((GimpleRealType) type).getPrecision();
    } else if(type instanceof GimpleBooleanType) {
      return 1;
    } else {
      throw new UnsupportedOperationException(type.toString());
    }
  }
  
}
