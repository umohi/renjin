package org.renjin.gcc.translate;

import java.util.List;

import org.renjin.gcc.gimple.GimpleAssign;
import org.renjin.gcc.gimple.expr.GimpleExpr;
import org.renjin.gcc.gimple.type.BooleanType;
import org.renjin.gcc.gimple.type.GimpleType;
import org.renjin.gcc.gimple.type.IntegerType;
import org.renjin.gcc.gimple.type.RealType;
import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.translate.assign.PrimitiveAssignment;
import org.renjin.gcc.translate.expr.LValue;
import org.renjin.gcc.translate.expr.PrimitiveLValue;
import org.renjin.gcc.translate.expr.Expr;

import com.google.common.collect.Lists;

public class AssignmentTranslator {
  private FunctionContext context;

  public AssignmentTranslator(FunctionContext context) {
    this.context = context;
  }

  public void translate(GimpleAssign assign) {
    Expr lhs = context.resolveExpr(assign.getLHS());
    List<Expr> operands = resolveOps(assign.getOperands());

    switch(assign.getOperator()) {
    case INTEGER_CST:
    case MEM_REF:
    case ADDR_EXPR:
    case REAL_CST:
    case VAR_DECL:
    case FLOAT_EXPR:
    case NOP_EXPR:
    case ARRAY_REF:
    case PAREN_EXPR:
      assign(lhs, operands.get(0));
      return;
      
    case POINTER_PLUS_EXPR:
      assign(lhs, operands.get(0).pointerPlus(operands.get(1)));
      return;
     
    case EQ_EXPR:
    case NE_EXPR:
    case LE_EXPR:
    case LT_EXPR:
    case GT_EXPR:
    case GE_EXPR:
      assignComparison(lhs, new Comparison(assign.getOperator(), operands.get(0), operands.get(1)));
      break;
      
    case MULT_EXPR:
      assignBinaryOp(lhs, "*", operands);
      break;
      
    case PLUS_EXPR:
      assignBinaryOp(lhs, "+", operands);
      break;

    case MINUS_EXPR:
      assignBinaryOp(lhs, "-", operands);
      break;

    case RDIV_EXPR:
    case TRUNC_DIV_EXPR:
      assignDiv(lhs, operands);
      break;
      
    case TRUNC_MOD_EXPR:
      assignBinaryOp(lhs, "%", operands);
      break;

    case BIT_NOT_EXPR:
      assignBitNot(lhs, operands.get(0));
      break;
      
    case NEGATE_EXPR:
      assignNegated(lhs, operands.get(0));
      break;

    case ABS_EXPR:
      assignAbs(lhs, operands.get(0));
      break;

    case MAX_EXPR:
      assignMax(lhs, operands);
      break;

    case UNORDERED_EXPR:
      assignUnordered(lhs, operands);
      break;

    case TRUTH_NOT_EXPR:
      assignTruthNot(lhs, operands.get(0));
      break;

    
    case TRUTH_OR_EXPR:
      assignTruthOr(lhs, operands);
      break;
      
    default:
      throw new UnsupportedOperationException(assign.getOperator().toString());
    }
  }

  private void assignDiv(Expr lhs, List<Expr> operands) {
    Expr x = operands.get(0);
    Expr y = operands.get(1);

    if(!x.type().equals(y.type())) {
      throw new UnsupportedOperationException();
    }

    if(! (x.type() instanceof RealType || x.type() instanceof  IntegerType) ) {
      throw new UnsupportedOperationException("unsupported type for div " + x.type());
    }
    
    assignBinaryOp(lhs, "/", operands);
  }


  private void assignNegated(Expr lhs, Expr expr) {
    TypeChecker.assertSameType(lhs, expr);
    
    assignPrimitive(lhs, new JimpleExpr("neg " + expr.translateToPrimitive(context)));
  }

  private void assignBinaryOp(Expr lhs, String operator, List<Expr> operands) {

    TypeChecker.assertSameType(lhs, operands.get(0), operands.get(1));
    
    JimpleExpr a = operands.get(0).translateToPrimitive(context);
    JimpleExpr b = operands.get(1).translateToPrimitive(context);

    assignPrimitive(lhs, JimpleExpr.binaryInfix(operator, a, b));
  }
  
  private List<Expr> resolveOps(List<GimpleExpr> operands) {
    List<Expr> exprs = Lists.newArrayList();
    for(GimpleExpr op : operands) {
      exprs.add(context.resolveExpr(op));
    }
    return exprs;
  }

  private void assignComparison(Expr lhs, Comparison comparison) {
    assignIfElse(lhs, comparison.toCondition(context), JimpleExpr.integerConstant(1), JimpleExpr.integerConstant(0));
  }
  
  private void assignPrimitive(Expr lhs, JimpleExpr jimpleExpr) {
    ((PrimitiveLValue)lhs).writePrimitiveAssignment(jimpleExpr);
  }

