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
package net.sourceforge.plantuml.salt;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.plantuml.Guillemet;
import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.LineBreakStrategy;
import net.sourceforge.plantuml.SpriteContainer;
import net.sourceforge.plantuml.creole.command.CommandCreoleMonospaced;
import net.sourceforge.plantuml.salt.element.Element;
import net.sourceforge.plantuml.salt.element.WrappedElement;
import net.sourceforge.plantuml.sprite.Sprite;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.ugraphic.color.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.color.HColorSet;

public class Dictionary implements SpriteContainer, ISkinSimple {

	private final Map<String, Element> data = new HashMap<String, Element>();

	public void put(String name, Element element) {
		data.put(name, element);
	}

	public Element get(String name) {
		final Element result = data.get(name);
		if (result == null) {
			throw new IllegalArgumentException();
		}
		return new WrappedElement(result);
	}

	public Sprite getSprite(String name) {
		return sprites.get(name);
	}

	public String getValue(String key) {
		return null;
	}

	public double getPadding() {
		return 0;
	}

	public Guillemet guillemet() {
		return Guillemet.GUILLEMET;
	}

	public String getMonospacedFamily() {
		return CommandCreoleMonospaced.MONOSPACED;
	}

	public int getTabSize() {
		return 8;
	}

	public HColorSet getIHtmlColorSet() {
		return HColorSet.instance();
	}

	public int getDpi() {
		return 96;
	}

	private final Map<String, Sprite> sprites = new HashMap<String, Sprite>();

	public void addSprite(String name, Sprite sprite) {
		sprites.put(name, sprite);

	}

	public LineBreakStrategy wrapWidth() {
		return LineBreakStrategy.NONE;
	}

	public ColorMapper getColorMapper() {
		return new ColorMapperIdentity();
	}

	public void copyAllFrom(ISkinSimple other) {
		throw new UnsupportedOperationException();
	}

	public Map<String, String> values() {
		throw new UnsupportedOperationException();
	}

}
