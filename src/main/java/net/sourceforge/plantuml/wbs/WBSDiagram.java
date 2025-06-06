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
package net.sourceforge.plantuml.wbs;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.Colors;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.AbstractCommonUGraphic;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.mindmap.IdeaShape;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.regex.Matcher2;
import net.sourceforge.plantuml.regex.Pattern2;
import net.sourceforge.plantuml.skin.UmlDiagramType;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.NoStyleAvailableException;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.utils.Direction;

public class WBSDiagram extends UmlDiagram {

	private WElement root;
	private WElement last;
	private String first;
	private final Map<String, WElement> codes = new LinkedHashMap<>();
	private final List<WBSLink> links = new ArrayList<>();

	public DiagramDescription getDescription() {
		return new DiagramDescription("Work Breakdown Structure");
	}

	public WBSDiagram(UmlSource source, PreprocessingArtifact preprocessing) {
		super(source, UmlDiagramType.WBS, null, preprocessing);
	}

	@Override
	protected ImageData exportDiagramInternal(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {

		return createImageBuilder(fileFormatOption).drawable(getTextMainBlock(fileFormatOption)).write(os);
	}

	@Override
	protected TextBlock getTextMainBlock(FileFormatOption fileFormatOption) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				drawMe(ug);
			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				return getDrawingElement().calculateDimension(stringBounder);
			}
		};
	}

	private void drawMe(UGraphic ug) {
		UTranslate translate = null;
		if (ug instanceof AbstractCommonUGraphic)
			translate = ((AbstractCommonUGraphic) ug).getTranslate();

		final Fork fork = getDrawingElement();
		fork.drawU(ug);

		if (translate == null)
			return;

		ug = ug.apply(translate.reverse());
		for (WBSLink link : links)
			link.drawU(ug);

	}

	private Fork getDrawingElement() {
		return new Fork(getSkinParam(), root);
	}

	public final static Pattern2 patternStereotype = Pattern2.cmpile("^\\s*(.*?)\\s*(\\<\\<\\s*(.*)\\s*\\>\\>)\\s*$");

	public CommandExecutionResult addIdea(String code, HColor backColor, int level, String label, Direction direction,
			IdeaShape shape) {
		final Matcher2 m = patternStereotype.matcher(label);
		String stereotype = null;
		if (m.matches()) {
			label = m.group(1);
			stereotype = m.group(2);
		}
		final Display display = Display.getWithNewlines(getPragma(), label);
		return addIdea(code, backColor, level, display, Stereotype.build(stereotype), direction, shape);
	}

	public CommandExecutionResult addIdea(String code, HColor backColor, int level, Display display, Stereotype stereotype,
			Direction direction, IdeaShape shape) {
		try {
			if (level == 0) {
				if (root != null)
					return CommandExecutionResult.error("Error 44");

				initRoot(backColor, display, stereotype, shape);
				return CommandExecutionResult.ok();
			}
			return add(code, backColor, level, display, stereotype, direction, shape);
		} catch (NoStyleAvailableException e) {
			// Logme.error(e);
			return CommandExecutionResult.error("General failure: no style available.");
		}
	}

	private void initRoot(HColor backColor, Display display, Stereotype stereotype, IdeaShape shape) {
		root = new WElement(backColor, display, stereotype, getSkinParam().getCurrentStyleBuilder(), shape);
		last = root;
	}

	private WElement getParentOfLast(int nb) {
		WElement result = last;
		for (int i = 0; i < nb; i++)
			result = result.getParent();

		return result;
	}

	public int getSmartLevel(String type) {
		if (root == null) {
			assert first == null;
			first = type;
			return 0;
		}
		type = type.replace('\t', ' ');
		if (type.contains(" ") == false)
			return type.length() - 1;

		if (type.endsWith(first))
			return type.length() - first.length();

		if (type.trim().length() == 1)
			return type.length() - 1;

		if (type.startsWith(first))
			return type.length() - first.length();

		throw new UnsupportedOperationException("type=<" + type + ">[" + first + "]");
	}

	private CommandExecutionResult add(String code, HColor backColor, int level, Display display, Stereotype stereotype,
			Direction direction, IdeaShape shape) {
		try {
			if (level == last.getLevel() + 1) {
				final WElement newIdea = last.createElement(backColor, level, display, stereotype, direction, shape,
						getSkinParam().getCurrentStyleBuilder());
				last = newIdea;
				if (code != null)
					codes.put(code, newIdea);
				return CommandExecutionResult.ok();
			}
			if (level <= last.getLevel()) {
				final int diff = last.getLevel() - level + 1;
				final WElement newIdea = getParentOfLast(diff).createElement(backColor, level, display, stereotype,
						direction, shape, getSkinParam().getCurrentStyleBuilder());
				last = newIdea;
				if (code != null)
					codes.put(code, newIdea);
				return CommandExecutionResult.ok();
			}
			return CommandExecutionResult.error("Bad tree structure");
		} catch (NoStyleAvailableException e) {
			// Logme.error(e);
			return CommandExecutionResult.error("General failure: no style available.");
		}
	}

	public CommandExecutionResult link(String code1, String code2, Colors colors, Stereotype stereotype) {
		final WElement element1 = codes.get(code1);
		if (element1 == null)
			return CommandExecutionResult.error("No such node " + code1);
		final WElement element2 = codes.get(code2);
		if (element2 == null)
			return CommandExecutionResult.error("No such node " + code2);
		HColor color = colors.getColor(ColorType.LINE);

		if (color == null) {
			final Style style = StyleSignatureBasic.of(SName.root, SName.element, SName.wbsDiagram, SName.arrow)
					.withTOBECHANGED(stereotype).getMergedStyle(getCurrentStyleBuilder());

			color = style.value(PName.LineColor).asColor(getSkinParam().getIHtmlColorSet());
		}

		links.add(new WBSLink(element1, element2, color));

		return CommandExecutionResult.ok();
	}

}
