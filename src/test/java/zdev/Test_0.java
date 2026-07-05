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

@startuml
!pragma layout smetana

' if the line is here it is working
' [L_5161099] -right-> L_5098739 : "C-IAV"

[L_5098739] -right-> S_5161051 : "C-LA"

' if the line is here (like in orig) it is not working
[L_5161099] -right-> L_5098739 : "C-IAV"

S_5161051 -right-> L_5098861 : "C-LA"
L_5098739 -right-> L_1338054 : "C-BFT"
L_5098739 -right-> L_5098753 : "C-VD"
L_5098741 -right-> L_5098737 : "C-IAV"
L_5098859 -right-> L_5098861 : "C-IAV"
L_5098859 -right-> L_5646095 : "C-VD"
L_5098859 -right-> L_5646097 : "C-VD"
@enduml
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
