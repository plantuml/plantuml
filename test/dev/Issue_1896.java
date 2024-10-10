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

https://github.com/plantuml/plantuml/issues/1896

@startuml
<style>
map {
    BackGroundColor lightblue
    LineColor blue
    FontName Helvetica
    FontColor blue
    FontSize 12
    FontStyle bold
    RoundCorner 20
    LineThickness 2
    LineStyle 10-5
    header {
      BackGroundColor palegreen
      LineColor red
      FontName Helvetica
      FontColor red
      FontSize 18
      FontStyle bold
      RoundCorner 20
      LineThickness 3
      LineStyle 30-5
    }
}

object {
    BackGroundColor lightblue
    LineColor blue
    FontName Helvetica
    FontColor blue
    FontSize 12
    FontStyle italic
    RoundCorner 20
    LineThickness 2
    LineStyle 10-5
    header {
      BackGroundColor palegreen
      LineColor red
      FontName Helvetica
      FontColor red
      FontSize 18
      FontStyle bold
      RoundCorner 20
      LineThickness 3
      LineStyle 30-5
    }
}



json {
    BackGroundColor lightblue
    LineColor blue
    FontName Helvetica
    FontColor blue
    FontSize 12
    FontStyle italic
    RoundCorner 20
    LineThickness 2
    LineStyle 10-5
    header {
      BackGroundColor palegreen
      LineColor red
      FontName Helvetica
      FontColor red
      FontSize 18
      FontStyle bold
      RoundCorner 20
      LineThickness 3
      LineStyle 30-5
    }
}
</style>

object Object_user {
  name = "Dummy"
  id = 123
}

object London

map Map_CapitalCity {
 UK *-> London
 USA => Washington
 Germany => Berlin
}

json "JSON_data" as j {
  "name" : "Dummy",
  "id" : 123
}
@enduml
 */
public class Issue_1896 {

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
