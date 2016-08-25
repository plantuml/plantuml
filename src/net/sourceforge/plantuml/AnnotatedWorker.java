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
package net.sourceforge.plantuml;

import net.sourceforge.plantuml.activitydiagram3.ftile.EntityImageLegend;
import net.sourceforge.plantuml.cucadiagram.DisplayPositionned;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.svek.DecorateEntityImage;
import net.sourceforge.plantuml.svek.TextBlockBackcolored;

public class AnnotatedWorker {

	private final Annotated annotated;
	private final ISkinParam skinParam;

	public AnnotatedWorker(Annotated annotated, ISkinParam skinParam) {
		this.annotated = annotated;
		this.skinParam = skinParam;

	}

	public TextBlockBackcolored addAdd(TextBlock result) {
		result = addLegend(result);
		result = addTitle(result);
		result = addCaption(result);
		result = addHeaderAndFooter(result);
		return (TextBlockBackcolored) result;
	}

	private TextBlock addLegend(TextBlock original) {
		if (DisplayPositionned.isNull(annotated.getLegend())) {
			return original;
		}
		final TextBlock text = EntityImageLegend.create(annotated.getLegend().getDisplay(), getSkinParam());

		return DecorateEntityImage.add(original, text, annotated.getLegend().getHorizontalAlignment(), annotated
				.getLegend().getVerticalAlignment());
	}

	private ISkinParam getSkinParam() {
		return skinParam;
	}

	private TextBlock addCaption(TextBlock original) {
		if (DisplayPositionned.isNull(annotated.getCaption())) {
			return original;
		}
		final TextBlock text = getCaption();

		return DecorateEntityImage.addBottom(original, text, HorizontalAlignment.CENTER);
	}

	public TextBlock getCaption() {
		if (DisplayPositionned.isNull(annotated.getCaption())) {
			return TextBlockUtils.empty(0, 0);
		}
		return annotated
				.getCaption()
				.getDisplay()
				.create(new FontConfiguration(getSkinParam(), FontParam.CAPTION, null), HorizontalAlignment.CENTER,
						getSkinParam());
	}

	private TextBlock addTitle(TextBlock original) {
		if (DisplayPositionned.isNull(annotated.getTitle())) {
			return original;
		}
		final TextBlock text = annotated
				.getTitle()
				.getDisplay()
				.create(new FontConfiguration(getSkinParam(), FontParam.TITLE, null), HorizontalAlignment.CENTER,
						getSkinParam());

		return DecorateEntityImage.addTop(original, text, HorizontalAlignment.CENTER);
		// return new DecorateTextBlock(original, text, HorizontalAlignment.CENTER);
	}

	private TextBlock addHeaderAndFooter(TextBlock original) {
		if (DisplayPositionned.isNull(annotated.getFooter()) && DisplayPositionned.isNull(annotated.getHeader())) {
			return original;
		}
		final TextBlock textFooter = DisplayPositionned.isNull(annotated.getFooter()) ? null : annotated
				.getFooter()
				.getDisplay()
				.create(new FontConfiguration(getSkinParam(), FontParam.FOOTER, null),
						annotated.getFooter().getHorizontalAlignment(), getSkinParam());
		final TextBlock textHeader = DisplayPositionned.isNull(annotated.getHeader()) ? null : annotated
				.getHeader()
				.getDisplay()
				.create(new FontConfiguration(getSkinParam(), FontParam.HEADER, null),
						annotated.getHeader().getHorizontalAlignment(), getSkinParam());

		// return new DecorateTextBlock(original, textHeader, annotated.getHeader().getHorizontalAlignment(),
		// textFooter,
		// annotated.getFooter().getHorizontalAlignment());
		return new DecorateEntityImage(original, textHeader, annotated.getHeader().getHorizontalAlignment(),
				textFooter, annotated.getFooter().getHorizontalAlignment());
	}

}
