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

https://github.com/plantuml/plantuml/issues/1109#issuecomment-2794418145

@startuml

!pragma teoz true

hide stereotype

<style>
BackGroundColor: #Black
lifeLine {
    ' superseded by pink, defined later
    BackGroundColor: #Red
}
.A {
    BackGroundColor: #LightCoral
    lifeLine {
        ' ignored, why?
        BackGroundColor: #LightCoral
    }
}

.B {
    BackGroundColor: #Green
    lifeLine {
        ' ignored, why?
        BackGroundColor: #Green
    }
}
</style>


participant A <<A>>
participant B <<B>>

activate A
A -> B ++: Hello
A <- B --: Hey
A --

' Takes effect globally, not past this point as i intended
<style>
lifeLine {
    BackGroundColor: #Pink
}
</style>

A -> B ++: Hello
A <- B   : Hey

@enduml

 */
public class Test_0000 {

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
