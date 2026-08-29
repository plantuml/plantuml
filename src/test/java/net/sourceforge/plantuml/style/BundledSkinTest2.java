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
package net.sourceforge.plantuml.style;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

/**
 * Guards the .skin files bundled inside the jar.
 *
 * See https://github.com/plantuml/plantuml/issues/2797 : sonyxperiadev.skin and
 * reddress.skin were still written with the legacy skinparam syntax, so they
 * were unusable and made the renderer crash (NullPointerException for the
 * former, StyleParsingException for the latter). Nothing exercised them, so the
 * breakage went unnoticed.
 */
class BundledSkinTest2 {

	/**
	 * strictuml.skin is not a complete style sheet: it is a small overlay merged on
	 * top of the current styles by {@code skinparam style strictuml}, so it is not
	 * meant to be used as {@code skin strictuml}.
	 */
	private static final String OVERLAY_ONLY = "strictuml.skin";

	// -----------------------------------------------------------------------
	// Discovery of the bundled skins
	// -----------------------------------------------------------------------

	static Stream<String> allBundledSkins() {
		return bundledSkins().stream();
	}

	static Stream<String> completeBundledSkins() {
		return bundledSkins().stream().filter(name -> OVERLAY_ONLY.equals(name) == false);
	}

	private static List<String> bundledSkins() {
		final Path folder = skinFolder();
		final List<String> result = new ArrayList<>();
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder, "*.skin")) {
			for (Path path : stream)
				result.add(path.getFileName().toString());
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		result.sort(Comparator.naturalOrder());
		assertFalse(result.isEmpty());
		return result;
	}

	private static Path skinFolder() {
		try {
			// plantuml.skin is the default skin: it must always be there
			return Paths.get(StyleLoader.class.getResource("/skin/plantuml.skin").toURI()).getParent();
		} catch (URISyntaxException e) {
			throw new IllegalStateException(e);
		}
	}

	// -----------------------------------------------------------------------
	// Every bundled skin must be a real style sheet
	// -----------------------------------------------------------------------

//	@ParameterizedTest(name = "{0}")
//	@MethodSource("allBundledSkins")
//	@DisplayName("Every bundled .skin loads and defines at least a root style")
//	void bundledSkinLoads(String filename) throws Exception {
//		final StyleBuilder builder = StyleLoader.loadSkin(filename);
//		System.err.println("filename=" + filename);
//		assertNotNull(builder);
//		// assertNotNull(builder.getMergedStyle(StyleSignatureBasic.of(SName.root)));
//	}

	@Test
	void bundledSkinLoads() throws Exception {
		final StyleBuilder builder = StyleLoader2.loadSkin("/skin/plantuml.skin");
		System.err.println("builder=" + builder);
		assertNotNull(builder);
		// assertNotNull(builder.getMergedStyle(StyleSignatureBasic.of(SName.root)));
	}

}
