/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2025, Arnaud Roques
 *
 * Project Info:  https://plantuml.com
 *
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *
 * Original Author:  Arnaud Roques
 *
 */
package net.sourceforge.plantuml.openpdf;

import java.awt.Color;

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
	private Color backgroundColor;
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

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public PdfOption withBackgroundColor(Color backgroundColor) {
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

}
