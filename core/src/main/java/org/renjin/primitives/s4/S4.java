package org.renjin.primitives.s4;


import org.renjin.primitives.annotations.ArgumentList;
import org.renjin.primitives.annotations.Current;
import org.renjin.primitives.annotations.Primitive;
import org.renjin.primitives.annotations.SessionScoped;
import org.renjin.sexp.*;

public class S4 {

  @Primitive
  public static String setClass(@Current MethodTable methodTable,
                              String className, ListVector representation) {

    methodTable.setClass(className, representation);

    return className;
  }


  @Primitive
  public static ListVector representation(@ArgumentList ListVector slots) {
    return slots;
  }

  @Primitive("new")
  public static S4Object newS4Instance(String className, @ArgumentList ListVector slots) {

    S4ObjectBuilder object = new S4ObjectBuilder(className);
    for(NamedValue slot : slots.namedValues()) {
      object.setSlot(slot.getName(), slot.getValue());
    }
    return object.build();

  }

  @Primitive
  public static String setGeneric(@Current MethodTable methodTable,
                                  String name,
                                  Function def) {

    throw new UnsupportedOperationException();

  }

  @Primitive
  public static String setMethod(@Current MethodTable methodTable,
                                 String methodName, StringVector classSignature,
                                 Function definition) {


    S4Method method = new S4Method(methodName,
        new S4MethodSignature(classSignature),
        definition);

    methodTable.addMethod(method);



    return methodName;
  }
}
