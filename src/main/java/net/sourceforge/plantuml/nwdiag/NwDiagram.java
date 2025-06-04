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
package net.sourceforge.plantuml.nwdiag;

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
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.jaws.Jaws;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.klimt.shape.UDrawable;
import net.sourceforge.plantuml.klimt.shape.UEmpty;
import net.sourceforge.plantuml.klimt.sprite.SpriteContainerEmpty;
import net.sourceforge.plantuml.nwdiag.core.NServer;
import net.sourceforge.plantuml.nwdiag.core.NStackable;
import net.sourceforge.plantuml.nwdiag.core.Network;
import net.sourceforge.plantuml.nwdiag.core.NwGroup;
import net.sourceforge.plantuml.nwdiag.next.GridTextBlockDecorated;
import net.sourceforge.plantuml.nwdiag.next.NBar;
import net.sourceforge.plantuml.nwdiag.next.NPlayField;
import net.sourceforge.plantuml.nwdiag.next.NServerDraw;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.skin.UmlDiagramType;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

public class NwDiagram extends UmlDiagram {

	private boolean initDone;
	private final Map<String, NServer> servers = new LinkedHashMap<>();
	private final List<Network> networks = new ArrayList<>();
	private final List<NwGroup> groups = new ArrayList<>();
	private final List<NStackable> stack = new ArrayList<NStackable>();

	private final NPlayField playField = new NPlayField();

	public DiagramDescription getDescription() {
		return new DiagramDescription("(Nwdiag)");
	}

	public NwDiagram(UmlSource source, PreprocessingArtifact preprocessing) {
		super(source, UmlDiagramType.NWDIAG, null, preprocessing);
	}

	public void init() {
		initDone = true;
	}

	private Network currentNetwork() {
		for (int i = 0; i < stack.size(); i++)
			if (stack.get(i) instanceof Network)
				return (Network) stack.get(i);

		return null;
	}

	private NwGroup currentGroup() {
		if (stack.size() > 0 && stack.get(0) instanceof NwGroup)
			return (NwGroup) stack.get(0);
		return null;
	}

	@Override
	public void makeDiagramReady() {
		super.makeDiagramReady();
		if (networks.size() == 0)
			createNetwork("").goInvisible();
		for (NServer server : servers.values()) {
			server.connectMeIfAlone(networks.get(0));
			playField.addInPlayfield(server.getBar());
		}
	}

	public CommandExecutionResult openGroup(String name) {
		if (initDone == false)
			return errorNoInit();

		for (NStackable element : stack)
			if (element instanceof NwGroup)
				return CommandExecutionResult.error("Cannot nest group");

		final NwGroup newGroup = new NwGroup(name);
		stack.add(0, newGroup);
		groups.add(newGroup);
		return CommandExecutionResult.ok();
	}

	public CommandExecutionResult openNetwork(String name) {
		if (initDone == false)
			return errorNoInit();

		if (currentGroup() != null)
			return CommandExecutionResult.error("Cannot open network in a group");

		for (NStackable element : stack)
			if (element instanceof Network)
				return CommandExecutionResult.error("Cannot nest network");

		if (networks.size() == 0 && groups.size() == 0)
			eventuallyConnectAllStandaloneServersToHiddenNetwork();

		final Network network = createNetwork(name);
		stack.add(0, network);
		return CommandExecutionResult.ok();
	}

	private void eventuallyConnectAllStandaloneServersToHiddenNetwork() {
		Network first = null;
		for (NServer server : servers.values())
			if (server.isAlone()) {
				if (first == null) {
					first = createNetwork("");
					first.goInvisible();
				}
				server.connectMeIfAlone(first);
			}
	}

	public CommandExecutionResult closeSomething() {
		if (initDone == false)
			return errorNoInit();

		if (stack.size() > 0)
			stack.remove(0);

		return CommandExecutionResult.ok();
	}

	private Network createNetwork(String name) {
		final Network network = new Network(playField.getLast(), playField.createNewStage(), name);
		networks.add(network);
		return network;
	}

