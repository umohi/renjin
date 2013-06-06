package org.renjin.primitives.s4;

import org.renjin.sexp.AttributeMap;
import org.renjin.sexp.S4Object;
import org.renjin.sexp.SEXP;


public class S4ObjectBuilder {

  private AttributeMap.Builder attributes;

  public S4ObjectBuilder(String className) {
    this.attributes = AttributeMap.builder();
    this.attributes.setClass(className);
  }

  public void setSlot(String name, SEXP value) {
    attributes.set(name, value);
  }


  public S4Object build() {
    return new S4Object(attributes.build());
  }
}
