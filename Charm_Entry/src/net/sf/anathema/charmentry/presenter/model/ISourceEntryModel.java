package net.sf.anathema.charmentry.presenter.model;

import net.sf.anathema.lib.util.IIdentificate;

public interface ISourceEntryModel {
  public IIdentificate[] getPredefinedSources();

  public void setSourceBook(IIdentificate identificate);

  public boolean enablePageSelection();

  public void setSourcePage(int newValue);
}
