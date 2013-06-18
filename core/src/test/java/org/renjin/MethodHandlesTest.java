package org.renjin;

import com.google.common.base.Stopwatch;
import org.junit.Test;
import org.renjin.invoke.LengthFunction;
import org.renjin.invoke.LengthFunctionMH;
import org.renjin.invoke.reflection.ClassBindingImpl;
import org.renjin.primitives.Types;
import org.renjin.sexp.IntArrayVector;

import java.util.Random;

import static org.junit.Assert.assertThat;


public class MethodHandlesTest extends EvalTestCase {

  private final Random random = new Random();

  @Test
  public void test() {

    global.setVariable("lengthg", new LengthFunction());
    global.setVariable("lengthmh", new LengthFunctionMH());
    global.setVariable("lengthr", ClassBindingImpl.get(Types.class).getStaticMember("length"));


    timeEval("lengthmh(x)");

  }

  private void timeEval(String expr) {

    int numIterations[] = new int[] { 1000, 10_000, 50_000, 100_000, 1_000_000, 10_000_000 };

    Stopwatch stopwatch = new Stopwatch().start();

    int n = 0;
    for(int p=0;p<numIterations.length;++p) {
      int limit = numIterations[p];
      while(n < limit) {
        int size = random.nextInt(32);
        global.setVariable("x", new IntArrayVector(new int[size]));
        assertThat(eval(expr), elementsEqualTo(size));
        n++;
      }
      System.out.println(expr + "," + n + "," + ((double)stopwatch.elapsedMillis()) / ((double)n));
    }
  }

}
