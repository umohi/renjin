package org.renjin.gcc.gimple.type;

public class ArrayType extends AbstractGimpleType {
	private GimpleType componentType;

	public GimpleType getComponentType() {
		return componentType;
	}

	public void setComponentType(GimpleType componentType) {
		this.componentType = componentType;
	}

	@Override
	public String toString() {
		return componentType + "[]";
	}
}
