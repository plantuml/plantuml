/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
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
 */
package net.sourceforge.plantuml.jsondiagram;

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import net.sourceforge.plantuml.AnnotatedWorker;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.Scale;
import net.sourceforge.plantuml.TitledDiagram;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.InnerStrategy;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.json.JsonArray;
import net.sourceforge.plantuml.json.JsonValue;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.ImageParameter;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class JsonDiagram extends TitledDiagram {

	private final JsonValue root;
	private final List<String> highlighted;

	public JsonDiagram(UmlDiagramType type, JsonValue json, List<String> highlighted) {
		super(type);
		if (json != null && (json.isString() || json.isBoolean() || json.isNumber())) {
			this.root = new JsonArray();
			((JsonArray) this.root).add(json);
		} else {
			this.root = json;
		}
		this.highlighted = highlighted;
	}

	public DiagramDescription getDescription() {
		if (getUmlDiagramType() == UmlDiagramType.YAML) {
			return new DiagramDescription("(Yaml)");
		}
		return new DiagramDescription("(Json)");
	}

	@Override
	protected ImageData exportDiagramNow(OutputStream os, int index, FileFormatOption fileFormatOption, long seed)
			throws IOException {
		final Scale scale = getScale();

		final double dpiFactor = scale == null ? 1 : scale.getScale(100, 100);
		final ISkinParam skinParam = getSkinParam();
		final ImageParameter imageParameter = new ImageParameter(this, fileFormatOption, dpiFactor);
		final ImageBuilder imageBuilder = ImageBuilder.build(imageParameter);
		TextBlock result = getTextBlock();
		result = new AnnotatedWorker(this, skinParam, fileFormatOption.getDefaultStringBounder(getSkinParam()))
				.addAdd(result);
		imageBuilder.setUDrawable(result);

		return imageBuilder.writeImageTOBEMOVED(0, os);
	}

	private void drawInternal(UGraphic ug) {
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

	private TextBlockBackcolored getTextBlock() {
		return new TextBlockBackcolored() {

			public void drawU(UGraphic ug) {
				drawInternal(ug);
			}

			public MinMax getMinMax(StringBounder stringBounder) {
				return null;
			}

			public Rectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
				return null;
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return null;
			}

			public HColor getBackcolor() {
				return null;
			}
		};
	}

}
