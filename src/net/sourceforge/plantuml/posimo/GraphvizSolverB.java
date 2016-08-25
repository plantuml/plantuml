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
 *
 * Original Author:  Arnaud Roques
 *
 * 
 */
package net.sourceforge.plantuml.posimo;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.cucadiagram.dot.Graphviz;
import net.sourceforge.plantuml.cucadiagram.dot.GraphvizUtils;
import net.sourceforge.plantuml.cucadiagram.dot.ProcessState;
import net.sourceforge.plantuml.svek.MinFinder;

public class GraphvizSolverB {

	// static private void traceDotString(String dotString) throws IOException {
	// final File f = new File("dottmpfile" + UniqueSequence.getValue() + ".tmp");
	// PrintWriter pw = null;
	// try {
	// pw = new PrintWriter(new FileWriter(f));
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
	// final File f = new File("svgtmpfile" + UniqueSequence.getValue() + ".svg");
	// PrintWriter pw = null;
	// try {
	// pw = new PrintWriter(new FileWriter(f));
	// pw.print(svg);
	// Log.info("Creating file " + f);
	// } finally {
	// if (pw != null) {
	// pw.close();
	// }
	// }
	// }

	public Dimension2D solve(Cluster root, Collection<Path> paths) throws IOException {
		final String dotString = new DotxMaker(root, paths).createDotString("nodesep=0.2;", "ranksep=0.2;");

		// if (OptionFlags.getInstance().isKeepTmpFiles()) {
		// traceDotString(dotString);
		// }

		final MinFinder minMax = new MinFinder();

		// Log.println("dotString=" + dotString);

		// exportPng(dotString, new File("png", "test1.png"));

		final Graphviz graphviz = GraphvizUtils.create(null, dotString, "svg");
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final ProcessState state = graphviz.createFile3(baos);
		baos.close();
		if (state.differs(ProcessState.TERMINATED_OK())) {
			throw new IllegalStateException("Timeout2 " + state);
		}
		final byte[] result = baos.toByteArray();
		final String s = new String(result, "UTF-8");
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

		for (Block b : root.getRecursiveContents()) {
			final String start = "b" + b.getUid();
			final int p1 = s.indexOf("<title>" + start + "</title>");
			if (p1 == -1) {
				throw new IllegalStateException();
			}
			final List<Point2D.Double> pointsList = extractPointsList(s, p1);
			b.setX(getMinX(pointsList));
			b.setY(getMinY(pointsList) + height);
			minMax.manage(b.getPosition());
		}

		for (Cluster cl : root.getSubClusters()) {
			final String start = "cluster" + cl.getUid();
			final int p1 = s.indexOf("<title>" + start + "</title>");
			if (p1 == -1) {
				throw new IllegalStateException();
			}
			final List<Point2D.Double> pointsList = extractPointsList(s, p1);
			cl.setX(getMinX(pointsList));
			cl.setY(getMinY(pointsList) + height);
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
			final DotPath dotPath = new DotPath(points, height);
			p.setDotPath(dotPath);
			minMax.manage(dotPath.getMinFinder());

			// Log.println("pointsList=" + pointsList);
			if (p.getLabel() != null) {
				final List<Point2D.Double> pointsList = extractPointsList(s, p1);
				final double x = getMinX(pointsList);
				final double y = getMinY(pointsList) + height;
				p.setLabelPosition(x, y);
				minMax.manage(x, y);
			}
		}
		return new Dimension2DDouble(width, height);
	}

	static private List<Point2D.Double> extractPointsList(final String svg, final int starting) {
		final String pointsString = "points=\"";
		final int p2 = svg.indexOf(pointsString, starting);
		final int p3 = svg.indexOf("\"", p2 + pointsString.length());
		final String points = svg.substring(p2 + pointsString.length(), p3);
		final List<Point2D.Double> pointsList = getPoints(points);
		return pointsList;
	}

	static private double getMaxX(List<Point2D.Double> points) {
		double result = points.get(0).x;
		for (int i = 1; i < points.size(); i++) {
			if (points.get(i).x > result) {
				result = points.get(i).x;
			}
		}
		return result;
	}

	static private double getMinX(List<Point2D.Double> points) {
		double result = points.get(0).x;
		for (int i = 1; i < points.size(); i++) {
			if (points.get(i).x < result) {
				result = points.get(i).x;
			}
		}
		return result;
	}

	static private double getMaxY(List<Point2D.Double> points) {
		double result = points.get(0).y;
		for (int i = 1; i < points.size(); i++) {
			if (points.get(i).y > result) {
				result = points.get(i).y;
			}
		}
		return result;
	}

	static private double getMinY(List<Point2D.Double> points) {
		double result = points.get(0).y;
		for (int i = 1; i < points.size(); i++) {
			if (points.get(i).y < result) {
				result = points.get(i).y;
			}
		}
		return result;
	}

	static private List<Point2D.Double> getPoints(String points) {
		final List<Point2D.Double> result = new ArrayList<Point2D.Double>();
		final StringTokenizer st = new StringTokenizer(points, " ");
		while (st.hasMoreTokens()) {
			final String t = st.nextToken();
			final StringTokenizer st2 = new StringTokenizer(t, ",");
			final double x = Double.parseDouble(st2.nextToken());
			final double y = Double.parseDouble(st2.nextToken());
			result.add(new Point2D.Double(x, y));
		}
		return result;
	}

	private void exportPng(final String dotString, File f) throws IOException {
		final Graphviz graphviz = GraphvizUtils.create(null, dotString, "png");
		final OutputStream os = new BufferedOutputStream(new FileOutputStream(f));
		final ProcessState state = graphviz.createFile3(os);
		os.close();
		if (state.differs(ProcessState.TERMINATED_OK())) {
			throw new IllegalStateException("Timeout3 " + state);
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
