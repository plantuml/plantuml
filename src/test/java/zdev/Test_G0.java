package zdev;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.GeneratedImage;
import net.sourceforge.plantuml.SourceFileReader;
import net.sourceforge.plantuml.preproc.Defines;

/*
 * 

You can use this file to put a test you are working on.
Here is a simple example:

@startgantt

Project starts 2020-07-01
hide column duration
hide column end
show column task

[Prototype design] requires 14 days
[Test prototype] requires 10 days

[Prototype design] starts 2020-07-01
[Test prototype] starts 2020-07-16
@endgantt

So you can edit this file, but please do not push any modification in the "main" branch.
Put your own tests on your own branches.

However, if your test are interesting, you can add them to the "pdiff" project.
See https://github.com/plantuml/pdiff

 */
public class Test_G0 {

	protected File getJavaFile() {
		final String name = getClass().getName();
		final File f = new File("src/test/java/" + name.replace('.', '/') + ".java");
		return f;
	}

	@Test
	public void testExecute() throws IOException, InterruptedException {
		final File file = getJavaFile();
		final FileFormatOption options = new FileFormatOption(FileFormat.PNG);

		final File outputDirectory = new File("outputdev").getAbsoluteFile();
		outputDirectory.mkdirs();

		final SourceFileReader reader = new SourceFileReader(false, Defines.createWithFileName(file), file, outputDirectory,
				Collections.<String>emptyList(), "UTF-8", options);
		final List<GeneratedImage> list = reader.getGeneratedImages();

		assertEquals(1, list.size());

	}

}