	public CommandExecutionResult link(String name1, String name2) {
		if (initDone == false)
			return errorNoInit();

		NServer server1 = servers.get(name1);
		if (server1 == null) {
			if (networks.size() == 0)
				return veryFirstLink(name1, name2);
			return CommandExecutionResult.error("what about " + name1);
		}

		if (server1.isAlone()) {
			if (networks.size() == 0)
				createNetwork("").goInvisible();
			server1.connectMeIfAlone(networks.get(0));
		}

		final Network tmp1 = server1.getMainNetworkNext();
		final Network justAfter = justAfter(tmp1);

		final Network network;
		if (justAfter != null && justAfter.isVisible() == false)
			network = justAfter;
		else
			network = createNetwork("");

		NServer server2 = servers.get(name2);

		network.goInvisible();
		if (server2 == null) {
			server2 = new NServer(name2, server1.getBar(), getSkinParam());
			servers.put(name2, server2);
			server1.connectTo(network, "");
			server2.connectTo(network, "");
		} else {
			server1.blankSomeAddress();
			server1.connectTo(network, server1.someAddress());
			server2.connectTo(network, server2.someAddress());

		}
		playField.addInPlayfield(server2.getBar());

		return CommandExecutionResult.ok();
	}

	private Network justAfter(Network n) {
		final int x = networks.indexOf(n);
		if (x != -1 && x < networks.size() - 1)
			return networks.get(x + 1);
		return null;
	}

	private CommandExecutionResult veryFirstLink(String name1, String name2) {
		Network network = createNetwork(name1);
		NServer server2 = NServer.create(name2, getSkinParam());
		servers.put(name2, server2);
		server2.connectTo(network, "");
		playField.addInPlayfield(server2.getBar());
		return CommandExecutionResult.ok();
	}

	public CommandExecutionResult addElement(String name, String definition) {
		if (initDone == false)
			return errorNoInit();

		final Map<String, String> props = toSet(definition);
		NServer server = servers.get(name);
		if (server == null) {
			server = NServer.create(name, getSkinParam());
			servers.put(name, server);
		}

		if (currentGroup() != null) {
			if (alreadyInSomeGroup(name))
				return CommandExecutionResult.error("Element already in another group.");

			currentGroup().addName(name);
			if (currentNetwork() == null) {
				server.updateProperties(props);
				return CommandExecutionResult.ok();
			}
		}

		if (networks.size() == 0 || currentNetwork() == null) {
			server.updateProperties(props);
			server.learnThisAddress(props.get("address"));
			return CommandExecutionResult.ok();

		}

		if (networks.size() == 0) {
			final Network network = createNetwork("");
			network.goInvisible();
			server.connectTo(network, props.get("address"));
			playField.addInPlayfield(server.getBar());
			server.doNotPrintFirstLink();
		} else {
			/*
			 * if (networks.size() == 1) server.connectTo(networks.get(0),
			 * props.get("address")); else
			 */
			if (currentNetwork() != null) {
				server.connectTo(currentNetwork(), props.get("address"));
				playField.addInPlayfield(server.getBar());
			} else {
				server.learnThisAddress(props.get("address"));
			}
		}
		server.updateProperties(props);
		return CommandExecutionResult.ok();
	}

	private boolean alreadyInSomeGroup(String name) {
		for (NwGroup g : groups)
			if (g.names().contains(name))
				return true;

		return false;
	}

	private CommandExecutionResult errorNoInit() {
		return CommandExecutionResult.error("Maybe you forget 'nwdiag {' in your diagram ?");
	}

	private static final Pattern p = Pattern.compile("\\s*(\\w+)\\s*=\\s*(\"([^\"]*)\"|[^\\s,]+)");
	
	private Map<String, String> toSet(String definition) {
		final Map<String, String> result = new HashMap<String, String>();
		if (definition == null)
			return result;

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

		return createImageBuilder(fileFormatOption).drawable(getTextMainBlock(fileFormatOption)).write(os);
	}

