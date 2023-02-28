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
package net.sourceforge.plantuml.posimo;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.dot.Graphviz;
import net.sourceforge.plantuml.dot.GraphvizUtils;
import net.sourceforge.plantuml.dot.ProcessState;
import net.sourceforge.plantuml.klimt.geom.MinFinder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.DotPath;
import net.sourceforge.plantuml.security.SFile;
import net.sourceforge.plantuml.svek.SvgResult;
import net.sourceforge.plantuml.svek.YDelta;

public class GraphvizSolverB {
	// ::remove file when __CORE__

	// static private void traceDotString(String dotString) throws IOException {
	// final File f = SecurityUtils.File("dottmpfile" + UniqueSequence.getValue() +
	// ".tmp");
	// PrintWriter pw = null;
	// try {
	// pw = SecurityUtils.PrintWriter(new FileWriter(f));
	// pw.print(dotString);
	// Log.info("Creating file " + f);
	// } finally {
	// if (pw != null) {
	// pw.close();
	// }
	// }
	// }
	//
	// static private void traceSvgString(String svg) throws IOException {
	// final File f = SecurityUtils.File("svgtmpfile" + UniqueSequence.getValue() +
	// ".svg");
	// PrintWriter pw = null;
	// try {
	// pw = SecurityUtils.PrintWriter(new FileWriter(f));
	// pw.print(svg);
	// Log.info("Creating file " + f);
	// } finally {
	// if (pw != null) {
	// pw.close();
	// }
	// }
	// }

	public XDimension2D solve(Cluster root, Collection<Path> paths) throws IOException {
		final String dotString = new DotxMaker(root, paths).createDotString("nodesep=0.2;", "ranksep=0.2;");

		// if (OptionFlags.getInstance().isKeepTmpFiles()) {
		// traceDotString(dotString);
		// }

		final MinFinder minMax = new MinFinder();

		// Log.println("dotString=" + dotString);

		// exportPng(dotString, SecurityUtils.File("png", "test1.png"));

		final Graphviz graphviz = GraphvizUtils.create(null, dotString, "svg");
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final ProcessState state = graphviz.createFile3(baos);
		baos.close();
		if (state.differs(ProcessState.TERMINATED_OK())) {
			throw new IllegalStateException("Timeout2 " + state);
		}
		final byte[] result = baos.toByteArray();
		final String s = new String(result, UTF_8);
		// Log.println("result=" + s);

		// if (OptionFlags.getInstance().isKeepTmpFiles()) {
		// traceSvgString(s);
		// }

		final Pattern pGraph = Pattern.compile("(?m)\\<svg\\s+width=\"(\\d+)pt\"\\s+height=\"(\\d+)pt\"");
		final Matcher mGraph = pGraph.matcher(s);
		if (mGraph.find() == false) {
			throw new IllegalStateException();
		}
		final int width = Integer.parseInt(mGraph.group(1));
		final int height = Integer.parseInt(mGraph.group(2));

		final YDelta yDelta = new YDelta(height);
		for (Block b : root.getRecursiveContents()) {
			final String start = "b" + b.getUid();
			final int p1 = s.indexOf("<title>" + start + "</title>");
			if (p1 == -1) {
				throw new IllegalStateException();
			}
			final List<XPoint2D> pointsList = extractPointsList(s, p1, yDelta);
			b.setX(getMinX(pointsList));
			b.setY(getMinY(pointsList));
			minMax.manage(b.getPosition());
		}

		for (Cluster cl : root.getSubClusters()) {
			final String start = "cluster" + cl.getUid();
			final int p1 = s.indexOf("<title>" + start + "</title>");
			if (p1 == -1) {
				throw new IllegalStateException();
			}
			final List<XPoint2D> pointsList = extractPointsList(s, p1, yDelta);
			cl.setX(getMinX(pointsList));
			cl.setY(getMinY(pointsList));
			final double w = getMaxX(pointsList) - getMinX(pointsList);
			final double h = getMaxY(pointsList) - getMinY(pointsList);
			cl.setHeight(h);
			cl.setWidth(w);
			minMax.manage(cl.getPosition());
		}

		for (Path p : paths) {
			final String start = "b" + p.getStart().getUid();
			final String end = "b" + p.getEnd().getUid();
			final String searched = "<title>" + start + "&#45;&gt;" + end + "</title>";
			final int p1 = s.indexOf(searched);
			if (p1 == -1) {
				throw new IllegalStateException(searched);
			}
			final int p2 = s.indexOf(" d=\"", p1);
			final int p3 = s.indexOf("\"", p2 + " d=\"".length());
			final String points = s.substring(p2 + " d=\"".length(), p3);
			final DotPath dotPath = new SvgResult(points, yDelta).toDotPath();
			p.setDotPath(dotPath);
			minMax.manage(dotPath.getMinFinder());

			// Log.println("pointsList=" + pointsList);
			if (p.getLabel() != null) {
				final List<XPoint2D> pointsList = extractPointsList(s, p1, yDelta);
				final double x = getMinX(pointsList);
				final double y = getMinY(pointsList);
				p.setLabelPosition(x, y);
				minMax.manage(x, y);
			}
		}
		return new XDimension2D(width, height);
	}

	static private List<XPoint2D> extractPointsList(final String svg, final int starting, final YDelta yDelta) {
		return new SvgResult(svg, yDelta).substring(starting).extractList(SvgResult.POINTS_EQUALS);
	}

	static private double getMaxX(List<XPoint2D> points) {
		double result = points.get(0).x;
		for (int i = 1; i < points.size(); i++) {
			if (points.get(i).x > result) {
				result = points.get(i).x;
			}
		}
		return result;
	}

	static private double getMinX(List<XPoint2D> points) {
		double result = points.get(0).x;
		for (int i = 1; i < points.size(); i++) {
			if (points.get(i).x < result) {
				result = points.get(i).x;
			}
		}
		return result;
	}

	static private double getMaxY(List<XPoint2D> points) {
		double result = points.get(0).y;
		for (int i = 1; i < points.size(); i++) {
			if (points.get(i).y > result) {
				result = points.get(i).y;
			}
		}
		return result;
	}

	static private double getMinY(List<XPoint2D> points) {
		double result = points.get(0).y;
		for (int i = 1; i < points.size(); i++) {
			if (points.get(i).y < result) {
				result = points.get(i).y;
			}
		}
		return result;
	}

	private void exportPng(final String dotString, SFile f) throws IOException {
		final Graphviz graphviz = GraphvizUtils.create(null, dotString, "png");
		try (OutputStream os = f.createBufferedOutputStream()) {
			final ProcessState state = graphviz.createFile3(os);
			if (state.differs(ProcessState.TERMINATED_OK())) {
				throw new IllegalStateException("Timeout3 " + state);
			}
		}
	}

	private Path getPath(Collection<Path> paths, int start, int end) {
		for (Path p : paths) {
			if (p.getStart().getUid() == start && p.getEnd().getUid() == end) {
				return p;
			}
		}
		throw new IllegalArgumentException();

	}
}
