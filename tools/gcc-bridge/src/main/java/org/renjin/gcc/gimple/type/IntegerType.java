package org.renjin.gcc.gimple.type;

public class IntegerType extends PrimitiveType {
	private int precision;
	private boolean unsigned;

	/**
	 * 
	 * @return The number of bits of precision
	 */
	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public boolean isUnsigned() {
		return unsigned;
	}

	public void setUnsigned(boolean unsigned) {
		this.unsigned = unsigned;
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		if(unsigned) {
			s.append("unsigned ");
		}
		s.append("int" + precision);
		return s.toString();
	}
}
