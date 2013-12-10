/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * Revision $Revision: 6711 $
 *
 */
package net.sourceforge.plantuml.svek;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.QuoteUtils;
import net.sourceforge.plantuml.ugraphic.UAntiAliasing;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.version.Version;

public class GraphvizCrash extends GraphicStrings implements IEntityImage {

	private static final UFont font = new UFont("SansSerif", Font.PLAIN, 12);

	public GraphvizCrash() {
		super(init(), font, HtmlColorUtils.BLACK, HtmlColorUtils.WHITE, UAntiAliasing.ANTI_ALIASING_ON);
	}

	private static List<String> init() {
		final List<String> strings = new ArrayList<String>();
		strings.add("An error has occured!");
		final String quote = QuoteUtils.getSomeQuote();
		strings.add("<i>" + quote);
		strings.add(" ");
		strings.add("For some reason, dot/Graphviz has crashed.");
		strings.add("This has been generated with PlantUML (" + Version.versionString() + ").");
		strings.add(" ");
		strings.add("You should send this diagram and this image to <b>plantuml@gmail.com</b> to solve this issue.");
		strings.add("You can try to turn arround this issue by simplifing your diagram.");
		return strings;
	}

}
