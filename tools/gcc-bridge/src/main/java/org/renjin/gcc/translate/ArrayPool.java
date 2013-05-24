package org.renjin.gcc.translate;


import com.google.common.collect.Maps;
import org.renjin.gcc.jimple.*;

import java.util.Map;

public class ArrayPool {
//
//
//  public static class MemRef {
//    private JimpleExpr arrayExpr;
//    private int offset;
//
//    public JimpleExpr getArrayExpr() {
//      return arrayExpr;
//    }
//
//    public int getOffset() {
//      return offset;
//    }
//  }
//  private FunctionContext context;
//  private Map<Object, MemRef> constantPool = Maps.newHashMap();
//
//  public ArrayPool(FunctionContext context) {
//    this.context = context;
//  }
//
//  private static final String CHAR_ARRAY = "__STRING_POOL";
//  private StringBuilder strings = new StringBuilder();
//
//  public MemRef get(String string) {
//    MemRef ref = constantPool.get(string);
//    if(ref == null) {
//      ref = new MemRef();
//      ref.offset = strings.length();
//      ref.arrayExpr = new JimpleExpr(CHAR_ARRAY);
//
//      strings.append(string);
//      constantPool.put(string, ref);
//    }
//    return ref;
//  }
//
//  public void writeJimple(JimpleClassBuilder mainClass) {
//    if(strings.length() > 0) {
//      writeStringPool(mainClass);
//    }
//  }
//
//  private void writeStringPool() {
//    context.getBuilder().addVarDecl(char[].class, CHAR_ARRAY);
//
//    JimpleFieldBuilder field = mainClass.newField();
//    field.setName(CHAR_ARRAY);
//    field.setModifiers(JimpleModifiers.PRIVATE, JimpleModifiers.FINAL, JimpleModifiers.STATIC);
//    field.setType(new RealJimpleType(char[].class));
//
//
//    JimpleMethodBuilder clinit = mainClass.getStaticInitializer();
//    clinit.addVarDecl(String.class, "charPoolContents");
//    clinit.addVarDecl(char[].class, "charPoolArray");
//
//    clinit.addStatement("charPoolContents = \"" + escapeString() + "\"");
//    clinit.addStatement("charPoolArray = virtualinvoke charPoolContents.<java.lang.String: char[] toCharArray()>()");
//    clinit.addStatement("<Test: char[] __STRING_POOL> = temp$1; = charPoolArray");
//
//  }
//

}
