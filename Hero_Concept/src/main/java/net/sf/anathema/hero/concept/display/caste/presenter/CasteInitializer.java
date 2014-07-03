package net.sf.anathema.hero.concept.display.caste.presenter;

import net.sf.anathema.framework.IApplicationModel;
import net.sf.anathema.framework.environment.Environment;
import net.sf.anathema.framework.environment.dependencies.Weight;
import net.sf.anathema.hero.concept.HeroConcept;
import net.sf.anathema.hero.concept.HeroConceptFetcher;
import net.sf.anathema.hero.display.presenter.HeroModelInitializer;
import net.sf.anathema.hero.display.presenter.RegisteredInitializer;
import net.sf.anathema.hero.framework.display.SectionView;
import net.sf.anathema.hero.model.Hero;

import static net.sf.anathema.hero.display.HeroModelGroup.Outline;

@RegisteredInitializer(Outline)
@Weight(weight = 100)
public class CasteInitializer implements HeroModelInitializer {

  @SuppressWarnings("UnusedParameters")
  public CasteInitializer(IApplicationModel applicationModel) {
    //nothing to do
  }

  @Override
  public void initialize(SectionView sectionView, Hero hero, Environment environment) {
    String conceptHeader = environment.getString("CardView.CharacterConcept.Title");
    CasteView conceptView = sectionView.addView(conceptHeader, CasteView.class);
    new CastePresenter(hero, conceptView, environment).initPresentation();
  }

  @Override
  public boolean canWorkForHero(Hero hero) {
    HeroConcept concept = HeroConceptFetcher.fetch(hero);
    return concept != null && !concept.getCasteCollection().isEmpty();
  }
}