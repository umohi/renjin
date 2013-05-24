package org.renjin.gcc.translate.expr;

import org.renjin.gcc.gimple.expr.GimpleStringConstant;
import org.renjin.gcc.gimple.type.ArrayType;
import org.renjin.gcc.gimple.type.IndirectType;
import org.renjin.gcc.gimple.type.PointerType;
import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.translate.FunctionContext;


public class StringConstant extends AbstractExpr {

  private GimpleStringConstant constant;
  private ArrayType type;
  
  public StringConstant(FunctionContext context, GimpleStringConstant constant) {
    this.constant = constant;
    this.type = (ArrayType)constant.getType();
  }

  @Override
  public ArrayType type() {
    return type;
  }

  @Override
  public String toString() {
    return "\"" + constant.toString() + "\"";
  }


  private JimpleExpr literal() {
    return new JimpleExpr(constant.literal());
  }

  @Override
  public Expr addressOf() {
    return new Pointer();
  }
  
  public class Pointer extends AbstractExpr implements IndirectExpr {

    @Override
    public ArrayRef translateToArrayRef(FunctionContext context) {
      String stringTmp = context.declareTemp(String.class);
      String arrayTmp = context.declareTemp(char.class);
      
      context.getBuilder().addStatement(stringTmp + " = " + literal());
      context.getBuilder().addStatement(arrayTmp + " = virtualinvoke " +
              stringTmp + ".<java.lang.String: char[] toCharArray()>()");

      return new ArrayRef(new JimpleExpr(arrayTmp), JimpleExpr.integerConstant(0));
    }

    @Override
    public IndirectType type() {
      return new PointerType(type.getComponentType());
    }

    @Override
    public String toString() {
      return "&" + StringConstant.this.toString();
    }
    
  }
 
  
}
