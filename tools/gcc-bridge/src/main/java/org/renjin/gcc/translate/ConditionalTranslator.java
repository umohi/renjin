package org.renjin.gcc.translate;

import org.renjin.gcc.gimple.GimpleConditional;
import org.renjin.gcc.gimple.GimpleLabel;
import org.renjin.gcc.gimple.GimpleOp;
import org.renjin.gcc.gimple.expr.GimpleConstant;
import org.renjin.gcc.gimple.expr.GimpleExpr;
import org.renjin.gcc.gimple.expr.GimpleVariableRef;
import org.renjin.gcc.gimple.expr.SymbolRef;
import org.renjin.gcc.gimple.type.GimpleType;
import org.renjin.gcc.gimple.type.PrimitiveType;
import org.renjin.gcc.jimple.Jimple;
import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.jimple.JimpleGoto;
import org.renjin.gcc.jimple.JimpleType;
import org.renjin.gcc.translate.types.PrimitiveTypes;
import org.renjin.gcc.translate.var.Variable;

import java.util.List;

public class ConditionalTranslator {
  private FunctionContext context;

  public ConditionalTranslator(FunctionContext context) {
    this.context = context;
  }

  public void translate(GimpleConditional conditional) {
    context.getBuilder().addStatement(
        "if " + translateCondition(conditional.getOperator(), conditional.getOperands()) + " goto "
            + label(conditional.getTrueLabel()));
    context.getBuilder().addStatement(new JimpleGoto(label(conditional.getFalseLabel())));
  }

  private String label(int label) {
    return "BB" + label;
  }

  public JimpleExpr translateCondition(GimpleOp operator, List<GimpleExpr> operands) {
    JimpleType type = PrimitiveTypes.get((PrimitiveType) findType(operands.get(0)));
    if (type.equals(JimpleType.DOUBLE) || type.equals(JimpleType.FLOAT)) {
      switch (operator) {
      case NE_EXPR:
        return floatComparison("cmpl", "!= 0", operands, type);
      case EQ_EXPR:
        return floatComparison("cmpl", "== 0", operands, type);
      case LE_EXPR:
        return floatComparison("cmpg", "<= 0", operands, type);
      case LT_EXPR:
        return floatComparison("cmpg", "< 0", operands, type);
      case GT_EXPR:
        return floatComparison("cmpl", "> 0", operands, type);
      case GE_EXPR:
        return floatComparison("cmpl", ">= 0", operands, type);
      }
    } else {
      switch (operator) {
      case NE_EXPR:
        return intComparison("!=", operands);
      case EQ_EXPR:
        return intComparison("==", operands);
      case LE_EXPR:
        return intComparison("<=", operands);
      case LT_EXPR:
        return intComparison("<", operands);
      case GT_EXPR:
        return intComparison(">", operands);
      case GE_EXPR:
        return intComparison(">=", operands);
      }
    }
    throw new UnsupportedOperationException(operator.name() + operands.toString());
  }

  private GimpleType findType(GimpleExpr gimpleExpr) {
    if (gimpleExpr instanceof SymbolRef) {
      Variable var = context.lookupVar(gimpleExpr);
      return var.getGimpleType();
    } else if (gimpleExpr instanceof GimpleConstant) {
      return ((GimpleConstant) gimpleExpr).getType();
    } else {
      throw new UnsupportedOperationException("Could not resolve type of expression " + gimpleExpr);
    }
  }

  private JimpleExpr floatComparison(String operator, String condition, List<GimpleExpr> operands, JimpleType type) {

    String cmp = context.declareTemp(JimpleType.INT);
    context.getBuilder().addStatement(
        cmp + " = " + context.asNumericExpr(operands.get(0), type) + " " + operator + " "
            + context.asNumericExpr(operands.get(1), type));

    return new JimpleExpr(cmp + " " + condition);
  }

  private JimpleExpr intComparison(String operator, List<GimpleExpr> operands) {
    return JimpleExpr.binaryInfix(operator, context.asNumericExpr(operands.get(0), JimpleType.INT),
        context.asNumericExpr(operands.get(1), JimpleType.INT));

  }
}
