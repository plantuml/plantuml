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
 *
 */
package net.sourceforge.plantuml.graphic;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.plantuml.ComponentStyle;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.skin.ActorStyle;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.svek.PackageStyle;

public abstract class USymbols {

	private USymbols() {
	}

	private static final Map<String, USymbol> all = new HashMap<String, USymbol>();

	private static USymbol record(String code, SkinParameter skinParameter, USymbol symbol) {
		all.put(StringUtils.goUpperCase(code), symbol);
		return symbol;
	}

	public final static USymbol STORAGE = record("STORAGE", SkinParameter.STORAGE, new USymbolStorage());
	public final static USymbol DATABASE = record("DATABASE", SkinParameter.DATABASE, new USymbolDatabase());
	public final static USymbol CLOUD = record("CLOUD", SkinParameter.CLOUD, new USymbolCloud());
	public final static USymbol CARD = record("CARD", SkinParameter.CARD, new USymbolCard(SkinParameter.CARD));
	public final static USymbol FRAME = record("FRAME", SkinParameter.FRAME, new USymbolFrame());
	public final static USymbol NODE = record("NODE", SkinParameter.NODE, new USymbolNode());
	public final static USymbol ARTIFACT = record("ARTIFACT", SkinParameter.ARTIFACT, new USymbolArtifact());
	public final static USymbol PACKAGE = record("PACKAGE", SkinParameter.PACKAGE,
			new USymbolFolder(SName.package_, SkinParameter.PACKAGE, true));
	public final static USymbol FOLDER = record("FOLDER", SkinParameter.FOLDER,
			new USymbolFolder(SName.folder, SkinParameter.FOLDER, false));
	public final static USymbol FILE = record("FILE", SkinParameter.FILE, new USymbolFile());
	public final static USymbol RECTANGLE = record("RECTANGLE", SkinParameter.RECTANGLE,
			new USymbolRectangle(SkinParameter.RECTANGLE));
	public final static USymbol HEXAGON = record("HEXAGON", SkinParameter.HEXAGON, new USymbolHexagon());
	public final static USymbol PERSON = record("PERSON", SkinParameter.PERSON, new USymbolPerson());
	public final static USymbol LABEL = record("LABEL", SkinParameter.LABEL, new USymbolLabel(SkinParameter.LABEL));
	public final static USymbol ARCHIMATE = record("ARCHIMATE", SkinParameter.ARCHIMATE,
			new USymbolRectangle(SkinParameter.ARCHIMATE));
	public final static USymbol COLLECTIONS = record("COLLECTIONS", SkinParameter.COLLECTIONS,
			new USymbolCollections(SkinParameter.COLLECTIONS));
	public final static USymbol AGENT = record("AGENT", SkinParameter.AGENT, new USymbolRectangle(SkinParameter.AGENT));
	public final static USymbol ACTOR_STICKMAN = record("ACTOR_STICKMAN", SkinParameter.ACTOR,
			new USymbolActor(ActorStyle.STICKMAN));
	public final static USymbol ACTOR_STICKMAN_BUSINESS = record("ACTOR_STICKMAN_BUSINESS", SkinParameter.ACTOR,
			new USymbolActor(ActorStyle.STICKMAN_BUSINESS));
	public final static USymbol ACTOR_AWESOME = record("ACTOR_AWESOME", SkinParameter.ACTOR,
			new USymbolActor(ActorStyle.AWESOME));
	public final static USymbol ACTOR_HOLLOW = record("ACTOR_HOLLOW", SkinParameter.ACTOR,
			new USymbolActor(ActorStyle.HOLLOW));
	public final static USymbol USECASE = null;
	public final static USymbol COMPONENT1 = record("COMPONENT1", SkinParameter.COMPONENT1, new USymbolComponent1());
	public final static USymbol COMPONENT2 = record("COMPONENT2", SkinParameter.COMPONENT2, new USymbolComponent2());
	public final static USymbol BOUNDARY = record("BOUNDARY", SkinParameter.BOUNDARY, new USymbolBoundary());
	public final static USymbol ENTITY_DOMAIN = record("ENTITY_DOMAIN", SkinParameter.ENTITY,
			new USymbolEntityDomain());
	public final static USymbol CONTROL = record("CONTROL", SkinParameter.CONTROL, new USymbolControl());
	public final static USymbol INTERFACE = record("INTERFACE", SkinParameter.INTERFACE, new USymbolInterface());
	public final static USymbol QUEUE = record("QUEUE", SkinParameter.QUEUE, new USymbolQueue());
	public final static USymbol STACK = record("STACK", SkinParameter.STACK, new USymbolStack());
	public final static USymbol TOGETHER = record("TOGETHER", SkinParameter.QUEUE, new USymbolTogether());

