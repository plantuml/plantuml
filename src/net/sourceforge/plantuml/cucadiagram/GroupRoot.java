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
package net.sourceforge.plantuml.cucadiagram;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineLocation;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.cucadiagram.entity.EntityFactory;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.svek.IEntityImage;
import net.sourceforge.plantuml.svek.PackageStyle;
import net.sourceforge.plantuml.svek.SingleStrategy;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public class GroupRoot implements IGroup {

	private final EntityFactory entityFactory;

	public GroupRoot(EntityFactory entityFactory) {
		this.entityFactory = entityFactory;
	}

	@Override
	public Collection<ILeaf> getLeafsDirect() {
		final List<ILeaf> result = new ArrayList<>();
		for (ILeaf ent : entityFactory.leafs()) {
			if (ent.getParentContainer() == this) {
				result.add(ent);
			}
		}
		return Collections.unmodifiableCollection(result);

	}

	@Override
	public String toString() {
		return "ROOT";
	}

	@Override
	public boolean isGroup() {
		return true;
	}

	@Override
	public Display getDisplay() {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setDisplay(Display display) {
		throw new UnsupportedOperationException();

	}

	@Override
	public LeafType getLeafType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getUid() {
		throw new UnsupportedOperationException();

	}

	@Override
	public Url getUrl99() {
		return null;

	}

	@Override
	public Stereotype getStereotype() {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setStereotype(Stereotype stereotype) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Code getCode() {
		return CodeImpl.of("__ROOT__");
	}

	@Override
	public String getCodeGetName() {
		return getCode().getName();
	}

	@Override
	public void addUrl(Url url) {
		throw new UnsupportedOperationException();

	}

	@Override
	public IGroup getParentContainer() {
		return null;
	}

	@Override
	public boolean containsLeafRecurse(ILeaf entity) {
		throw new UnsupportedOperationException();

	}

	@Override
	public Collection<IGroup> getChildren() {
		final List<IGroup> result = new ArrayList<>();
		if (entityFactory.namespaceSeparator.V1972()) {
			for (IGroup ent : entityFactory.groups()) {
				if (ent.getIdent().size() == 1) {
					result.add(ent);
				}
			}

		} else {
			for (IGroup ent : entityFactory.groups()) {
				if (ent.getParentContainer() == this) {
					result.add(ent);
				}
			}
		}
		return Collections.unmodifiableCollection(result);
	}

	@Override
	public void moveEntitiesTo(IGroup dest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		throw new UnsupportedOperationException();
	}

	@Override
	public GroupType getGroupType() {
		return null;
	}

	@Override
	public Code getNamespace() {
		throw new UnsupportedOperationException();

	}

	@Override
	public PackageStyle getPackageStyle() {
		throw new UnsupportedOperationException();

	}

	@Override
	public void overrideImage(IEntityImage img, LeafType state) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isHidden() {
		return false;
	}

	@Override
	public USymbol getUSymbol() {
		return null;
		// throw new UnsupportedOperationException();
	}

	@Override
	public void setUSymbol(USymbol symbol) {
		throw new UnsupportedOperationException();
	}

	@Override
	public SingleStrategy getSingleStrategy() {
		return SingleStrategy.SQUARE;
	}

	@Override
	public boolean isRemoved() {
		return false;
	}

	@Override
	public boolean hasUrl() {
		return false;
	}

	@Override
	public int getRawLayout() {
		throw new UnsupportedOperationException();
	}

	@Override
	public char getConcurrentSeparator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setConcurrentSeparator(char separator) {
		// throw new UnsupportedOperationException();
	}

	@Override
	public void putTip(String member, Display display) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Map<String, Display> getTips() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Bodier getBodier() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Colors getColors(ISkinParam skinParam) {
		return Colors.empty();
	}

	@Override
	public void setColors(Colors colors) {
		throw new UnsupportedOperationException();
	}

	public void setSpecificColorTOBEREMOVED(ColorType type, HColor color) {
		throw new UnsupportedOperationException();
	}

	public void setSpecificLineStroke(UStroke specificLineStroke) {
		throw new UnsupportedOperationException();
	}

	public FontConfiguration getFontConfigurationForTitle(ISkinParam skinParam) {
		throw new UnsupportedOperationException();
	}

	public void addStereotag(Stereotag tag) {
		throw new UnsupportedOperationException();
	}

	public Set<Stereotag> stereotags() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setLegend(DisplayPositioned legend) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DisplayPositioned getLegend() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Ident getIdent() {
		return Ident.empty();
	}

	@Override
	public boolean isAloneAndUnlinked() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setThisIsTogether() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getCodeLine() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCodeLine(LineLocation codeLine) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setStereostyle(String stereo) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Stereostyles getStereostyles() {
		throw new UnsupportedOperationException();
	}
}
