package org.renjin.gcc.translate.types;

import org.renjin.gcc.gimple.type.*;
import org.renjin.gcc.jimple.JimpleType;
import org.renjin.gcc.jimple.RealJimpleType;
import org.renjin.gcc.runtime.CharPtr;
import org.renjin.gcc.runtime.DoublePtr;
import org.renjin.gcc.runtime.IntPtr;
import org.renjin.gcc.runtime.LongPtr;

public class PrimitiveTypes {


  
  public static JimpleType get(GimpleType type) {
    if (type instanceof GimpleRealType) {
      if (((GimpleRealType) type).getPrecision() == 64) {
        return JimpleType.DOUBLE;
      } else if (((GimpleRealType) type).getPrecision() == 32) {
        return JimpleType.FLOAT;
      }
    } else if (type instanceof GimpleIntegerType) {
      int precision = ((GimpleIntegerType) type).getPrecision();
      switch(precision) {
      case 8:
        return JimpleType.CHAR;
      case 32:
        return JimpleType.INT;
      case 64:
        return JimpleType.LONG;
      }
    } else if (type instanceof GimpleBooleanType) {
      return JimpleType.BOOLEAN;
    }
    throw new UnsupportedOperationException("type:" + type);
  }

  public static JimpleType getArrayType(GimplePrimitiveType type) {
    if (type instanceof GimpleRealType) {
      if (((GimpleRealType) type).getPrecision() == 64) {
        return new RealJimpleType(double[].class);
      } else if (((GimpleRealType) type).getPrecision() == 32) {
        return new RealJimpleType(float[].class);
      }
    } else if (type instanceof GimpleIntegerType) {
      int precision = ((GimpleIntegerType) type).getPrecision();
      switch(precision) {
      case 8:
        return new RealJimpleType(char[].class);
      case 32:
        return new RealJimpleType(int[].class);
      case 64:
        return new RealJimpleType(long[].class);
      }
    } else if (type instanceof GimpleBooleanType) {
      return new RealJimpleType(boolean[].class);
    }
    throw new UnsupportedOperationException(type.toString());
  }

  public static JimpleType getWrapperType(GimplePrimitiveType type) {
    if (type instanceof GimpleRealType) {
      if (((GimpleRealType) type).getPrecision() == 64) {
        return new RealJimpleType(DoublePtr.class);
      } else if (((GimpleRealType) type).getPrecision() == 32) {
        // TODO
      }
    } else if (type instanceof GimpleIntegerType) {
      int precision = ((GimpleIntegerType) type).getPrecision();
      switch(precision) {
      case 8:
        return new RealJimpleType(CharPtr.class);
      case 32:
        return new RealJimpleType(IntPtr.class);
      case 64: 
        return new RealJimpleType(LongPtr.class);
      }
    } else if (type instanceof GimpleBooleanType) {
      // TODO
    }
    throw new UnsupportedOperationException(type.toString());
  }
}
