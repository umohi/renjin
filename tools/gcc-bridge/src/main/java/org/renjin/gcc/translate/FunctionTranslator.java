package org.renjin.gcc.translate;

import java.lang.reflect.Field;

import org.renjin.gcc.gimple.GimpleAssign;
import org.renjin.gcc.gimple.GimpleBasicBlock;
import org.renjin.gcc.gimple.GimpleCall;
import org.renjin.gcc.gimple.GimpleConditional;
import org.renjin.gcc.gimple.GimpleFunction;
import org.renjin.gcc.gimple.GimpleLabelIns;
import org.renjin.gcc.gimple.GimpleReturn;
import org.renjin.gcc.gimple.GimpleSwitch;
import org.renjin.gcc.gimple.GimpleVisitor;
import org.renjin.gcc.gimple.GimpleGoto;
import org.renjin.gcc.gimple.expr.GimpleAddressOf;
import org.renjin.gcc.gimple.expr.GimpleComponentRef;
import org.renjin.gcc.gimple.expr.GimpleConstant;
import org.renjin.gcc.gimple.expr.GimpleExpr;
import org.renjin.gcc.gimple.expr.GimpleExternal;
import org.renjin.gcc.gimple.expr.GimpleMemRef;
import org.renjin.gcc.gimple.expr.GimpleNull;
import org.renjin.gcc.gimple.expr.GimpleVariableRef;
import org.renjin.gcc.gimple.expr.SymbolRef;
import org.renjin.gcc.gimple.type.GimpleType;
import org.renjin.gcc.gimple.type.PointerType;
import org.renjin.gcc.gimple.type.PrimitiveType;
import org.renjin.gcc.gimple.type.VoidType;
import org.renjin.gcc.jimple.*;
import org.renjin.gcc.translate.call.CallTranslator;
import org.renjin.gcc.translate.expr.Expr;
import org.renjin.gcc.translate.types.PrimitiveTypes;
import org.renjin.gcc.translate.var.Variable;

/**
 * Translates a GimpleFunction to a Jimple function
 */
public class FunctionTranslator extends GimpleVisitor {

  private TranslationContext translationContext;
  private JimpleMethodBuilder builder;

  private FunctionContext context;

  public FunctionTranslator(TranslationContext translationContext) {
    this.translationContext = translationContext;
  }

  public void translate(GimpleFunction function) {
    try {
      this.builder = translationContext.getMainClass().newMethod();
      builder.setModifiers(JimpleModifiers.PUBLIC, JimpleModifiers.STATIC);
      builder.setName(function.getName());
      builder.setReturnType(translateReturnType(function.getReturnType()));

      context = new FunctionContext(translationContext, function, builder);

      function.visitIns(this);
    } catch (Exception e) {
      throw new TranslationException("Exception translating function " + function.getName(), e);
    }
  }

  private JimpleType translateReturnType(GimpleType returnType) {
    if (returnType instanceof PrimitiveType) {
      return PrimitiveTypes.get(returnType);
    } else if (returnType instanceof PointerType) {
      GimpleType innerType = returnType.getBaseType();
      if (innerType instanceof PrimitiveType) {
        return PrimitiveTypes.getWrapperType((PrimitiveType) innerType);
      }
    } else if (returnType instanceof VoidType) {
      return JimpleType.VOID;
    }
    throw new UnsupportedOperationException(returnType.toString());
  }

  @Override
  public void blockStart(GimpleBasicBlock bb) {
    builder.addLabel(basicBlockLabel(bb.getIndex()));
  }

  private String basicBlockLabel(int index) {
    return "BB" + index;
  }

  @Override
  public void visitAssignment(GimpleAssign assignment) {
    AssignmentTranslator translator = new AssignmentTranslator(context);
    translator.translate(assignment);
  }

  @Override
  public void visitReturn(GimpleReturn gimpleReturn) {
    if (gimpleReturn.getValue() == null) {
      builder.addStatement("return");
    } else {
      if (gimpleReturn.getValue() instanceof SymbolRef) {
        Variable var = context.lookupVar(gimpleReturn.getValue());
        builder.addStatement("return " + var.returnExpr());
      } else {
        throw new UnsupportedOperationException(gimpleReturn.getValue().toString());
      }
    }
  }

  @Override
  public void visitGoto(GimpleGoto gotoIns) {
    builder.addStatement(new JimpleGoto(basicBlockLabel(gotoIns.getTarget())));
  }

  @Override
  public void visitLabelIns(GimpleLabelIns labelIns) {
    builder.addLabel(labelIns.getLabel().getName());
  }

  @Override
  public void visitSwitch(GimpleSwitch gimpleSwitch) {
    Expr switchExpr = context.resolveExpr(gimpleSwitch.getExpr());
    JimpleSwitchStatement jimpleSwitch = new JimpleSwitchStatement(switchExpr.asPrimitiveValue(context).toString());
    for (GimpleSwitch.Branch branch : gimpleSwitch.getBranches()) {
      jimpleSwitch.addBranch(branch.getValue(), branch.getLabel().getName());
    }
    builder.add(jimpleSwitch);
  }

  @Override
  public void visitCall(GimpleCall call) {
    try {
    new CallTranslator(context, call).translate();
      } catch(Exception e) {
      throw new TranslationException("Exception thrown while translating call "
      + call, e);
      }
  }

  @Override
  public void visitConditional(GimpleConditional conditional) {
    try {
      new ConditionalTranslator(context).translate(conditional);
    } catch (Exception e) {
      throw new RuntimeException("Exception translating " + conditional, e);
    }
  }

}
