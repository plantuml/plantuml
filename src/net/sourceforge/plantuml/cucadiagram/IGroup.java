/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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

import java.util.Collection;

import net.sourceforge.plantuml.svek.IEntityImage;
import net.sourceforge.plantuml.svek.PackageStyle;
import net.sourceforge.plantuml.svek.SingleStrategy;

public interface IGroup extends IEntity {

	public boolean containsLeafRecurse(ILeaf entity);

	public Collection<ILeaf> getLeafsDirect();

	public Collection<IGroup> getChildren();

	public void moveEntitiesTo(IGroup dest);

	public int size();

	public GroupType getGroupType();

	public String getNamespace();

	public boolean isAutonom();

	public void setAutonom(boolean autonom);

	public Rankdir getRankdir();

	public void setRankdir(Rankdir rankdir);

	public PackageStyle getPackageStyle();

	public void overideImage(IEntityImage img, LeafType state);

	public SingleStrategy getSingleStrategy();
}
