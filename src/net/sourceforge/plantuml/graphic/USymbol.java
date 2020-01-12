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
 *
 */
package net.sourceforge.plantuml.graphic;

import java.awt.geom.Dimension2D;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.creole.Stencil;
import net.sourceforge.plantuml.skin.ActorStyle;

public abstract class USymbol {

	private static final Map<String, USymbol> all = new HashMap<String, USymbol>();

	public final static USymbol STORAGE = record("STORAGE", SkinParameter.STORAGE, new USymbolStorage());
	public final static USymbol DATABASE = record("DATABASE", SkinParameter.DATABASE, new USymbolDatabase());
	public final static USymbol CLOUD = record("CLOUD", SkinParameter.CLOUD, new USymbolCloud());
	public final static USymbol CARD = record("CARD", SkinParameter.CARD, new USymbolCard(SkinParameter.CARD));
	public final static USymbol FRAME = record("FRAME", SkinParameter.FRAME, new USymbolFrame());
	public final static USymbol NODE = record("NODE", SkinParameter.NODE, new USymbolNode());
	public final static USymbol ARTIFACT = record("ARTIFACT", SkinParameter.ARTIFACT, new USymbolArtifact());
	public final static USymbol PACKAGE = record("PACKAGE", SkinParameter.PACKAGE, new USymbolFolder(
			SkinParameter.PACKAGE, true));
	public final static USymbol FOLDER = record("FOLDER", SkinParameter.FOLDER, new USymbolFolder(SkinParameter.FOLDER,
			false));
	public final static USymbol FILE = record("FILE", SkinParameter.FILE, new USymbolFile());
	public final static USymbol RECTANGLE = record("RECTANGLE", SkinParameter.RECTANGLE, new USymbolRect(
			SkinParameter.RECTANGLE));
	public final static USymbol LABEL = record("LABEL", SkinParameter.RECTANGLE, new USymbolLabel(
			SkinParameter.RECTANGLE));
	public final static USymbol ARCHIMATE = record("ARCHIMATE", SkinParameter.ARCHIMATE, new USymbolRect(
			SkinParameter.ARCHIMATE));
	public final static USymbol COLLECTIONS = record("COLLECTIONS", SkinParameter.COLLECTIONS, new USymbolCollections(
			SkinParameter.RECTANGLE));
	public final static USymbol AGENT = record("AGENT", SkinParameter.AGENT, new USymbolRect(SkinParameter.AGENT));
	public final static USymbol ACTOR_STICKMAN = record("ACTOR_STICKMAN", SkinParameter.ACTOR, new USymbolActor(
			ActorStyle.STICKMAN));
	public final static USymbol ACTOR_AWESOME = record("ACTOR_AWESOME", SkinParameter.ACTOR, new USymbolActor(
			ActorStyle.AWESOME));
	public final static USymbol USECASE = null;
	public final static USymbol COMPONENT1 = record("COMPONENT1", SkinParameter.COMPONENT1, new USymbolComponent1());
	public final static USymbol COMPONENT2 = record("COMPONENT2", SkinParameter.COMPONENT2, new USymbolComponent2());
	public final static USymbol BOUNDARY = record("BOUNDARY", SkinParameter.BOUNDARY, new USymbolBoundary());
	public final static USymbol ENTITY_DOMAIN = record("ENTITY_DOMAIN", SkinParameter.ENTITY,
			new USymbolEntityDomain(2));
	public final static USymbol CONTROL = record("CONTROL", SkinParameter.CONTROL, new USymbolControl(2));
	public final static USymbol INTERFACE = record("INTERFACE", SkinParameter.INTERFACE, new USymbolInterface());
	public final static USymbol QUEUE = record("QUEUE", SkinParameter.QUEUE, new USymbolQueue());
	public final static USymbol STACK = record("STACK", SkinParameter.STACK, new USymbolStack());
	public final static USymbol TOGETHER = record("TOGETHER", SkinParameter.QUEUE, new USymbolTogether());

	abstract public SkinParameter getSkinParameter();

	// public USymbol withStereoAlignment(HorizontalAlignment alignment) {
	// return this;
	// }

	public FontParam getFontParam() {
		return getSkinParameter().getFontParam();
	}

	public FontParam getFontParamStereotype() {
		return getSkinParameter().getFontParamStereotype();

	}

	public ColorParam getColorParamBack() {
		return getSkinParameter().getColorParamBack();
	}

	public ColorParam getColorParamBorder() {
		return getSkinParameter().getColorParamBorder();
	}

	public static USymbol getFromString(String s, ActorStyle actorStyle) {
		if (s == null) {
			return null;
		}
		if (s.equalsIgnoreCase("actor")) {
			return actorStyle.getUSymbol();
		}
		final USymbol result = all.get(StringUtils.goUpperCase(s.replaceAll("\\W", "")));
		if (result == null) {
			if (s.equalsIgnoreCase("component")) {
				return COMPONENT2;
			}
			if (s.equalsIgnoreCase("entity")) {
				return ENTITY_DOMAIN;
			}
			return null;
		}
		return result;
	}

