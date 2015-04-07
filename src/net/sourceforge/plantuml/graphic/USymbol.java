/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 8066 $
 *
 */
package net.sourceforge.plantuml.graphic;

import java.awt.geom.Dimension2D;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.StringUtils;

public abstract class USymbol {

	private static final Map<String, USymbol> all = new HashMap<String, USymbol>();

	public final static USymbol STORAGE = record("STORAGE", new USymbolStorage());
	public final static USymbol DATABASE = record("DATABASE", new USymbolDatabase());
	public final static USymbol CLOUD = record("CLOUD", new USymbolCloud());
	public final static USymbol CARD = record("CARD", new USymbolCard(ColorParam.rectangleBackground,
			ColorParam.rectangleBorder, FontParam.RECTANGLE, FontParam.RECTANGLE_STEREOTYPE));
	public final static USymbol FRAME = record("FRAME", new USymbolFrame());
	public final static USymbol NODE = record("NODE", new USymbolNode());
	public final static USymbol ARTIFACT = record("ARTIFACT", new USymbolArtifact());
	public final static USymbol PACKAGE = record("PACKAGE", new USymbolFolder(ColorParam.packageBackground,
			ColorParam.packageBorder));
	public final static USymbol FOLDER = record("FOLDER", new USymbolFolder(ColorParam.folderBackground,
			ColorParam.folderBorder));
	public final static USymbol RECTANGLE = record("RECTANGLE", new USymbolRect(ColorParam.rectangleBackground,
			ColorParam.rectangleBorder, FontParam.RECTANGLE, FontParam.RECTANGLE_STEREOTYPE));
	public final static USymbol AGENT = record("AGENT", new USymbolRect(ColorParam.agentBackground,
			ColorParam.agentBorder, FontParam.AGENT, FontParam.AGENT_STEREOTYPE));
	public final static USymbol ACTOR = record("ACTOR", new USymbolActor());
	public final static USymbol USECASE = null;
	public final static USymbol COMPONENT1 = record("COMPONENT1", new USymbolComponent1());
	public final static USymbol COMPONENT2 = record("COMPONENT2", new USymbolComponent2());
	public final static USymbol BOUNDARY = record("BOUNDARY", new USymbolBoundary());
	public final static USymbol ENTITY_DOMAIN = record("ENTITY_DOMAIN", new USymbolEntityDomain(2));
	public final static USymbol CONTROL = record("CONTROL", new USymbolControl(2));
	public final static USymbol INTERFACE = record("INTERFACE", new USymbolInterface());

	private final ColorParam colorParamBorder;
	private final ColorParam colorParamBack;
	private final FontParam fontParam;
	private final FontParam fontParamStereotype;

	public USymbol(ColorParam colorParamBack, ColorParam colorParamBorder, FontParam fontParam,
			FontParam fontParamStereotype) {
		this.colorParamBack = colorParamBack;
		this.colorParamBorder = colorParamBorder;
		this.fontParam = fontParam;
		this.fontParamStereotype = fontParamStereotype;
	}

	public FontParam getFontParam() {
		return fontParam;

	}

	public FontParam getFontParamStereotype() {
		return fontParamStereotype;

	}

	public ColorParam getColorParamBack() {
		return colorParamBack;
	}

	public ColorParam getColorParamBorder() {
		return colorParamBorder;
	}

	public static USymbol getFromString(String s) {
		final USymbol result = all.get(StringUtils.goUpperCase(s));
		if (result == null) {
			if (s.equalsIgnoreCase("component")) {
				return COMPONENT2;
			}
			throw new IllegalArgumentException("s=" + s);
		}
		return result;
	}

	private static USymbol record(String code, USymbol symbol) {
		all.put(StringUtils.goUpperCase(code), symbol);
		return symbol;
	}

	public abstract TextBlock asSmall(TextBlock label, TextBlock stereotype, SymbolContext symbolContext);

	public abstract TextBlock asBig(TextBlock label, TextBlock stereotype, double width, double height,
			SymbolContext symbolContext);

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

}