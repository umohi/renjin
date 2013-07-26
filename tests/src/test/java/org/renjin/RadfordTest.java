package org.renjin;


import org.junit.Test;
import org.renjin.eval.Session;
import org.renjin.eval.SessionBuilder;
import org.renjin.script.RenjinScriptEngine;
import org.renjin.script.RenjinScriptEngineFactory;

import javax.script.ScriptException;

public class RadfordTest  {
  
  @Test
  public void test() throws ScriptException {

    Session session = new SessionBuilder().withDefaultPackages().build();
    RenjinScriptEngineFactory factory = new RenjinScriptEngineFactory();
    RenjinScriptEngine engine = factory.getScriptEngine(session);

    engine.eval("source('/home/alexander/dev/radford/test.R')");
    while(true) {
      engine.eval("doit(f2, 2500)");
    }
    
  }
}
