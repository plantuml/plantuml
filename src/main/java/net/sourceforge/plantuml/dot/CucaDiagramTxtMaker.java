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
package net.sourceforge.plantuml.dot;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.atmp.CucaDiagram;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.EntityPortion;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.asciiart.BasicCharArea;
import net.sourceforge.plantuml.cucadiagram.PortionShower;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.txt.UGraphicTxt;
import net.sourceforge.plantuml.klimt.geom.XCubicCurve2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.DotPath;
import net.sourceforge.plantuml.posimo.Block;
import net.sourceforge.plantuml.posimo.Cluster;
import net.sourceforge.plantuml.posimo.GraphvizSolverB;
import net.sourceforge.plantuml.posimo.Path;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.security.SecurityUtils;
import net.sourceforge.plantuml.skin.Pragma;

public final class CucaDiagramTxtMaker {
	// ::remove file when __CORE__

	// private final CucaDiagram diagram;
	private final FileFormat fileFormat;
	private final UGraphicTxt globalUg = new UGraphicTxt();
	private final PortionShower portionShower;
	private final Pragma pragma;

	private static double getXPixelPerChar() {
		return 5;
	}

	private static double getYPixelPerChar() {
		return 10;
	}

	private boolean showMember(Entity entity) {
		final boolean showMethods = portionShower.showPortion(EntityPortion.METHOD, entity);
		final boolean showFields = portionShower.showPortion(EntityPortion.FIELD, entity);
		return showMethods || showFields;
	}

	public CucaDiagramTxtMaker(CucaDiagram diagram, FileFormat fileFormat) throws IOException {
		this.fileFormat = fileFormat;
		this.portionShower = diagram;
		this.pragma = diagram.getPragma();

		final Cluster root = new Cluster(null, 0, 0);
		int uid = 0;

		final Map<Entity, Block> blocks = new HashMap<Entity, Block>();

		for (Entity ent : diagram.leafs()) {
			// printClass(ent);
			// ug.translate(0, getHeight(ent) + 1);
			final double width = getWidth(ent) * getXPixelPerChar();
			final double height = getHeight(ent) * getYPixelPerChar();
			final Block b = new Block(uid++, width, height, null);
			root.addBloc(b);
			blocks.put(ent, b);
		}

		if (blocks.size() > 1) {
			final GraphvizSolverB solver = new GraphvizSolverB();

			final Collection<Path> paths = new ArrayList<>();
			for (Link link : diagram.getLinks()) {
				final Block b1 = blocks.get(link.getEntity1());
				final Block b2 = blocks.get(link.getEntity2());
				if (b1 != null && b2 != null)
					paths.add(new Path(b1, b2, null, link.getLength(), link.isInvis()));
			}
			solver.solve(root, paths);
			for (Path p : paths) {
				if (p.isInvis())
					continue;

				drawDotPath(p.getDotPath(), globalUg.getCharArea(), getXPixelPerChar(), getYPixelPerChar());
			}

		}

		for (Entity ent : diagram.leafs()) {
			final Block b = blocks.get(ent);
			final XPoint2D p = b.getPosition();
			printClass(ent, (UGraphicTxt) globalUg
					.apply(new UTranslate(p.getX() / getXPixelPerChar(), p.getY() / getYPixelPerChar())));
		}

	}

	private void drawDotPath(DotPath dotPath, BasicCharArea area, double pixelXPerChar, double pixelYPerChar) {
		for (XCubicCurve2D bez : dotPath.getBeziers())
			if (bez.x1 == bez.x2)
				area.drawVLine('|', (int) (bez.x1 / pixelXPerChar), (int) (bez.y1 / pixelYPerChar),
						(int) (bez.y2 / pixelYPerChar));
			else if (bez.y1 == bez.y2)
				area.drawHLine('-', (int) (bez.y1 / pixelYPerChar), (int) (bez.x1 / pixelXPerChar),
						(int) (bez.x2 / pixelXPerChar));

	}

	private void printClass(final Entity ent, UGraphicTxt ug) {
		if (fileFormat == FileFormat.UTXT)
			drawClassUnicode(ent, ug);
		else
			drawClassSimple(ent, ug);
	}

