package net.sourceforge.plantuml.test;

import net.sourceforge.plantuml.FileFormat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static java.util.Collections.unmodifiableSet;
import static net.sourceforge.plantuml.FileFormat.PNG;
import static net.sourceforge.plantuml.test.TestUtils.calculateOutputFile;
import static net.sourceforge.plantuml.test.TestUtils.renderTestCase;

/**
 * A TestGroup is an immutable set of "test names" plus some helper methods.
 */
class TestGroup {

	public static final TestGroup ALL;

	/**
	 * Some of these might not do anything with skinparam but they at least allow it
	 */
	public static final TestGroup ALL_ALLOWING_SKINPARAM;

	/**
	 * Things that users would probably consider a "diagram"
	 */
	public static final TestGroup ALL_DIAGRAMS;

	public static final TestGroup ALL_ERRORS;

	public static final TestGroup ALL_EGGS;

	public static final TestGroup ALL_HELP;

	public static final TestGroup ALL_INFO;

	public static final TestGroup ALL_MISC;

	/**
	 * Subclasses of TitledDiagram
	 */
	public static final TestGroup ALL_TITLED_DIAGRAMS;

	static {
		final String[] data = new String[]{
				"activity DPT",
				"activity3 DPT",
				"apple_two G",
				"board DPT",
				"bpm DT",
				"charlie G",
				"class DPT",
				"colors I",
				"creole M",
				"dedication M",
				"definition M",
				"description DPT",
				"ditaa DM",
				"donors I",
				"dot D",
				"error_preprocessor E",
				"error_v2 E",
				"flow DT",
				"gantt DPT",
				"git DPT",
				"help HT",
				"help_colors HT",
				"help_fonts HT",
				"help_keywords HT",
				"help_skinparams HT",
				"help_types HT",
				"jcckit DP",
				"json DT",
				"keycheck I",
				"keygen I",
				"latex DP",
				"license I",
				"listfonts I",
				"listopeniconic I",
				// TODO confused about ListSpriteDiagram vs PSystemListInternalSprites so not well categorised
				//      seems like both use the "listsprite" command?
				"listsprites I",
				"math DP",
				"mindmap DPT",
				"network DPT",
				"openiconic I",
				"path DM",
				"rip G",
				"salt DPT",
				"sequence DPT",
				"skinparameters I",
				"state DPT",
				"stdlib IT",
				"sudoku M",
				"timing DPT",
				"version I",
				"wbs DPT",
				"welcome I",
				"wire DPT",
				"xearth M",
				"yaml DT",
		};

		final List<String> all = new ArrayList<>();
		final List<String> allAllowingSkinparam = new ArrayList<>();
		final List<String> allDiagrams = new ArrayList<>();
		final List<String> allEggs = new ArrayList<>();
		final List<String> allErrors = new ArrayList<>();
		final List<String> allHelp = new ArrayList<>();
		final List<String> allInfo = new ArrayList<>();
		final List<String> allMisc = new ArrayList<>();
		final List<String> allTitledDiagrams = new ArrayList<>();

		for (String d : data) {
			String[] parts = d.split(" ");
			String name = parts[0];
			String groups = parts.length > 1 ? parts[1] : "";
			all.add(name);
			if (groups.contains("D")) allDiagrams.add(name);
			if (groups.contains("E")) allErrors.add(name);
			if (groups.contains("G")) allEggs.add(name);
			if (groups.contains("H")) allHelp.add(name);
			if (groups.contains("I")) allInfo.add(name);
			if (groups.contains("M")) allMisc.add(name);
			if (groups.contains("P")) allAllowingSkinparam.add(name);
			if (groups.contains("T")) allTitledDiagrams.add(name);
		}

		ALL = testGroup(all);
		ALL_ALLOWING_SKINPARAM = testGroup(allAllowingSkinparam);
		ALL_DIAGRAMS = testGroup(allDiagrams);
		ALL_EGGS = testGroup(allEggs);
		ALL_ERRORS = testGroup(allErrors);
		ALL_HELP = testGroup(allHelp);
		ALL_INFO = testGroup(allInfo);
		ALL_MISC = testGroup(allMisc);
		ALL_TITLED_DIAGRAMS = testGroup(allTitledDiagrams);
	}

