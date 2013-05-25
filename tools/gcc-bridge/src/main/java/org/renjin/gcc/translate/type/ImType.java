package org.renjin.gcc.translate.type;

import org.renjin.gcc.jimple.JimpleType;
import org.renjin.gcc.translate.FunctionContext;
import org.renjin.gcc.translate.VarUsage;
import org.renjin.gcc.translate.var.Variable;

/**
 * An intermediate representation of a type used during translation
 */
public abstract class ImType {

  public abstract JimpleType paramType();

  public abstract JimpleType returnType();

  public abstract Variable createLocalVariable(
      FunctionContext functionContext,
      String gimpleName,
      VarUsage varUsage);

  public ImType pointerType() {
    throw new UnsupportedOperationException();
  }
}
