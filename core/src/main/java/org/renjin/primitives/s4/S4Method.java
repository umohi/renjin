package org.renjin.primitives.s4;

import org.renjin.sexp.Function;

public class S4Method {
  private S4MethodSignature signature;
  private Function definition;

  public S4Method(String name, S4MethodSignature signature, Function definition) {
    this.signature = signature;
    this.definition = definition;
  }
}
