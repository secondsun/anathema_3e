package net.sf.anathema.hero.spells.sheet.content;

import net.sf.anathema.hero.charms.display.tooltip.IMagicSourceStringBuilder;
import net.sf.anathema.hero.charms.display.tooltip.source.MagicSourceContributor;
import net.sf.anathema.hero.charms.sheet.content.IMagicStats;
import net.sf.anathema.hero.charms.sheet.content.stats.AbstractCharmStats;
import net.sf.anathema.hero.charms.sheet.content.stats.AbstractMagicStats;
import net.sf.anathema.hero.spells.data.Spell;
import net.sf.anathema.library.resources.Resources;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpellStats extends AbstractMagicStats<Spell> {

  public SpellStats(Spell spell) {
    super(spell);
  }

  @Override
  public String getGroupName(Resources resources) {
    return resources.getString("Sheet.Magic.Group.Sorcery");
  }

  @Override
  public String getType(Resources resources) {
    return resources.getString(getMagic().getCircleType().getId());
  }

  @Override
  public String getDurationString(Resources resources) {
    return "-";
  }

  @Override
  public String getSourceString(Resources resources) {
    IMagicSourceStringBuilder<Spell> stringBuilder = new MagicSourceContributor<>(resources);
    return stringBuilder.createShortSourceString(getMagic());
  }

  protected Collection<String> getDetailKeys() {
    String target = getMagic().getTarget();
    if (target != null) {
      return Lists.newArrayList("Spells.Target." + target);
    }
    return new ArrayList<>();
  }

  @Override
  public Collection<String> getDetailStrings(final Resources resources) {
    Stream<String> keys = getDetailKeys().stream();
    return keys.map(resources::getString).collect(Collectors.toList());
  }

  @Override
  public String getNameString(Resources resources) {
    return resources.getString(getMagic().getName().text);
  }

  @SuppressWarnings("NullableProblems")
  @Override
  public int compareTo(IMagicStats stats) {
    if (stats instanceof AbstractCharmStats) {
      return 1;
    }
    SpellStats spell = (SpellStats) stats;
    int r = getMagic().getCircleType().compareTo(spell.getMagic().getCircleType());
    if (r == 0) {
      r = getMagic().getName().text.compareTo(spell.getMagic().getName().text);
    }
    return r;
  }

  @SuppressWarnings("SimplifiableIfStatement")
  @Override
  public boolean equals(Object obj) {
    if (obj == null || obj.getClass() != getClass()) {
      return false;
    }
    return compareTo((SpellStats) obj) == 0;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(getMagic().getCircleType()).append(getMagic().getName().text).build();
  }
}
