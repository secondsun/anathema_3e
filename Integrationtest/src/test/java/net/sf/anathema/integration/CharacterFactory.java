package net.sf.anathema.integration;

import net.sf.anathema.TestInitializer;
import net.sf.anathema.framework.IApplicationModel;
import net.sf.anathema.fx.hero.perspective.CharacterSystemInitializer;
import net.sf.anathema.hero.framework.Character;
import net.sf.anathema.hero.framework.HeroEnvironment;
import net.sf.anathema.hero.framework.HeroEnvironmentExtractor;
import net.sf.anathema.hero.framework.item.CharacterItem;
import net.sf.anathema.hero.framework.item.Item;
import net.sf.anathema.hero.framework.persistence.HeroItemPersister;
import net.sf.anathema.hero.framework.persistence.RepositoryItemPersister;
import net.sf.anathema.hero.framework.perspective.model.CharacterIdentifier;
import net.sf.anathema.hero.framework.perspective.model.CharacterPersistenceModel;
import net.sf.anathema.hero.framework.type.CharacterTypes;
import net.sf.anathema.hero.template.HeroTemplate;
import net.sf.anathema.hero.template.TemplateTypeImpl;
import net.sf.anathema.lib.util.SimpleIdentifier;
import org.apache.commons.io.FileUtils;

import java.io.File;

public class CharacterFactory {

  private CharacterTypes characterTypes;
  private IApplicationModel model;
  private HeroEnvironment heroEnvironment;

  public void startAnathema() {
    TestInitializer initializer = TestInitializer.Create();
    this.model = initializer.initialize();
    new CharacterSystemInitializer(model,initializer.getEnvironment()).initializeCharacterSystem();
    heroEnvironment = HeroEnvironmentExtractor.getGenerics(model);
    this.characterTypes = heroEnvironment.getCharacterTypes();
  }

  public Character createCharacter(String type, String subtype) {
    HeroTemplate characterTemplate = loadTemplateForType(type, subtype);
    return createCharacter(characterTemplate);
  }

  public Character saveAndReload(Character character) throws  Exception{
    CharacterPersistenceModel persistenceModel = new CharacterPersistenceModel(model, heroEnvironment);
    CharacterItem characterItem = new CharacterItem(character);
    persistenceModel.save(characterItem);
    String repositoryId = characterItem.getRepositoryLocation().getId();
    Item loadItem = persistenceModel.loadItem(new CharacterIdentifier(repositoryId));
    return (Character) loadItem.getItemData();
  }

  private HeroTemplate loadTemplateForType(String type, String subtype) {
    HeroEnvironment generics = getCharacterGenerics();
    return generics.getTemplateRegistry().getTemplate(new TemplateTypeImpl(characterTypes.findById(type), new SimpleIdentifier(subtype)));
  }

  private Character createCharacter(HeroTemplate template) {
    RepositoryItemPersister itemPersister = new HeroItemPersister(getCharacterGenerics(), model.getMessaging());
    Item item = itemPersister.createNew(template);
    return (Character) item.getItemData();
  }

  private HeroEnvironment getCharacterGenerics() {
    return HeroEnvironmentExtractor.getGenerics(model);
  }

  public void tearDownRepository() throws Throwable{
    File repositoryDirectory = new File(model.getRepository().getRepositoryPath());
      if (repositoryDirectory.exists()) {
        FileUtils.deleteDirectory(repositoryDirectory);
      }
   }
}