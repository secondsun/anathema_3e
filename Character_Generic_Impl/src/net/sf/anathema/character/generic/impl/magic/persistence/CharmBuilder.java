package net.sf.anathema.character.generic.impl.magic.persistence;

import static net.sf.anathema.character.generic.impl.magic.ICharmXMLConstants.ATTRIB_ALL_ABILITIES;
import static net.sf.anathema.character.generic.impl.magic.ICharmXMLConstants.ATTRIB_ATTRIBUTE;
import static net.sf.anathema.character.generic.impl.magic.ICharmXMLConstants.ATTRIB_COMBOABLE;
import static net.sf.anathema.character.generic.impl.magic.ICharmXMLConstants.ATTRIB_EXALT;
import static net.sf.anathema.character.generic.impl.magic.ICharmXMLConstants.ATTRIB_ID;
import static net.sf.anathema.character.generic.impl.magic.ICharmXMLConstants.ATTRIB_PAGE;
import static net.sf.anathema.character.generic.impl.magic.ICharmXMLConstants.ATTRIB_SOURCE;
import static net.sf.anathema.character.generic.impl.magic.ICharmXMLConstants.ATTRIB_TYPE;
import static net.sf.anathema.character.generic.impl.magic.ICharmXMLConstants.ATTRIB_VISUALIZE;
import static net.sf.anathema.character.generic.impl.magic.ICharmXMLConstants.TAG_ATTRIBUTE;
import static net.sf.anathema.character.generic.impl.magic.ICharmXMLConstants.TAG_CHARM;
import static net.sf.anathema.character.generic.impl.magic.ICharmXMLConstants.TAG_CHARMTYPE;
import static net.sf.anathema.character.generic.impl.magic.ICharmXMLConstants.TAG_COMBO;
import static net.sf.anathema.character.generic.impl.magic.ICharmXMLConstants.TAG_COST;
import static net.sf.anathema.character.generic.impl.magic.ICharmXMLConstants.TAG_DURATION;
import static net.sf.anathema.character.generic.impl.magic.ICharmXMLConstants.TAG_PERMANENT;
import static net.sf.anathema.character.generic.impl.magic.ICharmXMLConstants.TAG_PREREQUISITE_LIST;
import static net.sf.anathema.character.generic.impl.magic.ICharmXMLConstants.TAG_RESTRICTIONS;
import static net.sf.anathema.character.generic.impl.magic.ICharmXMLConstants.TAG_SOURCE;
import static net.sf.anathema.character.generic.impl.magic.ICharmXMLConstants.TAG_TEMPORARY;
import static net.sf.anathema.character.generic.impl.magic.ICharmXMLConstants.TAG_TRAIT_REFERENCE;

import java.util.ArrayList;
import java.util.List;

import net.disy.commons.core.util.Ensure;
import net.sf.anathema.character.generic.impl.magic.Charm;
import net.sf.anathema.character.generic.impl.magic.CharmAttribute;
import net.sf.anathema.character.generic.impl.magic.ICharmXMLConstants;
import net.sf.anathema.character.generic.impl.magic.MagicSource;
import net.sf.anathema.character.generic.impl.magic.persistence.builder.CharmTypeBuilder;
import net.sf.anathema.character.generic.impl.magic.persistence.builder.CostListBuilder;
import net.sf.anathema.character.generic.impl.magic.persistence.builder.DurationBuilder;
import net.sf.anathema.character.generic.impl.magic.persistence.builder.GroupStringBuilder;
import net.sf.anathema.character.generic.impl.magic.persistence.builder.ICostListBuilder;
import net.sf.anathema.character.generic.impl.magic.persistence.builder.IIdStringBuilder;
import net.sf.anathema.character.generic.impl.magic.persistence.builder.prerequisite.ITraitPrerequisitesBuilder;
import net.sf.anathema.character.generic.impl.magic.persistence.builder.prerequisite.PrerequisiteListBuilder;
import net.sf.anathema.character.generic.impl.magic.persistence.prerequisite.CharmPrerequisiteList;
import net.sf.anathema.character.generic.impl.traits.TraitTypeUtils;
import net.sf.anathema.character.generic.magic.charms.CharmException;
import net.sf.anathema.character.generic.magic.charms.ComboRestrictions;
import net.sf.anathema.character.generic.magic.charms.Duration;
import net.sf.anathema.character.generic.magic.charms.ICharmAttribute;
import net.sf.anathema.character.generic.magic.charms.IComboRestrictions;
import net.sf.anathema.character.generic.magic.charms.type.CharmType;
import net.sf.anathema.character.generic.magic.charms.type.ICharmTypeModel;
import net.sf.anathema.character.generic.magic.general.ICostList;
import net.sf.anathema.character.generic.magic.general.IMagicSource;
import net.sf.anathema.character.generic.magic.general.IPermanentCostList;
import net.sf.anathema.character.generic.traits.IGenericTrait;
import net.sf.anathema.character.generic.type.CharacterType;
import net.sf.anathema.lib.exception.PersistenceException;
import net.sf.anathema.lib.xml.ElementUtilities;

