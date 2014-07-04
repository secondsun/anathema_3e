package net.sf.anathema.hero.framework.perspective;

import net.sf.anathema.hero.dummy.DummyCharacterTypes;
import net.sf.anathema.hero.dummy.DummyMundaneCharacterType;
import net.sf.anathema.hero.dummy.template.SimpleDummyCharacterTemplate;
import net.sf.anathema.hero.framework.HeroEnvironment;
import net.sf.anathema.hero.framework.type.CharacterType;
import net.sf.anathema.hero.template.TemplateRegistry;
import net.sf.anathema.library.event.ChangeListener;
import org.junit.Test;
import org.mockito.Mockito;

import static java.util.Collections.singletonList;

public class CharacterItemCreationModelTest {

  @Test
  public void comparesNewlySetCharacterTypeViaEqualsNotIdentity() throws Exception {
    HeroEnvironment generics = createGenericsWithCharacterType(new DummyMundaneCharacterType());
    CharacterItemCreationModel model = new CharacterItemCreationModel(generics);
    ChangeListener listener = Mockito.mock(ChangeListener.class);
    model.addListener(listener);
    model.setCharacterType(new DummyMundaneCharacterType());
    Mockito.verifyZeroInteractions(listener);
  }

  private HeroEnvironment createGenericsWithCharacterType(CharacterType characterType) {
    HeroEnvironment generics = Mockito.mock(HeroEnvironment.class);
    DummyCharacterTypes characterTypes = new DummyCharacterTypes();
    characterTypes.add(characterType);
    Mockito.when(generics.getCharacterTypes()).thenReturn(characterTypes);
    TemplateRegistry registry = Mockito.mock(TemplateRegistry.class);
    SimpleDummyCharacterTemplate characterTemplate = new SimpleDummyCharacterTemplate(characterType, null);
    Mockito.when(registry.getAllSupportedTemplates(characterType)).thenReturn(singletonList(characterTemplate));
    Mockito.when(generics.getTemplateRegistry()).thenReturn(registry);
    return generics;
  }
}
