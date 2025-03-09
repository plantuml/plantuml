package dev.newline;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.GeneratedImage;
import net.sourceforge.plantuml.SourceFileReader;
import net.sourceforge.plantuml.preproc.Defines;

/*
 * 

https://forum.plantuml.net/3055/support-l-and-r-in-additon-to-n?show=3058#a3058
@startuml
class cl1
class cl2
class cl3
class cl4
cl1 -- cl2 : this is\non several\nlines
cl2 -- cl3 : this is\lon several\llines
cl3 -- cl4 : this is\ron several\rlines
@enduml

https://forum.plantuml.net/14510/class-diagram-how-place-the-function-parameters-multiline
@startuml
class CImaging {
    +void Init(const ToolConfig& config,\n\
        PolarizedIllumSource& illum_src,\n\
        C3DStructure& structure3d,\n\
        CVectorialProjection& projection );
}
@enduml



 */
public class class_funcparam_arrow {

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
				Arrays.asList("!pragma layout smetana"), "UTF-8", options);
		final List<GeneratedImage> list = reader.getGeneratedImages();

		assertEquals(2, list.size());

	}


}
