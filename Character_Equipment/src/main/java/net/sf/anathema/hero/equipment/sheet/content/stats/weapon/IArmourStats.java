package net.sf.anathema.hero.equipment.sheet.content.stats.weapon;

import net.sf.anathema.library.identifier.Identifier;

import java.util.Collection;

public interface IArmourStats extends IEquipmentStats {

  Integer getMobilityPenalty();

  Integer getHardness();

  Integer getSoak();

  Collection<Identifier> getTags();
}