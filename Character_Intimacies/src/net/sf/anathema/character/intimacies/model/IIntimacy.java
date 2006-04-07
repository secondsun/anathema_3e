package net.sf.anathema.character.intimacies.model;

import net.sf.anathema.character.library.trait.ITrait;
import net.sf.anathema.lib.control.booleanvalue.IBooleanValueChangedListener;

public interface IIntimacy {

  public String getName();

  public ITrait getTrait();

  public void resetCurrentValue();

  public void setComplete(boolean complete);

  public void addCompletionListener(IBooleanValueChangedListener listener);
}