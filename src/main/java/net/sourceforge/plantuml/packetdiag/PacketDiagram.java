/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2026, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  kolulu23
 *
 * 
 */
package net.sourceforge.plantuml.packetdiag;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.skin.UmlDiagramType;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.PName;
import net.sourceforge.plantuml.style.SName;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.style.StyleSignatureBasic;

public class PacketDiagram extends UmlDiagram {

	public static final int DEFAULT_COL_WIDTH = 16;

	/**
	 * Packet width in a row
	 */
	private int colWidth = DEFAULT_COL_WIDTH;

	/**
	 * The width in px of one bit, used as scale
	 */
	private double bitWidth = 24D;

	/**
	 * The height in px of one bit, used as scale
	 */
	private double bitHeight = 32D;

	/**
	 * Bit interval for showing indicator number
	 */
	private int scaleInterval = colWidth / 2;

	/**
	 * Default scale interval is the half of {@link #colWidth}.
	 * By calling {@link #updateScaleInterval(int)} user can override this setting.
	 */
	private boolean useDefaultScaleInterval = true;

	/**
	 * Packet grow direction
	 */
	private ScaleDirection scaleDirection = ScaleDirection.LTR;

	/**
	 * All packets the diagram would draw in declaration order
	 */
	private final List<PacketItem> packetItems = new ArrayList<>();

	/**
	 * All packets rearranged per row for drawing
	 */
	private List<List<PacketBlock>> packetGrid = new ArrayList<>();

	/**
	 * Indicator drawing in order
	 */
	private List<PacketIndicator> packetIndicators = new ArrayList<>();

	/**
	 * Current overall style config for the diagram
	 */
	private Style style;

	public PacketDiagram(UmlSource source, PreprocessingArtifact preprocessing) {
		super(source, UmlDiagramType.PACKET, null, preprocessing);
	}

	@Override
	protected ImageData exportDiagramInternal(OutputStream os, int index, FileFormatOption fileFormatOption) throws IOException {
		return createImageBuilder(fileFormatOption).drawable(getTextMainBlock(fileFormatOption)).write(os);
	}