	@Override
	protected TextBlock getTextMainBlock(FileFormatOption fileFormatOption) {
		return new AbstractTextBlock() {
			public void drawU(UGraphic ug) {
				drawMe(ug);
			}

			public XDimension2D calculateDimension(StringBounder stringBounder) {
				return getTotalDimension(stringBounder);
			}

		};
	}

	private StyleSignatureBasic getStyleDefinitionNetwork(SName sname) {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.nwdiagDiagram, sname);
	}

	private TextBlock toTextBlockForNetworkName(String name, String s) {
		if (s != null)
			name += "" + Jaws.BLOCK_E1_NEWLINE + s;

		final StyleBuilder styleBuilder = getSkinParam().getCurrentStyleBuilder();
		final Style style = getStyleDefinitionNetwork(SName.network).getMergedStyle(styleBuilder);
		final FontConfiguration fontConfiguration = style.getFontConfiguration(getSkinParam().getIHtmlColorSet());
		return Display.getWithNewlines(getPragma(), name).create(fontConfiguration, HorizontalAlignment.RIGHT,
				new SpriteContainerEmpty());
	}

	private XDimension2D getTotalDimension(StringBounder stringBounder) {
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
			final XDimension2D dim = desc.calculateDimension(stringBounder);
			if (i == 0)
				deltaY = (dim.getHeight() - GridTextBlockDecorated.NETWORK_THIN) / 2;

			deltaX = Math.max(deltaX, dim.getWidth());
		}
		double y = 0;
		for (int i = 0; i < networks.size(); i++) {
			final Network current = networks.get(i);
			final String address = current.getOwnAdress();
			final TextBlock desc = toTextBlockForNetworkName(current.getDisplayName(), address);
			final XDimension2D dim = desc.calculateDimension(stringBounder);
			desc.drawU(ug.apply(new UTranslate(deltaX - dim.getWidth(), y)));

			y += grid.lineHeight(stringBounder, i);
		}
		deltaX += 5;

		grid.drawU(ug.apply(new UTranslate(deltaX, deltaY)));
		final XDimension2D dimGrid = grid.calculateDimension(stringBounder);

		ug.apply(new UTranslate(dimGrid.getWidth() + deltaX + margin, dimGrid.getHeight() + deltaY + margin))
				.draw(new UEmpty(1, 1));

	}

	private Map<Network, String> getLinks(NServer element) {
		final Map<Network, String> result = new LinkedHashMap<>();
		for (Network network : networks) {
			final String s = element.getAdress(network);
			if (s != null)
				result.put(network, s);

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
					final Integer col = layout.get(server.getBar());
					if (col == null)
						continue;
					double topMargin = NServerDraw.MAGIC;
					NwGroup group = getGroupOf(server);
					if (group != null)
						topMargin += group.getTopHeaderHeight(stringBounder, getSkinParam());
					grid.add(i, col, server.getDraw(topMargin, conns, networks, getSkinParam()));
				}
			}
		}

		return grid;
	}

	private NwGroup getGroupOf(NServer server) {
		for (NwGroup group : groups)
			if (group.contains(server))
				return group;

		return null;
	}

	public CommandExecutionResult setProperty(String property, String value) {
		if (initDone == false)
			return errorNoInit();

		if ("address".equalsIgnoreCase(property) && currentNetwork() != null)
			currentNetwork().setOwnAdress(value);

		if ("width".equalsIgnoreCase(property) && currentNetwork() != null)
			currentNetwork().setFullWidth("full".equalsIgnoreCase(value));

		if ("color".equalsIgnoreCase(property)) {
			final HColor color = value == null ? null : getSkinParam().getIHtmlColorSet().getColorOrWhite(value);
			if (currentGroup() != null)
				currentGroup().setColor(color);
			else if (currentNetwork() != null)
				currentNetwork().setColor(color);

		}
		if ("description".equalsIgnoreCase(property))
			if (currentGroup() == null)
				currentNetwork().setDescription(value);
			else
				currentGroup().setDescription(value);

		return CommandExecutionResult.ok();
	}

	@Override
	public ClockwiseTopRightBottomLeft getDefaultMargins() {
		return ClockwiseTopRightBottomLeft.none();
	}
}
