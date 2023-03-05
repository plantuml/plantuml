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
package net.sourceforge.plantuml.salt;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.creole.Parser;
import net.sourceforge.plantuml.klimt.creole.SheetBuilder;
import net.sourceforge.plantuml.klimt.creole.legacy.CreoleParser;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.sprite.Sprite;
import net.sourceforge.plantuml.klimt.sprite.SpriteContainer;
import net.sourceforge.plantuml.salt.element.Element;
import net.sourceforge.plantuml.salt.element.WrappedElement;
import net.sourceforge.plantuml.style.ISkinSimple;
import net.sourceforge.plantuml.style.Style;
import net.sourceforge.plantuml.text.Guillemet;

public class SaltDictionary implements SpriteContainer, ISkinSimple {

	private final Map<String, Element> data = new HashMap<String, Element>();

	public void put(String name, Element element) {
		data.put(name, element);
	}

	public Element get(String name) {
		final Element result = Objects.requireNonNull(data.get(Objects.requireNonNull(name)));
		return new WrappedElement(result);
	}

	@Override
	public Sprite getSprite(String name) {
		return sprites.get(name);
	}

	@Override
	public String getValue(String key) {
		return null;
	}

	@Override
	public double getPadding() {
		return 0;
	}

	@Override
	public Guillemet guillemet() {
		return Guillemet.GUILLEMET;
	}

	@Override
	public String getMonospacedFamily() {
		return Parser.MONOSPACED;
	}

	@Override
	public int getTabSize() {
		return 8;
	}

	@Override
	public HColorSet getIHtmlColorSet() {
		return HColorSet.instance();
	}

	@Override
	public int getDpi() {
		return 96;
	}

	private final Map<String, Sprite> sprites = new HashMap<String, Sprite>();

	public void addSprite(String name, Sprite sprite) {
		sprites.put(name, sprite);

	}

	@Override
	public void copyAllFrom(Map<String, String> other) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Map<String, String> values() {
		throw new UnsupportedOperationException();
	}

	public double minClassWidthTOBEREMOVED(Style style) {
		return 0;
	}

	@Override
	public String transformStringForSizeHack(String s) {
		return s;
	}

	@Override
	public SheetBuilder sheet(FontConfiguration fontConfiguration, HorizontalAlignment horizontalAlignment,
			CreoleMode creoleMode) {
		throw new UnsupportedOperationException();
	}

	@Override
	public SheetBuilder sheet(FontConfiguration fontConfiguration, HorizontalAlignment horizontalAlignment,
			CreoleMode creoleMode, FontConfiguration stereo) {
		return new CreoleParser(fontConfiguration, horizontalAlignment, this, creoleMode, stereo);
	}

}
