package net.sourceforge.plantuml.nassidiagram;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.UmlSource;

public class NassiDiagramTest {

    @Test
    public void testBasicNassiDiagram() throws IOException {
        String source = "@startnassi\n" +
                       "block \"Initialize x = 0\"\n" +
                       "if \"x < 10\" then\n" +
                       "  block \"Process x\"\n" +
                       "  block \"x = x + 1\"\n" +
                       "else\n" +
                       "  block \"x is too large\"\n" +
                       "endif\n" +
                       "@endnassi";

        UmlSource umlSource = UmlSource.create(source, "test");
        NassiDiagram diagram = new NassiDiagram(umlSource);

        // Test diagram description
        DiagramDescription desc = diagram.getDescription();
        assertEquals("Nassi-Shneiderman diagram", desc.getDescription());

        // Test diagram generation
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        FileFormatOption formatOption = new FileFormatOption(FileFormat.PNG);
        diagram.exportDiagramNow(os, 0, formatOption);

        assertTrue(os.size() > 0, "Diagram should generate non-empty output");
    }
}