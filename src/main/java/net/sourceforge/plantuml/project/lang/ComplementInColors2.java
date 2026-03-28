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
package net.sourceforge.plantuml.project.lang;

import java.util.List;

import com.plantuml.ubrex.UMatcher;
import com.plantuml.ubrex.builder.UBrexConcat;
import com.plantuml.ubrex.builder.UBrexLeaf;
import com.plantuml.ubrex.builder.UBrexNamed;
import com.plantuml.ubrex.builder.UBrexOptional;
import com.plantuml.ubrex.builder.UBrexPart;

import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.project.Failable;
import net.sourceforge.plantuml.project.GanttDiagram;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexResult;

public class ComplementInColors2 implements Something<GanttDiagram> {

	public IRegex toRegex(String suffix) {
		return new RegexLeaf(2, "COMPLEMENT" + suffix, "colou?red[%s]+(?:in[%s]+)?(#?\\w+)(?:/(#?\\w+))?");
	}

	@Override
	public UBrexPart toUnicodeBracketedExpressionComplement() {
		return UBrexConcat.build( //
				new UBrexLeaf("colo〇?ured"), //
				UBrexLeaf.spaceOneOrMore(), //
				new UBrexOptional(new UBrexLeaf("in〇+〴s")), //
				new UBrexNamed("COMPLEMENT1", new UBrexLeaf("〇?#〇+〴w")), //
				new UBrexOptional(UBrexConcat.build( //
						new UBrexLeaf("/"), //
						new UBrexNamed("COMPLEMENT2", new UBrexLeaf("〇?#〇+〴w")))));
	}

	@Override
	public Failable<? extends Object> ugetMe(GanttDiagram diagram, UMatcher arg) {
		final List<String> color1 = arg.getCapture("COMPLEMENT1");
		final List<String> color2 = arg.getCapture("COMPLEMENT2");
		final HColor col1 = color1.size() == 0 ? null : diagram.getIHtmlColorSet().getColorOrWhite(color1.get(0));
		final HColor col2 = color2.size() == 0 ? null : diagram.getIHtmlColorSet().getColorOrWhite(color2.get(0));
		return Failable.ok(new CenterBorderColor(col1, col2));
	}

	public Failable<CenterBorderColor> getMe(GanttDiagram diagram, RegexResult arg, String suffix) {
		final String color1 = arg.get("COMPLEMENT" + suffix, 0);
		final String color2 = arg.get("COMPLEMENT" + suffix, 1);
		final HColor col1 = color1 == null ? null : diagram.getIHtmlColorSet().getColorOrWhite(color1);
		final HColor col2 = color2 == null ? null : diagram.getIHtmlColorSet().getColorOrWhite(color2);
		return Failable.ok(new CenterBorderColor(col1, col2));
	}

}
