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
package net.sourceforge.plantuml.filesdiagram;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.emoji.Emoji;
import net.sourceforge.plantuml.openiconic.OpenIconic;

/**
 * Chooses the icon drawn in front of each entry.
 * <p>
 * An icon can be given as an emoji ({@code <:coffee:>}, {@code :coffee:},
 * {@code <:2615:>}), an OpenIconic icon ({@code <&folder>}, {@code &folder}), a
 * sprite ({@code <$mysprite>}, {@code $mysprite}) or {@code none}. A bare word
 * is tried as an emoji, then as an OpenIconic icon.
 * <p>
 * Rules can be declared in the diagram, one per line:
 *
 * <pre>
 * [icon *.java &lt;:coffee:&gt;]     ' by extension (".java" works too)
 * [icon Makefile &lt;:hammer:&gt;]     ' by file name
 * [icon build/ &lt;&amp;wrench&gt;]      ' by folder name
 * [icon * &lt;:1f4c4:&gt;]      ' any other file
 * [icon *&#47; none]           ' any other folder
 * </pre>
 *
 * Lookup order: the entry's own {@code icon(...)}, then the declared rules
 * (name, then extension, then {@code *} or {@code *}{@code /}), then the
 * built-in rules (name, then extension), then the default folder and file
 * icons.
 */
final class FilesIcons {

	private static final Pattern DECLARATION = Pattern.compile("^\\[icon\\s+(\\S+)\\s+(\\S+)\\s*\\]$");

	static final String DEFAULT_FOLDER = "<:1f4c2:>";
	static final String DEFAULT_FILE = "<:1f4c4:>";

	private static final Map<String, String> BUILTIN_EXT = new HashMap<>();
	private static final Map<String, String> BUILTIN_NAME = new HashMap<>();

	static {
		builtinExt("coffee", "java", "class", "jsp");
		builtinExt("snake", "py", "pyc", "pyw", "ipynb");
		builtinExt("gem", "rb", "gemspec");
		builtinExt("crab", "rs");
		builtinExt("elephant", "php");
		builtinExt("seedling", "puml", "plantuml", "iuml", "pu", "wsd");
		builtinExt("memo", "md", "markdown", "txt", "adoc", "asciidoc", "rst", "tex");
		builtinExt("framed_picture", "png", "jpg", "jpeg", "gif", "bmp", "svg", "webp", "ico", "tif", "tiff");
		builtinExt("closed_book", "pdf");
		builtinExt("bar_chart", "csv", "tsv", "xls", "xlsx", "ods");
		builtinExt("gear", "json", "yaml", "yml", "xml", "toml", "ini", "properties", "conf", "cfg", "config",
				"env", "gradle", "kts");
		builtinExt("package", "zip", "jar", "war", "ear", "tar", "gz", "tgz", "bz2", "xz", "7z", "rar", "deb",
				"rpm", "whl");
		builtinExt("computer", "sh", "bash", "zsh", "bat", "cmd", "ps1", "exe", "dll", "so", "bin");
		builtinExt("globe_with_meridians", "html", "htm", "css", "scss", "less");
		builtinExt("page_with_curl", "c", "h", "cpp", "hpp", "cc", "cs", "js", "mjs", "ts", "jsx", "tsx", "kt",
				"scala", "groovy", "go", "swift", "lua", "pl", "r", "vue");
		builtinExt("card_file_box", "sql", "db", "sqlite");
		builtinExt("scroll", "log");
		builtinExt("lock", "lock");
		builtinExt("key", "pem", "crt", "cer", "key", "p12", "jks");
		builtinExt("musical_note", "mp3", "wav", "ogg", "flac", "m4a");
		builtinExt("clapper", "mp4", "avi", "mov", "mkv", "webm");

		builtinName("whale", "dockerfile", "docker-compose.yml", "docker-compose.yaml", "compose.yaml");
		builtinName("hammer", "makefile", "build.xml");
		builtinName("scroll", "license", "licence", "copying");
		builtinName("memo", "readme");
		builtinName("see_no_evil", ".gitignore", ".dockerignore");
	}

	private static void builtinExt(String emoji, String... extensions) {
		for (String ext : extensions)
			BUILTIN_EXT.put(ext, "<:" + emoji + ":>");
	}

