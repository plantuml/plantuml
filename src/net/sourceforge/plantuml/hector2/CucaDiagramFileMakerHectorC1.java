/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
package net.sourceforge.plantuml.hector2;

import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.hector2.continuity.Skeleton;
import net.sourceforge.plantuml.hector2.continuity.SkeletonBuilder;
import net.sourceforge.plantuml.hector2.graphic.Foo2;
import net.sourceforge.plantuml.hector2.layering.Layer;
import net.sourceforge.plantuml.hector2.layering.LayerFactory;
import net.sourceforge.plantuml.hector2.mpos.Distribution;
import net.sourceforge.plantuml.hector2.mpos.MutationLayer;
import net.sourceforge.plantuml.svek.CucaDiagramFileMaker;
import net.sourceforge.plantuml.ugraphic.UGraphic2;

public class CucaDiagramFileMakerHectorC1 implements CucaDiagramFileMaker {

	private final CucaDiagram diagram;

	public CucaDiagramFileMakerHectorC1(CucaDiagram diagram) {
		this.diagram = diagram;
	}

	public ImageData createFile(OutputStream os, List<String> dotStrings, FileFormatOption fileFormatOption)
			throws IOException {
		final SkeletonBuilder skeletonBuilder = new SkeletonBuilder();
		for (Link link : diagram.getLinks()) {
			skeletonBuilder.add(link);
		}
		final List<Skeleton> skeletons = skeletonBuilder.getSkeletons();
		if (skeletons.size() != 1) {
			throw new UnsupportedOperationException("size=" + skeletons.size());
		}
		final List<Layer> layers = new LayerFactory().getLayers(skeletons.get(0));
		// System.err.println("layers=" + layers);

		final Distribution distribution = new Distribution(layers);
		final double cost1 = distribution.cost(diagram.getLinks());
		System.err.println("cost1=" + cost1);

		final List<MutationLayer> mutations = distribution.getPossibleMutations();
		for (MutationLayer m : mutations) {
			System.err.println(m.toString());
			final Distribution muted = distribution.mute(m);
			final double cost2 = muted.cost(diagram.getLinks());
			System.err.println("cost2=" + cost2);
		}

		final Foo2 foo2 = new Foo2(distribution, diagram);

		final Dimension2D dimTotal = foo2.calculateDimension(TextBlockUtils.getDummyStringBounder());

		UGraphic2 ug = null; //fileFormatOption.createUGraphic(diagram.getColorMapper(), diagram.getDpiFactor(fileFormatOption),
				//dimTotal, null, false);
		foo2.drawU(ug);

//		ug.writeImageTOBEMOVED(os, null, diagram.getDpi(fileFormatOption));
//		return new ImageDataSimple(dimTotal);
		throw new UnsupportedOperationException();
	}

}
