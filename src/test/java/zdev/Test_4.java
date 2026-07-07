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

left to right direction
skinparam packageStyle rect
actor Kurier
actor Empfanger
package Kurierfahrt{
  Kurier -- (Erfassung der Daten)
  Kurier -- (Transport der Sendung\n__foo1__\n--foo2--\n==foo3==\nEP: Quittung gewunscht)
  Empfanger -- (Personliche Ubergabe und Quittierung)
(Transport der Sendung\n----\ndummy\n====\nEP: Quittung gewunscht) .> (Automatische Rechnungserstellung) : <include>
note "condition: {Quittung gewunscht ist wahr}\nEP: Quittung gewunscht" as N
(Personliche Ubergabe und Quittierung) ... N
N.> (Transport der Sendung) : <extend>
}
@enduml
 */
public class Test_4 {

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
