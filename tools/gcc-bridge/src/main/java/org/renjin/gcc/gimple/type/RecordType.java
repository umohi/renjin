package org.renjin.gcc.gimple.type;

import java.util.List;

import com.google.common.collect.Lists;

public class RecordType extends AbstractGimpleType {
  private String name;
  private int id;
  
  private List<Field> fields = Lists.newArrayList();
    
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  } 
  

  public List<Field> getFields() {
    return fields;
  }

  @Override
  public String toString() {
    return "struct " + name;
  }
}
