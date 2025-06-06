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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.Style;

public class BodierSimple implements Bodier {

	private final List<CharSequence> rawBody = new ArrayList<>();
	private final ISkinParam skinParam;
	private Entity leaf;

	@Override
	public void muteClassToObject() {
		throw new UnsupportedOperationException();
	}

	BodierSimple(ISkinParam skinParam) {
		this.skinParam = skinParam;
	}

	@Override
	public void setLeaf(Entity leaf) {
		this.leaf = Objects.requireNonNull(leaf);
	}

	@Override
	public boolean addFieldOrMethod(String s) throws NoSuchColorException {
		final Display display = Display
				.getWithNewlines2(skinParam.getPragma(), s);
		rawBody.addAll(display.asList());
		return true;
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
	public List<CharSequence> getRawBody() {
		return Collections.unmodifiableList(rawBody);
	}

	@Override
	public TextBlock getBody(ISkinParam skinParam, boolean showMethods, boolean showFields, Stereotype stereotype,
			Style style, FontConfiguration fontConfiguration) {
		return BodyFactory.create1(skinParam.getDefaultTextAlignment(HorizontalAlignment.LEFT), rawBody, skinParam,
				stereotype, leaf, style);
	}

}