  private void assignTruthNot(Expr lhs, Expr op) {
    JimpleExpr expr = op.translateToPrimitive(context);
    JimpleExpr condition = new JimpleExpr(expr + " != 0");
    assignBoolean(lhs, condition);
  }

  private void assignTruthOr(Expr lhs, List<Expr> ops) {
    if(! (ops.get(0).type() instanceof BooleanType &&
          ops.get(1).type() instanceof BooleanType)) {
      throw new UnsupportedOperationException();
    }



    JimpleExpr a = ops.get(0).translateToPrimitive(context);
    JimpleExpr b = ops.get(1).translateToPrimitive(context);
    
    String checkB = context.newLabel();
    String noneIsTrue = context.newLabel();
    String doneLabel = context.newLabel();


    context.getBuilder().addStatement("if " + a + " == 0 goto " + checkB);
    assignPrimitive(lhs, JimpleExpr.integerConstant(1));
    context.getBuilder().addStatement("goto " + doneLabel);
    
    context.getBuilder().addLabel(checkB);
    context.getBuilder().addStatement("if " + b + " == 0 goto " + noneIsTrue);
    assignPrimitive(lhs, JimpleExpr.integerConstant(1));
    context.getBuilder().addStatement("goto " + doneLabel);

    context.getBuilder().addLabel(noneIsTrue);
    assignPrimitive(lhs, JimpleExpr.integerConstant(0));

    context.getBuilder().addLabel(doneLabel);

  }
  

  private void assignBoolean(Expr lhs, JimpleExpr booleanExpr) {
    assignIfElse(lhs, booleanExpr, JimpleExpr.integerConstant(1), JimpleExpr.integerConstant(0));
  }

  private void assignBitNot(Expr lhs, Expr op) {
    TypeChecker.assertSameType(lhs, op);

    assignPrimitive(lhs, JimpleExpr.binaryInfix("^", op.translateToPrimitive(context), JimpleExpr.integerConstant(-1)));
  }

  private void assignIfElse(Expr lhs, JimpleExpr booleanExpr, JimpleExpr ifTrue, JimpleExpr ifFalse) {
    String trueLabel = context.newLabel();
    String doneLabel = context.newLabel();

    context.getBuilder().addStatement("if " + booleanExpr + " goto " + trueLabel);

    assignPrimitive(lhs, ifFalse);
    context.getBuilder().addStatement("goto " + doneLabel);

    context.getBuilder().addLabel(trueLabel);
    assignPrimitive(lhs, ifTrue);
    context.getBuilder().addStatement("goto " + doneLabel);

    context.getBuilder().addLabel(doneLabel);
  }


  private void assignUnordered(Expr lhs, List<Expr> operands) {
    Expr x = operands.get(0);
    Expr y = operands.get(1);

    TypeChecker.assertSameType(x, y);

    if(TypeChecker.isDouble(x.type())) {
      //assignPrimitive(lhs, JimpleExpr.integerConstant(0));
      assignPrimitive(lhs, new JimpleExpr(String.format(
              "staticinvoke <org.renjin.gcc.runtime.Builtins: boolean unordered(double, double)>(%s, %s)",
              x.translateToPrimitive(context),
              y.translateToPrimitive(context))));
    } else {
      throw new UnsupportedOperationException();
    }
  }


  private void assignAbs(Expr lhs, Expr expr) {
    
    TypeChecker.assertSameType(lhs, expr);
    
    assignPrimitive(lhs, new JimpleExpr(String.format("staticinvoke <java.lang.Math: %s>(%s)",
            absMethodForType(expr.type()),
            expr.translateToPrimitive(context))));
    
  }

  private String absMethodForType(GimpleType type) {
    if (type instanceof RealType) {
      if (((RealType) type).getPrecision() == 64) {
        return "double abs(double)";
      }
    }
    if (type instanceof IntegerType) {
      if (((IntegerType) type).getPrecision() == 32) {
        return "int abs(int)";
      }
    }
    throw new UnsupportedOperationException("abs on type " + type.toString());
  }

  private void assignMax(Expr lhs, List<Expr> operands) {
    TypeChecker.assertSameType(lhs, operands.get(0), operands.get(1));

    String signature = "{t} max({t}, {t})"
            .replace("{t}", TypeChecker.primitiveJvmTypeName(lhs.type()));
    
    JimpleExpr a = operands.get(0).translateToPrimitive(context);
    JimpleExpr b = operands.get(1).translateToPrimitive(context);

    assignPrimitive(lhs, new JimpleExpr(String.format(
            "staticinvoke <java.lang.Math: %s>(%s, %s)",
            signature, a.toString(), b.toString())));


  }
  
  private void assign(Expr lhs, Expr rhs) {
    if(lhs instanceof PrimitiveLValue) {
      PrimitiveAssignment.assign(context, lhs, rhs);
    } else if(lhs instanceof LValue) {
      ((LValue) lhs).writeAssignment(context, rhs);
    } else {
      throw new UnsupportedOperationException("Unsupported assignment of " + rhs.toString() + " to " + lhs.toString());
    }
  }

}
