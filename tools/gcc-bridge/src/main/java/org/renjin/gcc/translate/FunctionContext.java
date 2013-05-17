package org.renjin.gcc.translate;

import java.util.Collection;
import java.util.Map;

import org.renjin.gcc.CallingConvention;
import org.renjin.gcc.gimple.GimpleCall;
import org.renjin.gcc.gimple.GimpleFunction;
import org.renjin.gcc.gimple.GimpleParameter;
import org.renjin.gcc.gimple.GimpleVarDecl;
import org.renjin.gcc.gimple.expr.*;
import org.renjin.gcc.gimple.type.GimpleType;
import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.jimple.JimpleMethodBuilder;
import org.renjin.gcc.jimple.JimpleType;
import org.renjin.gcc.translate.call.MethodRef;
import org.renjin.gcc.translate.expr.*;
import org.renjin.gcc.translate.types.TypeTranslator;
import org.renjin.gcc.translate.var.Variable;

import com.google.common.collect.Maps;

public class FunctionContext {

  private TranslationContext translationContext;
  private GimpleFunction gimpleFunction;
  private JimpleMethodBuilder builder;
  private Map<Integer, Variable> symbolTable = Maps.newHashMap();

  private int nextTempId = 0;
  private int nextLabelId = 1000;

  public FunctionContext(TranslationContext translationContext, GimpleFunction gimpleFunction,
      JimpleMethodBuilder builder) {
    this.gimpleFunction = gimpleFunction;
    this.translationContext = translationContext;
    this.builder = builder;

    VarUsageInspector varUsage = new VarUsageInspector(gimpleFunction);

    for (GimpleVarDecl decl : gimpleFunction.getVariableDeclarations()) {
      Variable localVariable = translationContext.resolveType(decl.getType()).createLocalVariable(this, decl.getName(),
          varUsage.getUsage(decl.getId()));

      symbolTable.put(decl.getId(), localVariable);
    }

    for (GimpleParameter param : gimpleFunction.getParameters()) {
      TypeTranslator type = translationContext.resolveType(param.getType());
      builder.addParameter(type.paramType(), param.getName());
      Variable variable = type.createLocalVariable(this, param.getName(), varUsage.getUsage(param.getId()));
      variable.initFromParameter();
      symbolTable.put(param.getId(), variable);
    }
  }

  public MethodRef resolveMethod(GimpleCall call) {
    return translationContext.resolveMethod(call, getCallingConvention());
  }

  public CallingConvention getCallingConvention() {
    return gimpleFunction.getCallingConvention();
  }

  public String declareTemp(JimpleType type) {
    String name = "_tmp" + (nextTempId++);
    builder.addVarDecl(type, name);
    return name;
  }

  public JimpleMethodBuilder getBuilder() {
    return builder;
  }

  public TranslationContext getTranslationContext() {
    return translationContext;
  }

  public String newLabel() {
    return "trlabel" + (nextLabelId++) + "__";
  }

  private Variable lookupVar(int id) {
    Variable variable = symbolTable.get(id);
    if (variable == null) {
      throw new IllegalArgumentException("No such variable " + id);
    }
    return variable;
  }

  public Variable lookupVar(GimpleExpr gimpleExpr) {
    if (gimpleExpr instanceof SymbolRef) {
      return lookupVar(((SymbolRef) gimpleExpr).getId());
    } else {
      throw new UnsupportedOperationException("Expected GimpleVar, got: " + gimpleExpr + " ["
          + gimpleExpr.getClass().getSimpleName() + "]");
    }
  }

  public Collection<Variable> getVariables() {
    return symbolTable.values();
  }

  public GimpleType getGimpleVariableType(GimpleExpr expr) {
    if(expr instanceof SymbolRef) {
      Variable variable = symbolTable.get(((SymbolRef) expr).getId());
      if(variable == null) { 
        throw new IllegalArgumentException(expr.toString());
      }
      return variable.getGimpleType();
    } else if(expr instanceof GimpleConstant) {
      return ((GimpleConstant) expr).getType();
    } else {
      throw new UnsupportedOperationException(expr.toString());
    }
  }

  public Expr resolveExpr(GimpleExpr gimpleExpr) {
    if(gimpleExpr instanceof GimpleMemRef) {
      return resolveExpr(((GimpleMemRef) gimpleExpr).getPointer()).value();
    } else if(gimpleExpr instanceof SymbolRef) {
      return lookupVar(gimpleExpr);
    } else if(gimpleExpr instanceof GimpleStringConstant) {
      return new StringConstant((GimpleStringConstant) gimpleExpr);
    } else if(gimpleExpr instanceof GimpleConstant) {
      return new PrimitiveConstant(this, (GimpleConstant) gimpleExpr);
    } else if(gimpleExpr instanceof GimpleAddressOf) {
      return resolveExpr(((GimpleAddressOf) gimpleExpr).getValue()).addressOf();
    } else if(gimpleExpr instanceof GimpleFunctionRef) {
      return new FunctionExpr(translationContext.resolveMethod(((GimpleFunctionRef) gimpleExpr).getName()));
    } else if(gimpleExpr instanceof GimpleArrayRef) {
      GimpleArrayRef arrayRef = (GimpleArrayRef) gimpleExpr;
      return resolveExpr(arrayRef.getArray()).elementAt(resolveExpr(arrayRef.getIndex()));
    } else if(gimpleExpr instanceof GimpleComponentRef) {
      GimpleComponentRef componentRef = (GimpleComponentRef) gimpleExpr;
      return resolveExpr(componentRef.getValue()).member(componentRef.getMember());
    } else if(gimpleExpr instanceof GimpleConstantRef) {
      return resolveExpr(((GimpleConstantRef) gimpleExpr).getValue());
    }
    throw new UnsupportedOperationException(gimpleExpr.toString());
  }

}
