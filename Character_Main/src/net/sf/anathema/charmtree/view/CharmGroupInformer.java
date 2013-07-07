package net.sf.anathema.charmtree.view;

import net.sf.anathema.character.main.magic.charms.ICharmGroup;

public interface CharmGroupInformer {
  boolean hasGroupSelected();

  ICharmGroup getCurrentGroup();
}