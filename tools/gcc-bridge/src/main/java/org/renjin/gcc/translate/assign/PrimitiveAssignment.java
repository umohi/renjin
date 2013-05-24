package org.renjin.gcc.translate.assign;

import org.renjin.gcc.gimple.type.BooleanType;
import org.renjin.gcc.gimple.type.GimpleType;
import org.renjin.gcc.gimple.type.IntegerType;
import org.renjin.gcc.gimple.type.PrimitiveType;
import org.renjin.gcc.gimple.type.RealType;
import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.jimple.JimpleType;
import org.renjin.gcc.translate.FunctionContext;
import org.renjin.gcc.translate.expr.Expr;
import org.renjin.gcc.translate.expr.PrimitiveLValue;
import org.renjin.gcc.translate.types.PrimitiveTypes;

public class PrimitiveAssignment {

  public static void assign(FunctionContext context, Expr lhs, Expr rhs) {
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

  private static boolean requiresCast(Expr lhs, Expr rhs) {
    // we need to insert a cast if the *jimple* types of the arguments
    // differ. (this is different than the gimple types
    // not matching, because we ignore signed/unsigned right now)

    JimpleType ltype = PrimitiveTypes.get(lhs.type());
    JimpleType rtype = PrimitiveTypes.get(rhs.type());
    
    return !ltype.equals(rtype);
  }

  private static boolean typesCompatible(GimpleType lhs, GimpleType rhs) {
    if(lhs instanceof IntegerType) {
      return (rhs instanceof IntegerType || rhs instanceof BooleanType) && 
          precision(lhs) >= precision(rhs);
          
    } else if(lhs instanceof RealType) {
      return rhs instanceof PrimitiveType && precision(lhs) >= precision(rhs);
      
    } else {
      return lhs.equals(rhs);
    }
  }

  private static int precision(GimpleType type) {
    if(type instanceof IntegerType) {
      return ((IntegerType) type).getPrecision();
    } else if(type instanceof RealType) {
      return ((RealType) type).getPrecision();
    } else if(type instanceof BooleanType) {
      return 1;
    } else {
      throw new UnsupportedOperationException(type.toString());
    }
  }
  
}
