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
package net.sourceforge.plantuml.tim;

import java.io.IOException;

import net.sourceforge.plantuml.log.Logme;
import net.sourceforge.plantuml.nio.PathSystem;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.theme.Theme;
import net.sourceforge.plantuml.theme.ThemeUtils;

public class EaterTheme extends Eater {
	// ::remove folder when __HAXE__
	// ::remove file when __TEAVM__
	
	private String realName;
	private String name;
	private String from;
	private TContext context;
	private final PathSystem pathSystem;

	public EaterTheme(StringLocated s, PathSystem pathSystem) {
		super(s);
		this.pathSystem = pathSystem;
	}

	@Override
	public void analyze(TContext context, TMemory memory) throws EaterException {
		skipSpaces();
		checkAndEatChar("!theme");
		skipSpaces();
		this.name = this.eatAllToEnd();

		final int x = this.name.toLowerCase().indexOf(" from ");
		if (x != -1) {
			final String fromTmp = this.name.substring(x + " from ".length()).trim();
			this.from = context.applyFunctionsAndVariables(memory, new StringLocated(fromTmp, getLineLocation()));
			this.name = this.name.substring(0, x).trim();
			this.context = context;
		}

		this.realName = context.applyFunctionsAndVariables(memory, new StringLocated(this.name, getLineLocation()));

	}

	public final Theme getTheme() throws EaterException {
		try {
			final Theme theme = ThemeUtils.loadTheme(pathSystem, realName, from, getStringLocated());
			if (theme == null) {
				final String location = (from == null) ? "" : " in " + from;
				throw new EaterException("Cannot load theme " + realName + location, getStringLocated());
			}
			return theme;
		} catch (IOException e) {
			Logme.error(e);
			throw new EaterException("Cannot load theme " + realName, getStringLocated());
		}
	}

	public String getName() {
		return name;
	}

	public PathSystem getNewImportedFiles() {
		return pathSystem;
	}

}
