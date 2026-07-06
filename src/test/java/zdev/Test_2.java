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

package "entities" #DDDDDD {

class FcUser {
+ String login
+ String name
+ String email
~ String passwordHash = md5 of "login:password"
boolean checkPassowd(String pw)
void setPassword(String pw)
}

class Card {
+ String frontHtml
+ String backHtml
}

class Answer {
+ int rating [0..3]
+ Date timestamp
}

Answer -> Card : card
Answer -> FcUser : user

class Category {
+ String name
}

Category -> FcUser : owner

Category <- Card : category

package entities.mindmap {
class MindMap {
+ String name
}

abstract class MindMapNode {
+ double x
+ double y
+ boolean collapsed
}

MindMap -|> MindMapNode
MindMapCard -|> MindMapNode

class MindMapLink {
+ String text
+ boolean leftArrow
+ boolean rightArrow
}

MindMapCard -> Card : card
MindMapNode -> MindMapNode : parent
MindMapNode <- MindMap : children
MindMapLink -> MindMapNode : leftNode
MindMapLink -> MindMapNode : rightNode


MindMap -> FcUser : owner


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
