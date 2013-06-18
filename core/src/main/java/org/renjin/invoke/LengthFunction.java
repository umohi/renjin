package org.renjin.invoke;

import org.renjin.eval.Context;
import org.renjin.primitives.Types;
import org.renjin.invoke.annotations.processor.ArgumentIterator;
import org.renjin.sexp.*;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 6/17/13
 * Time: 9:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class LengthFunction extends PrimitiveFunction {
  @Override
  public String getName() {
    return "length";
  }

  @Override
  public SEXP apply(Context context, Environment rho, FunctionCall call, PairList args) {
    ArgumentIterator it = new ArgumentIterator(context, rho, args);
    return IntArrayVector.valueOf(Types.length(it.evalNext()));
  }

  @Override
  public String getTypeName() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void accept(SexpVisitor visitor) {
    //To change body of implemented methods use File | Settings | File Templates.
  }
}
