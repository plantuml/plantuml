package net.sourceforge.plantuml.nassidiagram;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.skin.UmlDiagramType;

public class NassiDiagram extends UmlDiagram {
    private final List<NassiElement> elements = new ArrayList<>();

    public NassiDiagram(UmlSource source, Map<String, String> skinParam) {
        super(source, UmlDiagramType.NASSI, skinParam);
    }

    @Override
    protected ImageData exportDiagramInternal(OutputStream os, int index, FileFormatOption fileFormatOption)
            throws IOException {
        return createImageBuilder(fileFormatOption)
                .drawable(getTextMainBlock(fileFormatOption))
                .write(os);
    }

    @Override
    public DiagramDescription getDescription() {
        return new DiagramDescription("Nassi-Shneiderman diagram");
    }

    public void addElement(NassiElement element) {
        if (element != null) {
            elements.add(element);
        }
    }

    @Override
    protected TextBlock getTextMainBlock(FileFormatOption fileFormatOption) {
        return new NassiDrawer(elements, getSkinParam());
    }
}