	private static USymbol record(String code, SkinParameter skinParameter, USymbol symbol) {
		all.put(StringUtils.goUpperCase(code), symbol);
		return symbol;
	}

	public abstract TextBlock asSmall(TextBlock name, TextBlock label, TextBlock stereotype,
			SymbolContext symbolContext, HorizontalAlignment stereoAlignment);

	public abstract TextBlock asBig(TextBlock label, HorizontalAlignment labelAlignment, TextBlock stereotype,
			double width, double height, SymbolContext symbolContext, HorizontalAlignment stereoAlignment);

	static class Margin {
		private final double x1;
		private final double x2;
		private final double y1;
		private final double y2;

		Margin(double x1, double x2, double y1, double y2) {
			this.x1 = x1;
			this.x2 = x2;
			this.y1 = y1;
			this.y2 = y2;
		}

		double getWidth() {
			return x1 + x2;
		}

		double getHeight() {
			return y1 + y2;
		}

		public Dimension2D addDimension(Dimension2D dim) {
			return new Dimension2DDouble(dim.getWidth() + x1 + x2, dim.getHeight() + y1 + y2);
		}

		public double getX1() {
			return x1;
		}

		public double getY1() {
			return y1;
		}
	}

	public boolean manageHorizontalLine() {
		return false;
	}

	public int suppHeightBecauseOfShape() {
		return 0;
	}

	public int suppWidthBecauseOfShape() {
		return 0;
	}

	final Stencil getRectangleStencil(final Dimension2D dim) {
		return new Stencil() {
			public double getStartingX(StringBounder stringBounder, double y) {
				return 0;
			}

			public double getEndingX(StringBounder stringBounder, double y) {
				return dim.getWidth();
			}
		};
	}

	public static USymbol getFromString(String symbol, ISkinParam skinParam) {
		USymbol usymbol = null;
		if (symbol.equalsIgnoreCase("artifact")) {
			usymbol = USymbol.ARTIFACT;
		} else if (symbol.equalsIgnoreCase("folder")) {
			usymbol = USymbol.FOLDER;
		} else if (symbol.equalsIgnoreCase("file")) {
			usymbol = USymbol.FILE;
		} else if (symbol.equalsIgnoreCase("package")) {
			usymbol = USymbol.PACKAGE;
		} else if (symbol.equalsIgnoreCase("rectangle")) {
			usymbol = USymbol.RECTANGLE;
		} else if (symbol.equalsIgnoreCase("label")) {
			usymbol = USymbol.LABEL;
		} else if (symbol.equalsIgnoreCase("collections")) {
			usymbol = USymbol.COLLECTIONS;
		} else if (symbol.equalsIgnoreCase("node")) {
			usymbol = USymbol.NODE;
		} else if (symbol.equalsIgnoreCase("frame")) {
			usymbol = USymbol.FRAME;
		} else if (symbol.equalsIgnoreCase("cloud")) {
			usymbol = USymbol.CLOUD;
		} else if (symbol.equalsIgnoreCase("database")) {
			usymbol = USymbol.DATABASE;
		} else if (symbol.equalsIgnoreCase("queue")) {
			usymbol = USymbol.QUEUE;
		} else if (symbol.equalsIgnoreCase("stack")) {
			usymbol = USymbol.STACK;
		} else if (symbol.equalsIgnoreCase("storage")) {
			usymbol = USymbol.STORAGE;
		} else if (symbol.equalsIgnoreCase("agent")) {
			usymbol = USymbol.AGENT;
		} else if (symbol.equalsIgnoreCase("actor")) {
			usymbol = skinParam.getActorStyle().getUSymbol();
		} else if (symbol.equalsIgnoreCase("component")) {
			usymbol = skinParam.useUml2ForComponent() ? USymbol.COMPONENT2 : USymbol.COMPONENT1;
		} else if (symbol.equalsIgnoreCase("boundary")) {
			usymbol = USymbol.BOUNDARY;
		} else if (symbol.equalsIgnoreCase("control")) {
			usymbol = USymbol.CONTROL;
		} else if (symbol.equalsIgnoreCase("entity")) {
			usymbol = USymbol.ENTITY_DOMAIN;
		} else if (symbol.equalsIgnoreCase("card")) {
			usymbol = USymbol.CARD;
		} else if (symbol.equalsIgnoreCase("interface")) {
			usymbol = USymbol.INTERFACE;
		} else if (symbol.equalsIgnoreCase("()")) {
			usymbol = USymbol.INTERFACE;
		}
		return usymbol;
	}
}