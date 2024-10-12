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
<style>
Package,Frame {
  FontColor yellow
  FontSize 10
  Fontstyle italic
  BackgroundColor blue
  title {
    FontColor red
    FontSize 20
    Fontstyle bold
    BackgroundColor orange
  }
}
</style>

title title 
package test {
    file test2
}

package test0 {
}

frame f
frame f0 {
}
frame f1 {
file a
}
@enduml

 */
public class Style_001 {

	protected File getJavaFile() {
		final String name = getClass().getName();
		final File f = new File("test/" + name.replace('.', '/') + ".java");
		return f;
	}

	@Test
	public void testExecute() throws IOException, InterruptedException {
		final File file = getJavaFile();
		final FileFormatOption options = new FileFormatOption(FileFormat.PNG);

		final File outputDirectory = new File("outputdev", "png").getAbsoluteFile();
		outputDirectory.mkdirs();

		final SourceFileReader reader = new SourceFileReader(Defines.createWithFileName(file), file, outputDirectory,
				Collections.<String>emptyList(), "UTF-8", options);
		final List<GeneratedImage> list = reader.getGeneratedImages();

		assertEquals(1, list.size());

	}

}
