/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
 *
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 *
 * Revision $Revision: 11025 $
 *
 */
package net.sourceforge.plantuml.creole;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.FontPosition;
import net.sourceforge.plantuml.graphic.FontStyle;
import net.sourceforge.plantuml.graphic.ImgValign;
import net.sourceforge.plantuml.openiconic.OpenIcon;
import net.sourceforge.plantuml.ugraphic.sprite.Sprite;
import net.sourceforge.plantuml.utils.CharHidder;

public class StripeSimple implements Stripe {

    final private List<Atom> atoms = new ArrayList<Atom>();
    final private List<Command> commands = new ArrayList<Command>();

    private FontConfiguration fontConfiguration;

    final private StripeStyle style;
    final private ISkinSimple skinParam;

    public StripeSimple(FontConfiguration fontConfiguration, StripeStyle style, CreoleContext context,
            ISkinSimple skinParam, CreoleMode modeSimpleLine) {
        this.fontConfiguration = fontConfiguration;
        this.style = style;
        this.skinParam = skinParam;

        // class Splitter
        commands.add(CommandCreoleStyle.createCreole(FontStyle.BOLD));
        commands.add(CommandCreoleStyle.createLegacy(FontStyle.BOLD));
        commands.add(CommandCreoleStyle.createLegacyEol(FontStyle.BOLD));
        commands.add(CommandCreoleStyle.createCreole(FontStyle.ITALIC));
        commands.add(CommandCreoleStyle.createLegacy(FontStyle.ITALIC));
        commands.add(CommandCreoleStyle.createLegacyEol(FontStyle.ITALIC));
        if (modeSimpleLine == CreoleMode.FULL) {
            commands.add(CommandCreoleStyle.createCreole(FontStyle.UNDERLINE));
        }
        commands.add(CommandCreoleStyle.createLegacy(FontStyle.UNDERLINE));
        commands.add(CommandCreoleStyle.createLegacyEol(FontStyle.UNDERLINE));
        commands.add(CommandCreoleStyle.createCreole(FontStyle.STRIKE));
        commands.add(CommandCreoleStyle.createLegacy(FontStyle.STRIKE));
        commands.add(CommandCreoleStyle.createLegacyEol(FontStyle.STRIKE));
        commands.add(CommandCreoleStyle.createCreole(FontStyle.WAVE));
        commands.add(CommandCreoleStyle.createLegacy(FontStyle.WAVE));
        commands.add(CommandCreoleStyle.createLegacyEol(FontStyle.WAVE));
        commands.add(CommandCreoleStyle.createLegacy(FontStyle.BACKCOLOR));
        commands.add(CommandCreoleStyle.createLegacyEol(FontStyle.BACKCOLOR));
        commands.add(CommandCreoleSizeChange.create());
        commands.add(CommandCreoleSizeChange.createEol());
        commands.add(CommandCreoleColorChange.create());
        commands.add(CommandCreoleColorChange.createEol());
        commands.add(CommandCreoleColorAndSizeChange.create());
        commands.add(CommandCreoleColorAndSizeChange.createEol());
        commands.add(CommandCreoleExposantChange.create(FontPosition.EXPOSANT));
        commands.add(CommandCreoleExposantChange.create(FontPosition.INDICE));
        commands.add(CommandCreoleImg.create());
        commands.add(CommandCreoleOpenIcon.create());
        commands.add(CommandCreoleSprite.create());
        commands.add(CommandCreoleSpace.create());
        commands.add(CommandCreoleFontFamilyChange.create());
        commands.add(CommandCreoleFontFamilyChange.createEol());
        commands.add(CommandCreoleMonospaced.create(skinParam.getMonospacedFamily()));
        commands.add(CommandCreoleUrl.create(skinParam));
        commands.add(CommandCreoleSvgAttributeChange.create());

        final Atom header = style.getHeader(fontConfiguration, context);

        if (header != null) {
            atoms.add(header);
        }
    }

    @Override
    public List<Atom> getAtoms() {
        if (atoms.size() == 0) {
            atoms.add(AtomText.create(" ", fontConfiguration));
        }
        return Collections.unmodifiableList(atoms);
    }

    public FontConfiguration getActualFontConfiguration() {
        return fontConfiguration;
    }

    public void setActualFontConfiguration(FontConfiguration fontConfiguration) {
        this.fontConfiguration = fontConfiguration;
    }

    public void analyzeAndAdd(String line) {

        System.out.println(line);
        if (line == null) {
            throw new IllegalArgumentException();
        }
        line = CharHidder.hide(line);
        if (style.getType() == StripeStyleType.HEADING) {
            atoms.add(AtomText.createHeading(line, fontConfiguration, style.getOrder()));
        } else if (style.getType() == StripeStyleType.HORIZONTAL_LINE) {
            atoms.add(CreoleHorizontalLine.create(fontConfiguration, line, style.getStyle(), skinParam));
        } else {
            modifyStripe(line);
        }
    }

    public void addImage(String src) {
        atoms.add(AtomImg.create(src, ImgValign.TOP, 0));
    }

    public void addSpace(int size) {
        atoms.add(AtomSpace.create(size));
    }

    public void addUrl(Url url) {
        atoms.add(AtomText.createUrl(url, fontConfiguration));
    }

    public void addSprite(String src) {
        final Sprite sprite = skinParam.getSprite(src);
        if (sprite != null) {
            atoms.add(new AtomSprite(sprite.asTextBlock(fontConfiguration.getColor()), fontConfiguration));
        }
    }

    public void addOpenIcon(String src) {
        final OpenIcon openIcon = OpenIcon.retrieve(src);
        if (openIcon != null) {
            atoms.add(new AtomOpenIcon(openIcon, fontConfiguration));
        }
    }

    private void modifyStripe(String line) {
        final StringBuilder pending = new StringBuilder();

        while (line.length() > 0) {
            final Command cmd = searchCommand(line);
            if (cmd == null) {
                pending.append(line.charAt(0));
                line = line.substring(1);
            } else {
                addPending(pending);
                line = cmd.executeAndGetRemaining(line, this);
            }
        }
        addPending(pending);
    }

    private void addPending(StringBuilder pending) {
        if (pending.length() == 0) {
            return;
        }
        atoms.add(AtomText.create(pending.toString(), fontConfiguration));
        pending.setLength(0);
    }

    private Command searchCommand(String line) {
        for (final Command cmd : commands) {
            final int i = cmd.matchingSize(line);
            if (i != 0) {
                return cmd;
            }
        }
        return null;
    }

}
