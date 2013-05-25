package org.renjin.gcc.translate.type.struct;

import com.google.common.collect.Maps;
import org.renjin.gcc.gimple.type.GimpleField;
import org.renjin.gcc.gimple.type.GimpleRecordType;
import org.renjin.gcc.jimple.*;
import org.renjin.gcc.translate.FunctionContext;
import org.renjin.gcc.translate.TranslationContext;
import org.renjin.gcc.translate.VarUsage;
import org.renjin.gcc.translate.type.ImType;
import org.renjin.gcc.translate.var.SimpleRecordVar;
import org.renjin.gcc.translate.var.Variable;

import java.util.Map;

/**
 * Represents a simple implementation of a record type, where
 * each gimple field is mapped to a public JVM field.
 */
public class SimpleRecordType extends ImRecordType {
  private TranslationContext context;
  private GimpleRecordType gimpleType;
  private String name;
  private JimpleClassBuilder recordClass;

  private Map<String, JimpleType> types = Maps.newHashMap();

  public SimpleRecordType(TranslationContext context, GimpleRecordType recordType) {
    this.context = context;
    gimpleType = recordType;
    this.name = recordType.getName();
    this.recordClass = context.getJimpleOutput().newClass();
    this.recordClass.setPackageName(context.getMainClass().getPackageName());
    this.recordClass.setClassName(context.getMainClass().getClassName() + "$" + name);


    for (GimpleField member : recordType.getFields()) {
      JimpleType type = context.resolveType(member.getType()).paramType();
      types.put(member.getName(), type);

      JimpleFieldBuilder field = recordClass.newField();
      field.setName(member.getName());
      field.setType(type);
      field.setModifiers(JimpleModifiers.PUBLIC);
    }
  }

  @Override
  public JimpleType getJimpleType() {
    return new SyntheticJimpleType(recordClass.getFqcn());
  }

  @Override
  public ImType pointerType() {
    return new SimpleRecordPtrType(context, gimpleType);
  }

  @Override
  public JimpleType paramType() {
    return getJimpleType();
  }

  @Override
  public JimpleType returnType() {
    return getJimpleType();
  }

  @Override
  public Variable createLocalVariable(FunctionContext functionContext, String gimpleName, VarUsage varUsage) {
    return new SimpleRecordVar(functionContext, gimpleName, this);
  }

  public JimpleType getMemberType(String member) {
    return types.get(member);
  }
}
