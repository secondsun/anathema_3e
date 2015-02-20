package net.sf.anathema.character.equipment.character.model.stats.modification.equipment;

import net.sf.anathema.equipment.stats.IWeaponModifiers;
import net.sf.anathema.equipment.stats.modification.StatModifier;

public class EquipmentDefenceModifier implements StatModifier {

  private final IWeaponModifiers modifiers;

  public EquipmentDefenceModifier(IWeaponModifiers modifiers) {
    this.modifiers = modifiers;
  }

  @Override
  public int calculate() {
    return modifiers.getPDVPoolMod();
  }
}