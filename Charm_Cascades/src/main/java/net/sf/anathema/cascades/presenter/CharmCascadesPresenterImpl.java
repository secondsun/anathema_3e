package net.sf.anathema.cascades.presenter;

import net.sf.anathema.framework.environment.Environment;
import net.sf.anathema.framework.environment.ObjectFactory;
import net.sf.anathema.hero.charms.compiler.CharmCache;
import net.sf.anathema.hero.charms.display.coloring.ConfigurableCharmDye;
import net.sf.anathema.hero.charms.display.presenter.CharmDisplayPropertiesMap;
import net.sf.anathema.hero.charms.display.tree.CascadePresenter;
import net.sf.anathema.hero.charms.display.view.CharmView;
import net.sf.anathema.hero.framework.HeroEnvironment;
import net.sf.anathema.hero.framework.type.CharacterTypes;
import net.sf.anathema.hero.magic.description.MagicDescriptionProvider;

public class CharmCascadesPresenterImpl {
  private final CascadePresenter cascadePresenter;
  private final CharmCache cache;
  private final ObjectFactory objectFactory;
  private final CharacterTypes characterTypes;
  private final CharmView view;
  private final CharmTreeMap identifierMap;

  public CharmCascadesPresenterImpl(Environment environment, HeroEnvironment heroEnvironment, CharmView view,
                                    MagicDescriptionProvider magicDescriptionProvider,
                                    CharmTreeMap identifierMap) {
    this.view = view;
    this.identifierMap = identifierMap;
    this.cache = heroEnvironment.getDataSet(CharmCache.class);
    this.cascadePresenter = new CascadePresenter(environment, cache, magicDescriptionProvider);
    this.objectFactory = environment;
    this.characterTypes = heroEnvironment.getCharacterTypes();
  }

  public void initPresentation() {
    CascadeSpecialCharmSet specialCharmSet = new CascadeSpecialCharmSet(cache);
    CharmDisplayPropertiesMap charmDisplayPropertiesMap = new CharmDisplayPropertiesMap(objectFactory);
    CascadeCharmGroupChangeListener selectionListener = new CascadeCharmGroupChangeListener(specialCharmSet, charmDisplayPropertiesMap);
    cascadePresenter.setCharmTreeCollectionMap(identifierMap);
    cascadePresenter.setCategoryCollection(new CascadeCategoryCollection(characterTypes, cache));
    cascadePresenter.setChangeListener(selectionListener);
    cascadePresenter.setView(view);
    cascadePresenter.setCharmDye(new ConfigurableCharmDye(selectionListener, new CascadeColoringStrategy()));
    cascadePresenter.setCharmTrees(new CascadeGroupCollection(cache, characterTypes, identifierMap));
    cascadePresenter.setSpecialCharmSet(specialCharmSet);
    cascadePresenter.initPresentation();
  }
}