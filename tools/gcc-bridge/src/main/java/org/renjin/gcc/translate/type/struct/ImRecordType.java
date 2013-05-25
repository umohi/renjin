package org.renjin.gcc.translate.type.struct;

import org.renjin.gcc.jimple.JimpleType;
import org.renjin.gcc.translate.type.ImType;

/**
 *
 */
public abstract class ImRecordType extends ImType {

  public abstract JimpleType getJimpleType();

  public abstract ImType pointerType();
}
