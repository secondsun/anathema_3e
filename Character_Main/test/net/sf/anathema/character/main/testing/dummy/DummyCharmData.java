package net.sf.anathema.character.main.testing.dummy;

import net.sf.anathema.character.main.magic.model.charm.Charm;
import net.sf.anathema.character.main.magic.model.charm.CharmData;
import net.sf.anathema.character.main.magic.model.magic.attribute.MagicAttribute;
import net.sf.anathema.character.main.magic.model.combos.IComboRestrictions;
import net.sf.anathema.character.main.magic.model.charm.IndirectCharmRequirement;
import net.sf.anathema.character.main.magic.model.charm.duration.SimpleDuration;
import net.sf.anathema.character.main.magic.model.charm.type.CharmType;
import net.sf.anathema.character.main.magic.model.charm.type.ICharmTypeModel;
import net.sf.anathema.character.main.magic.model.magic.cost.ICostList;
import net.sf.anathema.character.main.magic.parser.magic.IExaltedSourceBook;
import net.sf.anathema.character.main.traits.ValuedTraitType;
import net.sf.anathema.character.main.traits.TraitType;
import net.sf.anathema.character.main.traits.types.AbilityType;
import net.sf.anathema.character.main.type.ICharacterType;
import net.sf.anathema.lib.exception.NotYetImplementedException;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DummyCharmData implements CharmData {
  private final Set<Charm> parents = new HashSet<>();

  @Override
  public ICharacterType getCharacterType() {
    return null;
  }

  public CharmType getCharmType() {
    return null;
  }

  @Override
  public SimpleDuration getDuration() {
    return null;
  }

  @Override
  public ValuedTraitType getEssence() {
    return new net.sf.anathema.character.main.traits.types.ValuedTraitType(null, 1);
  }

  @Override
  public List<String> getParentSubEffects() {
    throw new NotYetImplementedException();
  }

  @Override
  public ValuedTraitType[] getPrerequisites() {
    return new ValuedTraitType[0];
  }

  @Override
  public String getGroupId() {
    return null;
  }

  @Override
  public boolean isInstanceOfGenericCharm() {
    return false;
  }

  @Override
  public IComboRestrictions getComboRules() {
    return null;
  }

  @Override
  public IndirectCharmRequirement[] getAttributeRequirements() {
    return null;
  }

  public void setParentCharms(Charm[] parentcharms) {
    Collections.addAll(this.parents, parentcharms);
  }

  @Override
  public Set<Charm> getParentCharms() {
    return parents;
  }

  @Override
  public IExaltedSourceBook[] getSources() {
    return null;
  }

  @Override
  public IExaltedSourceBook getPrimarySource() {
    return null;
  }

  @Override
  public ICostList getTemporaryCost() {
    return null;
  }

  @Override
  public String getId() {
    return null;
  }

  @Override
  public ICharmTypeModel getCharmTypeModel() {
    return null;
  }

  @Override
  public MagicAttribute[] getAttributes() {
    return new MagicAttribute[0];
  }

  @Override
  public TraitType getPrimaryTraitType() {
    return AbilityType.Archery;
  }
}