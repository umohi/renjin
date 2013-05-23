package org.renjin.gcc.translate.expr;

import org.renjin.gcc.gimple.type.GimpleType;
import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.translate.FunctionContext;

public interface Expr {

  public Expr addressOf();
  
  public Expr value();
  
  public JimpleExpr asPrimitiveValue(FunctionContext context);
  
  public void assign(Expr expr) ;
    
  public Expr elementAt(Expr index);
  
  public abstract GimpleType type();
  
  public Expr pointerPlus(Expr resolveExpr) ;

  public Expr member(String member) ;

}
