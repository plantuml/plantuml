/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 6711 $
 *
 */
package net.sourceforge.plantuml.hector;

import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.svek.CucaDiagramFileMaker;
import net.sourceforge.plantuml.svek.CucaDiagramFileMakerSvek2;
import net.sourceforge.plantuml.svek.IEntityImage;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class CucaDiagramFileMakerHector2 implements CucaDiagramFileMaker {

	private final CucaDiagram diagram;
	private SkeletonConfiguration configuration;

	private double singleWidth;
	private double singleHeight;
	private double nodeMargin = 40;

	public CucaDiagramFileMakerHector2(CucaDiagram diagram) {
		this.diagram = diagram;
	}

	final private Map<Pin, IEntityImage> images = new LinkedHashMap<Pin, IEntityImage>();

	final private Map<Link, PinLink> links = new LinkedHashMap<Link, PinLink>();

	private double getX(Pin pin) {
		return singleWidth * configuration.getCol(pin);
	}

	private double getY(Pin pin) {
		return singleHeight * pin.getRow();
	}

	private double getCenterX(Pin pin) {
		return singleWidth * configuration.getCol(pin) + singleWidth / 2.0;
	}

	private double getCenterY(Pin pin) {
		return singleHeight * pin.getRow() + singleHeight / 2.0;
	}

	public ImageData createFile(OutputStream os, List<String> dotStrings, FileFormatOption fileFormatOption)
			throws IOException {
		final PinFactory pinFactory = new PinFactory();
		final SkeletonBuilder skeletonBuilder = new SkeletonBuilder();
		for (Link link : diagram.getLinks()) {
			final PinLink pinLink = pinFactory.createPinLink(link);
			links.put(link, pinLink);
			skeletonBuilder.add(pinLink);
		}

		final Skeleton skeleton = skeletonBuilder.createSkeletons().get(0);
		this.configuration = SkeletonConfiguration.getDefault(skeleton);

		this.singleWidth = 0;
		this.singleHeight = 0;

		for (Pin pin : skeleton.getPins()) {
			final ILeaf leaf = (ILeaf) pin.getUserData();
			final IEntityImage image = computeImage(leaf);
			final Dimension2D dim = TextBlockUtils.getDimension(image);
			if (dim.getWidth() > singleWidth) {
				singleWidth = dim.getWidth();
			}
			if (dim.getHeight() > singleHeight) {
				singleHeight = dim.getHeight();
			}
			images.put(pin, image);
		}
		singleHeight += nodeMargin;
		singleWidth += nodeMargin;

		MinMax minMax = MinMax.getEmpty(false);
		for (Pin pin : skeleton.getPins()) {
			minMax = minMax.addPoint(getX(pin), getY(pin));
			minMax = minMax.addPoint(getX(pin) + singleWidth, getY(pin) + singleHeight);
		}

		final double borderMargin = 10;

		final Dimension2D dimTotal = new Dimension2DDouble(2 * borderMargin + minMax.getMaxX(), 2 * borderMargin
				+ minMax.getMaxY());
		UGraphic ug = fileFormatOption.createUGraphic(diagram.getColorMapper(), diagram.getDpiFactor(fileFormatOption),
				dimTotal, null, false);
		ug = ug.apply(new UTranslate(borderMargin, borderMargin));

		for (PinLink pinLink : skeleton.getPinLinks()) {
			drawPinLink(ug, pinLink);
		}

		for (Map.Entry<Pin, IEntityImage> ent : images.entrySet()) {
			final Pin pin = ent.getKey();
			final IEntityImage im = ent.getValue();
			final double x = getX(pin);
			final double y = getY(pin);
			final Dimension2D dimImage = im.calculateDimension(ug.getStringBounder());
			im.drawU(ug.apply(new UTranslate(x + (singleWidth - dimImage.getWidth()) / 2, y
					+ (singleHeight - dimImage.getHeight()) / 2)));
		}

		ug.writeImage(os, null, diagram.getDpi(fileFormatOption));
		return new ImageDataSimple(dimTotal);
	}

	private void drawPinLink(UGraphic ug, PinLink pinLink) {
		final Rose rose = new Rose();
		final HtmlColor color = rose.getHtmlColor(diagram.getSkinParam(), ColorParam.classArrow);
		ug = ug.apply(new UChangeColor(color)).apply(new UStroke(1.5));
		final double x1 = getCenterX(pinLink.getPin1());
		final double y1 = getCenterY(pinLink.getPin1());
		final double x2 = getCenterX(pinLink.getPin2());
		final double y2 = getCenterY(pinLink.getPin2());
		ug = ug.apply(new UTranslate(x1, y1));
		ug.draw(new ULine(x2 - x1, y2 - y1));

	}

	private IEntityImage computeImage(final ILeaf leaf) {
		final IEntityImage image = CucaDiagramFileMakerSvek2.createEntityImageBlock(leaf, diagram.getSkinParam(),
				false, diagram, null);
		return image;
	}
}
