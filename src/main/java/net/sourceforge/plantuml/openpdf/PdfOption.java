/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2026, Arnaud Roques
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
package net.sourceforge.plantuml.openpdf;

import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;

/**
 * Configuration options for {@link PdfGraphics}.
 *
 * Kept intentionally small for a first iteration: scale, title/author metadata
 * and an optional background color. We will extend this as the driver matures
 * (mirroring what SvgOption does for the SVG backend).
 */
public class PdfOption {

	private double scale = 1.0;
	private String title;
	private String author;
	private String subject;
	private HColor backgroundColor;
	private double minWidth = 10;
	private double minHeight = 10;

	public PdfOption() {
	}

	public double getScale() {
		return scale;
	}

	public PdfOption withScale(double scale) {
		this.scale = scale;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public PdfOption withTitle(String title) {
		this.title = title;
		return this;
	}

	public String getAuthor() {
		return author;
	}

	public PdfOption withAuthor(String author) {
		this.author = author;
		return this;
	}

	public String getSubject() {
		return subject;
	}

	public PdfOption withSubject(String subject) {
		this.subject = subject;
		return this;
	}

	public HColor getBackgroundColor() {
		return backgroundColor;
	}

	public PdfOption withBackgroundColor(HColor backgroundColor) {
		this.backgroundColor = backgroundColor;
		return this;
	}

	public double getMinWidth() {
		return minWidth;
	}

	public double getMinHeight() {
		return minHeight;
	}

	public PdfOption withMinDimension(double width, double height) {
		this.minWidth = width;
		this.minHeight = height;
		return this;
	}

	public ColorMapper getColorMapper() {
		return ColorMapper.IDENTITY;
	}

}
