package net.sf.anathema.framework.repository.tree;

import net.sf.anathema.framework.item.IItemType;
import net.sf.anathema.framework.repository.access.RepositoryWriteAccess;
import net.sf.anathema.framework.view.PrintNameFile;
import net.sf.anathema.library.event.ChangeListener;

import java.util.Collection;

public interface IRepositoryTreeModel extends ExportModel {
  IItemType[] getAllItemTypes();

  IItemType getItemTypeForId(String id);

  Collection<PrintNameFile> getPrintNameFiles(IItemType itemType);

  void addRepositoryTreeModelListener(IRepositoryTreeModelListener listener);

  void deleteSelection();

  boolean canSelectionBeDeleted();

  String getRepositoryPath();

  void setSelectedObject(Object[] object);

  void addTreeSelectionChangeListener(ChangeListener changeListener);

  String createUniqueId(IItemType type, String id);

  RepositoryWriteAccess getWriteAccess(IItemType type, String id);

  String getMainFilePath(IItemType type, String id);

  void refreshItem(IItemType type, String id);
}