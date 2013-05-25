package org.renjin.gcc.translate.expr;

import org.renjin.gcc.gimple.type.GimpleType;
import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.translate.FunctionContext;

/**
 * An intermediate representation of an expression used during
 * the translation of Gimple to Jimple.
 */
public interface ImExpr {

  /**
   *
   * @return an intermediate expression which evaluates to
   * the memory address of this expression.
   */
  ImExpr addressOf();

  /**
   *
   * @return an intermediate expression representing the value
   * to which this expression points.
   */
  ImExpr memref();


  /**
   * @return a jimple expression that will
   * evaluate to the primitive value of this expression.
   * @param context the function context
   */
  JimpleExpr translateToPrimitive(FunctionContext context);

  JimpleExpr translateToObjectReference(FunctionContext context, String className);

  /**
   * 
   * @param index
   * @return an intermediate expression which represents the array element
   * at index {@code index}
   * @throws UnsupportedOperationException if this expression does not support
   * array indexing
   */
  ImExpr elementAt(ImExpr index);

  /**
   *
   * @return the gimple type of this expression
   */
  GimpleType type();

  /**
   *
   * @param offset an expression which evaluates to the number of bytes to advance this pointer
   * @return a new expression which points to this memory address plus {@code offset} bytes.
   */
  ImExpr pointerPlus(ImExpr offset) ;

  ImExpr member(String member) ;

  /**                                      *
   * @return true if this expression can be interpreted as NULL in a pointer context.
   */
  boolean isNull();

}
