package net.sf.anathema.hero.template.magic;

import net.sf.anathema.hero.traits.model.types.AbilityType;

public class AbilityFavoringType implements FavoringTraitType {
  @Override
  public AbilityType[] getTraitTypesForGenericCharms() {
    return AbilityType.values();
  }

  @Override
  public String getId() {
    return "Abilities";
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof AbilityFavoringType;
  }

  @Override
  public int hashCode() {
    return 1;
  }
}