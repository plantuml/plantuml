/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2026, Jamison Jiang
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
 * Original Author:  Jamison Jiang
 *
 *
 */
package net.atmp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.parallel.ResourceAccessMode;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.NewpagedDiagram;
import net.sourceforge.plantuml.PSystemBuilder;
import net.sourceforge.plantuml.Previous;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.dot.GraphvizRuntimeEnvironment;
import net.sourceforge.plantuml.dot.GraphvizVersion;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.nio.PathSystem;
import net.sourceforge.plantuml.preproc.OptionKey;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.sdot.CucaDiagramFileMakerSmetana;
import net.sourceforge.plantuml.skin.PragmaKey;
import test.utils.PlantUmlTestUtils;

@ResourceLock(value = "net.sourceforge.plantuml.dot.GraphvizRuntimeEnvironment", mode = ResourceAccessMode.READ_WRITE)
class CucaDiagramGraphSupportSelectionTest {

	private final GraphvizRuntimeEnvironment environment = GraphvizRuntimeEnvironment.getInstance();
	private String previousDotExecutable;
	private String previousDotVersion;
	private Map<File, GraphvizVersion> previousVersionCache;

	@BeforeEach
	void captureGraphvizState() throws Exception {
		previousDotExecutable = getField(environment, "dotExecutable");
		previousDotVersion = getField(environment, "dotVersion");
		previousVersionCache = new HashMap<>(CucaDiagramGraphSupportSelectionTest
				.<Map<File, GraphvizVersion>>getField(environment, "map"));
	}

	@AfterEach
	void restoreGraphvizState() throws Exception {
		setField(environment, "dotExecutable", previousDotExecutable);
		setField(environment, "dotVersion", previousDotVersion);
		final Map<File, GraphvizVersion> versionCache = getField(environment, "map");
		versionCache.clear();
		versionCache.putAll(previousVersionCache);
	}

	@Test
	void automaticFailureReplaysSmetanaFromSource() throws Exception {
		assertSmetanaReplay("skinparam groupInheritance 2\nclass A\nclass B\nclass C\n"
				+ "A <|-- B\nA <|-- C\nnote right of B: retained note");
	}

	@ParameterizedTest
	@ValueSource(strings = {
			"state Outer {\nstate Inner {\nA --> B\n}\nInner --> C\n}\nOuter --> D",
			"(*) --> {\n(*) --> First\nFirst --> Second\n}\n--> Last\nLast --> (*)" })
	void nestedFailuresDiscardPreviouslySimplifiedStatesAndActivities(String body) throws Exception {
		assertSmetanaReplay(body);
	}

	@Test
	void fallbackReplaysPreprocessedSourceWithoutRunningPreprocessorAgain() throws Exception {
		assertTrue(assertSmetanaReplay("!$name = \"Expanded\"\nclass $name\nclass B\n$name --> B")
				.contains("Expanded"));
	}

	@ParameterizedTest
	@ValueSource(strings = {
			"class A\nclass B\nA --> B",
			"state Outer {\nA --> B\n}\nOuter --> C",
			"(*) --> {\n(*) --> First\nFirst --> Second\n}\n--> Last\nLast --> (*)" })
	void explicitLayoutPragmaDisablesTheSmetanaReplay(String body) throws Exception {
		environment.setDotExecutable("/guaranteed-missing/graph-support-test/dot");
		final CucaDiagram pinned = parse("!pragma layout graph-support", body);
		final CucaDiagram reference = parse("!pragma layout graph-support", body);
		final CucaDiagram smetana = parse("!pragma layout smetana", body);
		forceAutomaticGraphSupportFailure(pinned);

		final String rendered = svg(pinned);
		assertEquals("graph-support", pinned.getPragma().getValue(PragmaKey.LAYOUT));
		assertFalse(rendered.contains("Dot Executable"));
		assertFalse(rendered.contains("An error has occurred"));
		// The explicit pragma has to short-circuit the replay, so the flag is still set afterwards.
		assertTrue(automaticGraphSupportFailed(pinned));
		assertEquals("graph-support", reference.getPragma().getValue(PragmaKey.LAYOUT));
		assertEquals("smetana", smetana.getPragma().getValue(PragmaKey.LAYOUT));
	}

