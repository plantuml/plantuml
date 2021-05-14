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
package net.sourceforge.plantuml.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.graphic.FontStyle;

public class StyledString {

	private final String text;
	private final FontStyle style;

	private StyledString(String text, FontStyle style) {
		this.text = text;
		this.style = style;
	}

	@Override
	public String toString() {
		return style + "[" + text + "]";
	}

	public final String getText() {
		return text;
	}

	public final FontStyle getStyle() {
		return style;
	}

	public static List<StyledString> build(String s) {
		final List<StyledString> result = new ArrayList<>();
		while (s.length() > 0) {
			final int i1 = s.indexOf(StringUtils.BOLD_START);
			if (i1 == -1) {
				result.add(new StyledString(s, FontStyle.PLAIN));
				s = "";
				break;
			}
			final int i2 = s.indexOf(StringUtils.BOLD_END);
			if (i1 > 0)
				result.add(new StyledString(s.substring(0, i1), FontStyle.PLAIN));

			if (i2 == -1) {
				result.add(new StyledString(s.substring(i1 + 1), FontStyle.BOLD));
				s = "";
			} else {
				result.add(new StyledString(s.substring(i1 + 1, i2), FontStyle.BOLD));
				s = s.substring(i2 + 1);
			}
		}
		return Collections.unmodifiableList(result);

	}

}
