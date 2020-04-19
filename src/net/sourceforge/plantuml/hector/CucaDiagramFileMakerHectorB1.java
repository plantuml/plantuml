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
package net.sourceforge.plantuml.hector;

import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.svek.CucaDiagramFileMaker;
import net.sourceforge.plantuml.svek.GeneralImageBuilder;
import net.sourceforge.plantuml.svek.IEntityImage;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UGraphic2;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

public class CucaDiagramFileMakerHectorB1 implements CucaDiagramFileMaker {

	private final CucaDiagram diagram;
	private SkeletonConfiguration configuration;

	// private double singleWidth;
	// private double singleHeight;
	private double nodeMargin = 40;

	public CucaDiagramFileMakerHectorB1(CucaDiagram diagram) {
		this.diagram = diagram;
	}

	// final private Map<Pin, IEntityImage> images = new LinkedHashMap<Pin, IEntityImage>();
	// final private Map<Pin, Box2D> boxes = new LinkedHashMap<Pin, Box2D>();

	final private Map<Link, PinLink> links = new LinkedHashMap<Link, PinLink>();

	// final private List<Box2D> forbidden = new ArrayList<Box2D>();

	private double getX(Pin pin) {
		return nodeMargin * configuration.getCol(pin);
	}

	private double getY(Pin pin) {
		return nodeMargin * pin.getRow();
	}

	// private double getCenterX(Pin pin) {
	// return singleWidth * configuration.getCol(pin) + singleWidth / 2.0;
	// }
	//
	// private double getCenterY(Pin pin) {
	// return singleHeight * pin.getRow() + singleHeight / 2.0;
	// }

	public ImageData createFile(OutputStream os, List<String> dotStrings, FileFormatOption fileFormatOption)
			throws IOException {
		final PinFactory pinFactory = new PinFactory();
		final SkeletonBuilder skeletonBuilder = new SkeletonBuilder();
		links.clear();
		for (Link link : diagram.getLinks()) {
			final PinLink pinLink = pinFactory.createPinLink(link);
			links.put(link, pinLink);
			skeletonBuilder.add(pinLink);
		}

		final Skeleton skeleton = skeletonBuilder.createSkeletons().get(0);
		this.configuration = SkeletonConfigurationUtils.getBest(skeleton);

		MinMax minMax = MinMax.getEmpty(false);
		for (Pin pin : skeleton.getPins()) {
			minMax = minMax.addPoint(getX(pin), getY(pin));
		}

		final double borderMargin = 10;

		final Dimension2D dimTotal = new Dimension2DDouble(2 * borderMargin + minMax.getMaxX(), 2 * borderMargin
				+ minMax.getMaxY());
		UGraphic2 ug = null;// fileFormatOption.createUGraphic(diagram.getColorMapper(), diagram.getDpiFactor(fileFormatOption),
				// dimTotal, null, false);
		ug = (UGraphic2) ug.apply(new UTranslate(borderMargin, borderMargin));

		for (PinLink pinLink : skeleton.getPinLinks()) {
			drawPinLink(ug, pinLink);
		}

		for (Pin pin : skeleton.getPins()) {
			drawPin(ug, pin);
		}

//		ug.writeImageTOBEMOVED(os, null, diagram.getDpi(fileFormatOption));
//		return new ImageDataSimple(dimTotal);
		throw new UnsupportedOperationException();
	}

	private void drawPin(UGraphic ug, Pin pin) {
		final double x = getX(pin);
		final double y = getY(pin);
		final UEllipse circle = new UEllipse(6, 6);
		ug.apply(HColorUtils.BLACK).apply(HColorUtils.BLACK.bg())
				.apply(new UTranslate(x - 3, y - 3)).draw(circle);
	}

	private void drawPinLink(UGraphic ug, PinLink pinLink) {
		final double x1 = getX(pinLink.getPin1());
		final double y1 = getY(pinLink.getPin1());
		final double x2 = getX(pinLink.getPin2());
		final double y2 = getY(pinLink.getPin2());

		final Rose rose = new Rose();
		final HColor color = rose.getHtmlColor(diagram.getSkinParam(), ColorParam.arrow);
		final List<Box2D> b = new ArrayList<Box2D>();
		final SmartConnection connection = new SmartConnection(x1, y1, x2, y2, b);
		connection.draw(ug, color);
	}

	private IEntityImage computeImage(final ILeaf leaf) {
		final IEntityImage image = GeneralImageBuilder.createEntityImageBlock(leaf, diagram.getSkinParam(),
				false, diagram, null, null, null, diagram.getLinks());
		return image;
	}
}
