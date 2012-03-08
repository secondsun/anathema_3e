package net.sf.anathema.character.presenter.charm;

import com.google.common.collect.Lists;

import net.sf.anathema.character.generic.template.magic.IUniqueCharmType;
import net.sf.anathema.character.generic.type.ICharacterType;
import net.sf.anathema.lib.util.IIdentificate;

import java.util.ArrayList;
import java.util.List;

public class CharacterCharmTypes extends AbstractCharmTypes {

  private CharacterCharmModel model;

  public CharacterCharmTypes(CharacterCharmModel model) {
    this.model = model;
  }

  @Override
  protected List<IIdentificate> getCurrentCharacterTypes() {
    boolean alienCharms = model.isAllowedAlienCharms();
    ICharacterType[] characterTypes = model.getCharmConfiguration().getCharacterTypes(alienCharms);
    return Lists.<IIdentificate>newArrayList(characterTypes);
  }

	@Override
	protected List<IIdentificate> getAdditionalCharmTypes() {
		IUniqueCharmType uniqueType = model.getUniqueCharmType();
		List<IIdentificate> typeIds = new ArrayList<IIdentificate>();
		if (uniqueType != null)
			typeIds.add(uniqueType.getId());
		return typeIds;
	} 
}