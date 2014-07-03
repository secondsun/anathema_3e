package net.sf.anathema.hero.charms.advance.creation;

import net.sf.anathema.hero.charms.advance.costs.CostAnalyzer;
import net.sf.anathema.hero.charms.model.WeightedMagicSorter;
import net.sf.anathema.magic.data.Magic;
import net.sf.anathema.points.model.BonusPointCalculator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MagicCreationCostCalculator implements BonusPointCalculator {

  private final MagicCreationCostEvaluator magicCreationCostEvaluator;
  private final MagicCreationData creationData;
  private final CostAnalyzer analyzer;
  private int generalPicksSpent = 0;
  private int favoredPicksSpent = 0;
  protected int bonusPointsSpent = 0;

  public MagicCreationCostCalculator(MagicCreationCostEvaluator costEvaluator, MagicCreationData creationData,
                                     CostAnalyzer analyzer) {
    this.magicCreationCostEvaluator = costEvaluator;
    this.creationData = creationData;
    this.analyzer = analyzer;
  }

  public void calculateMagicCosts() {
    recalculate();
  }

  private void handleMagic(Magic magic, Set<Magic> handledMagic) {
    int bonusPointFactor = getMagicCosts(magic);
    boolean favored = analyzer.isMagicFavored(magic);
    int learnCount = magicCreationCostEvaluator.getLearnCount(magic, handledMagic);
    for (int timesHandled = 0; timesHandled < learnCount; timesHandled++) {
      if (favored) {
        handleFavoredMagic(bonusPointFactor);
      } else {
        handleGeneralMagic(bonusPointFactor);
      }
    }
    bonusPointsSpent += magicCreationCostEvaluator.getAdditionalBonusPoints(magic);
    handledMagic.add(magic);
  }

  private void handleFavoredMagic(int bonusPointFactor) {
    if (favoredPicksSpent < creationData.getFavoredMagicPicks()) {
      favoredPicksSpent++;
    } else {
      handleGeneralMagic(bonusPointFactor);
    }
  }

  private void handleGeneralMagic(final int bonusPointFactor) {
    if (generalPicksSpent < creationData.getGeneralMagicPicks()) {
      generalPicksSpent++;
    } else {
      bonusPointsSpent += bonusPointFactor;
    }
  }

  @Override
  public void recalculate() {
    clear();
    List<Magic> magicToHandle = magicCreationCostEvaluator.compileCompleteMagicList();
    if (magicToHandle == null || magicToHandle.size() == 0) {
      return;
    }
    int[] weights = new int[magicToHandle.size()];
    for (int index = 0; index < weights.length; index++) {
      weights[index] = getMagicCosts(magicToHandle.get(index));
    }
    List<Magic> sortedMagicList = new WeightedMagicSorter().sortDescending(magicToHandle.toArray(new Magic[magicToHandle.size()]), weights);
    Set<Magic> handledMagic = new HashSet<>();
    for (Magic magic : sortedMagicList) {
      handleMagic(magic, handledMagic);
    }
  }

  @Override
  public int getBonusPointCost() {
    return bonusPointsSpent;
  }

  @Override
  public int getBonusPointsGranted() {
    return 0;
  }

  public int getFavoredCharmPicksSpent() {
    return favoredPicksSpent;
  }

  public int getGeneralCharmPicksSpent() {
    return generalPicksSpent;
  }

  private int getMagicCosts(Magic magic) {
    return creationData.getMagicCosts(magic, analyzer);
  }

  private void clear() {
    generalPicksSpent = 0;
    favoredPicksSpent = 0;
    bonusPointsSpent = 0;
  }
}