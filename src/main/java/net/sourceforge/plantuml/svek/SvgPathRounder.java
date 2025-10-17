/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
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
package net.sourceforge.plantuml.svek;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for post-processing SVG output from GraphViz to add rounded
 * corners to orthogonal edges.
 *
 * This class takes standard SVG output with sharp-cornered orthogonal paths
 * and transforms them to use smooth rounded corners via quadratic Bézier curves.
 */
public class SvgPathRounder {

	/**
	 * Round corners in all edge paths within SVG content
	 *
	 * @param svgContent Full SVG document from GraphViz or PlantUML
	 * @param radius Corner radius in pixels
	 * @return Modified SVG with rounded corners
	 */
	public static String roundOrthogonalPaths(String svgContent, double radius) {
		if (radius <= 0 || svgContent == null || svgContent.isEmpty())
			return svgContent;

		// Process both GraphViz edges and PlantUML links
		String result = svgContent;
		result = processEdgeGroup(result, radius, "edge");  // GraphViz: <g id="edge...">
		result = processEdgeGroup(result, radius, "link");  // PlantUML: <g class="link">
		return result;
	}

	/**
	 * Process edge/link groups in SVG
	 *
	 * @param svgContent SVG content
	 * @param radius Corner radius
	 * @param groupType "edge" for GraphViz or "link" for PlantUML
	 * @return Modified SVG
	 */
	private static String processEdgeGroup(String svgContent, double radius, String groupType) {
		// Pattern for GraphViz: <g id="edge...">...<path...d="..."/>...</g>
		// Pattern for PlantUML: <g class="link">...<path...d="..."/>...</g>
		final String patternStr;
		if (groupType.equals("edge")) {
			patternStr = "(<g[^>]*id=\"edge[^\"]*\"[^>]*>.*?)<path([^>]*?)d=\"([^\"]+)\"([^>]*?/?>)(.*?</g>)";
		} else {
			patternStr = "(<g[^>]*class=\"link\"[^>]*>.*?)<path([^>]*?)d=\"([^\"]+)\"([^>]*?/?>)(.*?</g>)";
		}

		final Pattern pathPattern = Pattern.compile(patternStr, Pattern.DOTALL);
		final Matcher matcher = pathPattern.matcher(svgContent);
		final StringBuffer result = new StringBuffer();

		while (matcher.find()) {
			final String beforePath = matcher.group(1);
			final String pathAttrsStart = matcher.group(2);
			final String originalPath = matcher.group(3);
			final String pathAttrsEnd = matcher.group(4);
			final String afterPath = matcher.group(5);

			try {
				// Parse and process the path
				final List<PathPoint> points = parsePath(originalPath);
				final List<Corner> corners = detectCorners(points);

				if (!corners.isEmpty() && isOrthogonalPath(points)) {
					// Generate rounded version
					final String roundedPath = generateRoundedPath(points, corners, radius);
					matcher.appendReplacement(result,
						Matcher.quoteReplacement(
							beforePath + "<path" + pathAttrsStart + "d=\"" +
							roundedPath + "\"" + pathAttrsEnd + afterPath
						)
					);
				} else {
					// No corners or not orthogonal, keep original
					matcher.appendReplacement(result, Matcher.quoteReplacement(matcher.group(0)));
				}
			} catch (Exception e) {
				// On any parsing error, keep original path
				matcher.appendReplacement(result, Matcher.quoteReplacement(matcher.group(0)));
			}
		}

		matcher.appendTail(result);
		return result.toString();
	}

	/**
	 * Parse SVG path d attribute into list of points
	 * Handles M (moveto), L (lineto), C (curveto) commands
	 */
	private static List<PathPoint> parsePath(String pathData) {
		final List<PathPoint> points = new ArrayList<>();
		if (pathData == null || pathData.isEmpty())
			return points;

		// Split path into tokens (commands and coordinates)
		// Match: commands (M,L,C) and numbers (including negative and decimal)
		final Pattern tokenPattern = Pattern.compile("[MLCmlc]|[-+]?[0-9]*\\.?[0-9]+");
		final Matcher matcher = tokenPattern.matcher(pathData);

		char currentCommand = 0;
		double currentX = 0;
		double currentY = 0;
		final List<Double> coords = new ArrayList<>();

		while (matcher.find()) {
			final String token = matcher.group();

			// Check if it's a command letter
			if (token.length() == 1 && Character.isLetter(token.charAt(0))) {
				currentCommand = Character.toUpperCase(token.charAt(0));
				coords.clear();
			} else {
				// It's a number
				coords.add(Double.parseDouble(token));

				// Process based on command and number of coordinates collected
				if (currentCommand == 'M' && coords.size() == 2) {
					currentX = coords.get(0);
					currentY = coords.get(1);
					points.add(new PathPoint(currentX, currentY, 'M'));
					coords.clear();
				} else if (currentCommand == 'L' && coords.size() == 2) {
					currentX = coords.get(0);
					currentY = coords.get(1);
					points.add(new PathPoint(currentX, currentY, 'L'));
					coords.clear();
				} else if (currentCommand == 'C' && coords.size() == 6) {
					// For Cubic Bezier, we only care about the end point (last 2 coords)
					// GraphViz ortho creates degenerate curves where control points = endpoints
					currentX = coords.get(4);
					currentY = coords.get(5);
					points.add(new PathPoint(currentX, currentY, 'C'));
					coords.clear();
				}
			}
		}

		return points;
	}

