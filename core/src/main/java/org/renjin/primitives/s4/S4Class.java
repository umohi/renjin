package org.renjin.primitives.s4;


import com.google.common.collect.Maps;

import java.util.Map;

public class S4Class {

  private final String name;
  private final Map<String, S4Slot> slots = Maps.newHashMap();

  public S4Class(String name) {
    this.name = name;
  }

  public void addSlot(String name, S4Class clazz) {
    S4Slot slot = new S4Slot();
    slot.setName(name);
    slot.setSlotClass(clazz);
    slots.put(name, slot);
  }


}
