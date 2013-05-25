package org.renjin.gcc.translate.var;

import org.renjin.gcc.gimple.type.GimpleIndirectType;
import org.renjin.gcc.gimple.type.GimplePrimitiveType;
import org.renjin.gcc.gimple.type.GimpleType;
import org.renjin.gcc.jimple.Jimple;
import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.jimple.JimpleType;
import org.renjin.gcc.translate.FunctionContext;
import org.renjin.gcc.translate.TypeChecker;
import org.renjin.gcc.translate.PrimitiveAssignment;
import org.renjin.gcc.translate.expr.*;
import org.renjin.gcc.translate.type.PrimitiveTypes;


public class PrimitivePtrVar extends AbstractImExpr implements Variable, ImIndirectExpr {

  private enum OffsetType {
    BYTES,
    ELEMENTS
  }

  private FunctionContext context;
  private String gimpleName;
  private GimpleIndirectType pointerType;
  private GimplePrimitiveType gimpleType;
  private String jimpleArrayName;
  private String jimpleOffsetName;
  private JimpleType arrayType;
  private JimpleType wrapperType;

  public PrimitivePtrVar(FunctionContext context, String gimpleName, GimpleIndirectType type) {
    this.context = context;
    this.gimpleName = gimpleName;
    this.pointerType = type;
    this.gimpleType = type.getBaseType();
    this.jimpleArrayName = Jimple.id(gimpleName) + "_array";
    this.jimpleOffsetName = Jimple.id(gimpleName + "_offset");
    this.arrayType = PrimitiveTypes.getArrayType(gimpleType);
    this.wrapperType = PrimitiveTypes.getWrapperType(gimpleType);

    context.getBuilder().addVarDecl(arrayType, jimpleArrayName);
    context.getBuilder().addVarDecl(JimpleType.INT, jimpleOffsetName);
  }

  private int sizeOf() {
    return gimpleType.getSize() / 8;
  }

  @Override
  public void writeAssignment(FunctionContext context, ImExpr rhs) {
    if(rhs.isNull()) {
      context.getBuilder().addStatement(jimpleArrayName + " = null");
    } else if(rhs instanceof ImIndirectExpr) {
      ArrayRef ptr = ((ImIndirectExpr) rhs).translateToArrayRef(context);
      context.getBuilder().addStatement(jimpleArrayName + " = " +  ptr.getArrayExpr());
      context.getBuilder().addStatement(jimpleOffsetName + " = " + ptr.getIndexExpr());
    }
  }

  private JimpleExpr wrapPointer() {
    JimpleType wrapperType = PrimitiveTypes.getWrapperType(gimpleType);
    String tempWrapper = context.declareTemp(wrapperType);
    context.getBuilder().addStatement(tempWrapper + " = new " + wrapperType);
    context.getBuilder().addStatement(
        "specialinvoke " + tempWrapper + ".<" + wrapperType + ": void <init>("
            + PrimitiveTypes.getArrayType(gimpleType) + ", int)>(" + jimpleArrayName + ", " + jimpleOffsetName + ")");

    return new JimpleExpr(tempWrapper);
  }

  @Override
  public ArrayRef translateToArrayRef(FunctionContext context) {
    return new ArrayRef(jimpleArrayName, jimpleOffsetName);
  }

  @Override
  public ImExpr memref() {
    return new ValueExpr();
  }

  @Override
  public GimpleIndirectType type() {
    return pointerType;
  }

  @Override
  public ImExpr pointerPlus(ImExpr offset) {
    return new OffsetExpr(offset, OffsetType.BYTES);
  }

  @Override
  public String toString() {
    return gimpleName + ":" + pointerType;
  }

  /**
   * An expression representing this pointer + an offset (p+4)
   *
   */
  public class OffsetExpr extends AbstractImExpr implements ImIndirectExpr {

    private ImExpr offset;
    private OffsetType offsetType;


    public OffsetExpr(ImExpr offset, OffsetType offsetType) {
      super();
      this.offset = offset;
      this.offsetType = offsetType;
    }

    @Override
    public GimpleIndirectType type() {
      return PrimitivePtrVar.this.type();
    }

    public PrimitivePtrVar variable() {
      return PrimitivePtrVar.this;
    }

    @Override
    public ArrayRef translateToArrayRef(FunctionContext context) {
      return new ArrayRef(jimpleArrayName, computeIndex());
    }

    private JimpleExpr computeIndex() {
      if(offsetType == OffsetType.BYTES) {
        JimpleExpr bytesToIncrement = offset.translateToPrimitive(context);
        String positionsToIncrement = context.declareTemp(JimpleType.INT);
        context.getBuilder().addStatement(positionsToIncrement + " = " + bytesToIncrement + " / " + sizeOf());
        return new JimpleExpr(jimpleOffsetName + " + " + positionsToIncrement);    
      } else {
        return new JimpleExpr(jimpleOffsetName + " + " + offset.translateToPrimitive(context));
      }
    }
  }

  /**
   * An expression representing the value of the pointer
   * (*x)
   *
   */
  public class ValueExpr extends AbstractImExpr implements PrimitiveLValue, ImLValue {


    @Override
    public ImExpr addressOf() {
      return PrimitivePtrVar.this;
    }

    @Override
    public JimpleExpr translateToPrimitive(FunctionContext context) {
      return new JimpleExpr(jimpleArrayName + "[" + jimpleOffsetName + "]");
    }

    @Override
    public GimpleType type() {
      return pointerType.getBaseType();
    }

    @Override
    public void writePrimitiveAssignment(JimpleExpr expr) {
      context.getBuilder().addStatement(jimpleArrayName + "[" + jimpleOffsetName + "] = " + expr);
    }

    @Override
    public void writeAssignment(FunctionContext context, ImExpr rhs) {
      PrimitiveAssignment.assign(context, this, rhs);
    }

    @Override
    public ImExpr elementAt(ImExpr index) {
      return new ArrayElementExpr(index);
    }

  }

  public class ArrayElementExpr extends AbstractImExpr implements PrimitiveLValue, ImLValue {

    /**
     * Index of the array, with reference to the current offset.
     */
    private ImExpr index;

    public ArrayElementExpr(ImExpr index) {
      if(!TypeChecker.isInt(index.type())) {
        throw new UnsupportedOperationException();
      }
      this.index = index;
    }

    @Override
    public JimpleExpr translateToPrimitive(FunctionContext context) {
      // get the overall index
      return new JimpleExpr(jimpleArrayName + "[" + computeOverallIndex(context) + "]");
    }

    
    /**
     * Create a temporary variable storing the index of the element this expr references
     * with reference to the beginning of the array.
     * @return the name of the temporary variable
     */
    private String computeOverallIndex(FunctionContext context) {
      String overallIndex = context.declareTemp(JimpleType.INT);
      context.getBuilder().addStatement(overallIndex + " = " + jimpleOffsetName + " + " +
              index.translateToPrimitive(context));
      return overallIndex;
    }


    @Override
    public ImExpr addressOf() {
      return new OffsetExpr(index, OffsetType.ELEMENTS);
    }

    @Override
    public GimpleType type() {
      return pointerType.getBaseType();
    }

    @Override
    public void writePrimitiveAssignment(JimpleExpr expr) {
      context.getBuilder().addStatement(jimpleArrayName + "[" + computeOverallIndex(context) + "] = " + expr);
    }

    @Override
    public void writeAssignment(FunctionContext context, ImExpr rhs) {
      PrimitiveAssignment.assign(context, this, rhs);
    }
  }
}

