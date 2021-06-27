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
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.InnerStrategy;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.nwdiag.legacy.GridTextBlockDecorated;
import net.sourceforge.plantuml.nwdiag.legacy.NServerLegacy;
import net.sourceforge.plantuml.nwdiag.legacy.NetworkLegacy;
import net.sourceforge.plantuml.nwdiag.legacy.NwGroupLegacy;
import net.sourceforge.plantuml.nwdiag.next.NPlayField;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UEmpty;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class NwDiagram extends UmlDiagram {

	private boolean initDone;
	private final Map<String, NServerLegacy> servers = new LinkedHashMap<String, NServerLegacy>();
	private final List<NetworkLegacy> networks = new ArrayList<>();
	private final List<NwGroupLegacy> groups = new ArrayList<>();
	private NwGroupLegacy currentGroup = null;

	private final NPlayField playField = new NPlayField();

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Nwdiag)");
	}

	public NwDiagram(UmlSource source) {
		super(source, UmlDiagramType.NWDIAG);
	}

	public void init() {
		initDone = true;
	}

	private NetworkLegacy currentNetwork() {
		if (networks.size() == 0) {
			return null;
		}
		return networks.get(networks.size() - 1);
	}

	public CommandExecutionResult openGroup(String name) {
		if (initDone == false) {
			return errorNoInit();
		}
		currentGroup = new NwGroupLegacy(name, currentNetwork());
		groups.add(currentGroup);
		return CommandExecutionResult.ok();
	}

	public CommandExecutionResult openNetwork(String name) {
		if (initDone == false) {
			return errorNoInit();
		}
		createNetwork(name);
		return CommandExecutionResult.ok();
	}

	private NetworkLegacy createNetwork(String name) {
		final NetworkLegacy network = new NetworkLegacy(playField.createNewStage(), name, networks.size());
		networks.add(network);
		return network;
	}

	public CommandExecutionResult link(String name1, String name2) {
		if (initDone == false) {
			return errorNoInit();
		}
		final NServerLegacy element;
		if (currentNetwork() == null) {
			createNetwork(name1);
			element = new NServerLegacy(name2, currentNetwork(), this.getSkinParam());
		} else {
			final NServerLegacy already = servers.get(name1);
			final NetworkLegacy network1 = createNetwork("");
			network1.goInvisible();
			if (already != null) {
				currentNetwork().addServer(already, toSet(null));
				already.connect(currentNetwork(), toSet(null));
			}
			element = new NServerLegacy(name2, currentNetwork(), this.getSkinParam());
			element.sameColThan(already);
		}
		servers.put(name2, element);
		addInternal(element, toSet(null));
		return CommandExecutionResult.ok();
	}

	private void addInternal(NServerLegacy server, Map<String, String> props) {
		currentNetwork().addServer(Objects.requireNonNull(server), props);
		server.connect(currentNetwork(), props);
		final String description = props.get("description");
		if (description != null) {
			server.setDescription(description);
		}
		final String shape = props.get("shape");
		if (shape != null) {
			server.setShape(shape);
		}
	}

	public CommandExecutionResult addElement(String name, String definition) {
		if (initDone == false) {
			return errorNoInit();
		}
		if (currentGroup != null) {
			currentGroup.addName(name);
		}
		NServerLegacy server = null;
		if (currentNetwork() == null) {
			if (currentGroup != null) {
				return CommandExecutionResult.ok();
			}
			assert currentGroup == null;
			final NetworkLegacy network1 = createNetwork("");
			network1.goInvisible();
			server = new NServerLegacy(name, currentNetwork(), this.getSkinParam());
			servers.put(name, server);
			server.doNotHaveItsOwnColumn();
		} else {
			server = servers.get(name);
			if (server == null) {
				server = new NServerLegacy(name, currentNetwork(), this.getSkinParam());
				servers.put(name, server);
			}
		}
		addInternal(server, toSet(definition));
		return CommandExecutionResult.ok();
	}

	public CommandExecutionResult endSomething() {
		if (initDone == false) {
			return errorNoInit();
		}
		this.currentGroup = null;
		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult errorNoInit() {
		return CommandExecutionResult.error("Maybe you forget 'nwdiag {' in your diagram ?");
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

		return createImageBuilder(fileFormatOption).drawable(getTextBlock()).write(os);
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
		final GridTextBlockDecorated grid = buildGrid();

		double deltaX = 0;
		double deltaY = 0;
		for (int i = 0; i < networks.size(); i++) {
			final NetworkLegacy current = networks.get(i);
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
			final NetworkLegacy current = networks.get(i);
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

	private Map<NetworkLegacy, String> getLinks(NServerLegacy element) {
		final Map<NetworkLegacy, String> result = new LinkedHashMap<NetworkLegacy, String>();
		for (NetworkLegacy network : networks) {
			final String s = network.getAdress(element);
			if (s != null) {
				result.put(network, s);
			}
		}
		return result;
	}

	private GridTextBlockDecorated buildGrid() {
		final GridTextBlockDecorated grid = new GridTextBlockDecorated(networks.size(), servers.size(), groups,
				networks, getSkinParam());

		for (int i = 0; i < networks.size(); i++) {
			final NetworkLegacy current = networks.get(i);
			int j = 0;
			for (Map.Entry<String, NServerLegacy> ent : servers.entrySet()) {
				final NServerLegacy square = ent.getValue();
				if (square.getMainNetwork() == current) {
					final Map<NetworkLegacy, String> conns = getLinks(square);
					final NServerLegacy sameCol = square.getSameCol();
					if (sameCol != null) {
						square.setNumCol(sameCol.getNumCol());
					} else {
						square.setNumCol(j);
					}
					grid.add(i, square.getNumCol(), square.asTextBlock(conns, networks));
				}
				if (square.hasItsOwnColumn()) {
					j++;
				}
			}
		}

		grid.checkGroups();

		return grid;
	}

	public CommandExecutionResult setProperty(String property, String value) {
		if (initDone == false) {
			return errorNoInit();
		}
		if ("address".equalsIgnoreCase(property) && currentNetwork() != null) {
			currentNetwork().setOwnAdress(value);
		}
		if ("width".equalsIgnoreCase(property) && currentNetwork() != null) {
			currentNetwork().setFullWidth("full".equalsIgnoreCase(value));
		}
		if ("color".equalsIgnoreCase(property)) {
			final HColor color = value == null ? null
					: NwGroupLegacy.colors.getColorOrWhite(getSkinParam().getThemeStyle(), value);
			if (currentGroup != null) {
				currentGroup.setColor(color);
			} else if (currentNetwork() != null) {
				currentNetwork().setColor(color);
			}
		}
		if ("description".equalsIgnoreCase(property)) {
			if (currentGroup != null) {
				currentGroup.setDescription(value);
			}
		}
		return CommandExecutionResult.ok();
	}

	@Override
	public ClockwiseTopRightBottomLeft getDefaultMargins() {
		return ClockwiseTopRightBottomLeft.none();
	}
}
