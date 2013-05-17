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

public class PrimitiveAssigner implements Assigner {

  @Override
  public boolean assign(FunctionContext context, Expr lhs, Expr rhs) {
    if(lhs instanceof PrimitiveAssignable && typesCompatible(lhs.type(), rhs.type())) {
      JimpleExpr jimpleExpr = rhs.asPrimitiveValue(context);
      if(lhs.type() instanceof RealType && rhs.type() instanceof IntegerType) {
        jimpleExpr = JimpleExpr.cast(jimpleExpr, JimpleType.DOUBLE);
      }
      ((PrimitiveAssignable) lhs).assignPrimitiveValue(jimpleExpr);
      return true;
    }
    return false;
  }

  private boolean typesCompatible(GimpleType lhs, GimpleType rhs) {
    if(lhs instanceof IntegerType) {
      return (rhs instanceof IntegerType || rhs instanceof BooleanType) && 
          precision(lhs) >= precision(rhs);
          
    } else if(lhs instanceof RealType) {
      return rhs instanceof PrimitiveType && precision(lhs) >= precision(rhs);
      
    } else {
      return lhs.equals(rhs);
    }
  }

  private int precision(GimpleType type) {
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
