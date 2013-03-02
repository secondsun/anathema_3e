package net.sf.anathema.character.equipment.character.view;

import net.sf.anathema.lib.gui.IView;
import net.sf.anathema.lib.gui.selection.IListObjectSelectionView;

import javax.swing.Action;

public interface IEquipmentAdditionalView extends IView {

  IListObjectSelectionView<String> getEquipmentTemplatePickList();

  IEquipmentObjectView addEquipmentObjectView();

  void removeEquipmentObjectView(IEquipmentObjectView objectView);

  void setSelectButtonAction(Action action);

  void setRefreshButtonAction(Action action);

  IMagicalMaterialView getMagicMaterialView();

  void revalidateEquipmentViews();
}