package org.renjin.primitives.s4;

import org.junit.Test;
import org.renjin.EvalTestCase;

public class S4Test extends EvalTestCase{

  @Test
  public void bmi() {
    eval("setClass('BMI', representation(weight='numeric', size='numeric'))");
    eval("myBMI <- new('BMI',weight=85,size=1.84)");
    eval("setMethod('show', 'BMI', function(object){cat('BMI=',object@weight/(object@size^2), ' \n  ')})");

    eval("show(myBMI)");
  }

}