	private static void builtinName(String emoji, String... names) {
		for (String name : names)
			BUILTIN_NAME.put(name, "<:" + emoji + ":>");
	}

	private final Map<String, String> userExt = new HashMap<>();
	private final Map<String, String> userFileName = new HashMap<>();
	private final Map<String, String> userFolderName = new HashMap<>();
	private String userAnyFile;
	private String userAnyFolder;

	/**
	 * Reads an {@code [icon <pattern> <icon>]} declaration.
	 *
	 * @return false if the line is not a declaration
	 */
	public boolean declare(String line) {
		final Matcher m = DECLARATION.matcher(line.trim());
		if (m.find() == false)
			return false;
		final String pattern = m.group(1);
		final String icon = toCreole(m.group(2));
		if (icon == null)
			return false;

		if (pattern.equals("*"))
			userAnyFile = icon;
		else if (pattern.equals("*/"))
			userAnyFolder = icon;
		else if (pattern.startsWith("*."))
			userExt.put(lower(pattern.substring(2)), icon);
		else if (pattern.startsWith(".") && pattern.length() > 1 && pattern.endsWith("/") == false) {
			// ".env" is both an extension and the name of a dot file
			userExt.put(lower(pattern.substring(1)), icon);
			userFileName.put(lower(pattern), icon);
		}
		else if (pattern.endsWith("/"))
			userFolderName.put(lower(pattern.substring(0, pattern.length() - 1)), icon);
		else
			userFileName.put(lower(pattern), icon);
		return true;
	}

	/**
	 * @return the creole text of the icon, empty for no icon at all
	 */
	public String getIcon(FEntry entry) {
		if (entry.getIcon() != null) {
			final String explicit = toCreole(entry.getIcon());
			if (explicit != null)
				return explicit;
		}
		final String name = lower(entry.getName());
		if (entry.isFolder()) {
			if (userFolderName.containsKey(name))
				return userFolderName.get(name);
			if (userAnyFolder != null)
				return userAnyFolder;
			return DEFAULT_FOLDER;
		}

		if (userFileName.containsKey(name))
			return userFileName.get(name);
		String found = byExtension(name, userExt);
		if (found != null)
			return found;
		if (userAnyFile != null)
			return userAnyFile;

		if (BUILTIN_NAME.containsKey(name))
			return BUILTIN_NAME.get(name);
		found = byExtension(name, BUILTIN_EXT);
		if (found != null)
			return found;
		return DEFAULT_FILE;
	}

	/**
	 * Longest extension first: {@code archive.tar.gz} tries {@code tar.gz}, then
	 * {@code gz}.
	 */
	private static String byExtension(String name, Map<String, String> map) {
		int x = name.indexOf('.', 1);
		while (x != -1) {
			final String result = map.get(name.substring(x + 1));
			if (result != null)
				return result;
			x = name.indexOf('.', x + 1);
		}
		return null;
	}

	private static String lower(String s) {
		return s.toLowerCase(Locale.ROOT);
	}

	/**
	 * Turns the many accepted spellings of an icon into creole.
	 *
	 * @return the creole text, empty for {@code none}, or null if this cannot be
	 *         an icon
	 */
	static String toCreole(String spec) {
		spec = spec.trim();
		if (spec.length() == 0 || spec.equalsIgnoreCase("none"))
			return "";
		if (spec.startsWith("<") && spec.endsWith(">"))
			return spec;
		if (spec.startsWith(":") && spec.endsWith(":") && spec.length() > 2)
			return "<" + spec + ">";
		if (spec.startsWith("&") || spec.startsWith("$"))
			return "<" + spec + ">";
		// Mermaid style "pack:name": only the name can mean something here
		final int colon = spec.lastIndexOf(':');
		if (colon != -1)
			spec = spec.substring(colon + 1);
		if (spec.matches("[0-9a-zA-Z][-_0-9a-zA-Z]*") == false)
			return null;
		if (Emoji.retrieve(spec) != null)
			return "<:" + lower(spec) + ":>";
		if (OpenIconic.retrieve(spec) != null)
			return "<&" + spec + ">";
		return null;
	}

}
