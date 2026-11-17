package net.sourceforge.plantuml.tim;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import net.sourceforge.plantuml.nio.NFolderRegular;
import net.sourceforge.plantuml.nio.PathSystem;
import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.utils.LineLocationImpl;

class TContextIncludeTest {

	@TempDir
	Path directory;

	private TContext context() {
		return new TContext(PathSystem.fetch().withCurrentDir(new NFolderRegular(directory)),
				Defines.createEmpty(), StandardCharsets.UTF_8, null);
	}

	private void execute(TContext context, String... lines) throws EaterException {
		final List<StringLocated> body = new ArrayList<>();
		for (String line : lines)
			body.add(new StringLocated(line, new LineLocationImpl("test", null)));
		context.executeLines(new TMemoryGlobal(), body, null, false);
	}

	private List<String> output(TContext context) {
		return context.getResultList().stream().map(StringLocated::getString).collect(Collectors.toList());
	}

	@Test
	void defaultSkipsDuplicatesWhileManyExecutesAgain() throws Exception {
		Files.writeString(directory.resolve("common.puml"), "alice -> bob\n");
		final TContext context = context();
		execute(context, "!include common.puml", "!include common.puml", "!include_many common.puml", "finished");
		assertEquals(Arrays.asList("alice -> bob", "alice -> bob", "finished"), output(context));
		assertEquals(1, context.getFilesUsedCurrent().size());
	}

	@Test
	void onceRejectsDuplicatesWithOriginalMessage() throws Exception {
		Files.writeString(directory.resolve("common.puml"), "alice -> bob\n");
		final TContext context = context();
		final EaterException error = assertThrows(EaterException.class,
				() -> execute(context, "!include common.puml", "!include_once common.puml"));
		assertEquals("This file has already been included", error.getMessage());
		assertEquals(Arrays.asList("alice -> bob"), output(context));
	}

	@Test
	void nestedIncludesRestoreTheParentDirectory() throws Exception {
		Files.createDirectory(directory.resolve("child"));
		Files.writeString(directory.resolve("child/first.puml"), "!include second.puml\nchild\n");
		Files.writeString(directory.resolve("child/second.puml"), "nested\n");
		Files.writeString(directory.resolve("second.puml"), "parent\n");
		final TContext context = context();
		execute(context, "!include child/first.puml", "!include second.puml");
		assertEquals(Arrays.asList("nested", "child", "parent"), output(context));
	}

	@Test
	void executionErrorRestoresDirectoryAndKeepsFileRegistered() throws Exception {
		Files.createDirectory(directory.resolve("child"));
		Files.writeString(directory.resolve("child/broken.puml"), "!include missing.puml\n");
		Files.writeString(directory.resolve("after.puml"), "after\n");
		final TContext context = context();
		assertThrows(EaterException.class, () -> execute(context, "!include child/broken.puml"));
		assertEquals(1, context.getFilesUsedCurrent().size());
		execute(context, "!include after.puml");
		assertEquals(Arrays.asList("after"), output(context));
	}

	@Test
	void stripsYamlHeaderBeforeExecutingContent() throws Exception {
		Files.writeString(directory.resolve("header.puml"), "---\ntitle: ignored\n---\nalice -> bob\n");
		final TContext context = context();
		execute(context, "!include header.puml");
		assertEquals(Arrays.asList("alice -> bob"), output(context));
	}

	@Test
	void missingFileRetainsOriginalError() {
		final EaterException error = assertThrows(EaterException.class,
				() -> execute(context(), "!include missing.puml"));
		assertEquals("cannot include missing.puml", error.getMessage());
	}

	@Test
	void standardLibraryIncludeReturnsToFollowingLines() throws Exception {
		final TContext context = context();
		execute(context, "!include <C4/C4>", "finished");
		final List<String> result = output(context);
		assertEquals("finished", result.get(result.size() - 1));
	}
}
