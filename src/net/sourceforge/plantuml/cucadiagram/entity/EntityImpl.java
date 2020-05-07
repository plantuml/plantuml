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
 * Contribution :  Hisashi Miyashita  * 
 *
 */
package net.sourceforge.plantuml.cucadiagram.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.Guillemet;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.cucadiagram.Bodier;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.DisplayPositionned;
import net.sourceforge.plantuml.cucadiagram.EntityPosition;
import net.sourceforge.plantuml.cucadiagram.EntityUtils;
import net.sourceforge.plantuml.cucadiagram.GroupRoot;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.Ident;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.Stereotag;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.cucadiagram.dot.Neighborhood;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.skin.VisibilityModifier;
import net.sourceforge.plantuml.svek.IEntityImage;
import net.sourceforge.plantuml.svek.PackageStyle;
import net.sourceforge.plantuml.svek.SingleStrategy;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.color.HColor;
import net.sourceforge.plantuml.utils.UniqueSequence;

final public class EntityImpl implements ILeaf, IGroup {

	private final EntityFactory entityFactory;

	// Entity
	private/* final */Code code;
	private/* final */Ident ident;

	private Url url;

	private final Bodier bodier;
	private final String uid = StringUtils.getUid("cl", UniqueSequence.getValue());
	private Display display = Display.empty();
	private DisplayPositionned legend = null;

	private LeafType leafType;
	private Stereotype stereotype;
	private String generic;
	private IGroup parentContainer;

	private boolean top;

	// Group
	private Code namespace;

	private GroupType groupType;

	// Other
	private boolean nearDecoration = false;
	private final Collection<String> portShortNames = new HashSet<String>();
	private int xposition;
	private IEntityImage svekImage;

	private USymbol symbol;
	private final int rawLayout;
	private char concurrentSeparator;

	private Set<Stereotag> tags = new LinkedHashSet<Stereotag>();

	public void addStereotag(Stereotag tag) {
		this.tags.add(tag);
	}

	public Set<Stereotag> stereotags() {
		return Collections.unmodifiableSet(tags);
	}

	// Back to Entity
	public final boolean isTop() {
		checkNotGroup();
		return top;
	}

	public final void setTop(boolean top) {
		checkNotGroup();
		this.top = top;
	}

	private EntityImpl(Ident ident, EntityFactory entityFactory, Code code, Bodier bodier, IGroup parentContainer,
			String namespaceSeparator, int rawLayout) {
		checkNotNull(ident);
		if (entityFactory.namespaceSeparator.V1972()) {
			code = ident;
		}
		if (code == null) {
			throw new IllegalArgumentException();
		}
		this.ident = ident;
		this.entityFactory = entityFactory;
		this.bodier = bodier;
		this.code = code;
		this.parentContainer = parentContainer;
		this.rawLayout = rawLayout;
	}

	EntityImpl(Ident ident, Code code, EntityFactory entityFactory, Bodier bodier, IGroup parentContainer,
			LeafType leafType, String namespaceSeparator, int rawLayout) {
		this(ident, entityFactory, code, bodier, parentContainer, namespaceSeparator, rawLayout);
		checkNotNull(ident);
		// System.err.println("ID for leaf=" + code + " " + ident);
		// ident.checkSameAs(code, namespaceSeparator);
		this.leafType = leafType;
	}

	EntityImpl(Ident ident, Code code, EntityFactory entityFactory, Bodier bodier, IGroup parentContainer,
			GroupType groupType, Code namespace, String namespaceSeparator, int rawLayout) {
		this(ident, entityFactory, code, bodier, parentContainer, namespaceSeparator, rawLayout);
		checkNotNull(ident);
		// System.err.println("ID for group=" + code + " " + ident);
		ident.checkSameAs(code, namespaceSeparator, entityFactory.namespaceSeparator);
		this.groupType = groupType;
		this.namespace = namespace;
	}

	private void checkNotNull(Ident id) {
		if (id == null) {
			throw new IllegalArgumentException();
		}
	}

	public void setContainer(IGroup container) {
		checkNotGroup();
		if (container == null) {
			throw new IllegalArgumentException();
		}
		this.parentContainer = container;
	}

	public LeafType getLeafType() {
		return leafType;
	}

