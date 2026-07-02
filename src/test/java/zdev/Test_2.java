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
par
  Bxxx->Cxxx : message3
  par2
    par2
      Axxx ->Axxx2 : message1 which is long
      Axxx ->Axxx2 : message1 which is long
      Axxx ->Axxx2 : message1 which is long
    else
	    Bxxx ->Cxxx : message2
    end
  also
    Dxxx -> Dxxx
  also
    par2
      ZAxxx ->ZAxxx2 : message1 which is long
      ZAxxx ->ZAxxx2 : message1 which is long
      ZAxxx ->ZAxxx2 : message1 which is long
    else
	    ZBxxx ->ZCxxx : message2
    end
  end

  Bxxx->Cxxx : message3
end

Cxxx -> Dxxx : message4
note left of Dxxx : this is to note
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
