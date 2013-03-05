package net.sf.anathema.character.impl.model.traits.creation;

import net.sf.anathema.character.generic.caste.ICasteCollection;
import net.sf.anathema.character.generic.caste.ICasteType;
import net.sf.anathema.character.generic.template.abilities.IGroupedTraitType;
import net.sf.anathema.character.generic.traits.ITraitType;
import net.sf.anathema.character.generic.traits.groups.IIdentifiedCasteTraitTypeGroup;
import net.sf.anathema.character.generic.traits.groups.IdentifiedCasteTraitTypeGroup;
import net.sf.anathema.lib.collection.MultiEntryMap;
import net.sf.anathema.lib.util.Identified;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTraitTypeGroupFactory {

  protected abstract Identified getGroupIdentifier(ICasteCollection casteCollection, String groupId);

  public IIdentifiedCasteTraitTypeGroup[] createTraitGroups(ICasteCollection casteCollection, IGroupedTraitType[] traitTypes) {
    List<String> groupIds = new ArrayList<>();
    MultiEntryMap<String, ITraitType> traitTypesByGroupId = new MultiEntryMap<>();
    for (IGroupedTraitType type : traitTypes) {
      String groupId = type.getGroupId();
      if (!groupIds.contains(groupId)) {
        groupIds.add(groupId);
      }
      traitTypesByGroupId.add(type.getGroupId(), type.getTraitType());
    }
    IIdentifiedCasteTraitTypeGroup[] groups = new IIdentifiedCasteTraitTypeGroup[groupIds.size()];
    for (int groupIndex = 0; groupIndex < groups.length; groupIndex++) {
      String groupId = groupIds.get(groupIndex);
      List<ITraitType> groupTraitTypes = traitTypesByGroupId.get(groupId);
      List<List<ICasteType>> traitCasteSet = createTraitCasteSet(groupId, traitTypes, casteCollection);
      groups[groupIndex] = createTraitGroup(casteCollection, groupId, groupTraitTypes, traitCasteSet);
    }
    return groups;
  }

  private List<List<ICasteType>> createTraitCasteSet(String groupId, IGroupedTraitType[] traitTypes, ICasteCollection casteCollection) {
    List<List<ICasteType>> allTypeList = new ArrayList<>();
    for (IGroupedTraitType type : traitTypes) {
      if (!type.getGroupId().equals(groupId)) {
        continue;
      }
      List<ICasteType> currentTypeList = new ArrayList<>();
      allTypeList.add(currentTypeList);
      if (type.getTraitCasteSet() != null) {
        for (int subIndex = 0; subIndex != type.getTraitCasteSet().size(); subIndex++) {
          String casteTypeId = type.getTraitCasteSet().get(subIndex);
          ICasteType casteType = casteCollection.getById(casteTypeId);
          currentTypeList.add(casteType);
        }
      } else if (type.getGroupCasteId() != null) {
        ICasteType casteType = casteCollection.getById(type.getGroupCasteId());
        currentTypeList.add(casteType);
      }
    }
    return allTypeList;
  }

  private IIdentifiedCasteTraitTypeGroup createTraitGroup(ICasteCollection casteCollection, String groupId, List<ITraitType> traitTypes,
                                                          List<List<ICasteType>> traitCasteTypes) {
    Identified groupIdentifier = getGroupIdentifier(casteCollection, groupId);
    ITraitType[] types = traitTypes.toArray(new ITraitType[traitTypes.size()]);
    return new IdentifiedCasteTraitTypeGroup(types, groupIdentifier, traitCasteTypes);
  }
}