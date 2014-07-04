package net.sf.anathema.hero.intimacies.display;

import net.sf.anathema.interaction.Tool;
import net.sf.anathema.lib.gui.selection.ObjectSelectionView;
import net.sf.anathema.library.event.ObjectValueListener;

public interface IntimacyEntryView {

  void addTextChangeListener(ObjectValueListener<String> listener);

  Tool addTool();

  void clear();

  <T> ObjectSelectionView<T> addSelection();
}