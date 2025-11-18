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
https://github.com/plantuml/plantuml/issues/589

@startuml
participant server
participant api
database db

activate server

partition #lightgreen Creation 
server -> api ++: [HTTP]\nPOST /api/job
api --> server --: [HTTP]\n201 {"jobId": 1}
end

partition #lightsalmon Initialization 
server -> api ++: [HTTP]\nPUT /api/job/1
api --> server --: [HTTP]\n202 {}
end

partition #lightblue Fetching Result 
loop #FFFFFFAA Polling until output is available
 server -> api ++: [HTTP]\nGET /api/job/1
 alt #FFFFFFAA Job still processing
   api -[#blue]-> server : [HTTP]\n200 {}
 else Job complete. Fetch output
   api -[#blue]-> server --: [HTTP]\n303 {"URL": "/api/job/1/output"}
   server -> api ++: [HTTP]\nGET /api/job/1/output
   api --> server --: [HTTP]\n200 {... output ...}
 end
end
   server -> db ++: Store output
   deactivate server
   deactivate db  
end
@enduml

 */
public class Test_0001 {

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