	@Override
	protected TextBlock getTextMainBlock(FileFormatOption fileFormatOption) {
		return new AbstractTextBlock() {
			@Override
			public XDimension2D calculateDimension(StringBounder stringBounder) {
				return null;
			}

			@Override
			public void drawU(UGraphic ug) {
				double maxWidth = 0D;
				double maxHeight = 0D;

				for (PacketIndicator indicator : packetIndicators) {
					indicator.drawU(ug);
					ug = ug.apply(new UTranslate(bitWidth, 0D));

					XDimension2D iDim = indicator.calculateDimension(ug.getStringBounder());
					maxWidth = maxWidth + bitWidth;
					maxHeight = Math.max(iDim.getHeight(), maxHeight);
				}
				ug = ug.apply(new UTranslate(-maxWidth, maxHeight));

				// Draw packets
				for (List<PacketBlock> blocks : packetGrid) {
					double totalWidth = 0D;
					double drawHeight = 0D;
					for (PacketBlock block : blocks) {
						TextBlock tb = block.getShapeTextBlock(ug.getStringBounder(), bitWidth, bitHeight);
						XDimension2D dim = tb.calculateDimension(ug.getStringBounder());

						totalWidth += dim.getWidth();
						drawHeight = dim.getHeight();
						tb.drawU(ug);
						ug = ug.apply(new UTranslate(dim.getWidth(), 0D));
					}
					ug = ug.apply(new UTranslate(-totalWidth, drawHeight));
				}
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

	public int getColWidth() {
		return colWidth;
	}

	public void setColWidth(int colWidth) {
		if (colWidth > 0)
			this.colWidth = colWidth;
	}

	/**
	 * Bit interval for showing full length indicator
	 */
	int getFullIndicatorInterval() {
		return colWidth >= 4 ? colWidth / 4 : colWidth;
	}

	void updateScaleInterval(int value) {
		if (value > 0) {
			this.scaleInterval = Math.min(value, this.colWidth);
			this.useDefaultScaleInterval = false;
		}
	}

	void updateNodeHeight(int nodeHeight) {
		this.bitHeight = Math.max(nodeHeight, 0);
	}

	void setScaleDirection(ScaleDirection scaleDirection) {
		this.scaleDirection = scaleDirection;
	}

	void addPacketItem(PacketItem packetItem) {
		packetItems.add(packetItem);
	}

	public Style getStyle() {
		if (style == null) {
			style = StyleSignatureBasic.of(SName.root, SName.element, SName.packetdiagDiagram)
							.getMergedStyle(getSkinParam().getCurrentStyleBuilder());
		}
		return style;
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

	public void build() {
		if (isUseDefaultScaleInterval()) {
			updateScaleInterval(getColWidth() / 2);
		}
		adjustLayout();
		adjustColWidth();
		adjustIndicators();
		adjustBitWidth();
	}

	/**
	 * Keeps column width under control
	 */
	void adjustColWidth() {
		if (!this.packetGrid.isEmpty()) {
			int maxWidth = this.packetGrid.get(0).stream().mapToInt(PacketBlock::getWidth).sum();
			this.colWidth = Math.min(maxWidth, this.colWidth);
		}
	}

	void adjustIndicators() {
		final int fullLengthInterval = getFullIndicatorInterval();
		IntStream idxStream = IntStream.iterate(0, i -> i + 1).limit(colWidth + 1);
		if (scaleDirection == ScaleDirection.RTL) {
			idxStream = IntStream.iterate(colWidth, i -> i - 1).limit(colWidth + 1);
		}
		this.packetIndicators = idxStream.mapToObj(i -> {
			boolean full = i % fullLengthInterval == 0;
			boolean numbered = i % scaleInterval == 0;
			return new PacketIndicator(full, numbered, i, getStyle(), getSkinParam());
		}).collect(Collectors.toList());
	}

	/**
	 * Rearranges packets into one grid, split long packets if it went out of the row
	 */
	void adjustLayout() {
		List<List<PacketBlock>> grid = new ArrayList<>();
		List<PacketBlock> currRow = createPacketRow();
		int remainRowWidth = colWidth;
		for (PacketItem packet : packetItems) {
			Optional<PacketItem> op = fitPacketInRow(packet, remainRowWidth, currRow);
			if (op.isPresent()) {
				remainRowWidth = colWidth;
				grid.add(currRow);
				currRow = createPacketRow();

				PacketItem p = op.get();
				int rowCnt = p.width / remainRowWidth;
				int remain = p.width % remainRowWidth;
				for (int i = 0; i < rowCnt; i++) {
					PacketBlock pb = p.toPacketBlock(remainRowWidth, true, true, getSkinParam());
					grid.add(createPacketRow(pb));
				}
				if (remain > 0) {
					PacketBlock pb = p.toPacketBlock(remainRowWidth, true, false, getSkinParam());
					currRow.add(pb);
					remainRowWidth -= remain;
				} else {
					grid.get(grid.size() - 1).get(0).openRight(false);
				}
			} else {
				remainRowWidth -= packet.width;
				if (remainRowWidth == 0) {
					remainRowWidth = colWidth;
					grid.add(currRow);
					currRow = createPacketRow();
				}
			}
		}
		if (!currRow.isEmpty()) {
			grid.add(currRow);
		}
		// Equalize packet height, this behavior is not in the original implementation, but I find it make sense to do so
		grid.forEach(blocks -> {
			final int maxH = blocks.stream().map(PacketBlock::getHeight).max(Integer::compare).orElse(1);
			blocks.forEach(blk -> blk.setHeight(maxH));
			if (scaleDirection == ScaleDirection.RTL) {
				Collections.reverse(blocks);
			}
		});

		this.packetGrid = grid;
	}

	/**
	 * Adjust the draw width for one bit based on font size
	 */
	void adjustBitWidth() {
		final double bitScale = 3.0;
		this.bitWidth = getStyle().value(PName.FontSize).asDouble() * bitScale;
	}

	private static List<PacketBlock> createPacketRow(PacketBlock... pbs) {
		ArrayList<PacketBlock> row = new ArrayList<>();
		Collections.addAll(row, pbs);
		return row;
	}

	private Optional<PacketItem> fitPacketInRow(PacketItem packet, int remainRowWidth, List<PacketBlock> row) {
		assert remainRowWidth > 0;
		int overflow = packet.width - remainRowWidth;
		if (overflow > 0) {
			int margin = packet.width - remainRowWidth;
			int bitEnd = packet.bitEnd - margin;
			row.add(packet.toPacketBlock(remainRowWidth, false, true, getSkinParam()));
			return Optional.of(new PacketItem(margin, packet.height, bitEnd, packet.bitEnd, packet.desc));
		} else {
			row.add(packet.toPacketBlock(getSkinParam()));
			return Optional.empty();
		}
	}

	enum ScaleDirection {
		/**
		 * Left to right
		 */
		LTR,
		/**
		 * Right to left
		 */
		RTL
	}

	static class PacketItem {
		final int width;
		final int height;
		final int bitStart;
		final int bitEnd;
		final String desc;
		int textRotation;

		private PacketItem(int width, int height, int bitStart, int bitEnd, String desc) {
			this.width = width;
			this.height = height;
			this.bitStart = bitStart;
			this.bitEnd = bitEnd;
			this.desc = desc == null ? "" : desc;
		}

		static PacketItem ofRange(int start, int end, int height, String desc) {
			int width = end - start + 1;
			return new PacketItem(width, height, start, end, desc);
		}

		PacketBlock toPacketBlock(ISkinParam skinParam) {
			return new PacketBlock(width, height, desc, skinParam);
		}

		PacketBlock toPacketBlock(int newWidth, boolean openLeft, boolean openRight, ISkinParam skinParam) {
			return new PacketBlock(Math.min(newWidth, width), height, desc, skinParam, openLeft, openRight);
		}
	}
}
