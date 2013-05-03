package org.renjin.gcc.gimple.expr;


public class GimpleIntegerConstant extends GimpleConstant {
	private int value;

	public Integer getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
}
