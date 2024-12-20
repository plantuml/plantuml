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
package net.sourceforge.plantuml.nwdiag.core;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sourceforge.plantuml.decoration.symbol.USymbol;
import net.sourceforge.plantuml.decoration.symbol.USymbols;
import net.sourceforge.plantuml.jaws.Jaws;
import net.sourceforge.plantuml.jaws.JawsStrange;
import net.sourceforge.plantuml.klimt.Fashion;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlockUtils;
import net.sourceforge.plantuml.nwdiag.next.NBar;
import net.sourceforge.plantuml.nwdiag.next.NServerDraw;
import net.sourceforge.plantuml.skin.ActorStyle;
import net.sourceforge.plantuml.skin.ComponentStyle;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.svek.PackageStyle;

public class NServer {

	private final Map<Network, String> connections = new LinkedHashMap<Network, String>();

	private USymbol shape = USymbols.RECTANGLE;
	private final String name;
	private String description;
	private String backcolor;
	private final NBar bar;
	private final ISkinParam skinParam;
	private String declaredAddress;

	private boolean printFirstLink = true;

	public void doNotPrintFirstLink() {
		this.printFirstLink = false;
	}

	public void connectMeIfAlone(Network network) {
		if (isAlone()) {
			connectTo(network, "");
			if (network.isVisible() == false)
				this.doNotPrintFirstLink();
		}
	}

	public boolean isAlone() {
		return connections.size() == 0;
	}

	public String someAddress() {
		if (connections.size() > 0 && connections.values().iterator().next().length() > 0)
			return connections.values().iterator().next();
		if (declaredAddress != null)
			return declaredAddress;
		return "";
	}

	public Network someNetwork() {
		if (connections.size() > 0)
			return connections.keySet().iterator().next();
		return null;
	}

	public void blankSomeAddress() {
		if (connections.size() > 0) {
			final Network it = connections.keySet().iterator().next();
			connections.put(it, "");
		}
	}

	public void learnThisAddress(String address) {
		if (address == null)
			address = "";
		for (Entry<Network, String> ent : connections.entrySet()) {
			if (ent.getValue().length() == 0) {
				connections.put(ent.getKey(), address);
				return;
			}
		}

	}

	public final boolean printFirstLink() {
		return printFirstLink;
	}

	public Network getMainNetworkNext() {
		return connections.keySet().iterator().next();
	}

	public String getAdress(Network network) {
		return connections.get(network);
	}

	@JawsStrange
	public TextBlock toTextBlock(SName sname, String s) {
		if (s == null)
			return null;

		if (s.length() == 0)
			return TextBlockUtils.empty(0, 0);

		s = s.replace(", ", "" + Jaws.BLOCK_E1_NEWLINE);
		return Display.getWithNewlines(skinParam.getPragma(), s).create(getFontConfiguration(sname),
				HorizontalAlignment.LEFT, skinParam);
	}

	private StyleSignatureBasic getStyleDefinition(SName sname) {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.nwdiagDiagram, sname);
	}

	private FontConfiguration getFontConfiguration(SName sname) {
		final StyleBuilder styleBuilder = skinParam.getCurrentStyleBuilder();
		final Style style = getStyleDefinition(sname).getMergedStyle(styleBuilder);
		return style.getFontConfiguration(skinParam.getIHtmlColorSet());
	}

	public NServerDraw getDraw(double topMargin, Map<Network, String> conns, List<Network> networks,
			ISkinParam skinParam) {
		final StyleBuilder styleBuilder = skinParam.getCurrentStyleBuilder();
		Fashion symbolContext = getStyleDefinition(SName.server).getMergedStyle(styleBuilder)
				.getSymbolContext(skinParam.getIHtmlColorSet());
		if (backcolor != null)
			try {
				final HColor back = skinParam.getIHtmlColorSet().getColor(backcolor);
				symbolContext = symbolContext.withBackColor(back);
			} catch (NoSuchColorException e) {
			}

		final TextBlock desc = toTextBlock(SName.server, getDescription());
		final TextBlock box = getShape().asSmall(TextBlockUtils.empty(0, 0), desc, TextBlockUtils.empty(0, 0),
				symbolContext, HorizontalAlignment.CENTER);
		return new NServerDraw(this, box, conns, networks, topMargin);
	}

	public void connectTo(Network network, String address) {
		if (network == null)
			throw new IllegalArgumentException();
		if (address == null)
			address = "";
		if (address.length() == 0 && connections.containsKey(network))
			return;

		connections.put(network, address);
		if (bar.getStart() == null)
			bar.addStage(network.getNstage());
		else if (this.getMainNetworkNext() != network)
			bar.addStage(network.getUp());
	}

	public void updateProperties(Map<String, String> props) {
		if (props.get("description") != null)
			this.description = props.get("description");

		if (props.get("color") != null)
			this.backcolor = props.get("color");

		if (props.get("address") != null)
			this.declaredAddress = props.get("address");

		final String shape = props.get("shape");
		if (shape != null) {
			final USymbol shapeFromString = USymbols.fromString(shape, ActorStyle.STICKMAN, ComponentStyle.RECTANGLE,
					PackageStyle.RECTANGLE);
			if (shapeFromString != null)
				this.shape = shapeFromString;
		}

	}

	@Override
	public final String toString() {
		return name;
	}

	public static NServer create(String name, ISkinParam skinParam) {
		return new NServer(name, new NBar(), skinParam);
	}

	public NServer(String name, NBar bar, ISkinParam skinParam) {
		this.description = name;
		this.name = name;
		this.bar = bar;
		this.skinParam = skinParam;
	}

	public final String getDescription() {
		return description;
	}

	public final String getName() {
		return name;
	}

	public final USymbol getShape() {
		return shape;
	}

	public final NBar getBar() {
		return bar;
	}

	public final ISkinParam getSkinParam() {
		return skinParam;
	}

}
