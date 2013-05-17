package org.renjin.gcc.translate.var;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import org.renjin.gcc.gimple.type.GimpleType;
import org.renjin.gcc.gimple.type.IndirectType;
import org.renjin.gcc.gimple.type.PointerType;
import org.renjin.gcc.gimple.type.PrimitiveType;
import org.renjin.gcc.jimple.Jimple;
import org.renjin.gcc.jimple.JimpleExpr;
import org.renjin.gcc.jimple.JimpleType;
import org.renjin.gcc.jimple.RealJimpleType;
import org.renjin.gcc.translate.FunctionContext;
import org.renjin.gcc.translate.TypeChecker;
import org.renjin.gcc.translate.assign.NullAssignable;
import org.renjin.gcc.translate.assign.PrimitiveAssignable;
import org.renjin.gcc.translate.expr.AbstractExpr;
import org.renjin.gcc.translate.expr.Expr;
import org.renjin.gcc.translate.expr.IndirectExpr;
import org.renjin.gcc.translate.types.PrimitiveTypes;

public class PrimitivePtrVar extends Variable implements NullAssignable, IndirectExpr {

  private enum OffsetType {
    BYTES,
    ELEMENTS
  }

  private FunctionContext context;
  private String gimpleName;
  private IndirectType pointerType;
  private PrimitiveType gimpleType;
  private String jimpleArrayName;
  private String jimpleOffsetName;
  private JimpleType arrayType;
  private JimpleType wrapperType;

