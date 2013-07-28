package org.renjin.compiler.ir.tac.statements;

import org.renjin.compiler.ir.tac.expressions.EnvironmentVariable;
import org.renjin.compiler.ir.tac.expressions.Expression;


public class Reassignment extends Assignment {
 
  public Reassignment(EnvironmentVariable lhs, Expression rhs) {
    super(lhs, rhs);
  }


}
