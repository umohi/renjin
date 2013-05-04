package org.renjin.gcc.translate.types;

import org.renjin.gcc.gimple.type.BooleanType;
import org.renjin.gcc.gimple.type.IntegerType;
import org.renjin.gcc.gimple.type.PrimitiveType;
import org.renjin.gcc.gimple.type.RealType;
import org.renjin.gcc.jimple.JimpleType;
import org.renjin.gcc.jimple.RealJimpleType;
import org.renjin.gcc.runtime.CharPtr;
import org.renjin.gcc.runtime.DoublePtr;
import org.renjin.gcc.runtime.IntPtr;

public class PrimitiveTypes {


	public static JimpleType get(PrimitiveType type) {
		if(type instanceof RealType) {
			if(((RealType) type).getPrecision() == 64) {
				return JimpleType.DOUBLE;
			} else if(((RealType) type).getPrecision() == 32) {
				return JimpleType.FLOAT;
			}
		} else if(type instanceof IntegerType) {
			if(((IntegerType) type).getPrecision() == 64) {
				return JimpleType.LONG;
			} else if(((IntegerType) type).getPrecision() == 32) {
				return JimpleType.INT;
			}
		} else if(type instanceof BooleanType) {
			return JimpleType.BOOLEAN;
		} 
		throw new UnsupportedOperationException("type:" + type);
	}

	public static JimpleType getArrayType(PrimitiveType type) {
		if(type instanceof RealType) {
			if(((RealType) type).getPrecision() == 64) {
				return new RealJimpleType(double[].class);
			} else if(((RealType) type).getPrecision() == 32) {
				return new RealJimpleType(float[].class);
			}
		} else if(type instanceof IntegerType) {
			if(((IntegerType) type).getPrecision() == 64) {
				return new RealJimpleType(long[].class);
			} else if(((IntegerType) type).getPrecision() == 32) {
				return new RealJimpleType(int[].class);
			}
		} else if(type instanceof BooleanType) {
			return new RealJimpleType(boolean[].class);
		} 
		throw new UnsupportedOperationException(type.toString());
	}

	public static JimpleType getWrapperType(PrimitiveType type) {
		if(type instanceof RealType) {
			if(((RealType) type).getPrecision() == 64) {
				return new RealJimpleType(DoublePtr.class);
			} else if(((RealType) type).getPrecision() == 32) {
				// TODO
			}
		} else if(type instanceof IntegerType) {
			if(((IntegerType) type).getPrecision() == 64) {
				// TODO
			} else if(((IntegerType) type).getPrecision() == 32) {
				return new RealJimpleType(IntPtr.class);
			}
		} else if(type instanceof BooleanType) {
			// TODO
		} 
		throw new UnsupportedOperationException(type.toString());
	}
}
