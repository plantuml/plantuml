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
package net.sourceforge.plantuml.ebnf;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.activitydiagram3.ftile.vcompact.FloatingNote;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.skin.PragmaKey;
import net.sourceforge.plantuml.skin.UmlDiagramType;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.utils.BlocLines;
import net.sourceforge.plantuml.utils.CharInspector;

public class PSystemEbnf extends TitledDiagram {

	private final List<TextBlockable> expressions = new ArrayList<>();

	public PSystemEbnf(UmlSource source, PreprocessingArtifact preprocessing) {
		super(source, UmlDiagramType.EBNF, null, preprocessing);
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("(EBNF)");
	}

	public CommandExecutionResult addBlocLines(BlocLines blines, String commentAbove, String commentBelow) {
		final boolean isCompact = getPragma().isDefine(PragmaKey.COMPACT);
		final CharInspector it = blines.inspector();
		final EbnfExpression tmp1 = EbnfExpression.create(it, isCompact, commentAbove, commentBelow);
		if (tmp1.isEmpty())
			return CommandExecutionResult.error("Unparsable expression");
		expressions.add(tmp1);
		return CommandExecutionResult.ok();

	}

	public CommandExecutionResult addNote(final Display note, Colors colors) {
		expressions.add(new TextBlockable() {
			@Override
			public TextBlock getUDrawable(ISkinParam skinParam, PreprocessingArtifact preprocessing) {
				final FloatingNote f = FloatingNote.create(note, skinParam, SName.ebnf);
				return TextBlockUtils.withMargin(f, 0, 0, 5, 15);
			}
		});
		return CommandExecutionResult.ok();
	}

	@Override
	protected ImageData exportDiagramNow(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {
		return createImageBuilder(fileFormatOption).drawable(getTextMainBlock(fileFormatOption)).write(os);
	}

	@Override
	protected TextBlock getTextMainBlock(FileFormatOption fileFormatOption) {
		if (expressions.size() == 0) {
			final Style style = ETile.getStyleSignature().getMergedStyle(getSkinParam().getCurrentStyleBuilder());
			final FontConfiguration fc = style.getFontConfiguration(getSkinParam().getIHtmlColorSet());

			final TextBlock tmp = EbnfEngine.syntaxError(fc, getSkinParam());
			return TextBlockUtils.addBackcolor(tmp, null);
		}

		TextBlock result = expressions.get(0).getUDrawable(getSkinParam(), getPreprocessingArtifact());
		for (int i = 1; i < expressions.size(); i++)
			result = TextBlockUtils.mergeTB(result,
					expressions.get(i).getUDrawable(getSkinParam(), getPreprocessingArtifact()),
					HorizontalAlignment.LEFT);
		return TextBlockUtils.addBackcolor(result, null);
	}

}
