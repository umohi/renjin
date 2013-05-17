package org.renjin.gcc.translate.var;

import java.util.List;

import org.renjin.gcc.gimple.GimpleOp;
import org.renjin.gcc.gimple.expr.GimpleExpr;
import org.renjin.gcc.gimple.type.ArrayType;
import org.renjin.gcc.gimple.type.GimpleType;
import org.renjin.gcc.gimple.type.IndirectType;
import org.renjin.gcc.gimple.type.PrimitiveType;
import org.renjin.gcc.jimple.Jimple;
import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.jimple.JimpleType;
import org.renjin.gcc.translate.FunctionContext;
import org.renjin.gcc.translate.types.PrimitiveTypes;

/** 
 * A variable storing a pointer to an array of primitives
 */
public class PrimitiveArrayPtrVar extends Variable {


  private FunctionContext context;
  private GimpleType type;
  private String jimpleArrayName;
  private String jimpleOffsetName;

  public PrimitiveArrayPtrVar(FunctionContext context, String gimpleName, IndirectType type) {
    this.context = context;
    this.type = type;
    this.jimpleArrayName = Jimple.id(gimpleName) + "_array";
    this.jimpleOffsetName = Jimple.id(gimpleName + "_offset");
    
    ArrayType arrayType = type.getBaseType();
    PrimitiveType primitiveType = (PrimitiveType) arrayType.getComponentType();
    
    context.getBuilder().addVarDecl(PrimitiveTypes.getArrayType(primitiveType), jimpleArrayName);
    context.getBuilder().addVarDecl(JimpleType.INT, jimpleOffsetName);
  }

  
  
  @Override
  public GimpleType type() {
    return type;
  }

  @Override
  public JimpleExpr returnExpr() {
    throw new UnsupportedOperationException();
  }

}
