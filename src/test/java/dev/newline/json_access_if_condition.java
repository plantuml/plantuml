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

Test for JSON access in if conditions with missing keys.
Related to issue: Allow JSON access on 'if test'

This test verifies that JSON access with brackets works in if conditions,
even when the key doesn't exist in the JSON object.

@startuml
!$data = {
    "a": "foo",
    "b": "bar"
}

!procedure $test($name)
!if %json_key_exists($data, $name)
    !if $data[$name]=="foo"
        :This is 'foo';
    !else
        :This is not 'foo';
    !endif
!else
    :Key does not exist;
!endif
!endprocedure

$test("a")
$test("b")
$test("c")
@enduml

*/
public class json_access_if_condition {

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

		// Test should complete successfully without errors
		assertEquals(1, list.size());
	}
}
