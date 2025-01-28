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
package net.sourceforge.plantuml.klimt.creole.legacy;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.PlainDiagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.klimt.LineBreakStrategy;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.creole.Sheet;
import net.sourceforge.plantuml.klimt.creole.SheetBlock1;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.skin.Pragma;
import net.sourceforge.plantuml.skin.SkinParam;
import net.sourceforge.plantuml.skin.UmlDiagramType;

public class PSystemCreole extends PlainDiagram {
	// ::remove file when __CORE__

	private final List<String> lines = new ArrayList<>();

	public PSystemCreole(UmlSource source, PreprocessingArtifact preprocessing) {
		super(source, preprocessing);
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Creole)");
	}

	public void doCommandLine(String line) {
		lines.add(line);
	}

	@Override
	protected UDrawable getRootDrawable(FileFormatOption fileFormatOption) {
		final Display display = Display.create(lines);
		final UFont font = UFont.serif(14);
		final FontConfiguration fontConfiguration = FontConfiguration.blackBlueTrue(font);
		final SkinParam skinParam = SkinParam.create(UmlDiagramType.SEQUENCE, Pragma.createEmpty(), getPreprocessingArtifact().getOption());
		final Sheet sheet = skinParam.sheet(fontConfiguration, HorizontalAlignment.LEFT, CreoleMode.FULL)
				.createSheet(display);
		return new SheetBlock1(sheet, LineBreakStrategy.NONE, 0);

		// final Dimension2D dim = TextBlockUtils.getDimension(sheetBlock);
		// final UGraphic2 ug = fileFormat.createUGraphic(ColorMapper.IDENTITY, 1,
		// dim, null, false);
		// // sheetBlock.drawU(ug.apply(UTranslate.dy(10)));
		// sheetBlock.drawU(ug);
		// ug.writeImageTOBEMOVED(os, null, 96);
		// return new ImageDataSimple(dim);
	}
}
