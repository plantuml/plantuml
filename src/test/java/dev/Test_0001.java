package dev;

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

@startuml
binary "sample" as sample
concise "tx1" as tx1
hide time-axis

@sample
@20 
sample is high
+0.1 is low
+9.9 is high
+0.1 is low
+19.9 is high
+0.1 is low
+19.9 is high
+0.1 is low
+19.9 is high
+0.1 is low
+19.9 is high
+0.1 is low
+19.9 is high
+0.1 is low
+19.9 is high
+0.1 is low
+19.9 is high
+0.1 is low
+19.9 is high
+0.1 is low
+19.9 is high
+0.1 is low

@tx1
0 is {hidden}
+20 is START
+19 is D0
+19 is D1
+19 is D2
+19 is D3
+19 is D4
+19 is D5
+19 is D6
+19 is D7
+19 is STOP
+19 is {hidden}
@enduml


 */
public class Test_0001 {

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

		final SourceFileReader reader = new SourceFileReader(Defines.createWithFileName(file), file, outputDirectory,
				Collections.<String>emptyList(), "UTF-8", options);
		final List<GeneratedImage> list = reader.getGeneratedImages();

		assertEquals(1, list.size());

	}

}
