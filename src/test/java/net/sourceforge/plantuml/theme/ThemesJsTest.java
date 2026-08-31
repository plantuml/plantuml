package net.sourceforge.plantuml.theme;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.IndicativeSentencesGeneration;
import org.junit.jupiter.api.Test;

/**
 * Guards the generated theme artifacts against drift.
 * <p>
 * The bundled theme files in <code>/themes</code> are mirrored twice for the
 * browser build: {@link ThemesJsGenerator} writes their content into
 * teavm/themes.js (committed, like emoji.js), and {@link ThemeListGenerator}
 * writes their names into {@link ThemeList}. A theme added or edited without
 * re-running the generators would silently stop being available, or stop being
 * listed, in the browser. These tests fail in that case.
 */
@IndicativeSentencesGeneration(separator = ": ", generator = ReplaceUnderscores.class)
class ThemesJsTest {

	private static final Pattern ENTRY = Pattern
			.compile("^g\\.PLANTUML_THEMES\\[\"(.*?)\"\\]=\"(.*)\";$", Pattern.MULTILINE);

	@Test
	void themes_js_matches_the_bundled_theme_files() throws IOException {
		final Map<String, String> fromJs = parseThemesJs();
		final List<String> expected = ThemeUtils.getAllThemeNames();

		assertEquals(expected.size(), fromJs.size(),
				"themes.js has " + fromJs.size() + " themes but " + expected.size()
						+ " are bundled: re-run ThemesJsGenerator");

		for (String name : expected) {
			final String js = fromJs.get(name);
			assertNotNull(js, "themes.js has no entry for '" + name + "': re-run ThemesJsGenerator");
			assertEquals(readTheme(name), js,
					"themes.js is stale for '" + name + "': re-run ThemesJsGenerator");
		}
	}

	@Test
	void theme_list_matches_the_bundled_theme_files() throws IOException {
		// ThemeList is what the browser's %get_all_theme() returns, so it must list
		// exactly the themes that exist. getAllThemeNames() reads the classpath here
		// (isTeaVM() is false under JUnit), which makes it the ground truth.
		assertEquals(ThemeUtils.getAllThemeNames(), new ThemeList().getAll(),
				"ThemeList does not match the bundled theme files: re-run ThemeListGenerator");
	}

	@Test
	void themes_js_is_pure_ascii() throws IOException {
		final String content = read("/teavm/themes.js");
		for (int i = 0; i < content.length(); i++)
			assertTrue(content.charAt(i) < 0x80,
					"themes.js must stay pure ASCII, found a non-ASCII char at offset " + i);
	}

	private Map<String, String> parseThemesJs() throws IOException {
		final Map<String, String> result = new LinkedHashMap<>();
		final Matcher m = ENTRY.matcher(read("/teavm/themes.js"));
		while (m.find())
			result.put(unescape(m.group(1)), unescape(m.group(2)));

		return result;
	}

	private String readTheme(String name) throws IOException {
		return read("/themes/" + ThemeUtils.getFilename(name)).replace("\r\n", "\n");
	}

	private String read(String resource) throws IOException {
		try (InputStream is = ThemesJsTest.class.getResourceAsStream(resource)) {
			assertNotNull(is, "Missing resource " + resource);
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			final byte[] buffer = new byte[8192];
			int read;
			while ((read = is.read(buffer)) != -1)
				baos.write(buffer, 0, read);

			return new String(baos.toByteArray(), UTF_8);
		}
	}

	/** Reverses the JS string escaping done by ThemesJsGenerator. */
	private String unescape(String value) {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < value.length(); i++) {
			final char c = value.charAt(i);
			if (c != '\\') {
				sb.append(c);
				continue;
			}
			final char next = value.charAt(++i);
			switch (next) {
			case 'n':
				sb.append('\n');
				break;
			case 'r':
				sb.append('\r');
				break;
			case 't':
				sb.append('\t');
				break;
			case 'u':
				sb.append((char) Integer.parseInt(value.substring(i + 1, i + 5), 16));
				i += 4;
				break;
			default:
				sb.append(next);
			}
		}
		return sb.toString();
	}

}
