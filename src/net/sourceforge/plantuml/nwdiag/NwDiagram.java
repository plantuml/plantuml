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
package net.sourceforge.plantuml.nwdiag;

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.AnnotatedWorker;
import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.Scale;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.InnerStrategy;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UEmpty;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class NwDiagram extends UmlDiagram {

	private boolean initDone;
	private final Map<String, DiagElement> elements = new LinkedHashMap<String, DiagElement>();
	private final List<Network> networks = new ArrayList<Network>();
	private final List<DiagGroup> groups = new ArrayList<DiagGroup>();
	private DiagGroup currentGroup = null;

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Nwdiag)");
	}

	@Override
	public UmlDiagramType getUmlDiagramType() {
		return UmlDiagramType.NWDIAG;
	}

	public void init() {
		initDone = true;
	}

	private Network currentNetwork() {
		if (networks.size() == 0) {
			return null;
		}
		return networks.get(networks.size() - 1);
	}

	public CommandExecutionResult openGroup(String name) {
		if (initDone == false) {
			return error();
		}
		currentGroup = new DiagGroup(name, currentNetwork());
		groups.add(currentGroup);
		return CommandExecutionResult.ok();
	}

	public CommandExecutionResult openNetwork(String name) {
		if (initDone == false) {
			return error();
		}
		final Network network = new Network(name);
		networks.add(network);
		return CommandExecutionResult.ok();
	}

	public CommandExecutionResult endSomething() {
		if (initDone == false) {
			return error();
		}
		this.currentGroup = null;
		return CommandExecutionResult.ok();
	}

	public CommandExecutionResult addElement(String name, String definition) {
		if (initDone == false) {
			return error();
		}
		if (currentGroup != null) {
			currentGroup.addElement(name);
		}
		if (currentNetwork() != null) {
			DiagElement element = elements.get(name);
			if (element == null) {
				element = new DiagElement(name, currentNetwork());
				elements.put(name, element);
			}
			final Map<String, String> props = toSet(definition);
			final String description = props.get("description");
			if (description != null) {
				element.setDescription(description);
			}
			final String shape = props.get("shape");
			if (shape != null) {
				element.setShape(shape);
			}
			currentNetwork().addElement(element, props);
		}
		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult error() {
		return CommandExecutionResult.error("");
	}

	private Map<String, String> toSet(String definition) {
		final Map<String, String> result = new HashMap<String, String>();
		if (definition == null) {
			return result;
		}
		final Pattern p = Pattern.compile("\\s*(\\w+)\\s*=\\s*(\"([^\"]*)\"|[^\\s,]+)");
		final Matcher m = p.matcher(definition);
		while (m.find()) {
			final String name = m.group(1);
			final String value = m.group(3) == null ? m.group(2) : m.group(3);
			result.put(name, value);
		}
		return result;

	}

	@Override
	protected ImageData exportDiagramInternal(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {
		final Scale scale = getScale();

		final double dpiFactor = scale == null ? 1 : scale.getScale(100, 100);
		final ISkinParam skinParam = getSkinParam();
		final int margin1;
		final int margin2;
		if (SkinParam.USE_STYLES()) {
			margin1 = SkinParam.zeroMargin(0);
			margin2 = SkinParam.zeroMargin(0);
		} else {
			margin1 = 0;
			margin2 = 0;
		}
		final ImageBuilder imageBuilder = ImageBuilder.buildB(new ColorMapperIdentity(), false, ClockwiseTopRightBottomLeft.margin1margin2((double) margin1, (double) margin2),
		null, "", "", dpiFactor, null);
		TextBlock result = getTextBlock();
		result = new AnnotatedWorker(this, skinParam, fileFormatOption.getDefaultStringBounder()).addAdd(result);
		imageBuilder.setUDrawable(result);

		return imageBuilder.writeImageTOBEMOVED(fileFormatOption, 0, os);
	}

	private TextBlockBackcolored getTextBlock() {
		return new TextBlockBackcolored() {
			public void drawU(UGraphic ug) {
				drawMe(ug);
			}

			public Rectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
				return null;
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				return getTotalDimension(stringBounder);
			}

			public MinMax getMinMax(StringBounder stringBounder) {
				throw new UnsupportedOperationException();
			}

			public HColor getBackcolor() {
				return null;
			}

		};
	}

	private TextBlock toTextBlock(String name, String s) {
		if (s != null) {
			name += "\\n" + s;
		}
		return Display.getWithNewlines(name).create(getFontConfiguration(), HorizontalAlignment.RIGHT,
				new SpriteContainerEmpty());
	}

	private FontConfiguration getFontConfiguration() {
		final UFont font = UFont.serif(11);
		return new FontConfiguration(font, HColorUtils.BLACK, HColorUtils.BLACK, false);
	}

	private Dimension2D getTotalDimension(StringBounder stringBounder) {
		return TextBlockUtils.getMinMax(new UDrawable() {
			public void drawU(UGraphic ug) {
				drawMe(ug);
			}
		}, stringBounder, true).getDimension();
	}

	private final double margin = 5;

	private void drawMe(UGraphic ug) {
		ug = ug.apply(new UTranslate(margin, margin));

		final StringBounder stringBounder = ug.getStringBounder();
		final GridTextBlockDecorated grid = new GridTextBlockDecorated(networks.size(), elements.size(), groups);

		for (int i = 0; i < networks.size(); i++) {
			final Network current = networks.get(i);
			final Network next = i + 1 < networks.size() ? networks.get(i + 1) : null;
			int j = 0;
			for (Map.Entry<String, DiagElement> ent : elements.entrySet()) {
				final DiagElement element = ent.getValue();
				if (element.getMainNetwork() == current && current.constainsLocally(ent.getKey())) {
					final String ad1 = current.getAdress(element);
					final String ad2 = next == null ? null : next.getAdress(element);
					grid.add(i, j, element.asTextBlock(ad1, ad2));
				}
				j++;
			}
		}

		double deltaX = 0;
		double deltaY = 0;
		for (int i = 0; i < networks.size(); i++) {
			final Network current = networks.get(i);
			final String address = current.getOwnAdress();
			final TextBlock desc = toTextBlock(current.getName(), address);
			final Dimension2D dim = desc.calculateDimension(stringBounder);
			if (i == 0) {
				deltaY = (dim.getHeight() - GridTextBlockDecorated.NETWORK_THIN) / 2;
			}
			deltaX = Math.max(deltaX, dim.getWidth());
		}
		double y = 0;
		for (int i = 0; i < networks.size(); i++) {
			final Network current = networks.get(i);
			final String address = current.getOwnAdress();
			final TextBlock desc = toTextBlock(current.getName(), address);
			final Dimension2D dim = desc.calculateDimension(stringBounder);
			desc.drawU(ug.apply(new UTranslate(deltaX - dim.getWidth(), y)));

			y += grid.lineHeight(stringBounder, i);
		}
		deltaX += 5;

		grid.drawU(ug.apply(ColorParam.activityBorder.getDefaultValue())
				.apply(ColorParam.activityBackground.getDefaultValue().bg()).apply(new UTranslate(deltaX, deltaY)));
		final Dimension2D dimGrid = grid.calculateDimension(stringBounder);

		ug.apply(new UTranslate(dimGrid.getWidth() + deltaX + margin, dimGrid.getHeight() + deltaY + margin))
				.draw(new UEmpty(1, 1));

	}

	public CommandExecutionResult setProperty(String property, String value) {
		if (initDone == false) {
			return error();
		}
		if ("address".equalsIgnoreCase(property) && currentNetwork() != null) {
			currentNetwork().setOwnAdress(value);
		}
		if ("color".equalsIgnoreCase(property)) {
			final HColor color = GridTextBlockDecorated.colors.getColorIfValid(value);
			if (currentGroup != null) {
				currentGroup.setColor(color);
			} else if (currentNetwork() != null) {
				currentNetwork().setColor(color);
			}
		}
		return CommandExecutionResult.ok();
	}

	public CommandExecutionResult link() {
		if (initDone == false) {
			return error();
		}
		return CommandExecutionResult.ok();
	}

}
