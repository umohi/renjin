package org.renjin.primitives.vector;


import org.renjin.sexp.AttributeMap;
import org.renjin.sexp.DoubleVector;
import org.renjin.sexp.SEXP;
import org.renjin.sexp.Vector;

import java.util.Arrays;

public class CombinedDoubleVector extends DoubleVector implements DeferredComputation {
  
  private Vector[] operands;
  private boolean constantTime;
  
  public CombinedDoubleVector(Vector[] operands, AttributeMap attributes) {
    super(attributes);
    this.operands = Arrays.copyOf(operands, operands.length);
    
    constantTime = true;
    for(Vector operand : operands) {
      if(!operand.isConstantAccessTime()) {
        constantTime = false;
      }
    }
  }


  @Override
  public Vector[] getOperands() {
    return new Vector[0];  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public String getComputationName() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  protected SEXP cloneWithNewAttributes(AttributeMap attributes) {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public double getElementAsDouble(int index) {
    return 0;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public boolean isConstantAccessTime() {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public int length() {
    return 0;  //To change body of implemented methods use File | Settings | File Templates.
  }
}
