/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2023, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
package net.sourceforge.plantuml.ebnf;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;

public class PSystemEbnf extends TitledDiagram {

	private final List<String> lines = new ArrayList<>();

	public PSystemEbnf(UmlSource source) {
		super(source, UmlDiagramType.EBNF, null);
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(EBNF)");
	}

	public void addLine(String line) {
		lines.add(line.trim());
	}

	@Override
	protected ImageData exportDiagramNow(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {
		return createImageBuilder(fileFormatOption).drawable(getTextBlock()).write(os);
	}

	private TextBlockBackcolored getTextBlock() {
		final List<EbnfSingleExpression> all = EbnfExpressions.build(lines, getPragma());
		if (all.size() == 0) {
			final Style style = ETile.getStyleSignature().getMergedStyle(getSkinParam().getCurrentStyleBuilder());
			final FontConfiguration fc = style.getFontConfiguration(getSkinParam().getIHtmlColorSet());

			final TextBlock tmp = EbnfEngine.syntaxError(fc, getSkinParam());
			return TextBlockUtils.addBackcolor(tmp, null);
		}

		TextBlock result = all.get(0).getUDrawable(getSkinParam());
		for (int i = 1; i < all.size(); i++)
			result = TextBlockUtils.mergeTB(result, all.get(i).getUDrawable(getSkinParam()), HorizontalAlignment.LEFT);
		return TextBlockUtils.addBackcolor(result, null);
	}
}
