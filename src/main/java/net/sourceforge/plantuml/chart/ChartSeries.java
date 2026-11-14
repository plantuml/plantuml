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
		BAR, LINE, AREA, SCATTER, PIE, RADAR
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
	private boolean interpolate;
	private boolean connect;
	private MarkerShape markerShape;
	private Stereotype stereotype;
	// Data written as [key: value, ...]: the key of each value, else null
	private List<String> keys;
	// Color written after a value, as in [Dogs: 386 #3366CC]: one entry per value, null if none
	private List<HColor> itemColors;
	// Pie only: show percentages, and size of the hole of a donut (0 for a plain pie)
	private boolean showPercent;
	private double donutHole;

	public ChartSeries(String name, SeriesType type, List<Double> values) {
		this.name = name;
		this.type = type;
		this.values = new ArrayList<>(values);
		this.xValues = null; // Index-based mode
		this.useSecondaryAxis = false;
		this.showLabels = false;
		this.interpolate = false;
		this.connect = false;
		this.markerShape = MarkerShape.CIRCLE; // Default marker shape
	}

	public ChartSeries(String name, SeriesType type, List<Double> xValues, List<Double> yValues) {
		this.name = name;
		this.type = type;
		this.xValues = new ArrayList<>(xValues);
		this.values = new ArrayList<>(yValues);
		this.useSecondaryAxis = false;
		this.showLabels = false;
		this.interpolate = false;
		this.connect = false;
		this.markerShape = MarkerShape.CIRCLE; // Default marker shape
	}

	// The same series with the given x values, as if its values had been written as coordinate pairs
	public ChartSeries withXValues(List<Double> xValues) {
		return copyAttributesTo(new ChartSeries(name, type, xValues, values));
	}

	// The same series with other values and no keys: the values of a keyed series put in the
	// order of the categories, a missing one being null
	public ChartSeries withValues(List<Double> newValues) {
		final ChartSeries result = copyAttributesTo(new ChartSeries(name, type, newValues));
		result.keys = null;
		return result;
	}

	private ChartSeries copyAttributesTo(ChartSeries result) {
		result.color = color;
		result.useSecondaryAxis = useSecondaryAxis;
		result.showLabels = showLabels;
		result.interpolate = interpolate;
		result.connect = connect;
		result.markerShape = markerShape;
		result.stereotype = stereotype;
		result.keys = keys;
		result.itemColors = itemColors;
		result.showPercent = showPercent;
		result.donutHole = donutHole;
		return result;
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

	public boolean isInterpolate() {
		return interpolate;
	}

	public void setInterpolate(boolean interpolate) {
		this.interpolate = interpolate;
	}

	public boolean isConnect() {
		return connect;
	}

	public void setConnect(boolean connect) {
		this.connect = connect;
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

	public List<String> getKeys() {
		return keys;
	}

	public boolean hasKeys() {
		return keys != null;
	}

	public void setKeys(List<String> keys) {
		this.keys = keys == null ? null : new ArrayList<>(keys);
	}

	// The color written after the i-th value, or null
	public HColor getItemColor(int i) {
		if (itemColors == null || i >= itemColors.size())
			return null;
		return itemColors.get(i);
	}

	public void setItemColors(List<HColor> itemColors) {
		this.itemColors = itemColors == null ? null : new ArrayList<>(itemColors);
	}

	public boolean isShowPercent() {
		return showPercent;
	}

	public void setShowPercent(boolean showPercent) {
		this.showPercent = showPercent;
	}

	public double getDonutHole() {
		return donutHole;
	}

	public void setDonutHole(double donutHole) {
		this.donutHole = donutHole;
	}
}
