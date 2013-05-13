package org.renjin.gcc.gimple;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.renjin.gcc.CallingConvention;
import org.renjin.gcc.F77CallingConvention;
import org.renjin.gcc.gimple.expr.GimpleExpr;
import org.renjin.gcc.gimple.expr.GimpleNull;
import org.renjin.gcc.gimple.expr.GimpleVariableRef;
import org.renjin.gcc.gimple.type.GimpleType;
import org.renjin.gcc.gimple.type.PrimitiveType;

import java.util.List;
import java.util.Map;

public class GimpleFunction {
  private int id;
  private String name;
  private CallingConvention callingConvention;
  private GimpleType returnType;
  private List<GimpleBasicBlock> basicBlocks = Lists.newArrayList();
  private List<GimpleParameter> parameters = Lists.newArrayList();
  private List<GimpleVarDecl> variableDeclarations = Lists.newArrayList();
  private Map<String, GimpleType> typeMap = Maps.newHashMap();

  public GimpleFunction() {
    this.callingConvention = new F77CallingConvention();
  }

  public void setCallingConvention(CallingConvention callingConvention) {
    this.callingConvention = callingConvention;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void addParameter(GimpleParameter parameter) {
    parameters.add(parameter);
    typeMap.put(parameter.getName(), parameter.getType());
  }

  public GimpleBasicBlock addBasicBlock(String label) {
    if (!label.toLowerCase().startsWith("bb ")) {
      throw new IllegalArgumentException("Expected label in the form 'BB 999', got: '" + label + "'");
    }
    GimpleBasicBlock bb = new GimpleBasicBlock();
    basicBlocks.add(bb);
    return bb;
  }

  public void addVarDecl(GimpleVarDecl decl) {
    variableDeclarations.add(decl);
    typeMap.put(decl.getName(), decl.getType());
  }

  public boolean hasVariable(String name) {
    return typeMap.containsKey(name);
  }

  public GimpleType getVariableType(String name) {
    if (typeMap.containsKey(name)) {
      return typeMap.get(name);
    }
    throw new IllegalArgumentException(name);
  }

  public List<GimpleVarDecl> getVariableDeclarations() {
    return variableDeclarations;
  }

  public GimpleType getType(GimpleExpr expr) {
    if (expr instanceof GimpleVariableRef) {
      return getVariableType(((GimpleVariableRef) expr).getName());
    } else if (expr == GimpleNull.INSTANCE) {
      return PrimitiveType.VOID_TYPE;
    } else {
      throw new UnsupportedOperationException("don't know how to deduce type for '" + expr + "'");
    }
  }

  public List<GimpleParameter> getParameters() {
    return parameters;
  }

  public void setBasicBlocks(List<GimpleBasicBlock> basicBlocks) {
    this.basicBlocks = basicBlocks;
  }

  public void setParameters(List<GimpleParameter> parameters) {
    this.parameters = parameters;
  }

  public void visitIns(GimpleVisitor visitor) {
    for (GimpleBasicBlock bb : basicBlocks) {
      visitor.blockStart(bb);
      for (GimpleIns ins : bb.getInstructions()) {
        ins.visit(visitor);
      }
    }
  }

  public List<GimpleBasicBlock> getBasicBlocks() {
    return basicBlocks;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(name).append(" (");
    Joiner.on(", ").appendTo(sb, parameters);
    sb.append(")\n");
    sb.append("{\n");
    for (GimpleVarDecl decl : variableDeclarations) {
      sb.append(decl).append("\n");
    }
    for (GimpleBasicBlock bb : basicBlocks) {
      sb.append(bb.toString());
    }
    sb.append("}\n");
    return sb.toString();
  }

  public CallingConvention getCallingConvention() {
    return callingConvention;
  }

  public GimpleType getReturnType() {
    return returnType;
  }

  public void setReturnType(GimpleType returnType) {
    this.returnType = returnType;
  }

}
