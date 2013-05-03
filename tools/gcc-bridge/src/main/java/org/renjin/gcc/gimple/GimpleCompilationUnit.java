package org.renjin.gcc.gimple;

import java.util.List;

import com.google.common.collect.Lists;

public class GimpleCompilationUnit {
	
	private List<GimpleFunction> functions = Lists.newArrayList();

	public List<GimpleFunction> getFunctions() {
		return functions;
	}
}
