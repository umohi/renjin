package org.renjin.gcc;

import org.netlib.blas.BLAS;
import org.renjin.gcc.runtime.DoublePtr;
import org.renjin.gcc.runtime.IntPtr;

public class RStubs {

  public static double R_NaN = 0;

  public static boolean R_finite(double x) {
    return true;
  }

  public static boolean R_IsNA(double x) {
    return false;
  }

  public static void Rf_error(String msg) {
    throw new RuntimeException(msg);
  }
  
  public static double dnrm2_(IntPtr n, DoublePtr x, IntPtr incx) {
    if(x.offset != 0) {
      throw new UnsupportedOperationException();
    }
    return BLAS.getInstance().dnrm2(n.array[n.offset], x.array, incx.array[incx.offset]);
  }

  public static void dscal_(IntPtr n, DoublePtr da, DoublePtr dx, IntPtr incx) {
    BLAS.getInstance().dscal(n.unwrap(), da.unwrap(), array(dx), incx.unwrap());
  }


  public static void ddot_(IntPtr n, DoublePtr dx, IntPtr incx, DoublePtr dy, IntPtr incy) {
    BLAS.getInstance().ddot(n.unwrap(), array(dx), incx.unwrap(), array(dy), incy.unwrap());
  }


  public static void daxpy_(IntPtr n, DoublePtr da, DoublePtr dx,IntPtr incx, DoublePtr dy, IntPtr incy) {
    BLAS.getInstance().daxpy(n.unwrap(), da.unwrap(), array(dx), incx.unwrap(), array(dy), incy.unwrap());
  }
  
  private static double[] array(DoublePtr x) {
    if(x.offset != 0) {
      throw new RuntimeException();
    }
    return x.array;
  }
}
