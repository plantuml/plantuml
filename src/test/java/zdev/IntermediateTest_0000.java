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

@startuml
analog "A" as A
robust R
analog "B" as B
robust S
@0
A is 0
R is 0
B is 0
S is 0
@1
R is 1
S is 1
@2
R is 0
S is 0
@3
R is 1
S is 1
@4
A is 5
B is 5
R is 0
S is 0

R@0 <-> @3 : R1️⃣
R@1 <-> @2 : R2️⃣
R@2 <-> @3 : R3️
R@2 <-> @5 : R4️⃣

S@2 <-> @3 : S1️⃣
S@1 <-> @2 : S2️⃣
S@0 <-> @3 : S3️⃣
S@2 <-> @5 : S4️⃣
@enduml

So you can edit this file, but please do not push any modification in the "main" branch.
Put your own tests on your own branches.

However, if your test are interesting, you can add them to the "pdiff" project.
See https://github.com/plantuml/pdiff

 */
public class IntermediateTest_0000 {

	protected File getJavaFile() {
		final String name = getClass().getName();
		final File f = new File("src/test/java/" + name.replace('.', '/') + ".java");
		return f;
	}

	@Test
	public void testExecute() throws IOException, InterruptedException {
		final File file = getJavaFile();
		final FileFormatOption options = new FileFormatOption(FileFormat.SVG);

		final File outputDirectory = new File("outputdev").getAbsoluteFile();
		outputDirectory.mkdirs();

		final SourceFileReader reader = new SourceFileReader(Defines.createWithFileName(file), file, outputDirectory,
				Collections.<String>emptyList(), "UTF-8", options);
		final List<GeneratedImage> list = reader.getGeneratedImages();

		assertEquals(1, list.size());

	}

}
