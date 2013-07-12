package net.sf.anathema.character.main.template.experience;

import net.sf.anathema.hero.magic.model.martial.MartialArtsLevel;
import net.sf.anathema.character.main.magic.model.magic.Magic;

public interface CostAnalyzer {

  MartialArtsLevel getMartialArtsLevel(Magic magic);

  boolean isMagicFavored(Magic magic);

}