package org.renjin.gcc.translate.expr;

import org.renjin.gcc.gimple.expr.GimpleStringConstant;
import org.renjin.gcc.gimple.type.ArrayType;
import org.renjin.gcc.gimple.type.GimpleType;
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
    return new Pointer(null);
  }

  @Override
  public Expr elementAt(Expr index) {
    return new Substring(index);
  }
  
  public class Substring extends AbstractExpr {
    private Expr startIndex;

    public Substring(Expr startIndex) {
      this.startIndex = startIndex;
    }

    @Override
    public Expr addressOf() {
      return new Pointer(startIndex);
    }

    @Override
    public GimpleType type() {
      return type;
    }
  }

  public class Pointer extends AbstractExpr implements IndirectExpr {
    
    private Expr startIndex;

    public Pointer(Expr startIndex) {
      this.startIndex = startIndex;
    }

    @Override
    public ArrayRef translateToArrayRef(FunctionContext context) {
      String stringTmp = context.declareTemp(String.class);
      String arrayTmp = context.declareTemp(char.class);
      
      context.getBuilder().addStatement(stringTmp + " = " + literal());
      context.getBuilder().addStatement(arrayTmp + " = virtualinvoke " +
              stringTmp + ".<java.lang.String: char[] toCharArray()>()");
      
      JimpleExpr indexExpr;
      if(startIndex == null) {
        indexExpr = JimpleExpr.integerConstant(constant.getType().getLbound());        
      } else {
        indexExpr = subtractLowerBound(context, startIndex);
      }

      return new ArrayRef(new JimpleExpr(arrayTmp), indexExpr);
    }

    private JimpleExpr subtractLowerBound(FunctionContext context, Expr expr) { 
      if(constant.getType().getLbound() == 0) {
        return startIndex.translateToPrimitive(context);
      } else if(expr instanceof PrimitiveConstant) {
        Number startIndex = (Number)((PrimitiveConstant) expr).getConstantValue();
        return JimpleExpr.integerConstant(startIndex.intValue() - constant.getType().getLbound());
      } else {
        throw new UnsupportedOperationException();
      }
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