  public PrimitivePtrVar(FunctionContext context, String gimpleName, IndirectType type) {
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

  @Override
  public void initFromParameter() {
    assignFromWrapper(new JimpleExpr(Jimple.id(gimpleName)));
  }

  public void assignFromWrapper(JimpleExpr wrapperExpr) {
    context.getBuilder().addStatement(
        jimpleArrayName + " = " + wrapperExpr + ".<" + wrapperType + ": " + arrayType + " array>");
    context.getBuilder().addStatement(jimpleOffsetName + " = " + wrapperExpr + ".<" + wrapperType + ": int offset>");
  }

  private int sizeOf() {
    return gimpleType.getSize() / 8;
  }

  @Override
  public void assign(Expr expr) {
    // TODO Auto-generated method stub
    super.assign(expr);
  }

  @Override
  public void setToNull() {
    // TODO Auto-generated method stub

  }

  public void assignStringConstant(String literal) {
    String stringVar = context.getBuilder().addTempVarDecl(new RealJimpleType(String.class));
    context.getBuilder().addStatement(stringVar + " = " + JimpleExpr.stringLiteral(literal));
    context.getBuilder().addStatement(
        jimpleArrayName + " = virtualinvoke " + stringVar + ".<java.lang.String: char[] toCharArray()>()");
    context.getBuilder().addStatement(jimpleOffsetName + " = 0");
  }

  public JimpleExpr wrapPointer() {
    JimpleType wrapperType = PrimitiveTypes.getWrapperType(gimpleType);
    String tempWrapper = context.declareTemp(wrapperType);
    context.getBuilder().addStatement(tempWrapper + " = new " + wrapperType);
    context.getBuilder().addStatement(
        "specialinvoke " + tempWrapper + ".<" + wrapperType + ": void <init>("
            + PrimitiveTypes.getArrayType(gimpleType) + ", int)>(" + jimpleArrayName + ", " + jimpleOffsetName + ")");

    return new JimpleExpr(tempWrapper);
  }


  @Override
  public JimpleExpr backingArray() {
    return new JimpleExpr(jimpleArrayName);
  }

  @Override
  public JimpleExpr backingArrayIndex() {
    return new JimpleExpr(jimpleOffsetName);
  }

  @Override
  public JimpleExpr returnExpr() {
    return wrapPointer();
  }

  @Override
  public Expr value() {
    return new ValueExpr();
  }

  @Override
  public IndirectType type() {
    return pointerType;
  }

  public void assign(PrimitivePtrVar var) {
    context.getBuilder().addStatement(jimpleArrayName + " = " + var.jimpleArrayName);
    context.getBuilder().addStatement(jimpleOffsetName + " = " + var.jimpleOffsetName);
  }

  public void assign(OffsetExpr offset) {
    if(offset.variable() != this) {
      context.getBuilder().addStatement(jimpleArrayName + " = " + offset.variable().jimpleArrayName);
    }
    JimpleExpr bytesToIncrement = offset.offset.asPrimitiveValue(context);
    String positionsToIncrement = context.declareTemp(JimpleType.INT);
    context.getBuilder().addStatement(positionsToIncrement + " = " + bytesToIncrement + " / " + sizeOf() + "L");
    context.getBuilder().addStatement(jimpleOffsetName + " = " + offset.variable().jimpleOffsetName + " + " + positionsToIncrement);    
  }

  @Override
  public Expr pointerPlus(Expr offset) {
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
  public class OffsetExpr extends AbstractExpr implements IndirectExpr {

    private Expr offset;
    private OffsetType offsetType;


    public OffsetExpr(Expr offset, OffsetType offsetType) {
      super();
      this.offset = offset;
      this.offsetType = offsetType;
    }

    @Override
    public IndirectType type() {
      return PrimitivePtrVar.this.type();
    }

    public PrimitivePtrVar variable() {
      return PrimitivePtrVar.this;
    }

    public JimpleExpr backingArray() {
      return PrimitivePtrVar.this.backingArray();
    }

    public JimpleExpr backingArrayIndex() {
      if(offsetType == OffsetType.BYTES) {
        JimpleExpr bytesToIncrement = offset.asPrimitiveValue(context);
        String positionsToIncrement = context.declareTemp(JimpleType.INT);
        context.getBuilder().addStatement(positionsToIncrement + " = " + bytesToIncrement + " / " + sizeOf() + "L");
        return new JimpleExpr(jimpleOffsetName + " + " + positionsToIncrement);    
      } else {
        return new JimpleExpr(jimpleOffsetName + " + " + offset.asPrimitiveValue(context));    
      }
    }
  }

  /**
   * An expression representing the value of the pointer
   * (*x)
   *
   */
  public class ValueExpr extends AbstractExpr implements PrimitiveAssignable {


    @Override
    public Expr addressOf() {
      return PrimitivePtrVar.this;
    }

    @Override
    public JimpleExpr asPrimitiveValue(FunctionContext context) {
      return new JimpleExpr(jimpleArrayName + "[" + jimpleOffsetName + "]");
    }

    @Override
    public GimpleType type() {
      return pointerType.getBaseType();
    }

    @Override
    public void assignPrimitiveValue(JimpleExpr expr) {
      context.getBuilder().addStatement(jimpleArrayName + "[" + jimpleOffsetName + "] = " + expr);
    }

    @Override
    public Expr elementAt(Expr index) {
      return new ArrayElementExpr(index);
    }

  }

  public class ArrayElementExpr extends AbstractExpr implements PrimitiveAssignable {

    /**
     * Index of the array, with reference to the current offset.
     */
    private Expr index;

    public ArrayElementExpr(Expr index) {
      if(TypeChecker.isInt(index.type())) {
        throw new UnsupportedOperationException();
      }
      this.index = index;
    }

    @Override
    public JimpleExpr asPrimitiveValue(FunctionContext context) {
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
              index.asPrimitiveValue(context));
      return overallIndex;
    }


    @Override
    public Expr addressOf() {
      return new OffsetExpr(index, OffsetType.ELEMENTS);
    }

    @Override
    public GimpleType type() {
      return pointerType.getBaseType();
    }

    @Override
    public void assignPrimitiveValue(JimpleExpr expr) {
      context.getBuilder().addStatement(jimpleArrayName + "[" + computeOverallIndex(context) + "] = " + expr);
    }
  }
}

