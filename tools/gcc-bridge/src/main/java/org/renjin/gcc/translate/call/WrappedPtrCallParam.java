package org.renjin.gcc.translate.call;

import org.renjin.gcc.jimple.JimpleType;

/**
 * A call parameter of primitive pointer type (double*, int*, etc) that is
 * wrapped in a DblPtr or IntPtr.
 */
public class WrappedPtrCallParam extends CallParam {

  private JimpleType paramType;

  public WrappedPtrCallParam(JimpleType paramType) {
    this.paramType = paramType;
  }

  @Override
  public String toString() {
    return "WrappedPtr:" + paramType;
    
  }
}
