/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
 * Revision $Revision: 7755 $
 *
 */
package net.sourceforge.plantuml.cucadiagram.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.cucadiagram.Bodier;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.EntityPosition;
import net.sourceforge.plantuml.cucadiagram.EntityUtils;
import net.sourceforge.plantuml.cucadiagram.GroupRoot;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.LongCode;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.cucadiagram.dot.Neighborhood;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.graphic.color.Colors;
import net.sourceforge.plantuml.svek.IEntityImage;
import net.sourceforge.plantuml.svek.PackageStyle;
import net.sourceforge.plantuml.svek.SingleStrategy;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.utils.UniqueSequence;

final class EntityImpl implements ILeaf, IGroup {

	private final EntityFactory entityFactory;

	// Entity
	private final Code code;
	private final LongCode longCode;

	private Url url;

	private final Bodier bodier;
	private final String uid = StringUtils.getUid("cl", UniqueSequence.getValue());
	private Display display = Display.empty();

	private LeafType leafType;
	private Stereotype stereotype;
	private String generic;
	private IGroup parentContainer;

	private boolean top;

	// Group
	private Code namespace2;

	private GroupType groupType;

	private boolean autonom = true;

	// Other
	private boolean nearDecoration = false;
	private int xposition;
	private IEntityImage svekImage;

	private boolean removed = false;
	private USymbol symbol;
	private final int rawLayout;
	private char concurrentSeparator;

	// Back to Entity
	@Override
    public final boolean isTop() {
		checkNotGroup();
		return top;
	}

	@Override
    public final void setTop(boolean top) {
		checkNotGroup();
		this.top = top;
	}

	private EntityImpl(EntityFactory entityFactory, Code code, Bodier bodier, IGroup parentContainer,
			LongCode longCode, String namespaceSeparator, int rawLayout) {
		if (code == null) {
			throw new IllegalArgumentException();
		}
		this.entityFactory = entityFactory;
		this.bodier = bodier;
		this.code = code;
		this.parentContainer = parentContainer;
		this.longCode = longCode;
		this.rawLayout = rawLayout;
	}

	EntityImpl(EntityFactory entityFactory, Code code, Bodier bodier, IGroup parentContainer, LeafType leafType,
			LongCode longCode, String namespaceSeparator, int rawLayout) {
		this(entityFactory, code, bodier, parentContainer, longCode, namespaceSeparator, rawLayout);
		this.leafType = leafType;
	}

	EntityImpl(EntityFactory entityFactory, Code code, Bodier bodier, IGroup parentContainer, GroupType groupType,
			Code namespace2, LongCode longCode, String namespaceSeparator, int rawLayout) {
		this(entityFactory, code, bodier, parentContainer, longCode, namespaceSeparator, rawLayout);
		this.groupType = groupType;
		this.namespace2 = namespace2;
	}

	@Override
    public void setContainer(IGroup container) {
		checkNotGroup();
		if (container == null) {
			throw new IllegalArgumentException();
		}
		parentContainer = container;
	}

	@Override
    public LeafType getEntityType() {
		return leafType;
	}

	@Override
    public void muteToType(LeafType newType, USymbol newSymbol) {
		checkNotGroup();
		if (newType == null) {
			throw new IllegalArgumentException();
		}
		if (leafType != LeafType.STILL_UNKNOWN) {
			if (leafType != LeafType.ANNOTATION && leafType != LeafType.ABSTRACT_CLASS && leafType != LeafType.CLASS
					&& leafType != LeafType.ENUM && leafType != LeafType.INTERFACE) {
				throw new IllegalArgumentException("type=" + leafType);
			}
			if (newType != LeafType.ANNOTATION && newType != LeafType.ABSTRACT_CLASS && newType != LeafType.CLASS
					&& newType != LeafType.ENUM && newType != LeafType.INTERFACE && newType != LeafType.OBJECT) {
				throw new IllegalArgumentException("newtype=" + newType);
			}
		}
		if (leafType == LeafType.CLASS && newType == LeafType.OBJECT) {
			bodier.muteClassToObject();
		}
		leafType = newType;
		symbol = newSymbol;
	}

	@Override
    public Code getCode() {
		return code;
	}

	@Override
    public Display getDisplay() {
		return display;
	}

	@Override
    public void setDisplay(Display display) {
		this.display = display;
	}

	@Override
    public String getUid() {
		return uid;
	}

	@Override
    public Stereotype getStereotype() {
		return stereotype;
	}

	@Override
    public final void setStereotype(Stereotype stereotype) {
		this.stereotype = stereotype;
	}

	@Override
    public final IGroup getParentContainer() {
		if (parentContainer == null) {
			throw new IllegalArgumentException();
		}
		return parentContainer;
	}

