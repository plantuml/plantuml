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
package net.sourceforge.plantuml.jsondiagram;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.ScaleSimple;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.json.JsonArray;
import net.sourceforge.plantuml.json.JsonValue;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.drawing.hand.UGraphicHandwritten;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.skin.UmlDiagramType;
import net.sourceforge.plantuml.yaml.Highlighted;

public class JsonDiagram extends TitledDiagram {

	private final JsonValue root;
	private final List<Highlighted> highlighted;
	private final boolean handwritten;

	public JsonDiagram(UmlSource source, UmlDiagramType type, JsonValue json, List<Highlighted> highlighted,
			StyleExtractor styleExtractor) {
		super(source, type, null);
		this.handwritten = styleExtractor.isHandwritten();
		if (json != null && (json.isString() || json.isBoolean() || json.isNumber())) {
			this.root = new JsonArray();
			((JsonArray) this.root).add(json);
		} else {
			this.root = json;
		}
		this.highlighted = highlighted;
		setScale(new ScaleSimple(styleExtractor.getScale()));
	}

	public DiagramDescription getDescription() {
		if (getUmlDiagramType() == UmlDiagramType.YAML)
			return new DiagramDescription("(Yaml)");

		if (getUmlDiagramType() == UmlDiagramType.HCL)
			return new DiagramDescription("(HCL)");

		return new DiagramDescription("(Json)");
	}

	@Override
	protected ImageData exportDiagramNow(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {

		return createImageBuilder(fileFormatOption).drawable(getTextMainBlock(fileFormatOption)).write(os);
	}

	private void drawInternal(UGraphic ug) {
		if (handwritten)
			ug = new UGraphicHandwritten(ug);
		if (root == null) {
			final Display display = Display
					.getWithNewlines("Your data does not sound like " + getUmlDiagramType() + " data");
			final FontConfiguration fontConfiguration = FontConfiguration.blackBlueTrue(UFont.courier(14));
			TextBlock result = display.create(fontConfiguration, HorizontalAlignment.LEFT, getSkinParam());
			result = TextBlockUtils.withMargin(result, 5, 2);
			result.drawU(ug);
		} else {
			new SmetanaForJson(ug, getSkinParam()).drawMe(root, highlighted);
		}
	}

	@Override
	protected TextBlock getTextMainBlock(final FileFormatOption fileFormatOption) {
		return new AbstractTextBlock() {

			public void drawU(UGraphic ug) {
				drawInternal(ug);
			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				return TextBlockUtils.getMinMax(getTextMainBlock(fileFormatOption), stringBounder, true).getDimension();
			}
		};
	}

}
