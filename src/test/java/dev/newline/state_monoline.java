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

https://forum.plantuml.net/13071/newline-in-state-diagrams-is-inconsistent
@startuml
skinparam tabSize 2
state s1
s1 : line1
s1 : \tline2
s1 : \t\tline3
@enduml

https://forum.plantuml.net/13071/newline-in-state-diagrams-is-inconsistent
@startuml
state s1
s1 : line1
s1 : \nline2
s1 : \n\nline3
@enduml

https://forum.plantuml.net/17579/how-to-add-a-snippet-of-code-inside-a-state-in-a-statechart
@startuml
[*] --> State1
State1 --> [*]
State1 : this is a string
State1 : adding **some code**:\n\
main() {\n\
  printf("Hello world");\n\
}

State1 -> State2
State2 --> [*]
@enduml






 */
public class state_monoline {

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

		assertEquals(3, list.size());

	}


}
