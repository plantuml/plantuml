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
package net.sourceforge.plantuml.decoration.symbol;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.skin.ActorStyle;
import net.sourceforge.plantuml.skin.ComponentStyle;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.svek.PackageStyle;

public abstract class USymbols {

	private USymbols() {
	}

	private static final Map<String, USymbol> all = new HashMap<String, USymbol>();

	private static USymbol record(String code, USymbol symbol) {
		all.put(StringUtils.goUpperCase(code), symbol);
		return symbol;
	}

	public final static USymbol STORAGE = record("STORAGE", new USymbolStorage());
	public final static USymbol DATABASE = record("DATABASE", new USymbolDatabase());
	public final static USymbol CLOUD = record("CLOUD", new USymbolCloud());
	public final static USymbol CARD = record("CARD", new USymbolCard());
	public final static USymbol FRAME = record("FRAME", new USymbolFrame(SName.frame));
	public final static USymbol PARTITION = record("PARTITION", new USymbolFrame(SName.partition));
	public final static USymbol GROUP = record("GROUP", new USymbolFrame(SName.group));
	public final static USymbol NODE = record("NODE", new USymbolNode());
	public final static USymbol ARTIFACT = record("ARTIFACT", new USymbolArtifact());
	public final static USymbol PACKAGE = record("PACKAGE", new USymbolFolder(SName.package_, true));
	public final static USymbol FOLDER = record("FOLDER", new USymbolFolder(SName.folder, false));
	public final static USymbol FILE = record("FILE", new USymbolFile());
	public final static USymbol RECTANGLE = record("RECTANGLE", new USymbolRectangle(SName.rectangle));
	public final static USymbol ACTION = record("ACTION", new USymbolAction(SName.action));
	public final static USymbol PROCESS = record("PROCESS", new USymbolProcess(SName.process));
	public final static USymbol HEXAGON = record("HEXAGON", new USymbolHexagon());
	public final static USymbol PERSON = record("PERSON", new USymbolPerson());
	public final static USymbol LABEL = record("LABEL", new USymbolLabel());
	public final static USymbol ARCHIMATE = record("ARCHIMATE", new USymbolRectangle(SName.archimate));
	public final static USymbol COLLECTIONS = record("COLLECTIONS", new USymbolCollections());
	public final static USymbol AGENT = record("AGENT", new USymbolRectangle(SName.agent));
	public final static USymbol ACTOR_STICKMAN = record("ACTOR_STICKMAN", new USymbolActor(ActorStyle.STICKMAN));
	public final static USymbol ACTOR_STICKMAN_BUSINESS = record("ACTOR_STICKMAN_BUSINESS", new USymbolActorBusiness());
	public final static USymbol ACTOR_AWESOME = record("ACTOR_AWESOME", new USymbolActor(ActorStyle.AWESOME));
	public final static USymbol ACTOR_HOLLOW = record("ACTOR_HOLLOW", new USymbolActor(ActorStyle.HOLLOW));
	public final static USymbol USECASE = null;
	public final static USymbol COMPONENT1 = record("COMPONENT1", new USymbolComponent1());
	public final static USymbol COMPONENT2 = record("COMPONENT2", new USymbolComponent2());
	public final static USymbol COMPONENT_RECTANGLE = record("COMPONENT_RECTANGLE",
			new USymbolRectangle(SName.component));
	public final static USymbol BOUNDARY = record("BOUNDARY", new USymbolBoundary());
	public final static USymbol ENTITY_DOMAIN = record("ENTITY_DOMAIN", new USymbolEntityDomain());
	public final static USymbol CONTROL = record("CONTROL", new USymbolControl());
	public final static USymbol INTERFACE = record("INTERFACE", new USymbolInterface());
	public final static USymbol QUEUE = record("QUEUE", new USymbolQueue());
	public final static USymbol STACK = record("STACK", new USymbolStack());

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
		else if (symbol.equalsIgnoreCase("action"))
			usymbol = USymbols.ACTION;
		else if (symbol.equalsIgnoreCase("process"))
			usymbol = USymbols.PROCESS;
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