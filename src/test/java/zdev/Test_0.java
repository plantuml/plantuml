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

from 2018/04/17 to 2018/04/19 is closed

@startgantt
Project starts 2025-05-25
saturday are closed
sunday are closed

From 8:00 to 12:00 are working hours
From 14:00 to 18:00 are working hours
[Task 1] starts 2025-05-25
[Task 2] starts 2025-05-25
[Task 3] starts 2025-05-25
[Task 4] starts 2025-05-25
[Task 5] starts 2025-05-25
[Task 6] starts 2025-05-25
[Task 7] starts 2025-05-25
[Task 8] starts 2025-05-25
[Task 9] starts 2025-05-25
[Task 1] requires 1 hours
[Task 2] requires 2 hours
[Task 3] requires 3 hours
[Task 4] requires 4 hours
[Task 5] requires 5 hours
[Task 6] requires 6 hours
[Task 7] requires 7 hours
[Task 8] requires 8 hours
[Task 9] requires 9 hours
[Task 10] requires 100 hours
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
