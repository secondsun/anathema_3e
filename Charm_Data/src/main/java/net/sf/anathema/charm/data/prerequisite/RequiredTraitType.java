package net.sf.anathema.charm.data.prerequisite;

import net.sf.anathema.lib.lang.ReflectionEqualsObject;

public class RequiredTraitType extends ReflectionEqualsObject{

  public final String type;

  public RequiredTraitType(String type) {
    this.type = type;
  }
}