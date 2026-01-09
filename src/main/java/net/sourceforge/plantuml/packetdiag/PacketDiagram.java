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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PacketDiagram extends UmlDiagram {

	private int colWidth = 16;
	private int scaleInterval = colWidth / 2;
	private int nodeHeight = 32;
	private boolean useDefaultScaleInterval = true;
	ScaleDirection scaleDirection = ScaleDirection.LTR;
	final List<PacketItem> packetItems = new ArrayList<>();

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

	boolean isUseDefaultScaleInterval() {
		return useDefaultScaleInterval;
	}

	int getColWidth() {
		return colWidth;
	}

	void updateColWidth(int value) {
		if (value > 0)
			this.colWidth = value;
	}

	void updateScaleInterval(int value) {
		if (value > 0) {
			this.scaleInterval = Math.min(value, this.colWidth);
			this.useDefaultScaleInterval = false;
		}
	}

	void updateNodeHeight(int nodeHeight) {
		if (nodeHeight > 0)
			this.nodeHeight = nodeHeight;
	}

	void addPacketItem(PacketItem packetItem) {
		packetItems.add(packetItem);
	}

	/**
	 * Returns the last packet's end bit-position from the packet frame.
	 * If the system currently contains no packet, return empty.
	 *
	 * @return bit-position of the last packet item in system or empty
	 */
	Optional<Integer> getLastPacketEnd() {
		return packetItems.isEmpty() ?
						Optional.empty() :
						Optional.of(packetItems.get(packetItems.size() - 1).bitEnd);
	}

	enum ScaleDirection {
		LTR,
		RTL
	}

	static class PacketItem {
		final int width;
		final int bitStart;
		final int bitEnd;
		final String desc;
		int textRotation;

		private PacketItem(int width, int bitStart, int bitEnd, String desc) {
			this.width = width;
			this.bitStart = bitStart;
			this.bitEnd = bitEnd;
			this.desc = desc == null ? "" : desc;
		}

		static PacketItem ofOneBit(int start, String desc) {
			return new PacketItem(1, start, start, desc);
		}

		static PacketItem ofRange(int start, int end, String desc) {
			int width = end - start + 1;
			return new PacketItem(width, start, end, desc);
		}
	}
}