	private void drawClassSimple(final Entity ent, UGraphicTxt ug) {
		final int w = getWidth(ent);
		final int h = getHeight(ent);
		ug.getCharArea().drawBoxSimple(0, 0, w, h);
		ug.getCharArea().drawStringsLRSimple(ent.getDisplay().asList(), 1, 1);
		if (showMember(ent)) {
			int y = 2;
			ug.getCharArea().drawHLine('-', y, 1, w - 1);
			y++;
			for (CharSequence att : ent.getBodier().getRawBody()) {
				final List<String> disp = Display.getWithNewlines3(att.toString());
				ug.getCharArea().drawStringsLRSimple(disp, 1, y);
				y += StringUtils.getHeight(disp);
			}
//			for (Member att : ent.getBodier().getFieldsToDisplay()) {
//				final List<String> disp = BackSlash.getWithNewlines(att.getDisplay(true));
//				ug.getCharArea().drawStringsLR(disp, 1, y);
//				y += StringUtils.getHeight(disp);
//			}
//			ug.getCharArea().drawHLine('-', y, 1, w - 1);
//			y++;
//			for (Member att : ent.getBodier().getMethodsToDisplay()) {
//				final List<String> disp = BackSlash.getWithNewlines(att.getDisplay(true));
//				ug.getCharArea().drawStringsLR(disp, 1, y);
//				y += StringUtils.getHeight(disp);
//			}
		}
	}

	private void drawClassUnicode(final Entity ent, UGraphicTxt ug) {
		final int w = getWidth(ent);
		final int h = getHeight(ent);
		ug.getCharArea().drawBoxSimpleUnicode(0, 0, w, h);
		ug.getCharArea().drawStringsLRUnicode(ent.getDisplay().asList(), 1, 1);
		if (showMember(ent)) {
			int y = 2;
			ug.getCharArea().drawHLine('\u2500', y, 1, w - 1);
			ug.getCharArea().drawChar('\u251C', 0, y);
			ug.getCharArea().drawChar('\u2524', w - 1, y);
			y++;
			for (CharSequence att : ent.getBodier().getRawBody()) {
				final List<String> disp = Display.getWithNewlines3(att.toString());
				ug.getCharArea().drawStringsLRUnicode(disp, 1, y);
				y += StringUtils.getHeight(disp);
			}
		}
	}

	public List<SFile> createFiles(SFile suggestedFile) throws IOException {
		if (fileFormat == FileFormat.UTXT)
			globalUg.getCharArea().print(suggestedFile.createPrintStream(UTF_8));
		else
			globalUg.getCharArea().print(suggestedFile.createPrintStream());

		return Collections.singletonList(suggestedFile);
	}

	private int getHeight(Entity entity) {
		int result = StringUtils.getHeight(entity.getDisplay());
		if (showMember(entity)) {
			for (CharSequence att : entity.getBodier().getRawBody())
				result += StringUtils.getHeight(Display.getWithNewlines(pragma, att.toString()));

//			for (Member att : entity.getBodier().getMethodsToDisplay()) {
//				result += StringUtils.getHeight(Display.getWithNewlines(att.getDisplay(true)));
//			}
//			result++;
//			for (Member att : entity.getBodier().getFieldsToDisplay()) {
//				result += StringUtils.getHeight(Display.getWithNewlines(att.getDisplay(true)));
//			}
//			result++;
		}
		return result + 3;
	}

	private int getWidth(Entity entity) {
		int result = StringUtils.getWcWidth(entity.getDisplay());
		if (showMember(entity)) {
			for (CharSequence att : entity.getBodier().getRawBody()) {
				final int w = StringUtils.getWcWidth(Display.getWithNewlines(pragma, att.toString()));
				if (w > result)
					result = w;

			}
//			for (Member att : entity.getBodier().getMethodsToDisplay()) {
//			final int w = StringUtils.getWcWidth(Display.getWithNewlines(att.getDisplay(true)));
//			if (w > result) {
//				result = w;
//			}
//		}
//			for (Member att : entity.getBodier().getFieldsToDisplay()) {
//				final int w = StringUtils.getWcWidth(Display.getWithNewlines(att.getDisplay(true)));
//				if (w > result) {
//					result = w;
//				}
//			}
		}
		return result + 2;
	}

	public void createFiles(OutputStream os, int index) {
		globalUg.getCharArea().print(SecurityUtils.createPrintStream(os));
	}

}
