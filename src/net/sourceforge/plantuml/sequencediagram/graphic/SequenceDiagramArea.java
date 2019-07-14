/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.SkinParam;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.png.PngTitler;
import net.sourceforge.plantuml.utils.MathUtils;

public class SequenceDiagramArea {

	private final double sequenceWidth;
	private final double sequenceHeight;

	private double headerWidth;
	private double headerHeight;
	private double headerMargin;

	private double titleWidth;
	private double titleHeight;

	private double captionWidth;
	private double captionHeight;

	private double footerWidth;
	private double footerHeight;
	private double footerMargin;

	public SequenceDiagramArea(double width, double height) {
		this.sequenceWidth = width;
		this.sequenceHeight = height;
	}

	public void setTitleArea(double width, double height) {
		this.titleWidth = width;
		this.titleHeight = height;
	}

	private void setCaptionArea(double width, double height) {
		this.captionWidth = width;
		this.captionHeight = height;
	}

	public void setCaptionArea(Dimension2D dim) {
		setCaptionArea(dim.getWidth(), dim.getHeight());
	}

	public void setHeaderArea(double headerWidth, double headerHeight, double headerMargin) {
		this.headerWidth = headerWidth;
		this.headerHeight = headerHeight;
		this.headerMargin = headerMargin;
	}

	public void setFooterArea(double footerWidth, double footerHeight, double footerMargin) {
		this.footerWidth = footerWidth;
		this.footerHeight = footerHeight;
		this.footerMargin = footerMargin;
	}

	public double getWidth() {
		return MathUtils.max(sequenceWidth, headerWidth, titleWidth, footerWidth, captionWidth);
	}

	public double getHeight() {
		return sequenceHeight + headerHeight + headerMargin + titleHeight + footerMargin + footerHeight + captionHeight;
	}

	public double getTitleX() {
		return (getWidth() - titleWidth) / 2;
	}

	public double getTitleY() {
		return headerHeight + headerMargin;
	}

	public double getHeaderHeightMargin() {
		return headerHeight + headerMargin;
	}

	public double getCaptionX() {
		return (getWidth() - captionWidth) / 2;
	}

	public double getCaptionY() {
		return sequenceHeight + headerHeight + headerMargin + titleHeight;
	}

	public double getSequenceAreaX() {
		return (getWidth() - sequenceWidth) / 2;
	}

	public double getSequenceAreaY() {
		return getTitleY() + titleHeight;
	}

	public double getHeaderY() {
		return 0;
	}

	public double getFooterY() {
		return sequenceHeight + headerHeight + headerMargin + titleHeight + footerMargin + captionHeight;
	}

	public double getFooterX(HorizontalAlignment align) {
		if (align == HorizontalAlignment.LEFT) {
			return 0;
		}
		if (align == HorizontalAlignment.RIGHT) {
			return getWidth() - footerWidth;
		}
		if (align == HorizontalAlignment.CENTER) {
			return (getWidth() - footerWidth) / 2;
		}
		throw new IllegalStateException();
	}

	public double getHeaderX(HorizontalAlignment align) {
		if (align == HorizontalAlignment.LEFT) {
			return 0;
		}
		if (align == HorizontalAlignment.RIGHT) {
			return getWidth() - headerWidth;
		}
		if (align == HorizontalAlignment.CENTER) {
			return (getWidth() - headerWidth) / 2;
		}
		throw new IllegalStateException();
	}

	public void initFooter(PngTitler pngTitler, StringBounder stringBounder) {
		final Dimension2D dim = pngTitler.getTextDimension(stringBounder);
		if (dim != null) {
			if (SkinParam.USE_STYLES())
				setFooterArea(dim.getWidth(), dim.getHeight(), 0);
			else
				setFooterArea(dim.getWidth(), dim.getHeight(), 3);
		}
	}

	public void initHeader(PngTitler pngTitler, StringBounder stringBounder) {
		final Dimension2D dim = pngTitler.getTextDimension(stringBounder);
		if (dim != null) {
			if (SkinParam.USE_STYLES())
				setHeaderArea(dim.getWidth(), dim.getHeight(), 0);
			else
				setHeaderArea(dim.getWidth(), dim.getHeight(), 3);
		}
	}

}
