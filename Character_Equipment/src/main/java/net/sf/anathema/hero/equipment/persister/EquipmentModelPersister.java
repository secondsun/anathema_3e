package net.sf.anathema.hero.equipment.persister;

import net.sf.anathema.character.equipment.character.EquipmentHeroEvaluator;
import net.sf.anathema.character.equipment.character.model.IEquipmentItem;
import net.sf.anathema.character.equipment.character.model.IEquipmentStatsOption;
import net.sf.anathema.equipment.core.MagicalMaterial;
import net.sf.anathema.hero.equipment.EquipmentModel;
import net.sf.anathema.hero.equipment.sheet.content.stats.weapon.IEquipmentStats;
import net.sf.anathema.hero.model.Hero;
import net.sf.anathema.hero.persistence.AbstractModelJsonPersister;
import net.sf.anathema.lib.logging.Logger;
import net.sf.anathema.lib.message.Message;
import net.sf.anathema.lib.util.Identifier;

import java.text.MessageFormat;

import static net.sf.anathema.lib.message.MessageDuration.Permanent;
import static net.sf.anathema.lib.message.MessageType.WARNING;

@SuppressWarnings("UnusedDeclaration")
public class EquipmentModelPersister extends AbstractModelJsonPersister<EquipmentListPto, EquipmentModel> {

  private final Logger logger = Logger.getLogger(EquipmentModelPersister.class);

  public EquipmentModelPersister() {
    super("equipment", EquipmentListPto.class);
  }

  @Override
  protected void loadModelFromPto(Hero hero, EquipmentModel model, EquipmentListPto pto) {
    for (EquipmentPto equipment : pto.equipments) {
      fillEquipment(model, equipment);
    }
  }

  private void fillEquipment(EquipmentModel model, EquipmentPto equipment) {
    String templateId = equipment.templateId;
    String title = equipment.customTitle;
    String description = equipment.description;
    MagicalMaterial magicalMaterial = getMagicalMaterial(equipment);
    IEquipmentItem item = model.addItem(templateId, magicalMaterial);
    sendAMessageToThePlayerIfTheItemDefaultedItsMaterial(magicalMaterial, item);
    item.setPersonalization(title, description);
    item.setUnprinted();
    for (EquipmentStatsPto statsPto : equipment.printStats) {
      fillStats(model, item, statsPto);
    }
  }

  private void sendAMessageToThePlayerIfTheItemDefaultedItsMaterial(MagicalMaterial magicalMaterial, IEquipmentItem item) {
    if (item.getMaterial() != magicalMaterial) {
      Message message = new Message(MessageFormat.format("Your {0} requires a Magical Material. It found none, so it defaulted to Orichalcum.", item.getTitle()), WARNING, Permanent);
      messaging.addMessage(message);
    }
  }

  private MagicalMaterial getMagicalMaterial(EquipmentPto equipment) {
    if (equipment.material != null) {
      return MagicalMaterial.valueOf(equipment.material);
    }
    return null;
  }

  private void fillStats(EquipmentModel model, IEquipmentItem item, EquipmentStatsPto statsPto) {
    String printedStatId = statsPto.id;
    item.setPrinted(printedStatId);
    EquipmentHeroEvaluator provider = model.getHeroEvaluator();
    IEquipmentStats stats = item.getStat(printedStatId);
    for (EquipmentOptionPto optionPto : statsPto.options) {
      IEquipmentStatsOption option = provider.getCharacterSpecialtyOption(optionPto.name, optionPto.type);
      model.getOptionProvider().enableStatOption(item, stats, option);
    }
  }

  @Override
  protected EquipmentListPto saveModelToPto(EquipmentModel heroModel) {
    EquipmentListPto pto = new EquipmentListPto();
    ItemToPtoTransformer transformer = new ItemToPtoTransformer(heroModel);
    for (IEquipmentItem item : heroModel.getNaturalWeapons()) {
      pto.equipments.add(transformer.createPto(item));
    }
    for (IEquipmentItem item : heroModel.getEquipmentItems()) {
      pto.equipments.add(transformer.createPto(item));
    }
    return pto;
  }

  @Override
  public Identifier getModelId() {
    return EquipmentModel.ID;
  }
}