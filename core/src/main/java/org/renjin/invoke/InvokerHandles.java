package org.renjin.invoke;

import org.renjin.sexp.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;


public class InvokerHandles {

  public static SEXP argumentN(PairList args, int index) {
    return args.getElementAsSEXP(index);
  }

  public static MethodHandle nthArgument(int index) {
    MethodHandle handle = null;
    try {
      handle = MethodHandles.lookup().findStatic(InvokerHandles.class, "argumentN",
          MethodType.methodType(SEXP.class, PairList.class, int.class));
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }

    handle = MethodHandles.insertArguments(handle, 1, index);

    return handle;
  }

  public static MethodHandle intScalar() throws NoSuchMethodException, IllegalAccessException {
    MethodHandle handle = MethodHandles.lookup().findVirtual(Vector.class, "getElementAsInt",
        MethodType.methodType(int.class, int.class));

    handle = MethodHandles.insertArguments(handle, 1, 0);

    return handle;
  }

  public static MethodHandle wrapInt() throws NoSuchMethodException, IllegalAccessException {
    return MethodHandles.lookup().findStatic(IntVector.class, "valueOf",
        MethodType.methodType(SEXP.class, int.class));

  }
}
