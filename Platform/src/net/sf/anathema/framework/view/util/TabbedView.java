package net.sf.anathema.framework.view.util;

import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import net.infonode.tabbedpanel.TabDropDownListVisiblePolicy;
import net.infonode.tabbedpanel.TabbedPanel;
import net.infonode.tabbedpanel.TabbedPanelProperties;
import net.infonode.tabbedpanel.titledtab.TitledTab;
import net.sf.anathema.framework.presenter.view.ISimpleTabView;
import net.sf.anathema.lib.gui.IView;
import net.sf.anathema.lib.gui.widgets.RevalidatingScrollPane;

public class TabbedView {

  private static void initTabbedPaneProperties(TabbedPanelProperties paneProperties, TabDirection tabDirection) {
    paneProperties.removeSuperObject(paneProperties);
    paneProperties.setTabAreaOrientation(tabDirection.getDirection());
    paneProperties.setTabReorderEnabled(false);
    paneProperties.setTabDeselectable(false);
    paneProperties.setEnsureSelectedTabVisible(true);
    paneProperties.setHighlightPressedTab(true);
    paneProperties.setTabDropDownListVisiblePolicy(TabDropDownListVisiblePolicy.NEVER);
    paneProperties.getTabAreaComponentsProperties().setStretchEnabled(true);
  }

  private final TabbedPanel tabbedPane = new TabbedPanel();

  public TabbedView() {
    this(TabDirection.Left);
  }

  public TabbedView(TabDirection tabDirection) {
    initTabbedPaneProperties(tabbedPane.getProperties(), tabDirection);
  }

  public final void addTab(ISimpleTabView tabView, final String name) {
    addTab(tabView, new TabProperties(name).needsScrollbar());
  }

  public void addTab(IView content, TabProperties properties) {
    TitledTab tab;
    JComponent tabContent = content.getComponent();
    if (properties.isNeedsScrollbar()) {
      JPanel viewComponent = new JPanel(new FlowLayout(FlowLayout.LEFT));
      viewComponent.add(tabContent);
      tabContent = new RevalidatingScrollPane(viewComponent);
    }
    tab = new TitledTab(properties.getName(), null, tabContent, null);
    tabbedPane.addTab(tab);
  }

  public final JComponent getComponent() {
    return tabbedPane;
  }

  public final void setTabAreaComponents(JComponent[] components) {
    tabbedPane.setTabAreaComponents(components);
  }
}