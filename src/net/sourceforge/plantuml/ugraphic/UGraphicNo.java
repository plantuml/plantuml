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
package net.sourceforge.plantuml.ugraphic;

import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.ugraphic.color.HColorUtils;

import java.io.IOException;
import java.io.OutputStream;

public abstract class UGraphicNo implements UGraphic {

	private final StringBounder stringBounder;
	private final UTranslate translate;

	public UGraphicNo(StringBounder stringBounder) {
		this.stringBounder = stringBounder;
		this.translate = new UTranslate();
	}

	public UGraphicNo(UGraphicNo other, UChange change) {
		this.stringBounder = other.stringBounder;
		this.translate = change instanceof UTranslate ? other.translate.compose((UTranslate) change) : other.translate;
	}

	//
	// Implement UGraphic
	//
	
	@Override
	final public void startUrl(Url url) {
	}

	@Override
	public void startGroup(UGroupType type, String ident) {
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
		return HColorUtils.BLACK;
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

	protected UTranslate getTranslate() {
		return translate;
	}
}