	@Test
	void newpageFallbackPreservesPageAndInheritedSettingsOnRepeatedExports() throws Exception {
		environment.setDotExecutable("/guaranteed-missing/graph-support-test/dot");
		final Previous previous = Previous.createFrom(Collections.singletonMap("classfontsize", "23"));
		final PreprocessingArtifact preprocessing = new PreprocessingArtifact();
		preprocessing.getOption().define(OptionKey.SVG_TITLE, "Replay context title");
		final String[] source = {
				"@startuml", "class First", "class FirstTarget", "First --> FirstTarget",
				"skinparam classBackgroundColor #AABBCC", "newpage",
				"class Second", "class SecondTarget", "Second --> SecondTarget",
				"skinparam classFontSize 31", "newpage",
				"class Third", "class ThirdTarget", "Third --> ThirdTarget", "@enduml" };
		final NewpagedDiagram diagram = (NewpagedDiagram) PSystemBuilder.getInstance().createPSystem(
				PathSystem.fetch(), BlockUml.convert(source), BlockUml.convert(source), previous, preprocessing);
		final NewpagedDiagram clean = (NewpagedDiagram) PSystemBuilder.getInstance().createPSystem(
				PathSystem.fetch(), BlockUml.convert(source), BlockUml.convert(source), previous, preprocessing);
		assertEquals(3, diagram.getDiagrams().size());
		final Field replayPrevious = CucaDiagram.class.getDeclaredField("layoutPrevious");
		replayPrevious.setAccessible(true);
		final String[] names = { "FirstTarget", "SecondTarget", "ThirdTarget" };
		final String[] rendered = new String[3];
		// Start on a later page: neither page selection nor inheritance may depend on export order.
		for (int page : new int[] { 2, 0, 1 }) {
			final CucaDiagram expected = (CucaDiagram) clean.getDiagrams().get(page);
			assertSame(preprocessing, diagram.getDiagrams().get(page).getPreprocessingArtifact());
			assertEquals(previous.values(), ((Previous) replayPrevious.get(diagram.getDiagrams().get(page))).values());
			assertEquals(page == 0 ? "23" : "31", expected.getSkinParam().getValue("classfontsize"));
			expected.getPragma().define("layout", "smetana");
			expected.setUseSmetana(true);
			final String expectedSvg = svgPage(clean, page);
			assertTrue(expectedSvg.contains(names[page]));
			assertFalse(expectedSvg.contains("Dot Executable"));
			forceAutomaticGraphSupportFailure((CucaDiagram) diagram.getDiagrams().get(page));
			rendered[page] = svgPage(diagram, page);
			// The replay must land on this page, not on a neighbour, and must not fall back to an error.
			assertTrue(rendered[page].contains(names[page]));
			for (String other : names)
				assertEquals(other.equals(names[page]), rendered[page].contains(other));
			assertFalse(rendered[page].contains("Dot Executable"));
			assertEquals("#AABBCC", expected.getSkinParam().getValue("classbackgroundcolor"));
			assertNull(((CucaDiagram) diagram.getDiagrams().get(page)).getPragma().getValue(PragmaKey.LAYOUT));
		}
		for (int page = 0; page < 3; page++) {
			assertTrue(svgPage(diagram, page).contains(names[page]));
			// Direct child exports pass zero, but must still replay that child's original page.
			assertTrue(svgPage(diagram.getDiagrams().get(page), 0).contains(names[page]));
		}
	}