	public static TestGroup testGroup(String... names) {
		return testGroup(Arrays.asList(names));
	}

	public static TestGroup testGroup(Collection<String> names) {
		return new TestGroup(names);
	}

	private final Set<String> names;

	private TestGroup(Collection<String> names) {
		this.names = unmodifiableSet(new TreeSet<>(names));
	}

	public TestGroup add(String... names) {
		return add(Arrays.asList(names));
	}

	public TestGroup add(TestGroup... others) {
		final Set<String> result = new HashSet<>(this.names);
		for (TestGroup other : others) {
			result.addAll(other.names);
		}
		return testGroup(result);
	}

	public TestGroup add(Collection<String> names) {
		final Set<String> result = new HashSet<>(this.names);
		result.addAll(names);
		return testGroup(result);
	}

	public TestGroup subtract(String... names) {
		return subtract(Arrays.asList(names));
	}

	public TestGroup subtract(TestGroup... others) {
		final Set<String> result = new HashSet<>(this.names);
		for (TestGroup other : others) {
			result.removeAll(other.names);
		}
		return testGroup(result);
	}

	public TestGroup subtract(Collection<String> names) {
		final Set<String> result = new HashSet<>(this.names);
		result.removeAll(names);
		return testGroup(result);
	}

	public TestGroup difference(TestGroup other) {
		final Set<String> result = new HashSet<>();
		for (String name : this.names) {
			if (!other.names.contains(name)) {
				result.add(name);
			}
		}
		for (String name : other.names) {
			if (!this.names.contains(name)) {
				result.add(name);
			}
		}
		return testGroup(result);
	}

	public TestGrid gridHorizontal(String... dirs) {
		final String[] names = this.names.toArray(new String[]{});
		final File[][] files = new File[dirs.length][names.length];
		for (int row = 0; row < dirs.length; row++) {
			for (int col = 0; col < names.length; col++) {
				files[row][col] = calculateOutputFile(dirs[row], PNG, names[col]).conv();
			}
		}
		return new TestGrid(dirs, names, files);
	}

	public TestGrid gridVertical(String... dirs) {
		final String[] names = this.names.toArray(new String[]{});
		final File[][] files = new File[names.length][dirs.length];
		for (int row = 0; row < names.length; row++) {
			for (int col = 0; col < dirs.length; col++) {
				files[row][col] = calculateOutputFile(dirs[col], PNG, names[row]).conv();
			}
		}
		return new TestGrid(names, dirs, files);
	}

	public TestGroup markdownHorizontal(String outputFile, String... dirs) throws IOException {
		gridHorizontal(dirs).markdown(outputFile);
		return this;
	}

	public TestGroup markdownVertical(String outputFile, String... dirs) throws IOException {
		gridVertical(dirs).markdown(outputFile);
		return this;
	}

	public TestGroup pngHorizontal(String outputFile, String... dirs) throws IOException {
		gridHorizontal(dirs).png(outputFile);
		return this;
	}

	public TestGroup pngVertical(String outputFile, String... dirs) throws IOException {
		gridVertical(dirs).png(outputFile);
		return this;
	}

	public TestGroup render(String... config) {
		for (String name : names) {
			renderTestCase(PNG, "rendered", name, config);
		}
		return this;
	}

	public TestGroup render(FileFormat fileFormat, String outputDir, String... config) {
		for (String name : names) {
			renderTestCase(fileFormat, outputDir, name, config);
		}
		return this;
	}

	@Override
	public String toString() {
		return names.toString();
	}
}
