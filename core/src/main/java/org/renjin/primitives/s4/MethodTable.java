package org.renjin.primitives.s4;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.renjin.primitives.annotations.ArgumentList;
import org.renjin.primitives.annotations.Primitive;
import org.renjin.primitives.annotations.SessionScoped;
import org.renjin.sexp.ListVector;
import org.renjin.sexp.NamedValue;
import org.renjin.sexp.S4Object;
import org.renjin.sexp.StringVector;

import java.util.List;
import java.util.Map;

@SessionScoped
public class MethodTable {

  private Map<String, S4Class> classMap = Maps.newHashMap();

  private List<S4Method> methods = Lists.newArrayList();

  public void setClass(String className, ListVector representation) {

    S4Class clazz = new S4Class(className);
    for(NamedValue slot : representation.namedValues()) {
      StringVector slotClass = (StringVector) slot.getValue();
      clazz.addSlot(slot.getName(), getClass(slotClass.getElementAsString(0)));
    }
  }

  private S4Class getClass(String name) {
    return classMap.get(name);
  }

  public void addMethod(S4Method method) {
    methods.add(method);
  }
}
