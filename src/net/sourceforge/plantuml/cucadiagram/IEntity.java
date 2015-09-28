/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
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
 * Revision $Revision: 8218 $
 *
 */
package net.sourceforge.plantuml.cucadiagram;

import java.util.Map;

import net.sourceforge.plantuml.Hideable;
import net.sourceforge.plantuml.LineConfigurable;
import net.sourceforge.plantuml.Removeable;
import net.sourceforge.plantuml.SpecificBackcolorable;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.graphic.color.Colors;

public interface IEntity extends SpecificBackcolorable, Hideable, Removeable, LineConfigurable {

	public Code getCode();

	public LongCode getLongCode();

	public USymbol getUSymbol();

	public void setUSymbol(USymbol symbol);

	public LeafType getEntityType();

	public Display getDisplay();

	public IGroup getParentContainer();

	public void setDisplay(Display display);

	public String getUid();

	public Url getUrl99();

	public Stereotype getStereotype();

	public void setStereotype(Stereotype stereotype);

	public Bodier getBodier();

	public void addUrl(Url url);

	public boolean isGroup();

	public boolean hasUrl();

	public int getHectorLayer();

	public void setHectorLayer(int layer);

	public int getRawLayout();

	public void putTip(String member, Display display);

	public Map<String, Display> getTips();

	@Deprecated
	public void applyStroke(String s);

}
