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
 */
package net.sourceforge.plantuml.chart;

/**
 * Represents an annotation on a chart at a specific point.
 * Annotations display text labels at specific data coordinates with optional arrows.
 */
public class ChartAnnotation {

	private final String text;
	private final Object xPosition;  // Can be String (label) or Double (numeric)
	private final double yPosition;
	private final boolean showArrow;

	/**
	 * Creates a new chart annotation.
	 *
	 * @param text the annotation text to display
	 * @param xPosition the x-axis position (String for categorical, Double for numeric)
	 * @param yPosition the y-axis position (numeric value)
	 * @param showArrow whether to show an arrow pointing to the data point
	 */
	public ChartAnnotation(String text, Object xPosition, double yPosition, boolean showArrow) {
		this.text = text;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.showArrow = showArrow;
	}

	/**
	 * Gets the annotation text.
	 *
	 * @return the annotation text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Gets the x-axis position.
	 *
	 * @return the x-axis position (String for categorical, Double for numeric)
	 */
	public Object getXPosition() {
		return xPosition;
	}

	/**
	 * Gets the y-axis position.
	 *
	 * @return the y-axis position as a numeric value
	 */
	public double getYPosition() {
		return yPosition;
	}

	/**
	 * Checks whether this annotation should display an arrow.
	 *
	 * @return true if an arrow should be shown, false otherwise
	 */
	public boolean isShowArrow() {
		return showArrow;
	}
}
