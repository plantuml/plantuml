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
import net.sourceforge.plantuml.nwdiag.core.NServer;
import net.sourceforge.plantuml.nwdiag.core.NStackable;
import net.sourceforge.plantuml.nwdiag.core.Network;
import net.sourceforge.plantuml.nwdiag.core.NwGroup;
import net.sourceforge.plantuml.nwdiag.next.GridTextBlockDecorated;
import net.sourceforge.plantuml.nwdiag.next.LinkedElement;
import net.sourceforge.plantuml.nwdiag.next.NBar;
import net.sourceforge.plantuml.nwdiag.next.NPlayField;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignature;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UEmpty;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class NwDiagram extends UmlDiagram {

	private boolean initDone;
	private final Map<String, NServer> servers = new LinkedHashMap<>();
	private final List<Network> networks = new ArrayList<>();
	private final List<NwGroup> groups = new ArrayList<>();
	private NwGroup currentGroup = null;

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

	private Network lastNetwork() {
		if (networks.size() == 0) {
			return null;
		}
		return networks.get(networks.size() - 1);
	}

	private Network stackedNetwork() {
		for (NStackable element : stack)
			if (element instanceof Network)
				return (Network) element;
		return null;
	}

	private NwGroup stakedGroup() {
		for (NStackable element : stack)
			if (element instanceof NwGroup)
				return (NwGroup) element;
		return null;
	}

	private final List<NStackable> stack = new ArrayList<NStackable>();

	public CommandExecutionResult openGroup(String name) {
		if (initDone == false) {
			return errorNoInit();
		}
		for (NStackable element : stack)
			if (element instanceof NwGroup)
				return CommandExecutionResult.error("Cannot nest group");

		final NwGroup newGroup = new NwGroup(name);
		stack.add(0, newGroup);
		groups.add(newGroup);
		currentGroup = newGroup;
		return CommandExecutionResult.ok();
	}

	public CommandExecutionResult openNetwork(String name) {
		if (initDone == false) {
			return errorNoInit();
		}
		for (NStackable element : stack)
			if (element instanceof Network)
				return CommandExecutionResult.error("Cannot nest network");
		final Network network = createNetwork(name);
		stack.add(0, network);
		return CommandExecutionResult.ok();
	}

	public CommandExecutionResult closeSomething() {
		if (initDone == false) {
			return errorNoInit();
		}
		if (stack.size() > 0)
			stack.remove(0);
		this.currentGroup = null;
		return CommandExecutionResult.ok();
	}

	private Network createNetwork(String name) {
		final Network network = new Network(playField.getLast(), playField.createNewStage(), name);
		networks.add(network);
		return network;
	}

	public CommandExecutionResult link(String name1, String name2) {
		if (initDone == false) {
			return errorNoInit();
		}
		final NServer server2;
		if (lastNetwork() == null) {
			createNetwork(name1);
			server2 = new NServer(name2);
		} else {
			final NServer server1 = servers.get(name1);
			final Network network1 = createNetwork("");
			network1.goInvisible();
			if (server1 != null) {
				server1.connectTo(lastNetwork());
			}
			server2 = new NServer(name2, server1.getBar());
		}
		servers.put(name2, server2);
		server2.connectTo(lastNetwork());
		playField.addInPlayfield(server2.getBar());
		return CommandExecutionResult.ok();
	}

	public CommandExecutionResult addElement(String name, String definition) {
		if (initDone == false) {
			return errorNoInit();
		}
		if (currentGroup != null) {
			currentGroup.addName(name);
		}
		NServer server = null;
		if (lastNetwork() == null) {
			if (currentGroup != null) {
				return CommandExecutionResult.ok();
			}
			assert currentGroup == null;
			final Network network1 = createNetwork("");
			network1.goInvisible();
			server = new NServer(name);
			servers.put(name, server);
			server.doNotPrintFirstLink();
		} else {
			server = servers.get(name);
			if (server == null) {
				server = new NServer(name);
				servers.put(name, server);
			}
		}
		final Map<String, String> props = toSet(definition);
		server.connectTo(lastNetwork(), props.get("address"));
		server.updateProperties(props);
		playField.addInPlayfield(server.getBar());
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

	private StyleSignature getStyleDefinitionNetwork(SName sname) {
		return StyleSignature.of(SName.root, SName.element, SName.nwdiagDiagram, sname);
	}

	private TextBlock toTextBlockForNetworkName(String name, String s) {
		if (s != null) {
			name += "\\n" + s;
		}
		final StyleBuilder styleBuilder = getSkinParam().getCurrentStyleBuilder();
		final Style style = getStyleDefinitionNetwork(SName.network).getMergedStyle(styleBuilder);
		final FontConfiguration fontConfiguration = style.getFontConfiguration(getSkinParam().getThemeStyle(),
				getSkinParam().getIHtmlColorSet());
		return Display.getWithNewlines(name).create(fontConfiguration, HorizontalAlignment.RIGHT,
				new SpriteContainerEmpty());
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

		double deltaX = 0;
		double deltaY = 0;

		final GridTextBlockDecorated grid = buildGrid(stringBounder);

		for (int i = 0; i < networks.size(); i++) {
			final Network current = networks.get(i);
			final String address = current.getOwnAdress();
			final TextBlock desc = toTextBlockForNetworkName(current.getDisplayName(), address);
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
			final TextBlock desc = toTextBlockForNetworkName(current.getDisplayName(), address);
			final Dimension2D dim = desc.calculateDimension(stringBounder);
			desc.drawU(ug.apply(new UTranslate(deltaX - dim.getWidth(), y)));

			y += grid.lineHeight(stringBounder, i);
		}
		deltaX += 5;

		grid.drawU(ug.apply(new UTranslate(deltaX, deltaY)));
		final Dimension2D dimGrid = grid.calculateDimension(stringBounder);

		ug.apply(new UTranslate(dimGrid.getWidth() + deltaX + margin, dimGrid.getHeight() + deltaY + margin))
				.draw(new UEmpty(1, 1));

	}

	private Map<Network, String> getLinks(NServer element) {
		final Map<Network, String> result = new LinkedHashMap<>();
		for (Network network : networks) {
			final String s = element.getAdress(network);
			if (s != null) {
				result.put(network, s);
			}
		}
		return result;
	}

	private GridTextBlockDecorated buildGrid(StringBounder stringBounder) {

		playField.fixGroups(groups, servers.values());

		final GridTextBlockDecorated grid = new GridTextBlockDecorated(networks.size(), servers.size(), groups,
				networks, getSkinParam());

		final Map<NBar, Integer> layout = playField.doLayout();
		for (int i = 0; i < networks.size(); i++) {
			final Network current = networks.get(i);
			for (Map.Entry<String, NServer> ent : servers.entrySet()) {
				final NServer server = ent.getValue();
				if (server.getMainNetworkNext() == current) {
					final Map<Network, String> conns = getLinks(server);
					final int col = layout.get(server.getBar());
					double topMargin = LinkedElement.MAGIC;
					NwGroup group = getGroupOf(server);
					if (group != null)
						topMargin += group.getTopHeaderHeight(stringBounder, getSkinParam());
					grid.add(i, col, server.getLinkedElement(topMargin, conns, networks, getSkinParam()));
				}
			}
		}

		return grid;
	}

	private NwGroup getGroupOf(NServer server) {
		for (NwGroup group : groups) {
			if (group.contains(server)) {
				return group;
			}
		}
		return null;
	}

	public CommandExecutionResult setProperty(String property, String value) {
		if (initDone == false) {
			return errorNoInit();
		}
		if ("address".equalsIgnoreCase(property) && lastNetwork() != null) {
			lastNetwork().setOwnAdress(value);
		}
		if ("width".equalsIgnoreCase(property) && lastNetwork() != null) {
			lastNetwork().setFullWidth("full".equalsIgnoreCase(value));
		}
		if ("color".equalsIgnoreCase(property)) {
			final HColor color = value == null ? null
					: getSkinParam().getIHtmlColorSet().getColorOrWhite(getSkinParam().getThemeStyle(), value);
			if (currentGroup != null) {
				currentGroup.setColor(color);
			} else if (lastNetwork() != null) {
				lastNetwork().setColor(color);
			}
		}
		if ("description".equalsIgnoreCase(property)) {
			if (currentGroup == null) {
				lastNetwork().setDescription(value);
			} else {
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
