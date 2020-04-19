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
package net.sourceforge.plantuml.sequencediagram.teoz;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.UChange;
import net.sourceforge.plantuml.ugraphic.UBackground;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UParam;
import net.sourceforge.plantuml.ugraphic.UParamNull;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class LiveBoxFinder implements UGraphic {

	public boolean matchesProperty(String propertyName) {
		return false;
	}

	public double dpiFactor() {
		return 1;
	}

	public UGraphic apply(UChange change) {
		if (change instanceof UTranslate) {
			return new LiveBoxFinder(stringBounder, translate.compose((UTranslate) change));
		} else if (change instanceof UStroke) {
			return new LiveBoxFinder(this);
		} else if (change instanceof UBackground) {
			return new LiveBoxFinder(this);
		} else if (change instanceof HColor) {
			return new LiveBoxFinder(this);
		}
		throw new UnsupportedOperationException();
	}

	private final StringBounder stringBounder;
	private final UTranslate translate;

	public LiveBoxFinder(StringBounder stringBounder) {
		this(stringBounder, new UTranslate());
	}

	private LiveBoxFinder(StringBounder stringBounder, UTranslate translate) {
		this.stringBounder = stringBounder;
		this.translate = translate;
	}

	private LiveBoxFinder(LiveBoxFinder other) {
		this(other.stringBounder, other.translate);
	}

	public StringBounder getStringBounder() {
		return stringBounder;
	}

	public UParam getParam() {
		return new UParamNull();
	}

	public void draw(UShape shape) {
		final double x = translate.getDx();
		final double y = translate.getDy();
		if (shape instanceof GroupingTile) {
			((GroupingTile) shape).drawU(this);
		} else if (shape instanceof TileWithUpdateStairs) {
			((TileWithUpdateStairs) shape).updateStairs(stringBounder, y);
			// } else if (shape instanceof EmptyTile) {
			// // Nothing ?
			// } else if (shape instanceof TileParallel) {
			// // Nothing ?
			// } else if (shape instanceof NotesTile) {
			// // Nothing ?
			// } else if (shape instanceof Tile) {
			// Log.info("OtherTile " + shape);
		} else {
			// Nothing ?
			// throw new UnsupportedOperationException(shape.getClass().getName());
		}
	}

	public ColorMapper getColorMapper() {
		return new ColorMapperIdentity();
	}

	public void startUrl(Url url) {
	}

	public void closeAction() {
	}

	public void flushUg() {
	}

}
