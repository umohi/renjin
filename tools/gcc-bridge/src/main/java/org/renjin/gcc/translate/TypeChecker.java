package org.renjin.gcc.translate;

import org.renjin.gcc.gimple.type.GimpleIntegerType;
import org.renjin.gcc.gimple.type.GimpleRealType;
import org.renjin.gcc.gimple.type.GimpleType;
import org.renjin.gcc.translate.expr.Expr;

public class TypeChecker {

  public static void assertSameType(Expr expr, Expr... otherExprs) {
    
    for(Expr other : otherExprs) {
      if(!expr.type().equals(other.type())) {
        throw new IllegalArgumentException(String.format("Types do not match: %s:%s <> %s:%s",
            expr.toString(), expr.type().toString(),
            other.toString(), other.type().toString()));
      }
    }
  }
  
  public static boolean isDouble(GimpleType type) {
    return type instanceof GimpleRealType && ((GimpleRealType) type).getPrecision() == 64;
  }
  
  public static boolean isInt(GimpleType type) {
    return type instanceof GimpleIntegerType && ((GimpleIntegerType) type).getPrecision() == 32;
  }

  public static boolean isLong(GimpleType type) {
    return type instanceof GimpleIntegerType && ((GimpleIntegerType) type).getPrecision() == 64;
  }
  
  public static String primitiveJvmTypeName(GimpleType type) {
    if(isDouble(type)) {
      return "double";
    } else if(isInt(type)) {
      return "int";
    } else if(isLong(type)) {
      return "long";
    } else {
      throw new UnsupportedOperationException();
    }
  }
}
