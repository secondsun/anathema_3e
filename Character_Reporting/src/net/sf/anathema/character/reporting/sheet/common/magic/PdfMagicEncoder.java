package net.sf.anathema.character.reporting.sheet.common.magic;

import java.util.ArrayList;
import java.util.List;

import net.sf.anathema.character.generic.character.IGenericCharacter;
import net.sf.anathema.character.generic.impl.magic.CharmUtilities;
import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.character.generic.magic.IMagic;
import net.sf.anathema.character.generic.magic.IMagicVisitor;
import net.sf.anathema.character.generic.magic.ISpell;
import net.sf.anathema.character.generic.rules.IExaltedEdition;
import net.sf.anathema.character.generic.type.CharacterType;
import net.sf.anathema.character.reporting.sheet.common.IPdfContentEncoder;
import net.sf.anathema.character.reporting.sheet.common.magic.stats.IMagicStats;
import net.sf.anathema.character.reporting.sheet.common.magic.stats.MagicStats;
import net.sf.anathema.character.reporting.util.Bounds;
import net.sf.anathema.lib.resources.IResources;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;

public class PdfMagicEncoder implements IPdfContentEncoder {


  public static List<IMagicStats> collectPrintMagic(final IGenericCharacter character) {
    IExaltedEdition edition = character.getRules().getEdition();
    final CharacterType characterType = character.getTemplate().getTemplateType().getCharacterType();
    final List<IMagic> printMagic = new ArrayList<IMagic>();
    for (IMagic magic : character.getAllLearnedMagic()) {
      magic.accept(new IMagicVisitor() {
        public void visitCharm(ICharm charm) {
          if (!CharmUtilities.isGenericCharmFor(charm, characterType)) {
            printMagic.add(charm);
          }
        }

        public void visitSpell(ISpell spell) {
          printMagic.add(spell);
        }
      });
    }
    List<IMagicStats> printStats = new ArrayList<IMagicStats>();
    for (IMagic magic : printMagic) {
      printStats.add(new MagicStats(magic, edition));
    }
    return printStats;
  }

  private final IResources resources;
  private final BaseFont baseFont;
  private final List<IMagicStats> printMagic;

  public PdfMagicEncoder(IResources resources, BaseFont baseFont, List<IMagicStats> printMagic) {
    this.resources = resources;
    this.baseFont = baseFont;
    this.printMagic = printMagic;
  }

  public void encode(PdfContentByte directContent, IGenericCharacter character, Bounds bounds) throws DocumentException {
    new PdfMagicTableEncoder(resources, baseFont, printMagic).encodeTable(directContent, character, bounds);
  }
}