import org.dom4j.Element;

public class CharmBuilder implements ICharmBuilder {

  private final CharmTypeBuilder charmTypeBuilder = new CharmTypeBuilder();
  private final ICostListBuilder costListBuilder = new CostListBuilder();
  private final DurationBuilder durationBuilder = new DurationBuilder();
  private final TraitTypeUtils traitUtils = new TraitTypeUtils();
  private final GroupStringBuilder groupBuilder = new GroupStringBuilder();
  private final IIdStringBuilder idBuilder;
  private final ITraitPrerequisitesBuilder traitsBuilder;

  public CharmBuilder(IIdStringBuilder idBuilder, ITraitPrerequisitesBuilder traitsBuilder) {
    this.idBuilder = idBuilder;
    this.traitsBuilder = traitsBuilder;
  }

  public Charm buildCharm(Element charmElement) throws PersistenceException {
    Element rulesElement = charmElement;
    String id = idBuilder.build(charmElement);
    String typeAttribute = charmElement.attributeValue(ATTRIB_EXALT);
    CharacterType characterType;
    try {
      characterType = CharacterType.getById(typeAttribute);
    }
    catch (IllegalArgumentException e) {
      throw new CharmException("No chararacter type given for Charm: " + id, e); //$NON-NLS-1$
    }
    Element costElement = rulesElement.element(TAG_COST);
    Ensure.ensureArgumentNotNull("No cost specified for Charm: " + id, costElement); //$NON-NLS-1$
    ICostList temporaryCost = costListBuilder.buildTemporaryCostList(costElement.element(TAG_TEMPORARY));
    IPermanentCostList permanentCost = costListBuilder.buildPermanentCostList(costElement.element(TAG_PERMANENT));
    IComboRestrictions comboRules = getComboRules(rulesElement, id);
    Duration duration = durationBuilder.buildDuration(rulesElement.element(TAG_DURATION));
    ICharmTypeModel charmTypeModel = charmTypeBuilder.build(rulesElement);
    List<IMagicSource> sources = new ArrayList<IMagicSource>();
    List<Element> sourceElements = ElementUtilities.elements(rulesElement, TAG_SOURCE);
    if (sourceElements.isEmpty()) {
      sources.add(MagicSource.CUSTOM_SOURCE);
    }
    else {
      for (Element sourceElement : sourceElements) {
        String source = sourceElement.attributeValue(ATTRIB_SOURCE);
        String page = String.valueOf(sourceElement.attributeValue(ATTRIB_PAGE));
        sources.add(new MagicSource(source, page));
      }
    }

    Element prerequisiteListElement = ElementUtilities.getRequiredElement(charmElement, TAG_PREREQUISITE_LIST);
    CharmPrerequisiteList prerequisiteList;
    try {
      prerequisiteList = new PrerequisiteListBuilder(traitsBuilder).buildPrerequisiteList(prerequisiteListElement);
    }
    catch (PersistenceException e) {
      throw new CharmException("Error in Charm " + id, e); //$NON-NLS-1$
    }
    IGenericTrait[] prerequisites = prerequisiteList.getPrerequisites();
    final IGenericTrait primaryPrerequisite = (prerequisites.length != 0) ? prerequisites[0] : null;
    String group = groupBuilder.build(charmElement, primaryPrerequisite);
    Charm charm = new Charm(
        characterType,
        id,
        group,
        prerequisiteList,
        temporaryCost,
        permanentCost,
        comboRules,
        duration,
        charmTypeModel,
        sources.toArray(new IMagicSource[0]));
    for (ICharmAttribute attribute : getCharmAttributes(rulesElement, primaryPrerequisite)) {
      charm.addCharmAttribute(attribute);
    }
    loadSpecialLearning(charmElement, charm);
    return charm;
  }

