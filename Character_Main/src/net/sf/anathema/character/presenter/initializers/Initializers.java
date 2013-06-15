package net.sf.anathema.character.presenter.initializers;

import net.sf.anathema.character.generic.framework.CharacterGenericsExtractor;
import net.sf.anathema.character.model.CharacterModelGroup;
import net.sf.anathema.framework.IApplicationModel;
import net.sf.anathema.initialization.Instantiater;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Initializers {

  private final IApplicationModel applicationModel;
  private final Instantiater instantiater;

  public Initializers(IApplicationModel applicationModel) {
    this.instantiater = CharacterGenericsExtractor.getGenerics(applicationModel).getInstantiater();
    this.applicationModel = applicationModel;
  }

  public List<CoreModelInitializer> getInOrderFor(CharacterModelGroup group) {
    ArrayList<CoreModelInitializer> initializers = new ArrayList<>();
    Collection<CoreModelInitializer> collection = instantiater.instantiateOrdered(RegisteredInitializer.class, applicationModel);
    for (CoreModelInitializer initializer : collection) {
      CharacterModelGroup targetGroup = initializer.getClass().getAnnotation(RegisteredInitializer.class).value();
      if (targetGroup.equals(group)) {
        initializers.add(initializer);
      }
    }
    return initializers;
  }
}