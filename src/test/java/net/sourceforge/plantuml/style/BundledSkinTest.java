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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.style.parser.StyleParsingException;

/**
 * Guards the .skin files bundled inside the jar.
 *
 * See https://github.com/plantuml/plantuml/issues/2797 : sonyxperiadev.skin and
 * reddress.skin were still written with the legacy skinparam syntax, so they
 * were unusable and made the renderer crash (NullPointerException for the
 * former, StyleParsingException for the latter). Nothing exercised them, so the
 * breakage went unnoticed.
 */
class BundledSkinTest {

	/**
	 * strictuml.skin is not a complete style sheet: it is a small overlay merged on
	 * top of the current styles by {@code skinparam style strictuml}, so it is not
	 * meant to be used as {@code skin strictuml}.
	 */
	private static final String OVERLAY_ONLY = "strictuml.skin";

	private static final String[][] DIAGRAMS = { //
			{ "object", "object o1\nobject o2\no1 --> o2\n" }, //
			{ "sequence", "participant Alice\ndatabase Db\nbox \"Team\"\nparticipant Bob\nend box\n"
					+ "Alice -> Bob: hello\nnote right: a note\nBob -> Db: query\n" }, //
			{ "class", "class Foo {\n +int x\n}\nclass Bar\nFoo --> Bar\n" }, //
			{ "usecase", "actor User\nusecase UC1\nUser --> UC1\n" }, //
			{ "activity", "start\n:hello;\nstop\n" }, //
			{ "state", "[*] --> S1\nS1 --> [*]\n" }, //
			{ "component", "component C1\ncomponent C2\nC1 --> C2\n" }, //
	};

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

	@ParameterizedTest(name = "{0}")
	@MethodSource("allBundledSkins")
	@DisplayName("Every bundled .skin loads and defines at least a root style")
	void bundledSkinLoads(String filename) throws Exception {
		final StyleBuilder builder = StyleLoader.loadSkin(filename);
		assertNotNull(builder);
		assertNotNull(builder.getMergedStyleTOBEREMOVED(StyleSignature.ofSName0(SName.root)));
	}

	// -----------------------------------------------------------------------
	// Every complete skin must actually draw something
	// -----------------------------------------------------------------------

	@ParameterizedTest(name = "{0}")
	@MethodSource("completeBundledSkins")
	@DisplayName("Every complete bundled .skin renders all diagram kinds")
	void bundledSkinRenders(String filename) throws Exception {
		final String skin = filename.substring(0, filename.length() - ".skin".length());
		for (String[] diagram : DIAGRAMS) {
			final String svg = renderSvg("@startuml\nskin " + skin + "\n" + diagram[1] + "@enduml");
			assertFalse(svg.isEmpty());
			assertFalse(svg.contains("Welcome to PlantUML")); // the image drawn when a command fails
			assertFalse(svg.contains("Cannot find style"));
			assertFalse(svg.contains("Cannot parse style"));
			assertFalse(svg.contains("Incomplete style"));
			assertFalse(svg.contains("net.sourceforge.plantuml")); // no stack trace inside the SVG
		}
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("completeBundledSkins")
	@DisplayName("Every complete bundled .skin defines a full root style")
	void bundledSkinHasCompleteRoot(String filename) throws Exception {
		assertTrue(StyleLoader.getMissingRootProperties(StyleLoader.loadSkin(filename)).isEmpty());
	}

	// -----------------------------------------------------------------------
	// A fragment cannot be used as a whole style sheet
	// -----------------------------------------------------------------------

	@Test
	@DisplayName("An overlay fragment does not define a full root style")
	void overlayFragmentHasIncompleteRoot() throws Exception {
		assertFalse(StyleLoader.getMissingRootProperties(StyleLoader.loadSkin(OVERLAY_ONLY)).isEmpty());
	}

	@Test
	@DisplayName("The skin command rejects an overlay fragment instead of drawing garbage")
	void skinCommandRejectsOverlayFragment() throws Exception {
		final String skin = OVERLAY_ONLY.substring(0, OVERLAY_ONLY.length() - ".skin".length());
		final String svg = renderSvg("@startuml\nskin " + skin + "\nparticipant Alice\nAlice -> Bob: hi\n@enduml");
		assertTrue(svg.contains("Incomplete style"));
	}

	// -----------------------------------------------------------------------
	// A legacy skinparam file must be rejected, not silently ignored
	// -----------------------------------------------------------------------

	@Test
	@DisplayName("A legacy skinparam file is rejected instead of giving an empty StyleBuilder")
	void legacySkinparamFileIsRejected(@TempDir Path tempDir) throws Exception {
		final Path legacy = tempDir.resolve("legacy.skin");
		Files.write(legacy, ("SkinParam BackgroundColor #white\n" //
				+ "SkinParam ParticipantBackgroundColor #dde5ff\n").getBytes(StandardCharsets.UTF_8));

		assertThrows(StyleParsingException.class, () -> StyleLoader.loadSkin(legacy.toAbsolutePath().toString()));
	}

	@Test
	@DisplayName("An unknown skin is reported on the skin line, not as a crash")
	void unknownSkinIsReportedAsAnError() throws Exception {
		final String svg = renderSvg("@startuml\nskin thisSkinDoesNotExist\nobject o1\n@enduml");
		assertTrue(svg.contains("Cannot find style"));
	}

	// -----------------------------------------------------------------------

	private static String renderSvg(String source) throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new SourceStringReader(source).outputImage(baos, new FileFormatOption(FileFormat.SVG));
		return new String(baos.toByteArray(), StandardCharsets.UTF_8);
	}

}
