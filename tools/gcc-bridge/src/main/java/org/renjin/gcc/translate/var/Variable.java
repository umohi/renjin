package org.renjin.gcc.translate.var;

import java.util.List;

import org.renjin.gcc.gimple.GimpleOp;
import org.renjin.gcc.gimple.expr.GimpleExpr;
import org.renjin.gcc.gimple.type.GimpleType;
import org.renjin.gcc.gimple.type.IndirectType;
import org.renjin.gcc.gimple.type.PointerType;
import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.translate.FunctionContext;
import org.renjin.gcc.translate.expr.AbstractExpr;
import org.renjin.gcc.translate.expr.Expr;
import org.renjin.gcc.translate.expr.IndirectExpr;
import org.renjin.gcc.translate.expr.LValue;

public abstract class Variable extends AbstractExpr implements LValue {



  public void initFromParameter() {

  }


}
