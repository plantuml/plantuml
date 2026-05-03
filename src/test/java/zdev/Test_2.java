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

https://github.com/plantuml/plantuml/issues/2679


@startuml
<style>
partition {
  LineColor green
  LineStyle 5
  BackgroundColor lightCyan
  LineThickness 3
  ArrowThickness 0.5
  FontName Arial Narrow
  FontColor red
  FontSize 11
  FontStyle regular
  RoundCorner 30
}
partitionHeader {
  FontStyle plain
  BackGroundColor palegreen
  LineColor blue
  LineStyle 2
}
</style>
'!pragma teoz
participant a

partition p1
b -> c: msg
c --> b: OK
note right: Some right note
end

partition p2
a -> b: msg
note left: Some left note
end
@enduml
 */
public class Test_2 {

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
