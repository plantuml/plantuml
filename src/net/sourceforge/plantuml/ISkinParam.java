/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 *
 * Revision $Revision: 4236 $
 *
 */
package net.sourceforge.plantuml;

import java.awt.Font;

import net.sourceforge.plantuml.graphic.HtmlColor;

public interface ISkinParam {

	public HtmlColor getBackgroundColor();

	public String getValue(String key);

	public HtmlColor getHtmlColor(ColorParam param, String stereotype);

	public int getFontSize(FontParam param, String stereotype);

	public String getFontFamily(FontParam param, String stereotype);

	public HtmlColor getFontHtmlColor(FontParam param, String stereotype);

	public int getFontStyle(FontParam param, String stereotype);

	public Font getFont(FontParam fontParam, String stereotype);

	public int getCircledCharacterRadius();

	public boolean isClassCollapse();
	
	public int classAttributeIconSize();
	
	public boolean isMonochrome();
	
	public int getDpi();

}