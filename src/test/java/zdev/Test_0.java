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

@startgantt
Project starts 2024-09-21
saturday are closed
sunday are closed

From 8:00 to 12:00 are working hours
From 14:00 to 18:00 are working hours
[Task 1] starts 2024-09-22 10:30:46 and ends 2024-09-22 11:28:48
@endgantt

 */
public class Test_0 {

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
