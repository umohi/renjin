package org.renjin.gcc.translate.expr;

import org.renjin.gcc.gimple.expr.GimpleStringConstant;
import org.renjin.gcc.gimple.type.ArrayType;
import org.renjin.gcc.gimple.type.GimpleType;
import org.renjin.gcc.gimple.type.PointerType;


public class StringConstant extends AbstractExpr {

  private GimpleStringConstant constant;
  private ArrayType type;
  
  public StringConstant(GimpleStringConstant constant) {
    this.constant = constant;
    this.type = (ArrayType)constant.getType();
  }

  @Override
  public ArrayType type() {
    return type;
  }

  @Override
  public String toString() {
    return constant.toString();
  }

  @Override
  public Expr addressOf() {
    return new Pointer();
  }
  
  public class Pointer extends AbstractExpr {

    @Override
    public GimpleType type() {
      return new PointerType(type.getComponentType());
    }

    public String stringValue() {
      return constant.getValue();
    }
    
  }
 
  
}
