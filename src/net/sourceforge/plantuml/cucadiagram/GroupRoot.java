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

	public Collection<ILeaf> getLeafsDirect() {
		final List<ILeaf> result = new ArrayList<ILeaf>();
		for (ILeaf ent : entityFactory.leafs()) {
			if (ent.getParentContainer() == this) {
				result.add(ent);
			}
		}
		return Collections.unmodifiableCollection(result);

	}

	public boolean isGroup() {
		return true;
	}

	public Display getDisplay() {
		throw new UnsupportedOperationException();

	}

	public void setDisplay(Display display) {
		throw new UnsupportedOperationException();

	}

	public LeafType getLeafType() {
		throw new UnsupportedOperationException();
	}

	public String getUid() {
		throw new UnsupportedOperationException();

	}

	public Url getUrl99() {
		return null;

	}

	public Stereotype getStereotype() {
		throw new UnsupportedOperationException();

	}

	public void setStereotype(Stereotype stereotype) {
		throw new UnsupportedOperationException();

	}

	public TextBlock getBody(PortionShower portionShower, FontParam fontParam, ISkinParam skinParam) {
		throw new UnsupportedOperationException();

	}

	public Code getCode() {
		return CodeImpl.of("__ROOT__");
	}

	public String getCodeGetName() {
		return getCode().getName();
	}

	public void addUrl(Url url) {
		throw new UnsupportedOperationException();

	}

	public IGroup getParentContainer() {
		return null;
	}

	public boolean containsLeafRecurse(ILeaf entity) {
		throw new UnsupportedOperationException();

	}

	public Collection<IGroup> getChildren() {
		final List<IGroup> result = new ArrayList<IGroup>();
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

	public void moveEntitiesTo(IGroup dest) {
		throw new UnsupportedOperationException();
	}

	public int size() {
		throw new UnsupportedOperationException();
	}

	public GroupType getGroupType() {
		return null;
	}

	public Code getNamespace() {
		throw new UnsupportedOperationException();

	}

	public PackageStyle getPackageStyle() {
		throw new UnsupportedOperationException();

	}

	public void overrideImage(IEntityImage img, LeafType state) {
		throw new UnsupportedOperationException();
	}

	public boolean isHidden() {
		return false;
	}

	public USymbol getUSymbol() {
		return null;
		// throw new UnsupportedOperationException();
	}

	public void setUSymbol(USymbol symbol) {
		throw new UnsupportedOperationException();
	}

	public SingleStrategy getSingleStrategy() {
		return SingleStrategy.SQUARRE;
	}

	public boolean isRemoved() {
		return false;
	}

	public boolean hasUrl() {
		return false;
	}

	public int getHectorLayer() {
		throw new UnsupportedOperationException();
	}

	public void setHectorLayer(int layer) {
		throw new UnsupportedOperationException();
	}

	public int getRawLayout() {
		throw new UnsupportedOperationException();
	}

	public char getConcurrentSeparator() {
		throw new UnsupportedOperationException();
	}

	public void setConcurrentSeparator(char separator) {
		// throw new UnsupportedOperationException();
	}

	public void putTip(String member, Display display) {
		throw new UnsupportedOperationException();
	}

	public Map<String, Display> getTips() {
		throw new UnsupportedOperationException();
	}

	public Bodier getBodier() {
		throw new UnsupportedOperationException();
	}

	public Colors getColors(ISkinParam skinParam) {
		return Colors.empty();
	}

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

	public void setLegend(DisplayPositionned legend) {
		throw new UnsupportedOperationException();
	}

	public DisplayPositionned getLegend() {
		throw new UnsupportedOperationException();
	}

	public Ident getIdent() {
		return Ident.empty();
	}

	public boolean isAloneAndUnlinked() {
		throw new UnsupportedOperationException();
	}

	public void setThisIsTogether() {
		throw new UnsupportedOperationException();
	}
}
