package net.sf.anathema.equipment.editor.perspective;

import net.sf.anathema.character.equipment.item.EquipmentDatabasePresenter;
import net.sf.anathema.character.equipment.item.model.EquipmentDatabaseManagement;
import net.sf.anathema.character.equipment.item.model.IEquipmentDatabase;
import net.sf.anathema.character.equipment.item.model.IEquipmentDatabaseManagement;
import net.sf.anathema.character.equipment.item.model.gson.GsonEquipmentDatabase;
import net.sf.anathema.character.equipment.item.view.fx.FxEquipmentDatabaseView;
import net.sf.anathema.framework.IApplicationModel;
import net.sf.anathema.framework.environment.fx.UiEnvironment;
import net.sf.anathema.framework.view.perspective.Container;
import net.sf.anathema.framework.view.perspective.Perspective;
import net.sf.anathema.framework.view.perspective.PerspectiveAutoCollector;
import net.sf.anathema.framework.view.perspective.PerspectiveToggle;
import net.sf.anathema.lib.file.RelativePath;
import net.sf.anathema.library.initialization.Weight;
import net.sf.anathema.library.resources.Resources;
import net.sf.anathema.platform.environment.Environment;

@PerspectiveAutoCollector
@Weight(weight = 5000)
public class EquipmentPerspective implements Perspective {

  @Override
  public void configureToggle(PerspectiveToggle toggle) {
    toggle.setIcon(new RelativePath("icons/EquipmentPerspective.png"));
    toggle.setTooltip("EquipmentDatabase.Perspective.Name");
  }

  @Override
  public void initContent(Container container, IApplicationModel applicationModel, Environment environment, UiEnvironment uiEnvironment) {
    IEquipmentDatabaseManagement databaseManagement = createDatabaseManagement(applicationModel);
    initInFx(container, environment, databaseManagement,uiEnvironment);
  }

  private void initInFx(Container container, Resources resources, IEquipmentDatabaseManagement databaseManagement, UiEnvironment uiEnvironment) {
    FxEquipmentDatabaseView view = new FxEquipmentDatabaseView(uiEnvironment);
    new EquipmentDatabasePresenter(resources, databaseManagement, view.view).initPresentation();
    container.setContent(view.perspectivePane.getNode());
  }

  private IEquipmentDatabaseManagement createDatabaseManagement(IApplicationModel model) {
    IEquipmentDatabase database = GsonEquipmentDatabase.CreateFrom(model);
    return new EquipmentDatabaseManagement(database);
  }
}
