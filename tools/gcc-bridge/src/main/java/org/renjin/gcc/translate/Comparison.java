package org.renjin.gcc.translate;

import org.renjin.gcc.gimple.GimpleOp;
import org.renjin.gcc.gimple.type.BooleanType;
import org.renjin.gcc.gimple.type.IntegerType;
import org.renjin.gcc.gimple.type.RealType;
import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.jimple.JimpleType;
import org.renjin.gcc.translate.expr.Expr;

public class Comparison {
  private GimpleOp op;
  private Expr a;
  private Expr b;


  public Comparison(GimpleOp op, Expr a, Expr b) {
    super();
    this.op = op;
    this.a = a;
    this.b = b;
  }


  public JimpleExpr toCondition(FunctionContext context) {
    TypeChecker.assertSameType(a, b);

    if(a.type() instanceof RealType) {
      switch (op) {
      case NE_EXPR:
        return floatComparison(context, "cmpl", "!=", 0);
      case EQ_EXPR:
        return floatComparison(context, "cmpl", "==", 0);
      case LE_EXPR:
        return floatComparison(context, "cmpg", "<=", 0);
      case LT_EXPR:
        return floatComparison(context, "cmpg", "<", 0);
      case GT_EXPR:
        return floatComparison(context, "cmpl", ">", 0);
      case GE_EXPR:
        return floatComparison(context, "cmpl", ">=", 0);
      }
    } else if(a.type() instanceof IntegerType || a.type() instanceof BooleanType) {
      switch (op) {
      case NE_EXPR:
        return intComparison(context, "!=");
      case EQ_EXPR:
        return intComparison(context, "==");
      case LE_EXPR:
        return intComparison(context, "<=");
      case LT_EXPR:
        return intComparison(context, "<");
      case GT_EXPR:
        return intComparison(context, ">");
      case GE_EXPR:
        return intComparison(context, ">=");
      }
    }
    throw new UnsupportedOperationException(" don't know how to compare expressions of type " + a.type());
  }
//
//  private void translate(FunctionContext context) {
//    String trueLabel = context.newLabel();
//    String doneLabel = context.newLabel();
//    String tempVar = context.getBuilder().addTempVarDecl(JimpleType.INT);
//
//    context.getBuilder().addStatement(String.format("if %s %s %s goto %s",
//        a.asPrimitiveValue(context),
//        op,
//        b.asPrimitiveValue(context),
//        trueLabel));
//
//    context.getBuilder().addStatement(tempVar + " = 0");
//    context.getBuilder().addStatement("goto " + doneLabel);
//
//    context.getBuilder().addLabel(trueLabel);
//    context.getBuilder().addStatement(tempVar + " = 1");
//    context.getBuilder().addStatement("goto " + doneLabel);
//
//    context.getBuilder().addLabel(doneLabel);
//    return new JimpleExpr(tempVar);
//  }
//  

  private JimpleExpr floatComparison(FunctionContext context, String operator, String condition, int operand) {
    String cmp = context.declareTemp(JimpleType.INT);
    context.getBuilder().addStatement(String.format("%s = %s %s %s",
        cmp, 
        a.asPrimitiveValue(context), 
        operator, 
        b.asPrimitiveValue(context)));

    return new JimpleExpr(cmp + " " + condition + " " + operand);
  }
  
  private JimpleExpr intComparison(FunctionContext context, String operator) {
    return JimpleExpr.binaryInfix(operator,
        a.asPrimitiveValue(context),
        b.asPrimitiveValue(context));
    
  }
}