	public static USymbol fromString(String s, ActorStyle actorStyle, ComponentStyle componentStyle,
			PackageStyle packageStyle) {
		if (s == null)
			return null;

		if (s.equalsIgnoreCase("package"))
			return packageStyle.toUSymbol();

		if (s.equalsIgnoreCase("actor"))
			return actorStyle.toUSymbol();

		if (s.equalsIgnoreCase("component"))
			return componentStyle.toUSymbol();

		if (s.equalsIgnoreCase("entity"))
			return ENTITY_DOMAIN;

		if (s.equalsIgnoreCase("circle"))
			return INTERFACE;

		final USymbol result = all.get(StringUtils.goUpperCase(s.replaceAll("\\W", "")));
		return result;
	}

	public static USymbol fromString(String symbol, ISkinParam skinParam) {
		USymbol usymbol = null;
		if (symbol.equalsIgnoreCase("artifact"))
			usymbol = USymbols.ARTIFACT;
		else if (symbol.equalsIgnoreCase("folder"))
			usymbol = USymbols.FOLDER;
		else if (symbol.equalsIgnoreCase("file"))
			usymbol = USymbols.FILE;
		else if (symbol.equalsIgnoreCase("package"))
			usymbol = USymbols.PACKAGE;
		else if (symbol.equalsIgnoreCase("rectangle"))
			usymbol = USymbols.RECTANGLE;
		else if (symbol.equalsIgnoreCase("person"))
			usymbol = USymbols.PERSON;
		else if (symbol.equalsIgnoreCase("hexagon"))
			usymbol = USymbols.HEXAGON;
		else if (symbol.equalsIgnoreCase("label"))
			usymbol = USymbols.LABEL;
		else if (symbol.equalsIgnoreCase("collections"))
			usymbol = USymbols.COLLECTIONS;
		else if (symbol.equalsIgnoreCase("node"))
			usymbol = USymbols.NODE;
		else if (symbol.equalsIgnoreCase("frame"))
			usymbol = USymbols.FRAME;
		else if (symbol.equalsIgnoreCase("cloud"))
			usymbol = USymbols.CLOUD;
		else if (symbol.equalsIgnoreCase("database"))
			usymbol = USymbols.DATABASE;
		else if (symbol.equalsIgnoreCase("queue"))
			usymbol = USymbols.QUEUE;
		else if (symbol.equalsIgnoreCase("stack"))
			usymbol = USymbols.STACK;
		else if (symbol.equalsIgnoreCase("storage"))
			usymbol = USymbols.STORAGE;
		else if (symbol.equalsIgnoreCase("agent"))
			usymbol = USymbols.AGENT;
		else if (symbol.equalsIgnoreCase("actor/"))
			usymbol = USymbols.ACTOR_STICKMAN_BUSINESS;
		else if (symbol.equalsIgnoreCase("actor"))
			usymbol = skinParam.actorStyle().toUSymbol();
		else if (symbol.equalsIgnoreCase("component"))
			usymbol = skinParam.componentStyle().toUSymbol();
		else if (symbol.equalsIgnoreCase("boundary"))
			usymbol = USymbols.BOUNDARY;
		else if (symbol.equalsIgnoreCase("control"))
			usymbol = USymbols.CONTROL;
		else if (symbol.equalsIgnoreCase("entity"))
			usymbol = USymbols.ENTITY_DOMAIN;
		else if (symbol.equalsIgnoreCase("card"))
			usymbol = USymbols.CARD;
		else if (symbol.equalsIgnoreCase("interface"))
			usymbol = USymbols.INTERFACE;
		else if (symbol.equalsIgnoreCase("()"))
			usymbol = USymbols.INTERFACE;

		return usymbol;
	}

}