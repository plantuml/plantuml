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
 */
package net.sourceforge.plantuml.nwdiag.core;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sourceforge.plantuml.ComponentStyle;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.SymbolContext;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.graphic.USymbols;
import net.sourceforge.plantuml.nwdiag.next.LinkedElement;
import net.sourceforge.plantuml.nwdiag.next.NBar;
import net.sourceforge.plantuml.skin.ActorStyle;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleBuilder;
import net.sourceforge.plantuml.style.StyleSignatureBasic;
import net.sourceforge.plantuml.svek.PackageStyle;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.NoSuchColorException;

public class NServer {

	private final Map<Network, String> connections = new LinkedHashMap<Network, String>();

	private USymbol shape = USymbols.RECTANGLE;
	private final String name;
	private String description;
	private String backcolor;
	private final NBar bar;

	private boolean printFirstLink = true;

	public void doNotPrintFirstLink() {
		this.printFirstLink = false;
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

	private TextBlock toTextBlock(String s, ISkinParam skinParam, SName sname) {
		if (s == null)
			return null;

		if (s.length() == 0)
			return TextBlockUtils.empty(0, 0);

		s = s.replace(", ", "\\n");
		return Display.getWithNewlines(s).create(getFontConfiguration(skinParam, sname), HorizontalAlignment.LEFT,
				skinParam);
	}

	private StyleSignatureBasic getStyleDefinition(SName sname) {
		return StyleSignatureBasic.of(SName.root, SName.element, SName.nwdiagDiagram, sname);
	}

	private FontConfiguration getFontConfiguration(ISkinParam skinParam, SName sname) {
		final StyleBuilder styleBuilder = skinParam.getCurrentStyleBuilder();
		final Style style = getStyleDefinition(sname).getMergedStyle(styleBuilder);
		return style.getFontConfiguration(skinParam.getIHtmlColorSet());
	}

	public LinkedElement getLinkedElement(double topMargin, Map<Network, String> conns, List<Network> networks,
			ISkinParam skinParam) {
		final StyleBuilder styleBuilder = skinParam.getCurrentStyleBuilder();
		SymbolContext symbolContext = getStyleDefinition(SName.server).getMergedStyle(styleBuilder)
				.getSymbolContext(skinParam.getIHtmlColorSet());
		if (backcolor != null)
			try {
				final HColor back = skinParam.getIHtmlColorSet().getColor(backcolor);
				symbolContext = symbolContext.withBackColor(back);
			} catch (NoSuchColorException e) {
			}

		final Map<Network, TextBlock> conns2 = new LinkedHashMap<Network, TextBlock>();
		for (Entry<Network, String> ent : conns.entrySet())
			conns2.put(ent.getKey(), toTextBlock(ent.getValue(), skinParam, SName.arrow));

		final TextBlock desc = toTextBlock(getDescription(), skinParam, SName.server);
		final TextBlock box = getShape().asSmall(TextBlockUtils.empty(0, 0), desc, TextBlockUtils.empty(0, 0),
				symbolContext, HorizontalAlignment.CENTER);
		return new LinkedElement(topMargin, this, box, conns2, networks);
	}

	public void connectTo(Network network) {
		connectTo(network, "");
	}

	public void connectTo(Network network, String address) {
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
		this.backcolor = props.get("color");

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

	public static NServer create(String name) {
		return new NServer(name, new NBar());
	}

	public NServer(String name, NBar bar) {
		this.description = name;
		this.name = name;
		this.bar = bar;
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

}
