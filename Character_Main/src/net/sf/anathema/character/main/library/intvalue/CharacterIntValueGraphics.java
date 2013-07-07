package net.sf.anathema.character.main.library.intvalue;

import net.sf.anathema.character.main.CharacterUI;
import net.sf.anathema.character.main.type.ICharacterType;
import net.sf.anathema.framework.value.IntValueDisplayGraphics;

import javax.swing.Icon;

public class CharacterIntValueGraphics implements IntValueDisplayGraphics {

  private final CharacterUI ui = new CharacterUI();
  private final ICharacterType type;

  public CharacterIntValueGraphics(ICharacterType type) {
    this.type = type;
  }

  @Override
  public Icon getActiveIcon() {
    return ui.getMediumBallResource(type);
  }

  @Override
  public Icon getPassiveIcon() {
    return ui.getUnselectedBallResource();
  }

  @Override
  public Icon getBlockedIcon() {
    return ui.getUnselectableBallResource();
  }
}
