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
package net.sourceforge.plantuml.cucadiagram;

import java.util.List;

import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.json.JsonValue;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.Style;

public class BodierJSon implements Bodier {

	private JsonValue json;

	@Override
	public void muteClassToObject() {
		throw new UnsupportedOperationException();
	}

	public BodierJSon() {
	}

	@Override
	public void setLeaf(Entity leaf) {

	}

	@Override
	public Display getMethodsToDisplay() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Display getFieldsToDisplay() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasUrl() {
		return false;
	}

	@Override
	public TextBlock getBody(ISkinParam skinParam, final boolean showMethods, final boolean showFields,
			Stereotype stereotype, Style style, FontConfiguration fontConfiguration) {
		return new TextBlockCucaJSon(fontConfiguration, skinParam, json, style.wrapWidth());
	}

	@Override
	public List<CharSequence> getRawBody() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addFieldOrMethod(String s) throws NoSuchColorException {
		throw new UnsupportedOperationException();
	}

	public void setJson(JsonValue json) {
		this.json = json;
	}

	@Override
	public CharSequence getBestMatch(CharSequence candidate) {
		throw new UnsupportedOperationException();
	}

}
