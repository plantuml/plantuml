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
 */
package net.sourceforge.plantuml.help;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.atmp.ImageBuilder;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.klimt.LineBreakStrategy;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.creole.Sheet;
import net.sourceforge.plantuml.klimt.creole.SheetBlock1;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.skin.UmlDiagramType;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;

public class Help extends UmlDiagram {

	private final List<CharSequence> lines = new ArrayList<>();

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Help)");
	}

	public Help(UmlSource source) {
		super(source, UmlDiagramType.HELP, null);
	}

	@Override
	public ImageBuilder createImageBuilder(FileFormatOption fileFormatOption) throws IOException {
		return super.createImageBuilder(fileFormatOption).annotations(false);
	}

	@Override
	protected ImageData exportDiagramInternal(OutputStream os, int index, FileFormatOption fileFormat)
			throws IOException {
		final Display display = Display.create(lines);
		final UFont font = UFont.serif(16);
		final FontConfiguration fontConfiguration = FontConfiguration.blackBlueTrue(font);
		final Sheet sheet = getSkinParam().sheet(fontConfiguration, HorizontalAlignment.LEFT, CreoleMode.FULL)
				.createSheet(display);
		final SheetBlock1 sheetBlock = new SheetBlock1(sheet, LineBreakStrategy.NONE, 0);
		return createImageBuilder(fileFormat).drawable(sheetBlock).write(os);
	}

	public void add(CharSequence line) {
		this.lines.add(line);
	}

	@Override
	public ClockwiseTopRightBottomLeft getDefaultMargins() {
		return ClockwiseTopRightBottomLeft.same(0);
	}

	@Override
	protected TextBlock getTextMainBlock(FileFormatOption fileFormatOption) {
		throw new UnsupportedOperationException();
	}

}
