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
 *
 */
package net.sourceforge.plantuml.klimt.drawing;

import java.util.Objects;

import net.sourceforge.plantuml.klimt.CopyForegroundColorToBackgroundColor;
import net.sourceforge.plantuml.klimt.UBackground;
import net.sourceforge.plantuml.klimt.UChange;
import net.sourceforge.plantuml.klimt.UClip;
import net.sourceforge.plantuml.klimt.UGroup;
import net.sourceforge.plantuml.klimt.UParam;
import net.sourceforge.plantuml.klimt.UPattern;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.shape.UHidden;
import net.sourceforge.plantuml.url.Url;

public abstract class AbstractCommonUGraphic implements UGraphic {

	private UStroke stroke = UStroke.simple();
	private UPattern pattern = UPattern.FULL;
	private boolean hidden = false;
	private HColor backColor = HColors.none();
	private HColor color = HColors.none();
	private boolean enlargeClip = false;

	private final StringBounder stringBounder;
	private UTranslate translate = UTranslate.none();

	private /* final */ ColorMapper colorMapper;
	private UClip clip;

	private /* final */ HColor defaultBackground;

	protected AbstractCommonUGraphic(StringBounder stringBounder) {
		this.stringBounder = stringBounder;
	}

	public void basicCopy(HColor defaultBackground, ColorMapper colorMapper) {
		this.colorMapper = colorMapper;
		this.defaultBackground = Objects.requireNonNull(defaultBackground);
	}

	protected void basicCopy(AbstractCommonUGraphic other) {
		this.defaultBackground = Objects.requireNonNull(other.defaultBackground);
		this.enlargeClip = other.enlargeClip;
		this.colorMapper = other.colorMapper;
		this.translate = other.translate;
		this.clip = other.clip;

		this.stroke = other.stroke;
		this.pattern = other.pattern;
		this.hidden = other.hidden;
		this.color = other.color;
		this.backColor = other.backColor;
	}

	protected abstract AbstractCommonUGraphic copyUGraphic();

	@Override
	public HColor getDefaultBackground() {
		return defaultBackground;
	}

	public double dpiFactor() {
		return 1;
	}

	public UGraphic apply(UChange change) {
		Objects.requireNonNull(change);
		final AbstractCommonUGraphic copy = copyUGraphic();
		if (change instanceof UTranslate) {
			copy.translate = ((UTranslate) change).compose(copy.translate);
		} else if (change instanceof UClip) {
			copy.clip = (UClip) change;
			copy.clip = copy.clip.translate(getTranslateX(), getTranslateY());
		} else if (change instanceof UStroke) {
			copy.stroke = (UStroke) change;
		} else if (change instanceof UPattern) {
			copy.pattern = (UPattern) change;
		} else if (change instanceof UHidden) {
			copy.hidden = change == UHidden.HIDDEN;
		} else if (change instanceof UBackground) {
			copy.backColor = ((UBackground) change).getBackColor();
		} else if (change instanceof HColor) {
			copy.color = (HColor) change;
		} else if (change instanceof CopyForegroundColorToBackgroundColor) {
			copy.backColor = this.color;
		}
		return copy;
	}

	final public UClip getClip() {
		if (enlargeClip && clip != null)
			return clip.enlarge(1);

		return clip;
	}

	final public void enlargeClip() {
		this.enlargeClip = true;
	}

	final public UParam getParam() {
		return new UParam() {

			public boolean isHidden() {
				return hidden;
			}

			public UStroke getStroke() {
				return stroke;
			}

			public HColor getColor() {
				return color;
			}

			public HColor getBackcolor() {
				return backColor;
			}

			public UPattern getPattern() {
				return pattern;
			}
		};
	}

	@Override
	public StringBounder getStringBounder() {
		return stringBounder;
	}

	final protected double getTranslateX() {
		return translate.getDx();
	}

	final protected double getTranslateY() {
		return translate.getDy();
	}

	final public ColorMapper getColorMapper() {
		return colorMapper;
	}

	final public void flushUg() {
	}

	@Override
	public void startUrl(Url url) {
	}

	@Override
	public void closeUrl() {
	}

	public void startGroup(UGroup group) {
	}

	public void closeGroup() {
	}

	public boolean matchesProperty(String propertyName) {
		return false;
	}

	public final UTranslate getTranslate() {
		return translate;
	}

}
