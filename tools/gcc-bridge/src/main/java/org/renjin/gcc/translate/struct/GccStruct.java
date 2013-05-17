package org.renjin.gcc.translate.struct;

import com.google.common.collect.Maps;
import org.renjin.gcc.gimple.GimpleFunction;
import org.renjin.gcc.gimple.type.Field;
import org.renjin.gcc.gimple.type.GimpleType;
import org.renjin.gcc.gimple.type.RecordType;
import org.renjin.gcc.jimple.*;
import org.renjin.gcc.translate.FunctionContext;
import org.renjin.gcc.translate.TranslationContext;

import java.util.Map;

/**
 * Represents a "struct" that is constructed from Gcc output.
 */
public class GccStruct extends Struct {
  private TranslationContext context;
  private String name;
  private JimpleClassBuilder structClass;

  private Map<String, JimpleType> types = Maps.newHashMap();

  public GccStruct(TranslationContext context, RecordType recordType) {
    this.context = context;
    this.name = recordType.getName();
    this.structClass = context.getJimpleOutput().newClass();
    this.structClass.setPackageName(context.getMainClass().getPackageName());
    this.structClass.setClassName(context.getMainClass().getClassName() + "$" + name);


    for (Field member : recordType.getFields()) {
      JimpleType type = context.resolveType(member.getType()).paramType();
      types.put(member.getName(), type);

      JimpleFieldBuilder field = structClass.newField();
      field.setName(member.getName());
      field.setType(type);
      field.setModifiers(JimpleModifiers.PUBLIC);
    }
  }

  public JimpleExpr memberRef(JimpleExpr instanceExpr, String member, JimpleType jimpleType) {
    return new JimpleExpr(instanceExpr + ".<" + structClass.getFqcn() + ": " + types.get(member) + " " + member + ">");
  }

  @Override
  public void assignMember(FunctionContext context, JimpleExpr instance, String member, JimpleExpr jimpleExpr) {
    context.getBuilder().addStatement(
        instance + ".<" + structClass.getFqcn() + ": " + types.get(member) + " " + member + "> = " + jimpleExpr);
  }

  @Override
  public JimpleType getJimpleType() {
    return new StructJimpleType(structClass.getFqcn());
  }
}
