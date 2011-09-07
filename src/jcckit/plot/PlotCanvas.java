/*
 * Copyright 2003-2004, Franz-Josef Elmer, All rights reserved
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details
 * (http://www.gnu.org/copyleft/lesser.html).
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package jcckit.plot;

import jcckit.data.DataPlot;
import jcckit.graphic.Anchor;
import jcckit.graphic.ClippingRectangle;
import jcckit.util.ConfigParameters;

/**
 * An abstract canvas containg a single {@link Plot}. The canvas is specified
 * by a {@link ClippingRectangle}, called <em>paper</em>. A horizontal and
 * vertical {@link Anchor} determine the position of the paper on the actual
 * device.
 * 
 * @author Franz-Josef Elmer
 */
public class PlotCanvas implements PlotListener {
	/** Configuration parameter key. */
	public static final String PAPER_KEY = "paper", HORIZONTAL_ANCHOR_KEY = "horizontalAnchor",
			VERTICAL_ANCHOR_KEY = "verticalAnchor", PLOT_KEY = "plot";
	private final ClippingRectangle _paper;
	private final Anchor _horizontalAnchor;
	private final Anchor _verticalAnchor;
	private final Plot _plot;

	/**
	 * Creates an instance from the specified configuration parameters. <table
	 * border=1 cellpadding=5>
	 * <tr>
	 * <th>Key &amp; Default Value</th>
	 * <th>Type</th>
	 * <th>Mandatory</th>
	 * <th>Description</th>
	 * </tr>
	 * <tr>
	 * <td><tt>horizontalAnchor = center</tt></td>
	 * <td><tt>String</tt></td>
	 * <td>no</td>
	 * <td>Horizontal position of the paper relative to the device border.
	 * Possible values are <tt>left</tt>, <tt>center</tt>, and
	 * <tt>right</tt>.</td>
	 * </tr>
	 * <tr>
	 * <td><tt>paper = 0,&nbsp;0,&nbsp;1,&nbsp;0.6</tt></td>
	 * <td><tt>double[]</tt></td>
	 * <td>no</td>
	 * <td>Rectangle defining the paper. The first two values determine the x-
	 * and y- coordinates (in device-independent units) of the lower-left
	 * corner. The last two values determine the upper-right corner.</td>
	 * </tr>
	 * <tr>
	 * <td><tt>plot = </tt>default values of {@link Plot}</td>
	 * <td><tt>ConfigParameters</tt></td>
	 * <td>no</td>
	 * <td>Definition of the {@link Plot}.</td>
	 * </tr>
	 * <tr>
	 * <td><tt>verticalAnchor = center</tt></td>
	 * <td><tt>String</tt></td>
	 * <td>no</td>
	 * <td>Vertical position of the paper relative to the device border.
	 * Possible values are <tt>top</tt>, <tt>center</tt>, and
	 * <tt>bottom</tt>.</td>
	 * </tr>
	 * </table>
	 * <p>
	 * Note, that this instance registers itself at the wrapped {@link Plot}
	 * instance.
	 */
	public PlotCanvas(ConfigParameters config) {
		double[] paper = config.getDoubleArray(PAPER_KEY, new double[] { 0, 0, 1, 0.6 });
		_paper = new ClippingRectangle(paper[0], paper[1], paper[2], paper[3]);
		_horizontalAnchor = Anchor.getHorizontalAnchor(config, HORIZONTAL_ANCHOR_KEY, Anchor.CENTER);
		_verticalAnchor = Anchor.getVerticalAnchor(config, VERTICAL_ANCHOR_KEY, Anchor.CENTER);
		_plot = new Plot(config.getNode(PLOT_KEY));
		_plot.addPlotListener(this);
	}

	/** Returns the paper definition. */
	public ClippingRectangle getPaper() {
		return _paper;
	}

	/** Returns the horizontal anchor. */
	public Anchor getHorizontalAnchor() {
		return _horizontalAnchor;
	}

	/** Returns the vertical anchor. */
	public Anchor getVerticalAnchor() {
		return _verticalAnchor;
	}

	/** Returns the plot. */
	public Plot getPlot() {
		return _plot;
	}

	/**
	 * Connects the wrapped {@link Plot} instance with the specified
	 * {@link DataPlot}.
	 * 
	 * @param dataPlot
	 *            Data to be connected with this plot canvas. Can be
	 *            <tt>null</tt> in order to disconnect this instance from a
	 *            <tt>DataPlot</tt>.
	 */
	public void connect(DataPlot dataPlot) {
		_plot.connect(dataPlot);
	}

	/**
	 * Handles the spcified event. Here nothing is done. But subclass may
	 * override this method.
	 */
	public void plotChanged(PlotEvent event) {
	}
}
