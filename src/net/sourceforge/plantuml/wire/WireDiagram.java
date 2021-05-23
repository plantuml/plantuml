/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
 * Original Author:  Arnaud Roques
 *
 *
 */
package net.sourceforge.plantuml.wire;

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.InnerStrategy;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class WireDiagram extends UmlDiagram {

	private final WBlock root = new WBlock("", new UTranslate(), 0, 0, null);
	private final List<Spot> spots = new ArrayList<>();
	private final List<WLinkHorizontal> hlinks = new ArrayList<>();
	private final List<WLinkVertical> vlinks = new ArrayList<>();

	public DiagramDescription getDescription() {
		return new DiagramDescription("Wire Diagram");
	}

	public WireDiagram(UmlSource source) {
		super(source, UmlDiagramType.WIRE);
	}

	@Override
	protected ImageData exportDiagramInternal(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {

		return createImageBuilder(fileFormatOption)
				.drawable(getTextBlock())
				.write(os);
	}

	private TextBlockBackcolored getTextBlock() {
		return new TextBlockBackcolored() {

			public void drawU(UGraphic ug) {
				drawMe(ug);
			}

			public Rectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
				return null;
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				// return getDrawingElement().calculateDimension(stringBounder);
				throw new UnsupportedOperationException();

			}

			public MinMax getMinMax(StringBounder stringBounder) {
				throw new UnsupportedOperationException();
			}

			public HColor getBackcolor() {
				return null;
			}
		};
	}

	private void drawMe(UGraphic ug) {
		root.drawMe(ug);
		for (Spot spot : spots) {
			spot.drawMe(ug);
		}
		for (WLinkHorizontal link : hlinks) {
			link.drawMe(ug);
		}
		for (WLinkVertical link : vlinks) {
			link.drawMe(ug);
		}

	}

	public CommandExecutionResult addComponent(String indent, String name, int width, int height, HColor color) {
		final int level = computeIndentationLevel(indent);
		return this.root.addBlock(level, name, width, height, color);
	}

	public CommandExecutionResult newColumn(String indent) {
		final int level = computeIndentationLevel(indent);
		return this.root.newColumn(level);
	}

	public CommandExecutionResult spot(String name, HColor color, String x, String y) {
		final WBlock block = this.root.getBlock(name);
		if (block == null) {
			return CommandExecutionResult.error("No such element " + name);
		}
		final Spot spot = new Spot(block, color, x, y);
		this.spots.add(spot);
		return CommandExecutionResult.ok();
	}

	public CommandExecutionResult wgoto(String indent, double x, double y) {
		final int level = computeIndentationLevel(indent);
		return this.root.wgoto(level, x, y);
	}

	public CommandExecutionResult wmove(String indent, double x, double y) {
		final int level = computeIndentationLevel(indent);
		return this.root.wmove(level, x, y);
	}

	public CommandExecutionResult print(String indent, String text) {
		final int level = computeIndentationLevel(indent);

		final StringBounder stringBounder = FileFormat.PNG.getDefaultStringBounder();
		return this.root.print(stringBounder, getSkinParam(), level, text);
	}

	private int computeIndentationLevel(String indent) {
		final int level = indent.replace("    ", "\t").length();
		return level;
	}

	public CommandExecutionResult vlink(String name1, String x1, String y1, String name2, WLinkType type,
			WArrowDirection direction, HColor color, Display label) {
		final WBlock block1 = this.root.getBlock(name1);
		if (block1 == null) {
			return CommandExecutionResult.error("No such element " + name1);
		}
		final WBlock block2 = this.root.getBlock(name2);
		if (block2 == null) {
			return CommandExecutionResult.error("No such element " + name2);
		}

		final UTranslate start = block1.getNextOutVertical(x1, y1, type);
		final double destination = block2.getAbsolutePosition("0", "0").getDy();

		this.vlinks.add(new WLinkVertical(getSkinParam(), start, destination, type, direction, color, label));

		return CommandExecutionResult.ok();
	}

	public CommandExecutionResult hlink(String name1, String x1, String y1, String name2, WLinkType type,
			WArrowDirection direction, HColor color, Display label) {
		final WBlock block1 = this.root.getBlock(name1);
		if (block1 == null) {
			return CommandExecutionResult.error("No such element " + name1);
		}
		final WBlock block2 = this.root.getBlock(name2);
		if (block2 == null) {
			return CommandExecutionResult.error("No such element " + name2);
		}

		final UTranslate start = block1.getNextOutHorizontal(x1, y1, type);
		final double destination = block2.getAbsolutePosition("0", "0").getDx();

		this.hlinks.add(new WLinkHorizontal(getSkinParam(), start, destination, type, direction, color, label));

		return CommandExecutionResult.ok();
	}

}
