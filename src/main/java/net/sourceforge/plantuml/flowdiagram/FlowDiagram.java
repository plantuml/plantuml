/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
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
 * Original Author:  Arnaud Roques
 *
 *
 */
package net.sourceforge.plantuml.flowdiagram;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import net.atmp.ImageBuilder;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.golem.MinMaxGolem;
import net.sourceforge.plantuml.golem.Path;
import net.sourceforge.plantuml.golem.Position;
import net.sourceforge.plantuml.golem.Tile;
import net.sourceforge.plantuml.golem.TileArea;
import net.sourceforge.plantuml.golem.TileGeometry;
import net.sourceforge.plantuml.golem.TilesField;
import net.sourceforge.plantuml.klimt.UShape;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.MagneticBorder;
import net.sourceforge.plantuml.klimt.geom.MagneticBorderNone;
import net.sourceforge.plantuml.klimt.geom.MinMax;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.geom.XRectangle2D;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.UEllipse;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.skin.UmlDiagramType;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;

public class FlowDiagram extends UmlDiagram implements TextBlock {
	// ::remove folder when __HAXE__

	private static double SINGLE_SIZE_X = 100;
	private static double SINGLE_SIZE_Y = 35;

	private TilesField field;
	private final Map<Tile, ActivityBox> tilesBoxes = new HashMap<Tile, ActivityBox>();
	private Tile lastTile;

	@Override
	public XRectangle2D getInnerPosition(CharSequence member, StringBounder stringBounder) {
		throw new UnsupportedOperationException();
	}

	public DiagramDescription getDescription() {
		return new DiagramDescription("Flow Diagram");
	}

	public FlowDiagram(UmlSource source, PreprocessingArtifact preprocessing) {
		super(source, UmlDiagramType.FLOW, null, preprocessing);
	}

	public void lineSimple(TileGeometry orientation, String idDest, String label) {
		final Tile newTile;
		if (field == null) {
			field = new TilesField();
			tilesBoxes.clear();
			newTile = field.getRoot();
		} else {
			newTile = field.createTile(lastTile, orientation);
		}
		final ActivityBox box = new ActivityBox(newTile, idDest, label);
		tilesBoxes.put(newTile, box);
		lastTile = newTile;
		return;
	}

	public void linkSimple(TileGeometry orientation, String idDest) {
		final Tile tile = getTileById(idDest);
		field.addPath(lastTile, tile, orientation);
	}

	private Tile getTileById(String id) {
		for (Map.Entry<Tile, ActivityBox> ent : tilesBoxes.entrySet()) {
			if (ent.getValue().getId().equals(id)) {
				return ent.getKey();
			}
		}
		throw new IllegalArgumentException(id);
	}

	@Override
	public ImageBuilder createImageBuilder(FileFormatOption fileFormatOption) throws IOException {
		return ImageBuilder.create(fileFormatOption)
				.dimension(calculateDimension(fileFormatOption.getDefaultStringBounder(getSkinParam())))
				.margin(getDefaultMargins()).metadata(fileFormatOption.isWithMetadata() ? getMetadata() : null)
				.seed(seed());
	}

	@Override
	protected ImageData exportDiagramInternal(OutputStream os, int index, FileFormatOption fileFormatOption)
			throws IOException {

		return createImageBuilder(fileFormatOption).drawable(this).write(os);
	}

	public void drawU(UGraphic ug) {
		double x = 0;
		double y = 0;
		final MinMaxGolem minMax = getMinMax();
		x -= minMax.getMinX() * SINGLE_SIZE_X;
		y -= minMax.getMinY() * SINGLE_SIZE_Y;
		final StringBounder stringBounder = ug.getStringBounder();
		for (Map.Entry<Tile, ActivityBox> ent : tilesBoxes.entrySet()) {
			final Tile tile = ent.getKey();
			final Position pos = field.getPosition(tile);
			final int xmin = pos.getXmin();
			final int ymin = pos.getYmin();
			final ActivityBox box = ent.getValue();
			final XDimension2D dimBox = box.calculateDimension(stringBounder);
			final double deltaX = SINGLE_SIZE_X * 2 - dimBox.getWidth();
			final double deltaY = SINGLE_SIZE_Y * 2 - dimBox.getHeight();
			box.drawU(ug.apply(
					new UTranslate((x + xmin * SINGLE_SIZE_X + deltaX / 2), (y + ymin * SINGLE_SIZE_Y + deltaY / 2))));
		}
		ug = ug.apply(HColors.MY_RED);
		ug = ug.apply(HColors.MY_RED.bg());
		final UShape arrow = UEllipse.build(7, 7);
		for (Path p : field.getPaths()) {
			final TileArea start = p.getStart();
			final TileArea dest = p.getDest();
			final XPoint2D pStart = movePoint(getCenter(start), start.getTile(), start.getGeometry(), stringBounder);
			final XPoint2D pDest = movePoint(getCenter(dest), dest.getTile(), dest.getGeometry(), stringBounder);
			final ULine line = new ULine(pDest.getX() - pStart.getX(), pDest.getY() - pStart.getY());
			ug.apply(new UTranslate(x + pStart.getX(), y + pStart.getY())).draw(line);
			ug.apply(new UTranslate(x + pDest.getX() - 3, y + pDest.getY() - 3)).draw(arrow);

		}
	}

	private XPoint2D getCenter(TileArea area) {
		final Tile tile = area.getTile();
		final Position position = field.getPosition(tile);
		final double x = position.getCenterX();
		final double y = position.getCenterY();
		return new XPoint2D(x * SINGLE_SIZE_X, y * SINGLE_SIZE_Y);
	}

	private XPoint2D movePoint(XPoint2D pt, Tile tile, TileGeometry tileGeometry, StringBounder stringBounder) {
		final XDimension2D dim = tilesBoxes.get(tile).calculateDimension(stringBounder);
		final double width = dim.getWidth();
		final double height = dim.getHeight();
		double x = pt.getX();
		double y = pt.getY();
		switch (tileGeometry) {
		case SOUTH:
			y += height / 2;
			break;
		case NORTH:
			y -= height / 2;
			break;
		case EAST:
			x += width / 2;
			break;
		case WEST:
			x -= width / 2;
			break;
		default:
			throw new IllegalStateException();
		}
		return new XPoint2D(x, y);
	}

	private MinMaxGolem getMinMax() {
		final MinMaxGolem minMax = new MinMaxGolem();
		for (Tile tile : tilesBoxes.keySet()) {
			minMax.manage(field.getPosition(tile));
		}
		return minMax;
	}

	public XDimension2D calculateDimension(StringBounder stringBounder) {
		final MinMaxGolem minMax = getMinMax();
		return new XDimension2D(minMax.getWidth() * SINGLE_SIZE_X, minMax.getHeight() * SINGLE_SIZE_Y);
	}

	public MinMax getMinMax(StringBounder stringBounder) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ClockwiseTopRightBottomLeft getDefaultMargins() {
		return ClockwiseTopRightBottomLeft.same(0);
	}

	@Override
	protected TextBlock getTextMainBlock(FileFormatOption fileFormatOption) {
		return this;
	}

	@Override
	public MagneticBorder getMagneticBorder() {
		return new MagneticBorderNone();
	}

	@Override
	public HColor getBackcolor() {
		return null;
	}
}