	@Override
	public String toString() {
		return code + " " + display + "(" + leafType + ") " + xposition + " " + getUid();
	}

	@Override
    public final Url getUrl99() {
		return url;
	}

	@Override
    public boolean hasUrl() {
		if (Display.isNull(display) == false && display.hasUrl()) {
			return true;
		}
		if (bodier.hasUrl()) {
			return true;
		}
		return url != null;
	}

	@Override
    public final void addUrl(Url url) {
		this.url = url;
	}

	@Override
    public final boolean hasNearDecoration() {
		checkNotGroup();
		return nearDecoration;
	}

	@Override
    public final void setNearDecoration(boolean nearDecoration) {
		// checkNotGroup();
		this.nearDecoration = nearDecoration;
	}

	@Override
    public int getXposition() {
		checkNotGroup();
		return xposition;
	}

	@Override
    public void setXposition(int pos) {
		checkNotGroup();
		xposition = pos;
	}

	@Override
    public final IEntityImage getSvekImage() {
		checkNotGroup();
		return svekImage;
	}

	@Override
    public final void setSvekImage(IEntityImage svekImage) {
		checkNotGroup();
		this.svekImage = svekImage;
	}

	@Override
    public final void setGeneric(String generic) {
		checkNotGroup();
		this.generic = generic;
	}

	@Override
    public final String getGeneric() {
		checkNotGroup();
		return generic;
	}

	@Override
    public Bodier getBodier() {
		return bodier;
	}

	@Override
    public EntityPosition getEntityPosition() {
		checkNotGroup();
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
		final String label = stereotype.getLabel(false);
		if ("<<entrypoint>>".equalsIgnoreCase(label)) {
			return EntityPosition.ENTRY_POINT;
		}
		if ("<<exitpoint>>".equalsIgnoreCase(label)) {
			return EntityPosition.EXIT_POINT;
		}
		return EntityPosition.NORMAL;

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

	@Override
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
		for (final IGroup child : getChildren()) {
			if (child.containsLeafRecurse(leaf)) {
				return true;
			}
		}
		return false;
	}

	@Override
    public Collection<ILeaf> getLeafsDirect() {
		checkGroup();
		final List<ILeaf> result = new ArrayList<ILeaf>();
		for (final ILeaf ent : entityFactory.getLeafs().values()) {
			if (ent.isGroup()) {
				throw new IllegalStateException();
			}
			if (ent.getParentContainer() == this) {
				result.add(ent);
			}
		}
		return Collections.unmodifiableCollection(result);
	}

	@Override
    public Collection<IGroup> getChildren() {
		checkGroup();
		final Collection<IGroup> result = new ArrayList<IGroup>();
		for (final IGroup g : entityFactory.getGroups().values()) {
			if (g != this && g.getParentContainer() == this) {
				result.add(g);
			}
		}
		return Collections.unmodifiableCollection(result);
	}

	@Override
    public void moveEntitiesTo(IGroup dest) {
		checkGroup();
		if (dest.isGroup() == false) {
			throw new UnsupportedOperationException();
		}
		for (final ILeaf ent : getLeafsDirect()) {
			((EntityImpl) ent).parentContainer = dest;
		}
		for (final IGroup g : dest.getChildren()) {
			// ((EntityImpl) g).parentContainer = dest;
			throw new IllegalStateException();
		}

		for (final IGroup g : getChildren()) {
			if (g == dest) {
				continue;
			}
			((EntityImpl) g).parentContainer = dest;
		}

	}

	@Override
    public int size() {
		checkGroup();
		return getLeafsDirect().size();
	}

	@Override
    public GroupType getGroupType() {
		checkGroup();
		return groupType;
	}

	@Override
    public Code getNamespace2() {
		checkGroup();
		return namespace2;
	}

	@Override
    public boolean isAutonom() {
		checkGroup();
		return autonom;
	}

	@Override
    public void setAutonom(boolean autonom) {
		this.autonom = autonom;

	}

	@Override
    public PackageStyle getPackageStyle() {
		checkGroup();
		if (stereotype == null) {
			return null;
		}
		return stereotype.getPackageStyle();
	}

	@Override
    public boolean isGroup() {
		if (groupType != null && leafType != null) {
			throw new IllegalStateException();
		}
		if (groupType != null) {
			return true;
		}
		if (leafType != null) {
			return false;
		}
		throw new IllegalStateException();
	}

	// ---- other

	@Override
    public void overideImage(IEntityImage img, LeafType leafType) {
		checkGroup();
		svekImage = img;
		url = null;

		for (final Link link : new ArrayList<Link>(entityFactory.getLinks())) {
			if (EntityUtils.isPureInnerLink12(this, link)) {
				entityFactory.removeLink(link);
			}
		}

		entityFactory.removeGroup(this.getCode());
		for (final ILeaf ent : new ArrayList<ILeaf>(entityFactory.getLeafs().values())) {
			if (this != ent && this == ent.getParentContainer()) {
				entityFactory.removeLeaf(ent.getCode());
			}
		}

		entityFactory.addLeaf(this);
		groupType = null;
		this.leafType = leafType;
	}

