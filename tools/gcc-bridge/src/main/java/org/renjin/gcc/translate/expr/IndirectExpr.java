package org.renjin.gcc.translate.expr;

import org.renjin.gcc.gimple.type.GimpleIndirectType;
import org.renjin.gcc.translate.FunctionContext;

/**
 * An intermediate expression that references a memory location
 * (and is backed by a JVM array)
 */
public interface IndirectExpr extends Expr {

  ArrayRef translateToArrayRef(FunctionContext context);

  @Override
  GimpleIndirectType type();
}
