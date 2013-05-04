package org.renjin.gcc.gimple;

import org.renjin.gcc.gimple.type.GimpleType;

public class GimpleVarDecl {
	private int id;
	private GimpleType type;
	private String name;
	private Object constantValue;

	public GimpleVarDecl() {
		
	}
	
	public GimpleVarDecl(GimpleType type, String name) {
		super();
		this.type = type;
		this.name = name;
		constantValue = null;
	}

	public GimpleVarDecl(GimpleType type, String name, Object constantValue) {
		this.type = type;
		this.name = name;
		this.constantValue = constantValue;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public GimpleType getType() {
		return type;
	}

	public void setType(GimpleType type) {
		this.type = type;
	}

	public String getName() {
		if(name != null) {
			return name;
		} else {
			return "T" + Math.abs(id);
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getConstantValue() {
		return constantValue;
	}

	@Override
	public String toString() {
		return type + " " + (name == null ? "T" + Math.abs(id) : name);
	}

}