	void muteToGroup(Code namespace2, GroupType groupType, IGroup parentContainer) {
		checkNotGroup();
		if (parentContainer.isGroup() == false) {
			throw new IllegalArgumentException();
		}
		this.namespace2 = namespace2;
		this.groupType = groupType;
		leafType = null;
		this.parentContainer = parentContainer;
	}

	@Override
    public boolean isHidden() {
		if (entityFactory.isHidden(leafType)) {
			return true;
		}
		if (stereotype != null) {
			return stereotype.isHidden();
		}
		return false;
	}

	@Override
    public USymbol getUSymbol() {
		if (symbol != null && stereotype != null && stereotype.getSprite() != null) {
			return symbol.withStereoAlignment(HorizontalAlignment.RIGHT);
		}
		return symbol;
	}

	@Override
    public void setUSymbol(USymbol symbol) {
		this.symbol = symbol;
	}

	@Override
    public SingleStrategy getSingleStrategy() {
		return SingleStrategy.SQUARRE;
	}

	@Override
    public boolean isRemoved() {
		if (isGroup()) {
			if (getLeafsDirect().size() == 0) {
				return false;
			}
			for (final ILeaf leaf : getLeafsDirect()) {
				if (leaf.isRemoved() == false) {
					return false;
				}
			}
			for (final IGroup g : getChildren()) {
				if (g.isRemoved() == false) {
					return false;
				}
			}
			return true;
		}
		return removed;
	}

	@Override
    public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	private int layer;

	@Override
    public int getHectorLayer() {
		return layer;
	}

	@Override
    public void setHectorLayer(int layer) {
		this.layer = layer;
		if (layer > 1000) {
			throw new IllegalArgumentException();
		}
	}

	@Override
    public LongCode getLongCode() {
		return longCode;
	}

	private FontParam getTitleFontParam() {
		if (symbol != null) {
			return symbol.getFontParam();
		}
		return getGroupType() == GroupType.STATE ? FontParam.STATE : FontParam.PACKAGE;
	}

	@Override
    public FontConfiguration getFontConfigurationForTitle(final ISkinParam skinParam) {
		final FontParam fontParam = getTitleFontParam();
		final HtmlColor fontHtmlColor = skinParam.getFontHtmlColor(getStereotype(), fontParam, FontParam.PACKAGE);
		final UFont font = skinParam.getFont(getStereotype(), true, fontParam, FontParam.PACKAGE);
		final FontConfiguration fontConfiguration = new FontConfiguration(font, fontHtmlColor,
				skinParam.getHyperlinkColor(), skinParam.useUnderlineForHyperlink(), skinParam.getTabSize());
		return fontConfiguration;
	}

	@Override
    public final int getRawLayout() {
		return rawLayout;
	}

	@Override
    public char getConcurrentSeparator() {
		return concurrentSeparator;
	}

	@Override
    public void setConcurrentSeparator(char separator) {
		concurrentSeparator = separator;
	}

	private Neighborhood neighborhood;

	@Override
    public void setNeighborhood(Neighborhood neighborhood) {
		this.neighborhood = neighborhood;
	}

	@Override
    public Neighborhood getNeighborhood() {
		return neighborhood;
	}

	private final Map<String, Display> tips = new LinkedHashMap<String, Display>();

	@Override
    public void putTip(String member, Display display) {
		tips.put(member, display);
	}

	@Override
    public Map<String, Display> getTips() {
		return Collections.unmodifiableMap(tips);
	}

	private Colors colors = Colors.empty();

	@Override
    public Colors getColors(ISkinParam skinParam) {
		return colors;
	}

	@Override
    public void setColors(Colors colors) {
		this.colors = colors;
	}

	@Override
    public void setSpecificColorTOBEREMOVED(ColorType type, HtmlColor color) {
		if (color != null) {
			colors = colors.add(type, color);
		}
	}

	// public void setSpecificLineStroke(UStroke specificLineStroke) {
	// colors = colors.addSpecificLineStroke(specificLineStroke);
	// }

	@Override
    @Deprecated
	public void applyStroke(String s) {
		throw new UnsupportedOperationException();
		// if (s == null) {
		// return;
		// }
		// final LinkStyle style = LinkStyle.valueOf(StringUtils.goUpperCase(s));
		// colors = colors.addSpecificLineStroke(style);
		// // setSpecificLineStroke(LinkStyle.getStroke(style));
	}

}
