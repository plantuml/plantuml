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
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

// ::uncomment when __TEAVM__
//import net.sourceforge.plantuml.teavm.browser.TeaVmScriptLoader;
//import org.teavm.jso.JSBody;
// ::done

// Emojji from https://twemoji.twitter.com/
// Shorcut from https://api.github.com/emojis

public class Emoji {

	// ::comment when __TEAVM__
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
	//::done

	private SvgNanoParser nano;

	private final String unicode;
	private final String shortcut;

	private Emoji(String unicode) {
		final int x = unicode.indexOf(';');
		if (x == -1) {
			this.shortcut = null;
		} else {
			this.shortcut = unicode.substring(x + 1);
			// ::comment when __TEAVM__
			ALL.put(this.shortcut, this);
			//::done
			unicode = unicode.substring(0, x);
		}
		this.unicode = unicode;
		// ::comment when __TEAVM__
		ALL.put(unicode, this);
		//::done
	}

	public static Emoji retrieve(String name) {
		// ::revert when __TEAVM__
		return ALL.get(name.toLowerCase());
		// return retrieveFromJs(name.toLowerCase());
		//::done
	}

	// ::uncomment when __TEAVM__
	// private static Emoji retrieveFromJs(String name) {
	// 	loadEmojiJsIfNeeded();
	// 	String unicode = jsGetShortcut(name);
	// 	if (unicode == null)
	// 		unicode = name;
	// 	final String svgData = jsGetEmojiSvg(unicode);
	// 	if (svgData == null)
	// 		return null;
	// 	return new Emoji(unicode);
	// }
	//
	// private static volatile boolean emojiJsLoaded = false;
	//
	// private static void loadEmojiJsIfNeeded() {
	// 	if (emojiJsLoaded)
	// 		return;
	// 	TeaVmScriptLoader.loadOnceSync("emoji.js");
	// 	emojiJsLoaded = true;
	// }
	//
	// @JSBody(params = "name", script =
	// 		"var s = window.PLANTUML_EMOJI_SHORTCUT;" +
	// 		"return (s && s[name]) ? s[name] : null;")
	// private static native String jsGetShortcut(String name);
	//
	// @JSBody(params = "unicode", script =
	// 		"var e = window.PLANTUML_EMOJI;" +
	// 		"return (e && e[unicode]) ? e[unicode] : null;")
	// private static native String jsGetEmojiSvg(String unicode);
	// ::done

	private synchronized void loadIfNeed() throws IOException {
		if (nano != null)
			return;

		final List<String> data = new ArrayList<String>();
		// ::revert when __TEAVM__
		// final String svgData = jsGetEmojiSvg(unicode);
		// if (svgData != null)
		// 	data.add(svgData);
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(Dummy.class.getResourceAsStream(unicode + ".svg")))) {
			final String singleLine = br.readLine();
			data.add(singleLine);
		}
		// ::done
		
		this.nano = new SvgNanoParser(data);
	}

	public void drawU(UGraphic ug, double scale, HColor colorForMonochrome) {
		try {
			loadIfNeed();
		} catch (IOException e) {
			Logme.error(e);
		}
		nano.drawU(ug, scale, colorForMonochrome, colorForMonochrome);
	}

	public String getShortcut() {
		return shortcut;
	}
	
//	public static void main(String[] args) throws IOException {
//		final String outputFile = (args.length > 0) ? args[0]
//				: "src/main/resources/teavm/emoji.js";
//
//		try (PrintWriter pw = new PrintWriter(new FileWriter(outputFile))) {
//			pw.println("// emoji.js - Generated by Emoji.main()");
//			pw.println("// Do not edit manually");
//			pw.println("(function () {");
//			pw.println("window.PLANTUML_EMOJI = window.PLANTUML_EMOJI || {};");
//			pw.println("window.PLANTUML_EMOJI_SHORTCUT = window.PLANTUML_EMOJI_SHORTCUT || {};");
//			pw.println();
//
//			final InputStream is = Dummy.class.getResourceAsStream("emoji.txt");
//			if (is == null)
//				throw new IOException("Cannot find emoji.txt resource");
//
//			int count = 0;
//			try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
//				String line;
//				while ((line = br.readLine()) != null) {
//					line = line.trim();
//					if (line.isEmpty())
//						continue;
//
//					final String unicode;
//					final String shortcut;
//					final int semi = line.indexOf(';');
//					if (semi == -1) {
//						unicode = line;
//						shortcut = null;
//					} else {
//						unicode = line.substring(0, semi);
//						shortcut = line.substring(semi + 1);
//					}
//
//					final String svgContent = readSvgResource(unicode);
//					if (svgContent == null) {
//						System.err.println("WARNING: No SVG found for " + unicode);
//						continue;
//					}
//
//					pw.println("  window.PLANTUML_EMOJI[\"" + unicode + "\"]=\""
//							+ escapeForJs(svgContent) + "\";");
//
//					if (shortcut != null)
//						pw.println("  window.PLANTUML_EMOJI_SHORTCUT[\"" + shortcut + "\"]=\""
//								+ unicode + "\";");
//
//					count++;
//				}
//			}
//
//			pw.println();
//			pw.println("})();");
//			System.out.println("Generated " + outputFile + " with " + count + " emojis.");
//		}
//	}
//
//	private static String readSvgResource(String unicode) {
//		final InputStream svgStream = Dummy.class.getResourceAsStream(unicode + ".svg");
//		if (svgStream == null)
//			return null;
//
//		try (BufferedReader br = new BufferedReader(new InputStreamReader(svgStream))) {
//			return br.readLine();
//		} catch (IOException e) {
//			System.err.println("ERROR reading " + unicode + ".svg: " + e.getMessage());
//			return null;
//		}
//	}
//
//	private static String escapeForJs(String s) {
//		return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "");
//	}

}
