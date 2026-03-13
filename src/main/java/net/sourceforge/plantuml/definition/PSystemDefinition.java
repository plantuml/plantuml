/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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
 *
 * Original Author:  Arnaud Roques
 * 
 *
 */
package net.sourceforge.plantuml.definition;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.UgSimpleDiagram;
import net.sourceforge.plantuml.annotation.Fast;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.font.UFontFactory;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.sprite.SpriteContainerEmpty;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.skin.Pragma;

public class PSystemDefinition extends UgSimpleDiagram {

	private final List<String> lines = new ArrayList<>();
	private final String startLine;

	public PSystemDefinition(UmlSource source, String startLine, PreprocessingArtifact preprocessing) {
		super(source, preprocessing);
		this.startLine = startLine;
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Definition)");
	}

	@Override
	public void drawU(UGraphic ug) {
		getTextBlock().drawU(ug);
	}

	@Fast
	@Override
	public XDimension2D calculateDimension(StringBounder stringBounder) {
		return getTextBlock().calculateDimension(stringBounder);
	}

	private TextBlock getTextBlock() {
		final UFont font = UFontFactory.sansSerif(14);
		final FontConfiguration fc = FontConfiguration.create(font, HColors.BLACK, HColors.BLACK, null);
		return Display.getWithNewlines(Pragma.createEmpty(), startLine).create(fc, HorizontalAlignment.LEFT,
				new SpriteContainerEmpty());
	}

	public void doCommandLine(String line) {
		this.lines.add(line);
	}

}