	private static String svgPage(Diagram diagram, int page) throws IOException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		diagram.exportDiagram(output, page, new FileFormatOption(FileFormat.SVG, false));
		return new String(output.toByteArray(), StandardCharsets.UTF_8);
	}

	/**
	 * Renders once with the real engine so that states and activities are simplified in place,
	 * then forces the state a SmetanaFallback would have left behind and checks that the next
	 * render replays the preprocessed source from scratch.
	 */
	private String assertSmetanaReplay(String body) throws Exception {
		environment.setDotExecutable("/guaranteed-missing/graph-support-test/dot");
		final CucaDiagram diagram = parse(body);
		final CucaDiagram clean = parse(body);
		final FileFormatOption format = new FileFormatOption(FileFormat.SVG);
		final StringBounder bounder = format.getDefaultStringBounder(clean.getSkinParam(), clean.getPragma());
		diagram.getTextBlock(0, format);
		forceAutomaticGraphSupportFailure(diagram);

		final TextBlock expected = new CucaDiagramFileMakerSmetana(clean).getTextBlock(Collections.emptyList(), format);
		final TextBlock actual = diagram.getTextBlock(0, format);
		assertEquals(expected.calculateDimension(bounder).getWidth(), actual.calculateDimension(bounder).getWidth());
		assertEquals(expected.calculateDimension(bounder).getHeight(), actual.calculateDimension(bounder).getHeight());
		assertNull(diagram.getPragma().getValue(PragmaKey.LAYOUT));
		final PlantUmlTestUtils.ExportDiagram export = new PlantUmlTestUtils.ExportDiagram(diagram).withMetadata(false);
		final String svg = export.asString(FileFormat.SVG);
		assertTrue(svg.contains("<path"));
		assertFalse(svg.contains("Dot Executable"));
		assertFalse(svg.contains("An error has occurred"));
		assertFalse(svg.contains("using Graphviz"));
		return svg;
	}

	private static boolean automaticGraphSupportFailed(CucaDiagram diagram) throws Exception {
		final Field field = CucaDiagram.class.getDeclaredField("automaticGraphSupportFailed");
		field.setAccessible(true);
		return ((Boolean) field.get(diagram)).booleanValue();
	}

	private static void forceAutomaticGraphSupportFailure(CucaDiagram diagram) throws Exception {
		final Field field = CucaDiagram.class.getDeclaredField("automaticGraphSupportFailed");
		field.setAccessible(true);
		field.set(diagram, Boolean.TRUE);
	}

	@Test
	void retainsGraphSupportAsTheRequestedLayout() {
		final CucaDiagram diagram = (CucaDiagram) PlantUmlTestUtils.exportDiagram(
				"@startuml",
				"!pragma layout graph-support",
				"class A",
				"class B",
				"A --> B",
				"@enduml")
				.assertNoError()
				.getDiagram();

		assertEquals("graph-support", diagram.getPragma().getValue(PragmaKey.LAYOUT));
		assertTrue(diagram.isUseGraphSupport());
	}

	@ParameterizedTest
	@ValueSource(strings = { "", "dot - graphviz version 2.44.0 (cached)", "Error: missing dot" })
	void selectsGraphSupportWithoutDotRegardlessOfCachedVersion(String cachedVersion) throws Exception {
		environment.setDotExecutable("/guaranteed-missing/graph-support-test/dot");
		setField(environment, "dotVersion", cachedVersion.isEmpty() ? null : cachedVersion);
		final CucaDiagram diagram = diagram("");

		assertTrue(diagram.isUseGraphSupport());
		assertNull(diagram.getPragma().getValue(PragmaKey.LAYOUT));
	}

	@Test
	void selectsNativeDotDespiteCachedFailure(@TempDir Path directory) throws Exception {
		final File executable = Files.createFile(directory.resolve("dot")).toFile();
		assumeTrue(executable.setExecutable(true) && executable.canExecute());
		environment.setDotExecutable(executable.getAbsolutePath());
		setField(environment, "dotVersion", "Error: missing dot");

		assertFalse(diagram("").isUseGraphSupport());
		assertTrue(diagram("!pragma layout graph-support").isUseGraphSupport());
	}

	@Test
	void rechecksExecutableAfterItDisappears(@TempDir Path directory) throws Exception {
		final File executable = Files.createFile(directory.resolve("dot")).toFile();
		assumeTrue(executable.setExecutable(true) && executable.canExecute());
		environment.setDotExecutable(executable.getAbsolutePath());
		final CucaDiagram diagram = diagram("");
		assertFalse(diagram.isUseGraphSupport());

		Files.delete(executable.toPath());

		assertTrue(diagram.isUseGraphSupport());
	}

	@Test
	void selectsGraphSupportForNonExecutableFileOrDirectory(@TempDir Path directory) throws Exception {
		final File notExecutable = Files.createFile(directory.resolve("dot")).toFile();
		assumeTrue(notExecutable.setExecutable(false) && notExecutable.canExecute() == false);
		environment.setDotExecutable(notExecutable.getAbsolutePath());
		assertTrue(diagram("").isUseGraphSupport());

		environment.setDotExecutable(directory.toString());
		assertTrue(diagram("").isUseGraphSupport());
	}

	@ParameterizedTest
	@ValueSource(strings = { "smetana", "elk" })
	void preservesExplicitEngineWithoutDot(String engine) {
		environment.setDotExecutable("/guaranteed-missing/graph-support-test/dot");
		final CucaDiagram diagram = diagram("!pragma layout " + engine);

		assertFalse(diagram.isUseGraphSupport());
		assertEquals("smetana".equals(engine), diagram.isUseSmetana());
		assertEquals("elk".equals(engine), diagram.isUseElk());
	}

	@ParameterizedTest
	@ValueSource(booleans = { false, true })
	void preservesExplicitVizJsWhenAvailable(boolean executableSetting) {
		environment.setDotExecutable(executableSetting ? "vizjs" : "/guaranteed-missing/graph-support-test/dot");
		final CucaDiagram diagram = diagram("");
		diagram.getSkinParam().setUseVizJs(executableSetting == false);
		assumeTrue(environment.useVizJs(diagram.getSkinParam()), "Optional VizJs runtime required");

		assertFalse(diagram.isUseGraphSupport());
	}

	private static CucaDiagram diagram(String pragma) {
		return (CucaDiagram) PlantUmlTestUtils.exportDiagram("@startuml", pragma, "class A", "class B",
				"A --> B", "@enduml").assertNoError().getDiagram();
	}

	private static CucaDiagram parse(String body) {
		return (CucaDiagram) PlantUmlTestUtils.exportDiagram("@startuml", body, "@enduml").assertNoError()
				.getDiagram();
	}

	private static CucaDiagram parse(String pragma, String body) {
		return (CucaDiagram) PlantUmlTestUtils.exportDiagram("@startuml", pragma, body, "@enduml").assertNoError()
				.getDiagram();
	}

	private static String svg(CucaDiagram diagram) throws IOException {
		return new PlantUmlTestUtils.ExportDiagram(diagram).withMetadata(false).asString(FileFormat.SVG);
	}

	@SuppressWarnings("unchecked")
	private static <T> T getField(GraphvizRuntimeEnvironment environment, String name) throws Exception {
		final Field field = GraphvizRuntimeEnvironment.class.getDeclaredField(name);
		field.setAccessible(true);
		return (T) field.get(environment);
	}

	private static void setField(GraphvizRuntimeEnvironment environment, String name, Object value) throws Exception {
		final Field field = GraphvizRuntimeEnvironment.class.getDeclaredField(name);
		field.setAccessible(true);
		field.set(environment, value);
	}
}
