package org.renjin.gcc.gimple;

import java.util.List;

import org.renjin.gcc.CallingConvention;
import org.renjin.gcc.F77CallingConvention;
import org.renjin.gcc.gimple.expr.GimpleExpr;
import org.renjin.gcc.gimple.expr.GimpleNull;
import org.renjin.gcc.gimple.expr.GimpleParamRef;
import org.renjin.gcc.gimple.expr.SymbolRef;
import org.renjin.gcc.gimple.type.GimpleType;
import org.renjin.gcc.gimple.type.VoidType;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public class GimpleFunction {
  private int id;
  private String name;
  private CallingConvention callingConvention;
  private GimpleType returnType;
  private List<GimpleBasicBlock> basicBlocks = Lists.newArrayList();
  private List<GimpleParameter> parameters = Lists.newArrayList();
  private List<GimpleVarDecl> variableDeclarations = Lists.newArrayList();

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

  public List<GimpleVarDecl> getVariableDeclarations() {
    return variableDeclarations;
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
