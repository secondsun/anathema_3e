package net.sf.anathema.campaign.music.impl.view;

import net.sf.anathema.framework.presenter.view.IInitializableContentView;

import javax.swing.JComponent;

public class SimpleTabViewFactory {
  public IInitializableContentView<?> createTabView(final JComponent component) {
    return new IInitializableContentView<Object>() {
      @Override
      public JComponent getComponent() {
        return component;
      }

      @Override
      public void initGui(Object properties) {
        // Nothing to do
      }
    };
  }
}