package org.renjin.gcc.gimple.expr;

public class GimpleCompoundRef extends GimpleLValue {

  private final GimpleVariableRef var;
  private String member;

  public GimpleCompoundRef(GimpleVariableRef var, String member) {
    this.var = var;
    this.member = member;
  }

  public GimpleVariableRef getVar() {
    return var;
  }

  public String getMember() {
    return member;
  }

  @Override
  public String toString() {
    return var + "." + member;
  }
}
