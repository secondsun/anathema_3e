package net.sf.anathema.hero.charms.model;

import net.sf.anathema.lib.compare.WeightedObject;
import net.sf.anathema.lib.compare.WeightedObjectSorter;
import net.sf.anathema.magic.data.Magic;

import java.util.Collection;

public class WeightedMagicSorter extends WeightedObjectSorter<Magic> {

  @Override
  public WeightedObject<Magic> createWeightedObject(Magic magic, int weight) {
    return new WeightedMagic(magic, weight);
  }

  @Override
  public WeightedObject<Magic>[] convertToArray(Collection<WeightedObject<Magic>> collection) {
    return collection.toArray(new WeightedMagic[collection.size()]);
  }
}