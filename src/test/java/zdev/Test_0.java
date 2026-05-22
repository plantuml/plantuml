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

{
  "sha1": "h4hukjrsgvm9hjs2qjy7lpj8vgoazdp",
  "insertion": {
    "when": "2024-06-03T18:59:29.144Z",
    "url": "https://github.com/plantuml/plantuml/issues/1664",
    "user": "plantuml@gmail.com"
  },
  "humhash": "cisede-80-vove445"
}
@startgantt

Print between 2020-09-01 and 2020-09-30
Printscale weekly

Project starts 2020-08-01
[A] starts 2020-08-01 and lasts 1 week
[B] starts 2020-09-01 and lasts 2 weeks

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