	public boolean muteToType(LeafType newType, USymbol newSymbol) {
		checkNotGroup();
		if (newType == null) {
			throw new IllegalArgumentException();
		}
		if (leafType != LeafType.STILL_UNKNOWN) {
			if (newType == this.leafType) {
				return true;
			}
			if (leafType != LeafType.ANNOTATION && leafType != LeafType.ABSTRACT_CLASS && leafType != LeafType.CLASS
					&& leafType != LeafType.ENUM && leafType != LeafType.INTERFACE) {
				return false;
				// throw new IllegalArgumentException("type=" + leafType);
			}
			if (newType != LeafType.ANNOTATION && newType != LeafType.ABSTRACT_CLASS && newType != LeafType.CLASS
					&& newType != LeafType.ENUM && newType != LeafType.INTERFACE && newType != LeafType.OBJECT) {
				return false;
				// throw new IllegalArgumentException("newtype=" + newType);
			}
		}
		if (leafType == LeafType.CLASS && newType == LeafType.OBJECT) {
			bodier.muteClassToObject();
		}
		this.leafType = newType;
		this.symbol = newSymbol;
		return true;
	}

	public Code getCode() {
		return code;
	}

	public String getCodeGetName() {
		return getCode().getName();
	}

	public Ident getIdent() {
		return ident;
	}

	public Display getDisplay() {
		if (intricated) {
			return entityFactory.getIntricatedDisplay(ident);
		}
		return display;
	}

	public void setDisplay(Display display) {
		this.display = display;
	}

	public String getUid() {
		return uid;
	}

	public Stereotype getStereotype() {
		return stereotype;
	}

	public final void setStereotype(Stereotype stereotype) {
		this.stereotype = stereotype;
	}

	public final IGroup getParentContainer() {
		return entityFactory.getParentContainer(ident, parentContainer);
		// if (parentContainer == null) {
		// throw new IllegalArgumentException();
		// }
		// return parentContainer;
	}

	@Override
	public String toString() {
		// return super.toString() + code + " " + display + "(" + leafType + ")[" +
		// groupType + "] " + xposition + " "
		// + getUid();
		if (entityFactory.namespaceSeparator.V1972())
			return getUid() + " " + ident + " " + display + "(" + leafType + ")[" + groupType + "]";
		return super.toString() + code + ident + " " + display + "(" + leafType + ")[" + groupType + "] " + getUid();
	}

	public final Url getUrl99() {
		return url;
	}

	public boolean hasUrl() {
		if (Display.isNull(display) == false && display.hasUrl()) {
			return true;
		}
		if (bodier.hasUrl()) {
			return true;
		}
		return url != null;
	}

	public final void addUrl(Url url) {
		this.url = url;
	}

	public final boolean hasNearDecoration() {
		checkNotGroup();
		return nearDecoration;
	}

	public final void setNearDecoration(boolean nearDecoration) {
		// checkNotGroup();
		this.nearDecoration = nearDecoration;
	}

	public int getXposition() {
		checkNotGroup();
		return xposition;
	}

	public void setXposition(int pos) {
		checkNotGroup();
		xposition = pos;
	}

	public final IEntityImage getSvekImage() {
		checkNotGroup();
		return svekImage;
	}

	public final void setSvekImage(IEntityImage svekImage) {
		checkNotGroup();
		this.svekImage = svekImage;
	}

	public final void setGeneric(String generic) {
		checkNotGroup();
		this.generic = generic;
	}

	public final String getGeneric() {
		checkNotGroup();
		return generic;
	}

	public Bodier getBodier() {
		return bodier;
	}

	public EntityPosition getEntityPosition() {
		checkNotGroup();
		if (leafType == LeafType.PORT) {
			return EntityPosition.PORT;
		}
		if (leafType == LeafType.PORTIN) {
			return EntityPosition.PORTIN;
		}
		if (leafType == LeafType.PORTOUT) {
			return EntityPosition.PORTOUT;
		}
		if (leafType != LeafType.STATE) {
			return EntityPosition.NORMAL;
		}
		if (getParentContainer() instanceof GroupRoot) {
			return EntityPosition.NORMAL;
		}
		final Stereotype stereotype = getStereotype();
		if (stereotype == null) {
			return EntityPosition.NORMAL;
		}
		return EntityPosition.fromStereotype(stereotype.getLabel(Guillemet.DOUBLE_COMPARATOR));

	}

