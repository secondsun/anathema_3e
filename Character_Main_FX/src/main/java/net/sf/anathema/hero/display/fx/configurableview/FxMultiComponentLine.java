package net.sf.anathema.hero.display.fx.configurableview;

import javafx.scene.Node;
import javafx.scene.control.Label;
import net.sf.anathema.library.fx.text.FxTextView;
import net.sf.anathema.library.model.IntegerModel;
import net.sf.anathema.library.text.ITextView;
import net.sf.anathema.library.view.IntegerView;
import net.sf.anathema.library.view.MultiComponentLine;
import org.tbee.javafx.scene.layout.MigPane;

public class FxMultiComponentLine implements MultiComponentLine {
  private final MigPane fieldPanel = new MigPane();

  @Override
  public ITextView addFieldsView(String labelText) {
    FxTextView view = FxTextView.SingleLine(labelText);
    addLabeledComponent("", view.getNode());
    return view;
  }

  @Override
  public IntegerView addIntegerView(String labelText, IntegerModel description) {
    IntegerSpinner spinner = new IntegerSpinner();
    spinner.setValue(description.getValue());
    addLabeledComponent(labelText, spinner.getNode());
    return spinner;
  }

  private void addLabeledComponent(String text, final Node component) {
    Label label = new Label(text);
    fieldPanel.add(label);
    fieldPanel.add(component);
  }

  public Node getNode() {
    return fieldPanel;
  }
}