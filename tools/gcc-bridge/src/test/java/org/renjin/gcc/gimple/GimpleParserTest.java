package org.renjin.gcc.gimple;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class GimpleParserTest {

	@Test
	public void test() throws JsonParseException, JsonMappingException, IOException {
		
		GimpleParser parser = new GimpleParser();
		GimpleCompilationUnit unit = parser.parse(getClass().getResource("dqrdc2.f.json"));
		
		System.out.println(unit.getFunctions());
	}
	
}
