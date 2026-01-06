package net.sourceforge.plantuml.packetdiag;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.skin.UmlDiagramType;

import java.io.IOException;
import java.io.OutputStream;

public class PacketDiagram extends UmlDiagram {
	public PacketDiagram(UmlSource source, PreprocessingArtifact preprocessing) {
		super(source, UmlDiagramType.PACKET, null, preprocessing);
	}

	@Override
	protected ImageData exportDiagramInternal(OutputStream os, int index, FileFormatOption fileFormatOption) throws IOException {
		return null;
	}

	@Override
	protected TextBlock getTextMainBlock(FileFormatOption fileFormatOption) {
		return new AbstractTextBlock() {
			@Override
			public XDimension2D calculateDimension(StringBounder stringBounder) {
				// TODO
				return null;
			}

			@Override
			public void drawU(UGraphic ug) {
				// TODO
			}
		};
	}

	@Override
	public DiagramDescription getDescription() {
		return new DiagramDescription("Packet Diagram");
	}
}