	// ----------

	private void checkGroup() {
		if (isGroup() == false) {
			throw new UnsupportedOperationException();
		}
	}

	private void checkNotGroup() {
		if (isGroup()) {
			throw new UnsupportedOperationException();
		}
	}

	public boolean containsLeafRecurse(ILeaf leaf) {
		if (leaf == null) {
			throw new IllegalArgumentException();
		}
		if (leaf.isGroup()) {
			throw new IllegalArgumentException();
		}
		checkGroup();
		if (leaf.getParentContainer() == this) {
			return true;
		}
		for (IGroup child : getChildren()) {
			if (child.containsLeafRecurse(leaf)) {
				return true;
			}
		}
		return false;
	}

	public Collection<ILeaf> getLeafsDirect() {
		checkGroup();
		final List<ILeaf> result = new ArrayList<ILeaf>();
		for (ILeaf ent : entityFactory.leafs()) {
			if (ent.isGroup()) {
				throw new IllegalStateException();
			}
			if (ent.getParentContainer() == this) {
				result.add(ent);
			}
		}
		return Collections.unmodifiableCollection(result);
	}

	public Collection<IGroup> getChildren() {
		checkGroup();
		final Collection<IGroup> result = new ArrayList<IGroup>();
		for (IGroup g : entityFactory.groups()) {
			if (g != this && g.getParentContainer() == this) {
				result.add(g);
			}
		}
		return Collections.unmodifiableCollection(result);
	}

	public void moveEntitiesTo(IGroup dest) {
		if (entityFactory.namespaceSeparator.V1972()) {
			moveEntitiesTo1972(dest);
			return;
		}
		checkGroup();
		if (dest.isGroup() == false) {
			throw new UnsupportedOperationException();
		}
		for (ILeaf ent : getLeafsDirect()) {
			((EntityImpl) ent).parentContainer = dest;
		}
		for (IGroup g : dest.getChildren()) {
			// ((EntityImpl) g).parentContainer = dest;
			throw new IllegalStateException();
		}

		for (IGroup g : getChildren()) {
			if (g == dest) {
				continue;
			}
			((EntityImpl) g).parentContainer = dest;
		}

	}

	private void moveEntitiesTo1972(IGroup dest) {
		checkGroup();
		if (dest.isGroup() == false) {
			throw new UnsupportedOperationException();
		}
		// System.err.println("moveEntitiesTo1972::before1::groups2=" +
		// entityFactory.groups2());
		final Ident firstIdent = getIdent();
		final Ident destIdent = dest.getIdent();
		// System.err.println("moveEntitiesTo1972::this=" + firstIdent);
		// System.err.println("moveEntitiesTo1972::dest=" + destIdent);
		if (destIdent.startsWith(firstIdent) == false) {
			throw new UnsupportedOperationException();
		}
		// System.err.println("moveEntitiesTo1972::before2::groups2=" +
		// entityFactory.groups2());
		for (ILeaf ent : new ArrayList<ILeaf>(entityFactory.leafs2())) {
			Ident ident = ent.getIdent();
			if (ident.equals(firstIdent) == false && ident.startsWith(firstIdent)
					&& ident.startsWith(destIdent) == false) {
				// System.err.print("moving leaf ident1=" + ident);
				entityFactory.leafs2.remove(ident);
				ident = ident.move(firstIdent, destIdent);
				// System.err.println(" to ident2=" + ident);
				((EntityImpl) ent).ident = ident;
				((EntityImpl) ent).code = ident;
				entityFactory.leafs2.put(ident, ent);
			}
		}
		// System.err.println("moveEntitiesTo1972::before3::groups2=" +
		// entityFactory.groups2());
		for (IGroup ent : new ArrayList<IGroup>(entityFactory.groups2())) {
			Ident ident = ent.getIdent();
			// System.err.println("found=" + ident + " " + ident.startsWith(firstIdent) + "
			// "
			// + ident.startsWith(destIdent));
			if (ident.equals(firstIdent) == false && ident.startsWith(firstIdent)
					&& ident.startsWith(destIdent) == false) {
				// System.err.print("moving gr ident1=" + ident);
				entityFactory.groups2.remove(ident);
				ident = ident.move(firstIdent, destIdent);
				// System.err.println(" to ident2=" + ident);
				((EntityImpl) ent).ident = ident;
				((EntityImpl) ent).code = ident;
				entityFactory.groups2.put(ident, ent);
				// System.err.println("-->groups2=" + entityFactory.groups2());
			}
		}
		// System.err.println("moveEntitiesTo1972::after::groups2=" +
		// entityFactory.groups2());
		// for (IGroup g : dest.getChildren()) {
		// // ((EntityImpl) g).parentContainer = dest;
		// throw new IllegalStateException();
		// }
		//
		// for (IGroup g : getChildren()) {
		// if (g == dest) {
		// continue;
		// }
		// ((EntityImpl) g).parentContainer = dest;
		// }

	}

