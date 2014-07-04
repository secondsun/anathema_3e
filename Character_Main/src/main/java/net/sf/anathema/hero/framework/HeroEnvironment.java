package net.sf.anathema.hero.framework;

import net.sf.anathema.hero.framework.data.ExtensibleDataSet;
import net.sf.anathema.hero.framework.type.CharacterTypes;
import net.sf.anathema.hero.template.TemplateRegistry;
import net.sf.anathema.initialization.repository.DataFileProvider;
import net.sf.anathema.library.initialization.ObjectFactory;

public interface HeroEnvironment {

  TemplateRegistry getTemplateRegistry();

  DataFileProvider getDataFileProvider();

  <T extends ExtensibleDataSet> T getDataSet(Class<T> set);

  ObjectFactory getObjectFactory();

  CharacterTypes getCharacterTypes();
}