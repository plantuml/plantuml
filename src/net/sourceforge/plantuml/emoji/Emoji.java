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
package net.sourceforge.plantuml.emoji;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sourceforge.plantuml.emoji.data.Dummy;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.log.Logme;

// Emojji from https://twemoji.twitter.com/
// Shorcut from https://api.github.com/emojis

// ::uncomment when __CORE__
//import static com.plantuml.api.cheerpj.StaticMemory.cheerpjPath;
//import java.io.FileInputStream;
// ::done

public class Emoji {
	// ::remove folder when __HAXE__
	private final static Map<String, Emoji> ALL = new HashMap<>();

	static {
		final InputStream is = Dummy.class.getResourceAsStream("emoji.txt");
		if (is != null)
			try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
				String s = null;
				while ((s = br.readLine()) != null)
					new Emoji(s);

			} catch (IOException e) {
				Logme.error(e);
			}
	}

	public static Map<String, Emoji> getAll() {
		return Collections.unmodifiableMap(new TreeMap<>(ALL));
	}

	private SvgNanoParser nano;

	private final String unicode;
	private final String shortcut;

	private Emoji(String unicode) {
		final int x = unicode.indexOf(';');
		if (x == -1) {
			this.shortcut = null;
		} else {
			this.shortcut = unicode.substring(x + 1);
			ALL.put(this.shortcut, this);
			unicode = unicode.substring(0, x);
		}
		this.unicode = unicode;
		ALL.put(unicode, this);
	}

	public static String pattern() {
		final StringBuilder sb = new StringBuilder("\\<(#\\w+)?:(");
		for (String s : ALL.keySet()) {
			if (sb.toString().endsWith("(") == false)
				sb.append("|");
			sb.append(s);
		}
		sb.append("):\\>");
		return sb.toString();
	}

	public static Emoji retrieve(String name) {
		return ALL.get(name.toLowerCase());
	}

	private synchronized void loadIfNeed() throws IOException {
		if (nano != null)
			return;

		final List<String> data = new ArrayList<String>();
		// ::uncomment when __CORE__
//		final String fullpath = cheerpjPath + "emoji/" + unicode + ".svg";
//		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fullpath)))) {
		// ::done
		// ::comment when __CORE__
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(Dummy.class.getResourceAsStream(unicode + ".svg")))) {
			// ::done
			final String singleLine = br.readLine();
			data.add(singleLine);
		}
		this.nano = new SvgNanoParser(data, false);
	}

	public void drawU(UGraphic ug, double scale, HColor colorForMonochrome) {
		try {
			loadIfNeed();
		} catch (IOException e) {
			Logme.error(e);
		}
		nano.drawU(ug, scale, colorForMonochrome);
	}

	public String getShortcut() {
		return shortcut;
	}

}
