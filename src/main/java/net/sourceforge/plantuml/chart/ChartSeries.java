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
 * Original Author:  David Fyfe
 *
 */
package net.sourceforge.plantuml.chart;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.stereo.Stereotype;

public class ChartSeries {

	public enum SeriesType {
		BAR, LINE, AREA, SCATTER
	}

	public enum MarkerShape {
		CIRCLE, SQUARE, TRIANGLE
	}

	private final String name;
	private final SeriesType type;
	private final List<Double> values;
	private final List<Double> xValues; // null for index-based mode, non-null for coordinate pairs
	private HColor color;
	private boolean useSecondaryAxis;
	private boolean showLabels;
	private MarkerShape markerShape;
	private Stereotype stereotype;

	public ChartSeries(String name, SeriesType type, List<Double> values) {
		this.name = name;
		this.type = type;
		this.values = new ArrayList<>(values);
		this.xValues = null; // Index-based mode
		this.useSecondaryAxis = false;
		this.showLabels = false;
		this.markerShape = MarkerShape.CIRCLE; // Default marker shape
	}

	public ChartSeries(String name, SeriesType type, List<Double> xValues, List<Double> yValues) {
		this.name = name;
		this.type = type;
		this.xValues = new ArrayList<>(xValues);
		this.values = new ArrayList<>(yValues);
		this.useSecondaryAxis = false;
		this.showLabels = false;
		this.markerShape = MarkerShape.CIRCLE; // Default marker shape
	}

	public String getName() {
		return name;
	}

	public SeriesType getType() {
		return type;
	}

	public List<Double> getValues() {
		return values;
	}

	public List<Double> getXValues() {
		return xValues;
	}

	public boolean hasExplicitXValues() {
		return xValues != null;
	}

	public HColor getColor() {
		return color;
	}

	public void setColor(HColor color) {
		this.color = color;
	}

	public boolean isUseSecondaryAxis() {
		return useSecondaryAxis;
	}

	public void setUseSecondaryAxis(boolean useSecondaryAxis) {
		this.useSecondaryAxis = useSecondaryAxis;
	}

	public boolean isShowLabels() {
		return showLabels;
	}

	public void setShowLabels(boolean showLabels) {
		this.showLabels = showLabels;
	}

	public MarkerShape getMarkerShape() {
		return markerShape;
	}

	public void setMarkerShape(MarkerShape markerShape) {
		this.markerShape = markerShape;
	}

	public Stereotype getStereotype() {
		return stereotype;
	}

	public void setStereotype(Stereotype stereotype) {
		this.stereotype = stereotype;
	}
}