  private ICharmAttribute[] getCharmAttributes(Element rulesElement, IGenericTrait primaryPrerequisite) {
    List<ICharmAttribute> attributes = new ArrayList<ICharmAttribute>();
    for (Element attributeElement : getElementsFromRules(rulesElement, TAG_ATTRIBUTE)) {
      String attributeId = attributeElement.attributeValue(ATTRIB_ATTRIBUTE);
      boolean visualizeAttribute = ElementUtilities.getBooleanAttribute(attributeElement, ATTRIB_VISUALIZE, false);
      attributes.add(new CharmAttribute(attributeId, visualizeAttribute));
    }
    if (primaryPrerequisite != null) {
      attributes.add(new CharmAttribute(primaryPrerequisite.getType().getId(), false));
    }
    return attributes.toArray(new ICharmAttribute[attributes.size()]);
  }

  private IComboRestrictions getComboRules(Element rulesElement, String id) throws CharmException {
    String typeAttribute;
    Element comboElement = rulesElement.element(TAG_COMBO);
    if (comboElement == null) {
      return new ComboRestrictions();
    }
    Boolean allAbilities = Boolean.valueOf(comboElement.attributeValue(ATTRIB_ALL_ABILITIES));
    String comboAllowedValue = comboElement.attributeValue(ATTRIB_COMBOABLE);
    Boolean comboAllowed = comboAllowedValue == null ? new Boolean(true) : Boolean.valueOf(comboAllowedValue);
    Element restrictionElement = comboElement.element(TAG_RESTRICTIONS);
    ComboRestrictions comboRules = new ComboRestrictions(allAbilities, comboAllowed);
    if (restrictionElement != null) {
      List<Element> restrictedCharmList = ElementUtilities.elements(restrictionElement, TAG_CHARM);
      for (int i = 0; i < restrictedCharmList.size(); i++) {
        comboRules.addRestrictedCharmId(restrictedCharmList.get(i).attributeValue(ATTRIB_ID));
      }
      List<Element> restrictedCharmTypeList = ElementUtilities.elements(restrictionElement, TAG_CHARMTYPE);
      for (int i = 0; i < restrictedCharmTypeList.size(); i++) {
        try {
          typeAttribute = restrictedCharmTypeList.get(i).attributeValue(ATTRIB_TYPE);
          comboRules.addRestrictedCharmType(CharmType.valueOf(typeAttribute));
        }
        catch (IllegalArgumentException e) {
          throw new CharmException("Bad charm type in Combo restrictions for Charm: " + id, e); //$NON-NLS-1$
        }
      }
      List<Element> restrictedTraitTypeList = ElementUtilities.elements(restrictionElement, TAG_TRAIT_REFERENCE);
      for (int i = 0; i < restrictedTraitTypeList.size(); i++) {
        try {
          typeAttribute = restrictedTraitTypeList.get(i).attributeValue(ATTRIB_ID);
          comboRules.addRestrictedTraitType(traitUtils.getTraitTypeById(typeAttribute));
        }
        catch (IllegalArgumentException e) {
          throw new CharmException("Bad trait type in Combo restrictions for Charm: " + id); //$NON-NLS-1$
        }
      }
    }
    return comboRules;
  }

  private Element[] getElementsFromRules(Element rulesElement, String elementName) {
    List<Element> elements = ElementUtilities.elements(rulesElement, elementName);
    return elements.toArray(new Element[elements.size()]);
  }

  private void loadSpecialLearning(Element charmElement, Charm charm) {
    Element learningElement = charmElement.element(ICharmXMLConstants.TAG_LEARNING);
    if (learningElement == null) {
      return;
    }
    for (Element favoredElement : ElementUtilities.elements(learningElement, ICharmXMLConstants.ATTRB_FAVORED)) {
      String casteId = favoredElement.attributeValue(ICharmXMLConstants.TAG_CASTE);
      charm.addFavoredCasteId(casteId);
    }
  }
}