	public int size() {
		checkGroup();
		return getLeafsDirect().size();
	}

	public GroupType getGroupType() {
		checkGroup();
		return groupType;
	}

	public Code getNamespace() {
		checkGroup();
		return namespace;
	}

	public PackageStyle getPackageStyle() {
		checkGroup();
		if (stereotype == null) {
			return null;
		}
		return stereotype.getPackageStyle();
	}

	public boolean isGroup() {
		if (groupType != null && leafType != null) {
			throw new IllegalStateException();
		}
		assert groupType == null || leafType == null;
		if (groupType != null) {
			return true;
		}
		if (leafType != null) {
			return false;
		}
		throw new IllegalStateException();
	}

	// ---- other

	public void overrideImage(IEntityImage img, LeafType leafType) {
		checkGroup();
		this.svekImage = img;
		this.url = null;

		for (final Link link : new ArrayList<Link>(entityFactory.getLinks())) {
			if (EntityUtils.isPureInnerLink12(this, link)) {
				entityFactory.removeLink(link);
			}
		}

		if (entityFactory.namespaceSeparator.V1972()) {
			entityFactory.removeGroup(getIdent());
			for (ILeaf ent : new ArrayList<ILeaf>(entityFactory.leafs())) {
				if (this != ent && getIdent().equals(ent.getIdent().parent())) {
					entityFactory.removeLeaf(ent.getIdent());
				}
			}
		} else {
			entityFactory.removeGroup(getCodeGetName());
			for (ILeaf ent : new ArrayList<ILeaf>(entityFactory.leafs())) {
				if (this != ent && this == ent.getParentContainer()) {
					entityFactory.removeLeaf(ent.getCodeGetName());
				}
			}
		}

		entityFactory.addLeaf(this);
		this.groupType = null;
		this.leafType = leafType;
	}

	void muteToGroup(Code namespaceNew, GroupType groupType, IGroup parentContainer) {
		checkNotGroup();
		if (parentContainer.isGroup() == false) {
			throw new IllegalArgumentException();
		}
		this.namespace = namespaceNew;
		this.groupType = groupType;
		this.leafType = null;
		this.parentContainer = parentContainer;
	}

	public USymbol getUSymbol() {
		if (getLeafType() == LeafType.CIRCLE) {
			return USymbol.INTERFACE;
		}
		// if (symbol != null && stereotype != null && stereotype.getSprite() != null) {
		// return symbol.withStereoAlignment(HorizontalAlignment.RIGHT);
		// }
		return symbol;
	}

	public void setUSymbol(USymbol symbol) {
		this.symbol = symbol;
	}

	public SingleStrategy getSingleStrategy() {
		return SingleStrategy.SQUARRE;
	}

	public boolean isHidden() {
		if (parentContainer != null && parentContainer.isHidden()) {
			return true;
		}
		return isHiddenInternal();
	}

	private boolean isHiddenInternal() {
		if (isGroup()) {
			if (entityFactory.isHidden(this)) {
				return true;
			}
			if (getLeafsDirect().size() == 0) {
				return false;
			}
			for (ILeaf leaf : getLeafsDirect()) {
				if (((EntityImpl) leaf).isHiddenInternal() == false) {
					return false;
				}
			}
			for (IGroup g : getChildren()) {
				if (((EntityImpl) g).isHiddenInternal() == false) {
					return false;
				}
			}
			return true;
		}
		return entityFactory.isHidden(this);
	}

