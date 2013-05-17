package org.renjin.gcc.translate.struct;

import com.google.common.collect.Maps;

import org.renjin.gcc.gimple.type.RecordType;
import org.renjin.gcc.translate.TranslationContext;

import java.util.Map;

/**
 * Maintains a mapping of Gimple Record types to our internal 
 * {@link Struct} objects.
 * 
 * <p>Struct objects may refer to a Gimple-defined record_type for which we
 * will create a JVM class during compilation, or an existing JVM class to which
 * we will map the fields to existing fields or to getters/setters.
 * 
 *
 */
public class StructTable {

  private final TranslationContext context;

  private final Map<String, Struct> map = Maps.newHashMap();

  public StructTable(TranslationContext context) {
    this.context = context;
  }

  public Struct resolveStruct(RecordType recordType) {
    if (map.containsKey(recordType.getName())) {
      return map.get(recordType.getName());
    } else {
      GccStruct struct = new GccStruct(context, recordType);
      map.put(recordType.getName(), struct);
      return struct;
    }
  }
}
