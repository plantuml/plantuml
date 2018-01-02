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
package jcckit;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import jcckit.graphic.GraphicalElement;
import jcckit.graphic.Renderer;
import jcckit.plot.Plot;
import jcckit.plot.PlotCanvas;
import jcckit.renderer.Graphics2DRenderer;
import jcckit.renderer.Transformation;
import jcckit.util.ConfigParameters;
import jcckit.util.Factory;

/**
 * Class which handles plotting into a <tt>Graphics</tt> context based on the
 * {@link jcckit.renderer.GraphicsRenderer}. This class is not a subclass of
 * <tt>java.awt.Component</tt>. The actual AWT component presenting the plot
 * is an innerclass. Its instance wrapped by <tt>GraphicsPlotCanvas</tt> can
 * be obtained with {@link #getGraphicsCanvas}.
 * <p>
 * The plot is painted by using double-buffering and pre-rendered view of the
 * coordinate system. That is, the coordinate system is drawn into an off-screen
 * image. It will be redrawn only if the size of the embedding AWT component is
 * changed.
 * 
 * @author Franz-Josef Elmer
 */
public class GraphicsPlotCanvas extends PlotCanvas {
	/** Key of a configuration parameter. */
	public static final String BACKGROUND_KEY = "background";
	public static final String FOREGROUND_KEY = "foreground";
	public static final String DOUBLE_BUFFERING_KEY = "doubleBuffering";

	/**
	 * Class which does the actual painting. Needs the <tt>Component</tt> into
	 * which the plot is painted for some resources like size, background color,
	 * etc.
	 * 
	 * @author Franz-Josef Elmer
	 */
	private final BufferedImage img3;

	private final Graphics2D g3;

	private Transformation _transformation;
	private String _renderer = "jcckit.renderer.GraphicsRenderer";

	private GraphicalElement _marker;

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
	 * <td><tt>background = </tt><i>default background color of the wrapped
	 * AWT component</i></td>
	 * <td><tt>Color</tt></td>
	 * <td>no</td>
	 * <td>Background color of the wrapped AWT component.</td>
	 * </tr>
	 * <tr>
	 * <td><tt>foreground = </tt><i>default foreground color of the wrapped
	 * AWT component</i></td>
	 * <td><tt>Color</tt></td>
	 * <td>no</td>
	 * <td>Foreground color of the wrapped AWT component.</td>
	 * </tr>
	 * <tr>
	 * <td><tt>doubleBuffering = true</td>
	 *     <td><tt>boolean</tt></td><td>no</td>
	 *     <td>If <tt>true</tt> the plot will be painted by using 
	 *         double-buffering and pre-rendered view of the coordinate system.
	 *     </td></tr>
	 * </table>
	 * In addition the configuration parameters of the
	 * <a href="plot/PlotCanvas.html#PlotCanvas(jcckit.util.ConfigParameters)">
	 * constructor</a> of the superclass {@link jcckit.plot.PlotCanvas} apply.
	 */
	public GraphicsPlotCanvas(ConfigParameters config, BufferedImage img3) {
		super(config);
		this.img3 = img3;
		setRenderer("jcckit.renderer.Graphics2DRenderer");

		g3 = img3.createGraphics();
		g3.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// _doubleBuffering = config.getBoolean(DOUBLE_BUFFERING_KEY, true);
		background = config.getColor(BACKGROUND_KEY, Color.WHITE);
		foreground = config.getColor(FOREGROUND_KEY, Color.BLACK);
	}

	private final Color background;
	private final Color foreground;

	/**
	 * Paints the plot. If {@link GraphicsPlotCanvas#_doubleBuffering} is set
	 * double-buffering and pre-rendered view of the coordinate system is used.
	 */
	public void paint() {
		Dimension size = new Dimension(img3.getWidth(), img3.getHeight());
		g3.setColor(background);
		g3.fillRect(0, 0, size.width + 1, size.height + 1);

		init(size);

		_transformation.apply(g3);
		Plot plot = getPlot();
		drawCoordinateSystem(size, plot);
		drawPlot(plot);
		if (_marker != null) {
			_marker.renderWith(createRenderer());
		}
	}

	private void drawPlot(Plot plot) {
		prepare();
		Renderer renderer = createRenderer();
		GraphicalElement[] curves = plot.getCurves();
		for (int i = 0; i < curves.length; i++) {
			curves[i].renderWith(renderer);
		}
		GraphicalElement annotation = plot.getAnnotation();
		if (annotation != null) {
			annotation.renderWith(renderer);
		}
		if (plot.isLegendVisible()) {
			plot.getLegend().renderWith(renderer);
		}
	}

	private void init(Dimension size) {
		calculateTransformation(size);
	}

	private void drawCoordinateSystem(Dimension size, Plot plot) {
		g3.setColor(foreground);
		plot.getCoordinateSystem().renderWith(createRenderer());
	}

	/**
	 * Prepare graphics context before drawing the pre-rendered view of the
	 * coordinate system. Does nothing but will be used in subclasses.
	 */
	protected void prepare() {
	}

	/**
	 * Calculate the transformation form device-independent coordinates into
	 * device-dependent coordinates according to the specified canvas size.
	 */
	protected void calculateTransformation(Dimension size) {
		_transformation = new Transformation(size.width, size.height, getPaper(), getHorizontalAnchor(),
				getVerticalAnchor());
	}

	/**
	 * Creates an appropriated {@link Renderer} for the specified
	 * <tt>Graphics</tt> context.
	 */
	protected Renderer createRenderer() {
		return ((Graphics2DRenderer) Factory.create(_renderer)).init(g3);
		// return ((GraphicsRenderer) Factory.create(_renderer)).init(g, null,
		// _transformation);
	}

	/**
	 * Sets the renderer used to render the plot. The default value is
	 * {@link GraphicsRenderer}.
	 * 
	 * @param className
	 *            Fully qualified name of the renderer class.
	 */
	public void setRenderer(String className) {
		_renderer = className;
	}

	// /**
	// * Maps the cursor position onto a point in device-independent
	// coordinates.
	// *
	// * @param x
	// * X-coordinate of the cursor.
	// * @param y
	// * Y-coordinate of the cursor.
	// */
	// public GraphPoint mapCursorPosition(int x, int y) {
	// return _transformation.transformBack(x, y);
	// }

	/**
	 * Defines a graphical marker which will be drawn on top of the plot. To
	 * remove the marker call this method with argument <tt>null</tt>.
	 * 
	 * @param marker
	 *            Marker element. Can be <tt>null</tt>.
	 */
	public void setMarker(GraphicalElement marker) {
		_marker = marker;
	}

}