	public boolean isRemoved() {
		if (parentContainer != null && parentContainer.isRemoved()) {
			return true;
		}
		return isRemovedInternal();
	}

	private boolean isRemovedInternal() {
		if (isGroup()) {
			if (entityFactory.isRemoved(this)) {
				return true;
			}
			if (getLeafsDirect().size() == 0) {
				return false;
			}
			for (ILeaf leaf : getLeafsDirect()) {
				if (((EntityImpl) leaf).isRemovedInternal() == false) {
					return false;
				}
			}
			for (IGroup g : getChildren()) {
				if (((EntityImpl) g).isRemovedInternal() == false) {
					return false;
				}
			}
			return true;
		}
		return entityFactory.isRemoved(this);
	}

	public boolean isAloneAndUnlinked() {
		if (isGroup()) {
			return false;
		}
		for (Link link : entityFactory.getLinks()) {
			if (link.contains(this) && link.getType().isInvisible() == false) {
				return false;
			}
		}
		return true;
	}

	private int layer;

	public int getHectorLayer() {
		return layer;
	}

	public void setHectorLayer(int layer) {
		this.layer = layer;
		if (layer > 1000) {
			throw new IllegalArgumentException();
		}
	}

	private FontParam getTitleFontParam() {
		if (symbol != null) {
			return symbol.getFontParam();
		}
		return getGroupType() == GroupType.STATE ? FontParam.STATE : FontParam.PACKAGE;
	}

	public FontConfiguration getFontConfigurationForTitle(final ISkinParam skinParam) {
		final FontParam fontParam = getTitleFontParam();
		final HColor fontHtmlColor = skinParam.getFontHtmlColor(getStereotype(), fontParam, FontParam.PACKAGE);
		final UFont font = skinParam.getFont(getStereotype(), true, fontParam, FontParam.PACKAGE);
		final FontConfiguration fontConfiguration = new FontConfiguration(font, fontHtmlColor,
				skinParam.getHyperlinkColor(), skinParam.useUnderlineForHyperlink(), skinParam.getTabSize());
		return fontConfiguration;
	}

	public final int getRawLayout() {
		return rawLayout;
	}

	public char getConcurrentSeparator() {
		return concurrentSeparator;
	}

	public void setConcurrentSeparator(char separator) {
		this.concurrentSeparator = separator;
	}

	private Neighborhood neighborhood;

	public void setNeighborhood(Neighborhood neighborhood) {
		this.neighborhood = neighborhood;
	}

	public Neighborhood getNeighborhood() {
		return neighborhood;
	}

	private final Map<String, Display> tips = new LinkedHashMap<String, Display>();

	public void putTip(String member, Display display) {
		tips.put(member, display);
	}

	public Map<String, Display> getTips() {
		return Collections.unmodifiableMap(tips);
	}

	private Colors colors = Colors.empty();

	public Colors getColors(ISkinParam skinParam) {
		return colors;
	}

	public void setColors(Colors colors) {
		this.colors = colors;
	}

	public void setSpecificColorTOBEREMOVED(ColorType type, HColor color) {
		if (color != null) {
			this.colors = colors.add(type, color);
		}
	}

	public Collection<String> getPortShortNames() {
		checkNotGroup();
		// return Collections.unmodifiableCollection(portShortNames);
		return portShortNames;
	}

	public void addPortShortName(String portShortName) {
		portShortNames.add(portShortName);
	}

	private VisibilityModifier visibility;

	public void setVisibilityModifier(VisibilityModifier visibility) {
		this.visibility = visibility;

	}

	public VisibilityModifier getVisibilityModifier() {
		return visibility;
	}

	public void setLegend(DisplayPositionned legend) {
		checkGroup();
		this.legend = legend;
	}

	public DisplayPositionned getLegend() {
		checkGroup();
		return legend;
	}

	private boolean intricated;

	public void setIntricated(boolean intricated) {
		this.intricated = intricated;

	}

	private IGroup originalGroup;

	public void setOriginalGroup(IGroup originalGroup) {
		this.originalGroup = originalGroup;
	}

	public IGroup getOriginalGroup() {
		return originalGroup;
	}
	
	private boolean together;

	public void setThisIsTogether() {
		this.together = true;
		// System.err.println("setThisIsTogether");
	}

}
