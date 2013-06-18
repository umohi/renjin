package org.renjin.invoke;

import org.renjin.eval.Context;
import org.renjin.eval.EvalException;
import org.renjin.primitives.Types;
import org.renjin.invoke.annotations.processor.ArgumentIterator;
import org.renjin.sexp.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 6/16/13
 * Time: 2:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class LengthFunctionMH extends PrimitiveFunction {

  private String name;
  private MethodHandle handle;
  private MethodHandle spreader;
  private int arity = 1;

  public LengthFunctionMH() {
    try {

      // we start out with the arguments
      // (context, rho, call, args)

      handle = MethodHandles.lookup().findStatic(Types.class, "length",
          MethodType.methodType(int.class, SEXP.class));


      // cast from SEXP to Vector
     // handle = MethodHandles.explicitCastArguments(handle, MethodType.methodType(int.class, Vector.class));

      // wrap return type
      handle = MethodHandles.filterReturnValue(handle, InvokerHandles.wrapInt());

      // drop the args (context, rho, call)
      handle = MethodHandles.dropArguments(handle, 0, Context.class, Environment.class, FunctionCall.class);

      // we now have a handle for  (context, rho, call, SEXP), we need a handle
      // that excepts (context, rho, call, Object[])
      //

      spreader = MethodHandles.spreadInvoker(
          MethodType.methodType(SEXP.class, Context.class, Environment.class, FunctionCall.class, SEXP.class),
          3);


    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public SEXP apply(Context context, Environment rho, FunctionCall call, PairList args) {

    Object[] argArray = new Object[arity];

    // evaluate args
    ArgumentIterator it = new ArgumentIterator(context, rho, args);
    int argIndex = 0;
    while(it.hasNext()) {
      argArray[argIndex] = it.evalNext();
    }

    try {
      return (SEXP)spreader.invokeExact(handle, context, rho, call, argArray);
    } catch (EvalException e) {
      throw e;
    } catch (Error e) {
      throw e;
    } catch(Throwable e) {
      EvalException evalException = new EvalException("Exception in " + getName(), e);
      evalException.initContext(context);
      throw evalException;
    }
  }

  @Override
  public String getTypeName() {
    return "primitive";
  }

  @Override
  public void accept(SexpVisitor visitor) {
    //To change body of implemented methods use File | Settings | File Templates.
  }
}
