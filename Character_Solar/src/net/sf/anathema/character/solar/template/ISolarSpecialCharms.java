package net.sf.anathema.character.solar.template;

import java.util.LinkedHashMap;

import net.sf.anathema.character.generic.health.HealthLevelType;
import net.sf.anathema.character.generic.impl.magic.charm.special.OxBodyTechniqueCharm;
import net.sf.anathema.character.generic.impl.magic.charm.special.StaticMultiLearnableCharm;
import net.sf.anathema.character.generic.impl.magic.charm.special.SubeffectCharm;
import net.sf.anathema.character.generic.impl.magic.charm.special.TraitDependentMultiLearnableCharm;
import net.sf.anathema.character.generic.impl.traits.EssenceTemplate;
import net.sf.anathema.character.generic.magic.charms.special.IMultiLearnableCharm;
import net.sf.anathema.character.generic.magic.charms.special.IOxBodyTechniqueCharm;
import net.sf.anathema.character.generic.magic.charms.special.ISubeffectCharm;
import net.sf.anathema.character.generic.traits.types.AbilityType;
import net.sf.anathema.character.generic.traits.types.OtherTraitType;

public interface ISolarSpecialCharms {

  public static final IOxBodyTechniqueCharm OX_BODY_TECHNIQUE = new OxBodyTechniqueCharm(
      "Solar.Ox-BodyTechnique", AbilityType.Endurance,//$NON-NLS-1$
      new LinkedHashMap<String, HealthLevelType[]>() {
        {
          put("Category.-0", new HealthLevelType[] { HealthLevelType.ZERO }); //$NON-NLS-1$
          put("Category.-1x2", new HealthLevelType[] { HealthLevelType.ONE, HealthLevelType.ONE }); //$NON-NLS-1$
          put(
              "Category.-1-2x2", new HealthLevelType[] { HealthLevelType.ONE, HealthLevelType.TWO, HealthLevelType.TWO }); //$NON-NLS-1$
        }
      });

  public static final IOxBodyTechniqueCharm OX_BODY_TECHNIQUE_SECOND_EDITION = new OxBodyTechniqueCharm(
      "Solar.Ox-BodyTechnique", AbilityType.Resistance,//$NON-NLS-1$
      new LinkedHashMap<String, HealthLevelType[]>() {
        {
          put("Category.-0", new HealthLevelType[] { HealthLevelType.ZERO }); //$NON-NLS-1$
          put("Category.-1x2", new HealthLevelType[] { HealthLevelType.ONE, HealthLevelType.ONE }); //$NON-NLS-1$
          put(
              "Category.-1-2x2", new HealthLevelType[] { HealthLevelType.ONE, HealthLevelType.TWO, HealthLevelType.TWO }); //$NON-NLS-1$
        }
      });

  public static final IMultiLearnableCharm GLORIOUS_SOLAR_SABER = new TraitDependentMultiLearnableCharm(
      "Solar.GloriousSolarSaber", //$NON-NLS-1$
      EssenceTemplate.SYSTEM_ESSENCE_MAX,
      AbilityType.Melee);

  public static final IMultiLearnableCharm CITY_MOVING_SECRETS = new TraitDependentMultiLearnableCharm(
      "Solar.City-MovingSecrets", //$NON-NLS-1$
      EssenceTemplate.SYSTEM_ESSENCE_MAX,
      OtherTraitType.Essence);

  public static final IMultiLearnableCharm IMMANENT_SOLAR_GLORY = new TraitDependentMultiLearnableCharm(
      "Solar.ImmanentSolarGlory", //$NON-NLS-1$
      EssenceTemplate.SYSTEM_ESSENCE_MAX,
      OtherTraitType.Essence);

  public static final IMultiLearnableCharm RIGHTEOUS_LION_DEFENSE = new StaticMultiLearnableCharm(
      "Solar.RighteousLionDefense", //$NON-NLS-1$
      2);

  public static final ISubeffectCharm ESSENCE_ARROW_ATTACK = new SubeffectCharm("Solar.EssenceArrowAttack", //$NON-NLS-1$
      new String[] { "FirstEffect", "SecondEffect", "ThirdEffect" });
}