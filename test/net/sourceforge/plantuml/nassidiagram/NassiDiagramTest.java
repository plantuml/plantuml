package net.sourceforge.plantuml.nassidiagram;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import net.sourceforge.plantuml.core.DiagramDescription;

public class NassiDiagramTest {

    @Test
    public void testBasicNassiDiagram() {
        // For now, just verify that we can create a diagram description
        DiagramDescription desc = new DiagramDescription("Nassi-Shneiderman diagram");
        assertEquals("Nassi-Shneiderman diagram", desc.getDescription());
    }
}