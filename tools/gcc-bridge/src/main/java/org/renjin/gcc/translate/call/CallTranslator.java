package org.renjin.gcc.translate.call;

import java.util.List;

import org.renjin.gcc.gimple.GimpleCall;
import org.renjin.gcc.gimple.expr.GimpleAddressOf;
import org.renjin.gcc.gimple.expr.GimpleExpr;
import org.renjin.gcc.gimple.expr.GimpleFunctionRef;
import org.renjin.gcc.gimple.expr.GimpleVariableRef;
import org.renjin.gcc.gimple.expr.SymbolRef;
import org.renjin.gcc.gimple.type.FunctionType;
import org.renjin.gcc.gimple.type.GimpleType;
import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.translate.FunSignature;
import org.renjin.gcc.translate.FunctionContext;
import org.renjin.gcc.translate.var.FunPtrVar;
import org.renjin.gcc.translate.var.Variable;

import com.google.common.collect.Lists;

public class CallTranslator {

  private ParamMarshallers paramMarshallers = new ParamMarshallers();
  private CallUnmarshallers callUnmarshallers = new CallUnmarshallers();

  private List<CallUnmarshaller> returnUnmarshallers;
  private FunctionContext context;
  private GimpleCall call;

  public CallTranslator(FunctionContext context, GimpleCall call) {
    this.context = context;
    this.call = call;

    returnUnmarshallers = Lists.newArrayList();
    returnUnmarshallers.add(new WrappedPtrUnmarshaller());
  }

  public void translate() {
    GimpleExpr functionExpr = call.getFunction();
    if (functionExpr instanceof GimpleAddressOf && 
        ((GimpleAddressOf) functionExpr).getValue() instanceof GimpleFunctionRef) {
      translateStaticCall();

    } else if (functionExpr instanceof SymbolRef) {
      translateFunctionPointerCall();

    } else {
      throw new UnsupportedOperationException(functionExpr.toString());
    }
  }

  private void translateStaticCall() {
    MethodRef method = context.resolveMethod(call);
    JimpleExpr callExpr = composeCallExpr(method);

    callUnmarshallers.unmarshall(context, call.getLhs(), method.getReturnType(), callExpr);
  }

  private void translateFunctionPointerCall() {
    FunSignature funPtr = getFunPtrInterface();
    FunPtrVar var = getFunPtrVar();

    StringBuilder expr = new StringBuilder();
    expr.append("interfaceinvoke ").append(var.getJimpleVariable()).append(".").append(funPtr.jimpleSignature());

    marshalParamList(expr, funPtr.getParams());

    callUnmarshallers.unmarshall(context, call.getLhs(), funPtr.getReturnType(), new JimpleExpr(expr.toString()));
  }

  private FunPtrVar getFunPtrVar() {
    Variable var = context.lookupVar(call.getFunction());
    if (!(var instanceof FunPtrVar)) {
      throw new UnsupportedOperationException("Function value must be a FunPtrVar, got: " + var);
    }
    return (FunPtrVar) var;
  }

  private FunSignature getFunPtrInterface() {
    GimpleType type = context.getGimpleVariableType(call.getFunction());
    if (!type.isPointerTo(FunctionType.class)) {
      throw new UnsupportedOperationException("Function value must be of type FunctionPointer, got: " + type);
    }
    return context.getTranslationContext().getFunctionPointerMethod((FunctionType)type.getBaseType());
  }

  private JimpleExpr composeCallExpr(MethodRef method) {
    StringBuilder callExpr = new StringBuilder();
    callExpr.append("staticinvoke").append(method.signature());
    marshalParamList(callExpr, method.getParams());
    return new JimpleExpr(callExpr.toString());
  }

  private void marshalParamList(StringBuilder callExpr, List<CallParam> params) {
    callExpr.append("(");
    boolean needsComma = false;
    for (JimpleExpr param : marshallParams(params)) {
      if (needsComma) {
        callExpr.append(", ");
      }
      callExpr.append(param.toString());
      needsComma = true;
    }
    callExpr.append(")");
  }

  private List<JimpleExpr> marshallParams(List<CallParam> callParams) {
    List<JimpleExpr> exprs = Lists.newArrayList();
    for (int i = 0; i != call.getParamCount(); ++i) {
      exprs.add(paramMarshallers.marshall(context, context.resolveExpr(call.getArguments().get(i)), callParams.get(i)));
    }
    return exprs;
  }

}
