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

import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.klimt.UGroup;
import net.sourceforge.plantuml.klimt.UParam;
import net.sourceforge.plantuml.klimt.UParamNull;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.url.Url;

public abstract class UGraphicNo implements UGraphic {
	// ::remove file when __HAXE__

	private final StringBounder stringBounder;
	private final UTranslate translate;

//	private UGraphicNo(UGraphicNo other, UChange change) {
//		this(other.stringBounder,
//				change instanceof UTranslate ? other.translate.compose((UTranslate) change) : other.translate);
//	}

	public UGraphicNo(StringBounder stringBounder, UTranslate translate) {
		this.stringBounder = stringBounder;
		this.translate = translate;
	}

	//
	// Implement UGraphic
	//

	@Override
	final public void startUrl(Url url) {
	}

	@Override
	public void startGroup(UGroup group) {
	}

	@Override
	final public void closeUrl() {
	}

	@Override
	final public void closeGroup() {
	}

	@Override
	public ColorMapper getColorMapper() {
		throw new UnsupportedOperationException();
	}

	@Override
	public HColor getDefaultBackground() {
		return HColors.BLACK;
	}

	@Override
	public UParam getParam() {
		return new UParamNull();
	}

	@Override
	public StringBounder getStringBounder() {
		return stringBounder;
	}

	@Override
	public void flushUg() {
	}

	@Override
	public boolean matchesProperty(String propertyName) {
		return false;
	}

	@Override
	public void writeToStream(OutputStream os, String metadata, int dpi) throws IOException {
		throw new UnsupportedOperationException();
	}

	//
	// Internal things
	//

	protected final UTranslate getTranslate() {
		return translate;
	}
}
