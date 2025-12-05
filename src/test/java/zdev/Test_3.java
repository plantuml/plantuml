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
 * 
https://github.com/plantuml/plantuml/issues/2436

@startmindmap
<style>
  mindmapDiagram {
  Scale 10
  LineColor Black
   node {
    FontName Roboto
    Padding 5
    Margin 15
   ' RoundCorner 11
    LineColor black
    BackgroundColor #cc33cc-#0c33ac
    FontColor white
    LineStyle 0
    LineThickness 1
    DiagonalCorner 50
  }
  rootNode {
   Padding 10
   FontStyle  Italic
}
  leafNode {
    Padding 7
 }
  .n1 {
    BackgroundColor white
    FontStyle  Bold
    FontColor black
    Shadowing 0
  }
}
</style>
* Class Templates
** foo <<n1>>
**:template typename T1
class cname{
void f1()
...
}; <<n1>>
**:template typename T2
class cname{
void f2()
...
};
@endmindmap

 */
public class Test_3 {

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
