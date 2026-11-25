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
package net.sourceforge.plantuml.openiconic;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.PSystemSingleLineFactory;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;

public class PSystemOpenIconicFactory extends PSystemSingleLineFactory {
	

	@Override
	protected Diagram executeLine(UmlSource source, String line, PreprocessingArtifact preprocessing) {
		// Everything after the keyword, blanks removed: "openiconic  home" names "home" too,
		// and so do an indented line and a tab or a non-breaking space after the keyword
		final String argument = StringUtils.getKeywordArgument(line, "openiconic");
		if (argument == null)
			return null;

		final String iconName = StringUtils.goLowerCase(argument);
		final OpenIconic icon = OpenIconic.retrieve(iconName);
		// An unknown icon is reported on its line instead of crashing on a null icon
		if (icon == null)
			return null;

		return new PSystemOpenIconic(source, icon, 1.0, preprocessing);
	}

}