	/**
	 * Detect corners in orthogonal path where segments meet at approximately 90 degrees
	 *
	 * @return List of corner positions and metadata
	 */
	private static List<Corner> detectCorners(List<PathPoint> points) {
		final List<Corner> corners = new ArrayList<>();
		if (points.size() < 3)
			return corners;

		for (int i = 1; i < points.size() - 1; i++) {
			final PathPoint prev = points.get(i - 1);
			final PathPoint curr = points.get(i);
			final PathPoint next = points.get(i + 1);

			// Calculate direction vectors
			double dx1 = curr.x - prev.x;
			double dy1 = curr.y - prev.y;
			double dx2 = next.x - curr.x;
			double dy2 = next.y - curr.y;

			// Calculate lengths
			final double len1 = Math.sqrt(dx1 * dx1 + dy1 * dy1);
			final double len2 = Math.sqrt(dx2 * dx2 + dy2 * dy2);

			// Skip zero-length segments
			if (len1 < 0.01 || len2 < 0.01)
				continue;

			// Normalize to unit vectors
			dx1 /= len1;
			dy1 /= len1;
			dx2 /= len2;
			dy2 /= len2;

			// Check if perpendicular (dot product ≈ 0 for 90-degree angle)
			final double dotProduct = dx1 * dx2 + dy1 * dy2;

			// Allow small tolerance for floating point comparison
			// dot product = 0 means 90 degrees, ±0.1 allows for small deviations
			if (Math.abs(dotProduct) < 0.1) {
				final Corner corner = new Corner();
				corner.index = i;
				corner.position = curr;
				corners.add(corner);
			}
		}

		return corners;
	}

	/**
	 * Check if path is truly orthogonal (all horizontal/vertical segments)
	 */
	private static boolean isOrthogonalPath(List<PathPoint> points) {
		if (points.size() < 2)
			return false;

		for (int i = 1; i < points.size(); i++) {
			final PathPoint prev = points.get(i - 1);
			final PathPoint curr = points.get(i);

			final double dx = Math.abs(curr.x - prev.x);
			final double dy = Math.abs(curr.y - prev.y);

			// Check if segment is either horizontal or vertical (not diagonal)
			// Allow small tolerance for floating point comparison
			if (dx > 0.1 && dy > 0.1)
				return false; // Diagonal segment found
		}

		return true;
	}

	/**
	 * Generate new path string with rounded corners
	 * Replaces sharp corners with quadratic Bézier arcs
	 */
	private static String generateRoundedPath(List<PathPoint> points, List<Corner> corners, double radius) {
		final StringBuilder result = new StringBuilder();
		int cornerIdx = 0;
		double currentX = 0;
		double currentY = 0;

		for (int i = 0; i < points.size(); i++) {
			final PathPoint p = points.get(i);

			// Check if this point is a corner
			Corner corner = null;
			if (cornerIdx < corners.size() && corners.get(cornerIdx).index == i) {
				corner = corners.get(cornerIdx);
				cornerIdx++;
			}

			if (i == 0) {
				// Start with M (moveto)
				result.append(String.format("M %.2f,%.2f", p.x, p.y));
				currentX = p.x;
				currentY = p.y;
			} else if (corner != null && i < points.size() - 1) {
				// Insert rounded corner
				final PathPoint prev = points.get(i - 1);
				final PathPoint next = points.get(i + 1);

				// Calculate direction vectors
				double dx1 = p.x - prev.x;
				double dy1 = p.y - prev.y;
				final double len1 = Math.sqrt(dx1 * dx1 + dy1 * dy1);

				double dx2 = next.x - p.x;
				double dy2 = next.y - p.y;
				final double len2 = Math.sqrt(dx2 * dx2 + dy2 * dy2);

				// Calculate truncation distance (cannot exceed half segment length)
				final double truncDist1 = Math.min(radius, len1 / 2);
				final double truncDist2 = Math.min(radius, len2 / 2);

				// Calculate truncation points
				final double truncX1 = p.x - (dx1 / len1) * truncDist1;
				final double truncY1 = p.y - (dy1 / len1) * truncDist1;
				final double truncX2 = p.x + (dx2 / len2) * truncDist2;
				final double truncY2 = p.y + (dy2 / len2) * truncDist2;

				// Only add line segment if there's distance to cover
				if (Math.abs(truncX1 - currentX) > 0.01 || Math.abs(truncY1 - currentY) > 0.01) {
					result.append(String.format(" L %.2f,%.2f", truncX1, truncY1));
				}

				// Add quadratic Bézier arc around corner
				// Control point is at the original corner position
				result.append(String.format(" Q %.2f,%.2f %.2f,%.2f", p.x, p.y, truncX2, truncY2));

				currentX = truncX2;
				currentY = truncY2;
			} else {
				// Regular line segment (no corner)
				if (Math.abs(p.x - currentX) > 0.01 || Math.abs(p.y - currentY) > 0.01) {
					result.append(String.format(" L %.2f,%.2f", p.x, p.y));
					currentX = p.x;
					currentY = p.y;
				}
			}
		}

		return result.toString();
	}

	/**
	 * Represents a point in an SVG path
	 */
	private static class PathPoint {
		final double x;
		final double y;
		final char command;

		PathPoint(double x, double y, char command) {
			this.x = x;
			this.y = y;
			this.command = command;
		}

		@Override
		public String toString() {
			return String.format("%c(%.2f,%.2f)", command, x, y);
		}
	}

	/**
	 * Represents a corner detected in an orthogonal path
	 */
	private static class Corner {
		int index;
		PathPoint position;

		@Override
		public String toString() {
			return String.format("Corner at index %d: (%.2f,%.2f)", index, position.x, position.y);
		}
	}
}
