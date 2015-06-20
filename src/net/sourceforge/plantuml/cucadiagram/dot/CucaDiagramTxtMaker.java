/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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
 * Revision $Revision: 5079 $
 *
 */
package net.sourceforge.plantuml.cucadiagram.dot;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.Member;
import net.sourceforge.plantuml.posimo.Block;
import net.sourceforge.plantuml.posimo.Cluster;
import net.sourceforge.plantuml.posimo.GraphvizSolverB;
import net.sourceforge.plantuml.posimo.Path;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.txt.UGraphicTxt;
import net.sourceforge.plantuml.StringUtils;

public final class CucaDiagramTxtMaker {

	// private final CucaDiagram diagram;
	private final FileFormat fileFormat;
	private final UGraphicTxt globalUg = new UGraphicTxt();

	private static double getXPixelPerChar() {
		return 5;
	}

	private static double getYPixelPerChar() {
		return 10;
	}

	public CucaDiagramTxtMaker(CucaDiagram diagram, FileFormat fileFormat) throws IOException {
		// this.diagram = diagram;
		this.fileFormat = fileFormat;

		final Cluster root = new Cluster(null, 0, 0);
		int uid = 0;

		final Map<IEntity, Block> blocks = new HashMap<IEntity, Block>();

		for (IEntity ent : diagram.getLeafsvalues()) {
			// printClass(ent);
			// ug.translate(0, getHeight(ent) + 1);
			final double width = getWidth(ent) * getXPixelPerChar();
			final double height = getHeight(ent) * getYPixelPerChar();
			final Block b = new Block(uid++, width, height, null);
			root.addBloc(b);
			blocks.put(ent, b);
		}

		final GraphvizSolverB solver = new GraphvizSolverB();

		final Collection<Path> paths = new ArrayList<Path>();
		for (Link link : diagram.getLinks()) {
			final Block b1 = blocks.get(link.getEntity1());
			final Block b2 = blocks.get(link.getEntity2());
			paths.add(new Path(b1, b2, null, link.getLength()));
		}
		solver.solve(root, paths);
		for (Path p : paths) {
			p.getDotPath().draw(globalUg.getCharArea(), getXPixelPerChar(), getYPixelPerChar());
		}
		for (IEntity ent : diagram.getLeafsvalues()) {
			final Block b = blocks.get(ent);
			final Point2D p = b.getPosition();
			printClass(
					ent,
					(UGraphicTxt) globalUg.apply(new UTranslate(p.getX() / getXPixelPerChar(), p.getY()
							/ getYPixelPerChar())));
		}

	}

	private void printClass(final IEntity ent, UGraphicTxt ug) {
		final int w = getWidth(ent);
		final int h = getHeight(ent);
		ug.getCharArea().drawBoxSimple(0, 0, w, h);
		ug.getCharArea().drawStringsLR(ent.getDisplay().as(), 1, 1);
		int y = 2;
		ug.getCharArea().drawHLine('-', y, 1, w - 1);
		y++;
		for (Member att : ent.getBodier().getFieldsToDisplay()) {
			final List<String> disp = StringUtils.getWithNewlines(att.getDisplay(true));
			ug.getCharArea().drawStringsLR(disp, 1, y);
			y += StringUtils.getHeight(disp);
		}
		ug.getCharArea().drawHLine('-', y, 1, w - 1);
		y++;
		for (Member att : ent.getBodier().getMethodsToDisplay()) {
			final List<String> disp = StringUtils.getWithNewlines(att.getDisplay(true));
			ug.getCharArea().drawStringsLR(disp, 1, y);
			y += StringUtils.getHeight(disp);
		}
	}

	public List<File> createFiles(File suggestedFile) throws IOException {
		if (fileFormat == FileFormat.UTXT) {
			globalUg.getCharArea().print(new PrintStream(suggestedFile, "UTF-8"));
		} else {
			globalUg.getCharArea().print(new PrintStream(suggestedFile));
		}
		return Collections.singletonList(suggestedFile);
	}

	private int getHeight(IEntity entity) {
		int result = StringUtils.getHeight(entity.getDisplay());
		for (Member att : entity.getBodier().getMethodsToDisplay()) {
			result += StringUtils.getHeight(Display.getWithNewlines(att.getDisplay(true)));
		}
		for (Member att : entity.getBodier().getFieldsToDisplay()) {
			result += StringUtils.getHeight(Display.getWithNewlines(att.getDisplay(true)));
		}
		return result + 4;
	}

	private int getWidth(IEntity entity) {
		int result = StringUtils.getWidth(entity.getDisplay());
		for (Member att : entity.getBodier().getMethodsToDisplay()) {
			final int w = StringUtils.getWidth(Display.getWithNewlines(att.getDisplay(true)));
			if (w > result) {
				result = w;
			}
		}
		for (Member att : entity.getBodier().getFieldsToDisplay()) {
			final int w = StringUtils.getWidth(Display.getWithNewlines(att.getDisplay(true)));
			if (w > result) {
				result = w;
			}
		}
		return result + 2;
	}

	public void createFiles(OutputStream os, int index) {
		globalUg.getCharArea().print(new PrintStream(os));
	}

}
