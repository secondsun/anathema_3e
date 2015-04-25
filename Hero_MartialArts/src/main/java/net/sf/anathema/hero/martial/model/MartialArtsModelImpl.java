package net.sf.anathema.hero.martial.model;

import net.sf.anathema.characterengine.support.Announcer;
import net.sf.anathema.hero.charms.model.CharmTree;
import net.sf.anathema.hero.charms.model.CharmsModel;
import net.sf.anathema.hero.charms.model.CharmsModelFetcher;
import net.sf.anathema.hero.environment.HeroEnvironment;
import net.sf.anathema.hero.individual.change.ChangeAnnouncer;
import net.sf.anathema.hero.individual.change.ChangeFlavor;
import net.sf.anathema.hero.individual.model.Hero;
import net.sf.anathema.hero.individual.model.HeroModel;
import net.sf.anathema.hero.merits.model.MechanicalDetailReference;
import net.sf.anathema.hero.merits.model.Merit;
import net.sf.anathema.hero.merits.model.MeritsModel;
import net.sf.anathema.hero.merits.model.MeritsModelFetcher;
import net.sf.anathema.hero.traits.model.Trait;
import net.sf.anathema.hero.traits.model.TraitImpl;
import net.sf.anathema.hero.traits.model.TraitModel;
import net.sf.anathema.hero.traits.model.TraitModelFetcher;
import net.sf.anathema.hero.traits.model.TraitType;
import net.sf.anathema.hero.traits.model.event.TraitValueChangedListener;
import net.sf.anathema.hero.traits.model.rules.TraitRulesImpl;
import net.sf.anathema.hero.traits.template.TraitTemplate;
import net.sf.anathema.library.event.ChangeListener;
import net.sf.anathema.library.identifier.Identifier;
import net.sf.anathema.library.model.RemovableEntryListener;
import net.sf.anathema.magic.data.reference.CategoryReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public class MartialArtsModelImpl implements MartialArtsModel {
  private final List<Trait> availableStyles = new ArrayList<>();
  private final List<Trait> learnedStyles = new ArrayList<>();
  private final Announcer<ChangeListener> selectionAnnouncer = Announcer.to(ChangeListener.class);
  private final Announcer<StyleLearnListener> learnAnnouncer = Announcer.to(StyleLearnListener.class);
  private final Announcer<StyleForgetListener> forgetAnnouncer = Announcer.to(StyleForgetListener.class);
  private final Announcer<ChangeListener> martialArtistAnnouncer = Announcer.to(ChangeListener.class);
  private final Announcer<ChangeListener> noMartialArtistAnnouncer = Announcer.to(ChangeListener.class);
  private Trait selectedStyle;
  private TraitImpl martialArts;

  @Override
  public Identifier getId() {
    return MartialArtsModel.ID;
  }

  @Override
  public void initialize(HeroEnvironment environment, Hero hero) {
    listenForMartialArtsMerits(hero);
    extractAvailableStyles(hero);
    TraitModel traitModel = TraitModelFetcher.fetch(hero);
    martialArts = new TraitImpl(hero, new TraitRulesImpl(new TraitType("MartialArts"), new TraitTemplate(), hero));
    traitModel.addTraits(martialArts);
    for (Trait availableStyle : availableStyles) {
      availableStyle.addCurrentValueListener(newValue -> updateMartialArtsRating());
    }
    selectStyle(availableStyles.get(0));
  }

  private void listenForMartialArtsMerits(Hero hero) {
    MeritsModel meritsModel = MeritsModelFetcher.fetch(hero);
    meritsModel.addModelChangeListener(new RemovableEntryListener<Merit>() {
      @Override
      public void entryAdded(Merit entry) {
        if (entry.hasMechanicalDetail(new MechanicalDetailReference("GrantsMartialArts"))) {
          martialArtistAnnouncer.announce().changeOccurred();
        }
      }

      @Override
      public void entryRemoved(Merit entry) {
        boolean isAMartialArtist = meritsModel.getEntries().stream().anyMatch(merit -> merit.hasMechanicalDetail(new MechanicalDetailReference("GrantsMartialArts")));
        if (!isAMartialArtist) {
          noMartialArtistAnnouncer.announce().changeOccurred();
        }
      }

      @Override
      public void entryAllowed(boolean complete) {
        //nothing to do
      }
    });
  }

  private void updateMartialArtsRating() {
    int maximumStyleRating = 0;
    for (Trait style : learnedStyles) {
      maximumStyleRating = Math.max(maximumStyleRating, style.getCurrentValue());
    }
    martialArts.setCurrentValue(maximumStyleRating);
  }

  private void extractAvailableStyles(Hero hero) {
    TraitModel traitModel = TraitModelFetcher.fetch(hero);
    CharmsModel charmsModel = CharmsModelFetcher.fetch(hero);
    Collection<CharmTree> martialArtsTrees = charmsModel.getTreesFor(new CategoryReference("MartialArts"));
    for (CharmTree charmTree : martialArtsTrees) {
      String treeId = charmTree.getReference().name.text;
      TraitImpl style = new TraitImpl(hero, new TraitRulesImpl(new TraitType(treeId), new TraitTemplate(), hero));
      availableStyles.add(style);
      traitModel.addTraits(style);
    }
  }

  @Override
  public void initializeListening(ChangeAnnouncer announcer) {
    learnAnnouncer.addListener(style -> announcer.announceChangeFlavor(ChangeFlavor.UNSPECIFIED));
    forgetAnnouncer.addListener(style -> announcer.announceChangeFlavor(ChangeFlavor.UNSPECIFIED));
    for (Trait style : availableStyles) {
      style.addCurrentValueListener(new TraitValueChangedListener(announcer, style));
    }
  }

  @Override
  public List<Trait> getAvailableStyles() {
    return availableStyles;
  }

  @Override
  public List<Trait> getLearnedStyles() {
    return learnedStyles;
  }

  @Override
  public void selectStyle(Trait newValue) {
    if (newValue == selectedStyle) {
      return;
    }
    this.selectedStyle = newValue;
    selectionAnnouncer.announce().changeOccurred();
  }

  @Override
  public void selectStyle(StyleName styleName) {
    for (Trait candidate : availableStyles) {
      if (candidate.getType().getId().equals(styleName.name)) {
        selectStyle(candidate);
        return;
      }
    }
  }


  @Override
  public Trait getSelectedStyle() {
    return selectedStyle;
  }

  @Override
  public void learnSelectedStyle() {
    learnedStyles.add(selectedStyle);
    availableStyles.remove(selectedStyle);
    learnAnnouncer.announce().styleLearned(selectedStyle);
    selectStyle(availableStyles.get(0));
  }

  @Override
  public void forget(Trait style) {
    style.setCurrentValue(0);
    learnedStyles.remove(style);
    availableStyles.add(style);
    forgetAnnouncer.announce().styleForgotten(style);
    selectStyle(availableStyles.get(0));
  }

  @Override
  public void whenStyleIsSelected(ChangeListener listener) {
    selectionAnnouncer.addListener(listener);
  }

  @Override
  public void whenStyleIsLearned(StyleLearnListener listener) {
    learnAnnouncer.addListener(listener);
  }

  @Override
  public void whenStyleIsForgotten(StyleForgetListener listener) {
    forgetAnnouncer.addListener(listener);
  }

  @Override
  public void whenCharacterBecomesAMartialArtist(ChangeListener listener) {
    martialArtistAnnouncer.addListener(listener);
  }

  @Override
  public void whenCharacterNoLongerIsAMartialArtist(ChangeListener listener) {
    noMartialArtistAnnouncer.addListener(listener);
  }
}