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
package net.sourceforge.plantuml.style;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.sourceforge.plantuml.FileSystem;
import net.sourceforge.plantuml.LineLocationImpl;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.command.BlocLines;
import net.sourceforge.plantuml.command.CommandControl;

public class StyleLoader {

	public static StyleBuilder mainStyle(SkinParam skinParam) throws IOException {
		return new StyleLoader(skinParam).loadSkin(SkinParam.DEFAULT_STYLE);
	}

	private final SkinParam skinParam;

	public StyleLoader(SkinParam skinParam) {
		this.skinParam = skinParam;
	}

	private StyleBuilder result;

	public StyleBuilder loadSkin(String filename) throws IOException {
		this.result = new StyleBuilder(skinParam);

		InputStream internalIs = null;
		File localFile = new File(filename);
		Log.info("Trying to load style " + filename);
		if (localFile.exists() == false) {
			localFile = FileSystem.getInstance().getFile(filename);
		}
		if (localFile.exists()) {
			Log.info("File found : " + localFile.getAbsolutePath());
			internalIs = new FileInputStream(localFile);
		} else {
			Log.info("File not found");
			final String res = "/skin/" + filename;
			internalIs = StyleLoader.class.getResourceAsStream(res);
		}
		if (internalIs == null) {
			return null;
		}
		final BlocLines lines2 = BlocLines.load(internalIs, new LineLocationImpl(filename, null));
		loadSkinInternal(lines2);
		return result;
	}

	private void loadSkinInternal(final BlocLines lines) {
		final CommandStyleMultilines cmd2 = new CommandStyleMultilines();
		for (int i = 0; i < lines.size(); i++) {
			final BlocLines ext1 = lines.subList(i, i + 1);
			if (cmd2.isValid(ext1) == CommandControl.OK_PARTIAL) {
				i = tryMultilines(cmd2, i, lines);
			}
		}
	}

	private int tryMultilines(CommandStyleMultilines cmd2, int i, BlocLines lines) {
		for (int j = i + 1; j <= lines.size(); j++) {
			final BlocLines ext1 = lines.subList(i, j);
			if (cmd2.isValid(ext1) == CommandControl.OK) {
				final Style newStyle = cmd2.getDeclaredStyle(ext1, result);
				this.result.put(newStyle.getStyleName(), newStyle);
				return j;
			} else if (cmd2.isValid(ext1) == CommandControl.NOT_OK) {
				return j;
			}
		}
		return i;
	}

}